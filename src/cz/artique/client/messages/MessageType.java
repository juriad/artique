package cz.artique.client.messages;

/**
 * Type or severity of message.
 * 
 * @author Adam Juraszek
 * 
 */
public enum MessageType {
	/**
	 * Shown when application cannot contact server.
	 */
	OFFLINE(Integer.MAX_VALUE),
	/**
	 * Informs user about failed operation.
	 */
	ERROR(10000),
	/**
	 * Currently not used.
	 */
	WARN(6000),
	/**
	 * Informs about success.
	 */
	INFO(4000),
	/**
	 * This type is not shown to user.
	 */
	DEBUG(-1);

	private final int timeout;

	private MessageType(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return time the message shall be shown for
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @return whether the message shall be shown
	 */
	public boolean isNotification() {
		return getTimeout() > 0;
	}
}
