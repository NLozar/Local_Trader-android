package com.example.localtrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
                        "ctx=" + ctx +
                        ", api=" + api +
                        ", callerActivity=" + callerActivity +
                        '}';
            }

            @Override
            public void run() {
                Object responseObj = this.api.execute();
                if (!responseObj.getClass().equals(String.class)) {
                    AllItemsViewAdapter allItemsViewAdapter = new AllItemsViewAdapter(this.ctx, (ArrayList<AllItemsViewEntry>) responseObj);
                    ListView listView = findViewById(R.id.itemsList);

                    class SetAdapterOnUiThread implements Runnable {
                        private ListView listView;
                        private AllItemsViewAdapter adapter;
                        public SetAdapterOnUiThread(ListView listView, AllItemsViewAdapter adapter) {
                            this.listView = listView;
                            this.adapter = adapter;
                        }

                        @Override
                        public void run() {
                            this.listView.setAdapter(this.adapter);
                        }
                    }
                    SetAdapterOnUiThread setAdapterOnUiThread = new SetAdapterOnUiThread(listView, allItemsViewAdapter);
                    runOnUiThread(setAdapterOnUiThread);
                } else {
                    class RunnableToast implements Runnable {
                        private Context ctx;
                        private int redId;
                        private int duration;

                        public RunnableToast(Context ctx, int resId, int duration) {
                            this.ctx = ctx;
                            this.redId = resId;
                            this.duration = duration;
                        }
                        @Override
                        public void run() {
                            Toast.makeText(this.ctx, this.redId, this.duration).show();
                        }
                    }
                    RunnableToast runnableToast = new RunnableToast(this.ctx, R.string.network_error, Toast.LENGTH_LONG);
                    runOnUiThread(runnableToast);
                }
            }
        }

        Runnable runnableApiCall = new RunnableApiCall(this, this);
        Log.i(this.getClass().getSimpleName(), "runnableApiCall: " + runnableApiCall);
        new Thread(runnableApiCall).start();
    }
}