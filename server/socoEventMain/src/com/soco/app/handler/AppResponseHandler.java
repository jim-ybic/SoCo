package com.soco.app.handler;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import io.netty.handler.codec.http.HttpResponseStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class AppResponseHandler {

	/*
	 * Deal with kinds of response
	 * */
	
	
	public static String getRegisterSuccessResponse(int httpStatus, long uid, String token){
		
		JSONObject jsonResp = new JSONObject();
		try {
			jsonResp.put("status", httpStatus);
			jsonResp.put("user_id", uid);
			jsonResp.put("token", token);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResp.toString();
	}
	
	public static String getRegisterFailureResponse(int httpStatus, int error_code, String property, String message){
		
		JSONObject jsonResp = new JSONObject();
		try {
			jsonResp.put("status", httpStatus);
			jsonResp.put("error_code", error_code);
			jsonResp.put("property", property);
			jsonResp.put("message", message);
			jsonResp.put("more_info", System.getProperty("API_HELP_URL") + "/" + error_code);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResp.toString();
	}
	
	public static String getSocialLoginSuccessResponse(int httpStatus, long uid, String token){
		
		JSONObject jsonResp = new JSONObject();
		try {
			jsonResp.put("status", httpStatus);
			jsonResp.put("user_id", uid);
			if(!token.equals("")) jsonResp.put("token", token);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResp.toString();
	}
	
	public static String getSocialLoginFailureResponse(int httpStatus, int error_code, String property, String message){
		
		JSONObject jsonResp = new JSONObject();
		try {
			jsonResp.put("status", httpStatus);
			jsonResp.put("error_code", error_code);
			jsonResp.put("property", property);
			jsonResp.put("message", message);
			jsonResp.put("more_info", System.getProperty("API_HELP_URL") + "/" + error_code);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResp.toString();
	}
	
    public static String getLoginSuccessResponse(int httpStatus, long uid, String token, String verified){
		
		JSONObject jsonResp = new JSONObject();
		try {
			jsonResp.put("status", httpStatus);
			jsonResp.put("user_id", uid);
			jsonResp.put("token", token);
			jsonResp.put("verified", verified);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResp.toString();
	}
}
