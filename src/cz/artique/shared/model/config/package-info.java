/**
 * Contains config packages, both
 * {@link cz.artique.shared.model.config.server.ServerConfig} (options related
 * to server side) and
 * {@link cz.artique.shared.model.config.client.ClientConfig} (options related
 * to client side). Both packages are almost identical except for a single
 * difference: {@link cz.artique.shared.model.config.client.ClientConfig} has
 * attribute userId, which allows - in theory - to change preferences for each
 * user individually. This possibility is not used.
 * 
 * <p>
 * *Config classes contains *ConfigKey enum attribute and String, Integer and
 * Double attributes to store value. *ConfigKey specifies type of value (
 * {@link cz.artique.shared.model.config.ConfigType}).
 * {@link cz.artique.shared.model.config.ConfigValue} contains attributes of all
 * possible types, it is used to preserve type.
 * 
 * @author Adam Juraszek
 * @see cz.artique.shared.model.config.server
 * @see cz.artique.shared.model.config.client
 * 
 */
package cz.artique.shared.model.config;