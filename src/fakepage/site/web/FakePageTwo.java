package fakepage.site.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import utils.scrape.web.PrintHelper;
import dumber.scrape.web.ALinkPage;
import dumber.scrape.web.APageDriver;


public class FakePageTwo extends ALinkPage{

	public FakePageTwo(String infname, String outfname, String...urlroots) {
		super(infname, outfname, urlroots);
	}


	@Override
	public Map<String, String> retrieveLinks(Entry<String, String> me,
			APageDriver pd) {
		PrintHelper.print("[%s] I_am_here.", FakePageTwo.class.getName());

		Map<String,String> ret = new HashMap<String,String>();
		return ret;
	}


	
}
