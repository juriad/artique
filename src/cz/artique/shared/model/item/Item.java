package cz.artique.shared.model.item;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import cz.artique.client.listing.ArtiqueList;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;

/**
 * Item represents one item shown in {@link ArtiqueList}, this item has no
 * relation to {@link UserInfo}, it is only related to the {@link Source} it
 * comes from.
 * 
 * <p>
 * Type hierarchy of items reflects type hierarchy of {@link Source}s. For each
 * source type, there exists one {@link Item} type.
 * <ul>
 * <li> {@link ArticleItem} - provided by {@link XMLSource}
 * <li> {@link PageChangeItem} - provided by {@link PageChangeSource}
 * <li> {@link LinkItem} - provided by {@link WebSiteSource}
 * <li> {@link ManualItem} - items of {@link ManualSource}
 * </ul>
 * 
 * <p>
 * Like the sources are shared among users, the items are shared as well. There
 * always exists only a single item representing an article or link in the
 * system for each source. For information about personalization of items see
 * {@link UserItem}.
 * 
 * <p>
 * Each item has following attributes:
 * <ul>
 * <li>Source - the source the item comes from or belongs to
 * <li>Url - URL of original location of the web-page the item represents; see
 * {@link #getUrl()} for more information
 * <li>Title - title of the item; see {@link #getTitle()} for more information
 * <li>Added - time-stamp representing the date when the item was created in
 * system
 * <li>Content - plain text or html content of item; see {@link #getContent()}
 * for more information
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public abstract class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * Source which contains this item
	 */
	private Key source;

	/**
	 * URL of this item
	 */
	@Attribute(unindexed = true)
	private Link url;

	/**
	 * Title of this item
	 */
	@Attribute(unindexed = true)
	private String title;

	/**
	 * When the item was added to system
	 */
	@Attribute(unindexed = true)
	private Date added;

	/**
	 * Content of item
	 */
	@Attribute(unindexed = true)
	private Text content;

	/**
	 * Type of content
	 */
	@Attribute(unindexed = true)
	private ContentType contentType;

	/**
	 * Hash of this item; used for comparison
	 */
	private String hash;

	/**
	 * Default constructor for slim3 framework.
	 */
	public Item() {}

	/**
	 * Constructor which sets added attribute - the date when the item was added
	 * to system and the source the item comes from.
	 * 
	 * @param source
	 */
	public Item(Source source) {
		setAdded(new Date());
		setSource(source.getKey());
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
		Item other = (Item) obj;
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
	 * @return date of creation of the item in system
	 */
	public Date getAdded() {
		return added;
	}

	/**
	 * Content of item may be plain text or html, use {@link #getContentType()}
	 * to distinguish between these two.
	 * 
	 * <ul>
	 * <li>Content gathered from RSS/Atom for {@link ArticleItem}
	 * <li>Text content of new version of page for {@link PageChangeItem}
	 * <li>Text content of immediate parent element of link for {@link LinkItem}
	 * <li>User defined content for {@link ManualItem}
	 * </ul>
	 * 
	 * @return content of item, null if content was not provided
	 */
	public Text getContent() {
		return content;
	}

	/**
	 * @return key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @return source this item comes from
	 */
	public Key getSource() {
		return source;
	}

	/**
	 * Title may never be null. Its value is:
	 * <ul>
	 * <li>Title gathered from RSS/Atom for {@link ArticleItem}
	 * <li>Generic title for {@link PageChangeItem}
	 * <li>Value of anchor for {@link LinkItem}
	 * <li>Title of page possibly changed by user for {@link ManualItem}
	 * </ul>
	 * 
	 * @return title of item
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <ul>
	 * <li>Link gathered from RSS/Atom for {@link ArticleItem}
	 * <li>URL of source for {@link PageChangeItem}
	 * <li>Href of anchor for {@link LinkItem}
	 * <li>URL of web-page possibly changed by user for {@link ManualItem}
	 * </ul>
	 * 
	 * @return URL to original location of item
	 */
	public Link getUrl() {
		return url;
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
	 * @param added
	 *            date of creation of the item in system
	 */
	public void setAdded(Date added) {
		this.added = added;
	}

	/**
	 * Use {@link ContentType} to distinguish between plain text and html
	 * 
	 * @param content
	 *            content of this item, null if content is not provided
	 */
	public void setContent(Text content) {
		this.content = content;
	}

	/**
	 * @param key
	 *            key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @param source
	 *            the source this item comes from
	 */
	public void setSource(Key source) {
		this.source = source;
	}

	/**
	 * @param title
	 *            title of the item, may not be null
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param url
	 *            URL to original location of item
	 */
	public void setUrl(Link url) {
		this.url = url;
	}

	/**
	 * @param version
	 *            version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return hash code used for determination whether there already exists
	 *         such item
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash
	 *            hash code used for determination whether there already exists
	 *            such item
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * <ul>
	 * <li>Gathered {@link ContentType#PLAIN_TEXT} or {@link ContentType#HTML}
	 * from RSS/Atom or guessed from content for {@link ArticleItem}
	 * <li>{@link ContentType#PLAIN_TEXT} for {@link PageChangeItem}
	 * <li>{@link ContentType#PLAIN_TEXT} for {@link WebSiteSource}
	 * <li>{@link ContentType#PLAIN_TEXT} for {@link ManualItem}
	 * 
	 * @return MIME type of content of this item
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * See {@link #getContentType()} for more information.
	 * 
	 * @param contentType
	 *            MIME type of content of this item
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

}
