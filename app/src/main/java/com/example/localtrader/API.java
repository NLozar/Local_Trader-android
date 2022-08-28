package com.example.localtrader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
            return this.requestHandler.attemptLogin(this.username, this.password);
        }
    }

    private static class RegisterUserCallable implements Callable<Object> {

        private final RequestHandler requestHandler;
        private final String username;
        private final String password;

        private RegisterUserCallable(RequestHandler requestHandler, String username, String password) {
            this.requestHandler = requestHandler;
            this.username = username;
            this.password = password;
        }

        @Override
        public Object call() throws Exception {
            return this.requestHandler.registerUser(this.username, this.password);
        }
    }

    private static class PostItemCallable implements Callable<Object> {

        private final RequestHandler requestHandler;
        private final HashMap<String, String> details;

        public PostItemCallable(RequestHandler requestHandler, HashMap<String, String> details) {
            this.requestHandler = requestHandler;
            this.details = details;
        }

        @Override
        public Object call() throws Exception {
            return this.requestHandler.postItem(details);
        }
    }

    private static class EditProfileCallable implements Callable<Object> {

        private final RequestHandler requestHandler;
        private final HashMap<String, String> profileChangeDetails;

        public EditProfileCallable(RequestHandler requestHandler, HashMap<String, String> profileChangeDetails) {
            this.requestHandler = requestHandler;
            this.profileChangeDetails = profileChangeDetails;
        }

        @Override
        public Object call() throws Exception {
            return this.requestHandler.editProfile(this.profileChangeDetails);
        }
    }

    private static class EditItemCallable implements Callable<Object> {

        private final RequestHandler requestHandler;
        private final ItemDetailsDataHolder data;

        public EditItemCallable(RequestHandler requestHandler, ItemDetailsDataHolder data) {
            this.requestHandler = requestHandler;
            this.data = data;
        }

        @Override
        public Object call() throws Exception {
            return this.requestHandler.editItem(this.data);
        }
    }

    private static class DeleteItemCallable implements Callable<Object> {

        private final RequestHandler requestHandler;
        private final String uuid;

        public DeleteItemCallable(RequestHandler requestHandler, String uuid) {
            this.requestHandler = requestHandler;
            this.uuid = uuid;
        }

        @Override
        public Object call() throws Exception {
            return this.requestHandler.deleteItem(this.uuid);
        }
    }

    private static class DeleteUserCallable implements Callable<Object> {

        private final RequestHandler requestHandler;
        private final String password;

        public DeleteUserCallable(RequestHandler requestHandler, String password) {
            this.requestHandler = requestHandler;
            this.password = password;
        }

        @Override
        public Object call() throws Exception {
            return requestHandler.deleteUser(this.password);
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

    public Object registerUser(String username, String password) {
        return this.call(new RegisterUserCallable(this.requestHandler, username, password));
    }

    public Object postItem(HashMap<String, String> details) {
        return this.call(new PostItemCallable(this.requestHandler, details));
    }

    public Object editProfile(HashMap<String, String> profileChangeDetails) {
        return this.call(new EditProfileCallable(this.requestHandler, profileChangeDetails));
    }

    public Object editItem(ItemDetailsDataHolder data) {
        return this.call(new EditItemCallable(this.requestHandler, data));
    }

    public Object deleteItem(String uuid) {
        return this.call(new DeleteItemCallable(this.requestHandler, uuid));
    }

    public Object deleteUser(String password) {
        return this.call(new DeleteUserCallable(this.requestHandler, password));
    }
}
