package com.security.armor;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionManager {

	private static EncryptionManager instance;
	private static final String PUBLIC_KEY_FILE="/path/public_key.der";
	private static final String PRIVATE_KEY_FILE="/path/private_key.der";
	
	
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	
	protected EncryptionManager() throws GeneralSecurityException{
		
	}
	
	public static EncryptionManager getEncryptionManager(){
		if(null==instance){
			try {
				instance = new EncryptionManager();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				privateKey = loadPrivateKey(PRIVATE_KEY_FILE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			  try {
				publicKey = loadPublicKey(PUBLIC_KEY_FILE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	
	/**
    *
    * @param filename
    * @return
    * @throws Exception
    */
   private static PublicKey loadPublicKey(String filename) throws Exception {
       DataInputStream dis = new DataInputStream(File.class.getResourceAsStream(filename));
       byte[] keyBytes = new byte[dis.available()];
       dis.readFully(keyBytes);
       dis.close();

       X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
       KeyFactory kf = KeyFactory.getInstance("RSA");
       return kf.generatePublic(spec);
   }
   
   
   /**
   *
   * @param filename
   * @return
   * @throws Exception
   */
  private static PrivateKey loadPrivateKey(String filename) throws Exception {
      File f = new File(filename);
      FileInputStream fis = new FileInputStream(f);
      DataInputStream dis = new DataInputStream(fis);
      byte[] keyBytes = new byte[(int) f.length()];
      dis.readFully(keyBytes);
      dis.close();

      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return kf.generatePrivate(spec);
  }
}

