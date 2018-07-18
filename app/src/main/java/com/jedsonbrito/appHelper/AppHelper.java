package com.jedsonbrito.appHelper;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppHelper {


    public static boolean isConnected(Object systemService) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) systemService;
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                connected = true;
            }
        }
            return connected;
    }
}
