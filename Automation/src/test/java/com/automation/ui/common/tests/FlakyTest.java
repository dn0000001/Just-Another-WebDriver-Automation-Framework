package com.automation.ui.common.tests;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.AttemptTracker;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.FlakinessChecks;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.TestResults;

/**
 * This class is for unit testing the FlakinessChecks class
 */
public class FlakyTest {
	@Test
	public static void runReflectionTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runReflectionTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		AttemptTracker at = new AttemptTracker();
		at.setMaxAttempts(2);
		FlakinessChecks flaky = new FlakinessChecks(null, 1000, 100);

		bResult = flaky.isReflection(at, "isMaxAttempts");
		sWarning = "isMaxAttempts no parameters test failed";
		results.expectTrue(bResult, sWarning);

		at.resetAttempts();
		Object[] args = new Object[] { 3 };
		bResult = flaky.isReflection(at, "isMaxAttempts", args);
		sWarning = "isMaxAttempts with parameters test failed";
		results.expectTrue(bResult, sWarning);

		bResult = at.getAttempts() == 4;
		sWarning = "Attempts was incorrect";
		if (!results.expectTrue(bResult, sWarning))
			results.logWarn(4, at.getAttempts());

		at.resetAttempts();
		at.setMaxAttempts(1000);
		bResult = flaky.isReflection(at, "isMaxAttempts");
		sWarning = "max not reached test";
		results.expectFalse(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runReflectionTest");
	}
}
