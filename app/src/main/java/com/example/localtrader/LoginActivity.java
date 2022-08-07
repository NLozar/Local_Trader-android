package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private TextView tvWarning;

    private void attemptLogin() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String username = this.etUsername.getText().toString();
        if (username.length() == 0) {
            this.tvWarning.setVisibility(View.VISIBLE);
            this.tvWarning.setText(R.string.missing_username);
            return;
        }
        String password = this.etPassword.getText().toString();
        if (password.length() < 8) {
            this.tvWarning.setVisibility(View.VISIBLE);
            this.tvWarning.setText(R.string.pw_too_short_warn);
            return;
        }
        API api = new API(this, new RequestHandler(this, String.valueOf(R.string.base_api_url)));
        new Thread(() -> {
            Object resObj = api.attemptLogin(etUsername.getText().toString(), etPassword.getText().toString());
            if (!resObj.getClass().equals(String.class)) {
                AppState.logUserIn(etUsername.getText().toString(), (JwtHolder) resObj);
                startActivity(new Intent(this, MainActivity.class));
            } else {
                runOnUiThread(() -> Toast.makeText(this, R.string.request_failure, Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.etUsername = findViewById(R.id.login_username);
        this.etPassword = findViewById(R.id.login_password);
        Button btnLogIn = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);
        this.tvWarning = findViewById(R.id.login_warning);

        btnLogIn.setOnClickListener(l -> {
            try {
                Log.i(this.getClass().getSimpleName(), "Login will be attempted");
                this.attemptLogin();
            } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        });
    }
}