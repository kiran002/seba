package utils;

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
	
}
