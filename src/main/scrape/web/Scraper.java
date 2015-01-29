package main.scrape.web;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;

import utils.scrape.web.PathHelper;
import utils.scrape.web.PrintHelper;
import utils.scrape.web.RandomInteger;

import com.beust.jcommander.JCommander;
import com.csvreader.CsvWriter;

import dumber.scrape.web.AContentPage;
import dumber.scrape.web.ALinkPage;

/** optional **/
import fakepage.site.web.FakePage;
import fakepage.site.web.FakePageTwo;

import ponparemall.site.web.ContentPageA;
import ponparemall.site.web.LinkPageA;
import ponparemall.site.web.LinkPageB;
import ponparemall.site.web.LinkPageC;


public class Scraper implements IFScraper {

	public static Params settings  = new Params();

	public static Map<String, String> cateset = new HashMap<String, String>();
	public static List<JSONObject> linkset = new ArrayList<JSONObject>();
	public static List<JSONObject> contentset = new ArrayList<JSONObject>();

    public static List<String>    cateName = new ArrayList<String>();
    public static List<CsvWriter> cateWriteHdr = new ArrayList<CsvWriter>();
    public static List<String>    cateFilename = new ArrayList<String>(); 
    public static List<Integer> cateSizeTemp = new ArrayList<Integer>(); //when to write-in    
    public static List<Integer> cateSize = new ArrayList<Integer>();        		

    static List<AContentPage> cplisteners = new ArrayList<AContentPage>();
    static List<ALinkPage> lplisteners  = new ArrayList<ALinkPage>();
    
    
	static long dump_start_t = 0l;    
	static int dump_refresh_t = 1000*40;    	
	static long global_start_t = 0l;
	
	public static void addCPListener(AContentPage ac) {
		cplisteners.add(ac);
	}
	
	public static void addLPListener(ALinkPage ap) {
		lplisteners.add(ap);
	}	

	Scraper() {		
		display();		
		
		PathHelper.initPath(settings.sitename);	
		
		ProxyAgent.initProxy(settings.filename);
	}	

	static void display() {
		String[] msgs = {"========== notice ==========",
"	Steps to create a new webiste scraping:",
"	 - 1. create package [A]",
"	 - 2. create LinkPage class(s) and ContentPage class(s) in package [A]",
"		2.1 LinkPage",
"		2.1.1 inherits from class 'ALinkPage'",
"		2.1.2 overrite class-method 'retrieveLinks'",
"		2.2 ContentPage",
"		2.2.1 inherits from class 'AContentPage'",
"		2.1.2 overrite class-method 'retrieveContent'",
"	 - 3 register created class(s) in order to class 'Scraper'",
"		3.1 link-page listener 'lplistener'",
"		3.2 content-page listener 'cplistener'",
"	 - 4 system parameter",
"		4.1 see class 'Params'",
"	 - NOTTE: please refer to project 'fakepage' and 'ponparemall' for extension details.",
"",
"	 Scrape workflow:",
"	  [urlroot, null] -> [urlstring_A, catestring_A] ->  ... -> [urlstring_Az, catestring_A]  -> [catestring_A, title, text]",
"	  |--------------------       link page  scraping 	    --------------|---------- content page scraping -----------|",
"	  |--------------------       key-value Map 	    ----------------------|------ key-value Map  -> JSONObject --------|",
"",
"	 How to execute:",
"	 >>java -Dfile.encoding=UTF-8 -jar [JAR_FILE] -fname [PROXY_FILE] -thread [NUM_OF_THREADS]" };
		String msg = "";
		for (String s:msgs){ msg += s + "\n"; }
		PrintHelper.print(msg);
	}
	
	public void initCSV(Map<String,String> me) {
		
    	for (Map.Entry<String, String> me2:me.entrySet()) {
    		String _fname = PathHelper.joinPath( me2.getKey() + ".csv");
    		boolean alreadyExists = new File(_fname).exists();
			try {
	    		CsvWriter wrt = new CsvWriter( new FileWriter(_fname, true), ',');
				//use the same index;
	    		cateName.add(me2.getKey());
	    		cateFilename.add(_fname);
	    		cateWriteHdr.add(wrt);
	    		cateSizeTemp.add(0);
	    		cateSize.add(0);
	    		if (!alreadyExists)	{
	    			for (String s:AContentPage.tagstrings) {
	    				wrt.write(s);
	    			}	    			
	    			wrt.endRecord();
	    			wrt.flush();
	    		}    	    	    			    		
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}

	public void closeCSV(Map<String,String> me) {
		
    	for (Map.Entry<String, String> me2:me.entrySet()) {
    		int i = cateName.indexOf(me2.getKey());    		
    		CsvWriter wrt;
    		if (i>=0) {
    			wrt = cateWriteHdr.get(i);
    			wrt.close();
    		}
    	}
	}    
	
	
	public static void main(String[] args) {

		global_start_t = System.currentTimeMillis();		
		new JCommander(settings, args);
		
		Scraper ms = new Scraper();
		
		ms.subsricbe();
				
		ms.execute();
				
		ms.stop();
        
        PrintHelper.print("Finished all threads.");
	}


	@Override
	public void execute() {

		Map<String,String> elemap = new HashMap<String, String>();			
		Map<String,String> _tagsmap = new HashMap<String,String>();

		// temp Map<urlstring, titlestring>
		Map<String,String> temp = new HashMap<String, String>();
		for (ALinkPage ap:lplisteners) {
			if (temp.size()>0) {
				ap.urlmap.putAll(temp);
				temp.clear();
			}
			temp.putAll(new ScrapeeProter(ap).retrieveLinkAgent());
		}

		for (Map.Entry<String, String> me:temp.entrySet()) {
				//elemap <urlstring, titlestring>
				elemap.put(me.getKey(), me.getValue());
				//_tagsmp <titlestring, "">
				_tagsmap.put(me.getValue(), "");
		}
				
		this.initCSV(_tagsmap);		

		//cateset <urlstring, >
		cateset.putAll(elemap);
		
		int _sumOfSet = cateset.size(), _numOfSet = 0;
		int numOfThrs = settings.numOfThread;		
		if (numOfThrs<1) {
			PrintHelper.print("[warning] threads-node is off(%d). please check @thread if necessary.", numOfThrs);
		} else {
			ExecutorService executor = Executors.newFixedThreadPool(numOfThrs);
			for (int i=1 ; i<=numOfThrs ; i++) {
				Runnable worker = new Scrapee();
				executor.execute(worker);
				RandomInteger.waitSecond(0.5,  1.2);

			}
			executor.shutdown();		
			while (true) {
				if ( executor.isTerminated() && contentset.isEmpty()) {
					break;
				}
		        	
				long cur_t = System.currentTimeMillis();
		        	
				if (cur_t-dump_start_t>dump_refresh_t) {
					dump_start_t = cur_t;
					if (_numOfSet>0) {
						PrintHelper.print("MAIN: %d %3.1f(%%)", _numOfSet, (float)_numOfSet/_sumOfSet * 100.0);
		        	}else{
		        		PrintHelper.print("MAIN: %d ", _numOfSet);		        			
		        	}
		        }		        	
		        	
				JSONObject obj = null;

		        synchronized(contentset) {
		        	if (!contentset.isEmpty()) {
		        		obj = contentset.remove(0);       			
		        		_numOfSet++;
		        	}
		        }
		        	
		       	if (obj != null ) {
		       		//examines whether all tagstrings are filled.		       		
		       		boolean isCompleted = true;
		       		for (String s:AContentPage.tagstrings) {
		       			if (!obj.containsKey(s)) {
		       				isCompleted = false;		       				
		       				PrintHelper.print("[Error] key %s is not found in JSONObject.", s);
		       			}
		       		}
		       		if (!isCompleted) { return; }
		       		
		       		int idx = cateName.indexOf(obj.get("cate"));
		       		CsvWriter wrt = cateWriteHdr.get(idx);
		       		cateSizeTemp.set( idx, obj.size()+cateSizeTemp.get(idx) );
		       		try {
		       			for (String s:AContentPage.tagstrings) {
		       				wrt.write((String) obj.get(s));
		       			}	    										
		       			wrt.endRecord();		    			
		       			if (cateSizeTemp.get(idx) >= 1024) {
		       				cateSizeTemp.set(idx, 0);
		       				wrt.flush();
		       			}
		       		} catch (IOException e) {
		       		}	        		
		       	}
			}
		}
		
		this.closeCSV(_tagsmap);
	}

	@Override
	public void stop() {
		
        float spent = (System.currentTimeMillis()-global_start_t)/1000;
        float sum = 0;
        for (Integer i:cateSize) {
        	sum += i.floatValue();
        }
        PrintHelper.print("all are done, all %s items, %10.2f(s), efficiency: %4.2f(s).", sum, spent, spent/sum);
	}

	@Override
	public void subsricbe() {
		/***  USE AS EXAMPLE  ***
		 * lplisteners.add( new FakePage(null, "FakeAll.csv", "http://localhost/test.html") );
		 * lplisteners.add( new FakePageTwo("FakeAll.csv", "FakeAll.1.csv") );
		 ***/
		lplisteners.add( new LinkPageA(null, "ignore.csv", "http://localhost/") );
		//lplisteners.add( new LinkPageA(null, "fishAll.csv", "http://www.ponparemall.com/genre/fish-aquarium") );
		//lplisteners.add( new LinkPageB("fishAll.csv", "fishAll.2.csv") );
		//lplisteners.add( new LinkPageC("fishAll.2.csv", "fishAll.3.csv") );		
		cplisteners.add( new ContentPageA("fishAll.3.csv", null) );
	}
}
