package cz.artique.client.shortcuts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;

import cz.artique.client.common.ScrollableCellList;
import cz.artique.client.i18n.I18n;
import cz.artique.client.manager.Managers;
import cz.artique.shared.model.shortcut.Shortcut;
import cz.artique.shared.model.shortcut.ShortcutAction;
import cz.artique.shared.model.shortcut.ShortcutType;

/**
 * Editor shown inside {@link ShortcutsDialog}; defines layout and control.
 * It shows list of all defined shortcuts.
 * 
 * @author Adam Juraszek
 * 
 */
public class ShortcutsEditor extends Composite {
	private static ShortcutsEditorUiBinder uiBinder = GWT
		.create(ShortcutsEditorUiBinder.class);

	interface ShortcutsEditorUiBinder extends UiBinder<Widget, ShortcutsEditor> {}

	/**
	 * Cell used to render a shortcut.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	class ShortcutCell extends AbstractCell<Shortcut> {

		public ShortcutCell() {}

		@Override
		public void render(Context context, Shortcut value, SafeHtmlBuilder sb) {
			// Value can be null, so do a null check..
			if (value == null) {
				return;
			}

			sb.appendHtmlConstant("<span class='shortcutCell'>");
			sb.appendHtmlConstant("<span class='shortcutType'>");
			sb.appendEscaped(shortcutTypeAsString(value.getType()));
			sb.appendHtmlConstant("</span>");
			sb.appendEscaped(": ");
			sb.appendHtmlConstant("<span class='shortcutKeyStroke'>");
			sb.appendEscaped(value.getKeyStroke());
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("</span>");
		}
	}

	@UiField(provided = true)
	ScrollableCellList<Shortcut> cellList;

	@UiField
	InlineLabel keyStroke;

	@UiField
	InlineLabel type;

	@UiField
	InlineLabel referenced;

	@UiField
	InlineLabel referencedLabel;

	@UiField
	Button deleteButton;

	@UiField
	Button actionShortcutButton;

	/**
	 * @param type
	 *            type of shortcut
	 * @return internationalized string representing the shortcut type
	 */
	private String shortcutTypeAsString(ShortcutType type) {
		ShortcutsConstants constants = I18n.getShortcutsConstants();
		String method = "shortcutType_" + type.name();
		return constants.getString(method);
	}

	public ShortcutsEditor() {
		cellList = new ScrollableCellList<Shortcut>(new ShortcutCell());
		initWidget(uiBinder.createAndBindUi(this));
		cellList.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				setFields();
			}
		});
	}

	/**
	 * When the delete button has been clicked.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("deleteButton")
	protected void deleteButtonClicked(ClickEvent event) {
		Shortcut selected = cellList.getSelected();
		if (selected == null) {
			return;
		}
		Managers.SHORTCUTS_MANAGER.deleteShortcut(selected,
			new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					setValue();
				}

				public void onFailure(Throwable caught) {
					setValue();
				}
			});
	}

	/**
	 * When the add action shortcut button has been clicked.
	 * 
	 * @param event
	 *            event
	 */
	@UiHandler("actionShortcutButton")
	protected void actionShortcutButtonClicked(ClickEvent event) {
		ActionShortcutDialog.DIALOG.showDialog();
	}

	/**
	 * Clears the editor and loads list of existing shortcuts.
	 */
	public void setValue() {
		cellList.clearSelection();
		setFields();
		Map<String, Shortcut> shortcuts =
			Managers.SHORTCUTS_MANAGER.getAllShortcuts();
		ArrayList<Shortcut> list = new ArrayList<Shortcut>(shortcuts.values());
		Collections.sort(list, new Comparator<Shortcut>() {
			public int compare(Shortcut o1, Shortcut o2) {
				int t = o1.getType().compareTo(o2.getType());
				if (t != 0) {
					return t;
				}
				int k = o1.getKeyStroke().compareTo(o2.getKeyStroke());
				return k;
			}
		});
		cellList.setRowData(list);
	}

	/**
	 * Sets content of fields describing currently selected shortcut.
	 */
	private void setFields() {
		Shortcut selected = cellList.getSelected();
		if (selected == null) {
			keyStroke.setText("");
			type.setText("");
			referenced.setText("");
			referencedLabel.setText(I18n
				.getShortcutsConstants()
				.referencedObject());
			deleteButton.setEnabled(false);
		} else {
			keyStroke.setText(selected.getKeyStroke());
			type.setText(shortcutTypeAsString(selected.getType()));
			referenced.setText(referencedAsString(selected));
			referencedLabel.setText(ShortcutType.ACTION.equals(selected
				.getType())
				? I18n.getShortcutsConstants().referencedAction()
				: I18n.getShortcutsConstants().referencedObject());
			deleteButton.setEnabled(true);
		}
	}

	/**
	 * @param shortcut
	 *            the shortcut
	 * @return internationalized string representing the shortcut referenced
	 *         object or action.
	 */
	private String referencedAsString(Shortcut selected) {
		switch (selected.getType()) {
		case LABEL:
			return selected.getReferencedLabel().getDisplayName();
		case LIST_FILTER:
			return selected.getReferencedListFilter().getName();
		case ACTION:
			return getActionString(selected.getAction());
		default:
			return null;
		}
	}

	/**
	 * @param action
	 *            action of the shortcut
	 * @return internationalized string representing the shortcut's action
	 */
	private String getActionString(ShortcutAction action) {
		String method = "shortcutAction_" + action.name();
		String actionName = I18n.getShortcutsConstants().getString(method);
		return actionName;
	}
}
