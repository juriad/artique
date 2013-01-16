package cz.artique.shared.model.label;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class ListFilter implements Serializable, HasKey<Key>, HasName {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	private Key filter;

	@Attribute(persistent = false)
	private Filter filterObject;

	private Boolean read;

	private String name;

	private FilterOrder order;

	private Date startFrom;

	private Date endTo;

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
		ListFilter other = (ListFilter) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	public Key getFilter() {
		return filter;
	}

	public void setFilter(Key filter) {
		this.filter = filter;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FilterOrder getOrder() {
		return order;
	}

	public void setOrder(FilterOrder order) {
		this.order = order;
	}

	public Date getEndTo() {
		return endTo;
	}

	public void setEndTo(Date endTo) {
		this.endTo = endTo;
	}

	public Date getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(Date startFrom) {
		this.startFrom = startFrom;
	}

	public Filter getFilterObject() {
		return filterObject;
	}

	public void setFilterObject(Filter filterObject) {
		this.filterObject = filterObject;
	}
}