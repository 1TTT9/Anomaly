package dumber.scrape.web;

import java.util.Map;


public interface ILinkPage {

	/***
	 * Return an Map<String,String> Object that can be taken used by next PageLink object or A ContentPage object.
	 * The me parameter must specify an absolute {@link URL} and its belonged category. The pd parameter
	 * is a selenium web-driver wrapper and provided for XPath, CSSPath and HTML query (see {@code APageDriver} for details).
	 * <p>
	 * Currently (2015/01/26) the pd parameter only supports methods, 
	 *  such as "findLinksByXPath", "findTextsByXPath", "selectAllTextByCSS".
	 * @param me	a key-value pair giving the targeted URL(key) along with its category(value).
	 * @param pd	a page-driver is passed to execute search tasks.
	 * @return		a key-value pair restores new URL(s) retrieved from targeted URL so as to its category.
	 */
	Map<String, String> retrieveLinks(Map.Entry<String, String> me, APageDriver pd);
}
