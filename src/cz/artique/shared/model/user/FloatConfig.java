package cz.artique.shared.model.user;

import java.io.Serializable;

import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class FloatConfig extends UserConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double configValue;

	public Double getConfigValue() {
		return configValue;
	}

	public void setConfigValue(Double configValue) {
		this.configValue = configValue;
	}

}
