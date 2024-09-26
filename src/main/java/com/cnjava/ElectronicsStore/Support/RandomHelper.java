package com.cnjava.ElectronicsStore.Support;

import java.util.Random;

public class RandomHelper {
	public String randomOtp(int length) {
		String values = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
						+ "abcdefghijklmnopqrstuvwxyz"
						+ "0123456789";
		  
        char[] otpCode = new char[length]; 
        for (int i = 0; i < length; i++) 
        	otpCode[i] = values.charAt((new Random()).nextInt(values.length())); 
		
		return new String(otpCode);
	}
}
