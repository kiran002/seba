package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class Library {
	public static int getInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (Exception ex) {
			return 0;
		}
	}	
	
public static String getActivationCode() {
	Random r = new Random();
	String activationCode="";
	for(int i=0;i<5;i++){
		activationCode = activationCode + r.nextInt(10);
	}
	return activationCode;
}

public static Date addDays(Date date, int days) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(Calendar.DATE, days); // minus number would decrement the days
	return cal.getTime();
}
	
}
