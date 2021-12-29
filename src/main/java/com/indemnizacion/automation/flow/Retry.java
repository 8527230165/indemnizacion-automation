package com.indemnizacion.automation.flow;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer{

	public static Logger log=Logger.getLogger(Retry.class);
	@Override
	public boolean retry(ITestResult result) {
	
		log.info("Exception Occurred. Test Retring...");
		return true;
	}
	
	

}
