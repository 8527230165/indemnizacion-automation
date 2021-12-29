package com.indemnizacion.automation.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	public XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	public ExcelReader(String path) {

		this.path = path;
		try {
			fis = new FileInputStream(path);
			
			workbook = new XSSFWorkbook(fis);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}



	// returns the row count in a sheet
	public int getRowCount(String... sheetName) {
		int index = workbook.getSheetIndex(sheetName.length > 0 ? sheetName[0] : "Data");
		if (index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}

	}

	// returns number of columns in a sheet
	public int getColumnCount(String sheetName) {

		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);

		if (row == null)
			return -1;

		return row.getLastCellNum();

	}

	// returns the data from a cell
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);

			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";

			if (cell.getCellType() == CellType.STRING) {
				String cellText = cell.getStringCellValue();
				try {
					SimpleDateFormat dateformat = new SimpleDateFormat("dd/mm/yyyy");
					cellText = dateformat.format(dateformat.parse(cellText));
				}
				catch(ParseException e) {
					return cellText;
				}

				return cellText;
			}
			else if (cell.getCellType() == CellType.NUMERIC) {
				String cellText=String.valueOf((int)cell.getNumericCellValue());
				
				if(DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
					cellText = dateformat.format(cell.getDateCellValue());
					return cellText;
				}
				
				return cellText;
			} else if (cell.getCellType() == CellType.BLANK)
				return "";
			else

				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}


	// returns the data from a cell based on col name
	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			int col_Num=-1;
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);

			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				if(row.getCell(i).getStringCellValue().toString().equals(colName.trim()))
					col_Num=i;

			}
			if(col_Num==-1)
				return "";


			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			if (row == null)
				return "";
			cell = row.getCell(col_Num);
			if (cell == null)
				return "";

			if (cell.getCellType() == CellType.STRING) {
				String cellText = cell.getStringCellValue();
				try {
					SimpleDateFormat dateformat = new SimpleDateFormat("dd/mm/yyyy");
					cellText = dateformat.format(dateformat.parse(cellText));
				}
				catch(ParseException e) {
					return cellText;
				}
				//System.out.println(cell.getCellType());
				return cellText;
			}

			else if (cell.getCellType() == CellType.NUMERIC) {
				String cellText=String.valueOf((int)cell.getNumericCellValue());

				if(DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
					cellText = dateformat.format(cell.getDateCellValue());
					return cellText;
				}
				//System.out.println(cell.getCellType());
				return cellText;
			} else if (cell.getCellType() == CellType.BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist  in xls";
		}
	}


	public void setCellData(String sheetName, String  colName, int rowNum,String data ) {


		try {
			int col_Num=-1;

			if (rowNum <= 0)
				System.out.println("row no existe");


			int index = workbook.getSheetIndex(sheetName);

			if (index == -1)
				System.out.println("sheet no existe");

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);

			for(int i=0;i<row.getLastCellNum();i++){

				if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num=i;
			}
			if(col_Num==-1)
				System.out.println("column no existe");

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);

			cell=row.createCell(col_Num);

			cell.setCellValue(data);

			fileOut = new FileOutputStream(path);

			workbook.write(fileOut);

			fileOut.close();	

		}
		catch(Exception e){
			e.printStackTrace();

		}
	}

}
