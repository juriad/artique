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
import cz.artique.client.artiqueHistory.CachingHistoryUtils;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.source.UserSource;

public class UserSourceWidget extends Composite
		implements HierarchyTreeWidget<UserSource> {

	public static class UserSourceWidgetFactory
			implements HierarchyTreeWidgetFactory<UserSource> {

		public static final HierarchyTreeWidgetFactory<UserSource> FACTORY =
			new UserSourceWidgetFactory();

		public HierarchyTreeWidget<UserSource> createWidget(
				Hierarchy<UserSource> hierarchy) {
			return new UserSourceWidget(hierarchy);
		}
	}

	private final Hierarchy<UserSource> hierarchy;

	private final Anchor anchor;

	private Filter filter;

	private String serialized;

	public UserSourceWidget(Hierarchy<UserSource> hierarchy) {
		this.hierarchy = hierarchy;

		anchor = new Anchor(hierarchy.getName());
		initWidget(anchor);
		filter = constructFilter();
		refresh();

		anchor.addClickHandler(new ClickHandler() {
			final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

			public void onClick(ClickEvent event) {
				if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {
					ListFilter baseListFilter =
						ArtiqueHistory.HISTORY.getBaseListFilter();
					baseListFilter.setFilterObject(filter);
					ArtiqueHistory.HISTORY.addListFilter(baseListFilter,
						serialized);

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

	public void refresh() {
		serialized = CachingHistoryUtils.UTILS.serializeListFilter(filter);
		anchor.setHref("#" + serialized);
	}

}
