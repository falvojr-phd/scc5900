package br.usp.falvojr.sudoku.heuristic;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * Forward Checking (FC) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class ForwardChecking {

    private Map<String, String> domains;

    public Map<String, String> getDomains() {
	return this.domains;
    }

    public void setDomains(Map<String, String> domains) {
	this.domains = domains;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getClonedDomains() {
	final Map<String, String> newMap;
	try {
	    newMap = this.domains.getClass().newInstance();
	    newMap.putAll(this.domains);
	    return newMap;
	} catch (InstantiationException | IllegalAccessException e) {
	    return null;
	}
    }

    public void init(Integer[][] sudoku) {
	this.domains = new HashMap<>();
	for (int row = 0; row < SuDokus.BOARD_SIZE; row++) {
	    for (int col = 0; col < SuDokus.BOARD_SIZE; col++) {
		if (sudoku[row][col] == 0) {
		    final String key = SuDokus.generateKey(row, col);
		    final StringBuilder possibleValues = new StringBuilder();
		    for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
			if (SuDokus.isLegal(row, col, value, sudoku)) {
			    possibleValues.append(value);
			}
		    }
		    this.domains.put(key, possibleValues.toString());
		}
	    }
	}
    }

    public void syncDomains(int row, int col, int value, Integer[][] sudoku) {
	// verify if exists rows synchronizations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    final String key = SuDokus.generateKey(row, i);
	    this.syncDomainKey(key, value);
	}
	// verify if exists columns synchronizations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    final String key = SuDokus.generateKey(i, col);
	    this.syncDomainKey(key, value);
	}
	// verify if exists boxes synchronizations
	final int boxRowOffset = (row / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	final int boxColOffset = (col / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	for (int i = 0; i < SuDokus.BOX_SIZE; i++) {
	    for (int j = 0; j < SuDokus.BOX_SIZE; j++) {
		final String key = SuDokus.generateKey(boxRowOffset + i, boxColOffset + j);
		this.syncDomainKey(key, value);
	    }
	}
    }

    public void syncDomainKey(final String key, int value) {
	final String oldPossibilities = this.domains.get(key);
	final String syncValue = String.valueOf(value);
	if (StringUtils.contains(oldPossibilities, syncValue)) {
	    final String newPossibilities = StringUtils.replace(oldPossibilities, syncValue, StringUtils.EMPTY);
	    this.domains.put(key, newPossibilities);
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
