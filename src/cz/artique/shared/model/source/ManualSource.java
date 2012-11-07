package cz.artique.shared.model.source;

import java.io.Serializable;

import org.slim3.datastore.Model;

import com.google.appengine.api.users.User;

@Model(schemaVersion = 1)
public class ManualSource extends Source implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Owner of this source
	 */
	private User owner;

	public ManualSource() {}

	public ManualSource(User user) {
		super(null);
		setOwner(user);
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

}
