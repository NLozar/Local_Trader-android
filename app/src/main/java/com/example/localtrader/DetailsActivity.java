package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvDescr;
    private TextView tvSeller;
    private TextView tvContact;

    private void setDisplayedData(ItemDetailsDataHolder data) {
        this.tvTitle.setText(data.getTitle());
        if (data.getPrice() != null) {
            this.tvPrice.setText(String.format("%s: %s", getResources().getString(R.string.price), data.getPrice()));
        }
        if (data.getDescr() != null) {
            this.tvDescr.setText(String.format("%s: %s", getResources().getString(R.string.description), data.getDescr()));
        }
        this.tvSeller.setText(String.format("%s: %s", getResources().getString(R.string.seller), data.getSeller_name()));
        this.tvContact.setText(String.format("%s: %s", getResources().getString(R.string.contact_info), data.getContact()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle receivedIntentBundle = getIntent().getExtras();
        ItemDetailsDataHolder data = (ItemDetailsDataHolder) receivedIntentBundle.get("item_data");
        this.tvTitle = findViewById(R.id.detailsTitle);
        this.tvPrice = findViewById(R.id.detailsPrice);
        this.tvDescr = findViewById(R.id.detailsDescr);
        this.tvSeller = findViewById(R.id.detailsSellerName);
        this.tvContact = findViewById(R.id.detailsContact);
        Button btnEdit = findViewById(R.id.btn_editDetails);
        if (AppState.userLoggedIn) {
            if (AppState.userName.equals(data.getSeller_name())) {
                findViewById(R.id.detailsForehead).setVisibility(View.VISIBLE);
                TextView tvProfileName = findViewById(R.id.details_profileName);
                tvProfileName.setText(AppState.userName);
            }
        }
        this.setDisplayedData(data);
        Intent editIntent = new Intent(this, PostItemActivity.class);
        editIntent.putExtra("item_data", data);
        btnEdit.setOnClickListener(l -> {
            startActivity(editIntent);
            finish();
        });
    }
}