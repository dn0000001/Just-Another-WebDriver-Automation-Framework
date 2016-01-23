package com.automation.ui.common.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.utilities.CSV;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Rand;

/**
 * This class hold the unit tests for the CSV class
 */
public class CSV_Test {
	private static final String _CSV_File = "csvTest.csv";

	/**
	 * Note: Tests are being executed in alphabetical order.
	 */
	@Test
	public static void unitTestA_WriteData()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();

		Logs.log.info("unitTestWriteData:  START");
		char delimiter = ',';
		String sHeader = "Salutation,First Name,Last Name,Country,Title,Mobile,Email or Fax,Company";
		String[] salutation = new String[] { "Mr", "Mrs" };
		String[] country = new String[] { "Canada", "United States" };

		int nRecords = 10;

		CSV csvFile = new CSV();
		csvFile.setDelimiter(delimiter);

		List<String> dataToWrite = new ArrayList<String>();
		dataToWrite.add(sHeader);

		int nEmails = 0;
		int nFaxes = 0;
		for (int i = 0; i < nRecords; i++)
		{
			// Random data for the row
			String sSalutation = salutation[Rand.randomRange(0, salutation.length - 1)];
			String sFirstName = Rand.letters(3);
			String sLastName = Rand.letters(6);
			String sCountry = country[Rand.randomRange(0, country.length - 1)];
			String sTitle = Rand.letters(5);
			String sMobile = Rand.numbers(10);
			String sCompany = Rand.alphanumeric(15);
			String sEmailOrFax;
			boolean bEmail = Rand.randomBoolean();
			if (bEmail)
			{
				sEmailOrFax = Rand.alphanumeric(5) + "@" + Rand.alphanumeric(10) + ".com";
				nEmails++;
			}
			else
			{
				sEmailOrFax = Rand.numbers(10);
				sCountry = "";
				nFaxes++;
			}

			// Construct the row
			String sRow = sSalutation + "," + sFirstName + "," + sLastName + "," + sCountry + "," + sTitle
					+ "," + sMobile + "," + sEmailOrFax + "," + sCompany;

			// Add row
			dataToWrite.add(sRow);
		}

		csvFile.writeCSVwithResults(_CSV_File, dataToWrite);
		Logs.log.info("Emails:  " + nEmails);
		Logs.log.info("Faxes:   " + nFaxes);

		Logs.log.info("unitTestWriteData:  END");
	}

	/**
	 * Method called by testNG to test getAllData
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public static void unitTestB_GetAllData() throws FileNotFoundException
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();

		Logs.log.info("getAllData:  START");
		CSV csvFile = null;
		boolean bHeader = true;
		char delimiter = ',';
		csvFile = new CSV(_CSV_File, bHeader, delimiter);

		List<String[]> allData = csvFile.getAllData();
		for (String[] row : allData)
		{
			String output = "";
			for (int i = 0; i < row.length; i++)
			{
				output += row[i] + "\t";
			}

			Logs.log.info(output);
		}

		Logs.log.info("getAllData:  END");
	}

	/**
	 * Method called by testNG to test getAllDataAsArray
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public static void unitTestB_GetAllDataAsArray() throws FileNotFoundException
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();

		Logs.log.info("getAllDataAsArray:  START");
		CSV csvFile = null;
		boolean bHeader = true;
		char delimiter = ',';
		csvFile = new CSV(_CSV_File, bHeader, delimiter);

		String[][] allData = csvFile.getAllDataAsArray();
		for (int i = 0; i < allData.length; i++)
		{
			String output = "";
			for (int j = 0; j < allData[i].length; j++)
			{
				output += allData[i][j] + "\t";
			}

			Logs.log.info(output);
		}

		Logs.log.info("getAllDataAsArray:  END");
	}

	/**
	 * Deletes the created csv file during unit testing
	 */
	@Test
	public static void unitTestZ_CleanUp()
	{
		File file = new File("csvTest.csv");
		file.delete();
	}
}
