package com.croct.challenger.geolocation.adapters.caches;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;

public class InMemoryConsumedEventTimestampRepository implements ConsumedEventTimestampByUserRepository {
	private final String KEY_FORMAT = "timestamp-%s-%s";
	private final Map<String, Long> inMemoryStorage;
	public InMemoryConsumedEventTimestampRepository() {
		 inMemoryStorage = new ConcurrentHashMap<String, Long>();
	}
	private String getKeyFrom(String clientId, String ip) {
		return String.format(KEY_FORMAT, clientId, ip);
	}

	@Override
	public Optional<Long> get(String ipAddress, String clientId) {
		String key = getKeyFrom(clientId, ipAddress);
		if (inMemoryStorage.containsKey(key)) {
			return Optional.of(inMemoryStorage.get(key));
		}
		return Optional.empty();
	}

	@Override
	public void store(String ipAddress, String clientId, long timestampUnixInMs) {
		inMemoryStorage.put(getKeyFrom(clientId, ipAddress), timestampUnixInMs);
		
	}
}
