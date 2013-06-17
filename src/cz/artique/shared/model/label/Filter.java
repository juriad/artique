package cz.artique.shared.model.label;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.utils.HasDeepEquals;
import cz.artique.shared.utils.SharedUtils;

/**
 * Filter represents composition of several {@link Label}s combined by AND or OR
 * operator. Each filter belongs to a user and is part of {@link ListFilter}.
 * 
 * <p>
 * A filter consists of two levels:
 * <ul>
 * <li>TOP_LEVEL_FILTER - contains filters of level SECOND_LEVEL_FILTER and
 * {@link Label}s combined by operator OR
 * <li>SECONF_LEVEL_FILTER - contains {@link Label}s combined by operator AND.
 * If this filter contains only one {@link Label}, it is discarded and replaced
 * by label on top level.
 * </ul>
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class Filter implements Serializable, HasDeepEquals<Filter> {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * Owner of this filter
	 */
	private String userId;

	/**
	 * Filter composition
	 */
	@Attribute(unindexed = true)
	private List<Key> filters;

	@Attribute(persistent = false)
	private List<Filter> filterObjects;

	/**
	 * Operands of this filter
	 */
	private List<Key> labels;

	private FilterLevel level;

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
	 * @return key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @return list of labels
	 */
	public List<Key> getLabels() {
		return labels;
	}

	/**
	 * @return owner of the filter
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return version
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
	 * @param key
	 *            key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @param labels
	 *            list of labels
	 */
	public void setLabels(List<Key> labels) {
		this.labels = labels;
	}

	/**
	 * @param userId
	 *            owner of the filter
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param version
	 *            version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return list of filters
	 */
	public List<Key> getFilters() {
		return filters;
	}

	/**
	 * @param filters
	 *            list of filters
	 */
	public void setFilters(List<Key> filters) {
		this.filters = filters;
	}

	/**
	 * @return list of filter objects
	 */
	public List<Filter> getFilterObjects() {
		return filterObjects;
	}

	/**
	 * @param filterObjects
	 *            list of filter objects
	 */
	public void setFilterObjects(List<Filter> filterObjects) {
		this.filterObjects = filterObjects;
	}

	/**
	 * @return level of filter
	 */
	public FilterLevel getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            level of filter
	 */
	public void setLevel(FilterLevel level) {
		this.level = level;
	}

	/**
	 * Compares equality of filters by its attributes.
	 * 
	 * @see cz.artique.shared.utils.HasDeepEquals#equalsDeeply(java.lang.Object)
	 */
	public boolean equalsDeeply(Filter e) {
		return SharedUtils.eq(getLabels(), e.getLabels())
			&& SharedUtils.eq(getLevel(), e.getLevel())
			&& SharedUtils.eq(getUserId(), e.getUserId())
			&& SharedUtils.deepEq(getFilterObjects(), e.getFilterObjects());
	}

	/**
	 * @return list of all labels referenced by this filter and all sub-filters
	 */
	public List<Key> flat() {
		List<Key> keys = new ArrayList<Key>();
		if (getLabels() != null) {
			keys.addAll(getLabels());
		}
		if (getFilterObjects() != null) {
			for (Filter f : getFilterObjects()) {
				keys.addAll(f.flat());
			}
		}
		return keys;
	}
}
