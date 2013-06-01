package cz.artique.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

import cz.artique.client.login.ErrorPage;
import cz.artique.client.login.LoginPage;
import cz.artique.client.service.UserServiceWrapper;
import cz.artique.client.service.UserServiceWrapperAsync;

public class Main implements EntryPoint {
	public void onModuleLoad() {
		UserServiceWrapperAsync userService =
			GWT.create(UserServiceWrapper.class);

		userService.login(GWT.getHostPageBaseURL(),
			new AsyncCallback<UserInfo>() {
				public void onFailure(Throwable error) {
					loadError();
				}

				public void onSuccess(UserInfo result) {
					ArtiqueWorld.WORLD.setUserInfo(result);
					if (result.getUser() != null) {
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
