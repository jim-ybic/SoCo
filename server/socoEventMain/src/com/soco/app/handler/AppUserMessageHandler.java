package com.soco.app.handler;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.soco.log.Log;

public class AppUserMessageHandler implements AppMessageHandler {
	
	public static final String CMD_REGISTER = "register";
	public static final String CMD_LOGIN = "login";
	public static final String CMD_LOGOUT = "logout";
	public static final String CMD_USERS = "users";
	
	private static final String FIELD_NAME = "name";
	private static final String FIELD_EMAIL = "email";
	private static final String FIELD_PHONE = "phone";
	private static final String FIELD_PASSWORD = "password";
	private static final String FIELD_LOCATION = "location";
	
	private static ArrayList<String> _cmdList = new ArrayList<String>();
	
	private HttpResponseStatus _http_status;
	private String _http_response_content;

	@Override
	public boolean messageHandler(String version, String className, String httpMethod, String paramters, String content) {
		// TODO Auto-generated method stub
		boolean ret = false;
		System.out.println("In AppUserMessageHandler. The command " + httpMethod + " and message is: ");
		// format to json object
		try {
			this.set_http_response_content(content);
			this.set_http_status(OK);
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
		if(AppUserMessageHandler._cmdList != null){
			_cmdList.add(CMD_REGISTER);
			_cmdList.add(CMD_LOGIN);
			_cmdList.add(CMD_LOGOUT);
			_cmdList.add(CMD_USERS);
		} else {
			Log.debug("In AppUserMessageHandler. The command list is null.");
		}
		
		return AppUserMessageHandler._cmdList;
	}
	
	/**
	 * Register by email and password
	 * @return
	 */
	public boolean post_register_v1 (JSONObject json, String param){
		boolean ret = false;
		Log.debug("In register.");
		
		long uid = 0;
		
		if(json.has(FIELD_NAME)){
			if(json.has(FIELD_EMAIL)){
				if(json.has(FIELD_PASSWORD)){
					if(json.has(FIELD_LOCATION)){
						try {
							String name = json.getString(FIELD_NAME);
							String email = json.getString(FIELD_EMAIL);
							String password = json.getString(FIELD_PASSWORD);
							String location = json.getString(FIELD_LOCATION);
							String phone = "";
							if(json.has(FIELD_PHONE)){
								phone = json.getString(FIELD_PHONE);
							} else {
								Log.debug("There is no phone.");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Log.error("There is no location field in request.");
						this.set_http_status(HttpResponseStatus.BAD_REQUEST);
					}
				} else {
					Log.error("There is no password field in request.");
					this.set_http_status(HttpResponseStatus.BAD_REQUEST);
				}
			} else {
				Log.error("There is no email field in request.");
				this.set_http_status(HttpResponseStatus.BAD_REQUEST);
			}
		} else {
			Log.error("There is no name field in request.");
			this.set_http_status(HttpResponseStatus.BAD_REQUEST);
		}
		
		return ret;
	}
	
	public boolean post_login_v1 (JSONObject json, String param){
		boolean ret = false;
		Log.debug("In login.");
		return ret;
	}
	
	public boolean post_logout_v1 (JSONObject json, String param){
		boolean ret = false;
		Log.debug("In logout.");
		return ret;
	}
	
	public void post_users_v1 (JSONObject json, String param) {
		
	}
	
	private boolean sendEmail() {
		boolean ret = false;
		return ret;
	}

	@Override
	public FullHttpResponse getResponse() {
		// TODO Auto-generated method stub
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, this.get_http_status(), Unpooled.wrappedBuffer(this.get_http_response_content().getBytes()));
        response.headers().set(CONTENT_TYPE, "text/json");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(CONNECTION, Values.KEEP_ALIVE);
		return response;
	}

	public HttpResponseStatus get_http_status() {
		return _http_status;
	}

	public void set_http_status(HttpResponseStatus _http_status) {
		this._http_status = _http_status;
	}

	public String get_http_response_content() {
		return _http_response_content;
	}

	public void set_http_response_content(String _http_response_content) {
		this._http_response_content = _http_response_content;
	}

}
