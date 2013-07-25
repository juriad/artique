package cz.artique.server.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Entry point for AppEngine platform cron to launch recommendation
 * recalculation.
 * 
 * @author Adam Juraszek
 * 
 */
public class CronRecommendationServlet extends HttpServlet {

	/**
	 * The servlet path.
	 */
	public static final String SERVLET_PATH = "/cron/recommendation";

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
	 * Delegates recalculation to {@link RecommendationService#recalc()}.
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
		RecommendationService rs = new RecommendationService();
		rs.recalc();
	}
}
