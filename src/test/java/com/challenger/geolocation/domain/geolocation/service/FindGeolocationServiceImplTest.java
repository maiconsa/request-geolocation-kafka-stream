package com.challenger.geolocation.domain.geolocation.service;

import static com.challenger.geolocation.TestsConstants.CLIENT_ID;
import static com.challenger.geolocation.TestsConstants.IP;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenger.geolocation.TestsAssertions;
import com.challenger.geolocation.TestsConstants;
import com.challenger.geolocation.domain.geolocation.entity.Geolocation;
import com.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;
import com.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;
import com.challenger.geolocation.domain.geolocation.service.FindGeolocationServiceImpl;

@ExtendWith(MockitoExtension.class)
class FindGeolocationServiceImplTest {

	@InjectMocks
	private FindGeolocationServiceImpl service;

	@Mock
	private RequestGeolocationRepository repository;

	@Mock
	private GeolocationApiService geolocationApi;

	@Test
	void shouldBeAbleToFindGeolocation() {
		when(geolocationApi.getGeolocation(anyString())).thenReturn(TestsConstants.MOCK_GEOLOCATION);

		Geolocation geolocation = this.service.run(IP, CLIENT_ID);
		TestsAssertions.assertMockedGeolocation(geolocation);
	}



}
