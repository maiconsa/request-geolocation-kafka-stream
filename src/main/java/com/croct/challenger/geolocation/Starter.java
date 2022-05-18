package com.croct.challenger.geolocation;

import java.time.Duration;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.croct.challenger.geolocation.adapters.streaming.kafka.ProcessGeolocationStreaming;
import com.croct.challenger.geolocation.config.ApplicationProperties;
import com.croct.challenger.geolocation.config.IpStackGeolocationProperties;
import com.croct.challenger.geolocation.domain.geolocation.ports.CheckCanConsumeEventService;
import com.croct.challenger.geolocation.domain.geolocation.ports.FindGeolocationByIpAddressService;
import com.croct.challenger.geolocation.domain.geolocation.ports.StoreConsumedTimestampEventService;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.croct.challenger.geolocation.domain.geolocation.service.CheckByTimeWindowServiceImpl;
import com.croct.challenger.geolocation.domain.geolocation.service.FindGeolocationServiceImpl;
import com.croct.challenger.geolocation.domain.geolocation.service.StoreTimestampEventServiceImpl;

public class Starter {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final String REQUIRED_ENVS[] = new String[]{
			"IP_STACK_ACCESS_KEY",
			"KAFKA_BOOTSTRAP_SERVER",
			"TIME_WINDOW_IN_MINUTES",
			"SOURCE_TOPIC",
			"TARGET_TOPIC"};
	
	public static void main(String[] args) throws Exception {

		new Starter().run();
		

	}
	
	public void run() throws Exception {
				
		for (String ENV : REQUIRED_ENVS) {
			if(!System.getenv().containsKey(ENV)) {
				throw new  Exception("Please set the "+ENV+" on environment");
			}
		}
		
		String sourceTopic  = System.getenv("SOURCE_TOPIC");
		String targetTopic  = System.getenv("TARGET_TOPIC");
		
		Properties config = new Properties();
		config.put(IpStackGeolocationProperties.ACCESS_KEY,System.getenv("IP_STACK_ACCESS_KEY"));
		
		config.put(ApplicationProperties.APP_ID, "geolocation-stream-app");
		config.put(ApplicationProperties.BOOTSTRAP_SERVER, System.getenv("KAFKA_BOOTSTRAP_SERVER"));
		config.put(ApplicationProperties.SOURCE_TOPIC, System.getenv("SOURCE_TOPIC"));
		config.put(ApplicationProperties.TARGET_TOPIC, System.getenv("TARGET_TOPIC"));
		config.put(ApplicationProperties.TIME_WINDOW_IN_MINUTES, System.getenv("TIME_WINDOW_IN_MINUTES"));
		
		createTopics(config, sourceTopic,targetTopic);
		

		ApplicationProperties applicationProperties = ApplicationProperties.build(config);
		
		ContextFactory contextFactory = ContextEnum.IN_MEMORY.createContext(config);		
		ConsumedEventTimestampByUserRepository timestampRepository = contextFactory.getTimestampRepository();
		
		FindGeolocationByIpAddressService findGeolocation = new FindGeolocationServiceImpl(contextFactory.getRequestGeolocationRepository(), contextFactory.getApiService());
		CheckCanConsumeEventService checkCanConsume = new CheckByTimeWindowServiceImpl(timestampRepository ,applicationProperties.getTimeWindow());
		StoreConsumedTimestampEventService storeTimestamp = new StoreTimestampEventServiceImpl(timestampRepository);
		
		ProcessGeolocationStreaming stream  = new ProcessGeolocationStreaming(findGeolocation,checkCanConsume,storeTimestamp,applicationProperties);
		stream.start();

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
