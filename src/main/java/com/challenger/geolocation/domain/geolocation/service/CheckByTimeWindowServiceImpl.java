package com.challenger.geolocation.domain.geolocation.service;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenger.geolocation.domain.geolocation.entity.IpAddress;
import com.challenger.geolocation.domain.geolocation.ports.CheckCanConsumeEventService;
import com.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;

public class CheckByTimeWindowServiceImpl implements CheckCanConsumeEventService {
	private static final String CHECKING_REQUEST_MESSAGE = "Cheking localization per client and IP within a time window of {} {}";
	private static final String FIRST_REQUEST_GEOLOCATION_MESSAGE = "First request geolocation to the user {} using IP {}";	
	private static final String ACTION_REASON_MESSAGE = "Action:{} event  - Reason:{} time window";
	
	private final ConsumedEventTimestampByUserRepository repository;

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Duration timeWindow;
	
	public CheckByTimeWindowServiceImpl(final ConsumedEventTimestampByUserRepository repository , Duration timeWindow) {
		this.repository = repository;
		this.timeWindow  = timeWindow;
	}

	@Override
	public boolean run(IpAddress ipaddres, String clientId) {
		logger.info(CHECKING_REQUEST_MESSAGE,this.timeWindow.get(ChronoUnit.SECONDS)," seconds");
		
		Optional<Long> timestamp =  this.repository.get(ipaddres.getIp(), clientId);
		
		if(timestamp.isEmpty()) {
			logger.info(FIRST_REQUEST_GEOLOCATION_MESSAGE, clientId,ipaddres);
			return true;
		}else {
			
			OffsetDateTime last =    OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.get()), ZoneId.systemDefault());
			OffsetDateTime now = OffsetDateTime.now();
			boolean allowed =  Duration.between(last.toInstant(), now.toInstant()).get(ChronoUnit.SECONDS) > this.timeWindow.get(ChronoUnit.SECONDS)  ;
			logger.info(ACTION_REASON_MESSAGE, !allowed ? "Discarting": "Consuming" , !allowed ?  " Outside " :  "Inside " );
			return allowed;
		}
	}

}
