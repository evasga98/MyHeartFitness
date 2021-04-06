package com.myhearfitness.app.srqa;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class functions {

    public static int sumList(List<Integer> list) {
        int sum = 0;
        if(!list.isEmpty()) {
            for (Integer element : list) {
                sum += element;
            }
            return sum;
        }
        return sum;
    }

    public static double getMean(List<Double> list) {
        double sum = 0;
        if(!list.isEmpty()) {
            for (Double element : list) {
                sum += element;
            }
            return sum / list.size();
        }
        return sum;
    }

    public static double getMedian(List<Double> list){
       Collections.sort(list);
        int middle = list.size() / 2;
        middle = middle > 0 && middle % 2 == 0 ? middle - 1 : middle;
        return list.get(middle);
    }

    public static double getSTD(List<Double> list)
    {
        double variance = 0;
        double mean = getMean(list);
        if(!list.isEmpty()) {
            for (Double element : list) {
                variance += Math.pow(element - mean, 2);
            }
        }
        return Math.sqrt(variance/list.size());
    }

    public static double getMeanAbsDispersion(List<Double> list, double median) {
        double sum = 0;
        if(!list.isEmpty()) {
            for (Double element : list) {
                sum += Math.abs(element-median);
            }
            return sum / median;
        }
        return sum;
    }

    public static long factorial(int n) {
        if (n <= 2) {
            return n;
        }
        return n * factorial(n - 1);
    }

    public static int[] subArray(int[] array, int beg, int end) {
        return Arrays.copyOfRange(array, beg, end + 1);
    }

    public static int[][] listToMatrix(List<Integer> list){
        int[][] matrix = new int[list.size()][1];
        for (int i = 0 ; i < list.size(); i ++){
            matrix[i][0] = list.get(i);
        }
        return  matrix;
    }

    public static int[][] transposeMatrix(int[][] a){
        int[][] t = new int[a[0].length][a.length];
        for(int i = 0; i < a.length; i++) {
            for(int j = 0; j < a[0].length; j++) {
                t[j][i] = a[i][j];
            }

        }
        return t;
    }

    public static int[] getIncrementingArray(int start, int end){
        int[] t = new int[end - start + 1];
        for(int i = 0; i < t.length; i++) {
            t[i] = start + i;
        }
        return t;
    }

    public static int[][] sumMatrix(int[][] a, int[][] b){
        int[][] c = new int[a.length][a[0].length];
        for (int i=0; i<a.length; i++){
            for (int j=0; j<a[0].length; j++){
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    public static int[] sumRow(int[][] a){
        int [] sum = new int[a.length];

        //Calculates sum of each row of given matrix
        for(int i = 0; i < a.length; i++){
            int sumRow = 0;
            for(int j = 0; j < a[0].length; j++){
                sumRow = sumRow + a[i][j];
            }
            sum[i] = sumRow;
        }
        return  sum;
    }

    public static int sumColumn(int[] a){
        //Calculates sum of each column of given vector
        int sum = 0;
        for(int i = 0; i < a.length; i++){
            sum += a[i];
        }
        return  sum;
    }


    public static int[][] multiplyMatrix(int[][] a, int[][] b){
        int[][] product = new int[a.length][b[0].length];
        for(int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    product[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return product;
    }

}
