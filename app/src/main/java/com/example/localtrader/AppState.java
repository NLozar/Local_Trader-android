package com.example.localtrader;

public class AppState {

    protected static boolean userLoggedIn;
    protected static String userName;
    protected static String token;

    public static void logUserOut() {
        userLoggedIn = false;
        userName = null;
        token = null;
    }

    public static void logUserIn(String uName, JwtHolder jwt) {
        userLoggedIn = true;
        userName = uName;
        token = jwt.toString();
    }
}
