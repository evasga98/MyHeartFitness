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
import java.util.stream.DoubleStream;

import static com.myhearfitness.app.srqa.functions.*;

public class Sensors {
    List<Boolean> dataD;
    List<Double> dataRR;

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

        for(int i=0; i < data.size();  i++ ) {
            RR.add(Double.parseDouble(data.get(i).get(1)));
            D.add(Boolean.parseBoolean(data.get(i).get(2)));
        }

        int lw = Math.round((data.size()/w));
        List<Integer> pos_af = new ArrayList<Integer>(Collections.nCopies(lw, 0));

        List<Boolean> dataD;
        List<Double> dataRR;
        for(int k=1; k < lw;  k++ ) {
            // take a time series of RR interval of length w
            dataRR =  RR.subList(( w*(k-1)),k*w);
            //System.out.println("Inicio: " + dataRR.toString());

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

            //median
            List<Double> list = new ArrayList<>(dataRR);
            double median = getMedian(list);

            // person coefficient of variation
            double VRR = getSTD(dataRR)/getMean(dataRR);

            //mean absolute dispersion with respect to the median
            double VmeRR = getMeanAbsDispersion(dataRR, median);

            int m = 3;
            int d = 1;
            SRP srp = SRP.funcSRP(dataRR, m, d);

            int[][] Ai = srp.A.get((int) (factorial(m)-1));

            SRQA srqa = SRQA.Recu_SRQA(Ai, 2, 0);
            System.out.println("RR " + srqa.RR);
            System.out.println("DET " +srqa.DET);
            System.out.println("DEThat " + srqa.DEThat);
            System.out.println("ENTR " + srqa.ENTR);
            System.out.println("L " +srqa.L);
            System.out.println("Lhat " +srqa.Lhat);
            System.out.println("V " +srqa.V.toString());
            System.out.println("dd " +Arrays.toString(srqa.dd));
            System.out.println("nd " +Arrays.toString(srqa.nd));

        }
    }

}