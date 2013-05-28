package cz.artique.server.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BackupServlet extends HttpServlet {

	/**
	 * The servlet path.
	 */
	public static final String SERVLET_PATH = "/export/backupService";

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
		String[] backups = parameterMap.get("backup");
		if (backups == null || backups.length <= 0) {
			throw new IllegalArgumentException("Missing backup key.");
		}
		String backup = backups[0].trim();

		BackupService bs = new BackupService();
		bs.serveBackup(backup, resp);
	}
}
