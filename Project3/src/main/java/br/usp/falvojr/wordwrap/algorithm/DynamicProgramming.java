package br.usp.falvojr.wordwrap.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Word wrap algorithm using Dynamic Programming (DP) strategy. Following its recurrence relation:<br/>
 * <br/>
 *
 * <b>OPT(j) = { 0 }</b> if j = 0<br/>
 * <b>OPT(j) = { min { OPT(i - 1) + lowCost(i, j) } }</b> if j > 0 (where 1 <= i <= j)
 *
 * @author Venilton FalvoJr (falvojr)
 * @see http://xxyxyz.org/line-breaking/
 */
public class DynamicProgramming {

    private static List<Integer> memoization = new ArrayList<>();

    //    def dynamic(text, width):
    //        words = text.split()
    //        count = len(words)
    //        slack = [[0] * count for i in range(count)]
    //        for i in range(count):
    //            slack[i][i] = width - len(words[i])
    //            for j in range(i + 1, count):
    //                slack[i][j] = slack[i][j - 1] - len(words[j]) - 1
    //
    //        minima = [0] + [10 ** 20] * count
    //        breaks = [0] * count
    //        for j in range(count):
    //            i = j
    //            while i >= 0:
    //                if slack[i][j] < 0:
    //                    cost = 10 ** 10
    //                else:
    //                    cost = minima[i] + slack[i][j] ** 2
    //                if minima[j + 1] > cost:
    //                    minima[j + 1] = cost
    //                    breaks[j] = i
    //                i -= 1
    //
    //        lines = []
    //        j = count
    //        while j > 0:
    //            i = breaks[j - 1]
    //            lines.append(' '.join(words[i:j]))
    //            j = i
    //        lines.reverse()
    //        return lines

    public void solve(List<String> words, int width) {
        final int count = words.size();
        final int slack[][] = new int[count][count];
        for (int i = 0; i < count; i++) {
            slack[i][i] = width - words.get(i).length();
            for (int j = i + 1; i < count; j++) {
                slack[i][j] = slack[i][j - 1] - words.get(j).length() - 1;
            }
        }
        for (int i = 0; i < slack.length; i++) {
            for (int j = 0; j < slack.length; j++) {
                System.out.printf("/t %d", slack[i][j]);
            }
            System.out.println();
        }
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

    public static DynamicProgramming getInstance() {
        return DynamicProgramingHolder.INSTANCE;
    }

}
