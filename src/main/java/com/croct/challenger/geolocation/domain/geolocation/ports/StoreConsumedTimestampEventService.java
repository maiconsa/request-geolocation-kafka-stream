package com.croct.challenger.geolocation.domain.geolocation.ports;

import com.croct.challenger.geolocation.domain.geolocation.entity.IpAddress;

public interface StoreConsumedTimestampEventService {
	void run(IpAddress ipaddres,String clientId,Long timestampUnixInMilli);
}
