package main.scrape.web;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import utils.scrape.web.PrintHelper;
import utils.scrape.web.RandomInteger;
import dumber.scrape.web.AContentPage;
import dumber.scrape.web.APageDriver;
import dumber.scrape.web.IContentPage;

public class Scrapee extends APageDriver implements Runnable {

	long proxy_start_t = 0l;
	final long proxy_refersh_t = 1000*60* RandomInteger.getRandomInteger(28, 45);	
	
	private void checkProxy() {
		long cur_t = System.currentTimeMillis();
		if (cur_t-this.proxy_start_t > this.proxy_refersh_t) {
			this.proxy_start_t = cur_t;
			
			this.terminate();
	    	this.recontract();			
	    	
	    	PrintHelper.print("%s p~%s", this.thrName, this.proxystring);
		}
	}
	
	@Override
	public void run() {

		this.thrName = Thread.currentThread().getName().substring( Thread.currentThread().getName().length()-2 );
		
    	while (true) {    	 
    		
    		this.checkProxy();    		
    		
    		Map<String,String> tmp = null;
    		synchronized (Scraper.cateset) {    			
        		if (Scraper.cateset.isEmpty()) {
        			//no cate
        			break;
        		}
        		        		
        		tmp= new HashMap<String, String>();
        		for (String k:Scraper.cateset.keySet()) {
            		String v = Scraper.cateset.remove(k);
            		tmp.put(k,v);
            		break;        			
        		}
    		}

    		for (Map.Entry<String, String> me:tmp.entrySet()) {
    			PrintHelper.print("c(%s-%s)", this.thrName, me.getKey());
    			//this.retrieveLinkContent(e);    			
    			for (IContentPage cp:Scraper.cplisteners) {	
    				JSONObject obj = cp.retrieveContent(me, this);
					boolean isvalid = true;    				
    				if (obj != null) {
    					for (String k:AContentPage.tagstrings) {
    						if (!obj.containsKey(k)) {
    							PrintHelper.print("[Warning] %s returned JSONObject doesnt have key '%s'", this.thrName, k);
    							isvalid = false;
    							break;
    						}
    					}
    				}
					if (isvalid) {
    					synchronized(Scraper.contentset) {
    						Scraper.contentset.add(obj);
    					}
					}
    			}
    		}    		
    		RandomInteger.waitSecond(2.2, 3.5);
    	}    	
    	
    	this.terminate();    	
		PrintHelper.print("(%s-left-ok)", this.thrName);						
		
	}

}
