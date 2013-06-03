package cz.artique.server.meta.source;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-06-03 18:01:00")
/** */
public final class SourceMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.source.Source> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Boolean> enabled = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Boolean>(this, "enabled", "enabled", boolean.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Boolean> enqued = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Boolean>(this, "enqued", "enqued", boolean.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Integer> errorSequence = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Integer>(this, "errorSequence", "errorSequence", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.util.Date> lastCheck = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.util.Date>(this, "lastCheck", "lastCheck", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.util.Date> nextCheck = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.util.Date>(this, "nextCheck", "nextCheck", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, com.google.appengine.api.datastore.Link> url = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, com.google.appengine.api.datastore.Link>(this, "url", "url", com.google.appengine.api.datastore.Link.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Integer> usage = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Integer>(this, "usage", "usage", int.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.Source, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final SourceMeta slim3_singleton = new SourceMeta();

    /**
     * @return the singleton
     */
    public static SourceMeta get() {
       return slim3_singleton;
    }

    /** */
    public SourceMeta() {
        super("Source", cz.artique.shared.model.source.Source.class);
    }

    @Override
    public cz.artique.shared.model.source.Source entityToModel(com.google.appengine.api.datastore.Entity entity) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.Source) is abstract.");
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.Source) is abstract.");
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.source.Source m = (cz.artique.shared.model.source.Source) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.source.Source m = (cz.artique.shared.model.source.Source) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.source.Source m = (cz.artique.shared.model.source.Source) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.Source) is abstract.");
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.source.Source m = (cz.artique.shared.model.source.Source) model;
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
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.Source) is abstract.");
    }

    @Override
    protected cz.artique.shared.model.source.Source jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        throw new java.lang.UnsupportedOperationException("The class(cz.artique.shared.model.source.Source) is abstract.");
    }
}