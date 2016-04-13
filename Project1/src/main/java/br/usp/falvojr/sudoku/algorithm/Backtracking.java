package br.usp.falvojr.sudoku.algorithm;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import br.usp.falvojr.sudoku.heuristic.impl.ForwardChecking;
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
	    final ForwardChecking forwardChecking = new ForwardChecking(sudoku);
	    forwardChecking.syncPossibleValues();
	    // solves in place
	    if (isSolved(0, 0, sudoku, forwardChecking)) {
		SuDokus.write(sudoku, true);
	    } else {
		System.err.println("Solution not found!");
	    }
	});
    }

    private boolean isSolved(int i, int j, Integer[][] sudoku, ForwardChecking... heuristics) {
	if (i == SuDokus.BOARD_SIZE) {
	    i = 0;
	    if (++j == SuDokus.BOARD_SIZE) {
		return true;
	    }
	}
	// skip filled cells
	if (sudoku[i][j] != 0) {
	    return isSolved(i + 1, j, sudoku, heuristics[0]);
	}

	final Map<String, List<Integer>> possibleValues = heuristics[0].getPossibleValues();
	for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
	    final String key = String.format("%d%d", i, j);
	    if (possibleValues.containsKey(key) && !possibleValues.get(key).isEmpty()) {
		if (possibleValues.get(key).contains(value)) {
		    sudoku[i][j] = value;
		    heuristics[0].syncPossibleValues();
		    if (isSolved(i + 1, j, sudoku, heuristics[0])) {
			return true;
		    }
		}
	    } else {
		break;
	    }
	}
	// reset on backtrack
	sudoku[i][j] = 0;
	return false;
    }

}
