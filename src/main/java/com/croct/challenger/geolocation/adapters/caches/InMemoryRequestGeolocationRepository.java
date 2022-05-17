package com.croct.challenger.geolocation.adapters.caches;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.croct.challenger.geolocation.domain.geolocation.entity.RequestGeolocation;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;

public class InMemoryRequestGeolocationRepository implements RequestGeolocationRepository {
	private final String KEY_FORMAT = "request-geolocation-%s-%s";
	private Map<String, RequestGeolocation> inMemoryStorage;
	public InMemoryRequestGeolocationRepository() {
		inMemoryStorage = new ConcurrentHashMap<String, RequestGeolocation>();
	}
	@Override
	public  void store(RequestGeolocation request) {
		String key = getKeyFrom(request.getClientId(), request.getIp());
		inMemoryStorage.put(key, request);
	}

	private String getKeyFrom(String clientId, String ip) {
		return String.format(KEY_FORMAT, clientId, ip);
	}

	@Override
	public Optional<RequestGeolocation> findLastRequesteredGeolocation(String ipAddress, String clientId) {
		String key = getKeyFrom(clientId, ipAddress);
		if (inMemoryStorage.containsKey(key)) {
			return Optional.of(inMemoryStorage.get(key));
		}
		return Optional.empty();
	}
}
