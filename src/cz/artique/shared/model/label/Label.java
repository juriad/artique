package cz.artique.shared.model.label;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@Model(schemaVersion = 1)
public class Label implements Serializable {

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
	 */
	private String name;

	/**
	 * Level of backup
	 */
	@Attribute(unindexed = true)
	private BackupLevel backupLevel;

	/**
	 * Apperiance of this label
	 */
	@Attribute(lob = true)
	private LabelApperiance apperiance;

	public Label() {}

	public Label(User user, String name) {
		setUser(user);
		setName(name);
		setBackupLevel(BackupLevel.NO_BACKUP);
		setApperiance(new LabelApperianceImpl(VisibilityLevel.ONLY_ASSIGNED,
			ApperianceType.TEXT));
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
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	public LabelApperiance getApperiance() {
		return apperiance;
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

	public void setApperiance(LabelApperiance apperiance) {
		this.apperiance = apperiance;
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
}
