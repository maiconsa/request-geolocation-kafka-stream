package com.croct.challenger.geolocation.domain.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ValidationUtilsTest {
	
	@Test
	public void shouldBeFalseWhenNullParameter() {
		assertFalse(ValidationUtils.isValidUUID(null));
	}
	
	@Test
	public void shouldBeValidWhenValidParameter() {
		assertTrue(ValidationUtils.isValidUUID(UUID.randomUUID().toString()));
	}

}
