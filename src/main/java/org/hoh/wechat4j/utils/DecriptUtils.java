package org.hoh.wechat4j.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Random;

public class DecriptUtils { 
	 
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final String RANDOM_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
	private static final Random RANDOM = new java.util.Random(); 
	
	private static String decriptSHA1(byte[] bytes) {
		int len = bytes.length;
		StringBuilder builder = new StringBuilder(len * 2);
		for (int j = 0; j < len; j++) {
			builder.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			builder.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return builder.toString();
	}
	
	public static final String encode(String decript){
		if (decript == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(decript.getBytes());
			return decriptSHA1(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
    public static final String streamToString(InputStream input) throws UnsupportedEncodingException, IOException{
		if (input == null)
			return "";
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = input.read(b)) != -1;) {
			out.append(new String(b, 0, n, "UTF-8"));
		}
        return out.toString();
    }
    
    /**
     * 
    * @Title: generateNonceString 
    * @Description: TODO(生成JSSDK签名的noncestr) 
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
	public static String generateNonceString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			sb.append(RANDOM_STR.charAt(RANDOM.nextInt(RANDOM_STR.length())));
		}
		return sb.toString();
	}
	
	/**
	 * 
	* @Title: generateTimestamp 
	* @Description: TODO(生成JSSDK签名的timestamp) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String generateTimestamp(){
		  return Long.toString(System.currentTimeMillis() / 1000);
	}
	 
	/**
	 * 
	* @Title: byteToHex 
	* @Description: TODO(字节转Hex) 
	* @param @param hash
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
    
}
