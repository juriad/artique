package cz.artique.server.meta.source;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-07-25 16:29:21")
/** */
public final class HTMLSourceMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.source.HTMLSource> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Boolean> enabled = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Boolean>(this, "enabled", "enabled", boolean.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Boolean> enqued = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Boolean>(this, "enqued", "enqued", boolean.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Integer> errorSequence = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Integer>(this, "errorSequence", "errorSequence", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.util.Date> lastCheck = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.util.Date>(this, "lastCheck", "lastCheck", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.util.Date> nextCheck = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.util.Date>(this, "nextCheck", "nextCheck", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, com.google.appengine.api.datastore.Link> url = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, com.google.appengine.api.datastore.Link>(this, "url", "url", com.google.appengine.api.datastore.Link.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Integer> usage = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Integer>(this, "usage", "usage", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.HTMLSource, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final HTMLSourceMeta slim3_singleton = new HTMLSourceMeta();

    /**
     * @return the singleton
     */
    public static HTMLSourceMeta get() {
       return slim3_singleton;
    }

    /** */
    public HTMLSourceMeta() {
        super("Source", cz.artique.shared.model.source.HTMLSource.class, java.util.Arrays.asList("cz.artique.shared.model.source.HTMLSource"));
    }

    @Override
    public cz.artique.shared.model.source.HTMLSource entityToModel(com.google.appengine.api.datastore.Entity entity) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.HTMLSource) is abstract.");
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.HTMLSource) is abstract.");
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.source.HTMLSource m = (cz.artique.shared.model.source.HTMLSource) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.source.HTMLSource m = (cz.artique.shared.model.source.HTMLSource) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.source.HTMLSource m = (cz.artique.shared.model.source.HTMLSource) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.HTMLSource) is abstract.");
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.source.HTMLSource m = (cz.artique.shared.model.source.HTMLSource) model;
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
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.HTMLSource) is abstract.");
    }

    @Override
    protected cz.artique.shared.model.source.HTMLSource jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.HTMLSource) is abstract.");
    }
}