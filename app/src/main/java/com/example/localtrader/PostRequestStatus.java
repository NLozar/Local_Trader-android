package com.example.localtrader;

public class PostRequestStatus {

    protected boolean badJwt;
    protected boolean success;

    public PostRequestStatus(boolean success) {
        this.success = success;
    }

    public void setBadJwt(boolean badJwt) {
        this.badJwt = badJwt;
    }
}
