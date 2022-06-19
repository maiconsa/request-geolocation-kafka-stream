package com.challenger.geolocation;

import static com.challenger.geolocation.commons.Env.check;
import static com.challenger.geolocation.commons.Env.extract;
import static java.lang.System.getenv;

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

import com.challenger.geolocation.adapters.streaming.kafka.ProcessGeolocationStreaming;
import com.challenger.geolocation.config.ApplicationProperties;
import com.challenger.geolocation.domain.geolocation.ports.CheckCanConsumeEventService;
import com.challenger.geolocation.domain.geolocation.ports.FindGeolocationByIpAddressService;
import com.challenger.geolocation.domain.geolocation.ports.StoreConsumedTimestampEventService;
import com.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.challenger.geolocation.domain.geolocation.service.CheckByTimeWindowServiceImpl;
import com.challenger.geolocation.domain.geolocation.service.FindGeolocationServiceImpl;
import com.challenger.geolocation.domain.geolocation.service.StoreTimestampEventServiceImpl;

public class Starter {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) throws Exception {
		new Starter().run();
	}
	
	public void run() throws Exception {		
		check();
		Properties config = extract();
		logger.info(config.toString());
		ApplicationProperties applicationProperties = ApplicationProperties.build(config);
		createTopics(applicationProperties.getProperties(), applicationProperties.getSourceTopic(),applicationProperties.getTargetTopic());
			
		ContextEnum context = ContextEnum.valueOf(getenv("CONTEXT_EXECUTION"));
		ContextFactory contextFactory = context.createContext(config);		
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
