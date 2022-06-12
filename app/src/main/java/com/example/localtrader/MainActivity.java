package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<AllItemsViewEntry> allItemsViewEntries;
    ListView itemsList;
    API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            RequestHandler requestHandler = new RequestHandler(getResources().getString(R.string.base_api_url));
            this.api = new API(this, requestHandler);
        } catch (IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }
        this.itemsList = findViewById(R.id.itemsList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
    }
}