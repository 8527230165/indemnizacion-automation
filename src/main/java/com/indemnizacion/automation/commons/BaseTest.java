package com.indemnizacion.automation.commons;

import org.openqa.selenium.support.PageFactory;

import com.indemnizacion.automation.commons.helpers.DriverFactory;
import com.indemnizacion.automation.commons.helpers.DriverFacade;

public class BaseTest {
	protected DriverFacade driverFacade;
    
    public BaseTest() {
		this.driverFacade = DriverFactory.getDriverFacade();
		PageFactory.initElements(driverFacade.getWebDriver(),this);
	}
}
