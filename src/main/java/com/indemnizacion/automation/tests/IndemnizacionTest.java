package com.indemnizacion.automation.tests;

import java.util.Hashtable;

import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.indemnizacion.automation.commons.Hooks;
import com.indemnizacion.automation.utils.DataProviders;
import com.indemnizacion.automation.utils.DataUtil;

import io.qameta.allure.Step;

public class IndemnizacionTest extends Hooks{
	
	public static Logger log=Logger.getLogger(IndemnizacionTest.class);
	ThreadLocal<IndemnizacionStepDef> steps= ThreadLocal.withInitial(IndemnizacionStepDef::new);

	@Step("Test Combinations of Causa,Consequencia & Cobertura")
	@Test(dataProviderClass=DataProviders.class,dataProvider= "SeccionProductData",retryAnalyzer=com.indemnizacion.automation.flow.Retry.class)
	public void verify_combinations(Hashtable<String,String> data) {
		steps.get().consultpoliza(DataUtil.tipo_doc, DataUtil.num_doc, DataUtil.fecha_sini,data.get("S.No"), data.get("Seccion"),data.get("Producto"));
		
	}
	
}
