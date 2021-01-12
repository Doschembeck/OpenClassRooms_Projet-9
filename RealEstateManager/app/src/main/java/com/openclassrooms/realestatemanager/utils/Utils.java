package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.openclassrooms.realestatemanager.model.Devise;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

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

    private static String getSymbolDevise(String deviseISO){

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

    public static String formatEditTextWithDevise(double amountInDollars, String deviseISO){
        double amountInDevise = convertDollarToDevise(amountInDollars, deviseISO);

        return String.format("%s %s", NumberFormat.getInstance(Locale.FRANCE).format((long) amountInDevise), getSymbolDevise(deviseISO));
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
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

    //   region --- LoanSimulator ---

    // Retourne le montant mensuel credit + banque
    public static double calculateMonthlyPaymentBank(double amountLoan, double annualRateBank, int nbOfMonthlyPayment){
        // Source : https://www.younited-credit.com/projets/pret-personnel/calculs/calculer-remboursement-pret
        double J = annualRateBank / 12; // Taux d'interet effectif = taux annuel / 12

        return amountLoan * (J / (1 - Math.pow(1 + J, - nbOfMonthlyPayment))); // Mensualité
    }

    // Retroune le montant mensuel de la banque uniquement
    public static double calculateMonthlyPaymentInterestBankOnly(double amountLoan, double annualRateBank, int nbOfMonthlyPayment){
        return calculateMonthlyPaymentBank(amountLoan, annualRateBank, nbOfMonthlyPayment) - calculateMonthlyPaymentWithNotRate(amountLoan, nbOfMonthlyPayment);
    }

    // Retourne le montant mensuel de l'assurance uniquement
    public static double calculateMonthlyPaymentInsuranceOnly(double amountLoan, double annualRateInsurance){
        return amountLoan * (annualRateInsurance / 12);
    }

    // Retourne le montant credit uniquement (sans taux)
    public static double calculateMonthlyPaymentWithNotRate(double amountLoan, int nbOfMonthlyPayment){
        return amountLoan / nbOfMonthlyPayment;
    }

    // Retourne le montant mensuel total (credit + banque + assurance)
    public static double calculateMonthlyPaymentTotal(double amountLoan, double annualRateBank, double annualRateInsurance, int nbOfMonthlyPayment){
        double monthlyPaymentBank = calculateMonthlyPaymentBank(amountLoan, annualRateBank, nbOfMonthlyPayment);
        double monthlyPaymentInsurance = calculateMonthlyPaymentInsuranceOnly(amountLoan, annualRateInsurance);

        return monthlyPaymentBank + monthlyPaymentInsurance;
    }


    // Calculate cout total
    public static double calculateCostTotal(double amountLoan, double annualRateBank, double annualRateInsurance, int nbOfMonthlyPayment){
        return (calculateMonthlyPaymentInterestBankOnly(amountLoan, annualRateBank, nbOfMonthlyPayment)
                + calculateMonthlyPaymentInsuranceOnly(amountLoan, annualRateInsurance))
                * nbOfMonthlyPayment;
    }

    //endregion
}
