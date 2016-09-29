package al.com.dreamdays.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 
 * @author Fatty
 *
 */
public class MD5Util {
	
	
	/**
	 * md5加密
	 * 
	 * @param password
	 * @return
	 */
	public static String makeMD5(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			String pwd = new BigInteger(1, md.digest()).toString(16);
			System.err.println(pwd);
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}
}