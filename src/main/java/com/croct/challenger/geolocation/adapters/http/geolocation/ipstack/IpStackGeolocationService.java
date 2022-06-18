package com.croct.challenger.geolocation.adapters.http.geolocation.ipstack;

import com.croct.challenger.geolocation.commons.HttpClientRequest;
import com.croct.challenger.geolocation.config.IpStackGeolocationProperties;
import com.croct.challenger.geolocation.domain.geolocation.entity.Geolocation;
import com.croct.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;

public class IpStackGeolocationService implements GeolocationApiService {
	
	private IpStackGeolocationProperties properties;
	
	private HttpClientRequest client;
	
	private IpStackConverters converters = new IpStackConverters();
	
	public IpStackGeolocationService(final IpStackGeolocationProperties properties,final HttpClientRequest httpClient) {
		this.properties = properties;
		this.client= httpClient;
	}
		
	public Geolocation getGeolocation(String ip) {
		String url = properties.getUrl() + "/"+ip+"?access_key=" +properties.getAccessKey(); 
		String json = client.get(url);
		IpStackResponse response = converters.from(json);
		return converters.from(response);
	}
	
}
