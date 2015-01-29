package dumber.scrape.web;

import java.util.HashMap;
import java.util.Map;


public abstract class APage extends AMessage {

	public Map<String, String> urlmap = null;
	
	/***
	 * The constructor of APage must include the necessary file names in order to avoid re-producing when those files are existent.
	 * In here we consider file-first as auot-reloading is required and time-cost is saved.
	 * @param infname	input filename of a CSV file
	 * @param outfname	output filename of a CSV file
	 * @param urlroots	a set of {@link URLs}
	 */
	public APage (String infname, String outfname, String...urlroots) {
		this.infilename = infname;
		this.outfilename = outfname;
		this.urlmap = new HashMap<String, String>();			
		if (urlroots.length>0) {
			for (String u:urlroots) {
				this.urlmap.put(u, "");
			}
		}
	}
	
}
