package main.scrape.web;

import com.beust.jcommander.Parameter;
public class Params {
	
	@Parameter(names = "-fname", description = "proxylist filename", required = false)
	public String filename = "default_proxylist.txt";	
	
	@Parameter(names = "-sitename", description = "website name", required = false)
	public String sitename = "default_site";		
	
	@Parameter(names = "-thread", description = "thread number", required = false)
	public int numOfThread = 0;	
}
