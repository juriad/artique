package cz.artique.client.shortcuts;

import cz.artique.client.common.EnumListPicker;
import cz.artique.client.i18n.I18n;
import cz.artique.shared.model.shortcut.ShortcutAction;

public class ActionPicker extends EnumListPicker<ShortcutAction> {

	public ActionPicker() {
		super(ShortcutAction.REFRESH, "shortcutAction", I18n
			.getShortcutsConstants());
	}

}
