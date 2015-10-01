package com.soco.app.handler;

import io.netty.handler.codec.http.FullHttpResponse;

import java.util.List;

public interface AppMessageHandler {

	public boolean messageHandler(String version, String className, String method, String paramters, String content);
	
	public List<String> getCmdList();
	
	public FullHttpResponse getResponse();
}
