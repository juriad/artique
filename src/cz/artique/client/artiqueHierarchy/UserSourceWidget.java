package cz.artique.client.artiqueHierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.code.gwteyecandy.Tooltip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.CachingHistoryUtils;
import cz.artique.client.artiqueSources.UserSourceDialog;
import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.InnerNode;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.client.i18n.ArtiqueConstants;
import cz.artique.client.i18n.ArtiqueI18n;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterType;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.source.SourceType;
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

	private ClickHandler createNewHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			UserSource userSource = new UserSource();
			userSource.setHierarchy(getHierarchy().getHierarchy());
			UserSourceDialog.DIALOG.showDialog(userSource);
		}
	};

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

		if (hierarchy instanceof LeafNode) {
			LeafNode<UserSource> leaf = (LeafNode<UserSource>) hierarchy;
			final UserSource item = leaf.getItem();
			String detailTooltip =
				ArtiqueI18n.I18N.getConstants().detailUserSourceTooltip();
			createImage(panel, ArtiqueWorld.WORLD.getResources().detail(),
				new ClickHandler() {
					public void onClick(ClickEvent event) {
						UserSourceDialog.DIALOG.showDialog(item);
					}
				}, detailTooltip);

			if (SourceType.MANUAL.equals(leaf.getItem().getSourceType())) {
				// manual source
				String addManualItemTooltip =
					ArtiqueI18n.I18N.getConstants().addManualItemTooltip();
				createImage(panel, ArtiqueWorld.WORLD.getResources().add(),
					new ClickHandler() {
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub

						}
					}, addManualItemTooltip);
			}
		} else if (hierarchy instanceof InnerNode) {
			String createNewTooltip =
				ArtiqueI18n.I18N.getConstants().createNewUserSourceTooltip();
			createImage(panel, ArtiqueWorld.WORLD.getResources().createNew(),
				createNewHandler, createNewTooltip);

			if (hierarchy.getParent() == null) {
				// root node
				final Image showHideDisabled = new Image();
				showHideDisabled.setStylePrimaryName("hiddenAction");
				panel.add(showHideDisabled);
				final Tooltip tt = addTooltip("");
				tt.attachTo(showHideDisabled);

				showHideDisabled.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						ArtiqueWorld.WORLD.getSourcesTree().toggleDisabled();
						setShowHideDisabled(showHideDisabled, tt);
					}
				});
				setShowHideDisabled(showHideDisabled, tt);
			}

			String sourceFolderTooltip =
				ArtiqueI18n.I18N.getConstants().sourceFolderTooltip();
			Tooltip folderTooltip = addTooltip(sourceFolderTooltip);
			folderTooltip.attachTo(anchor);
		}
		refresh();
	}

	private void setShowHideDisabled(Image image, Tooltip tt) {
		ArtiqueConstants constants = ArtiqueI18n.I18N.getConstants();
		boolean showingDisabled =
			ArtiqueWorld.WORLD.getSourcesTree().isShowingDisabled();
		String tooltip;
		ImageResource ir;
		if (showingDisabled) {
			tooltip = constants.hideDisabledSourcesTooltip();
			ir = ArtiqueWorld.WORLD.getResources().hideDisabled();
		} else {
			tooltip = constants.showDisabledSourcesTooltip();
			ir = ArtiqueWorld.WORLD.getResources().hideDisabled();
		}
		tt.setText(tooltip);
		image.setResource(ir);
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

	public boolean refresh() {
		anchor.setName(getHierarchy().getName());
		serialized = CachingHistoryUtils.UTILS.serializeListFilter(filter);
		anchor.setHref("#" + serialized);
		if (getHierarchy() instanceof LeafNode) {
			return ((LeafNode<UserSource>) getHierarchy())
				.getItem()
				.isWatching();
		} else if (getHierarchy() instanceof InnerNode) {
			return false;
		} else {
			return false;
		}
	}

}
