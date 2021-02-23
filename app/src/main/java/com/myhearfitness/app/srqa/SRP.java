package com.myhearfitness.app.srqa;

import static com.myhearfitness.app.srqa.functions.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SRP {
    public static  void funcSRP(List<Double> x, int m,int d) {
        int T = x.size();

        List<Double> dataRR;
        List<Double> me = new ArrayList<>();
        List<Double> dt = new ArrayList<>();
        List<Double> cv = new ArrayList<>();
        double mean;
        double std;

        int ii = 0;
        for (int i = 1; i <= T -( d * (m - 1)); i++) {
            ii += 1;
            //System.out.println("List: " + x.toString());
            dataRR = x.subList(i - 1, i + d * (m - 1));
            //System.out.print("Sublist: " + dataRR.toString());
            List<Double> list = new ArrayList<>(dataRR);
            Collections.sort(list);

            mean = getMean(list);
            std = getSTD(list);

            me.add(mean);
            dt.add(std);
            cv.add(mean/std);
        }

    }

}
