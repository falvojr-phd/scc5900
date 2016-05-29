package br.usp.falvojr.dtw.classification;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import br.usp.falvojr.dtw.algorithm.DynamicTimeWarping;

/**
 * 1-Nearest Neighbor classification.
 * 
 * @author Venilton FalvoJr (falvojr)
 */
public class OneNearestNeighbor {
	
	public int computeAccuracyRate(final List<Double[]> trainingSeries, final List<Double[]> testSeries, final Integer w) {
		int accuracy = 0;
		for (Double[] rowTestSerie : testSeries) {
			double target = 0, distance = Double.POSITIVE_INFINITY;
			final Double[] testSerie = (Double[]) ArrayUtils.remove(rowTestSerie, 0);
			for (Double[] rowTrainingSerie : trainingSeries) {
				final Double[] trainingSerie = (Double[]) ArrayUtils.remove(rowTrainingSerie, 0);
				final Double dtwDistance;
				if (w == null) {
					dtwDistance = DynamicTimeWarping.getInstance().dtwDistance(trainingSerie, testSerie);
				} else {
					dtwDistance = DynamicTimeWarping.getInstance().dtwDistance(trainingSerie, testSerie, w);
				}
				if (dtwDistance < distance) {
					distance = dtwDistance;
					target = rowTrainingSerie[0];
				}
			}
			if (rowTestSerie[0].equals(target)) {
				accuracy++;
			}
		}
		return accuracy;
	}

	public int computeAccuracyRate(final List<Double[]> trainingSeries, final List<Double[]> testSeries) {
		return computeAccuracyRate(trainingSeries, testSeries, null);
	}
	
	/**
	 * Private constructor for Singleton Pattern.
	 */
	private OneNearestNeighbor() {
		super();
	}

	private static class OneNearestNeighborHolder {
		public static final OneNearestNeighbor INSTANCE = new OneNearestNeighbor();
	}

	public static OneNearestNeighbor getInstance() {
		return OneNearestNeighborHolder.INSTANCE;
	}
}
