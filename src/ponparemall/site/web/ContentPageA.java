package ponparemall.site.web;

import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import dumber.scrape.web.AContentPage;
import dumber.scrape.web.APageDriver;

public class ContentPageA extends AContentPage {

	public ContentPageA(String infname, String outfname, String...urlroots) {
		super(infname, outfname, urlroots);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JSONObject retrieveContent(Entry<String, String> me, APageDriver pd) {

		JSONObject ret = null;
		
		String urlcur = me.getKey();
		String fidstring = "", catestring = "", titlestring = "", textstring = "";
		String xpathstringformat = "", csstringformat = "";
		//--- fid ---
		//pass		
		//--- cate ---
		catestring = me.getValue();
		//--- title ---
		//*[@id="main"]/div[2]/div[1]/div[2]/ul/li/ul/li[2]   		
		xpathstringformat = "//*[@id='shopItemDetail']/div[2]/div[1]/h1/b";
		List<String> titles = pd.findTextsByXPath(urlcur, xpathstringformat, 1, 3);
		for (String s:titles) {
			titlestring = s;
			break;
		}
		//--- text ---
		csstringformat = "#sectionShopContent";
		List<String> texts = pd.selectAllTextByCSS(urlcur, csstringformat, 1, 3);
		for (String s:texts) {
			textstring = s;
			break;
		}
		
		//-- fill in ---
		if (titlestring.length() == 0 || textstring.length() == 0) {
			return ret;
		} else {
			ret = new JSONObject();		
			ret.put("fid", fidstring);
			ret.put("cate", catestring);
			ret.put("title", titlestring);
			ret.put("text", textstring);
		}	
		return ret;
	}

}
