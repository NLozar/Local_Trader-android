package com.example.localtrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
            private Context ctx;
            private API api;
            private Activity callerActivity;

            public RunnableApiCall(Context ctx, Activity callerActivity) {
                this.ctx = ctx;
                this.callerActivity = callerActivity;
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
                Object responseObj = this.api.execute();
                if (!responseObj.getClass().equals(String.class)) {
                    AllItemsViewAdapter allItemsViewAdapter = new AllItemsViewAdapter(this.ctx, (ArrayList<AllItemsViewEntry>) responseObj);
                    ListView listView = findViewById(R.id.itemsList);

                    class RunnableAllItemsViewAdapterSetter implements Runnable {
                        private ListView listView;
                        private AllItemsViewAdapter adapter;
                        public RunnableAllItemsViewAdapterSetter(ListView listView, AllItemsViewAdapter adapter) {
                            this.listView = listView;
                            this.adapter = adapter;
                        }

                        @Override
                        public void run() {
                            this.listView.setAdapter(this.adapter);
                            class ItemListener implements AdapterView.OnItemClickListener {
                                private AllItemsViewAdapter adapter;

                                public ItemListener(AllItemsViewAdapter adapter) {
                                    this.adapter = adapter;
                                }

                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.i(this.getClass().getSimpleName(), "Item got clicked: " + this.adapter.getUuid(i));
                                    Intent intentDetails = new Intent(ctx, DetailsActivity.class); // Risky AF. ctx comes from outside of the class
                                    intentDetails.putExtra("uuid", this.adapter.getUuid(i));
                                    startActivity(intentDetails);
                                }
                            }
                            ItemListener itemListener = new ItemListener(this.adapter);
                            this.listView.setOnItemClickListener(itemListener);
                        }
                    }
                    RunnableAllItemsViewAdapterSetter runnableAllItemsViewAdapterSetter = new RunnableAllItemsViewAdapterSetter(listView, allItemsViewAdapter);
                    runOnUiThread(runnableAllItemsViewAdapterSetter);
                } else {
                    RunnableToast runnableToastNetErr = new RunnableToast(this.ctx, R.string.network_error, Toast.LENGTH_LONG);
                    runOnUiThread(runnableToastNetErr);
                }
            }
        }

        Runnable runnableApiCall = new RunnableApiCall(this, this);
        new Thread(runnableApiCall).start();
    }
}