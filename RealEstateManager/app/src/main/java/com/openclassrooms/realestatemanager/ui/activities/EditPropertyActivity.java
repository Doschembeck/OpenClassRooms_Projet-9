package com.openclassrooms.realestatemanager.ui.activities;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.resources.CancelableFontCallback;
import com.openclassrooms.realestatemanager.databinding.ActivityEditPropertyBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Devise;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyNearbyPoiJoin;
import com.openclassrooms.realestatemanager.utils.ActivityUtils;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.FormatUtils;
import com.openclassrooms.realestatemanager.utils.ScriptsStats;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class EditPropertyActivity extends BaseActivity {

    private final int RESULT_LOAD_IMG = 312;
    private PropertyViewModel mViewModel;
    private ActivityEditPropertyBinding binding;
    private SharedPreferences mSharedPreferences;

    private Property mEditProperty;
    private List<Photo> mEditListPhoto;
    private List<NearbyPOI> mEditListNearbyPOI;
    private Address mAddress;
    private Devise mDevise;
    private Geocoder mGeocoder;
    private List<NearbyPOI> selectedNearbyPOI = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        binding = ActivityEditPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // --- INIT ---
        mGeocoder = ActivityUtils.setupAutoComplete(this);
        configureSpinnerDevise();
        configureSpinnerPropertyType();
        updateUIWithSharedPreferences();

        // --- Listeners ---
        binding.activityEditPropertyButtonCreatephotos.setOnClickListener(this::onClickButtonCreatePhotos);
        binding.activityEditPropertyButtonAddphotos.setOnClickListener(this::onClickButtonAddPhotos);
        binding.activityEditPropertyEdittextAddress.setOnFocusChangeListener((view, b) -> {
            if(b){
                view.clearFocus();
                startAutoComplete(view);
            }
        });
        binding.activityEditPropertyToolbar.setOnClickListener(v -> onBackPressed());
        binding.activityEditPropertyButtonCreatepoi.setOnClickListener(this::onClickButtonCreateNearbyPoi);
        binding.activityEditPropertySwitchIssold.setOnCheckedChangeListener(this::onClickSwitchIsSold);
        binding.activityEditPropertyButtonAddnearbypoi.setOnClickListener(this::onClickButtonAddNearbyPOI);
        binding.activityEditPropertyImageviewDateofsale.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, binding.activityEditPropertyEdittextDateofsold));
        binding.activityEditPropertyAddproperty.setOnClickListener(this::onClickButtonAddProperty);
        binding.activityEditPropertySpinnerDevise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDevise = Constants.LIST_OF_DEVISES[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // UpdateUI
        binding.activityEditPropertyEdittextDateofsold.setText(Utils.getTodayDate());

        // Check if this is a modification mode
        Property property = getIntent().getParcelableExtra("property");
        if (property != null){
            mEditProperty = property;
            updateUIWithProperty();

            binding.activityEditPropertyAddproperty.setText("Modifier");
            binding.activityEditPropertyToolbar.setTitle("Edit Property");
        }else {
            mViewModel.getAllNearbyPOI().observe(this, this::updateUIWithAllNearbyPOI);
        }
    }

    private void onClickButtonAddPhotos(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), RESULT_LOAD_IMG);
    }

    private void onClickSwitchIsSold(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked){
            binding.activityEditPropertyLinearlayoutDateofsale.setVisibility(View.VISIBLE);
        }else {
            binding.activityEditPropertyLinearlayoutDateofsale.setVisibility(View.GONE);
        }
    }

    private void onClickButtonCreateNearbyPoi(View view) {
        if(binding.activityEditPropertyLinearlayoutCreatenearbypoi.getVisibility() == View.GONE){
            binding.activityEditPropertyButtonCreatepoi.setText("Annuler");
            binding.activityEditPropertyLinearlayoutCreatenearbypoi.setVisibility(View.VISIBLE);
        } else {
            binding.activityEditPropertyButtonCreatepoi.setText("Créer un point d'interet");
            binding.activityEditPropertyLinearlayoutCreatenearbypoi.setVisibility(View.GONE);
        }
    }

    private void onClickButtonCreatePhotos(View view) {
        if(binding.activityEditPropertyLinearlayoutCreatephotos.getVisibility() == View.GONE){
            binding.activityEditPropertyButtonCreatephotos.setText("Annuler");
            binding.activityEditPropertyLinearlayoutCreatephotos.setVisibility(View.VISIBLE);
        } else {
            binding.activityEditPropertyButtonCreatephotos.setText("Ajouter une photo");
            binding.activityEditPropertyLinearlayoutCreatephotos.setVisibility(View.GONE);
        }    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.AUTOCOMPLETE_REQUEST_CODE){
            if (resultCode == AutocompleteActivity.RESULT_OK){
                LatLng latLng = Autocomplete.getPlaceFromIntent(data).getLatLng();;

                getAddress(latLng.latitude, latLng.longitude);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED){
            }
        }else if (requestCode == RESULT_LOAD_IMG){
            if (resultCode == RESULT_OK) {

                // Update ListPictures
                List<Photo> currentPhotos = mViewModel.propertyPictureListMutableLiveData.getValue();
                currentPhotos.add(new Photo(0, 0, data.getData().toString(), FormatUtils.formatPOIName(binding.activityEditPropertyEdittextPicturedescription.getText().toString())));
                mViewModel.propertyPictureListMutableLiveData.setValue(currentPhotos);

                binding.activityEditPropertyEdittextPicturedescription.setText("");
                binding.activityEditPropertyLinearlayoutCreatephotos.setVisibility(View.GONE);
                binding.activityEditPropertyButtonCreatephotos.setText("Ajouter une photo");

            }else {
                Toast.makeText(getApplicationContext(),"Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getAddress(Double latitude, Double longitude) {

        try {
            if (mGeocoder != null){
                List<android.location.Address> addresses = mGeocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    android.location.Address address = addresses.get(0);
                    long addressId = 0;
                    long propertyId = 0;

                    if (mAddress != null){
                        addressId = mAddress.getId();
                        propertyId = mAddress.getPropertyId();
                    }

                    mAddress = new Address(addressId, propertyId, address.getFeatureName(), address.getThoroughfare(), address.getLocality(), address.getPostalCode(), address.getCountryName(), address.getLatitude(), address.getLongitude());

                    binding.activityEditPropertyEdittextAddress.setText(mAddress.getCompleteAddress());
                }
            } else {
                Toast.makeText(this, "Erreur veuillez choisir une adresse valide", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startAutoComplete(View view) {

        startActivityForResult(
                new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN,
                    Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                        .build(this),
                Constants.AUTOCOMPLETE_REQUEST_CODE);
    }

    // === OnClick buttons ===

    private void onClickButtonAddNearbyPOI(View view){
        String nearbyPoiName = binding.activityEditPropertyEdittextNearbypoi.getText().toString();
        binding.activityEditPropertyEdittextNearbypoi.setText("");

        if (!nearbyPoiName.replaceAll("\\s", "").equals("")){
            mViewModel.createNearbyPOI(new NearbyPOI(0, FormatUtils.formatPOIName(nearbyPoiName)));

            binding.activityEditPropertyButtonCreatepoi.setText("Créer un point d'interet");
            binding.activityEditPropertyLinearlayoutCreatenearbypoi.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "Le nom du point d'interet de peut pas etre vide !", Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean checkIfFormIsValid(){
        List<Photo> photoList =  mViewModel.propertyPictureListMutableLiveData.getValue();

        String price = binding.activityEditPropertyEdittextPrice.getText().toString();
        String area = binding.activityEditPropertyEdittextArea.getText().toString();
        String nbOfRooms = binding.activityEditPropertyEdittextNbofrooms.getText().toString();
        String nbOfBedRooms = binding.activityEditPropertyEdittextNbofbedrooms.getText().toString();

        if (photoList.size() == 0){
            Toast.makeText(this, "Merci de bien vouloir ajouter une photo", Toast.LENGTH_SHORT).show();
            return false;
        } else if (price.equals("")){
            Toast.makeText(this, "Merci de bien vouloir ajouter un prix", Toast.LENGTH_SHORT).show();
            return false;
        } else if (area.equals("")){
            Toast.makeText(this, "Merci de bien vouloir ajouter une surface", Toast.LENGTH_SHORT).show();
            return false;
        }else if(nbOfRooms.equals("")){
            Toast.makeText(this, "Merci de bien vouloir ajouter un nombre de pièces", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nbOfBedRooms.equals("")) {
            Toast.makeText(this, "Merci de bien vouloir ajouter un nombre de chambre", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void onClickButtonAddProperty(View v){

        if (!checkIfFormIsValid()) {
            return;
        }

        // UPDATE
        if (mEditProperty != null){
            if (updateProperty()) {
                Toast.makeText(this, "La propriété a bien été mis a jour", Toast.LENGTH_SHORT).show();
                finish();
            }
            else Toast.makeText(this, "Une erreur est survenue", Toast.LENGTH_LONG).show();

        // CREATE
        } else if (createCompleteProperty()) {
            Toast.makeText(this, "La propriété a bien été créer", Toast.LENGTH_SHORT).show();
            finish();
        }

        // ERROR
        else Toast.makeText(this, "Veuillez selectionner une address avant de continuer", Toast.LENGTH_LONG).show();
    }

    private Boolean updateProperty(){

        // 1- Update l'address
        mAddress.setPropertyId(mEditProperty.getId());
        mViewModel.updateAddress(mAddress);

        // 2- Update Photos
        List<Photo> photoUrlList = mViewModel.propertyPictureListMutableLiveData.getValue();

            // NEED DELETE
        for (int i = 0; i < mEditListPhoto.size(); i++){
            Boolean needDelete = true;

            for (Photo photo : photoUrlList){
                if (photo.getId() == mEditListPhoto.get(i).getId()){
                    needDelete = false;
                    break;
                }
            }
            if (needDelete){
                mViewModel.deletePhoto(mEditListPhoto.get(i));
            }
        }

            // NEED CREATE
        for (int i = 0; i < photoUrlList.size(); i++){
            Boolean needCreate = true;

            for (Photo photo : mEditListPhoto){
                if (photo.getId() == photoUrlList.get(i).getId()){
                    needCreate = false;
                    break;
                }
            }
            if (needCreate){
                Photo photo = photoUrlList.get(i);
                photo.setPropertyId(mEditProperty.getId());

                mViewModel.createPhoto(photo);
            }
        }

        // 3- Update Property
        Property currentProperty = createProperty();
        currentProperty.setId(mEditProperty.getId());
        currentProperty.setCity(mAddress.getCity());

        mViewModel.updateProperty(currentProperty);

        // 4- Update NearbyPOI

        // NEED DELETE
        for (int i = 0; i < mEditListNearbyPOI.size(); i++){
            Boolean needDelete = true;

            for (NearbyPOI nearbyPOI : selectedNearbyPOI){
                if (nearbyPOI.getId() == mEditListNearbyPOI.get(i).getId()){
                    needDelete = false;
                    break;
                }
            }

            if (needDelete){
                mViewModel.deletePropertyNearbyPoiJoin(new PropertyNearbyPoiJoin(mEditProperty.getId(), mEditListNearbyPOI.get(i).getId()));
            }
        }

        // NEED CREATE
        for (int i = 0; i < selectedNearbyPOI.size(); i++){
            Boolean needCreate = true;

            for (NearbyPOI nearbyPOI : mEditListNearbyPOI){
                if (nearbyPOI.getId() == selectedNearbyPOI.get(i).getId()){
                    needCreate = false;
                    break;
                }
            }

            if (needCreate){
                mViewModel.createPropertyNearbyPoiJoin(new PropertyNearbyPoiJoin(mEditProperty.getId(), selectedNearbyPOI.get(i).getId()));
            }
        }

        return true;
    }

    // === Update UI ===

    private void updateUIWithPhotos(List<Photo> photos) {

        if (mEditListPhoto == null){
            mEditListPhoto = new ArrayList<>();
            mEditListPhoto.addAll(photos);
        }

        mViewModel.propertyPictureListMutableLiveData.setValue(photos);
    }

    private void updateUIWithAddress(Address address) {
        binding.activityEditPropertyEdittextAddress.setText(address.getCompleteAddress());

        if (mAddress == null){
            mAddress = address;
        }
    }

    private void updateUIWithProperty(){
        mViewModel.getAddressWithPropertyId(mEditProperty.getId()).observe(this, this::updateUIWithAddress);
        mViewModel.getPropertyForNearbyPoi(mEditProperty.getId()).observe(this, this::updateUIWithNearbyPOI);
        mViewModel.getAllPropertyPhoto(mEditProperty.getId()).observe(this, this::updateUIWithPhotos);

        binding.activityEditPropertySpinnerDevise.setSelection(Utils.getCurrentDeviseId(mSharedPreferences));
        binding.activityEditPropertySpinnerPropertytype.setSelection(mEditProperty.getPropertyTypeId());
        binding.activityEditPropertyEdittextPrice.setText(FormatUtils.formatEditTextWithNotDevise(mEditProperty.getPrice(), mDevise));
        binding.activityEditPropertyEdittextArea.setText(String.valueOf(mEditProperty.getArea()));
        binding.activityEditPropertyEdittextNbofrooms.setText(String.valueOf(mEditProperty.getNbOfRooms()));
        binding.activityEditPropertyEdittextNbofbedrooms.setText(String.valueOf(mEditProperty.getNbOfBedRooms()));
        binding.activityEditPropertyEdittextDescription.setText(mEditProperty.getDescription());

        if (mEditProperty.isSold()){
            binding.activityEditPropertySwitchIssold.setChecked(true);
            binding.activityEditPropertyEdittextDateofsold.setText(FormatUtils.formatDate(mEditProperty.getDateOfSale()));
        }

    }

    private void updateUIWithNearbyPOI(List<NearbyPOI> nearbyPOIList) {
        if (mEditListNearbyPOI == null){
            mEditListNearbyPOI = nearbyPOIList;
        }
        selectedNearbyPOI.addAll(nearbyPOIList);
        mViewModel.getAllNearbyPOI().observe(this, this::updateUIWithAllNearbyPOI);
    }

    private void updateUIWithAllNearbyPOI(List<NearbyPOI> nearbyPOIList){

        binding.activityEditPropertyLinearlayoutNearbypoi.removeAllViewsInLayout();
        for (NearbyPOI nearbyPOI : nearbyPOIList){
            CheckBox checkBox = new CheckBox(this);

            // Si le nearby est selectionné alors on le check
            for (int i = 0; i < selectedNearbyPOI.size(); i++){
                if (selectedNearbyPOI.get(i).getId() == nearbyPOI.getId()){
                    checkBox.setChecked(true);
                }
            }

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked){
                    selectedNearbyPOI.add(nearbyPOI);
                }else {
                    for (int i = 0; i < selectedNearbyPOI.size(); i++){
                        if (selectedNearbyPOI.get(i).getId() == nearbyPOI.getId()){
                            selectedNearbyPOI.remove(i);
                        }
                    }
                }
            });
            checkBox.setText(nearbyPOI.getName());
            binding.activityEditPropertyLinearlayoutNearbypoi.addView(checkBox);
        }
    }

    private void updateUIWithSharedPreferences(){
        mSharedPreferences = Utils.getSharedPreferences(this);
        mDevise = Utils.getCurrentDevise(mSharedPreferences);
        binding.activityEditPropertySpinnerDevise.setSelection(Utils.getCurrentDeviseId(mSharedPreferences));
    }

    // === Process ===

    private boolean createCompleteProperty(){

        if (mAddress != null){
            // Créer la Property
            long propertyId = mViewModel.createProperty(createProperty());

            // Créer l'address
            mAddress.setPropertyId(propertyId);
            mViewModel.createAddress(mAddress);

            // Créer les photos
            List<Photo> photoUrlList = mViewModel.propertyPictureListMutableLiveData.getValue();
            for (int i=0; i < photoUrlList.size(); i++){
                Photo photo = photoUrlList.get(i);
                photo.setPropertyId(propertyId);

                mViewModel.createPhoto(photo);
            }

            // Lie les NearbyPOI aux Property
            for (NearbyPOI nearbyPOI : selectedNearbyPOI){
                mViewModel.createPropertyNearbyPoiJoin(new PropertyNearbyPoiJoin(propertyId, nearbyPOI.getId()));
            }
        } else {
            return false;
        }

        return true;
    }

    private Property createProperty() {

        List<Photo> photoList =  mViewModel.propertyPictureListMutableLiveData.getValue();
        String mainPictureUrl =  photoList.get(0).getUrlPicture();
        int nbOfPictures = photoList.size();

        int propertyType = (int) binding.activityEditPropertySpinnerPropertytype.getSelectedItemId();
        double price = mDevise.convertDeviseToDollar(Double.parseDouble(binding.activityEditPropertyEdittextPrice.getText().toString()));
        float area = Float.parseFloat(binding.activityEditPropertyEdittextArea.getText().toString());
        int nbOfRooms = Integer.parseInt(binding.activityEditPropertyEdittextNbofrooms.getText().toString());
        int nbOfBedRooms = Integer.parseInt(binding.activityEditPropertyEdittextNbofbedrooms.getText().toString());
        String description = binding.activityEditPropertyEdittextDescription.getText().toString();
        double pricePerSquareMeter = price / area;
        float rate = ScriptsStats.getRateProperty(mViewModel, pricePerSquareMeter, mAddress.getCity());
        Date date = FormatUtils.formatStringFormattedToDate(binding.activityEditPropertyEdittextDateofsold.getText().toString());
        Date dateOfSale = binding.activityEditPropertySwitchIssold.isChecked() ? date : new Date(0);

        return new Property(0,propertyType,price, pricePerSquareMeter, area,nbOfRooms,nbOfBedRooms,description, rate, mAddress.getCity(), mainPictureUrl, nbOfPictures, getCurrentAgentId() , dateOfSale, date, date);
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

        binding.activityEditPropertyEdittextDescription.setText("Maison sans vis a vis, bien située...");
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utils.getAllNameOfDevises());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activityEditPropertySpinnerDevise.setAdapter(adapter);
    }

    private void configureSpinnerPropertyType(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constants.ListPropertyType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activityEditPropertySpinnerPropertytype.setAdapter(adapter);
    }

    private void configureViewModel(){
        this.mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this)).get(PropertyViewModel.class);
    }

}