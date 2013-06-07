package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class ManualSource extends Source implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	private String userId;

	public ManualSource() {}

	public ManualSource(String userId) {
		super(null);
		setUserId(userId);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getKeyName() {
		String prefix = "MANUAL_SOURCE";
		String userId = getUserId();
		return SharedUtils.combineStringParts(prefix, userId);
	}
}
