package com.automation.ui.common.sampleProject.tests;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Logs;

/**
 * Example test class that does multiple threads with TestNG.
 */
public class MultipleParallelThreads {
	@DataProvider(name = "TestProvider")
	public static Object[][] dataForTest(Method m)
	{
		Object[][] testngDataObject = new Object[1][1];
		BasicTestContext btc = new BasicTestContext();
		btc.setBrowserRelated("IE", "C:\\_Libraries\\IEDriverServer.exe", "", "http://www.google.ca");
		testngDataObject[0][0] = btc;
		return testngDataObject;
	}

	@Test(dataProvider = "TestProvider")
	public static void something(BasicTestContext details)
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Logs.log.info("Start:  A1");
		Framework.get(details.getDriver(), details.getURL());

		for (int i = 0; i < 10; i++)
		{
			Framework.sleep(3000);
			Logs.log.info("A1:  output");
		}

		Logs.log.info("End:  A1");
	}

	@Test(dataProvider = "TestProvider")
	public static void something2(BasicTestContext details)
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Logs.log.info("Start:  A2");
		Framework.get(details.getDriver(), details.getURL());

		for (int i = 0; i < 10; i++)
		{
			Framework.sleep(2000);
			Logs.log.info("A2:  output");
		}

		Logs.log.info("End:  A2");
	}

	/**
	 * Used to close the browser after each method
	 * 
	 * @param details - Test Context
	 */
	@AfterMethod(alwaysRun = true)
	public static void quitBrowser(Object[] obj)
	{
		((BasicTestContext) obj[0]).quitBrowser();
	}
}
