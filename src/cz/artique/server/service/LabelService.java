package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.label.LabelMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.utils.TransactionException;

public class LabelService {

	public LabelService() {}

	public List<Label> getAllLabels(User user) {
		LabelMeta meta = LabelMeta.get();
		ModelQuery<Label> query =
			Datastore.query(meta).filter(meta.user.equal(user));
		List<Label> labels = query.asList();
		return labels;
	}

	public Label creatIfNotExist(Label label) {
		Key key = KeyGen.genKey(label);
		LabelMeta meta = LabelMeta.get();
		Transaction tx = Datastore.beginTransaction();
		Label theLabel;
		try {
			theLabel = Datastore.getOrNull(tx, meta, key);
			if (theLabel == null) {
				label.setKey(key);
				Datastore.put(tx, label);
				theLabel = label;
			}
			tx.commit();
		} catch (Exception e) {
			throw new TransactionException();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
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

	public Label getLabelByKey(Key labelKey) {
		if (labelKey == null) {
			return null;
		}
		return getLabelsByKeys(Arrays.asList(labelKey)).get(0);
	}
}
