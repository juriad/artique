package cz.artique.server.service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONValue;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import cz.artique.shared.model.item.ContentType;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.item.UserItem;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.user.UserInfo;

public class ClientServlet extends HttpServlet {

	/**
	 * The servlet path.
	 */
	public static final String SERVLET_PATH = "/export/client";

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
		@SuppressWarnings("unchecked")
		Map<String, String[]> parameterMap = req.getParameterMap();

		String clientToken;
		if (parameterMap.containsKey("token")) {
			String[] clientTokens = parameterMap.get("token");
			if (clientTokens == null || clientTokens.length == 0) {
				resp.sendError(400, "Missing token");
				return;
			}
			clientToken = clientTokens[0];
		} else {
			resp.sendError(400, "Missing token");
			return;
		}

		UserService us = new UserService();
		UserInfo userInfo = us.getUserInfoByClientToken(clientToken);
		if (userInfo == null) {
			resp.sendError(400, "Wrong token");
			return;
		}

		String action;
		if (parameterMap.containsKey("action")) {
			String[] actions = parameterMap.get("action");
			if (actions == null || actions.length == 0) {
				resp.sendError(400, "Missing action");
				return;
			}
			action = actions[0];
		} else {
			resp.sendError(400, "Missing action");
			return;
		}

		if (action.equalsIgnoreCase("getLabels")) {
			getLabels(userInfo, resp);
		} else if (action.equalsIgnoreCase("addItem")) {
			String item;
			if (parameterMap.containsKey("item")) {
				String[] items = parameterMap.get("item");
				if (items == null || items.length == 0) {
					resp.sendError(400, "Missing item");
					return;
				}
				item = items[0];
			} else {
				resp.sendError(400, "Missing item");
				return;
			}
			addItem(userInfo, item, resp);
		} else {
			resp.sendError(400, "Wrong action");
			return;
		}
	}

	private void addItem(UserInfo userInfo, String jsonItem,
			HttpServletResponse resp) throws IOException {
		Map<?, ?> map;
		try {
			Object obj = JSONValue.parseWithException(jsonItem);
			if (!(obj instanceof Map)) {
				resp.sendError(400, "Invalid JSON item");
				return;
			}
			map = (Map<?, ?>) obj;
		} catch (Exception e) {
			resp.sendError(400, "Invalid JSON item");
			return;
		}

		ManualItem item;
		try {
			item = createManualItem(map);
		} catch (Exception e) {
			resp.sendError(400, e.getLocalizedMessage());
			return;
		}
		UserItem userItem;
		try {
			userItem = createUserItem(map, item, userInfo.getUserId());
		} catch (Exception e) {
			resp.sendError(400, e.getLocalizedMessage());
			return;
		}

		ItemService is = new ItemService();
		is.addManualItem(userItem);

		JSONValue.writeJSONString("OK", resp.getWriter());
	}

	private UserItem createUserItem(Map<?, ?> map, ManualItem item,
			String userId) {
		UserItem userItem = new UserItem();
		userItem.setAdded(item.getAdded());
		userItem.setItemObject(item);
		{
			Object labelsObj = map.get("labels");
			if (labelsObj != null) {
				Set<String> labels = new HashSet<String>();
				if (labelsObj instanceof String) {
					String label = checkLabel((String) labelsObj);
					if (label != null) {
						labels.add(label);
					} else {
						throw new RuntimeException("Invalid label");
					}
				} else if (labelsObj instanceof List) {
					List<?> list = (List<?>) labelsObj;
					for (Object labelObj : list) {
						if (labelObj != null && labelObj instanceof String) {
							String label = checkLabel((String) labelObj);
							if (label != null) {
								labels.add(label);
							} else {
								throw new RuntimeException("Invalid label");
							}
						}
					}
				} else {
					throw new RuntimeException("Invalid labels type");
				}

				List<Key> labelKeys = getLabelKeys(labels, userId);
				userItem.setLabels(labelKeys);
			}
		}
		userItem.setRead(false);
		userItem.setUserId(userId);
		return userItem;
	}

	private List<Key> getLabelKeys(Set<String> labels, String userId) {
		List<Key> keys = new ArrayList<Key>();
		LabelService ls = new LabelService();
		for (String l : labels) {
			Label label = new Label(userId, l);
			Label creatIfNotExist = ls.creatIfNotExist(label);
			keys.add(creatIfNotExist.getKey());
		}
		return keys;
	}

	private String checkLabel(String label) {
		label = label.trim();
		if (label.isEmpty()) {
			return null;
		}
		for (int i = 0; i < label.length(); i++) {
			char c = label.charAt(i);
			if (Character.isWhitespace(c) || c == '$') {
				return null;
			}
		}
		return label;
	}

	private ManualItem createManualItem(Map<?, ?> map) {
		ManualItem item = new ManualItem();
		item.setAdded(new Date());
		{
			Object contentObj = map.get("content");
			if (contentObj != null && contentObj instanceof String) {
				String content = (String) contentObj;
				if (!content.trim().isEmpty()) {
					item.setContent(new Text(content));
				}
			}
		}
		item.setContentType(ContentType.PLAIN_TEXT);
		{
			Object titleObj = map.get("title");
			if (titleObj != null && titleObj instanceof String) {
				String title = (String) titleObj;
				if (!title.trim().isEmpty() && title.length() <= 500) {
					item.setTitle(title);
				} else {
					throw new RuntimeException(
						"Item title is either empty or too long");
				}
			} else {
				throw new RuntimeException("Missing item title");
			}
		}
		{
			Object urlObj = map.get("url");
			if (urlObj != null && urlObj instanceof String) {
				String url = (String) urlObj;
				if (!url.trim().isEmpty()) {
					try {
						URI uri = new URI(url);
						URI normalized = uri.normalize();
						item.setUrl(new Link(normalized.toString()));
					} catch (Exception e) {
						throw new RuntimeException("Item url is invalid");
					}
				} else {
					throw new RuntimeException("Item url is empty");
				}
			} else {
				throw new RuntimeException("Missing item url");
			}
		}
		return item;
	}

	private void getLabels(UserInfo userInfo, HttpServletResponse resp)
			throws IOException {
		LabelService ls = new LabelService();
		List<Label> allLabels =
			ls.getAllLabels(userInfo.getUserId(), LabelType.USER_DEFINED);
		JSONValue.writeJSONString(allLabels, resp.getWriter());
	}
}
