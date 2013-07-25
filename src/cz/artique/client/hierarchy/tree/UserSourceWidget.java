package cz.artique.client.hierarchy.tree;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

import cz.artique.client.hierarchy.Hierarchy;
import cz.artique.client.hierarchy.HierarchyTreeWidget;
import cz.artique.client.hierarchy.HierarchyTreeWidgetFactory;
import cz.artique.client.hierarchy.InnerNode;
import cz.artique.client.hierarchy.LeafNode;
import cz.artique.client.history.CachingHistoryUtils;
import cz.artique.client.history.HistoryManager;
import cz.artique.client.i18n.I18n;
import cz.artique.client.items.ManualItemDialog;
import cz.artique.client.sources.UserSourceDialog;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.FilterLevel;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.SourceType;
import cz.artique.shared.model.source.UserSource;

/**
 * Widget representing {@link UserSource} in {@link SourcesTree}.
 * 
 * @author Adam Juraszek
 * 
 */
public class UserSourceWidget extends AbstractHierarchyTreeWidget<UserSource> {

	/**
	 * Factory.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
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

	private Filter filter;

	private String serialized;

	private final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

	private SourcesTree sourcesTree;

	private Image showHideDisabled;

	/**
	 * Constructs widget depending on whether it is root, inner, leaf node.
	 * 
	 * @param hierarchy
	 *            hierarchy node
	 */
	public UserSourceWidget(Hierarchy<UserSource> hierarchy) {
		super(hierarchy);
		anchor = createAnchor(hierarchy.getName(), null, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (impl.handleAsClick(Event.as(event.getNativeEvent()))) {
					ListFilter baseListFilter =
						HistoryManager.HISTORY.getBaseListFilter();
					baseListFilter.setFilterObject(filter);
					HistoryManager.HISTORY.setListFilter(baseListFilter,
						serialized);
					event.preventDefault();
				}
			}
		}, null);

		if (hierarchy instanceof LeafNode) {
			LeafNode<UserSource> leaf = (LeafNode<UserSource>) hierarchy;
			final UserSource item = leaf.getItem();
			String detailTooltip =
				I18n.getHierarchyTreeConstants().detailUserSourceTooltip();
			createImage(HierarchyResources.INSTANCE.detail(),
				new ClickHandler() {
					public void onClick(ClickEvent event) {
						UserSourceDialog.DIALOG.showDialog(item);
					}
				}, detailTooltip);

			if (SourceType.MANUAL.equals(leaf.getItem().getSourceType())) {
				// manual source
				String addManualItemTooltip =
					I18n.getHierarchyTreeConstants().addManualItemTooltip();
				createImage(HierarchyResources.INSTANCE.add(),
					new ClickHandler() {
						public void onClick(ClickEvent event) {
							ManualItemDialog.DIALOG.showDialog();
						}
					}, addManualItemTooltip);
			}
		} else if (hierarchy instanceof InnerNode) {
			String createNewTooltip =
				I18n.getHierarchyTreeConstants().createNewUserSourceTooltip();
			createImage(HierarchyResources.INSTANCE.createNew(),
				createNewHandler, createNewTooltip);

			if (hierarchy.getParent() == null) {
				// root node
				anchor.setText(I18n
					.getHierarchyTreeConstants()
					.sourceRootText());
				showHideDisabled = new Image();
				getWidget().add(showHideDisabled);

				showHideDisabled.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if (getSourcesTree() == null) {
							return;
						}
						getSourcesTree().toggleDisabled();
						setShowHideDisabled(showHideDisabled);
					}
				});
				setShowHideDisabled(showHideDisabled);
			}

			String sourceFolderTooltip =
				I18n.getHierarchyTreeConstants().sourceFolderTooltip();
			anchor.setTitle(sourceFolderTooltip);
		}
		refresh();
	}

	/**
	 * Set icon and tooltip according to current {@link SourcesTree} status.
	 * 
	 * @param image
	 *            show-hide image
	 */
	private void setShowHideDisabled(Image image) {
		if (getSourcesTree() == null) {
			return;
		}
		HierarchyTreeConstants constants = I18n.getHierarchyTreeConstants();
		boolean showingDisabled = getSourcesTree().isShowingDisabled();
		String tooltip;
		ImageResource ir;
		if (showingDisabled) {
			tooltip = constants.hideDisabledSourcesTooltip();
			ir = HierarchyResources.INSTANCE.hideDisabled();
		} else {
			tooltip = constants.showDisabledSourcesTooltip();
			ir = HierarchyResources.INSTANCE.showDisabled();
		}
		image.setResource(ir);
		image.setTitle(tooltip);
		image.setStylePrimaryName("hiddenAction");
	}

	/**
	 * @return {@link Filter} representing disjunction of {@link UserSource}s in
	 *         subtree
	 */
	private Filter constructFilter() {
		Filter f = new Filter();
		f.setLevel(FilterLevel.TOP_LEVEL_FILTER);
		if (getHierarchy().getParent() != null) {
			f.setLabels(getListOfLabels());
		}
		return f;
	}

	/**
	 * @return list of {@link Label}s of {@link Source}s in subtree
	 */
	private List<Key> getListOfLabels() {
		List<UserSource> list = new ArrayList<UserSource>();
		getHierarchy().getAll(list);
		List<Key> labels = new ArrayList<Key>(list.size());
		for (UserSource us : list) {
			labels.add(us.getLabel());
		}
		return labels;
	}

	/**
	 * Rebuilds hash part of href.
	 * 
	 * @see cz.artique.client.hierarchy.tree.AbstractHierarchyTreeWidget#refresh()
	 */
	@Override
	public boolean refresh() {
		anchor.setName(getHierarchy().getName());
		filter = constructFilter();
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

	/**
	 * @return {@link SourcesTree} this widget is inside
	 */
	public SourcesTree getSourcesTree() {
		return sourcesTree;
	}

	/**
	 * @param sourcesTree
	 *            {@link SourcesTree} this widget is inside
	 */
	public void setSourcesTree(SourcesTree sourcesTree) {
		this.sourcesTree = sourcesTree;
		if (showHideDisabled != null) {
			setShowHideDisabled(showHideDisabled);
		}
	}

}
