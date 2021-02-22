package com.openclassrooms.realestatemanager.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.Geocoder;
import android.util.Log;
import android.widget.EditText;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityUtils {

    public static Geocoder setupAutoComplete(Context context) {
        Geocoder geocoder = null;

        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyDKEy4YPdOH5ErxxEZ0SPFBUF4JNGf83kw"); //todo: Recuperer la clé depuis les ressources
            geocoder = new Geocoder(context, Locale.getDefault());
        }

        return geocoder;
    }

    public static void createDatePickerDialog(Context context, EditText editText){

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, dayOfMonth) -> {
            Date date1 = FormatUtils.formatStringFormattedToDate(dayOfMonth + "/" + (month + 1) + "/" + year);
            editText.setText(FormatUtils.formatDate(date1));
        };

        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(context,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

}