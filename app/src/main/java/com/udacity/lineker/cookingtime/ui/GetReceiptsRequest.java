package com.udacity.lineker.cookingtime.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.lineker.cookingtime.model.Receipt;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetReceiptsRequest extends AsyncTaskLoader<List<Receipt>> {
    private static final String LOG_TAG = GetReceiptsRequest.class.getName();
    public static final int REQUEST_MOVIES_LOADER = 1;
    public static final String REQUEST_MOVIES_TYPE_EXTRA = "type";
    private static final String URL_RECEIPTS = "http://go.udacity.com/android-baking-app-json";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String NOW_PLAYING = "now_playing";
    public static final String UPCOMING = "upcoming";
    public static final String URL_IMAGES = "http://image.tmdb.org/t/p/w185";
    private final Bundle args;

    List<Receipt> receipts;

    public GetReceiptsRequest(Context context, Bundle args, List<Receipt> receipts) {
        super(context);
        this.args = args;
        this.receipts = receipts;
    }

    @Override
    protected void onStartLoading() {

        if (args == null) {
            return;
        }

        if (receipts != null) {
            deliverResult(receipts);
        } else {
            forceLoad();
        }
    }



    @Override
    public List<Receipt> loadInBackground() {
        String urlString = URL_RECEIPTS;


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlString)
                .build();


        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Receipt>>(){}.getType();
            List<Receipt> receipts = gson.fromJson(json, listType);

            return receipts;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Falha ao acessar Web service", e);
        }

        return null;
    }

    @Override
    public void deliverResult(List<Receipt> result) {
        receipts = result;
        super.deliverResult(result);
    }
}
