package cz.artique.server.meta.item;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-06-11 12:49:27")
/** */
public final class LinkItemMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.item.LinkItem> {

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, java.util.Date> added = new org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, java.util.Date>(this, "added", "added", java.util.Date.class);

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, com.google.appengine.api.datastore.Text> content = new org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, com.google.appengine.api.datastore.Text>(this, "content", "content", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, cz.artique.shared.model.item.ContentType> contentType = new org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, cz.artique.shared.model.item.ContentType>(this, "contentType", "contentType", cz.artique.shared.model.item.ContentType.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.item.LinkItem> hash = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.item.LinkItem>(this, "hash", "hash");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.LinkItem, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.LinkItem, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, java.util.Date> published = new org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, java.util.Date>(this, "published", "published", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.LinkItem, com.google.appengine.api.datastore.Key> source = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.LinkItem, com.google.appengine.api.datastore.Key>(this, "source", "source", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem> title = new org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem>(this, "title", "title");

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, com.google.appengine.api.datastore.Link> url = new org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.item.LinkItem, com.google.appengine.api.datastore.Link>(this, "url", "url", com.google.appengine.api.datastore.Link.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.LinkItem, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.LinkItem, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final LinkItemMeta slim3_singleton = new LinkItemMeta();

    /**
     * @return the singleton
     */
    public static LinkItemMeta get() {
       return slim3_singleton;
    }

    /** */
    public LinkItemMeta() {
        super("Item", cz.artique.shared.model.item.LinkItem.class, java.util.Arrays.asList("cz.artique.shared.model.item.LinkItem"));
    }

    @Override
    public cz.artique.shared.model.item.LinkItem entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.item.LinkItem model = new cz.artique.shared.model.item.LinkItem();
        model.setAdded((java.util.Date) entity.getProperty("added"));
        model.setContent((com.google.appengine.api.datastore.Text) entity.getProperty("content"));
        model.setContentType(stringToEnum(cz.artique.shared.model.item.ContentType.class, (java.lang.String) entity.getProperty("contentType")));
        model.setHash((java.lang.String) entity.getProperty("hash"));
        model.setKey(entity.getKey());
        model.setPublished((java.util.Date) entity.getProperty("published"));
        model.setSource((com.google.appengine.api.datastore.Key) entity.getProperty("source"));
        model.setTitle((java.lang.String) entity.getProperty("title"));
        model.setUrl((com.google.appengine.api.datastore.Link) entity.getProperty("url"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.item.LinkItem m = (cz.artique.shared.model.item.LinkItem) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("added", m.getAdded());
        entity.setUnindexedProperty("content", m.getContent());
        entity.setUnindexedProperty("contentType", enumToString(m.getContentType()));
        entity.setProperty("hash", m.getHash());
        entity.setUnindexedProperty("published", m.getPublished());
        entity.setProperty("source", m.getSource());
        entity.setUnindexedProperty("title", m.getTitle());
        entity.setUnindexedProperty("url", m.getUrl());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        entity.setProperty("slim3.classHierarchyList", classHierarchyList);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.item.LinkItem m = (cz.artique.shared.model.item.LinkItem) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.item.LinkItem m = (cz.artique.shared.model.item.LinkItem) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.item.LinkItem m = (cz.artique.shared.model.item.LinkItem) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.item.LinkItem m = (cz.artique.shared.model.item.LinkItem) model;
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
        cz.artique.shared.model.item.LinkItem m = (cz.artique.shared.model.item.LinkItem) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getAdded() != null){
            writer.setNextPropertyName("added");
            encoder0.encode(writer, m.getAdded());
        }
        if(m.getContent() != null && m.getContent().getValue() != null){
            writer.setNextPropertyName("content");
            encoder0.encode(writer, m.getContent());
        }
        if(m.getContentType() != null){
            writer.setNextPropertyName("contentType");
            encoder0.encode(writer, m.getContentType());
        }
        if(m.getHash() != null){
            writer.setNextPropertyName("hash");
            encoder0.encode(writer, m.getHash());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getPublished() != null){
            writer.setNextPropertyName("published");
            encoder0.encode(writer, m.getPublished());
        }
        if(m.getSource() != null){
            writer.setNextPropertyName("source");
            encoder0.encode(writer, m.getSource());
        }
        if(m.getSourceObject() != null){
            writer.setNextPropertyName("sourceObject");
            encoder0.encode(writer, m.getSourceObject());
        }
        if(m.getTitle() != null){
            writer.setNextPropertyName("title");
            encoder0.encode(writer, m.getTitle());
        }
        if(m.getUrl() != null){
            writer.setNextPropertyName("url");
            encoder0.encode(writer, m.getUrl());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.item.LinkItem jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.item.LinkItem m = new cz.artique.shared.model.item.LinkItem();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("added");
        m.setAdded(decoder0.decode(reader, m.getAdded()));
        reader = rootReader.newObjectReader("content");
        m.setContent(decoder0.decode(reader, m.getContent()));
        reader = rootReader.newObjectReader("contentType");
        m.setContentType(decoder0.decode(reader, m.getContentType(), cz.artique.shared.model.item.ContentType.class));
        reader = rootReader.newObjectReader("hash");
        m.setHash(decoder0.decode(reader, m.getHash()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("published");
        m.setPublished(decoder0.decode(reader, m.getPublished()));
        reader = rootReader.newObjectReader("source");
        m.setSource(decoder0.decode(reader, m.getSource()));
        reader = rootReader.newObjectReader("sourceObject");
        m.setSourceObject(decoder0.decode(reader, m.getSourceObject(), cz.artique.shared.model.source.Source.class));
        reader = rootReader.newObjectReader("title");
        m.setTitle(decoder0.decode(reader, m.getTitle()));
        reader = rootReader.newObjectReader("url");
        m.setUrl(decoder0.decode(reader, m.getUrl()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}