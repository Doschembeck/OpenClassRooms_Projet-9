package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.openclassrooms.realestatemanager.model.Devise;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    private final static String  TAG = "TAG1";

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);
    }

    public static int getCurrentDeviseId(SharedPreferences sharedPreferences){
        return sharedPreferences.getInt(Constants.PREF_CURRENCY_KEY, Constants.PREF_CURRENCY_DEFVALUE);
    }

    public static Devise getCurrentDevise(SharedPreferences sharedPreferences){
        return Constants.LIST_OF_DEVISES[getCurrentDeviseId(sharedPreferences)];
    }

    public static List<String> getAllNameOfDevises(){
        List<String> listOfName = new ArrayList<>();

        for (Devise devise : Constants.LIST_OF_DEVISES){
            listOfName.add(devise.getName());
        }

        return listOfName;
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        return FormatUtils.formatDate(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param
     * @return
     */
    public static Boolean isInternetAvailable(){

        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static Double convertToDouble(String str){
        if (str.equals("")) return 0.0;
        return Double.parseDouble(str);
    }

    public static int convertToInt(String str){
        if (str.equals("")) return 0;
        return Integer.parseInt(str);
    }

}
