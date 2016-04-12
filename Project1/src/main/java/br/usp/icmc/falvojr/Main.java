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

	    // Prepares file data, ignoring the number of test cases and the empty lines. Moreover, parse String values to Integer.
	    final Iterator<Integer[]> preparedLines = Files.lines(inputPath).parallel().filter(row -> {
		return !(row == null || "".equals(row.trim())) && pattern.split(row).length == DIMENSION;
	    }).map(row -> {
		return pattern.splitAsStream(row).map(Integer::parseInt).toArray(Integer[]::new);
	    }).iterator();

	    // Through of the preparedLines variable, creates the SuDoku matrices.
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

	    // Call method for solve the SuDokus
	    Main.solveSuDokus(sudokus);

	} catch (final IOException e) {
	    e.printStackTrace();
	}

    }

    private static void solveSuDokus(final List<Integer[][]> sudokus) {
	final long startTime = System.currentTimeMillis();

	final SuDokuBacktracking sudokuBacktracking = new SuDokuBacktracking(sudokus.stream());
	sudokuBacktracking.solve();

	final long endTime = System.currentTimeMillis();

	System.err.printf("\n%.2f seconds\n\n", (endTime - startTime) / 1000D);
    }

}
