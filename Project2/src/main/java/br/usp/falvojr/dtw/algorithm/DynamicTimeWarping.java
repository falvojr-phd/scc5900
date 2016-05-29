package br.usp.falvojr.dtw.algorithm;

import java.util.Arrays;
import java.util.Collections;

public class DynamicTimeWarping {

	public Double dtwDistance(Double[] serieS, Double[] serieT) {
		final Double[][] dtw = new Double[serieS.length + 1][serieT.length + 1];

		for (int i = 0; i <= serieS.length; i++) {
			dtw[i][0] = Double.POSITIVE_INFINITY;
		}
		for (int i = 0; i <= serieT.length; i++) {
			dtw[0][i] = Double.POSITIVE_INFINITY;
		}
		dtw[0][0] = 0.0;

		for (int i = 1; i <= serieS.length; i++) {
			for (int j = 1; j <= serieT.length; j++) {
				final double cost = distance(serieS[i - 1], serieT[j - 1]);
				dtw[i][j] = cost + min(
					  dtw[i - 1][j - 1] // match
					, dtw[i][j - 1]		// deletion
					, dtw[i - 1][j]		// insertion
				);
			}
		}
		return dtw[serieS.length][serieT.length];
	}

	private Double min(Double... values) {
		return Collections.min(Arrays.asList(values));
	}

	private Double distance(Double pointA, Double pointB) {
		return Math.pow(pointA - pointB, 2);
	}

	/**
	 * Private constructor for Singleton Pattern.
	 */
	private DynamicTimeWarping() {
		super();
	}

	private static class DynamicTimeWarpingHolder {
		public static final DynamicTimeWarping INSTANCE = new DynamicTimeWarping();
	}

	public static DynamicTimeWarping getInstance() {
		return DynamicTimeWarpingHolder.INSTANCE;
	}
}
