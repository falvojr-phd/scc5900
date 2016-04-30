package br.usp.falvojr.sudoku.heuristic;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * Minimum Remaining Values (MRV) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class MinimumRemainingValues {

	private final Comparator<String> comparator = (values1, value2) -> values1.length() - value2.length();
	private List<String> usedKeys = new LinkedList<>();

	/**
	 * Private constructor for Singleton Pattern.
	 */
	private MinimumRemainingValues() {
		super();
	}

	public void setUsedKeys(List<String> usedKeys) {
		this.usedKeys = usedKeys;
	}

	public List<String> getUsedKeys() {
		return usedKeys;
	}

	public List<String> getClonedUsedKeys() {
		final List<String> newList = new LinkedList<>();
		newList.addAll(this.usedKeys);
		return newList;
	}

	public Map<String, String> init(Map<String, String> domains) {
		this.usedKeys.clear();
		return this.sortByDomains(domains);
	}

	public Map<String, String> sortByDomains(Map<String, String> domains) {
		return SuDokus.sortByValue(domains, this.comparator);
	}

	public String getNextKey(Map<String, String> domains) {
		final Set<String> keySet = domains.keySet();
		for (final String key : keySet) {
			if (!this.usedKeys.contains(key)) {
				return key;
			}
		}
		return StringUtils.EMPTY;
	}

	public boolean hasNextKey(Map<String, String> domains) {
		return StringUtils.isNotEmpty(this.getNextKey(domains));
	}
	
	public void burnKey(String key) {
		this.usedKeys.add(key);
	}
	
	private static class MinimumRemainingValuesHolder {
		public static final MinimumRemainingValues INSTANCE = new MinimumRemainingValues();
	}

	public static MinimumRemainingValues getInstance() {
		return MinimumRemainingValuesHolder.INSTANCE;
	}

}
