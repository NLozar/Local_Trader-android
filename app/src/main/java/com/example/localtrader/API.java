package com.example.localtrader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;

public class API {

    private final Activity callerActivity;
    private final RequestHandler requestHandler;

    public API(Activity callerActivity, RequestHandler requestHandler) {
        this.callerActivity = callerActivity;
        this.requestHandler = requestHandler;
        Log.i(this.getClass().getSimpleName(), "Constructor called");
    }

    public Object execute() {
        ConnectivityManager connMgr = (ConnectivityManager) callerActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        try {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        catch (Exception e){
            Log.e(this.getClass().getSimpleName(), "Couldn't get active network info.");
            return callerActivity.getResources().getString(R.string.network_error);
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                JsonNode resJn = this.requestHandler.sendGetRequest();
                return DataHandler.jsonNodeToAllItemsViewEntryArrayList(resJn);
                //Log.i(this.getClass().getSimpleName(), "responseBuffer: " + responseBuffer.toString());
                //return "Success";
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Request failed.");
                e.printStackTrace();
                return callerActivity.getResources().getString(R.string.network_error);
            }
        }
        else{
            Log.e(this.getClass().getSimpleName(), "Network not connected");
            return callerActivity.getResources().getString(R.string.network_error);
        }
    }
}
