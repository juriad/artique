package cz.artique.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.artiqueHierarchy.ArtiqueSourcesTree;
import cz.artique.client.artiqueHistory.ArtiqueHistory;
import cz.artique.client.artiqueHistory.ArtiqueHistoryHandler;
import cz.artique.client.artiqueHistory.HistoryEvent;
import cz.artique.client.artiqueHistory.HistoryHandler;
import cz.artique.client.artiqueListing.ArtiqueList;
import cz.artique.client.artiqueListing.ArtiqueListProvider;
import cz.artique.client.artiqueListing.UserItemRow;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.label.ListFilter;

public class Artique extends Composite {

	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);

	interface MainUiBinder extends UiBinder<Widget, Artique> {}

	@UiField(provided = true)
	ArtiqueList list;

	@UiField
	Label userName;

	@UiField
	Anchor logout;

	@UiField
	ArtiqueSourcesTree sources;

	private static Resources resources;

	static {
		resources = GWT.create(Resources.class);
		resources.css().ensureInjected();
	}

	public Artique() {
		list = new ArtiqueList(UserItemRow.factory);
		initWidget(uiBinder.createAndBindUi(this));
		ArtiqueWorld.WORLD.setList(list);
		initHistory();

		userName.setText(ArtiqueWorld.WORLD.getUser().getNickname());
		logout.setHref(ArtiqueWorld.WORLD.getUserInfo().getLogoutUrl());
	}

	private void initHistory() {
		// change list provider when history changes
		ArtiqueHistory.HISTORY.addHistoryHandler(new HistoryHandler() {
			public void onHistoryChanged(HistoryEvent e) {
				ListFilter listFilter =
					ArtiqueHistory.HISTORY.getLastListFilter();
				if (listFilter != null) {
					new ArtiqueListProvider(listFilter, list);
				} else {
					// TODO log error
				}
			}
		});

		// observe GWT history
		History.addValueChangeHandler(new ArtiqueHistoryHandler());

		// fire initial history when managers are ready
		ArtiqueWorld.WORLD.waitForManager(new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				History.fireCurrentHistoryState();
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		}, Managers.CONFIG_MANAGER, Managers.LABELS_MANAGER);
	}
}
