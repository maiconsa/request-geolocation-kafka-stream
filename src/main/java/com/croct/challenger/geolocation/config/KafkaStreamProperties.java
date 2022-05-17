package com.croct.challenger.geolocation.config;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

public class KafkaStreamProperties {

	private String appId;
	private String bootstrapserver;
	private String sourceTopic;
	private String targetTopic;

	public KafkaStreamProperties(String appId, String bootstrapServer, String sourceTopic, String targetTopic) {
		super();

		this.appId = appId;
		this.sourceTopic = sourceTopic;
		this.targetTopic = targetTopic;
		this.bootstrapserver = bootstrapServer;

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
		return properties;
	}
}
