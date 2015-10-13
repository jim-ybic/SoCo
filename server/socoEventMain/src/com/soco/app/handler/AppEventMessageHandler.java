package com.soco.app.handler;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpHeaders.Values;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.soco.log.Log;

public class AppEventMessageHandler implements AppMessageHandler {

	private static final String[] EVENT_CMD_ARRAY = { "event" };
	private static ArrayList<String> _cmdList = new ArrayList<String>();
	
	private static final String FIELD_NAME = "name";
	
	
	private HttpResponseStatus _http_status;
	private String _http_response_content;
	
	
	@Override
	public boolean messageHandler(String version, String className, String httpMethod, String paramters, String content) {
		// TODO Auto-generated method stub
		boolean ret = false;
		Log.debug("In AppEventMessageHandler. The command " + httpMethod + " and message is: ");
		// format to json object
		try {
			this.setHttpResponseContent(content);
			this.setHttpStatus(OK);
			JSONObject jsonObj = new JSONObject(content);
			String methodName = httpMethod.toLowerCase() + "_" + className + "_" + version;
			Method method = this.getClass().getMethod(methodName, JSONObject.class, String.class);
			method.invoke(this, jsonObj, paramters);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<String> getCmdList() {
		// TODO Auto-generated method stub
		if(AppEventMessageHandler._cmdList != null){
			for(String cmd: AppEventMessageHandler.EVENT_CMD_ARRAY){
				_cmdList.add(cmd);
			}
		} else {
			Log.debug("In AppEventMessageHandler. The command list is null.");
		}
		
		return AppEventMessageHandler._cmdList;
	}

	@Override
	public FullHttpResponse getResponse() {
		// TODO Auto-generated method stub
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, this.getHttpStatus(), Unpooled.wrappedBuffer(this.getHttpResponseContent().getBytes()));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(CONNECTION, Values.KEEP_ALIVE);
		return response;
	}

	public HttpResponseStatus getHttpStatus() {
		return _http_status;
	}

	public void setHttpStatus(HttpResponseStatus _http_status) {
		this._http_status = _http_status;
	}

	public String getHttpResponseContent() {
		return _http_response_content;
	}

	public void setHttpResponseContent(String _http_response_content) {
		this._http_response_content = _http_response_content;
	}
	
	public boolean post_event_v1(JSONObject json, String param){
		Log.infor("In event post.");
		Log.debug("request: " + json.toString());
		
		this.setHttpStatus(getHttpStatus());
		this.setHttpResponseContent("{}");
		return true;
	}

}
