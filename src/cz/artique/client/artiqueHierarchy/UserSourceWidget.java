package cz.artique.client.artiqueHierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryUtils;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.source.UserSource;

public class UserSourceWidget extends Composite {

	private final Hierarchy<UserSource> hierarchy;

	public UserSourceWidget(Hierarchy<UserSource> hierarchy) {
		this.hierarchy = hierarchy;

		final ListFilter listFilter =
			ArtiqueHistory.HISTORY.getBaseListFilter();
		listFilter.setFilterObject(constructFilter());
		final String serialized = HistoryUtils.serializeListFilter(listFilter);
		Anchor source = new Anchor(hierarchy.getName(), "#" + serialized);
		initWidget(source);
		source.addClickHandler(new ClickHandler() {
			final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

			public void onClick(ClickEvent event) {
				if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {

					ArtiqueHistory.HISTORY
						.addListFilter(listFilter, serialized);

					event.preventDefault();
				}
			}
		});
	}

	public Hierarchy<UserSource> getHierarchy() {
		return hierarchy;
	}

	private Filter constructFilter() {
		Filter f = new Filter();
		f.setUser(ArtiqueWorld.WORLD.getUser());
		f.setType(FilterType.TOP_LEVEL_FILTER);
		if (hierarchy.getParent() != null) {
			f.setLabels(getListOfLabels());
		}
		return f;
	}

	private List<Key> getListOfLabels() {
		List<UserSource> list = new ArrayList<UserSource>();
		hierarchy.getAll(list);
		List<Key> labels = new ArrayList<Key>(list.size());
		for (UserSource us : list) {
			labels.add(us.getLabel());
		}
		return labels;
	}

}
