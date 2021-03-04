package com.myhearfitness.app.srqa;

import java.util.Arrays;


public class LogisticRegression {

    /**data
     * 0.8   0.2   0.3   0.1   0.1   1
     * 0.3   0.1   0.4   0.3  0.2    1
     * 0.1   0.2   0.3   0.3   0.7   0
     * 0.1   0.3   0.1   0.5  0.7    0
     */

    public static double[]   printy(double[] w,double[][] x){
        double[] y = new double[x.length];
        for(int i = 0 ;i < x.length;i++){
            double predict = 0;
            for(int j = 0;j<x[i].length;j++){
                predict+=w[j]*x[i][j];
            }
            y[i] = sigmod(predict);
        }
        return y;
    }
    public static  double loss(double[] w,double[][] x,int[] label){
        double losssum = 0;
        for(int i = 0;i<label.length;i++){
            int j = 0;
            double sum = 0;
            while(j<w.length){
                sum+=w[j]*x[i][j];
                j++;
            }
            losssum += label[i]*Math.log(sigmod(sum))+(1-label[i])*Math.log(1-sigmod(sum));

        }
        return -losssum/4;
    }
    public static  double[]  getNewW(double[] w,double[][] x,int[] label){
        double[] gradient = new double[w.length];
        double a = 0.4;
        double[] temp = new double[x.length];
        for(int i = 0; i<x.length;i++){
            double[] xi = x[i];
            double sum = 0;
            for(int j = 0;j <xi.length;j++){
                sum+=w[j]*xi[j];
            }
            temp[i]= label[i]-sigmod(sum);
        }
        for(int i = 0;i<gradient.length;i++){
            double sum = 0;
            for(int j = 0;j<x.length;j++){
                sum+=x[j][i]*temp[j];
            }
            gradient[i] = sum;
        }
        for(int i = 0;i<w.length;i++){
            w[i]=w[i]+a*gradient[i];
        }
        return w;
    }
    public static double sigmod(double x){
        return (double) (1/(1+Math.exp(-x)));
    }

}