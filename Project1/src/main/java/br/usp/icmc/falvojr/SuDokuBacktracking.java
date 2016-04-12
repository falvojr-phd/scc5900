package br.usp.icmc.falvojr;

import java.util.stream.Stream;

/**
 * SuDoku solver implemented using the Backtracking algorithm.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class SuDokuBacktracking {

    protected static final Integer DIMENSION = 9;

    protected Stream<Integer[][]> sudokus;

    public SuDokuBacktracking(Stream<Integer[][]> sudokus) {
	super();
	this.sudokus = sudokus;
    }

    public void solve() {
	this.sudokus.forEach(sudoku -> {
	    if (isSolved(0, 0, sudoku)) { // solves in place
		write(sudoku, true);
	    } else {
		System.err.println("Solution not found!");
	    }
	});
    }

    protected boolean isSolved(int i, int j, Integer[][] sudoku) {
	if (i == DIMENSION) {
	    i = 0;
	    if (++j == DIMENSION) {
		return true;
	    }
	}
	if (sudoku[i][j] != 0) { // skip filled cells
	    return isSolved(i + 1, j, sudoku);
	}
	for (int value = 1; value <= DIMENSION; ++value) {
	    if (isLegal(i, j, value, sudoku)) {
		sudoku[i][j] = value;
		if (isSolved(i + 1, j, sudoku)) {
		    return true;
		}
	    }
	}
	sudoku[i][j] = 0; // reset on backtrack
	return false;
    }

    protected boolean isLegal(int i, int j, int value, Integer[][] sudoku) {
	for (int k = 0; k < DIMENSION; ++k) { // row
	    if (value == sudoku[k][j]) {
		return false;
	    }
	}
	for (int k = 0; k < DIMENSION; ++k) { // column
	    if (value == sudoku[i][k]) {
		return false;
	    }
	}
	final int boxRowOffset = (i / 3) * 3;
	final int boxColOffset = (j / 3) * 3;
	for (int k = 0; k < 3; ++k) { // box
	    for (int m = 0; m < 3; ++m) {
		if (value == sudoku[boxRowOffset + k][boxColOffset + m]) {
		    return false;
		}
	    }
	}
	return true; // no violations, so it's legal
    }

    protected synchronized void write(Integer[][] sudoku, boolean isFormatted) {
	if (isFormatted) {
	    for (int i = 0; i < DIMENSION; ++i) {
		if (i % 3 == 0) {
		    System.out.println(" -----------------------");
		}
		for (int j = 0; j < DIMENSION; ++j) {
		    if (j % 3 == 0) {
			System.out.print("| ");
		    }
		    System.out.printf("%s ", sudoku[i][j] == 0 ? " " : Integer.toString(sudoku[i][j]));
		}
		System.out.println("|");
	    }
	    System.out.println(" -----------------------");
	} else {
	    for (int i = 0; i < DIMENSION; ++i) {
		for (int j = 0; j < DIMENSION; ++j) {
		    System.out.printf("%d ", sudoku[i][j]);
		}
		System.out.println();
	    }
	    System.out.println();
	}

    }
}
