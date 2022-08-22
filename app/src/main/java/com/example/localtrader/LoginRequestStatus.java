package com.example.localtrader;

public class LoginRequestStatus {

    protected boolean badCreds;
    protected String jwt;

    public LoginRequestStatus(String jwt) {
        this.badCreds = false;
        this.jwt = jwt;
    }

    public LoginRequestStatus(boolean badCreds) {
        // this constructor should always set badCreds to true
        this.badCreds = badCreds;
    }
}
