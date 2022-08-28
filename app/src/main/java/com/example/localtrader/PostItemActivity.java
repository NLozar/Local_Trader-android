package com.example.localtrader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class PostItemActivity extends AppCompatActivity {

    private EditText etTitle;
    private TextView tvTitleWarn;
    private EditText etPrice;
    private EditText etDescr;
    private EditText etContactInfo;
    private TextView tvContactWarn;
    private ItemDetailsDataHolder itemData;

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
        String title = this.etTitle.getText().toString();
        String contactInfo = this.etContactInfo.getText().toString();
        String price = this.etPrice.getText().toString();
        String descr = this.etDescr.getText().toString();
        if (this.setWarnings(title, contactInfo)) return;
        HashMap<String, String> details = new HashMap<>();
        details.put("title", title);
        details.put("price", price);
        details.put("descr", descr);
        details.put("contact_info", contactInfo);
        if (this.itemData != null) {
            this.itemData.setTitle(title);
            this.itemData.setPrice(price);
            this.itemData.setDescr(descr);
            this.itemData.setContact(contactInfo);
        }
        API api = new API(this, new RequestHandler(this, getResources().getString(R.string.base_api_url)));
        new Thread(() -> {
            Object resObj;
            if (this.itemData != null) {
                resObj = api.editItem(this.itemData);
            } else {
                resObj = api.postItem(details);
            }
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

    private void deleteItem() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        API api = new API(this, new RequestHandler(this, getResources().getString(R.string.base_api_url)));
        new Thread(() -> {
            Object resObj = api.deleteItem(this.itemData.getUuid());
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

        Bundle receivedIntentExtras = getIntent().getExtras();
        this.itemData = null;
        if (receivedIntentExtras != null) {
            this.itemData = (ItemDetailsDataHolder) receivedIntentExtras.get("item_data");
        }
        this.etTitle = findViewById(R.id.pi_et_title);
        this.tvTitleWarn = findViewById(R.id.pi_title_warn);
        this.etPrice = findViewById(R.id.pi_et_price);
        this.etDescr = findViewById(R.id.pi_et_descr);
        this.etContactInfo = findViewById(R.id.pi_et_contactInfo);
        this.tvContactWarn = findViewById(R.id.pi_contact_warn);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        if (this.itemData != null) {
            this.etTitle.setText(this.itemData.getTitle());
            this.etPrice.setText(this.itemData.getPrice());
            this.etDescr.setText(this.itemData.getDescr());
            this.etContactInfo.setText(this.itemData.getContact());
            Button btnDeleteItem = findViewById(R.id.btn_delete_item);
            btnDeleteItem.setVisibility(View.VISIBLE);
            btnDeleteItem.setOnClickListener(l -> adb.setTitle(R.string.warning)
                    .setMessage(R.string.delete_listing_warn)
                    .setPositiveButton("YES", (m, n) -> {
                        try {
                            this.deleteItem();
                        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton("Cancel", (m, n) -> m.cancel())
                    .show());
        }
        findViewById(R.id.btn_pi_post).setOnClickListener(l -> {
            try {
                this.postItem();
            } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        });
    }
}