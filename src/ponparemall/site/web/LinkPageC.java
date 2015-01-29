package ponparemall.site.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.scrape.web.PrintHelper;
import dumber.scrape.web.ALinkPage;
import dumber.scrape.web.APageDriver;

public class LinkPageC extends ALinkPage{

	public LinkPageC(String infname, String outfname, String...urlroots) {
		super(infname, outfname, urlroots);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, String> retrieveLinks(Map.Entry<String, String> me, APageDriver pd) {

		Map<String,String> ret = new HashMap<String,String>();
		
		String urlcur = me.getKey();
		
		int numOfGoods = 30;

		String xpathstringformat = "//*[@id='main']/div[3]/div[6]/div[%d]/a";
		//*[@id="main"]/div[3]/div[6]/div[1]/a
		//*[@id="main"]/div[3]/div[6]/div[2]/a
		List<String> links = pd.findLinksByXPath(urlcur, xpathstringformat, numOfGoods, 3);
		for (String s:links) {	
			ret.put(s, me.getValue());
		}
		return ret;
	}
}
