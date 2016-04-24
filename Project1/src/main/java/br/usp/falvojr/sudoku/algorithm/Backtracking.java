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
		System.err.println("Numero de atribuicoes excede limite maximo");
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
	    return this.next(row, col, sudoku);
	}

	if (this.hasForwardCheking()) {
	    final String key = SuDokus.generateKey(row, col);
	    String domain;
	    // try only FC domains possible values
	    while (!(domain = this.fc.getDomains().get(key)).isEmpty()) {
		this.steps++;
		// clone domain to facilitate the synchronization of the global map
		final HashMap<String, String> clonedDomains = this.fc.getClonedDomains();
		// get the first value on the domain
		final int value = Integer.valueOf(StringUtils.substring(domain, 0, 1));
		// attribute the value, synchronize FC domains and try solve next cell
		sudoku[row][col] = value;
		this.fc.syncDomains(row, col, value, sudoku);
		if (this.next(row, col, sudoku)) {
		    return true;
		}
		// if the tested value is not a solution, restore the domains and undoes the attribution
		sudoku[row][col] = 0;
		this.fc.setDomains(clonedDomains);
		this.fc.syncDomainKey(key, value);
	    }
	} else {
	    // try all possible values
	    for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
		this.steps++;
		// verify if attribution is legal for solve next cell
		if (SuDokus.isLegal(row, col, value, sudoku)) {
		    sudoku[row][col] = value;
		    if (this.next(row, col, sudoku)) {
			return true;
		    }
		}
	    }
	}

	// reset on backtrack
	sudoku[row][col] = 0;
	return false;
    }

    private boolean next(int row, int col, Integer[][] sudoku) {
	return this.isSolved(row + 1, col, sudoku);
    }

}
