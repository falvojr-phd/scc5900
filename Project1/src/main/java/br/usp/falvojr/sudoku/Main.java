package br.usp.falvojr.sudoku;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;

import br.usp.falvojr.sudoku.algorithm.Backtracking;
import br.usp.falvojr.sudoku.heuristic.ForwardChecking;
import br.usp.falvojr.sudoku.heuristic.MinimumRemainingValues;
import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * Main class.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class Main {

	private static final String TXT_EXTENSION = ".TXT";

	private static boolean FLAG_FC = false;
	private static boolean FLAG_MRV = false;

	public static void main(String[] args) throws Exception {
		final int dirIndex = ArrayUtils.indexOf(args, "-d");
		final int pathIndex = dirIndex + 1;
		if (dirIndex > -1 && pathIndex < args.length) {
			try {
				final Path filePath = Paths.get(args[pathIndex]);

				Main.FLAG_FC = ArrayUtils.contains(args, "-fc");
				Main.FLAG_MRV = ArrayUtils.contains(args, "-mrv");
				Backtracking.FLAG_STEPS = !ArrayUtils.contains(args, "-f");

				Files.walk(filePath).forEach(file -> {
					final String fileName = file.getFileName().toString().toUpperCase();
					if (Files.isRegularFile(file) && fileName.endsWith(TXT_EXTENSION)) {
						Main.processInput(file);
					}
				});
			} catch (InvalidPathException | NoSuchFileException exception) {
				System.err.println("O path especificado para o argumento -d nao e valido");
			}
		} else {
			System.err.println("O argumento -d e obrigatorio, bem como seu respectivo path. Sintaxe: -d [path]");
		}
	}

	private static void processInput(Path inputPath) {
		try {
			final List<Integer[][]> sudokus = new ArrayList<>();
			final Pattern pattern = Pattern.compile(" ");

			// Prepares file data, ignoring the number of test cases and the empty lines. Moreover, parse String values to Integer.
			final Iterator<Integer[]> preparedLines = Files.lines(inputPath).parallel().filter(row -> {
				return !(row == null || "".equals(row.trim())) && pattern.split(row).length == SuDokus.BOARD_SIZE;
			}).map(row -> {
				return pattern.splitAsStream(row).map(Integer::parseInt).toArray(Integer[]::new);
			}).iterator();

			// Through of the preparedLines variable, creates the SuDoku matrices.
			Integer[][] sudoku = new Integer[SuDokus.BOARD_SIZE][SuDokus.BOARD_SIZE];
			Integer sudokuIndex = 0;
			while (preparedLines.hasNext()) {
				final Integer[] row = preparedLines.next();
				sudoku[sudokuIndex] = row;
				if (++sudokuIndex == SuDokus.BOARD_SIZE) {
					sudokus.add(sudoku);
					sudokuIndex = 0;
					sudoku = new Integer[SuDokus.BOARD_SIZE][SuDokus.BOARD_SIZE];
				}
			}

			// Call method for solve the SuDokus
			Main.solveSuDokus(sudokus);

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	private static void solveSuDokus(final List<Integer[][]> sudokus) {
		final long startTime = System.nanoTime();

		final Backtracking sudokuBacktracking = new Backtracking(sudokus);

		if (FLAG_MRV) {
			sudokuBacktracking.setFc(ForwardChecking.getInstance());
			sudokuBacktracking.setMrv(MinimumRemainingValues.getInstance());
		} else if (FLAG_FC) {
			sudokuBacktracking.setFc(ForwardChecking.getInstance());
		}

		sudokuBacktracking.solve();

		final long endTime = System.nanoTime();

		final long elapsedTime = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		System.out.printf("%1$sTempo de execucao: %2$.3f segundos%1$s%1$s", System.lineSeparator(),
		        elapsedTime / 1000D);
	}

}
