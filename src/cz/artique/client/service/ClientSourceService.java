package cz.artique.client.service;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.recomandation.Recommendation;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;

/**
 * The service responsible for {@link Source}s, {@link UserSource}s,
 * {@link Region}s and {@link Recommendation}s passing and related
 * operations between client and server.
 * 
 * @author Adam Juraszek
 * 
 */
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

	/**
	 * Creates a new {@link Source} if it does not exist yet or gets an existing
	 * one.
	 * 
	 * @param source
	 *            Source to be potentially created
	 * @return Created or existing {@link Source}
	 * @throws ValidationException
	 *             if validation of the {@link Source} fails
	 */
	<E extends Source> E addSource(E source) throws ValidationException;

	public enum GetRegions implements HasIssue {
		SOURCE,
		GENERAL;
		public String enumName() {
			return "GetRegions";
		}
	}

	/**
	 * Gets list of existing {@link Region}s for {@link HTMLSource}.
	 * 
	 * @param source
	 *            key of {@link HTMLSource} which are the {@link Region}s gotten
	 *            for
	 * @return list of {@link Region}s
	 * @throws ValidationException
	 *             if validation of the {@link Source} fails
	 */
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

	/**
	 * Creates a new {@link UserSource} for current user.
	 * 
	 * @param userSource
	 *            {@link UserSource} to be created
	 * @return created {@link UserSource}
	 * @throws ValidationException
	 *             if validation of the {@link UserSource} fails
	 */
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

	/**
	 * Updates an existing {@link UserSource}.
	 * 
	 * @param userSource
	 *            {@link UserSource} to be updated
	 * @return updated {@link UserSource}
	 * @throws ValidationException
	 *             if validation of the {@link UserSource} fails
	 */
	UserSource updateUserSource(UserSource userSource)
			throws ValidationException;

	public enum GetUserSources implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetUserSources";
		}
	}

	/**
	 * Gets list of all existing {@link UserSource}s for current user.
	 * 
	 * @return list of all {@link UserSource}s
	 */
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

	/**
	 * Checks whether the {@link Region} is valid
	 * 
	 * @param region
	 *            {@link Region} to be checked
	 * @return normalized {@link Region} if it was valid
	 * @throws ValidationException
	 *             if validation of the {@link Region} fails
	 */
	Region checkRegion(Region region) throws ValidationException;

	public enum PlanSourceCheck implements HasIssue {
		SOURCE,
		GENERAL;
		public String enumName() {
			return "PlanSourceCheck";
		}
	}

	/**
	 * Plans immediate check of specified {@link Source}.
	 * 
	 * @param source
	 *            key of {@link Source} to be checked
	 * @return date of planned check
	 * @throws ValidationException
	 *             if validation of the {@link Source} fails
	 */
	Date planSourceCheck(Key source) throws ValidationException;

	public enum GetRecommendation implements HasIssue {
		GENERAL;
		public String enumName() {
			return "GetRecommendation";
		}
	}

	/**
	 * Gets {@link Recommendation} (list of recommended {@link Source}s) for
	 * current
	 * user. The {@link Recommendation} may be null or empty.
	 * 
	 * @return {@link Recommendation} of {@link Source}s
	 */
	Recommendation getRecommendation();

}
