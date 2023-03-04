package com.example.socialcompass.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "friends_list_items")
public class FriendListItem {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String public_code;
    @NonNull
    public String label;
    @NonNull
    public String latitude;

    public String longitude;
    public int order;

    public FriendListItem(@NonNull String label, @NonNull String public_code, @NonNull String latitude,@NonNull String longitude, int order) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.public_code = public_code;
        this.order = order;
    }

    public static List<FriendListItem> loadJSON(Context context, String path){
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<FriendListItem>>(){}.getType();
            return gson.fromJson(reader,type);

        }catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "FriendListItem{" +
                "public_code='" + public_code + '\'' +
                ", label='" + label + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", order=" + order +
                '}';
    }
}
