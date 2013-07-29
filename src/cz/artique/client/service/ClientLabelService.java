package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.label.Label;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

/**
 * The service responsible for {@link Label}s passing and related operations
 * between client and server.
 * 
 * @author Adam Juraszek
 */
@RemoteServiceRelativePath("service.s3gwt")
public interface ClientLabelService extends RemoteService {

	public enum GetAllLabels implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetAllLabels";
		}
	}

	/**
	 * Gets list of all {@link Label}s (no matter of their type) of the current
	 * user.
	 * 
	 * @return list of all {@link Label}s
	 */
	List<Label> getAllLabels();

	public enum AddLabel implements HasIssue {
		LABEL,
		NAME,
		GENERAL;
		public String enumName() {
			return "AddLabel";
		}
	}

	/**
	 * Creates a new user-defined {@link Label} for the current user.
	 * 
	 * @param label
	 *            {@link Label} to be created
	 * @return created {@link Label}
	 * @throws ValidationException
	 *             if validation of the {@link Label} fails
	 */
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

	/**
	 * Updates appearance and behavior of set of {@link Label}s or possibly
	 * deletes
	 * the {@link Label} marked to be deleted if they are unused.
	 * 
	 * @param labels
	 *            {@link Label} which are changed or marked to be deleted
	 * @throws ValidationException
	 *             if validation of the list of {@link Label}s fails
	 */
	void updateLabels(List<Label> labels) throws ValidationException;

}
