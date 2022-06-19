package com.challenger.geolocation.domain.geolocation.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenger.geolocation.domain.geolocation.entity.Geolocation;
import com.challenger.geolocation.domain.geolocation.entity.IpAddress;
import com.challenger.geolocation.domain.geolocation.entity.RequestGeolocation;
import com.challenger.geolocation.domain.geolocation.ports.FindGeolocationByIpAddressService;
import com.challenger.geolocation.domain.geolocation.ports.GeolocationApiService;
import com.challenger.geolocation.domain.geolocation.ports.repository.RequestGeolocationRepository;
import com.challenger.geolocation.domain.utils.AssertionsUtils;

public class FindGeolocationServiceImpl implements FindGeolocationByIpAddressService {
	private final RequestGeolocationRepository repository;
	private final GeolocationApiService geolocationApi;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public FindGeolocationServiceImpl(RequestGeolocationRepository repository, GeolocationApiService geolocationApi) {
		this.repository = repository;
		this.geolocationApi = geolocationApi;
	}

	public Geolocation run(IpAddress ipaddres, String clientId) {
		AssertionsUtils.assertNonNull(clientId,"clientId");
		AssertionsUtils.assertNonNull(ipaddres,"ipaddres");
		
		logger.info("Checking geolocation request on cache...");
		Optional<RequestGeolocation> lastRequest =  repository.findLastRequesteredGeolocation(ipaddres.getIp(), clientId);

		return lastRequest.orElseGet(() -> {
			logger.info("None register find in cache....");
			logger.info("Calling geolocation api...");
			Geolocation geolocation = geolocationApi.getGeolocation(ipaddres.getIp());

			RequestGeolocation request = new RequestGeolocation(geolocation, clientId, ipaddres.getIp());
			logger.info("Save request {}", request);
			repository.store(request);
			logger.info("Geolocation request stored for client {} and  ip {}", clientId, ipaddres.getIp());
			return request;
		}).getGeolocation();
	}
}
