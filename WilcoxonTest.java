import java.util.*;

public class WilcoxonTest {
/*     public static void main(String[] args) {
        double[] x = {85, 70, 65, 90, 95}; // sample 1
        double[] y = {80, 60, 70, 85, 100}; // sample 2

        double pValue = wilcoxonSignedRankTest(x, y);
        System.out.println("P-Value: " + pValue);
    } */

    public double sample1[];
    public double sample2[];

    public WilcoxonTest(double s1[], double s2[]){
        this.sample1 = new double[s1.length];
        this.sample2 = new double[s2.length];

        for(int i=0; i<s1.length; i++){
            this.sample1[i] = s1[i];
            this.sample2[i] = s2[i];
        }
    }

    public double wilcoxonSignedRankTest(double[] x, double[] y) {
        int n = x.length;
        double[] diffs = new double[n];
        double[] absDiffs = new double[n];
        int nonZeroCount = 0;

        // Step 1: Calculate differences and absolute differences
        for (int i = 0; i < n; i++) {
            diffs[i] = x[i] - y[i];
            absDiffs[i] = Math.abs(diffs[i]);
            if (absDiffs[i] != 0) {
                nonZeroCount++;
            }
        }

        // Step 2: Rank absolute differences
        double[] ranks = rank(absDiffs);

        // Step 3: Sum positive and negative ranks
        double Wplus = 0, Wminus = 0;
        for (int i = 0; i < n; i++) {
            if (diffs[i] > 0) {
                Wplus += ranks[i];
            } else if (diffs[i] < 0) {
                Wminus += ranks[i];
            }
        }

        double W = Math.min(Wplus, Wminus);

        // Step 4: Use normal approximation for large n
        double mean = nonZeroCount * (nonZeroCount + 1) / 4.0;
        double stdDev = Math.sqrt(nonZeroCount * (nonZeroCount + 1) * (2 * nonZeroCount + 1) / 24.0);

        double z = (W - mean) / stdDev;
        double pValue = 2 * cumulativeStandardNormal(-Math.abs(z)); // two-tailed

        return pValue;
    }

    // Rank absolute differences (handling ties simply)
    public static double[] rank(double[] values) {
        int n = values.length;
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;

        Arrays.sort(indices, Comparator.comparingDouble(i -> values[i]));

        double[] ranks = new double[n];
        for (int i = 0; i < n; i++) {
            ranks[indices[i]] = i + 1;
        }

        return ranks;
    }

    // Standard normal CDF using error function approximation
    public static double cumulativeStandardNormal(double z) {
        return 0.5 * (1.0 + erf(z / Math.sqrt(2)));
    }

    // Approximation of the error function
    public static double erf(double z) {
        // Abramowitz and Stegun formula 7.1.26
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));
        double ans = 1 - t * Math.exp(-z * z - 1.26551223 +
                t * (1.00002368 +
                t * (0.37409196 +
                t * (0.09678418 +
                t * (-0.18628806 +
                t * (0.27886807 +
                t * (-1.13520398 +
                t * (1.48851587 +
                t * (-0.82215223 +
                t * 0.17087277)))))))));
        return z >= 0 ? ans : -ans;
    }
}

