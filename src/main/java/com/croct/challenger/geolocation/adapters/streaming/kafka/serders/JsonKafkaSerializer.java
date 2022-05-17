package com.croct.challenger.geolocation.adapters.streaming.kafka.serders;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonKafkaSerializer<T> implements Serializer<T> {
	private ObjectMapper mapper ;
	public JsonKafkaSerializer() {
		this.mapper = new ObjectMapper();
	}
	
	@Override
	public byte[] serialize(String topic, T data) {
		
		try {
			return mapper.writeValueAsBytes(data);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Serialize erro ", e);
		}
	
	}
	
}
