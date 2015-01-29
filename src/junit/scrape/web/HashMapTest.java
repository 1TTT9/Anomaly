package junit.scrape.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


public class HashMapTest {

	
	@Test
	public void putall() {
		
		Map<String,String> origin = new HashMap<String, String>();
		
		
		Map<String,String> second = new HashMap<String, String>();		
		Map<String,String> third = new HashMap<String, String>();		
				
		
		String[] test = {"abc", "ggc", "arra", "3238" };
		
		for (String s:test) {
			second.put(s, "");
			third.put(s, "");			
		}		
				
		origin.putAll(second);
		Assert.assertEquals(origin.size(), second.size());
		
		second.clear();
		Assert.assertEquals(origin.size(), third.size());		
		
		
	}
}
