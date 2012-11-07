package cz.artique.shared.model.user;

import org.slim3.datastore.ModelMeta;

import cz.artique.server.meta.user.FloatConfigMeta;
import cz.artique.server.meta.user.IntConfigMeta;
import cz.artique.server.meta.user.StringConfigMeta;

public enum ConfigType {
	INT_CONFIG(IntConfigMeta.get()),
	STRING_CONFIG(StringConfigMeta.get()),
	FLOAT_CONFIG(FloatConfigMeta.get());

	private final Class<?> clazz;
	private final ModelMeta<?> meta;

	private ConfigType(ModelMeta<?> meta) {
		this.meta = meta;
		this.clazz = meta.getModelClass();
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public ModelMeta<?> getMeta() {
		return meta;
	}
}
