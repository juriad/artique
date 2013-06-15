/**
 * Source package contains all model classes related to sources.
 * 
 * <p>
 * The most notable class is {@link cz.artique.shared.model.source.Source} which
 * represents any source the user adds to the system. Source may be one
 * following types:
 * <ul>
 * <li> {@link cz.artique.shared.model.source.XMLSource} - system periodically
 * checks content of XML document (RSS or Atom) which describes latest news on
 * the web-site in machine-readable form. This source type provides
 * {@link cz.artique.shared.model.item.ArticleItem}s.
 * <li> {@link cz.artique.shared.model.source.HTMLSource} - web-site does not
 * provide any information about news; therefore we must parse HTML manually.
 * This type has two sub-types, both work on low level with HTML code.
 * <ul>
 * <li> {@link cz.artique.shared.model.source.PageChangeSource} - saves text
 * content of the page and checks during next visit whether the content changed.
 * If the content changed, a new item is created, it contains the new content
 * and marked-up difference page. Items are of type
 * {@link cz.artique.shared.model.source.PageChangeSource}.
 * <li> {@link cz.artique.shared.model.source.WebSiteSource} - is similar to
 * {@link cz.artique.shared.model.source.XMLSource}; it parses the HTML code and
 * finds all anchors. It suppose, that a new anchor means a link to a new
 * article. Type of items is {@link cz.artique.shared.model.item.LinkItem}.
 * </ul>
 * All HTMLSources require a {@link cz.artique.shared.model.source.Region}
 * specified which defines the area in the HTML page to include and exclude. The
 * process follows:
 * <ol>
 * <li>Get the HTML page and parse it to DOM tree.
 * <li>Filter the page to include only parts matched by positive selector.
 * <li>Discard parts which match negative selector.
 * <li>Either get text content and compare with older version or find all
 * {@code <a>} elements.
 * </ol>
 * <li> {@link cz.artique.shared.model.source.ManualSource} - does not provide
 * any items; it just allows user to add his own items via
 * {@link cz.artique.client.items.ManualItemDialog} or client-side extension.
 * Manually added items are of type
 * {@link cz.artique.shared.model.item.ManualItem}.
 * </ul>
 * 
 * <p>
 * To effectively lower number of requests for each source, the sources are
 * shared among users. This way it is necessary to make only one request for
 * each source. Even for source which would be watched by all users.
 * {@link cz.artique.shared.model.source.Source} itself does not contain any
 * personalizating information of the users. All user information related to
 * source are stored in {@link cz.artique.shared.model.source.UserSource} -
 * mapping class between {@link cz.artique.shared.model.user.UserInfo} and
 * {@link cz.artique.shared.model.source.Source}. UserSource allows user:
 * <ul>
 * <li>to set to the source his own name
 * <li>to place into hierarchy - make a folder of several similar sources
 * <li>to temporarily or permanently stop watching source
 * <li>to set list of labels which will be automatically assigned to all
 * {@link cz.artique.shared.model.item.UserItem}s of that source
 * <li>to set custom region in case the source is
 * {@link cz.artique.shared.model.source.HTMLSource}.
 * 
 */
package cz.artique.shared.model.source;

