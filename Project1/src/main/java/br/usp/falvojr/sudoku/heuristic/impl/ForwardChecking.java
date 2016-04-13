package br.usp.falvojr.sudoku.heuristic.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.usp.falvojr.sudoku.heuristic.Heuristic;
import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * Forward Checking (FC) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class ForwardChecking implements Heuristic {

    private final Integer[][] sudoku;
    private final Map<String, List<Integer>> possibleValues = new LinkedHashMap<>();

    public ForwardChecking(Integer[][] sudoku) {
	super();
	this.sudoku = sudoku;
    }

    public Map<String, List<Integer>> getPossibleValues() {
	return this.possibleValues;
    }

    @Override
    public void syncPossibleValues() {
	this.possibleValues.clear();
	for (Integer i = 0; i < this.sudoku.length; i++) {
	    for (Integer j = 0; j < this.sudoku.length; j++) {
		final int value = this.sudoku[i][j];
		final String key = String.format("%d%d", i, j);
		if (value == 0) {
		    for (int k = 1; k <= this.sudoku.length; k++) {
			if (SuDokus.isLegal(i, j, k, this.sudoku)) {
			    if (this.possibleValues.get(key) == null) {
				this.possibleValues.put(key, new ArrayList<>());
			    }
			    this.possibleValues.get(key).add(k);
			}
		    }
		} else {
		    this.possibleValues.put(key, new ArrayList<>());
		}
	    }
	}
    }
}
