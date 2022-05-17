package com.croct.challenger.geolocation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.croct.challenger.geolocation.domain.geolocation.entity.Geolocation;
import com.croct.challenger.geolocation.domain.geolocation.entity.IpAddress;

public class TestsConstants {
	public  static final String CITY = "MOCK CITY";
	public static final String REGION = "MOCK REGION";
	public static final String COUNTRY = "MOCK COUNTRY";

	public static final Geolocation MOCK_GEOLOCATION = new Geolocation(BigDecimal.TEN, BigDecimal.TEN, CITY, REGION, COUNTRY);
	
	public static final  String VALID_IP = "127.0.0.1";
	public static final IpAddress IP  = IpAddress.builder(VALID_IP).build();
	
	public static final String INVALID_IP = "127.0.0";
	
	public static final String CLIENT_ID = 	UUID.randomUUID().toString();
	
	public static final long TIMESTAMP_UNIX_MILLI_NOW = OffsetDateTime.now().toInstant().toEpochMilli();
	

}
