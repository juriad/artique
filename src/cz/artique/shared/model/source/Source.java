package cz.artique.shared.model.source;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

@Model(schemaVersion = 1)
public abstract class Source implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * URL of this source; this must be unique
	 */
	private Link url;
	
	/**
	 * Specifies how many users uses this source
	 */
	private int usage;

	/**
	 * Last change on this source
	 */
	private Date lastChange;

	/**
	 * Content of source used for comparison
	 */
	private Text lastContent;

	/**
	 * Planned next check
	 */
	private Date nextCheck;

	public Source() {}

	protected Source(Link url) {
		setUrl(url);
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
		Source other = (Source) obj;
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
	 * Returns the key.
	 * 
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	public Text getLastContent() {
		return lastContent;
	}

	public Date getLastChange() {
		return lastChange;
	}

	public Link getUrl() {
		return url;
	}

	public int getUsage() {
		return usage;
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

	/**
	 * Sets the key.
	 * 
	 * @param key
	 *            the key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	public void setLastContent(Text lastContent) {
		this.lastContent = lastContent;
	}

	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}

	public void setUrl(Link url) {
		this.url = url;
	}

	public void setUsage(int usage) {
		this.usage = usage;
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

	public Date getNextCheck() {
		return nextCheck;
	}

	public void setNextCheck(Date nextCheck) {
		this.nextCheck = nextCheck;
	}
	
}
