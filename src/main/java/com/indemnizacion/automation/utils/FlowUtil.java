package com.indemnizacion.automation.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.indemnizacion.automation.commons.helpers.DriverFactory;
import com.indemnizacion.automation.ui.Pre_Aviso_IndemnizacionesUI;

public class FlowUtil extends Pre_Aviso_IndemnizacionesUI{
	
	public static Logger log=Logger.getLogger(FlowUtil.class.getName());
	
	public static void sendvalue(WebElement element , String value) {
		
		element.sendKeys(value);
	}
	public static void click(WebElement element) {
		
		waitTillClickable(element);
		element.click();
	}
	public static String getFirstSelectedOption(WebElement element) {
		
		
		boolean staleElement = true; 
		String text="";
		while(staleElement){
		  try{
			  Select select=new Select(element);
			  text=select.getFirstSelectedOption().getText();
		     staleElement = false;
		  } catch(StaleElementReferenceException e){
		    staleElement = true;
		  }
		}
		return text;
	}

	public static void selectByValue(WebElement element,String value) {
		
		boolean staleElement = true; 
		while(staleElement){
		  try{
			  Select select=new Select(element);
				select.selectByValue(value);
		     staleElement = false;
		  } catch(StaleElementReferenceException e){
		    staleElement = true;
		  }
		}
	}

	public static void selectByVisibleText(WebElement element,String value) {
		
		boolean staleElement = true; 
		while(staleElement){
		  try{
			  Select select=new Select(element);
			  select.selectByVisibleText(value);
		     staleElement = false;
		  } catch(StaleElementReferenceException e){
		    staleElement = true;
		  }
		}
	}

	public static int getOptionsSize(WebElement element) {
		
		int size=0;
		boolean staleElement = true; 
		while(staleElement){
			  try{
				  Select select=new Select(element);
				   size=select.getOptions().size();
			     staleElement = false;
			  } catch(StaleElementReferenceException e){
			    staleElement = true;
			  }
			}
		return size;
	}

	public static void movetoElement(WebElement element) {
		
		Actions action=new Actions(DriverFactory.getDriverFacade().getWebDriver());
		action.moveToElement(element).perform();
	}

	public static void movetoElementandClick(WebElement element) {
		
		Actions action=new Actions(DriverFactory.getDriverFacade().getWebDriver());
		action.moveToElement(element).click().build().perform();
	}

	public static void javascriptClick(WebElement element) {
		
		JavascriptExecutor jse2 = (JavascriptExecutor)DriverFactory.getDriverFacade().getWebDriver();
		jse2.executeScript("arguments[0].click();", element);
	}
	
	public static void scrollToElement(WebElement element) {
		
		JavascriptExecutor jse2 = (JavascriptExecutor)DriverFactory.getDriverFacade().getWebDriver();
		jse2.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public static void clearvalue(WebElement element) {
		
		element.clear();
		
	}
	public static void close_window() {
		
		DriverFactory.getDriverFacade().getWebDriver().close();
		
	}
	public static void win_maximize() {
		
		DriverFactory.getDriverFacade().getWebDriver().manage().window().maximize();
		
	}
	public static Set<String> getWindowHandles() {
		Set<String> windows=DriverFactory.getDriverFacade().getWebDriver().getWindowHandles();
		return windows;
	}
	public static void swichToWindow(String window_name) {
		
		DriverFactory.getDriverFacade().getWebDriver().switchTo().window(window_name);
	}
	public static String getWindowTitle(String window_name) {
		
		String title=DriverFactory.getDriverFacade().getWebDriver().switchTo().window(window_name).getTitle();
		return title;
	}
	public static boolean elementIsDisplayed(WebElement element) {
		
		boolean isDisplayed=element.isDisplayed();
		return isDisplayed;
	}

	public static boolean isElementPresent(WebElement element) {
		try {
			DriverFactory.getDriverFacade().getWebDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement ele=element;

			return ele.isDisplayed();
		}
		catch(NoSuchElementException e) {

			Assert.fail();
			return false;
		}
	}

	public static void waitTillVisibility(WebElement element) {
		
		WebDriverWait wait=new WebDriverWait(DriverFactory.getDriverFacade().getWebDriver(), Long.parseLong(PropertyManager.getConfigValueByKey("waitinseconds")));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void waitTillClickable(WebElement element) {
		
		WebDriverWait wait=new WebDriverWait(DriverFactory.getDriverFacade().getWebDriver(), Long.parseLong(PropertyManager.getConfigValueByKey("waitinseconds")));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public static void waitTillInivisibility(WebElement element) {
		
		WebDriverWait wait=new WebDriverWait(DriverFactory.getDriverFacade().getWebDriver(), Long.parseLong(PropertyManager.getConfigValueByKey("waitinseconds")));
		wait.until(ExpectedConditions.invisibilityOf(element));
	}

	public static void pressTab() {
		
		try {
			new Robot().keyPress(KeyEvent.VK_TAB);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static void sleep(int time) {
		
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
