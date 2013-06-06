package cz.artique.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.history.HistoryItem;
import cz.artique.client.i18n.I18n;
import cz.artique.client.labels.LabelsDialog;
import cz.artique.client.listing.ArtiqueList;
import cz.artique.client.listing.NewDataEvent;
import cz.artique.client.listing.NewDataHandler;
import cz.artique.client.manager.Managers;
import cz.artique.client.shortcuts.ShortcutsDialog;

public class OptionPanel extends Composite {

	private static OptionUiBinder uiBinder = GWT.create(OptionUiBinder.class);

	interface OptionUiBinder extends UiBinder<Widget, OptionPanel> {}

	@UiField
	Label userName;

	@UiField
	Anchor logout;

	@UiField
	Button editLabelsButton;

	@UiField
	Button editShortcutsButton;

	@UiField
	Button markAllReadButton;

	@UiField
	Button refreshButton;

	@UiField
	Button addNewItemsButton;

	public OptionPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		userName.setText(ArtiqueWorld.WORLD.getUser().getNickname());
		logout.setHref(ArtiqueWorld.WORLD.getUserInfo().getLogoutUrl());

		final ArtiqueList list = ArtiqueWorld.WORLD.getList();
		list.addNewDataHandler(new NewDataHandler() {
			public void onNewData(NewDataEvent event) {
				int available = list.getAvailableHeadSize();
				ArtiqueConstants constants = I18n.getArtiqueConstants();
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

	@UiHandler("editLabelsButton")
	protected void editLabelsButtonClicked(ClickEvent event) {
		LabelsDialog.DIALOG.showDialog();
	}

	@UiHandler("editShortcutsButton")
	protected void editShortcutsButtonClicked(ClickEvent event) {
		ShortcutsDialog.DIALOG.showDialog();
	}

	@UiHandler("markAllReadButton")
	protected void markAllReadButtonClicked(ClickEvent event) {
		ArtiqueWorld.WORLD.getList().markAllRead();
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
		ArtiqueWorld.WORLD.getList().showHead();
	}

}
