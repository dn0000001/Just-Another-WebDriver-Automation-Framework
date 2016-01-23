package com.automation.ui.common.sampleProject.tests;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.dataStructures.config.ConfigSQL;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.SQL_FileReader;

public class UnitTestSQL {
	@Test
	public static void unitTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();

		unitTestSQL_FileReader();

		Logs.log.info("");
	}

	/**
	 * Unit testing for the SQL_FileReader class
	 */
	public static void unitTestSQL_FileReader()
	{
		Logs.log.info("Unit Test SQL File Reader");

		SQL_FileReader reader = new SQL_FileReader();
		reader.setFile(ConfigSQL._SQL_Sample);
		reader.readFile();
		String sSQL = reader.getSQL();
		Logs.log.info(sSQL);

		Logs.log.info("unitTestSQL_FileReader pass");
		Logs.log.info("");
	}
}
