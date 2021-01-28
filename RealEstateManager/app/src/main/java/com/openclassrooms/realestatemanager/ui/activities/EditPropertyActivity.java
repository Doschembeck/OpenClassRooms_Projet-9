package com.openclassrooms.realestatemanager.ui.activities;

import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.openclassrooms.realestatemanager.databinding.ActivityEditPropertyBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyNearbyPoiJoin;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class EditPropertyActivity extends BaseActivity {

    private PropertyViewModel mViewModel;
    private ActivityEditPropertyBinding binding;
    private DatePickerDialog datePickerDialog;

    private Date thisDate;
    private int thisYear;
    private int thisMonth;
    private int thisDayOfMonth;
    private List<NearbyPOI> selectedNearbyPOI = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        binding = ActivityEditPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateDate();

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            month++;
            String strMonth = ""; if(month<10) strMonth += "0"; strMonth += month;

            try {
                thisDate = new SimpleDateFormat("yyyy/MM/dd").parse(year + "/" + strMonth + "/" + dayOfMonth);
            } catch (ParseException e) { e.printStackTrace(); }

            binding.activityEditPropertyEdittextDateofsold.setText(String.format("%d/%s/%d", dayOfMonth, strMonth, year));
        };

        // Listeners
        binding.activityEditPropertyToolbar.toolbarOnlyback.setOnClickListener(v -> onBackPressed());
        binding.activityEditPropertyAddproperty.setOnClickListener(v -> onClickButtonAddProperty());
        binding.activityEditPropertySwitchIssold.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                binding.activityEditPropertyLinearlayoutDateofsale.setVisibility(View.VISIBLE);
            }else {
                binding.activityEditPropertyLinearlayoutDateofsale.setVisibility(View.GONE);
            }
        });
        binding.activityEditPropertyButtonAddnearbypoi.setOnClickListener(this::onClickButtonAddNearbyPOI);
        binding.activityEditPropertyImageviewDateofsale.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, dateSetListener, thisYear, thisMonth, thisDayOfMonth);
            datePickerDialog.show();
        });

        // Configuration
        configureSpinnerDevise();
        configureSpinnerPropertyType();

        // UpdateUI
        mViewModel.getAllNearbyPOI().observe(this, this::updateUIWithNearbyPOI);
        updateUIWithSharedPreferences();
        generateFakeInfoProperty(); //todo à supprimer

    }

    // === OnClick buttons ===

    private void onClickButtonAddNearbyPOI(View view){
        String nearbyPoiName = binding.activityEditPropertyEdittextNearbypoi.getText().toString();

        mViewModel.createNearbyPOI(new NearbyPOI(0, nearbyPoiName));
    }

    private void onClickButtonAddProperty(){
        createCompleteProperty();
        finish();
    }

    // === Update UI ===

    private void updateDate(){
        thisDate = new Date();
        thisYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(thisDate)) ;
        thisMonth = Integer.parseInt(new SimpleDateFormat("MM").format(thisDate));
        thisDayOfMonth = Integer.parseInt(new SimpleDateFormat("dd").format(thisDate));

        binding.activityEditPropertyEdittextDateofsold.setText(new SimpleDateFormat("dd/MM/yyyy").format(thisDate));
    }

    private void updateUIWithNearbyPOI(List<NearbyPOI> nearbyPOIList){

        binding.activityEditPropertyLinearlayoutNearbypoi.removeAllViewsInLayout();

        for (NearbyPOI nearbyPOI : nearbyPOIList){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked){
                    selectedNearbyPOI.add(nearbyPOI);
                }else {
                    selectedNearbyPOI.remove(nearbyPOI);
                }

            });
            checkBox.setText(nearbyPOI.getName());
            binding.activityEditPropertyLinearlayoutNearbypoi.addView(checkBox);
        }
    }

    private void updateUIWithSharedPreferences(){

        String deviseSelected = getSharedPreferences().getString(Constants.PREF_CURRENCY_KEY, "");
        int positionDevise = 0;

        for (int i = 0; i < Constants.LIST_OF_DEVISES_ISO.length; i++){
            if (deviseSelected.equals(Constants.LIST_OF_DEVISES_ISO[i])){
                positionDevise = i;
            }
        }

        binding.activityEditPropertySpinnerDevise.setSelection(positionDevise);
    }

    // === Process ===

    private void createCompleteProperty(){
        //todo: faire une creation complete

        // Créer l'address
        int streetNumber = Integer.parseInt(binding.activityEditPropertyEdittextStreetnumber.getText().toString());
        String streetName = binding.activityEditPropertyEdittextStreetname.getText().toString();
        String city = binding.activityEditPropertyEdittextCity.getText().toString();
        String zipcode = binding.activityEditPropertyEdittextZipcode.getText().toString();
        String country = binding.activityEditPropertyEdittextCountry.getText().toString();
        double longitude = new Random().nextInt(360) - 180; //todo
        double latitude = new Random().nextInt(180) - 90; //todo
        long addressId = mViewModel.createAddress((new Address(0, streetNumber, streetName, city, zipcode, country, latitude, longitude)));

        String mainPictureUrl = generateFakePhotos().get(0);

        // Créer la Property
        int propertyType = (int) binding.activityEditPropertySpinnerPropertytype.getSelectedItemId();
        double price = Utils.convertDeviseToDollar(Integer.parseInt(binding.activityEditPropertyEdittextPrice.getText().toString()), Constants.LIST_OF_DEVISES_ISO[binding.activityEditPropertySpinnerDevise.getSelectedItemPosition()]);
        float area = Float.parseFloat(binding.activityEditPropertyEdittextArea.getText().toString());
        int nbOfRooms = Integer.parseInt(binding.activityEditPropertyEdittextNbofrooms.getText().toString());
        int nbOfBedRooms = Integer.parseInt(binding.activityEditPropertyEdittextNbofbedrooms.getText().toString());
        String description = binding.activityEditPropertyEdittextDescription.getText().toString();

        Date dateOfSale;
        if (binding.activityEditPropertySwitchIssold.isChecked()) dateOfSale = thisDate; else dateOfSale = null;

        long propertyId = mViewModel.createProperty((new Property(0,propertyType,price,area,nbOfRooms,nbOfBedRooms,description,
                addressId, city, mainPictureUrl, getCurrentAgentId() , dateOfSale, thisDate, thisDate)));

        // Créer les photos
        List<String> photoUrlList = generateFakePhotos();
        for (int i=0; i < photoUrlList.size(); i++){
            mViewModel.createPhoto(new Photo(0, propertyId, photoUrlList.get(i), "Salon"));
        }

        // Lie les NearbyPOI aux Property
        for (NearbyPOI nearbyPOI : selectedNearbyPOI){
            mViewModel.createPropertyNearbyPoiJoin(new PropertyNearbyPoiJoin(propertyId, nearbyPOI.getId()));
        }

    }

    // === Generators ===

    private void generateFakeInfoProperty() {

        int nbOfRooms = 1 + new Random().nextInt(15-1);
        int isSoldInt = new Random().nextInt(2);
        Boolean isSold;
        if (isSoldInt == 0){
            isSold = false;
        } else{
            isSold = true;
        }

        binding.activityEditPropertySpinnerPropertytype.setSelection(new Random().nextInt(Constants.ListPropertyType.length));
        binding.activityEditPropertyEdittextPrice.setText(String.valueOf(80000 + new Random().nextInt(1000000-80000)));
        binding.activityEditPropertyEdittextArea.setText(String.valueOf(15 + new Random().nextInt(600-15)));
        binding.activityEditPropertyEdittextNbofrooms.setText(String.valueOf(nbOfRooms));
        binding.activityEditPropertyEdittextNbofbedrooms.setText(String.valueOf(1 + new Random().nextInt(nbOfRooms)));
        binding.activityEditPropertySwitchIssold.setChecked(isSold);
    }

    private List<String> generateFakePhotos(){

        List<String> newList = new ArrayList<>();
        List<String> listPhotos = Arrays.asList("https://photo.barnes-international.com/annonces/bms/178/xl/14569816415d5d245c21a232.24573384_b968cfeda8_1920.jpg",
                "https://www.book-a-flat.com/magazine/wp-content/uploads/2016/12/espace-optimise-appartement-meuble-paris.jpg",
                "https://www.vanupied.com/wp-content/uploads/68550354.jpg",
                "https://storage.gra.cloud.ovh.net/v1/AUTH_e0b83750570d4ff1986fe199b41300e4/kimono/83aedb23ed928457af040d8a0628cfcba5161167/600x370-fit-cover-orientation-0deg?width=600&height=370&fit=cover",
                "https://labordelaise.staticlbi.com/680x680/images/biens/1/fe4ad38359cf7382d53e0156737948f3/original/photo_750ed3f33e9274bcb7bb920c4ae7c3f0.jpg",
                "https://magazine.bellesdemeures.com/sites/default/files/styles/manual_crop_735x412/public/article/image/appartement-luxe_0.jpg?itok=umeiiOxc",
                "https://www.protegez-vous.ca/var/protegez_vous/storage/images/6/5/3/6/2946356-1-fre-CA/fraude-appartement.jpg",
                "https://storage.gra.cloud.ovh.net/v1/AUTH_e0b83750570d4ff1986fe199b41300e4/kimono/3fdeb5dda266bfac27f633aebaf3afe7e458de7d/600x370-fit-cover-orientation-0deg?width=600&height=370&fit=cover",
                "https://costainvest.com/media/images/properties/thumbnails/61097_lg.jpg"
                );

        int nbOfPhotos = 1 + new Random().nextInt(listPhotos.size() - 1);

        for (int i = 0; i < nbOfPhotos; i++){
            newList.add(listPhotos.get(new Random().nextInt(listPhotos.size())));
        }

        return newList;
    }

    // === Configuration ===

    private void configureSpinnerDevise(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constants.LIST_OF_DEVISES_NAME);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activityEditPropertySpinnerDevise.setAdapter(adapter);
    }

    private void configureSpinnerPropertyType(){
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constants.ListPropertyType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activityEditPropertySpinnerPropertytype.setAdapter(adapter);
    }

    private void configureViewModel(){
        this.mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this)).get(PropertyViewModel.class);
    }

}