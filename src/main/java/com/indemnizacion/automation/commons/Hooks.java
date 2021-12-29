package com.indemnizacion.automation.commons;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.indemnizacion.automation.commons.helpers.DriverFactory;
import com.indemnizacion.automation.tests.IndemnizacionStepDef;
import com.indemnizacion.automation.utils.DataUtil;
import com.indemnizacion.automation.utils.ExcelReader;
import com.indemnizacion.automation.utils.PropertyManager;

public class Hooks {

	public static Logger log=Logger.getLogger(Hooks.class);
	ThreadLocal<IndemnizacionStepDef> steps= ThreadLocal.withInitial(IndemnizacionStepDef::new);

	@BeforeSuite
	public void starttimeofexecution() {
		// creates file appender
        FileAppender fileAppender = new FileAppender();
        fileAppender.setFile("app_log.txt");
        fileAppender.setLayout(new PatternLayout("%p %d %c  - %m%n"));
        fileAppender.activateOptions();
        
     // configures the root logger
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.addAppender(fileAppender);
		
		BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%p %d %c  - %m%n")));
		LocalDateTime d = LocalDateTime.now();
		DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String date=ft.format(d);
		log.info("Execution Started at: "+date);
		ExcelReader loginexcel=DataUtil.loginexcel;
		loginexcel.setCellData(DataUtil.loginSheetName, "Start_Time", 2, date);
		DataUtil.excelLoadingLog();
		
	}

	@BeforeMethod
	public void before() {
		
		DriverFactory.setWebDriver();
		try {
			String env=PropertyManager.getConfigValueByKey("Environment");
			if(env.equalsIgnoreCase("Test")) {
				DriverFactory.getDriverFacade().getWebDriver().get(PropertyManager.getConfigValueByKey("url"));
				log.info("Browser is Launched!!!");
				steps.get().clickLogin(DataUtil.user_id);
				log.info("Login clicked");
				steps.get().navigateTomenu();
				log.info("Navigated to Menu");
			}
			else if(env.equalsIgnoreCase("Production")){
				DriverFactory.getDriverFacade().getWebDriver().get(PropertyManager.getConfigValueByKey("url"));
				log.info("Browser is Launched!!!");
				steps.get().ambienteLogin(DataUtil.user,DataUtil.password);
				log.info("Login clicked");
				steps.get().navigateTomenu();
				log.info("Navigated to Menu");
			}
		}
		catch(WebDriverException e) {
			LocalDateTime d = LocalDateTime.now();
			DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String date=ft.format(d);
			ExcelReader loginexcel=DataUtil.loginexcel;
			loginexcel.setCellData(DataUtil.loginSheetName, "End_Time", 2, date);
		}
		
	}


	@AfterMethod
	public void after() {
		DriverFactory.getDriverFacade().getWebDriver().quit();
	}

	@AfterSuite
	public void endtimeofexecution() {
		LocalDateTime d = LocalDateTime.now();
		DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String date=ft.format(d);
		log.info("Execution Ended at: "+date);
		ExcelReader loginexcel=DataUtil.loginexcel;
		loginexcel.setCellData(DataUtil.loginSheetName, "End_Time", 2, date);
	}

}
