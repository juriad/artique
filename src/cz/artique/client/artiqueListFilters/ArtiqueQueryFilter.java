package cz.artique.client.artiqueListFilters;

import static cz.artique.client.artiqueLabels.ArtiqueLabelsManager.AND;
import static cz.artique.client.artiqueLabels.ArtiqueLabelsManager.OR;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.artiqueLabels.ArtiqueLabelSuggestionFactory;
import cz.artique.client.artiqueLabels.ArtiqueLabelWidget;
import cz.artique.client.labels.suggestion.SuggestionResult;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.Label;

public class ArtiqueQueryFilter extends AbstractQueryFilter {

	public ArtiqueQueryFilter() {
		super(Managers.LABELS_MANAGER, ArtiqueLabelWidget.REMOVABLE_FACTORY,
			ArtiqueLabelSuggestionFactory.factory);
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
	protected com.google.gwt.user.client.ui.Label newAddButton() {
		return new com.google.gwt.user.client.ui.Label(addLabelSign);
	}

	@Override
	protected Label getAddedLabel(SuggestionResult<Label> selectedItem) {
		if (selectedItem.isHasValue()) {
			if (selectedItem.isExisting()) {
				return selectedItem.getExistingValue();
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
