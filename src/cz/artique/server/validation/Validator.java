package cz.artique.server.validation;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Selector;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import cz.artique.server.crawler.CrawlerException;
import cz.artique.server.crawler.Fetcher;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

/**
 * Used to validate data received through client services. This class can check
 * following types:
 * <ul>
 * <li>if property is null or empty in case of String property
 * <li>if URL is well formed and/or accessible
 * <li>if String is too long (limit of Datastore is 500 characters)
 * <li>if Text is too long (limit of Datastore is 10^6 characters)
 * <li>if CSS selector is valid
 * <li>if {@link Label} name does not contain whitespaces or dollar sign
 * <li>if user equals to assumed user
 * </ul>
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            enum listing all properties which are validated
 */
public class Validator<E extends Enum<E> & HasIssue> {

	/**
	 * Default constructor; this class is state-less.
	 */
	public Validator() {}

	private static final class FetcherValidator extends Fetcher {
		public void testReachability(URI uri) throws CrawlerException {
			HttpEntity entity = getEntity(uri);
			try {
				entity.getContent().close();
			} catch (Exception e) {}
		}
	}

	/**
	 * @param property
	 *            name of property to be checked
	 * @param nullable
	 *            if null value is allowed
	 * @param objs
	 *            values to be check
	 * @return true=not null, false=good null, exception=bad null
	 * @throws ValidationException
	 */
	public boolean checkNullability(E property, boolean nullable,
			Object... objs) throws ValidationException {
		if (objs == null) {
			return true;
		}
		for (Object obj : objs) {
			if (obj == null) {
				if (nullable) {
					return false;
				}
				throw new ValidationException(new Issue<E>(property,
					IssueType.EMPTY_OR_NULL));
			}
			if (obj instanceof CharSequence) {
				if (((CharSequence) obj).length() == 0) {
					if (nullable) {
						return false;
					}
					throw new ValidationException(new Issue<E>(property,
						IssueType.EMPTY_OR_NULL));
				}
				if (obj instanceof String) {
					if (((String) obj).trim().isEmpty()) {
						if (nullable) {
							return false;
						}
						throw new ValidationException(new Issue<E>(property,
							IssueType.EMPTY_OR_NULL));
					}
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param property
	 *            name of property to be checked
	 * @param url
	 *            URL value to be check
	 * @param nullable
	 *            if null value is allowed
	 * @return normalized URI
	 * @throws ValidationException
	 */
	private URI checkUri(E property, Link url, boolean nullable)
			throws ValidationException {
		if (!checkNullability(property, nullable, url)) {
			return null;
		}
		if (url != null) {
			if (!checkNullability(property, nullable, url.getValue())) {
				return null;
			}
		}

		try {
			return new URI(url.getValue()).normalize();
		} catch (URISyntaxException e) {
			throw new ValidationException(new Issue<E>(property,
				IssueType.INVALID_VALUE));
		}
	}

	/**
	 * @param property
	 *            name of property to be checked
	 * @param url
	 *            URL value to be check
	 * @param nullable
	 *            if null value is allowed
	 * @return normalized Link
	 * @throws ValidationException
	 */
	public Link checkUrl(E property, Link url, boolean nullable)
			throws ValidationException {
		URI uri = checkUri(property, url, nullable);
		if (uri == null) {
			return null;
		}
		return new Link(uri.toString());
	}

	/**
	 * @param property
	 *            name of property to be checked
	 * @param url
	 *            URL value to be check
	 * @param nullable
	 *            if null value is allowed
	 * @return normalized Link
	 * @throws ValidationException
	 */
	public Link checkReachability(E property, Link url, boolean nullable)
			throws ValidationException {
		FetcherValidator fv = new FetcherValidator();
		URI uri = checkUri(property, url, nullable);
		if (uri == null) {
			return null;
		}

		try {
			fv.testReachability(uri);
		} catch (Exception e) {
			throw new ValidationException(new Issue<E>(property,
				IssueType.INVALID_VALUE));
		}
		return new Link(uri.toString());
	}

	/**
	 * @param property
	 *            name of property to be checked
	 * @param value
	 *            string value to be check
	 * @param nullable
	 *            if null value is allowed
	 * @param cut
	 *            whether too long value shall be cut instead of throwing
	 *            exception
	 * @return trimmed string
	 * @throws ValidationException
	 */
	public String checkString(E property, String value, boolean nullable,
			boolean cut) throws ValidationException {
		if (!checkNullability(property, nullable, value)) {
			return null;
		}

		value = value.trim();
		if (value.length() > 500) {
			if (cut) {
				return value.substring(0, Math.min(value.length(), 500));
			}
			throw new ValidationException(new Issue<E>(property,
				IssueType.TOO_LONG));
		}
		return value;
	}

	/**
	 * @param property
	 *            name of property to be checked
	 * @param value
	 *            text value to be check
	 * @param nullable
	 *            if null value is allowed
	 * @param cut
	 *            whether too long value shall be cut instead of throwing
	 *            exception
	 * @return trimmed text
	 * @throws ValidationException
	 */
	public Text checkText(E property, Text value, boolean nullable, boolean cut)
			throws ValidationException {
		if (!checkNullability(property, nullable, value)) {
			return null;
		}
		if (value != null) {
			if (!checkNullability(property, nullable, value.getValue())) {
				return null;
			}
		}

		value = new Text(value.getValue().trim());

		if (value.getValue().length() > 1000 * 1000) {
			if (cut) {
				return new Text(value.getValue().substring(0,
					Math.min(value.getValue().length(), 1000 * 1000)));
			}
			throw new ValidationException(new Issue<E>(property,
				IssueType.TOO_LONG));
		}
		return value;
	}

	/**
	 * @param property
	 *            name of property to be checked
	 * @param value
	 *            selector value to be check
	 * @param nullable
	 *            if null value is allowed
	 * @return normalized selector
	 * @throws ValidationException
	 */
	public String checkSelector(E property, String value, boolean nullable)
			throws ValidationException {
		String selector = checkString(property, value, nullable, false);
		if (selector == null) {
			return null;
		}

		try {
			Selector.select(selector, new Element(Tag.valueOf("html"), ""));
		} catch (Exception e) {
			throw new ValidationException(new Issue<E>(property,
				IssueType.INVALID_VALUE));
		}
		return selector;
	}

	/**
	 * @param property
	 *            name of property to be checked
	 * @param value
	 *            label name to be check
	 * @return normalized label name
	 * @throws ValidationException
	 */
	public String checkLabel(E property, String value)
			throws ValidationException {
		String selector = checkString(property, value, false, false);
		if (selector == null) {
			return null;
		}

		value = value.trim();
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (Character.isWhitespace(c) || c == '$') {
				throw new ValidationException(new Issue<E>(property,
					IssueType.INVALID_VALUE));
			}
		}
		return value;
	}

	/**
	 * @param property
	 *            name of property to be checked
	 * @param userId
	 *            assumed user
	 * @param userIds
	 *            real userIds which shall be equal to userId
	 * @throws ValidationException
	 */
	public void checkUser(E property, String userId, String... userIds)
			throws ValidationException {
		if (userIds == null || userIds.length == 0) {
			return;
		}
		for (String u : userIds) {
			if (!userId.equals(u)) {
				throw new ValidationException(new Issue<E>(property,
					IssueType.SECURITY_BREACH));
			}
		}
	}
}
