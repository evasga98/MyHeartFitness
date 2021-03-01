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

            SRQA srqa_inc = SRQA.Recu_SRQA(Ai, 2, 0);
//            System.out.print("RR " + srqa_inc.RR);
//            System.out.print(" DET " +srqa_inc.DET);
//            System.out.print(" DEThat " + srqa_inc.DEThat);
//            System.out.print(" ENTR " + srqa_inc.ENTR);
//            System.out.print(" L " +srqa_inc.L);
//            System.out.print(" Lhat " +srqa_inc.Lhat);
//            System.out.print(" V " +srqa_inc.V.toString());
//            System.out.print(" dd " +Arrays.toString(srqa_inc.dd));
//            System.out.println(" nd " +Arrays.toString(srqa_inc.nd));
//            System.out.println("                 ");

            SRQA srqa_dec = SRQA.Recu_SRQA(srp.A.get(0), 2, 0);
//            System.out.print("RR " + srqa_dec.RR);
//            System.out.print(" DET " +srqa_dec.DET);
//            System.out.print(" DEThat " + srqa_dec.DEThat);
//            System.out.print(" ENTR " + srqa_dec.ENTR);
//            System.out.print(" L " +srqa_dec.L);
//            System.out.print(" Lhat " +srqa_dec.Lhat);
//            System.out.print(" V " +srqa_dec.V.toString());
//            System.out.print(" dd " +Arrays.toString(srqa_dec.dd));
//            System.out.println(" nd " +Arrays.toString(srqa_dec.nd));
//            System.out.println("                  ");

            SRQA srqa_all = SRQA.Recu_SRQA(srp.matriz, 2, 0);
//            System.out.print("RR " + srqa_all.RR);
//            System.out.print(" DET " +srqa_all.DET);
//            System.out.print(" DEThat " + srqa_all.DEThat);
//            System.out.print(" ENTR " + srqa_all.ENTR);
//            System.out.print(" L " +srqa_all.L);
//            System.out.print(" Lhat " +srqa_all.Lhat);
//            System.out.print(" V " +srqa_all.V.toString());
//            System.out.print(" dd " +Arrays.toString(srqa_all.dd));
//            System.out.println(" nd " +Arrays.toString(srqa_all.nd));
//            System.out.println("                  ");

        }
    }

}