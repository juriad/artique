package cz.artique.client.artiqueHierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
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

public class UserSourceWidget extends AbstractHierarchyTreeWidget<UserSource> {

	public static class UserSourceWidgetFactory
			implements HierarchyTreeWidgetFactory<UserSource> {

		public static final HierarchyTreeWidgetFactory<UserSource> FACTORY =
			new UserSourceWidgetFactory();

		public HierarchyTreeWidget<UserSource> createWidget(
				Hierarchy<UserSource> hierarchy) {
			return new UserSourceWidget(hierarchy);
		}
	}

	private final Anchor anchor;

	private final Filter filter;

	private String serialized;

	public UserSourceWidget(Hierarchy<UserSource> hierarchy) {
		super(hierarchy);

		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		filter = constructFilter();
		anchor =
			createAnchor(panel, hierarchy.getName(), null, new ClickHandler() {
				final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

				public void onClick(ClickEvent event) {
					if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {
						ListFilter baseListFilter =
							ArtiqueHistory.HISTORY.getBaseListFilter();
						baseListFilter.setFilterObject(filter);
						ArtiqueHistory.HISTORY.setListFilter(baseListFilter,
							serialized);
						event.preventDefault();
					}
				}
			}, null);
		// TODO tooltip
		refresh();
	}

	private Filter constructFilter() {
		Filter f = new Filter();
		f.setUser(ArtiqueWorld.WORLD.getUser());
		f.setType(FilterType.TOP_LEVEL_FILTER);
		if (getHierarchy().getParent() != null) {
			f.setLabels(getListOfLabels());
		}
		return f;
	}

	private List<Key> getListOfLabels() {
		List<UserSource> list = new ArrayList<UserSource>();
		getHierarchy().getAll(list);
		List<Key> labels = new ArrayList<Key>(list.size());
		for (UserSource us : list) {
			labels.add(us.getLabel());
		}
		return labels;
	}

	public void refresh() {
		anchor.setName(getHierarchy().getName());
		serialized = CachingHistoryUtils.UTILS.serializeListFilter(filter);
		anchor.setHref("#" + serialized);
	}

}
