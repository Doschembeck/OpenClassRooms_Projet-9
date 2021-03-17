package com.openclassrooms.realestatemanager.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Parameter;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.ui.fragments.ListView.ListViewFragment;
import com.openclassrooms.realestatemanager.ui.fragments.MapsFragment;
import com.openclassrooms.realestatemanager.utils.ComPermissions;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.LocationUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private PropertyViewModel mViewModel;
    private ActivityMainBinding binding;
    private SharedPreferences mSharedPreferences;
    private Activity mActivity;

    private int currentFragment = 1;
    private final int ID_FRAGMENT_LIST = 1;
    private final int ID_FRAGMENT_MAP = 2;
    private Fragment fragmentList;
    private Fragment fragmentMaps;

    private TextView mTextviewAgentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mActivity = this;

        mSharedPreferences = Utils.getSharedPreferences(this);

        ComPermissions.checkPermissionLocation(this);

        // --- LISTENERS ---
        binding.activityMainToolbar.setNavigationOnClickListener(view -> {
            if (binding.activityMainDrawerLayout.isDrawerOpen(GravityCompat.START)){
                binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);
            }else {
                binding.activityMainDrawerLayout.openDrawer(GravityCompat.START);
                updateUI();
            }
        });
        binding.activityMainToolbar.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()){

                case R.id.menu_toolbar_item_switchview:
                    switch (currentFragment){
                        case ID_FRAGMENT_LIST:
                            showMapFragment();
                        break;
                        case ID_FRAGMENT_MAP:
                            showListFragment();
                            break;
                        default:
                            break;
                    }
                    break;

                case R.id.menu_toolbar_item_filter:
                    startActivityForResult(new Intent(this, ParameterActivity.class)
                            .putExtra("parameter", mViewModel.mCurrentParameterMutableLiveData.getValue()),
                            Constants.LAUNCH_PARAMETER_ACTIVITY);
                    break;
                default:
                    break;
            }

            return false;
        });

        // Configuration
        binding.activityMainNavView.setNavigationItemSelectedListener(this);

        showListFragment();

        searchProperties(mViewModel.mCurrentParameterMutableLiveData.getValue());

    }

    private void updateUI() {
        long agentId = Utils.getSharedPreferences(this).getLong(Constants.PREF_AGENT_ID_LOGGED_KEY, -1);

        mViewModel.getAgent(agentId).observe(this, (Observer<Agent>) agent -> {
            if (mTextviewAgentName == null){
                mTextviewAgentName = (TextView) findViewById(R.id.activity_main_nav_header_textview_agentname);
            }

            if (mTextviewAgentName != null){
                mTextviewAgentName.setText( agent.getFirstname() + " " + agent.getLastname().toUpperCase());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.LAUNCH_DETAILS_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){

                searchProperties(mViewModel.mCurrentParameterMutableLiveData.getValue());

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == Constants.LAUNCH_PARAMETER_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){

                Parameter parameter = data.getParcelableExtra("result");

                searchProperties(parameter);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }

    private void searchProperties(Parameter parameter){

        this.mViewModel.searchProperties(parameter).observe(this, properties -> {

            // Gere le cas ou on cherche uniquement dans les favoris
            if (mViewModel.onlyFavorites){
                List<Property> newList = new ArrayList<>();
                Set<String> listFavorites = mSharedPreferences.getStringSet(Constants.PREF_FAVORITES_PROPERTIES_KEY, null);

                if (listFavorites != null){
                    for (int i = 0; i < properties.size(); i++){
                        if (listFavorites.contains(String.valueOf(properties.get(i).getId()))){
                            newList.add(properties.get(i));
                        }
                    }
                }

                mViewModel.mListPropertyMutableLiveData.setValue(newList);

            } else {

                // Filtrer par distance
                if(parameter.getLatitude() != 999999999.0 && parameter.getLongitude() != 999999999.0){

                    List<Property> newList = new ArrayList<>();

                    Location referenceLocation = new Location("Ref");
                    referenceLocation.setLatitude(parameter.getLatitude());
                    referenceLocation.setLongitude(parameter.getLongitude());

                    for (Property property : properties){
                        mViewModel.getAddress(property.getAddressId()).observe(this, address -> {
                            Location propertyLocation = new Location("Property");
                            propertyLocation.setLatitude(address.getLatitude());
                            propertyLocation.setLongitude(address.getLongitude());

                            if(referenceLocation.distanceTo(propertyLocation) > parameter.getDistanceAddressMin() &&
                                    referenceLocation.distanceTo(propertyLocation) < parameter.getDistanceAddressMax())
                            {
                                newList.add(property);
                                mViewModel.mListPropertyMutableLiveData.setValue(newList);
                            }
                        });
                    }

                    mViewModel.mListPropertyMutableLiveData.setValue(newList);

                } else {
                    mViewModel.mListPropertyMutableLiveData.setValue(properties);
                }
            }
        });
    }


    // 4 - Create each fragment page and show it

    private void showMapFragment(){
        if (this.fragmentMaps == null) this.fragmentMaps = MapsFragment.newInstance();
        this.startTransactionFragment(this.fragmentMaps);

        binding.activityMainToolbar.getMenu().findItem(R.id.menu_toolbar_item_switchview).setIcon(R.drawable.ic_baseline_list_24);
        currentFragment = ID_FRAGMENT_MAP;
    }

    private void showListFragment(){
        if (this.fragmentList == null) this.fragmentList = ListViewFragment.newInstance();
        this.startTransactionFragment(this.fragmentList);

        binding.activityMainToolbar.getMenu().findItem(R.id.menu_toolbar_item_switchview).setIcon(R.drawable.ic_baseline_map_24);
        currentFragment = ID_FRAGMENT_LIST;
    }

    // ---

    // 3 - Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (binding.activityMainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_add_property:
                startActivity(new Intent(this, EditPropertyActivity.class));
                break;
            case R.id.activity_main_drawer_loan_simulator:
                startActivity(new Intent(this, LoanSimulatorActivity.class));
                break;
            case R.id.activity_main_drawer_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                break;
            case R.id.activity_main_drawer_favorite:

                mViewModel.onlyFavorites = true;
                binding.activityMainToolbar.setBackgroundColor(getResources().getColor(R.color.starFavorite));
                binding.activityMainToolbar.setTitle("Favoris");
                binding.activityMainToolbar.getMenu().findItem(R.id.menu_toolbar_item_filter).setVisible(false);

                showListFragment();
                searchProperties(mViewModel.mCurrentParameterMutableLiveData.getValue());

                break;
            case R.id.activity_main_drawer_search_property:

                mViewModel.onlyFavorites = false;
                binding.activityMainToolbar.setBackgroundColor(getResources().getColor(R.color.main_activity_toolbar_backgroundcolor));
                binding.activityMainToolbar.setTitle("Search Property");
                binding.activityMainToolbar.getMenu().findItem(R.id.menu_toolbar_item_filter).setVisible(true);

                showListFragment();
                searchProperties(mViewModel.mCurrentParameterMutableLiveData.getValue());

                break;
            default:
                break;
        }

        this.binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


}
