package com.soco.test.cases;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.soco.log.Log;
import com.soco.test.main.MainTest;

public class TestCaseReport {

	private static String REPORT_PATH = "./TestReport";

	private String TestCaseFileName;
	private List<TestResult> listTestResult = new ArrayList<TestResult>();
	
	public TestCaseReport(){
		init();
	}
	
	public void init(){
		File folder = new File(REPORT_PATH);
		if(!folder.exists()){
			folder.mkdir();
		}
	}
	
	
	public void genTestReport(TestCaseFile tcf){
		if(tcf == null){
			Log.error("The test case file is null.");
			return;
		}
		this.setTestCaseFileName(tcf.getFileName());
		
		for(TestCase tcase : tcf.getListTestCase()){
			//// the response status
			TestResult tResult = new TestResult();
			if(tcase.getRespExpectedStatus() == tcase.getRespStatus()){
				// success
				//// the response content
				try {
					tResult = compareJson(tcase.getRespExpectedJson(), tcase.getRespJson());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					tResult.setTestResult(TestResult.TEST_FAIL);
					tResult.setTestFailReason("Exception: " + e.getMessage());
				}
			}else{
				// failse
				tResult.setTestResult(TestResult.TEST_FAIL);
				tResult.setTestFailReason("The expected status " + tcase.getRespExpectedStatus() + " is not equal to " + tcase.getRespStatus() + " received from server.");
			}
			tResult.setTestCaseName(tcase.getName());
			this.listTestResult.add(tResult);
		}
		//
		this.writeFileAsText();
	}
	
	private TestResult compareJson(JSONObject dest, JSONObject src) throws JSONException{
		TestResult tResult = new TestResult();
		Iterator ite = dest.keys();
		boolean ret = true;
		if(dest != null){
			if(src != null){
				while(ite.hasNext()){
					String key = (String) ite.next();
					
					if(src.has(key) && (dest.get(key).equals("?") || src.get(key).equals(dest.get(key)))){
						ret = true;
					} else {
						tResult.setTestResult(TestResult.TEST_FAIL);
						tResult.setTestFailReason("The expected value: " + dest.get(key) + " of " + key + " is not equal to " + src.get(key) + " received from server.");
						ret = false;
						break;
					}
				}
			}else{
				tResult.setTestResult(TestResult.TEST_FAIL);
				tResult.setTestFailReason("There is no response received.");
			}
		}
		
		if(ret){
			tResult.setTestResult(TestResult.TEST_PASS);
		}
		
		return tResult;
	}
	
	private void writeFileAsText(){
		if(this.getListTestResult().size() > 0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(new Date()); 
			String date1 = sdf1.format(new Date());
			String fileName = "TestReport-" + this.getTestCaseFileName().substring(0, (this.getTestCaseFileName().length() - 4)) + "-" + date + ".txt";
			File reportFile = new File(this.REPORT_PATH + "/" + fileName);
			try {
				if(!reportFile.exists()){
					reportFile.createNewFile();
				}
				if(reportFile.exists()){
					FileWriter fileWriter = new FileWriter(reportFile);
					fileWriter.write("=======================================\n");
					fileWriter.write(" Auto testing tool v " + MainTest.VERSION + "\n");
					fileWriter.write(" Testing at " + date1 + "\n");
					fileWriter.write(" Test file: " + this.getTestCaseFileName() + "\n");
					fileWriter.write("----------------------------------\n");
					for(TestResult tResult : this.getListTestResult()){
						fileWriter.write("Test case: " + tResult.getTestCaseName() + "\n");
						fileWriter.write("Test result: " + tResult.getTestResult() + "\n");
						if(tResult.getTestResult().equals(TestResult.TEST_FAIL)){
							fileWriter.write("Fail message: " + tResult.getTestFailReason() + "\n");
						}
						fileWriter.write("\n");
					}
					fileWriter.write("----------------------------------\n");
					fileWriter.flush();
					fileWriter.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Log.warn("The string buffer is nothing.");
		}
	}
	
	public String getTestCaseFileName() {
		return TestCaseFileName;
	}

	public void setTestCaseFileName(String testCaseFileName) {
		TestCaseFileName = testCaseFileName;
	}

	public List<TestResult> getListTestResult() {
		return listTestResult;
	}

	public void setListTestResult(List<TestResult> listTestResult) {
		this.listTestResult = listTestResult;
	}

	private class TestResult {
		
		public static final String TEST_PASS = "PASS";
		public static final String TEST_FAIL = "FAIL";
		
		private String testCaseName;
		private String testResult;
		private String testFailReason;
		
		public TestResult(){
			this.setTestCaseName("");
			this.setTestFailReason("");
			this.setTestResult("");
		}
		
		public String getTestCaseName() {
			return testCaseName;
		}
		public void setTestCaseName(String testCaseName) {
			this.testCaseName = testCaseName;
		}
		public String getTestResult() {
			return testResult;
		}
		public void setTestResult(String testResult) {
			this.testResult = testResult;
		}
		public String getTestFailReason() {
			return testFailReason;
		}
		public void setTestFailReason(String testFailReason) {
			this.testFailReason = testFailReason;
		}
	}
	
}
