package cz.artique.shared.model.hierarchy;

import java.util.ArrayList;
import java.util.List;

public class HierarchyUtils {
	private static final int fillZeros = 4;

	private static final String format = "%0" + fillZeros + "d";
	public static final String root = formatNumber(0) + "root";
	public static String formatNumber(int number) {
		return String.format(format, number);
	}

	public static String[] getOrderAndName(String hierarchy) {
		int start = hierarchy.lastIndexOf('[');
		int end = hierarchy.lastIndexOf(']');

		String order = hierarchy.substring(start + 1, end);
		String name = hierarchy.substring(end + 1);
		return new String[] { order, name };
	}

	public static List<String> getPrefixes(String hierarchy) {
		List<String> prefixes = new ArrayList<String>();
		prefixes.add(root);
		int start = 1;
		int slash;
		while ((slash = hierarchy.indexOf('/', start)) > 0) {
			prefixes.add(hierarchy.substring(0, slash));
			start = slash + 1;
		}
		return prefixes;
	}

	private HierarchyUtils() {}
}
