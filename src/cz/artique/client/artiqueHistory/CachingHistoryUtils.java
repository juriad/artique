package cz.artique.client.artiqueHistory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.ArtiqueWorld;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;

public class CachingHistoryUtils extends HistoryUtils {
	public static final CachingHistoryUtils UTILS = new CachingHistoryUtils();

	private Map<Label, Filter> filterForLabel = new HashMap<Label, Filter>();

	private Map<Key, String> serializedFilterForLabelKey =
		new HashMap<Key, String>();

	protected CachingHistoryUtils() {}

	private String serializedBaseListFilter;

	public void setBaseListFilter() {
		ListFilter baseListFilter = ArtiqueHistory.HISTORY.getBaseListFilter();
		serializedBaseListFilter = serializeBaseListFilter(baseListFilter);
	}

	public Filter getFilterForLabel(Label label) {
		Filter f = filterForLabel.get(label);
		if (f == null) {
			f = new Filter();
			f.setUser(ArtiqueWorld.WORLD.getUser());
			f.setType(FilterType.TOP_LEVEL_FILTER);
			f.setLabels(Arrays.asList(label.getKey()));
			filterForLabel.put(label, f);
		}
		return f;
	}

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

	@Override
	public String serializeBaseListFilter(ListFilter listFilter) {
		return serializedBaseListFilter;
	}

	public String serializeListFilter(Filter filter) {
		if (filter == null) {
			return "";
		}
		String serializedFilter = serializeFilter(filter);
		String serializedBaseListFilter = serializeBaseListFilter(null);
		return serializeListFilter(serializedBaseListFilter, serializedFilter);
	}
}
