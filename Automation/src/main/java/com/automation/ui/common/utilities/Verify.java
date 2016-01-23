package com.automation.ui.common.utilities;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.DropDownDefaults;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.dataStructures.Selection;
import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.exceptions.NoErrorOccurredException;

/**
 * This class is for verification of elements
 */
public class Verify {

	/**
	 * Verify that check box has correct state<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) null parameters are not supported<BR>
	 * 2) If expected is null, then log all is set to false<BR>
	 * 3) Verification will be skipped if expected.skip is true<BR>
	 * 
	 * @param element - Check box element to work with
	 * @param sLog - Element name to be logged
	 * @param expected - CheckBox object with expected state & whether to log all or only failures
	 */
	public static void checkBox(WebElement element, String sLog, CheckBox expected)
	{
		// Does user want to skip verification?
		if (expected != null && expected.skip)
			return;

		boolean bLogAll = (expected == null) ? false : expected.logAll;
		checkBox(element, sLog, expected, bLogAll);
	}

	/**
	 * Verify that check box has correct state<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) null parameters are not supported<BR>
	 * 2) Handles case where CheckBox element could be stale by retrying to get the information up to max
	 * retries<BR>
	 * 3) If expected is null, then log all is set to false<BR>
	 * 4) Verification will be skipped if expected.skip is true<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to CheckBox
	 * @param sLog - CheckBox name to be logged
	 * @param expected - CheckBox object with expected state & whether to log all or only failures
	 * @param retries - Number of retries to get information
	 */
	public static void checkBox(WebDriver driver, String sLocator, String sLog, CheckBox expected, int retries)
	{
		// Does user want to skip verification?
		if (expected != null && expected.skip)
			return;

		boolean bLogAll = (expected == null) ? false : expected.logAll;
		checkBox(driver, sLocator, sLog, expected, retries, bLogAll);
	}

	/**
	 * Verify that check box has correct state<BR>
	 * <BR>
	 * <B>Note: </B>null parameters are not supported<BR>
	 * 
	 * @param element - Check box element to work with
	 * @param sLog - Element name to be logged
	 * @param expected - CheckBox object with expected state
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void checkBox(WebElement element, String sLog, CheckBox expected, boolean bLogAll)
	{
		if (element == null && expected != null && !expected.skip)
		{
			Logs.logError("Check box element ('" + sLog
					+ "') was null.  (Verification against null is not supported.)");
		}

		if (expected == null)
		{
			Logs.logError("Expected Check box state object was null for '" + sLog
					+ "'.  (Verification against null is not supported.)");
		}

		boolean bActual = Framework.isElementSelected(element);
		if (bActual == expected.check)
		{
			if (bLogAll)
			{
				Logs.log.info("Check box ('" + sLog + "') was in the correct state (" + expected.check + ")");
			}
		}
		else
		{
			if (!expected.skip)
			{
				Logs.logError("Check box ('" + sLog + "') was not in the correct state (" + expected.check
						+ ")");
			}
		}
	}

	/**
	 * Verify that check box has correct state<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) null parameters are not supported<BR>
	 * 2) Handles case where CheckBox element could be stale by retrying to get the information up to max
	 * retries<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to CheckBox
	 * @param sLog - CheckBox name to be logged
	 * @param expected - CheckBox object with expected state
	 * @param retries - Number of retries to get information
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void checkBox(WebDriver driver, String sLocator, String sLog, CheckBox expected,
			int retries, boolean bLogAll)
	{
		if (expected == null)
		{
			Logs.logError("Expected Check box state object was null for '" + sLog
					+ "'.  (Verification against null is not supported.)");
		}

		boolean bActual = Framework.isElementSelected(driver, sLocator, retries);
		if (bActual == expected.check)
		{
			if (bLogAll)
			{
				Logs.log.info("Check box ('" + sLog + "') was in the correct state (" + expected.check + ")");
			}
		}
		else
		{
			if (!expected.skip)
			{
				Logs.logError("Check box ('" + sLog + "') was not in the correct state (" + expected.check
						+ ")");
			}
		}
	}

	/**
	 * Verify that selected drop down option matches the value option<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) null parameters are not supported<BR>
	 * 2) If expectedOption is null, then log all is set to false<BR>
	 * 3) Verification will be skipped if expectedOption.using equals Selection.Skip<BR>
	 * 
	 * @param element - Element (drop down) to verify has the correct option
	 * @param expectedOption - DropDown object used to select the option & whether to log all or only failures
	 */
	public static void dropDown(WebElement element, DropDown expectedOption)
	{
		// Does user want to skip verification?
		if (expectedOption != null && expectedOption.using == Selection.Skip)
			return;

		boolean bLogAll = (expectedOption == null) ? false : expectedOption.logAll;
		dropDown(element, expectedOption, bLogAll);
	}

	/**
	 * Verify that selected drop down option matches the value option<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) null parameters are not supported<BR>
	 * 2) Handles case where drop down element could be stale by retrying to get the information up to max
	 * retries<BR>
	 * 3) If expectedOption is null, then log all is set to false<BR>
	 * 4) Verification will be skipped if expectedOption.using equals Selection.Skip<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to drop down
	 * @param expectedOption - DropDown object used to select the option & whether to log all or only failures
	 * @param retries - Number of retries to get information
	 */
	public static void dropDown(WebDriver driver, String sLocator, DropDown expectedOption, int retries)
	{
		// Does user want to skip verification?
		if (expectedOption != null && expectedOption.using == Selection.Skip)
			return;

		boolean bLogAll = (expectedOption == null) ? false : expectedOption.logAll;
		dropDown(driver, sLocator, expectedOption, retries, bLogAll);
	}

	/**
	 * Verify that selected drop down option matches the value option<BR>
	 * <BR>
	 * <B>Note: </B>null parameters are not supported<BR>
	 * 
	 * @param element - Element (drop down) to verify has the correct option
	 * @param expectedOption - DropDown object used to select the option
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void dropDown(WebElement element, DropDown expectedOption, boolean bLogAll)
	{
		if (element == null)
			Logs.logError("Drop Down element was null.  (Verification against null is not supported.)");

		if (expectedOption == null)
			Logs.logError("Drop Down option to be selected was null.  (Verification against null is not supported.)");

		DropDownDefaults selected = DropDownDefaults.defaultsFromElement(element);
		if (selected.equivalent(expectedOption))
		{
			if (bLogAll)
			{
				if (expectedOption.using != Selection.Skip)
				{
					Logs.log.info("Current selection (" + selected.toString() + ") was option ("
							+ expectedOption.toString() + ") to be selected");
				}
			}
		}
		else
		{
			Logs.logError("Option (" + expectedOption.toString()
					+ ") was not selected.  The selected option was " + selected.toString());
		}
	}

	/**
	 * Verify that selected drop down option matches the value option<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) null parameters are not supported<BR>
	 * 2) Handles case where drop down element could be stale by retrying to get the information up to max
	 * retries<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to drop down
	 * @param expectedOption - DropDown object used to select the option
	 * @param retries - Number of retries to get information
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void dropDown(WebDriver driver, String sLocator, DropDown expectedOption, int retries,
			boolean bLogAll)
	{
		if (expectedOption == null)
			Logs.logError("Drop Down option to be selected was null.  (Verification against null is not supported.)");

		// Get the information
		// Note: This handles any exception that might occur getting the data by retrying up to the specified
		// times
		DropDownDefaults selected = DropDownDefaults.defaultsFromElement(driver, sLocator, retries);

		// Verify the data is correct
		if (selected.equivalent(expectedOption))
		{
			if (bLogAll)
			{
				if (expectedOption.using != Selection.Skip)
				{
					Logs.log.info("Current selection (" + selected.toString() + ") was option ("
							+ expectedOption.toString() + ") to be selected");
				}
			}
		}
		else
		{
			Logs.logError("Option (" + expectedOption.toString()
					+ ") was not selected.  The selected option was " + selected.toString());
		}
	}

	/**
	 * Verify that an entered value matches the actual value (case insensitive) only logs a failure<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Reads "value" attribute to get actual value used in the comparison<BR>
	 * 2) Should only be used for input fields<BR>
	 * 3) Use other overloaded method if need to control case comparison or logging level<BR>
	 * 
	 * @param element - Element (input field) to verify has the correct value
	 * @param sExpectedValue - Expected (entered) value
	 */
	public static void field(WebElement element, String sExpectedValue)
	{
		field(element, sExpectedValue, false, false);
	}

	/**
	 * Verify that an entered value matches the actual value<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Reads "value" attribute to get actual value used in the comparison<BR>
	 * 2) Should only be used for input fields<BR>
	 * 
	 * @param element - Element (input field) to verify has the correct value
	 * @param sExpectedValue - Expected (entered) value
	 * @param bCaseSensitive - true to do a case sensitive comparison, false to do a case insensitive
	 *            comparison
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void field(WebElement element, String sExpectedValue, boolean bCaseSensitive,
			boolean bLogAll)
	{
		// Assume that the values match
		boolean bFailure = false;

		/*
		 * 1) Get Actual value
		 * 2) Ensure both values are not null
		 * 3) Trim both values
		 * 4) Compare the values
		 */
		String sExpected = Conversion.nonNull(sExpectedValue).trim();
		String sActual = Conversion.nonNull(Framework.getAttribute(element, Framework.getInputAttr())).trim();

		if (bCaseSensitive)
		{
			if (!sActual.equals(sExpected))
				bFailure = true;
		}
		else
		{
			if (!sActual.equalsIgnoreCase(sExpected))
				bFailure = true;
		}

		// Did the values match?
		if (bFailure)
		{
			Logs.logError("Expected value ('" + sExpected + "') did not match the Actual value ('" + sActual
					+ "')");
		}
		else
		{
			// Log success if necessary
			if (bLogAll)
				Logs.log.info("Expected value ('" + sExpected + "') matched the Actual value ('" + sActual
						+ "')");
		}
	}

	/**
	 * Verify that an entered value matches the actual value<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Reads "value" attribute to get actual value used in the comparison<BR>
	 * 2) Should only be used for input fields<BR>
	 * 3) Handles case where element could be stale by retrying to get the information up to max retries<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to element
	 * @param sExpectedValue - Expected (entered) value
	 * @param bCaseSensitive - true to do a case sensitive comparison, false to do a case insensitive
	 *            comparison
	 * @param retries - Number of retries to get information
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void field(WebDriver driver, String sLocator, String sExpectedValue,
			boolean bCaseSensitive, int retries, boolean bLogAll)
	{
		// Assume that the values match
		boolean bFailure = false;

		/*
		 * 1) Get Actual value
		 * 2) Ensure both values are not null
		 * 3) Trim both values
		 * 4) Compare the values
		 */
		String sExpected = Conversion.nonNull(sExpectedValue).trim();
		String sActual = Framework.getAttribute(driver, sLocator, Framework.getInputAttr(), retries).trim();

		if (bCaseSensitive)
		{
			if (!sActual.equals(sExpected))
				bFailure = true;
		}
		else
		{
			if (!sActual.equalsIgnoreCase(sExpected))
				bFailure = true;
		}

		// Did the values match?
		if (bFailure)
		{
			Logs.logError("Expected value ('" + sExpected + "') did not match the Actual value ('" + sActual
					+ "')");
		}
		else
		{
			// Log success if necessary
			if (bLogAll)
				Logs.log.info("Expected value ('" + sExpected + "') matched the Actual value ('" + sActual
						+ "')");
		}
	}

	/**
	 * Verify that Input Field has correct state<BR>
	 * <BR>
	 * <B>Note: </B>null parameters are not supported<BR>
	 * 
	 * @param element - Input Field element to work with
	 * @param sLog - Element name to be logged
	 * @param expected - InputField object with expected state
	 */
	public static void inputField(WebElement element, String sLog, InputField expected)
	{
		if (element == null && expected != null && !expected.skip)
		{
			Logs.logError("Input Field element ('" + sLog
					+ "') was null.  (Verification against null is not supported.)");
		}

		if (expected == null)
		{
			Logs.logError("Expected Input Field ('" + sLog
					+ "') state object was null.  (Verification against null is not supported.)");
		}

		if (expected.skip)
			return;
		else
			field(element, expected.getVerificationValue(), expected.caseSensitive, expected.logAll);
	}

	/**
	 * Verify that Input Field has correct state<BR>
	 * <BR>
	 * <B>Note: </B><BR>
	 * 1) null parameters are not supported<BR>
	 * 2) Handles case where element could be stale by retrying to get the information up to max retries<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to element
	 * @param sLog - Element name to be logged
	 * @param retries - Number of retries to get information
	 * @param expected - InputField object with expected state
	 */
	public static void inputField(WebDriver driver, String sLocator, String sLog, int retries,
			InputField expected)
	{
		if (expected == null)
		{
			Logs.logError("Expected Input Field ('" + sLog
					+ "') state object was null.  (Verification against null is not supported.)");
		}

		if (expected.skip)
			return;
		else
		{
			field(driver, sLocator, expected.getVerificationValue(), expected.caseSensitive, retries,
					expected.logAll);
		}
	}

	/**
	 * Verifies the element is in the correct state (enabled/disabled)<BR>
	 * <BR>
	 * <B>Note: </B> Only failures are logged<BR>
	 * 
	 * @param element - Element to verify status
	 * @param sLog - Element Name to log
	 * @param bDisabled - true to verify that element is disabled
	 */
	public static void state(WebElement element, String sLog, boolean bDisabled)
	{
		state(element, sLog, bDisabled, false);
	}

	/**
	 * Verifies the element is in the correct state (enabled/disabled)
	 * 
	 * @param element - Element to verify state
	 * @param sLog - Element Name to log
	 * @param bDisabled - true to verify that element is disabled
	 * @param bLogAll - true to log all checks even ones that are successful
	 */
	public static void state(WebElement element, String sLog, boolean bDisabled, boolean bLogAll)
	{
		if (bDisabled)
		{
			if (Framework.isElementEnabled(element))
				Logs.logError("'" + sLog + "' was enabled but should have been disabled");
			else
			{
				if (bLogAll)
					Logs.log.info("'" + sLog + "' was disabled as expected");
			}
		}
		else
		{
			if (Framework.isElementEnabled(element))
			{
				if (bLogAll)
					Logs.log.info("'" + sLog + "' was enabled as expected");
			}
			else
				Logs.logError("'" + sLog + "' was disabled but should have been enabled");
		}
	}

	/**
	 * Verifies the element is in the correct state (enabled/disabled)<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Handles case where element could be stale by retrying to get the information up to max retries<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to element
	 * @param sLog - element name to be logged
	 * @param bDisabled - true to verify that element is disabled
	 * @param retries - Number of retries to get information
	 * @param bLogAll - true to log all checks even ones that are successful
	 */
	public static void state(WebDriver driver, String sLocator, String sLog, boolean bDisabled, int retries,
			boolean bLogAll)
	{
		if (bDisabled)
		{
			if (Framework.isElementEnabled(driver, sLocator, retries))
				Logs.logError("'" + sLog + "' was enabled but should have been disabled");
			else
			{
				if (bLogAll)
					Logs.log.info("'" + sLog + "' was disabled as expected");
			}
		}
		else
		{
			if (Framework.isElementEnabled(driver, sLocator, retries))
			{
				if (bLogAll)
					Logs.log.info("'" + sLog + "' was enabled as expected");
			}
			else
				Logs.logError("'" + sLog + "' was disabled but should have been enabled");
		}
	}

	/**
	 * Verifies the element has the expected (visible) text<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param element - Element to check text
	 * @param criteria - Criteria for the text comparison
	 * @param sExpectedText - Expected (visible) text of the element (non-null)
	 * @param bLogAll - true to log all checks even ones that are successful
	 */
	public static void text(WebElement element, Comparison criteria, String sExpectedText, boolean bLogAll)
	{
		boolean bResult = Framework.isTextDisplayed(element, criteria, Framework.getTimeoutInMilliseconds(),
				sExpectedText);

		//
		// Get the Actual text for logging purposes
		// Note: It is possible (but highly unlikely) that the Actual text at this point has changed
		//
		String sActualText = Conversion.nonNull(Framework.getText(element)).trim();

		if (bResult)
		{
			if (bLogAll)
			{
				Logs.log.info("Expected value ('" + sExpectedText + "') matched the Actual value ('"
						+ sActualText + "')");
			}
		}
		else
		{
			Logs.logError("Expected value ('" + sExpectedText + "') did not match the Actual value ('"
					+ sActualText + "')");
		}
	}

	/**
	 * Verifies the element has the expected (visible) text<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element to check text
	 * @param criteria - Criteria for the text comparison
	 * @param sExpectedText - Expected (visible) text of the element (non-null)
	 * @param bLogAll - true to log all checks even ones that are successful
	 */
	public static void text(WebDriver driver, String sLocator, Comparison criteria, String sExpectedText,
			boolean bLogAll)
	{
		boolean bResult = Framework.isTextDisplayed(driver, sLocator, criteria,
				Framework.getTimeoutInMilliseconds(), sExpectedText);

		//
		// Get the Actual text for logging purposes
		// Note: It is possible (but highly unlikely) that the Actual text at this point has changed
		//
		WebElement element = Framework.findElement(driver, sLocator, false);
		String sActualText = Conversion.nonNull(Framework.getText(element)).trim();

		if (bResult)
		{
			if (bLogAll)
			{
				Logs.log.info("Expected value ('" + sExpectedText + "') matched the Actual value ('"
						+ sActualText + "')");
			}
		}
		else
		{
			Logs.logError("Expected value ('" + sExpectedText + "') did not match the Actual value ('"
					+ sActualText + "')");
		}
	}

	/**
	 * Verifies that the correct exception occurred<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method can be used for negative testing<BR>
	 * 2) The expected exception cannot be GenericUnexpectedException as this indicates an error<BR>
	 * 3) The expected exception cannot be NoErrorOccurredException as this indicates an error<BR>
	 * 
	 * @param actual - The actual runtime exception
	 * @param expected - The expected runtime exception
	 * @throws GenericUnexpectedException if actual & expected are not the same type
	 */
	public static void exception(RuntimeException actual, RuntimeException expected)
	{
		// If GenericUnexpectedException log error as it should never occur and when it does it indicates an
		// error
		if (actual instanceof GenericUnexpectedException)
		{
			Logs.logError(actual);
		}

		// Did no exception occur?
		if (actual instanceof NoErrorOccurredException)
		{
			Logs.logError(actual);
		}

		// Extract the class name without package parts
		String sActualException, sExpectedException;
		try
		{
			String[] pieces = actual.getClass().getName().split("\\.");
			sActualException = pieces[pieces.length - 1];

			pieces = expected.getClass().getName().split("\\.");
			sExpectedException = pieces[pieces.length - 1];
		}
		catch (Exception ex)
		{
			// Fall back to full name if any exception occurs
			sActualException = actual.getClass().getName();
			sExpectedException = expected.getClass().getName();
		}

		// Did the correct exception occur?
		if (actual.getClass().isInstance(expected))
		{
			Logs.log.info("The expected exception (" + sActualException + ") occurred with message:  "
					+ actual.getMessage());
			Logs.log.info("");
		}
		else
		{
			Logs.logError("The expected exception (" + sExpectedException
					+ ") did not occur instead the actual exception (" + sActualException
					+ ") occurred with message: " + actual.getMessage());
		}
	}

	/**
	 * Verifies the text values meet the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The text values are compared as is without trimming<BR>
	 * 2) null values are not supported<BR>
	 * 
	 * @param sActual - Actual text
	 * @param sExpected - Expected text
	 * @param criteria - Criteria for the text comparison
	 * @param bLogAll - true to log all checks even ones that are successful
	 * @throws GenericUnexpectedException if actual does not meet criteria
	 */
	public static void text(String sActual, String sExpected, Comparison criteria, boolean bLogAll)
	{
		boolean bResult;
		String sCriteria;
		if (criteria == Comparison.NotEqual)
		{
			bResult = !sActual.equals(sExpected);
			sCriteria = "Not Equal to '" + sExpected + "'";
		}
		else if (criteria == Comparison.Equal)
		{
			bResult = sActual.equals(sExpected);
			sCriteria = "Equal to '" + sExpected + "'";
		}
		else if (criteria == Comparison.EqualsIgnoreCase)
		{
			bResult = sActual.equalsIgnoreCase(sExpected);
			sCriteria = "Equals Ignore Case to '" + sExpected + "'";
		}
		else if (criteria == Comparison.RegEx)
		{
			bResult = sActual.matches(sExpected);
			sCriteria = "Match using Regular Expression '" + sExpected + "'";
		}
		else if (criteria == Comparison.DoesNotContain)
		{
			bResult = !sActual.contains(sExpected);
			sCriteria = "Does Not Contain '" + sExpected + "'";
		}
		else
		{
			bResult = sActual.contains(sExpected);
			sCriteria = "Contain '" + sExpected + "'";
		}

		if (bResult)
		{
			if (bLogAll)
				Logs.log.info("The Actual value ('" + sActual + "') met the criteria (" + sCriteria + ")");
		}
		else
		{
			Logs.logError("The Actual value ('" + sActual + "') did not meet criteria (" + sCriteria + ")");
		}
	}

	/**
	 * Verifies the element has correct visibility (hidden/displayed) <BR>
	 * <BR>
	 * <B>Note: </B> Only failures are logged<BR>
	 * 
	 * @param element - Element to verify visibility
	 * @param sLog - Element Name to log
	 * @param bHidden - true to verify that element is hidden
	 */
	public static void visibility(WebElement element, String sLog, boolean bHidden)
	{
		visibility(element, sLog, bHidden, false);
	}

	/**
	 * Verifies the element has correct visibility (hidden/displayed)
	 * 
	 * @param element - Element to verify visibility
	 * @param sLog - Element Name to log
	 * @param bHidden - true to verify that element is hidden
	 * @param bLogAll - true to log all checks even ones that are successful
	 */
	public static void visibility(WebElement element, String sLog, boolean bHidden, boolean bLogAll)
	{
		if (bHidden)
		{
			if (Framework.isElementDisplayed(element))
				Logs.logError("'" + sLog + "' was displayed but should have been hidden");
			else
			{
				if (bLogAll)
					Logs.log.info("'" + sLog + "' was hidden as expected");
			}
		}
		else
		{
			if (Framework.isElementDisplayed(element))
			{
				if (bLogAll)
					Logs.log.info("'" + sLog + "' was displayed as expected");
			}
			else
				Logs.logError("'" + sLog + "' was hidden but should have been displayed");
		}
	}

	/**
	 * Verifies the element has correct visibility (hidden/displayed)
	 * 
	 * @param driver
	 * @param sLocator - Locator to element
	 * @param sLog - Element Name to log
	 * @param bHidden - true to verify that element is hidden
	 * @param retries - Number of retries to get information
	 * @param bLogAll - true to log all checks even ones that are successful
	 */
	public static void visibility(WebDriver driver, String sLocator, String sLog, boolean bHidden,
			int retries, boolean bLogAll)
	{
		if (bHidden)
		{
			if (Framework.isElementDisplayed(driver, sLocator, retries))
				Logs.logError("'" + sLog + "' was displayed but should have been hidden");
			else
			{
				if (bLogAll)
					Logs.log.info("'" + sLog + "' was hidden as expected");
			}
		}
		else
		{
			if (Framework.isElementDisplayed(driver, sLocator, retries))
			{
				if (bLogAll)
					Logs.log.info("'" + sLog + "' was displayed as expected");
			}
			else
				Logs.logError("'" + sLog + "' was hidden but should have been displayed");
		}
	}

	/**
	 * Compares 2 objects using the equals method and logs the results<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The objects should override the equals method<BR>
	 * 2) The objects should override the toString method for logging<BR>
	 * 3) Method also works with integers<BR>
	 * 
	 * @param actual - Object containing the Actual Results
	 * @param expected - Object containing the Expected Results
	 * @param bLogAll - true to log all checks even ones that are successful
	 * @throws GenericUnexpectedException if objects are not equal
	 */
	public static void equals(Object actual, Object expected, boolean bLogAll)
	{
		if (actual.equals(expected))
		{
			if (bLogAll)
				Logs.log.info("The actual and expected data were the same.");
		}
		else
		{
			Logs.log.warn("Expected:  " + expected);
			Logs.log.warn("Actual:    " + actual);
			Logs.logError("The actual data did not match the expected data.  See above for details");
		}
	}

	/**
	 * Compares 2 objects using reflection to verify equality excluding specified fields and logs the results<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The objects should override the toString method for logging<BR>
	 * 
	 * @param actual - Object containing the Actual Results
	 * @param expected - Object containing the Expected Results
	 * @param excludeFields - Fields that are excluded from the comparison
	 * @param bLogAll - true to log all checks even ones that are successful
	 * @throws GenericUnexpectedException if objects are not equal
	 */
	public static void equals(Object actual, Object expected, List<String> excludeFields, boolean bLogAll)
	{
		if (Compare.equals(actual, expected, excludeFields))
		{
			if (bLogAll)
				Logs.log.info("The actual and expected data were the same.");
		}
		else
		{
			Logs.log.warn("Expected:  " + expected);
			Logs.log.warn("Actual:    " + actual);
			Logs.logError("The actual data did not match the expected data.  See above for details");
		}
	}

	/**
	 * Verify that all expected options are selected in the drop down<BR>
	 * <BR>
	 * <B>Note: </B>null parameters are not supported<BR>
	 * 
	 * @param element - Element (drop down) to verify has the correct option(s)
	 * @param expectedOptions - List of DropDown objects that should be selected
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 * @throws GenericUnexpectedException if any expected selected options are not found to be selected
	 */
	public static void dropDownMulti(WebElement element, List<DropDown> expectedOptions, boolean bLogAll)
	{
		if (element == null)
			Logs.logError("Drop Down element was null.  (Verification against null is not supported.)");

		// Get all the selected options for the drop down
		List<DropDownDefaults> allSelected = Framework.getAllSelectedOptions(element);

		// Verify that each option is selected
		// Note: Not removing found options from the list to allow for duplicate selected options
		boolean bError = false;
		for (DropDown expected : expectedOptions)
		{
			boolean bFound = false;
			for (int i = 0; i < allSelected.size(); i++)
			{
				if (allSelected.get(i).equivalent(expected))
				{
					bFound = true;
					break;
				}
			}

			if (bFound)
			{
				if (bLogAll)
					Logs.log.info("Found select option for:  " + expected);
			}
			else
			{
				Logs.log.warn("Could not find select option for:  " + expected);
				bError = true;
			}
		}

		if (bError)
		{
			Logs.logError("Could not find all expected selected options.  See above for details.");
		}
		else
		{
			if (bLogAll)
				Logs.log.info("All expected selected options were found");
		}
	}

	/**
	 * Verify that all expected options are selected in the drop down<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) If max retries is reached, then the list may not be complete with all selected options<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the drop down
	 * @param expectedOptions - List of DropDown objects that should be selected
	 * @param retries - Max number of retries to get all selected options
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 * @throws GenericUnexpectedException if any expected selected options are not found to be selected
	 */
	public static void dropDownMulti(WebDriver driver, String sLocator, List<DropDown> expectedOptions,
			int retries, boolean bLogAll)
	{
		// Get all the selected options for the drop down
		List<DropDownDefaults> allSelected = Framework.getAllSelectedOptions(driver, sLocator, retries);

		// Verify that each option is selected
		// Note: Not removing found options from the list to allow for duplicate selected options
		boolean bError = false;
		for (DropDown expected : expectedOptions)
		{
			boolean bFound = false;
			for (int i = 0; i < allSelected.size(); i++)
			{
				if (allSelected.get(i).equivalent(expected))
				{
					bFound = true;
					break;
				}
			}

			if (bFound)
			{
				if (bLogAll)
					Logs.log.info("Found select option for:  " + expected);
			}
			else
			{
				Logs.log.warn("Could not find select option for:  " + expected);
				bError = true;
			}
		}

		if (bError)
		{
			Logs.logError("Could not find all expected selected options.  See above for details.");
		}
		else
		{
			if (bLogAll)
				Logs.log.info("All expected selected options were found");
		}
	}

	/**
	 * Compares 2 lists using the equals method and logs the results<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The objects should override the equals method<BR>
	 * 2) The objects should override the toString method for logging<BR>
	 * 
	 * @param actual - List containing the Actual Results
	 * @param expected - List containing the Expected Results
	 * @param excludeFields - For each list item, the fields that are excluded from the comparison
	 * @param logAll - true to log successful message (all failures are logged)
	 * @throws GenericUnexpectedException if lists are not equal
	 */
	public static <T> void equals(List<T> actual, List<T> expected, List<String> excludeFields, boolean logAll)
	{
		if (actual.size() != expected.size())
		{
			Logs.logError("Actual list size (" + actual.size() + ") did not match the Expected list size ("
					+ expected.size() + ")");
		}

		TestResults results = new TestResults();
		for (int i = 0; i < actual.size(); i++)
		{
			boolean bResult = Compare.equals(actual.get(i), expected.get(i), excludeFields);
			if (!results.expectTrue(bResult))
			{
				results.logWarn("Index:  " + i);
				results.logWarn("Expected:  " + expected.get(i));
				results.logWarn("Actual:    " + actual.get(i));
			}
		}

		String sSuccess = "The actual and expected data were the same.";
		String sFailure = "The actual data did not match the expected data.  See above for details";
		if (logAll)
			results.verify(sSuccess, sFailure);
		else
			results.verify(sFailure);
	}

	/**
	 * Verifies the Search list contains all the items to be found using a contains (or equals) comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Contains: Supported options are Lower and Upper. For any other option, it does a normal contains<BR>
	 * 2) Equals: Supported options are Lower, Upper & EqualsIgnoreCase. For any other option, it does a
	 * normal equals<BR>
	 * 
	 * @param contains - true for contains comparison else equals comparison
	 * @param searchItems - List of items to be searched
	 * @param findItems - List of items to be found
	 * @param option - Option to modify the strings before the operation
	 * @param logAll - true to log successful message (all failures are logged)
	 * @throws GenericUnexpectedException if search list does not contain all items to be found
	 */
	private static void genericListVerify(boolean contains, List<String> searchItems, List<String> findItems,
			Comparison option, boolean logAll)
	{
		TestResults results = new TestResults();
		for (int i = 0; i < findItems.size(); i++)
		{
			boolean bResult;
			if (contains)
				bResult = Compare.contains(searchItems, findItems.get(0), option);
			else
				bResult = Compare.match(searchItems, findItems.get(0), option);

			if (!results.expectTrue(bResult))
				results.logWarn("The search list did not contain:  " + findItems.get(i));
		}

		if (results.isError())
			results.logWarn("Search List Items:  " + Conversion.toString(searchItems, ", "));

		String sUsing;
		if (contains)
			sUsing = " (using contains)";
		else
			sUsing = " (using equals)";

		String sSuccess = "The search list contained all the items to be found" + sUsing;
		String sFailure = "The search list did not contain all of the items to be found" + sUsing
				+ ".  See above for details";
		if (logAll)
			results.verify(sSuccess, sFailure);
		else
			results.verify(sFailure);
	}

	/**
	 * Verifies the Search list contains all the items to be found using a contains comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Supported options are Lower and Upper. For any other option, it does a normal contains<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param findItems - List of items to be found
	 * @param option - Option to modify the strings before the contains operation
	 * @param logAll - true to log successful message (all failures are logged)
	 * @throws GenericUnexpectedException if search list does not contain all items to be found
	 */
	public static void contains(List<String> searchItems, List<String> findItems, Comparison option,
			boolean logAll)
	{
		genericListVerify(true, searchItems, findItems, option, logAll);
	}

	/**
	 * Verifies the Search list contains all the items to be found using an equals comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Supported options are Lower, Upper & EqualsIgnoreCase. For any other option, it does a normal equals<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param findItems - List of items to be found
	 * @param option - Option to modify the strings before the equals operation
	 * @param logAll - true to log successful message (all failures are logged)
	 * @throws GenericUnexpectedException if search list does not contain all items to be found
	 */
	public static void match(List<String> searchItems, List<String> findItems, Comparison option,
			boolean logAll)
	{
		genericListVerify(false, searchItems, findItems, option, logAll);
	}

	/**
	 * Verify that a non-standard check box has correct state<BR>
	 * <BR>
	 * <B>Note: </B>null parameters are not supported<BR>
	 * 
	 * @param element - Check box element to work with
	 * @param sLog - Element name to be logged
	 * @param attribute - Attribute used to determine if element is selected
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 * @param expected - CheckBox object with expected state
	 */
	public static void checkBox(WebElement element, String sLog, String attribute,
			Comparison checkedCriteria, String checkedAttributeValue, Comparison uncheckedCriteria,
			String uncheckedAttributeValue, CheckBox expected)
	{
		// Does user want to skip verification?
		if (expected != null && expected.skip)
			return;

		boolean bLogAll = (expected == null) ? false : expected.logAll;
		checkBox(element, sLog, attribute, checkedCriteria, checkedAttributeValue, uncheckedCriteria,
				uncheckedAttributeValue, expected, bLogAll);
	}

	/**
	 * Verify that a non-standard check box has correct state<BR>
	 * <BR>
	 * <B>Note: </B>null parameters are not supported<BR>
	 * 
	 * @param element - Check box element to work with
	 * @param sLog - Element name to be logged
	 * @param attribute - Attribute used to determine if element is selected
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 * @param expected - CheckBox object with expected state
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void checkBox(WebElement element, String sLog, String attribute,
			Comparison checkedCriteria, String checkedAttributeValue, Comparison uncheckedCriteria,
			String uncheckedAttributeValue, CheckBox expected, boolean bLogAll)
	{
		if (element == null && expected != null && !expected.skip)
		{
			Logs.logError("Check box element ('" + sLog
					+ "') was null.  (Verification against null is not supported.)");
		}

		if (expected == null)
		{
			Logs.logError("Expected Check box state object was null for '" + sLog
					+ "'.  (Verification against null is not supported.)");
		}

		boolean state = Framework.isCorrectState(element, sLog, attribute, expected.check, checkedCriteria,
				checkedAttributeValue, uncheckedCriteria, uncheckedAttributeValue);
		if (state)
		{
			if (bLogAll)
				Logs.log.info("Check box ('" + sLog + "') was in the correct state (" + expected.check + ")");
		}
		else
		{
			Logs.logError("Check box was not in correct selection state of " + expected.check);
		}
	}

	/**
	 * Verify that a non-standard check box has correct state<BR>
	 * <BR>
	 * <B>Note: </B>null parameters are not supported<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to find element
	 * @param sLog - Element name to be logged
	 * @param attribute - Attribute used to determine if element is selected
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 * @param expected - CheckBox object with expected state
	 * @param retries - Number of retries to get information
	 */
	public static void checkBox(WebDriver driver, String locator, String sLog, String attribute,
			Comparison checkedCriteria, String checkedAttributeValue, Comparison uncheckedCriteria,
			String uncheckedAttributeValue, CheckBox expected, int retries)
	{
		// Does user want to skip verification?
		if (expected != null && expected.skip)
			return;

		boolean bLogAll = (expected == null) ? false : expected.logAll;
		checkBox(driver, locator, sLog, attribute, checkedCriteria, checkedAttributeValue, uncheckedCriteria,
				uncheckedAttributeValue, expected, retries, bLogAll);
	}

	/**
	 * Verify that a non-standard check box has correct state<BR>
	 * <BR>
	 * <B>Note: </B>null parameters are not supported<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to find element
	 * @param sLog - Element name to be logged
	 * @param attribute - Attribute used to determine if element is selected
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 * @param expected - CheckBox object with expected state
	 * @param retries - Number of retries to get information
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void checkBox(WebDriver driver, String locator, String sLog, String attribute,
			Comparison checkedCriteria, String checkedAttributeValue, Comparison uncheckedCriteria,
			String uncheckedAttributeValue, CheckBox expected, int retries, boolean bLogAll)
	{
		if (expected == null)
		{
			Logs.logError("Expected Check box state object was null for '" + sLog
					+ "'.  (Verification against null is not supported.)");
		}

		if (expected == null)
		{
			Logs.logError("Expected Check box state object was null for '" + sLog
					+ "'.  (Verification against null is not supported.)");
		}

		boolean state = Framework.isCorrectState(driver, locator, sLog, attribute, expected.check,
				checkedCriteria, checkedAttributeValue, uncheckedCriteria, uncheckedAttributeValue, retries);
		if (state)
		{
			if (bLogAll)
				Logs.log.info("Check box ('" + sLog + "') was in the correct state (" + expected.check + ")");
		}
		else
		{
			Logs.logError("Check box was not in correct selection state of " + expected.check);
		}
	}
}
