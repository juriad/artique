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
import cz.artique.client.labels.LabelsDialog;
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

	public OptionPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		userName.setText(ArtiqueWorld.WORLD.getUser().getNickname());
		logout.setHref(ArtiqueWorld.WORLD.getUserInfo().getLogoutUrl());
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

}
