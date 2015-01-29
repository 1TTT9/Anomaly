package dumber.scrape.web;

public abstract class AContentPage extends APage implements IContentPage {

	public final static String[] tagstrings = {"fid", "cate", "title", "text"};	
	
	public AContentPage(String infname, String outfname, String[] urlroots) {
		super(infname, outfname, urlroots);
		// TODO Auto-generated constructor stub
	}


}
