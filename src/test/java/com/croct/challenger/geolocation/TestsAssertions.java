package com.croct.challenger.geolocation;
import static com.croct.challenger.geolocation.TestsConstants.CITY;
import static com.croct.challenger.geolocation.TestsConstants.COUNTRY;
import static com.croct.challenger.geolocation.TestsConstants.REGION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import com.croct.challenger.geolocation.domain.geolocation.entity.Geolocation;

public final class TestsAssertions {
	public static void assertMockedGeolocation(Geolocation geolocation) {
		assertNotNull(geolocation);
		assertEquals(CITY, geolocation.getCity());
		assertEquals(REGION, geolocation.getRegion());
		assertEquals(COUNTRY, geolocation.getCountry());
		assertNotNull(geolocation.getLocation());
		assertEquals(BigDecimal.TEN, geolocation.getLocation().getLatitude());
		assertEquals(BigDecimal.TEN, geolocation.getLocation().getLongitude());
	}
}
