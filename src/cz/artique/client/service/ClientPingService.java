package cz.artique.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The service responsible for testing connection between client and server.
 * 
 * @author Adam Juraszek
 * 
 */
@RemoteServiceRelativePath("service.s3gwt")
public interface ClientPingService extends RemoteService {
	/**
	 * Does nothing, this method is used to test connection.
	 */
	void ping();
}
