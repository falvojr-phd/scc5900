package br.usp.falvojr.sudoku.heuristic.impl;

import br.usp.falvojr.sudoku.heuristic.Heuristic;

/**
 * Minimum Remaining Values (MRV) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class MinimumRemainingValues implements Heuristic {

    @Override
    public void init(Integer[][] sudoku) {
	// TODO Auto-generated method stub

    }

    /**
     * Private constructor for Singleton Pattern.
     */
    private MinimumRemainingValues() {
	super();
    }

    private static class MinimumRemainingValuesHolder {
	public static final MinimumRemainingValues INSTANCE = new MinimumRemainingValues();
    }

    public static MinimumRemainingValues getInstance() {
	return MinimumRemainingValuesHolder.INSTANCE;
    }
}
