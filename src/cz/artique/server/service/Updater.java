package cz.artique.server.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.datastore.Datastore;

import cz.artique.server.meta.item.UserItemMeta;
import cz.artique.server.meta.source.UserSourceMeta;
import cz.artique.shared.model.item.UserItem;
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
		List<UserItem> asList = Datastore.query(UserItemMeta.get()).asList();
		for(UserItem ui: asList) {
			UserSource userSource = Datastore.get(UserSourceMeta.get(), ui.getUserSource());
			ui.getLabels().add(userSource.getLabel());
		}
		Datastore.put(asList);
		
		PrintWriter out = resp.getWriter();
		out.println("OK");
	}
}
