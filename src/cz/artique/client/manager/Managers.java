package cz.artique.client.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.config.ConfigManager;
import cz.artique.client.history.HistoryManager;
import cz.artique.client.items.ItemsManager;
import cz.artique.client.labels.LabelsManager;
import cz.artique.client.listFilters.ListFiltersManager;
import cz.artique.client.messages.MessagesManager;
import cz.artique.client.sources.SourcesManager;
import cz.artique.shared.model.config.ClientConfigKey;

public class Managers {
	private Managers() {}

	// must be before all managers
	private static List<Manager> ready = new ArrayList<Manager>();
	private static List<WaitRequest> waiting = new LinkedList<WaitRequest>();

	public static final ConfigManager CONFIG_MANAGER =
		ConfigManager.MANAGER;
	public static final ItemsManager ITEMS_MANAGER =
		ItemsManager.MANAGER;
	public static final LabelsManager LABELS_MANAGER =
		LabelsManager.MANAGER;
	public static final SourcesManager SOURCES_MANAGER =
		SourcesManager.MANAGER;
	public static final ListFiltersManager LIST_FILTERS_MANAGER =
		ListFiltersManager.MANAGER;
	public static final HistoryManager HISTORY_MANAGER = HistoryManager.HISTORY;
	public static final MessagesManager MESSAGES_MANAGER =
		MessagesManager.MESSENGER;

	public static final Manager[] MANAGERS = new Manager[] {
		CONFIG_MANAGER,
		ITEMS_MANAGER,
		LABELS_MANAGER,
		SOURCES_MANAGER,
		LIST_FILTERS_MANAGER,
		HISTORY_MANAGER,
		MESSAGES_MANAGER };

	static {
		for (final Manager m : MANAGERS) {
			m.ready(new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					ready.add(m);
					Iterator<WaitRequest> iter = waiting.iterator();
					while (iter.hasNext()) {
						WaitRequest r = iter.next();
						r.managers.remove(m);
						if (r.managers.isEmpty()) {
							iter.remove();
							r.ping.onSuccess(null);
						}
					}
				}

				public void onFailure(Throwable caught) {
					// TODO failed manager
				}
			});
		}
		waitForManagers(new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				// ignore
			}

			public void onSuccess(Void result) {
				for (Manager m : MANAGERS) {
					if (m instanceof AbstractManager) {
						((AbstractManager<?>) m).setTimeout(CONFIG_MANAGER
							.getConfig(ClientConfigKey.SERVICE_TIMEOUT)
							.get()
							.getI());
					}
				}
			}
		}, CONFIG_MANAGER);
	}

	private static class WaitRequest {
		List<Manager> managers;
		AsyncCallback<Void> ping;

		public WaitRequest(List<Manager> managers, AsyncCallback<Void> ping) {
			this.managers = managers;
			this.ping = ping;
		}
	}

	public static void waitForManagers(AsyncCallback<Void> ping,
			Manager... managers) {
		List<Manager> managersList = new ArrayList<Manager>();
		if (managers != null) {
			for (Manager m : managers) {
				managersList.add(m);
			}
		}

		managersList.removeAll(ready);
		if (managersList.isEmpty()) {
			ping.onSuccess(null);
		} else {
			waiting.add(new WaitRequest(managersList, ping));
		}
	}
}
