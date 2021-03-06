package com.myhearfitness.app.srqa;



import android.content.Context;
import android.graphics.Paint;
import android.os.Environment;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import smile.classification.LogisticRegression;

import static com.myhearfitness.app.srqa.functions.*;

public class Sensors {
    List<Boolean> dataD;
    List<Double> dataRR;

    private static final String COMMA_DELIMITER = ",";

    public static void  readCSV(Context context){
        List<List<String>> data = new ArrayList<>();
        InputStreamReader is;
        try {
            is = new InputStreamReader(context.getAssets().open("datos_2.csv"));
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
        List<Double> D = new ArrayList<>();

        // define window size
        int w = 30;

        for(int i=0; i < data.size();  i++ ) {
            RR.add(Double.parseDouble(data.get(i).get(1)));
            D.add(Double.parseDouble(data.get(i).get(2)));
        }

        int lw = Math.round((data.size()/w));

        int[] pos_af = new int[lw];
        double [][] measure = new double[lw][];

        List<Double> dataD;
        List<Double> dataRR;
        for(int k=0; k < lw;  k++ ) {
            // take a time series of RR interval of length w

            dataRR =  RR.subList(( w*k),(k+1)*w);

            /* a window is defined to be in AF iif number of AF (Atrial Fibrilation) detections in data is >=w/2
            define pos_af as the boolean vector identifying the AF windows*/
            dataD = D.subList(( w*k),(k+1)*w);

            if (Collections.frequency(dataD, 1.0) > w/2) {
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

            // mean absolute dispersion with respect to the median
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

            double[] row = new double[m_k.size()];
            for (int i = 0; i < m_k.size(); i++) {
                row[i] = m_k.get(i).doubleValue();
            }
            measure[k]  = row;

        }


        /*compute the logistic model*/
        LogisticRegression.Binomial bin = LogisticRegression.binomial(measure, pos_af,0.0, 1E-10, 500);

        /*compute the estimated probability of AF*/
        double[] prob = new double[measure.length];
        for (int i = 0; i < measure.length; i++){
            double[] posteriori = new   double[2];
            bin.predict(measure[i], posteriori);
            prob[i] = posteriori[1];
        }


        double[] Tlist = new double[(int)(1/0.001)];
        Tlist[0] = 0.001F;
        for (int i = 1; i < Tlist.length; i++){
            Tlist[i] = Tlist[i-1] + 0.001F;
        }

        double [] dista = new double[Tlist.length];
        double[] xc = new  double[Tlist.length];
        double[] yc = new  double[Tlist.length];
        double ths, TPR, FPR, SPC = 0, ACC;


        for (int i = 0; i < Tlist.length; i++){
            List<Integer> pos_af2 = new ArrayList<>();
            List<Double> TP_2 = new ArrayList<>();
            List<Double> TN_2 = new ArrayList<>();
            ths = Tlist[i];
            int TP = 0, FP=0, TN=0, FN=0;
            for (int j = 0; j < pos_af.length; j++) {
                if (pos_af[j] > 0) {
                    pos_af2.add(j);
                    if (prob[j] > ths) TP++;        /*true possitive*/
                    else if (prob[j] < ths) FN++;   /*false negative*/
                } else if (pos_af[j] == 0) {
                    if (prob[j] > ths) FP++;        /*false positive*/
                    else if (prob[j] < ths) TN++;   /*true negative*/
                }
            }

            /*specificity*/
            TPR = (double)TP/(TP+FN);
            FPR =(double) FP/(FP+TN);

            /*specificity*/
            SPC = (double) 1 - FPR;
            xc[i] = FPR;
            yc[i] = TPR;
            dista[i] = (double) ( Math.pow(FPR, 2) + Math.pow((1-TPR), 2));
        }

        /* threshold*/
        double ths0  = 0;
        List<Double> dista_list = Arrays.asList(ArrayUtils.toObject(dista));
        for (int i = 0; i < dista.length; i++){
            if (dista[i] == Collections.min(dista_list)) {
                ths0 = Tlist[i];
                break;
            }
        }

        int[] y = pos_af;
        /*compute the sensitivity, specificity and accuracy of the model*/
        int VP = 0, VN=0, FN=0, FP=0;
        for (int i = 0; i < pos_af.length; i++) {
            if (y[i] > 0) {
                if (prob[i] > ths0) VP++;
                else if (prob[i] < ths0) FN++;
            } else if (y[i] == 0) {
                if (prob[i] > ths0) FP++;
                else if (prob[i] < ths0) VN++;
            }
        }

        TPR = (double) VP/(VP+FN);               /*sensitivity*/
        FPR = (double) FP/ (FP+VN);              /*razón de falsas alarmas*/
        SPC = (double) 1-FPR;                    /*specificity*/
        ACC = (double) (VP+VN)/(VP+FN+FP+VN);    /*accuracy*/

        int AF = 0, NS =0;
        for (int i = 0; i < prob.length; i++) {
            if (prob[i] > ths0 ) AF++;
            else  NS ++;

        }

        }


}