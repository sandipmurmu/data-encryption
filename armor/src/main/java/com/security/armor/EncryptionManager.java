package com.security.armor;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionManager {

	private static EncryptionManager instance;
	
	//private static final String PUBLIC_KEY_FILE = System.getenv("ALG_KEYS")+File.separator+"public_key.der";
	//private static final String PRIVATE_KEY_FILE = System.getenv("ALG_KEYS")+File.separator+"private_key.der";
	private static final String PUBLIC_KEY_FILE="D:\\sandip_git\\public_key.der";
	private static final String PRIVATE_KEY_FILE="D:\\sandip_git\\private_key.der";
	private static PublicKey publicKey;
	private static PrivateKey privateKey;

	protected EncryptionManager() throws GeneralSecurityException {

	}

	/**
	 * @return instance of EncryptionManager
	 */
	public static EncryptionManager getEncryptionManager() {
		
		if (null == instance) {
			
			try {
				instance = new EncryptionManager();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				System.out.println("path " +PRIVATE_KEY_FILE);
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
	 * @return PublicKey
	 * @throws Exception
	 */
	private static PublicKey loadPublicKey(String filename) throws Exception {
		File f = new File(filename);
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[dis.available()];
		dis.readFully(keyBytes);
		dis.close();
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
		// X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(publicKeySpec);
	}

	/**
	 *
	 * @param filename
	 * @return PrivateKey
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

	public boolean verify(byte[] data, byte[] sig)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		// Initialize the signing algorithm with our public key
		Signature rsaSignature = Signature.getInstance("SHA1withRSA");
		rsaSignature.initVerify(publicKey);
		// Update the signature algorithm with the data.
		rsaSignature.update(data);
		// Validate the signature
		return rsaSignature.verify(sig);
	}

	public byte[] sign(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		if (privateKey == null) {
			throw new UnsupportedOperationException("Can't sign when the private key is not available.");
		}
		// Initialize the signing algorithm with our private key
		Signature rsaSignature = Signature.getInstance("SHA1withRSA");
		rsaSignature.initSign(privateKey);
		rsaSignature.update(data);
		// Generate the signature.
		return rsaSignature.sign();
	}
}