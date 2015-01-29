package utils.scrape.web;

public class PrintHelper {

	public static void print(String msg) {
	    System.out.println(msg);
	}	
	
	public static void print(String format, Object... arguments) {
	    System.out.println(String.format(format, arguments));
	}
}
