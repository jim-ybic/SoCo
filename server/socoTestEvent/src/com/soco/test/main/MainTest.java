package com.soco.test.main;

import javax.net.ssl.SSLException;

import com.soco.log.Log;
import com.soco.nettyclient.ClientMain;
import com.soco.test.cases.TestCase;
import com.soco.test.cases.TestCaseFile;
import com.soco.test.cases.TestCaseFileManager;
import com.soco.test.cases.TestCaseReport;

public class MainTest {
	
	public static final String VERSION = "0.1";

	public static void main(String[] args){
		Log.infor("====== Auto Test Application v"+ VERSION +" =======");
		
		TestCaseFileManager tcfm = new TestCaseFileManager();
		runTestCases(tcfm);
		reportTestCase(tcfm);
		//////
		//showTestCases(tcfm);
	}
	
	public static void showTestCases(TestCaseFileManager tcfm){
		for(TestCaseFile tcf : tcfm.getListTestcaseFile()){
			Log.debug("Test case file name: " + tcf.getFileName());
			for(TestCase tc : tcf.getListTestCase()){
				Log.debug(tc.toString());
			}
		}
	}
	
	public static void runTestCases(TestCaseFileManager tcfm){
		tcfm.searchTestCaseFiles();
		ClientMain cMain = new ClientMain();
		for(TestCaseFile tcf : tcfm.getListTestcaseFile()){
			try {
				cMain.start(tcf);
			} catch (SSLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void reportTestCase(TestCaseFileManager tcfm){
		
		for(TestCaseFile tcf : tcfm.getListTestcaseFile()){
			Log.debug("======================================");
			TestCaseReport tCaseReport = new TestCaseReport();
			tCaseReport.genTestReport(tcf);
			/*
			Log.debug("Test case file: " + tcf.getFileName());
			for(TestCase tcase : tcf.getListTestCase()){
				Log.debug(tcase.toString());
			}
			*/
			Log.debug("------------------------------");
			Log.debug(tcf.getTestcaseVariable().toString());
			
			Log.debug("======================================");
		}
		
	}
	
}
