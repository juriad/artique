package cz.artique.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.artique.shared.model.user.UserInfo;

/**
 * The service responsible for getting information about current user.
 * 
 * @author Adam Juraszek
 * 
 */
@RemoteServiceRelativePath("userService")
public interface ClientUserService extends RemoteService {
	/**
	 * Test whether user is logged-in. If he is, a full {@link UserInfo} with
	 * logout URL is returned.
	 * Otherwise an empty {@link UserInfo} with login URL is returned.
	 * 
	 * @param requestUri
	 *            URL of application
	 * @return {@link UserInfo} describing logged user and contining URL for
	 *         login or logout
	 */
	public UserInfo login(String requestUri);
}
