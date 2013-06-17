package cz.artique.shared.model.source;

import java.io.Serializable;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import cz.artique.client.hierarchy.tree.SourcesTree;
import cz.artique.client.items.ManualItemLabelsBar;
import cz.artique.server.crawler.Crawler;
import cz.artique.server.crawler.PageChangeCrawler;
import cz.artique.server.service.CrawlerService;
import cz.artique.shared.model.config.server.ServerConfigKey;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.user.UserInfo;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.HasDeepEquals;
import cz.artique.shared.utils.HasHierarchy;
import cz.artique.shared.utils.SharedUtils;

/**
 * UserSource represents personalized {@link Source}. It allows several users to
 * have attributes of their sources customized: name, location in hierarchy,
 * auto-assigned labels.
 * 
 * <p>
 * UserSource has relations to several classes in model:
 * <ul>
 * <li> {@link UserInfo} via userId - the user having this userSource listed in
 * his {@link SourcesTree}.
 * <li> {@link Source} - the source the user watches
 * <li> {@link Region} - the region on {@link HTMLSource} page the user is
 * interested in
 * <li> {@link CrawlerData} - Auxiliary data used and stored by {@link Crawler}
 * when crawling source. Currently only {@link PageChangeCrawler} requires
 * additional data: {@link PageChangeCrawlerData}, text of web-page which is new
 * content compared to.
 * <li> {@link Label} - label of type {@link LabelType#USER_SOURCE} which is
 * always assigned to a {@link UserItem} which belongs to this usersource
 * <li>List of {@link Label}s - list of all labels which are assigned when a new
 * {@link UserItem} is created. For {@link ManualSource}, the labels are only
 * preassigned in {@link ManualItemLabelsBar} and user can adjust them before
 * the {@link UserItem} is created.
 * </ul>
 * 
 * <p>
 * There are attributes which define state of {@link UserSource}:
 * <ul>
 * <li>Name - the name assigned by user; {@link ManualSource} has default name
 * {@link ServerConfigKey#MANUAL_SOURCE_NAME}.
 * <li>Hierarchy - path to the root of sources tree; defined by
 * {@link HasHierarchy#getHierarchy()}
 * <li>Watching - boolean flag indicating whether user wants to be informed
 * about new items on this source. UserSource cannot be deleted, user can only
 * mark it as "not watching". Watching state may be changed any number-times;
 * the items which occurred while the source was not watched are permanently
 * unavailable to the user.
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class UserSource
		implements Serializable, GenKey, HasHierarchy,
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

	/**
	 * Default constructor for slim3 framework.
	 */
	public UserSource() {}

	/**
	 * Constructs a new UserSource and sets some essential attributes.
	 * 
	 * @param userId
	 *            owner of this UserSource
	 * @param source
	 *            watched {@link Source}
	 * @param name
	 *            name of this UserSource
	 */
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
	 * @return key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * Name of this {@link UserSource} assigned by user during its creation. For
	 * {@link ManualSource}, the default name is
	 * {@link ServerConfigKey#MANUAL_SOURCE_NAME}.
	 * 
	 * @see cz.artique.shared.utils.HasHierarchy#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * The concrete source this UserSource is proxy for.
	 * 
	 * @return source
	 */
	public Key getSource() {
		return source;
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

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	/**
	 * @param key
	 *            key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @param name
	 *            the name assigned by user to this UserSource
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param source
	 *            the source this UserSource is proxy for
	 */
	public void setSource(Key source) {
		this.source = source;
	}

	/**
	 * @param version
	 *            version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return true if user is watching this UserSource, false otherwise
	 */
	public boolean isWatching() {
		return watching;
	}

	/**
	 * Sets whether this UserSource should generate new {@link UserItem}s.
	 * 
	 * @param watching
	 *            watching
	 */
	public void setWatching(boolean watching) {
		this.watching = watching;
	}

	public String getKeyName() {
		String userId = getUserId();
		String sourceId = KeyFactory.keyToString(getSource());
		return SharedUtils.combineStringParts(userId, sourceId);
	}

	/**
	 * @return object of source
	 */
	public Source getSourceObject() {
		return sourceObject;
	}

	/**
	 * @param sourceObject
	 *            object of source
	 */
	public void setSourceObject(Source sourceObject) {
		this.sourceObject = sourceObject;
	}

	/**
	 * Default labels list includes label returned by {@link #getLabel()}.
	 * All these labels are assigned to a new {@link UserItem} except for
	 * UserItem representing {@link ManualItem}, those labels are only
	 * preassigned and may be changed.
	 * 
	 * @return list of auto-assigned labels
	 */
	public List<Key> getDefaultLabels() {
		return defaultLabels;
	}

	/**
	 * @param defaultLabels
	 *            list of auto-assigned labels
	 */
	public void setDefaultLabels(List<Key> defaultLabels) {
		this.defaultLabels = defaultLabels;
	}

	/**
	 * @return the label marking {@link UserItem} is belonging to this
	 *         UserSource
	 */
	public Key getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label marking {@link UserItem} is belonging to this
	 *            UserSource
	 */
	public void setLabel(Key label) {
		this.label = label;
	}

	/**
	 * Used for testing whether this UserSource has been changed by user.
	 * 
	 * @see cz.artique.shared.utils.HasDeepEquals#equalsDeeply(java.lang.Object)
	 */
	public boolean equalsDeeply(UserSource o) {
		return this.equals(o) && SharedUtils.eq(getUserId(), o.getUserId())
			&& SharedUtils.eq(getDefaultLabels(), o.getDefaultLabels())
			&& SharedUtils.eq(getHierarchy(), o.getHierarchy())
			&& SharedUtils.eq(getLabel(), o.getLabel())
			&& SharedUtils.eq(getName(), o.getLabel())
			&& SharedUtils.eq(getSource(), o.getSource());
	}

	/**
	 * @return type of this source
	 */
	public SourceType getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType
	 *            type of this source
	 */
	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * Region may or may not exist only for {@link HTMLSource}s.
	 * No other type of sources supports regions.
	 * 
	 * @return region if exists, null otherwise
	 */
	public Key getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            region for {@link HTMLSource}
	 */
	public void setRegion(Key region) {
		this.region = region;
	}

	/**
	 * @return object of region
	 */
	public Region getRegionObject() {
		return regionObject;
	}

	/**
	 * @param regionObject
	 *            object of region
	 */
	public void setRegionObject(Region regionObject) {
		this.regionObject = regionObject;
	}

	/**
	 * @return auxiliary data used by some {@link CrawlerService}s
	 */
	public Key getCrawlerData() {
		return crawlerData;
	}

	/**
	 * @param crawlerData
	 *            auxiliary data used by some {@link CrawlerService}s
	 */
	public void setCrawlerData(Key crawlerData) {
		this.crawlerData = crawlerData;
	}

	/**
	 * Object of label is set only in a single situation: a new UserSource has
	 * been created and is to be send to client.
	 * 
	 * @return object of label
	 */
	public Label getLabelObject() {
		return labelObject;
	}

	/**
	 * 
	 * Object of label is set only in a single situation: a new UserSource has
	 * been created and is to be send to client.
	 * 
	 * @param labelObject
	 *            object of label
	 */
	public void setLabelObject(Label labelObject) {
		this.labelObject = labelObject;
	}

	/**
	 * @return owner of this UserSource
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            owner of this UserSource
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
