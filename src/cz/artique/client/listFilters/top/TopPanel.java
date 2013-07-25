package cz.artique.client.listFilters.top;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.history.CachingHistoryUtils;
import cz.artique.client.history.HistoryEvent;
import cz.artique.client.history.HistoryHandler;
import cz.artique.client.history.HistoryItem;
import cz.artique.client.i18n.I18n;
import cz.artique.client.listFilters.AdhocDialog;
import cz.artique.client.listFilters.ListFiltersConstants;
import cz.artique.client.listing.ArtiqueList;
import cz.artique.client.listing.NewDataEvent;
import cz.artique.client.listing.NewDataHandler;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.ListFilter;

public class TopPanel extends Composite {
	private static TopPanelUiBinder uiBinder = GWT
		.create(TopPanelUiBinder.class);

	interface TopPanelUiBinder extends UiBinder<Widget, TopPanel> {}

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("TopPanel.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	@UiField
	Button clearButton;

	@UiField
	Button markAllReadButton;

	@UiField
	Button refreshButton;

	@UiField
	Button addNewItemsButton;

	@UiField
	CurrentQueryFilter currentFilter;

	@UiField
	CurrentCriteria currentCriteria;

	private final ArtiqueList list;

	public TopPanel(ArtiqueList list) {
		this.list = list;
		res.style().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		setStylePrimaryName("topPanel");

		Managers.HISTORY_MANAGER.addHistoryHandler(new HistoryHandler() {
			public void onHistoryChanged(HistoryEvent e) {
				HistoryItem lastHistoryItem =
					Managers.HISTORY_MANAGER.getLastHistoryItem();
				if (lastHistoryItem == null) {
					return;
				}
				ListFilter listFilter = lastHistoryItem.getListFilter();
				Filter filter = listFilter.getFilterObject();
				ListFiltersConstants constants = I18n.getListFiltersConstants();
				if (filter != null && filter.flat().size() > 0) {
					clearButton.setText(constants.clearLabels());
				} else {
					clearButton.setText(constants.clearAll());
				}

				currentFilter.setFilter(filter);
				currentCriteria.setListFilter(listFilter);
			}
		});

		list.addNewDataHandler(new NewDataHandler() {
			public void onNewData(NewDataEvent event) {
				int available = TopPanel.this.list.getAvailableHeadSize();
				ListFiltersConstants constants = I18n.getListFiltersConstants();
				if (available > 0) {
					addNewItemsButton.setEnabled(true);
					addNewItemsButton.setText(constants.addNewItems() + ": "
						+ available);
				} else {
					addNewItemsButton.setEnabled(false);
					addNewItemsButton.setText(constants.noNewItems());
				}
			}
		});
	}

	@UiHandler("editButton")
	protected void editButtonClicked(ClickEvent event) {
		AdhocDialog.DIALOG.showDialog();
	}

	@UiHandler("clearButton")
	protected void clearButtonClicked(ClickEvent event) {
		HistoryItem lastHistoryItem =
			Managers.HISTORY_MANAGER.getLastHistoryItem();
		if (lastHistoryItem == null) {
			return;
		}

		ListFilter listFilter = lastHistoryItem.getListFilter();
		Filter filter = listFilter.getFilterObject();
		if (filter != null && filter.flat().size() > 0) {
			listFilter = Managers.HISTORY_MANAGER.getBaseListFilter();
		} else {
			listFilter = new ListFilter();
		}
		Managers.HISTORY_MANAGER.setListFilter(listFilter,
			CachingHistoryUtils.UTILS.serializeListFilter(listFilter));
	}

	@UiHandler("refreshButton")
	protected void refreshButtonClicked(ClickEvent event) {
		HistoryItem lastHistoryItem =
			Managers.HISTORY_MANAGER.getLastHistoryItem();
		if (lastHistoryItem != null) {
			Managers.HISTORY_MANAGER.setListFilter(
				lastHistoryItem.getListFilter(), lastHistoryItem.getToken());
		}
	}

	@UiHandler("addNewItemsButton")
	protected void addNewItemsButtonClicked(ClickEvent event) {
		list.showHead();
	}

	@UiHandler("markAllReadButton")
	protected void markAllReadButtonClicked(ClickEvent event) {
		list.markAllRead();
	}
}
