package com.croct.challenger.geolocation.adapters.caches.redis;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.croct.challenger.geolocation.adapters.caches.CacheKey;
import com.croct.challenger.geolocation.commons.AbstractJsonConverter;
import com.croct.challenger.geolocation.domain.geolocation.entity.RequestGeolocation;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import redis.clients.jedis.Jedis;

public class RedisRequestGeolocationRepository implements RequestGeolocationRepository {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final CacheKey cacheKey = CacheKey.GEOLOCATION;
	private Jedis redisClient;

	private final AbstractJsonConverter<RequestGeolocation> converter = new AbstractJsonConverter<RequestGeolocation>();

	public RedisRequestGeolocationRepository(Jedis redisClient) {
		this.redisClient = redisClient;
	}

	@Override
	public void store(RequestGeolocation request) {
		String key = cacheKey.getKey(request.getClientId(), request.getIp());
		try {
			redisClient.set(key, converter.apply(request));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error while save cache...");
		}
	}

	@Override
	public Optional<RequestGeolocation> findLastRequesteredGeolocation(String ipAddress, String clientId) {
		String key = cacheKey.getKey(clientId, ipAddress);
		if (redisClient.exists(key)) {
			try {
				String json = redisClient.get(key);
				logger.info("Value recovery from redis cache using key {}: {}",key,json);
				RequestGeolocation request = converter.apply(json, RequestGeolocation.class);
				return Optional.of(request);
			} catch (IOException e) {
				logger.error(e.getMessage());
				throw new RuntimeException("Error recovery cache...");
			}
		}
		return Optional.empty();
	}
}
