package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

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

    private ArrayList<AllItemsViewEntry> allItemsViewEntries;
    ListView itemsList;
    API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            RequestHandler requestHandler = new RequestHandler(this, getResources().getString(R.string.base_api_url));
            this.api = new API(this, requestHandler);
        } catch (IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            Log.e(this.getClass().getSimpleName(), "RequestHandler or API creation creation crapped the bed");
            e.printStackTrace();
        }
        Object apiRes = api.execute(this.allItemsViewEntries);
        if (apiRes.getClass() == this.allItemsViewEntries.getClass()) {
            this.allItemsViewEntries = (ArrayList<AllItemsViewEntry>) apiRes;
        } else {
            try {
                Toast toast = Toast.makeText(this, (String) apiRes, Toast.LENGTH_LONG);
                toast.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.itemsList = findViewById(R.id.itemsList);
        AllItemsViewAdapter allItemsViewAdapter = new AllItemsViewAdapter(this, this.allItemsViewEntries);
        this.itemsList.setAdapter(allItemsViewAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
    }
}