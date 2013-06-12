package cz.artique.shared.model.user;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import cz.artique.server.service.ClientServlet;
import cz.artique.server.service.UserServiceWrapperImpl;
import cz.artique.shared.model.item.ManualItem;
import cz.artique.shared.model.label.Label;
import cz.artique.shared.model.source.ManualSource;
import cz.artique.shared.model.source.UserSource;
import cz.artique.shared.utils.GenKey;

/**
 * <p>
 * UserInfo stores basic information about user to datastore. The persistent
 * attributes are:
 * <ul>
 * <li>nickname - used in logout link and personalized URL
 * <li>userId - stable identifier of user
 * <li>clientToken - random secret token used by crossfire extension
 * </ul>
 * 
 * <p>
 * Warning: Keep clientToken as secret as possible; it is injected to page head
 * element and read by client extension. It allows anybody to perform following
 * two actions via {@link ClientServlet}:
 * <ul>
 * <li>To get list of all existing {@link Label}s and
 * {@link UserSource#getDefaultLabels()} for {@link ManualSource}.
 * <li>To add a new {@link ManualItem} with some labels. New labels will be
 * created if they did not exist.
 * </ul>
 * 
 * <p>
 * There are also some non-persistent attributes:
 * <ul>
 * <li>loginUrl, logoutUrl - URL provided by Google to log user in or out
 * </ul>
 * 
 * <p>
 * UserInfo is custom implementation of {@link User}. There was an issue with
 * {@link User}: when User is put to datastore and get back, the userId
 * attribute is not preserved. It caused massive problems in recommendation: the
 * only part of application which works with several users at a time.
 * 
 * @author Adam Juraszek
 * 
 */
@Model(schemaVersion = 1)
public class UserInfo implements Serializable, GenKey {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version;

	private String nickname;

	private String userId;

	@Attribute(persistent = false)
	private String loginUrl;

	@Attribute(persistent = false)
	private String logoutUrl;

	// TODO nice to have: client token expiration

	private String clientToken;

	/**
	 * @return key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @param key
	 *            key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserInfo other = (UserInfo) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	public String getKeyName() {
		return getUserId();
	}

	/**
	 * @return nickname of user
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname
	 *            nickname of user
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return user id, stable user identifier
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param user
	 *            id user id, stable user identifier
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * Login URL is not part of persistent state of UserInfo. It is filled by
	 * {@link UserServiceWrapperImpl} only if user <b>is not</b> signed in.
	 * 
	 * @return login URL or null if not set
	 */
	public String getLoginUrl() {
		return loginUrl;
	}

	/**
	 * Set by {@link UserServiceWrapperImpl} only if user <b>is not</b> signed
	 * in.
	 * 
	 * @param loginUrl
	 *            login URL
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * Logout URL is not part of persistent state of UserInfo. It is filled by
	 * {@link UserServiceWrapperImpl} only if user <b>is</b> signed in.
	 * 
	 * @return logout URL or null if not set
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}

	/**
	 * Set by {@link UserServiceWrapperImpl} only if user <b>is</b> signed in.
	 * 
	 * @param logoutUrl
	 *            logout URL
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	/**
	 * Client token is a random persistent token used by crossfire extension to
	 * add {@link ManualItem}.
	 * 
	 * @return client token
	 */
	public String getClientToken() {
		return clientToken;
	}

	/**
	 * @param clientToken
	 *            client token
	 */
	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}
}
