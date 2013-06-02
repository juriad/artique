package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.label.Label;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientLabelService extends RemoteService {

	public enum GetAllLabels implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetAllLabels";
		}
	}

	List<Label> getAllLabels();

	public enum AddLabel implements HasIssue {
		LABEL,
		NAME,
		GENERAL;
		public String enumName() {
			return "AddLabel";
		}
	}

	Label addLabel(Label label) throws ValidationException;

	public enum UpdateLabels implements HasIssue {
		LABELS,
		LABEL,
		DELETE_SOURCE,
		DELETE_FILTER,
		DELETE_ITEM,
		GENERAL;
		public String enumName() {
			return "UpdateLabels";
		}
	}

	void updateLabels(List<Label> labels) throws ValidationException;

}
