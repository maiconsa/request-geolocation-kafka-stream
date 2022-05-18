package com.croct.challenger.geolocation.adapters.caches;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;

public class InMemoryConsumedEventTimestampRepository implements ConsumedEventTimestampByUserRepository {
	private final CacheKey cacheKey = CacheKey.TIMESTAMP;
	private final Map<String, Long> inMemoryStorage;
	public InMemoryConsumedEventTimestampRepository() {
		 inMemoryStorage = new ConcurrentHashMap<String, Long>();
	}

	@Override
	public Optional<Long> get(String ipAddress, String clientId) {
		String key = cacheKey.getKey(clientId, ipAddress);
		if (inMemoryStorage.containsKey(key)) {
			return Optional.of(inMemoryStorage.get(key));
		}
		return Optional.empty();
	}

	@Override
	public void store(String ipAddress, String clientId, long timestampUnixInMs) {
		String key = cacheKey.getKey(clientId, ipAddress);
		inMemoryStorage.put(key, timestampUnixInMs);
		
	}
}
