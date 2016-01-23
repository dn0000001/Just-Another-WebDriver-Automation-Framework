package com.automation.ui.common.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * This class is for reading from a CSV file.
 */
public class CSV {
	private CSVReader reader;
	private boolean bHeader;
	private char delimiter;

	/**
	 * Constructor - No variables initialized
	 */
	public CSV()
	{
	}

	/**
	 * Constructor loads the CSV file
	 * 
	 * @param sFile - File to load
	 * @param bHeader - Does file have header?
	 * @param delimiter - Delimiter for file
	 * @throws FileNotFoundException - File not found
	 */
	public CSV(String sFile, boolean bHeader, char delimiter) throws FileNotFoundException
	{
		reader = new CSVReader(new FileReader(sFile), delimiter);
		this.bHeader = bHeader;
		this.delimiter = delimiter;
	}

	/**
	 * This function returns all the data in the CSV file
	 * 
	 * @return - List of all rows in file
	 */
	public List<String[]> getAllData()
	{
		List<String[]> allData = new ArrayList<String[]>();
		try
		{
			String[] nextLine;

			// throw out the header
			if (bHeader)
				nextLine = reader.readNext();

			while ((nextLine = reader.readNext()) != null)
			{
				allData.add(nextLine);
			}
		}
		catch (Exception ex)
		{
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
			}
		}

		return allData;
	}

	/**
	 * This function returns all the data in the CSV file using getAllData as an
	 * array.<BR>
	 * <BR>
	 * Note: If the rows have different column lengths then the data returned
	 * can have less or more data based on the size of the 1st row. With more
	 * data some values will be null. It would be recommended to use getAllData
	 * in these cases.
	 * 
	 * @return - Array of String[][]
	 */
	public String[][] getAllDataAsArray()
	{
		List<String[]> allData = getAllData();
		if (allData.size() == 0)
			return null;

		// Assumes that all rows have the same number of columns
		int nExpectedColumns = allData.get(0).length;
		String[][] data = new String[allData.size()][nExpectedColumns];

		// Convert to String[][]
		int i = 0;
		for (String[] row : allData)
		{
			// Prevent exception should some rows have a greater number of
			// columns than expected
			int nActualColumns = row.length;
			int nColumns = Math.min(nExpectedColumns, nActualColumns);
			for (int j = 0; j < nColumns; j++)
			{
				data[i][j] = row[j];
			}
			i++;
		}

		return data;
	}

	/**
	 * Writes data to a file. <BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the file exists, then the contents are overwritten.<BR>
	 * 2) Each item in the list should contain each row to be written<BR>
	 * 
	 * @param sFile - File to write to
	 * @param dataToWrite - List of data to write to the file
	 */
	public void writeCSVwithResults(String sFile, List<String> dataToWrite)
	{
		try
		{
			CSVWriter writer = new CSVWriter(new FileWriter(sFile), delimiter);

			for (String enterLineOfData : dataToWrite)
			{
				String[] write = enterLineOfData.split(String.valueOf(delimiter));
				writer.writeNext(write);
			}

			writer.close();
		}
		catch (Exception ex)
		{
			Logs.log.error("Failure writing file ('" + sFile + "')");
		}
	}

	/**
	 * Get the header flag
	 * 
	 * @return bHeader
	 */
	public boolean getHeader()
	{
		return bHeader;
	}

	/**
	 * Set the header flag
	 * 
	 * @param value - true to indicate a header, else false
	 */
	public void setHeader(boolean value)
	{
		bHeader = value;
	}

	/**
	 * Get the delimiter
	 * 
	 * @return delimiter
	 */
	public char getDelimiter()
	{
		return delimiter;
	}

	/**
	 * Set the delimiter
	 * 
	 * @param value - Delimiter to use
	 */
	public void setDelimiter(char value)
	{
		delimiter = value;
	}

	/**
	 * Initializes the reader<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses the delimiter that was already set<BR>
	 * 
	 * @param sFile - File to load
	 * @throws FileNotFoundException
	 */
	public void setReader(String sFile) throws FileNotFoundException
	{
		reader = new CSVReader(new FileReader(sFile), delimiter);
	}
}
