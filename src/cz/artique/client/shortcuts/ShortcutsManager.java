package cz.artique.client.shortcuts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import cz.artique.client.history.CachingHistoryUtils;
import cz.artique.client.history.HistoryItem;
import cz.artique.client.items.ManualItemDialog;
import cz.artique.client.manager.AbstractManager;
import cz.artique.client.manager.ManagerReady;
import cz.artique.client.manager.Managers;
import cz.artique.client.messages.MessageType;
import cz.artique.client.messages.ValidationMessage;
import cz.artique.client.service.ClientShortcutService;
import cz.artique.client.service.ClientShortcutService.CreateShortcut;
import cz.artique.client.service.ClientShortcutService.DeleteShortcut;
import cz.artique.client.service.ClientShortcutService.GetAllShortcuts;
import cz.artique.client.service.ClientShortcutServiceAsync;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.shortcut.Shortcut;

public class ShortcutsManager
		extends AbstractManager<ClientShortcutServiceAsync>
		implements HasShortcutHandlers {
	public static final ShortcutsManager MANAGER = new ShortcutsManager();

	private Map<String, Shortcut> keystrokes = new HashMap<String, Shortcut>();
	private Map<Key, Shortcut> referenced = new HashMap<Key, Shortcut>();

	public ShortcutsManager() {
		super(GWT
			.<ClientShortcutServiceAsync> create(ClientShortcutService.class));
		Managers.waitForManagers(new ManagerReady() {
			public void onReady() {
				refresh(null);
			}
		}, Managers.LABELS_MANAGER, Managers.LIST_FILTERS_MANAGER);

		addShortcutHandler(new ShortcutHandler() {
			public void onShortcut(ShortcutEvent e) {
				String token = null;
				ListFilter listFilter = null;

				switch (e.getShortcut().getType()) {
				case ACTION:
					switch (e.getShortcut().getAction()) {
					case CLEAR_FILTER:
						listFilter =
							Managers.HISTORY_MANAGER.getBaseListFilter();
						break;
					case TOTAL_CLEAR_FILTER:
						listFilter = new ListFilter();
						break;
					case REFRESH:
						HistoryItem lastHistoryItem =
							Managers.HISTORY_MANAGER.getLastHistoryItem();
						if (lastHistoryItem != null) {
							listFilter = lastHistoryItem.getListFilter();
							token = lastHistoryItem.getToken();
						}
						break;
					case ADD_MANUAL_ITEM:
						ManualItemDialog.DIALOG.showDialog();
					default:
						break;
					}
					break;
				case LIST_FILTER:
					listFilter = e.getShortcut().getReferencedListFilter();
					break;
				default:
					break;
				}
				if (listFilter != null) {
					if (token == null) {
						token =
							CachingHistoryUtils.UTILS
								.serializeListFilter(listFilter);
					}
					Managers.HISTORY_MANAGER.setListFilter(listFilter, token);
				}
			}
		});
	}

	private Shortcut getShortcut(KeyEvent<?> event) {
		String stroke;
		if (event instanceof KeyPressEvent) {
			stroke = "+" + ((KeyPressEvent) event).getCharCode();
		} else if (event instanceof KeyCodeEvent) {
			stroke = "+$" + ((KeyCodeEvent<?>) event).getNativeKeyCode();
		} else {
			stroke = ""; // will never happen
		}

		if (event.isShiftKeyDown()) {
			stroke = "S" + stroke;
		}
		if (event.isMetaKeyDown()) {
			stroke = "M" + stroke;
		}
		if (event.isControlKeyDown()) {
			stroke = "C" + stroke;
		}
		if (event.isAltKeyDown()) {
			stroke = "A" + stroke;
		}
		Shortcut shortcut = keystrokes.get(stroke);
		return shortcut;
	}

	private void processShortcut(Shortcut shortcut) {
		for (int i = 0; i < handlers.size(); i++) {
			handlers.get(i).onShortcut(new ShortcutEvent(shortcut));
		}
	}

	public void refresh(final AsyncCallback<Void> ping) {
		assumeOnline();
		service.getAllShortcuts(new AsyncCallback<List<Shortcut>>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<GetAllShortcuts>(GetAllShortcuts.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(List<Shortcut> result) {
				for (Shortcut cut : result) {
					if (fillShortcut(cut)) {
						String normalized =
							normalizeKeyStroke(cut.getKeyStroke());
						keystrokes.put(normalized, cut);
						if (cut.getReferenced() != null) {
							referenced.put(cut.getReferenced(), cut);
						}
					}
				}

				new ValidationMessage<GetAllShortcuts>(GetAllShortcuts.GENERAL)
					.onSuccess(MessageType.DEBUG);
				if (ping != null) {
					ping.onSuccess(null);
				}
				setReady();
			}
		});
	}

	protected boolean fillShortcut(Shortcut cut) {
		switch (cut.getType()) {
		case LABEL:
			Label labelByKey =
				Managers.LABELS_MANAGER.getLabelByKey(cut.getReferenced());
			if (labelByKey != null) {
				cut.setReferencedLabel(labelByKey);
			}
			return labelByKey != null;
		case LIST_FILTER:
			ListFilter listFilterByKey =
				Managers.LIST_FILTERS_MANAGER.getListFilterByKey(cut
					.getReferenced());
			if (listFilterByKey != null) {
				cut.setReferencedListFilter(listFilterByKey);
			}
			return listFilterByKey != null;
		default:
			return true;
		}
	}

	@Override
	protected synchronized void setReady() {
		if (isReady()) {
			return;
		}
		RootPanel.get().addDomHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				Shortcut shortcut = getShortcut(event);
				if (shortcut != null) {
					processShortcut(shortcut);
					event.preventDefault();
					event.stopPropagation();
				}
			}
		}, KeyDownEvent.getType());
		RootPanel.get().addDomHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				Shortcut shortcut = getShortcut(event);
				if (shortcut != null) {
					processShortcut(shortcut);
					event.preventDefault();
					event.stopPropagation();
				}
			}
		}, KeyPressEvent.getType());
		super.setReady();
	}

	public Map<String, Shortcut> getAllShortcuts() {
		return keystrokes;
	}

	public Shortcut getByReferenced(Key referencedKey) {
		return referenced.get(referencedKey);
	}

	public void deleteShortcut(final Shortcut value,
			final AsyncCallback<Void> ping) {
		assumeOnline();
		service.deleteShortcut(value.getKey(), new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<DeleteShortcut>(DeleteShortcut.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(Void result) {
				String normalized = normalizeKeyStroke(value.getKeyStroke());
				keystrokes.remove(normalized);
				if (value.getReferenced() != null) {
					referenced.remove(value.getReferenced());
				}

				new ValidationMessage<DeleteShortcut>(DeleteShortcut.GENERAL)
					.onSuccess();
				if (ping != null) {
					ping.onSuccess(null);
				}
			}
		});
	}

	public void createShortcut(final Shortcut value,
			final AsyncCallback<Shortcut> ping) {
		assumeOnline();
		service.createShortcut(value, new AsyncCallback<Shortcut>() {
			public void onFailure(Throwable caught) {
				serviceFailed(caught);
				new ValidationMessage<CreateShortcut>(CreateShortcut.GENERAL)
					.onFailure(caught);
				if (ping != null) {
					ping.onFailure(caught);
				}
			}

			public void onSuccess(Shortcut result) {
				if (fillShortcut(result)) {
					String normalized =
						normalizeKeyStroke(result.getKeyStroke());
					keystrokes.put(normalized, result);
					if (result.getReferenced() != null) {
						referenced.put(result.getReferenced(), result);
					}
				}

				new ValidationMessage<CreateShortcut>(CreateShortcut.GENERAL)
					.onSuccess();
				if (ping != null) {
					ping.onSuccess(null);
				}
			}
		});
	}

	protected String normalizeKeyStroke(String keyStroke) {
		if (keyStroke == null) {
			return null;
		}
		String[] split = keyStroke.split("\\+", 2);
		if (split.length < 2) {
			// does not contain modifiers (separator is plus sign)
			KeyCodesEnum byName =
				KeyCodesEnum.getByName(keyStroke.toUpperCase());
			if (byName == null) {
				return "+" + keyStroke;
			} else {
				return "+$" + byName.getCode();
			}
		} else if (split[1].length() == 0) {
			// contains plus sign but it is character not separator
			return "+" + keyStroke;
		} else {
			String mod = split[0];
			KeyCodesEnum byName =
				KeyCodesEnum.getByName(keyStroke.toUpperCase());
			String stroke;
			if (byName == null) {
				stroke = "+" + split[1];
			} else {
				stroke = "+$" + byName.getCode();
			}

			if (mod.contains("S") || mod.contains("s")) {
				stroke = "S" + stroke;
			}
			if (mod.contains("M") || mod.contains("m")) {
				stroke = "M" + stroke;
			}
			if (mod.contains("C") || mod.contains("c")) {
				stroke = "C" + stroke;
			}
			if (mod.contains("A") || mod.contains("a")) {
				stroke = "A" + stroke;
			}
			return stroke;
		}
	}

	private List<ShortcutHandler> handlers = new ArrayList<ShortcutHandler>();

	public HandlerRegistration addShortcutHandler(final ShortcutHandler handler) {
		handlers.add(handler);
		return new HandlerRegistration() {
			public void removeHandler() {
				handlers.remove(handler);
			}
		};
	}
}
