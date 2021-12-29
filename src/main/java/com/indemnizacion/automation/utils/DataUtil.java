package com.indemnizacion.automation.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.json.simple.JSONObject;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DataUtil {

	public static Logger log=Logger.getLogger(DataUtil.class);
	public static String clv_ui = "";
	public static String loginSheetName=PropertyManager.getConfigValueByKey("loginSheetName");
	public static String inputSheetName=PropertyManager.getConfigValueByKey("inputExcelSheetName");
	public static String company=PropertyManager.getConfigValueByKey("compania");
	public static String user=PropertyManager.getConfigValueByKey("user");
	public static String password=PropertyManager.getConfigValueByKey("password");
	public static boolean dbInsertion=Boolean.parseBoolean(PropertyManager.getConfigValueByKey("dbInsertion"));
	
	public static ExcelReader loginexcel=new ExcelReader(System.getProperty("user.dir")+File.separatorChar+PropertyManager.getConfigValueByKey("logindata_excelpath"));
	public static ExcelReader clasificacion_excel=new ExcelReader(System.getProperty("user.dir")+File.separatorChar+PropertyManager.getConfigValueByKey("clasificaciondata_excelpath"));
	public static ExcelReader definicion_excel=new ExcelReader(System.getProperty("user.dir")+File.separatorChar+PropertyManager.getConfigValueByKey("definaciondata_excelpath"));
	public static String user_id=loginexcel.getCellData(loginSheetName, 1, 2);
	public static String tipo_doc=loginexcel.getCellData(loginSheetName, 2, 2);
	public static String num_doc=loginexcel.getCellData(loginSheetName, 3, 2);
	public static String fecha_sini=loginexcel.getCellData(loginSheetName, 4, 2);
	public static String description=loginexcel.getCellData(loginSheetName, 5, 2);
	public static int valor_pretension=Integer.parseInt(loginexcel.getCellData(loginSheetName, 6, 2));
	
	
	
	
	public static String clasificacionPath=System.getProperty("user.dir")+File.separatorChar+PropertyManager.getConfigValueByKey("outputClasificacionFileName")+"_Output_"+DataUtil.localDateTime()+"_Clasificacion.xlsx";
	public static String definicionPath=System.getProperty("user.dir")+File.separatorChar+PropertyManager.getConfigValueByKey("outputDefinicionFileName")+"_Output_"+DataUtil.localDateTime()+"_Definicion.xlsx";
	public static String sheetname=PropertyManager.getConfigValueByKey("outputExcelSheet");
	public static ClasificacionExcelCreator outputClasificacionExcel=new ClasificacionExcelCreator(clasificacionPath, sheetname);
	public static DefinicionExcelCreator outputDefinicionExcel=new DefinicionExcelCreator(definicionPath, sheetname);
	
	public static void excelLoadingLog() {
		int clasificacionRecords=clasificacion_excel.getRowCount()-1;
		int definicionRecords=definicion_excel.getRowCount()-1;
		log.info("All Excel loaded");
		log.info("Clasificacion Data Excel Loaded with Total records:"+clasificacionRecords);
		log.info("Definicion Data Excel Loaded with Total records:"+definicionRecords);
	}
	
	
	public static boolean  isValuePresent(WebElement element,int value) {

		boolean staleElement = true; 
		boolean valuefound = false;
		while(staleElement){
		  try{
			  Select select=new Select(element);

				for(int i=1;i<select.getOptions().size();i++) {
					int ui_value=Integer.parseInt(select.getOptions().get(i).getText().split(" - ")[0]);

					if(ui_value==value) {
						valuefound=true;
					}
				}
		     staleElement = false;
		  } catch(StaleElementReferenceException e){
		    staleElement = true;
		  }
		}

		
		return valuefound;

	}

	public static boolean isElementEnabled(WebElement element) {
		WebElement ele=element;
		if(ele.isEnabled()) {
			return true;
		}
		else{
			return false;
		}

	}
	
	
	public static String clas_stringtoJson(int seccion, int product,String num_pol,String clv,Object causa, 
			Object consequencia, Object cobertura,Object mod_ries_val,
			Object reisgo_valor, Object resultado_encuesta 
			) {
		JSONObject obj=new JSONObject();
		obj.put("Seccion", seccion);
		obj.put("Poliza", num_pol);
		obj.put("Producto", product);
		obj.put("CLV", clv);
		obj.put("Causa", causa);
		obj.put("Consequencia", consequencia);
		obj.put("Cobertura", cobertura);
		obj.put("MODELO RIESGO PRETENSION", mod_ries_val);
		obj.put("SCORE RIESGO", reisgo_valor);
		obj.put("Resultado Encuesta", resultado_encuesta);
		
		return obj.toString();
	}
	
	public static JSONObject clasificacionJsonObject(String company,int seccion,int product,String clv,int causa, 
			int consequencia, int cobertura,int mod_ries_val,
			int reisgo_valor, int resultado_encuesta,int clasificacion_desc,String result,String comment 
			) {
		JSONObject obj=new JSONObject();
		obj.put("Seccion", seccion);
		obj.put("Company", company);
		obj.put("Producto", product);
		obj.put("CLV", clv);
		obj.put("MODELO RIESGO PRETENSION", mod_ries_val);
		obj.put("Cobertura", cobertura);
		obj.put("Causa", causa);
		obj.put("Consequencia", consequencia);
		obj.put("SCORE RIESGO", reisgo_valor);
		obj.put("Resultado Encuesta", resultado_encuesta);
		obj.put("SALIDA MOTOR", clasificacion_desc);
		obj.put("Pass/Fail", result);
		obj.put("Comment", comment);
		
		return obj;
	}
	
	public static JSONObject definicionJsonObject(String company, int seccion,int product,String clv,int causa, 
			int consequencia, int cobertura,int mod_ries_val,
			int reisgo_valor, int resultado_encuesta, 
			int clasificacion,Object resultado_evidencia,Object definicion,String result,String comment 
			) {
		JSONObject obj=new JSONObject();
		obj.put("Seccion", seccion);
		obj.put("Company", company);
		obj.put("Producto", product);
		obj.put("CLV", clv);
		obj.put("Causa", causa);
		obj.put("Consequencia", consequencia);
		obj.put("Cobertura", cobertura);
		obj.put("MODELO RIESGO PRETENSION", mod_ries_val);
		obj.put("SCORE RIESGO", reisgo_valor);
		obj.put("Resultado Encuesta", resultado_encuesta);
		obj.put("CLASIFICACION CASO CODE", clasificacion);
		obj.put("Resultado Evidencia", resultado_evidencia);
		obj.put("SALIDA MOTOR", definicion);
		obj.put("Pass/Fail", result);
		obj.put("Comment", comment);
		
		return obj;
	}
	public static String def_stringtoJson(Object resultado_evidencia, 
			Object clasificacion) {
		JSONObject obj=new JSONObject();
		obj.put("Resultado Evidencia", resultado_evidencia);
		obj.put("Clasificacion", clasificacion);
		
		return obj.toString();
	}
	
	public static String clasificacion_response(Object clasificacion) {
		//String str= "{Clasificacion:"+clasificacion+"}";
		JSONObject obj=new JSONObject();
		obj.put("Clasificacion", clasificacion);
		return obj.toString();
	}
	
	public static String definicion_response(Object definicion) {
		//String str= "{Definicion:"+definicion+"}";
		JSONObject obj=new JSONObject();
		obj.put("Definicion", definicion);
		return obj.toString();
	}
	
	
	public static String sysDate() {
		Calendar c= Calendar.getInstance();
		SimpleDateFormat dateformat = new SimpleDateFormat("dd_MMM_YY_hh_mm_ss");
		String date=dateformat.format(c.getTime());
		return date;
	}
	
	public static String localDateTime() {
		LocalDateTime d = LocalDateTime.now();
		DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
		String date=ft.format(d);
		return date;
	}
	
	
	

	public static int getCode(String value) {
		int val = 0;
			try {
				 val=Integer.parseInt(value.split("-")[0]);
				 return val;
			}
			catch(NumberFormatException e) {
				if(value.startsWith("-1")) {
					return -1;
				}
				else if(!value.startsWith("-1")){
					return val;
				}
				else {
					BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%p %d %c  - %m%n")));
					e.printStackTrace();
					log.info("Row contains Null value or Invalid value: "+e.getStackTrace()[0].getLineNumber());
					return 0;
				}
			}
	}

}
