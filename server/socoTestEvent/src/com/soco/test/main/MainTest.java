package com.soco.test.main;

import com.soco.log.Log;
import com.soco.test.cases.TestCase;
import com.soco.test.cases.TestCaseFile;
import com.soco.test.cases.TestCaseFileManager;

public class MainTest {
	
	public static final String VERSION = "0.1";

	public static void main(String[] args){
		Log.infor("====== Auto Test Application v"+ VERSION +" =======");
		
		TestCaseFileManager tcfm = new TestCaseFileManager();
		tcfm.searchTestCaseFiles();
		//////
		showTestCases(tcfm);
	}
	
	public static void showTestCases(TestCaseFileManager tcfm){
		for(TestCaseFile tcf : tcfm.getListTestcaseFile()){
			Log.debug("Test case file name: " + tcf.getFileName());
			for(TestCase tc : tcf.getListTestCase()){
				Log.debug(tc.toString());
			}
		}
	}
	
}
