package cz.artique.shared.model.recomandation;

import java.io.Serializable;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.sources.UserSourceDialog;
import cz.artique.server.service.RecommendationService;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.utils.GenKey;

/**
 * Recommendation is list of {@link Source}s recommended by system to each user
 * by similarity of sources and similarity of users watching similar sources.
 * Recommendation is calculated off-line once a day. The algorithm of
 * calculation is described in {@link RecommendationService}.
 * 
 * <p>
 * Each recommendation consists of following attributes:
 * <ul>
 * <li>UserId - user the recommendation is calculated for
 * <li>RecommendedSources - list of source recommended to user
 * </ul>
 * 
 * <p>
 * List of recommended sources is available to user in {@link UserSourceDialog}
 * when creating new source.
 * 
 * @author Adam Juraszek
 * 
 */
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

	/**
	 * Default constructor for slim3 framework.
	 */
	public Recommendation() {}

	/**
	 * Constructor setting owner of this recommendation.
	 * 
	 * @param userId
	 *            user this recommendation is computer for
	 */
	public Recommendation(String userId) {
		this.userId = userId;
	}

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
	 * @return version
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

	/**
	 * @return user id of user this recommendation is for
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            user id of user this recommendation is for
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns list of keys of sources, to get list of sources, use
	 * {@link #getRecommendedSourcesObjects()}.
	 * 
	 * @return list of recommended sources to user {@link #getUserId()}
	 */
	public List<Key> getRecommendedSources() {
		return recommendedSources;
	}

	/**
	 * @param recommendedSources
	 *            list of recommended sources to user {@link #getUserId()}
	 */
	public void setRecommendedSources(List<Key> recommendedSources) {
		this.recommendedSources = recommendedSources;
	}

	/**
	 * @return list of sources objects recommended to user {@link #getUserId()}
	 */
	public List<Source> getRecommendedSourcesObjects() {
		return recommendedSourcesObjects;
	}

	/**
	 * @param recommendedSourcesObjects
	 *            list of sources objects recommended to user
	 *            {@link #getUserId()}
	 */
	public void setRecommendedSourcesObjects(
			List<Source> recommendedSourcesObjects) {
		this.recommendedSourcesObjects = recommendedSourcesObjects;
	}

	public String getKeyName() {
		return getUserId();
	}
}
