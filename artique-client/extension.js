/*******************************************************************************
 * This is your Page Code. The appAPI.ready() code block will be executed on
 * every page load. For more information please visit our docs site:
 * http://docs.crossrider.com
 ******************************************************************************/

appAPI.ready(function($) {
	function getSelectionText() {
		var text = "";
		if (window.getSelection) {
			text = window.getSelection().toString();
		} else if (document.selection && document.selection.type != "Control") {
			text = document.selection.createRange().text;
		}
		return text;
	}

	if (appAPI.isMatchPages("*www.artique.cz*")) {
		window.setTimeout(function() {
			var meta = $('head meta[name="clientToken"]');
			if (meta.length > 0) {
				var token = meta.attr('content');
				appAPI.db.set('clientToken', token);

				appAPI.message.toBackground({
					message : "tokenAvailable"
				});
			}
		}, 5000);
	}

	appAPI.message.addListener(function(msg) {
		if (msg.message == "page") {
			var url = window.location.href;

			var titleElems = $('title');
			var title = '';
			if (titleElems.length > 0) {
				title = titleElems.text();
			}

			var content = $.trim(getSelectionText());

			appAPI.message.toPopup({
				message : "page",
				url : url,
				title : title,
				content : content
			});
		}
	});
});
