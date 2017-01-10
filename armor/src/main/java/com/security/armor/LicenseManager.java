package com.security.armor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;


public class LicenseManager {
	
	
	private static String generateLicenseKey(LicenseContent lc){
		
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
	
	private static void writeLicense(LicenseContent lc){
		ObjectMapper mapper = new ObjectMapper();
		mapper.convertValue(lc, Map.class);
		// Create property file
        Properties prop = new Properties();
        for (Entry<String, String> e : lc.getProperties().entrySet()) {
            prop.setProperty(e.getKey(), e.getValue());
        }
     // Write the property file
        try {
			prop.store(new FileWriter(new File(lc.getName()+".txt")), "License file");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
}
