package com.croct.challenger.geolocation.domain.geolocation.ports;

import com.croct.challenger.geolocation.domain.geolocation.entity.Geolocation;
import com.croct.challenger.geolocation.domain.geolocation.entity.IpAddress;

public interface FindGeolocationByIpAddressService {
	public Geolocation run(IpAddress ipaddres,String clientId);
}
