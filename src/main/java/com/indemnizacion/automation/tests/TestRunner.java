package com.indemnizacion.automation.tests;

import org.testng.TestNG;

public class TestRunner {
	static TestNG testng;
	
	public static void main(String[] args) {
		
		 testng = new TestNG();
		testng.setTestClasses(new Class[] {IndemnizacionTest.class});
		//testng.addListener(extentlistener);
		testng.run();
		}
}
