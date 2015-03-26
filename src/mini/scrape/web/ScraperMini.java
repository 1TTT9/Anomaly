package mini.scrape.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.scrape.web.PrintHelper;
import utils.scrape.web.RandomInteger;

import com.beust.jcommander.JCommander;


/** optional **/
import main.scrape.web.IFScraper;
import main.scrape.web.Params;
import main.scrape.web.ProxyAgent;


public class ScraperMini implements IFScraper {

	public static Params settings  = new Params();

	static long start_t = 0l, cur_t = 0l;

	ScraperMini() {		
		display();				
		ProxyAgent.initProxy(settings.filename);
	}	

	static void display() {
		String[] msgs = {"========== notice ==========",
"	 >>java -Dfile.encoding=UTF-8 -jar [JAR_FILE] -fname [PROXY_FILE] -thread [NUM_OF_THREADS]" };
		String msg = "";
		for (String s:msgs)	msg += s + "\n";		
		PrintHelper.print(msg);
	}
		
	
	public static void main(String[] args) {

		start_t = System.currentTimeMillis();		
		new JCommander(settings, args);
		
		ScraperMini ms = new ScraperMini();
						
		ms.execute();
				        
        PrintHelper.print("Finished all threads.");
	}


	@Override
	public void execute() {

		Map<String, Boolean> proxyStatus = new HashMap<String, Boolean>();
		
		for (String s:ProxyAgent._proxylist) {
			proxyStatus.put(s, new Boolean(false));
		}
				
		int nInvalid = 0;
		for (Map.Entry<String, Boolean> mp: proxyStatus.entrySet()) {
			if (!mp.getValue().booleanValue())  nInvalid++;
			
			ProxyChecker.taskQ.add(mp.getKey());
		}
		PrintHelper.print("Total: %d, unavailable: %d", proxyStatus.size(), nInvalid);

		int numOfThrs = settings.numOfThread;		
		if (numOfThrs<1) {
			PrintHelper.print("[warning] threads-node is off(%d). please check @thread if necessary.", numOfThrs);
			return;
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(numOfThrs);
		for (int i=1 ; i<=numOfThrs ; i++) {
			Runnable worker = new ProxyChecker();
			executor.execute(worker);
			RandomInteger.waitSecond(0.5,  1.2);
		}
		
		executor.shutdown();
		
		long period_t = settings.applife;
		ProxyChecker.wait_t = settings.twait;
		
		int counter = 0;
		while (true) {
			if ( executor.isTerminated() ){
				executor.shutdownNow();				
				break;
			}
			
			cur_t = System.currentTimeMillis();
			
			if (cur_t-start_t > period_t) {
				PrintHelper.print("Time up!");
				executor.shutdownNow();
				break;
			}
			
			Map<String,Boolean> ret = null;
			if (!ProxyChecker.resQ.isEmpty()) {
				synchronized(ProxyChecker.resQ){
					ret = ProxyChecker.resQ.remove(0);
				}
				counter++;
			}
			
			if (ret == null) continue;
			
			for (Map.Entry<String, Boolean> mp:ret.entrySet()) {				
				if (proxyStatus.containsKey(mp.getKey())) {
					proxyStatus.put(mp.getKey(), mp.getValue());
				}
			}
			
			if (proxyStatus.size() == counter) {
				counter = 0;				
				for (Map.Entry<String, Boolean> mp: proxyStatus.entrySet()) {
					ProxyChecker.taskQ.add(mp.getKey());
				}
				PrintHelper.print("MAIN: query again.");				
			}
		}
		
		nInvalid = 0;
		for (Map.Entry<String, Boolean> mp: proxyStatus.entrySet()) {
			if (!mp.getValue().booleanValue())  nInvalid++;
			
			ProxyChecker.taskQ.add(mp.getKey());
		}
		PrintHelper.print("Total: %d, unavailable: %d", proxyStatus.size(), nInvalid);		
	}

	@Override
	public void subsricbe() {
		
	}

	@Override
	public void stop() {
		
	}
}
