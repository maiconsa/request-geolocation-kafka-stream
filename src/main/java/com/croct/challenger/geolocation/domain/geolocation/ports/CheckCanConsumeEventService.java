package com.croct.challenger.geolocation.domain.geolocation.ports;

import com.croct.challenger.geolocation.domain.geolocation.entity.IpAddress;

public interface CheckCanConsumeEventService {
	boolean run(IpAddress ipaddres,String clientId);
}
