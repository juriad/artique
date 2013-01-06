package cz.artique.server.meta.user;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-01-04 14:21:03")
/** */
public final class ConfigMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.user.Config> {

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.Config> configKey = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.Config>(this, "configKey", "configKey");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.Config, java.lang.Double> doubleValue = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.Config, java.lang.Double>(this, "doubleValue", "doubleValue", double.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.Config, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.Config, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.Config, java.lang.Long> longValue = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.Config, java.lang.Long>(this, "longValue", "longValue", long.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.Config> stringValue = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.Config>(this, "stringValue", "stringValue");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.Config, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.Config, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final ConfigMeta slim3_singleton = new ConfigMeta();

    /**
     * @return the singleton
     */
    public static ConfigMeta get() {
       return slim3_singleton;
    }

    /** */
    public ConfigMeta() {
        super("Config", cz.artique.shared.model.user.Config.class);
    }

    @Override
    public cz.artique.shared.model.user.Config entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.user.Config model = new cz.artique.shared.model.user.Config();
        model.setConfigKey((java.lang.String) entity.getProperty("configKey"));
        model.setDoubleValue(doubleToPrimitiveDouble((java.lang.Double) entity.getProperty("doubleValue")));
        model.setKey(entity.getKey());
        model.setLongValue(longToPrimitiveLong((java.lang.Long) entity.getProperty("longValue")));
        model.setStringValue((java.lang.String) entity.getProperty("stringValue"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.user.Config m = (cz.artique.shared.model.user.Config) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("configKey", m.getConfigKey());
        entity.setProperty("doubleValue", m.getDoubleValue());
        entity.setProperty("longValue", m.getLongValue());
        entity.setProperty("stringValue", m.getStringValue());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.user.Config m = (cz.artique.shared.model.user.Config) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.user.Config m = (cz.artique.shared.model.user.Config) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.user.Config m = (cz.artique.shared.model.user.Config) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.user.Config m = (cz.artique.shared.model.user.Config) model;
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
        cz.artique.shared.model.user.Config m = (cz.artique.shared.model.user.Config) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getConfigKey() != null){
            writer.setNextPropertyName("configKey");
            encoder0.encode(writer, m.getConfigKey());
        }
        writer.setNextPropertyName("doubleValue");
        encoder0.encode(writer, m.getDoubleValue());
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        writer.setNextPropertyName("longValue");
        encoder0.encode(writer, m.getLongValue());
        if(m.getStringValue() != null){
            writer.setNextPropertyName("stringValue");
            encoder0.encode(writer, m.getStringValue());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.user.Config jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.user.Config m = new cz.artique.shared.model.user.Config();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("configKey");
        m.setConfigKey(decoder0.decode(reader, m.getConfigKey()));
        reader = rootReader.newObjectReader("doubleValue");
        m.setDoubleValue(decoder0.decode(reader, m.getDoubleValue()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("longValue");
        m.setLongValue(decoder0.decode(reader, m.getLongValue()));
        reader = rootReader.newObjectReader("stringValue");
        m.setStringValue(decoder0.decode(reader, m.getStringValue()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}