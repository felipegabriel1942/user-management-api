package com.felipegabriel.usermanagementapi.api.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Md5 implements PasswordEncoder {
	
	@Override
	public String encode(CharSequence charSequence) {
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		try {
			
	    	if (rawPassword == null) {
	    		return false;
	    	}

			return md5Encripter(rawPassword.toString()).equals(encodedPassword);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
    public static String md5Encripter(String rawPassword) throws NoSuchAlgorithmException {
    	 MessageDigest md = MessageDigest.getInstance("MD5");
    	    	
        if (md == null) {
            return "";
        }
        
        return new String(hexCodes(md.digest(rawPassword.getBytes())));
    }
    
    private static char[] hexCodes(byte[] text) {
        char[] hexOutput = new char[text.length * 2];
        String hexString;

        for (int i = 0; i < text.length; i++) {
           hexString = "00" + Integer.toHexString(text[i]);
           hexString.toUpperCase().getChars(hexString.length() - 2,
                   hexString.length(), hexOutput, i * 2);
        }
        return hexOutput;
    }
}
