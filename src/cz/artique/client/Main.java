package cz.artique.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

import cz.artique.client.login.ErrorPage;
import cz.artique.client.login.LoginPage;
import cz.artique.client.service.ClientUserService;
import cz.artique.client.service.ClientUserServiceAsync;
import cz.artique.shared.model.user.UserInfo;

/**
 * Main and the only entry point to the application.
 * Depending on whether the user is logger, it shows {@link Artique} or
 * {@link LoginPage}.
 * 
 * @author Adam Juraszek
 * 
 */
public class Main implements EntryPoint {
	/**
	 * Reacts to any uncaught exception by reloading application.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public class ClientExceptionHandler implements UncaughtExceptionHandler {
		public void onUncaughtException(Throwable e) {
			e.printStackTrace();
			Window.Location.reload();
		}
	}

	/**
	 * Sets {@link ClientExceptionHandler} and inits application.
	 * 
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new ClientExceptionHandler());
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				initApplication();
			}
		});
	}

	/**
	 * Contacts server via {@link ClientUserService}.
	 * If the user is already logged in, call {@link #loadArtique()}, else call
	 * {@link #loadLogin()}.
	 */
	private void initApplication() {
		ClientUserServiceAsync userService =
			GWT.create(ClientUserService.class);

		userService.login(GWT.getHostPageBaseURL(),
			new AsyncCallback<UserInfo>() {
				public void onFailure(Throwable error) {
					loadError();
				}

				public void onSuccess(UserInfo result) {
					ArtiqueWorld.WORLD.setUserInfo(result);
					if (result.getUserId() != null) {
						loadArtique();
					} else {
						loadLogin();
					}
				}
			});
	}

	/**
	 * Shows {@link Artique}.
	 */
	protected void loadArtique() {
		Artique t = new Artique();
		RootLayoutPanel.get().add(t);
	}

	/**
	 * Shows {@link LoginPage}.
	 */
	private void loadLogin() {
		RootPanel.get().add(new LoginPage());
	}

	/**
	 * Shows {@link ErrorPage}.
	 */
	private void loadError() {
		RootPanel.get().add(new ErrorPage());
	}

}
