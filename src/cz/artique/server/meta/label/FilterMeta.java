package cz.artique.server.meta.label;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2013-06-03 09:40:43")
/** */
public final class FilterMeta
		extends
		org.slim3.datastore.ModelMeta<cz.artique.shared.model.label.Filter> {

	/** */
	public final org.slim3.datastore.CollectionUnindexedAttributeMeta<cz.artique.shared.model.label.Filter, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key> filters =
		new org.slim3.datastore.CollectionUnindexedAttributeMeta<cz.artique.shared.model.label.Filter, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key>(
			this, "filters", "filters", java.util.List.class);

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
	public final org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, cz.artique.shared.model.label.FilterType> type =
		new org.slim3.datastore.CoreAttributeMeta<cz.artique.shared.model.label.Filter, cz.artique.shared.model.label.FilterType>(
			this, "type", "type",
			cz.artique.shared.model.label.FilterType.class);

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
		model.setFilters(toList(com.google.appengine.api.datastore.Key.class,
			entity.getProperty("filters")));
		model.setKey(entity.getKey());
		model.setLabels(toList(com.google.appengine.api.datastore.Key.class,
			entity.getProperty("labels")));
		model.setType(stringToEnum(
			cz.artique.shared.model.label.FilterType.class,
			(java.lang.String) entity.getProperty("type")));
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
		entity.setUnindexedProperty("filters", m.getFilters());
		entity.setProperty("labels", m.getLabels());
		entity.setProperty("type", enumToString(m.getType()));
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
		if (m.getFilterObjects() != null) {
			writer.setNextPropertyName("filterObjects");
			// cz.artique.shared.model.label.Filter is not supported.
		}
		if (m.getFilters() != null) {
			writer.setNextPropertyName("filters");
			writer.beginArray();
			for (com.google.appengine.api.datastore.Key v : m.getFilters()) {
				encoder0.encode(writer, v);
			}
			writer.endArray();
		}
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
		if (m.getType() != null) {
			writer.setNextPropertyName("type");
			encoder0.encode(writer, m.getType());
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
		reader = rootReader.newObjectReader("filterObjects");
		reader = rootReader.newObjectReader("filters");
		{
			java.util.ArrayList<com.google.appengine.api.datastore.Key> elements =
				new java.util.ArrayList<com.google.appengine.api.datastore.Key>();
			org.slim3.datastore.json.JsonArrayReader r =
				rootReader.newArrayReader("filters");
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
				m.setFilters(elements);
			}
		}
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
		reader = rootReader.newObjectReader("type");
		m.setType(decoder0.decode(reader, m.getType(),
			cz.artique.shared.model.label.FilterType.class));
		reader = rootReader.newObjectReader("user");
		m.setUser(decoder0.decode(reader, m.getUser()));
		reader = rootReader.newObjectReader("version");
		m.setVersion(decoder0.decode(reader, m.getVersion()));
		return m;
	}
}
