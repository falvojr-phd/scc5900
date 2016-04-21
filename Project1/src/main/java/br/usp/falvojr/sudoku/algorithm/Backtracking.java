package br.usp.falvojr.sudoku.algorithm;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import br.usp.falvojr.sudoku.heuristic.impl.ForwardChecking;
import br.usp.falvojr.sudoku.heuristic.impl.MinimumRemainingValues;
import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * SuDoku solver implemented using the Backtracking algorithm.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class Backtracking {

    private Stream<Integer[][]> sudokus;

    private ForwardChecking fc;
    @SuppressWarnings("unused")
    private MinimumRemainingValues mrv;

    public Backtracking(Stream<Integer[][]> sudokus) {
	super();
	this.sudokus = sudokus;
    }

    public Backtracking(Stream<Integer[][]> sudokus, ForwardChecking fc) {
	this(sudokus);
	this.fc = fc;
    }

    public Backtracking(Stream<Integer[][]> sudokus, ForwardChecking fc, MinimumRemainingValues mrv) {
	this(sudokus, fc);
	this.mrv = mrv;
    }

    public void solve() {
	this.sudokus.forEach(sudoku -> {

	    if (this.fc != null) {
		this.fc.init(sudoku);
	    }

	    final long startTime = System.nanoTime();
	    // solves in place
	    if (isSolved(0, 0, sudoku)) {
		SuDokus.write(sudoku, true);
	    } else {
		System.err.println("Solution not found!");
	    }

	    final long endTime = System.nanoTime();

	    final long elapsedTime = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);

	    System.out.printf("\n%.2f seconds\n\n", elapsedTime / 1000D);
	});
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
	    return isSolved(row + 1, col, sudoku);
	}

	if (this.fc == null) {
	    for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
		if (SuDokus.isLegal(row, col, value, sudoku)) {
		    sudoku[row][col] = value;
		    if (isSolved(row + 1, col, sudoku)) {
			return true;
		    }
		}
	    }
	} else {
	    final String key = SuDokus.generateKey(row, col);
	    while (!this.fc.getDomains().get(key).isEmpty()) {
		final HashMap<String, String> clonedDomainSync = this.fc.getClonedDomains();
		final int value = Integer.valueOf(this.fc.getDomains().get(key).substring(0, 1));
		sudoku[row][col] = value;
		this.fc.sync(row, col, value, sudoku);
		if (isSolved(row + 1, col, sudoku)) {
		    return true;
		}
		sudoku[row][col] = 0;
		this.fc.setDomains(clonedDomainSync);
		this.fc.syncKey(key, value);
	    }
	}

	// reset on backtrack
	sudoku[row][col] = 0;
	return false;
    }

}
