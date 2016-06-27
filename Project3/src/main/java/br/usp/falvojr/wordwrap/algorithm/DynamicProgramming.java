package br.usp.falvojr.wordwrap.algorithm;

import java.util.List;

/**
 * Word wrap algorithm using Dynamic Programming (DP) strategy..
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class DynamicProgramming {

    private int l;
    private List<String> words;

    public void solveWordWrap() {
        final int[] wordSizes = this.words.stream().mapToInt(word -> {
            return word.length();
        }).toArray();

        // For simplicity, 1 extra space is used in all below arrays
        final int n = this.words.size();

        // extras[i][j] will have number of extra spaces if words from i to j are put in a single line
        final int[][] extras = new int[n + 1][n + 1];

        // lc[i][j] will have cost of a line which has words from i to j
        final int[][] lc = new int[n + 1][n + 1];

        // c[i] will have total cost of optimal arrangement of words from 1 to i
        final int[] c = new int[n + 1];

        // p[] is used to print the solution.
        final int[] p = new int[n + 1];

        int i, j;

        // calculate extra spaces in a single line. The value extra[i][j] indicates extra spaces if words from word number i to j are placed in a single line
        for (i = 1; i <= n; i++) {
            extras[i][i] = this.l - wordSizes[i - 1];
            for (j = i + 1; j <= n; j++) {
                extras[i][j] = extras[i][j - 1] - wordSizes[j - 1] - 1;
            }
        }

        // Calculate line cost corresponding to the above calculated extra spaces. The value lc[i][j] indicates cost of putting words from word number i to j in
        // a single line
        for (i = 1; i <= n; i++) {
            for (j = i; j <= n; j++) {
                if (extras[i][j] < 0) {
                    lc[i][j] = Integer.MAX_VALUE;
                } else if (j == n && extras[i][j] >= 0) {
                    lc[i][j] = 0;
                } else {
                    lc[i][j] = extras[i][j] * extras[i][j];
                }
            }
        }

        // Calculate minimum cost and find minimum cost arrangement. The value c[j] indicates optimized cost to arrange words from word number 1 to j.
        c[0] = 0;
        for (j = 1; j <= n; j++) {
            c[j] = Integer.MAX_VALUE;
            for (i = 1; i <= j; i++) {
                if (c[i - 1] != Integer.MAX_VALUE && lc[i][j] != Integer.MAX_VALUE && (c[i - 1] + lc[i][j] < c[j])) {
                    c[j] = c[i - 1] + lc[i][j];
                    p[j] = i;
                }
            }
        }

        printSolution(p, n);
    }

    int printSolution (int p[], int n)
    {
        int k;
        if (p[n] == 1) {
            k = 1;
        } else {
            k = printSolution(p, p[n]-1) + 1;
        }
        System.out.printf("Line number %d: From word no. %d to %d \n", k, p[n], n);
        return k;
    }

    /**
     * Private constructor for Singleton Pattern.
     */
    private DynamicProgramming() {
        super();
    }

    private static class DynamicProgramingHolder {
        public static final DynamicProgramming INSTANCE = new DynamicProgramming();
    }

    public static DynamicProgramming getInstance(final int l, final List<String> words) {
        final DynamicProgramming instance = DynamicProgramingHolder.INSTANCE;
        instance.l = l;
        instance.words = words;
        return instance;
    }

}
