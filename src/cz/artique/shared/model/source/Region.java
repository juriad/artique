package cz.artique.shared.model.source;

import java.io.Serializable;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class Region implements Serializable, GenKey {

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
	private List<String> negativeSelectors;

	/**
	 * Reference to HTMLSource (either PageChange or WebSite)
	 */
	private Key htmlSource;
	
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

	public List<String> getNegativeSelectors() {
		return negativeSelectors;
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

	public void setNegativeSelectors(List<String> negativeSelectors) {
		this.negativeSelectors = negativeSelectors;
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

	public Key getKeyParent() {
		return getHtmlSource();
	}

	public String getKeyName() {
		String name = getName();
		return SharedUtils.combineStringParts(name);
	}
}
