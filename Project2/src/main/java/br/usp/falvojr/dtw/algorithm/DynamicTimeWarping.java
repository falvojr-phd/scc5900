package br.usp.falvojr.dtw.algorithm;

public class DynamicTimeWarping {

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
