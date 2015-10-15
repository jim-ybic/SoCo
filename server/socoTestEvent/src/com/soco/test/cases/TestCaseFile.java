package com.soco.test.cases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.soco.log.Log;

public class TestCaseFile {
	
	private List<TestCase> _list_test_case = new ArrayList<TestCase>();
	private String fileName;
	
	private TestCaseVariable _testcase_variable = new TestCaseVariable();
	
	public boolean parseTestCase(String filePathName) throws JSONException{
		boolean ret = false;
		Log.infor("In TestCaseFile. parse file: " + filePathName);;
		File file = new File(filePathName);
		if(file.exists()){
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			    String line;
			    TestCase tc = null;
			    while ((line = br.readLine()) != null) {
			       // process the line.
			    	line = line.trim();
			    	if(!line.isEmpty()){
			    		Log.debug("line: " + line);
			    		if(line.substring(0, 1).equals("#")){
			    			//Comments
			    			Log.debug("Comments: " + line);
			    		}else{
			    			int i = line.indexOf("=");
			    			if(i > -1){
			    				String tagName = line.substring(0, i).trim();
			    				String value = line.substring(i+1, line.length()).trim();
			    				
			    				switch(tagName){
			    				case "TestCaseName":
			    					Log.debug(tagName + " = " + value);
			    					if(tc != null) {
			    						this._list_test_case.add(tc);
			    					}
			    					tc = new TestCase();
			    					tc.setName(value);
			    					break;
			    				case "Request-URL":
			    					Log.debug(tagName + " = " + value);
			    					tc.setReqUrl(value);
			    					break;
			    				case "Request-Method":
			    					Log.debug(tagName + " = " + value);
			    					tc.setReqMethod(value);
			    					break;
			    				case "Request-Parameters":
			    					Log.debug(tagName + " = " + value);
			    					tc.setReqParameter(value);
			    					break;
			    				case "Request-JSON-Content":
			    					Log.debug(tagName + " = " + value);
			    					JSONObject jsonReq = new JSONObject(value);
			    					tc.setReqJson(jsonReq);
			    					break;
			    				case "Response-Expected-JSON":
			    					Log.debug(tagName + " = " + value);
			    					JSONObject jsonRespS = new JSONObject(value);
			    					tc.setRespExpectedJson(jsonRespS);
			    					this.getTestcaseVariable().parseResponseVariables(jsonRespS);
			    					break;
			    				case "Response-Expected-Status":
			    					Log.debug(tagName + " = " + value);
			    					tc.setRespExpectedStatus(Integer.valueOf(value));
			    					break;
			    				default:
			    					Log.debug("No tag matched.");
			    					break;	
			    				}
			    			}else{
			    				Log.warn("Invalid line string which not include '=' :" + line);
			    			}
			    		}
			    	}
			    }
			    //
			    if(tc != null){
			    	this._list_test_case.add(tc);
			    }
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ret = true;
		}else{
			Log.error("File " + filePathName + " is not existent.");
		}
		
		return ret;
	}

	public List<TestCase> getListTestCase() {
		return _list_test_case;
	}

	public void setListTestCase(List<TestCase> _list_test_case) {
		this._list_test_case = _list_test_case;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public TestCaseVariable getTestcaseVariable() {
		return _testcase_variable;
	}

	public void setTestcaseVariable(TestCaseVariable caseVariable) {
		this._testcase_variable = caseVariable;
	}
	
	

}
