package com.croct.challenger.geolocation.adapters.streaming.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.croct.challenger.geolocation.TestsConstants;
import com.croct.challenger.geolocation.adapters.streaming.kafka.events.FindedGeolocationEvent;
import com.croct.challenger.geolocation.adapters.streaming.kafka.serders.JsonKafkaDeserializer;
import com.croct.challenger.geolocation.config.KafkaStreamProperties;
import com.croct.challenger.geolocation.domain.geolocation.entity.IpAddress;
import com.croct.challenger.geolocation.domain.geolocation.ports.CheckCanConsumeEventService;
import com.croct.challenger.geolocation.domain.geolocation.ports.FindGeolocationByIpAddressService;
import com.croct.challenger.geolocation.domain.geolocation.ports.StoreConsumedTimestampEventService;

@ExtendWith(MockitoExtension.class)
public class ProcessGeolocationStreamingTest {
	
	private ProcessGeolocationStreaming streaming ;
	
	@Mock
	private FindGeolocationByIpAddressService findGeolocation;
	
	@Mock
	private CheckCanConsumeEventService checkCanConsume;
	
	@Mock
	private StoreConsumedTimestampEventService storeTimestamp;

	
	private KafkaStreamProperties properties = new KafkaStreamProperties("app", "localhost:8080", "source", "target"); 
	
	
	@Test
	public void shouldBeAbleToStart() {
		streaming = new ProcessGeolocationStreaming(findGeolocation, checkCanConsume, storeTimestamp, properties);
		streaming.start();
	}
	
	
	@Test
	public void shoudBeAbleToRequestedGeolocationOutsideTimeWindow() {
		
		when(checkCanConsume.run(any(IpAddress.class), anyString())).thenReturn(true);
		when(findGeolocation.run(any(IpAddress.class), anyString())).thenReturn(TestsConstants.MOCK_GEOLOCATION);
		doNothing().when(storeTimestamp).run(any(IpAddress.class), anyString(), anyLong());
		
		streaming = new ProcessGeolocationStreaming(findGeolocation, checkCanConsume, storeTimestamp, properties);
		TopologyTestDriver testDriver  = new TopologyTestDriver(streaming.getTopology(), properties.getProperties());
		
		TestInputTopic<String, String> inbound = testDriver.createInputTopic(properties.getSourceTopic(),new StringSerializer(),new StringSerializer());
		inbound.pipeInput("",inputJson());
		
		
		verify(findGeolocation).run(any(IpAddress.class), anyString());
		verify(checkCanConsume).run(any(IpAddress.class), anyString());
		verify(storeTimestamp).run(any(IpAddress.class), anyString(), anyLong());
		
		TestOutputTopic<String,String> outbound =  testDriver.createOutputTopic(properties.getTargetTopic(),new StringDeserializer(),new StringDeserializer());
		assertOutput(outbound.readRecord());
	}
	

	
	private void assertOutput(TestRecord<String, String> readRecord) {
		String expectedKey = String.format("%s-%s", TestsConstants.CLIENT_ID, TestsConstants.VALID_IP);
		assertEquals(expectedKey, readRecord.getKey());
		
		FindedGeolocationEvent produced =  new JsonKafkaDeserializer<>(FindedGeolocationEvent.class).deserialize(null,readRecord.getValue().getBytes());
		assertEquals(produced.getRegion(), TestsConstants.MOCK_GEOLOCATION.getRegion());
		assertEquals(produced.getClientId(), TestsConstants.CLIENT_ID);
		assertEquals(produced.getIp(), TestsConstants.VALID_IP);
	}

	private String inputJson() {
		return String.format("{ \"clientId\": \"%s\", \"ip\": \"%s\" ,\"timestampUnixInMs\": %s}",TestsConstants.CLIENT_ID, TestsConstants.VALID_IP,TestsConstants.TIMESTAMP_UNIX_MILLI_NOW);
	}
	
	

	
	
}
