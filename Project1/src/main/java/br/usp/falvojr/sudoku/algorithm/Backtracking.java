package br.usp.falvojr.sudoku.algorithm;

import java.util.stream.Stream;

import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * SuDoku solver implemented using the Backtracking algorithm.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class Backtracking {

    private final Stream<Integer[][]> sudokus;

    public Backtracking(Stream<Integer[][]> sudokus) {
	super();
	this.sudokus = sudokus;
    }

    public void solve() {
	this.sudokus.forEach(sudoku -> {
	    // solves in place
	    if (isSolved(0, 0, sudoku)) {
		SuDokus.write(sudoku, true);
	    } else {
		System.err.println("Solution not found!");
	    }
	});
    }

    private boolean isSolved(int i, int j, Integer[][] sudoku) {
	if (i == SuDokus.BOARD_SIZE) {
	    i = 0;
	    if (++j == SuDokus.BOARD_SIZE) {
		return true;
	    }
	}
	// skip filled cells
	if (sudoku[i][j] != 0) {
	    return isSolved(i + 1, j, sudoku);
	}
	for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
	    if (SuDokus.isLegal(i, j, value, sudoku)) {
		sudoku[i][j] = value;
		if (isSolved(i + 1, j, sudoku)) {
		    return true;
		}
	    }
	}
	// reset on backtrack
	sudoku[i][j] = 0;
	return false;
    }

}
