package com.croct.challenger.geolocation.adapters.streaming.kafka.serders;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonKafkaDeserializer<T> implements Deserializer<T> {
	private ObjectMapper mapper;
	private Class<T> clazz;

	public JsonKafkaDeserializer(Class<T> clazz) {
		this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.mapper.findAndRegisterModules();
		this.clazz = clazz;
	}

	@Override
	public T deserialize(String topic, byte[] data) {
		try {
			T value = mapper.readValue(data, clazz);
			return value;
		} catch (Exception e) {
			throw new IllegalArgumentException("JsonDeserializer erro ", e);
		}
	}
}
