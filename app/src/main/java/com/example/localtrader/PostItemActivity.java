package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;

public class PostItemActivity extends AppCompatActivity {

    private EditText etTitle;
    private TextView tvTitleWarn;
    private EditText etPrice;
    private EditText etDescr;
    private EditText etContactInfo;
    private TextView tvContactWarn;

    private boolean setWarnings(String title, String contactInfo) {
        boolean tripped = false;
        if (title.length() == 0) {
            this.tvTitleWarn.setVisibility(View.VISIBLE);
            tripped = true;
        } else {
            this.tvTitleWarn.setVisibility(View.GONE);
        }
        if (contactInfo.length() == 0) {
            this.tvContactWarn.setVisibility(View.VISIBLE);
            tripped = true;
        } else {
            this.tvContactWarn.setVisibility(View.GONE);
        }
        return tripped;
    }

    private void postItem() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String title = etTitle.getText().toString();
        String contactInfo = etContactInfo.getText().toString();
        if (this.setWarnings(title, contactInfo)) return;
        HashMap<String, String> details = new HashMap<>();
        details.put("title", title);
        details.put("price", etPrice.getText().toString());
        details.put("descr", etDescr.getText().toString());
        details.put("contact_info", contactInfo);
        API api = new API(this, new RequestHandler(this, getResources().getString(R.string.base_api_url)));
        new Thread(() -> {
            Object resObj = api.postItem(AppState.token, details);
            if (!resObj.getClass().equals(String.class)) {
                PostRequestStatus prs = (PostRequestStatus) resObj;
                if (prs.badJwt) {
                    runOnUiThread(() -> Toast.makeText(this, R.string.expired_credentials, Toast.LENGTH_LONG).show());
                    startActivity(new Intent(this, LoginActivity.class));
                } else if (prs.success) {
                    runOnUiThread(() -> Toast.makeText(this, R.string.listing_posted, Toast.LENGTH_LONG).show());
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
        setContentView(R.layout.activity_post_item);

        this.etTitle = findViewById(R.id.pi_et_title);
        this.tvTitleWarn = findViewById(R.id.pi_title_warn);
        this.etPrice = findViewById(R.id.pi_et_price);
        this.etDescr = findViewById(R.id.pi_et_descr);
        this.etContactInfo = findViewById(R.id.pi_et_contactInfo);
        this.tvContactWarn = findViewById(R.id.pi_contact_warn);
        findViewById(R.id.btn_pi_post).setOnClickListener(l -> {
            try {
                this.postItem();
            } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        });
    }
}