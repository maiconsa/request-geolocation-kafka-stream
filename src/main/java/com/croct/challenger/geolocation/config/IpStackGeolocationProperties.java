package com.croct.challenger.geolocation.config;

import java.util.Properties;

public class IpStackGeolocationProperties   {
	private static final String DEFAULT_URL = "https://api.ipstack.com/";
	
	private String url;
	private String accessKey;
	
	public IpStackGeolocationProperties(String url, String accessKey) {
		super();
		this.url = url;
		this.accessKey = accessKey;
	}
	
	private IpStackGeolocationProperties(Properties properties) {
		this.url = (String) properties.get("url");
		this.accessKey = (String) properties.get("accessKey");
	
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
