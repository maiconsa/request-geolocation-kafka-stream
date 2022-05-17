package com.croct.challenger.geolocation;

import com.croct.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;

public interface ContextFactory {
		public GeolocationApiService getApiService();
		public RequestGeolocationRepository getRequestGeolocationRepository();
		public ConsumedEventTimestampByUserRepository getTimestampRepository();
		
}
