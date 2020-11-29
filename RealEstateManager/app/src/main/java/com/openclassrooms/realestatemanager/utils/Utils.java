package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    private final static String  TAG = "TAG1";

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.84228);
    }

    public static int convertEuroToDollar(int euro){
        return (int) Math.round(euro * 1.187);
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

    public static String formatEditTextWithDevise(double amountLoan, String devise){
        return String.format("%s %s", NumberFormat.getInstance().format(amountLoan), devise);
    }

    //endregion
}
