package br.usp.falvojr.sudoku.heuristic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * TODO Minimum Remaining Values (MRV) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class MinimumRemainingValues {

    private final Comparator<String> comparator = (values1, value2) -> values1.length() - value2.length();
    private int nextIndex, endIndex;

    /**
     * Private constructor for Singleton Pattern.
     */
    private MinimumRemainingValues() {
	super();
    }

    public int getCurrentIndex() {
	return this.nextIndex;
    }

    public void setCurrentIndex(int currentIndex) {
	this.nextIndex = currentIndex;
    }

    public Map<String, String> init(Map<String, String> domains) {
	this.nextIndex = 0;
	this.endIndex = domains.size();
	return this.sortByDomains(domains);
    }

    public Map<String, String> sortByDomains(Map<String, String> domains) {
	return SuDokus.sortByValue(domains, this.comparator);
    }

    public String getNextKey(final Map<String, String> domains) {
	final List<String> keys = new ArrayList<String>(domains.keySet());
	if (hasNextKey()) {
	    return keys.get(this.nextIndex++);
	}
	return StringUtils.EMPTY;
    }

    public boolean hasNextKey() {
	return this.nextIndex < this.endIndex;
    }

    private static class MinimumRemainingValuesHolder {
	public static final MinimumRemainingValues INSTANCE = new MinimumRemainingValues();
    }

    public static MinimumRemainingValues getInstance() {
	return MinimumRemainingValuesHolder.INSTANCE;
    }

}
