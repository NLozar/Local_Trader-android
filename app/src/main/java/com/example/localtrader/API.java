package com.example.localtrader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class API {

    private final Activity callerActivity;
    private final RequestHandler requestHandler;
    private final AllItemsCallable allItemsCallable;

    private static class AllItemsCallable implements Callable<Object> {

        private final RequestHandler requestHandler;

        public AllItemsCallable(RequestHandler requestHandler) {
            this.requestHandler = requestHandler;
        }

        @Override
        public ArrayList<AllItemsViewEntry> call() throws Exception {
            JsonNode resJn = this.requestHandler.getAllItems();
            return DataHandler.jsonNodeToAllItemsViewEntryArrayList(resJn);
        }
    }

    private static class ItemDetailsCallable implements Callable<Object> {

        private final RequestHandler requestHandler;
        private final String uuid;

        public ItemDetailsCallable(RequestHandler requestHandler, String uuid) {
            this.requestHandler = requestHandler;
            this.uuid = uuid;
        }

        @Override
        public ItemDetailsDataHolder call() throws IOException {
            JsonNode resJn = this.requestHandler.getItemDetails(this.uuid);
            return DataHandler.jsonNodeToItemDetailsDataHolder(resJn);
        }
    }

    private static class LoginCallable implements Callable<Object> {

        private final RequestHandler requestHandler;
        private final String username;
        private final String password;

        private LoginCallable(RequestHandler requestHandler, String username, String password) {
            this.requestHandler = requestHandler;
            this.username = username;
            this.password = password;
        }

        @Override
        public Object call() throws Exception {
            return DataHandler.jwtToString(this.requestHandler.attemptLogin(this.username, this.password));
        }
    }

    public API(Activity callerActivity, RequestHandler requestHandler) {
        this.callerActivity = callerActivity;
        this.requestHandler = requestHandler;
        this.allItemsCallable = new AllItemsCallable(requestHandler);
        Log.i(this.getClass().getSimpleName(), "Constructor called");
    }

    private Object call(Callable<Object> callSpec) {
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
                return callSpec.call(); // SUCCESS
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "Request failed.");
                e.printStackTrace();
                return callerActivity.getResources().getString(R.string.request_failure);
            }
        }
        else{
            Log.e(this.getClass().getSimpleName(), "Network not connected");
            return callerActivity.getResources().getString(R.string.network_error);
        }
    }

    public Object getAllItems() {
        return this.call(this.allItemsCallable);
    }

    public Object getItemDetails(String uuid) {
        ItemDetailsCallable itemDetailsCallable = new ItemDetailsCallable(this.requestHandler, uuid);
        return this.call(itemDetailsCallable);
    }

    public Object attemptLogin(String username, String password) {
        return this.call(new LoginCallable(this.requestHandler, username, password));
    }
}
