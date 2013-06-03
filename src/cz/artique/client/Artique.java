package cz.artique.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.hierarchy.tree.HistoryTree;
import cz.artique.client.hierarchy.tree.ListFiltersTree;
import cz.artique.client.hierarchy.tree.SourcesTree;
import cz.artique.client.history.HistoryEvent;
import cz.artique.client.history.HistoryHandler;
import cz.artique.client.i18n.I18n;
import cz.artique.client.listing.ArtiqueList;
import cz.artique.client.listing.ArtiqueListProvider;
import cz.artique.client.manager.ManagerReady;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
import cz.artique.client.messages.Messenger;
import cz.artique.shared.model.config.ClientConfigKey;
import cz.artique.shared.model.label.ListFilter;

public class Artique extends Composite {

	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);

	interface MainUiBinder extends UiBinder<Widget, Artique> {}

	@UiField(provided = true)
	ArtiqueList list;

	@UiField
	SourcesTree sources;

	@UiField
	HistoryTree history;

	@UiField
	ListFiltersTree filters;

	@UiField
	Messenger messenger;

	@UiField
	StackLayoutPanel stack;

	private static Resources resources;

	static {
		resources = GWT.create(Resources.class);
		resources.css().ensureInjected();
		ArtiqueWorld.WORLD.setResources(resources);
	}

	public Artique() {
		list = new ArtiqueList();
		initWidget(uiBinder.createAndBindUi(this));
		ArtiqueWorld.WORLD.setList(list);
		ArtiqueWorld.WORLD.setSourcesTree(sources);

		initHistory();
	}

	private void initHistory() {
		// change list provider when history changes
		Managers.HISTORY_MANAGER.addHistoryHandler(new HistoryHandler() {
			public void onHistoryChanged(HistoryEvent e) {
				ListFilter listFilter =
					Managers.HISTORY_MANAGER
						.getLastHistoryItem()
						.getListFilter();
				if (listFilter != null) {
					new ArtiqueListProvider(listFilter, list);
				} else {
					// TODO log error
				}
			}
		});

		final Timer timer = new Timer() {
			@Override
			public void run() {
				ArtiqueConstants constants = I18n.getArtiqueConstants();
				Managers.MESSAGES_MANAGER.addMessage(new Message(
					MessageType.ERROR, constants.failedToLoadManagers()), true);
			}
		};
		timer.schedule(5000);

		// fire initial history when managers are ready
		Managers.waitForManagers(new ManagerReady() {

			public void onReady() {
				ArtiqueConstants constants = I18n.getArtiqueConstants();
				Managers.MESSAGES_MANAGER.addMessage(new Message(
					MessageType.INFO, constants.initialized()), true);
				timer.cancel();
				History.fireCurrentHistoryState();
				showPanel();
			}
		}, Managers.CONFIG_MANAGER, Managers.LABELS_MANAGER,
			Managers.SOURCES_MANAGER, Managers.ITEMS_MANAGER);
	}

	protected void showPanel() {
		Element parent = stack.getElement().getParentElement();
		parent.addClassName("leftPanel");

		String panel =
			Managers.CONFIG_MANAGER
				.getConfig(ClientConfigKey.SHOW_PANEL)
				.get()
				.getS();
		int duration = stack.getAnimationDuration();
		stack.setAnimationDuration(0);
		if (panel.equals("O")) {
			stack.showWidget(0);
		} else if (panel.equals("S")) {
			stack.showWidget(1);
		} else if (panel.equals("F")) {
			stack.showWidget(2);
		} else if (panel.equals("M")) {
			stack.showWidget(3);
		} else if (panel.equals("H")) {
			stack.showWidget(4);
		}
		stack.setAnimationDuration(duration);
	}
}
