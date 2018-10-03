package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class ConnectionDetector {
    private Context _context;

    public ConnectionDetector (Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager ) this._context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        }
        NetworkInfo nInfo = connectivity.getActiveNetworkInfo();
        if (nInfo == null || nInfo.getState() != State.CONNECTED) {
            return false;
        }
        return true;
    }
}
