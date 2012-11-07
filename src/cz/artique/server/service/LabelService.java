package cz.artique.server.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.label.LabelMeta;
import cz.artique.shared.model.label.Label;
import cz.artique.utils.SourceException;

public class LabelService {
	private final User user;

	public LabelService(User user) {
		this.user = user;
	} 

	public List<Label> getAllLabels() {
		LabelMeta meta = LabelMeta.get();
		List<Label> labels =
			Datastore.query(meta).filter(meta.user.equal(user)).asList();
		return labels;
	}

	public Label getLabelOrCreate(String name) {
		LabelMeta meta = LabelMeta.get();
		List<Label> labels =
			Datastore
				.query(meta)
				.filter(meta.user.equal(user))
				.filter(meta.name.equal(name))
				.asList();
		if (labels.size() == 0) {
			Label l = new Label(user, name);
			Key key = Datastore.put(l);
			l.setKey(key);
			return l;
		} else if (labels.size() == 1) {
			return labels.get(0);
		} else {
			throw new SourceException(
				"Two labels with the same name for one user");
		}
	}
}
