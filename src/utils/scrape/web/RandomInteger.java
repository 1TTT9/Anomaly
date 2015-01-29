package utils.scrape.web;

import java.util.Random;

public class RandomInteger {

    public static int getRandomInteger(int aStart, int aEnd){
        Random aRandom = new Random();
        if (aStart > aEnd) {
          throw new IllegalArgumentException("Start cannot exceed End.");
        }
        long range = (long)aEnd - (long)aStart + 1;
        long fraction = (long)(range * aRandom.nextDouble());
        int randomNumber =  (int)(fraction + aStart);    
        return randomNumber;
    }
    
    
    public static void waitSecond(double aStart, double aEnd){
        try {
            long t_wait = RandomInteger.getRandomInteger((int)aStart*1000, (int)aEnd*1000);
        	Thread.sleep(t_wait);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}        	
    }
    
}
