package cz.artique.shared.model.label;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.listing.ArtiqueList;
import cz.artique.server.service.ExportServlet;
import cz.artique.shared.utils.HasDeepEquals;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.SharedUtils;

/**
 * ListFilter controls what and in what order items are displayed in
 * {@link ArtiqueList}. Following criteria are supported:
 * <ul>
 * <li>Filter - {@link Filter} filters items by presence of {@link Label}s
 * <li>Read - show only read/unread items or all if read is null
 * <li>StartFrom - show items no older than this date
 * <li>EndTo - show items no younger than this date
 * <li>Order - show items in descending/ascending order
 * </ul>
 * Leave the criterion blank of null to disable it.
 * 
 * <p>
 * ListFilter can be used either via ad-hoc query or saved filter. The later one
 * requires specifying persistent attributes:
 * <ul>
 * <li>UserId - owner of this list filter
 * <li>Name - name of this list filter assigned by user
 * <li>Hierarchy - path to root in list filters tree
 * <li>ExportAlias - non-null if items matching this list filter shall be
 * publicly accessible via {@link ExportServlet} in form of RSS/Atom. Set to
 * null in order to disable sharing.
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class ListFilter
		implements Serializable, HasHierarchy, HasDeepEquals<ListFilter> {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	@Attribute(unindexed = true)
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

	private String userId;

	private String exportAlias;

	@Attribute(persistent = false)
	private String shortcutStroke;

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
	 * Returns the version.
	 * 
	 * @return the version
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

	/**
	 * @return labels filter
	 */
	public Key getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            labels filter
	 */
	public void setFilter(Key filter) {
		this.filter = filter;
	}

	/**
	 * @return read criterion
	 */
	public Boolean getRead() {
		return read;
	}

	/**
	 * @param read
	 *            read criterion
	 */
	public void setRead(Boolean read) {
		this.read = read;
	}

	/**
	 * @return name of list filter
	 * @see cz.artique.shared.utils.HasHierarchy#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            name of list filter
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return desired items order
	 */
	public ListFilterOrder getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            desired items order
	 */
	public void setOrder(ListFilterOrder order) {
		this.order = order;
	}

	/**
	 * @return criterion filtering youngest items
	 */
	public Date getEndTo() {
		return endTo;
	}

	/**
	 * @param endTo
	 *            criterion filtering youngest items
	 */
	public void setEndTo(Date endTo) {
		this.endTo = endTo;
	}

	/**
	 * @return criterion filtering oldest items
	 */
	public Date getStartFrom() {
		return startFrom;
	}

	/**
	 * @param startFrom
	 *            criterion filtering oldest items
	 */
	public void setStartFrom(Date startFrom) {
		this.startFrom = startFrom;
	}

	/**
	 * @return object of filter
	 */
	public Filter getFilterObject() {
		return filterObject;
	}

	/**
	 * @param filterObject
	 *            object of filter
	 */
	public void setFilterObject(Filter filterObject) {
		this.filterObject = filterObject;
	}

	/**
	 * @return path to the root in list filters tree
	 * @see cz.artique.shared.utils.HasHierarchy#getHierarchy()
	 */
	public String getHierarchy() {
		return hierarchy;
	}

	/**
	 * @param hierarchy
	 *            path to the root in list filters tree
	 */
	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	/**
	 * Compares two list filters by values of their attributes.
	 * 
	 * @see cz.artique.shared.utils.HasDeepEquals#equalsDeeply(java.lang.Object)
	 */
	public boolean equalsDeeply(ListFilter e) {
		return SharedUtils.eq(getUserId(), e.getUserId())
			&& SharedUtils.eq(getRead(), e.getRead())
			&& SharedUtils.eq(getEndTo(), e.getEndTo())
			&& SharedUtils.eq(getHierarchy(), e.getHierarchy())
			&& SharedUtils.eq(getName(), e.getOrder())
			&& SharedUtils.eq(getOrder(), e.getOrder())
			&& SharedUtils.eq(getStartFrom(), e.getStartFrom())
			&& SharedUtils.eq(getExportAlias(), e.getExportAlias())
			&& SharedUtils.deepEq(getFilterObject(), e.getFilterObject());
	}

	/**
	 * @return owner of this list filter
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            owner of this list filter
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return alias for this list filter to be publicly accessible, null when
	 *         list filter is private
	 */
	public String getExportAlias() {
		return exportAlias;
	}

	/**
	 * @param exportAlias
	 *            alias for this list filter to be publicly accessible, null
	 *            when list filter is private
	 */
	public void setExportAlias(String exportAlias) {
		this.exportAlias = exportAlias;
	}

	// @Override
	/**
	 * Override annotation cannot be used, because {@link Object#clone()} is not
	 * implemented in GWT.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public ListFilter clone() {
		ListFilter clone = new ListFilter();
		clone.setEndTo(endTo);
		clone.setFilterObject(filterObject);
		clone.setHierarchy(hierarchy);
		clone.setName(name);
		clone.setOrder(order);
		clone.setRead(read);
		clone.setStartFrom(startFrom);
		clone.setUserId(userId);
		return clone;
	}

	/**
	 * Filled only on client-side.
	 * 
	 * @return key combination triggering this list filter
	 */
	public String getShortcutStroke() {
		return shortcutStroke;
	}

	/**
	 * @param shortcutStroke
	 *            key combination triggering this list filter
	 */
	public void setShortcutStroke(String shortcutStroke) {
		this.shortcutStroke = shortcutStroke;
	}
}
