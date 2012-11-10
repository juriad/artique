package cz.artique.server.meta.user;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-11-07 14:04:22")
/** */
public final class FloatConfigMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.user.FloatConfig> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, java.lang.Double> configValue = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, java.lang.Double>(this, "configValue", "configValue", java.lang.Double.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.FloatConfig> configKey = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.FloatConfig>(this, "configKey", "configKey");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, cz.artique.shared.model.user.ConfigType> configType = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, cz.artique.shared.model.user.ConfigType>(this, "configType", "configType", cz.artique.shared.model.user.ConfigType.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, com.google.appengine.api.users.User> user = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, com.google.appengine.api.users.User>(this, "user", "user", com.google.appengine.api.users.User.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.FloatConfig, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final FloatConfigMeta slim3_singleton = new FloatConfigMeta();

    /**
     * @return the singleton
     */
    public static FloatConfigMeta get() {
       return slim3_singleton;
    }

    /** */
    public FloatConfigMeta() {
        super("UserConfig", cz.artique.shared.model.user.FloatConfig.class, java.util.Arrays.asList("cz.artique.shared.model.user.FloatConfig"));
    }

    @Override
    public cz.artique.shared.model.user.FloatConfig entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.user.FloatConfig model = new cz.artique.shared.model.user.FloatConfig();
        model.setConfigValue((java.lang.Double) entity.getProperty("configValue"));
        model.setConfigKey((java.lang.String) entity.getProperty("configKey"));
        model.setConfigType(stringToEnum(cz.artique.shared.model.user.ConfigType.class, (java.lang.String) entity.getProperty("configType")));
        model.setKey(entity.getKey());
        model.setUser((com.google.appengine.api.users.User) entity.getProperty("user"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.user.FloatConfig m = (cz.artique.shared.model.user.FloatConfig) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("configValue", m.getConfigValue());
        entity.setProperty("configKey", m.getConfigKey());
        entity.setProperty("configType", enumToString(m.getConfigType()));
        entity.setProperty("user", m.getUser());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        entity.setProperty("slim3.classHierarchyList", classHierarchyList);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.user.FloatConfig m = (cz.artique.shared.model.user.FloatConfig) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.user.FloatConfig m = (cz.artique.shared.model.user.FloatConfig) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.user.FloatConfig m = (cz.artique.shared.model.user.FloatConfig) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.user.FloatConfig m = (cz.artique.shared.model.user.FloatConfig) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected void prePut(Object model) {
    }

    @Override
    protected void postGet(Object model) {
    }

    @Override
    public String getSchemaVersionName() {
        return "slim3.schemaVersion";
    }

    @Override
    public String getClassHierarchyListName() {
        return "slim3.classHierarchyList";
    }

    @Override
    protected boolean isCipherProperty(String propertyName) {
        return false;
    }

    @Override
    protected void modelToJson(org.slim3.datastore.json.JsonWriter writer, java.lang.Object model, int maxDepth, int currentDepth) {
        cz.artique.shared.model.user.FloatConfig m = (cz.artique.shared.model.user.FloatConfig) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getConfigValue() != null){
            writer.setNextPropertyName("configValue");
            encoder0.encode(writer, m.getConfigValue());
        }
        if(m.getConfigKey() != null){
            writer.setNextPropertyName("configKey");
            encoder0.encode(writer, m.getConfigKey());
        }
        if(m.getConfigType() != null){
            writer.setNextPropertyName("configType");
            encoder0.encode(writer, m.getConfigType());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getUser() != null){
            writer.setNextPropertyName("user");
            encoder0.encode(writer, m.getUser());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.user.FloatConfig jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.user.FloatConfig m = new cz.artique.shared.model.user.FloatConfig();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("configValue");
        m.setConfigValue(decoder0.decode(reader, m.getConfigValue()));
        reader = rootReader.newObjectReader("configKey");
        m.setConfigKey(decoder0.decode(reader, m.getConfigKey()));
        reader = rootReader.newObjectReader("configType");
        m.setConfigType(decoder0.decode(reader, m.getConfigType(), cz.artique.shared.model.user.ConfigType.class));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("user");
        m.setUser(decoder0.decode(reader, m.getUser()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}