package com.myhearfitness.app.srqa;

import static com.myhearfitness.app.srqa.functions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.DoubleStream;

/* Recurrence quantification analysis of symbolic recurrence plots
         RP:  the Symbolic Recurrence Plot
         I:   the indication marks (I=0 RP is the symmetry matrix
                                    I=1 RP is the asymmetry matrix)
         RR:   Recurrence rate RR, The percentage of recurrence points in an RP
               Corresponds to the correlation sum;
         DET:  Determinism DET, The percentage of recurrence points which form
               diagonal lines
         ENTR: Entropy ENTR, The Shannon entropy of the probability distribution of the diagonal
               line lengths p(l)
         L:    Averaged diagonal line length L, The average length of the diagonal lines
         V: contains the distribution of vertical lines together with its entropy
         see below*/



public class SRQA {

    public double RR;
    public double DET;
    public double DEThat;
    public double ENTR;
    public double L;
    public double Lhat;
    public List<List<Double>> V;
    public int[] dd;
    public int[] nd;

    public SRQA() {

    }

    public SRQA(double RR, double DET,double DEThat, double ENTR, double L, double Lhat, List<List<Double>> V, int[] dd, int[] nd) {
        this.RR = RR;
        this.DET = DET;
        this.DEThat = DEThat;
        this.ENTR = ENTR;
        this.L = L;
        this.Lhat = Lhat;
        this.V = V;
        this.dd = dd;
        this.nd = nd;
    }


    public static SRQA Recu_SRQA(int[][] RP, int Lmin, int I) {

        int N1 = RP.length;
        int[] Yout;
        int[] S = new int[N1];

        Yout = getYout(N1, RP);

        if (I==0)
        {
            for (int i = 0; i < N1; i++)
            {
                S[i] = Yout[i]*2;
            }
        }


        if(I==1)
        {
            int[][] RP_transpose = transposeMatrix(RP);
            Yout = getYout( RP_transpose.length, RP_transpose);
            S = Yout;
        }

        /*calculate the recurrence rate (RR)*/
        int SR = 0;
        for (int i = 1; i<=N1; i++)
        {
            SR = SR + i*S[i-1];
        }
        double RR =(double) SR/(N1*(N1-1));

        /*calculate the determinism (%DET)*/
        double DET = 0.0;
        if (SR!=0)  DET = (double) (SR- sumColumn(Arrays.copyOfRange(S, 0,  Lmin-1)))/SR;
        double DEThat = (double) (SR- sumColumn(Arrays.copyOfRange(S, 0,  Lmin-1)))/N1;

        /*calculate the ENTR = entropy (ENTR)*/
        double ENTR = 0;
        int sumS = sumColumn(S);
        double[] pp = new double[N1];
        for (int i = 0; i < N1; i++)
        {
            pp[i] = (double) S[i]/sumS;
        }

        //get index of values not equal to 0
        List<Integer> F = new ArrayList<>();
        int[] S_subarray = Arrays.copyOfRange(S, Lmin-1,  N1-1);
        for (int j = 0; j < S_subarray.length; j++)
        {
            if(S_subarray[j] !=0 ) {  F.add(j);}
        }


        int[] dd = new int[0];
        int[] nd = getIncrementingArray(2, S.length+1);;
        double[] pp_subarray = new double[F.size()];
        if(F.size() != 0)
        {
            for (int m = 0; m < F.size(); m++)
            {
                F.set(m, F.get(m) +Lmin -1);
                pp_subarray[m] = pp[F.get(m)];
            }

            for (int n = 0; n < pp_subarray.length; n++)
            {
                ENTR += pp_subarray[n]*Math.log(pp_subarray[n]);
            }
            ENTR = ENTR*(-1);
            dd = Arrays.copyOfRange(S, F.get(0),  F.get(F.size()-1)+1);

        }


        /*calculate Averaged diagonal line length (L)*/
        double L = 0;
        double Lhat = 0;
        if (sumColumn(S_subarray) != 0)
        {
            int [] inc = getIncrementingArray(1, Lmin-1);
            int[] S_1 = Arrays.copyOfRange(S, 0,  Lmin-1);
            int[] S_2 = Arrays.copyOfRange(S, Lmin-1,  S.length-1);

            double s = 0;
            for(int i=0; i < S_1.length; i++)
            {
                s += inc[i]*S_1[i];
            }
            L += (SR - s) /sumColumn(S_2);
            Lhat = L/N1;
        }

        /*calculate vertical line length distributions (V)*/
        int [] RPvi = new int[RP.length];
        List<List<Integer>> cv = new ArrayList<>();
        List<Integer> VV = new ArrayList<>();

        for (int i = 0; i < N1; i++)
        {
            for (int j = 0; j < RP.length; j++)
            {
                RPvi[j] = RP[j][i];
            }

            cv = cellfun(RPvi);

            int[] v = new int[cv.size()];

            for (int k = 0; k < cv.size(); k++)
            {
                v[k] = sumList(cv.get(k));
            }

            for (int l = 0; l < v.length; l++)
            {
                if(v[l] > 1){
                    VV.add(v[l]);
                }
            }

        }

        List<Integer> av_list = new ArrayList<>(new HashSet<>(VV));
        Integer[] av = new Integer[av_list.size()];
        av_list.toArray(av);
        int[] sv = new int[VV.size()];
        for (int i = 0; i < VV.size(); i++)
        {
            for (int j = 0; j < av.length; j++)
            {
               if(av[j]==VV.get(i)){
                   sv[i] = j + 1;
               }
            }
        }
        int[] distV = hist(sv, av.length);


        double[] pV = new double[distV.length];
        for (int i = 0; i < pV.length; i++)
        {


            pV[i] = (double) distV[i]/sumColumn(distV);
        }


        double entV = 0;
        for (int i = 0; i < distV.length; i++)
        {
            if(pV[i] == 0) pV[i] = 1;
            entV += pV[i] * Math.log(pV[i]);
        }
        if (entV !=0) entV = entV*(-1);


        int[] sumV_arr= new int[av.length];
        for (int i = 0; i < av.length; i++)
        {
            sumV_arr[i] += av[i] * distV[i];
        }

        int sumV = 0;
        if(sumColumn(distV)!= 0) sumV = sumColumn(sumV_arr)/sumColumn(distV);

        List<List<Double>> V = new ArrayList<>();
        if(av.length == 0 )  V.add(new ArrayList<>(Arrays.asList(0.0, 1.0, entV, (double)sumV)));
        else {
            for (int i = 0; i < av.length; i++)
            {
            List<Double> list = new ArrayList<>(Arrays.asList((double)av[i], (double)distV[i], entV, (double)sumV));
            V.add(list);
            }
        }



        return new SRQA(RR, DET, DEThat, ENTR, L, Lhat, V, dd, nd);



    }
    private static int[] hist(int[] scores, int n) {
        int[] hist = new int[n];
        for (int score : scores)
        {
            hist[score -1]++;
        }
        return hist;
    }


    private static List<List<Integer>> cellfun(int[] RPvi){
        List<List<Integer>> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();
        a.add(b);
        b = new ArrayList<>();
        int pevious = 0;
        for (int i = 0; i < RPvi.length; i++)
        {
            if(RPvi[i] == 1){ b.add(1);}
            else {
                if(pevious == 1) a.add(b);

                b = new ArrayList<>();
            }
            pevious = RPvi[i];

        }
        a.add(b);
        //b = new ArrayList<>();
        //a.add(b);
        return a;
    }

    private static int[] getYout(int N1, int[][] RP){
        int[] Yout = new int[N1];
        for (int k = 2; k < N1; k++)
        {
            int On = 1;
            while (On <= (N1+1-k))
            {
                if(RP[On-1][k+On-2]== 1)
                {
                    int A = 1; int off = 0;
                    while((off==0) && (On != N1+1-k))
                    {
                        if(RP[On][k+On-1] == 1)
                        {
                            A +=1; On +=1;
                        } else {
                            off = 1;
                        }
                    }
                    Yout[A-1] = Yout[A-1] + 1;
                }
                On += 1;
            }
        }
        return Yout;

    }
}
