/**
 * This package contains model classes which represent an
 * {@link cz.artique.shared.model.item.Item} acquired from
 * {@link cz.artique.shared.model.source.Source}s or added manually by user. It
 * also contains personalization of {@link cz.artique.shared.model.item.Item} -
 * {@link cz.artique.shared.model.item.UserItem} for each user.
 * 
 * <p>
 * Items are created by {@link cz.artique.server.crawler.Crawler}s; there exists
 * {@link cz.artique.server.crawler.Crawler} for each type of
 * {@link cz.artique.shared.model.source.Source} or the
 * {@link cz.artique.shared.model.item.Item}s may be created by user either via
 * {@link cz.artique.client.items.ManualItemDialog} or by client browser
 * extension via {@link cz.artique.server.service.ClientServlet}. Type hierarchy
 * of items reflects the type hierarchy of sources.
 * 
 * <p>
 * Like there exists {@link cz.artique.shared.model.source.UserSource}, there
 * exists {@link cz.artique.shared.model.item.UserItem} which personalizes
 * {@link cz.artique.shared.model.item.Item}s for each user. Users may adjust
 * labels, change read status and backup item. The
 * {@link cz.artique.shared.model.item.UserItem} is also created by
 * {@link cz.artique.server.crawler.Crawler} and all its values are set to
 * default values; for more information see
 * {@link cz.artique.shared.model.item.UserItem}.
 */
package cz.artique.shared.model.item;

