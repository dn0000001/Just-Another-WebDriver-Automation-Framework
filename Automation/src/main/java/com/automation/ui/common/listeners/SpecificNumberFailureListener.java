package com.automation.ui.common.listeners;

import java.io.File;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.automation.ui.common.dataStructures.config.Listeners;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Misc;

/**
 * When using this Listener with TestNG, it will create a failure file when the threshold of total failures is
 * reached. The calling batch file can return a specific error code if the failure file exists which can be
 * used to fail the build with CI.
 */
public class SpecificNumberFailureListener extends TestListenerAdapter {
	// The failure file will be created if failure count greater than this
	private static int nFailureThreshold;

	// Counters for total failures
	private static int nTotalFailureCount;

	@Override
	public void onStart(ITestContext testContext)
	{
		// Get Failure Threshold from parameter
		nFailureThreshold = getFailureThreshold(testContext);

		// Get Failure Count stored in a System Property
		nTotalFailureCount = Conversion.parseInt(
				System.getProperty(Listeners.sPropertyName_nTotalFailureCount), 0);
	}

	@Override
	public void onFinish(ITestContext testContext)
	{
		/*
		 * I found the following issues:
		 * 1) For some unknown reason the static variables were not being retained between test runs (maybe
		 * the class is being garbage collected.)
		 * 
		 * To overcome these issues the following was needed:
		 * 1) Using System Properties to store the variables upon completion.
		 */
		if (!Misc.addProperty(Listeners.sPropertyName_nTotalFailureCount, String.valueOf(nTotalFailureCount)))
			System.setProperty(Listeners.sPropertyName_nTotalFailureCount, String.valueOf(nTotalFailureCount));
	}

	@Override
	public void onTestFailure(ITestResult tr)
	{
		/*
		 * I found the following issue:
		 * 1) TestNG does not have a running total of pass/fail/run
		 * 
		 * To overcome these issues the following was needed:
		 * 1) Increase count to failed & total tests when needed manually
		 */
		nTotalFailureCount++;
		check();
	}

	/**
	 * Check if the threshold is met and creates or deletes the failure file as necessary
	 */
	private static void check()
	{
		// Check if failure percentage greater than the threshold
		File file = new File(Listeners.sFailFilename);
		if (nTotalFailureCount > nFailureThreshold)
		{
			/*
			 * If file does not exist, then create it
			 */
			if (!file.exists())
			{
				try
				{
					file.createNewFile();
				}
				catch (Exception ex)
				{
				}
			}
		}
	}

	/**
	 * Get the Failure Threshold (passed as parameter. If not specified defaults to 0.)
	 * 
	 * @return
	 */
	private static int getFailureThreshold(ITestContext testContext)
	{
		String sFailureThreshold = testContext.getSuite().getParameter(
				Listeners.sParameterName_FailureThreshold);
		return Conversion.parseInt(sFailureThreshold, 0);
	}
}