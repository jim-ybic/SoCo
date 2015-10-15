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

import com.soco.algorithm.event.EventInfor;
import com.soco.db.event.EventController;
import com.soco.event.Event;
import com.soco.log.Log;
import com.soco.security.AuthenticationToken;
import com.soco.security.authentication.UserAuthentication;

public class AppEventMessageHandler implements AppMessageHandler {

	private static final String[] EVENT_CMD_ARRAY = { "event", "suggested_events" };
	private static ArrayList<String> _cmdList = new ArrayList<String>();
	
	private static final String FIELD_USER_ID = "user_id";
	private static final String FIELD_TOKEN = "token";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_ADDRESS = "address";
	private static final String FIELD_LAT = "lat";
	private static final String FIELD_LON = "lon";
	private static final String FIELD_DESCRIPTION = "description";
	private static final String FIELD_CITY = "city";
	
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
	
	/*
	 * Event post method to create a event
	 * param input: json, this is request message how/what to create
	 * return success is true, otherwise false
	 * */
	public boolean post_event_v1(JSONObject json, String param){
		Log.infor("In event post.");
		Log.debug("request: " + json.toString());
		boolean ret = false;
		String property = "";
		String message = "";
		String more = "";
		int error_code = 20;
		try {
			if(json.has(FIELD_USER_ID)){
				if(json.has(FIELD_TOKEN)){
					if(json.has(FIELD_NAME)){
						if(json.has(FIELD_ADDRESS)){
							if(json.has(FIELD_LAT)){
								if(json.has(FIELD_LON)){
									//// authenticate user
									if(UserAuthentication.authentication(json.getLong(FIELD_USER_ID),json.getString(FIELD_TOKEN))){
										Event event = new Event();
										EventController eController = new EventController();
										
										String description = "";
										if(json.has(FIELD_DESCRIPTION)){
											description = json.getString(FIELD_DESCRIPTION);
										}
										String city = "";
										if(json.has(FIELD_CITY)){
											city = json.getString(FIELD_CITY);
										}
										//TODO : area code
										long eid = EventInfor.getEID(1, 1);
										event.setId(eid);
										event.setName(json.getString(FIELD_NAME));
										event.setAddress(FIELD_ADDRESS);
										event.setLat(json.getDouble(FIELD_LAT));
										event.setLon(json.getDouble(FIELD_LON));
										event.setDescription(description);
										event.setCity(city);
										//
										if(eController.createEvent(event) > 0){
											// set response
											String resp = AppResponseHandler.getEventPostSuccessResponse(200);
											this.setHttpStatus(OK);
											this.setHttpResponseContent(resp);
											ret = true;
										} else {
											Log.error("Can't create event in database.");
											property = "";
											message = "Can't create event in database";
										}
									} else {
										Log.error("Authentication is failed, please to check the user id and token.");
										property = "";
										message = "Authentication is failed, please to check the user id and token.";
									}
								} else {
									Log.error("There is no longitude in request.");
									property = "lon";
									message = "There is no longitude in request.";
								}
							} else {
								Log.error("There is no latitude in request.");
								property = "lat";
								message = "There is no latitude in request.";
							}
						} else {
							Log.error("There is no address in request.");
							property = "address";
							message = "There is no address in request.";
						}
					} else {
						Log.error("There is no name in request.");
						property = "name";
						message = "There is no name in request.";
					}
				} else {
					Log.error("There is no token in request.");
					property = "token";
					message = "There is no token in request.";
				}
			} else {
				Log.error("There is no user id in request.");
				property = "user_id";
				message = "There is no user id in request.";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.error("There is an exception.");
			property = "exception";
			message = "There is an exception.";
			more = e.getMessage();
		}
		if(!ret){
			String resp = AppResponseHandler.getEventPostFailureResponse(400, error_code, property, message, more);
			this.setHttpStatus(HttpResponseStatus.BAD_REQUEST);
			this.setHttpResponseContent(resp);
		}

		return ret;
	}
	
	/*
	 * Suggest event get method to get the suggestion event for user.
	 * param input: json, this is request message how to suggest
	 * return success is true, otherwise false
	 * */
	public boolean get_suggested_events_v1(JSONObject json, String param){
		Log.infor("In suggested events get methode.");
		Log.debug("request: " + json.toString());
		boolean ret = false;
		String property = "";
		String message = "";
		String more = "";
		int error_code = 21;
		try {
			if(json.has(FIELD_USER_ID)){
				if(json.has(FIELD_TOKEN)){
					//
					if(UserAuthentication.authentication(json.getLong(FIELD_USER_ID),json.getString(FIELD_TOKEN))){
						if(json.has(FIELD_ADDRESS)){
							
						}
						
						if(json.has(FIELD_LAT)){
							
						}
									
						if(json.has(FIELD_LON)){
							
						}
						
						// set response
						String resp = AppResponseHandler.getEventPostSuccessResponse(200);
						this.setHttpStatus(OK);
						this.setHttpResponseContent(resp);
						ret = true;
					} else {
						
					}
				} else {
					Log.error("There is no token in request.");
					property = "token";
					message = "There is no token in request.";
				}
			} else {
				Log.error("There is no user id in request.");
				property = "user_id";
				message = "There is no user id in request.";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.error("There is an exception.");
			property = "exception";
			message = "There is an exception.";
			more = e.getMessage();
		}
		if(!ret){
			String resp = AppResponseHandler.getEventPostFailureResponse(400, error_code, property, message, more);
			this.setHttpStatus(HttpResponseStatus.BAD_REQUEST);
			this.setHttpResponseContent(resp);
		}
		
		return ret;
	}

}
