package com.example.localtrader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class API {

    private final String url;
    private final Activity callerActivity;
    private final RequestHandler requestHandler;

    public API(String url, Activity callerActivity, RequestHandler requestHandler) {
        this.url = url;
        this.callerActivity = callerActivity;
        this.requestHandler = requestHandler;
    }

    public String execute() {
        ConnectivityManager connMgr = (ConnectivityManager) callerActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        try {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        catch (Exception e){
            return callerActivity.getResources().getString(R.string.network_error);
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                return "TODO";// TODO
                //return requestHandler.sendRequest();
            } catch (Exception e) {
                return callerActivity.getResources().getString(R.string.network_error);
            }
        }
        else{
            return callerActivity.getResources().getString(R.string.network_error);
        }
    }
}
