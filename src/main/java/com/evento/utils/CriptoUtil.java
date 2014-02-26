package com.evento.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CriptoUtil {
	
	public static final String MD5_ALGORITHM = "MD5";
	public static final String SHA1_ALGORITHM = "SHA-1";
	public static final String SHA256_ALGORITHM = "SHA-256";
	
	public static String makeMD5Hash(String word){
		return makeHash(word, MD5_ALGORITHM);
	}
	
	public static String makeHash(String word, String algorithm){
		String retornaHash;
		
		try  {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			
		    BigInteger hash = new BigInteger(1, md.digest(word.getBytes()));
		    retornaHash = hash.toString(16);
		    if (retornaHash.length() % 2 != 0) {
		    	retornaHash = "0" + retornaHash;
		    }
		}
		catch(NoSuchAlgorithmException e)  {
			e.printStackTrace();
			retornaHash = "";
		}
		
		return retornaHash;
	}
	
	public static void main(String args[]){
		String word = "Thiago bichona";
		System.out.println(word + " - " + makeMD5Hash(word));
		
		System.out.println(word + " - " + makeHash(word, MD5_ALGORITHM));
		System.out.println(word + " - " + makeHash(word, SHA1_ALGORITHM));
		System.out.println(word + " - " + makeHash(word, SHA256_ALGORITHM));
	}

}