package cz.artique.server.meta.source;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-05-06 13:06:22")
/** */
public final class PageChangeSourceMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.source.PageChangeSource> {

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Text> content = new org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Text>(this, "content", "content", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.util.Date> lastChange = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.util.Date>(this, "lastChange", "lastChange", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Key> region = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Key>(this, "region", "region", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Boolean> enabled = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Boolean>(this, "enabled", "enabled", boolean.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Boolean> enqued = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Boolean>(this, "enqued", "enqued", boolean.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Integer> errorSequence = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Integer>(this, "errorSequence", "errorSequence", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.util.Date> lastCheck = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.util.Date>(this, "lastCheck", "lastCheck", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.util.Date> nextCheck = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.util.Date>(this, "nextCheck", "nextCheck", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Key> parent = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Key>(this, "parent", "parent", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Link> url = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, com.google.appengine.api.datastore.Link>(this, "url", "url", com.google.appengine.api.datastore.Link.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Integer> usage = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Integer>(this, "usage", "usage", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeSource, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final PageChangeSourceMeta slim3_singleton = new PageChangeSourceMeta();

    /**
     * @return the singleton
     */
    public static PageChangeSourceMeta get() {
       return slim3_singleton;
    }

    /** */
    public PageChangeSourceMeta() {
        super("Source", cz.artique.shared.model.source.PageChangeSource.class, java.util.Arrays.asList("cz.artique.shared.model.source.HTMLSource", "cz.artique.shared.model.source.PageChangeSource"));
    }

    @Override
    public cz.artique.shared.model.source.PageChangeSource entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.source.PageChangeSource model = new cz.artique.shared.model.source.PageChangeSource();
        model.setContent((com.google.appengine.api.datastore.Text) entity.getProperty("content"));
        model.setLastChange((java.util.Date) entity.getProperty("lastChange"));
        model.setRegion((com.google.appengine.api.datastore.Key) entity.getProperty("region"));
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
        cz.artique.shared.model.source.PageChangeSource m = (cz.artique.shared.model.source.PageChangeSource) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("content", m.getContent());
        entity.setProperty("lastChange", m.getLastChange());
        entity.setProperty("region", m.getRegion());
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
        cz.artique.shared.model.source.PageChangeSource m = (cz.artique.shared.model.source.PageChangeSource) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.source.PageChangeSource m = (cz.artique.shared.model.source.PageChangeSource) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.source.PageChangeSource m = (cz.artique.shared.model.source.PageChangeSource) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.source.PageChangeSource m = (cz.artique.shared.model.source.PageChangeSource) model;
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
        cz.artique.shared.model.source.PageChangeSource m = (cz.artique.shared.model.source.PageChangeSource) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getContent() != null && m.getContent().getValue() != null){
            writer.setNextPropertyName("content");
            encoder0.encode(writer, m.getContent());
        }
        if(m.getLastChange() != null){
            writer.setNextPropertyName("lastChange");
            encoder0.encode(writer, m.getLastChange());
        }
        if(m.getRegion() != null){
            writer.setNextPropertyName("region");
            encoder0.encode(writer, m.getRegion());
        }
        if(m.getRegionObject() != null){
            writer.setNextPropertyName("regionObject");
            encoder0.encode(writer, m.getRegionObject());
        }
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
    protected cz.artique.shared.model.source.PageChangeSource jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.source.PageChangeSource m = new cz.artique.shared.model.source.PageChangeSource();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("content");
        m.setContent(decoder0.decode(reader, m.getContent()));
        reader = rootReader.newObjectReader("lastChange");
        m.setLastChange(decoder0.decode(reader, m.getLastChange()));
        reader = rootReader.newObjectReader("region");
        m.setRegion(decoder0.decode(reader, m.getRegion()));
        reader = rootReader.newObjectReader("regionObject");
        m.setRegionObject(decoder0.decode(reader, m.getRegionObject(), cz.artique.shared.model.source.Region.class));
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