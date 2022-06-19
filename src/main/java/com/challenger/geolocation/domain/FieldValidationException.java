package com.challenger.geolocation.domain;

public class FieldValidationException extends BusinessException {
	private static final String INVALID_MESSAGE = "The  field  %s is invalid %s";

	public FieldValidationException(String message) {
		super(message);
	}
	
public FieldValidationException(String  field, String value) {
	super(String.format(INVALID_MESSAGE, field,value));
}
}
