package com.croct.challenger.geolocation.domain.geolocation.service;

import static com.croct.challenger.geolocation.TestsConstants.CLIENT_ID;
import static com.croct.challenger.geolocation.TestsConstants.IP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
@ExtendWith(MockitoExtension.class)
class StoreTimestampEventServiceImplTest {

	
	private final Long TIMESTAMP_UNIX_IN_MILLI = OffsetDateTime.now().toInstant().toEpochMilli();
	
	
	@InjectMocks
	private StoreTimestampEventServiceImpl service;

	@Mock
	private ConsumedEventTimestampByUserRepository repository;
	
	@Captor
	private ArgumentCaptor<String> captorIp;
	
	@Captor
	private  ArgumentCaptor<String> captorClientId;
	
	@Captor
	private  ArgumentCaptor<Long> captorTimestamp;
	
	@Test
	void shouldBeAbleToStoreTimestamp() {

		doNothing().when(repository).store(captorIp.capture(), captorClientId.capture(), captorTimestamp.capture());
		
		 this.service.run(IP, CLIENT_ID, TIMESTAMP_UNIX_IN_MILLI);
	
		verify(repository).store(IP.getIp()	, CLIENT_ID, TIMESTAMP_UNIX_IN_MILLI);
		
		assertEquals(IP.getIp(), captorIp.getValue());
		assertEquals(CLIENT_ID, captorClientId.getValue());
		assertEquals(TIMESTAMP_UNIX_IN_MILLI, captorTimestamp.getValue());
	}
	
	@Test
	void shouldFailCaseIpNull() {
		assertThrows(IllegalArgumentException.class, () ->this.service.run(null, CLIENT_ID, TIMESTAMP_UNIX_IN_MILLI));
	}
	
	@Test
	void shouldFailCaseClientIdNull() {
		assertThrows(IllegalArgumentException.class, () -> this.service.run(IP, null, TIMESTAMP_UNIX_IN_MILLI));
	}
	
	@Test
	void shouldFailCaseTimestampNull() {
		assertThrows(IllegalArgumentException.class, () -> this.service.run(IP, CLIENT_ID, null));
	}
	

	
	


}
