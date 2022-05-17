package com.croct.challenger.geolocation;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Properties;

import com.croct.challenger.geolocation.adapters.streaming.kafka.ProcessGeolocationStreaming;
import com.croct.challenger.geolocation.config.KafkaStreamProperties;
import com.croct.challenger.geolocation.domain.geolocation.ports.CheckCanConsumeEventService;
import com.croct.challenger.geolocation.domain.geolocation.ports.FindGeolocationByIpAddressService;
import com.croct.challenger.geolocation.domain.geolocation.ports.StoreConsumedTimestampEventService;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.croct.challenger.geolocation.domain.geolocation.service.CheckByTimeWindowServiceImpl;
import com.croct.challenger.geolocation.domain.geolocation.service.FindGeolocationServiceImpl;
import com.croct.challenger.geolocation.domain.geolocation.service.StoreTimestampEventServiceImpl;

public class Starter {
	public static void main(String[] args) {
		new Starter().run();
		

	}
	
	public void run() {
		Properties ipstackProperties = new Properties();
		ipstackProperties.put("accessKey","YOUR_ACCESS_KEY");
		
		Properties config = new Properties();
		config.put("ipstack", ipstackProperties);
		config.put("kafka-stream", new KafkaStreamProperties("geolocation-stream-app", "localhost:9092","requested-geolocation-stream","result-geolocation-requested-stream"));
		

		 ContextFactory contextFactory = ContextEnum.IN_MEMORY.createContext(config);		
		ConsumedEventTimestampByUserRepository timestampRepository = contextFactory.getTimestampRepository();
		
		FindGeolocationByIpAddressService findGeolocation = new FindGeolocationServiceImpl(contextFactory.getRequestGeolocationRepository(), contextFactory.getApiService());
		CheckCanConsumeEventService checkCanConsume = new CheckByTimeWindowServiceImpl(timestampRepository ,Duration.ofMinutes(2));
		StoreConsumedTimestampEventService storeTimestamp = new StoreTimestampEventServiceImpl(timestampRepository);
		
		ProcessGeolocationStreaming stream  = new ProcessGeolocationStreaming(findGeolocation,checkCanConsume,storeTimestamp,
				(KafkaStreamProperties) config.get("kafka-stream"));
		stream.start();
		
		System.out.println("now epoch milli:" + OffsetDateTime.now().toInstant().toEpochMilli());
	}
}
