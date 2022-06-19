package com.challenger.geolocation.domain.geolocation.ports;

import com.challenger.geolocation.domain.geolocation.entity.IpAddress;

public interface CheckCanConsumeEventService {
	boolean run(IpAddress ipaddres,String clientId);
}
