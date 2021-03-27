package com.openclassrooms.realestatemanager.utils;

public class SimulatorUtils {

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

}
