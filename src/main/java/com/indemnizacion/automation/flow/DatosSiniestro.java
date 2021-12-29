package com.indemnizacion.automation.flow;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;

import com.indemnizacion.automation.commons.ConnectionDB;
import com.indemnizacion.automation.commons.helpers.DriverFactory;
import com.indemnizacion.automation.ui.Pre_Aviso_IndemnizacionesUI;
import com.indemnizacion.automation.utils.DataUtil;
import com.indemnizacion.automation.utils.ExcelReader;
import com.indemnizacion.automation.utils.FlowUtil;
import com.indemnizacion.automation.utils.PropertyManager;

public class DatosSiniestro extends Pre_Aviso_IndemnizacionesUI{
	public static Logger log=Logger.getLogger(DatosSiniestro.class);

	public static void datosiniestro(String company,String num_pol,String clv,String description,int valor_pretension,int seccion,int producto) 
	{
		FlowUtil.movetoElement(continuar);
		FlowUtil.click(sini_description);
		FlowUtil.sendvalue(sini_description,description);
		FlowUtil.movetoElement(causa);
		FlowUtil.waitTillInivisibility(cargando);

		ExcelReader DefinicionExcel = DataUtil.definicion_excel;
		ExcelReader clasificacionExcel = DataUtil.clasificacion_excel;
		log.info("Searching for Causa, Consequencia & Cobertura....");

		for(int i=2;i<clasificacionExcel.getRowCount()+1;i++) {
			log.info("Running...");
			int pretension_value=valor_pretension;
			int causa_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName,"CAUSAS",i));
			DriverFactory.getDriverFacade().getWebDriver().getTitle();
			if(producto==DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName,"PRODUCTO",i)))
			{
				if(FlowUtil.getOptionsSize(causa)>1 
						&& DataUtil.isValuePresent(causa,causa_value)
						&& clasificacionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",i).equalsIgnoreCase("")) 
				{

					FlowUtil.selectByValue(causa,String.valueOf(causa_value));
					FlowUtil.waitTillInivisibility(cargando);
					FlowUtil.movetoElement(consequncia);

					int consequncia_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName,"CONSECUENCIA",i));
					if(FlowUtil.getOptionsSize(consequncia)>1 
							&&  DataUtil.isValuePresent(consequncia,consequncia_value))
					{
						FlowUtil.selectByValue(consequncia,String.valueOf(consequncia_value));
						FlowUtil.waitTillInivisibility(cargando);
						FlowUtil.movetoElement(cobertura);						


						int cobertura_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", i));
						if(FlowUtil.getOptionsSize(cobertura)>1 
								&&  DataUtil.isValuePresent(cobertura,cobertura_value)) 
						{

							FlowUtil.selectByValue(cobertura,String.valueOf(cobertura_value));
							FlowUtil.waitTillInivisibility(cargando);

							if(DataUtil.isElementEnabled(cliente_riesgo)) {
								log.info("Executing combination of Causa: "+causa_value+", Consequencia:"+consequncia_value+" & Cobertura:"+cobertura_value);
								int score_riesgo_count=Integer.parseInt(PropertyManager.getConfigValueByKey("scoreRiesgoValuesCount"));
								String score_riesgo_values=  PropertyManager.getConfigValueByKey("scoreRiesgo");
								for(int j=0;j<score_riesgo_count;j++) 
								{
									int cliente_riesgo_code=Integer.parseInt(score_riesgo_values.split(",")[j].split(" - ")[0]);//cliente_ries_code
									FlowUtil.selectByVisibleText(cliente_riesgo,score_riesgo_values.split(",")[j]);
									FlowUtil.waitTillInivisibility(cargando);

									while(pretension_value<=12000000) {

										FlowUtil.waitTillClickable(valo_pretension);
										FlowUtil.click(valo_pretension);
										FlowUtil.sendvalue(valo_pretension,String.valueOf(pretension_value));
										FlowUtil.click(sini_description);
										FlowUtil.waitTillInivisibility(cargando);
										FlowUtil.movetoElement(cobertura);											
										FlowUtil.waitTillInivisibility(cargando);
										FlowUtil.click(continuar);
										FlowUtil.waitTillInivisibility(cargando);
										try {
											Modelos.clasificacion_definacion(DataUtil.company,num_pol,clv,description,pretension_value,seccion,producto,causa_value,consequncia_value,cobertura_value,cliente_riesgo_code);	
										}
										catch(NoSuchElementException e) {
											log.info("NoSuchElementException caught");
											log.error(e);
											Assert.fail("No Such Element Exception");
											
										}
										catch(TimeoutException e) {
											log.info("Session Got Expired!!!");
											log.error(e);
											int doneRecord=i-1;
											log.info("Combinations Data is done till Record:"+doneRecord);
											Assert.fail("TimeoutException");
											
										}
										FlowUtil.movetoElementandClick(reset);
										FlowUtil.waitTillInivisibility(cargando);
										FlowUtil.click(sini_description);
										FlowUtil.sendvalue(sini_description,description);
										FlowUtil.movetoElement(causa);											
										FlowUtil.waitTillInivisibility(cargando);
										FlowUtil.selectByValue(causa,String.valueOf(causa_value));											
										FlowUtil.waitTillInivisibility(cargando);
										FlowUtil.selectByValue(consequncia,String.valueOf(consequncia_value));											
										FlowUtil.waitTillInivisibility(cargando);
										FlowUtil.selectByValue(cobertura,String.valueOf(cobertura_value));
										FlowUtil.waitTillInivisibility(cargando);
										FlowUtil.selectByVisibleText(cliente_riesgo,score_riesgo_values.split(",")[j]);
										FlowUtil.waitTillInivisibility(cargando);
										pretension_value=pretension_value+3000000;
									}

									pretension_value=valor_pretension;
								}
								if(DataUtil.dbInsertion) {
									log.info("Inserted into Database table");
								}
								log.info("Clasificacion & Definicion Output File Created");
								log.info("A Combination of Causa:"+causa_value+", Consequencia:"+consequncia_value+",Cobertura:"+cobertura_value+" is done!!!");
								int doneRecord=i-1;
								int totalRecords=clasificacionExcel.getRowCount()-1;
								log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");
							}
							else {
								int cliente_riesgo_value=Integer.parseInt(FlowUtil.getFirstSelectedOption(cliente_riesgo).split(" - ")[0]);//cliente_ries_code	
								

								for(int j=2;j<clasificacionExcel.getRowCount()+1;j++) 
								{
									if(producto==DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", j))
											&& causa_value==DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",j))
											&& consequncia_value==DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", j))
											&& cobertura_value==DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", j))
											&& (clv.equalsIgnoreCase("0")
													|| clv.equalsIgnoreCase("1")
													|| clv.equalsIgnoreCase("2")
													|| clv.equalsIgnoreCase("3")
													|| clv.equalsIgnoreCase("4"))
											&& clasificacionExcel.getCellData(DataUtil.inputSheetName, "Execution_Status", j).equalsIgnoreCase("") 
											)

									{
										if(cliente_riesgo_value==DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", j))) 
										{
											while(pretension_value<=12000000) {

												FlowUtil.waitTillClickable(valo_pretension);
												FlowUtil.click(valo_pretension);
												FlowUtil.sendvalue(valo_pretension,String.valueOf(pretension_value));
												FlowUtil.pressTab();
												FlowUtil.waitTillInivisibility(cargando);
												FlowUtil.movetoElement(cobertura);											
												FlowUtil.waitTillInivisibility(cargando);
												FlowUtil.click(continuar);
												FlowUtil.waitTillInivisibility(cargando);
												try {
													Modelos.clasificacion_definacion(DataUtil.company,num_pol,clv,description,pretension_value,seccion,producto,causa_value,consequncia_value,cobertura_value,cliente_riesgo_value);	
												}
												catch(NoSuchElementException e) {
													
										    		log.error(e);
													Assert.fail("NoSuchElementException");
												}
												catch(TimeoutException e) {
													log.info("Session Got Expired!!!");
													log.error(e);
													int doneRecord=i-1;
													log.info("Combinations Data is done till Record:"+doneRecord);
													Assert.fail("TimeoutException");
													
												}

												FlowUtil.movetoElementandClick(reset);
												FlowUtil.waitTillInivisibility(cargando);
												FlowUtil.click(sini_description);
												FlowUtil.sendvalue(sini_description,description);
												FlowUtil.movetoElement(causa);											
												FlowUtil.waitTillInivisibility(cargando);
												FlowUtil.selectByValue(causa,String.valueOf(causa_value));											
												FlowUtil.waitTillInivisibility(cargando);
												FlowUtil.selectByValue(consequncia,String.valueOf(consequncia_value));											
												FlowUtil.waitTillInivisibility(cargando);
												FlowUtil.selectByValue(cobertura,String.valueOf(cobertura_value));
												FlowUtil.waitTillInivisibility(cargando);
												pretension_value=pretension_value+3000000;
												
											}
											pretension_value=valor_pretension;
											
											if(DataUtil.dbInsertion) {
												log.info("Inserted into Database table");
											}
											log.info("Clasificacion & Definicion Output File Created");
											log.info("A Combination of Causa:"+causa_value+", Consequencia:"+consequncia_value+",Cobertura:"+cobertura_value+" is done!!!");
											int doneRecord=i-1;
											int totalRecords=clasificacionExcel.getRowCount()-1;
											log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");
										}
										else {
											clasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", j, "Executed");
											int score_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", j));
											int modelo_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", j));
											int resultado_encuesta=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", j));
											int clasificacion=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", j));
											
											for(int k=2;k<DefinicionExcel.getRowCount()+1;k++) {
												if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", k)) 
														&& causa_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",k)) 
														&& consequncia_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", k))
														&& cobertura_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", k))
														&& (clv.equalsIgnoreCase("0")
																|| clv.equalsIgnoreCase("1")
																|| clv.equalsIgnoreCase("2")
																|| clv.equalsIgnoreCase("3")
																|| clv.equalsIgnoreCase("4"))
														&& score_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", k))
														&& modelo_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", k))
														&& resultado_encuesta==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", k))
														&& clasificacion==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CLASIFICACION CASO CODE", k))
														&& 	DefinicionExcel.getCellData(DataUtil.inputSheetName, "Execution_Status", k).equalsIgnoreCase("")
														) {
													DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", k, "Executed");
													int resultado_evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", k));
													int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", k));
													DataUtil.outputDefinicionExcel.setDefinicionCellValue(k,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,resultado_evidencia,definicion,"Fail","Score Riesgo Not Found"));

												}

											}

											DataUtil.outputClasificacionExcel.setClasificacionCellValue(j,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,"Fail","Score Riesgo Not Found"));

											String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv,causa_value,consequncia_value,cobertura_value,"",score_riesgo,"");
											String clas_response=DataUtil.clasificacion_response("");
											if(DataUtil.dbInsertion) {
												ConnectionDB.Konnection();
												ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail","Score Riesgo Not Found",DataUtil.sysDate());
												
											}
											
										}
									}
								}
								if(DataUtil.dbInsertion) {
									log.info("Inserted into Database table");
								}
					
								log.info("Clasificacion & Definicion Output File Created");
								log.info("A Combination of Causa:"+causa_value+", Consequencia:"+consequncia_value+",Cobertura:"+cobertura_value+" is done!!!");
								int doneRecord=i-1;
								int totalRecords=clasificacionExcel.getRowCount()-1;
								log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");

								/*if(!causaFound && !consequenciaFound && !coberturaFound) {
									String data="Causa, Consequencia & Cobertura not Found in Input Excel File.";
									clasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i, data);
									DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i, data);
								}*/
							}											
						}
						else if(FlowUtil.getOptionsSize(cobertura)<=1
								&& clasificacionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",i).equalsIgnoreCase("")){
							String data="No Cobertura Found";
							clasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i,"Executed");

							int score_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", i));
							int modelo_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", i));
							int resultado_encuesta=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
							int clasificacion=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));

							for(int j=2;j<DefinicionExcel.getRowCount()+1;j++) {
								if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", j)) 
										&& causa_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",j)) 
										&& consequncia_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", j))
										&& cobertura_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", j))
										&& (clv.equalsIgnoreCase("0")
												|| clv.equalsIgnoreCase("1")
												|| clv.equalsIgnoreCase("2")
												|| clv.equalsIgnoreCase("3")
												|| clv.equalsIgnoreCase("4"))
										&& score_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", j))
										&& modelo_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", j))
										&& resultado_encuesta==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", j))
										) {
									DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", j, "Executed");
									int resultado_evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", j));
									int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", j));
									DataUtil.outputDefinicionExcel.setDefinicionCellValue(i,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,resultado_evidencia,definicion,"Fail",data));

								}

							}

							DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,"Fail",data));

							String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv,causa_value,consequncia_value,"","","","");
							String clas_response=DataUtil.clasificacion_response("");
							if(DataUtil.dbInsertion) {
								ConnectionDB.Konnection();
								ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail",data,DataUtil.sysDate());
								
							}
							int doneRecord=i-1;
							int totalRecords=clasificacionExcel.getRowCount()-1;
							log.info(data);
							log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");


						}
						else if(!DataUtil.isValuePresent(cobertura,cobertura_value)
								&& clasificacionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",i).equalsIgnoreCase("")){
							String data="Cobertura Not Found for Causa and Consequencia";
							clasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i, "Executed");

							int score_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", i));
							int modelo_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", i));
							int resultado_encuesta=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
							int clasificacion=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));

							for(int j=2;j<DefinicionExcel.getRowCount()+1;j++) {
								if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", j)) 
										&& causa_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",j)) 
										&& consequncia_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", j))
										&& cobertura_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", j))
										&& (clv.equalsIgnoreCase("0")
												|| clv.equalsIgnoreCase("1")
												|| clv.equalsIgnoreCase("2")
												|| clv.equalsIgnoreCase("3")
												|| clv.equalsIgnoreCase("4"))
										&& score_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", j))
										&& modelo_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", j))
										&& resultado_encuesta==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", j))
										) {
									DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", j, "Executed");
									int resultado_evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", j));
									int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", j));
									DataUtil.outputDefinicionExcel.setDefinicionCellValue(i,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,resultado_evidencia,definicion,"Fail",data));

								}

							}

							DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,"Fail",data));


							String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv,causa_value,consequncia_value,cobertura_value,"","","");
							String clas_response=DataUtil.clasificacion_response("");
							if(DataUtil.dbInsertion) {
								ConnectionDB.Konnection();
								ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail",data,DataUtil.sysDate());

							}
							
							int doneRecord=i-1;
							int totalRecords=clasificacionExcel.getRowCount()-1;
							log.info(data);
							log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");

						}
					}
					else if(FlowUtil.getOptionsSize(consequncia)<=1
							&& clasificacionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",i).equalsIgnoreCase("")){
						String data= "No Consequencia Found";

						clasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i,"Executed");

						int cobertura_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", i));
						int score_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", i));
						int modelo_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", i));
						int resultado_encuesta=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
						int clasificacion=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));

						for(int j=2;j<DefinicionExcel.getRowCount()+1;j++) {
							if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", j)) 
									&& causa_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",j)) 
									&& consequncia_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", j))
									&& cobertura_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", j))
									&& (clv.equalsIgnoreCase("0")
											|| clv.equalsIgnoreCase("1")
											|| clv.equalsIgnoreCase("2")
											|| clv.equalsIgnoreCase("3")
											|| clv.equalsIgnoreCase("4"))
									&& score_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", j))
									&& modelo_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", j))
									&& resultado_encuesta==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", j))
									) {
								DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", j, "Executed");
								int resultado_evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", j));
								int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", j));
								DataUtil.outputDefinicionExcel.setDefinicionCellValue(i,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,resultado_evidencia,definicion,"Fail",data));

							}

						}

						DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,"Fail",data));

						String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv,causa_value,"","","","","");
						String clas_response=DataUtil.clasificacion_response("");
						if(DataUtil.dbInsertion) {
							ConnectionDB.Konnection();
							ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail",data,DataUtil.sysDate());

						}
						
						int doneRecord=i-1;
						int totalRecords=clasificacionExcel.getRowCount()-1;
						log.info(data);
						log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");



					}
					else if(!DataUtil.isValuePresent(consequncia,consequncia_value)
							&& clasificacionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",i).equalsIgnoreCase("")){
						String data="Consequencia Not Found for Causa";
						clasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i,"Executed");
						int cobertura_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", i));
						int score_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", i));
						int modelo_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", i));
						int resultado_encuesta=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
						int clasificacion=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));

						for(int j=2;j<DefinicionExcel.getRowCount()+1;j++) {
							if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", j)) 
									&& causa_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",j)) 
									&& consequncia_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", j))
									&& cobertura_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", j))
									&& (clv.equalsIgnoreCase("0")
											|| clv.equalsIgnoreCase("1")
											|| clv.equalsIgnoreCase("2")
											|| clv.equalsIgnoreCase("3")
											|| clv.equalsIgnoreCase("4"))
									&& score_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", j))
									&& modelo_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", j))
									&& resultado_encuesta==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", j))
									&& clasificacion==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CLASIFICACION CASO CODE", j))
									) {
								DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", j, "Executed");
								int resultado_evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", j));
								int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", j));
								DataUtil.outputDefinicionExcel.setDefinicionCellValue(i,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,resultado_evidencia,definicion,"Fail",data));

							}

						}
						DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,"Fail",data));

						String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv,causa_value,consequncia_value,"","","","");
						String clas_response=DataUtil.clasificacion_response("");
						if(DataUtil.dbInsertion) {
							ConnectionDB.Konnection();
							ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail",data,DataUtil.sysDate());

						}
						
						int doneRecord=i-1;
						int totalRecords=clasificacionExcel.getRowCount()-1;
						log.info(data);
						log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");
					}
				}
				else if(FlowUtil.getOptionsSize(causa)<=1 
						&& clasificacionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",i).equalsIgnoreCase("")){
					String data="No Causa Found";
					
					clasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i,"Executed");
					int consequncia_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", i));
					int cobertura_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", i));
					int score_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", i));
					int modelo_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", i));
					int resultado_encuesta=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
					int clasificacion=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));

					for(int j=2;j<DefinicionExcel.getRowCount()+1;j++) {
						if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", j)) 
								&& causa_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",j)) 
								&& consequncia_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", j))
								&& cobertura_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", j))
								&& (clv.equalsIgnoreCase("0")
										|| clv.equalsIgnoreCase("1")
										|| clv.equalsIgnoreCase("2")
										|| clv.equalsIgnoreCase("3")
										|| clv.equalsIgnoreCase("4"))
								&& score_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", j))
								&& modelo_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", j))
								&& resultado_encuesta==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", j))
								) {
							DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", j, "Executed");
							int resultado_evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", j));
							int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", j));
							DataUtil.outputDefinicionExcel.setDefinicionCellValue(i,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,resultado_evidencia,definicion,"Fail",data));

						}

					}

					DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,"Fail",data));

					String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv,"","","","","","");
					String clas_response=DataUtil.clasificacion_response("");
					if(DataUtil.dbInsertion) {
						ConnectionDB.Konnection();
						ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail",data,DataUtil.sysDate());

					}
					
					int doneRecord=i-1;
					int totalRecords=clasificacionExcel.getRowCount()-1;
					log.info(data);
					log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");
				
					
				}
				else if(!DataUtil.isValuePresent(causa,causa_value)
						&& clasificacionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",i).equalsIgnoreCase("")){
					String data="Causa Not Found for Producto";
					clasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i,"Executed");
					int consequncia_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", i));
					int cobertura_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", i));
					int score_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", i));
					int modelo_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", i));
					int resultado_encuesta=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
					int clasificacion=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));

					for(int j=2;j<DefinicionExcel.getRowCount()+1;j++) {
						if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", j)) 
								&& causa_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",j)) 
								&& consequncia_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", j))
								&& cobertura_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", j))
								&& (clv.equalsIgnoreCase("0")
										|| clv.equalsIgnoreCase("1")
										|| clv.equalsIgnoreCase("2")
										|| clv.equalsIgnoreCase("3")
										|| clv.equalsIgnoreCase("4"))
								&& score_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", j))
								&& modelo_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", j))
								&& resultado_encuesta==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", j))
								) {
							DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", j, "Executed");
							int resultado_evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", j));
							int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", j));
							DataUtil.outputDefinicionExcel.setDefinicionCellValue(i,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,resultado_evidencia,definicion,"Fail",data));

						}

					}

					DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv,causa_value,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,"Fail",data));

					String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv,causa_value,"","","","","");
					String clas_response=DataUtil.clasificacion_response("");
					if(DataUtil.dbInsertion) {
						ConnectionDB.Konnection();
						ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail",data,DataUtil.sysDate());

					}
					
					int doneRecord=i-1;
					int totalRecords=clasificacionExcel.getRowCount()-1;
					log.info(data);
					log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");
				
					
				}
			}
			else {//if producto is not equal
				String data="Producto Not Found";
				clasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i,"Executed");
				int causa_val=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS", i));
				int consequncia_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", i));
				int cobertura_value=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", i));
				int score_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", i));
				int modelo_riesgo=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", i));
				int resultado_encuesta=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
				int clasificacion=DataUtil.getCode(clasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));

				for(int j=2;j<DefinicionExcel.getRowCount()+1;j++) {
					if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", j)) 
							&& causa_val==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",j)) 
							&& consequncia_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", j))
							&& cobertura_value==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", j))
							&& (clv.equalsIgnoreCase("0")
									|| clv.equalsIgnoreCase("1")
									|| clv.equalsIgnoreCase("2")
									|| clv.equalsIgnoreCase("3")
									|| clv.equalsIgnoreCase("4"))
							&& score_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", j))
							&& modelo_riesgo==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", j))
							&& resultado_encuesta==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", j))
							) {
						DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", j, "Executed");
						int resultado_evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", j));
						int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", j));
						DataUtil.outputDefinicionExcel.setDefinicionCellValue(i,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv,causa_val,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,resultado_evidencia,definicion,"Fail",data));

					}

				}

				DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv,causa_val,consequncia_value,cobertura_value,modelo_riesgo,score_riesgo,resultado_encuesta,clasificacion,"Fail",data));

				String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv,"","","","","","");
				String clas_response=DataUtil.clasificacion_response("");
				if(DataUtil.dbInsertion) {
					ConnectionDB.Konnection();
					ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail",data,DataUtil.sysDate());

				}
				
				int doneRecord=i-1;
				int totalRecords=clasificacionExcel.getRowCount()-1;
				log.info(data);
				log.info("Number of Record:"+doneRecord+" done out of Total Records in Clasificacion Excel Data File:"+totalRecords+"!!!");
			
			}

		}
	}
	
}


