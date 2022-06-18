package com.croct.challenger.geolocation.adapters.caches.redis;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.croct.challenger.geolocation.adapters.caches.CacheKey;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;

import redis.clients.jedis.Jedis;

public class RedisConsumedEventTimestampRepository implements ConsumedEventTimestampByUserRepository {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final CacheKey cacheKey = CacheKey.TIMESTAMP;
	private Jedis redisClient;

	public RedisConsumedEventTimestampRepository(Jedis redisClient) {
		this.redisClient = redisClient;
	}
	
	@Override
	public Optional<Long> get(String ipAddress, String clientId) {
		String key = cacheKey.getKey(clientId, ipAddress);
		if (redisClient.exists(key)) {
			String value =  redisClient.get(key);
			return Optional.of(Long.parseLong(value));
		}
		return Optional.empty();
	}

	@Override
	public void store(String ipAddress, String clientId, long timestampUnixInMs) {
		String key = cacheKey.getKey(clientId, ipAddress);
		redisClient.set(key, String.valueOf(timestampUnixInMs));
		
	}


}
