package dumber.scrape.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import main.scrape.web.ProxyAgent;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import utils.scrape.web.PrintHelper;
import utils.scrape.web.RandomInteger;


public class APageDriver implements IPageDriver {
	
	protected String thrName = "";
	protected WebDriver driver = null;
	protected String proxystring = "";	
	
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
	
	public void recontract() {
		if (this.driver == null) {
			this.proxystring = ProxyAgent.acquireProxy();
	    	if (proxystring.length()>0) {
	    		this.driver = setupWebDriver(this.proxystring);
	    	} else {
	    		this.driver = setupWebDriver();
	    	}
		}
	}
		
	public void terminate() {		
		if (this.proxystring.length()>0) {
			ProxyAgent.releaseProxy(this.proxystring);
			this.proxystring = "";
		}
		
		if (this.driver != null) {
			this.driver.quit();
			this.driver = null;
		}
	}
	
	/***
	 * @param urlcur	an URL string is the targeted web page in which the scraper digs into.
	 * @param stringformat	format string of a element selector. 
	 * @param numOfItems	an Integer adopted to the stringformat to specify and read "numOfItems" items in increasing order.
	 * @param numOfTry	an Integer used to stop accessing when numOfTry happens.
	 * @param access_type  an Integer specifies access formats (CSS/XPATH/HTML) and access data attribute (link/text)
	 * @return return retrieved collection of URLs, titles, and so on.
	 */
	List<String> _accessUrl(String urlcur, String stringformat, int numOfItems, int numOfTry, int access_type) {
		
		List<String> ret = new ArrayList<String>();		
		WebElement item = null;
		//String xpathstring = "", titlestring = "", textstring = "", linkstring = "";
		String xpathstring = "", textstring = "", linkstring = "";
		try{
			this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);				
			this.driver.get(urlcur);									
		} catch (TimeoutException e) {
			PrintHelper.print("[warning] Timeout (%s), quit.", urlcur);
			return ret;
		} catch(NoSuchWindowException e4) {
			PrintHelper.print("NoSuchWindowException  %s", urlcur);
			return ret;
		}
		int numOfError = 0;
		for (int i=1;i<=numOfItems;i++) {
			try {
				if (numOfError>=numOfTry) {
					break;
				}
				xpathstring = String.format(stringformat, i);
				
				if (access_type<3) {
					
					item = this.driver.findElement(By.xpath(xpathstring));				
					switch(access_type) {
					case 1:
						linkstring = item.getAttribute("href");	
						ret.add(linkstring);					
						break;
					case 2:
						textstring = item.getText();	
						ret.add(textstring);
						break;
					}
				} else if (access_type==3) {
					item = this.driver.findElement(By.cssSelector(stringformat));
					item.sendKeys( Keys.chord(Keys.CONTROL, "a") );
					textstring = item.getText();
					ret.add(textstring);
				}				
				RandomInteger.waitSecond(0.3, 0.5);							
				if (numOfError>0) {
					numOfError--;
				}
			} catch(NoSuchElementException e1) {
				numOfError++;					
				PrintHelper.print("NoSuchElementException page/error:(%d/%d) %s", i, numOfError, stringformat);		        						
			} catch(StaleElementReferenceException e2) {
				numOfError++;					
				PrintHelper.print("StaleElementReferenceException page/error:(%d/%d)", i, numOfError);
				RandomInteger.waitSecond(1.2, 1.9);		    			
			} catch(ElementNotVisibleException e3) {
				numOfError++;					
				this.driver.navigate().refresh();		        	
				PrintHelper.print("ElementNotVisibleException page/error:(%d/%d)", i, numOfError);	        	
			} catch(NoSuchWindowException e4) {
				numOfError++;										
				PrintHelper.print("NoSuchWindowException page/error:(%d/%d)", i, numOfError);
				break;
			}
		}		
		
		return ret;
	}
	
	public List<String> findLinksByXPath(String urlcur, String xpathstringformat, int numOfItems, int numOfTry) {

		return this._accessUrl(urlcur, xpathstringformat, numOfItems, numOfTry, 1);				
	}

	public List<String> findTextsByXPath(String urlcur, String xpathstringformat, int numOfItems, int numOfTry) {
	
		return this._accessUrl(urlcur, xpathstringformat, numOfItems, numOfTry, 2);
	}		

	public List<String> selectAllTextByCSS(String urlcur, String cssstringformat, int numOfItems, int numOfTry) {
		
		return this._accessUrl(urlcur, cssstringformat, numOfItems, numOfTry, 3);
	}
}
