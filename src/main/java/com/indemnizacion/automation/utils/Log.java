package com.indemnizacion.automation.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {
	
	public static Logger log;
	 public static Properties properties = new Properties();
	 
	public Log(String className) {
		log = Logger.getLogger(className);
	}
	
	public void info(String message) {
		try {
			properties.load(new FileInputStream("./src/test/resources/log4j.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	PropertyConfigurator.configure(properties);
		log.info(message);
	}
	public  void error(Object object) {
		try {
			properties.load(new FileInputStream("./src/test/resources/log4j.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	PropertyConfigurator.configure(properties);
		log.error(object);
	}

}
