package cz.artique.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import cz.artique.client.hierarchy.tree.ListFiltersTree;
import cz.artique.client.hierarchy.tree.SourcesTree;
import cz.artique.client.history.HistoryEvent;
import cz.artique.client.history.HistoryHandler;
import cz.artique.client.history.HistoryItem;
import cz.artique.client.i18n.I18n;
import cz.artique.client.leftPanel.LeftPanel;
import cz.artique.client.listFilters.top.TopPanel;
import cz.artique.client.listing.ArtiqueList;
import cz.artique.client.listing.ArtiqueListProvider;
import cz.artique.client.manager.ManagerReady;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.Message;
import cz.artique.client.messages.MessageType;
import cz.artique.client.messages.Messenger;
import cz.artique.shared.model.config.client.ClientConfigKey;
import cz.artique.shared.model.label.ListFilter;

//import cz.artique.client.hierarchy.tree.HistoryTree;

/**
 * Defines the application layout when the user is logged-in.
 * It consists of:
 * <ul>
 * <li> {@link LeftPanel} (options, sources, filters, messages),
 * <li> {@link TopPanel} (filter and items single-click operations),
 * <li> {@link Messenger} (shows latest messages) and
 * <li> {@link ArtiqueList} (shows all items matched by current
 * {@link ListFilter}).
 * </ul>
 * 
 * @author Adam Juraszek
 * 
 */
public class Artique extends Composite {

	private static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);

	interface MainUiBinder extends UiBinder<Widget, Artique> {}

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("Artique.css")
		CssResource style();
	}

	private static final MyResources res = GWT.create(MyResources.class);

	@UiField(provided = true)
	ArtiqueList list;

	@UiField
	SourcesTree sources;

//	@UiField
//	HistoryTree history;

	@UiField
	ListFiltersTree filters;

	@UiField
	Messenger messenger;

	@UiField
	LeftPanel leftPanel;

	@UiField(provided = true)
	TopPanel topPanel;

	static {
		res.style().ensureInjected();
	}

	/**
	 * Constructs UI and inits history and sets client token for client
	 * extension.
	 */
	public Artique() {
		list = new ArtiqueList();
		topPanel = new TopPanel(list);
		initWidget(uiBinder.createAndBindUi(this));

		initHistory();
		initTokenForClient();
	}

	/**
	 * Adds new element meta named "clientToken" to head of the page.
	 */
	private void initTokenForClient() {
		Element head = Document.get().getElementsByTagName("head").getItem(0);
		MetaElement meta = Document.get().createMetaElement();
		meta.setName("clientToken");
		meta.setContent(ArtiqueWorld.WORLD.getUserInfo().getClientToken());
		head.appendChild(meta);
	}

	/**
	 * Registers HistoryHandler which sets {@link ArtiqueListProvider} and
	 * waits 10 seconds for all managers to initialize.
	 * When they are ready, it fires current history state which sets history to
	 * initial state.
	 */
	private void initHistory() {
		// change list provider when history changes
		Managers.HISTORY_MANAGER.addHistoryHandler(new HistoryHandler() {
			public void onHistoryChanged(HistoryEvent e) {
				HistoryItem hi = Managers.HISTORY_MANAGER.getLastHistoryItem();
				ListFilter listFilter;
				if (hi == null) {
					listFilter = new ListFilter();
				} else {
					listFilter = hi.getListFilter();
				}
				new ArtiqueListProvider(listFilter, list);
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
		timer.schedule(10000);

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
			Managers.SOURCES_MANAGER, Managers.ITEMS_MANAGER,
			Managers.LIST_FILTERS_MANAGER, Managers.SHORTCUTS_MANAGER);
	}

	/**
	 * Change slightly class of left panel layer (to achieve hover effect) and
	 * show appropriate widget in left panel.
	 */
	protected void showPanel() {
		Element parent = leftPanel.getElement().getParentElement();
		parent.addClassName("leftPanelLayer");

		String panel =
			Managers.CONFIG_MANAGER
				.getConfig(ClientConfigKey.SHOW_PANEL)
				.get()
				.getS();
		// H for history
		int index = "OSFM".indexOf(panel);
		if (index >= 0) {
			leftPanel.showWidget(false, index);
		}
	}
}
