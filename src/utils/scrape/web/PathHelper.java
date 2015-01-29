package utils.scrape.web;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PathHelper {

	static String _rootpathstring = "";
	
	public static String initPath(String rootstring) {
		
		String todaystring = new SimpleDateFormat("yyyyMMdd").format(new Date());
		_rootpathstring = _getPath(rootstring, todaystring);
		
		return _rootpathstring;
	}
	
	public static String getRootPath() {
		return _rootpathstring;
	}	
	
	
    public static String _getPath(String path1, String path2) {
    	
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
		boolean success = file2.mkdirs();
		if (!success) {
		    // Directory creation failed
		}    		                
        return file2.getPath();
    }
    
        
    public static String joinPath(String...args)
    {
    	String filepath = _rootpathstring;
    	for (String s:args) {
    		File file1 = new File(filepath);
    		File file2 = new File(file1, s);    		
    		if (! (s.contains(".txt") || s.contains(".csv")) ) {
	    		file2.mkdirs();
    		}
    		filepath = file2.getPath();
    	}
    	return filepath;
    }    
    
}