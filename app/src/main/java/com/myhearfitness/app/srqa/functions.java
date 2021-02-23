package com.myhearfitness.app.srqa;

import java.util.Collections;
import java.util.List;

public class functions {
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
}
