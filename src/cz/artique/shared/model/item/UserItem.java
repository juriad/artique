package cz.artique.shared.model.item;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.listing.ArtiqueList;
import cz.artique.server.crawler.Crawler;
import cz.artique.shared.model.label.BackupLevel;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.user.UserInfo;

/**
 * Personalizes {@link Item}: it allows users to add his own {@link Label}s to
 * an {@link Item}.
 * 
 * <p>
 * When a new {@link Item} is created either by {@link Crawler} or
 * {@link ManualItem} by user, a new {@link UserItem} is created for each user
 * watching the crawled {@link Source}. The default state of UserItem (after its
 * creation) contains:
 * <ul>
 * <li>UserId - reference to {@link UserInfo}, the user owning this UserItem
 * <li>UserSource - reference to personalized (user) source, which this UserItem
 * belong to
 * <li>Item - reference to {@link Item}, which contains general information like
 * content, author etc.
 * <li>LastChange - set to null; this UserItem has not been changed yet; this
 * attribute is used when fetching more items for {@link ArtiqueList}, the
 * updates are fetched as well.
 * <li>Added - attribute set to the same value as {@link Item#getAdded()}, it is
 * used when querying {@link UserItem}s by {@link ListFilter} which contains
 * date range.
 * <li>Read - set to false; it is automatically set to true when the user
 * selected the row in {@link ArtiqueList}
 * <li>Labels - set to the same list of labels as
 * {@link UserSource#getDefaultLabels()}; user may add or remove any
 * USER_DEFINED labels.
 * <li>Backup BlobKey - key to file storage, which provides backed-up version of
 * original page.
 * </ul>
 * 
 * @author Adam Juraszek
 * 
 */
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
	private boolean backup;

	@Attribute(persistent = false)
	private String serializedKey;

	/**
	 * Default constructor for slim3 framework.
	 */
	public UserItem() {}

	/**
	 * Constructs the object and sets the most essential attributes: the item,
	 * added time-stamp, user source, labels, user, read. See {@link UserItem}
	 * for information about default values.
	 * 
	 * @param item
	 * @param userSource
	 */
	public UserItem(Item item, UserSource userSource) {
		this.item = item.getKey();
		this.added = item.getAdded();
		if (userSource != null) {
			this.userSource = userSource.getKey();
			this.labels = userSource.getDefaultLabels();
			this.userId = userSource.getUserId();
		}
		this.read = false;
	}

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

	/**
	 * @return item this UserItem is pesonalization for
	 */
	public Key getItem() {
		return item;
	}

	/**
	 * @param item
	 *            item this UserItem is pesonalization for
	 */
	public void setItem(Key item) {
		this.item = item;
	}

	/**
	 * @return owner of this UserItem
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            owner of this UserItem
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * List always contains at least one {@link Label} of type USER_SOURCE.
	 * 
	 * @return list of {@link Label}s assigned by user to this UserSource
	 */
	public List<Key> getLabels() {
		return labels;
	}

	/**
	 * @param labels
	 *            list of {@link Label}s assigned by user to this UserSource
	 */
	public void setLabels(List<Key> labels) {
		this.labels = labels;
	}

	/**
	 * @return whether UserItem has been read
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @param read
	 *            whether UserItem has been read
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * The same as {@link Item#getAdded()}
	 * 
	 * @return time-stamp, when UserItem was created
	 */
	public Date getAdded() {
		return added;
	}

	/**
	 * @param added
	 *            time-stamp, when UserItem was created
	 */
	public void setAdded(Date added) {
		this.added = added;
	}

	/**
	 * @return time-stamp of last change
	 */
	public Date getLastChanged() {
		return lastChanged;
	}

	/**
	 * @param lastChanged
	 *            time-stamp of last change
	 */
	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}

	/**
	 * @return object of item
	 */
	public Item getItemObject() {
		return itemObject;
	}

	/**
	 * @param itemObject
	 *            object of item
	 */
	public void setItemObject(Item itemObject) {
		this.itemObject = itemObject;
	}

	/**
	 * @return {@link UserSource} this {@link UserItem} belongs to
	 */
	public Key getUserSource() {
		return userSource;
	}

	/**
	 * @param userSource
	 *            {@link UserSource} this {@link UserItem} belongs to
	 */
	public void setUserSource(Key userSource) {
		this.userSource = userSource;
	}

	/**
	 * Backup is automatically created when a {@link Label} with
	 * {@link BackupLevel} higher than NO_BACKUP is assigned.
	 * 
	 * @return whether this {@link UserItem} has been backed up
	 */
	public boolean isBackup() {
		return backup;
	}

	/**
	 * @param backup
	 *            whether the {@link UserItem} is backed up
	 */
	public void setBackup(boolean backup) {
		this.backup = backup;
	}

	public String getSerializedKey() {
		return serializedKey;
	}

	public void setSerializedKey(String serializedKey) {
		this.serializedKey = serializedKey;
	}
}
