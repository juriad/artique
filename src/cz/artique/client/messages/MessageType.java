package cz.artique.client.messages;

public enum MessageType {
	FAILURE(Integer.MAX_VALUE),
	ERROR(10000),
	WARN(6000),
	INFO(4000),
	DEBUG(-1);

	private final int timeout;

	private MessageType(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

	public boolean isNotification() {
		return getTimeout() > 0;
	}
}
