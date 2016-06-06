package br.usp.falvojr.sudoku.algorithm;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import br.usp.falvojr.sudoku.heuristic.ForwardChecking;
import br.usp.falvojr.sudoku.heuristic.MinimumRemainingValues;
import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * SuDoku solver implemented using the Backtracking algorithm.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class Backtracking {

	private static final int MAX_STEPS = 1000000;

	public static boolean FLAG_STEPS;

	private final List<Integer[][]> sudokus;
	private ForwardChecking fc;
	private MinimumRemainingValues mrv;

	private int steps;

	public Backtracking(List<Integer[][]> sudokus) {
		super();
		this.sudokus = sudokus;
	}

	public Backtracking(List<Integer[][]> sudokus, ForwardChecking fc) {
		this(sudokus);
		this.fc = fc;
	}

	public Backtracking(List<Integer[][]> sudokus, ForwardChecking fc, MinimumRemainingValues mrv) {
		this(sudokus, fc);
		this.mrv = mrv;
	}

	public void setFc(ForwardChecking fc) {
		this.fc = fc;
	}

	public void setMrv(MinimumRemainingValues mrv) {
		this.mrv = mrv;
	}

	private boolean hasForwardCheking() {
		return this.fc != null;
	}

	private boolean hasMinimumRemainingValues() {
		return this.mrv != null;
	}

	private void writeConfigs() {
		final StringBuilder sbConfigs = new StringBuilder();
		if (!this.hasForwardCheking() && !this.hasMinimumRemainingValues()) {
			sbConfigs.append("sem poda");
		} else if (this.hasForwardCheking()) {
			sbConfigs.append("FC");
			if (this.hasMinimumRemainingValues()) {
				sbConfigs.append(" + MRV");
			}
		} else if (this.hasMinimumRemainingValues()) {
			sbConfigs.append("MRV");
		}
		System.out.printf("Resolvendo %1$d SuDokus com Backtracking (%2$s):%3$s%3$s", this.sudokus.size(),
		        sbConfigs.toString(), System.lineSeparator());
	}

	public void solve() {

		this.writeConfigs();

		int countSteps = 0, successSuDokus = 0;
		for (final Integer[][] sudoku : this.sudokus) {
			// if FC is present, initialize it
			if (this.hasForwardCheking()) {
				this.fc.init(sudoku);
			}
			int initialRow = 0, initialCol = 0;
			// if MRV is present, initialize it and row and col
			if (this.hasMinimumRemainingValues()) {
				this.mrv.init(this.fc.getDomains());
				final String nextKey = this.getNextMrvKey();
				initialRow = this.getMrvRow(nextKey);
				initialCol = this.getMrvCol(nextKey);
			}
			// solves in place
			if (this.isSolved(initialRow, initialCol, sudoku)) {
				SuDokus.write(sudoku);
				successSuDokus++;
			} else {
				System.err.printf("Numero de atribuicoes excede limite maximo%1$s%1$s", System.lineSeparator());
			}
			countSteps += this.steps;
			this.steps = 0;
		}

		System.out.printf("%d/%d SuDokus foram resolvidos com %d atribuicoes%s", successSuDokus, this.sudokus.size(),
		        countSteps, System.lineSeparator());
	}

	private boolean isSolved(int row, int col, Integer[][] sudoku) {
		if (this.hasMinimumRemainingValues()) {
			// MRV break criteria
			if (this.mrv.getUsedKeys().size() == this.fc.getDomains().size()) {
				return true;
			} else {
				this.mrv.burnKey(SuDokus.generateKey(row, col));
			}
		} else {
			// break criteria
			if (row == SuDokus.BOARD_SIZE) {
				row = 0;
				if (++col == SuDokus.BOARD_SIZE) {
					return true;
				}
			}
			// skip filled cells
			if (sudoku[row][col] != 0) {
				return this.next(sudoku, row, col);
			}
		}
		if (this.hasForwardCheking()) {
			final String key = SuDokus.generateKey(row, col);
			String domain;
			// try only FC domains possible values
			while (!(domain = this.fc.getDomains().get(key)).isEmpty()) {
				if (!this.hasIncreaseSteps()) {
					return false;
				}
				// copy domain to facilitate the synchronization of the FC global map on backtrack
				final Map<String, String> clonedFcDomains = this.fc.getClonedDomains();
				// copy keys to facilitate the synchronization of the MRV on backtrack
				final List<String> clonedMrvKeys = this.hasMinimumRemainingValues() ? this.mrv.getClonedUsedKeys()
				        : null;
				// get the first value on the domain
				final int value = Integer.valueOf(StringUtils.substring(domain, 0, 1));
				// attribute the value, synchronize FC domains and try solve next cell
				sudoku[row][col] = value;
				this.fc.syncDomains(row, col, value, sudoku);
				if (this.next(sudoku, row, col)) {
					return true;
				}
				// if the tested value is not a solution, restore the domains and undoes the attribution
				sudoku[row][col] = 0;
				this.fc.setDomains(clonedFcDomains);
				this.fc.syncDomainKey(key, value);
				if (this.hasMinimumRemainingValues()) {
					this.mrv.setUsedKeys(clonedMrvKeys);
				}
			}
		} else {
			// try all possible values
			for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
				if (!this.hasIncreaseSteps()) {
					return false;
				}
				// verify if attribution is legal for solve next cell
				if (SuDokus.isLegal(row, col, value, sudoku)) {
					sudoku[row][col] = value;
					if (this.next(sudoku, row, col)) {
						return true;
					}
				}
			}
		}

		// reset on backtrack
		sudoku[row][col] = 0;
		return false;
	}

	private boolean hasIncreaseSteps() {
		if (Backtracking.FLAG_STEPS && this.steps == Backtracking.MAX_STEPS) {
			return false;
		}
		this.steps++;
		return true;
	}

	private boolean next(Integer[][] sudoku, int... rowAndCol) {
		int row = 0, col = 0;
		if (this.hasMinimumRemainingValues()) {
			final String nextKey = this.getNextMrvKey();
			if (StringUtils.isNotEmpty(nextKey)) {
				row = this.getMrvRow(nextKey);
				col = this.getMrvCol(nextKey);
			}
		} else {
			row = rowAndCol[0] + 1;
			col = rowAndCol[1];
		}

		return this.isSolved(row, col, sudoku);
	}

	private int getMrvRow(final String nextKey) {
		return Integer.valueOf(nextKey.substring(0, 1));
	}

	private int getMrvCol(final String nextKey) {
		return Integer.valueOf(nextKey.substring(1));
	}

	private String getNextMrvKey() {
		final Map<String, String> ordenedDomains = this.mrv.sortByDomains(this.fc.getDomains());
		this.fc.setDomains(ordenedDomains);
		final String nextKey = this.mrv.getNextKey(this.fc.getDomains());
		return nextKey;
	}

}
