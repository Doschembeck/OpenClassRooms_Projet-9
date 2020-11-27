package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void calculateCostTotalTest() {
        double expected = 1190.552;
        double actual = Utils.calculateCostTotal(10000, 0.0296, 0.0084, 60);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void calculateMonthlyPaymentBankOnlyTest() {
        double expected = 12.842;
        double actual = Utils.calculateMonthlyPaymentInterestBankOnly(10000,  0.0296, 60);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void calculateMonthlyPaymentInsuranceOnlyTest() {
        double expected = 7;
        double actual = Utils.calculateMonthlyPaymentInsuranceOnly(10000, 0.0084);

        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void calculateMonthlyPaymentWithNotRateTest() {
        double expected = 166.666;
        double actual = Utils.calculateMonthlyPaymentWithNotRate(10000,  60);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void calculateMonthlyPaymentTotalTest() {
        double expected = 186.51;
        double actual = Utils.calculateMonthlyPaymentTotal(10000, 0.0296, 0.0084, 60);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void calculateMonthlyPaymentBankTest(){
        double expected = 179.51;
        double actual = Utils.calculateMonthlyPaymentBank(10000, 0.0296, 60);

        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void convertDollarToEuroTest() throws Exception {
        int expected = 543;
        int actual = Utils.convertDollarToEuro(645);

        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void convertEuroToDollarTest() throws Exception {
        int expected = 766;
        int actual = Utils.convertEuroToDollar(645);

        assertEquals(expected, actual,0.001);
    }

    @Test
    public void getTodayDateTest() {
        String date = Utils.getTodayDate();
        String[] dateUnformated = date.split("/");

        assertTrue(dateUnformated[0].length() == 2);
        assertTrue(dateUnformated[1].length() == 2);
        assertTrue(dateUnformated[2].length() == 4);

    }
}