package com.automation.ui.common.utilities;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * This class is for reading from an Excel file.
 */
public class Excel {
	/**
	 * Gets all the data from a specific Excel Worksheet
	 * 
	 * @param sExcelFile - Excel file to read from
	 * @param sWorkSheet - Excel Worksheet to read from
	 * @return null if any error
	 */
	public static String[][] getAllData(String sExcelFile, String sWorkSheet)
	{
		try
		{
			File inputWorkbook = new File(sExcelFile);
			Workbook w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(sWorkSheet);

			int rows = sheet.getRows();
			int cols = sheet.getColumns();
			String[][] data = new String[rows][cols];

			for (int i = 0; i < cols; i++)
			{
				for (int j = 0; j < rows; j++)
				{
					Cell cell = sheet.getCell(i, j);
					data[j][i] = cell.getContents().toString();
				}
			}

			return data;
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}