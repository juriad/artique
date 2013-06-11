package cz.artique.shared.model.item;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.source.UserSource;

@Model(schemaVersion = 1)
public class UserItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * Owner of this item
	 */
	private String userId;

	/**
	 * Item
	 */
	private Key item;

	@Attribute(persistent = false)
	private Item itemObject;

	/**
	 * Assigned labels
	 */
	private List<Key> labels;

	private boolean read;

	private Date added;

	private Date lastChanged;

	private Key userSource;

	@Attribute(unindexed = true)
	private String backupBlobKey;

	public UserItem() {}

	public UserItem(Item item, UserSource userSource) {
		this.item = item.getKey();
		this.added = item.getAdded();
		this.lastChanged = added;
		if (userSource != null) {
			this.userSource = userSource.getKey();
			this.labels = userSource.getDefaultLabels();
			this.userId = userSource.getUserId();
		}
		this.read = false;
	}

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
		UserItem other = (UserItem) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	public Key getItem() {
		return item;
	}

	public void setItem(Key item) {
		this.item = item;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Key> getLabels() {
		return labels;
	}

	public void setLabels(List<Key> labels) {
		this.labels = labels;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Date getAdded() {
		return added;
	}

	public void setAdded(Date added) {
		this.added = added;
	}

	public Date getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}

	public Item getItemObject() {
		return itemObject;
	}

	public void setItemObject(Item itemObject) {
		this.itemObject = itemObject;
	}

	public Key getUserSource() {
		return userSource;
	}

	public void setUserSource(Key userSource) {
		this.userSource = userSource;
	}

	public String getBackupBlobKey() {
		return backupBlobKey;
	}

	public void setBackupBlobKey(String backupBlobKey) {
		this.backupBlobKey = backupBlobKey;
	}
}
