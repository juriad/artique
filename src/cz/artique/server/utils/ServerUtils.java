package cz.artique.server.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Contains useful static methods.
 * 
 * @author Adam Juraszek
 * 
 */
public class ServerUtils {
	private ServerUtils() {}

	/**
	 * Calculates SHA1 hash of original String.
	 * 
	 * @param original
	 *            original string
	 * @return hashed string
	 */
	public static String toSHA1(String original) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			return original.substring(0, Math.min(32, original.length()));
		}
		byte[] bytes = md.digest(original.getBytes());

		return byteArrayToHexString(bytes);
	}

	private static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
}
