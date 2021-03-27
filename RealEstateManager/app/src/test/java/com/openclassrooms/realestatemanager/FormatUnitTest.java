package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.FormatUtils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class FormatUnitTest {

    @Test
    public void formatNumberTest() throws Exception {
        double expected = 123456;
        double actual = FormatUtils.formatNumber(" 123 456 ");

        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void formatPOINameTest() throws Exception {
        String expected = "Ecole";
        String actual = FormatUtils.formatPOIName(" e cole ");

        assertEquals(expected, actual);
    }

    @Test
    public void formatEditTextWithDeviseTest() throws Exception {
        String expected = "82 238 €";
        String actual = FormatUtils.formatEditTextWithDevise(100000, Constants.eur);

        assertEquals(expected, actual);
    }

    @Test
    public void formatEditTextWithNotDeviseTest() throws Exception {
        String expected = "82 238";
        String actual = FormatUtils.formatEditTextWithNotDevise(100000, Constants.eur);

        assertEquals(expected, actual);
    }

    @Test
    public void formatStringFormattedToDateTest() throws Exception {
        Date expected = new Date();
        expected.setDate(26);
        expected.setMonth(2);
        expected.setYear(121);
        expected.setHours(0);
        expected.setMinutes(0);
        expected.setSeconds(0);

        Date actual = FormatUtils.formatStringFormattedToDate("26/03/2021");

        assertEquals(expected.getTime(), actual.getTime(), 1);
    }

    @Test
    public void formatDateTest() throws Exception {
        Date date = new Date();
        date.setDate(26);
        date.setMonth(2);
        date.setYear(121);

        String expected = "26/03/2021";
        String actual = FormatUtils.formatDate(date);

        assertEquals(expected, actual);
    }

}
