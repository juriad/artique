package cz.artique.shared.model.config.client;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

/**
 * Configuration option which contains client configuration specific for each
 * user.
 * 
 * @author Adam Juraszek
 * @see cz.artique.shared.model.config
 * 
 */
@Model(schemaVersion = 1)
public class ClientConfig implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	private String configKey;

	@Attribute(unindexed = true)
	private int intValue;

	@Attribute(unindexed = true)
	private double doubleValue;

	@Attribute(unindexed = true)
	private String stringValue;

	private String userId;

	/**
	 * Default constructor.
	 */
	public ClientConfig() {}

	/**
	 * Sets config key and owner of this config option.
	 * 
	 * @param userId
	 *            owner of this option
	 * @param configKey
	 *            configuration key
	 */
	public ClientConfig(String userId, String configKey) {
		this.userId = userId;
		this.configKey = configKey;
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
		ClientConfig other = (ClientConfig) obj;
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
	 * @return configuration key
	 */
	public String getConfigKey() {
		return configKey;
	}

	/**
	 * @param configKey
	 *            configuration key
	 */
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	/**
	 * @return integer value
	 */
	public int getIntValue() {
		return intValue;
	}

	/**
	 * @param intValue
	 *            integer value
	 */
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	/**
	 * @return double value
	 */
	public double getDoubleValue() {
		return doubleValue;
	}

	/**
	 * @param doubleValue
	 *            double value
	 */
	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	/**
	 * @return string value
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * @param stringValue
	 *            string value
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getKeyName() {
		return SharedUtils.combineStringParts(getUserId(), getConfigKey());
	}

	/**
	 * @return owner of this option
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            owner of this option
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
