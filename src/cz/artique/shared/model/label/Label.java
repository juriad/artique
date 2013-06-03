package cz.artique.shared.model.label;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.HasDeepEquals;
import cz.artique.shared.utils.HasDisplayName;
import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class Label
		implements Serializable, GenKey, HasName, HasDisplayName, HasKey<Key>,
		Comparable<Label>, HasDeepEquals<Label> {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * Owner of this label
	 */
	private User user;

	/**
	 * Name of this label
	 * Name must not contain spaces or $.
	 */
	private String name;

	/**
	 * Name to display
	 */
	@Attribute(persistent = false)
	private transient String displayName;

	private LabelType labelType;

	/**
	 * Level of backup
	 */
	@Attribute(unindexed = true)
	private BackupLevel backupLevel;

	@Attribute(persistent = false)
	private boolean toBeDeleted;

	@Attribute(unindexed = true)
	private int priority;

	@Attribute(persistent = false)
	private String shortcutStroke;

	/**
	 * Apperiance of this label
	 */
	@Attribute(lob = true)
	private LabelAppearance appearance;

	public Label() {}

	public Label(User user, String name) {
		setUser(user);
		setName(name);
		setBackupLevel(BackupLevel.NO_BACKUP);
		setAppearance(new LabelAppearance());
		setLabelType(LabelType.USER_DEFINED);
		setPriority(0);
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
		Label other = (Label) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			} else {
				if (!equalsDeeply(other)) {
					return false;
				}
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	public LabelAppearance getAppearance() {
		return appearance;
	}

	public BackupLevel getBackupLevel() {
		return backupLevel;
	}

	/**
	 * Returns the key.
	 * 
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public User getUser() {
		return user;
	}

	/**
	 * Returns the version.
	 * 
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	public void setAppearance(LabelAppearance appearance) {
		this.appearance = appearance;
	}

	public void setBackupLevel(BackupLevel backupLevel) {
		this.backupLevel = backupLevel;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getKeyName() {
		String type = getLabelType().name();
		String userId = getUser().getUserId();
		String name = getName();
		return SharedUtils.combineStringParts(type, userId, name);
	}

	public LabelType getLabelType() {
		return labelType;
	}

	public void setLabelType(LabelType labelType) {
		this.labelType = labelType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int compareTo(Label o) {
		int res = ((Integer) this.getPriority()).compareTo(o.getPriority());
		if (res == 0) {
			res = this.getDisplayName().compareToIgnoreCase(o.getDisplayName());
		}
		return res;
	}

	public String getDisplayName() {
		return displayName == null ? getName() : displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean equalsDeeply(Label e) {
		return getName().equals(e.getName()) && getUser().equals(e.getUser());
	}

	public boolean isToBeDeleted() {
		return toBeDeleted;
	}

	public void setToBeDeleted(boolean toBeDeleted) {
		this.toBeDeleted = toBeDeleted;
	}

	public String getShortcutStroke() {
		return shortcutStroke;
	}

	public void setShortcutStroke(String shortcutStroke) {
		this.shortcutStroke = shortcutStroke;
	}
}
