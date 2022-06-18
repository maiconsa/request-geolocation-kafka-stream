package com.croct.challenger.geolocation;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.croct.challenger.geolocation.adapters.caches.inmemory.InMemoryConsumedEventTimestampRepository;
import com.croct.challenger.geolocation.adapters.caches.inmemory.InMemoryRequestGeolocationRepository;
import com.croct.challenger.geolocation.adapters.caches.redis.RedisConsumedEventTimestampRepository;
import com.croct.challenger.geolocation.adapters.caches.redis.RedisRequestGeolocationRepository;
import com.croct.challenger.geolocation.adapters.http.geolocation.fakeapi.FakeGeolocationApiService;
import com.croct.challenger.geolocation.adapters.http.geolocation.ipstack.IpStackGeolocationService;
import com.croct.challenger.geolocation.config.IpStackGeolocationProperties;
import com.croct.challenger.geolocation.config.RedisProperties;

import redis.clients.jedis.Jedis;

@ExtendWith(MockitoExtension.class)
public class AllContextFactoryTest {
		
	@Test
	public void shouldBeToUseInMemoryContext() {
		ContextFactory contextFactory = ContextEnum.IN_MEMORY.createContext(new Properties());	
		assertInstanceOf(FakeGeolocationApiService.class, contextFactory.getApiService());
		assertInstanceOf(InMemoryConsumedEventTimestampRepository.class, contextFactory.getTimestampRepository());
		assertInstanceOf(InMemoryRequestGeolocationRepository.class, contextFactory.getRequestGeolocationRepository());
	}
	
//	@Test
//	public void shouldBeAbleToUseRealContext() {
//		Properties propeties =  new Properties();
//		propeties.put(IpStackGeolocationProperties.ACCESS_KEY, "ACCESS_KEY");
//		propeties.put(RedisProperties.PORT, "1");
//		
//		ContextFactory contextFactory = ContextEnum.REAL.createContext(propeties);	
//		
//		assertInstanceOf(IpStackGeolocationService.class, contextFactory.getApiService());
//		assertInstanceOf(RedisConsumedEventTimestampRepository.class, contextFactory.getTimestampRepository());
//		assertInstanceOf(RedisRequestGeolocationRepository.class, contextFactory.getRequestGeolocationRepository());
//	}
	
	
	
	@Test
	public void shouldFailWhenPassingInvalidContext() {
		assertThrows(IllegalArgumentException.class, () -> ContextEnum.INVALID.createContext(new Properties()));
	}
}
