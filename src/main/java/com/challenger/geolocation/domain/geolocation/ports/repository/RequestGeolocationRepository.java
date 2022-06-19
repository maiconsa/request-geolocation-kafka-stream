package com.challenger.geolocation.domain.geolocation.ports.repository;

import java.util.Optional;

import com.challenger.geolocation.domain.geolocation.entity.RequestGeolocation;

public interface RequestGeolocationRepository {
	Optional<RequestGeolocation>  findLastRequesteredGeolocation(String ipAddress,String clientId);
	void store(RequestGeolocation request);
}
