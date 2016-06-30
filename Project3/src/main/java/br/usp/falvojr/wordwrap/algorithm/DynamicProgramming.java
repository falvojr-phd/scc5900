package br.usp.falvojr.wordwrap.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * The deficiency of first idea lies in that it repeatedly solves the same subproblems. Yet suppose there was an optimal configuration of lines. Plucking off
 * its last line would still keep the layout optimal because otherwise it would be possible to improve it and, together with the removed line, would result in
 * even better configuration, contradicting its optimality. To solve each subproblem just once, it is then necessary to find out and later re-use which of the
 * lines ending with some word contributes least to the overall cost. As each of the "n" words could terminate at most "n" potential lines, the algorithm runs
 * in <b>O(n^2)</b>.<br/>
 * Following its recurrence relation:<br/>
 * <br/>
 *
 * <b>OPT(j) = { 0 }</b> if j = 0<br/>
 * <b>OPT(j) = { min { OPT(i - 1) + minimum(i, j) } }</b> if j > 0 (where 1 <= i <= j)
 *
 * @author Venilton FalvoJr (falvojr)
 *
 * @see <a href="http://xxyxyz.org/line-breaking">Line breaking</a>
 */
public final class DynamicProgramming {

    private int memoization[];

    public void init() {
        this.memoization = null;
    }

    public Integer opt(List<String> words, int l) {
        if (this.memoization == null) {
            int i, j;
            final int count = words.size();
            final int slack[][] = new int[count][count];
            for (i = 0; i < count; i++) {
                slack[i][i] = l - words.get(i).length();
                for (j = i + 1; j < count; j++) {
                    slack[i][j] = slack[i][j - 1] - words.get(j).length() - 1;
                }
            }
            final int minimum[] = new int[count + 1];
            for (i = 1; i < minimum.length; i++) {
                minimum[i] = Integer.MAX_VALUE;
            }
            this.memoization = new int[count];
            for (j = 0; j < count; j++) {
                i = j;
                while (i >= 0) {
                    int cost;
                    if (slack[i][j] < 0) {
                        cost = Integer.MAX_VALUE;
                    } else {
                        cost = minimum[i] + ((int) Math.pow(slack[i][j], 2));
                    }
                    if (minimum[j + 1] > cost) {
                        minimum[j + 1] = cost;
                        this.memoization[j] = i;
                    }
                    i--;
                }
            }
        }
        return this.memoization[words.size() - 1];
    }

    public void printSolution(List<String> words) {
        int i, j;
        final int count = words.size();
        final ArrayList<String> lines = new ArrayList<>();
        j = count;
        while (j > 0) {
            i = this.memoization[j - 1];
            final String paragraph = StringUtils.join(words.subList(i, j), " ");
            lines.add(paragraph);
            j = i;
        }
        Collections.reverse(lines);

        final String solution = StringUtils.join(lines, System.lineSeparator());

        System.out.println(solution);
        System.out.println();
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
