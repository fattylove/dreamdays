package al.com.dreamdays.utils;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * 
 * @author Fatty
 *
 */
public class Base64Util {

	/**
	 * BASE64 解密
	 * 
	 * @param s
	 * @return
	 */
	public static String BASE64_decode(String s) { 
		if (s == null) return null; 
			BASE64Decoder decoder = new BASE64Decoder(); 
		try { 
			byte[] b = decoder.decodeBuffer(s); 
			return new String(b); 
		} catch (Exception e) { 
			return null; 
		} 
	}
	
	/**
	 * 
	 * BASE64 加密
	 * 
	 * @param s
	 * @return
	 */
	public static String BASE64_encode(String s) { 
		return (new BASE64Encoder()).encode( s.getBytes() ); 
	}

}
