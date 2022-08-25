package com.example.localtrader;

public class PostRequestStatus {

    public boolean badJwt;
    public boolean success;

    public PostRequestStatus(boolean success) {
        this.success = success;
    }
}
