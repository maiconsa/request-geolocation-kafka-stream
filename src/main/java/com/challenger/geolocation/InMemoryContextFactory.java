package com.challenger.geolocation;

import com.challenger.geolocation.adapters.caches.inmemory.InMemoryConsumedEventTimestampRepository;
import com.challenger.geolocation.adapters.caches.inmemory.InMemoryRequestGeolocationRepository;
import com.challenger.geolocation.adapters.http.geolocation.fakeapi.FakeGeolocationApiService;
import com.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;
import com.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;

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
