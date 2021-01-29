package com.openclassrooms.realestatemanager.utils;

import android.database.Cursor;
import android.util.Log;

import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.HashMap;
import java.util.List;

public class ScriptsStats {

    public static float getRateProperty(PropertyViewModel viewModel, Double propertyPricePerSquareMeter, String propertyCity){
        float rate = 1;

        // Recupere la liste des city avec la moyenne du prix par mettre carré
        HashMap<String, Double> hashMap = calculateAverage(viewModel);

        // Calcul le ratio (rate)
        if (hashMap.containsKey(propertyCity)){
            rate = (float) (propertyPricePerSquareMeter / hashMap.get(propertyCity));
        }

        return rate;
    }

    public static void scriptStatsAllRate(PropertyViewModel viewModel){

        // Recupere toutes les properties
        List<Property> propertyList = viewModel.getAllProperty();

        // Recupere la liste des city avec la moyenne du prix par mettre carré
        HashMap<String, Double> hashMap = calculateAverage(viewModel);

        // Met a jour toutes les properties
        updateAllRateProperty(propertyList, hashMap, viewModel);
    }

    private static HashMap<String, Double> calculateAverage(PropertyViewModel viewModel) {

        // Recupere la liste des city avec la moyenne du prix par mettre carré
        Cursor cursor = viewModel.getListCityWithAveragePricePerSquareMeter();

        // convertie le cursor en HashMap
        return convertCursorToHashMap(cursor);
    }

    private static HashMap<String, Double> convertCursorToHashMap(Cursor cursor){
        HashMap<String, Double> hashMap = new HashMap<>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
            Double averagePricePerSquareMeter = cursor.getDouble(cursor.getColumnIndexOrThrow("AVG(pricePerSquareMeter)"));

            hashMap.put(city, averagePricePerSquareMeter);

            cursor.moveToNext();
        }

        Log.d("TAG1", "convertCursorToHashMap is finished : " + hashMap);

        return hashMap;
    }


    private static void updateAllRateProperty(List<Property> propertyList, HashMap<String, Double> listCityWithAverage, PropertyViewModel viewModel){

        // Update all properties
        for (int i = 0; i < propertyList.size(); i++){
            Property property = propertyList.get(i);

            // Calcul le ratio (rate)
            if (listCityWithAverage.containsKey(property.getCity())){

                float rate = (float) (property.getPricePerSquareMeter() / listCityWithAverage.get(property.getCity()));
                property.setRate(rate);
                viewModel.updateProperty(property);
            }
        }
    }

}
