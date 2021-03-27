package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.model.Devise;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class  FormatUtils {

    private static SimpleDateFormat getSimpleFormatDate(){
        return new SimpleDateFormat("dd/MM/yyyy");
    }

    public static String formatPOIName(String poiName){

        String formattedString = removeAllSpaceChar(poiName);
        formattedString = formattedString.substring(0, 1).toUpperCase() + formattedString.substring(1);

        return formattedString;
    }

    public static String formatEditTextWithDevise(double amountInDollars, Devise devise){
        double amountInDevise = devise.convertDollarToDevise(amountInDollars);

        return String.format("%s %s", formatNumberToString((long) amountInDevise), devise.getSymbole());
    }

    public static String formatEditTextWithNotDevise(double amountInDollars, Devise devise){
        double amountInDevise = devise.convertDollarToDevise(amountInDollars);

        return removeAllSpaceChar(formatNumberToString((long) amountInDevise));
    }

    private static String formatNumberToString(double number){
        return NumberFormat.getInstance(Locale.FRANCE).format(number);
    }

    public static String removeAllSpaceChar(String str){
        return str.replaceAll("\\s+","");
    }

    public static double formatNumber(String number){
        return Double.parseDouble(removeAllSpaceChar(number));
    }

    public static String formatDate(Date date){
        return getSimpleFormatDate().format(date);
    }

    public static Date formatStringFormattedToDate(String date) {
        try {
            return getSimpleFormatDate().parse(date);
        } catch (ParseException e) { e.printStackTrace(); }

        return new Date();
    }

}
