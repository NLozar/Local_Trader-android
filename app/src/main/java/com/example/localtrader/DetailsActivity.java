package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle receivedIntentBundle = getIntent().getExtras();
        Log.i(this.getClass().getSimpleName(), "Received UUID from intent: " + receivedIntentBundle.getString("uuid"));
    }
}