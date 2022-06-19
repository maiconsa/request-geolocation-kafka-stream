package com.challenger.geolocation.domain.geolocation.service;

import static com.challenger.geolocation.TestsConstants.CLIENT_ID;
import static com.challenger.geolocation.TestsConstants.IP;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.challenger.geolocation.domain.geolocation.service.CheckByTimeWindowServiceImpl;;

@ExtendWith(MockitoExtension.class)
class CheckByTimeWindowServiceImplTest {
	
	private final Long  minutes = 2l;
	private Duration timeWindow = Duration.ofMinutes(minutes);
	
	private CheckByTimeWindowServiceImpl service;

	@Mock
	private ConsumedEventTimestampByUserRepository repository;
	
	@BeforeEach
	public void setup() {
		service = new CheckByTimeWindowServiceImpl(repository, timeWindow);
	}
	
	@Test
	void shouldBeReturnTrueForFirstCalling() {
		boolean result = this.service.run(IP, CLIENT_ID);
		assertTrue(result);
		verify(repository).get(anyString(),anyString());
	}
	
	@Test
	void shouldBeConsumeEventIfOutsideTimeWindow() {

		Long timestampOutsideTimeWindow = OffsetDateTime.now().minusMinutes(minutes *2).toInstant().toEpochMilli();
		when(repository.get(anyString(),anyString())).thenReturn(Optional.of(timestampOutsideTimeWindow));
			
		boolean result = this.service.run(IP, CLIENT_ID);
		assertTrue(result);
		verify(repository).get(anyString(),anyString());
	}
	
	@Test
	void shouldNotBeAbleToConsumeEventIfInsideTimeWindow() {
		
		Long timestampInsideTimeWindow = OffsetDateTime.now().minusMinutes(minutes -1).toInstant().toEpochMilli();
		when(repository.get(anyString(),anyString())).thenReturn(Optional.of(timestampInsideTimeWindow));
			
		boolean result = this.service.run(IP, CLIENT_ID);
		assertFalse(result);
		verify(repository).get(anyString(),anyString());
	}
	
	


}
