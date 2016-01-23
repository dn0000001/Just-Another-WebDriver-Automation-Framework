package com.automation.ui.common.utilities;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;

/**
 * This class provides methods to handle intermittent behaviors in the application
 */
public class FlakinessChecks {
	private WebDriver driver;

	/**
	 * Stored timeout (in milliseconds)
	 */
	private int timeout;

	/**
	 * Stored poll interval (in milliseconds)
	 */
	private int poll;

	/**
	 * Stored popup handle
	 */
	private String handle;

	/**
	 * Stored alert message
	 */
	private String message;

	/**
	 * Stored value
	 */
	private String storedValue;

	/**
	 * Stored locator
	 */
	private String locator;

	/**
	 * Generic Actions that can be checked
	 */
	private enum Actions
	{
		Displayed, //
		Removed, //
		Ready, //
		Exists, //
		Enabled, //
		Disabled, //
		Selected, //
		UnSelected, //
		JS_Selected, //
		JS_UnSelected, //
		Fresh, //
		Stale, //
		Text, //
		JS_Text, //
		Attribute, //
		URL; //
	}

	/**
	 * Constructor
	 * 
	 * @param driver
	 * @param timeout - Max timeout in milliseconds to be used (needs to be greater than 0)
	 * @param poll - Poll interval in milliseconds to be used (needs to be greater than 0)
	 */
	public FlakinessChecks(WebDriver driver, int timeout, int poll)
	{
		setDriver(driver);
		setTimeout(timeout);
		setPoll(poll);
	}

	/**
	 * Constructor - Poll interval is same as Framework
	 * 
	 * @param driver
	 * @param timeout - Max timeout in milliseconds to be used (needs to be greater than 0)
	 */
	public FlakinessChecks(WebDriver driver, int timeout)
	{
		this(driver, timeout, Framework.getPollInterval());
	}

	/**
	 * Constructor - Timeout & Poll interval is same as Framework
	 * 
	 * @param driver
	 */
	public FlakinessChecks(WebDriver driver)
	{
		this(driver, Framework.getTimeoutInMilliseconds(), Framework.getPollInterval());
	}

	/**
	 * Constructor - Poll interval is same as Framework and timeout is poll interval * retries
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public FlakinessChecks(Framework pageObject)
	{
		int flakyPoll = Framework.getPollInterval();
		int flakyTimeout = flakyPoll * pageObject.getRetries();

		setDriver(pageObject.getDriver());
		setTimeout(flakyTimeout);
		setPoll(flakyPoll);
	}

	/**
	 * @return the driver
	 */
	public WebDriver getDriver()
	{
		return driver;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout()
	{
		return timeout;
	}

	/**
	 * @return the poll
	 */
	public int getPoll()
	{
		return poll;
	}

	/**
	 * @return the handle
	 */
	public String getHandle()
	{
		return handle;
	}

	/**
	 * @return the message (non-null)
	 */
	public String getMessage()
	{
		return Conversion.nonNull(message);
	}

	/**
	 * Get the stored value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This may not necessarily be the value used in the comparison if the element refreshed before the
	 * comparison occurred.<BR>
	 * 
	 * @return the stored value (non-null)
	 */
	public String getStoredValue()
	{
		return Conversion.nonNull(storedValue);
	}

	/**
	 * @return the locator (non-null)
	 */
	public String getLocator()
	{
		return Conversion.nonNull(locator);
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	/**
	 * @param poll the poll to set
	 */
	public void setPoll(int poll)
	{
		this.poll = poll;
	}

	/**
	 * @param handle the handle to set
	 */
	protected void setHandle(String handle)
	{
		this.handle = handle;
	}

	/**
	 * @param message the message to set
	 */
	protected void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @param value the stored value to set
	 */
	public void setStoredValue(String value)
	{
		this.storedValue = value;
	}

	/**
	 * @param locator the locator to set
	 */
	protected void setLocator(String locator)
	{
		this.locator = locator;
	}

	/**
	 * Check if popup appears before timeout occurs & stores the handle to be retrieved later<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Handle is empty if no popup appeared else it contains the popup's window handle<BR>
	 * 
	 * @param existing - List of existing pop-up windows to ignore
	 * @return true if popup appeared before timeout occurred else false
	 */
	public boolean isPopup(List<String> existing)
	{
		Framework f = new Framework(driver);
		setHandle(f.waitForPopup(existing, timeout));
		if (Compare.equals(getHandle(), "", Comparison.Equal))
			return false;
		else
			return true;
	}

	/**
	 * Check if alert appears before timeout occurs & stores the message to be retrieved later<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Message is empty if no alert appeared else it contains the alert message<BR>
	 * 
	 * @param accept - true to click 'OK' (accept)
	 * @return true if alert appeared before timeout occurred else false
	 */
	public boolean isAlert(boolean accept)
	{
		setMessage(Framework.waitForAlert(driver, timeout, accept, false));
		if (Compare.equals(getMessage(), "", Comparison.Equal))
			return false;
		else
			return true;
	}

	/**
	 * Performs the specified check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The criteria will be null if not used<BR>
	 * 2) The expected text will be null if not used<BR>
	 * 3) The attribute will be null if not used<BR>
	 * 
	 * @param action - Check to be performed
	 * @param element - Element to perform check against
	 * @param criteria - Criteria for the comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @param attribute - Attribute to check for
	 * @return result of action specific check
	 * @throws GenericUnexpectedException if unsupported action
	 */
	private boolean genericCheck(Actions action, WebElement element, Comparison criteria,
			String expectedText, String attribute)
	{
		if (action == Actions.Displayed)
		{
			return Framework.isElementDisplayed(element);
		}
		else if (action == Actions.Removed)
		{
			return !Framework.isElementDisplayed(element);
		}
		else if (action == Actions.Ready)
		{
			return Framework.isElementReady(element);
		}
		else if (action == Actions.Exists)
		{
			if (element == null)
				return false;
			else
				return true;
		}
		else if (action == Actions.Enabled)
		{
			return Framework.isElementEnabled(element);
		}
		else if (action == Actions.Disabled)
		{
			return !Framework.isElementEnabled(element);
		}
		else if (action == Actions.Selected)
		{
			return Framework.isElementSelected(element);
		}
		else if (action == Actions.UnSelected)
		{
			return !Framework.isElementSelected(element);
		}
		else if (action == Actions.JS_Selected)
		{
			return JS_Util.isElementSelected(element);
		}
		else if (action == Actions.JS_UnSelected)
		{
			return !JS_Util.isElementSelected(element);
		}
		else if (action == Actions.Fresh)
		{
			return Framework.isElementFresh(element);
		}
		else if (action == Actions.Stale)
		{
			return !Framework.isElementFresh(element);
		}
		else if (action == Actions.Text)
		{
			setStoredValue(Conversion.nonNull(Framework.getText(element)).trim());
			return Framework.isTextDisplayed(element, criteria, expectedText);
		}
		else if (action == Actions.JS_Text)
		{
			setStoredValue(Conversion.nonNull(JS_Util.getText(element)).trim());
			return JS_Util.isText(element, criteria, expectedText);
		}
		else if (action == Actions.Attribute)
		{
			setStoredValue(Conversion.nonNull(Framework.getAttribute(element, attribute)));
			return Framework.isAttribute(element, attribute, criteria, expectedText);
		}

		Logs.logError("Unsupported action:  " + action);
		return false;
	}

	/**
	 * Performs the specified check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Any parameter that is not used will be null<BR>
	 * 
	 * @param action - Check to be performed
	 * @param locator - How to locate the element
	 * @param criteria - Criteria for the comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @param attribute - Attribute to check for
	 * @param value - Compare the current URL against this value
	 * @return result of action specific check
	 * @throws GenericUnexpectedException if unsupported action
	 */
	private boolean genericCheck(Actions action, String locator, Comparison criteria, String expectedText,
			String attribute, String value)
	{
		if (action == Actions.URL)
		{
			boolean result = Framework.isURL(driver, value, criteria);
			setStoredValue(Framework.getCurrentURL(driver));
			return result;
		}
		else
		{
			WebElement element = Framework.findElement(driver, locator, false);
			return genericCheck(action, element, criteria, expectedText, attribute);
		}
	}

	/**
	 * Checks if the specified check occurred before timeout occurred<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Any parameter that is not used will be null<BR>
	 * 
	 * @param action - Check to be performed
	 * @param locator - How to locate the element
	 * @param criteria - Criteria for the comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @param attribute - Attribute to check for
	 * @param value - Compare the current URL against this value
	 * @return true if generic check was successful before timeout else false
	 */
	private boolean genericCheckOccurred(Actions action, String locator, Comparison criteria,
			String expectedText, String attribute, String value)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(timeout))
		{
			if (genericCheck(action, locator, criteria, expectedText, attribute, value))
				return true;
			else
				Framework.sleep(poll);
		}

		return false;
	}

	/**
	 * Checks if the specified check occurred before timeout occurred<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should not be used if the element becomes stale as the results may be incorrect<BR>
	 * 2) The criteria will be null if not used<BR>
	 * 3) The expected text will be null if not used<BR>
	 * 4) The attribute will be null if not used<BR>
	 * 
	 * @param action - Check to be performed
	 * @param element - Element to perform check against
	 * @param criteria - Criteria for the comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @param attribute - Attribute to check for
	 * @return true if generic check was successful before timeout else false
	 */
	private boolean genericCheckOccurred(Actions action, WebElement element, Comparison criteria,
			String expectedText, String attribute)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(timeout))
		{
			if (genericCheck(action, element, criteria, expectedText, attribute))
				return true;
			else
				Framework.sleep(poll);
		}

		return false;
	}

	/**
	 * Checks if the specified check occurred before timeout occurred<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Unused parameters are set to null<BR>
	 * 
	 * @param action - Check to be performed
	 * @param locator - How to locate the element
	 * @return true if generic check was successful before timeout else false
	 */
	private boolean genericCheckOccurred(Actions action, String locator)
	{
		return genericCheckOccurred(action, locator, null, null, null, null);
	}

	/**
	 * Checks if the specified check occurred before timeout occurred<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Unused parameters are set to null<BR>
	 * 
	 * @param action - Check to be performed
	 * @param element - Element to perform check against
	 * @return true if generic check was successful before timeout else false
	 */
	private boolean genericCheckOccurred(Actions action, WebElement element)
	{
		return genericCheckOccurred(action, element, null, null, null);
	}

	/**
	 * Checks if the specified check occurred before timeout occurred<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Unused parameters are set to null<BR>
	 * 
	 * @param action - Check to be performed
	 * @param locator - How to locate the element
	 * @param criteria - Criteria for the text comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @return true if generic check was successful before timeout else false
	 */
	private boolean genericCheckOccurred(Actions action, String locator, Comparison criteria,
			String expectedText)
	{
		return genericCheckOccurred(action, locator, criteria, expectedText, null, null);
	}

	/**
	 * Checks if the specified check occurred before timeout occurred<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Unused parameters are set to null<BR>
	 * 
	 * @param action - Check to be performed
	 * @param element - Element to perform check against
	 * @param criteria - Criteria for the text comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @return true if generic check was successful before timeout else false
	 */
	private boolean genericCheckOccurred(Actions action, WebElement element, Comparison criteria,
			String expectedText)
	{
		return genericCheckOccurred(action, element, criteria, expectedText, null);
	}

	/**
	 * Checks if the specified check occurred before timeout occurred<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Unused parameters are set to null<BR>
	 * 
	 * @param action - Check to be performed
	 * @param locator - How to locate the element
	 * @param attribute - Attribute to check for
	 * @param criteria - Criteria for the text comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @return true if generic check was successful before timeout else false
	 */
	private boolean genericCheckOccurred(Actions action, String locator, String attribute,
			Comparison criteria, String expectedText)
	{
		return genericCheckOccurred(action, locator, criteria, expectedText, attribute, null);
	}

	/**
	 * Checks if an element is removed (hidden) before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element is removed before timeout else false
	 */
	public boolean isElementRemoved(String locator)
	{
		return genericCheckOccurred(Actions.Removed, locator);
	}

	/**
	 * Checks if an element is ready (enabled & displayed) before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element becomes ready (displayed & enabled) else false
	 */
	public boolean isElementReady(String locator)
	{
		return genericCheckOccurred(Actions.Ready, locator);
	}

	/**
	 * Checks if an element is displayed before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element is displayed else false
	 */
	public boolean isElementDisplayed(String locator)
	{
		return genericCheckOccurred(Actions.Displayed, locator);
	}

	/**
	 * Checks if an element exists before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If an element exists, it may not be displayed to the user (or enabled)<BR>
	 * 
	 * @param locator - How to locate the element
	 * @return true if element exists else false
	 */
	public boolean isElementExists(String locator)
	{
		return genericCheckOccurred(Actions.Exists, locator);
	}

	/**
	 * Checks if an element is enabled before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element is enabled else false
	 */
	public boolean isElementEnabled(String locator)
	{
		return genericCheckOccurred(Actions.Enabled, locator);
	}

	/**
	 * Checks if an element is disabled before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element is disabled else false
	 */
	public boolean isElementDisabled(String locator)
	{
		return genericCheckOccurred(Actions.Disabled, locator);
	}

	/**
	 * Checks if an element is selected before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element is selected else false
	 */
	public boolean isElementSelected(String locator)
	{
		return genericCheckOccurred(Actions.Selected, locator);
	}

	/**
	 * Checks if an element is unselected before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element is unselected else false
	 */
	public boolean isElementUnSelected(String locator)
	{
		return genericCheckOccurred(Actions.UnSelected, locator);
	}

	/**
	 * Checks if an element is selected using JavaScript before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element is selected using JavaScript else false
	 */
	public boolean isElementSelectedJS(String locator)
	{
		return genericCheckOccurred(Actions.JS_Selected, locator);
	}

	/**
	 * Checks if an element is unselected using JavaScript before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element is unselected using JavaScript else false
	 */
	public boolean isElementUnSelectedJS(String locator)
	{
		return genericCheckOccurred(Actions.JS_UnSelected, locator);
	}

	/**
	 * Checks if an element remains fresh before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element remains fresh else false
	 */
	public boolean isElementFresh(String locator)
	{
		return genericCheckOccurred(Actions.Fresh, locator);
	}

	/**
	 * Checks if an element becomes stale before timeout occurs
	 * 
	 * @param locator - How to locate the element
	 * @return true if element becomes stale else false
	 */
	public boolean isElementStale(String locator)
	{
		return genericCheckOccurred(Actions.Stale, locator);
	}

	/**
	 * Checks if element's text matches the criteria before timeout occurs & stores the value to be retrieved
	 * later<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param locator - How to locate the element
	 * @param criteria - Criteria for the text comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @return true if element has text matching criteria else false
	 */
	public boolean isText(String locator, Comparison criteria, String expectedText)
	{
		return genericCheckOccurred(Actions.Text, locator, criteria, expectedText);
	}

	/**
	 * Checks if element's text matches the criteria using JavaScript before timeout occurs & stores the value
	 * to be retrieved later<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param locator - How to locate the element
	 * @param criteria - Criteria for the text comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @return true if element has text matching criteria using JavaScript else false
	 */
	public boolean isTextJS(String locator, Comparison criteria, String expectedText)
	{
		return genericCheckOccurred(Actions.JS_Text, locator, criteria, expectedText);
	}

	/**
	 * Checks if the element has the attribute matching the criteria & stores the value to be retrieved later<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Standard<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Contains<BR>
	 * Does Not Contain<BR>
	 * Regular Expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If you do not want to check the attribute value, then use Comparison.Standard for the criteria<BR>
	 * 
	 * @param locator - How to locate the element
	 * @param attribute - Attribute to check for
	 * @param criteria - Criteria for attribute comparison
	 * @param expectedText - Expected attribute value
	 * @return true if attribute with matching criteria is found else false
	 */
	public boolean isAttribute(String locator, String attribute, Comparison criteria, String expectedText)
	{
		return genericCheckOccurred(Actions.Attribute, locator, attribute, criteria, expectedText);
	}

	/**
	 * Checks if the current URL matches the value using the specified criteria
	 * 
	 * @param value - Compare the current URL against this value
	 * @param criteria - Criteria used to determine if current URL matches the value
	 * @return true if current URL matches the value using the specified criteria
	 */
	public boolean isURL(String value, Comparison criteria)
	{
		return genericCheckOccurred(Actions.URL, null, criteria, null, null, value);
	}

	/**
	 * Checks if an element is removed (hidden) before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element is removed before timeout else false
	 */
	public boolean isElementRemoved(WebElement element)
	{
		return genericCheckOccurred(Actions.Removed, element);
	}

	/**
	 * Checks if an element is ready (enabled & displayed) before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element becomes ready (displayed & enabled) else false
	 */
	public boolean isElementReady(WebElement element)
	{
		return genericCheckOccurred(Actions.Ready, element);
	}

	/**
	 * Checks if an element is displayed before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element is displayed else false
	 */
	public boolean isElementDisplayed(WebElement element)
	{
		return genericCheckOccurred(Actions.Displayed, element);
	}

	/**
	 * Checks if an element exists before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If an element exists, it may not be displayed to the user (or enabled)<BR>
	 * 2) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element exists else false
	 */
	public boolean isElementExists(WebElement element)
	{
		return genericCheckOccurred(Actions.Exists, element);
	}

	/**
	 * Checks if an element is enabled before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element is enabled else false
	 */
	public boolean isElementEnabled(WebElement element)
	{
		return genericCheckOccurred(Actions.Enabled, element);
	}

	/**
	 * Checks if an element is disabled before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element is disabled else false
	 */
	public boolean isElementDisabled(WebElement element)
	{
		return genericCheckOccurred(Actions.Disabled, element);
	}

	/**
	 * Checks if an element is selected before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element is selected else false
	 */
	public boolean isElementSelected(WebElement element)
	{
		return genericCheckOccurred(Actions.Selected, element);
	}

	/**
	 * Checks if an element is unselected before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element is unselected else false
	 */
	public boolean isElementUnSelected(WebElement element)
	{
		return genericCheckOccurred(Actions.UnSelected, element);
	}

	/**
	 * Checks if an element is selected using JavaScript before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element is selected using JavaScript else false
	 */
	public boolean isElementSelectedJS(WebElement element)
	{
		return genericCheckOccurred(Actions.JS_Selected, element);
	}

	/**
	 * Checks if an element is unselected using JavaScript before timeout occurs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @return true if element is unselected using JavaScript else false
	 */
	public boolean isElementUnSelectedJS(WebElement element)
	{
		return genericCheckOccurred(Actions.JS_UnSelected, element);
	}

	/**
	 * Checks if an element remains fresh before timeout occurs
	 * 
	 * @param element - Element to perform check against
	 * @return true if element remains fresh else false
	 */
	public boolean isElementFresh(WebElement element)
	{
		return genericCheckOccurred(Actions.Fresh, element);
	}

	/**
	 * Checks if an element becomes stale before timeout occurs
	 * 
	 * @param element - Element to perform check against
	 * @return true if element becomes stale else false
	 */
	public boolean isElementStale(WebElement element)
	{
		return genericCheckOccurred(Actions.Stale, element);
	}

	/**
	 * Checks if element's text matches the criteria before timeout occurs & stores the value to be retrieved
	 * later<BR>
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
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @param criteria - Criteria for the text comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @return true if element has text matching criteria else false
	 */
	public boolean isText(WebElement element, Comparison criteria, String expectedText)
	{
		return genericCheckOccurred(Actions.Text, element, criteria, expectedText);
	}

	/**
	 * Checks if element's text matches the criteria using JavaScript before timeout occurs & stores the value
	 * to be retrieved later<BR>
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
	 * 1) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @param criteria - Criteria for the text comparison
	 * @param expectedText - Expected text to match the criteria (non-null)
	 * @return true if element has text matching criteria using JavaScript else false
	 */
	public boolean isTextJS(WebElement element, Comparison criteria, String expectedText)
	{
		return genericCheckOccurred(Actions.JS_Text, element, criteria, expectedText);
	}

	/**
	 * Checks if the element has the attribute matching the criteria & stores the value to be retrieved later<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Standard<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Contains<BR>
	 * Does Not Contain<BR>
	 * Regular Expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If you do not want to check the attribute value, then use Comparison.Standard for the criteria<BR>
	 * 2) Do not use method if element could become stale<BR>
	 * 
	 * @param element - Element to perform check against
	 * @param attribute - Attribute to check for
	 * @param criteria - Criteria for attribute comparison
	 * @param expectedText - Expected attribute value
	 * @return true if attribute with matching criteria is found else false
	 */
	public boolean isAttribute(WebElement element, String attribute, Comparison criteria, String expectedText)
	{
		return genericCheckOccurred(Actions.Attribute, element, criteria, expectedText, attribute);
	}

	/**
	 * Check if window appears with specified URL before timeout occurs & stores the handle to be retrieved
	 * later<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Handle is empty if no window appeared with specified URL else it contains the matching window handle<BR>
	 * 
	 * @param url - URL to search for
	 * @return true if window appeared with specified URL before timeout occurred else false
	 */
	public boolean isWindow(String url)
	{
		Framework f = new Framework(driver);
		setHandle(f.waitForWindowHandle(url, timeout, poll));
		if (Compare.equals(getHandle(), "", Comparison.Equal))
			return false;
		else
			return true;
	}

	/**
	 * Check if any of the locators become ready before timeout occurs & stores the corresponding locator to
	 * be retrieved later<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The locator variable is set with the 1st locator in the list that is ready or empty if none of the
	 * locators become ready<BR>
	 * 
	 * @param locators - List of locators to elements to check if ready
	 * @param displayOnly - true to only consider if element is displayed (instead of ready)
	 * @return true if any of the locators became ready before timeout occurred else false
	 */
	public boolean isLocator(List<String> locators, boolean displayOnly)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(timeout))
		{
			setLocator(Framework.getLocator(driver, locators, displayOnly));
			if (!Compare.equals(getLocator(), "", Comparison.Equal))
				return true;

			Framework.sleep(poll);
		}

		return false;
	}

	/**
	 * Checks if the left hand side matches the right hand side using the comparison option
	 * 
	 * @param lhs - Left Hand Side
	 * @param rhs - Right Hand Side
	 * @param option - Option for the comparison
	 * @return true if the left hand side matches the right hand side using the comparison option
	 */
	private boolean isComparisonMatch(int lhs, int rhs, Comparison option)
	{
		if (option == Comparison.NotEqual)
			return lhs != rhs;
		else if (option == Comparison.LessThan)
			return lhs < rhs;
		else if (option == Comparison.LessThanEqualTo)
			return lhs <= rhs;
		else if (option == Comparison.GreaterThan)
			return lhs > rhs;
		else if (option == Comparison.GreaterThanEqualTo)
			return lhs >= rhs;
		else
			return lhs == rhs;
	}

	/**
	 * Checks if the value (left hand side) matches the selected/unselected count (right hand side) using the
	 * comparison option<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * NotEqual<BR>
	 * LessThan<BR>
	 * LessThanEqualTo<BR>
	 * Equal (default)<BR>
	 * GreaterThan<BR>
	 * GreaterThanEqualTo<BR>
	 * <BR>
	 * 
	 * @param elements - List of elements (Check box or radio button) to get selected count from
	 * @param selected - true to get selected count, false to get unselected count
	 * @param option - Option for the comparison
	 * @param value - Left Hand Side of the comparison
	 * @return true if the value (left hand side) matches the selected/unselected count (right hand side)
	 *         using the comparison option before timeout occurred else false
	 */
	public boolean isSelectedCount(List<WebElement> elements, boolean selected, Comparison option, int value)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(timeout))
		{
			int count = Framework.getSelectedCount(elements, selected);
			boolean result = isComparisonMatch(value, count, option);
			if (result)
				return true;

			Framework.sleep(poll);
		}

		return false;
	}

	/**
	 * Checks if the value (left hand side) matches the selected/unselected count (right hand side) using the
	 * comparison option<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * NotEqual<BR>
	 * LessThan<BR>
	 * LessThanEqualTo<BR>
	 * Equal (default)<BR>
	 * GreaterThan<BR>
	 * GreaterThanEqualTo<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Each locator is used to get the element and immediately perform the check which is added to the
	 * returned count<BR>
	 * 
	 * @param selected - true to get selected count, false to get unselected count
	 * @param locators - List of locators used to get the elements to get selected count from
	 * @param option - Option for the comparison
	 * @param value - Left Hand Side of the comparison
	 * @return true if the value (left hand side) matches the selected/unselected count (right hand side)
	 *         using the comparison option before timeout occurred else false
	 */
	public boolean isSelectedCount(boolean selected, List<String> locators, Comparison option, int value)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(timeout))
		{
			int count = Framework.getSelectedCount(driver, locators, selected);
			boolean result = isComparisonMatch(value, count, option);
			if (result)
				return true;

			Framework.sleep(poll);
		}

		return false;
	}
}
