package com.croct.challenger.geolocation.adapters.streaming.kafka;

import java.time.OffsetDateTime;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.croct.challenger.geolocation.adapters.streaming.kafka.events.FindedGeolocationEvent;
import com.croct.challenger.geolocation.adapters.streaming.kafka.events.RequestedGeolocationEvent;
import com.croct.challenger.geolocation.adapters.streaming.kafka.serders.GenerateSerderFactory;
import com.croct.challenger.geolocation.config.ApplicationProperties;
import com.croct.challenger.geolocation.domain.geolocation.entity.Geolocation;
import com.croct.challenger.geolocation.domain.geolocation.entity.IpAddress;
import com.croct.challenger.geolocation.domain.geolocation.ports.CheckCanConsumeEventService;
import com.croct.challenger.geolocation.domain.geolocation.ports.FindGeolocationByIpAddressService;
import com.croct.challenger.geolocation.domain.geolocation.ports.StoreConsumedTimestampEventService;
import com.croct.challenger.geolocation.domain.utils.ValidationUtils;


public class ProcessGeolocationStreaming {

	private static final String NEW_EVENT_MESSAGE = "New Message  on topic  {}  arrived : {}";

	private String sourceTopic;
	private String targetTopic;

	private final FindGeolocationByIpAddressService findGeolocation;
	private final  CheckCanConsumeEventService checkCanConsume;
	private final StoreConsumedTimestampEventService storeTimestamp;

	private Properties properties;
	final Logger logger = LoggerFactory.getLogger(ProcessGeolocationStreaming.class);

	private Topology topology;
	
	public ProcessGeolocationStreaming(FindGeolocationByIpAddressService findGeolocation,
			CheckCanConsumeEventService checkCanConsume, StoreConsumedTimestampEventService storeTimestamp,
			ApplicationProperties properties) {
		this.sourceTopic = properties.getSourceTopic();
		this.targetTopic = properties.getTargetTopic();
		this.findGeolocation = findGeolocation;
		this.checkCanConsume = checkCanConsume;
		this.storeTimestamp = storeTimestamp;
		this.properties = properties.getProperties();
		registerStream();
	}

	public void start() {
		logger.info("Starting application...");
		KafkaStreams streams = new KafkaStreams(topology, this.properties);
		streams.start();
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	}
	

	/*
	 * Use for Test
	 * */
	public Topology getTopology() {
		return topology;
	}

	private void registerStream() {
		final StreamsBuilder builder = new StreamsBuilder();
		final KStream<String, FindedGeolocationEvent> requestedEvents = builder
				.stream(this.sourceTopic,Consumed.with(Serdes.String(),GenerateSerderFactory.getJsonSerder(RequestedGeolocationEvent.class)))
				.selectKey((key, value) -> value.getKey())
				.peek((k, v) -> logger.info(NEW_EVENT_MESSAGE, this.sourceTopic, v))
				.peek((key,value) -> logger.info("Validate payload..."))
				.filter(checkIsValidEvent())
				.filter(checkEventInsideTimeWindow())
				.map(this::callService)
				.map(this::convert)
				.peek((k, value) -> this.storeTimestamp.run(IpAddress.builder(value.getIp()).build(),value.getClientId(), value.getTimestampUnixInMS()));
		requestedEvents.to(this.targetTopic,
				Produced.with(Serdes.String(), GenerateSerderFactory.getJsonSerder(FindedGeolocationEvent.class)));
		
		this.topology = builder.build();
	}
	
	private Predicate<? super String, ? super RequestedGeolocationEvent> checkIsValidEvent(){
		return (key, value) -> {
			boolean valid = ValidationUtils.isIpv4Valid(value.getIp()) &&  ValidationUtils.isValidUUID(value.getClientId());
			logger.info("{}", valid  ? "Valid Payload" : "Inválid Payload");
			return valid; 
		};
	}
	
	private Predicate<? super String, ? super RequestedGeolocationEvent> checkEventInsideTimeWindow() {
		return (key, value) -> this.checkCanConsume.run(IpAddress.builder(value.getIp()).build(), value.getClientId());
	}

	private KeyValue<String, FindedGeolocationEvent> convert(String key, ResponseApiService response) {
		Geolocation geolocation = response.getGeolocation();
		long timestampUnixInMs = response.getTimestamp();
		FindedGeolocationEvent output = new FindedGeolocationEvent(response.getClientId(), response.getIp(),
				geolocation.getLocation().getLatitude(), geolocation.getLocation().getLongitude(),
				geolocation.getCountry(), geolocation.getRegion(), geolocation.getCity(), timestampUnixInMs);
		return new KeyValue<String, FindedGeolocationEvent>(key, output);
	}

	private KeyValue<String, ResponseApiService> callService(String key, RequestedGeolocationEvent event) {
		IpAddress ipAddress = IpAddress.builder(event.getIp()).build();
		Geolocation geolocation = this.findGeolocation.run(ipAddress, event.getClientId());

		long requestedTimestamp = OffsetDateTime.now().toInstant().toEpochMilli();
		ResponseApiService response = new ResponseApiService(event.getClientId(), event.getIp(), requestedTimestamp,
				geolocation);

		return new KeyValue<String, ResponseApiService>(key, response);
	}

	public static class ResponseApiService {
		private String clientId;
		private String ip;
		private long timestamp;
		private Geolocation geolocation;

		public ResponseApiService(String clientId, String ip, long timestamp, Geolocation geolocation) {
			super();
			this.clientId = clientId;
			this.ip = ip;
			this.timestamp = timestamp;
			this.geolocation = geolocation;
		}

		public String getIp() {
			return ip;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public Geolocation getGeolocation() {
			return geolocation;
		}

		public String getClientId() {
			return clientId;
		}

	}

}
