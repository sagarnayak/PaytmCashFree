package com.sagar.android_projects.paytmcashfree.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sagar on 8/29/2017.
 * Class to check network connection
 */
public class NetworkUtil {

    /**
     * method to check if device is connected to internet.
     * @param context context of activity
     * @return true of connected, false otherwise
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager CManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        return (NInfo != null && NInfo.isConnectedOrConnecting());
    }
}
