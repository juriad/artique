<h2>Source detail</h2>

<img src="13.png">

<h3>Types of sources</h3>
<ul>
	<li>RSS/Atom – new items are imported periodically from RSS or Atom feed.</li>
	<li>Page change — checks periodically whether content of page has changed since last visit.</li>
	<li>Website – searches for new outgoing links in page content.</li>
	<li>Manual – does not provide new items; user can manually add them.</li>
</ul>

Page change and Website types parse HTML page.
The region that will be checked must be specified by CSS selector.

<h3>Dialog content</h3>

Detail of source contains these information:
<ul>
	<li>Type of source – source may be one of four types.</li>
	<li>URL – URL of the source.</li>
	<li>Domain – guessed domain from URL.</li>
	<li>Name – name of the source; user may change the name.</li>
	<li>Watching – whether the source is being watched (active) or not (disabled).
		User will not be notified about new items of disabled source.</li>
	<li>Hierarchy – path to root; folder names separated by slashes.</li>
	<li>Default labels – labels which are automatically assigned to new item of the source.</li>
	<li>Region (only if HTML source) – Specifies what area on the page shall be processed and what area shall be skipped.</li>
	<li>Last check – date when the source has been last checked.</li>
	<li>Errors – number of errors since last successful check.</li>
	<li>Next check – date of next planned check. User may force immediate check.</li>
</ul>

In comparison to filters, sources may not be deleted; they can only be disabled and hidden.
