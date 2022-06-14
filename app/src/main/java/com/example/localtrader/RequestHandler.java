package com.example.localtrader;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class RequestHandler {

    private String apiBaseUrl;
    private HttpsURLConnection conn;

    public RequestHandler(Context ctx, String apiBaseUrl) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.apiBaseUrl = apiBaseUrl;
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] kspw = "tnuvhl".toCharArray();
        ks.load(ctx.getResources().openRawResource(R.raw.keystore), kspw);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(ks);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        this.conn = (HttpsURLConnection) new URL(this.apiBaseUrl).openConnection();
        this.conn.setSSLSocketFactory(sslContext.getSocketFactory());
        this.conn.setReadTimeout(5000);
        this.conn.setConnectTimeout(10000);
    }

    public JsonNode sendGetRequest() throws IOException {
        this.conn.setRequestMethod("GET");
        this.conn.setDoInput(true);
        this.conn.setRequestProperty("Accept", "application/json");
        final InputStream[] resIs = new InputStream[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(this.getClass().getSimpleName(), "Connection will be attempted");
                    conn.connect();
                    int responseCode = conn.getResponseCode(); // blocks further execution until response
                    resIs[0] = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //this.conn.connect();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(resIs[0]);
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
