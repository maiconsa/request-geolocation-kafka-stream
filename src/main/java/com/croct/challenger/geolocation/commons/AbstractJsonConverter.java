package com.croct.challenger.geolocation.commons;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractJsonConverter<T>   {
	private ObjectMapper mapper;
	public AbstractJsonConverter() {
		this.mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public String apply(T object) throws JsonProcessingException {
		 return mapper.writeValueAsString(object);	
	}
	
	public T  apply(String json,Class<T>clazz) throws StreamReadException, DatabindException, IOException {
		
		return mapper.readValue(json.getBytes(), clazz);
	}
	

}
