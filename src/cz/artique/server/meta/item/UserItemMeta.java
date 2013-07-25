package cz.artique.server.meta.item;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-07-25 16:29:22")
/** */
public final class UserItemMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.item.UserItem> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, java.util.Date> added = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, java.util.Date>(this, "added", "added", java.util.Date.class);

    /** */
    public final org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.item.UserItem> backupBlobKey = new org.slim3.datastore.StringUnindexedAttributeMeta<cz.artique.shared.model.item.UserItem>(this, "backupBlobKey", "backupBlobKey");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, com.google.appengine.api.datastore.Key> item = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, com.google.appengine.api.datastore.Key>(this, "item", "item", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CollectionAttributeMeta<cz.artique.shared.model.item.UserItem, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key> labels = new org.slim3.datastore.CollectionAttributeMeta<cz.artique.shared.model.item.UserItem, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key>(this, "labels", "labels", java.util.List.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, java.util.Date> lastChanged = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, java.util.Date>(this, "lastChanged", "lastChanged", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, java.lang.Boolean> read = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, java.lang.Boolean>(this, "read", "read", boolean.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.item.UserItem> userId = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.item.UserItem>(this, "userId", "userId");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, com.google.appengine.api.datastore.Key> userSource = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, com.google.appengine.api.datastore.Key>(this, "userSource", "userSource", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.item.UserItem, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final UserItemMeta slim3_singleton = new UserItemMeta();

    /**
     * @return the singleton
     */
    public static UserItemMeta get() {
       return slim3_singleton;
    }

    /** */
    public UserItemMeta() {
        super("UserItem", cz.artique.shared.model.item.UserItem.class);
    }

    @Override
    public cz.artique.shared.model.item.UserItem entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.item.UserItem model = new cz.artique.shared.model.item.UserItem();
        model.setAdded((java.util.Date) entity.getProperty("added"));
        model.setBackupBlobKey((java.lang.String) entity.getProperty("backupBlobKey"));
        model.setItem((com.google.appengine.api.datastore.Key) entity.getProperty("item"));
        model.setKey(entity.getKey());
        model.setLabels(toList(com.google.appengine.api.datastore.Key.class, entity.getProperty("labels")));
        model.setLastChanged((java.util.Date) entity.getProperty("lastChanged"));
        model.setRead(booleanToPrimitiveBoolean((java.lang.Boolean) entity.getProperty("read")));
        model.setUserId((java.lang.String) entity.getProperty("userId"));
        model.setUserSource((com.google.appengine.api.datastore.Key) entity.getProperty("userSource"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.item.UserItem m = (cz.artique.shared.model.item.UserItem) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("added", m.getAdded());
        entity.setUnindexedProperty("backupBlobKey", m.getBackupBlobKey());
        entity.setProperty("item", m.getItem());
        entity.setProperty("labels", m.getLabels());
        entity.setProperty("lastChanged", m.getLastChanged());
        entity.setProperty("read", m.isRead());
        entity.setProperty("userId", m.getUserId());
        entity.setProperty("userSource", m.getUserSource());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.item.UserItem m = (cz.artique.shared.model.item.UserItem) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.item.UserItem m = (cz.artique.shared.model.item.UserItem) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.item.UserItem m = (cz.artique.shared.model.item.UserItem) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.item.UserItem m = (cz.artique.shared.model.item.UserItem) model;
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
        cz.artique.shared.model.item.UserItem m = (cz.artique.shared.model.item.UserItem) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getAdded() != null){
            writer.setNextPropertyName("added");
            encoder0.encode(writer, m.getAdded());
        }
        if(m.getBackupBlobKey() != null){
            writer.setNextPropertyName("backupBlobKey");
            encoder0.encode(writer, m.getBackupBlobKey());
        }
        if(m.getItem() != null){
            writer.setNextPropertyName("item");
            encoder0.encode(writer, m.getItem());
        }
        if(m.getItemObject() != null){
            writer.setNextPropertyName("itemObject");
            encoder0.encode(writer, m.getItemObject());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getLabels() != null){
            writer.setNextPropertyName("labels");
            writer.beginArray();
            for(com.google.appengine.api.datastore.Key v : m.getLabels()){
                encoder0.encode(writer, v);
            }
            writer.endArray();
        }
        if(m.getLastChanged() != null){
            writer.setNextPropertyName("lastChanged");
            encoder0.encode(writer, m.getLastChanged());
        }
        writer.setNextPropertyName("read");
        encoder0.encode(writer, m.isRead());
        if(m.getUserId() != null){
            writer.setNextPropertyName("userId");
            encoder0.encode(writer, m.getUserId());
        }
        if(m.getUserSource() != null){
            writer.setNextPropertyName("userSource");
            encoder0.encode(writer, m.getUserSource());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.item.UserItem jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.item.UserItem m = new cz.artique.shared.model.item.UserItem();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("added");
        m.setAdded(decoder0.decode(reader, m.getAdded()));
        reader = rootReader.newObjectReader("backupBlobKey");
        m.setBackupBlobKey(decoder0.decode(reader, m.getBackupBlobKey()));
        reader = rootReader.newObjectReader("item");
        m.setItem(decoder0.decode(reader, m.getItem()));
        reader = rootReader.newObjectReader("itemObject");
        m.setItemObject(decoder0.decode(reader, m.getItemObject(), cz.artique.shared.model.item.Item.class));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("labels");
        {
            java.util.ArrayList<com.google.appengine.api.datastore.Key> elements = new java.util.ArrayList<com.google.appengine.api.datastore.Key>();
            org.slim3.datastore.json.JsonArrayReader r = rootReader.newArrayReader("labels");
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
                m.setLabels(elements);
            }
        }
        reader = rootReader.newObjectReader("lastChanged");
        m.setLastChanged(decoder0.decode(reader, m.getLastChanged()));
        reader = rootReader.newObjectReader("read");
        m.setRead(decoder0.decode(reader, m.isRead()));
        reader = rootReader.newObjectReader("userId");
        m.setUserId(decoder0.decode(reader, m.getUserId()));
        reader = rootReader.newObjectReader("userSource");
        m.setUserSource(decoder0.decode(reader, m.getUserSource()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}