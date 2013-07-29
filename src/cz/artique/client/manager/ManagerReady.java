package cz.artique.client.manager;

/**
 * Interface describing callbacks used to inform caller about manager being
 * ready.
 * 
 * @author Adam Juraszek
 * 
 */
public interface ManagerReady {

	/**
	 * Called when {@link Manager} becomes ready.
	 */
	public void onReady();

}
