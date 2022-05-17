package com.croct.challenger.geolocation.domain.geolocation.service;

import com.croct.challenger.geolocation.domain.geolocation.entity.IpAddress;
import com.croct.challenger.geolocation.domain.geolocation.ports.StoreConsumedTimestampEventService;
import com.croct.challenger.geolocation.domain.geolocation.ports.repository.ConsumedEventTimestampByUserRepository;
import com.croct.challenger.geolocation.domain.utils.AssertionsUtils;

public class StoreTimestampEventServiceImpl
		implements StoreConsumedTimestampEventService {
	private final ConsumedEventTimestampByUserRepository repository;

	public StoreTimestampEventServiceImpl(final ConsumedEventTimestampByUserRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(IpAddress ipaddres, String clientId, Long timestampUnixInMilli) {
		AssertionsUtils.assertNonNull(ipaddres, "ipaddress");
		AssertionsUtils.assertNonNull(clientId, "clientId");
		AssertionsUtils.assertNonNull(timestampUnixInMilli, "timestampUnixInMilli");
		this.repository.store(ipaddres.getIp(), clientId, timestampUnixInMilli);
	}

}
