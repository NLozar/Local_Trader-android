package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private EditText etBaseApiUrl;

    private void confirmSettings() {
        AppState.setBaseApiUrl(this.etBaseApiUrl.getText().toString());
        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.etBaseApiUrl = findViewById(R.id.et_settings_base_api);
        Button btnConfirm = findViewById(R.id.btn_confirm_settings);
        this.etBaseApiUrl.setText(AppState.baseApiUrl);
        btnConfirm.setOnClickListener(l -> this.confirmSettings());
    }
}