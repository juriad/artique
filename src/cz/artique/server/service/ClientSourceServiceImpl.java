package cz.artique.server.service;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientSourceService;
import cz.artique.server.meta.source.HTMLSourceMeta;
import cz.artique.server.meta.source.PageChangeSourceMeta;
import cz.artique.server.meta.source.WebSiteSourceMeta;
import cz.artique.server.meta.source.XMLSourceMeta;
import cz.artique.shared.model.source.HTMLSource;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
import cz.artique.shared.model.source.RegionType;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.WebSiteSource;
import cz.artique.shared.model.source.XMLSource;
import cz.artique.shared.utils.PropertyEmptyException;
import cz.artique.shared.utils.PropertyValueException;

public class ClientSourceServiceImpl implements ClientSourceService {

	public XMLSource addSource(XMLSource source)
			throws PropertyValueException, PropertyEmptyException,
			NullPointerException {
		sourceCheck(source);
		Sanitizer.expectValue("parent", source.getParent(), null);
		SourceService ss = new SourceService();
		return ss.creatIfNotExist(source, XMLSourceMeta.get());
	}

	public HTMLSource addSource(HTMLSource source)
			throws PropertyValueException, PropertyEmptyException,
			NullPointerException {
		sourceCheck(source);
		Sanitizer.expectValue("parent", source.getParent(), null);
		SourceService ss = new SourceService();
		return ss.creatIfNotExist(source, HTMLSourceMeta.get());
	}

	public PageChangeSource addSource(PageChangeSource source)
			throws PropertyValueException, PropertyEmptyException,
			NullPointerException {
		sourceCheck(source);
		if (source.getParent() == null) {
			throw new PropertyEmptyException("parent");
		}
		SourceService ss = new SourceService();
		Source parentObject = ss.getSourceByKey(source.getParent());
		if (parentObject == null) {
			throw new PropertyValueException("parent", "null", "does not exist");
		}
		source.setParentObject(parentObject);

		if (source.getRegion() == null) {
			if (source.getRegionObject() == null) {
				throw new PropertyEmptyException("region");
			} else {
				Region regionObject = source.getRegionObject();
				regionObject.setHtmlSource(source.getParent());
				regionObject.setType(RegionType.PAGE_CHANGE);

				Region theRegion =
					ss.addRegionIfNotExist(source.getRegionObject());
				source.setRegion(theRegion.getKey());
			}
		} else {
			Region regionObject = ss.getRegionByKey(source.getRegion());
			if (regionObject == null) {
				throw new PropertyValueException("region", "null",
					"does not exist");
			}
			if (!regionObject.getHtmlSource().equals(source.getParent())) {
				throw new PropertyValueException("region", "", "wrong source");
			}
			if (!regionObject.getType().equals(RegionType.PAGE_CHANGE)) {
				throw new PropertyValueException("region", regionObject
					.getType()
					.name(), "wrong type");
			}
			source.setRegionObject(regionObject);
		}
		return ss.creatIfNotExist(source, PageChangeSourceMeta.get());
	}

	public WebSiteSource addSource(WebSiteSource source)
			throws PropertyValueException, PropertyEmptyException,
			NullPointerException {
		sourceCheck(source);
		if (source.getParent() == null) {
			throw new PropertyEmptyException("parent");
		}
		SourceService ss = new SourceService();
		Source parentObject = ss.getSourceByKey(source.getParent());
		if (parentObject == null) {
			throw new PropertyValueException("parent", "null", "does not exist");
		}
		source.setParentObject(parentObject);

		if (source.getRegion() == null) {
			if (source.getRegionObject() == null) {
				throw new PropertyEmptyException("region");
			} else {
				Region regionObject = source.getRegionObject();
				regionObject.setHtmlSource(source.getParent());
				regionObject.setType(RegionType.WEB_SITE);

				Region theRegion =
					ss.addRegionIfNotExist(source.getRegionObject());
				source.setRegion(theRegion.getKey());
			}
		} else {
			Region regionObject = ss.getRegionByKey(source.getRegion());
			if (regionObject == null) {
				throw new PropertyValueException("region", "null",
					"does not exist");
			}
			if (!regionObject.getHtmlSource().equals(source.getParent())) {
				throw new PropertyValueException("region", "", "wrong source");
			}
			if (!regionObject.getType().equals(RegionType.WEB_SITE)) {
				throw new PropertyValueException("region", regionObject
					.getType()
					.name(), "wrong type");
			}
			source.setRegionObject(regionObject);
		}
		return ss.creatIfNotExist(source, WebSiteSourceMeta.get());
	}

	private void sourceCheck(Source source)
			throws PropertyEmptyException, PropertyValueException,
			NullPointerException {
		if (source == null) {
			throw new NullPointerException();
		}
		Sanitizer.checkUrl("url", source.getUrl());
		Sanitizer.expectValue("usage", source.getUsage(), 0);
		Sanitizer.expectValue("enabled", source.isEnabled(), false);
		Sanitizer.expectValue("enqued", source.isEnqued(), false);
		Sanitizer.expectValue("errorSequence", source.getErrorSequence(), 0);
		Sanitizer.expectValue("lastCheck", source.getLastCheck(), null);
		Sanitizer.expectValue("nextCheck", source.getNextCheck(), null);
	}

	public List<Region> getRegions(HTMLSource source, RegionType type)
			throws NullPointerException {
		if (source == null) {
			throw new NullPointerException();
		}
		SourceService ss = new SourceService();
		return ss.getRegions(source, type);
	}

	public UserSource addUserSource(UserSource userSource) {
		if (userSource == null) {
			throw new NullPointerException();
		}
		Sanitizer.checkUser("user", userSource.getUser());
		Sanitizer.checkStringLength("name", userSource.getName());
		Sanitizer.checkStringEmpty("name", userSource.getName());
		SourceService ss = new SourceService();
		Source sourceObject = ss.getSourceByKey(userSource.getSource());
		if (sourceObject == null) {
			throw new PropertyValueException("source", "null", "does not exist");
		}
		userSource.setSourceObject(sourceObject);
		// TODO sanitize default labels
		UserSourceService uss = new UserSourceService();
		return uss.creatIfNotExist(userSource);
	}

	public void updateUserSource(UserSource userSource) {
		if (userSource == null) {
			throw new NullPointerException();
		}
		Sanitizer.checkUser("user", userSource.getUser());
		Sanitizer.checkStringLength("name", userSource.getName());
		Sanitizer.checkStringEmpty("name", userSource.getName());
		Sanitizer.checkPreserveKey(userSource);
		// TODO sanitize default labels
		UserSourceService uss = new UserSourceService();
		uss.updateUserSource(userSource);
	}

	public List<UserSource> getUserSources() {

		UserSourceService uss = new UserSourceService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		return uss.getUserSources(user);
	}

}
