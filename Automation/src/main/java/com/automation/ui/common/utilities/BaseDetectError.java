package com.automation.ui.common.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.exceptions.GenericErrorDetectedException;
import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.exceptions.NoErrorOccurredException;

/**
 * This is an abstract class for detecting validation errors
 */
public abstract class BaseDetectError {
	private WebDriver driver;

	/**
	 * Max amount of time in milliseconds to wait for the validation error to occur
	 */
	private int maxWaitTime;

	/**
	 * The poll interval in milliseconds to check for the validation error
	 */
	private int poll;

	/**
	 * The language for the validation error message
	 */
	private Languages lang;

	/**
	 * The comparison option used to match the actual error to an expected error
	 */
	private Comparison matchOption;

	/**
	 * The cache of expected errors and corresponding runtime exceptions. This variable should only be
	 * accessed using the method getCache to prevent any issues with the cache.
	 */
	private HashMap<String, RuntimeException> cache;

	/**
	 * All the error locators<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only use the getter/setter to access variable to prevent any exceptions<BR>
	 */
	private List<String> errorLocators;

	/**
	 * Constructor - Sets all variables except error locators
	 * 
	 * @param driver
	 * @param maxWaitTime - Max amount of time in milliseconds to wait for the validation error to occur
	 * @param poll - The poll interval in milliseconds to check for the validation error
	 * @param lang - The language for the validation error message
	 * @param matchOption - Match Option
	 * @throws GenericUnexpectedException if the language is not set (Languages.KEY or null)
	 */
	public BaseDetectError(WebDriver driver, int maxWaitTime, int poll, Languages lang, Comparison matchOption)
	{
		if (lang == null || lang == Languages.KEY)
			throw new GenericUnexpectedException("The language was not set based on value:  " + lang);

		setDriver(driver);
		setMaxWaitTime(maxWaitTime);
		setPoll(poll);
		setLanguage(lang);
		setMatchOption(matchOption);
	}

	/**
	 * Constructor that sets the default max wait time & poll interval using the Framework values and assumes
	 * language is English & Comparison is equals ignoring case. Driver is set to null.
	 */
	public BaseDetectError()
	{
		this(null, Framework.getTimeoutInMilliseconds(), Framework.getPollInterval(), Languages.English,
				Comparison.EqualsIgnoreCase);
	}

	/**
	 * Constructor - Sets all variables except error locators
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 * @param matchOption - Match Option
	 * @throws GenericUnexpectedException if the language is not set (Languages.KEY or null)
	 */
	public BaseDetectError(Framework pageObject, Comparison matchOption)
	{
		this(pageObject.getDriver(), Framework.getTimeoutInMilliseconds(), Framework.getPollInterval(),
				pageObject.getLanguage(), matchOption);
	}

	/**
	 * @return the driver
	 */
	public WebDriver getDriver()
	{
		return driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}

	/**
	 * @return the maxWaitTime in milliseconds
	 */
	public int getMaxWaitTime()
	{
		return maxWaitTime;
	}

	/**
	 * @param maxWaitTime the maxWaitTime to set in milliseconds
	 */
	public void setMaxWaitTime(int maxWaitTime)
	{
		this.maxWaitTime = maxWaitTime;
	}

	/**
	 * @return the poll in milliseconds
	 */
	public int getPoll()
	{
		return poll;
	}

	/**
	 * @param poll the poll to set in milliseconds
	 */
	public void setPoll(int poll)
	{
		this.poll = poll;
	}

	/**
	 * @return the language
	 */
	public Languages getLanguage()
	{
		return lang;
	}

	/**
	 * @param lang the language to set
	 */
	public void setLanguage(Languages lang)
	{
		this.lang = lang;
	}

	/**
	 * @return the match option
	 */
	public Comparison getMatchOption()
	{
		return matchOption;
	}

	/**
	 * @param matchOption the match option to set
	 */
	public void setMatchOption(Comparison matchOption)
	{
		this.matchOption = matchOption;
	}

	/**
	 * Refresh the cache (using current language)
	 */
	public void refreshCache()
	{
		cache = getAvailableErrors(getLanguage());
	}

	/**
	 * Get the cache
	 * 
	 * @return HashMap&lt;String, RuntimeException&gt;
	 */
	protected HashMap<String, RuntimeException> getCache()
	{
		if (cache == null)
			cache = getAvailableErrors(getLanguage());

		return cache;
	}

	/**
	 * If an error is expected, then throws a runtime exception without logging. If not expecting an error,
	 * then only NoErrorOccurredException will allow execution to continue else the detected error is logged
	 * and thrown.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) In the case of not expecting an error and no error is detected, then this method will take
	 * <B>timeout</B> to complete as such user should only use method if an error is detected to have it
	 * logged or executing a negative test<BR>
	 * 
	 * @param expectError - true if expecting a validation error
	 */
	public void detectError(boolean expectError)
	{
		try
		{
			detectError();
		}
		catch (NoErrorOccurredException ex)
		{
			// If expecting an error, then just re-throw and caller will handle else this exception should
			// have occurred and no action is needed
			if (expectError)
				throw ex;
		}
		catch (RuntimeException ex)
		{
			// If expecting an error, then just re-throw and caller will handle
			if (expectError)
				throw ex;

			// Log error as no expecting an exception
			Logs.logError(ex);
		}
	}

	/**
	 * Throws exception corresponding to the detected validation error<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method will only throw a runtime exception. The user needs to catch the error and ensure the
	 * correct exception was thrown.<BR>
	 * 
	 * @throws NoErrorOccurredException if no validation error is detected
	 * @throws GenericErrorDetectedException if an unsupported validation error is detected
	 * @throws GenericUnexpectedException if issue throwing matching exception
	 */
	public void detectError()
	{
		// One of these locators will contain the validation error
		List<String> locators = getErrorLocators();

		// Wait for validation error to be displayed
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(getMaxWaitTime()))
		{
			// Find 1st locator that has non-null validation error message
			boolean errorFound = false;
			String actualError = null;
			for (String locator : locators)
			{
				actualError = getDisplayedError(locator);
				if (actualError != null)
				{
					errorFound = true;
					break;
				}
			}

			if (errorFound)
			{
				// Check error against all known errors and throw corresponding exception
				findMatchingException(actualError.trim(), getMatchOption(), getCache());

				String sError = "Unsupported error message was displayed:  " + actualError;
				throw new GenericErrorDetectedException(sError);
			}

			Framework.sleep(getPoll());
		}

		throw new NoErrorOccurredException("");
	}

	/**
	 * If an expected exception can be found using the actual error message, then throw the corresponding
	 * exception
	 * 
	 * @param actualError - The actual error to be found
	 * @param option - Comparison option
	 * @param lookup - Map of Expected Errors and corresponding Runtime exceptions to be thrown
	 */
	private void findMatchingException(String actualError, Comparison option,
			HashMap<String, RuntimeException> lookup)
	{
		// If lookup contains the error (exact error message), then we can throw the exception immediately and
		// get better performance
		if (lookup.containsKey(actualError))
			throwRuntimeException(lookup.get(actualError), actualError);

		String key = "";
		boolean found = false;
		for (Entry<String, RuntimeException> item : lookup.entrySet())
		{
			String expectedError = item.getKey();
			if (Comparison.Contains == option || Comparison.Lower == option || Comparison.Upper == option)
				found = Compare.contains(actualError, expectedError, option);
			else if (Comparison.DoesNotContain == option)
				found = !Compare.contains(actualError, expectedError, option);
			else if (Comparison.NotEqual == option)
				found = !Compare.equals(actualError, expectedError, option);
			else if (Comparison.RegEx == option)
				found = Compare.matches(actualError, expectedError);
			else
				found = Compare.equals(actualError, expectedError, option);

			if (found)
			{
				key = expectedError;
				break;
			}
		}

		if (found)
			throwRuntimeException(lookup.get(key), actualError);
	}

	/**
	 * Throw runtime exception with specified error
	 * 
	 * @param run - The Runtime Exception
	 * @param error - Message for the exception
	 * @throws RuntimeException runtime exception with the specified error
	 * @throws GenericUnexpectedException if exception instantiating the runtime exception
	 */
	private void throwRuntimeException(RuntimeException run, String error)
	{
		try
		{
			@SuppressWarnings("rawtypes")
			Constructor ctor = run.getClass().getDeclaredConstructor(String.class);
			throw (RuntimeException) ctor.newInstance(error);
		}
		catch (NoSuchMethodException ex)
		{
			throw new GenericUnexpectedException(ex.getMessage());
		}
		catch (SecurityException ex)
		{
			throw new GenericUnexpectedException(ex.getMessage());
		}
		catch (InstantiationException ex)
		{
			throw new GenericUnexpectedException(ex.getMessage());
		}
		catch (IllegalAccessException ex)
		{
			throw new GenericUnexpectedException(ex.getMessage());
		}
		catch (IllegalArgumentException ex)
		{
			throw new GenericUnexpectedException(ex.getMessage());
		}
		catch (InvocationTargetException ex)
		{
			throw new GenericUnexpectedException(ex.getMessage());
		}
	}

	/**
	 * Get the locators used to detect validation errors
	 * 
	 * @return List&lt;String&gt;
	 */
	public List<String> getErrorLocators()
	{
		if (errorLocators == null)
			errorLocators = new ArrayList<String>();

		return errorLocators;
	}

	/**
	 * Set the locators to be used to detect validation errors
	 * 
	 * @param errorLocators - Locators for all the errors
	 */
	public void setErrorLocators(List<String> errorLocators)
	{
		this.errorLocators = errorLocators;
	}

	/**
	 * Gets the displayed validation error using the specified locator<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should not wait for an error to occur<BR>
	 * 
	 * @param locator - Locator used to get the displayed validation error
	 * @return null if no error else non-null string that is the displayed validation error
	 */
	protected abstract String getDisplayedError(String locator);

	/**
	 * Get all the errors that can be displayed<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The implementing class needs to handle duplicate errors as only the last one will be in the map and
	 * will cause the wrong exception to be thrown at some point.<BR>
	 * 
	 * @param lang - Language to get the available errors
	 * @return all the errors that can be displayed in the specified language
	 */
	protected abstract HashMap<String, RuntimeException> getAvailableErrors(Languages lang);
}
