package com.example.socialcompass.model.friend;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.time.Instant;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;

public class TimestampAdapter extends TypeAdapter<Long> {
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
