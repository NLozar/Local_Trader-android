package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private TextView tvWarning;

    private void attemptLogin() {
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

        btnLogIn.setOnClickListener(l -> this.attemptLogin());
    }
}