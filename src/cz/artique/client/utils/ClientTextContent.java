// Club AJAX General Purpose Code
//
// getPlainText()
//
// author:
//		Mike Wilcox
// site:
//		http://clubajax.org
// support:
//		http://groups.google.com/group/clubajax
//
//
//	DESCRIPTION:
//		Returns a line-break, properly spaced, normailized plain text
//		representation of multiple child nodes which can't be done via
//		textContent or innerText because those two methods are vastly
//		different, and even innerText works differently across browsers.
//
//

package cz.artique.client.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import cz.artique.server.utils.ServerTextContent;

/**
 * Converts Element's DOM subtree into plain text preserving white spaces.
 * Original author is Mike Wilcox.
 * 
 * @see ServerTextContent reimplementation on server side
 * @author Adam Juraszek
 * 
 */
public class ClientTextContent {
	/**
	 * Gets plain text content of element.
	 * 
	 * @param node
	 *            {@link Element} to process
	 * @return plain text content
	 */
	public static native String getPlainText(Element node) /*-{
		// used for testing:
		//return node.innerText || node.textContent;

		var normalize = function(a) {
			// clean up double line breaks and spaces
			if (!a)
				return "";
			return a.replace(/ +/g, " ").replace(/[\t]+/gm, "").replace(
					/[ ]+$/gm, "").replace(/^[ ]+/gm, "").replace(/\n+/g, "\n")
					.replace(/\n+$/, "").replace(/^\n+/, "").replace(
							/\nNEWLINE\n/g, "\n\n").replace(/NEWLINE\n/g,
							"\n\n"); // IE
		}
		var removeWhiteSpace = function(node) {
			// getting rid of empty text nodes
			var isWhite = function(node) {
				return !(/[^\t\n\r ]/.test(node.nodeValue));
			}
			var ws = [];
			var findWhite = function(node) {
				for ( var i = 0; i < node.childNodes.length; i++) {
					var n = node.childNodes[i];
					if (n.nodeType == 3 && isWhite(n)) {
						ws.push(n)
					} else if (n.hasChildNodes()) {
						findWhite(n);
					}
				}
			}
			findWhite(node);
			for ( var i = 0; i < ws.length; i++) {
				ws[i].parentNode.removeChild(ws[i])
			}

		}
		var sty = function(n, prop) {
			// Get the style of the node.
			// Assumptions are made here based on tagName.
			if (n.style[prop])
				return n.style[prop];
			var s = n.currentStyle
					|| n.ownerDocument.defaultView.getComputedStyle(n, null);
			if (n.tagName == "SCRIPT")
				return "none";
			if (!s[prop])
				return "LI,P,TR".indexOf(n.tagName) > -1 ? "block"
						: n.style[prop];
			if (s[prop] == "block" && n.tagName == "TD")
				return "feaux-inline";
			return s[prop];
		}

		var blockTypeNodes = "table-row,block,list-item";
		var isBlock = function(n) {
			// diaply:block or something else
			var s = sty(n, "display") || "feaux-inline";
			if (blockTypeNodes.indexOf(s) > -1)
				return true;
			return false;
		}
		var recurse = function(n) {
			// Loop through all the child nodes
			// and collect the text, noting whether
			// spaces or line breaks are needed.
			if (/pre/.test(sty(n, "whiteSpace"))) {
				t += n.innerHTML.replace(/\t/g, " ").replace(/\n/g, " "); // to match IE
				return "";
			}
			var s = sty(n, "display");
			if (s == "none")
				return "";
			var gap = isBlock(n) ? "\n" : " ";
			t += gap;
			for ( var i = 0; i < n.childNodes.length; i++) {
				var c = n.childNodes[i];
				if (c.nodeType == 3)
					t += c.nodeValue;
				if (c.childNodes.length)
					recurse(c);
			}
			t += gap;
			return t;
		}
		// Use a copy because stuff gets changed
		node = node.cloneNode(true);
		// Line breaks aren't picked up by textContent
		node.innerHTML = node.innerHTML.replace(/<br>/g, "\n");

		// Double line breaks after P tags are desired, but would get
		// stripped by the final RegExp. Using placeholder text.
		var paras = node.getElementsByTagName("p");
		for ( var i = 0; i < paras.length; i++) {
			paras[i].innerHTML += "NEWLINE";
		}

		var t = "";
		removeWhiteSpace(node);
		// Make the call!
		return normalize(recurse(node));
	}-*/;

	/**
	 * Wraps plain text with span preserving new-lines.
	 * 
	 * @param node
	 *            {@link Element} to process
	 * @return plain text content wrapped in span
	 */
	public static SafeHtml asPlainText(Element node) {
		String text = getPlainText(node);
		return SafeHtmlUtils
			.fromTrustedString("<span style=\"white-space: pre-line;\">" + text
				+ "</span>");
	}
}
