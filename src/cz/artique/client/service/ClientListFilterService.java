package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

/**
 * The service responsible for {@link ListFilter}s passing and related
 * operations between client and server.
 * 
 * @author Adam Juraszek
 */
@RemoteServiceRelativePath("service.s3gwt")
public interface ClientListFilterService extends RemoteService {

	public enum GetAllListFilters implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetAllListFilters";
		}
	}

	/**
	 * Gets list of all {@link ListFilter} for current user.
	 * 
	 * @return list of all {@link ListFilter}s
	 */
	List<ListFilter> getAllListFilters();

	public enum AddListFilter implements HasIssue {
		LIST_FILTER,
		NAME,
		HIERARCHY,
		EXPORT_ALIAS,
		FILTER_LABELS,
		GENERAL;
		public String enumName() {
			return "AddListFilter";
		}
	}

	/**
	 * Creates a new {@link ListFilter} for current user.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} to be created
	 * @return created {@link ListFilter}
	 * @throws ValidationException
	 *             if validation of the {@link ListFilter} fails
	 */
	ListFilter addListFilter(ListFilter listFilter) throws ValidationException;

	public enum UpdateListFilter implements HasIssue {
		LIST_FILTER,
		NAME,
		HIERARCHY,
		EXPORT_ALIAS,
		FILTER_LABELS,
		GENERAL;
		public String enumName() {
			return "UpdateListFilter";
		}
	}

	/**
	 * Updates existing {@link ListFilter}.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} to be updated
	 * @return updated {@link ListFilter}
	 * @throws ValidationException
	 *             if validation of the {@link ListFilter} fails
	 */
	ListFilter updateListFilter(ListFilter listFilter)
			throws ValidationException;

	public enum DeleteListFilter implements HasIssue {
		LIST_FILTER,
		GENERAL;
		public String enumName() {
			return "DeleteListFilter";
		}
	}

	/**
	 * Deletes existing {@link ListFilter}.
	 * 
	 * @param listFilter
	 *            {@link ListFilter} to be deleted
	 * @throws ValidationException
	 *             if validation of the {@link ListFilter} fails
	 */
	void deleteListFilter(ListFilter listFilter) throws ValidationException;
}
