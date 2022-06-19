package com.challenger.geolocation.adapters.caches.inmemory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.challenger.geolocation.adapters.caches.CacheKey;
import com.challenger.geolocation.domain.geolocation.entity.RequestGeolocation;
import com.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;

public class InMemoryRequestGeolocationRepository implements RequestGeolocationRepository {
	private final CacheKey cacheKey = CacheKey.GEOLOCATION;
	private Map<String, RequestGeolocation> inMemoryStorage;
	public InMemoryRequestGeolocationRepository() {
		inMemoryStorage = new ConcurrentHashMap<String, RequestGeolocation>();
	}
	@Override
	public  void store(RequestGeolocation request) {
		String key = cacheKey.getKey(request.getClientId(), request.getIp());
		inMemoryStorage.put(key, request);
	}

	@Override
	public Optional<RequestGeolocation> findLastRequesteredGeolocation(String ipAddress, String clientId) {
		String key = cacheKey.getKey(clientId, ipAddress);
		if (inMemoryStorage.containsKey(key)) {
			return Optional.of(inMemoryStorage.get(key));
		}
		return Optional.empty();
	}
}
