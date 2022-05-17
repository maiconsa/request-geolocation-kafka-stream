package com.croct.challenger.geolocation.domain.geolocation.entity;

import java.time.OffsetDateTime;

public class RequestGeolocation {
	private Geolocation geolocation;
	private String clientId;
	private String ip;
	private long timestampUnixInMs;

	public RequestGeolocation(Geolocation geolocation, String clientId, String ip ) {
		this.geolocation = geolocation;
		this.clientId = clientId;
		this.ip = ip;

		this.timestampUnixInMs = OffsetDateTime.now().toInstant().toEpochMilli();
	}

	public Geolocation getGeolocation() {
		return this.geolocation;
	}

	public String getClientId() {
		return clientId;
	}

	public String getIp() {
		return ip;
	}

	public long getTimestampUnixInMs() {
		return timestampUnixInMs;
	}

	
	
}
