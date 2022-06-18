package com.croct.challenger.geolocation.domain.geolocation.entity;

import java.math.BigDecimal;

public class Geolocation {
	private Location location;

	private String city;
	
	private String region;
	
	private String country;
	
	public Geolocation() {
	}
	
	public Geolocation(BigDecimal latitude, BigDecimal longitude , String city , String region , String country) {
		this.location  = new Location(latitude , longitude);
		this.city  = city;
		this.region  = region;
		this.country  = country;
	}
	public Location getLocation() {
		return location;
	}
	public String getCity() {
		return city;
	}
	public String getRegion() {
		return region;
	}
	public String getCountry() {
		return country;
	}
	

}
