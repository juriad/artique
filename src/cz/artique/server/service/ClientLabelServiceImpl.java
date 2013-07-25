package cz.artique.server.service;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import cz.artique.client.service.ClientLabelService;
import cz.artique.server.validation.Validator;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.validation.ValidationException;

/**
 * Provides methods which manipulate with {@link Label}s.
 * Methods are defined by communication interface.
 * 
 * @see ClientLabelService
 * @author Adam Juraszek
 * 
 */
public class ClientLabelServiceImpl implements ClientLabelService {

	public List<Label> getAllLabels() {
		String userId = UserService.getCurrentUserId();
		LabelService ls = new LabelService();
		return ls.getAllLabels(userId, null);
	}

	public Label addLabel(Label label) throws ValidationException {
		Validator<AddLabel> validator = new Validator<AddLabel>();
		validator.checkNullability(AddLabel.LABEL, false, label);
		String userId = UserService.getCurrentUserId();
		label.setUserId(userId);
		label.setLabelType(LabelType.USER_DEFINED);
		label.setName(validator.checkLabel(AddLabel.NAME, label.getName()));

		LabelService ls = new LabelService();
		return ls.creatIfNotExist(label);
	}

	public void updateLabels(List<Label> labels) throws ValidationException {
		Validator<UpdateLabels> validator = new Validator<UpdateLabels>();
		validator.checkNullability(UpdateLabels.LABELS, false, labels);
		List<Key> keys = new ArrayList<Key>();
		for (Label label : labels) {
			Key key = label.getKey();
			validator.checkNullability(UpdateLabels.LABEL, false, key);
			keys.add(key);
		}

		String userId = UserService.getCurrentUserId();
		LabelService ls = new LabelService();
		List<Label> labelsByKeys = ls.getLabelsByKeys(keys);
		List<Key> toDelete = new ArrayList<Key>();
		int i = 0;
		for (Label labelByKey : labelsByKeys) {
			Label label = labels.get(i);
			validator.checkUser(UpdateLabels.LABEL, userId,
				labelByKey.getUserId());
			labelByKey.setBackupLevel(label.getBackupLevel());
			labelByKey.setAppearance(label.getAppearance());

			if (label.isToBeDeleted()) {
				toDelete.add(label.getKey());
			}
			i++;
		}
		ls.saveLabels(labelsByKeys);

		ls.deleteLabels(toDelete);
	}
}
