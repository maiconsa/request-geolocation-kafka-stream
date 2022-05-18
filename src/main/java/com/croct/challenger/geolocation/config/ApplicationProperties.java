package com.croct.challenger.geolocation.config;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;

public class ApplicationProperties {
	private static final String PREFIX = "application";
	
	public static final String APP_ID = PREFIX.join(".", StreamsConfig.APPLICATION_ID_CONFIG);
	public static final String BOOTSTRAP_SERVER = PREFIX.join(".", StreamsConfig.BOOTSTRAP_SERVERS_CONFIG);
	public static final String SOURCE_TOPIC = PREFIX.join(".", "topic.source");
	public static final String TARGET_TOPIC = PREFIX.join(".", "topic.target");
	
	public static final String TIME_WINDOW_IN_MINUTES = PREFIX.join(".", "time-window-in-minutes");
	
	private String appId;
	private String bootstrapserver;
	private String sourceTopic;
	private String targetTopic;
	private Long timeWIndowInMinutes; 
	private ApplicationProperties(Properties properties) {

		this.appId = properties.getProperty(APP_ID);
		this.sourceTopic = properties.getProperty(SOURCE_TOPIC);
		this.targetTopic = properties.getProperty(TARGET_TOPIC);
		this.bootstrapserver = properties.getProperty(BOOTSTRAP_SERVER);
		timeWIndowInMinutes = Long.valueOf(properties.getProperty(TIME_WINDOW_IN_MINUTES));

	}

	public String getAppId() {
		return appId;
	}

	public String getSourceTopic() {
		return sourceTopic;
	}

	public String getTargetTopic() {
		return targetTopic;
	}

	public Properties getProperties() {
		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapserver);
		
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
		return properties;
	}
	
	
	public  static ApplicationProperties build(Properties properties) {
		return new ApplicationProperties(properties);
	}
	
	public Duration getTimeWindow() {
		return Duration.ofSeconds(timeWIndowInMinutes*60);
	}
	
}
