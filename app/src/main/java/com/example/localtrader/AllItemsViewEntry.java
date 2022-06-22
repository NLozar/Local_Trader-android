package com.example.localtrader;

import androidx.annotation.NonNull;

public class AllItemsViewEntry {
    private final String title;
    private final String price;
    private final String descr;
    private final String uuid;

    public AllItemsViewEntry(String title, String price, String descr, String uuid) {
        this.title = title;
        this.price = price;
        this.descr = descr;
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDescr() {
        return descr;
    }

    public String getUuid() {
        return uuid;
    }

    @NonNull
    @Override
    public String toString() {
        // only for debugging
        return "AllItemsViewEntry [title=" + this.title + ", price=" + this.price + ", descr=" + this.descr + ", uuid=" + this.uuid + "]";
    }
}
