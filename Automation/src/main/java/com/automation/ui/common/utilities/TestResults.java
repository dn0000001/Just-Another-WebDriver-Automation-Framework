package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import com.automation.ui.common.dataStructures.Parameter;

/**
 * This class stores the accumulated result of all tests. This can be useful if there are a series of tests
 * that need to be executed but you do not want to fail immediately but after all tests have been executed
 * based on count or percentage.
 */
public class TestResults {
	/**
	 * Flag indicating whether to store the warnings such that they can be logged all at the same time later
	 * or to log immediately in real time.
	 */
	private boolean _RealTimeLogging;

	/**
	 * Store the logs to be written later.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) param stores the log to be written<BR>
	 * 2) value stores the log type. If _INFO then uses <B>Logs.log.info</B> else uses <B>Logs.log.warn</B><BR>
	 */
	private List<Parameter> storedLogs;

	/**
	 * Indicates that stored log should be written using <B>Logs.log.info</B>
	 */
	private static final String _INFO = "info";

	private enum CalcMethod
	{
		Count, Percent
	}

	/**
	 * Which method is used to determine if execution of tests is considered a failure or pass
	 */
	private CalcMethod method;

	/**
	 * Total number tests executed
	 */
	private Count _TotalTests;

	/**
	 * Total number of passed tests executed
	 */
	private Count _PassedTests;

	/**
	 * Total number of failed tests executed
	 */
	private Count _FailedTests;

	/**
	 * The limit of failures before the test execution is considered a failure. This could be a specific count
	 * or percentage.
	 */
	private int _Limit;

	/**
	 * Default Constructor - Uses failure count method with limit 0 (no failures are allowed.) Also, logs are
	 * written immediately.
	 */
	public TestResults()
	{
		setWriteLogsInRealTime();
		resetStoredLogs();
		method = CalcMethod.Count;
		_Limit = 0;
		_TotalTests = new Count();
		_PassedTests = new Count();
		_FailedTests = new Count();
	}

	/**
	 * Sets the method used to determine if execution of tests is considered a failure or pass to be Count.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Using this method the test execution is considered a failure if the number of failures is greater
	 * than the specified acceptable limit. Example, if limit is set to 1 failure, then once 2 failures is
	 * reached the test execution is considered a failure. In this case, if there is only 1 failure, then the
	 * test execution would be considered pass.<BR>
	 */
	public void setMethod_Count()
	{
		method = CalcMethod.Count;
	}

	/**
	 * Sets the method used to determine if execution of tests is considered a failure or pass to be Percent<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Using this method the test execution is considered a failure if the percentage of failures is
	 * greater than the specified acceptable limit. Example, if the limit is set to 5% failure rate, then
	 * based on the number of tests executed only 5% of tests can be failed. If the number of tests executed
	 * was 100 and 5 failed, then this would be considered a pass.
	 */
	public void setMethod_Percent()
	{
		method = CalcMethod.Percent;
	}

	/**
	 * Set the flag to have the logs written in real time instead of later when calling the verify method.
	 */
	public void setWriteLogsInRealTime()
	{
		_RealTimeLogging = true;
	}

	/**
	 * Set the flag to store the logs to be written such that they can be written together when calling the
	 * verify method.
	 */
	public void setWriteLogsInFuture()
	{
		_RealTimeLogging = false;
	}

	/**
	 * Set the Failure "Limit" at which the test execution is considered a failure
	 * 
	 * @param _Limit - The number of failures or the percentage of failures
	 */
	public void setLimit(int _Limit)
	{
		this._Limit = _Limit;
	}

	/**
	 * Expect the result of the test to be false
	 * 
	 * @param bResult - Result of test
	 * @return true if passed tests increased else false
	 */
	public boolean expectFalse(boolean bResult)
	{
		return expectFalse(bResult, null, null);
	}

	/**
	 * Expect the result of the test to be true
	 * 
	 * @param bResult - Result of test
	 * @return true if passed tests increased else false
	 */
	public boolean expectTrue(boolean bResult)
	{
		return expectTrue(bResult, null, null);
	}

	/**
	 * Expect the result of the test to be false
	 * 
	 * @param bResult - Result of test
	 * @param sWarning - Warning to be logged if necessary
	 * @return true if passed tests increased else false
	 */
	public boolean expectFalse(boolean bResult, String sWarning)
	{
		return expectFalse(bResult, null, sWarning);
	}

	/**
	 * Expect the result of the test to be true
	 * 
	 * @param bResult - Result of test
	 * @param sWarning - Warning to be logged if necessary
	 * @return true if passed tests increased else false
	 */
	public boolean expectTrue(boolean bResult, String sWarning)
	{
		return expectTrue(bResult, null, sWarning);
	}

	/**
	 * Expect the result of the test to be false<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If logging variable is null, then no logging will occur if triggered<BR>
	 * 2) Based on real time logging flag, the logging may occur immediately or be stored & written later when
	 * calling the method verify<BR>
	 * 
	 * @param bResult - Result of test
	 * @param sInfo - Information to be logged for pass
	 * @param sWarning - Warning to be logged for failure
	 * @return true if passed tests increased else false
	 */
	public boolean expectFalse(boolean bResult, String sInfo, String sWarning)
	{
		_TotalTests.increment();
		if (bResult)
		{
			if (sWarning != null)
			{
				if (isRealTimeLogging())
					Logs.log.warn(sWarning);
				else
					storedLogs.add(new Parameter(sWarning, ""));
			}

			_FailedTests.increment();
			return false;
		}
		else
		{
			if (sInfo != null)
			{
				if (isRealTimeLogging())
					Logs.log.info(sInfo);
				else
					storedLogs.add(new Parameter(sInfo, _INFO));
			}

			_PassedTests.increment();
			return true;
		}
	}

	/**
	 * Expect the result of the test to be true<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If logging variable is null, then no logging will occur if triggered<BR>
	 * 2) Based on real time logging flag, the logging may occur immediately or be stored & written later when
	 * calling the method verify<BR>
	 * 
	 * @param bResult - Result of test
	 * @param sInfo - Information to be logged for pass
	 * @param sWarning - Warning to be logged for failure
	 * @return true if passed tests increased else false
	 */
	public boolean expectTrue(boolean bResult, String sInfo, String sWarning)
	{
		_TotalTests.increment();
		if (!bResult)
		{
			if (sWarning != null)
			{
				if (isRealTimeLogging())
					Logs.log.warn(sWarning);
				else
					storedLogs.add(new Parameter(sWarning, ""));
			}

			_FailedTests.increment();
			return false;
		}
		else
		{
			if (sInfo != null)
			{
				if (isRealTimeLogging())
					Logs.log.info(sInfo);
				else
					storedLogs.add(new Parameter(sInfo, _INFO));
			}

			_PassedTests.increment();
			return true;
		}
	}

	/**
	 * Verifies that the test execution was considered successful. If the test execution is considered a
	 * failure, then logs an error and throws exception.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Writes all stored logs based on flag (if not real time logging, then the stored logs are written)<BR>
	 * 2) If the stored logs are written, then the stored logs will be reset to the empty list<BR>
	 * 3) No logging for the overall success (but an overall failure will be written)<BR>
	 * 
	 * @param sError - Error to be logged if necessary
	 * @throws GenericUnexpectedException if the test execution was considered a failure
	 */
	public void verify(String sError)
	{
		if (!isRealTimeLogging())
			writeStoredLogs();

		if (isError())
			Logs.logError(sError);
	}

	/**
	 * Verifies that the test execution was considered successful<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Either a success or failure will be logged<BR>
	 * 2) Writes all stored logs based on flag (if not real time logging, then the stored logs are written)<BR>
	 * 3) If the stored logs are written, then the stored logs will be reset to the empty list<BR>
	 * 
	 * @param sSuccess - Success message to be logged if test execution considered a success
	 * @param sFailure - Failure message to be logged if test execution considered a failure
	 * @throws GenericUnexpectedException if the test execution was considered a failure
	 */
	public void verify(String sSuccess, String sFailure)
	{
		if (!isRealTimeLogging())
			writeStoredLogs();

		if (isError())
			Logs.logError(sFailure);
		else
			Logs.log.info(sSuccess);
	}

	/**
	 * Get the Pass Percentage<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The total percentage of Pass & Failure may not add to 100 due to rounding<BR>
	 * 
	 * @return 0 to 100
	 */
	public int getPassPercent()
	{
		if (_TotalTests.get() == 0)
			return 100;

		Float percent = Float.valueOf(_PassedTests.get()) / Float.valueOf(_TotalTests.get())
				* Float.valueOf("100");
		return percent.intValue();
	}

	/**
	 * Get the Failure Percentage<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The total percentage of Pass & Failure may not add to 100 due to rounding<BR>
	 * 
	 * @return 0 to 100
	 */
	public int getFailurePercent()
	{
		if (_TotalTests.get() == 0)
			return 0;

		Float percent = Float.valueOf(_FailedTests.get()) / Float.valueOf(_TotalTests.get())
				* Float.valueOf("100");
		return percent.intValue();
	}

	/**
	 * Get the number of failed tests
	 * 
	 * @return number of failed tests
	 */
	public int getFailureCount()
	{
		return _FailedTests.get();
	}

	/**
	 * Get the number of passed tests
	 * 
	 * @return number of passed tests
	 */
	public int getPassCount()
	{
		return _PassedTests.get();
	}

	/**
	 * Get the number of tests executed
	 * 
	 * @return number of tests executed
	 */
	public int getTotalCount()
	{
		return _TotalTests.get();
	}

	/**
	 * Is the test execution considered a failure
	 * 
	 * @return true if execution considered a failure else false
	 */
	public boolean isError()
	{
		if (method == CalcMethod.Percent)
		{
			if (getFailurePercent() > _Limit)
				return true;
			else
				return false;
		}
		else
		{
			if (getFailureCount() > _Limit)
				return true;
			else
				return false;
		}
	}

	/**
	 * Is the test execution considered a success
	 * 
	 * @return true if execution considered a success else false
	 */
	public boolean isSuccess()
	{
		return !isError();
	}

	/**
	 * Reset the counts to zero & resets the stored logs
	 */
	public void reset()
	{
		_TotalTests.set(0);
		_PassedTests.set(0);
		_FailedTests.set(0);
		resetStoredLogs();
	}

	/**
	 * Add log entry for info without a test
	 * 
	 * @param sMessage - Message to be logged
	 */
	public void logInfo(String sMessage)
	{
		if (isRealTimeLogging())
			Logs.log.info(sMessage);
		else
			storedLogs.add(new Parameter(sMessage, ""));
	}

	/**
	 * Add log entry for warning without a test
	 * 
	 * @param sMessage - Message to be logged
	 */
	public void logWarn(String sMessage)
	{
		if (isRealTimeLogging())
			Logs.log.warn(sMessage);
		else
			storedLogs.add(new Parameter(sMessage, ""));
	}

	/**
	 * Add log entries for debug information for 2 objects that do not match without a test<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The objects need to override the toString method for logging<BR>
	 * 
	 * @param expected - Object containing the Expected Results
	 * @param actual - Object containing the Actual Results
	 */
	public <T> void logWarn(T expected, T actual)
	{
		String sExpected = "Expected:  " + Conversion.nonNull(expected, "null");
		String sActual = "Actual:    " + Conversion.nonNull(actual, "null");

		if (isRealTimeLogging())
		{
			Logs.log.warn(sExpected);
			Logs.log.warn(sActual);
		}
		else
		{
			storedLogs.add(new Parameter(sExpected, ""));
			storedLogs.add(new Parameter(sActual, ""));
		}
	}

	/**
	 * Add the counts & stored logs from another object
	 * 
	 * @param se - Add Counts & Stored Logs from
	 */
	public void add(TestResults se)
	{
		_TotalTests.add(se.getTotalCount());
		_PassedTests.add(se.getPassCount());
		_FailedTests.add(se.getFailureCount());

		List<Parameter> transferLogs = se.getStoredLogs();
		for (int i = 0; i < transferLogs.size(); i++)
		{
			storedLogs.add(transferLogs.get(i));
		}
	}

	/**
	 * Returns the flag indicating if real time logging enabled or logging is done later when method verify is
	 * called.
	 * 
	 * @return true if real time logging else logs will be written later when method verify is called
	 */
	public boolean isRealTimeLogging()
	{
		return _RealTimeLogging;
	}

	/**
	 * Reset the stored logs to empty list
	 */
	public void resetStoredLogs()
	{
		storedLogs = new ArrayList<Parameter>();
	}

	/**
	 * Gets the Stored Logs
	 * 
	 * @return List&lt;Parameter&gt;
	 */
	public List<Parameter> getStoredLogs()
	{
		return Cloner.deepClone(storedLogs);
	}

	/**
	 * Writes the stored logs and resets the stored logs to the empty list
	 */
	public void writeStoredLogs()
	{
		for (int i = 0; i < storedLogs.size(); i++)
		{
			if (storedLogs.get(i).value.equals(_INFO))
				Logs.log.info(storedLogs.get(i).param);
			else
				Logs.log.warn(storedLogs.get(i).param);
		}

		// Reset the stored logs to empty list
		resetStoredLogs();
	}
}
