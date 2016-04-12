package br.usp.icmc.falvojr;

import java.util.stream.Stream;

/**
 * SuDoku resolver implemented using the Backtracking algorithm with Forward
 * Checking (FC) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class SuDokuBacktrackingFc extends SuDokuBacktracking {

    public SuDokuBacktrackingFc(Stream<Integer[][]> sudokus) {
	super(sudokus);
    }

}
