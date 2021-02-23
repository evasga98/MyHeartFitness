package com.myhearfitness.app.srqa;


import android.content.Context;
import android.os.Build;
import android.util.Log;

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
        List<Float> RR = new ArrayList<>();

        // load the Boolean time series of AF detections for RR
        List<Boolean> D = new ArrayList<>();

        // define window size
        int w = 30;

        for(int i=1; i < data.size();  i++ ) {
            RR.add(Float.parseFloat(data.get(i).get(1)));
            D.add(Boolean.parseBoolean(data.get(i).get(2)));

        }

        int lw = Math.round((data.size()/w));
        List<Integer> pos_af = new ArrayList<Integer>(Collections.nCopies(lw, 0));
        //System.out.println(pos_af.size());
        //System.out.println(lw);

        List<Float> dataRR;
        List<Boolean> dataD;
        for(int k=1; k < lw;  k++ ) {
            // take a time series of RR interval of length w
            dataRR = RR.subList(( w*(k-1)),k*w);
            System.out.println("Sublist: " + dataRR.toString());


            /* a window is defined to be in AF iif number of AF (Atrial Fibrilation) detections in data is >=w/2
            define pos_af as the boolean vector identifying the AF windows*/
            dataD = D.subList(( w*(k-1)),k*w);

            if (Collections.frequency(dataD, 1) > w/2) {
                pos_af.add(k,1);
            }

            /*Computation of covariates*/
            // Descriptive mesures

            // mean
            double mean = getMean(dataRR);

            //double lower = dataRR.get(dataRR.size() / 2 - 1);
            //double upper = dataRR.get(dataRR.size() / 2);

            //median
            double median = getMedian(dataRR);

            // person coefficient of variation
            double VRR = getSTD(dataRR)/getMean(dataRR);

            //System.out.println("Mean: " + mean);
            //System.out.println("Median: " + median);
            System.out.println("VRR: " + VRR);
            //TimeUnit.SECONDS.sleep(10);


        }

    }


    /* Descriptive mesures*/

    private static float getMean(List<Float> list) {
        float sum = 0;
        if(!list.isEmpty()) {
            for (Float element : list) {
                sum += element;
            }
            return sum / list.size();
        }
        return sum;
    }

    private static float getMedian(List<Float> list){
        Collections.sort(list);
        int middle = list.size() / 2;
        middle = middle > 0 && middle % 2 == 0 ? middle - 1 : middle;
        return list.get(middle);
    }

    private static double getSTD(List<Float> list)
    {
        float variance = 0;
        float mean = getMean(list);

        for (Float element : list) {
            variance += Math.pow(element - mean, 2);
        }
        return Math.sqrt(variance/list.size());
    }

}