package com.challenger.geolocation.domain.geolocation.ports;

import com.challenger.geolocation.domain.geolocation.entity.Geolocation;

public interface GeolocationApiService {
	Geolocation getGeolocation(String ip);
}
