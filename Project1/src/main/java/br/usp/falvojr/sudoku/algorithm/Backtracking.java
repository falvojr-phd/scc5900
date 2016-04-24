package br.usp.falvojr.sudoku.algorithm;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.usp.falvojr.sudoku.heuristic.impl.ForwardChecking;
import br.usp.falvojr.sudoku.heuristic.impl.MinimumRemainingValues;
import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * SuDoku solver implemented using the Backtracking algorithm.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class Backtracking {

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
	System.out.printf("Resolvendo %1$d SuDokus com Backtracking (%2$s):%3$s%3$s", this.sudokus.size(), sbConfigs.toString(), System.lineSeparator());
    }

    public void solve() {

	this.writeConfigs();

	for (final Integer[][] sudoku : this.sudokus) {
	    if (this.hasForwardCheking()) {
		this.fc.init(sudoku);
	    }

	    // solves in place
	    if (this.isSolved(0, 0, sudoku)) {
		SuDokus.write(sudoku);
	    } else {
		System.err.println("Solution not found!");
	    }
	}

	System.out.println(this.steps);
    }

    private boolean isSolved(int row, int col, Integer[][] sudoku) {
	// break criteria
	if (row == SuDokus.BOARD_SIZE) {
	    row = 0;
	    if (++col == SuDokus.BOARD_SIZE) {
		return true;
	    }
	}
	// skip filled cells
	if (sudoku[row][col] != 0) {
	    return this.isSolved(row + 1, col, sudoku);
	}

	if (this.fc == null) {
	    for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
		this.steps++;
		if (SuDokus.isLegal(row, col, value, sudoku)) {
		    sudoku[row][col] = value;
		    if (this.isSolved(row + 1, col, sudoku)) {
			return true;
		    }
		}
	    }
	} else {
	    final String key = SuDokus.generateKey(row, col);
	    String keyValue;
	    while (!(keyValue = this.fc.getDomains().get(key)).isEmpty()) {
		this.steps++;
		final HashMap<String, String> clonedDomain = this.fc.getClonedDomains();
		final int value = Integer.valueOf(StringUtils.substring(keyValue, 0, 1));
		sudoku[row][col] = value;
		this.fc.sync(row, col, value, sudoku);
		if (this.isSolved(row + 1, col, sudoku)) {
		    return true;
		}
		sudoku[row][col] = 0;
		this.fc.setDomains(clonedDomain);
		this.fc.syncKey(key, value);
	    }
	}

	// reset on backtrack
	sudoku[row][col] = 0;
	return false;
    }

}
