package com.croct.challenger.geolocation;

import java.util.Properties;

public enum ContextEnum {
	IN_MEMORY, REAL;
	
	public ContextFactory createContext(Properties config) {
		switch (this) {
		case IN_MEMORY:	
			return  new InMemoryContextFactory();
		case REAL:
			return new RealContextFactory(config);
		default:
			throw new IllegalArgumentException("Invalid context value");
		}
	}
	
}
