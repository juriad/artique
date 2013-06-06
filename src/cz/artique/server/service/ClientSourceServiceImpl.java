package cz.artique.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientSourceService;
import cz.artique.server.utils.KeyGen;
import cz.artique.server.utils.ServerSourceType;
import cz.artique.server.validation.Validator;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.recomandation.Recommendation;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.SourceType;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

public class ClientSourceServiceImpl implements ClientSourceService {

	@SuppressWarnings("unchecked")
	public <E extends Source> E addSource(E source) throws ValidationException {
		Validator<AddSource> validator = new Validator<AddSource>();
		validator.checkNullability(AddSource.SOURCE, false, source);
		validator.checkReachability(AddSource.URL, source.getUrl(), false);
		source.setUsage(0);
		source.setEnabled(false);
		source.setEnqued(false);
		source.setErrorSequence(0);
		source.setLastCheck(null);
		source.setNextCheck(null);
		if (source instanceof ManualSource) {
			throw new ValidationException(new Issue<AddSource>(AddSource.TYPE,
				IssueType.ALREADY_EXISTS));
		}

		SourceService ss = new SourceService();
		ServerSourceType serverSourceType =
			ServerSourceType.get(SourceType.get(source.getClass()));
		return ss.creatIfNotExist(source,
			(ModelMeta<E>) serverSourceType.getMeta());
	}

	public List<Region> getRegions(Key source) throws ValidationException {
		Validator<GetRegions> validator = new Validator<GetRegions>();
		validator.checkNullability(GetRegions.SOURCE, false, source);

		SourceService ss = new SourceService();
		return ss.getAllRegions(source);
	}

	public UserSource addUserSource(UserSource userSource)
			throws ValidationException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		Validator<AddUserSource> validator = new Validator<AddUserSource>();
		validator
			.checkNullability(AddUserSource.USER_SOURCE, false, userSource);
		userSource.setUser(user);
		userSource.setUserId(user.getUserId());
		userSource.setName(validator.checkString(AddUserSource.NAME,
			userSource.getName(), false, false));
		validator.checkNullability(AddUserSource.SOURCE_TYPE, false,
			userSource.getSourceType());
		if (SourceType.MANUAL.equals(userSource.getSourceType())) {
			throw new ValidationException(new Issue<AddUserSource>(
				AddUserSource.SOURCE_TYPE, IssueType.INVALID_VALUE));
		}
		userSource.setCrawlerData(null);
		userSource.setHierarchy(validator.checkString(AddUserSource.HIERARCHY,
			userSource.getHierarchy(), false, false));
		userSource.setLabel(null);
		userSource.setLabelObject(null);
		validator.checkNullability(AddUserSource.SOURCE, false,
			userSource.getSource());

		UserSourceService uss = new UserSourceService();
		uss.fillSources(userSource);

		LabelService ls = new LabelService();
		List<Label> labelsByKeys =
			ls.getLabelsByKeys(userSource.getDefaultLabels());
		for (Label l : labelsByKeys) {
			validator
				.checkUser(AddUserSource.DEFAULT_LABELS, user, l.getUser());
		}

		Region region = userSource.getRegionObject();
		if (userSource.getRegion() != null) {
			uss.fillRegions(userSource);
			if (!userSource
				.getRegionObject()
				.getHtmlSource()
				.equals(userSource.getSource())) {
				throw new ValidationException(new Issue<AddUserSource>(
					AddUserSource.REGION, IssueType.INVALID_VALUE));
			}
		}
		userSource.setRegionObject(region);

		if (region != null) {
			region.setHtmlSource(userSource.getSource());
			region.setName(validator.checkString(AddUserSource.REGION_NAME,
				region.getName(), false, false));
			region.setPositiveSelector(validator.checkSelector(
				AddUserSource.REGION_POSITIVE, region.getPositiveSelector(),
				true));
			region.setNegativeSelector(validator.checkSelector(
				AddUserSource.REGION_NEGATIVE, region.getNegativeSelector(),
				true));
		}

		UserSource userSource2 = uss.createUserSource(userSource);
		if (userSource2 == userSource) {
			uss.fillRegions(userSource);
		} else {
			throw new ValidationException(new Issue<AddUserSource>(
				AddUserSource.USER_SOURCE, IssueType.ALREADY_EXISTS));
		}

		return userSource;
	}

	public UserSource updateUserSource(UserSource userSource)
			throws ValidationException {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		Validator<UpdateUserSource> validator =
			new Validator<UpdateUserSource>();
		validator.checkNullability(UpdateUserSource.USER_SOURCE, false,
			userSource);
		validator.checkNullability(UpdateUserSource.USER_SOURCE, false,
			userSource.getKey());
		userSource.setUser(user);
		userSource.setUserId(user.getUserId());
		validator.checkNullability(UpdateUserSource.SOURCE, false,
			userSource.getSource());
		validator.checkNullability(UpdateUserSource.SOURCE_TYPE, false,
			userSource.getSourceType());
		Key genKey = KeyGen.genKey(userSource);
		if (!genKey.equals(userSource.getKey())) {
			throw new ValidationException(new Issue<UpdateUserSource>(
				UpdateUserSource.USER_SOURCE, IssueType.SECURITY_BREACH));
		}

		UserSourceService uss = new UserSourceService();
		UserSource old = uss.getUserSource(userSource.getKey());
		old.setName(validator.checkString(UpdateUserSource.NAME,
			userSource.getName(), false, false));
		old.setHierarchy(validator.checkString(UpdateUserSource.HIERARCHY,
			userSource.getHierarchy(), false, false));
		old.setWatching(userSource.isWatching());

		LabelService ls = new LabelService();
		List<Key> defaultLabels = userSource.getDefaultLabels();
		List<Label> labelsByKeys = ls.getLabelsByKeys(defaultLabels);
		for (Label l : labelsByKeys) {
			validator.checkUser(UpdateUserSource.DEFAULT_LABELS, user,
				l.getUser());
		}
		if (defaultLabels == null) {
			defaultLabels = new ArrayList<Key>();
		}
		if (!defaultLabels.contains(old.getLabel())) {
			defaultLabels.add(old.getLabel());
		}
		old.setDefaultLabels(defaultLabels);

		Region region = userSource.getRegionObject();
		if (userSource.getRegion() != null) {
			uss.fillRegions(userSource);
			if (!userSource
				.getRegionObject()
				.getHtmlSource()
				.equals(userSource.getSource())) {
				throw new ValidationException(new Issue<UpdateUserSource>(
					UpdateUserSource.REGION, IssueType.INVALID_VALUE));
			}
		}

		if (region != null) {
			region.setHtmlSource(userSource.getSource());
			region.setName(validator.checkString(UpdateUserSource.REGION_NAME,
				region.getName(), false, false));
			region.setPositiveSelector(validator.checkSelector(
				UpdateUserSource.REGION_POSITIVE, region.getPositiveSelector(),
				true));
			region.setNegativeSelector(validator.checkSelector(
				UpdateUserSource.REGION_NEGATIVE, region.getNegativeSelector(),
				true));
		}
		old.setRegionObject(region);

		uss.updateUserSource(old);
		return old;
	}

	public List<UserSource> getUserSources() {
		UserSourceService uss = new UserSourceService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		return uss.getUserSources(user);
	}

	public Region checkRegion(Region region) throws ValidationException {
		Validator<CheckRegion> validator = new Validator<CheckRegion>();
		validator.checkNullability(CheckRegion.REGION, false, region);

		// do not check source

		region.setName(validator.checkString(CheckRegion.REGION_NAME,
			region.getName(), false, false));
		region.setPositiveSelector(validator.checkSelector(
			CheckRegion.REGION_POSITIVE, region.getPositiveSelector(), true));
		region.setNegativeSelector(validator.checkSelector(
			CheckRegion.REGION_NEGATIVE, region.getNegativeSelector(), true));
		return region;
	}

	public Date planSourceCheck(Key key) throws ValidationException {
		Validator<PlanSourceCheck> validator = new Validator<PlanSourceCheck>();
		validator.checkNullability(PlanSourceCheck.SOURCE, false, key);

		SourceService ss = new SourceService();
		Source source = ss.getSourceByKey(key);
		validator.checkNullability(PlanSourceCheck.SOURCE, false, source);
		if (SourceType.MANUAL.equals(SourceType.get(ss.getClass()))) {
			throw new ValidationException(new Issue<PlanSourceCheck>(
				PlanSourceCheck.SOURCE, IssueType.INVALID_VALUE));
		}
		return ss.planSourceCheck(source);
	}

	public Recommendation getRecommendation() {
		RecommendationService rs = new RecommendationService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		return rs.getRecommendation(user); // may return null
	}
}
