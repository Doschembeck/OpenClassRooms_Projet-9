package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.model.Devise;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String PREF_SHARED_KEY = "real_estate_manager_shared_preferences";
    public static final String PREF_CURRENCY_KEY = "currency";
    public static final String PREF_AGENT_ID_LOGGED_KEY = "agent_id";
    public static final String PREF_FAVORITES_PROPERTIES_KEY = "favorites_properties";

    public static final int PREF_CURRENCY_DEFVALUE = 0;

    public static final int LAUNCH_PARAMETER_ACTIVITY = 932;
    public static final int LAUNCH_DETAILS_ACTIVITY = 933;
    public static final int AUTOCOMPLETE_REQUEST_CODE = 934;

    // === DEVISES ===
    public static Devise usd = new Devise("Dollar des États-Unis", "USD", "$", 1);
    public static Devise eur = new Devise("Euro", "EUR", "€", 0.822385);
    public static Devise gbp = new Devise("Livre sterling", "GBP", "£", 0.741245);
    public static Devise jpy = new Devise("Yen", "JPY", "¥", 104.258);
    public static Devise krw = new Devise("Won sud-coréen", "KRW", "₩", 1097.075);
    public static Devise chf = new Devise("Franc suisse", "CHF", "CHF", 0.89057);
    public static final Devise[] LIST_OF_DEVISES = {usd, eur, gbp, jpy, krw, chf};

    // PropertyType
    public static final String[] ListPropertyType = {"Maison", "Appartement", "Loft", "Manoir", "Chateau"};

    public static String[] arrayOrderBy = { "Prix", "Pièces", "Chambres", "Surface", "Date de disponnibilité", "Date de vente"};
    public enum OrderBy { PRICE, NB_OF_ROOMS, NB_OF_BEDROOMS, AREA, MARKETING_DATE, DATE_OF_SALE }
    public enum SortDirection{ ASCENDANT, DESCENDANT }

}
