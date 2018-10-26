package com.udacity.lineker.cookingtime.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.udacity.lineker.cookingtime.model.Receipt;

import java.util.List;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceiptRepository {
    private static final String TAG = "ReceiptRepository";
    private Webservice webservice;
    private static ReceiptRepository instance;
    private LiveData<List<Receipt>> receiptsCache;
    OkHttpClient okHttpClient = UnsafeOkHttpClient.get();

    public static ReceiptRepository getInstance() {
        if (instance == null) {
            instance = new ReceiptRepository();
        }
        return instance;
    }

    private ReceiptRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Webservice.HTTPS_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webservice = retrofit.create(Webservice.class);
    }


    public LiveData<List<Receipt>> getReceipts() {
        LiveData<List<Receipt>> cached = receiptsCache;
        if (cached != null) {
            return cached;
        }

        final MutableLiveData<List<Receipt>> data = new MutableLiveData<>();
        receiptsCache = data;

        webservice.getReceipts().enqueue(new Callback<List<Receipt>>() {
            @Override
            public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {
                Log.d(TAG, "sucess");
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Receipt>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(null);
            }

            // Error case is left out for brevity.
        });
        return data;
    }

    public void clearCache() {
        receiptsCache = null;
    }
}
