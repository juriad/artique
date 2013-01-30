package cz.artique.server.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.server.utils.ServerUtils;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.label.LabelType;
import cz.artique.shared.model.source.UserSource;

public class Updater extends HttpServlet {
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

	protected void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserSourceMeta meta = UserSourceMeta.get();
		List<UserSource> list = Datastore.query(meta).asList();
		for (UserSource us : list) {
			Key labelKey;
			{
				String labelName =
					KeyFactory.keyToString(us.getSource());
				final Label l = new Label(us.getUser(), labelName);
				l.setLabelType(LabelType.USER_SOURCE);
				labelKey = ServerUtils.genKey(l);
				l.setKey(labelKey);
				Datastore.put(l);
			}
			us.setLabel(labelKey);
			Datastore.put(us);
		}
		PrintWriter out = resp.getWriter();
		out.println("OK");
	}
}
