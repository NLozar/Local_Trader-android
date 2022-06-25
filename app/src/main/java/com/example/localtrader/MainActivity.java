package com.example.localtrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        class RunnableApiCall implements Runnable {
            private final Context ctx;
            private API api;
            private final Activity callerActivity;
            private final SwipeRefreshLayout swipeRefreshLayout;

            public RunnableApiCall(Context ctx, Activity callerActivity, SwipeRefreshLayout swipeRefreshLayout) {
                this.ctx = ctx;
                this.callerActivity = callerActivity;
                this.swipeRefreshLayout = swipeRefreshLayout;
                try {
                    RequestHandler requestHandler = new RequestHandler(this.ctx, this.ctx.getResources().getString(R.string.base_api_url));
                    this.api = new API(this.callerActivity, requestHandler);
                } catch (IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
                    Log.e(this.ctx.getClass().getSimpleName(), "RequestHandler or API creation creation crapped the bed");
                    e.printStackTrace();
                }
            }

            @NonNull
            @Override
            public String toString() {
                return "RunnableApiCall{" +
                        "ctx=" + this.ctx +
                        ", api=" + this.api +
                        ", callerActivity=" + this.callerActivity +
                        '}';
            }

            @Override
            public void run() {
                Object responseObj = this.api.getAllItems();
                if (!responseObj.getClass().equals(String.class)) {
                    AllItemsViewAdapter allItemsViewAdapter = new AllItemsViewAdapter(this.ctx, (ArrayList<AllItemsViewEntry>) responseObj);
                    ListView listView = findViewById(R.id.itemsList);

                    class RunnableAllItemsViewAdapterSetter implements Runnable {
                        private final ListView listView;
                        private final AllItemsViewAdapter adapter;
                        private final SwipeRefreshLayout swipeRefreshLayout;

                        public RunnableAllItemsViewAdapterSetter(ListView listView, AllItemsViewAdapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
                            this.listView = listView;
                            this.adapter = adapter;
                            this.swipeRefreshLayout = swipeRefreshLayout;
                        }

                        @Override
                        public void run() {
                            this.listView.setAdapter(this.adapter);
                            this.swipeRefreshLayout.setRefreshing(false);

                            class ItemListener implements AdapterView.OnItemClickListener {
                                private final AllItemsViewAdapter adapter;

                                public ItemListener(AllItemsViewAdapter adapter) {
                                    this.adapter = adapter;
                                }

                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String uuid = this.adapter.getUuid(i);
                                    Log.i(this.getClass().getSimpleName(), "Item got clicked: " + uuid);

                                    class RunnableDetailsApiCall implements Runnable {
                                        private final Context ctx;
                                        private final API api;
                                        private final String uuid;

                                        public RunnableDetailsApiCall(Context ctx, API api, String uuid) {
                                            this.ctx = ctx;
                                            this.api = api;
                                            this.uuid = uuid;
                                        }

                                        @Override
                                        public void run() {
                                            Object resObj = this.api.getItemDetails(this.uuid);
                                            if (!resObj.getClass().equals(String.class)) {
                                                Intent intentDetails = new Intent(this.ctx, DetailsActivity.class); // Risky AF. ctx comes from outside of the class
                                                intentDetails.putExtra("item_data", (Parcelable) resObj);
                                                startActivity(intentDetails);
                                            } else {
                                                RunnableToast requestFailureToast = new RunnableToast(this.ctx, R.string.request_failure, Toast.LENGTH_LONG);
                                                runOnUiThread(requestFailureToast);
                                            }
                                        }
                                    }

                                    RunnableDetailsApiCall runnableDetailsApiCall = new RunnableDetailsApiCall(ctx, api, uuid);
                                    new Thread(runnableDetailsApiCall).start();
                                }
                            }

                            ItemListener itemListener = new ItemListener(this.adapter);
                            this.listView.setOnItemClickListener(itemListener);
                        }
                    }

                    RunnableAllItemsViewAdapterSetter runnableAllItemsViewAdapterSetter = new RunnableAllItemsViewAdapterSetter(listView, allItemsViewAdapter, this.swipeRefreshLayout);
                    runOnUiThread(runnableAllItemsViewAdapterSetter);
                } else {
                    RunnableToast runnableToastNetErr = new RunnableToast(this.ctx, R.string.network_error, Toast.LENGTH_LONG);
                    runOnUiThread(runnableToastNetErr);
                }
            }
        }

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.itemsListRefresh);
        RunnableApiCall runnableApiCall = new RunnableApiCall(this, this, swipeRefreshLayout);
        new Thread(runnableApiCall).start();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(this.getClass().getSimpleName(), "Refresh initiated.");
                new Thread(runnableApiCall).start();
            }
        });
    }
}