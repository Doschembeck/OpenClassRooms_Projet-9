package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.model.Devise;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.SimulatorUtils;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class SimulatorUnitTest {

    @Test
    public void calculateCostTotalTest() {
        double expected = 1190.552;
        double actual = SimulatorUtils.calculateCostTotal(10000, 0.0296, 0.0084, 60);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void calculateMonthlyPaymentBankOnlyTest() {
        double expected = 12.842;
        double actual = SimulatorUtils.calculateMonthlyPaymentInterestBankOnly(10000,  0.0296, 60);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void calculateMonthlyPaymentInsuranceOnlyTest() {
        double expected = 7;
        double actual = SimulatorUtils.calculateMonthlyPaymentInsuranceOnly(10000, 0.0084);

        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void calculateMonthlyPaymentWithNotRateTest() {
        double expected = 166.666;
        double actual = SimulatorUtils.calculateMonthlyPaymentWithNotRate(10000,  60);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void calculateMonthlyPaymentTotalTest() {
        double expected = 186.51;
        double actual = SimulatorUtils.calculateMonthlyPaymentTotal(10000, 0.0296, 0.0084, 60);

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void calculateMonthlyPaymentBankTest(){
        double expected = 179.51;
        double actual = SimulatorUtils.calculateMonthlyPaymentBank(10000, 0.0296, 60);

        assertEquals(expected, actual, 0.01);
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