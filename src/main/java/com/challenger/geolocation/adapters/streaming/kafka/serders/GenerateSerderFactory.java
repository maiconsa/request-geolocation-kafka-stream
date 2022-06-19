package com.challenger.geolocation.adapters.streaming.kafka.serders;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public final class GenerateSerderFactory {
	private GenerateSerderFactory() {

	}

	public static <T> Serde<T> getJsonSerder(Class<T> clazz) {
		return Serdes.serdeFrom(new JsonKafkaSerializer<T>(), new JsonKafkaDeserializer<>(clazz));
	}

}
