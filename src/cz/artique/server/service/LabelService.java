package cz.artique.server.service;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.label.LabelMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.label.Label;

public class LabelService {

	public LabelService() {}

	public List<Label> getAllLabels(User user) {
		LabelMeta meta = LabelMeta.get();
		List<Label> labels =
			Datastore.query(meta).filter(meta.user.equal(user)).asList();
		return labels;
	}

	public Label creatIfNotExist(Label label) {
		Key key = ServerUtils.genKey(label);
		LabelMeta meta = LabelMeta.get();
		Label theLabel = Datastore.getOrNull(meta, key);
		if (theLabel == null) {
			label.setKey(key);
			Datastore.put(label);
			theLabel = label;
		}
		return theLabel;
	}

	public List<Label> getLabelsByKeys(Iterable<Key> labelKeys) {
		if (labelKeys == null) {
			return new ArrayList<Label>();
		}
		List<Label> labels = Datastore.get(LabelMeta.get(), labelKeys);
		return labels;
	}

	public Label getLabelsByKeys(Key labelKey) {
		if (labelKey == null) {
			return null;
		}
		return getLabelsByKeys(labelKey);
	}
}
