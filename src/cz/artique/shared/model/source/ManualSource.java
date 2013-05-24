package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.users.User;

import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class ManualSource extends Source implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	private User user;

	public ManualSource() {}

	public ManualSource(User user) {
		super(null);
		setUser(user);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getKeyName() {
		String prefix = "MANUAL_SOURCE";
		String userId = user.getUserId();
		return SharedUtils.combineStringParts(prefix, userId);
	}
}
