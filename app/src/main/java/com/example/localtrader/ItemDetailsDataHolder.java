package com.example.localtrader;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ItemDetailsDataHolder implements Parcelable {

    private final String title, seller_name, price, descr, contact, uuid;

    public ItemDetailsDataHolder(String title, String seller_name, String price, String descr, String contact, String uuid) {
        this.title = title;
        this.seller_name = seller_name;
        this.price = price;
        this.descr = descr;
        this.contact = contact;
        this.uuid = uuid;
    }

    protected ItemDetailsDataHolder(Parcel in) {
        title = in.readString();
        seller_name = in.readString();
        price = in.readString();
        descr = in.readString();
        contact = in.readString();
        uuid = in.readString();
    }

    public static final Creator<ItemDetailsDataHolder> CREATOR = new Creator<ItemDetailsDataHolder>() {
        @Override
        public ItemDetailsDataHolder createFromParcel(Parcel in) {
            return new ItemDetailsDataHolder(in);
        }

        @Override
        public ItemDetailsDataHolder[] newArray(int size) {
            return new ItemDetailsDataHolder[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(seller_name);
        parcel.writeString(price);
        parcel.writeString(descr);
        parcel.writeString(contact);
        parcel.writeString(uuid);
    }
}
