package com.myhearfitness.app.srqa;


import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String COMMA_DELIMITER = ",";

    public static List<List<String>>  readCSV(Context context){
        List<List<String>> records = new ArrayList<>();
        InputStreamReader is;
        try {
            is = new InputStreamReader(context.getAssets().open("datos_min.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));

            }
            return records;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}