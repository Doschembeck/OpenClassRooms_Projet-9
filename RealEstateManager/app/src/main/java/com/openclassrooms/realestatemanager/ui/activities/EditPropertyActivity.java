package com.openclassrooms.realestatemanager.ui.activities;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityEditPropertyBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.model.Address;
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
import java.util.Locale;
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
    private String mDevise;
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
        binding.activityEditPropertyButtonGeocoding.setOnClickListener(this::startAutoComplete);
        binding.activityEditPropertyToolbar.setOnClickListener(v -> onBackPressed());
        binding.activityEditPropertyButtonCreatepoi.setOnClickListener(this::onClickButtonCreateNearbyPoi);
        binding.activityEditPropertySwitchIssold.setOnCheckedChangeListener(this::onClickSwitchIsSold);
        binding.activityEditPropertyButtonAddnearbypoi.setOnClickListener(this::onClickButtonAddNearbyPOI);
        binding.activityEditPropertyImageviewDateofsale.setOnClickListener(v -> ActivityUtils.createDatePickerDialog(this, binding.activityEditPropertyEdittextDateofsold));
        binding.activityEditPropertyAddproperty.setOnClickListener(this::onClickButtonAddProperty);

        // UpdateUI
        binding.activityEditPropertyEdittextDateofsold.setText(Utils.getTodayDate());

        Property property = getIntent().getParcelableExtra("property");
        if (property != null){
            mEditProperty = property;
            updateUIWithProperty();

            binding.activityEditPropertyAddproperty.setText("Modifier");
            binding.activityEditPropertyToolbar.setTitle("Edit Property");
        }else {
            mViewModel.getAllNearbyPOI().observe(this, this::updateUIWithAllNearbyPOI);
//            generateFakeInfoProperty(); //todo à supprimer
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

                List<Photo> photoList1 = mEditListPhoto;
                List<Photo> photoList2 = mViewModel.propertyPictureListMutableLiveData.getValue();

                // Update ListPictures
                List<Photo> currentPhotos = mViewModel.propertyPictureListMutableLiveData.getValue();
                Photo photo = new Photo(0, 0, data.getData().toString(), FormatUtils.formatPOIName(binding.activityEditPropertyEdittextPicturedescription.getText().toString()));

                //TODO BUG : LE PROBLEME VIENT DE LA LIGNE DE DESSOUS !!!
                currentPhotos.add(photo);
                Log.d("TAG", "onActivityResult: "); //todo TEST A SUPPRIMER
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
                    mAddress = new Address(0, address.getFeatureName(), address.getThoroughfare(), address.getLocality(), address.getPostalCode(), address.getCountryName(), address.getLatitude(), address.getLongitude());
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

    private void onClickButtonAddProperty(View v){

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
        mAddress.setId(mEditProperty.getAddressId());
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
        Property currentProperty = createProperty(mEditProperty.getAddressId());
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

    private void updateUIWithProperty(){

        //todo: les points d'interets

        mViewModel.getAddress(mEditProperty.getAddressId()).observe(this, this::updateUIWithAddress);
        mViewModel.getPropertyForNearbyPoi(mEditProperty.getId()).observe(this, this::updateUIWithNearbyPOI);
        mViewModel.getAllPropertyPhoto(mEditProperty.getId()).observe(this, this::updateUIWithPhotos);

        binding.activityEditPropertySpinnerPropertytype.setSelection(mEditProperty.getPropertyTypeId());
        binding.activityEditPropertyEdittextPrice.setText(FormatUtils.formatEditTextWithNotDevise(mEditProperty.getPrice(), mDevise));
        binding.activityEditPropertyEdittextArea.setText(String.valueOf(mEditProperty.getArea()));
        binding.activityEditPropertyEdittextNbofrooms.setText(String.valueOf(mEditProperty.getNbOfRooms()));
        binding.activityEditPropertyEdittextNbofbedrooms.setText(String.valueOf(mEditProperty.getNbOfBedRooms()));
        binding.activityEditPropertyEdittextDescription.setText(mEditProperty.getDescription());
        binding.activityEditPropertySwitchIssold.setChecked(mEditProperty.isSold());
        binding.activityEditPropertyEdittextDateofsold.setText(FormatUtils.formatDate(mEditProperty.getDateOfSale()));

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
        mSharedPreferences = getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);
        mDevise = mSharedPreferences.getString(Constants.PREF_CURRENCY_KEY, "ERROR_CURRENCY");

        int positionDevise = 0;
        for (int i = 0; i < Constants.LIST_OF_DEVISES_ISO.length; i++){
            if (mDevise.equals(Constants.LIST_OF_DEVISES_ISO[i])){
                positionDevise = i;
                break;
            }
        }

        binding.activityEditPropertySpinnerDevise.setSelection(positionDevise);
    }

    // === Process ===

    private boolean createCompleteProperty(){
        //todo: faire une creation complete

        if (mAddress != null){
            // Créer l'address
            long addressId = mViewModel.createAddress(mAddress);

            // Créer la Property
            long propertyId = mViewModel.createProperty(createProperty(addressId));

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

    private Property createProperty(long addressId) {

        //todo: obliger la completion des champs
        String mainPictureUrl = mViewModel.propertyPictureListMutableLiveData.getValue().get(0).getUrlPicture();

        int propertyType = (int) binding.activityEditPropertySpinnerPropertytype.getSelectedItemId();
        double price = Utils.convertDeviseToDollar(Double.parseDouble(binding.activityEditPropertyEdittextPrice.getText().toString()), Constants.LIST_OF_DEVISES_ISO[binding.activityEditPropertySpinnerDevise.getSelectedItemPosition()]);
        float area = Float.parseFloat(binding.activityEditPropertyEdittextArea.getText().toString());
        int nbOfRooms = Integer.parseInt(binding.activityEditPropertyEdittextNbofrooms.getText().toString());
        int nbOfBedRooms = Integer.parseInt(binding.activityEditPropertyEdittextNbofbedrooms.getText().toString());
        String description = binding.activityEditPropertyEdittextDescription.getText().toString();
        double pricePerSquareMeter = price / area;
        float rate = ScriptsStats.getRateProperty(mViewModel, pricePerSquareMeter, mAddress.getCity());
        Date date = FormatUtils.formatStringFormattedToDate(binding.activityEditPropertyEdittextDateofsold.getText().toString());
        Date dateOfSale = binding.activityEditPropertySwitchIssold.isChecked() ? date : null;

        return new Property(0,propertyType,price, pricePerSquareMeter, area,nbOfRooms,nbOfBedRooms,description,
                addressId, rate, mAddress.getCity(), mainPictureUrl, getCurrentAgentId() , dateOfSale, date, date);
    }

    // === Generators ===

    private void updateUIWithPhotos(List<Photo> photos) {

        if (mEditListPhoto == null){
            mEditListPhoto = photos;
        }

        mViewModel.propertyPictureListMutableLiveData.setValue(photos);

        Log.d("TAG", "updateUIWithPhotos: "); // todo TEST : a supprimer
    }

    private void updateUIWithAddress(Address address) {
        binding.activityEditPropertyEdittextAddress.setText(address.getCompleteAddress());

        if (mAddress == null){
            mAddress = address;
        }
    }

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