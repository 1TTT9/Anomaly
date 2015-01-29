package main.scrape.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.scrape.web.PrintHelper;
import utils.scrape.web.RandomInteger;


public class ProxyAgent {

    public static String _proxyfilename = "proxylist.txt";

	public static List<String> _proxylist = new ArrayList<String>();
    public static List<String> ts_proxyInUse = new ArrayList<String>();
    public static List<String> ts_proxyAvailable = new ArrayList<String>();	

    
    public static void initProxy(String... args) {
    	
    	if (args.length>0) {
    		//change filename
    		_proxyfilename = args[0];
    	}

    	try (BufferedReader br = new BufferedReader(new FileReader(_proxyfilename)))
		{ 
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				_proxylist.add(sCurrentLine);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}    	    	
    	//add to final
    	for (String s:_proxylist) {
    		ts_proxyAvailable.add(s);
    	}

		PrintHelper.print("proxylist filename : %s ", _proxyfilename);
    	PrintHelper.print("Proxy(available/in-use):%d/%d", ts_proxyAvailable.size(), ts_proxyInUse.size());    	
		if (true){ dumpProxylist(); }
    }
        
    public static String acquireProxy() {
    	String _proxystring = "";   	  
    	synchronized(ts_proxyAvailable) {    		
    		if (ts_proxyAvailable.size()>0) {
    	    	int i = RandomInteger.getRandomInteger(0, ts_proxyAvailable.size()-1);
    	    	_proxystring = ts_proxyAvailable.remove(i);    			
    		}else{
        		System.out.println(String.format("fail to retrieve proxy (all occupied: #%d), quit.", ts_proxyInUse.size()));
    		}
    	}
    	if (_proxystring.length()>0) {
        	synchronized(ts_proxyInUse) {    		
    			ts_proxyInUse.add(_proxystring);
    			PrintHelper.print(statProxy());
    		}
    	}
    	return _proxystring;
    }
    
    public static void releaseProxy(String _proxystring) {
    	
    	int i = -1;
    	synchronized(ts_proxyInUse) {
	    	if (!ts_proxyInUse.contains(_proxystring)) {
	    		System.out.println(String.format("[Error] proxy %s wasn't used before!!!, check code again. Quit.", _proxystring));    		
	    	} else {
	    		i = ts_proxyInUse.indexOf(_proxystring);	    		
    			ts_proxyInUse.remove(i);	    		
	    	}
    	}
    	synchronized(ts_proxyAvailable) {    	
	    	if (ts_proxyAvailable.contains(_proxystring)) {
	    		System.out.println(String.format("[Error] proxy %s should not be available before release!!!, check code again. Quit.", _proxystring)); 
	    	} else {
	    		ts_proxyAvailable.add(_proxystring);
	    	}
    	}
    }
    
    static String statProxy() {
    	//non thread-safe
    	return String.format("Proxy(avialable/total):%d/%d", ts_proxyAvailable.size(), ts_proxyAvailable.size()+ts_proxyInUse.size());
    }
    
    public static void dumpProxylist() {  
    	//non thread-safe
    	PrintHelper.print("==========");
    	PrintHelper.print("---dump---\nProxy(available/in-use):%d/%d", ts_proxyAvailable.size(), ts_proxyInUse.size());
    	for (String s:_proxylist) {
    		PrintHelper.print(s);
    	}    	
    	PrintHelper.print("==========");    	
    }    
    
}
