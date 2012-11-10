package cz.artique.shared.model.user;

import java.io.Serializable;

import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class IntConfig extends UserConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long configValue;

	public Long getConfigValue() {
		return configValue;
	}

	public void setConfigValue(Long configValue) {
		this.configValue = configValue;
	}

}
