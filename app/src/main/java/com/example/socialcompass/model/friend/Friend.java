package com.example.socialcompass.model.friend;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.time.Instant;


class TimestampAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter out, Long value) throws java.io.IOException {
        var instant = Instant.ofEpochSecond(value);
        out.value(instant.toString());
    }

    @Override
    public Long read(JsonReader in) throws java.io.IOException {
        var instant = Instant.parse(in.nextString());
        return instant.getEpochSecond();
    }
}

@Entity(tableName = "friends")
public class Friend {
    /** The public code of the user. Used as the primary key for shared notes (even on the cloud). */
    @PrimaryKey
    @SerializedName("publicCode")
    @NonNull
    public String publicCode;

    /** The label of the user. */
    @SerializedName("label")
    @NonNull
    public String label;

    /** The latitude of the user. */
    @SerializedName("latitude")
    @NonNull
    public float latitude;

    /** The longitude of the user. */
    @SerializedName("longitude")
    @NonNull
    public float longitude;


    @JsonAdapter(TimestampAdapter.class)
    @SerializedName(value = "created_at", alternate = "createdAt")
    public long createdAt;
    @JsonAdapter(TimestampAdapter.class)
    @SerializedName(value = "updated_at", alternate = "updatedAt")
    public long updatedAt = 0;

    // Empty constructor required by Room
    public Friend() {}

    /** General constructor for a note. */
    public Friend(@NonNull String publicCode, @NonNull String label, @NonNull float latitude, @NonNull float longitude) {
        this.publicCode = publicCode;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    public static Friend fromJSON(String json) {
        return new Gson().fromJson(json, Friend.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

}