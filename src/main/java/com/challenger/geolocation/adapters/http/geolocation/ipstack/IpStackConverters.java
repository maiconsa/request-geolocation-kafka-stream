package com.challenger.geolocation.adapters.http.geolocation.ipstack;

import com.challenger.geolocation.commons.ConverterExceptionError;
import com.challenger.geolocation.domain.geolocation.entity.Geolocation;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IpStackConverters {
	private ObjectMapper mapper;
	public IpStackConverters() {
	
		this.mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
	}
	public IpStackResponse from(String json) {
		try {
			IpStackResponse response = mapper.readValue(json.getBytes(), IpStackResponse.class);
			return response;
		}  catch (Exception e) {
			throw new ConverterExceptionError("Erro during converter",e);
		}
	}
	
	public Geolocation from(IpStackResponse ipStackReponse) {
		try {			
			String city = ipStackReponse.getCity();
			String region = ipStackReponse.getRegionName().concat(",").concat(ipStackReponse.getRegionCode());
			String country = ipStackReponse.getCountryName().concat(" - ").concat(ipStackReponse.getCountryCode());
			Geolocation geolocation = new Geolocation(ipStackReponse.getLatitude(), ipStackReponse.getLongitude(),city,region,country);
			return geolocation;
		}  catch (Exception e) {
			throw new ConverterExceptionError("Erro during converter",e);
		}
	}
}
