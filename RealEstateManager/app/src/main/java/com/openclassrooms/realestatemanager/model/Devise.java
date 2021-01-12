package com.openclassrooms.realestatemanager.model;

public class Devise {

    private String name;
    private String iso;
    private String symbole;
    private double rateChangeDollarToDevise;

    public Devise(String name, String iso, String symbole, double rateChangeDollarToDevise) {
        this.name = name;
        this.iso = iso;
        this.symbole = symbole;
        this.rateChangeDollarToDevise = rateChangeDollarToDevise;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getSymbole() {
        return symbole;
    }

    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }

    public double getRateChangeDollarToDevise() {
        return rateChangeDollarToDevise;
    }

    public void setRateChangeDollarToDevise(double rateChangeDollarToDevise) {
        this.rateChangeDollarToDevise = rateChangeDollarToDevise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
