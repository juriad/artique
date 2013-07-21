package cz.artique.client.labels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.artique.client.history.HistoryItem;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.AbstractManager;
import cz.artique.client.manager.ManagerReady;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.MessageType;
import cz.artique.client.messages.ValidationMessage;
import cz.artique.client.service.ClientLabelService;
import cz.artique.client.service.ClientLabelServiceAsync;
import cz.artique.client.service.ClientLabelService.AddLabel;
import cz.artique.client.service.ClientLabelService.GetAllLabels;
import cz.artique.client.service.ClientLabelService.UpdateLabels;
import cz.artique.client.utils.CatList;
import cz.artique.client.utils.SortedList;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelAppearance;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.ValidationException;

public class LabelsManager extends AbstractManager<ClientLabelServiceAsync> {

	public static final Label AND, OR;

	static {
		LabelAppearance operatorAppearance = new LabelAppearance(null);
		operatorAppearance.setForegroundColor("rgb(255,0,0)");

		LabelsConstants constants = I18n.getLabelsConstants();
		AND = new Label("__not_null__", "AND");
		AND.setLabelType(LabelType.SYSTEM);
		AND.setPriority(Integer.MAX_VALUE);
		AND.setDisplayName(constants.operatorAnd());
		AND.setAppearance(operatorAppearance);

		OR = new Label("__not_null__", "OR");
		OR.setLabelType(LabelType.SYSTEM);
		OR.setPriority(Integer.MAX_VALUE);
		OR.setDisplayName(constants.operatorOr());
		OR.setAppearance(operatorAppearance);
	}

	public static final LabelsManager MANAGER = new LabelsManager();

	private Map<Key, Label> labelsKeys = new HashMap<Key, Label>();
	private Map<String, Label> labelNames = new HashMap<String, Label>();

	private List<Label> userSourceLabels = new ArrayList<Label>();
	private List<Label> systemLabels = new ArrayList<Label>();
	private List<Label> userDefinedLabels = new ArrayList<Label>();

	private LabelsManager() {
		super(GWT.<ClientLabelServiceAsync> create(ClientLabelService.class));
		refresh(null);
		systemLabels.add(AND);
		systemLabels.add(OR);
	}

	public void updateUserSourceLabel(UserSource source) {
		Key labelKey = source.getLabel();
		Label label = labelsKeys.get(labelKey);
		if (label == null) {
			// source has been created, therefore it does not exist
			label = source.getLabelObject();

			userSourceLabels.add(label);
			labelNames.put(nameWithType(label.getLabelType(), label.getName()),
				label);
			labelsKeys.put(label.getKey(), label);
		}
		label.setDisplayName(source.getName());
	}

	private List<AsyncCallback<Void>> pingsWaiting =
		new ArrayList<AsyncCallback<Void>>();

	public void refresh(final AsyncCallback<Void> ping) {
		pingsWaiting.add(ping);
		if (pingsWaiting.size() > 2) {
			return;
		}
		assumeOnline();
		service.getAllLabels(new AsyncCallback<List<Label>>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<GetAllLabels>(GetAllLabels.GENERAL)
					.onFailure(caught);
				for (AsyncCallback<Void> p : pingsWaiting) {
					if (p != null) {
						p.onFailure(caught);
					}
				}
				pingsWaiting.clear();
			}

			public void onSuccess(final List<Label> list) {
				Managers.waitForManagers(new ManagerReady() {
					public void onReady() {
						Map<Key, Label> newLabelsKeys =
							new HashMap<Key, Label>();
						List<Label> newUserDefinedLabels =
							new ArrayList<Label>();
						List<Label> newUserSourceLabels =
							new ArrayList<Label>();
						HashMap<String, Label> newLabelNames =
							new HashMap<String, Label>();

						for (Label l : list) {
							newLabelsKeys.put(l.getKey(), l);
							newLabelNames.put(
								nameWithType(l.getLabelType(), l.getName()), l);

							switch (l.getLabelType()) {
							case USER_DEFINED:
								newUserDefinedLabels.add(l);
								break;
							case USER_SOURCE:
								l.setDisplayName(Managers.SOURCES_MANAGER
									.getByLabel(l)
									.getName());
								newUserSourceLabels.add(l);
								break;
							case SYSTEM:
							default:
								break;
							}
						}
						labelsKeys = newLabelsKeys;
						userDefinedLabels = newUserDefinedLabels;
						userSourceLabels = newUserSourceLabels;
						labelNames = newLabelNames;

						new ValidationMessage<GetAllLabels>(
							GetAllLabels.GENERAL).onSuccess(MessageType.DEBUG);
						for (AsyncCallback<Void> p : pingsWaiting) {
							if (p != null) {
								p.onSuccess(null);
							}
						}
						pingsWaiting.clear();
						setReady();
					}
				}, Managers.SOURCES_MANAGER);
			}
		});
	}

	public void createNewLabel(String name, final AsyncCallback<Label> ping) {
		Label label = new Label(null, name);
		assumeOnline();
		service.addLabel(label, new AsyncCallback<Label>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<AddLabel>(AddLabel.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(null);
				}
			}

			public void onSuccess(Label result) {
				if (!labelsKeys.containsKey(result.getKey())) {
					userDefinedLabels.add(result);
					labelNames.put(
						nameWithType(result.getLabelType(), result.getName()),
						result);
					labelsKeys.put(result.getKey(), result);
				}

				new ValidationMessage<AddLabel>(AddLabel.GENERAL)
					.onSuccess(MessageType.DEBUG);

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

		Collections.sort(labels);
		return labels;
	}

	@SuppressWarnings("unchecked")
	public List<Label> getLabels(LabelType filterType) {
		if (filterType == null) {
			return new CatList<Label>(systemLabels, userDefinedLabels,
				userSourceLabels);
		}
		switch (filterType) {
		case SYSTEM:
			return systemLabels;
		case USER_DEFINED:
			return userDefinedLabels;
		case USER_SOURCE:
			return userSourceLabels;
		default:
			// wont happen
			return null;
		}
	}

	public List<Label> fullTextSearch(String text, List<Label> allLabels) {
		text = text.trim().toLowerCase();

		LabelType filter = null;
		if (text.startsWith("@")) {
			filter = LabelType.USER_SOURCE;
			text = text.substring(1);
		} else if (text.startsWith("&") || text.startsWith("|")) {
			filter = LabelType.SYSTEM;
			text = text.substring(1);
		} else if (text.startsWith("=")) {
			filter = LabelType.USER_DEFINED;
			text = text.substring(1);
		}

		SortedList<Label> sorted = new SortedList<Label>();
		for (Label l : allLabels) {
			if (filter != null && filter.equals(l.getLabelType())
				|| filter == null) {
				if (l.getDisplayName().toLowerCase().startsWith(text)
					|| LabelType.SYSTEM.equals(l.getLabelType())
					&& l.getName().toLowerCase().startsWith(text)) {
					sorted.add(l);
				}
			}
		}
		return sorted;
	}

	private String nameWithType(LabelType type, String name) {
		return type.getType() + "$" + name;
	}

	public Label getLabelByName(LabelType type, String name) {
		Label label = labelNames.get(nameWithType(type, name));
		return label;
	}

	public void updateLabels(final List<Label> value,
			final AsyncCallback<Void> ping) {
		assumeOnline();
		service.updateLabels(value, new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				if (caught instanceof ValidationException) {
					List<Issue<? extends HasIssue>> issues =
						((ValidationException) caught).getIssues();
					if (issues != null && issues.size() > 0) {
						Issue<? extends HasIssue> issue = issues.get(0);
						if (UpdateLabels.DELETE_SOURCE.equals(issue
							.getProperty())
							|| UpdateLabels.DELETE_FILTER.equals(issue
								.getProperty())
							|| UpdateLabels.DELETE_ITEM.equals(issue
								.getProperty())) {
							doUpdateItems();
						}
					}
				}
				serviceFailed(caught);
				new ValidationMessage<UpdateLabels>(UpdateLabels.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			private void doUpdateItems() {
				for (Label l : value) {
					Label labelByKey = getLabelByKey(l.getKey());
					labelByKey.setBackupLevel(l.getBackupLevel());
					labelByKey.setAppearance(l.getAppearance());
				}

				HistoryItem lastHistoryItem =
					Managers.HISTORY_MANAGER.getLastHistoryItem();
				if (lastHistoryItem != null) {
					Managers.HISTORY_MANAGER.setListFilter(
						lastHistoryItem.getListFilter(),
						lastHistoryItem.getToken());
				}
			}

			public void onSuccess(Void result) {
				doUpdateItems();
				doDeleteItems();

				new ValidationMessage<UpdateLabels>(UpdateLabels.GENERAL)
					.onSuccess();
				if (ping != null) {
					ping.onSuccess(result);
				}
			}

			private void doDeleteItems() {
				for (Label l : value) {
					if (l.isToBeDeleted()) {
						Label labelByKey = getLabelByKey(l.getKey());
						userDefinedLabels.remove(labelByKey);
						labelsKeys.remove(l.getKey());
						labelNames.remove(l.getName());
					}
				}
			}
		});
	}
}
