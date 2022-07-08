package com.challenger.geolocation.commons;

import static java.lang.System.getenv;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenger.geolocation.ContextEnum;
import com.challenger.geolocation.config.ApplicationProperties;
import com.challenger.geolocation.config.IpStackGeolocationProperties;
import com.challenger.geolocation.config.RedisProperties;

public final  class Env {
	private final static Logger logger = LoggerFactory.getLogger(Env.class);

	private static final String REQUIRED_ENVS_REAL[] = new String[]{
			"IP_STACK_ACCESS_KEY",
			"KAFKA_BOOTSTRAP_SERVER",
			"TIME_WINDOW_IN_MINUTES",
			"SOURCE_TOPIC",
			"TARGET_TOPIC",
			"REDIS_PORT",
			"REDIS_HOST",
			"REDIS_PASS",
			"STASH_HOST"
	};
	
	private  static final String REQUIRED_ENVS_IN_MEMORY[] = new String[]{
			"KAFKA_BOOTSTRAP_SERVER",
			"TIME_WINDOW_IN_MINUTES",
			"SOURCE_TOPIC",
			"TARGET_TOPIC",
			"STASH_HOST"
			};
	
	private final static Map<ContextEnum, String[]> REQUIRED_ENV_BY_CONTEXT = Map.of(
			ContextEnum.REAL,REQUIRED_ENVS_REAL,
			ContextEnum.IN_MEMORY, REQUIRED_ENVS_IN_MEMORY) ;
	
	public static void check() throws Exception {
		check("CONTEXT_EXECUTION");
		ContextEnum context = ContextEnum.valueOf(System.getenv("CONTEXT_EXECUTION"));
		String[] requiredEnv = REQUIRED_ENV_BY_CONTEXT.get(context);
		for (String key : requiredEnv) {
			check(key);
		}
	}

	private static void check(String keyEnv) throws Exception {
		if(!System.getenv().containsKey(keyEnv)) {
			logger.error("Please set the "+keyEnv+" on environment");
			throw new  Exception("Please set the "+keyEnv+" on environment");
		}
	}
	
	
	public static Properties extract() {
		Properties config = new Properties();
		config.put(IpStackGeolocationProperties.ACCESS_KEY,getenv("IP_STACK_ACCESS_KEY"));
		
		config.put(ApplicationProperties.APP_ID, "geolocation-stream-app");
		config.put(ApplicationProperties.BOOTSTRAP_SERVER, getenv("KAFKA_BOOTSTRAP_SERVER"));
		config.put(ApplicationProperties.SOURCE_TOPIC, getenv("SOURCE_TOPIC"));
		config.put(ApplicationProperties.TARGET_TOPIC, getenv("TARGET_TOPIC"));
		config.put(ApplicationProperties.TIME_WINDOW_IN_MINUTES, getenv("TIME_WINDOW_IN_MINUTES"));
		
		config.put(RedisProperties.HOST, getenv("REDIS_HOST"));
		config.put(RedisProperties.PORT, getenv("REDIS_PORT"));
		config.put(RedisProperties.PASS, getenv("REDIS_PASS"));
	
		return config;
	}
	
	
}
