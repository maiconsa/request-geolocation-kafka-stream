package com.croct.challenger.geolocation;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.croct.challenger.geolocation.adapters.caches.InMemoryConsumedEventTimestampRepository;
import com.croct.challenger.geolocation.adapters.caches.InMemoryRequestGeolocationRepository;
import com.croct.challenger.geolocation.adapters.http.geolocation.fakeapi.FakeGeolocationApiService;
import com.croct.challenger.geolocation.adapters.http.geolocation.ipstack.IpStackGeolocationService;

import kotlin.NotImplementedError;

@ExtendWith(MockitoExtension.class)
public class AllContextFactoryTest {
	
	
	
	@Test
	public void shouldBeToUseInMemoryContext() {
		ContextFactory contextFactory = ContextEnum.IN_MEMORY.createContext(new Properties());	
		assertInstanceOf(FakeGeolocationApiService.class, contextFactory.getApiService());
		assertInstanceOf(InMemoryConsumedEventTimestampRepository.class, contextFactory.getTimestampRepository());
		assertInstanceOf(InMemoryRequestGeolocationRepository.class, contextFactory.getRequestGeolocationRepository());
	}
	
	@Test
	public void shouldNotBeAbleToUseRealContext() {
		Properties propeties =  new Properties();
		propeties.put("ipstack", new Properties());
		
		ContextFactory contextFactory = ContextEnum.REAL.createContext(propeties);	
		
		assertInstanceOf(IpStackGeolocationService.class, contextFactory.getApiService());
		assertThrows(NotImplementedError.class, () -> contextFactory.getTimestampRepository());
		assertThrows(NotImplementedError.class, () -> contextFactory.getRequestGeolocationRepository());
	}
	
}
