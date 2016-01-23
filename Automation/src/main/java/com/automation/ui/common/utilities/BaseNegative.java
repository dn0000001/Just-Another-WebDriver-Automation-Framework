package com.automation.ui.common.utilities;

import java.util.List;

import com.automation.ui.common.exceptions.NoErrorOccurredException;

/**
 * This is an abstract class for testing negative functionality
 */
public abstract class BaseNegative {
	/**
	 * Runs all negative tests and verifies the results
	 */
	public void runAllNegativeTests()
	{
		Logs.logSection("Running all negative tests (" + getFunctionalityName() + ") ...");

		// Get the list of exceptions to test
		List<RuntimeException> exceptions = getExceptions();
		for (RuntimeException expected : exceptions)
		{
			// Extract the class name without package parts
			String sExpectedException;
			try
			{
				String[] pieces = expected.getClass().getName().split("\\.");
				sExpectedException = pieces[pieces.length - 1];
			}
			catch (Exception ex)
			{
				// Fall back to full name if any exception occurs
				sExpectedException = expected.getClass().getName();
			}

			try
			{
				// Run the corresponding test to generate the exception
				executeNegativeTest(expected);

				// An exception must always occur as we are doing negative testing
				throw new NoErrorOccurredException("No exception occurred but expected ("
						+ sExpectedException + ")");
			}
			catch (RuntimeException actual)
			{
				Verify.exception(actual, expected);
			}

			Logs.log.info(Misc.repeat("*", 40));
			Logs.log.info("");
		}

		Logs.log.info("All Negative Tests (" + getFunctionalityName() + ") COMPLETE");
	}

	/**
	 * Get the name of the functionality being tested. This is used for logging only.
	 * 
	 * @return Functionality Name
	 */
	protected abstract String getFunctionalityName();

	/**
	 * Get a list of exceptions for negative testing. The abstract method <B>generateException</B> will be
	 * called for each exception in this list.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This controls the execution method of the negative tests.<BR>
	 * 
	 * @return List&lt;RuntimeException&gt;
	 */
	protected abstract List<RuntimeException> getExceptions();

	/**
	 * Execute the negative test to generate the specified exception. This method needs to handle all
	 * exceptions returned from the abstract method <B>getExceptions</B>.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be using <B>instanceof</B>.<BR>
	 * 2) If test data is needed, then in the Extending class add the variables as private and add appropriate
	 * getters/setters<BR>
	 * 3) This method should not handle the cases where no exceptions occur as the calling method
	 * <B>verifyNegativeFunctionality</B> verifies this.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * private String sInput;<BR>
	 * public String get(){&nbsp;&nbsp;return sInput;}<BR>
	 * public void set(String sInput){&nbsp;&nbsp;this.sInput = sInput;}<BR>
	 * protected void executeNegativeTest(RuntimeException runtime)<BR>
	 * {<BR>
	 * &nbsp;&nbsp;if(runtime instanceof SpecificException)<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;executeSpecificMethod(sInput)<BR>
	 * }<BR>
	 * 
	 * @param runtime - The exception to be generated
	 */
	protected abstract void executeNegativeTest(RuntimeException runtime);
}
