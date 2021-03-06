package br.usp.falvojr.wordwrap.algorithm;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Word wrap algorithm using Dynamic Programming (DP) strategy. To solve each subproblem just once,
 * it is then necessary to find out and later re-use which of the lines ending with some word
 * contributes least to the overall cost. As each of the "n" words could terminate at most "n"
 * potential lines, the algorithm runs in <b>O(n^2)</b>.<br/>
 * Following its recurrence relation:<br/>
 * <br/>
 *
 * <b>OPT(j) = min { OPT(i) + BADNESS(i, j) } where 0 < i <= j</b><br/>
 * <br/>
 * <b>BADNESS(i, j) = Infinity</b> , , caso as palavras de i a j ultrapassem L<br/>
 * <b>BADNESS(i, j) = (L - LENGTH[i:j])²</b> , caso contrário
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

	public Integer opt(String[] words, int l) {
		if (this.memoization == null) {
			int i, j;
			final int count = words.length;
			final int slack[][] = new int[count][count];
			for (i = 0; i < count; i++) {
				slack[i][i] = l - words[i].length();
				for (j = i + 1; j < count; j++) {
					slack[i][j] = slack[i][j - 1] - words[j].length() - 1;
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
						cost = Integer.MAX_VALUE / 2;
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
		return this.memoization[words.length - 1];
	}

	public void printSolution(String[] words) {
		int i, j;
		final int count = words.length;
		final ArrayList<String> lines = new ArrayList<>();
		j = count;
		while (j > 0) {
			i = this.memoization[j - 1];
			final String paragraph = StringUtils.join(ArrayUtils.subarray(words, i, j), " ");
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
