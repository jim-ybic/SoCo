package com.soco.test.cases;

import org.json.JSONObject;

public class TestCase {

	private String name;
	private String req_url;
	private String req_method;
	private String req_parameter;
	private JSONObject req_json;
	private int resp_status;
	private JSONObject resp_json;
	private JSONObject resp_expected_success;
	private JSONObject resp_expected_failure;
	private int resp_expected_status;
	
	
	public TestCase(){
		this.setReqUrl("");
		this.setName("");
		this.setReqMethod("");
		this.setReqParameter("");
		this.setReqJson(null);
		this.setRespExpectedSuccess(null);
		this.setRespExpectedFailure(null);
		this.setRespStatus(0);
		this.setRespExpectedFailure(null);
		this.setRespExpectedStatus(0);
		this.setRespExpectedSuccess(null);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReqMethod() {
		return req_method;
	}
	public void setReqMethod(String req_method) {
		this.req_method = req_method;
	}
	public String getReqParameter() {
		return req_parameter;
	}
	public void setReqParameter(String req_parameter) {
		this.req_parameter = req_parameter;
	}
	public JSONObject getReqJson() {
		return req_json;
	}
	public void setReqJson(JSONObject req_json) {
		this.req_json = req_json;
	}
	public int getRespStatus() {
		return resp_status;
	}
	public void setRespStatus(int resp_status) {
		this.resp_status = resp_status;
	}
	public JSONObject getRespExpectedSuccess() {
		return resp_expected_success;
	}
	public void setRespExpectedSuccess(JSONObject resp_json) {
		this.resp_expected_success = resp_json;
	}
	public JSONObject getRespExpectedFailure() {
		return resp_expected_failure;
	}
	public void setRespExpectedFailure(JSONObject resp_json) {
		this.resp_expected_failure = resp_json;
	}
	public String getReqUrl() {
		return req_url;
	}

	public void setReqUrl(String req_url) {
		this.req_url = req_url;
	}
	
	public JSONObject getRespJson() {
		return resp_json;
	}

	public void setRespJson(JSONObject resp_json) {
		this.resp_json = resp_json;
	}
	
	public String toString(){
		return "TestCase name: "+ this.getName() + "\n" +
				"Request URL: " + this.getReqUrl() + "\n" +
				"Request method: " + this.getReqMethod() + "\n" +
				"Request parameter: " + this.getReqParameter() + "\n" +
				"Request content: " + (this.getReqJson() != null ? this.getReqJson().toString() : "{}") + "\n" +
				"Expected success: " + (this.getRespExpectedSuccess() != null ? this.getRespExpectedSuccess().toString() : "{}") + "\n" +
				"Expected failure: " + (this.getRespExpectedFailure() != null ? this.getRespExpectedFailure().toString() : "{}") + "\n" +
				"Expected status: " + this.getRespExpectedStatus() + "\n" + 
				"Response content: " + (this.getRespJson() != null ? this.getRespJson().toString() : "{}") + "\n" +
				"Response status: " + this.getRespStatus() + "\n";
	}

	public int getRespExpectedStatus() {
		return resp_expected_status;
	}

	public void setRespExpectedStatus(int resp_expected_status) {
		this.resp_expected_status = resp_expected_status;
	}

	

	
}
