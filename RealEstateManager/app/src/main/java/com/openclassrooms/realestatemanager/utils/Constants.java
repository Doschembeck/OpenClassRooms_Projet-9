package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.model.Devise;

import java.util.Arrays;

public class Constants {

    public static final String PREF_SHARED_KEY = "real_estate_manager_shared_preferences";
    public static final String PREF_CURRENCY_KEY = "currency";
    public static final String PREF_AGENT_ID_LOGGED_KEY = "agent_id";
    public static final String PREF_DEFVALUE_TEST = "65GD46F46R54968R66sG7IHU79867YEHZ615zOIZ79GBONOGINkhoedugdjbkz5498454686C848656SCX";

    // All rate of change is Dollar US to devise === Dollar US | USD | $ | 1 ===
    // On les identifies grace a l'ISO qui est unique pour chaque devise
    public static final String DEVISE_USD_ISO = "USD";
    public static final String DEVISE_EUR_ISO = "EUR";
    public static final String DEVISE_GBP_ISO = "GBP";
    public static final String DEVISE_JPY_ISO = "JPY";
    public static final String DEVISE_KRW_ISO = "KRW";
    public static final String DEVISE_CHF_ISO = "CHF";

    public static final String DEVISE_USD_NAME ="Dollar des États-Unis";
    public static final String DEVISE_EUR_NAME = "Euro";
    public static final String DEVISE_GBP_NAME = "Livre sterling";
    public static final String DEVISE_JPY_NAME = "Yen";
    public static final String DEVISE_KRW_NAME = "Won sud-coréen";
    public static final String DEVISE_CHF_NAME = "Franc suisse";

    public static final String DEVISE_USD_SYMBOL = "$";
    public static final String DEVISE_EUR_SYMBOL = "€";
    public static final String DEVISE_GBP_SYMBOL = "£";
    public static final String DEVISE_JPY_SYMBOL = "¥";
    public static final String DEVISE_KRW_SYMBOL = "₩";
    public static final String DEVISE_CHF_SYMBOL = "CHF";

    //todo: Voir pour mettre a jour les valeurs selon le cours actuel
    public static final double DEVISE_USD_RATE_CHANGE_FROM_USD = 1;
    public static final double DEVISE_EUR_RATE_CHANGE_FROM_USD = 0.822385;
    public static final double DEVISE_GBP_RATE_CHANGE_FROM_USD = 0.741245;
    public static final double DEVISE_JPY_RATE_CHANGE_FROM_USD = 104.258;
    public static final double DEVISE_KRW_RATE_CHANGE_FROM_USD = 1097.075;
    public static final double DEVISE_CHF_RATE_CHANGE_FROM_USD = 0.89057;

    public static final String[] LIST_OF_DEVISES_ISO = {DEVISE_USD_ISO, DEVISE_EUR_ISO, DEVISE_GBP_ISO, DEVISE_JPY_ISO, DEVISE_KRW_ISO, DEVISE_CHF_ISO};
    public static final String[] LIST_OF_DEVISES_NAME = {DEVISE_USD_NAME, DEVISE_EUR_NAME, DEVISE_GBP_NAME, DEVISE_JPY_NAME, DEVISE_KRW_NAME, DEVISE_CHF_NAME};
    public static final String[] LIST_OF_DEVISES_SYMBOL = {DEVISE_USD_SYMBOL, DEVISE_EUR_SYMBOL, DEVISE_GBP_SYMBOL, DEVISE_JPY_SYMBOL, DEVISE_KRW_SYMBOL, DEVISE_CHF_SYMBOL};
    public static final Double[] LIST_OF_DEVISES_RATE_CHANGE_FROM_USD = {DEVISE_USD_RATE_CHANGE_FROM_USD, DEVISE_EUR_RATE_CHANGE_FROM_USD, DEVISE_GBP_RATE_CHANGE_FROM_USD, DEVISE_JPY_RATE_CHANGE_FROM_USD, DEVISE_KRW_RATE_CHANGE_FROM_USD, DEVISE_CHF_RATE_CHANGE_FROM_USD};


    // PropertyType
    public static final String[] ListPropertyType = {"Maison", "Appartement", "Loft", "Manoir", "Chateau"};
    public static final String[] ListNearbyPOI = {"Ecole maternelle", "Ecole primaire", "Collège", "Lycée", "Université", "Aeroport", "Gare",  "Bus", "Tramway", "Commerces", "Parc"};


}
