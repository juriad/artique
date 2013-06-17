package cz.artique.shared.model.label;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.labels.LabelsDialog;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.HasDeepEquals;
import cz.artique.shared.utils.SharedUtils;

/**
 * Label is the smallest possible information the user may add to a
 * {@link UserItem}. User can add {@link Label}s to {@link UserItem}s
 * to categorize interesting UserItems in order to find them easier later or to
 * backup the UserItem.
 * Label always belongs to a user; there exists policy restricting label's name:
 * name must not contain white-spaces or dollar sign.
 * This applies to labels of type {@link LabelType#USER_DEFINED}.
 * 
 * <p>
 * There also exists {@link Label}s of type {@link LabelType#USER_SOURCE}. Each
 * {@link UserSource} has each own {@link Label}. This label is used to mark
 * {@link UserItem}s belonging to the {@link UserSource}. Name of this type of
 * labels is string representation of key of the {@link UserSource}, that makes
 * the name stable. Nonpersistent attribute displayName solves the issue with
 * human unreadable name of USER_SOURCE labels.
 * 
 * <p>
 * Labels have following persistent attributes:
 * <ul>
 * <li>UserId - id of user who owns this label
 * <li>Name - name of label, see above for more information
 * <li>LabelType - type of label, value of {@link LabelType} enum
 * <li>BackupLevel - level of backup to be performed when the label is added
 * <li>Priority - priority of label when it is shown in list; labels with the
 * same priority are sorted alphabetically; priority is available to user
 * <li>Appearance - object controlling view of label when it is shown
 * </ul>
 * 
 * There are also many nonpersistent attributes:
 * <ul>
 * <li>DisplayName - shown instead of name, display name is human readable
 * <li>ToBeDeleted - set to true on client in dialog {@link LabelsDialog} when
 * label is marked to be deleted when labels are updated
 * <li>ShortcutStroke - key combination triggering addition or removal of this
 * label
 * </ul>
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class Label
		implements Serializable, GenKey, Comparable<Label>,
		HasDeepEquals<Label> {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * Owner of this label
	 */
	private String userId;

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
	 * Appearance of this label
	 */
	@Attribute(lob = true)
	private LabelAppearance appearance;

	public Label() {}

	public Label(String userId, String name) {
		setUserId(userId);
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

	public String getUserId() {
		return userId;
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

	public void setUserId(String userId) {
		this.userId = userId;
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
		String userId = getUserId();
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
		return getName().equals(e.getName())
			&& getUserId().equals(e.getUserId());
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
