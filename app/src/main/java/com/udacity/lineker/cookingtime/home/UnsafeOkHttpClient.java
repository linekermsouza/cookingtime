package com.udacity.lineker.cookingtime.home;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import okhttp3.OkHttpClient;

public class UnsafeOkHttpClient {
    public static OkHttpClient get() {
        OkHttpClient client=new OkHttpClient();
        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(new TLSSocketFactory())
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return client;
    }
}