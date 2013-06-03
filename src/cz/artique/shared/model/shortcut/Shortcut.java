package cz.artique.shared.model.shortcut;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class Shortcut implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	private Key referenced;

	private User user;

	private ShortcutType type;

	@Attribute(persistent = false)
	private Label referencedLabel;

	@Attribute(persistent = false)
	private ListFilter referencedListFilter;

	private ShortcutAction action;

	private String keyStroke;

	/**
	 * Returns the key.
	 * 
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * Sets the key.
	 * 
	 * @param key
	 *            the key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * Returns the version.
	 * 
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the version
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

	public Key getReferenced() {
		return referenced;
	}

	public void setReferenced(Key referenced) {
		this.referenced = referenced;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ShortcutType getType() {
		return type;
	}

	public void setType(ShortcutType type) {
		this.type = type;
	}

	public String getKeyStroke() {
		return keyStroke;
	}

	public void setKeyStroke(String keyStroke) {
		this.keyStroke = keyStroke;
	}

	public Label getReferencedLabel() {
		return referencedLabel;
	}

	public void setReferencedLabel(Label referencedLabel) {
		this.referencedLabel = referencedLabel;
	}

	public ListFilter getReferencedListFilter() {
		return referencedListFilter;
	}

	public void setReferencedListFilter(ListFilter referencedListFilter) {
		this.referencedListFilter = referencedListFilter;
	}

	public String getKeyName() {
		String userId = getUser().getUserId();
		String stroke = getKeyStroke();
		return SharedUtils.combineStringParts(userId, stroke);
	}

	public ShortcutAction getAction() {
		return action;
	}

	public void setAction(ShortcutAction action) {
		this.action = action;
	}
}
