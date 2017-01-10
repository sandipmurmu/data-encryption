package com.security.armor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;


public class LicenseManager {
	
	private static final String SIGNATURE = "signature";
	
	private EncryptionManager encryptionManager;
	
	public LicenseManager() {
		this.encryptionManager = EncryptionManager.getEncryptionManager();
	}
	
	public String generateLicenseKey(LicenseContent lc){
		
		String mac = lc.getMacAddr();
		//this is in milliseconds
		String validity = lc.getValidity();
		String data = new StringBuilder(mac)
				           .append(validity)
				           .toString();
		String licenseKey = null;
		try {
			licenseKey = KeyGenerator.encrypt(data, "AES");
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lc.setProperty("licenseKey", licenseKey);
		writeLicense(lc);
		return toHexString(licenseKey.getBytes(StandardCharsets.UTF_8));
		
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
	
	private  void writeLicense(LicenseContent lc){
	
		// Write the property file
		try {
			byte[] data = writeLicenseToByteArray(lc);
			// Then sign the byte array
	        byte[] signature = this.encryptionManager.sign(data);
	        String base64signature = Base64.getEncoder().encodeToString(signature);
			ObjectMapper mapper = new ObjectMapper();
			mapper.convertValue(lc, Map.class);
			// Create property file
	        Properties prop = new Properties();
	        for (Entry<String, String> e : lc.getProperties().entrySet()) {
	            prop.setProperty(e.getKey(), e.getValue());
	        }
	        prop.put(SIGNATURE, base64signature);
		    prop.store(new FileWriter(new File(lc.getName()+".txt")), "License file");
		} catch (IOException | InvalidKeyException | NoSuchAlgorithmException | SignatureException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	 protected byte[] writeLicenseToByteArray(LicenseContent lc) throws IOException {
        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(dataStream);
        // Sort the key to have a predictable results.
        List<String> keys = new ArrayList<String>(lc.getProperties().keySet());
        Collections.sort(keys);
        for (String key : keys) {
            String value = lc.getProperty(key);
            out.writeChars(key);
            out.writeChars(value);
        }
        out.flush();
        byte[] data = dataStream.toByteArray();
        out.close();
        return data;
	    }
	
}
