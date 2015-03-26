package mini.scrape.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import utils.scrape.web.PrintHelper;
import utils.scrape.web.RandomInteger;


public class ProxyChecker implements Runnable {

	public static String urlstring = "http://www.example.com";
	
	public static List<String> taskQ = new ArrayList<String>();	
	public static List<Map<String, Boolean>> resQ = new ArrayList<Map<String, Boolean>>();
		
	protected WebDriver driver = null;	
	
	protected String thrName = null;
	
    public static WebDriver setupWebDriver(String...args) {
    	
    	if (args.length>0) {
    		String _proxystring = args[0];
        	org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
        	proxy.setHttpProxy(_proxystring)
        	     .setFtpProxy(_proxystring)
        	     .setSslProxy(_proxystring);
        	DesiredCapabilities cap = new DesiredCapabilities();
        	cap.setCapability(CapabilityType.PROXY, proxy);
        	return new FirefoxDriver(cap);    	    		
    	} else {
    		return new FirefoxDriver();
    	}
        	
    }    	
		
	boolean validate(String proxystring) throws InterruptedException {
		boolean ret = true;
		
		this.driver = setupWebDriver(proxystring);		
		try{
			//this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);				
			this.driver.manage().timeouts().pageLoadTimeout(28, TimeUnit.SECONDS);
			this.driver.get(urlstring);
		} catch (TimeoutException e) {
			//PrintHelper.print("[warning] Timeout (%s), quit.", urlstring);
			ret = false;
		} catch(NoSuchWindowException e1) {
			PrintHelper.print("NoSuchWindowException  %s", urlstring);
			ret = false;
		} 

		this.driver.quit();
		return ret;
	}

	@Override
	public void run() {

		this.thrName = Thread.currentThread().getName().substring( Thread.currentThread().getName().length()-2 );
		
    	while (true) {    	 
    		    		
    		if (taskQ.isEmpty()) 
    			break;
    		
    		String proxystring = taskQ.remove( RandomInteger.getRandomInteger(1, taskQ.size())-1 );    		
    		boolean isAvailable = false;
			try {
				isAvailable = this.validate(proxystring);
			} catch (InterruptedException e) {
			}

			Map<String,Boolean> r = new HashMap<String, Boolean>();
			r.put(proxystring, new Boolean(isAvailable));
    		synchronized (resQ) {
    			resQ.add(r);
			}
 		
    		RandomInteger.waitSecond(1.0, 5.5);
    	}    	

		PrintHelper.print("(%s-left-ok)", this.thrName);	
	}	
}
