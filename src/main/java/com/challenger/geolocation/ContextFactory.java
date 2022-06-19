package com.challenger.geolocation;

import com.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;
import com.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;

public interface ContextFactory {
		public GeolocationApiService getApiService();
		public RequestGeolocationRepository getRequestGeolocationRepository();
		public ConsumedEventTimestampByUserRepository getTimestampRepository();
		
}
