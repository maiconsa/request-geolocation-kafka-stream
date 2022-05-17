package com.croct.challenger.geolocation.domain.utils;

public final class ValidationUtils {
	
	
	/*
	 * Reference : https://www.geeksforgeeks.org/how-to-validate-an-ip-address-using-regular-expressions-in-java/
	 * */
   private static final String zeroTo255 = "(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";
   private static final String  IPV4_REGEXX = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
	
	public static boolean isIpv4Valid(String  ip) {
		return ip.matches(IPV4_REGEXX);
	}
}
