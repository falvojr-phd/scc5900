package br.usp.falvojr.dtw.classification;

import java.util.List;
import java.util.Map;

import br.usp.falvojr.dtw.algorithm.DynamicTimeWarping;

public class OneNearestNeighbor {

	public Double computeAccuracyRate(final Map<Integer, List<Double[]>> mapA, final Map<Integer, List<Double[]>> mapB) {
		Double accuracy = 0D;
		Double distance = Double.POSITIVE_INFINITY;
		Integer target = 0;
		for (Integer keyA : mapA.keySet()) {	
			final List<Double[]> seriesA = mapA.get(keyA);
			for (Integer keyB : mapB.keySet()) {			
				final List<Double[]> seriesB = mapB.get(keyB);
				for (Double[] serieB : seriesB) {
					for (Double[] serieA : seriesA) {
						final Double dtwDistance = DynamicTimeWarping.getInstance().dtwDistance(serieA, serieB);
						if (dtwDistance < distance) {
							distance = dtwDistance;
							target = keyB;
						}
					}
				}
				if (keyA.equals(target)) {
					accuracy++;
				}
			}
		}
		
		return accuracy;
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
