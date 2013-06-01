package cz.artique.server.validation;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Selector;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

import cz.artique.server.crawler.CrawlerException;
import cz.artique.server.crawler.Fetcher;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;
import cz.artique.shared.validation.ValidationException;

public class Validator<E extends Enum<E> & HasIssue> {

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
	 * @param nullable
	 * @param objs
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

	public Link checkUrl(E property, Link url, boolean nullable)
			throws ValidationException {
		URI uri = checkUri(property, url, nullable);
		if (uri == null) {
			return null;
		}
		return new Link(uri.toString());
	}

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

	public void checkUser(E property, User user, User... users)
			throws ValidationException {
		if (users == null || users.length == 0) {
			return;
		}
		for (User u : users) {
			if (!user.equals(u)) {
				throw new ValidationException(new Issue<E>(property,
					IssueType.SECURITY_BREACH));
			}
		}
	}
}
