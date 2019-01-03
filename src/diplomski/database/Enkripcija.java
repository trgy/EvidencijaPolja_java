package diplomski.database;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Enkripcija {

	public static String MD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger broj = new BigInteger(1, messageDigest);
			String hashtext = broj.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
}
