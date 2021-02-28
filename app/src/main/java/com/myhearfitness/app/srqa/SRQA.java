package com.myhearfitness.app.srqa;

import static com.myhearfitness.app.srqa.functions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

    public static void Recu_SQRA(int[][] RP, int Lmin, int I) {

        int N1 = RP.length;
        int[] Yout = new int[N1];
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
        //System.out.println(Arrays.toString(Yout));
        //System.out.println(Arrays.toString(S));

        /*calculate the recurrence rate (RR)*/
        int SR = 0;
        for (int i = 1; i<=N1; i++)
        {
            SR = SR + i*S[i-1];
        }
        //System.out.println(SR);
        int RR = SR/(N1*(N1-1));

        /*calculate the determinism (%DET)*/
        double DET = 0.0;
        if (SR!=0)  DET = (double) (SR- sumColumn(Arrays.copyOfRange(S, 0,  Lmin-1)))/SR;
        double DEThat = (double) (SR- sumColumn(Arrays.copyOfRange(S, 0,  Lmin-1)))/N1;

        /*calculate the ENTR = entropy (ENTR)*/
        double entropy = 0;
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

        if(F.size() != 0)
        {
            for (int m = 0; m < F.size(); m++)
            {
                F.set(m,  F.get(m) + Lmin -1);
            }
            double[] pp_subarray = Arrays.copyOfRange(pp, F.get(0),  F.get(F.size()-1)+1);

            for (int n = 0; n < pp_subarray.length; n++)
            {
                entropy += pp_subarray[n]*Math.log(pp_subarray[n]);
            }
            entropy = entropy*(-1);
            int[] dd = Arrays.copyOfRange(S, F.get(0),  F.get(F.size()-1)+1);
            int[] nd = getIncrementingArray(2, S.length+1);
        }

        /*calculate Averaged diagonal line length (L)*/
        double L = 0;
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
            double Lhat = L/N1;
        }

        /*calculate vertical line length distributions (V)*/
        int [] RPvi = new int[RP.length];
        List<List<Integer>> cv;
        List<Integer> VV = new ArrayList<>();

        for (int i = 0; i < N1; i++)
        {
            for (int j = 0; j < RP.length; j++)
            {
                RPvi[j] = RP[j][i];
            }

            cv = cellfun(RPvi);
            //System.out.println(Arrays.toString(RPvi));
            //System.out.println(cv.toString());

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
        for (int i = 0; i < distV.length; i++)
        {
            pV[i] = (double) distV[i]/sumColumn(distV);
        }

        System.out.println(Arrays.toString(pV));


    }
    public static int[] hist(int[] scores, int n) {
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
        b = new ArrayList<>();
        a.add(b);
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
