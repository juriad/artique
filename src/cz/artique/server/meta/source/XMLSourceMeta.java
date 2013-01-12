package cz.artique.server.meta.source;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-01-12 14:31:36")
/** */
public final class XMLSourceMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.source.XMLSource> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Boolean> enabled = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Boolean>(this, "enabled", "enabled", boolean.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Boolean> enqued = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Boolean>(this, "enqued", "enqued", boolean.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Integer> errorSequence = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Integer>(this, "errorSequence", "errorSequence", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.util.Date> lastCheck = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.util.Date>(this, "lastCheck", "lastCheck", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.util.Date> nextCheck = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.util.Date>(this, "nextCheck", "nextCheck", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, com.google.appengine.api.datastore.Key> parent = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, com.google.appengine.api.datastore.Key>(this, "parent", "parent", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, com.google.appengine.api.datastore.Link> url = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, com.google.appengine.api.datastore.Link>(this, "url", "url", com.google.appengine.api.datastore.Link.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Integer> usage = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Integer>(this, "usage", "usage", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.XMLSource, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final XMLSourceMeta slim3_singleton = new XMLSourceMeta();

    /**
     * @return the singleton
     */
    public static XMLSourceMeta get() {
       return slim3_singleton;
    }

    /** */
    public XMLSourceMeta() {
        super("Source", cz.artique.shared.model.source.XMLSource.class, java.util.Arrays.asList("cz.artique.shared.model.source.XMLSource"));
    }

    @Override
    public cz.artique.shared.model.source.XMLSource entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.source.XMLSource model = new cz.artique.shared.model.source.XMLSource();
        model.setEnabled(booleanToPrimitiveBoolean((java.lang.Boolean) entity.getProperty("enabled")));
        model.setEnqued(booleanToPrimitiveBoolean((java.lang.Boolean) entity.getProperty("enqued")));
        model.setErrorSequence(longToPrimitiveInt((java.lang.Long) entity.getProperty("errorSequence")));
        model.setKey(entity.getKey());
        model.setLastCheck((java.util.Date) entity.getProperty("lastCheck"));
        model.setNextCheck((java.util.Date) entity.getProperty("nextCheck"));
        model.setParent((com.google.appengine.api.datastore.Key) entity.getProperty("parent"));
        model.setUrl((com.google.appengine.api.datastore.Link) entity.getProperty("url"));
        model.setUsage(longToPrimitiveInt((java.lang.Long) entity.getProperty("usage")));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.source.XMLSource m = (cz.artique.shared.model.source.XMLSource) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("enabled", m.isEnabled());
        entity.setProperty("enqued", m.isEnqued());
        entity.setProperty("errorSequence", m.getErrorSequence());
        entity.setProperty("lastCheck", m.getLastCheck());
        entity.setProperty("nextCheck", m.getNextCheck());
        entity.setProperty("parent", m.getParent());
        entity.setProperty("url", m.getUrl());
        entity.setProperty("usage", m.getUsage());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        entity.setProperty("slim3.classHierarchyList", classHierarchyList);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.source.XMLSource m = (cz.artique.shared.model.source.XMLSource) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.source.XMLSource m = (cz.artique.shared.model.source.XMLSource) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.source.XMLSource m = (cz.artique.shared.model.source.XMLSource) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.source.XMLSource m = (cz.artique.shared.model.source.XMLSource) model;
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
        cz.artique.shared.model.source.XMLSource m = (cz.artique.shared.model.source.XMLSource) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        writer.setNextPropertyName("enabled");
        encoder0.encode(writer, m.isEnabled());
        writer.setNextPropertyName("enqued");
        encoder0.encode(writer, m.isEnqued());
        writer.setNextPropertyName("errorSequence");
        encoder0.encode(writer, m.getErrorSequence());
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getLastCheck() != null){
            writer.setNextPropertyName("lastCheck");
            encoder0.encode(writer, m.getLastCheck());
        }
        if(m.getNextCheck() != null){
            writer.setNextPropertyName("nextCheck");
            encoder0.encode(writer, m.getNextCheck());
        }
        if(m.getParent() != null){
            writer.setNextPropertyName("parent");
            encoder0.encode(writer, m.getParent());
        }
        if(m.getParentObject() != null){
            writer.setNextPropertyName("parentObject");
            encoder0.encode(writer, m.getParentObject());
        }
        if(m.getUrl() != null){
            writer.setNextPropertyName("url");
            encoder0.encode(writer, m.getUrl());
        }
        writer.setNextPropertyName("usage");
        encoder0.encode(writer, m.getUsage());
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.source.XMLSource jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.source.XMLSource m = new cz.artique.shared.model.source.XMLSource();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("enabled");
        m.setEnabled(decoder0.decode(reader, m.isEnabled()));
        reader = rootReader.newObjectReader("enqued");
        m.setEnqued(decoder0.decode(reader, m.isEnqued()));
        reader = rootReader.newObjectReader("errorSequence");
        m.setErrorSequence(decoder0.decode(reader, m.getErrorSequence()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("lastCheck");
        m.setLastCheck(decoder0.decode(reader, m.getLastCheck()));
        reader = rootReader.newObjectReader("nextCheck");
        m.setNextCheck(decoder0.decode(reader, m.getNextCheck()));
        reader = rootReader.newObjectReader("parent");
        m.setParent(decoder0.decode(reader, m.getParent()));
        reader = rootReader.newObjectReader("parentObject");
        m.setParentObject(decoder0.decode(reader, m.getParentObject(), cz.artique.shared.model.source.Source.class));
        reader = rootReader.newObjectReader("url");
        m.setUrl(decoder0.decode(reader, m.getUrl()));
        reader = rootReader.newObjectReader("usage");
        m.setUsage(decoder0.decode(reader, m.getUsage()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}