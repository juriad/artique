package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

/**
 * Non-trivial extension of {@link Source}. Adds attribute user to
 * ManualSource to support queries for ManualSource of concrete user.
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class ManualSource extends Source implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	private String userId;

	/**
	 * Default constructor for slim3 framework.
	 */
	public ManualSource() {}

	public ManualSource(String userId) {
		super(null);
		setUserId(userId);
	}

	/**
	 * Gets id of user which this manual source belongs to. This user is never
	 * null.
	 * 
	 * @return user
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param user
	 *            id of user this source belongs to
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getKeyName() {
		String prefix = "MANUAL_SOURCE";
		String user = getUserId();
		return SharedUtils.combineStringParts(prefix, user);
	}
}
