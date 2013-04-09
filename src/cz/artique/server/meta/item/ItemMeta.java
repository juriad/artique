package cz.artique.server.meta.item;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-04-09 20:21:06")
/** */
public final class ItemMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.item.Item> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, java.util.Date> added = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, java.util.Date>(this, "added", "added", java.util.Date.class);

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.item.Item, com.google.appengine.api.datastore.Text> content = new org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.item.Item, com.google.appengine.api.datastore.Text>(this, "content", "content", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, cz.artique.shared.model.item.ContentType> contentType = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, cz.artique.shared.model.item.ContentType>(this, "contentType", "contentType", cz.artique.shared.model.item.ContentType.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.item.Item> hash = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.item.Item>(this, "hash", "hash");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, java.util.Date> published = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, java.util.Date>(this, "published", "published", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, com.google.appengine.api.datastore.Key> source = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, com.google.appengine.api.datastore.Key>(this, "source", "source", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.item.Item> title = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.item.Item>(this, "title", "title");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, com.google.appengine.api.datastore.Link> url = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, com.google.appengine.api.datastore.Link>(this, "url", "url", com.google.appengine.api.datastore.Link.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.Item, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final ItemMeta slim3_singleton = new ItemMeta();

    /**
     * @return the singleton
     */
    public static ItemMeta get() {
       return slim3_singleton;
    }

    /** */
    public ItemMeta() {
        super("Item", cz.artique.shared.model.item.Item.class);
    }

    @Override
    public cz.artique.shared.model.item.Item entityToModel(com.google.appengine.api.datastore.Entity entity) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.item.Item) is abstract.");
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.item.Item) is abstract.");
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.item.Item m = (cz.artique.shared.model.item.Item) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.item.Item m = (cz.artique.shared.model.item.Item) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.item.Item m = (cz.artique.shared.model.item.Item) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.item.Item) is abstract.");
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.item.Item m = (cz.artique.shared.model.item.Item) model;
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
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.item.Item) is abstract.");
    }

    @Override
    protected cz.artique.shared.model.item.Item jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.item.Item) is abstract.");
    }
}