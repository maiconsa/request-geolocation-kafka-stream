package com.croct.challenger.geolocation.adapters.caches.inmemory;

import static com.croct.challenger.geolocation.TestsConstants.CLIENT_ID;
import static com.croct.challenger.geolocation.TestsConstants.MOCK_GEOLOCATION;
import static com.croct.challenger.geolocation.TestsConstants.VALID_IP;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.croct.challenger.geolocation.adapters.caches.inmemory.InMemoryRequestGeolocationRepository;
import com.croct.challenger.geolocation.domain.geolocation.entity.RequestGeolocation;

@ExtendWith(MockitoExtension.class)
public class InMemoryRequestGeolocationRepositoryTest {
	

	private InMemoryRequestGeolocationRepository mock = new InMemoryRequestGeolocationRepository();
	
	@Test
	public void shouldBeAbleToStoreAndRecoveryRequestGeolocation() {
		mock.store(new RequestGeolocation(MOCK_GEOLOCATION,CLIENT_ID, VALID_IP));
		Optional<RequestGeolocation> suppoedIsPresent=  mock.findLastRequesteredGeolocation(VALID_IP, CLIENT_ID);
		assertTrue(suppoedIsPresent.isPresent());
	}
	
	@Test
	public void shouldBeAbleToRecoveryIfEmpty() {
		Optional<RequestGeolocation> supposedIsEmpty=  mock.findLastRequesteredGeolocation(VALID_IP, CLIENT_ID);
		assertTrue(supposedIsEmpty.isEmpty());
	}
}
