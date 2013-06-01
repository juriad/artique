package cz.artique.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientListFilterService extends RemoteService {

	public enum GetAllListFilters implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetAllListFilters";
		}
	}

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

	ListFilter updateListFilter(ListFilter listFilter)
			throws ValidationException;

	public enum DeleteListFilter implements HasIssue {
		LIST_FILTER,
		GENERAL;
		public String enumName() {
			return "DeleteListFilter";
		}
	}

	void deleteListFilter(ListFilter listFilter) throws ValidationException;
}
