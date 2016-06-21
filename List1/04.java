import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Main {

	private static final int MAX_N = 100;

	public static void main(String[] args) throws NumberFormatException, IOException {
		int husband[] = new int[100], wife[] = new int[100];
		String[] women_names = new String[MAX_N];

		Scanner reader = new Scanner(System.in);
		
		final int testCases = Integer.valueOf(reader.nextLine());
		for (int cases = 0; cases < testCases; cases++) {
			final Map<String, Integer> men = new HashMap<>();
			final Map<String, Integer> women = new HashMap<>();
			final List<List<Integer>> pm = new ArrayList<>();
			final List<List<Integer>> pw = new ArrayList<List<Integer>>();
			final int n = Integer.valueOf(reader.nextLine());

			final String[] names = reader.nextLine().split(" ");
			for (int i = 0; i < n; i++) {
				men.put(names[i], i);
			}
			for (int i = 0; i < n; i++) {
				final int nameIndex = i + 3;
				final String name = names[nameIndex];
				women.put(name, i);
				women_names[i] = name;
			}
			for (int i = 0; i < n; i++) {
				final String[] preferenceLine = reader.nextLine().split(":");
				final String entity = preferenceLine[0];
				final char[] preferences = preferenceLine[1].toCharArray();
				int pos = men.get(entity);
				for (int j = 0; j < n; j++) {
					try {
						pm.get(pos);
					} catch (IndexOutOfBoundsException e) {
						pm.add(pos, new ArrayList<Integer>());
					}
					pm.get(pos).add(women.get(String.valueOf(preferences[j])));
				}
			}
			for (int i = 0; i < n; i++) {
				final String[] preferenceLine = reader.nextLine().split(":");
				final String entity = preferenceLine[0];
				final char[] preferences = preferenceLine[1].toCharArray();
				int pos = women.get(entity);
				for (int j = 0; j < n; j++) {
					try {
						pw.get(pos);
					} catch (IndexOutOfBoundsException e) {
						pw.add(pos, new ArrayList<Integer>());
					}
					pw.get(pos).add(men.get(String.valueOf(preferences[j])));
				}
			}

			galeShapley(pm, pw, husband, wife, n);
			
			System.out.println();
			for (String menKey : men.keySet()) {
				System.out.printf("%s %s\n", menKey, women_names[wife[men.get(menKey)]]);
			}
		}
		
		reader.close();
	}

	private static void galeShapley(List<List<Integer>> pm, List<List<Integer>> pw, int husband[], int wife[], int n) {
		Queue<Integer> f = new LinkedList<Integer>();

		int m, w;

		Integer inverse[][] = new Integer[MAX_N][MAX_N];

		for (int i = 0; i < n; i++) {
			f.add(i);
			wife[i] = -1;
			husband[i] = -1;
		}
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				inverse[i][pw.get(i).get(j)] = j;

			}
		while (!f.isEmpty()) {
			m = (int) f.poll();
			w = (int) pm.get(m).iterator().next();
			pm.get(m).remove(pm.get(m).indexOf(w));

			if (husband[w] == -1) {
				wife[m] = w;
				husband[w] = m;
			} else if (inverse[w][m] < inverse[w][husband[w]]) {
				wife[husband[w]] = -1;
				f.add(husband[w]);
				wife[m] = w;
				husband[w] = m;
			} else {
				f.add(m);
			}
		}
	}
}
