package com.challenger.geolocation.domain.geolocation.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenger.geolocation.domain.FieldValidationException;
import com.challenger.geolocation.domain.geolocation.entity.IpAddress;

@ExtendWith(MockitoExtension.class)
public class IpAddressBuilderTest {
	
	private static final String VALID_IP = "127.0.0.1";
	private static final String INVALID_IP = "127.0.0";

	@Test
	public void shouldBeAbleToBuildIpAddress() {
			IpAddress ipAddress = IpAddress.builder(VALID_IP).build();
			assertNotNull(ipAddress);
			assertEquals(ipAddress.getIp(), VALID_IP);
	}
	
	@Test
	public void shouldFailtBuildInvalidIp() {
		assertThrows(FieldValidationException.class, () -> IpAddress.builder(INVALID_IP).build());
	}
}
