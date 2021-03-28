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

import smile.stat.Hypothesis;

@Entity(tableName = "results_table")
@TypeConverters({Converters.class})
public class Results {

    public Results(@NonNull int[] results, double accuracy, double sensitivity, double FPR, double specificity) {
        this.results = results;
        this.accuracy = accuracy;
        this.sensitivity = sensitivity;
        this.FPR = FPR;
        this.specificity = specificity;
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

    public double getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    @ColumnInfo(name = "sensitivity")
    private double sensitivity;

    public double getFPR() {
        return FPR;
    }

    public void setFPR(double FPR) {
        this.FPR = FPR;
    }

    @ColumnInfo(name = "FPR")
    private double FPR;

    public double getSpecificity() {
        return specificity;
    }

    public void setSpecificity(double specificity) {
        this.specificity = specificity;
    }

    @ColumnInfo(name = "specificity")
    private double specificity;

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