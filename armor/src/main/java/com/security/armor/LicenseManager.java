package com.security.armor;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LicenseManager {
	
	private static final String salt = "YouCanNeverKnowF";
	//private static final byte [] key = new byte[]{'N','O','T','O','M','O','R','O','N','O','T','O','M','O','R','O'};
	public static void main(String arg[]){
		LicenseManager lm = new LicenseManager();
		String result = lm.getEntropy();
		String time  = String.valueOf(System.currentTimeMillis());
		System.out.println("actual: " + result);
		System.out.println("date " + time);
		
		String serial = lm.getSerialNumber(result+time);
		System.out.println("Serial: " + serial);
		String encrypted = encrypt(serial);
		System.out.println(" encrypted "+ encrypted.toUpperCase());
		//String activationKey = lm.generateKey(serial);
		//System.out.println("key: " + activationKey);
		//String decrypted = decrypt(encrypted);
		//System.out.println("decrypted: " +decrypted);
		//String hashed = hash(result,getSalt());
		//System.out.println("hashed: " +hashed);
		//String zipped = compress(hashed).toUpperCase();
		//System.out.println("compressed: "+zipped);
	}
	
	

	public String getEntropy(){
		InetAddress ip;
		byte [] mac = null;
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface nw = NetworkInterface.getByInetAddress(ip);
			mac = nw.getHardwareAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String entropy = byteToString(mac).replace("-", "").toUpperCase();
		return entropy;
	}

	private static String byteToString(byte [] b){
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
           sb.append(String.format("%02X%s", b[i], (i < b.length - 1) ? "-" : ""));		
        }
        return sb.toString();
	}
	
	private static String toHexString(byte[] bytes){
		//Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
    /**
     * This is a simple function that we use to get a serial out of our MAC address. 
     * Say that x is the MAC and y is the serial,the function would be y += x[i] + (i * 2) 
     * where i is the indexof MAC address element.
     * @param mac
     * @return
     */
	private static String getSerialNumber(String mac){
		
		 char charArray [] = mac.toCharArray();
		 int sum=0;
		 int index = 1;
		 for(char c : charArray){
			 if(Character.isDigit(c)){
				 //System.out.println("Integer " + String.valueOf(c)); 
				 sum += Integer.parseInt(String.valueOf(c)) + (index *2);
			 }else if(Character.isLetter(c)){
				//System.out.println("character " + String.valueOf(c)); 
				 switch (String.valueOf(c)) {
					case "A":
						sum +=  10 + (index *2);
						//System.out.println(" A: " + sum);
						break;
					case "B":
						sum +=  11 + (index *2);
						//System.out.println(" B: " + sum);
						break;
					case "C":
						sum +=  12 + (index *2);
						//System.out.println(" C: " + sum);
						break;
					case "D":
						sum +=  13 + (index *2);
						//System.out.println(" D: " + sum);
						break;
					case "E":
						sum +=  14 + (index *2);
						//System.out.println(" E: " + sum);
						break;
					case "F":
						sum +=  15 + (index *2);
						//System.out.println(" F: " + sum);
						break;
					
				}
			 }
			 index++;
		 }
		 
		 return String.valueOf(sum);
	}
	
	/**
	 * In this case, I will use the function f(x) = x2 + 53/x + 113 * (x/4).
	 * @param serial
	 * @return
	 */
	@Deprecated
	private static String generateKey(String serial){
		String key = null;
		Long x = Long.valueOf(serial);
		Long y = x * x + 53/3 + 113 * (x/4);
		return String.valueOf(y);
	}
	
	@Deprecated
	private static String hash(String h, byte [] salt){
		String hexString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt);
			byte[] encrypBytes = md.digest(h.getBytes());
			hexString = toHexString(encrypBytes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hexString;
	}
	
	@Deprecated
	private static String compress(String data){
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		char [] chars = data.toCharArray();
		
		for(Character ch: chars){
			if(map.containsKey(ch)){
				map.put(ch, map.get(ch)+1);
			}else{
				map.put(ch, 1);
			}
		}
		
		StringBuilder b = new StringBuilder();
		Set<Character> keys = map.keySet();
		for(Character k : keys){
			b.append(k);
			if(map.get(k)>1){
				String count = String.valueOf(map.get(k));
				b.append(count);
			}
		}
		
		return b.toString();
	}
	
	private static String encrypt(String data){
		Key k =null;
		byte[] encVal = null;
		try {
			k = generateKey();
			// algorithm/mode/padding , 
			Cipher c = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	        c.init(Cipher.ENCRYPT_MODE, k);
	        encVal = c.doFinal(data.getBytes("UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        String encryptedValue = Base64.getMimeEncoder().encodeToString(encVal);
        return encryptedValue;
	}
	
	
	public static String decrypt(String encryptedData) {
        Key k =null;
        Cipher c =null;
        byte[] decValue = null;
		try {
			k = generateKey();
			c = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	        c.init(Cipher.DECRYPT_MODE, k);
	        byte[] decordedValue =  Base64.getDecoder().decode(encryptedData);
    		decValue = c.doFinal(decordedValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       String decryptedValue = new String(decValue);
        return decryptedValue;
    }
	
	
	private static Key generateKey() throws Exception {
		String actualSalt = new StringBuilder(salt).reverse().toString();
        Key k = new SecretKeySpec(actualSalt.getBytes(), "AES");
        return k;
	}
	
	private static byte[] getSalt(){
	    //Always use a SecureRandom generator
	    SecureRandom sr=null;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //Create array for salt
	    byte[] salt = new byte[16];
	    //Get a random salt
	    sr.nextBytes(salt);
	    //return salt
	    return salt;
	}
	
	
	private static LicenseContent generateLicense(LicenseContent lc){
		
		String mac = lc.getMacAddr();
		//this is in milliseconds
		String validity = lc.getValidity();
		
		return lc;
		
	}
}
