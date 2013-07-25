package cz.artique.server.test;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.datastore.Datastore;

import cz.artique.server.meta.source.SourceMeta;
import cz.artique.shared.model.source.Source;

/**
 * Template of servlet which is used ad-hoc during development to slightly
 * modify values in datastore.
 * 
 * @author Adam Juraszek
 * 
 */
@Deprecated
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
		// set enqued to false for all sources
		List<Source> asList =
			Datastore
				.query(SourceMeta.get())
				.filter(SourceMeta.get().enqued.equal(true))
				.asList();
		for (Source s : asList) {
			s.setEnqued(false);
		}
		Datastore.put(asList);
	}
}
