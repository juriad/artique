package cz.artique.server.meta.source;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-05-24 00:45:05")
/** */
public final class PageChangeCrawlerDataMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.source.PageChangeCrawlerData> {

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.source.PageChangeCrawlerData, com.google.appengine.api.datastore.Text> content = new org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.source.PageChangeCrawlerData, com.google.appengine.api.datastore.Text>(this, "content", "content", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeCrawlerData, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeCrawlerData, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeCrawlerData, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.PageChangeCrawlerData, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final PageChangeCrawlerDataMeta slim3_singleton = new PageChangeCrawlerDataMeta();

    /**
     * @return the singleton
     */
    public static PageChangeCrawlerDataMeta get() {
       return slim3_singleton;
    }

    /** */
    public PageChangeCrawlerDataMeta() {
        super("CrawlerData", cz.artique.shared.model.source.PageChangeCrawlerData.class, java.util.Arrays.asList("cz.artique.shared.model.source.PageChangeCrawlerData"));
    }

    @Override
    public cz.artique.shared.model.source.PageChangeCrawlerData entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.source.PageChangeCrawlerData model = new cz.artique.shared.model.source.PageChangeCrawlerData();
        model.setContent((com.google.appengine.api.datastore.Text) entity.getProperty("content"));
        model.setKey(entity.getKey());
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.source.PageChangeCrawlerData m = (cz.artique.shared.model.source.PageChangeCrawlerData) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("content", m.getContent());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        entity.setProperty("slim3.classHierarchyList", classHierarchyList);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.source.PageChangeCrawlerData m = (cz.artique.shared.model.source.PageChangeCrawlerData) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.source.PageChangeCrawlerData m = (cz.artique.shared.model.source.PageChangeCrawlerData) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.source.PageChangeCrawlerData m = (cz.artique.shared.model.source.PageChangeCrawlerData) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.source.PageChangeCrawlerData m = (cz.artique.shared.model.source.PageChangeCrawlerData) model;
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
        cz.artique.shared.model.source.PageChangeCrawlerData m = (cz.artique.shared.model.source.PageChangeCrawlerData) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getContent() != null && m.getContent().getValue() != null){
            writer.setNextPropertyName("content");
            encoder0.encode(writer, m.getContent());
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
    protected cz.artique.shared.model.source.PageChangeCrawlerData jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.source.PageChangeCrawlerData m = new cz.artique.shared.model.source.PageChangeCrawlerData();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("content");
        m.setContent(decoder0.decode(reader, m.getContent()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}