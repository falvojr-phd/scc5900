package br.usp.icmc.falvojr;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Main class.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class Main {

    private static final Integer DIMENSION = 9;

    private static final String TXT_EXTENSION = ".TXT";
    private static final String BIN_FOLDER = "";

    public static void main(String[] args) throws Exception {

	final URL url = ClassLoader.getSystemResources(BIN_FOLDER).nextElement();

	Files.walk(Paths.get(url.toURI())).forEach(filePath -> {
	    final String fileName = filePath.getFileName().toString().toUpperCase();
	    if (Files.isRegularFile(filePath) && fileName.endsWith(TXT_EXTENSION)) {
		Main.processInput(filePath);
	    }
	});
    }

    private static void processInput(Path inputPath) {
	try {
	    final List<Integer[][]> sudokus = new ArrayList<>();
	    final Pattern pattern = Pattern.compile(" ");

	    final Iterator<Integer[]> preparedLines = Files.lines(inputPath).parallel().filter(row -> {
		return !(row == null || "".equals(row.trim())) && pattern.split(row).length == DIMENSION;
	    }).map(row -> {
		return pattern.splitAsStream(row).map(Integer::parseInt).toArray(Integer[]::new);
	    }).iterator();

	    Integer[][] sudoku = new Integer[DIMENSION][DIMENSION];
	    Integer sudokuIndex = 0;
	    while (preparedLines.hasNext()) {
		final Integer[] row = preparedLines.next();
		sudoku[sudokuIndex] = row;
		if (++sudokuIndex == DIMENSION) {
		    sudokus.add(sudoku);
		    sudokuIndex = 0;
		    sudoku = new Integer[DIMENSION][DIMENSION];
		}
	    }

	    Main.solveSudokus(sudokus);

	} catch (final IOException e) {
	    e.printStackTrace();
	}

    }

    private static void solveSudokus(final List<Integer[][]> sudokus) {
	final long startTime = System.currentTimeMillis();
	sudokus.parallelStream().forEach(sudoku -> {
	    if (Main.isSolvedSudoku(0, 0, sudoku)) { // solves in place
		Main.writeSudoku(sudoku, true);
	    } else {
		System.out.println("NONE");
	    }
	});
	final long endTime = System.currentTimeMillis();

	System.err.printf("\n%.2f seconds\n\n", (endTime - startTime) / 1000D);
    }

    private static boolean isSolvedSudoku(int i, int j, Integer[][] cells) {
	if (i == DIMENSION) {
	    i = 0;
	    if (++j == DIMENSION) {
		return true;
	    }
	}
	if (cells[i][j] != 0) { // skip filled cells
	    return Main.isSolvedSudoku(i + 1, j, cells);
	}
	for (int value = 1; value <= DIMENSION; ++value) {
	    if (Main.isLegalSudoku(i, j, value, cells)) {
		cells[i][j] = value;
		if (Main.isSolvedSudoku(i + 1, j, cells)) {
		    return true;
		}
	    }
	}
	cells[i][j] = 0; // reset on backtrack
	return false;
    }

    private static boolean isLegalSudoku(int i, int j, int value, Integer[][] cells) {
	for (int k = 0; k < DIMENSION; ++k) { // row
	    if (value == cells[k][j]) {
		return false;
	    }
	}
	for (int k = 0; k < DIMENSION; ++k) { // column
	    if (value == cells[i][k]) {
		return false;
	    }
	}
	final int boxRowOffset = (i / 3) * 3;
	final int boxColOffset = (j / 3) * 3;
	for (int k = 0; k < 3; ++k) { // box
	    for (int m = 0; m < 3; ++m) {
		if (value == cells[boxRowOffset + k][boxColOffset + m]) {
		    return false;
		}
	    }
	}

	return true; // no violations, so it's legal
    }

    private static synchronized void writeSudoku(Integer[][] sudoku, boolean isFormatted) {
	if (isFormatted) {
	    for (int i = 0; i < DIMENSION; ++i) {
		if (i % 3 == 0) {
		    System.out.println(" -----------------------");
		}
		for (int j = 0; j < DIMENSION; ++j) {
		    if (j % 3 == 0) {
			System.out.print("| ");
		    }
		    System.out.printf("%s ", sudoku[i][j] == 0 ? " " : Integer.toString(sudoku[i][j]));
		}
		System.out.println("|");
	    }
	    System.out.println(" -----------------------");
	} else {
	    for (int i = 0; i < DIMENSION; ++i) {
		for (int j = 0; j < DIMENSION; ++j) {
		    System.out.printf("%d ", sudoku[i][j]);
		}
		System.out.println();
	    }
	    System.out.println();
	}

    }

}
