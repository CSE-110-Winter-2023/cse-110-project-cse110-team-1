package com.example.socialcompass.model.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.example.socialcompass.model.friend.Friend;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API {
    private volatile static API instance = null;
    private OkHttpClient client;

    private String url;
    public API(String url) {
        this.client = new OkHttpClient();
        if (url == null) {
            this.url = "https://socialcompass.goto.ucsd.edu/";
        } else {
            this.url = url;
        }
    }

    public static API provide(String url) {
        if (instance == null) {
            instance = new API(url);
        }
        return instance;
    }


    @WorkerThread
    public Friend getFriend(String publicCode) {
        // URLs cannot contain spaces, so we replace them with %20.
        publicCode = publicCode.replace(" ", "%20");

        var request = new Request.Builder()
                .url( url + "location/" + publicCode)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            if (response.isSuccessful()) {
                var body = response.body().string();
                Log.d("API_HELLO", Friend.fromJSON(body).toString());
                return Friend.fromJSON(body);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @WorkerThread
    public void putUser(Friend user, String privateCode) {
        // URLs cannot contain spaces, so we replace them with %20.
        String publicCode = user.publicCode.replace(" ", "%20");
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        JsonObject reqContent = new JsonObject();

        reqContent.addProperty("private_code", privateCode);
        reqContent.addProperty("label", user.label);
        reqContent.addProperty("latitude", user.latitude);
        reqContent.addProperty("longitude", user.longitude);


        var request = new Request.Builder()
                .url( url + "location/" + user.publicCode)
                .put(RequestBody.create(String.valueOf(reqContent), JSON))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                var body = response.body().string();
                if (response.isSuccessful()) {
                    Friend responseNote = Friend.fromJSON(body);
                } else {
                    Log.i("PUT USER failed response", body);
                }
            }
        });

    }
}
