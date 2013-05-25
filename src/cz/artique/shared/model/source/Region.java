package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Region implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * Name of this region, not personalizable
	 */
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

	private RegionType type;

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

	public Key getHtmlSource() {
		return htmlSource;
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

	public String getNegativeSelector() {
		return negativeSelector;
	}

	public String getPositiveSelector() {
		return positiveSelector;
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

	public void setHtmlSource(Key htmlSource) {
		this.htmlSource = htmlSource;
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

	public void setNegativeSelector(String negativeSelector) {
		this.negativeSelector = negativeSelector;
	}

	public void setPositiveSelector(String positiveSelector) {
		this.positiveSelector = positiveSelector;
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

	public RegionType getType() {
		return type;
	}

	public void setType(RegionType type) {
		this.type = type;
	}
}
