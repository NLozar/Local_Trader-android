package com.example.localtrader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNewUsername;
    private TextView tvNewUsernameWarn;
    private EditText etCurrPassword;
    private TextView tvCurrentPwWarn;
    private EditText etNewPassword;
    private TextView tvNewPwWarn;
    private EditText etConfirmNewPw;
    private TextView tvPwMismatch;

    private boolean setEditWarnings(String currPw, String newPw, String confirmNewPw) {
        boolean tripped = false;
        if (currPw.length() == 0) {
            this.tvCurrentPwWarn.setVisibility(View.VISIBLE);
            this.tvCurrentPwWarn.setText(R.string.current_pw_is_required);
            tripped = true;
        } else if (currPw.length() < 8) {
            this.tvCurrentPwWarn.setVisibility(View.VISIBLE);
            this.tvCurrentPwWarn.setText(R.string.pw_too_short_warn);
            tripped = true;
        } else {
            this.tvCurrentPwWarn.setVisibility(View.GONE);
        }
        if ((newPw.length() < 8) && (newPw.length() != 0)) {
            this.tvNewPwWarn.setVisibility(View.VISIBLE);
            this.tvNewPwWarn.setText(R.string.pw_too_short_warn);
            tripped = true;
        } else {
            this.tvNewPwWarn.setVisibility(View.GONE);
        }
        if (!newPw.equals(confirmNewPw) && (newPw.length() != 0)) {
            this.tvPwMismatch.setVisibility(View.VISIBLE);
            tripped = true;
        } else {
            this.tvPwMismatch.setVisibility(View.GONE);
        }
        return tripped;
    }

    private boolean setDeleteWarning(String curPw) {
        boolean tripped = false;
        if (curPw.length() == 0) {
            this.tvCurrentPwWarn.setVisibility(View.VISIBLE);
            this.tvCurrentPwWarn.setText(R.string.current_pw_is_required);
            tripped = true;
        } else if (curPw.length() < 8) {
            this.tvCurrentPwWarn.setVisibility(View.VISIBLE);
            this.tvCurrentPwWarn.setText(R.string.pw_too_short_warn);
            tripped = true;
        }
        return tripped;
    }

    private void confirmClick() {
        this.tvNewUsernameWarn.setVisibility(View.GONE);
        String currentPw = this.etCurrPassword.getText().toString();
        String newPw = this.etNewPassword.getText().toString();
        String confirmNewPw = this.etConfirmNewPw.getText().toString();
        if (this.setEditWarnings(currentPw, newPw, confirmNewPw)) return;
        String newUsername = this.etNewUsername.getText().toString();
        HashMap<String, String> profileChangeDetails = new HashMap<>();
        profileChangeDetails.put("newUsername", newUsername);
        profileChangeDetails.put("currentPw", currentPw);
        profileChangeDetails.put("newPw", newPw);
        new Thread(() -> {
            try {
                API api = new API(this, new RequestHandler(this));
                Object resObj = api.editProfile(profileChangeDetails);
                if (!resObj.getClass().equals(String.class)) {
                    ProfileEditRequestStatus pers = (ProfileEditRequestStatus) resObj;
                    if (pers.success) {
                        runOnUiThread(() -> Toast.makeText(this, R.string.changes_succeeded, Toast.LENGTH_LONG).show());
                        AppState.changeUserName(newUsername);
                        finish();
                    } else if (pers.wrongPassword) {
                        runOnUiThread(() -> {
                            this.tvCurrentPwWarn.setVisibility(View.VISIBLE);
                            this.tvCurrentPwWarn.setText(R.string.wrong_password);
                        });
                    } else if (pers.usernameTaken) {
                        runOnUiThread(() -> {
                            this.tvNewUsernameWarn.setVisibility(View.VISIBLE);
                            this.tvNewUsernameWarn.setText(R.string.username_taken);
                        });
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(this, R.string.request_failure, Toast.LENGTH_LONG).show());
                }
            } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void deleteUser() {
        String curPassword = this.etCurrPassword.getText().toString();
        new Thread(() -> {
            try {
                API api = new API(this, new RequestHandler(this));
                Object resObj = api.deleteUser(curPassword);
                if (!resObj.getClass().equals(String.class)) {
                    ProfileEditRequestStatus pers = (ProfileEditRequestStatus) resObj;
                    if (pers.wrongPassword) {
                        runOnUiThread(() -> {
                            this.tvCurrentPwWarn.setVisibility(View.VISIBLE);
                            this.tvCurrentPwWarn.setText(R.string.wrong_password);
                        });
                    } else if (pers.success) {
                        AppState.logUserOut();
                        runOnUiThread(() -> Toast.makeText(this, R.string.account_deleted, Toast.LENGTH_LONG));
                        finish();
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(this, R.string.request_failure, Toast.LENGTH_LONG).show());
                }
            } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        TextView tvCurrentName = findViewById(R.id.ep_tv_current_name);
        this.etNewUsername = findViewById(R.id.ep_et_new_username);
        this.tvNewUsernameWarn = findViewById(R.id.ep_tv_newUsername_warn);
        this.etCurrPassword = findViewById(R.id.ep_et_current_password);
        this.tvCurrentPwWarn = findViewById(R.id.ep_tv_current_password_warn);
        this.etNewPassword = findViewById(R.id.ep_et_new_password);
        this.tvNewPwWarn = findViewById(R.id.ep_tv_new_password_warn);
        this.etConfirmNewPw = findViewById(R.id.ep_et_confirm_new_password);
        this.tvPwMismatch = findViewById(R.id.ep_tv_pw_mismatch);
        Button btnConfirm = findViewById(R.id.ep_btn_confirm);
        Button btnDelete = findViewById(R.id.ep_btn_delete);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        tvCurrentName.setText(AppState.userName);
        btnConfirm.setOnClickListener(l -> this.confirmClick());
        btnDelete.setOnClickListener(l -> {
            if (this.setDeleteWarning(this.etCurrPassword.getText().toString())) return;
            adb.setTitle(R.string.warning)
                    .setMessage(R.string.account_delete_warn)
                    .setCancelable(true)
                    .setPositiveButton("OK", (m, n) -> this.deleteUser())
                    .setNegativeButton("Cancel", (m, n) -> m.cancel())
                    .show();
        });
    }
}