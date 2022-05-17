package com.croct.challenger.geolocation;

import java.util.MissingResourceException;
import java.util.Properties;

import com.croct.challenger.geolocation.adapters.http.geolocation.ipstack.IpStackGeolocationService;
import com.croct.challenger.geolocation.commons.HttpClientRequest;
import com.croct.challenger.geolocation.config.IpStackGeolocationProperties;
import com.croct.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;

import kotlin.NotImplementedError;

public class RealContextFactory implements ContextFactory {
	private  Properties properties;
	
	private HttpClientRequest httpClient;
	
	public RealContextFactory(Properties properties) {
		
		if(!properties.containsKey("ipstack")) {
			throw new MissingResourceException("Missing ipstack properties",null, "ipstack");
		}
		
		this.properties = properties;
		this.httpClient = new HttpClientRequest();
	}
	@Override
	public GeolocationApiService getApiService() {
		return new IpStackGeolocationService(IpStackGeolocationProperties.build((Properties) properties.get("ipstack")) ,httpClient);
	}

	@Override
	public RequestGeolocationRepository getRequestGeolocationRepository() {
		throw new NotImplementedError("Not implemented");
	}

	@Override
	public ConsumedEventTimestampByUserRepository getTimestampRepository() {
		throw new NotImplementedError("Not implemented");
	}

}
