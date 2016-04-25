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

    private static final int MAX_STEPS = 1000000;

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
	System.out.printf("Resolvendo %1$d SuDokus com Backtracking (%2$s):%3$s%3$s", this.sudokus.size(), sbConfigs.toString(), System.lineSeparator());
    }

    public void solve() {

	this.writeConfigs();
	int countSteps = 0, successSuDokus = 0;
	for (final Integer[][] sudoku : this.sudokus) {
	    // if FC is present, initialize it
	    if (this.hasForwardCheking()) {
		this.fc.init(sudoku);
	    }
	    // solves in place
	    if (this.isSolved(0, 0, sudoku)) {
		SuDokus.write(sudoku);
		successSuDokus++;
	    } else {
		System.err.printf("Numero de atribuicoes excede limite maximo%1$s%1$s", System.lineSeparator());
	    }
	    countSteps += this.steps;
	    this.steps = 0;
	}

	System.out.printf("%d/%d SuDokus foram resolvidos com %d atribuicoes%s", successSuDokus, this.sudokus.size(), countSteps,  System.lineSeparator());
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
		if(!this.hasIncreaseSteps()) {
		    return false;
		}
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
		if(!this.hasIncreaseSteps()) {
		    return false;
		}
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

    private boolean hasIncreaseSteps() {
	if (this.steps == MAX_STEPS) {
	    return false;
	}
	this.steps++;
	return true;
    }

    private boolean next(int row, int col, Integer[][] sudoku) {
	return this.isSolved(row + 1, col, sudoku);
    }

}
