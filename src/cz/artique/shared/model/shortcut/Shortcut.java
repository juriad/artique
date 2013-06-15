package cz.artique.shared.model.shortcut;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.shortcuts.KeyCodesEnum;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

/**
 * Shortcut represents the mechanism that allows user to set an action which
 * will be performed when he presses defined key combination.
 * 
 * <p>
 * Key combinations AKA key strokes may be composed of any modifiers: Alt,
 * Control, Meta, Shift (first letter only, ignoring case) followed by name of
 * key. The following strokes are valid:
 * <ul>
 * <li>a - key a pressed without any modifiers
 * <li>S+A - key a pressed with modifier shift
 * <li>aS+A - key a pressed with modifiers alt and shift
 * <li>+a - key a pressed without any modifiers
 * <li>+ - key + pressed without any modifiers
 * <li>++ - key + pressed without any modifiers
 * <li>a++ - key + pressed with modifier alt
 * <li>tab - key tab pressed without any modifiers
 * <li>s+tab - key tab pressed with shift modifier
 * </ul>
 * List of names of special keys can be found in {@link KeyCodesEnum}.
 * 
 * <p>
 * A shortcut can be one of several types defined in {@link ShortcutType}:
 * <ul>
 * <li>LABEL - assigns or removes label from an item
 * <li>LIST_FILTER - changes listing to show items of the list filter
 * <li>ACTION - one of parameterless actions defined in {@link ShortcutAction}
 * </ul>
 * 
 * <p>
 * The state of shortcut is represented by following attributes:
 * <ul>
 * <li>UserId - owner of this shortcut
 * <li>KeyStroke - combination of keys which triggers the shortcut
 * <li>ShortcutType - type of shortcut
 * <li>Referenced - {@link Key} to parameter of this shortcut in case when the
 * type is LABEL (referenced is key to {@link Label} object) or LIST_FILTER
 * (referenced is key to {@link ListFilter} object)
 * <li>Action - value of enum {@link ShortcutAction} in case when type is ACTION
 * </ul>
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class Shortcut implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	private Key referenced;

	private String userId;

	private ShortcutType type;

	@Attribute(persistent = false)
	private Label referencedLabel;

	@Attribute(persistent = false)
	private ListFilter referencedListFilter;

	private ShortcutAction action;

	private String keyStroke;

	/**
	 * @return key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @param key
	 *            key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Shortcut other = (Shortcut) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	/**
	 * @return key to referenced object: either {@link Label} or
	 *         {@link ListFilter}, or null if type is ACTION
	 */
	public Key getReferenced() {
		return referenced;
	}

	/**
	 * @param referenced
	 *            key to referenced object: either {@link Label} or
	 *            {@link ListFilter}
	 */
	public void setReferenced(Key referenced) {
		this.referenced = referenced;
	}

	/**
	 * @return owner of this shortcut
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            owner of this shortcut
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return type of shortcut
	 */
	public ShortcutType getType() {
		return type;
	}

	/**
	 * @param type
	 *            type of shortcut
	 */
	public void setType(ShortcutType type) {
		this.type = type;
	}

	/**
	 * @return key combination triggering this shortcut
	 */
	public String getKeyStroke() {
		return keyStroke;
	}

	/**
	 * @param keyStroke
	 *            key combination triggering this shortcut
	 */
	public void setKeyStroke(String keyStroke) {
		this.keyStroke = keyStroke;
	}

	/**
	 * @return referenced label object
	 */
	public Label getReferencedLabel() {
		return referencedLabel;
	}

	/**
	 * @param referencedLabel
	 *            referenced label object
	 */
	public void setReferencedLabel(Label referencedLabel) {
		this.referencedLabel = referencedLabel;
	}

	/**
	 * @return referenced list filter object
	 */
	public ListFilter getReferencedListFilter() {
		return referencedListFilter;
	}

	/**
	 * @param referencedListFilter
	 *            referenced list filter object
	 */
	public void setReferencedListFilter(ListFilter referencedListFilter) {
		this.referencedListFilter = referencedListFilter;
	}

	public String getKeyName() {
		String userId = getUserId();
		String stroke = getKeyStroke();
		return SharedUtils.combineStringParts(userId, stroke);
	}

	/**
	 * @return action performed when this shortcut is triggered in case when
	 *         type is ACTION, null otherwise
	 */
	public ShortcutAction getAction() {
		return action;
	}

	/**
	 * @param action
	 *            action performed when this shortcut is triggered
	 */
	public void setAction(ShortcutAction action) {
		this.action = action;
	}
}
