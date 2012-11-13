package cz.artique.server.meta.label;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-11-11 23:54:01")
/** */
public final class FilterMeta
		extends
		org.slim3.datastore.ModelMeta<cz.artique.shared.model.label.Filter> {

	/** */
	public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, com.google.appengine.api.datastore.Key> key =
		new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, com.google.appengine.api.datastore.Key>(
			this, "__key__", "key",
			com.google.appengine.api.datastore.Key.class);

	/** */
	public final org.slim3.datastore.CollectionAttributeMeta<cz.artique.shared.model.label.Filter, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key> labels =
		new org.slim3.datastore.CollectionAttributeMeta<cz.artique.shared.model.label.Filter, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key>(
			this, "labels", "labels", java.util.List.class);

	/** */
	public final org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.Filter> name =
		new org.slim3.datastore.StringAttributeMeta<cz.artique.shared.model.label.Filter>(
			this, "name", "name");

	/** */
	public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, cz.artique.shared.model.label.Operator> operator =
		new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, cz.artique.shared.model.label.Operator>(
			this, "operator", "operator",
			cz.artique.shared.model.label.Operator.class);

	/** */
	public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, com.google.appengine.api.datastore.Key> parent =
		new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, com.google.appengine.api.datastore.Key>(
			this, "parent", "parent",
			com.google.appengine.api.datastore.Key.class);

	/** */
	public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, com.google.appengine.api.users.User> user =
		new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, com.google.appengine.api.users.User>(
			this, "user", "user", com.google.appengine.api.users.User.class);

	/** */
	public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, java.lang.Long> version =
		new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, java.lang.Long>(
			this, "version", "version", java.lang.Long.class);

	private static final FilterMeta slim3_singleton = new FilterMeta();

	/**
	 * @return the singleton
	 */
	public static FilterMeta get() {
		return slim3_singleton;
	}

	/** */
	public FilterMeta() {
		super("Filter", cz.artique.shared.model.label.Filter.class);
	}

	@Override
	public cz.artique.shared.model.label.Filter entityToModel(
			com.google.appengine.api.datastore.Entity entity) {
		cz.artique.shared.model.label.Filter model =
			new cz.artique.shared.model.label.Filter();
		model.setKey(entity.getKey());
		model.setLabels(toList(com.google.appengine.api.datastore.Key.class,
			entity.getProperty("labels")));
		model.setName((java.lang.String) entity.getProperty("name"));
		model.setOperator(stringToEnum(
			cz.artique.shared.model.label.Operator.class,
			(java.lang.String) entity.getProperty("operator")));
		model.setParent((com.google.appengine.api.datastore.Key) entity
			.getProperty("parent"));
		model.setUser((com.google.appengine.api.users.User) entity
			.getProperty("user"));
		model.setVersion((java.lang.Long) entity.getProperty("version"));
		return model;
	}

	@Override
	public com.google.appengine.api.datastore.Entity modelToEntity(
			java.lang.Object model) {
		cz.artique.shared.model.label.Filter m =
			(cz.artique.shared.model.label.Filter) model;
		com.google.appengine.api.datastore.Entity entity = null;
		if (m.getKey() != null) {
			entity = new com.google.appengine.api.datastore.Entity(m.getKey());
		} else {
			entity = new com.google.appengine.api.datastore.Entity(kind);
		}
		entity.setProperty("labels", m.getLabels());
		entity.setProperty("name", m.getName());
		entity.setProperty("operator", enumToString(m.getOperator()));
		entity.setProperty("parent", m.getParent());
		entity.setProperty("user", m.getUser());
		entity.setProperty("version", m.getVersion());
		entity.setProperty("slim3.schemaVersion", 1);
		return entity;
	}

	@Override
	protected com.google.appengine.api.datastore.Key getKey(Object model) {
		cz.artique.shared.model.label.Filter m =
			(cz.artique.shared.model.label.Filter) model;
		return m.getKey();
	}

	@Override
	protected void setKey(Object model,
			com.google.appengine.api.datastore.Key key) {
		validateKey(key);
		cz.artique.shared.model.label.Filter m =
			(cz.artique.shared.model.label.Filter) model;
		m.setKey(key);
	}

	@Override
	protected long getVersion(Object model) {
		cz.artique.shared.model.label.Filter m =
			(cz.artique.shared.model.label.Filter) model;
		return m.getVersion() != null ? m.getVersion().longValue() : 0L;
	}

	@Override
	protected void assignKeyToModelRefIfNecessary(
			com.google.appengine.api.datastore.AsyncDatastoreService ds,
			java.lang.Object model) {}

	@Override
	protected void incrementVersion(Object model) {
		cz.artique.shared.model.label.Filter m =
			(cz.artique.shared.model.label.Filter) model;
		long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
		m.setVersion(Long.valueOf(version + 1L));
	}

	@Override
	protected void prePut(Object model) {}

	@Override
	protected void postGet(Object model) {}

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
	protected void modelToJson(org.slim3.datastore.json.JsonWriter writer,
			java.lang.Object model, int maxDepth, int currentDepth) {
		cz.artique.shared.model.label.Filter m =
			(cz.artique.shared.model.label.Filter) model;
		writer.beginObject();
		org.slim3.datastore.json.Default encoder0 =
			new org.slim3.datastore.json.Default();
		if (m.getKey() != null) {
			writer.setNextPropertyName("key");
			encoder0.encode(writer, m.getKey());
		}
		if (m.getLabels() != null) {
			writer.setNextPropertyName("labels");
			writer.beginArray();
			for (com.google.appengine.api.datastore.Key v : m.getLabels()) {
				encoder0.encode(writer, v);
			}
			writer.endArray();
		}
		if (m.getName() != null) {
			writer.setNextPropertyName("name");
			encoder0.encode(writer, m.getName());
		}
		if (m.getOperator() != null) {
			writer.setNextPropertyName("operator");
			encoder0.encode(writer, m.getOperator());
		}
		if (m.getParent() != null) {
			writer.setNextPropertyName("parent");
			encoder0.encode(writer, m.getParent());
		}
		if (m.getUser() != null) {
			writer.setNextPropertyName("user");
			encoder0.encode(writer, m.getUser());
		}
		if (m.getVersion() != null) {
			writer.setNextPropertyName("version");
			encoder0.encode(writer, m.getVersion());
		}
		writer.endObject();
	}

	@Override
	protected cz.artique.shared.model.label.Filter jsonToModel(
			org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth,
			int currentDepth) {
		cz.artique.shared.model.label.Filter m =
			new cz.artique.shared.model.label.Filter();
		org.slim3.datastore.json.JsonReader reader = null;
		org.slim3.datastore.json.Default decoder0 =
			new org.slim3.datastore.json.Default();
		reader = rootReader.newObjectReader("key");
		m.setKey(decoder0.decode(reader, m.getKey()));
		reader = rootReader.newObjectReader("labels");
		{
			java.util.ArrayList<com.google.appengine.api.datastore.Key> elements =
				new java.util.ArrayList<com.google.appengine.api.datastore.Key>();
			org.slim3.datastore.json.JsonArrayReader r =
				rootReader.newArrayReader("labels");
			if (r != null) {
				reader = r;
				int n = r.length();
				for (int i = 0; i < n; i++) {
					r.setIndex(i);
					com.google.appengine.api.datastore.Key v =
						decoder0.decode(reader,
							(com.google.appengine.api.datastore.Key) null);
					if (v != null) {
						elements.add(v);
					}
				}
				m.setLabels(elements);
			}
		}
		reader = rootReader.newObjectReader("name");
		m.setName(decoder0.decode(reader, m.getName()));
		reader = rootReader.newObjectReader("operator");
		m.setOperator(decoder0.decode(reader, m.getOperator(),
			cz.artique.shared.model.label.Operator.class));
		reader = rootReader.newObjectReader("parent");
		m.setParent(decoder0.decode(reader, m.getParent()));
		reader = rootReader.newObjectReader("user");
		m.setUser(decoder0.decode(reader, m.getUser()));
		reader = rootReader.newObjectReader("version");
		m.setVersion(decoder0.decode(reader, m.getVersion()));
		return m;
	}
}
