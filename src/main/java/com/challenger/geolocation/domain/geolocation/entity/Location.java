package com.challenger.geolocation.domain.geolocation.entity;

import java.math.BigDecimal;

public class Location {
	private BigDecimal latitude;
	private BigDecimal longitude;
	public Location() {
	}
	public Location(BigDecimal latitude, BigDecimal longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ \"latitude\":");
		builder.append("\"");
		builder.append(latitude);
		builder.append("\",");
		
		builder.append("{\"longitude\":");
		builder.append("\"");
		builder.append(longitude);
		builder.append("\"}");
		return builder.toString();
	}
	
	
}
