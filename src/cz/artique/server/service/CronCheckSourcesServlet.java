package cz.artique.server.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.artique.shared.model.source.Source;

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
		@SuppressWarnings("unchecked")
		Map<String, String[]> parameterMap = req.getParameterMap();

		SourceService ss = new SourceService();
		List<Source> sourcesList;
		if (parameterMap.containsKey("normal")) {
			sourcesList = ss.getSourcesForNormalCheck();
		} else if (parameterMap.containsKey("error")) {
			sourcesList = ss.getSourcesForErrorCheck();
		} else {
			resp.sendError(500, "No such check type");
			return;
		}

		CheckService cs = new CheckService();
		for (Source s : sourcesList) {
			cs.check(s);
		}
	}
}
