package com.example.localtrader;

public class AppState {

    protected static boolean userLoggedIn;
    protected static String userName;
    protected static String userUuid;

    public static void logUserOut() {
        userLoggedIn = false;
        userName = null;
        userUuid = null;
    }

    public static void logUserIn() {
    }
}
