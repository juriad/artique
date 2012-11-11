package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class UserSource implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * User side of this relation
	 */
	private User user;

	/**
	 * Source side of this relation
	 */
	private Key source;

	/**
	 * Personalized name of source
	 */
	private String name;

	/**
	 * Hierarchy of sources
	 */
	private String hierarchy;

	private boolean watching;

	public UserSource() {}

	public UserSource(User user, Source source, String name) {
		setUser(user);
		setSource(source.getKey());
		setName(name);
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
		UserSource other = (UserSource) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	public String getHierarchy() {
		return hierarchy;
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

	public Key getSource() {
		return source;
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

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
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

	public void setSource(Key source) {
		this.source = source;
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

	public boolean isWatching() {
		return watching;
	}

	public void setWatching(boolean watching) {
		this.watching = watching;
	}

	public Key getKeyParent() {
		return null;
	}

	public String getKeyName() {
		String userId = getUser().getUserId();
		String sourceId = KeyFactory.keyToString(getSource());
		return SharedUtils.combineStringParts(userId, sourceId);
	}
}
