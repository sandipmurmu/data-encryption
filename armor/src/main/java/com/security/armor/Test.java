package com.security.armor;

public class Test {

	public static void main(String[] args) {
		
		/*Properties props = new Properties();
		props.setProperty("key", "1232");
		props.setProperty("serial", "345sds");
		try {
			props.store(new FileWriter(new File("D:/algomedica/test.txt")), "generac");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		LicenseContent lc = new LicenseContent("sandip", "000a959d6816", "T", String.valueOf(System.currentTimeMillis()));
		LicenseManager lm = new LicenseManager();
		String l = lm.generateLicenseKey(lc);
		System.out.println("[]" +l);
		
	}

}
