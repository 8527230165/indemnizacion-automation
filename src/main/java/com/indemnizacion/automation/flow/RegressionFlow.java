package com.indemnizacion.automation.flow;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.indemnizacion.automation.ui.RegressionUI;
import com.indemnizacion.automation.utils.DataUtil;
import com.indemnizacion.automation.utils.FlowUtil;
import com.indemnizacion.automation.utils.PropertyManager;

public class RegressionFlow extends RegressionUI{

	public static Logger log=Logger.getLogger(RegressionFlow.class);
    public String loginUsername(){
        webDriverFacade.waitForVisibilityOfElement(buttonProfile);
       // webDriverFacade.awaitToFindElement(outlookNotSignedButton);
        buttonProfile.click();
        webDriverFacade.waitForVisibilityOfElement(labelUsername);
        return labelUsername.getText();
    }

    public void fillAllTheRequiredFields(){
        driverFacade.waitForVisibilityOfElement(loginPopUpContainer);
        inputUsername.click();
        inputUsername.sendKeys(PropertyManager.getConfigValueByKey("username"));
        inputPassword.sendKeys(PropertyManager.getConfigValueByKey("password"));
        buttonLogin.click();
    }

    public void clickOnLogin(){
        webDriverFacade.waitForVisibilityOfElement(linkLogin);
        linkLogin.click();
    }
    
    
    
    //simon web login
    public void login(String loginid) {	
		FlowUtil.clearvalue(user_id);
		FlowUtil.sendvalue(user_id, loginid);
		FlowUtil.waitTillClickable(submit);
		FlowUtil.click(submit);
		FlowUtil.close_window();
		Set<String> windos =FlowUtil.getWindowHandles();
		String subWindow = null;
		Iterator<String> iterate_window = windos.iterator();
		while (iterate_window.hasNext()) {
			subWindow = iterate_window.next();
			FlowUtil.swichToWindow(subWindow);
		}
		FlowUtil.win_maximize();
		if(FlowUtil.elementIsDisplayed(advertencia)) {
			FlowUtil.waitTillVisibility(advertencia);
			FlowUtil.click(advertencia);
		}
		FlowUtil.selectByValue(compania,DataUtil.company);
		FlowUtil.waitTillInivisibility(cargando);
		FlowUtil.click(compania_continuar);
	} 
    
    //simon web menu navigation//
    public void menuNavigation() {

		try {
			FlowUtil.waitTillVisibility(siniestros_test);
			FlowUtil.movetoElement(siniestros_test);
			FlowUtil.waitTillVisibility(aviso_de_siniestro_test);
			FlowUtil.movetoElement(aviso_de_siniestro_test);
			FlowUtil.waitTillVisibility(front_consult_operciones);
			FlowUtil.movetoElementandClick(front_consult_operciones);
			FlowUtil.waitTillVisibility(navigation_path);	
		}
		catch(Exception e) {
			//Assert.fail();
		}
		
	}
    
    public void ambientePruebasLogin(String user,String password) {
    	
    	try {
    		FlowUtil.waitTillVisibility(ambiente_simonweb);
    		FlowUtil.scrollToElement(ambiente_simonweb);
    		FlowUtil.movetoElementandClick(ambiente_simonweb);
    		Set<String> wins=FlowUtil.getWindowHandles();
    		String subWindow = null;
    		Iterator<String> iterate_window = wins.iterator();
    		while (iterate_window.hasNext()) {
    			subWindow = iterate_window.next();
    			FlowUtil.swichToWindow(subWindow);
    		}
    		FlowUtil.win_maximize();
    		FlowUtil.sendvalue(usuario, user);
    		FlowUtil.sendvalue(contraseña, password);
    		FlowUtil.click(ingresa);
    		Set<String> ambiente_wins=FlowUtil.getWindowHandles();
    		subWindow = null;
    		iterate_window = ambiente_wins.iterator();
    		while (iterate_window.hasNext()) {
    			subWindow = iterate_window.next();
    			String window_title=FlowUtil.getWindowTitle(subWindow);
    			if(window_title.equalsIgnoreCase("Inicio")) {
    				FlowUtil.swichToWindow(subWindow);
    			}
    			else {
    				FlowUtil.close_window();
    			}
    		}
    		FlowUtil.win_maximize();
    		if(FlowUtil.elementIsDisplayed(advertencia)) {
    			FlowUtil.waitTillVisibility(advertencia);
    			FlowUtil.click(advertencia);
    		}
    		FlowUtil.selectByValue(compania,DataUtil.company);
    		FlowUtil.waitTillInivisibility(cargando);
    		FlowUtil.click(compania_continuar);
    		FlowUtil.scrollToElement(siniestros_test);
    		
    	}
    	catch(Exception e) {
    		CharArrayWriter cw = new CharArrayWriter();
            PrintWriter w = new PrintWriter(cw);
            e.printStackTrace(w);
            w.close();
            String trace = cw.toString();
    		log.error(trace);
			//Assert.fail(e.toString());
		}
    	
    	
    }




}
