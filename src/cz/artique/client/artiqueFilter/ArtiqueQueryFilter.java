package cz.artique.client.artiqueFilter;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.artiqueLabels.ArtiqueLabelWidget;
import cz.artique.client.artiqueLabels.ArtiqueLabelsManager;
import cz.artique.client.labels.LabelWidgetFactory;
import cz.artique.client.labels.suggestion.SuggesionLabelFactory;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;

public class ArtiqueQueryFilter extends AbstractQueryFilter {

	public ArtiqueQueryFilter(LabelWidgetFactory<Label> factory,
			SuggesionLabelFactory<Label> factory2, Filter filter) {
		super(ArtiqueLabelWidget.factory, factory2, filter);
	}

	private static final Label AND;
	private static final Label OR;

	static {
		AND = new Label();
		AND.setName("AND");
		AND.setLabelType(LabelType.SYSTEM);

		OR = new Label();
		OR.setName("OR");
		OR.setLabelType(LabelType.SYSTEM);
	}

	@Override
	protected List<Label> getLabelsFromFilter(Filter filter) {
		List<Label> labels = new ArrayList<Label>();
		if (filter.getFilterObjects() != null
			&& !filter.getFilterObjects().isEmpty()) {
			for (Filter sub : filter.getFilterObjects()) {
				List<Label> subLabels = new ArrayList<Label>();
				if (sub.getLabels() != null && !sub.getLabels().isEmpty()) {
					for (Key key : sub.getLabels()) {
						Label labelByKey =
							ArtiqueLabelsManager.MANAGER.getLabelByKey(key);
						if (labelByKey != null) {
							if (!subLabels.isEmpty()) {
								subLabels.add(AND);
							}
							subLabels.add(labelByKey);
						}
					}
				}
				if (!subLabels.isEmpty()) {
					if (!labels.isEmpty()) {
						labels.add(OR);
					}
					labels.addAll(subLabels);
				}
			}
		}
		if (filter.getLabels() != null && !filter.getLabels().isEmpty()) {
			for (Key key : filter.getLabels()) {
				Label labelByKey =
					ArtiqueLabelsManager.MANAGER.getLabelByKey(key);
				if (labelByKey != null) {
					if (!labels.isEmpty()) {
						labels.add(OR);
					}
					labels.add(labelByKey);
				}
			}
		}
		return labels;
	}

	@Override
	protected com.google.gwt.user.client.ui.Label newAddButton() {
		return new com.google.gwt.user.client.ui.Label(addLabelSign);
	}

	private List<Label> labelsToSuggest = null;

	@Override
	protected List<Label> getLabelsToSuggest() {
		if (labelsToSuggest == null) {
			// XXX asi by chtelo filtrovat
			labelsToSuggest.addAll(ArtiqueLabelsManager.MANAGER.getLabels());
		}
		return labelsToSuggest;
	}

	@Override
	protected Label getAddedLabel(SuggestionResult<Label> selectedItem) {
		if (selectedItem.isHasValue()) {
			if (selectedItem.isExisting()) {
				return selectedItem.getExistingValue();
			} else {
				String value = selectedItem.getNewValue();
				if (value.equalsIgnoreCase("AND")) {
					return AND;
				} else if (value.equalsIgnoreCase("OR")) {
					return OR;
				}
				return ArtiqueLabelsManager.MANAGER.getLabelByName(selectedItem
					.getNewValue());
			}
		}
		// ignore non existing
		return null;
	}

	@Override
	public Filter getFilter() {
		List<List<Label>> topListFilter = new ArrayList<List<Label>>();
		ArrayList<Label> secondaryListFilter = new ArrayList<Label>();
		for (Label l : getLabels()) {
			if (l.equals(OR)) {
				topListFilter.add(secondaryListFilter);
				secondaryListFilter = new ArrayList<Label>();
			} else if (l.equals(AND)) {
				continue;
			} else {
				secondaryListFilter.add(l);
			}
		}
		topListFilter.add(secondaryListFilter);

		User user = ArtiqueWorld.WORLD.getUser();

		List<Key> topLabels = new ArrayList<Key>();
		List<Filter> topFilters = new ArrayList<Filter>();
		for (List<Label> sub : topListFilter) {
			if (sub.isEmpty()) {
				// do nothing, ignore
			} else if (sub.size() == 1) {
				topLabels.add(sub.get(0).getKey());
			} else {
				Filter subFilter = new Filter();
				subFilter.setUser(user);
				subFilter.setType(FilterType.SECOND_LEVEL_FILTER);
				subFilter.setLabels(labelsToKeys(sub));
				topFilters.add(subFilter);
			}
		}
		Filter filter = new Filter();
		filter.setUser(user);
		filter.setType(FilterType.TOP_LEVEL_FILTER);
		if (!topFilters.isEmpty()) {
			filter.setFilterObjects(topFilters);
		}
		if (!topLabels.isEmpty()) {
			filter.setLabels(topLabels);
		}
		return filter;
	}

	private List<Key> labelsToKeys(List<Label> labels) {
		List<Key> keys = new ArrayList<Key>();
		for (Label l : labels) {
			keys.add(l.getKey());
		}
		return keys;
	}
}
