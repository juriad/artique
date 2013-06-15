/**
 * Contains model class for {@link cz.artique.shared.model.shortcut.Shortcut}s -
 * mechanism, which define action to perform when a certain key combination is
 * pressed.
 * 
 * <p>
 * Shortcut may be one of the following types (
 * {@link cz.artique.shared.model.shortcut.ShortcutType}):
 * <ul>
 * <li>LABEL - assigns (or removes if already present)
 * {@link cz.artique.shared.model.label.Label}
 * <li>LIST_FILTER - changes listing to items matched by referenced
 * {@link cz.artique.shared.model.label.ListFilter}
 * <li>ACTION - perform one of actions defined in
 * {@link cz.artique.shared.model.shortcut.ShortcutAction}.
 */
package cz.artique.shared.model.shortcut;

