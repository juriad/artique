package cz.artique.client.i18n;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface ArtiqueMessages extends Messages {
	@DefaultMessage("Added: <span class='abbr'>{0}</span> (<span class='full'>{1}</span>)")
	SafeHtml getDateAdded(String abbrev, String full);

	@DefaultMessage("Published: <span class='abbr'>{0}</span> (<span class='full'>{1}</span>)")
	SafeHtml getDatePublished(String abbrev, String full);
}
