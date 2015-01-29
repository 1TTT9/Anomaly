package ponparemall.site.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.scrape.web.PrintHelper;
import dumber.scrape.web.ALinkPage;
import dumber.scrape.web.APageDriver;

public class LinkPageB extends ALinkPage{

	public LinkPageB(String infname, String outfname, String...urlroots) {
		super(infname, outfname, urlroots);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, String> retrieveLinks(Map.Entry<String, String> me, APageDriver pd) {

		Map<String,String> ret = new HashMap<String,String>();
		
		String urlcur = me.getKey();
		
		int numOfGoods = 30;

		String xpathstringformat = "";
		//*[@id="main"]/div[3]/div[5]/div/h2
		xpathstringformat = "//*[@id='main']/div[3]/div[5]/div/h2";
		List<String> text = pd.findTextsByXPath(urlcur, xpathstringformat, 1, 3);

		for (String numstring:text) {
			
			numstring = numstring.split(" ")[0];
			Pattern pattern = Pattern.compile("[0-9]");
			Matcher matcher = pattern.matcher(numstring);
		    // check all occurance
			String t = "";
		    while (matcher.find()) {
		      t += matcher.group();
		    }				
		    numstring = t;				
			int nums = Integer.parseInt(numstring);
			int d = nums/numOfGoods, r = nums%numOfGoods;
			if (r>0) {
				d++;
			}
			for (int i=1;i<=d;i++) {
				String v = urlcur+"?pn="+i;
				ret.put(v, me.getValue());
			}
			PrintHelper.print("[%s ] %s ~(%d)", this.getClass().getName(), urlcur, d);			
		}	
		return ret;
	}

}
