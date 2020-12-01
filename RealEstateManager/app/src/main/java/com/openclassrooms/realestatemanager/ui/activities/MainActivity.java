package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.ui.fragments.ListView.ListViewFragment;
import com.openclassrooms.realestatemanager.ui.ListViewViewModel;
import com.openclassrooms.realestatemanager.ui.fragments.MapViewFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.activity_main_floatingactionbutton_hamburger) FloatingActionButton mFloatingActionButtonAdd;
    @BindView(R.id.activity_main_floatingactionbutton_switch_view) FloatingActionButton mFloatingActionButtonSwitchView;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView navigationView;

    private ListViewViewModel mViewModel;

    private int currentFragment = 1;
    private final int ID_FRAGMENT_LIST = 1;
    private final int ID_FRAGMENT_MAP = 2;
    private Fragment fragmentMap;
    private Fragment fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ListViewViewModel.class);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        generateFakeFilterProperty();
        generateFakeProperty();

        showListFragment();

        // Configuration
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void generateFakeFilterProperty(){
        // todo à supprimer
        List<String> listTest = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"
                , "Item 7", "Item 8", "Item 9", "Item 10", "Item 11", "Item 12");

        mViewModel.mListFilterPropertyMutableLiveData.setValue(listTest);
    }

    private void generateFakeProperty(){
        // todo à remplacer par une recherche dans la BDD

        List<Photo> listPhoto = Arrays.asList(new Photo("https://d1qfj231ug7wdu.cloudfront.net/pictures/estate/3414/3413354/19651742155dd3194dd4f208.85103143_1920.jpg", "Description phoo"));
        List<String> listNearbyPOI = Arrays.asList("restaurant", "ecole", "gare");
        Address address1 = new Address(69, "rue de l'eglise", "Parcieux", "01600", "France");

        Property property1 = new Property(1, 220000d, 290,
                5, 2, "description", listPhoto, address1,
                listNearbyPOI, false, "22/11/2020", "Thomas", "22/11/2020", "22/11/2020");
        Property property2 = new Property(2, 386000d, 309,
                5, 3, "description", listPhoto, address1,
                listNearbyPOI, true, "22/11/2020", "Thomas", "22/11/2020", "22/11/2020");

        List<Property> listTest = Arrays.asList(property1, property1, property2, property1);

        mViewModel.mListPropertyMutableLiveData.setValue(listTest);
    }

    @OnClick(R.id.activity_main_floatingactionbutton_hamburger) public void onClickFloatingActionButtonHamburger(){
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            this.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @OnClick(R.id.activity_main_floatingactionbutton_switch_view) public void onClickFloatingActionButtonSwitchView(){

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
    }


    // 4 - Create each fragment page and show it

    private void showMapFragment(){
        if (this.fragmentMap == null) this.fragmentMap = MapViewFragment.newInstance();
        this.startTransactionFragment(this.fragmentMap);

        mFloatingActionButtonSwitchView.setImageResource(R.drawable.ic_baseline_list_24);
        currentFragment = ID_FRAGMENT_MAP;
    }

    private void showListFragment(){
        if (this.fragmentList == null) this.fragmentList = ListViewFragment.newInstance();
        this.startTransactionFragment(this.fragmentList);

        mFloatingActionButtonSwitchView.setImageResource(R.drawable.ic_baseline_map_24);
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
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_loan_simulator:
                startActivity(new Intent(this, LoanSimulatorActivity.class));
                break;
            case R.id.activity_main_drawer_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


}
