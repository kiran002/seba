package utils;


public class Library {
	public static int getInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (Exception ex) {
			return 0;
		}
	}
}
