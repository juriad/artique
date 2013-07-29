package cz.artique.server.meta.recomandation;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-07-29 20:55:23")
/** */
public final class RecommendationMeta extends org.slim3.datastore.ModelMeta<cz.artique.shared.model.recomandation.Recommendation> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.recomandation.Recommendation, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.recomandation.Recommendation, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CollectionUnindexedAttributeMeta<cz.artique.shared.model.recomandation.Recommendation, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key> recommendedSources = new org.slim3.datastore.CollectionUnindexedAttributeMeta<cz.artique.shared.model.recomandation.Recommendation, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key>(this, "recommendedSources", "recommendedSources", java.util.List.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.recomandation.Recommendation> userId = new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.recomandation.Recommendation>(this, "userId", "userId");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.recomandation.Recommendation, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.recomandation.Recommendation, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final RecommendationMeta slim3_singleton = new RecommendationMeta();

    /**
     * @return the singleton
     */
    public static RecommendationMeta get() {
       return slim3_singleton;
    }

    /** */
    public RecommendationMeta() {
        super("Recommendation", cz.artique.shared.model.recomandation.Recommendation.class);
    }

    @Override
    public cz.artique.shared.model.recomandation.Recommendation entityToModel(com.google.appengine.api.datastore.Entity entity) {
        cz.artique.shared.model.recomandation.Recommendation model = new cz.artique.shared.model.recomandation.Recommendation();
        model.setKey(entity.getKey());
        model.setRecommendedSources(toList(com.google.appengine.api.datastore.Key.class, entity.getProperty("recommendedSources")));
        model.setUserId((java.lang.String) entity.getProperty("userId"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        cz.artique.shared.model.recomandation.Recommendation m = (cz.artique.shared.model.recomandation.Recommendation) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("recommendedSources", m.getRecommendedSources());
        entity.setProperty("userId", m.getUserId());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        cz.artique.shared.model.recomandation.Recommendation m = (cz.artique.shared.model.recomandation.Recommendation) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        cz.artique.shared.model.recomandation.Recommendation m = (cz.artique.shared.model.recomandation.Recommendation) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        cz.artique.shared.model.recomandation.Recommendation m = (cz.artique.shared.model.recomandation.Recommendation) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        cz.artique.shared.model.recomandation.Recommendation m = (cz.artique.shared.model.recomandation.Recommendation) model;
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
        cz.artique.shared.model.recomandation.Recommendation m = (cz.artique.shared.model.recomandation.Recommendation) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getRecommendedSources() != null){
            writer.setNextPropertyName("recommendedSources");
            writer.beginArray();
            for(com.google.appengine.api.datastore.Key v : m.getRecommendedSources()){
                encoder0.encode(writer, v);
            }
            writer.endArray();
        }
        if(m.getRecommendedSourcesObjects() != null){
            writer.setNextPropertyName("recommendedSourcesObjects");
            // cz.artique.shared.model.source.Source is not supported.
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
    protected cz.artique.shared.model.recomandation.Recommendation jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        cz.artique.shared.model.recomandation.Recommendation m = new cz.artique.shared.model.recomandation.Recommendation();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("recommendedSources");
        {
            java.util.ArrayList<com.google.appengine.api.datastore.Key> elements = new java.util.ArrayList<com.google.appengine.api.datastore.Key>();
            org.slim3.datastore.json.JsonArrayReader r = rootReader.newArrayReader("recommendedSources");
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
                m.setRecommendedSources(elements);
            }
        }
        reader = rootReader.newObjectReader("recommendedSourcesObjects");
        reader = rootReader.newObjectReader("userId");
        m.setUserId(decoder0.decode(reader, m.getUserId()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}