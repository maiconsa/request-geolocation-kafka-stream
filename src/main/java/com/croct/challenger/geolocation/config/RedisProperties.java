package com.croct.challenger.geolocation.config;

import java.util.Properties;

public final class RedisProperties {
	private static final String PREFIX = "redis";

	public static final String HOST = String.join(".", PREFIX, "host");
	public static final String PORT = String.join(".", PREFIX, "port");
	public static final String PASS = String.join(".", PREFIX, "pass");

	private String host;
	private int port;
	private String pass;

	private RedisProperties(Properties properties) {
		this.host = properties.getProperty(HOST);
		this.port = Integer.valueOf(properties.getProperty(PORT));
		this.pass = properties.getProperty(PASS);
	}

	public static RedisProperties build(Properties properties) {
		return new RedisProperties(properties);
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getPass() {
		return pass;
	}

}
