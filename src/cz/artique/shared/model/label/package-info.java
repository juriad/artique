/**
 * {@link cz.artique.shared.model.label.Label}s and
 * {@link cz.artique.shared.model.label.ListFilter}s together makes a system
 * which allows user to categorize items. First user adds
 * {@link cz.artique.shared.model.label.Label}s to
 * {@link cz.artique.shared.model.item.UserItem}s and then defines
 * {@link cz.artique.shared.model.label.ListFilter} which matches items having
 * such {@link cz.artique.shared.model.label.Label}.
 * 
 * <p>
 * {@link cz.artique.shared.model.label.Label}s are assigned either manually by
 * user or automatically when a new
 * {@link cz.artique.shared.model.item.UserItem} is created. User may add one of
 * existing {@link cz.artique.shared.model.label.Label}s or create a new one.
 * Automatically assigned {@link cz.artique.shared.model.label.Label}s are of
 * two types:
 * <ul>
 * <li>USER_SOURCE labels - represents the
 * {@link cz.artique.shared.model.source.UserSource}, the
 * {@link cz.artique.shared.model.item.UserItem} belongs to; this type of
 * {@link cz.artique.shared.model.label.Label}s in not editable by user
 * <li>USER_DEFINED labels - labels defined by user which will be automatically
 * assigned to newly created {@link cz.artique.shared.model.item.UserItem}
 * together with USER_SOURCE label
 * </ul>
 * 
 * <p>
 * All user-defined {@link cz.artique.shared.model.label.Label}s are shown on
 * the row of item; the way they are shown can be configured: user may change
 * foreground and background color. Some
 * {@link cz.artique.shared.model.label.Label}s may have special meaning - they
 * can be set to automatically backup the original item when they are assigned.
 * 
 * <p>
 * {@link cz.artique.shared.model.label.ListFilter} uses the labels to filter
 * what {@link cz.artique.shared.model.item.UserItem}s are shown in list.
 * ListFilter may be ad-hoc - unnamed, unstored filter which is used for quick
 * search. On the other hand user may save
 * {@link cz.artique.shared.model.label.ListFilter}: give it a name and key
 * stroke which shows this ListFilter.
 * 
 * <p>
 * ListFilter may filter {@link cz.artique.shared.model.item.UserItem}s by:
 * <ul>
 * <li>Presence of {@link cz.artique.shared.model.label.Label}; user may create
 * more complex expression using operators
 * <li>Added date range; specify oldest and/or newest item
 * <li>Read state; match only read/unread items
 * </ul>
 * Matching {@link cz.artique.shared.model.item.UserItem} can be sorted either
 * in descending (default) or ascending order.
 */
package cz.artique.shared.model.label;

