package cz.artique.server.meta.user;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-07-01 15:08:02")
/** */
public final class UserInfoMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.user.UserInfo> {

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.UserInfo> clientToken = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.UserInfo>(this, "clientToken", "clientToken");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.UserInfo, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.UserInfo, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.UserInfo> nickname = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.UserInfo>(this, "nickname", "nickname");

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.UserInfo> userId = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.user.UserInfo>(this, "userId", "userId");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.UserInfo, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.user.UserInfo, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final UserInfoMeta slim3_singleton = new UserInfoMeta();

    /**
     * @return the singleton
     */
    public static UserInfoMeta get() {
       return slim3_singleton;
    }

    /** */
    public UserInfoMeta() {
        super("UserInfo", cz.artique.shared.model.user.UserInfo.class);
    }

    @Override
    public cz.artique.shared.model.user.UserInfo entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.user.UserInfo model = new cz.artique.shared.model.user.UserInfo();
        model.setClientToken((java.lang.String) entity.getProperty("clientToken"));
        model.setKey(entity.getKey());
        model.setNickname((java.lang.String) entity.getProperty("nickname"));
        model.setUserId((java.lang.String) entity.getProperty("userId"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.user.UserInfo m = (cz.artique.shared.model.user.UserInfo) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("clientToken", m.getClientToken());
        entity.setProperty("nickname", m.getNickname());
        entity.setProperty("userId", m.getUserId());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.user.UserInfo m = (cz.artique.shared.model.user.UserInfo) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.user.UserInfo m = (cz.artique.shared.model.user.UserInfo) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.user.UserInfo m = (cz.artique.shared.model.user.UserInfo) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.user.UserInfo m = (cz.artique.shared.model.user.UserInfo) model;
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
        cz.artique.shared.model.user.UserInfo m = (cz.artique.shared.model.user.UserInfo) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getClientToken() != null){
            writer.setNextPropertyName("clientToken");
            encoder0.encode(writer, m.getClientToken());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getLoginUrl() != null){
            writer.setNextPropertyName("loginUrl");
            encoder0.encode(writer, m.getLoginUrl());
        }
        if(m.getLogoutUrl() != null){
            writer.setNextPropertyName("logoutUrl");
            encoder0.encode(writer, m.getLogoutUrl());
        }
        if(m.getNickname() != null){
            writer.setNextPropertyName("nickname");
            encoder0.encode(writer, m.getNickname());
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
    protected cz.artique.shared.model.user.UserInfo jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.user.UserInfo m = new cz.artique.shared.model.user.UserInfo();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("clientToken");
        m.setClientToken(decoder0.decode(reader, m.getClientToken()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("loginUrl");
        m.setLoginUrl(decoder0.decode(reader, m.getLoginUrl()));
        reader = rootReader.newObjectReader("logoutUrl");
        m.setLogoutUrl(decoder0.decode(reader, m.getLogoutUrl()));
        reader = rootReader.newObjectReader("nickname");
        m.setNickname(decoder0.decode(reader, m.getNickname()));
        reader = rootReader.newObjectReader("userId");
        m.setUserId(decoder0.decode(reader, m.getUserId()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}