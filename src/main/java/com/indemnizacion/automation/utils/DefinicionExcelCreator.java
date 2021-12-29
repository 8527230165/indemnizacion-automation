package com.indemnizacion.automation.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.testng.log4testng.Logger;

public class DefinicionExcelCreator {
	
	
	public static Logger log=Logger.getLogger(DefinicionExcelCreator.class);
	public String path;
	public static Workbook workbook=null;
	public static Sheet sh=null;
	public static Row row=null;
	public Cell cell=null;
	public static FileOutputStream fileOut=null;

	public DefinicionExcelCreator(String path, String sheet) {
		this.path=path;
		workbook=new XSSFWorkbook();
		sh=workbook.createSheet(sheet);
		try {
			fileOut = new FileOutputStream(path);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		createCommonHeader();
		createDefinicionHeader();
		

	}
	
	public static void createCommonHeader() {
		row=sh.createRow(0);
		row.createCell(0).setCellValue("S.No");
		row.createCell(1).setCellValue("Seccion");
		row.createCell(2).setCellValue("Company");
		row.createCell(3).setCellValue("PRODUCTO");
		row.createCell(4).setCellValue("CLV");
		row.createCell(5).setCellValue("SCORE RIESGO");
		row.createCell(6).setCellValue("COBERTURAS");
		row.createCell(7).setCellValue("CAUSAS");
		row.createCell(8).setCellValue("CONSECUENCIA");
		row.createCell(9).setCellValue("MODELO RIESGO PRETENSION");
		row.createCell(10).setCellValue("RESULTADO ENCUESTA");
		try {
			workbook.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void createDefinicionHeader() {
		row.createCell(11).setCellValue("CLASIFICACION CASO CODE");
		row.createCell(12).setCellValue("RESULTADO EVIDENCIA");
		row.createCell(13).setCellValue("SALIDA MOTOR");
		row.createCell(14).setCellValue("Pass/Fail");
		row.createCell(15).setCellValue("Comment");
		try {
			workbook.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDefinicionCellValue(int rownum, JSONObject output) {

		row=sh.createRow(rownum-1);
		row.createCell(0).setCellValue(Integer.parseInt(String.valueOf((rownum-1))));
		row.createCell(1).setCellValue(Integer.parseInt(String.valueOf(output.get("Seccion"))));
		row.createCell(2).setCellValue(Integer.parseInt(String.valueOf(output.get("Company"))));
		row.createCell(3).setCellValue(Integer.parseInt(String.valueOf((output.get("Producto")))));
		row.createCell(4).setCellValue(Integer.parseInt(String.valueOf((output.get("CLV")))));
		row.createCell(5).setCellValue(Integer.parseInt(String.valueOf((output.get("SCORE RIESGO")))));
		row.createCell(6).setCellValue(Integer.parseInt(String.valueOf((output.get("Cobertura")))));
		row.createCell(7).setCellValue(Integer.parseInt(String.valueOf((output.get("Causa")))));
		row.createCell(8).setCellValue(Integer.parseInt(String.valueOf((output.get("Consequencia")))));
		row.createCell(9).setCellValue(Integer.parseInt(String.valueOf((output.get("MODELO RIESGO PRETENSION")))));
		row.createCell(10).setCellValue(Integer.parseInt(String.valueOf((output.get("Resultado Encuesta")))));
		row.createCell(11).setCellValue(Integer.parseInt(String.valueOf((output.get("CLASIFICACION CASO CODE")))));
		row.createCell(12).setCellValue(Integer.parseInt(String.valueOf((output.get("Resultado Evidencia")))));
		row.createCell(13).setCellValue(Integer.parseInt(String.valueOf((output.get("SALIDA MOTOR")))));
		row.createCell(14).setCellValue(String.valueOf((output.get("Pass/Fail"))));
		row.createCell(15).setCellValue(String.valueOf((output.get("Comment"))));

		try {
			fileOut = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			workbook.write(fileOut);
			fileOut.close();
			//workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
