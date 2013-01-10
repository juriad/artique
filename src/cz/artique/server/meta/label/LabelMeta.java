package cz.artique.server.meta.label;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-01-09 21:00:40")
/** */
public final class LabelMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.label.Label> {

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.label.Label, cz.artique.shared.model.label.LabelAppearance> appearance = new org.slim3.datastore.UnindexedAttributeMeta<cz.artique.shared.model.label.Label, cz.artique.shared.model.label.LabelAppearance>(this, "appearance", "appearance", cz.artique.shared.model.label.LabelAppearance.class);

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.Label, cz.artique.shared.model.label.BackupLevel> backupLevel = new org.slim3.datastore.CoreUnindexedAttributeMeta<cz.artique.shared.model.label.Label, cz.artique.shared.model.label.BackupLevel>(this, "backupLevel", "backupLevel", cz.artique.shared.model.label.BackupLevel.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Label, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Label, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Label, cz.artique.shared.model.label.LabelType> labelType = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Label, cz.artique.shared.model.label.LabelType>(this, "labelType", "labelType", cz.artique.shared.model.label.LabelType.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.Label> name = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.Label>(this, "name", "name");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Label, com.google.appengine.api.users.User> user = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Label, com.google.appengine.api.users.User>(this, "user", "user", com.google.appengine.api.users.User.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Label, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Label, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final LabelMeta slim3_singleton = new LabelMeta();

    /**
     * @return the singleton
     */
    public static LabelMeta get() {
       return slim3_singleton;
    }

    /** */
    public LabelMeta() {
        super("Label", cz.artique.shared.model.label.Label.class);
    }

    @Override
    public cz.artique.shared.model.label.Label entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.label.Label model = new cz.artique.shared.model.label.Label();
        cz.artique.shared.model.label.LabelAppearance _appearance = blobToSerializable((com.google.appengine.api.datastore.Blob) entity.getProperty("appearance"));
        model.setAppearance(_appearance);
        model.setBackupLevel(stringToEnum(cz.artique.shared.model.label.BackupLevel.class, (java.lang.String) entity.getProperty("backupLevel")));
        model.setKey(entity.getKey());
        model.setLabelType(stringToEnum(cz.artique.shared.model.label.LabelType.class, (java.lang.String) entity.getProperty("labelType")));
        model.setName((java.lang.String) entity.getProperty("name"));
        model.setUser((com.google.appengine.api.users.User) entity.getProperty("user"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.label.Label m = (cz.artique.shared.model.label.Label) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("appearance", serializableToBlob(m.getAppearance()));
        entity.setUnindexedProperty("backupLevel", enumToString(m.getBackupLevel()));
        entity.setProperty("labelType", enumToString(m.getLabelType()));
        entity.setProperty("name", m.getName());
        entity.setProperty("user", m.getUser());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.label.Label m = (cz.artique.shared.model.label.Label) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.label.Label m = (cz.artique.shared.model.label.Label) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.label.Label m = (cz.artique.shared.model.label.Label) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.label.Label m = (cz.artique.shared.model.label.Label) model;
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
        cz.artique.shared.model.label.Label m = (cz.artique.shared.model.label.Label) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getAppearance() != null){
            writer.setNextPropertyName("appearance");
            encoder0.encode(writer, m.getAppearance());
        }
        if(m.getBackupLevel() != null){
            writer.setNextPropertyName("backupLevel");
            encoder0.encode(writer, m.getBackupLevel());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getLabelType() != null){
            writer.setNextPropertyName("labelType");
            encoder0.encode(writer, m.getLabelType());
        }
        if(m.getName() != null){
            writer.setNextPropertyName("name");
            encoder0.encode(writer, m.getName());
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
    protected cz.artique.shared.model.label.Label jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.label.Label m = new cz.artique.shared.model.label.Label();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("appearance");
        m.setAppearance(decoder0.decode(reader, m.getAppearance(), cz.artique.shared.model.label.LabelAppearance.class));
        reader = rootReader.newObjectReader("backupLevel");
        m.setBackupLevel(decoder0.decode(reader, m.getBackupLevel(), cz.artique.shared.model.label.BackupLevel.class));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("labelType");
        m.setLabelType(decoder0.decode(reader, m.getLabelType(), cz.artique.shared.model.label.LabelType.class));
        reader = rootReader.newObjectReader("name");
        m.setName(decoder0.decode(reader, m.getName()));
        reader = rootReader.newObjectReader("user");
        m.setUser(decoder0.decode(reader, m.getUser()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}