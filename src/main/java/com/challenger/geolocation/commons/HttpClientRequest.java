package com.challenger.geolocation.commons;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClientRequest {
	private final OkHttpClient client = new OkHttpClient();
	public String get(String url) {
		 HttpUrl.Builder urlBuilder  = HttpUrl.parse(url).newBuilder();		 
	    String buildedUrl = urlBuilder.build().toString();
	    Request request = new Request.Builder()
	      .url(buildedUrl)
	      .build();
	    Call call = client.newCall(request);
	    try {
			Response response = call.execute();
			return response.body().string();
		} catch (IOException e) {
			throw new  RuntimeException("Error on complete request to IpStack API");
		}
	}
}
