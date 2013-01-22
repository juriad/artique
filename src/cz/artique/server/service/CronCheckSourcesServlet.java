package cz.artique.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.datastore.Datastore;

import cz.artique.server.meta.source.SourceMeta;
import cz.artique.shared.model.source.Source;
import cz.artique.shared.model.user.ConfigKey;

public class CronCheckSourcesServlet extends HttpServlet {

	/**
	 * The servlet path.
	 */
	public static final String SERVLET_PATH = "/cron/checkSource";

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
		SourceMeta meta = SourceMeta.get();
		@SuppressWarnings("unchecked")
		Map<String, String[]> parameterMap = req.getParameterMap();

		int maxErrors =
			ConfigService.CONFIG_SERVICE
				.getConfig(ConfigKey.MAX_ERROR_SEQUENCE)
				.get();
		List<Source> sourcesList;
		if (parameterMap.containsKey("normal")) {
			sourcesList =
				Datastore
					.query(meta)
					.filter(meta.enabled.equal(Boolean.TRUE))
					.filter(meta.nextCheck.lessThan(new Date()))
					.filter(meta.parent.equal(null))
					.filterInMemory(meta.errorSequence.lessThan(maxErrors))
					.asList();
		} else if (parameterMap.containsKey("error")) {
			sourcesList =
				Datastore
					.query(meta)
					.filter(meta.enabled.equal(Boolean.TRUE))
					.filter(meta.parent.equal(null))
					.filter(meta.errorSequence.greaterThanOrEqual(maxErrors))
					.asList();
		} else {
			sourcesList = new ArrayList<Source>();
		}

		CheckService cs = new CheckService();
		for (Source s : sourcesList) {
			cs.check(s);
		}
	}
}
