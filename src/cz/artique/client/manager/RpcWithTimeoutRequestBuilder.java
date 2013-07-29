package cz.artique.client.manager;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

/**
 * Defines RPC request having set timeout.
 * 
 * @author Adam Juraszek
 * 
 */
public class RpcWithTimeoutRequestBuilder extends RpcRequestBuilder {
	private final int timeoutMillis;

	/**
	 * New request with timeout.
	 * 
	 * @param timeoutMillis
	 *            number of milliseconds
	 */
	public RpcWithTimeoutRequestBuilder(int timeoutMillis) {
		if (timeoutMillis < 0) {
			throw new IllegalArgumentException("Cannot be negative");
		}
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	protected RequestBuilder doCreate(String serviceEntryPoint) {
		RequestBuilder builder =
			new RequestBuilder(RequestBuilder.POST, serviceEntryPoint);
		builder.setTimeoutMillis(timeoutMillis);
		return builder;
	}
}
