package cz.artique.server.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Entry point for users when serving backups of webpages.
 * 
 * @author Adam Juraszek
 * 
 */
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
	 * Gets backup key from request parameter and delegates it to
	 * {@link BackupService#serveBackup(String, HttpServletResponse)}
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

		// backup
		@SuppressWarnings("unchecked")
		Map<String, String[]> parameterMap = req.getParameterMap();
		String[] backups = parameterMap.get("backup");
		if (backups == null || backups.length <= 0) {
			resp.sendError(400, "Missing backup key.");
			return;
		}
		String backup = backups[0].trim();

		BackupService bs = new BackupService();
		bs.serveBackup(backup, resp);
	}
}
