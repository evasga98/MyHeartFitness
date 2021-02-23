package com.myhearfitness.app.srqa;

import android.os.strictmode.ServiceConnectionLeakedViolation;

import org.apache.commons.collections.list.AbstractLinkedList;

import static com.myhearfitness.app.srqa.functions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SRP {
    public static  void funcSRP(List<Double> x, int m,int d) {
        int T = x.size();

        List<Double> sub_list;
        List<Double> me = new ArrayList<>();
        List<Double> dt = new ArrayList<>();
        List<Double> cv = new ArrayList<>();
        List<List<Integer>> mh = new ArrayList<>();
        List<Double> B;
        List<Integer> mh_list;

        double mean;
        double std;

        System.out.println("List: " + x.toString());
        int ii = 0;
        for (int i = 1; i <= T -( d * (m - 1)); i++) {
            ii += 1;

            sub_list = x.subList(i - 1, i + d * (m - 1));

            Map<Integer, Double> map = getSortedMap(sub_list);
            B  = new ArrayList<>(map.values());
            mh_list  = new ArrayList<>(map.keySet());
            mh.add(mh_list);

            mean = getMean(B);
            std = getSTD(B);

            me.add(mean);
            dt.add(std);
            cv.add(mean/std);

        }

        List<Integer> numbers = new ArrayList<>();
        for(int j = 1; j < m+1; j++){
            numbers.add(j);
        }

        List<List<Integer>> symbs = generatePerm(numbers);

        List<Integer> loc = new ArrayList<>();
        for(int k = 0; k < getFactorial(m); k++){
            loc = getMatches(mh, symbs.get(k));
            int[][] loc_arr = listToMatrix(loc);
            int[][] A = multiplyMatrix(loc_arr, transposeMatrix(loc_arr));
            System.out.println(Arrays.toString(sumRow(A)));
            //System.out.println("loc: " + loc.toString());


        }

    }

    private static List<Integer> getMatches(List<List<Integer>> list, List<Integer> element){
        List<Integer> matches = new ArrayList<>();
        for (List<Integer> i : list){
            if(i.equals(element)) matches.add(1);
            else matches.add(0);
        }
        return matches;
    }

    private static Map<Integer, Double> getSortedMap(List<Double> list){
        Map<Integer, Double> map= new HashMap<Integer, Double>();
        int k=1;
        for(double value : list){
            map.put(k++, value);
        }
        return sortByValues(map);
    }

    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        Map<K,V> sortedMap = new LinkedHashMap<K,V>();

        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static <E> List<List<E>> generatePerm(List<E> original) {
        if (original.isEmpty()) {
            List<List<E>> result = new ArrayList<>();
            result.add((List<E>) new ArrayList<>());
            return result;
        }
        E firstElement = original.remove(original.size()-1);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = generatePerm(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index=0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }


}
