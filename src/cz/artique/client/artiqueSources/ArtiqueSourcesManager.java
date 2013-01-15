package cz.artique.client.artiqueSources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.Ping;
import cz.artique.client.service.ClientSourceService;
import cz.artique.client.service.ClientSourceServiceAsync;
import cz.artique.client.sources.SourcesManager;
import cz.artique.shared.model.source.UserSource;

public enum ArtiqueSourcesManager implements SourcesManager<UserSource, Key> {
	MANAGER;

	private Map<Key, UserSource> sourcesKeys = new HashMap<Key, UserSource>();
	private Map<String, UserSource> sourcesNames =
		new HashMap<String, UserSource>();

	private ClientSourceServiceAsync css = GWT
		.create(ClientSourceService.class);

	public void refresh(final Ping ping) {
		css.getUserSources(new AsyncCallback<List<UserSource>>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.pong(false);
				}
			}

			public void onSuccess(List<UserSource> result) {
				Map<Key, UserSource> newSourcesKeys =
					new HashMap<Key, UserSource>();
				Map<String, UserSource> newSourcesNames =
					new HashMap<String, UserSource>();
				for (UserSource us : result) {
					newSourcesKeys.put(us.getKey(), us);
					newSourcesNames.put(us.getName(), us);
				}
				sourcesKeys = newSourcesKeys;
				sourcesNames = newSourcesNames;

				if (ping != null) {
					ping.pong(true);
				}
			}
		});
	}

	public List<UserSource> getSources() {
		return new ArrayList<UserSource>(sourcesKeys.values());
	}

	public UserSource getSourceByName(String name) {
		return sourcesNames.get(name);
	}

	public UserSource getSourceByKey(Key key) {
		return sourcesKeys.get(key);
	}

}
