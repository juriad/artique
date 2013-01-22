package cz.artique.shared.model.user;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.shared.model.user.Config;
import cz.artique.shared.utils.GenKey;
import cz.artique.shared.utils.SharedUtils;

@Model(schemaVersion = 1)
public class ClientConfig extends Config
		implements Serializable, GenKey, DefaultValue {

	private static final long serialVersionUID = 1L;

	private User user;

	public ClientConfig() {}

	public ClientConfig(String configKey, User user) {
		super(configKey);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Key getKeyParent() {
		return null;
	}

	public String getKeyName() {
		String prefix = "CLIENT-CONFIG";
		String userId = getUser().getUserId();
		return SharedUtils.combineStringParts(prefix, userId, getConfigKey());
	}

}
