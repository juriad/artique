package cz.artique.server.meta.config.client;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-07-31 18:42:55")
/** */
public final class ClientConfigMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.config.client.ClientConfig> {

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.config.client.ClientConfig> configKey = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.config.client.ClientConfig>(this, "configKey", "configKey");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.config.client.ClientConfig, java.lang.Double> doubleValue = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.config.client.ClientConfig, java.lang.Double>(this, "doubleValue", "doubleValue", double.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.config.client.ClientConfig, java.lang.Integer> intValue = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.config.client.ClientConfig, java.lang.Integer>(this, "intValue", "intValue", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.config.client.ClientConfig, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.config.client.ClientConfig, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.config.client.ClientConfig> stringValue = new org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.config.client.ClientConfig>(this, "stringValue", "stringValue");

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.config.client.ClientConfig> userId = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.config.client.ClientConfig>(this, "userId", "userId");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.config.client.ClientConfig, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.config.client.ClientConfig, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final ClientConfigMeta slim3_singleton = new ClientConfigMeta();

    /**
     * @return the singleton
     */
    public static ClientConfigMeta get() {
       return slim3_singleton;
    }

    /** */
    public ClientConfigMeta() {
        super("ClientConfig", cz.artique.shared.model.config.client.ClientConfig.class);
    }

    @Override
    public cz.artique.shared.model.config.client.ClientConfig entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.config.client.ClientConfig model = new cz.artique.shared.model.config.client.ClientConfig();
        model.setConfigKey((java.lang.String) entity.getProperty("configKey"));
        model.setDoubleValue(doubleToPrimitiveDouble((java.lang.Double) entity.getProperty("doubleValue")));
        model.setIntValue(longToPrimitiveInt((java.lang.Long) entity.getProperty("intValue")));
        model.setKey(entity.getKey());
        model.setStringValue((java.lang.String) entity.getProperty("stringValue"));
        model.setUserId((java.lang.String) entity.getProperty("userId"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.config.client.ClientConfig m = (cz.artique.shared.model.config.client.ClientConfig) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("configKey", m.getConfigKey());
        entity.setUnindexedProperty("doubleValue", m.getDoubleValue());
        entity.setUnindexedProperty("intValue", m.getIntValue());
        entity.setUnindexedProperty("stringValue", m.getStringValue());
        entity.setProperty("userId", m.getUserId());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.config.client.ClientConfig m = (cz.artique.shared.model.config.client.ClientConfig) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.config.client.ClientConfig m = (cz.artique.shared.model.config.client.ClientConfig) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.config.client.ClientConfig m = (cz.artique.shared.model.config.client.ClientConfig) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.config.client.ClientConfig m = (cz.artique.shared.model.config.client.ClientConfig) model;
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
        cz.artique.shared.model.config.client.ClientConfig m = (cz.artique.shared.model.config.client.ClientConfig) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getConfigKey() != null){
            writer.setNextPropertyName("configKey");
            encoder0.encode(writer, m.getConfigKey());
        }
        writer.setNextPropertyName("doubleValue");
        encoder0.encode(writer, m.getDoubleValue());
        writer.setNextPropertyName("intValue");
        encoder0.encode(writer, m.getIntValue());
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getStringValue() != null){
            writer.setNextPropertyName("stringValue");
            encoder0.encode(writer, m.getStringValue());
        }
        if(m.getUserId() != null){
            writer.setNextPropertyName("userId");
            encoder0.encode(writer, m.getUserId());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.config.client.ClientConfig jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.config.client.ClientConfig m = new cz.artique.shared.model.config.client.ClientConfig();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("configKey");
        m.setConfigKey(decoder0.decode(reader, m.getConfigKey()));
        reader = rootReader.newObjectReader("doubleValue");
        m.setDoubleValue(decoder0.decode(reader, m.getDoubleValue()));
        reader = rootReader.newObjectReader("intValue");
        m.setIntValue(decoder0.decode(reader, m.getIntValue()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("stringValue");
        m.setStringValue(decoder0.decode(reader, m.getStringValue()));
        reader = rootReader.newObjectReader("userId");
        m.setUserId(decoder0.decode(reader, m.getUserId()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}