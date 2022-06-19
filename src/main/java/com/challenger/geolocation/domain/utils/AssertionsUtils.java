package com.challenger.geolocation.domain.utils;

public final class AssertionsUtils {
	public static void assertNonNull(Object object, String objectName){
			if(object == null) throw new IllegalArgumentException(String.format("The object %s must not be null", objectName));
	};
}
