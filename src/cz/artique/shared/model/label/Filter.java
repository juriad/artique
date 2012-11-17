package cz.artique.shared.model.label;

import java.io.Serializable;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@Model(schemaVersion = 1)
public class Filter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * Owner of this filter
	 */
	private User user;

	/**
	 * Name of this filter
	 */
	private String name;

	/**
	 * Filter composition
	 */
	private List<Key> filters;

	@Attribute(persistent = false)
	private List<Filter> filterObjects;

	/**
	 * Operands of this filter
	 */
	private List<Key> labels;

	/**
	 * Operator
	 */
	private Operator operator;

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
		Filter other = (Filter) obj;
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
	 * Returns the key.
	 * 
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	public List<Key> getLabels() {
		return labels;
	}

	public String getName() {
		return name;
	}

	public Operator getOperator() {
		return operator;
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

	/**
	 * Sets the key.
	 * 
	 * @param key
	 *            the key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	public void setLabels(List<Key> labels) {
		this.labels = labels;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
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

	public List<Key> getFilters() {
		return filters;
	}

	public void setFilters(List<Key> filters) {
		this.filters = filters;
	}

	public List<Filter> getFilterObjects() {
		return filterObjects;
	}

	public void setFilterObjects(List<Filter> filterObjects) {
		this.filterObjects = filterObjects;
	}
}
