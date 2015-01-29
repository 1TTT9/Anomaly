package dumber.scrape.web;

import java.util.Map;

import org.json.simple.JSONObject;


public interface IContentPage {

	/***
	 * Return a JSONObject Object that will be written into csv file.The me parameter must specify an absolute {@link URL} 
	 * and its belonged category. The pd parameter is a selenium web-driver wrapper and provided for XPath, 
	 * CSSPath and HTML query (see {@code APageDriver} for details). The defined JSONObject keys can be found through
	 *  {@code AContentPage}.
	 * <p>
	 * Currently (2015/01/26) the pd parameter only supports methods, such as "findLinksByXPath", "findTextsByXPath", "selectAllTextByCSS".
	 * returned JSONObject should be null if the content retrieving is failed.
	 * defaulted key-set of JSONObject can be found on {@code AContentPage.tagstrings}.
	 * @param me	a key-value pair giving the targeted URL(key) along with its category(value).
	 * @param pd	a page-driver is passed to execute search tasks.
	 * @return	a JSONOjbect restores content information retrieved from targeted URL.
	 */
	JSONObject retrieveContent(Map.Entry<String, String> me, APageDriver pd);
}
