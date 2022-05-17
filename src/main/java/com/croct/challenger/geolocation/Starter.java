package com.croct.challenger.geolocation;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) throws Exception {

		new Starter().run();
		

	}
	
	public void run() throws Exception {
				
		if(!System.getenv().containsKey("IP_STACK_ACCESS_KEY")) {
			throw new  Exception("Please set the IP_STACK_ACCESS_KEY on environment");
		}
		if(!System.getenv().containsKey("KAFKA_BOOTSTRAP_SERVER")) {
			throw new  Exception("Please set the KAFKA_BOOTSTRAP_SERVER on environment");
		}
		
		KafkaStreamProperties kafkaProps =  new KafkaStreamProperties("geolocation-stream-app", System.getenv("KAFKA_BOOTSTRAP_SERVER"),"requested-geolocation-stream","result-geolocation-requested-stream");
		
		Properties ipstackProperties = new Properties();
		ipstackProperties.put("accessKey",System.getenv("IP_STACK_ACCESS_KEY"));
		
		Properties config = new Properties();
		config.put("ipstack", ipstackProperties);
		config.put("kafka-stream",kafkaProps.getProperties());
		
		config.put(StreamsConfig.APPLICATION_ID_CONFIG,"geolocation-stream-app" );
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,System.getenv("KAFKA_BOOTSTRAP_SERVER") );
		createTopics(config, "requested-geolocation-stream","result-geolocation-requested-stream");
		

		ContextFactory contextFactory = ContextEnum.IN_MEMORY.createContext(config);		
		ConsumedEventTimestampByUserRepository timestampRepository = contextFactory.getTimestampRepository();
		
		FindGeolocationByIpAddressService findGeolocation = new FindGeolocationServiceImpl(contextFactory.getRequestGeolocationRepository(), contextFactory.getApiService());
		CheckCanConsumeEventService checkCanConsume = new CheckByTimeWindowServiceImpl(timestampRepository ,Duration.ofMinutes(2));
		StoreConsumedTimestampEventService storeTimestamp = new StoreTimestampEventServiceImpl(timestampRepository);
		
		ProcessGeolocationStreaming stream  = new ProcessGeolocationStreaming(findGeolocation,checkCanConsume,storeTimestamp,kafkaProps);
		stream.start();
		
		System.out.println("now epoch milli:" + OffsetDateTime.now().toInstant().toEpochMilli());
	}
	
	 private void createTopics(final Properties allProps, String sourceTopic, String targetTOpic)
	 
	            throws InterruptedException, ExecutionException, TimeoutException {
	        try (final AdminClient client = AdminClient.create(allProps)) {
	            logger.info("Creating topics");

	            List<NewTopic> topics = Arrays.asList(
                        new NewTopic(sourceTopic, Optional.empty(), Optional.empty()),
                        new NewTopic(targetTOpic, Optional.empty(), Optional.empty()));
				client.createTopics(topics).values().forEach( (topic, future) -> {
	                try {
	                    future.get();
	                } catch (Exception ex) {
	                    logger.info(ex.toString());
	                }
	            });

	            Collection<String> topicNames = topics
	                .stream()
	                .map(t -> t.name())
	                .collect(Collectors.toCollection(LinkedList::new));

	            logger.info("Asking cluster for topic descriptions");
	            client
	                .describeTopics(topicNames)
	                .allTopicNames()
	                .get(10, TimeUnit.SECONDS)
	                .forEach((name, description) -> logger.info("Topic Description: {}", description.toString()));
	        }
	    }

	  
	
}
