package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPw;
    private TextView unWarning;
    private TextView tvWarning1;
    private TextView tvWarning2;

    private void registerUser() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String username = this.etUsername.getText().toString();
        if (username.length() == 0) {
            this.tvWarning1.setVisibility(View.VISIBLE);
            this.tvWarning1.setText(R.string.missing_username);
            return;
        }
        String password = this.etPassword.getText().toString();
        if (password.length() < 8) {
            this.tvWarning1.setVisibility(View.VISIBLE);
            this.tvWarning1.setText(R.string.pw_too_short_warn);
            return;
        }
        String confirm_pw = this.etConfirmPw.getText().toString();
        if (!password.equals(confirm_pw)) {
            this.tvWarning2.setVisibility(View.VISIBLE);
            this.tvWarning2.setText(R.string.pw_mismatch);
            return;
        }
        API api = new API(this, new RequestHandler(this));
        new Thread(() -> {
            Object resObj = api.registerUser(username, password);
            if (!resObj.getClass().equals(String.class)) {
                boolean usernameTaken = DataHandler.isUsernameTaken((JsonNode) resObj);
                if (usernameTaken) {
                    runOnUiThread(() -> {
                        this.unWarning.setVisibility(View.VISIBLE);
                        this.unWarning.setText(R.string.username_taken);
                    });
                } else {    // USER SUCCESSFULLY REGISTERED
                    runOnUiThread(() -> Toast.makeText(this, R.string.registration_success, Toast.LENGTH_LONG).show());
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
            } else {
                runOnUiThread(() -> Toast.makeText(this, R.string.request_failure, Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Bundle receivedIntentBundle = getIntent().getExtras();
        this.etUsername = findViewById(R.id.register_username);
        this.etPassword = findViewById(R.id.register_password);
        this.etConfirmPw = findViewById(R.id.register_confirm_pw);
        this.unWarning = findViewById(R.id.register_username_warn);
        this.tvWarning1 = findViewById(R.id.register_warning_1);
        this.tvWarning2 = findViewById(R.id.register_warning_2);
        Button btnConfirm = findViewById(R.id.btn_confirm_register);

        this.etUsername.setText(receivedIntentBundle.get("username").toString());
        this.etPassword.setText(receivedIntentBundle.get("password").toString());
        btnConfirm.setOnClickListener(l -> {
            try {
                registerUser();
            } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        });
    }
}