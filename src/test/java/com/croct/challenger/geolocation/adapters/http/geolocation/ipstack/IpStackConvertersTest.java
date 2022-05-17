package com.croct.challenger.geolocation.adapters.http.geolocation.ipstack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.junit.jupiter.MockitoExtension;

import com.croct.challenger.geolocation.commons.ConverterExceptionError;

@ExtendWith(MockitoExtension.class)
public class IpStackConvertersTest {
	
	
	private IpStackConverters converter;
	
	@BeforeEach
	private void setup() {
		converter = new IpStackConverters();
	}
	
	@Test
	public void shouldConvertJsonToIpStackResponse() {
	 IpStackResponse response = 	converter.from(IpStackGeolocationServiceTest.JSON_EXAMPLE);
		
		assertNotNull(response);
		assertEquals("NA", response.getContinentCode());
		assertEquals("North America", response.getContinentName());
		assertEquals("United States", response.getCountryName());
		assertEquals("US", response.getCountryCode());
		assertEquals("90013", response.getZip());
	}
	
	@Test
	public void shouldFailJsonSource(){
		assertThrows(ConverterExceptionError.class, () -> converter.from((String)null) );
	}
	

	@Test
	public void shouldFail(){
		assertThrows(ConverterExceptionError.class, () -> converter.from((IpStackResponse)null) );
	}

}
