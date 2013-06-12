package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.server.crawler.Crawler;
import cz.artique.shared.utils.HasDeepEquals;

/**
 * Region represents area on a web-page defined by CSS selectors used by
 * {@link Crawler} while checking {@link Source}.
 * CSS selectors are positive - the area included in result, and negative - the
 * area excluded from result. Negative selector is relative to result of
 * positive selector. Any valid CSS selector (or none) may be specified.
 * 
 * <p>
 * Each region belongs to some {@link HTMLSource} and has its uneditable name
 * and may have positive selector, negative selector or both of them.
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class Region implements Serializable, HasDeepEquals<Region> {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * Name of this region, not personalizable not editable
	 */
	@Attribute(unindexed = true)
	private String name;

	/**
	 * Selector of content which shall be processed
	 */
	@Attribute(unindexed = true)
	private String positiveSelector;

	/**
	 * List of selectors of content which shall be excluded from processing
	 */
	@Attribute(unindexed = true)
	private String negativeSelector;

	/**
	 * Reference to HTMLSource
	 */
	private Key htmlSource;

	// TODO nice to have: attribute last used
	// TODO nice to have: attribute usage

	/**
	 * Default constructor
	 */
	public Region() {}

	/**
	 * Constructs region and sets the {@link HTMLSource} the region belongs to.
	 * 
	 * @param htmlSource
	 *            owner of this region
	 */
	public Region(Key htmlSource) {
		this.htmlSource = htmlSource;
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
		Region other = (Region) obj;
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
	 * @return the source this region belongs to
	 */
	public Key getHtmlSource() {
		return htmlSource;
	}

	/**
	 * @return key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @return name of region
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return negative CSS selector or null if it does not exist
	 */
	public String getNegativeSelector() {
		return negativeSelector;
	}

	/**
	 * @return positive CSS selector or null if it does not exist
	 */
	public String getPositiveSelector() {
		return positiveSelector;
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
	 * @param htmlSource
	 *            the source this region belongs to
	 */
	public void setHtmlSource(Key htmlSource) {
		this.htmlSource = htmlSource;
	}

	/**
	 * Tests equality of regions by equality of selectors and the source they
	 * belong to.
	 * 
	 * @see cz.artique.shared.utils.HasDeepEquals#equalsDeeply(java.lang.Object)
	 */
	public boolean equalsDeeply(Region other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (htmlSource == null) {
			if (other.htmlSource != null)
				return false;
		} else if (!htmlSource.equals(other.htmlSource))
			return false;
		if (negativeSelector == null) {
			if (other.negativeSelector != null)
				return false;
		} else if (!negativeSelector.equals(other.negativeSelector))
			return false;
		if (positiveSelector == null) {
			if (other.positiveSelector != null)
				return false;
		} else if (!positiveSelector.equals(other.positiveSelector))
			return false;
		return true;
	}

	/**
	 * @param key
	 *            key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * Change of name by user is not supported.
	 * 
	 * @param name
	 *            name of this source
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param negativeSelector
	 *            negative CSS selector or null
	 */
	public void setNegativeSelector(String negativeSelector) {
		this.negativeSelector = negativeSelector;
	}

	/**
	 * @param positiveSelector
	 *            positive CSS selector or null
	 */
	public void setPositiveSelector(String positiveSelector) {
		this.positiveSelector = positiveSelector;
	}

	/**
	 * @param version
	 *            version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

}
