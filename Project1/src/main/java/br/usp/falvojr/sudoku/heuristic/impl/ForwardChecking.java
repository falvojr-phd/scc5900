package br.usp.falvojr.sudoku.heuristic.impl;

import java.util.HashMap;
import java.util.Map;

import br.usp.falvojr.sudoku.heuristic.Heuristic;
import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * Forward Checking (FC) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class ForwardChecking implements Heuristic {

    private Map<String, String> possibilities;

    public Map<String, String> getPossibilities() {
	return this.possibilities;
    }

    @Override
    public void init(Integer[][] sudoku) {
	this.possibilities = new HashMap<>();
	final int sudokuLength = sudoku.length;
	for (int col = 0; col < sudokuLength; col++) {
	    for (int row = 0; row < sudokuLength; row++) {
		final String key = new StringBuilder().append(col).append(row).toString();
		final StringBuilder possibleValues = new StringBuilder();
		if (sudoku[col][row] == 0) {
		    for (int value = 1; value <= sudokuLength; value++) {
			if (SuDokus.isLegal(col, row, value, sudoku)) {
			    possibleValues.append(value);
			}
		    }
		}
		this.possibilities.put(key, possibleValues.toString());
	    }
	}
    }
    
    public void sync(int row, int col, int value, Integer[][] sudoku) {
	// verify if exists rows violations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    final String key = SuDokus.generateKey(row, i);
	    this.syncKey(key, value);
	}
	// verify if exists columns violations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    final String key = SuDokus.generateKey(i, col);
	    this.syncKey(key, value);
	}
	// verify if exists boxes violations
	final int boxRowOffset = (row / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	final int boxColOffset = (col / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	for (int i = 0; i < SuDokus.BOX_SIZE; i++) {
	    for (int j = 0; j < SuDokus.BOX_SIZE; j++) {
		final String key = SuDokus.generateKey(boxRowOffset + i, boxColOffset + j);
		this.syncKey(key, value);
	    }
	}
    }

    private void syncKey(final String key, int value) {
	final String oldPossibilities = this.possibilities.get(key);
	final String syncValue = String.valueOf(value);
	if (oldPossibilities.contains(syncValue)) {
	    this.possibilities.put(key, oldPossibilities.replace(syncValue, ""));
	}
    }

    public void reset(int row, int col, int value, Integer[][] sudoku) {
	// verify if exists rows violations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    // verify if exists columns violations
	    for (int j = 0; j < SuDokus.BOARD_SIZE; j++) {
		// ignore because backtrack
		if (i == row && j == col) {
		    continue;
		}
		// return reseted value on possible cells
		if (SuDokus.isLegal(i, j, value, sudoku)) {
		    final String key = SuDokus.generateKey(i, j);
		    this.resetKey(key, value);
		}
	    }
	}
    }

    private void resetKey(final String key, int value) {
	final String oldPossibilities = this.possibilities.get(key);
	final String resetValue = String.valueOf(value);
	if (!oldPossibilities.contains(resetValue)) {
	    this.possibilities.put(key, oldPossibilities.concat(resetValue));
	}
    }

    /**
     * Private constructor for Singleton Pattern.
     */
    private ForwardChecking() {
	super();
    }

    private static class ForwardCheckingHolder {
	public static final ForwardChecking INSTANCE = new ForwardChecking();
    }

    public static ForwardChecking getInstance() {
	return ForwardCheckingHolder.INSTANCE;
    }

}
