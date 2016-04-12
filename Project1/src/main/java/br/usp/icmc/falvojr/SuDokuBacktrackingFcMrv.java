package br.usp.icmc.falvojr;

import java.util.stream.Stream;

/**
 * SuDoku resolver implemented using the Backtracking algorithm with Forward
 * Checking (FC) and Minimum Remaining Values (MRV) heuristics.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class SuDokuBacktrackingFcMrv extends SuDokuBacktrackingFc {

    public SuDokuBacktrackingFcMrv(Stream<Integer[][]> sudokus) {
	super(sudokus);
    }

}
