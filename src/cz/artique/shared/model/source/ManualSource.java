package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.source.ManualSourceMeta;
import cz.artique.server.service.SourceType;
import cz.artique.shared.utils.Utils;

@Model(schemaVersion = 1)
public class ManualSource extends Source implements Serializable {

	private static final long serialVersionUID = 1L;

	private User user;

	public ManualSource() {}

	public ManualSource(User user) {
		super(null, null);
		setUser(user);
	}

	@Override
	public Key genKey() {
		String prefix = SourceType.MANUAL.name();
		String userId = user.getUserId();
		return Datastore.createKey(ManualSourceMeta.get(),
			Utils.combineStringParts(prefix, userId));
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
