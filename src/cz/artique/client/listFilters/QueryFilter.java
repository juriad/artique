package cz.artique.client.listFilters;

import static cz.artique.client.labels.LabelsManager.AND;
import static cz.artique.client.labels.LabelsManager.OR;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.labels.LabelWidget;
import cz.artique.client.labels.suggestion.LabelsPool;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.Label;

public class QueryFilter extends AbstractQueryFilter {

	private static class MyLabelsPool implements LabelsPool {
		public boolean isNewValueAllowed() {
			return false;
		}

		public List<Label> fullTextSearch(String text) {
			return Managers.LABELS_MANAGER.fullTextSearch(text,
				Managers.LABELS_MANAGER.getLabels(null));
		}

	}

	public QueryFilter() {
		super(new MyLabelsPool());
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
							Managers.LABELS_MANAGER.getLabelByKey(key);
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
				Label labelByKey = Managers.LABELS_MANAGER.getLabelByKey(key);
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
	protected Label getAddedLabel(SuggestionResult selectedItem) {
		return selectedItem.getExistingValue();
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

	@Override
	protected LabelWidget createWidget(Label l) {
		return LabelWidget.FACTORY.createWidget(l);
	}
}
