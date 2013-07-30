package cz.artique.client.manager;

import java.util.ArrayList;
import java.util.List;

import cz.artique.client.config.ConfigManager;
import cz.artique.client.history.HistoryManager;
import cz.artique.client.items.ItemsManager;
import cz.artique.client.labels.LabelsManager;
import cz.artique.client.listFilters.ListFiltersManager;
import cz.artique.client.messages.MessagesManager;
import cz.artique.client.shortcuts.ShortcutsManager;
import cz.artique.client.sources.SourcesManager;
import cz.artique.shared.model.config.client.ClientConfigKey;

/**
 * Lists all existing {@link Manager}s, making sure they are used as singletons.
 * It also provides functions allowing user to wait for several {@link Manager}s
 * become ready.
 * 
 * @author Adam Juraszek
 * 
 */
public class Managers {

	private static class WaitRequest {
		List<Manager> managers;
		ManagerReady ping;

		public WaitRequest(List<Manager> managers, ManagerReady ping) {
			this.managers = managers;
			this.ping = ping;
		}
	}

	public static interface ManagerInitCallback {
		void initManager();
	}

	private static List<ManagerInitCallback> callbacks =
		new ArrayList<ManagerInitCallback>();

	private static boolean complete = false;

	public static void addManagerInitCallback(ManagerInitCallback callback) {
		if (!complete) {
			callbacks.add(callback);
		} else {
			callback.initManager();
		}
	}

	public static final ConfigManager CONFIG_MANAGER = ConfigManager.MANAGER;
	public static final ItemsManager ITEMS_MANAGER = ItemsManager.MANAGER;
	public static final LabelsManager LABELS_MANAGER = LabelsManager.MANAGER;
	public static final SourcesManager SOURCES_MANAGER = SourcesManager.MANAGER;
	public static final ListFiltersManager LIST_FILTERS_MANAGER =
		ListFiltersManager.MANAGER;
	public static final HistoryManager HISTORY_MANAGER = HistoryManager.HISTORY;
	public static final MessagesManager MESSAGES_MANAGER =
		MessagesManager.MESSENGER;
	public static final ShortcutsManager SHORTCUTS_MANAGER =
		ShortcutsManager.MANAGER;

	private static final Manager[] MANAGERS = new Manager[] {
		CONFIG_MANAGER,
		ITEMS_MANAGER,
		LABELS_MANAGER,
		SOURCES_MANAGER,
		LIST_FILTERS_MANAGER,
		HISTORY_MANAGER,
		MESSAGES_MANAGER,
		SHORTCUTS_MANAGER };

	private static List<Manager> ready = new ArrayList<Manager>();
	private static List<WaitRequest> waiting = new ArrayList<WaitRequest>();

	static {
		registerOnReady();
		setTimeout();
		processCallbacks();
	}

	private static void processCallbacks() {
		if (complete) {
			return;
		}
		for (ManagerInitCallback callback : callbacks) {
			callback.initManager();
		}
		complete = true;
	}

	private static void registerOnReady() {
		for (final Manager m : MANAGERS) {
			m.ready(new ManagerReady() {
				public void onReady() {
					ready.add(m);

					List<WaitRequest> newWaiting = new ArrayList<WaitRequest>();
					List<WaitRequest> toBeFired = new ArrayList<WaitRequest>();
					for (WaitRequest r : waiting) {
						r.managers.remove(m);
						if (r.managers.isEmpty()) {
							toBeFired.add(r);
						} else {
							newWaiting.add(r);
						}
					}
					waiting = newWaiting;

					for (WaitRequest r : toBeFired) {
						r.ping.onReady();
					}
				}
			});
		}
	}

	private static void setTimeout() {
		waitForManagers(new ManagerReady() {
			public void onReady() {
				for (Manager m : MANAGERS) {
					m.setTimeout(CONFIG_MANAGER
						.getConfig(ClientConfigKey.SERVICE_TIMEOUT)
						.get()
						.getI());
				}
			}
		}, CONFIG_MANAGER);
	}

	public static void waitForManagers(ManagerReady ping, Manager... managers) {
		List<Manager> managersList = new ArrayList<Manager>();
		if (managers != null) {
			for (Manager m : managers) {
				managersList.add(m);
			}
		}

		managersList.removeAll(ready);
		if (managersList.isEmpty()) {
			ping.onReady();
		} else {
			waiting.add(new WaitRequest(managersList, ping));
		}
	}
}
