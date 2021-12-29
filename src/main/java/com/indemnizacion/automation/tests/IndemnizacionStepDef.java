package com.indemnizacion.automation.tests;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;

import com.indemnizacion.automation.flow.PolizaFlow;
import com.indemnizacion.automation.flow.RegressionFlow;

import io.qameta.allure.Step;

public class IndemnizacionStepDef extends IndemnizacionTest{
	
	public static Logger log=Logger.getLogger(IndemnizacionStepDef.class);
    @Step("The user clicks on the login Link with user {0}")
    public IndemnizacionTest clickLogin(String user){
    	new RegressionFlow().login(user);
        return this;
    }
    
    @Step("The user clicks on the login Link with user {0}")
    public IndemnizacionTest ambienteLogin(String user,String password){
    	new RegressionFlow().ambientePruebasLogin(user,password);
        return this;
    }
    
    @Step("User Navigates to Menu")
    public IndemnizacionTest navigateTomenu() {
    	new RegressionFlow().menuNavigation();
    	return this;
    }
    
    @Step("Consult Poliza")
    public IndemnizacionTest consultpoliza(String tipo_doc,String num_doc,String fecha_sini,String data_rownum,String seccion,String product) {
    	
    	try {
    		new PolizaFlow().consultar_poliza(tipo_doc, num_doc, fecha_sini,data_rownum,seccion,product);
    	}
    	catch(NoSuchElementException e) {
    		CharArrayWriter cw = new CharArrayWriter();
            PrintWriter w = new PrintWriter(cw);
            e.printStackTrace(w);
            w.close();
            String trace = cw.toString();
    		log.error(trace);
    		Assert.fail("NoSuchElementException");
    	}
    	catch(TimeoutException e) {
    		CharArrayWriter cw = new CharArrayWriter();
            PrintWriter w = new PrintWriter(cw);
            e.printStackTrace(w);
            w.close();
            String trace = cw.toString();
    		log.error(trace);
    		Assert.fail("TimeoutException");
    	}
    	catch(Exception e) {
    		CharArrayWriter cw = new CharArrayWriter();
            PrintWriter w = new PrintWriter(cw);
            e.printStackTrace(w);
            w.close();
            String trace = cw.toString();
    		log.error(trace);
    		Assert.fail(e.toString());
    	}
    	return this;
    	
    }
    
}
