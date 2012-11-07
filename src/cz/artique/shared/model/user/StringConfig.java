package cz.artique.shared.model.user;

import java.io.Serializable;

import org.slim3.datastore.Model;

import cz.artique.shared.model.user.UserConfig;

@Model(schemaVersion = 1)
public class StringConfig extends UserConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private String configValue;

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

}
