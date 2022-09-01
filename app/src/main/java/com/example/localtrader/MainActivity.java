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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnLogInOut;
    private Button btnMyItems;
    private FloatingActionButton btnPostItem;
    private TextView profileName;

    private void setButtons() {
        if (AppState.userLoggedIn) {
            this.btnMyItems.setVisibility(View.VISIBLE);
            this.btnLogInOut.setText(R.string.log_out);
            this.profileName.setText(AppState.userName);
            this.profileName.setOnClickListener(l -> startActivity(new Intent(this, EditProfileActivity.class)));
            this.btnLogInOut.setOnClickListener(l -> {
                AppState.logUserOut();
                this.btnLogInOut.setText(R.string.log_in);
                this.btnMyItems.setVisibility(View.GONE);
                this.profileName.setText(R.string.logged_out);
                this.profileName.setOnClickListener(null);
                this.btnLogInOut.setOnClickListener(m -> startActivity(new Intent(this, LoginActivity.class)));
                this.btnPostItem.setOnClickListener(m -> startActivity(new Intent(this, LoginActivity.class)));
                Toast.makeText(this, R.string.user_logged_out, Toast.LENGTH_LONG).show();
            });
            this.btnPostItem.setOnClickListener(l -> startActivity(new Intent(this, PostItemActivity.class)));
        } else {
            this.btnMyItems.setVisibility(View.GONE);
            this.btnLogInOut.setText(R.string.log_in);
            this.profileName.setText(R.string.logged_out);
            this.profileName.setOnClickListener(null);
            this.btnLogInOut.setOnClickListener(l -> startActivity(new Intent(this, LoginActivity.class)));
            this.btnPostItem.setOnClickListener(l -> startActivity(new Intent(this, LoginActivity.class)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnLogInOut = findViewById(R.id.btn_logInOut);
        this.btnMyItems = findViewById(R.id.btn_myItems);
        this.btnPostItem = findViewById(R.id.btn_postItem);
        this.profileName = findViewById(R.id.profile_name);
        FloatingActionButton btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(l -> startActivity(new Intent(this, SettingsActivity.class)));

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
                    RequestHandler requestHandler = new RequestHandler(this.ctx);
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
                                                Intent intentDetails = new Intent(this.ctx, DetailsActivity.class);
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
                    RunnableToast runnableToastNetErr = new RunnableToast(this.ctx, R.string.request_failure, Toast.LENGTH_LONG);
                    runOnUiThread(runnableToastNetErr);
                }
            }
        }

        this.setButtons();

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

    @Override
    protected void onResume() {
        super.onResume();
        this.setButtons();
    }
}