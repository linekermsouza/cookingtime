package com.udacity.lineker.cookingtime.home;

import com.udacity.lineker.cookingtime.model.Receipt;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Webservice {
    String HTTPS_API_URL = "http://go.udacity.com/";

    @GET("android-baking-app-json")
    Call<List<Receipt>> getReceipts();
}

