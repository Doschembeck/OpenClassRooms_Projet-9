package com.openclassrooms.realestatemanager.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.Property;

import java.util.List;

public class ListViewViewModel extends ViewModel {

    public MutableLiveData<List<Property>> mListPropertyMutableLiveData = new MutableLiveData<>();
}

