package dumber.scrape.web;

public abstract class ALinkPage extends APage implements ILinkPage {
	
	public final static String[] tagstrings = {"title", "text"};
	
	public ALinkPage(String infname, String outfname, String[] urlroots) {
		super(infname, outfname, urlroots);
		// TODO Auto-generated constructor stub
	}


}
