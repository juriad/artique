package cz.artique.server.service;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Selector;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import cz.artique.client.service.ClientSourceService;
import cz.artique.server.meta.source.PageChangeSourceMeta;
import cz.artique.server.meta.source.WebSiteSourceMeta;
import cz.artique.server.meta.source.XMLSourceMeta;
import cz.artique.shared.model.source.PageChangeSource;
import cz.artique.shared.model.source.Region;
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
		checkSouce(source);
		SourceService ss = new SourceService();
		return ss.creatIfNotExist(source, XMLSourceMeta.get());
	}

	public PageChangeSource addSource(PageChangeSource source)
			throws PropertyValueException, PropertyEmptyException,
			NullPointerException {
		checkSouce(source);
		SourceService ss = new SourceService();
		return ss.creatIfNotExist(source, PageChangeSourceMeta.get());
	}

	public WebSiteSource addSource(WebSiteSource source)
			throws PropertyValueException, PropertyEmptyException,
			NullPointerException {
		checkSouce(source);
		SourceService ss = new SourceService();
		return ss.creatIfNotExist(source, WebSiteSourceMeta.get());
	}

	private void checkSouce(Source source)
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

	public List<Region> getRegions(Key source) throws NullPointerException {
		if (source == null) {
			throw new NullPointerException();
		}
		SourceService ss = new SourceService();
		return ss.getRegions(source);
	}

	public UserSource addUserSource(UserSource userSource)
			throws NullPointerException, PropertyEmptyException,
			PropertyValueException {
		if (userSource == null) {
			throw new NullPointerException();
		}
		Sanitizer.checkUser("user", userSource.getUser());
		Sanitizer.checkStringLength("name", userSource.getName());
		Sanitizer.checkStringEmpty("name", userSource.getName());
		if (userSource.getSourceType() == null) {
			throw new PropertyValueException("source type", "null",
				"is not set");
		}
		SourceService ss = new SourceService();
		Source sourceObject = ss.getSourceByKey(userSource.getSource());
		if (sourceObject == null) {
			throw new PropertyValueException("source", "null", "does not exist");
		}
		userSource.setSourceObject(sourceObject);
		// TODO sanitize default labels

		// TODO regions
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

		// TODO regions
		UserSourceService uss = new UserSourceService();
		uss.updateUserSource(userSource);
	}

	public List<UserSource> getUserSources() {
		UserSourceService uss = new UserSourceService();
		User user = UserServiceFactory.getUserService().getCurrentUser();
		return uss.getUserSources(user);
	}

	public boolean checkRegion(Region region) {
		if (region.getPositiveSelector() != null) {
			if (!region.getPositiveSelector().trim().isEmpty()) {
				try {
					Selector.select(region.getPositiveSelector(), new Element(
						Tag.valueOf("html"), ""));
				} catch (Exception e) {
					return false;
				}
			}
		}

		if (region.getPositiveSelector() != null) {
			if (!region.getNegativeSelector().trim().isEmpty()) {
				try {
					Selector.select(region.getNegativeSelector(), new Element(
						Tag.valueOf("html"), ""));
				} catch (Exception e) {
					return false;
				}
			}
		}
		return true;

	}
}
