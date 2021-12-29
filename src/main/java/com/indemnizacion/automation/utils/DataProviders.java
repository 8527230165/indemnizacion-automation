package com.indemnizacion.automation.utils;

import java.io.File;
import java.util.Hashtable;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	@DataProvider(name="SeccionProductData")
	public static Object[][] getData2() {
		ExcelReader excel=new ExcelReader(System.getProperty("user.dir")+File.separatorChar+PropertyManager.getConfigValueByKey("logindata_excelpath"));
		String sheetName=PropertyManager.getConfigValueByKey("seccionproductSheetName");
		
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);

		Object[][] data = new Object[rows-1][1];

		Hashtable<String,String> table=null;

		for(int rowNum=2; rowNum<=rows; rowNum++){
				table=new Hashtable<String,String>();

				for(int colNum=0; colNum<cols; colNum++){

					table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
					data[rowNum-2][0]=table;
				}
		}
		return data;
	}

}
