package com.automation.ui.common.tests;

import java.lang.reflect.Field;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.config.ConfigJS;
import com.automation.ui.common.dataStructures.config.ConfigSQL;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.SQL_FileReader;
import com.automation.ui.common.utilities.TestResults;

/**
 * This class is for unit testing the configuration classes (ConfigJS & ConfigSQL)
 */
public class ConfigTest {
	@Test
	public static void runConfigJS_Test() throws Exception
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runConfigJS_Test");

		ConfigJS js = new ConfigJS();
		TestResults results = new TestResults();
		for (Field item : ConfigJS.class.getFields())
		{
			// Attempt to load all fields as files except 'baseFolder'
			if (!Compare.equals(item.getName(), "baseFolder", Comparison.EqualsIgnoreCase))
			{
				String file = String.valueOf(item.get(js));
				SQL_FileReader reader = new SQL_FileReader();
				reader.setFile(file);

				String info = "Successfully loaded JavaScript file:  " + file;
				String warn = "Could not load JavaScript file:  " + file;
				results.expectTrue(reader.readFile(), info, warn);
			}
		}

		results.verify("Some of the configured JavaScript files could not be loaded.  See above for details.");

		Controller.writeTestSuccessToLog("runConfigJS_Test");
	}

	@Test
	public static void runConfigSQLTest() throws Exception
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runConfigSQLTest");

		ConfigSQL sql = new ConfigSQL();
		TestResults results = new TestResults();
		for (Field item : ConfigSQL.class.getFields())
		{
			// Attempt to load all fields as files except 'baseFolder', 'placeholder' & 'uniqueTablePrefix'
			if (!Compare.equals(item.getName(), "baseFolder", Comparison.EqualsIgnoreCase)
					&& !Compare.equals(item.getName(), "placeholder", Comparison.EqualsIgnoreCase)
					&& !Compare.equals(item.getName(), "uniqueTablePrefix", Comparison.EqualsIgnoreCase))
			{
				String file = String.valueOf(item.get(sql));
				SQL_FileReader reader = new SQL_FileReader();
				reader.setFile(file);

				String info = "Successfully loaded SQL file:  " + file;
				String warn = "Could not load SQL file:  " + file;
				results.expectTrue(reader.readFile(), info, warn);
			}
		}

		results.verify("Some of the configured SQL files could not be loaded.  See above for details.");

		Controller.writeTestSuccessToLog("runConfigSQLTest");
	}
}
