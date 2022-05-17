package com.croct.challenger.geolocation.adapters.streaming.kafka.events;

import java.math.BigDecimal;

public class FindedGeolocationEvent {
	
	private String clientId;
	private String  ip;
	
	private BigDecimal latitude;
	private BigDecimal longitude;
	
	private String country;
	
	private String region;
	
	private String city;
	
	private long timestampUnixInMS;
	
	public FindedGeolocationEvent() {
		super();
	}

	public FindedGeolocationEvent(String clientId, String ip, BigDecimal latitude, BigDecimal longitude, String country,
			String region, String city, long timestampUnixInMS) {
		this.clientId = clientId;
		this.ip = ip;
		this.latitude = latitude;
		this.longitude = longitude;
		this.country = country;
		this.region = region;
		this.city = city;
		this.timestampUnixInMS = timestampUnixInMS;
	}

	public String getClientId() {
		return clientId;
	}

	public String getIp() {
		return ip;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public String getCountry() {
		return country;
	}

	public String getRegion() {
		return region;
	}

	public String getCity() {
		return city;
	}

	public long getTimestampUnixInMS() {
		return timestampUnixInMS;
	}
	
	
	
	
}
