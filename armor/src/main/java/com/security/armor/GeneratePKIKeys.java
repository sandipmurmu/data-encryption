package com.security.armor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Savepoint;

public class GeneratePKIKeys {

	private static void generateKeys(String keyAlgorithm, int numBits) {

		try {

			// Get the public/private key pair
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyAlgorithm);
			keyGen.initialize(numBits);
			KeyPair keyPair = keyGen.genKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();

			System.out.println("\n" + "Generating key/value pair using " + privateKey.getAlgorithm() + " algorithm");

			// Get the bytes of the public and private keys
			byte[] privateKeyBytes = privateKey.getEncoded();
			byte[] publicKeyBytes = publicKey.getEncoded();

			// Get the formats of the encoded bytes
			String formatPrivate = privateKey.getFormat(); // PKCS#8
			String formatPublic = publicKey.getFormat(); // X.509
			
			System.out.println("  Private Key Format : " + formatPrivate);
			System.out.println("  Public Key Format  : " + formatPublic);

			// The bytes can be converted back to public and private key objects
			KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
			EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			PrivateKey privateKey2 = keyFactory.generatePrivate(privateKeySpec);

			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			PublicKey publicKey2 = keyFactory.generatePublic(publicKeySpec);
			
			//save public key
			saveToFile("D:\\sandip_git\\public_key.der", publicKeySpec.getEncoded());
			//save private key
			saveToFile("D:\\sandip_git\\private_key.der", privateKeySpec.getEncoded());
			
			// The original and new keys are the same
			System.out.println("  Are both private keys equal? " + privateKey.equals(privateKey2));

			System.out.println("  Are both public keys equal? " + publicKey.equals(publicKey2));

		} catch (InvalidKeySpecException specException) {

			System.out.println("Exception");
			System.out.println("Invalid Key Spec Exception");

		} catch (NoSuchAlgorithmException e) {

			System.out.println("Exception");
			System.out.println("No such algorithm: " + keyAlgorithm);

		}

	}
	
	
	public static void main(String[] args) {

        // Generate a 1024-bit Digital Signature Algorithm (DSA) key pair
        //generateKeys("DSA", 1024);

        // Generate a 576-bit DH key pair
        //generateKeys("DH", 576);

        // Generate a 1024-bit RSA key pair
        generateKeys("RSA", 1024);
    }
	
	
	public static void saveToFile(String path, byte[] key)  {
		File f = new File(path);
		f.getParentFile().mkdirs();

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			fos.write(key);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
