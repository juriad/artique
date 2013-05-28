package cz.artique.server.validation;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Selector;

import com.google.appengine.api.datastore.Link;

import cz.artique.server.crawler.CrawlerException;
import cz.artique.server.crawler.Fetcher;
import cz.artique.shared.validation.HasIssue;
import cz.artique.shared.validation.ValidationException;
import cz.artique.shared.validation.Issue;
import cz.artique.shared.validation.IssueType;

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
	public boolean checkNullability(Enum<E> property, boolean nullable,
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
					IssueType.PROPERTY_EMPTY));
			}
			if (obj instanceof CharSequence) {
				if (((CharSequence) obj).length() == 0) {
					if (nullable) {
						return false;
					}
					throw new ValidationException(new Issue<E>(property,
						IssueType.PROPERTY_EMPTY));
				}
				if (obj instanceof String) {
					if (((String) obj).trim().isEmpty()) {
						if (nullable) {
							return false;
						}
						throw new ValidationException(new Issue<E>(property,
							IssueType.PROPERTY_EMPTY));
					}
				}
			}
		}
		return true;
	}

	private URI checkUri(Enum<E> property, Link url, boolean nullable)
			throws ValidationException {
		if (!checkNullability(property, nullable, url, url.getValue())) {
			return null;
		}

		try {
			return new URI(url.getValue()).normalize();
		} catch (URISyntaxException e) {
			throw new ValidationException(new Issue<E>(property,
				IssueType.INVALID_URL));
		}
	}

	public Link checkUrl(Enum<E> property, Link url, boolean nullable)
			throws ValidationException {
		URI uri = checkUri(property, url, nullable);
		if (uri == null) {
			return null;
		}
		return new Link(uri.toString());
	}

	public Link checkReachability(Enum<E> property, Link url, boolean nullable)
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
				IssueType.UNREACHABLE_URL));
		}
		return new Link(uri.toString());
	}

	public String checkString(Enum<E> property, String value, boolean nullable,
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
				IssueType.PROPERTY_TOO_LONG));
		}
		return value;
	}

	public String checkText(Enum<E> property, String value, boolean nullable,
			boolean cut) throws ValidationException {
		if (!checkNullability(property, nullable, value)) {
			return null;
		}

		if (value.length() > 1000 * 1000) {
			if (cut) {
				return value
					.substring(0, Math.min(value.length(), 1000 * 1000));
			}
			throw new ValidationException(new Issue<E>(property,
				IssueType.PROPERTY_TOO_LONG));
		}
		return value;
	}

	public String checkSelector(Enum<E> property, String value, boolean nullable)
			throws ValidationException {
		String selector = checkString(property, value, nullable, false);
		if (selector == null) {
			return null;
		}

		try {
			Selector.select(selector, new Element(Tag.valueOf("html"), ""));
		} catch (Exception e) {
			throw new ValidationException(new Issue<E>(property,
				IssueType.INVALID_SELECTOR));
		}
		return selector;
	}

	public String checkLabel(Enum<E> property, String value)
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
					IssueType.INVALID_LABEL));
			}
		}
		return value;
	}
}
