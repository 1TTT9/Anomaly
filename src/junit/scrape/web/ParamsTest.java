package junit.scrape.web;

import main.scrape.web.Params;

import org.junit.Assert;
import org.junit.Test;

import com.beust.jcommander.JCommander;


public class ParamsTest {

	@Test
	public void init() {

		/*
		 * 	@Parameter(names = "-fname", description = "proxylist filename", required = false)
	public String filename = "default.txt";	
	
	@Parameter(names = "-sitename", description = "website name", required = false)
	public String sitename = "default_site";		
	
	@Parameter(names = "-urlstring", description = "url for enquring all links", required = false)
	public String  urlstring = "http://127.0.0.1/";
	
	@Parameter(names = "-linkallfname", description = "urlall filename", required = false)
	public String  linkallfname = sitename + "All.csv";	
	
	@Parameter(names = "-csvfname", description = "csv filename", required = false)
	public String csvfilename = "default.csv";		
		 */

		Params settings  = new Params();
		
		String[] args = {"-fname", "mytest.txt", 
						 "sitename", "mytest.com"};

		new JCommander(settings, args);		
			
		Assert.assertEquals("mytest.txt", settings.filename);
		Assert.assertEquals("mytest.com", settings.sitename);		
	}
}
