package cz.artique.server.meta.source;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-11-10 17:14:08")
/** */
public final class UserSourceMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.source.UserSource> {

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource> hierarchy = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource>(this, "hierarchy", "hierarchy");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource> name = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.source.UserSource>(this, "name", "name");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key> source = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.datastore.Key>(this, "source", "source", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.users.User> user = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, com.google.appengine.api.users.User>(this, "user", "user", com.google.appengine.api.users.User.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.source.UserSource, java.lang.Long>(this, "version", "version", java.lang.Long.class);

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
        model.setHierarchy((java.lang.String) entity.getProperty("hierarchy"));
        model.setKey(entity.getKey());
        model.setName((java.lang.String) entity.getProperty("name"));
        model.setSource((com.google.appengine.api.datastore.Key) entity.getProperty("source"));
        model.setUser((com.google.appengine.api.users.User) entity.getProperty("user"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
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
        entity.setProperty("hierarchy", m.getHierarchy());
        entity.setProperty("name", m.getName());
        entity.setProperty("source", m.getSource());
        entity.setProperty("user", m.getUser());
        entity.setProperty("version", m.getVersion());
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
        if(m.getSource() != null){
            writer.setNextPropertyName("source");
            encoder0.encode(writer, m.getSource());
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
    protected cz.artique.shared.model.source.UserSource jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.source.UserSource m = new cz.artique.shared.model.source.UserSource();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("hierarchy");
        m.setHierarchy(decoder0.decode(reader, m.getHierarchy()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("name");
        m.setName(decoder0.decode(reader, m.getName()));
        reader = rootReader.newObjectReader("source");
        m.setSource(decoder0.decode(reader, m.getSource()));
        reader = rootReader.newObjectReader("user");
        m.setUser(decoder0.decode(reader, m.getUser()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}