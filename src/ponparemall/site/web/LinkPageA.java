package ponparemall.site.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.scrape.web.PrintHelper;
import dumber.scrape.web.ALinkPage;
import dumber.scrape.web.APageDriver;

public class LinkPageA extends ALinkPage{

	public LinkPageA(String infname, String outfname, String...urlroots) {
		super(infname, outfname, urlroots);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, String> retrieveLinks(Map.Entry<String, String> me, APageDriver pd) {

		Map<String,String> ret = new HashMap<String,String>();
		
		String urlcur = me.getKey();
		//*[@id="main"]/div[2]/div[1]/div[2]/ul/li/ul/li[2]/a
		String xpathstringformat = "//*[@id='main']/div[2]/div[1]/div[2]/ul/li/ul/li[%d]/a";		
		List<String> urls = pd.findLinksByXPath(urlcur, xpathstringformat, 11, 3);		

		//*[@id="main"]/div[2]/div[1]/div[2]/ul/li/ul/li[2]
		xpathstringformat = "//*[@id='main']/div[2]/div[1]/div[2]/ul/li/ul/li[%d]";
		List<String> titles = pd.findTextsByXPath(urlcur, xpathstringformat, 11, 3);		

		if (urls.size() == titles.size()) {
			for (int i=0;i<urls.size();i++) {
				ret.put(urls.get(i), titles.get(i));
				PrintHelper.print("c-ok(%s, %s)", titles.get(i), urls.get(i));
			}
		} else {
			PrintHelper.print("Parse error!!! titles/urs: %d/%d", titles.size(), urls.size());
			for (String s:titles){
				PrintHelper.print(s);
			}
			for (String s:urls){
				PrintHelper.print(s);
			}			
		}
		PrintHelper.print("[%s] %d accessed.", LinkPageA.class.getName(), ret.size());		
		return ret;
	}

}
