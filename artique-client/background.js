/*******************************************************************************
 * This is your background code. For more information please visit our wiki
 * site: http://docs.crossrider.com/#!/guide/scopes_background
 ******************************************************************************/

appAPI.ready(function($) {
	var token = appAPI.db.get('clientToken');
	var icon;
	if (token == null) {
		appAPI.browserAction.setResourceIcon('icon-bw.png');
		appAPI.browserAction.setTitle('Client is not available.');
		appAPI.browserAction.onClick(function() {
			appAPI.openURL({
				url : "http://localhost:8888/",
				where : "tab"
			});
		});
	} else {
		appAPI.browserAction.setResourceIcon('icon.png');
		appAPI.browserAction.setTitle('Click to add page.');
		appAPI.browserAction.setPopup({
			resourcePath : 'popup.html',
			height : 230,
			width : 500
		});
	}

	appAPI.message.addListener(function(msg) {
		if (msg.message == "tokenAvailable") {
			appAPI.browserAction.setResourceIcon('icon.png');
			appAPI.browserAction.setTitle('Click to add page.');
			appAPI.browserAction.setPopup({
				resourcePath : 'popup.html',
				height : 230,
				width : 500,
			});
		} else if (msg.message == "labels") {
			appAPI.request.post({
				url : 'http://localhost:8888/export/clientService',
				postData : {
					token : appAPI.db.get('clientToken'),
					action : 'getLabels'
				},
				onSuccess : function(response) {
					var labels = appAPI.JSON.parse(response);
					appAPI.message.toPopup({
						message : "labels",
						labels : labels,
					});
				},
			});
		} else if (msg.message == "item") {
			var item = {
				url : msg.url,
				title : msg.title,
				content : msg.content,
				labels : msg.labels,
			};
			var itemStr = appAPI.JSON.stringify(item);
			appAPI.request.post({
				url : 'http://localhost:8888/export/clientService',
				postData : {
					token : appAPI.db.get('clientToken'),
					action : 'addItem',
					item : itemStr,
				},
				onSuccess : function(response) {
					appAPI.message.toPopup({
						message : "item",
						status : "OK",
					});
				},
				onFailure : function(httpCode) {
					appAPI.message.toPopup({
						message : "item",
						status : "FAIL",
						code : httpCode,
					});
				}
			});
		}
	});
});
