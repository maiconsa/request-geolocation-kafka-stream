package com.croct.challenger.geolocation.domain.geolocation.entity;

import com.croct.challenger.geolocation.domain.FieldValidationException;
import com.croct.challenger.geolocation.domain.utils.ValidationUtils;

public class IpAddress {

	private String ip;
	
	private IpAddress(String ip) {
		this.ip = ip;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	
	public static IpAddressBuilder builder(String ip) {
		return new IpAddressBuilder(ip);
	}
	
	public static class IpAddressBuilder{
			private String value;
			public IpAddressBuilder(String ipAddress) {
				this.value = ipAddress;
			}
			
			public IpAddress build() {
				if(!ValidationUtils.isIpv4Valid(value)) {
					throw new FieldValidationException("ip", this.value);
				}
				return new IpAddress(this.value);
			}
			
	}
}
