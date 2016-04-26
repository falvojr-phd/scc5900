package br.usp.falvojr.sudoku.heuristic;

/**
 * TODO Minimum Remaining Values (MRV) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class MinimumRemainingValues {

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
