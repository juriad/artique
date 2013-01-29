package cz.artique.client.manager;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

public class RpcWithTimeoutRequestBuilder extends RpcRequestBuilder {
	private final int timeoutMilis;

	public RpcWithTimeoutRequestBuilder(int timeoutMilis) {
		if (timeoutMilis < 0) {
			throw new IllegalArgumentException("Cannot be negative");
		}
		this.timeoutMilis = timeoutMilis;
	}

	@Override
	protected RequestBuilder doCreate(String serviceEntryPoint) {
		RequestBuilder builder =
			new RequestBuilder(RequestBuilder.POST, serviceEntryPoint);
		builder.setTimeoutMillis(timeoutMilis);
		return builder;
	}
}
