package cz.artique.server.meta.shortcut;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-06-11 22:04:58")
/** */
public final class ShortcutMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.shortcut.Shortcut> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, cz.artique.shared.model.shortcut.ShortcutAction> action = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, cz.artique.shared.model.shortcut.ShortcutAction>(this, "action", "action", cz.artique.shared.model.shortcut.ShortcutAction.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.shortcut.Shortcut> keyStroke = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.shortcut.Shortcut>(this, "keyStroke", "keyStroke");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, com.google.appengine.api.datastore.Key> referenced = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, com.google.appengine.api.datastore.Key>(this, "referenced", "referenced", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, cz.artique.shared.model.shortcut.ShortcutType> type = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, cz.artique.shared.model.shortcut.ShortcutType>(this, "type", "type", cz.artique.shared.model.shortcut.ShortcutType.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.shortcut.Shortcut> userId = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.shortcut.Shortcut>(this, "userId", "userId");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.shortcut.Shortcut, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final ShortcutMeta slim3_singleton = new ShortcutMeta();

    /**
     * @return the singleton
     */
    public static ShortcutMeta get() {
       return slim3_singleton;
    }

    /** */
    public ShortcutMeta() {
        super("Shortcut", cz.artique.shared.model.shortcut.Shortcut.class);
    }

    @Override
    public cz.artique.shared.model.shortcut.Shortcut entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.shortcut.Shortcut model = new cz.artique.shared.model.shortcut.Shortcut();
        model.setAction(stringToEnum(cz.artique.shared.model.shortcut.ShortcutAction.class, (java.lang.String) entity.getProperty("action")));
        model.setKey(entity.getKey());
        model.setKeyStroke((java.lang.String) entity.getProperty("keyStroke"));
        model.setReferenced((com.google.appengine.api.datastore.Key) entity.getProperty("referenced"));
        model.setType(stringToEnum(cz.artique.shared.model.shortcut.ShortcutType.class, (java.lang.String) entity.getProperty("type")));
        model.setUserId((java.lang.String) entity.getProperty("userId"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.shortcut.Shortcut m = (cz.artique.shared.model.shortcut.Shortcut) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("action", enumToString(m.getAction()));
        entity.setProperty("keyStroke", m.getKeyStroke());
        entity.setProperty("referenced", m.getReferenced());
        entity.setProperty("type", enumToString(m.getType()));
        entity.setProperty("userId", m.getUserId());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.shortcut.Shortcut m = (cz.artique.shared.model.shortcut.Shortcut) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.shortcut.Shortcut m = (cz.artique.shared.model.shortcut.Shortcut) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.shortcut.Shortcut m = (cz.artique.shared.model.shortcut.Shortcut) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.shortcut.Shortcut m = (cz.artique.shared.model.shortcut.Shortcut) model;
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
        cz.artique.shared.model.shortcut.Shortcut m = (cz.artique.shared.model.shortcut.Shortcut) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getAction() != null){
            writer.setNextPropertyName("action");
            encoder0.encode(writer, m.getAction());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getKeyStroke() != null){
            writer.setNextPropertyName("keyStroke");
            encoder0.encode(writer, m.getKeyStroke());
        }
        if(m.getReferenced() != null){
            writer.setNextPropertyName("referenced");
            encoder0.encode(writer, m.getReferenced());
        }
        if(m.getReferencedLabel() != null){
            writer.setNextPropertyName("referencedLabel");
            encoder0.encode(writer, m.getReferencedLabel());
        }
        if(m.getReferencedListFilter() != null){
            writer.setNextPropertyName("referencedListFilter");
            encoder0.encode(writer, m.getReferencedListFilter());
        }
        if(m.getType() != null){
            writer.setNextPropertyName("type");
            encoder0.encode(writer, m.getType());
        }
        if(m.getUserId() != null){
            writer.setNextPropertyName("userId");
            encoder0.encode(writer, m.getUserId());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected cz.artique.shared.model.shortcut.Shortcut jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.shortcut.Shortcut m = new cz.artique.shared.model.shortcut.Shortcut();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("action");
        m.setAction(decoder0.decode(reader, m.getAction(), cz.artique.shared.model.shortcut.ShortcutAction.class));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("keyStroke");
        m.setKeyStroke(decoder0.decode(reader, m.getKeyStroke()));
        reader = rootReader.newObjectReader("referenced");
        m.setReferenced(decoder0.decode(reader, m.getReferenced()));
        reader = rootReader.newObjectReader("referencedLabel");
        m.setReferencedLabel(decoder0.decode(reader, m.getReferencedLabel(), cz.artique.shared.model.label.Label.class));
        reader = rootReader.newObjectReader("referencedListFilter");
        m.setReferencedListFilter(decoder0.decode(reader, m.getReferencedListFilter(), cz.artique.shared.model.label.ListFilter.class));
        reader = rootReader.newObjectReader("type");
        m.setType(decoder0.decode(reader, m.getType(), cz.artique.shared.model.shortcut.ShortcutType.class));
        reader = rootReader.newObjectReader("userId");
        m.setUserId(decoder0.decode(reader, m.getUserId()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}