package com.challenger.geolocation.adapters.http.geolocation.ipstack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenger.geolocation.adapters.http.geolocation.ipstack.IpStackGeolocationService;
import com.challenger.geolocation.commons.HttpClientRequest;
import com.challenger.geolocation.config.IpStackGeolocationProperties;
import com.challenger.geolocation.domain.geolocation.entity.Geolocation;

@ExtendWith(MockitoExtension.class)
public class IpStackGeolocationServiceTest {
	
	private final static String VALID_IP = "127.0.01";

	
	public  final static String JSON_EXAMPLE = "{\n"
			+ "  \"ip\": \"134.201.250.155\",\n"
			+ "  \"hostname\": \"134.201.250.155\",\n"
			+ "  \"type\": \"ipv4\",\n"
			+ "  \"continent_code\": \"NA\",\n"
			+ "  \"continent_name\": \"North America\",\n"
			+ "  \"country_code\": \"US\",\n"
			+ "  \"country_name\": \"United States\",\n"
			+ "  \"region_code\": \"CA\",\n"
			+ "  \"region_name\": \"California\",\n"
			+ "  \"city\": \"Los Angeles\",\n"
			+ "  \"zip\": \"90013\",\n"
			+ "  \"latitude\": 34.0453,\n"
			+ "  \"longitude\": -118.2413,\n"
			+ "  \"location\": {\n"
			+ "    \"geoname_id\": 5368361,\n"
			+ "    \"capital\": \"Washington D.C.\",\n"
			+ "    \"languages\": [\n"
			+ "        {\n"
			+ "          \"code\": \"en\",\n"
			+ "          \"name\": \"English\",\n"
			+ "          \"native\": \"English\"\n"
			+ "        }\n"
			+ "    ],\n"
			+ "    \"country_flag\": \"https://assets.ipstack.com/images/assets/flags_svg/us.svg\",\n"
			+ "    \"country_flag_emoji\": \"22\",\n"
			+ "    \"country_flag_emoji_unicode\": \"U+1F1FA U+1F1F8\",\n"
			+ "    \"calling_code\": \"1\",\n"
			+ "    \"is_eu\": false\n"
			+ "  },\n"
			+ "  \"time_zone\": {\n"
			+ "    \"id\": \"America/Los_Angeles\",\n"
			+ "    \"current_time\": \"2018-03-29T07:35:08-07:00\",\n"
			+ "    \"gmt_offset\": -25200,\n"
			+ "    \"code\": \"PDT\",\n"
			+ "    \"is_daylight_saving\": true\n"
			+ "  },\n"
			+ "  \"currency\": {\n"
			+ "    \"code\": \"USD\",\n"
			+ "    \"name\": \"US Dollar\",\n"
			+ "    \"plural\": \"US dollars\",\n"
			+ "    \"symbol\": \"$\",\n"
			+ "    \"symbol_native\": \"$\"\n"
			+ "  },\n"
			+ "  \"connection\": {\n"
			+ "    \"asn\": 25876,\n"
			+ "    \"isp\": \"Los Angeles Department of Water & Power\"\n"
			+ "  },\n"
			+ "  \"security\": {\n"
			+ "    \"is_proxy\": false,\n"
			+ "    \"proxy_type\": null,\n"
			+ "    \"is_crawler\": false,\n"
			+ "    \"crawler_name\": null,\n"
			+ "    \"crawler_type\": null,\n"
			+ "    \"is_tor\": false,\n"
			+ "    \"threat_level\": \"low\",\n"
			+ "    \"threat_types\": null\n"
			+ "  }\n"
			+ "}";

	
	@InjectMocks
	private IpStackGeolocationService service;


	
	@Mock
	private HttpClientRequest client;
	
	@Mock
	private IpStackGeolocationProperties properties;
	
	@Test
	void shouldBeAbleToRequest() {
		when(client.get(anyString())).thenReturn(JSON_EXAMPLE);
		Geolocation	 geolocation = service.getGeolocation(VALID_IP);
		assertNotNull(geolocation);
		assertEquals("United States - US", geolocation.getCountry());

	}
}
