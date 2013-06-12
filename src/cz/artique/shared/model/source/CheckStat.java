package cz.artique.shared.model.source;

import java.io.Serializable;
import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * Contains information about all checks performed for each source.
 * Persistent attributes are:
 * <ul>
 * <li>Source - reference to source key
 * <li>ProbeDate - date of check
 * <li>ItemsAcquired - number of items successfully acquired during this check
 * <li>Error - error message describing cause of check failure
 * </ul>
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class CheckStat implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	/**
	 * Relation to source
	 */
	private Key source;

	/**
	 * Date of probe
	 */
	private Date probeDate;

	/**
	 * Count of acquired items
	 * Negative for an error
	 */
	private int itemsAcquired;

	/**
	 * Error message
	 */
	private String error;

	/**
	 * Returns the key.
	 * 
	 * @return the key
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
		CheckStat other = (CheckStat) obj;
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
	 * @return key of source which this stat belongs to
	 */
	public Key getSource() {
		return source;
	}

	/**
	 * @param source
	 *            key of source which this stat belongs to
	 */
	public void setSource(Key source) {
		this.source = source;
	}

	/**
	 * @return date when check has been performed
	 */
	public Date getProbeDate() {
		return probeDate;
	}

	/**
	 * @param probeDate
	 *            date when check has been performed
	 */
	public void setProbeDate(Date probeDate) {
		this.probeDate = probeDate;
	}

	/**
	 * Number of items is negative if an error occurred.
	 * 
	 * @return number of items acquired during this check
	 */
	public int getItemsAcquired() {
		return itemsAcquired;
	}

	/**
	 * @param itemsAcquired
	 *            number of items acquired during this check
	 */
	public void setItemsAcquired(int itemsAcquired) {
		this.itemsAcquired = itemsAcquired;
	}

	/**
	 * Number of items is negative if an error occurred. If check was
	 * successful, the error is null.
	 * 
	 * @return error message if check failed
	 */
	public String getError() {
		return error;
	}

	/**
	 * Sets error message in case of check failure.
	 * 
	 * @param error
	 *            error message
	 */
	public void setError(String error) {
		this.error = error;
	}
}
