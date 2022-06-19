package com.challenger.geolocation.adapters.caches.redis;

import static com.challenger.geolocation.TestsConstants.CLIENT_ID;
import static com.challenger.geolocation.TestsConstants.TIMESTAMP_UNIX_MILLI_NOW;
import static com.challenger.geolocation.TestsConstants.VALID_IP;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenger.geolocation.adapters.caches.redis.RedisConsumedEventTimestampRepository;
import com.challenger.geolocation.adapters.caches.redis.RedisRequestGeolocationRepositoryTest.FakeJedis;

@ExtendWith(MockitoExtension.class)
public class RedisConsumedEventTimestampRepositoryTest {
	

	private RedisConsumedEventTimestampRepository mock;
	
	@Mock
	private FakeJedis jedis;
	
	@BeforeEach
	public void setup() {
		this.mock = new RedisConsumedEventTimestampRepository(jedis);
		
	}
	
	@Test
	public void shouldBeAbleToStoreAndRecoveryRequestTimestamp() {
		when(jedis.exists(anyString())).thenReturn(true);
		when(jedis.get(anyString())).thenReturn(String.valueOf(TIMESTAMP_UNIX_MILLI_NOW));
		mock.store(VALID_IP,CLIENT_ID,TIMESTAMP_UNIX_MILLI_NOW);
		Optional<Long> suppoedIsPresent=  mock.get(VALID_IP, CLIENT_ID);
		assertTrue(suppoedIsPresent.isPresent());
	}
	
	@Test
	public void shouldBeAbleToRecoveryIfEmpty() {
		when(jedis.exists(anyString())).thenReturn(false);
		Optional<Long> supposedIsEmpty=  mock.get(VALID_IP, CLIENT_ID);
		assertTrue(supposedIsEmpty.isEmpty());
	}
}
