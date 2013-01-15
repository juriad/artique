package cz.artique.client.artiqueLabels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.Ping;
import cz.artique.client.labels.LabelsManager;
import cz.artique.client.service.ClientLabelService;
import cz.artique.client.service.ClientLabelServiceAsync;
import cz.artique.shared.model.label.Label;

public enum ArtiqueLabelsManager implements LabelsManager<Label, Key> {
	MANAGER;

	private Map<Key, Label> labelsKeys = new HashMap<Key, Label>();
	private Map<String, Label> labelsNames = new HashMap<String, Label>();

	private ClientLabelServiceAsync cls = GWT.create(ClientLabelService.class);
	private static Comparator<Label> comparator = null;

	public void refreshAll(final Ping ping) {
		cls.getAllLabels(new AsyncCallback<List<Label>>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.pong(false);
				}
			}

			public void onSuccess(List<Label> result) {
				Map<Key, Label> newLabelsKeys = new HashMap<Key, Label>();
				Map<String, Label> newLabelsNames =
					new HashMap<String, Label>();
				for (Label l : result) {
					newLabelsKeys.put(l.getKey(), l);
					newLabelsNames.put(l.getName(), l);
				}
				labelsKeys = newLabelsKeys;
				labelsNames = newLabelsNames;

				if (ping != null) {
					ping.pong(true);
				}
			}
		});
	}

	public List<Label> getLabels() {
		return new ArrayList<Label>(labelsKeys.values());
	}

	public Label getLabelByName(String name) {
		return labelsNames.get(name);
	}

	public void createNewLabel(String name, final Ping ping) {
		Label label = new Label(ArtiqueWorld.WORLD.getUser(), name);
		cls.addLabel(label, new AsyncCallback<Label>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.pong(false);
				}
			}

			public void onSuccess(Label result) {
				if (!labelsKeys.containsKey(result.getKey())) {
					labelsKeys.put(result.getKey(), result);
					labelsNames.put(result.getName(), result);
				}

				if (ping != null) {
					ping.pong(true);
				}
			}
		});
	}

	public Label getLabelByKey(Key key) {
		return labelsKeys.get(key);
	}

	public List<Label> getSortedList(List<Key> keys) {
		List<Label> labels = new ArrayList<Label>(keys.size());
		for (Key key : keys) {
			labels.add(getLabelByKey(key));
		}

		Collections.sort(labels, getComparator());
		return labels;
	}

	public static Comparator<Label> getComparator() {
		if (comparator == null) {
			comparator = new Comparator<Label>() {
				public int compare(Label o1, Label o2) {
					int res =
						((Integer) o1.getPriority())
							.compareTo(o2.getPriority());
					if (res == 0) {
						res = o1.getName().compareToIgnoreCase(o2.getName());
					}
					return res;
				}
			};
		}
		return comparator;
	}

}
