package cz.artique.server.meta.source;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-05-27 12:39:21")
/** */
public final class StatsMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.source.Stats> {

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.Stats> error = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.Stats>(this, "error", "error");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, java.lang.Integer> itemsAcquired = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, java.lang.Integer>(this, "itemsAcquired", "itemsAcquired", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, java.util.Date> probeDate = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, java.util.Date>(this, "probeDate", "probeDate", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, com.google.appengine.api.datastore.Key> source = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, com.google.appengine.api.datastore.Key>(this, "source", "source", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Stats, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final StatsMeta slim3_singleton = new StatsMeta();

    /**
     * @return the singleton
     */
    public static StatsMeta get() {
       return slim3_singleton;
    }

    /** */
    public StatsMeta() {
        super("Stats", cz.artique.shared.model.source.Stats.class);
    }

    @Override
    public cz.artique.shared.model.source.Stats entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.source.Stats model = new cz.artique.shared.model.source.Stats();
        model.setError((java.lang.String) entity.getProperty("error"));
        model.setItemsAcquired(longToPrimitiveInt((java.lang.Long) entity.getProperty("itemsAcquired")));
        model.setKey(entity.getKey());
        model.setProbeDate((java.util.Date) entity.getProperty("probeDate"));
        model.setSource((com.google.appengine.api.datastore.Key) entity.getProperty("source"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.source.Stats m = (cz.artique.shared.model.source.Stats) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("error", m.getError());
        entity.setProperty("itemsAcquired", m.getItemsAcquired());
        entity.setProperty("probeDate", m.getProbeDate());
        entity.setProperty("source", m.getSource());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.source.Stats m = (cz.artique.shared.model.source.Stats) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.source.Stats m = (cz.artique.shared.model.source.Stats) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.source.Stats m = (cz.artique.shared.model.source.Stats) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.source.Stats m = (cz.artique.shared.model.source.Stats) model;
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
        cz.artique.shared.model.source.Stats m = (cz.artique.shared.model.source.Stats) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getError() != null){
            writer.setNextPropertyName("error");
            encoder0.encode(writer, m.getError());
        }
        writer.setNextPropertyName("itemsAcquired");
        encoder0.encode(writer, m.getItemsAcquired());
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getProbeDate() != null){
            writer.setNextPropertyName("probeDate");
            encoder0.encode(writer, m.getProbeDate());
        }
        if(m.getSource() != null){
            writer.setNextPropertyName("source");
            encoder0.encode(writer, m.getSource());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.source.Stats jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.source.Stats m = new cz.artique.shared.model.source.Stats();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("error");
        m.setError(decoder0.decode(reader, m.getError()));
        reader = rootReader.newObjectReader("itemsAcquired");
        m.setItemsAcquired(decoder0.decode(reader, m.getItemsAcquired()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("probeDate");
        m.setProbeDate(decoder0.decode(reader, m.getProbeDate()));
        reader = rootReader.newObjectReader("source");
        m.setSource(decoder0.decode(reader, m.getSource()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}