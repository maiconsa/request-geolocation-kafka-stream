package com.croct.challenger.geolocation.config;

import java.util.Properties;

public class IpStackGeolocationProperties   {
	private final static String PREFIX = "ipstack";
	
	private static final String DEFAULT_URL = "http://api.ipstack.com/";
	
	public static final String ACCESS_KEY = PREFIX.join(".","access_key");
	
	private String url;
	private String accessKey;
	
	public IpStackGeolocationProperties(String url, String accessKey) {
		super();
		this.url = url;
		this.accessKey = accessKey;
	}
	
	private IpStackGeolocationProperties(Properties properties) {
		this.url = DEFAULT_URL;
		this.accessKey = (String) properties.get(ACCESS_KEY);
	
	}
		
	public String getUrl() {
		return this.url; 
	}
	public String getAccessKey() {
		return this.accessKey;
	}
	
	
	public static IpStackGeolocationProperties  build(Properties properties) {
		return new IpStackGeolocationProperties(properties);
	}

	
}
