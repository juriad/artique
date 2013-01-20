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

import cz.artique.client.AbstractManager;
import cz.artique.client.ArtiqueWorld;
import cz.artique.client.labels.LabelsManager;
import cz.artique.client.service.ClientLabelService;
import cz.artique.client.service.ClientLabelServiceAsync;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;

public class ArtiqueLabelsManager
		extends AbstractManager<ClientLabelServiceAsync>
		implements LabelsManager<Label, Key> {
	public static final ArtiqueLabelsManager MANAGER =
		new ArtiqueLabelsManager();

	private ArtiqueLabelsManager() {
		super();
		setService(GWT
			.<ClientLabelServiceAsync> create(ClientLabelService.class));
		refresh(null);
	}

	private Map<Key, Label> labelsKeys = new HashMap<Key, Label>();
	private Map<String, Label> labelsNames = new HashMap<String, Label>();
	private List<Label> allLabels = new ArrayList<Label>();
	private List<Label> userDefinedLabels = new ArrayList<Label>();

	private static Comparator<Label> comparator = null;

	public void refresh(final AsyncCallback<Void> ping) {
		service.getAllLabels(new AsyncCallback<List<Label>>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(List<Label> result) {
				Map<Key, Label> newLabelsKeys = new HashMap<Key, Label>();
				Map<String, Label> newLabelsNames =
					new HashMap<String, Label>();
				List<Label> newUserDefinedLabels = new ArrayList<Label>();
				for (Label l : result) {
					newLabelsKeys.put(l.getKey(), l);
					newLabelsNames.put(l.getName(), l);
					if (LabelType.USER_DEFINED.equals(l.getLabelType())) {
						newUserDefinedLabels.add(l);
					}
				}
				labelsKeys = newLabelsKeys;
				labelsNames = newLabelsNames;
				userDefinedLabels = newUserDefinedLabels;
				allLabels = result;

				if (ping != null) {
					ping.onSuccess(null);
				}
				setReady();
			}
		});
	}

	public List<Label> getLabels() {
		return allLabels;
	}

	public List<Label> getUserDefinedLabels() {
		return userDefinedLabels;
	}

	public Label getLabelByName(String name) {
		return labelsNames.get(name);
	}

	public void createNewLabel(String name, final AsyncCallback<Label> ping) {
		Label label = new Label(ArtiqueWorld.WORLD.getUser(), name);
		service.addLabel(label, new AsyncCallback<Label>() {

			public void onFailure(Throwable caught) {
				if (ping != null) {
					ping.onFailure(null);
				}
			}

			public void onSuccess(Label result) {
				if (!labelsKeys.containsKey(result.getKey())) {
					allLabels.add(result);
					if (LabelType.USER_DEFINED.equals(result.getLabelType())) {
						userDefinedLabels.add(result);
					}
					labelsKeys.put(result.getKey(), result);
					labelsNames.put(result.getName(), result);
				}

				if (ping != null) {
					ping.onSuccess(result);
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
