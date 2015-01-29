package junit.scrape.web;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import utils.scrape.web.PathHelper;

public class PathHelperTest {

	
	@Test
	public void init() {
		String pathstring = PathHelper.initPath("JUNIT_init_folder");

		File dir = new File(pathstring);
		
		Assert.assertEquals(true, dir.exists());
		
	}

	@Test
	public void join() {
		
		String pathstring1 = PathHelper.getRootPath();
		
		String pathstring2 = PathHelper.joinPath("JUNIT_join_folder", "missing", "in", "action", "test.txt");
		
		Assert.assertEquals(pathstring1+"/JUNIT_join_folder/missing/in/action/test.txt",  pathstring2);				
	}
	
}
