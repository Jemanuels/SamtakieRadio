package za.co.samtakie.samtakieradio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RadioConnection {

    public static  boolean haveNetworkConnectionWifi(Context context) {
        boolean haveConnectedWifi = false;


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                }
            }

        }
        return haveConnectedWifi;
    }

    public static boolean haveNetworkConnectionMobile(Context context) {
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {

            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                }
            }
        }
        return haveConnectedMobile;
    }
}