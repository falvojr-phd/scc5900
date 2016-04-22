package br.usp.falvojr.sudoku.util;

import java.util.Arrays;
import java.util.HashSet;

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
     * Useful method to identifies if the value is legal considering row and column.
     *
     * @param row
     * @param col
     * @param value
     * @param sudoku
     *
     * @return a flag indicating if the value is legal considering row and column.
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
	return true; // no violations, so it's legal
    }

    /**
     * Print a SuDoku with formating flag parameter.
     *
     * @param sudoku
     * @param isFormatted
     */
    public static synchronized void write(Integer[][] sudoku, boolean isFormatted) {
	if (isFormatted) {
	    for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
		if (i % SuDokus.BOX_SIZE == 0) {
		    System.out.println(" -----------------------");
		}
		for (int j = 0; j < SuDokus.BOARD_SIZE; j++) {
		    if (j % SuDokus.BOX_SIZE == 0) {
			System.out.print("| ");
		    }
		    System.out.printf("%s ", sudoku[i][j] == 0 ? " " : Integer.toString(sudoku[i][j]));
		}
		System.out.println("|");
	    }
	    System.out.println(" -----------------------");
	} else {
	    for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
		for (int j = 0; j < SuDokus.BOARD_SIZE; j++) {
		    System.out.printf("%s ", sudoku[i][j] == 0 ? " " : Integer.toString(sudoku[i][j]));
		}
		System.out.println();
	    }
	    System.out.println();
	}

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
     * An O(nlogn) solution for difference between.
     *
     * @param arrayA
     * @param arrayB
     *
     * @return the difference between arrayA and arrayB
     */
    public static int[] difference(int[] arrayA, int[] arrayB) {
	final HashSet<Integer> difference = new HashSet<>();
	Arrays.sort(arrayA);
	for (final int itemB : arrayB) {
	    if (!difference.contains(itemB) && Arrays.binarySearch(arrayA, itemB) < 0) {
		difference.add(itemB);
	    }
	}
	final int[] differenceArray = new int[difference.size()];
	int i = 0;
	for (final int item : difference) {
	    differenceArray[i++] = item;
	}
	return differenceArray;

    }
}
