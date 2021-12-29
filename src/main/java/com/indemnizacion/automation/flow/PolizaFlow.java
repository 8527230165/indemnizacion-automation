package com.indemnizacion.automation.flow;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.indemnizacion.automation.commons.helpers.DriverFactory;
import com.indemnizacion.automation.ui.Pre_Aviso_IndemnizacionesUI;
import com.indemnizacion.automation.utils.DataUtil;
import com.indemnizacion.automation.utils.ExcelReader;
import com.indemnizacion.automation.utils.FlowUtil;
import com.indemnizacion.automation.utils.PropertyManager;

public class PolizaFlow extends Pre_Aviso_IndemnizacionesUI{
	

	public static Logger log=Logger.getLogger(PolizaFlow.class);
	public void consultar_poliza(String tipo_doc,String num_doc,String fecha_sini,String data_rownum,String seccion,String product) {
		
		FlowUtil.waitTillVisibility(aseg_tipo_doc);
		FlowUtil.selectByValue(aseg_tipo_doc, tipo_doc);
		FlowUtil.waitTillInivisibility(cargando);	
		FlowUtil.sendvalue(aseg_num_doc, num_doc);
		FlowUtil.pressTab();
		FlowUtil.waitTillInivisibility(cargando);
				
		if(DataUtil.isElementEnabled(clv)) {
			if(FlowUtil.getOptionsSize(clv)>1 ) {
				FlowUtil.sendvalue(fecha_del_siniestro, fecha_sini);
				boolean singleCLVTest=Boolean.parseBoolean(PropertyManager.getConfigValueByKey("singleCLVTest"));
				for(int i=1;i<FlowUtil.getOptionsSize(clv);i++) {
						FlowUtil.selectByValue(clv, String.valueOf(i));
						FlowUtil.waitTillInivisibility(cargando);
						DataUtil.clv_ui=FlowUtil.getFirstSelectedOption(clv).split(" - ")[0];//clv code
						FlowUtil.click(consultar);
						try {
							selectpoliza(data_rownum,DataUtil.clv_ui,seccion,product);
							log.info(DataUtil.clv_ui+" :CLV done");
							if(singleCLVTest) {
								log.info("Single CLV Done");
								break;
							}
						}
						catch(NoSuchElementException e) {
							log.error(e);
							Assert.fail("NoSuchElementException");
						}
				}
			}
		}
		else {
			FlowUtil.sendvalue(fecha_del_siniestro, fecha_sini);
			DataUtil.clv_ui=FlowUtil.getFirstSelectedOption(clv).split(" - ")[0];//clv code
			FlowUtil.javascriptClick(consultar);
			try {
				selectpoliza(data_rownum,DataUtil.clv_ui,seccion,product);
			}
			catch(NoSuchElementException e) {
				e.printStackTrace();
				log.error(e);
				Assert.fail("NoSuchElementException");
			}

		}
	}


	public void selectpoliza(String data_rowNum,String clv,String seccion, String producto) {
		ExcelReader loginExcel = DataUtil.loginexcel;
		FlowUtil.waitTillInivisibility(cargando);	
		int total_registros = 0;
		try {
			total_registros=Integer.parseInt(lista_poliza.getText().split(":")[1].trim());
		}
		catch(ArrayIndexOutOfBoundsException e) {
			total_registros=0;
		}
		FlowUtil.sleep(3000);
		if(total_registros>0) {
			if(FlowUtil.isElementPresent(polizas_table_navigation)) {
				FlowUtil.movetoElement(polizas_table_navigation);				
				int i=0;int k=2;int l=0;
				boolean seccPresent=false;boolean prodPresent=false;
				boolean secciondone=false;boolean productodone=false;
				while(i<=(total_registros/10) ) {
					int j=0;			
					while(j<10 && l<total_registros) {				
						WebElement poliza=DriverFactory.getDriverFacade().getWebDriver().findElement(By.xpath("//tr[@id='searchForm:policySearchTable:"+j+"']"));				
						FlowUtil.movetoElement(poliza);
						WebElement secc_prod_col=DriverFactory.getDriverFacade().getWebDriver().findElement(By.xpath("//tbody//tr/td[@id='searchForm:policySearchTable:"+l+":colProd']"));
						String secc=secc_prod_col.getText().split(":")[0].trim();
						String prod=secc_prod_col.getText().split(":")[1].split("-")[0].trim();
						if(seccion.equalsIgnoreCase(secc) && (!secciondone)) {
							if (producto.equalsIgnoreCase(prod) && (!productodone)) {
								WebElement policy=DriverFactory.getDriverFacade().getWebDriver().findElement(By.xpath("//td[@id='searchForm:policySearchTable:"+l+":col0']/span"));
								String num_pol=policy.getText().split(" - ")[0];
								log.info("Numero Poliza:"+num_pol);
								FlowUtil.movetoElement(secc_prod_col);
								FlowUtil.click(secc_prod_col);
								FlowUtil.waitTillInivisibility(cargando);
								try {
									consultRiesgo();
								}
								catch(NoSuchElementException e) {
									log.error(e);
									Assert.fail("NoSuchElementException");
								}
								try {
									DatosSiniestro.datosiniestro(DataUtil.company,num_pol,clv, DataUtil.description, DataUtil.valor_pretension,Integer.parseInt(secc),Integer.parseInt(prod));	
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
								seccPresent=true;
								prodPresent=true;
								secciondone=true;
								productodone=true;
								int row=Integer.parseInt(data_rowNum);
								int rowNum=row+1;
								loginExcel.setCellData("Seccion_Product", "Exists/Not_Exists",rowNum,"Exists and Done");
								
							}
							else {
								FlowUtil.movetoElement(secc_prod_col);
							}
						}
						else {
							FlowUtil.movetoElement(secc_prod_col);
						}
						j++;
						l++;
					}
					if(j>=10 && k<=(total_registros/10)) {				
						FlowUtil.movetoElement(polizas_table_navigation);
						WebElement next_page=DriverFactory.getDriverFacade().getWebDriver().findElement(By.xpath("//a[@id='searchForm:policySearchTable:dsPolicy_ds_"+k+"']"));
						FlowUtil.movetoElement(next_page);
						FlowUtil.javascriptClick(next_page);
						FlowUtil.waitTillInivisibility(cargando);
						k++;
					}
					i++;
				}
				if(!seccPresent && !prodPresent) {
					int row=Integer.parseInt(data_rowNum);
					int rowNum=row+1;
					loginExcel.setCellData("Seccion_Product", "Exists/Not_Exists",rowNum,"Not_Exists");
					FlowUtil.movetoElement(polizas_table_navigation);
					FlowUtil.movetoElement(navigation_first);
					FlowUtil.javascriptClick(navigation_first);
					FlowUtil.waitTillInivisibility(cargando);
				}

			}
			else {
				int i=0;int k=2;int l=0;
				boolean seccPresent=false;boolean prodPresent=false;
				boolean secciondone=false;boolean productodone=false;
				while(i<=(total_registros/10) ) {
					int j=0;			
					while(j<10 && l<total_registros) {				
						WebElement poliza=DriverFactory.getDriverFacade().getWebDriver().findElement(By.xpath("//tr[@id='searchForm:policySearchTable:"+j+"']"));				
						FlowUtil.movetoElement(poliza);
						WebElement secc_prod_col=DriverFactory.getDriverFacade().getWebDriver().findElement(By.xpath("//tbody//tr/td[@id='searchForm:policySearchTable:"+l+":colProd']"));
						String secc=secc_prod_col.getText().split(":")[0].trim();
						String prod=secc_prod_col.getText().split(":")[1].split("-")[0].trim();
						if(seccion.equalsIgnoreCase(secc) && (!secciondone)) {
							if (producto.equalsIgnoreCase(prod) && (!productodone)) {
								WebElement policy=DriverFactory.getDriverFacade().getWebDriver().findElement(By.xpath("//td[@id='searchForm:policySearchTable:"+l+":col0']/span"));
								String num_pol=policy.getText().split(" - ")[0];
								log.info("Numero Poliza:"+num_pol);
								FlowUtil.movetoElement(secc_prod_col);
								FlowUtil.click(secc_prod_col);
								FlowUtil.waitTillInivisibility(cargando);
								try {
									consultRiesgo();
								}
								catch(NoSuchElementException e) {
									log.info("NoSuchElementExeption");
									CharArrayWriter cw = new CharArrayWriter();
						            PrintWriter w = new PrintWriter(cw);
						            e.printStackTrace(w);
						            w.close();
						            String trace = cw.toString();
						    		log.error(trace);
									
									Assert.fail("NoSuchElementException");
								}
								try {
									DatosSiniestro.datosiniestro(DataUtil.company,num_pol,clv, DataUtil.description, DataUtil.valor_pretension,Integer.parseInt(secc),Integer.parseInt(prod));	
								}
								catch(NoSuchElementException e) {
									log.info("NoSuchElementExeption");
									CharArrayWriter cw = new CharArrayWriter();
						            PrintWriter w = new PrintWriter(cw);
						            e.printStackTrace(w);
						            w.close();
						            String trace = cw.toString();
						    		log.error(trace);
									Assert.fail("NoSuchElementException");
								}
								seccPresent=true;
								prodPresent=true;
								secciondone=true;
								productodone=true;
								int row=Integer.parseInt(data_rowNum);
								int rowNum=row+1;
								loginExcel.setCellData("Seccion_Product", "Exists/Not_Exists",rowNum,"Exists and Done");
							}
							else {
								FlowUtil.movetoElement(secc_prod_col);
							}
						}
						else {
							FlowUtil.movetoElement(secc_prod_col);
						}
						j++;
						l++;
					}
					if(j>=10 && k<=(total_registros/10)) {				
						WebElement next_page=DriverFactory.getDriverFacade().getWebDriver().findElement(By.xpath("//a[@id='searchForm:policySearchTable:dsPolicy_ds_"+k+"']"));
						FlowUtil.movetoElement(next_page);
						FlowUtil.javascriptClick(next_page);
						FlowUtil.waitTillInivisibility(cargando);
						k++;
					}
					i++;
				}
				if(!seccPresent && !prodPresent) {	
					int row=Integer.parseInt(data_rowNum);
					int rowNum=row+1;
					loginExcel.setCellData("Seccion_Product", "Exists/Not_Exists",rowNum,"Not_Exists");
					
				}

			}
		}
		else {
			int row=Integer.parseInt(data_rowNum);
			int rowNum=row+1;
			loginExcel.setCellData("Seccion_Product", "Exists/Not_Exists",rowNum, "No Policy Found");
			log.info("No Policy Found");
		}
		


	}

	public void consultRiesgo() {
		FlowUtil.movetoElementandClick(riesgo);
		FlowUtil.waitTillInivisibility(cargando);
	}
}
