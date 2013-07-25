package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

import cz.artique.client.service.ClientLabelService.UpdateLabels;
import cz.artique.server.meta.item.UserItemMeta;
import cz.artique.server.meta.label.FilterMeta;
import cz.artique.server.meta.label.LabelMeta;
import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.server.utils.KeyGen;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

/**
 * Provides methods which manipulates with entity {@link Label} in
 * database.
 * 
 * @author Adam Juraszek
 * 
 */
public class LabelService {

	public LabelService() {}

	/**
	 * Gets all {@link Label}s for a user, optionally filtered by their type.
	 * 
	 * @param userId
	 *            user the {@link Label}s are gotten for
	 * @param type
	 *            {@link LabelType} the {@link Label}s are filtered to, or null
	 *            to return all {@link Label}s
	 * @return list of {@link Label}s
	 */
	public List<Label> getAllLabels(String userId, LabelType type) {
		LabelMeta meta = LabelMeta.get();
		ModelQuery<Label> query =
			Datastore.query(meta).filter(meta.userId.equal(userId));
		if (type != null) {
			query = query.filter(meta.labelType.equal(type));
		}
		List<Label> labels = query.asList();
		return labels;
	}

	/**
	 * Creates a new {@link Label} if it had not existed.
	 * 
	 * @param label
	 *            {@link Label} to be created
	 * @return new {@link Label} or existing one
	 */
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
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		return theLabel;
	}

	/**
	 * Gets list of {@link Label}s identified by their keys.
	 * 
	 * @param labelKeys
	 *            iterable of keys of {@link Label}s
	 * @return list of {@link Label}s
	 */
	public List<Label> getLabelsByKeys(Iterable<Key> labelKeys) {
		if (labelKeys == null) {
			return new ArrayList<Label>();
		}
		List<Label> labels = Datastore.get(LabelMeta.get(), labelKeys);
		return labels;
	}

	/**
	 * Gets a {@link Label} identified by its key.
	 * 
	 * @param labelKey
	 *            key of {@link Label}
	 * @return {@link Label}
	 */
	public Label getLabelByKey(Key labelKey) {
		if (labelKey == null) {
			return null;
		}
		return getLabelsByKeys(Arrays.asList(labelKey)).get(0);
	}

	/**
	 * Saves an existing {@link Label}s to database.
	 * 
	 * @param labelsByKeys
	 *            list of {@link Label}s to be saved
	 */
	public void saveLabels(List<Label> labelsByKeys) {
		Datastore.put(labelsByKeys);
	}

	/**
	 * Deletes list of {@link Label}s identified by their keys.
	 * 
	 * @param toDelete
	 *            list of {@link Label}s to be deleted
	 * @throws ValidationException
	 *             exception thrown when some {@link Label} cannot be deleted;
	 *             cancels deletion of the rest {@link Label}s
	 */
	public void deleteLabels(List<Key> toDelete) throws ValidationException {
		for (Key key : toDelete) {
			UserSourceMeta usMeta = UserSourceMeta.get();
			List<Key> usKeys =
				Datastore
					.query(usMeta)
					.filter(usMeta.defaultLabels.equal(key))
					.asKeyList();
			if (usKeys != null && usKeys.size() > 0) {
				throw new ValidationException(new Issue<UpdateLabels>(
					UpdateLabels.DELETE_SOURCE, IssueType.ALREADY_EXISTS));
			}

			FilterMeta fMeta = FilterMeta.get();
			List<Key> fKeys =
				Datastore
					.query(fMeta)
					.filter(fMeta.labels.equal(key))
					.asKeyList();
			if (fKeys != null && fKeys.size() > 0) {
				throw new ValidationException(new Issue<UpdateLabels>(
					UpdateLabels.DELETE_FILTER, IssueType.ALREADY_EXISTS));
			}

			UserItemMeta iMeta = UserItemMeta.get();
			List<Key> iKeys =
				Datastore
					.query(iMeta)
					.filter(iMeta.labels.equal(key))
					.asKeyList();
			if (iKeys != null && iKeys.size() > 0) {
				throw new ValidationException(new Issue<UpdateLabels>(
					UpdateLabels.DELETE_ITEM, IssueType.ALREADY_EXISTS));
			}
		}
		Datastore.deleteAsync(toDelete);
	}
}
