package com.soco.app.handler;

import io.netty.handler.codec.http.FullHttpResponse;

import java.util.List;

public class AppAuthenticationMessageHandler implements AppMessageHandler {

	@Override
	public boolean messageHandler(String version, String className, String method,
			String paramters, String content) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getCmdList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FullHttpResponse getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

}
