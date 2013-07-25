package cz.artique.client.history;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterLevel;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;

/**
 * {@link HistoryUtils} extended with caching serialized baseListFilter.
 * 
 * @author Adam Juraszek
 * 
 */
public class CachingHistoryUtils extends HistoryUtils {
	public static final CachingHistoryUtils UTILS = new CachingHistoryUtils();

	private Map<Label, Filter> filterForLabel = new HashMap<Label, Filter>();

	private Map<Key, String> serializedFilterForLabelKey =
		new HashMap<Key, String>();

	protected CachingHistoryUtils() {}

	private String serializedBaseListFilter;

	/**
	 * Sets current baseListFilter.
	 */
	public void setBaseListFilter() {
		ListFilter baseListFilter = HistoryManager.HISTORY.getBaseListFilter();
		serializedBaseListFilter = serializeBaseListFilter(baseListFilter);
	}

	/**
	 * Creates {@link Filter} for a single {@link Label}.
	 * 
	 * @param label
	 *            single {@link Label}
	 * @return {@link Filter} containing {@link Label}
	 */
	public Filter getFilterForLabel(Label label) {
		Filter f = filterForLabel.get(label);
		if (f == null) {
			f = new Filter();
			f.setLevel(FilterLevel.TOP_LEVEL_FILTER);
			f.setLabels(Arrays.asList(label.getKey()));
			filterForLabel.put(label, f);
		}
		return f;
	}

	/**
	 * If filter contains single {@link Label} use cached serialized
	 * {@link Filter}.
	 * 
	 * @see cz.artique.client.history.HistoryUtils#serializeFilter(cz.artique.shared.model.label.Filter)
	 */
	@Override
	public String serializeFilter(Filter filter) {
		if (filter != null
			&& (filter.getFilterObjects() == null || filter
				.getFilterObjects()
				.isEmpty()) && filter.getLabels() != null
			&& filter.getLabels().size() == 1) {
			// single label
			Key key = filter.getLabels().get(0);
			String serialized = serializedFilterForLabelKey.get(key);
			if (serialized == null) {
				serialized = super.serializeFilter(filter);
				serializedFilterForLabelKey.put(key, serialized);
			}
			return serialized;
		} else {
			// too complex
			return super.serializeFilter(filter);
		}
	}

	/**
	 * BaseListFilter is already pre-serialized.
	 * 
	 * @see cz.artique.client.history.HistoryUtils#serializeBaseListFilter(cz.artique.shared.model.label.ListFilter)
	 */
	@Override
	public String serializeBaseListFilter(ListFilter listFilter) {
		return serializedBaseListFilter;
	}

	/**
	 * Suppose baseListFilter is the current one.
	 * 
	 * @param filter
	 *            {@link Filter} to serialize
	 * @return serialized {@link ListFilter}
	 */
	public String serializeListFilter(Filter filter) {
		if (filter == null) {
			return "";
		}
		String serializedFilter = serializeFilter(filter);
		String serializedBaseListFilter = serializeBaseListFilter(null);
		return serializeListFilter(serializedBaseListFilter, serializedFilter);
	}
}
