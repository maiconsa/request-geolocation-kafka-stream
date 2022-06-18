package com.croct.challenger.geolocation.adapters.caches.inmemory;

import static com.croct.challenger.geolocation.TestsConstants.CLIENT_ID;
import static com.croct.challenger.geolocation.TestsConstants.TIMESTAMP_UNIX_MILLI_NOW;
import static com.croct.challenger.geolocation.TestsConstants.VALID_IP;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InMemoryConsumedEventTimestampRepositoryTest {
	

	private InMemoryConsumedEventTimestampRepository mock = new InMemoryConsumedEventTimestampRepository();
	
	@Test
	public void shouldBeAbleToStoreAndRecoveryRequestTimestamp() {
		mock.store(VALID_IP,CLIENT_ID,TIMESTAMP_UNIX_MILLI_NOW);
		Optional<Long> suppoedIsPresent=  mock.get(VALID_IP, CLIENT_ID);
		assertTrue(suppoedIsPresent.isPresent());
	}
	
	@Test
	public void shouldBeAbleToRecoveryIfEmpty() {
		Optional<Long> supposedIsEmpty=  mock.get(VALID_IP, CLIENT_ID);
		assertTrue(supposedIsEmpty.isEmpty());
	}
}
