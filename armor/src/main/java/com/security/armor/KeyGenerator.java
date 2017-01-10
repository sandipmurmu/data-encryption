package com.security.armor;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyGenerator {

	private static final String secret_word = "YouCanNeverKnowP";
	
	private String algorithm;
	/**
	 * Performs data encryption based on the desired algorithm
	 * @param data
	 * @param algorithm
	 * @return
	 */
	public String encrypt(String data, String algorithm) throws GeneralSecurityException{
		if(null == data){
			throw new GeneralSecurityException("No data supplied");
		}
		if(null==algorithm){
			throw new GeneralSecurityException("Algorithm not specified");
		}
		this.algorithm = algorithm;
		
		SecretKey secretKey = getSecretKey(secret_word);
		Cipher c;
		byte encoded [] = null;
		try {
			c = Cipher.getInstance(algorithm);
			c.init(Cipher.ENCRYPT_MODE, secretKey);
			encoded = c.doFinal(data.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			
			throw new GeneralSecurityException(e);
		}
		String encryptData = Base64.getMimeEncoder().encodeToString(encoded);
		return encryptData;
	}
	
	
	
	
	
	/**
	 * Performs decryption of the encrypted value
	 * @param ecryptedData
	 * @return
	 */
	public String decrypt(String encryptedData) throws GeneralSecurityException{
		if(null==encryptedData){
			throw new GeneralSecurityException("No data supplied");
		}
		SecretKey secretKey = getSecretKey(secret_word);
        Cipher c =null;
        byte[] decValue = null;
		try {
			c = Cipher.getInstance(algorithm);
			c.init(Cipher.DECRYPT_MODE, secretKey);
	        byte[] decordedValue =  Base64.getDecoder().decode(encryptedData);
    		decValue = c.doFinal(decordedValue);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new GeneralSecurityException(e);
		}
	        
		
        String decryptedValue = new String(decValue);
        return decryptedValue;
	}
	
	/**
	 * Generates a secret key from the secret work specified as constant above
	 * @return
	 */
	private static SecretKey getSecretKey(String secret_word){
		String salt = new StringBuilder(secret_word).reverse().toString();
		SecretKey key = new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), "AES");
		return key;
	}
	
}
