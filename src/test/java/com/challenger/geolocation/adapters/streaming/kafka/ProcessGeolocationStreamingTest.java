package com.challenger.geolocation.adapters.streaming.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenger.geolocation.TestsConstants;
import com.challenger.geolocation.adapters.streaming.kafka.ProcessGeolocationStreaming;
import com.challenger.geolocation.adapters.streaming.kafka.events.FindedGeolocationEvent;
import com.challenger.geolocation.adapters.streaming.kafka.serders.JsonKafkaDeserializer;
import com.challenger.geolocation.config.ApplicationProperties;
import com.challenger.geolocation.domain.geolocation.entity.IpAddress;
import com.challenger.geolocation.domain.geolocation.ports.CheckCanConsumeEventService;
import com.challenger.geolocation.domain.geolocation.ports.FindGeolocationByIpAddressService;
import com.challenger.geolocation.domain.geolocation.ports.StoreConsumedTimestampEventService;

@ExtendWith(MockitoExtension.class)
public class ProcessGeolocationStreamingTest {
	
	private ProcessGeolocationStreaming streaming ;
	
	@Mock
	private FindGeolocationByIpAddressService findGeolocation;
	
	@Mock
	private CheckCanConsumeEventService checkCanConsume;
	
	@Mock
	private StoreConsumedTimestampEventService storeTimestamp;

	
	
	private ApplicationProperties properties; 
	
	
	@BeforeEach
	public void config() {
		
		Properties config = new Properties();		
		config.put(ApplicationProperties.APP_ID, "app");
		config.put(ApplicationProperties.BOOTSTRAP_SERVER, "dummyserver");
		config.put(ApplicationProperties.SOURCE_TOPIC,"dummy_source_topic");
		config.put(ApplicationProperties.TARGET_TOPIC, "dummy_target_topic");
		config.put(ApplicationProperties.TIME_WINDOW_IN_MINUTES, "2");
		properties = ApplicationProperties.build(config);
	}
	

	
	@Test
	public void shoudBeAbleToRequestedGeolocationOutsideTimeWindow() {
		
		when(checkCanConsume.run(any(IpAddress.class), anyString())).thenReturn(true);
		when(findGeolocation.run(any(IpAddress.class), anyString())).thenReturn(TestsConstants.MOCK_GEOLOCATION);
		doNothing().when(storeTimestamp).run(any(IpAddress.class), anyString(), anyLong());
		
		streaming = new ProcessGeolocationStreaming(findGeolocation, checkCanConsume, storeTimestamp, properties);
		TopologyTestDriver testDriver  = new TopologyTestDriver(streaming.getTopology(), properties.getProperties());
		
		TestInputTopic<String, String> inbound = testDriver.createInputTopic(properties.getSourceTopic(),new StringSerializer(),new StringSerializer());
		TestOutputTopic<String,String> outbound =  testDriver.createOutputTopic(properties.getTargetTopic(),new StringDeserializer(),new StringDeserializer());

		inbound.pipeInput("",validInputJson());
		testDriver.close();
		
		verify(findGeolocation).run(any(IpAddress.class), anyString());
		verify(checkCanConsume).run(any(IpAddress.class), anyString());
		verify(storeTimestamp).run(any(IpAddress.class), anyString(), anyLong());
		
		assertOutput(outbound.readRecord());
	}
	
	
	@Test
	public void shoudNotBeAbleToRequestIfIpNull() {
				
		streaming = new ProcessGeolocationStreaming(findGeolocation, checkCanConsume, storeTimestamp, properties);
		TopologyTestDriver testDriver  = new TopologyTestDriver(streaming.getTopology(), properties.getProperties());
		
		TestInputTopic<String, String> inbound = testDriver.createInputTopic(properties.getSourceTopic(),new StringSerializer(),new StringSerializer());

		inbound.pipeInput("",invalidInputJsonIpNull());
		testDriver.close();
		
		verify(findGeolocation,times(0)).run(any(IpAddress.class), anyString());
		verify(checkCanConsume,times(0)).run(any(IpAddress.class), anyString());
		verify(storeTimestamp,times(0)).run(any(IpAddress.class), anyString(), anyLong());
		
	}
	
	
	@Test
	public void shoudNotBeAbleToRequestIfClientIdInvalid() {
				
		streaming = new ProcessGeolocationStreaming(findGeolocation, checkCanConsume, storeTimestamp, properties);
		TopologyTestDriver testDriver  = new TopologyTestDriver(streaming.getTopology(), properties.getProperties());
		
		TestInputTopic<String, String> inbound = testDriver.createInputTopic(properties.getSourceTopic(),new StringSerializer(),new StringSerializer());

		inbound.pipeInput("",invalidInputJsonClientIdInvalid());
		testDriver.close();
		
		verify(findGeolocation,times(0)).run(any(IpAddress.class), anyString());
		verify(checkCanConsume,times(0)).run(any(IpAddress.class), anyString());
		verify(storeTimestamp,times(0)).run(any(IpAddress.class), anyString(), anyLong());
		
	}
	

	
	private void assertOutput(TestRecord<String, String> readRecord) {
		String expectedKey = String.format("%s-%s", TestsConstants.CLIENT_ID, TestsConstants.VALID_IP);
		assertEquals(expectedKey, readRecord.getKey());
		
		@SuppressWarnings("resource")
		FindedGeolocationEvent produced =  new JsonKafkaDeserializer<>(FindedGeolocationEvent.class).deserialize(null,readRecord.getValue().getBytes());
		assertEquals(produced.getRegion(), TestsConstants.MOCK_GEOLOCATION.getRegion());
		assertEquals(produced.getClientId(), TestsConstants.CLIENT_ID);
		assertEquals(produced.getIp(), TestsConstants.VALID_IP);
	}

	private String validInputJson() {
		return String.format("{ \"clientId\": \"%s\", \"ip\": \"%s\" ,\"timestampUnixInMs\": %s}",TestsConstants.CLIENT_ID, TestsConstants.VALID_IP,TestsConstants.TIMESTAMP_UNIX_MILLI_NOW);
	}
	
	private String invalidInputJsonClientIdInvalid() {
		return String.format("{ \"clientId\": \"%s\", \"ip\": \"%s\" ,\"timestampUnixInMs\": %s}","", TestsConstants.VALID_IP,TestsConstants.TIMESTAMP_UNIX_MILLI_NOW);
	}
	
	private String invalidInputJsonIpNull() {
		return String.format("{ \"clientId\": \"%s\", \"ip\": \"%s\" ,\"timestampUnixInMs\": %s}",TestsConstants.CLIENT_ID,null,TestsConstants.TIMESTAMP_UNIX_MILLI_NOW);
	}
	
	
	

	
	
}
