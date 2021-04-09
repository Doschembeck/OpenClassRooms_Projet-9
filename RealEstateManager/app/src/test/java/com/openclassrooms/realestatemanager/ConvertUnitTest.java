package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConvertUnitTest {

    @Test
    public void convertDollarToEuroTest() {
        int expected = 82238;
        int actual = (int) Constants.eur.convertDollarToDevise(100000);

        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void convertEuroToDollarTest() {
        int expected = 121597;
        int actual = (int) Constants.eur.convertDeviseToDollar(100000);

        assertEquals(expected, actual,0.001);
    }

}
