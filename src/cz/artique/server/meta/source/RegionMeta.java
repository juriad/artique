package cz.artique.server.meta.source;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-07-29 10:27:24")
/** */
public final class RegionMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.source.Region> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Region, com.google.appengine.api.datastore.Key> htmlSource = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Region, com.google.appengine.api.datastore.Key>(this, "htmlSource", "htmlSource", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Region, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Region, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.source.Region> name = new org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.source.Region>(this, "name", "name");

    /** */
    public final org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.source.Region> negativeSelector = new org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.source.Region>(this, "negativeSelector", "negativeSelector");

    /** */
    public final org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.source.Region> positiveSelector = new org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.source.Region>(this, "positiveSelector", "positiveSelector");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Region, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Region, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final RegionMeta slim3_singleton = new RegionMeta();

    /**
     * @return the singleton
     */
    public static RegionMeta get() {
       return slim3_singleton;
    }

    /** */
    public RegionMeta() {
        super("Region", cz.artique.shared.model.source.Region.class);
    }

    @Override
    public cz.artique.shared.model.source.Region entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.source.Region model = new cz.artique.shared.model.source.Region();
        model.setHtmlSource((com.google.appengine.api.datastore.Key) entity.getProperty("htmlSource"));
        model.setKey(entity.getKey());
        model.setName((java.lang.String) entity.getProperty("name"));
        model.setNegativeSelector((java.lang.String) entity.getProperty("negativeSelector"));
        model.setPositiveSelector((java.lang.String) entity.getProperty("positiveSelector"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.source.Region m = (cz.artique.shared.model.source.Region) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("htmlSource", m.getHtmlSource());
        entity.setUnindexedProperty("name", m.getName());
        entity.setUnindexedProperty("negativeSelector", m.getNegativeSelector());
        entity.setUnindexedProperty("positiveSelector", m.getPositiveSelector());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.source.Region m = (cz.artique.shared.model.source.Region) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.source.Region m = (cz.artique.shared.model.source.Region) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.source.Region m = (cz.artique.shared.model.source.Region) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.source.Region m = (cz.artique.shared.model.source.Region) model;
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
        cz.artique.shared.model.source.Region m = (cz.artique.shared.model.source.Region) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getHtmlSource() != null){
            writer.setNextPropertyName("htmlSource");
            encoder0.encode(writer, m.getHtmlSource());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getName() != null){
            writer.setNextPropertyName("name");
            encoder0.encode(writer, m.getName());
        }
        if(m.getNegativeSelector() != null){
            writer.setNextPropertyName("negativeSelector");
            encoder0.encode(writer, m.getNegativeSelector());
        }
        if(m.getPositiveSelector() != null){
            writer.setNextPropertyName("positiveSelector");
            encoder0.encode(writer, m.getPositiveSelector());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.source.Region jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.source.Region m = new cz.artique.shared.model.source.Region();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("htmlSource");
        m.setHtmlSource(decoder0.decode(reader, m.getHtmlSource()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("name");
        m.setName(decoder0.decode(reader, m.getName()));
        reader = rootReader.newObjectReader("negativeSelector");
        m.setNegativeSelector(decoder0.decode(reader, m.getNegativeSelector()));
        reader = rootReader.newObjectReader("positiveSelector");
        m.setPositiveSelector(decoder0.decode(reader, m.getPositiveSelector()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}