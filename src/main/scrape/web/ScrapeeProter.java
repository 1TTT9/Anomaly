package main.scrape.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import utils.scrape.web.PathHelper;
import utils.scrape.web.PrintHelper;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import dumber.scrape.web.ALinkPage;
import dumber.scrape.web.APageDriver;


public class ScrapeeProter extends APageDriver {

	final String thrName = ScrapeeProter.class.getName();
	
	private CsvWriter wrt = null;
	
	private long proxy_start_t = 0l;
	
	final int proxy_refresh_t = 1000*60*30; //30min
	
	ALinkPage ap = null;
	
	public ScrapeeProter(ALinkPage ap) {
		this.ap = ap;
	}
	
	void initCSV() {
		String _fname = PathHelper.joinPath(this.ap.outfilename);

		boolean alreadyExists = false;
		try{
			alreadyExists = new File(_fname).exists();
		} catch(NullPointerException e) {
			e.getStackTrace();
			PrintHelper.print("%s outfilename should be assigned before initialized.", this.thrName);
		}
		
		try {
			this.wrt = new CsvWriter( new FileWriter(_fname, true), ',' );
			if (!alreadyExists)	{				
				for (String s:ALinkPage.tagstrings) {
					this.wrt.write(s);					
				}
				this.wrt.endRecord();
				this.wrt.flush();
			}    	    	    			    		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeCSV() {
		
		if  (this.wrt!=null) {
			this.wrt.close();
		}
	}    	
			
	Map<String,String> readCSV(String fname) {
		String _fname = PathHelper.joinPath(fname);
		Map<String,String> ret = new HashMap<String,String>();
		try {
			CsvReader products = new CsvReader(_fname, ',', StandardCharsets.UTF_8);		
			products.readHeaders();
			String[] headers = products.getHeaders();			
			while (products.readRecord()) {
				//Printer.print("%s --- %s", products.get(headers[0]), products.get(headers[1]));
    	    	ret.put( products.get(headers[1]), products.get(headers[0]));
			}
			products.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}											
		return ret;
	}
	
	void writeCSV(Map<String,String> elemap) {
		for (Map.Entry<String, String> me:elemap.entrySet())
		{
			try {
				this.wrt.write(me.getValue());
				this.wrt.write(me.getKey());
				this.wrt.endRecord();				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}	
	
	private void checkProxy() {
		long cur_t = System.currentTimeMillis();		
		if (cur_t-this.proxy_start_t > this.proxy_refresh_t) {
			this.proxy_start_t = cur_t;			
			this.terminate();
	    	this.recontract();			
	    	PrintHelper.print("%s p~%s", this.thrName, this.proxystring);
		}
	}

	
	Map<String,String> retrieveLinkAgent() {
		Map<String,String> ret = new HashMap<String,String>();	
		String _fname  = "";
		boolean alreadyExists = false;
		try{
			_fname = PathHelper.joinPath(this.ap.outfilename);		
			alreadyExists = new File(_fname).exists();
		} catch(NullPointerException e) {}
		if (alreadyExists) {
			//if out_file already exists
			ret.putAll(this.readCSV(this.ap.outfilename));
		} else {		
			this.initCSV();
			this.checkProxy();				
			alreadyExists = false;
			try {
				_fname = PathHelper.joinPath(this.ap.infilename);		
				alreadyExists = new File(_fname).exists();
			} catch(NullPointerException e) {}
			
			//=== File first ====
			Map<String,String> prev = new HashMap<String,String>();
			if (alreadyExists) {
				//if in_file already exists	
				prev.putAll(this.readCSV(this.ap.infilename));				
			} else if (this.ap.urlmap.size()>0) {
				//if defined already there
				prev.putAll(this.ap.urlmap);
			}else {
				PrintHelper.print("[%s] no [LinkPage.urlmap] assigned, going to quit.", this.thrName);
			}
			for (Map.Entry<String, String> me:prev.entrySet()) {
				Map<String,String> temp = new HashMap<String,String>();
				temp.putAll( this.ap.retrieveLinks(me, this) );
				this.writeCSV(temp);
				ret.putAll( temp );					
			}						
	    	this.terminate();
	    	this.closeCSV();    				
		}
		return ret;		
	}	

}
