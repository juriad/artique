package cz.artique.server.meta.label;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-05-30 18:33:33")
/** */
public final class ListFilterMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.label.ListFilter> {

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.ListFilter, java.util.Date> endTo = new org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.ListFilter, java.util.Date>(this, "endTo", "endTo", java.util.Date.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.ListFilter> exportAlias = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.ListFilter>(this, "exportAlias", "exportAlias");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.ListFilter, com.google.appengine.api.datastore.Key> filter = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.ListFilter, com.google.appengine.api.datastore.Key>(this, "filter", "filter", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.ListFilter> hierarchy = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.ListFilter>(this, "hierarchy", "hierarchy");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.ListFilter, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.ListFilter, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.ListFilter> name = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.ListFilter>(this, "name", "name");

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.ListFilter, cz.artique.shared.model.label.ListFilterOrder> order = new org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.ListFilter, cz.artique.shared.model.label.ListFilterOrder>(this, "order", "order", cz.artique.shared.model.label.ListFilterOrder.class);

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.ListFilter, java.lang.Boolean> read = new org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.ListFilter, java.lang.Boolean>(this, "read", "read", java.lang.Boolean.class);

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.ListFilter, java.util.Date> startFrom = new org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.ListFilter, java.util.Date>(this, "startFrom", "startFrom", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.ListFilter, com.google.appengine.api.users.User> user = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.ListFilter, com.google.appengine.api.users.User>(this, "user", "user", com.google.appengine.api.users.User.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.ListFilter, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.ListFilter, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final ListFilterMeta slim3_singleton = new ListFilterMeta();

    /**
     * @return the singleton
     */
    public static ListFilterMeta get() {
       return slim3_singleton;
    }

    /** */
    public ListFilterMeta() {
        super("ListFilter", cz.artique.shared.model.label.ListFilter.class);
    }

    @Override
    public cz.artique.shared.model.label.ListFilter entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.label.ListFilter model = new cz.artique.shared.model.label.ListFilter();
        model.setEndTo((java.util.Date) entity.getProperty("endTo"));
        model.setExportAlias((java.lang.String) entity.getProperty("exportAlias"));
        model.setFilter((com.google.appengine.api.datastore.Key) entity.getProperty("filter"));
        model.setHierarchy((java.lang.String) entity.getProperty("hierarchy"));
        model.setKey(entity.getKey());
        model.setName((java.lang.String) entity.getProperty("name"));
        model.setOrder(stringToEnum(cz.artique.shared.model.label.ListFilterOrder.class, (java.lang.String) entity.getProperty("order")));
        model.setRead((java.lang.Boolean) entity.getProperty("read"));
        model.setStartFrom((java.util.Date) entity.getProperty("startFrom"));
        model.setUser((com.google.appengine.api.users.User) entity.getProperty("user"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.label.ListFilter m = (cz.artique.shared.model.label.ListFilter) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("endTo", m.getEndTo());
        entity.setProperty("exportAlias", m.getExportAlias());
        entity.setUnindexedProperty("filter", m.getFilter());
        entity.setProperty("hierarchy", m.getHierarchy());
        entity.setProperty("name", m.getName());
        entity.setUnindexedProperty("order", enumToString(m.getOrder()));
        entity.setUnindexedProperty("read", m.getRead());
        entity.setUnindexedProperty("startFrom", m.getStartFrom());
        entity.setProperty("user", m.getUser());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.label.ListFilter m = (cz.artique.shared.model.label.ListFilter) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.label.ListFilter m = (cz.artique.shared.model.label.ListFilter) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.label.ListFilter m = (cz.artique.shared.model.label.ListFilter) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.label.ListFilter m = (cz.artique.shared.model.label.ListFilter) model;
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
        cz.artique.shared.model.label.ListFilter m = (cz.artique.shared.model.label.ListFilter) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getEndTo() != null){
            writer.setNextPropertyName("endTo");
            encoder0.encode(writer, m.getEndTo());
        }
        if(m.getExportAlias() != null){
            writer.setNextPropertyName("exportAlias");
            encoder0.encode(writer, m.getExportAlias());
        }
        if(m.getFilter() != null){
            writer.setNextPropertyName("filter");
            encoder0.encode(writer, m.getFilter());
        }
        if(m.getFilterObject() != null){
            writer.setNextPropertyName("filterObject");
            encoder0.encode(writer, m.getFilterObject());
        }
        if(m.getHierarchy() != null){
            writer.setNextPropertyName("hierarchy");
            encoder0.encode(writer, m.getHierarchy());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getName() != null){
            writer.setNextPropertyName("name");
            encoder0.encode(writer, m.getName());
        }
        if(m.getOrder() != null){
            writer.setNextPropertyName("order");
            encoder0.encode(writer, m.getOrder());
        }
        if(m.getRead() != null){
            writer.setNextPropertyName("read");
            encoder0.encode(writer, m.getRead());
        }
        if(m.getStartFrom() != null){
            writer.setNextPropertyName("startFrom");
            encoder0.encode(writer, m.getStartFrom());
        }
        if(m.getUser() != null){
            writer.setNextPropertyName("user");
            encoder0.encode(writer, m.getUser());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.label.ListFilter jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.label.ListFilter m = new cz.artique.shared.model.label.ListFilter();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("endTo");
        m.setEndTo(decoder0.decode(reader, m.getEndTo()));
        reader = rootReader.newObjectReader("exportAlias");
        m.setExportAlias(decoder0.decode(reader, m.getExportAlias()));
        reader = rootReader.newObjectReader("filter");
        m.setFilter(decoder0.decode(reader, m.getFilter()));
        reader = rootReader.newObjectReader("filterObject");
        m.setFilterObject(decoder0.decode(reader, m.getFilterObject(), cz.artique.shared.model.label.Filter.class));
        reader = rootReader.newObjectReader("hierarchy");
        m.setHierarchy(decoder0.decode(reader, m.getHierarchy()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("name");
        m.setName(decoder0.decode(reader, m.getName()));
        reader = rootReader.newObjectReader("order");
        m.setOrder(decoder0.decode(reader, m.getOrder(), cz.artique.shared.model.label.ListFilterOrder.class));
        reader = rootReader.newObjectReader("read");
        m.setRead(decoder0.decode(reader, m.getRead()));
        reader = rootReader.newObjectReader("startFrom");
        m.setStartFrom(decoder0.decode(reader, m.getStartFrom()));
        reader = rootReader.newObjectReader("user");
        m.setUser(decoder0.decode(reader, m.getUser()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}