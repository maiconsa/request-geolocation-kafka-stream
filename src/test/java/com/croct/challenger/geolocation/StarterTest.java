package com.croct.challenger.geolocation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StarterTest {
	
	@Test
	public void shouldFailIfEnvFieldMissing() {
			assertThrows(Exception.class,() -> new Starter().run());
	}


}
