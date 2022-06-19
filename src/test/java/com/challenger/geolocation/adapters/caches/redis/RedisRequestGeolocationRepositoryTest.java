package com.challenger.geolocation.adapters.caches.redis;

import static com.challenger.geolocation.TestsConstants.CLIENT_ID;
import static com.challenger.geolocation.TestsConstants.MOCK_GEOLOCATION;
import static com.challenger.geolocation.TestsConstants.VALID_IP;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenger.geolocation.adapters.caches.redis.RedisRequestGeolocationRepository;
import com.challenger.geolocation.commons.AbstractJsonConverter;
import com.challenger.geolocation.domain.geolocation.entity.RequestGeolocation;
import com.fasterxml.jackson.core.JsonProcessingException;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;

@ExtendWith(MockitoExtension.class)
public class RedisRequestGeolocationRepositoryTest {

	private final static String JSON = "{\"geolocation\":{\"location\":{\"latitude\":10,\"longitude\":10},\"city\":\"MOCK CITY\",\"region\":\"MOCK REGION\",\"country\":\"MOCK COUNTRY\"},\"clientId\":\"cf19bc6c-962a-4d97-be02-dbc81b13ab49\",\"ip\":\"127.0.0.1\",\"timestampUnixInMs\":1655590520981}\n"
			+ "";

	@Mock
	private FakeJedis jedis;

	private RedisRequestGeolocationRepository mock;

	@BeforeEach
	public void setup() {
		this.mock = new RedisRequestGeolocationRepository(jedis);
	}

	@Test
	public void shouldBeAbleToStoreAndRecoveryRequestGeolocation() throws JsonProcessingException {

		when(jedis.exists(anyString())).thenReturn(true);
		when(jedis.get(anyString())).thenReturn(JSON);

		mock.store(new RequestGeolocation(MOCK_GEOLOCATION, CLIENT_ID, VALID_IP));
		Optional<RequestGeolocation> suppoedIsPresent = mock.findLastRequesteredGeolocation(VALID_IP, CLIENT_ID);
		assertTrue(suppoedIsPresent.isPresent());
	}

	@Test
	public void shouldBeAbleToRecoveryIfEmpty() {
		when(jedis.exists(anyString())).thenReturn(false);
		Optional<RequestGeolocation> supposedIsEmpty = mock.findLastRequesteredGeolocation(VALID_IP, CLIENT_ID);
		assertTrue(supposedIsEmpty.isEmpty());
	}

	public static class FakeJedis extends Jedis {
		public FakeJedis() {
		}

		@Override
		public String auth(String password) {
			return "OK";
		}

	}
}
