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
 * @see https://www.student.cs.uwaterloo.ca/~cs466/Old_courses/F14/1-Review.pdf
 */
public class DynamicProgramming {

    private static List<Integer> memoization = new ArrayList<>();

    public static int opt(int j) {
        if (memoization.isEmpty()) {
            memoization.set(0, 0);
        }
        return memoization.get(j);
    }

    /**
     * Private constructor.
     */
    private DynamicProgramming() {
        super();
    }

}
