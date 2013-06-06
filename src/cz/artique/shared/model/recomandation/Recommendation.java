package cz.artique.shared.model.recomandation;

import java.io.Serializable;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.model.source.Source;
import cz.artique.shared.utils.GenKey;

@Model(schemaVersion = 1)
public class Recommendation implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	private String userId;

	@Attribute(unindexed = true)
	private List<Key> recommendedSources;

	@Attribute(persistent = false)
	private List<Source> recommendedSourcesObjects;

	public Recommendation() {}

	public Recommendation(String userId) {
		this.userId = userId;
	}

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
		Recommendation other = (Recommendation) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Key> getRecommendedSources() {
		return recommendedSources;
	}

	public void setRecommendedSources(List<Key> recommendedSources) {
		this.recommendedSources = recommendedSources;
	}

	public List<Source> getRecommendedSourcesObjects() {
		return recommendedSourcesObjects;
	}

	public void setRecommendedSourcesObjects(
			List<Source> recommendedSourcesObjects) {
		this.recommendedSourcesObjects = recommendedSourcesObjects;
	}

	public String getKeyName() {
		return getUserId();
	}
}
