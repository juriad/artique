package cz.artique.server.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.users.User;

import cz.artique.server.meta.source.XMLSourceMeta;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.model.source.XMLSource;

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
		SourceService ss = new SourceService();
		UserSourceService uss = new UserSourceService();

		XMLSource s1 =
			new XMLSource(new Link("http://feeds2.feedburner.com/9GAG"));
		ss.creatIfNotExist(s1, XMLSourceMeta.get());

		UserSource us1 =
			new UserSource(new User("test@example.com", "gmail.com",
				"18580476422013912411"), s1, "9gag");
		us1.setWatching(true);
		uss.createUserSource(us1);
	}
}
