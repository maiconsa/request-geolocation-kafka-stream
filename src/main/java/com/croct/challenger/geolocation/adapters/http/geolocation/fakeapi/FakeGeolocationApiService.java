package com.croct.challenger.geolocation.adapters.http.geolocation.fakeapi;

import java.math.BigDecimal;

import com.croct.challenger.geolocation.domain.geolocation.entity.Geolocation;
import com.croct.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;

public class FakeGeolocationApiService implements GeolocationApiService {
	public static final String CITY = "FAKE CITY";
	public static final String REGION = "FAKE REGION";
	public static final String COUNTRY = "FAKE COUNTRY";


	@Override
	public Geolocation getGeolocation(String ip) {
		return new Geolocation(BigDecimal.TEN, BigDecimal.TEN, CITY	, REGION,COUNTRY );
	}

}
