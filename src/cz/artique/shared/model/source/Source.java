package cz.artique.shared.model.source;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;

import cz.artique.shared.utils.GenKey;

@Model(schemaVersion = 1)
public abstract class Source implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * URL of this source
	 */
	private Link url;

	/**
	 * Specifies how many users uses this source
	 */
	private int usage;

	/**
	 * Wraps usage (true iff usage>0)
	 */
	private boolean enabled;

	private boolean enqued;

	/**
	 * Planned next check
	 */
	private Date nextCheck;

	/**
	 * How many last checks were errors
	 */
	private int errorSequence;

	private Date lastCheck;

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getErrorSequence() {
		return errorSequence;
	}

	public void setErrorSequence(int errorSequence) {
		this.errorSequence = errorSequence;
	}

	public boolean isEnqued() {
		return enqued;
	}

	public void setEnqued(boolean enqued) {
		this.enqued = enqued;
	}

	public Date getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(Date lastCheck) {
		this.lastCheck = lastCheck;
	}

}
