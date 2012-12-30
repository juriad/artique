package cz.artique.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.service.ClientLabelService;
import cz.artique.client.service.ClientLabelServiceAsync;
import cz.artique.client.service.ClientSourceService;
import cz.artique.client.service.ClientSourceServiceAsync;
import cz.artique.shared.model.label.Filter;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.source.UserSource;

public enum ArtiqueWorld {
	WORLD;

	private Map<Key, UserSource> sources = new HashMap<Key, UserSource>();
	private Map<Key, Label> labels = new HashMap<Key, Label>();
	private Map<Key, Filter> filters = new HashMap<Key, Filter>();

	private ClientSourceServiceAsync css = GWT
		.create(ClientSourceService.class);
	private ClientLabelServiceAsync cls = GWT.create(ClientLabelService.class);

	private ArtiqueWorld() {
		refreshSources(null);
	}

	public Map<Key, UserSource> getSources() {
		return sources;
	}

	public void refreshSources(final Ping p) {
		css.getUserSources(new AsyncCallback<List<UserSource>>() {

			public void onSuccess(List<UserSource> result) {
				Map<Key, UserSource> newSources =
					new HashMap<Key, UserSource>();
				for (UserSource us : result) {
					newSources.put(us.getKey(), us);
				}
				sources = newSources;

				if (p != null) {
					p.ping();
				}
			}

			public void onFailure(Throwable caught) {
				fireFailure("get user sources", caught);
			}
		});
	}

	public UserSource getUserSource(Key key) {
		return sources.get(key);
	}

	protected void fireFailure(String string, Throwable caught) {
		// TODO
	}

	public Map<Key, Label> getLabels() {
		return labels;
	}

	public void refreshLabels() {
		this.labels = labels;
	}

	public Map<Key, Filter> getFilters() {
		return filters;
	}

	public void refreshFilters() {
		this.filters = filters;
	}
}
