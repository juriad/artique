package cz.artique.server.meta.source;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-07-31 18:42:55")
/** */
public final class UserSourceMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.source.UserSource> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key> crawlerData = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key>(this, "crawlerData", "crawlerData", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CollectionAttributeMeta<cz.artique.shared.model.source.UserSource, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key> defaultLabels = new org.slim3.datastore.CollectionAttributeMeta<cz.artique.shared.model.source.UserSource, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key>(this, "defaultLabels", "defaultLabels", java.util.List.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource> hierarchy = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource>(this, "hierarchy", "hierarchy");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key> label = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key>(this, "label", "label", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource> name = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource>(this, "name", "name");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key> region = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key>(this, "region", "region", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key> source = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key>(this, "source", "source", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, cz.artique.shared.model.source.SourceType> sourceType = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, cz.artique.shared.model.source.SourceType>(this, "sourceType", "sourceType", cz.artique.shared.model.source.SourceType.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource> userId = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource>(this, "userId", "userId");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, java.lang.Boolean> watching = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, java.lang.Boolean>(this, "watching", "watching", boolean.class);

    private static final UserSourceMeta slim3_singleton = new UserSourceMeta();

    /**
     * @return the singleton
     */
    public static UserSourceMeta get() {
       return slim3_singleton;
    }

    /** */
    public UserSourceMeta() {
        super("UserSource", cz.artique.shared.model.source.UserSource.class);
    }

    @Override
    public cz.artique.shared.model.source.UserSource entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.source.UserSource model = new cz.artique.shared.model.source.UserSource();
        model.setCrawlerData((com.google.appengine.api.datastore.Key) entity.getProperty("crawlerData"));
        model.setDefaultLabels(toList(com.google.appengine.api.datastore.Key.class, entity.getProperty("defaultLabels")));
        model.setHierarchy((java.lang.String) entity.getProperty("hierarchy"));
        model.setKey(entity.getKey());
        model.setLabel((com.google.appengine.api.datastore.Key) entity.getProperty("label"));
        model.setName((java.lang.String) entity.getProperty("name"));
        model.setRegion((com.google.appengine.api.datastore.Key) entity.getProperty("region"));
        model.setSource((com.google.appengine.api.datastore.Key) entity.getProperty("source"));
        model.setSourceType(stringToEnum(cz.artique.shared.model.source.SourceType.class, (java.lang.String) entity.getProperty("sourceType")));
        model.setUserId((java.lang.String) entity.getProperty("userId"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        model.setWatching(booleanToPrimitiveBoolean((java.lang.Boolean) entity.getProperty("watching")));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.source.UserSource m = (cz.artique.shared.model.source.UserSource) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("crawlerData", m.getCrawlerData());
        entity.setProperty("defaultLabels", m.getDefaultLabels());
        entity.setProperty("hierarchy", m.getHierarchy());
        entity.setProperty("label", m.getLabel());
        entity.setProperty("name", m.getName());
        entity.setProperty("region", m.getRegion());
        entity.setProperty("source", m.getSource());
        entity.setProperty("sourceType", enumToString(m.getSourceType()));
        entity.setProperty("userId", m.getUserId());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("watching", m.isWatching());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.source.UserSource m = (cz.artique.shared.model.source.UserSource) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.source.UserSource m = (cz.artique.shared.model.source.UserSource) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.source.UserSource m = (cz.artique.shared.model.source.UserSource) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.source.UserSource m = (cz.artique.shared.model.source.UserSource) model;
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
        cz.artique.shared.model.source.UserSource m = (cz.artique.shared.model.source.UserSource) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getCrawlerData() != null){
            writer.setNextPropertyName("crawlerData");
            encoder0.encode(writer, m.getCrawlerData());
        }
        if(m.getDefaultLabels() != null){
            writer.setNextPropertyName("defaultLabels");
            writer.beginArray();
            for(com.google.appengine.api.datastore.Key v : m.getDefaultLabels()){
                encoder0.encode(writer, v);
            }
            writer.endArray();
        }
        if(m.getHierarchy() != null){
            writer.setNextPropertyName("hierarchy");
            encoder0.encode(writer, m.getHierarchy());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getLabel() != null){
            writer.setNextPropertyName("label");
            encoder0.encode(writer, m.getLabel());
        }
        if(m.getLabelObject() != null){
            writer.setNextPropertyName("labelObject");
            encoder0.encode(writer, m.getLabelObject());
        }
        if(m.getName() != null){
            writer.setNextPropertyName("name");
            encoder0.encode(writer, m.getName());
        }
        if(m.getRegion() != null){
            writer.setNextPropertyName("region");
            encoder0.encode(writer, m.getRegion());
        }
        if(m.getRegionObject() != null){
            writer.setNextPropertyName("regionObject");
            encoder0.encode(writer, m.getRegionObject());
        }
        if(m.getSource() != null){
            writer.setNextPropertyName("source");
            encoder0.encode(writer, m.getSource());
        }
        if(m.getSourceObject() != null){
            writer.setNextPropertyName("sourceObject");
            encoder0.encode(writer, m.getSourceObject());
        }
        if(m.getSourceType() != null){
            writer.setNextPropertyName("sourceType");
            encoder0.encode(writer, m.getSourceType());
        }
        if(m.getUserId() != null){
            writer.setNextPropertyName("userId");
            encoder0.encode(writer, m.getUserId());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.setNextPropertyName("watching");
        encoder0.encode(writer, m.isWatching());
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.source.UserSource jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.source.UserSource m = new cz.artique.shared.model.source.UserSource();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("crawlerData");
        m.setCrawlerData(decoder0.decode(reader, m.getCrawlerData()));
        reader = rootReader.newObjectReader("defaultLabels");
        {
            java.util.ArrayList<com.google.appengine.api.datastore.Key> elements = new java.util.ArrayList<com.google.appengine.api.datastore.Key>();
            org.slim3.datastore.json.JsonArrayReader r = rootReader.newArrayReader("defaultLabels");
            if(r != null){
                reader = r;
                int n = r.length();
                for(int i = 0; i < n; i++){
                    r.setIndex(i);
                    com.google.appengine.api.datastore.Key v = decoder0.decode(reader, (com.google.appengine.api.datastore.Key)null)                    ;
                    if(v != null){
                        elements.add(v);
                    }
                }
                m.setDefaultLabels(elements);
            }
        }
        reader = rootReader.newObjectReader("hierarchy");
        m.setHierarchy(decoder0.decode(reader, m.getHierarchy()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("label");
        m.setLabel(decoder0.decode(reader, m.getLabel()));
        reader = rootReader.newObjectReader("labelObject");
        m.setLabelObject(decoder0.decode(reader, m.getLabelObject(), cz.artique.shared.model.label.Label.class));
        reader = rootReader.newObjectReader("name");
        m.setName(decoder0.decode(reader, m.getName()));
        reader = rootReader.newObjectReader("region");
        m.setRegion(decoder0.decode(reader, m.getRegion()));
        reader = rootReader.newObjectReader("regionObject");
        m.setRegionObject(decoder0.decode(reader, m.getRegionObject(), cz.artique.shared.model.source.Region.class));
        reader = rootReader.newObjectReader("source");
        m.setSource(decoder0.decode(reader, m.getSource()));
        reader = rootReader.newObjectReader("sourceObject");
        m.setSourceObject(decoder0.decode(reader, m.getSourceObject(), cz.artique.shared.model.source.Source.class));
        reader = rootReader.newObjectReader("sourceType");
        m.setSourceType(decoder0.decode(reader, m.getSourceType(), cz.artique.shared.model.source.SourceType.class));
        reader = rootReader.newObjectReader("userId");
        m.setUserId(decoder0.decode(reader, m.getUserId()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        reader = rootReader.newObjectReader("watching");
        m.setWatching(decoder0.decode(reader, m.isWatching()));
        return m;
    }
}