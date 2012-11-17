package cz.artique.server.meta.user;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-11-14 19:53:31")
/** */
public final class SystemConfigMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.user.SystemConfig> {

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.SystemConfig> configKey = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.SystemConfig>(this, "configKey", "configKey");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.SystemConfig, cz.artique.shared.model.user.ConfigType> configType = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.SystemConfig, cz.artique.shared.model.user.ConfigType>(this, "configType", "configType", cz.artique.shared.model.user.ConfigType.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.SystemConfig, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.SystemConfig, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.SystemConfig, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.SystemConfig, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final SystemConfigMeta slim3_singleton = new SystemConfigMeta();

    /**
     * @return the singleton
     */
    public static SystemConfigMeta get() {
       return slim3_singleton;
    }

    /** */
    public SystemConfigMeta() {
        super("SystemConfig", cz.artique.shared.model.user.SystemConfig.class);
    }

    @Override
    public cz.artique.shared.model.user.SystemConfig entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.user.SystemConfig model = new cz.artique.shared.model.user.SystemConfig();
        model.setConfigKey((java.lang.String) entity.getProperty("configKey"));
        model.setConfigType(stringToEnum(cz.artique.shared.model.user.ConfigType.class, (java.lang.String) entity.getProperty("configType")));
        model.setKey(entity.getKey());
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.user.SystemConfig m = (cz.artique.shared.model.user.SystemConfig) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("configKey", m.getConfigKey());
        entity.setProperty("configType", enumToString(m.getConfigType()));
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.user.SystemConfig m = (cz.artique.shared.model.user.SystemConfig) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.user.SystemConfig m = (cz.artique.shared.model.user.SystemConfig) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.user.SystemConfig m = (cz.artique.shared.model.user.SystemConfig) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.user.SystemConfig m = (cz.artique.shared.model.user.SystemConfig) model;
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
        cz.artique.shared.model.user.SystemConfig m = (cz.artique.shared.model.user.SystemConfig) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
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
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.user.SystemConfig jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.user.SystemConfig m = new cz.artique.shared.model.user.SystemConfig();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("configKey");
        m.setConfigKey(decoder0.decode(reader, m.getConfigKey()));
        reader = rootReader.newObjectReader("configType");
        m.setConfigType(decoder0.decode(reader, m.getConfigType(), cz.artique.shared.model.user.ConfigType.class));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}