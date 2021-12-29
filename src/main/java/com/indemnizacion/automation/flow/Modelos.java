package com.indemnizacion.automation.flow;

import org.apache.log4j.Logger;

import com.indemnizacion.automation.commons.ConnectionDB;
import com.indemnizacion.automation.ui.Pre_Aviso_IndemnizacionesUI;
import com.indemnizacion.automation.utils.DataUtil;
import com.indemnizacion.automation.utils.ExcelReader;
import com.indemnizacion.automation.utils.FlowUtil;
import com.indemnizacion.automation.utils.PropertyManager;

public class Modelos extends Pre_Aviso_IndemnizacionesUI{

	public static Logger log=Logger.getLogger(Modelos.class);
	public static void clasificacion_definacion(String company,String num_pol,String clv_desc,String description,
			int valor_pretension,int seccion,int producto,
			int causa,int consequencia,int cobertura, int cliente_ries_val) 
	{
		ExcelReader ClasificacionExcel = DataUtil.clasificacion_excel;
		ExcelReader DefinicionExcel = DataUtil.definicion_excel;
		FlowUtil.movetoElement(page_footer);


		for(int i=2;i<ClasificacionExcel.getRowCount()+1;i++) {

			if(producto==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", i)) 
					&& causa==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",i)) 
					&& consequencia==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", i))
					&& cobertura==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", i))
					&& cliente_ries_val==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", i))
					&& (clv_desc.equalsIgnoreCase("0")
							|| clv_desc.equalsIgnoreCase("1")
							|| clv_desc.equalsIgnoreCase("2")
							|| clv_desc.equalsIgnoreCase("3")
							|| clv_desc.equalsIgnoreCase("4"))
					&& ClasificacionExcel.getCellData(DataUtil.inputSheetName, "Execution_Status", i).equalsIgnoreCase(""))
			{
				if(DataUtil.isElementEnabled(modelo_riesgo)) {
					//System.out.println("modelo riesgo is enabled");
				}
				else {
					if(valor_pretension>10000000) {					
						String mod_ries_selected=FlowUtil.getFirstSelectedOption(modelo_riesgo);
						int mod_ries_code=Integer.parseInt(mod_ries_selected.split(" - ")[0]);//mod riesgo code
						if(mod_ries_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", i))) 
						{
							if(DataUtil.isElementEnabled(resultado_encuesta)) {
								int encuesta_count=Integer.parseInt(PropertyManager.getConfigValueByKey("encuestaValuesCount"));
								String encuesta_values=  PropertyManager.getConfigValueByKey("resultadoEncuesta");
								for(int j=0;j<encuesta_count;j++) {
									int encuesta_code=Integer.parseInt(encuesta_values.split(",")[j].split(" - ")[0]);
									//FlowUtil.movetoElementandClick(resultado_encuesta);
									FlowUtil.selectByVisibleText(resultado_encuesta, encuesta_values.split(",")[j]);
									FlowUtil.waitTillInivisibility(cargando);

									if(DataUtil.isElementEnabled(clasificacion)) {
										
										for(int p=2;p<DefinicionExcel.getRowCount()+1;p++ ) {

											if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"PRODUCTO",p)) 
													&& causa==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",p)) 
													&& consequencia==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", p))
													&& cobertura==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", p))
													&& (clv_desc.equalsIgnoreCase("0")
															|| clv_desc.equalsIgnoreCase("1")
															|| clv_desc.equalsIgnoreCase("2")
															|| clv_desc.equalsIgnoreCase("3")
															|| clv_desc.equalsIgnoreCase("4"))
													&& cliente_ries_val==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", p))
													&& mod_ries_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", p))
													&& encuesta_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", p))
													&& DefinicionExcel.getCellData(DataUtil.inputSheetName, "Execution_Status", p).equalsIgnoreCase("")

													) {
												DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", p, "Executed");
												String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code);
												String clas_response=DataUtil.clasificacion_response("");
												String def_resuest=DataUtil.def_stringtoJson("","");
												String def_response=DataUtil.definicion_response("");
												if(DataUtil.dbInsertion) {
													ConnectionDB.Konnection();
													ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail","Clasificacion Not Found",DataUtil.sysDate());
													ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Fail","Clasificacion Not Found",DataUtil.sysDate());

												}
												

												int clasificacion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CLASIFICACION CASO CODE", p));
												int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", p));
												int evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", p));


												
												DataUtil.outputDefinicionExcel.setDefinicionCellValue(p,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion,evidencia,definicion,"Fail","Clasificacion Not Found"));
											}
										}
										int encuesta=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
										int clasificacion=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));
										ClasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i, "Executed");
										DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta,clasificacion,"Fail","Clasificacion Not Found"));
									}
									else {
										String clasificacion_selected=FlowUtil.getFirstSelectedOption(clasificacion);
										int evidencia_count=Integer.parseInt(PropertyManager.getConfigValueByKey("evidenciaValuesCount"));
										int clasificacion_code=Integer.parseInt(clasificacion_selected.split(" - ")[0]);//claificacion code

										for(int p=2;p<ClasificacionExcel.getRowCount()+1;p++) {
											if(producto==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", p)) 
													&& causa==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",p)) 
													&& consequencia==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", p))
													&& cobertura==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", p))
													&& (clv_desc.equalsIgnoreCase("0")
															|| clv_desc.equalsIgnoreCase("1")
															|| clv_desc.equalsIgnoreCase("2")
															|| clv_desc.equalsIgnoreCase("3")
															|| clv_desc.equalsIgnoreCase("4"))
													&& cliente_ries_val==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", p))
													&& mod_ries_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", p))
													&& encuesta_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", p))
													&&  ClasificacionExcel.getCellData(DataUtil.inputSheetName, "Execution_Status", p).equalsIgnoreCase(""))
											{

												if(clasificacion_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", p))) 
												{

													String evidencia_values=  PropertyManager.getConfigValueByKey("resultadoEvidencia");
													for(int k=0;k<evidencia_count;k++) {
														int evidencia_code=Integer.parseInt(evidencia_values.split(",")[k].split(" - ")[0]);
														//FlowUtil.movetoElementandClick(resultado_evidencia);
														FlowUtil.selectByVisibleText(resultado_evidencia,evidencia_values.split(",")[k]);
														FlowUtil.waitTillInivisibility(cargando);

														if(DataUtil.isElementEnabled(definicion)) {

															for(int m=2;m<DefinicionExcel.getRowCount()+1;m++ ) {

																if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"PRODUCTO",m)) 
																		&& causa==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CAUSAS",m)) 
																		&& consequencia==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CONSECUENCIA",m))
																		&& cobertura==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"COBERTURAS",m))
																		&& (clv_desc.equalsIgnoreCase("0")
																				|| clv_desc.equalsIgnoreCase("1")
																				|| clv_desc.equalsIgnoreCase("2")
																				|| clv_desc.equalsIgnoreCase("3")
																				|| clv_desc.equalsIgnoreCase("4"))
																		&& cliente_ries_val==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"SCORE RIESGO",m))
																		&& mod_ries_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"MODELO RIESGO PRETENSION",m))
																		&& encuesta_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO ENCUESTA",m))
																		&& clasificacion_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CLASIFICACION CASO CODE",m))
																		&& evidencia_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO EVIDENCIA",m))
																		&& DefinicionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",m).equalsIgnoreCase("")
																		) {

																	DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
																	ClasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i, "Executed");
																	String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code);
																	String clas_response=DataUtil.clasificacion_response(clasificacion_selected);
																	String def_resuest=DataUtil.def_stringtoJson(evidencia_code,clasificacion_code);
																	String def_response=DataUtil.definicion_response("");
																	if(DataUtil.dbInsertion) {
																		ConnectionDB.Konnection();
																		ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Pass","",DataUtil.sysDate());
																		ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Fail","Definicion Not Found",DataUtil.sysDate());

																	}
																	
																	int clasificacion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CLASIFICACION CASO CODE", m));
																	int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", m));


																	DataUtil.outputDefinicionExcel.setDefinicionCellValue(m,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion,evidencia_code,definicion,"Fail","Definicion Not Found"));




																}
															}
															int encuesta=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
															int clasificacion=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));
															ClasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i, "Executed");
															DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta,clasificacion,"Pass",""));

														}
														else {//definicion disabled with set value
															String definicion_selected=FlowUtil.getFirstSelectedOption(definicion);

															int definicion_code=Integer.parseInt(definicion_selected.split(" - ")[0]);//definicion code

															for(int m=2;m<ClasificacionExcel.getRowCount()+1;m++) {
																if(producto==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", m)) 
																		&& causa==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",m)) 
																		&& consequencia==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", m))
																		&& cobertura==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", m))
																		&& (clv_desc.equalsIgnoreCase("0")
																				|| clv_desc.equalsIgnoreCase("1")
																				|| clv_desc.equalsIgnoreCase("2")
																				|| clv_desc.equalsIgnoreCase("3")
																				|| clv_desc.equalsIgnoreCase("4"))
																		&& cliente_ries_val==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", m))
																		&& mod_ries_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", m))
																		&& encuesta_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", m))
																		&& clasificacion_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR",m ))
																		&&  ClasificacionExcel.getCellData(DataUtil.inputSheetName, "Execution_Status", m).equalsIgnoreCase(""))
																{
																	ClasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
																	String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code);
																	String clas_response=DataUtil.clasificacion_response(clasificacion_selected);
																	if(DataUtil.dbInsertion) {
																		ConnectionDB.Konnection();
																		ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Pass","",DataUtil.sysDate());
																	}
																	DataUtil.outputClasificacionExcel.setClasificacionCellValue(m,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion_code,"Pass",""));
																}
															}

															for(int m=2;m<DefinicionExcel.getRowCount()+1;m++) {
																if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"PRODUCTO",m)) 
																		&& causa==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CAUSAS",m)) 
																		&& consequencia==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CONSECUENCIA",m))
																		&& cobertura==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"COBERTURAS",m))
																		&& (clv_desc.equalsIgnoreCase("0")
																				|| clv_desc.equalsIgnoreCase("1")
																				|| clv_desc.equalsIgnoreCase("2")
																				|| clv_desc.equalsIgnoreCase("3")
																				|| clv_desc.equalsIgnoreCase("4"))
																		&& cliente_ries_val==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"SCORE RIESGO",m))
																		&& mod_ries_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"MODELO RIESGO PRETENSION",m))
																		&& encuesta_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO ENCUESTA",m))
																		&& clasificacion_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CLASIFICACION CASO CODE",m))
																		&& evidencia_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO EVIDENCIA",m))
																		&& DefinicionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",m).equalsIgnoreCase("")
																		) {

																	if(definicion_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", m))) {

																		DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
																		
																		String def_resuest=DataUtil.def_stringtoJson(evidencia_code,definicion_code);
																		String def_response=DataUtil.definicion_response(definicion_selected);
																		if(DataUtil.dbInsertion) {
																			
																			ConnectionDB.Konnection();
																			ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Pass","",DataUtil.sysDate());
																				
																		}
																		DataUtil.outputDefinicionExcel.setDefinicionCellValue(m,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion_code,evidencia_code,definicion_code,"Pass",""));
																	}
																	else {
																		DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
																		String def_resuest=DataUtil.def_stringtoJson(evidencia_code,definicion_code);
																		String def_response=DataUtil.definicion_response(definicion_selected);
																		if(DataUtil.dbInsertion) {
																			ConnectionDB.Konnection();
																			ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Fail","Definicion Not Found",DataUtil.sysDate());
																			
																		}
																		
																		
																		
																		DataUtil.outputDefinicionExcel.setDefinicionCellValue(m,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion_code,evidencia_code,definicion_code,"Fail","Definicion Not Found"));

																	}


																}
															}

														}


													}


												}
												else {

													for(int m=2;m<DefinicionExcel.getRowCount()+1;m++) {
														if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"PRODUCTO",m)) 
																&& causa==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CAUSAS",m)) 
																&& consequencia==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CONSECUENCIA",m))
																&& cobertura==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"COBERTURAS",m))
																&& (clv_desc.equalsIgnoreCase("0")
																		|| clv_desc.equalsIgnoreCase("1")
																		|| clv_desc.equalsIgnoreCase("2")
																		|| clv_desc.equalsIgnoreCase("3")
																		|| clv_desc.equalsIgnoreCase("4"))
																&& cliente_ries_val==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"SCORE RIESGO",m))
																&& mod_ries_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"MODELO RIESGO PRETENSION",m))
																&& encuesta_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO ENCUESTA",m))
																&& DefinicionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",m).equalsIgnoreCase("")
																)
														{
															DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
															String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code);
															String clas_response=DataUtil.clasificacion_response(clasificacion_selected);
															String def_resuest=DataUtil.def_stringtoJson("","");
															String def_response=DataUtil.definicion_response("");

															int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", m));
															int evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", m));
															if(DataUtil.dbInsertion) {
																ConnectionDB.Konnection();
																ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail","Clasificacion Not Found",DataUtil.sysDate());
																ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Fail","Definicion Not Found",DataUtil.sysDate());
																
															}
															
															DataUtil.outputDefinicionExcel.setDefinicionCellValue(m,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion_code,evidencia,definicion,"Fail","Definicion Not Found"));



														}


													}
													int encuesta=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", p));
													int clasificacion=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", p));
													ClasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", p, "Executed");
													DataUtil.outputClasificacionExcel.setClasificacionCellValue(p,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta,clasificacion,"Fail","Clasificacion Not Found"));

												}

											}

										}
									}
								}
							}
							else {

							}
						}


					}//valor pretension < 10 mil	
					else {
						String mod_ries_selected=FlowUtil.getFirstSelectedOption(modelo_riesgo);
						int mod_ries_code=Integer.parseInt(mod_ries_selected.split(" - ")[0]);//mod riesgo code
						if(mod_ries_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", i))
								) {
							if(DataUtil.isElementEnabled(resultado_encuesta)) {
								int encuesta_count=Integer.parseInt(PropertyManager.getConfigValueByKey("encuestaValuesCount"));
								String encuesta_values=  PropertyManager.getConfigValueByKey("resultadoEncuesta");
								for(int j=0;j<encuesta_count;j++) {
									int encuesta_code=Integer.parseInt(encuesta_values.split(",")[j].split(" - ")[0]);
									//FlowUtil.movetoElementandClick(resultado_encuesta);
									FlowUtil.selectByVisibleText(resultado_encuesta, encuesta_values.split(",")[j]);
									FlowUtil.waitTillInivisibility(cargando);

									if(DataUtil.isElementEnabled(clasificacion)) {
										//String data="Clasificacion Not Found";
										
										for(int p=2;p<DefinicionExcel.getRowCount()+1;p++ ) {

											if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"PRODUCTO",p)) 
													&& causa==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",p)) 
													&& consequencia==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", p))
													&& cobertura==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", p))
													&& (clv_desc.equalsIgnoreCase("0")
															|| clv_desc.equalsIgnoreCase("1")
															|| clv_desc.equalsIgnoreCase("2")
															|| clv_desc.equalsIgnoreCase("3")
															|| clv_desc.equalsIgnoreCase("4"))
													&& cliente_ries_val==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", p))
													&& mod_ries_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", p))
													&& encuesta_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", p))
													&& DefinicionExcel.getCellData(DataUtil.inputSheetName, "Execution_Status", p).equalsIgnoreCase("")

													) {
												DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", p, "Executed");
												String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code);
												String clas_response=DataUtil.clasificacion_response("");
												String def_resuest=DataUtil.def_stringtoJson("","");
												String def_response=DataUtil.definicion_response("");
												if(DataUtil.dbInsertion) {
													ConnectionDB.Konnection();
													ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail","Clasificacion Not Found",DataUtil.sysDate());
													ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Fail","Clasificacion Not Found",DataUtil.sysDate());

												}
												

												int clasificacion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CLASIFICACION CASO CODE", p));
												int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", p));
												int evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", p));


												
												DataUtil.outputDefinicionExcel.setDefinicionCellValue(p,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion,evidencia,definicion,"Fail","Clasificacion Not Found"));
											}
										}
										int encuesta=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
										int clasificacion_code=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));
										ClasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i, "Executed");
										DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta,clasificacion_code,"Fail","Clasificacion Not Found"));
									}
									else {// clasificacion with set value
										String claificacion_selected=FlowUtil.getFirstSelectedOption(clasificacion);
										

										int clasificacion_code=Integer.parseInt(claificacion_selected.split(" - ")[0]);//claificacion code

										for(int p=2;p<ClasificacionExcel.getRowCount()+1;p++) {
											if(producto==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", p)) 
													&& causa==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",p)) 
													&& consequencia==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", p))
													&& cobertura==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", p))
													&& (clv_desc.equalsIgnoreCase("0")
															|| clv_desc.equalsIgnoreCase("1")
															|| clv_desc.equalsIgnoreCase("2")
															|| clv_desc.equalsIgnoreCase("3")
															|| clv_desc.equalsIgnoreCase("4"))
													&& cliente_ries_val==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", p))
													&& mod_ries_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", p))
													&& encuesta_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", p))
													&&  ClasificacionExcel.getCellData(DataUtil.inputSheetName, "Execution_Status", p).equalsIgnoreCase(""))
											{
												if(clasificacion_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", p))) 
												{

													int evidencia_count=Integer.parseInt(PropertyManager.getConfigValueByKey("evidenciaValuesCount"));
													String evidencia_values=  PropertyManager.getConfigValueByKey("resultadoEvidencia");
													for(int k=0;k<evidencia_count;k++) {
														int evidencia_code=Integer.parseInt(evidencia_values.split(",")[k].split(" - ")[0]);
														//FlowUtil.movetoElementandClick(resultado_evidencia);
														FlowUtil.selectByVisibleText(resultado_evidencia,evidencia_values.split(",")[k]);
														FlowUtil.waitTillInivisibility(cargando);

														if(DataUtil.isElementEnabled(definicion)) {
															for(int m=2;m<DefinicionExcel.getRowCount()+1;m++ ) {

																if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"PRODUCTO",m)) 
																		&& causa==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CAUSAS",m)) 
																		&& consequencia==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CONSECUENCIA",m))
																		&& cobertura==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"COBERTURAS",m))
																		&& (clv_desc.equalsIgnoreCase("0")
																				|| clv_desc.equalsIgnoreCase("1")
																				|| clv_desc.equalsIgnoreCase("2")
																				|| clv_desc.equalsIgnoreCase("3")
																				|| clv_desc.equalsIgnoreCase("4"))
																		&& cliente_ries_val==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"SCORE RIESGO",m))
																		&& mod_ries_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"MODELO RIESGO PRETENSION",m))
																		&& encuesta_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO ENCUESTA",m))
																		&& clasificacion_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CLASIFICACION CASO CODE",m))
																		&& evidencia_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO EVIDENCIA",m))
																		&& DefinicionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",m).equalsIgnoreCase("")
																		) {

																	DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
																	
																	String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code);
																	String clas_response=DataUtil.clasificacion_response(claificacion_selected);
																	String def_resuest=DataUtil.def_stringtoJson(evidencia_code,clasificacion_code);
																	String def_response=DataUtil.definicion_response("");
																	if(DataUtil.dbInsertion) {
																		ConnectionDB.Konnection();
																		ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Pass","",DataUtil.sysDate());
																		ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Fail","Definicion Not Found",DataUtil.sysDate());

																	}
																	
																	int clasificacion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "CLASIFICACION CASO CODE", m));
																	int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", m));


																	
																	DataUtil.outputDefinicionExcel.setDefinicionCellValue(m,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion,evidencia_code,definicion,"Fail","Definicion Not Found"));
																}
															}
															int encuesta=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", i));
															int clasificacion=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", i));
															ClasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", i, "Executed");
															DataUtil.outputClasificacionExcel.setClasificacionCellValue(i,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta,clasificacion,"Pass",""));

														}
														else {//definicion disabled with set value
															String definicion_selected=FlowUtil.getFirstSelectedOption(definicion);

															int definicion_code=Integer.parseInt(definicion_selected.split(" - ")[0]);//definicion code

															for(int m=2;m<ClasificacionExcel.getRowCount()+1;m++) {
																if(producto==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "PRODUCTO", m)) 
																		&& causa==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CAUSAS",m)) 
																		&& consequencia==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "CONSECUENCIA", m))
																		&& cobertura==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "COBERTURAS", m))
																		&& (clv_desc.equalsIgnoreCase("0")
																				|| clv_desc.equalsIgnoreCase("1")
																				|| clv_desc.equalsIgnoreCase("2")
																				|| clv_desc.equalsIgnoreCase("3")
																				|| clv_desc.equalsIgnoreCase("4"))
																		&& cliente_ries_val==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SCORE RIESGO", m))
																		&& mod_ries_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "MODELO RIESGO PRETENSION", m))
																		&& encuesta_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", m))
																		&& clasificacion_code==DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", m))
																		&&  ClasificacionExcel.getCellData(DataUtil.inputSheetName, "Execution_Status", m).equalsIgnoreCase(""))
																{
																	ClasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
																	String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code);
																	String clas_response=DataUtil.clasificacion_response(claificacion_selected);
																	if(DataUtil.dbInsertion) {
																		ConnectionDB.Konnection();
																		ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Pass","",DataUtil.sysDate());
																	}
																	DataUtil.outputClasificacionExcel.setClasificacionCellValue(m,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion_code,"Pass",""));
																}


															}

															for(int m=2;m<DefinicionExcel.getRowCount()+1;m++) {
																if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"PRODUCTO",m)) 
																		&& causa==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CAUSAS",m)) 
																		&& consequencia==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CONSECUENCIA",m))
																		&& cobertura==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"COBERTURAS",m))
																		&& (clv_desc.equalsIgnoreCase("0")
																				|| clv_desc.equalsIgnoreCase("1")
																				|| clv_desc.equalsIgnoreCase("2")
																				|| clv_desc.equalsIgnoreCase("3")
																				|| clv_desc.equalsIgnoreCase("4"))
																		&& cliente_ries_val==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"SCORE RIESGO",m))
																		&& mod_ries_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"MODELO RIESGO PRETENSION",m))
																		&& encuesta_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO ENCUESTA",m))
																		&& clasificacion_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CLASIFICACION CASO CODE",m))
																		&& evidencia_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO EVIDENCIA",m))
																		&& DefinicionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",m).equalsIgnoreCase("")
																		) {

																	if(definicion_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", m))) {
																		DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
																		String def_resuest=DataUtil.def_stringtoJson(evidencia_code,definicion_code);
																		String def_response=DataUtil.definicion_response(definicion_selected);
																		if(DataUtil.dbInsertion) {
																			ConnectionDB.Konnection();
																			ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Pass","",DataUtil.sysDate());
																				
																		}
																		
																		
																		DataUtil.outputDefinicionExcel.setDefinicionCellValue(m,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion_code,evidencia_code,definicion_code,"Pass",""));

																	}
																	else {
																		DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
																		String def_resuest=DataUtil.def_stringtoJson(evidencia_code,definicion_code);
																		String def_response=DataUtil.definicion_response(definicion_selected);
																		if(DataUtil.dbInsertion) {
																			ConnectionDB.Konnection();
																			ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Fail","Definicion not Found",DataUtil.sysDate());
																				
																		}
																		
																		DataUtil.outputDefinicionExcel.setDefinicionCellValue(m,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion_code,evidencia_code,definicion_code,"Fail","Definicion not Found"));
																	}

																}


															}

														}


													}

												
												}
												else {
													for(int m=2;m<DefinicionExcel.getRowCount()+1;m++) {
														if(producto==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"PRODUCTO",m)) 
																&& causa==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CAUSAS",m)) 
																&& consequencia==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"CONSECUENCIA",m))
																&& cobertura==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"COBERTURAS",m))
																&& (clv_desc.equalsIgnoreCase("0")
																		|| clv_desc.equalsIgnoreCase("1")
																		|| clv_desc.equalsIgnoreCase("2")
																		|| clv_desc.equalsIgnoreCase("3")
																		|| clv_desc.equalsIgnoreCase("4"))
																&& cliente_ries_val==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"SCORE RIESGO",m))
																&& mod_ries_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"MODELO RIESGO PRETENSION",m))
																&& encuesta_code==DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName,"RESULTADO ENCUESTA",m))
																&& DefinicionExcel.getCellData(DataUtil.inputSheetName,"Execution_Status",m).equalsIgnoreCase("")
																)
														{
															
															DefinicionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", m, "Executed");
															String clas_resuest=DataUtil.clas_stringtoJson(seccion,producto,num_pol,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code);
															String clas_response=DataUtil.clasificacion_response(claificacion_selected);
															String def_resuest=DataUtil.def_stringtoJson("","");
															String def_response=DataUtil.definicion_response("");

															int definicion=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", m));
															int evidencia=DataUtil.getCode(DefinicionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO EVIDENCIA", m));
															
															if(DataUtil.dbInsertion) {
																ConnectionDB.Konnection();
																ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_clasficiacion"),clas_resuest,clas_response,"Fail","Clasificacion Not Found",DataUtil.sysDate());
																ConnectionDB.InsertAutExecutionInicial(PropertyManager.getConfigValueByKey("req_type_definicion"),def_resuest,def_response,"Fail","Definicion Not Found",DataUtil.sysDate());
																
															}
															
															DataUtil.outputDefinicionExcel.setDefinicionCellValue(m,DataUtil.definicionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta_code,clasificacion_code,evidencia,definicion,"Fail","Definicion Not Found"));



														}


													}
													int encuesta=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "RESULTADO ENCUESTA", p));
													int clasificacion=DataUtil.getCode(ClasificacionExcel.getCellData(DataUtil.inputSheetName, "SALIDA MOTOR", p));
													ClasificacionExcel.setCellData(DataUtil.inputSheetName, "Execution_Status", p, "Executed");
													DataUtil.outputClasificacionExcel.setClasificacionCellValue(p,DataUtil.clasificacionJsonObject(DataUtil.company,seccion,producto,clv_desc,causa,consequencia,cobertura,mod_ries_code,cliente_ries_val,encuesta,clasificacion,"Fail","Clasificacion Not Found"));
													
													
												}
												
											}
											else {
											}
											
										}
									}
								}
							}
							else {

							}
						}
						else {
						}


					}


				}
			}
			else {
			}

		}
	}

}

