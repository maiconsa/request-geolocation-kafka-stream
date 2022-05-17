package com.croct.challenger.geolocation;

import com.croct.challenger.geolocation.adapters.caches.InMemoryConsumedEventTimestampRepository;
import com.croct.challenger.geolocation.adapters.caches.InMemoryRequestGeolocationRepository;
import com.croct.challenger.geolocation.adapters.http.geolocation.fakeapi.FakeGeolocationApiService;
import com.croct.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;

public final class InMemoryContextFactory implements ContextFactory {
	public InMemoryContextFactory() {
	}
	@Override
	public GeolocationApiService getApiService() {
		return new FakeGeolocationApiService();
	}

	@Override
	public RequestGeolocationRepository getRequestGeolocationRepository() {
		return new InMemoryRequestGeolocationRepository();
	}

	@Override
	public  ConsumedEventTimestampByUserRepository getTimestampRepository() {
		return new InMemoryConsumedEventTimestampRepository();
	}

}
