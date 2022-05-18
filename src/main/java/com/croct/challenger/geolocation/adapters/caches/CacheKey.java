package com.croct.challenger.geolocation.adapters.caches;

import java.util.UUID;

public enum CacheKey {

	GEOLOCATION,TIMESTAMP;
	
	private String prefix;
	private CacheKey( ) {
		this.prefix = UUID.randomUUID().toString();
	}
	
	public String getKey(String clientId,String ip ) {
		return String.format("%s-%s-%s",prefix, clientId, ip);
	}
}
