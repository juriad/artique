package cz.artique.shared.model.label;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.shared.utils.HasDeepEquals;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;
import cz.artique.shared.utils.SharedUtils;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class ListFilter
		implements Serializable, HasKey<Key>, HasName, HasHierarchy,
		HasDeepEquals<ListFilter> {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	private Key filter;

	@Attribute(persistent = false)
	private Filter filterObject;

	@Attribute(unindexed = true)
	private Boolean read;

	private String name;

	@Attribute(unindexed = true)
	private ListFilterOrder order;

	@Attribute(unindexed = true)
	private Date startFrom;

	@Attribute(unindexed = true)
	private Date endTo;

	private String hierarchy;

	private User user;

	private String exportAlias;

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

	public ListFilterOrder getOrder() {
		return order;
	}

	public void setOrder(ListFilterOrder order) {
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

	public String getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	public boolean equalsDeeply(ListFilter e) {
		return SharedUtils.eq(getUser(), e.getUser())
			&& SharedUtils.eq(getRead(), e.getRead())
			&& SharedUtils.eq(getEndTo(), e.getEndTo())
			&& SharedUtils.eq(getHierarchy(), e.getHierarchy())
			&& SharedUtils.eq(getName(), e.getOrder())
			&& SharedUtils.eq(getOrder(), e.getOrder())
			&& SharedUtils.eq(getStartFrom(), e.getStartFrom())
			&& SharedUtils.eq(getExportAlias(), e.getExportAlias())
			&& SharedUtils.deepEq(getFilterObject(), e.getFilterObject());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getExportAlias() {
		return exportAlias;
	}

	public void setExportAlias(String exportAlias) {
		this.exportAlias = exportAlias;
	}

	public ListFilter clone() {
		ListFilter clone = new ListFilter();
		clone.setEndTo(endTo);
		clone.setFilterObject(filterObject);
		clone.setHierarchy(hierarchy);
		clone.setName(name);
		clone.setOrder(order);
		clone.setRead(read);
		clone.setStartFrom(startFrom);
		clone.setUser(user);
		return clone;
	}
}
