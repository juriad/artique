package cz.artique.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

import cz.artique.shared.items.ListingRequest;
import cz.artique.shared.items.ListingResponse;
import cz.artique.shared.model.config.ConfigKey;
import cz.artique.shared.model.item.ArticleItem;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.ListFilter;
import cz.artique.shared.model.user.UserInfo;

public class ExportServlet extends HttpServlet {

	/**
	 * The servlet path.
	 */
	public static final String SERVLET_PATH = "/export/feedService";

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}

	/**
	 * Processes this request.
	 * 
	 * @param req
	 *            the request
	 * @param resp
	 *            the response
	 * @throws ServletException
	 *             if the path is illegal
	 * @throws IOException
	 *             if {@link IOException} occurred
	 */
	protected void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// user
		@SuppressWarnings("unchecked")
		Map<String, String[]> parameterMap = req.getParameterMap();
		String[] users = parameterMap.get("user");
		if (users == null || users.length <= 0) {
			resp.sendError(400, "User has not been specified.");
			return;
		}
		String user = users[0].trim();

		// export
		String[] exports = parameterMap.get("export");
		if (exports == null || exports.length <= 0) {
			resp.sendError(400, "Export has not been specified.");
			return;
		}
		String export = exports[0].trim();

		// type
		String type;
		String[] types = parameterMap.get("type");
		if (types == null || types.length <= 0) {
			type = "rss_2.0";
		} else {
			type = types[0].trim().toLowerCase();
			if (type.contains("atom")) {
				type = "atom_1.0";
			} else {
				type = "rss_1.0";
			}
		}

		ListFilter bestListFilter = findBestListFilter(user, export);
		if (bestListFilter == null) {
			resp.sendError(404, "No such export found: " + export
				+ " for user " + user);
			return;
		}

		SyndFeed feed = createFeed(bestListFilter, type);
		StringBuffer requestURL = req.getRequestURL();
		if (req.getQueryString() != null) {
			requestURL.append("?").append(req.getQueryString());
		}
		feed.setLink(requestURL.toString());
		feed.setUri(requestURL.toString());
		try {
			respondFeed(resp, feed, bestListFilter, type);
		} catch (FeedException e) {
			resp.sendError(500,
				"Failed to print feed to output: " + e.getLocalizedMessage());
		}
	}

	private void respondFeed(HttpServletResponse resp, SyndFeed feed,
			ListFilter listFilter, String type)
			throws IOException, FeedException {
		ItemService is = new ItemService();
		int fetchCount =
			ConfigService.CONFIG_SERVICE
				.getConfig(ConfigKey.EXPORT_FETCH_COUNT)
				.<Integer> get();
		ListingRequest request =
			new ListingRequest(listFilter, null, null, fetchCount);
		ListingResponse<UserItem> items =
			is.getItems(listFilter.getUserId(), request);

		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		for (UserItem item : items.getTail()) {
			SyndEntry entry = new SyndEntryImpl();
			Item object = item.getItemObject();
			entry.setLink(object.getUrl().getValue());
			entry.setTitle(object.getTitle());
			entry.setUri(object.getUrl().getValue());
			entry.setPublishedDate(object.getPublished() == null ? object
				.getAdded() : object.getPublished());
			if (object instanceof ArticleItem) {
				ArticleItem ai = (ArticleItem) object;
				if (ai.getAuthor() != null) {
					entry.setAuthor(ai.getAuthor());
				}
			}
			// read difference between description and content
			// TODO export: content

			SyndContentImpl description = new SyndContentImpl();
			description.setType(object.getContentType().getType());
			description.setValue(object.getContent().getValue());
			entry.setDescription(description);

			entries.add(entry);
		}
		feed.setEntries(entries);

		SyndFeedOutput output = new SyndFeedOutput();
		output.output(feed, resp.getWriter(), true);
	}

	private SyndFeed createFeed(ListFilter listFilter, String type) {
		UserService us = new UserService();
		UserInfo userInfo = us.getUserInfo(listFilter.getUserId());
		SyndFeed feed = new SyndFeedImpl();
		feed.setFeedType(type);
		feed.setTitle(listFilter.getExportAlias());
		feed.setDescription("This feed contains items which Artique user "
			+ userInfo.getNickname()
			+ " marked to be exported via RSS/Atom feed.");

		feed.setAuthor(userInfo.getNickname());
		return feed;
	}

	private ListFilter findBestListFilter(String user, String export) {
		UserService us = new UserService();
		UserInfo userInfo = us.getUserInfoByNickname(user);
		if (userInfo == null)
			return null;

		ListFilterService lfs = new ListFilterService();
		ListFilter exportByAlias =
			lfs.getExportByAlias(export, userInfo.getUserId());
		return exportByAlias;
	}
}