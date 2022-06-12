package com.example.localtrader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fasterxml.jackson.databind.JsonNode;

public class API {

    private final Activity callerActivity;
    private final RequestHandler requestHandler;

    public API(Activity callerActivity, RequestHandler requestHandler) {
        this.callerActivity = callerActivity;
        this.requestHandler = requestHandler;
    }

    public String execute(Object responseBuffer) {
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
                JsonNode resJn = this.requestHandler.sendGetRequest();
                responseBuffer = DataHandler.jsonNodeToAllItemsViewEntryArrayList(resJn);
                return "Connection successful";
            } catch (Exception e) {
                return callerActivity.getResources().getString(R.string.network_error);
            }
        }
        else{
            return callerActivity.getResources().getString(R.string.network_error);
        }
    }
}
