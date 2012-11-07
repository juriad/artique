package cz.artique.shared.model.item;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import cz.artique.shared.model.source.Source;

@Model(schemaVersion = 1)
public class Item implements Serializable {

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
	private Link url;

	/**
	 * Title of this item
	 */
	private String title;

	/**
	 * When the item was added to system
	 */
	private Date added;

	/**
	 * Content of item
	 */
	private Text content;
	
	/**
	 * Type of content
	 */
	private ContentType contentType;

	/**
	 * Type of item
	 */
	private ItemType itemType;
	
	/**
	 * Hash of this item; used for comparison
	 */
	private String hash;

	public Item() {}

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

	public Date getAdded() {
		return added;
	}

	public Text getContent() {
		return content;
	}

	public ItemType getItemType() {
		return itemType;
	}

	/**
	 * Returns the key.
	 * 
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	public Key getSource() {
		return source;
	}

	public String getTitle() {
		return title;
	}

	public Link getUrl() {
		return url;
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

	public void setAdded(Date added) {
		this.added = added;
	}

	public void setContent(Text content) {
		this.content = content;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
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

	public void setSource(Key source) {
		this.source = source;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(Link url) {
		this.url = url;
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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

}
