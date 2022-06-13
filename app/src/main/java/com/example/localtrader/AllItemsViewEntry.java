package com.example.localtrader;

public class AllItemsViewEntry {
    private String title;
    private String price;
    private String descr;
    private String uuid;

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

    @Override
    public String toString() {
        // only for debugging
        return "AllItemsViewEntry [title=" + this.title + ", price=" + this.price + ", descr=" + this.descr + ", uuid=" + this.uuid + "]";
    }
}
