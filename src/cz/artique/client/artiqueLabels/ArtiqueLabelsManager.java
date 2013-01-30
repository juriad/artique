package cz.artique.client.artiqueLabels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.ArtiqueWorld;
import cz.artique.client.labels.LabelsManager;
import cz.artique.client.manager.AbstractManager;
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
		super(GWT.<ClientLabelServiceAsync> create(ClientLabelService.class));
		refresh(null);
	}

	private Map<Key, Label> labelsKeys = new HashMap<Key, Label>();
	private Map<String, Label> labelsNames = new HashMap<String, Label>();
	private List<Label> allLabels = new ArrayList<Label>();
	private List<Label> userDefinedLabels = new ArrayList<Label>();

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
					if (LabelType.USER_DEFINED.equals(l.getLabelType())) {
						newLabelsNames.put(l.getName(), l);
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
		Label l = labelsNames.get(name);
		return l;
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
						labelsNames.put(result.getName(), result);
					}
					labelsKeys.put(result.getKey(), result);
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

	public Label getLabelByNameAndType(String name, LabelType type) {
		if (LabelType.USER_DEFINED.equals(type)) {
			return getLabelByName(name);
		}
		for (Label l : allLabels) {
			if (l.getName().equals(name) && l.getLabelType().equals(type)) {
				return l;
			}
		}
		return null;
	}

	public List<Label> getSortedList(List<Key> keys) {
		List<Label> labels = new ArrayList<Label>(keys.size());
		for (Key key : keys) {
			labels.add(getLabelByKey(key));
		}

		Collections.sort(labels);
		return labels;
	}

}
