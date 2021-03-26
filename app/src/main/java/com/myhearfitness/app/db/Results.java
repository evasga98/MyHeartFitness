package com.myhearfitness.app.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

@Entity(tableName = "results_table")
@TypeConverters({Converters.class})
public class Results {

    public Results(@NonNull int[] results, double accuracy) {
        this.results = results;
        this.accuracy = accuracy;
    }

    @PrimaryKey(autoGenerate = true) long _ID;

    @NonNull
    public int[] getResults() {
        return results;
    }

    public void setResults(@NonNull int[] results) {
        this.results = results;
    }

    @NonNull
    @ColumnInfo(name = "results")
    private int[] results;

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    @ColumnInfo(name = "accuracy")
    private double accuracy;

}

class Converters {
    @TypeConverter
    public static int[] fromString(String value) {
        Type listType = new TypeToken<int[]>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArray(int[] list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}