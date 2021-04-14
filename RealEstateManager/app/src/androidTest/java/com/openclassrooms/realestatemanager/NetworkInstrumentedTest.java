package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowNetworkInfo;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NetworkInstrumentedTest {
    private ConnectivityManager connectivityManager;
    private ShadowNetworkInfo shadowOfActiveNetworkInfo;

    @Before
    public void setUp() throws Exception {
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        shadowOfActiveNetworkInfo = shadowOf(connectivityManager.getActiveNetworkInfo());
    }

    @Test
    public void getActiveNetworkInfo_shouldInitializeItself() {
        assertNotNull(connectivityManager.getActiveNetworkInfo());
    }

    @Test
    public void getActiveNetworkInfo_shouldReturnTrueCorrectly() {
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED);
        assertTrue(connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting());
        assertTrue(connectivityManager.getActiveNetworkInfo().isConnected());

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTING);
        assertTrue(connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting());
        assertFalse(connectivityManager.getActiveNetworkInfo().isConnected());

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED);
        assertFalse(connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting());
        assertFalse(connectivityManager.getActiveNetworkInfo().isConnected());
    }

    //todo: depuis l'API 28 il n'est plus possible de bouchonné la connection pour faire les tests
    // https://stackoverflow.com/questions/58006340/enable-disable-wifi-on-android-29

    @Test
    public void testInternet(){
        assertTrue(Utils.isInternetAvailable());
    }

}
