package com.openclassrooms.realestatemanager;

import android.os.AsyncTask;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bumptech.glide.util.Util;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetAddress;

import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void testInternet(){
        assertTrue(Utils.isInternetAvailable());
    }

}
