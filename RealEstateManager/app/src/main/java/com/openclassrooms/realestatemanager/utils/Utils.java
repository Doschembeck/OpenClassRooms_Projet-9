package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    private final static String  TAG = "TAG1";

    public static double convertDollarToDevise(double amountInDollars, String deviseAfterConvert){

        switch (deviseAfterConvert){

            case Constants.DEVISE_EUR_ISO:
                return amountInDollars * Constants.DEVISE_EUR_RATE_CHANGE_FROM_USD;

            case Constants.DEVISE_GBP_ISO:
                return amountInDollars * Constants.DEVISE_GBP_RATE_CHANGE_FROM_USD;

            case Constants.DEVISE_JPY_ISO:
                return amountInDollars * Constants.DEVISE_JPY_RATE_CHANGE_FROM_USD;

            case Constants.DEVISE_KRW_ISO:
                return amountInDollars * Constants.DEVISE_KRW_RATE_CHANGE_FROM_USD;

            case Constants.DEVISE_CHF_ISO:
                return amountInDollars * Constants.DEVISE_CHF_RATE_CHANGE_FROM_USD;

            default:
                return amountInDollars;
        }
    }

    public static double convertDeviseToDollar(double amountInDevise, String deviseBeforeConvert){

        switch (deviseBeforeConvert){

            case Constants.DEVISE_EUR_ISO:
                return (int) (amountInDevise / Constants.DEVISE_EUR_RATE_CHANGE_FROM_USD);

            case Constants.DEVISE_GBP_ISO:
                return (int) (amountInDevise / Constants.DEVISE_GBP_RATE_CHANGE_FROM_USD);

            case Constants.DEVISE_JPY_ISO:
                return (int) (amountInDevise / Constants.DEVISE_JPY_RATE_CHANGE_FROM_USD);

            case Constants.DEVISE_KRW_ISO:
                return (int) (amountInDevise / Constants.DEVISE_KRW_RATE_CHANGE_FROM_USD);

            case Constants.DEVISE_CHF_ISO:
                return (int) (amountInDevise / Constants.DEVISE_CHF_RATE_CHANGE_FROM_USD);

            default:
                return (int) (amountInDevise);
        }
    }

    public static String getSymbolDevise(String deviseISO){

        switch (deviseISO){

            case Constants.DEVISE_EUR_ISO:
                return Constants.DEVISE_EUR_SYMBOL;

            case Constants.DEVISE_GBP_ISO:
                return Constants.DEVISE_GBP_SYMBOL;

            case Constants.DEVISE_JPY_ISO:
                return Constants.DEVISE_JPY_SYMBOL;

            case Constants.DEVISE_KRW_ISO:
                return Constants.DEVISE_KRW_SYMBOL;

            case Constants.DEVISE_CHF_ISO:
                return Constants.DEVISE_CHF_SYMBOL;

            default:
                return Constants.DEVISE_USD_SYMBOL;
        }
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
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){

        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test"); urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500); urlc.connect(); return (urlc.getResponseCode() == 200);
            } catch (IOException e) { Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
        } return false;
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
