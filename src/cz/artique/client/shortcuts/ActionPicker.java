package cz.artique.client.shortcuts;

import cz.artique.client.common.EnumListPicker;
import cz.artique.client.i18n.I18n;
import cz.artique.shared.model.shortcut.ShortcutAction;

/**
 * Allows user to choose a {@link ShortcutAction} from a list of all of them.
 * 
 * @author Adam Juraszek
 * 
 */
public class ActionPicker extends EnumListPicker<ShortcutAction> {

	public ActionPicker() {
		super(ShortcutAction.REFRESH, "shortcutAction", I18n
			.getShortcutsConstants());
	}

}
