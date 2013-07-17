/**
 * Crawler package contains classes which check
 * {@link cz.artique.shared.model.source.Source}s of all types for new items.
 * When new item (that didn't exist) is found,
 * {@link cz.artique.shared.model.item.Item} of
 * appropriate type is created and added for all active
 * {@link cz.artique.shared.model.source.UserSource}s
 * {@link cz.artique.shared.model.item.UserItem} is also created.
 * 
 * <p>
 * {@link cz.artique.shared.model.source.ManualSource} does not really need a
 * crawler, but one is implemented just to unify source checking.
 */
package cz.artique.server.crawler;