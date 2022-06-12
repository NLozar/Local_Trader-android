package com.example.localtrader;

public class AllItemsViewEntry {
    private String title;
    private String sellerName;
    private String descr;
    private String uuid;

    public AllItemsViewEntry(String title, String sellerName, String descr, String uuid) {
        this.title = title;
        this.sellerName = sellerName;
        this.descr = descr;
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getSellerName() {
        return sellerName;
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
        return "AllItemsViewEntry [title=" + this.title + ", sellerName=" + this.sellerName + ", descr=" + this.descr + ", uuid=" + this.uuid + "]";
    }
}
