package br.usp.falvojr.dtw.classification;

import java.util.List;

/**
 * Nearest Neighbor classification interface.
 * 
 * @author Venilton FalvoJr (falvojr)
 */
public interface NearestNeighbor {

	/**
	 * Signature for computation accuracy rate.
	 * 
	 * @param trainingSeries training temporal series.
	 * @param testSeries test temporal series.
	 * 
	 * @return {@link Integer} with accuracy rate.
	 */
	int computeAccuracyRate(final List<Double[]> trainingSeries, final List<Double[]> testSeries);

	/**
	 * Signature for computation accuracy rate with bandwidth Sakoe-Chiba.
	 * 
	 * @param trainingSeries training temporal series.
	 * @param testSeries test temporal series.
	 * @param w bandwidth.
	 * 
	 * @return {@link Integer} with accuracy rate.
	 */
	int computeAccuracyRate(final List<Double[]> trainingSeries, final List<Double[]> testSeries, final Integer w);
}
