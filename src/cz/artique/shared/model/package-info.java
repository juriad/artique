/**
 * Contains definition of model classes; objects of those types are stored in
 * Datastore and often send to client via services.
 * 
 * <p>
 * Because of dual nature of application - part runs on server, part on client.
 * Part compiles to java byte-code, part compiles to javascript, the model have
 * to be in shared package and must not reference any AppEngine classes except
 * for a few ones ({@link com.google.appengine.api.datastore.Key},
 * {@link com.google.appengine.api.datastore.Link},
 * {@link com.google.appengine.api.datastore.Text}). Used framework (slim3) adds
 * some restrictions on model:
 * <ul>
 * <li>All classes which are send to client must not contain final attribute.
 * <li>All attributes must have getter and setter methods.
 * <li>Class must have default constructor.
 * <li>Class must implement {@link java.io.Serializable}.
 * </ul>
 * 
 * <p>
 * There are some interesting points which are worth mentioning:
 * <ul>
 * <li>References to other model objects are represented as attribute of type
 * {@link com.google.appengine.api.datastore.Key}. There often exists attributes
 * of concrete model object types for references; they are called the same name
 * with Object suffix.
 * <li>Reference to {@link cz.artique.shared.model.user.UserInfo} is always
 * reference to {@link cz.artique.shared.model.user.UserInfo#getUserId()}, not
 * {@link cz.artique.shared.model.user.UserInfo#getKey()} as in every other
 * case. This allows us to use one less query each time a service is called.
 * <li>Many model classes implement {@link cz.artique.shared.utils.GenKey}. This
 * is used to set key rather than leave its value to be filled to datastore.
 */
package cz.artique.shared.model;