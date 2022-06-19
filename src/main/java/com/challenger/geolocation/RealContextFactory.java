package com.challenger.geolocation;

import java.util.Properties;

import com.challenger.geolocation.adapters.caches.redis.RedisConsumedEventTimestampRepository;
import com.challenger.geolocation.adapters.caches.redis.RedisRequestGeolocationRepository;
import com.challenger.geolocation.adapters.http.geolocation.fakeapi.FakeGeolocationApiService;
import com.challenger.geolocation.commons.HttpClientRequest;
import com.challenger.geolocation.config.IpStackGeolocationProperties;
import com.challenger.geolocation.config.RedisProperties;
import com.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;
import com.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;

import redis.clients.jedis.Jedis;

public final class RealContextFactory implements ContextFactory {

	private final HttpClientRequest httpClient;

	private final Jedis jedis;

	private final IpStackGeolocationProperties ipstackProp;
	private final RedisProperties redisProp;

	public RealContextFactory(Properties properties) {

		ipstackProp = IpStackGeolocationProperties.build(properties);
		redisProp = RedisProperties.build(properties);
		if (ipstackProp.getAccessKey() == null) {
			throw new RuntimeException("Please configure the ipstack properties...");
		}

		this.httpClient = new HttpClientRequest();
		this.jedis = new Jedis(redisProp.getHost(),redisProp.getPort());
		this.jedis.auth(redisProp.getPass());
	}

	@Override
	public GeolocationApiService getApiService() {
//		return new IpStackGeolocationService(ipstackProp, httpClient);
		return new  FakeGeolocationApiService();
	}

	@Override
	public RequestGeolocationRepository getRequestGeolocationRepository() {
		return new RedisRequestGeolocationRepository(jedis);
	}

	@Override
	public ConsumedEventTimestampByUserRepository getTimestampRepository() {
		return new RedisConsumedEventTimestampRepository(jedis);
	}

}
