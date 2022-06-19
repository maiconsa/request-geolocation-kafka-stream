package com.challenger.geolocation.commons;

public class ConverterExceptionError extends RuntimeException {
		public ConverterExceptionError(String message, Throwable e ) {
			super(message,e);
		}
}
