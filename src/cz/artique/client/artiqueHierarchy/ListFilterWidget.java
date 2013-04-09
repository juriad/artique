package cz.artique.client.artiqueHierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.HistoryItem;
import cz.artique.client.artiqueHistory.HistoryUtils;
import cz.artique.client.artiqueListFilters.ListFilterDialog;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.shared.model.label.ListFilter;

public class ListFilterWidget extends Composite
		implements HierarchyTreeWidget<ListFilter> {

	public static class ListFilterWidgetFactory
			implements HierarchyTreeWidgetFactory<ListFilter> {

		public static final HierarchyTreeWidgetFactory<ListFilter> FACTORY =
			new ListFilterWidgetFactory();

		public HierarchyTreeWidget<ListFilter> createWidget(
				Hierarchy<ListFilter> hierarchy) {
			return new ListFilterWidget(hierarchy);
		}
	}

	public ListFilterWidget(final Hierarchy<ListFilter> hierarchy) {
		this.hierarchy = hierarchy;

		FlowPanel panel = new FlowPanel();
		initWidget(panel);

		if (hierarchy instanceof LeafNode) {
			LeafNode<ListFilter> leaf = (LeafNode<ListFilter>) hierarchy;
			final ListFilter item = leaf.getItem();

			final String token = HistoryUtils.UTILS.serializeListFilter(item);

			Anchor anchor = new Anchor(hierarchy.getName(), "#" + token);
			panel.add(anchor);

			anchor.addClickHandler(new ClickHandler() {
				final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

				public void onClick(ClickEvent event) {
					if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {
						ArtiqueHistory.HISTORY.setListFilter(item, token);
						event.preventDefault();
					}
				}
			});

			Image detail =
				new Image(ArtiqueWorld.WORLD.getResources().detail());
			detail.setStylePrimaryName("hiddenAction");
			panel.add(detail);
			detail.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					ListFilterDialog.DIALOG.showDialog(item);
				}
			});
		} else {
			InlineLabel label = new InlineLabel(hierarchy.getName());
			panel.add(label);

			Image createNew =
				new Image(ArtiqueWorld.WORLD.getResources().createNew());
			createNew.setStylePrimaryName("hiddenAction");
			panel.add(createNew);
			createNew.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					ListFilter baseListFilter =
						ArtiqueHistory.HISTORY.getBaseListFilter();
					baseListFilter.setHierarchy(hierarchy.getHierarchy());
					ListFilterDialog.DIALOG.showDialog(baseListFilter);
				}
			});

			Image saveCurrent =
				new Image(ArtiqueWorld.WORLD.getResources().saveCurrent());
			saveCurrent.setStylePrimaryName("hiddenAction");
			panel.add(saveCurrent);
			saveCurrent.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					HistoryItem lastHistoryItem = ArtiqueHistory.HISTORY.getLastHistoryItem();
					final ListFilter lf; 
					if(lastHistoryItem == null) {
						lf = ArtiqueHistory.HISTORY.getBaseListFilter();
					} else {
						lf = lastHistoryItem.getListFilter();
					}
					lf.setHierarchy(hierarchy.getHierarchy());
					ListFilterDialog.DIALOG.showDialog(lf);
				}
			});
		}
	}

	private Hierarchy<ListFilter> hierarchy;

	public void refresh() {
		// do nothing
	}

	public Hierarchy<ListFilter> getHierarchy() {
		return hierarchy;
	}

}
