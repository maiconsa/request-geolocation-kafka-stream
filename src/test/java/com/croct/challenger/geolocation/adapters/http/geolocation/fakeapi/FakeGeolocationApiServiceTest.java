package com.croct.challenger.geolocation.adapters.http.geolocation.fakeapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.croct.challenger.geolocation.TestsConstants;
import com.croct.challenger.geolocation.domain.geolocation.entity.Geolocation;

@ExtendWith(MockitoExtension.class)
public class FakeGeolocationApiServiceTest {
	
	@Test
	public void shouldHaveSucess() {
		FakeGeolocationApiService service = new FakeGeolocationApiService();
		Geolocation geolocation = service.getGeolocation(TestsConstants.VALID_IP);
		assertGeolocation(geolocation);
	
		
	}
	
	private void assertGeolocation(Geolocation geolocation) {
		assertNotNull(geolocation);
		assertEquals(FakeGeolocationApiService.CITY, geolocation.getCity());
		assertEquals(FakeGeolocationApiService.REGION, geolocation.getRegion());
		assertEquals(FakeGeolocationApiService.COUNTRY, geolocation.getCountry());
		assertNotNull(geolocation.getLocation());
		assertEquals(BigDecimal.TEN, geolocation.getLocation().getLatitude());
		assertEquals(BigDecimal.TEN, geolocation.getLocation().getLongitude());

	}
}
