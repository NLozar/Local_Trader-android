package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle receivedIntentBundle = getIntent().getExtras();
        ItemDetailsDataHolder data = (ItemDetailsDataHolder) receivedIntentBundle.get("item_data");
        TextView tvTitle = findViewById(R.id.detailsTitle);
        TextView tvPrice = findViewById(R.id.detailsPrice);
        TextView tvDescr = findViewById(R.id.detailsDescr);
        TextView tvSeller = findViewById(R.id.detailsSellerName);
        TextView tvContact = findViewById(R.id.detailsContact);
        if (AppState.userLoggedIn) {
            if (AppState.userName.equals(data.getSeller_name())) {
                findViewById(R.id.detailsForehead).setVisibility(View.VISIBLE);
                TextView tvProfileName = findViewById(R.id.details_profileName);
                tvProfileName.setText(AppState.userName);
            }
        }
        tvTitle.setText(data.getTitle());
        tvPrice.setText(String.format("%s: %s", getResources().getString(R.string.price), data.getPrice()));
        tvDescr.setText(String.format("%s: %s", getResources().getString(R.string.description), data.getDescr()));
        tvSeller.setText(String.format("%s: %s", getResources().getString(R.string.seller), data.getSeller_name()));
        tvContact.setText(String.format("%s: %s", getResources().getString(R.string.contact_info), data.getContact()));
    }
}