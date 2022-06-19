package com.challenger.geolocation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenger.geolocation.Starter;

@ExtendWith(MockitoExtension.class)
public class StarterTest {
	
	@Test
	public void shouldFailIfEnvFieldMissing() {
			assertThrows(Exception.class,() -> new Starter().run());
	}


}
