package br.usp.falvojr.dtw.algorithm;

/**
 * Dynamic Time Warping (DTW) algorithm.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Dynamic_time_warping">Wikipedia</a>
 * 
 * @author Venilton FalvoJr (falvojr)
 */
public class DynamicTimeWarping {

	/**
	 * Calculate DTW distance.
	 * 
	 * @param serieA temporal serie A.
	 * @param serieB temporal serie B.
	 * 
	 * @return {@link Double} with DTW distance.
	 */
	public Double dtwDistance(Double[] serieA, Double[] serieB) {
		final Double[][] dtw = new Double[serieA.length + 1][serieB.length + 1];

		for (int i = 0; i <= serieA.length; i++) {
			dtw[i][0] = Double.POSITIVE_INFINITY;
		}
		for (int i = 0; i <= serieB.length; i++) {
			dtw[0][i] = Double.POSITIVE_INFINITY;
		}
		dtw[0][0] = 0.0;

		for (int i = 1; i <= serieA.length; i++) {
			for (int j = 1; j <= serieB.length; j++) {
				final double distance = this.distance(serieA[i - 1], serieB[j - 1]);
				dtw[i][j] = distance + this.min(dtw[i - 1][j - 1], dtw[i][j - 1], dtw[i - 1][j]);
			}
		}
		return dtw[serieA.length][serieB.length];
	}

	/**
	 * Calculate DTW distance with bandwidth Sakoe-Chiba (<b>extension 1</b>).
	 * 
	 * @param serieA temporal serie A.
	 * @param serieB temporal serie B.
	 * @param w bandwidth.
	 * 
	 * @return {@link Double} with DTW distance.
	 */
	public Double dtwDistance(Double[] serieA, Double[] serieB, int w) {
		final Double[][] dtw = new Double[serieA.length + 1][serieB.length + 1];

		// adapt window size (*)
		w = (Math.max(serieA.length, serieB.length) * w) / 100;
		w = Math.max(w, Math.abs(serieA.length - serieB.length));

		for (int i = 0; i <= serieA.length; i++) {
			for (int j = 0; j <= serieB.length; j++) {
				dtw[i][j] = Double.POSITIVE_INFINITY;
			}
		}
		dtw[0][0] = 0.0;

		for (int i = 1; i <= serieA.length; i++) {
			for (int j = Math.max(1, i - w); j <= Math.min(serieB.length, i + w); j++) {
				final double distance = this.distance(serieA[i - 1], serieB[j - 1]);
				dtw[i][j] = distance + this.min(dtw[i - 1][j - 1], dtw[i][j - 1], dtw[i - 1][j]);
			}
		}

		return dtw[serieA.length][serieB.length];
	}

	private Double min(Double value1, Double value2, Double value3) {
		Double min = value1;
		if (value2 < min) {
			min = value2;
		}
		if (value3 < min) {
			min = value3;
		}
		return min;
	}

	private Double distance(Double valueA, Double valueB) {
		return Math.pow(valueA - valueB, 2);
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
