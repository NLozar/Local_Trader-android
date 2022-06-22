package com.example.localtrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle receivedIntentBundle = getIntent().getExtras();
        String uuid = receivedIntentBundle.getString("uuid");
        Log.i(this.getClass().getSimpleName(), "Received UUID from intent: " + uuid);

        class RunnableApiCall implements Runnable {
            private final Context ctx;
            private API api;
            private final Activity callerActivity;
            private final String uuid;

            public RunnableApiCall(Context ctx, Activity callerActivity, String uuid) {
                this.ctx = ctx;
                this.callerActivity = callerActivity;
                this.uuid = uuid;
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
                Object responseObj = this.api.getItemDetails(this.uuid);
                //TODO
            }
        }

        RunnableApiCall runnableApiCall = new RunnableApiCall(this, this, uuid);
        new Thread(runnableApiCall).start();
    }
}