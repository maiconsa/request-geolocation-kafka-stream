package com.croct.challenger.geolocation;

import java.util.Properties;

import com.croct.challenger.geolocation.adapters.caches.InMemoryConsumedEventTimestampRepository;
import com.croct.challenger.geolocation.adapters.caches.InMemoryRequestGeolocationRepository;
import com.croct.challenger.geolocation.adapters.http.geolocation.ipstack.IpStackGeolocationService;
import com.croct.challenger.geolocation.commons.HttpClientRequest;
import com.croct.challenger.geolocation.config.IpStackGeolocationProperties;
import com.croct.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;

public class RealContextFactory implements ContextFactory {

	private HttpClientRequest httpClient;
	
	IpStackGeolocationProperties ipstackProp; 
	public RealContextFactory(Properties properties) {
		
		 ipstackProp = IpStackGeolocationProperties.build(properties);
		if(ipstackProp.getAccessKey() == null) {
			throw new RuntimeException("Please configure the ipstack properties...");
		}

		this.httpClient = new HttpClientRequest();
	}
	@Override
	public GeolocationApiService getApiService() {
		return new IpStackGeolocationService(ipstackProp ,httpClient);
	}

	@Override
	public RequestGeolocationRepository getRequestGeolocationRepository() {
		return  new InMemoryRequestGeolocationRepository();
	}

	@Override
	public ConsumedEventTimestampByUserRepository getTimestampRepository() {
	return new InMemoryConsumedEventTimestampRepository();
	}

}
