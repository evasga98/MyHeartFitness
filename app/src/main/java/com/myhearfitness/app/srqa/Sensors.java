package com.myhearfitness.app.srqa;



import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.chen0040.data.frame.BasicDataFrame;
import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataRow;

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


import static com.myhearfitness.app.srqa.LogisticRegression.*;


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

    private static void loadTimeSeries(List<List<String>> data) {
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
        //List<Integer> pos_af = new ArrayList<Integer>(Collections.nCopies(lw, 0));
        int[] pos_af = new int[lw];
        
        List<List<Double>> measure = new ArrayList<>();
        double[][] target2 = new double[166][];
        List<Boolean> dataD;
        List<Double> dataRR;
        for(int k=0; k < lw;  k++ ) {
            // take a time series of RR interval of length w

            dataRR =  RR.subList(( w*k),(k+1)*w);
            //System.out.println("Inicio: " + dataRR.toString());

            /* a window is defined to be in AF iif number of AF (Atrial Fibrilation) detections in data is >=w/2
            define pos_af as the boolean vector identifying the AF windows*/
            dataD = D.subList(( w*k),(k+1)*w);

            if (Collections.frequency(dataD, 1) > w/2) {
                pos_af[k] = 1;
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

            SRQA srqa_dec = SRQA.Recu_SRQA(srp.A.get(0), 2, 0);
            
            SRQA srqa_all = SRQA.Recu_SRQA(srp.matriz, 2, 0);
            
            List<Double> m_k = new ArrayList<>();
            m_k.addAll(Arrays.asList(mean, median, VRR, VmeRR));
            m_k.addAll(srp.SRR);
            m_k.addAll(Arrays.asList(srqa_all.DET, srqa_all.ENTR, 
                    srqa_inc.V.get(0).get(2), srqa_dec.V.get(0).get(2),
                    srqa_inc.V.get(0).get(3), srqa_dec.V.get(0).get(3)));
            
            measure.add(m_k);
            double[] target = new double[m_k.size()];
            for (int i = 0; i < target.length; i++) {
                target[i] = m_k.get(i);                // java 1.5+ style (outboxing)
            }
            target2[k] = target;




        }


        System.out.println(target2.length);
        System.out.println(target2[0].length);

        double[] w2 =  {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
        float[][]x  = {{0.8f,0.2f,0.3f,0.1f,0.1f,1f},{0.3f,0.1f,0.4f,0.3f,0.2f,1f},{0.1f,0.2f,0.3f,0.3f,0.7f,1f},{0.1f,0.3f,0.1f,0.5f,0.7f,1f}};
        float[] label = {1,1,0,0};
        int i = 0;
        while(i<100){
            w2=getNewW(w2,target2,pos_af);
            System.out.println("w :"+Arrays.toString(w2));
            System.out.println("y : "+Arrays.toString(printy(w2,target2)));
            System.out.println("loss £º"+loss(w2,target2,pos_af));
            i++;
        }

    }





}