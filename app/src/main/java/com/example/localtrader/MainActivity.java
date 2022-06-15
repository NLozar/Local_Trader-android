package com.example.localtrader;

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
            private ListView listView;
            private API api;
            private Activity callerActivity;

            public RunnableApiCall(Context ctx, Activity callerActivity, ListView listView) {
                this.ctx = ctx;
                this.callerActivity = callerActivity;
                this.listView = listView;
                try {
                    RequestHandler requestHandler = new RequestHandler(this.ctx, this.ctx.getResources().getString(R.string.base_api_url));
                    this.api = new API(this.callerActivity, requestHandler);
                } catch (IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
                    Log.e(this.ctx.getClass().getSimpleName(), "RequestHandler or API creation creation crapped the bed");
                    e.printStackTrace();
                }
            }

            @Override
            public String toString() {
                return "RunnableApiCall{" +
                        "ctx=" + ctx +
                        ", listView=" + listView +
                        ", api=" + api +
                        ", callerActivity=" + callerActivity +
                        '}';
            }

            @Override
            public void run() {
                Object responseObj = this.api.execute();
                if (!responseObj.getClass().equals(String.class)) {
                    AllItemsViewAdapter allItemsViewAdapter = new AllItemsViewAdapter(this.ctx, (ArrayList<AllItemsViewEntry>) responseObj);
                    this.listView.setAdapter(allItemsViewAdapter);
                } else {
                    Toast.makeText(this.ctx, R.string.network_error, Toast.LENGTH_LONG).show();
                }
            }
        }

        ListView itemsList = findViewById(R.id.itemsList);
        Runnable runnableApiCall = new RunnableApiCall(this, this, itemsList);
        Log.i(this.getClass().getSimpleName(), "runnableApiCall: " + runnableApiCall);
        new Thread(runnableApiCall).start();
    }
}