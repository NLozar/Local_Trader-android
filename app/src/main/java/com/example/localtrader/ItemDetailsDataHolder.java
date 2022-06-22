package com.example.localtrader;

import androidx.annotation.NonNull;

public class ItemDetailsDataHolder {

    private String title, seller_name, price, descr, contact, uuid;

    public ItemDetailsDataHolder(String title, String seller_name, String price, String descr, String contact, String uuid) {
        this.title = title;
        this.seller_name = seller_name;
        this.price = price;
        this.descr = descr;
        this.contact = contact;
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescr() {
        return descr;
    }

    public String getContact() {
        return contact;
    }

    public String getUuid() {
        return uuid;
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemDetailsDataHolder{" +
                "title='" + title + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", price='" + price + '\'' +
                ", descr='" + descr + '\'' +
                ", contact='" + contact + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
