package br.usp.falvojr.dtw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import br.usp.falvojr.dtw.classification.NearestNeighbor;
import br.usp.falvojr.dtw.classification.OneNearestNeighbor;
import br.usp.falvojr.dtw.classification.TreeNearestNeighbor;

/**
 * Main class.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class Main {

	private static final Pattern PATTERN_LABELS = Pattern.compile("[\t]");
	private static final Pattern PATTERN_SERIES = Pattern.compile(" ");

	public static void main(String[] args) throws Exception {
		final int dirIndex = ArrayUtils.indexOf(args, "-d");
		final int pathIndex = dirIndex + 1;
		if (dirIndex > -1) {
			try {
				final boolean is3d = ArrayUtils.contains(args, "-3d");
				
				final int wIndex = ArrayUtils.indexOf(args, "-w");

				final String training = String.format("treino%s.txt", is3d ? "3D" : StringUtils.EMPTY);
				final String test = String.format("teste%s.txt", is3d ? "3D" : StringUtils.EMPTY);

				final String basePath = args[pathIndex];

				final Path pathTraining = Paths.get(basePath, training);
				final Path pathTest = Paths.get(basePath, test);

				final List<Double[]> trainingSeries = Main.readFileToTemporalSeries(pathTraining);
				final List<Double[]> testSeries = Main.readFileToTemporalSeries(pathTest);

				final NearestNeighbor classificassion;
				if (is3d) {
					classificassion = TreeNearestNeighbor.getInstance();
				} else {
					classificassion = OneNearestNeighbor.getInstance();
				}
				
				final long startTime = System.nanoTime();
				
				final double accuracyRate;
				if (wIndex > -1) {
					final int wValueIndex = wIndex + 1;
					final int w = Integer.parseInt(args[wValueIndex]);
					accuracyRate = classificassion.computeAccuracyRate(trainingSeries, testSeries, w);
				} else {
					accuracyRate = classificassion.computeAccuracyRate(trainingSeries, testSeries);
				}
				final long endTime = System.nanoTime();

				final int total = is3d ? testSeries.size() / 3 : testSeries.size();
				System.out.printf("Razão de Acertos: %.0f/%d%s", accuracyRate, total, System.lineSeparator());
				
				final double acurracyPercentage = accuracyRate / total * 100D;		
				System.out.printf("Taxa de Acertos: %.2f%%%s", acurracyPercentage, System.lineSeparator());
				
				final long elapsedTime = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
				System.out.printf("Tempo de Execucao: %.3fs", elapsedTime / 1000D);
			} catch (InvalidPathException | NoSuchFileException | IndexOutOfBoundsException exception) {
				System.err.println("O path especificado para o argumento -d nao e valido");
			}
		} else {
			System.err.println("O argumento -d e obrigatorio, bem como seu respectivo path. Sintaxe: -d [path]");
		}
	}

	@SuppressWarnings("unused")
	private static Map<Integer, String> readFileToMapLabels(final Path path) throws IOException {
		final Map<Integer, String> mapLabels = new HashMap<>();
		Files.lines(path).forEach(row -> {
			final String[] rowSplit = PATTERN_LABELS.split(row);
			mapLabels.put(Integer.valueOf(rowSplit[0]), rowSplit[1]);
		});
		return mapLabels;
	}

	private static List<Double[]> readFileToTemporalSeries(final Path path) throws IOException {
		final List<Double[]> temporalSeries = new ArrayList<>();
		Files.lines(path).forEach(row -> {
			final Double[] serie = Stream.of(PATTERN_SERIES.split(row)).map(Double::parseDouble).toArray(Double[]::new);
			temporalSeries.add(serie);
		});
		return temporalSeries;
	}

}
