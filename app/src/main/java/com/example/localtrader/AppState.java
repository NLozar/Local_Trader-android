package com.example.localtrader;

public class AppState {

    protected static boolean userLoggedIn;
    protected static String userName;
    protected static String token;
    protected static String baseApiUrl = "https://192.168.1.147:14443";

    public static void logUserOut() {
        userLoggedIn = false;
        userName = null;
        token = null;
    }

    public static void logUserIn(String uName, String jwt) {
        userLoggedIn = true;
        userName = uName;
        token = jwt;
    }

    public static void changeUserName(String newName) {
        userName = newName;
    }

    public static void setBaseApiUrl(String newUrl) {
        baseApiUrl = newUrl;
    }
}
