package com.example.localtrader;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class RequestHandler {

    private final String apiBaseUrl;
    private HttpsURLConnection conn;
    private final SSLContext sslContext;

    public RequestHandler(Context ctx, String apiBaseUrl) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.apiBaseUrl = apiBaseUrl;
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] kspw = "tnuvhl".toCharArray();
        ks.load(ctx.getResources().openRawResource(R.raw.keystore), kspw);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(ks);
        this.sslContext = SSLContext.getInstance("TLS");
        this.sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        HostnameVerifier allHostsValid = (s, sslSession) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    public JsonNode getAllItems() throws IOException {
        String url = this.apiBaseUrl + "/itemsList";
        this.conn = (HttpsURLConnection) new URL(url).openConnection();
        this.conn.setSSLSocketFactory(this.sslContext.getSocketFactory());
        this.conn.setReadTimeout(5000);
        this.conn.setConnectTimeout(10000);
        this.conn.setRequestMethod("GET");
        this.conn.setDoInput(true);
        this.conn.setRequestProperty("Accept", "application/json");
        Log.i(this.getClass().getSimpleName(), "getAllItems connection will be attempted");
        this.conn.connect();
        int responseCode = this.conn.getResponseCode(); // blocks further execution until response
        Log.i(this.getClass().getSimpleName(), "Response code: " + responseCode);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(this.conn.getInputStream());
    }

    public JsonNode getItemDetails(String itemUuid) throws IOException {
        String url = this.apiBaseUrl + "/itemDetails/" + itemUuid;
        this.conn = (HttpsURLConnection) new URL(url).openConnection();
        this.conn.setSSLSocketFactory(this.sslContext.getSocketFactory());
        this.conn.setReadTimeout(5000);
        this.conn.setConnectTimeout(10000);
        this.conn.setRequestMethod("GET");
        this.conn.setDoInput(true);
        this.conn.setRequestProperty("Accept", "application/json");
        Log.i(this.getClass().getSimpleName(), "getItemDetails connection will be attempted");
        this.conn.connect();
        int responseCode = this.conn.getResponseCode(); // blocks further execution until response
        Log.i(this.getClass().getSimpleName(), "Response code: " + responseCode);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(this.conn.getInputStream());
    }

    public JsonNode attemptLogin(String username, String password) throws IOException {
        String url = this.apiBaseUrl + "/login";
        this.conn = (HttpsURLConnection) new URL(url).openConnection();
        this.conn.setSSLSocketFactory(this.sslContext.getSocketFactory());
        this.conn.setReadTimeout(5000);
        this.conn.setConnectTimeout(10000);
        this.conn.setRequestMethod("POST");
        this.conn.setDoInput(true);
        this.conn.setRequestProperty("Accept", "application/json");
        this.conn.setRequestProperty("username", username);
        this.conn.setRequestProperty("password", password);
        Log.i(this.getClass().getSimpleName(), "login connection will be attempted");
        this.conn.connect();
        int responseCode = this.conn.getResponseCode(); // blocks further execution until response
        Log.i(this.getClass().getSimpleName(), "Response code: " + responseCode);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(this.conn.getInputStream());
    }

    public JsonNode registerUser(String username, String password) throws IOException {
        String url = this.apiBaseUrl + "/registerUser";
        this.conn = (HttpsURLConnection) new URL(url).openConnection();
        this.conn.setSSLSocketFactory(this.sslContext.getSocketFactory());
        this.conn.setReadTimeout(5000);
        this.conn.setConnectTimeout(10000);
        this.conn.setRequestMethod("POST");
        this.conn.setDoInput(true);
        this.conn.setRequestProperty("Accept", "application/json");
        this.conn.setRequestProperty("username", username);
        this.conn.setRequestProperty("password", password);
        Log.i(this.getClass().getSimpleName(), "registerUser connection will be attempted");
        this.conn.connect();
        int responseCode = this.conn.getResponseCode(); // blocks further execution until response
        Log.i(this.getClass().getSimpleName(), "Response code: " + responseCode);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(this.conn.getInputStream());
    }
}
