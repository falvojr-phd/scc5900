package br.usp.falvojr.sudoku.heuristic.impl;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import br.usp.falvojr.sudoku.heuristic.Heuristic;
import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * Forward Checking (FC) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class ForwardChecking implements Heuristic {

    private HashMap<String, String> domains;

    public HashMap<String, String> getDomains() {
	return this.domains;
    }

    public void setDomains(HashMap<String, String> domains) {
	this.domains = domains;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getClonedDomains() {
	return (HashMap<String, String>) this.domains.clone();
    }

    @Override
    public void init(Integer[][] sudoku) {
	this.domains = new HashMap<>();
	for (int row = 0; row < SuDokus.BOARD_SIZE; row++) {
	    for (int col = 0; col < SuDokus.BOARD_SIZE; col++) {
		final String key = SuDokus.generateKey(row, col);
		final StringBuilder possibleValues = new StringBuilder();
		if (sudoku[row][col] == 0) {
		    for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
			if (SuDokus.isLegal(row, col, value, sudoku)) {
			    possibleValues.append(value);
			}
		    }
		}
		this.domains.put(key, possibleValues.toString());
	    }
	}
    }

    public void sync(int row, int col, int value, Integer[][] sudoku) {
	// verify if exists rows synchronizations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    final String key = SuDokus.generateKey(row, i);
	    this.syncKey(key, value);
	}
	// verify if exists columns synchronizations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    final String key = SuDokus.generateKey(i, col);
	    this.syncKey(key, value);
	}
	// verify if exists boxes synchronizations
	final int boxRowOffset = (row / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	final int boxColOffset = (col / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	for (int i = 0; i < SuDokus.BOX_SIZE; i++) {
	    for (int j = 0; j < SuDokus.BOX_SIZE; j++) {
		final String key = SuDokus.generateKey(boxRowOffset + i, boxColOffset + j);
		this.syncKey(key, value);
	    }
	}
    }

    public void syncKey(final String key, int value) {
	final String oldPossibilities = this.domains.get(key);
	final String syncValue = String.valueOf(value);
	if (StringUtils.contains(oldPossibilities, syncValue)) {
	    this.domains.put(key, StringUtils.replace(oldPossibilities, syncValue, StringUtils.EMPTY));
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
