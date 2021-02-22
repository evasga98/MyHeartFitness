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
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String COMMA_DELIMITER = ",";

    public static void  readCSV(Context context){
        List<List<String>> data = new ArrayList<>();
        InputStreamReader is;
        try {
            is = new InputStreamReader(context.getAssets().open("datos_min.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                data.add(Arrays.asList(values));

            }
            //System.out.println(data.toString());
            loadTimeSeries(data);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private static void loadTimeSeries(List<List<String>> data){
        // load time series RR intervals
        List<Double> RR = new ArrayList<>();

        // load the Boolean time series of AF detections for RR
        List<Boolean> D = new ArrayList<>();

        // define window size
        int w = 30;

        for(int i=1; i < data.size();  i++ ) {
            RR.add(Double.parseDouble(data.get(i).get(1)));
            D.add(Boolean.parseBoolean(data.get(i).get(2)));

        }

        int lw = Math.round((data.size()/w));
        List<Integer> pos_af = new ArrayList<Integer>(Collections.nCopies(lw, 0));
        //System.out.println(pos_af.size());
        //System.out.println(lw);

        List<Double> dataRR = new ArrayList<>();
        List<Boolean> dataD = new ArrayList<>();

        for(int k=1; k < lw;  k++ ) {
            // take a time series of RR interval of length w
            dataRR = RR.subList(( w*(k-1)),k*w);

            // a window is defined to be in AF iif number of AF (Atrial Fibrilation) detections in data is >=w/2
            // define pos_af as the boolean vector identifying the AF windows
            dataD = D.subList(( w*(k-1)),k*w);

            if (Collections.frequency(dataD, 1) > w/2) {
                pos_af.add(k,1);
            }

            // Computation of covariates
            // Descriptive mesures

            // mean
            double mean = getMean(dataRR);

            double lower = dataRR.get(dataRR.size() / 2 - 1);
            double upper = dataRR.get(dataRR.size() / 2);


            //double median  = (dataRR.get(dataRR.size()/2) + dataRR.get(dataRR.size()/2 - 1))/2;
            double median = getMedian(dataRR);

            //System.out.println((lower + upper) / 2.0);
            System.out.println(mean);
            System.out.println(median);
            //TimeUnit.SECONDS.sleep(10);


        }

    }

    private static double getMean(List<Double> list) {
        Double sum = 0.0;
        if(!list.isEmpty()) {
            for (Double element : list) {
                sum += element;
            }
            return sum.doubleValue() / list.size();
        }
        return sum;
    }

    private static double getMedian(List<Double> list){
        Collections.sort(list);
        if(list.size()%2 != 0){
            return list.get(list.size()/2);
        }
        return  ((list.get((list.size()-1)/2)- list.get(list.size()/2))/2.0);
    }


}