package cz.artique.client.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterLevel;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.label.ListFilterOrder;

/**
 * Serializes and deserializes {@link ListFilter}s and their parts.
 * 
 * @author Adam Juraszek
 * 
 */
public class HistoryUtils {
	public static final HistoryUtils UTILS = new HistoryUtils();

	protected HistoryUtils() {}

	/**
	 * Combines together serialized baseListFilter and serialized filter
	 * 
	 * @param serializedBaseListFilter
	 *            serialized baseListFilter
	 * @param serializedFilter
	 *            serialized filter
	 * @return serialized listFilter
	 */
	public String serializeListFilter(String serializedBaseListFilter,
			String serializedFilter) {
		StringBuilder sb = new StringBuilder();
		if (serializedBaseListFilter != null) {
			sb.append(serializedBaseListFilter);
		}

		if (sb.length() > 0) {
			sb.append("&");
		}
		sb.append("filter=");
		sb.append(serializedFilter);
		return sb.toString();

	}

	/**
	 * Serializes baseListFilter.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} to serialize
	 * @return serialized baseListFilter
	 */
	public String serializeBaseListFilter(ListFilter listFilter) {
		if (listFilter == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		if (listFilter != null) {
			if (listFilter.getRead() != null) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append("read=");
				sb.append(listFilter.getRead() ? "true" : "false");
			}

			if (listFilter.getStartFrom() != null) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append("startFrom=");
				sb.append(listFilter.getStartFrom().getTime());
			}
			if (listFilter.getEndTo() != null) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append("endTo=");
				sb.append(listFilter.getEndTo().getTime());
			}
			if (listFilter.getOrder() != null) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append("order=");
				sb.append(listFilter.getOrder().name());
			}
		}

		return sb.toString();
	}

	/**
	 * Serialize whole {@link ListFilter}.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} to serialize
	 * @return serialized listFilter
	 */
	public String serializeListFilter(ListFilter listFilter) {
		if (listFilter == null) {
			return "";
		}
		String serializedFilter = serializeFilter(listFilter.getFilterObject());
		String serializedBaseListFilter = serializeBaseListFilter(listFilter);
		return serializeListFilter(serializedBaseListFilter, serializedFilter);
	}

	/**
	 * Deserializes whole {@link ListFilter}.
	 * 
	 * @param token
	 *            token to deserialize
	 * @return deserialized listFilter
	 */
	public ListFilter deserializeListFilter(String token) {
		token = token.trim();
		ListFilter lf = new ListFilter();
		while (!token.isEmpty()) {
			if (token.startsWith("read=")) {
				Boolean read = null;
				if (token.startsWith("read=true")) {
					read = true;
				} else if (token.startsWith("read=false")) {
					read = false;
				}
				lf.setRead(read);
				token = consumeParameter(token);
			} else if (token.startsWith("startFrom=")) {
				Date start = null;
				String[] split = token.split("[=&]", 3);
				if (split.length >= 2) {
					try {
						Long l = Long.parseLong(split[1]);
						start = new Date(l);
					} catch (Exception e) {}
				}
				lf.setStartFrom(start);
				token = consumeParameter(token);
			} else if (token.startsWith("endTo=")) {
				Date end = null;
				String[] split = token.split("[=&]", 3);
				if (split.length >= 2) {
					try {
						Long l = Long.parseLong(split[1]);
						end = new Date(l);
					} catch (Exception e) {}
				}
				lf.setEndTo(end);
				token = consumeParameter(token);
			} else if (token.startsWith("order=")) {
				ListFilterOrder order = ListFilterOrder.getDefault();
				String[] split = token.split("[=&]", 3);
				if (split.length >= 2) {
					try {
						order = ListFilterOrder.valueOf(split[1]);
					} catch (Exception e) {}
				}
				lf.setOrder(order);
				token = consumeParameter(token);
			} else if (token.startsWith("filter=")) {
				Filter filter = null;
				String[] split = token.split("[=&]", 2);
				if (split.length >= 2) {
					filter = deserializeFilter(split[1]);
				}
				lf.setFilterObject(filter);
				token = "";
			} else {
				token = consumeParameter(token);
			}
		}
		return lf;
	}

	/**
	 * Consumes one parameter from input string.
	 * 
	 * @param s
	 *            input string
	 * @return consumed parameter
	 */
	protected String consumeParameter(String s) {
		int index = s.indexOf("&");
		if (index >= 0) {
			s = s.substring(index + 1);
		} else {
			s = "";
		}
		return s;
	}

	/**
	 * Serializes {@link Label}.
	 * 
	 * @param l
	 *            label to serialize
	 * @return serialized label
	 */
	protected String serializeLabel(Label l) {
		switch (l.getLabelType()) {
		case SYSTEM:
			return "lsys$" + l.getName();
		case USER_DEFINED:
			return "lusr$" + l.getName();
		case USER_SOURCE:
			return "lsrc$" + l.getName();
		default:
			return "";
		}
	}

	/**
	 * Deserializes list of {@link Label}s.
	 * Labels manager must be ready.
	 * 
	 * @param labelNames
	 * @return deserialized list of {@link Label}s
	 */
	protected List<Label> deserializeLabels(final List<String> labelNames) {
		if (!Managers.LABELS_MANAGER.isReady()) {
			return null;
		}

		List<Label> labels = new ArrayList<Label>();
		for (String labelName : labelNames) {
			labelName = labelName.trim();
			final LabelType type;
			if (labelName.startsWith("lsys")) {
				type = LabelType.SYSTEM;
			} else if (labelName.startsWith("lusr")) {
				type = LabelType.USER_DEFINED;
			} else if (labelName.startsWith("lsrc")) {
				type = LabelType.USER_SOURCE;
			} else {
				labels.add(null);
				continue;
			}

			// l...$
			labelName = labelName.substring(5);
			Label label =
				Managers.LABELS_MANAGER.getLabelByName(type, labelName);
			labels.add(label);
		}

		return labels;
	}

	/**
	 * Serializes {@link Filter}.
	 * Labels manager must be ready.
	 * 
	 * @param filter
	 *            {@link Filter} to serialize
	 * @return serialized {@link Filter}
	 */
	public String serializeFilter(Filter filter) {
		if (!Managers.LABELS_MANAGER.isReady()) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		if (filter == null) {
			return "";
		}

		if (FilterLevel.TOP_LEVEL_FILTER.equals(filter.getLevel())) {
			if (filter.getFilterObjects() != null
				&& !filter.getFilterObjects().isEmpty()) {
				for (int i = 0; i < filter.getFilterObjects().size(); i++) {
					String subFilter =
						serializeFilter(filter.getFilterObjects().get(i));
					if (sb.length() > 0) {
						sb.append(" OR ");
					}
					sb.append(subFilter);
				}
			}
		}

		if (filter.getLabels() != null && !filter.getLabels().isEmpty()) {
			for (int i = 0; i < filter.getLabels().size(); i++) {
				Key key = filter.getLabels().get(i);
				Label label = Managers.LABELS_MANAGER.getLabelByKey(key);
				String l = serializeLabel(label);

				if (sb.length() > 0) {
					if (FilterLevel.TOP_LEVEL_FILTER.equals(filter.getLevel())
						|| i == 0) {
						sb.append(" OR ");
					} else {
						sb.append(" AND ");
					}
				}
				sb.append(l);
			}
		}
		return sb.toString();
	}

	/**
	 * Deserializes {@link Filter} Labels manager must be ready.
	 * 
	 * @param string
	 *            serialized filter
	 * @return deserialized filter
	 */
	protected Filter deserializeFilter(String string) {
		if (!Managers.LABELS_MANAGER.isReady()) {
			return null;
		}

		Filter filter = new Filter();
		filter.setLevel(FilterLevel.TOP_LEVEL_FILTER);

		List<Key> labels = new ArrayList<Key>();
		List<Filter> filterObjects = new ArrayList<Filter>();

		String[] topLevel = string.split(" OR ");
		for (String top : topLevel) {
			top = top.trim();
			String[] secondLevel = top.split(" AND ");
			if (secondLevel.length > 1) {
				List<Label> deserializeLabels =
					deserializeLabels(Arrays.asList(secondLevel));
				List<Key> keys = new ArrayList<Key>();
				for (Label l : deserializeLabels) {
					if (l != null) {
						keys.add(l.getKey());
					}
				}
				if (keys.size() > 1) {
					Filter second = new Filter();
					second.setLevel(FilterLevel.SECOND_LEVEL_FILTER);
					second.setLabels(keys);
					filterObjects.add(second);
				} else if (keys.size() == 1) {
					labels.add(keys.get(0));
				}
			} else {
				List<Label> deserializeLabels =
					deserializeLabels(Arrays.asList(secondLevel[0]));
				Label l = deserializeLabels.get(0);
				if (l != null) {
					labels.add(l.getKey());
				}
			}
		}

		if (!labels.isEmpty()) {
			filter.setLabels(labels);
		}

		if (!filterObjects.isEmpty()) {
			filter.setFilterObjects(filterObjects);
		}

		return filter;
	}
}
