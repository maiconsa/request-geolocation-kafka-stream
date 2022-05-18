package com.croct.challenger.geolocation.domain.geolocation.ports.repository;

import java.util.Optional;

public interface ConsumedEventTimestampByUserRepository {
	Optional<Long> get(String ipAddress,String clientId);
	void store(String ipAddress,String clientId, long timestampUnixInMs);
	
	
	
}
