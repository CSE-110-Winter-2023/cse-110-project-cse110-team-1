package com.example.socialcompass.model.Friend;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.time.Instant;

import kotlin.jvm.Transient;


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

@Entity(tableName = "friends", ignoredColumns = {"private_code"})
public class Friend {
    /** The public code of the user. Used as the primary key for shared notes (even on the cloud). */
    @PrimaryKey
    @SerializedName("public_code")
    @NonNull
    public String public_code;

    /** The private code of the user. */
    @SerializedName("private_code")
    @NonNull
    public String private_code;

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

    @JsonAdapter(com.example.socialcompass.model.Friend.TimestampAdapter.class)
    @SerializedName(value = "created_at", alternate = "createdAt")
    public long createdAt;
    @JsonAdapter(com.example.socialcompass.model.Friend.TimestampAdapter.class)
    @SerializedName(value = "updated_at", alternate = "updatedAt")
    public long updatedAt = 0;

    // Empty constructor required by Room
    public Friend() {}

    /** General constructor for a note. */
    public Friend(@NonNull String public_code,  String private_code, @NonNull String label
            , @NonNull float latitude, @NonNull float longitude) {
        this.public_code = public_code;
        this.private_code = private_code;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.updatedAt = 0;
    }



    public static Friend fromJSON(String json) {
        return new Gson().fromJson(json, Friend.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

}