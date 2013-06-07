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
import cz.artique.client.service.UserServiceWrapper;
import cz.artique.client.service.UserServiceWrapperAsync;
import cz.artique.shared.model.user.UserInfo;

public class Main implements EntryPoint {
	public class ClientExceptionHandler implements UncaughtExceptionHandler {
		public void onUncaughtException(Throwable e) {
			e.printStackTrace();
			Window.Location.reload();
		}
	}

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new ClientExceptionHandler());
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				initApplication();
			}
		});
	}

	private void initApplication() {
		UserServiceWrapperAsync userService =
			GWT.create(UserServiceWrapper.class);

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

	protected void loadArtique() {
		Artique t = new Artique();
		RootLayoutPanel.get().add(t);
	}

	private void loadLogin() {
		RootPanel.get().add(new LoginPage());
	}

	private void loadError() {
		RootPanel.get().add(new ErrorPage());
	}

}
