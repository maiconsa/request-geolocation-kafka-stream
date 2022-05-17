package com.croct.challenger.geolocation.adapters.streaming.kafka.events;

public class RequestedGeolocationEvent {
	private String clientId;
	private String ip;
	
	private long timestampUnixInMs;
	
	public RequestedGeolocationEvent() {
		super();
	}
	
	public RequestedGeolocationEvent(String clientId, String ip, long timestampUnixInMs) {
		super();
		this.clientId = clientId;
		this.ip = ip;
		this.timestampUnixInMs = timestampUnixInMs;
	}

	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getTimestampUnixInMs() {
		return timestampUnixInMs;
	}
	

	
	public String getKey() {
		return clientId+"-"+ip;
	}
}
