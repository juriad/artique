package cz.artique.shared.model.source;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;

import cz.artique.shared.utils.GenKey;

/**
 * Source class represents an abstract source. It is an ancestor for four types
 * of sources:
 * <ul>
 * <li> {@link XMLSource} - processes any RSS or Atom
 * <li> {@link PageChangeSource} - watches changes in text on a web page
 * <li> {@link WebSiteSource} - searches for new anchors on web page
 * <li> {@link ManualSource} - contains only manually added items
 * </ul>
 * 
 * <p>
 * Source is generally not related to any user. It is just abstraction of URL;
 * many users may watch the same source.
 * 
 * <p>
 * Source contains several persistent attributes:
 * <ul>
 * <li>Url - URL of this source; it is non-null for all sources except for
 * {@link ManualSource}
 * <li>Usage, enabled - usage represents number of active {@link UserSource}s;
 * source is enabled iff {@code Source#getUsage()>0}.
 * <li>NextCheck, lastCheck, errorSequence - timestamps of next planned or last
 * performed check; errorSequence counts number of errors since last successful
 * check
 * <li>Enqued - lock preventing simultaneous checks of one source
 * </ul>
 * Each check saves information about its success or failure to datastore as
 * {@link CheckStat}.
 * 
 * @author Adam Juraszek
 * @see SourceType
 * @see UserSource
 * 
 */
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

	/**
	 * Default constructor for slim3 framework.
	 */
	public Source() {}

	/**
	 * Constructor meant to be called by subclasses.
	 * 
	 * @param url
	 *            URL of the source
	 */
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
	 * @return key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * URL is null only for {@link ManualSource}, in all other cases it is
	 * non-null.
	 * 
	 * @return URL of source
	 */
	public Link getUrl() {
		return url;
	}

	/**
	 * @return number of active {@link UserSource}s associated with this source
	 */
	public int getUsage() {
		return usage;
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
	 * @param key
	 *            key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * URL must be null for {@link ManualSource} and non-null for all other
	 * sources.
	 * 
	 * @param url
	 *            URL of source
	 */
	public void setUrl(Link url) {
		this.url = url;
	}

	/**
	 * @param usage
	 *            number of active {@link UserSource}s associated with this
	 *            source
	 */
	public void setUsage(int usage) {
		this.usage = usage;
	}

	/**
	 * @param version
	 *            version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * NextCheck is never null.
	 * 
	 * @return date of planned next check
	 */
	public Date getNextCheck() {
		return nextCheck;
	}

	/**
	 * Next check may never be null.
	 * 
	 * @param nextCheck
	 *            date of planned next check
	 */
	public void setNextCheck(Date nextCheck) {
		this.nextCheck = nextCheck;
	}

	/**
	 * Source is enabled iff {@code Source#getUsage()>0}.
	 * 
	 * @return true if source is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Source is enabled iff {@code Source#getUsage()>0}.
	 * 
	 * @param enabled
	 *            whether source is enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return number of errors since last successful check
	 */
	public int getErrorSequence() {
		return errorSequence;
	}

	/**
	 * @param errorSequence
	 *            number of errors since last successful check
	 */
	public void setErrorSequence(int errorSequence) {
		this.errorSequence = errorSequence;
	}

	/**
	 * Prevents planning several check tasks for the same source.
	 * 
	 * @return true if there exists planned check task, false otherwise
	 */
	public boolean isEnqued() {
		return enqued;
	}

	/**
	 * Prevents planning several check tasks for the same source.
	 * 
	 * @param enqued
	 *            true if there exists planned check task, false otherwise
	 */
	public void setEnqued(boolean enqued) {
		this.enqued = enqued;
	}

	/**
	 * @return date of last check, null if no check has been performed
	 */
	public Date getLastCheck() {
		return lastCheck;
	}

	/**
	 * @param lastCheck
	 *            date of last check
	 */
	public void setLastCheck(Date lastCheck) {
		this.lastCheck = lastCheck;
	}

}
