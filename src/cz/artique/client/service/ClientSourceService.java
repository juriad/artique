package cz.artique.client.service;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.recomandation.Recommendation;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

@RemoteServiceRelativePath("service.s3gwt")
public interface ClientSourceService extends RemoteService {

	public enum AddSource implements HasIssue {
		SOURCE,
		URL,
		TYPE,
		GENERAL;
		public String enumName() {
			return "AddSource";
		}
	}

	<E extends Source> E addSource(E source) throws ValidationException;

	public enum GetRegions implements HasIssue {
		SOURCE,
		GENERAL;
		public String enumName() {
			return "GetRegions";
		}
	}

	List<Region> getRegions(Key source) throws ValidationException;

	public enum AddUserSource implements HasIssue {
		USER_SOURCE,
		NAME,
		SOURCE_TYPE,
		HIERARCHY,
		SOURCE,
		DEFAULT_LABELS,
		REGION,
		REGION_NAME,
		REGION_POSITIVE,
		REGION_NEGATIVE,
		GENERAL;
		public String enumName() {
			return "AddUserSource";
		}
	}

	UserSource addUserSource(UserSource userSource) throws ValidationException;

	public enum UpdateUserSource implements HasIssue {
		USER_SOURCE,
		NAME,
		SOURCE_TYPE,
		HIERARCHY,
		SOURCE,
		DEFAULT_LABELS,
		REGION,
		REGION_NAME,
		REGION_POSITIVE,
		REGION_NEGATIVE,
		GENERAL;
		public String enumName() {
			return "UpdateUserSource";
		}
	}

	UserSource updateUserSource(UserSource userSource)
			throws ValidationException;

	public enum GetUserSources implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetUserSources";
		}
	}

	List<UserSource> getUserSources();

	public enum CheckRegion implements HasIssue {
		REGION,
		REGION_NAME,
		REGION_POSITIVE,
		REGION_NEGATIVE,
		GENERAL;
		public String enumName() {
			return "CheckRegion";
		}
	}

	Region checkRegion(Region region) throws ValidationException;

	public enum PlanSourceCheck implements HasIssue {
		SOURCE,
		GENERAL;
		public String enumName() {
			return "PlanSourceCheck";
		}
	}

	Date planSourceCheck(Key source) throws ValidationException;

	public enum GetRecommendation implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetRecommendation";
		}
	}

	Recommendation getRecommendation();

}
