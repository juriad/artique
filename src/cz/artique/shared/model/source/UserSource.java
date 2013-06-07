package cz.artique.shared.model.source;

import java.io.Serializable;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import cz.artique.shared.model.label.Label;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.HasDeepEquals;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.HasKey;
import cz.artique.shared.utils.HasName;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class UserSource
		implements Serializable, GenKey, HasName, HasKey<Key>, HasHierarchy,
		HasDeepEquals<UserSource> {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * User side of this relation
	 */
	private String userId;

	/**
	 * Source side of this relation
	 */
	private Key source;

	@Attribute(persistent = false)
	private Source sourceObject;

	/**
	 * Personalized name of source
	 */
	private String name;

	/**
	 * Hierarchy of sources
	 */
	private String hierarchy;

	private boolean watching;

	private List<Key> defaultLabels;

	private Key label;

	/**
	 * This label object is used exacly once: when the source is created
	 */
	@Attribute(persistent = false)
	private Label labelObject;

	private SourceType sourceType;

	private Key region;

	@Attribute(persistent = false)
	private Region regionObject;

	@Attribute(unindexed = true)
	private Key crawlerData;

	public UserSource() {}

	public UserSource(String userId, Source source, String name) {
		setUserId(userId);
		setSource(source.getKey());
		setName(name);
		setHierarchy("/");
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
		UserSource other = (UserSource) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	public String getHierarchy() {
		return hierarchy;
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

	public Key getSource() {
		return source;
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

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
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

	public void setSource(Key source) {
		this.source = source;
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

	public boolean isWatching() {
		return watching;
	}

	public void setWatching(boolean watching) {
		this.watching = watching;
	}

	public String getKeyName() {
		String userId = getUserId();
		String sourceId = KeyFactory.keyToString(getSource());
		return SharedUtils.combineStringParts(userId, sourceId);
	}

	public Source getSourceObject() {
		return sourceObject;
	}

	public void setSourceObject(Source sourceObject) {
		this.sourceObject = sourceObject;
	}

	public List<Key> getDefaultLabels() {
		return defaultLabels;
	}

	public void setDefaultLabels(List<Key> defaultLabels) {
		this.defaultLabels = defaultLabels;
	}

	public Key getLabel() {
		return label;
	}

	public void setLabel(Key label) {
		this.label = label;
	}

	public boolean equalsDeeply(UserSource o) {
		return this.equals(o) && SharedUtils.eq(getUserId(), o.getUserId())
			&& SharedUtils.eq(getDefaultLabels(), o.getDefaultLabels())
			&& SharedUtils.eq(getHierarchy(), o.getHierarchy())
			&& SharedUtils.eq(getLabel(), o.getLabel())
			&& SharedUtils.eq(getName(), o.getLabel())
			&& SharedUtils.eq(getSource(), o.getSource());
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	public Key getRegion() {
		return region;
	}

	public void setRegion(Key region) {
		this.region = region;
	}

	public Region getRegionObject() {
		return regionObject;
	}

	public void setRegionObject(Region regionObject) {
		this.regionObject = regionObject;
	}

	public Key getCrawlerData() {
		return crawlerData;
	}

	public void setCrawlerData(Key crawlerData) {
		this.crawlerData = crawlerData;
	}

	public Label getLabelObject() {
		return labelObject;
	}

	public void setLabelObject(Label labelObject) {
		this.labelObject = labelObject;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
