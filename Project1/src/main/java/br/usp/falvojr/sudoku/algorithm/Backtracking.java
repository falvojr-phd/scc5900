package br.usp.falvojr.sudoku.algorithm;

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

	    // solves in place
	    if (isSolved(0, 0, sudoku)) {
		SuDokus.write(sudoku, true);
	    } else {
		System.err.println("Solution not found!");
	    }
	});
    }

    private boolean isSolved(int row, int col, Integer[][] sudoku) {
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

	final boolean hasFc = this.fc != null;
	String possibilities = null;
	if (hasFc) {
	    this.fc.init(sudoku);
	    possibilities = this.fc.getPossibilities().get(SuDokus.generateKey(row, col));
	}

	for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
	    boolean isLegal = hasFc 
		    ? possibilities.contains(String.valueOf(value))
		    : SuDokus.isLegal(row, col, value, sudoku);
	    if (isLegal) {
		sudoku[row][col] = value;
		if (isSolved(row + 1, col, sudoku)) {
		    return true;
		}
	    }
	}
	// reset on backtrack
	sudoku[row][col] = 0;
	return false;
    }

}
