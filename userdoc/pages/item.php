<h2>Detail of item</h2>

<img src="1.png">

Each shown item consists of a header (always visible) and a content (hidden for all collapsed items).

<h3>Header</h3>

The header provides most of the information about the item (from the left):
<ul>
	<li>List of assigned labels. Labels are sorted alphabetically. The user may assign another label by clicking plus-sign button.</li>
	<li>The source the item belongs to / comes from. Hovering the cursor over the source name gives more information about the source.</li>
	<li>Open icon. Clicking onto this icon, original webpage will be loaded in a new tab.</li>
	<li>Title of the item. Clicking on the title (or blank space around it) selects the item and toggles its expanded/collapsed state.
		Clicking while holding control modifier opens the original webpage in a new tab.</li>
	<li>Backup item. If this icon is present, there exists backup of the item. Clicking on the icon opens the backup in a new tab.</li>
	<li>Date of addition. The date when the item was imported. 
		Hovering cursor over the date shown more information: precise timestamps of when the item was added and when it was published (only for RSS/Atom source).</li>
</ul>

<h3>Content</h3>

<img src="3.png">

The item also has a content panel which contains up to three contents.
The content panel is shown only when the item is expanded.
The content panel consists of a set of content switching buttons on top and actual content shown below.
Clicking on switching button changes what content is currently being shown.

Available contents are: NO (when no actual content is available), HTML DIFF (when the item is of type Page change), HTML (HTML content of the item), TXT (text content of the item; either when HTML is not available, or contains text extracted from HTML).

<h3>Labels</h3>

In the application, there are several places where the user may select or assign labels.
Those places are marked with a plus-sign button; clicking that button shows textbox.
Writing into textbox makes appear a list of suggestions.
Using <kbd>UP</kbd>, <kbd>DOWN</kbd> keys, the user may choose any particular suggestion; <kbd>RETURN</kbd> confirms the selection, <kbd>TAB</kbd> copies the current selection to the textbox.

Depending on context, sometimes when the user inserts a name of non-existing label, a new label will be quietly created.
