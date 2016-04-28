package br.usp.falvojr.sudoku.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Useful class for SuDoku constants configurations.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class SuDokus {

    private SuDokus() {
	super();
    }

    public static final Integer BOARD_SIZE = 9;
    public static final Integer BOX_SIZE = 3;

    /**
     * Useful method to identifies if the value is legal considering row and
     * column.
     *
     * @param row
     * @param col
     * @param value
     * @param sudoku
     *
     * @return a flag indicating if the value is legal considering row and
     *         column.
     */
    public static boolean isLegal(int row, int col, int value, Integer[][] sudoku) {
	// verify if exists rows violations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    if (value == sudoku[row][i]) {
		return false;
	    }
	}
	// verify if exists columns violations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    if (value == sudoku[i][col]) {
		return false;
	    }
	}
	// verify if exists boxes violations
	final int boxRowOffset = (row / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	final int boxColOffset = (col / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	for (int i = 0; i < SuDokus.BOX_SIZE; i++) {
	    for (int j = 0; j < SuDokus.BOX_SIZE; j++) {
		if (value == sudoku[boxRowOffset + i][boxColOffset + j]) {
		    return false;
		}
	    }
	}
	// no violations, so it's legal
	return true;
    }

    /**
     * Print a SuDoku.
     *
     * @param sudoku
     */
    public static void write(Integer[][] sudoku) {
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    for (int j = 0; j < SuDokus.BOARD_SIZE; j++) {
		System.out.printf("%s ", sudoku[i][j] == 0 ? " " : Integer.valueOf(sudoku[i][j]));
	    }
	    System.out.println();
	}
	System.out.println();
    }

    /**
     * Generates a SuDoku key.
     *
     * @param row
     * @param col
     *
     * @return unique key with col and row concatenation.
     */
    public static String generateKey(int row, int col) {
	return new StringBuilder().append(row).append(col).toString();
    }

    /**
     * Sort a {@link Map} based on your value.
     *
     * @param map
     * @param comparator
     *
     * @return sorted {@link Map}.
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, Comparator<V> comparator) {
	final Map<K, V> result = new LinkedHashMap<>();
	map.entrySet().stream().sorted(Map.Entry.comparingByValue(comparator)).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
	return result;
    }

}
