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

package cz.artique.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import cz.artique.client.utils.ClientTextContent;

/**
 * Reimplementation of {@link ClientTextContent} on server side by using DOM
 * defined by jsoup.
 * 
 * @author Adam Juraszek
 * 
 */
public class ServerTextContent {
	private static boolean isWhite(TextNode node) {
		return !node.getWholeText().matches(".*[^\t\n\r ].*");
	}

	private static void findWhite(Node node, List<Node> ws) {
		for (Node n : node.childNodes()) {
			if (n instanceof TextNode && isWhite((TextNode) n)) {
				ws.add(n);
			} else {
				findWhite(n, ws);
			}
		}
	}

	private static void removeWhiteSpace(Element element) {
		List<Node> ws = new ArrayList<Node>();
		findWhite(element, ws);
		for (Node n : ws) {
			n.remove();
		}
	}

	private static String sty(Node node) {
		if (!(node instanceof Element)) {
			return null;
		}
		Element e = (Element) node;
		if (",pre,".indexOf("," + e.tagName() + ",") > -1) {
			return "pre";
		}
		if (",script,style,".indexOf("," + e.tagName() + ",") > -1) {
			return "none";
		}
		if (",li,p,tr,".indexOf("," + e.tagName() + ",") > -1) {
			return "block";
		}
		if (",th,td,".indexOf("," + e.tagName() + ",") > -1) {
			return "inline";
		}
		boolean block = e.isBlock();
		return block ? "block" : "inline";
	}

	private static void recurse(Element element, StringBuilder sb) {
		if (sty(element).equals("pre")) {
			sb.append(element.html().replaceAll("[\t\n]", " "));
		}
		String gap = sty(element).equals("block") ? "\n" : " ";
		sb.append(gap);
		for (Node n : element.childNodes()) {
			if (n instanceof TextNode) {
				sb.append(((TextNode) n).getWholeText());
			} else if (n instanceof Element) {
				recurse((Element) n, sb);
			}
		}
		sb.append(gap);
	}

	private static String normalize(String string) {
		return string
			.replaceAll(" +", " ")
			.replaceAll("(?m)[\t]+", "")
			.replaceAll("(?m)[ ]+$", "")
			.replaceAll("(?m)^[ ]+", "")
			.replaceAll("\n+", "\n")
			.replaceAll("\n+$", "")
			.replaceAll("^\n+", "")
			.replaceAll("\nNEWLINE\n", "\n\n")
			.replaceAll("NEWLINE\n", "\n\n");
	}

	/**
	 * Processes single {@link Element}.
	 * 
	 * @param element
	 *            {@link Element} to process
	 * @return plain text content
	 */
	public static String getPlainText(Element element) {
		element = element.clone();
		element.html(element.html().replaceAll("<br[ /]?>", "\n"));
		Elements elementsByTag = element.getElementsByTag("p");
		for (Element e : elementsByTag) {
			e.html(e.html() + "NEWLINE");
		}

		StringBuilder sb = new StringBuilder();
		removeWhiteSpace(element);

		recurse(element, sb);
		return normalize(sb.toString());
	}

	/**
	 * Processes several {@link Elements} at once separating them by newline.
	 * 
	 * @param elements
	 *            {@link Elements} to process
	 * @return plain text content
	 */
	public static String asPlainText(Elements elements) {
		StringBuilder sb = new StringBuilder();
		for (Element e : elements) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(getPlainText(e));
		}
		return sb.toString();
	}
}
