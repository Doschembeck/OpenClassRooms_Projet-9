package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.fragments.ListView.ListViewFragment;
import com.openclassrooms.realestatemanager.ui.fragments.MapViewFragment;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private PropertyViewModel mViewModel;
    private ActivityMainBinding binding;

    private static final String[]paths = {"Massieux", "- 120 000€"};
    private int currentFragment = 1;
    private final int ID_FRAGMENT_LIST = 1;
    private final int ID_FRAGMENT_MAP = 2;
    private Fragment fragmentMap;
    private Fragment fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureSpinnerToolbar();

        binding.activityMainToolbar.setNavigationOnClickListener(view -> {
            if (binding.activityMainDrawerLayout.isDrawerOpen(GravityCompat.START)){
                binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);
            }else {
                binding.activityMainDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.activityMainToolbar.setOnMenuItemClickListener(item -> {
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
            return false;
        });

        generateFakeFilterProperty();

        showListFragment();

        // Configuration
        binding.activityMainNavView.setNavigationItemSelectedListener(this);

    }

    private void configureSpinnerToolbar(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activityMainToolbarSpinner.setAdapter(adapter);
    }

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }

    private void generateFakeFilterProperty(){
        // todo à supprimer
        List<String> listTest = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"
                , "Item 7", "Item 8", "Item 9", "Item 10", "Item 11", "Item 12");

        mViewModel.mListFilterPropertyMutableLiveData.setValue(listTest);
    }

    // 4 - Create each fragment page and show it

    private void showMapFragment(){
        if (this.fragmentMap == null) this.fragmentMap = MapViewFragment.newInstance();
        this.startTransactionFragment(this.fragmentMap);

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
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, fragment).commit();
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
                break;
            default:
                break;
        }

        this.binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


}
