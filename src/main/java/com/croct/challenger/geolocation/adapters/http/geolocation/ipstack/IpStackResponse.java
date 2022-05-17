package com.croct.challenger.geolocation.adapters.http.geolocation.ipstack;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IpStackResponse {
	 @JsonProperty("continent_code")
	 private String continentCode;
	 
	 @JsonProperty("continent_name")
	 private String continentName;

	 @JsonProperty("country_code")
	 private String countryCode;

	 
	 @JsonProperty("country_name")
	 private String countryName;
	 
	 @JsonProperty("region_code")
	 private String regionCode;
	 
	 @JsonProperty("region_name")
	 private String regionName;
	 
	 @JsonProperty("city")
	 private String city;

	 @JsonProperty("zip")
	 private String zip;

	 @JsonProperty("latitude")
	 private BigDecimal latitude;
	 
	 @JsonProperty("longitude")
	 private BigDecimal longitude;

	public String getContinentCode() {
		return continentCode;
	}

	public String getContinentName() {
		return continentName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public String getCity() {
		return city;
	}

	public String getZip() {
		return zip;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	 
}
