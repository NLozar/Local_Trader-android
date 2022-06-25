package com.example.localtrader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        tvTitle.setText(data.getTitle());
        tvPrice.setText("Price: " + data.getPrice());
        tvDescr.setText("Description:\n" + data.getDescr());
        tvSeller.setText("Seller: " + data.getSeller_name());
        tvContact.setText("Contact: " + data.getContact());
    }
}