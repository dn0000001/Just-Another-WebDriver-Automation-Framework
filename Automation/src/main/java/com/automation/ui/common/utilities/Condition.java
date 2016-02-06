package com.automation.ui.common.utilities;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.reflect.MethodUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.dataStructures.Selection;

/**
 * This class is used to find the first condition that matches a list of possible conditions. An example of
 * this would be if 2 possible outcomes of an action are a button becomes ready or a button is removed. You
 * want to handle each condition differently as such you need to detect which condition occurred. This class
 * will allow you to do determine this.
 */
public class Condition {
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
	 * The single wait time for actions that require a timeout in milliseconds
	 */
	private int singleWaitTime = 1000;

	/**
	 * Stored alert message
	 */
	private String message;

	/**
	 * Stored popup handle
	 */
	private String handle;

	/**
	 * Stored locator
	 */
	private String locator;

	/**
	 * Enumeration to support all condition types
	 */
	private enum Type
	{
		/**
		 * Check for an alert
		 */
		Alert,

		/**
		 * Element is ready (displayed & enabled)
		 */
		Ready,

		/**
		 * Element is displayed
		 */
		Displayed,

		/**
		 * Element is removed (not displayed)
		 */
		Removed,

		/**
		 * Element is enabled
		 */
		Enabled,

		/**
		 * Element is disabled
		 */
		Disabled,

		/**
		 * Element exists in the DOM (but not necessarily displayed)
		 */
		Exists,

		/**
		 * Perform text check (and key stores the expected text)
		 */
		Text,

		/**
		 * Check for a popup
		 */
		Popup,

		/**
		 * Check for an URL of the current window (and key stores the initial URL)
		 */
		URL,

		/**
		 * Perform attribute check (and key stores the attribute to be checked)
		 */
		Attribute,

		/**
		 * Check if element is fresh
		 */
		Fresh,

		/**
		 * Check if element is stale
		 */
		Stale,

		/**
		 * Check if element is selected
		 */
		Selected,

		/**
		 * Check if element is unselected
		 */
		UnSelected,

		/**
		 * Perform text check using JavaScript (and key stores the expected text)
		 */
		TextJS,

		/**
		 * Check if element is selected using JavaScript
		 */
		SelectedJS,

		/**
		 * Check if element is unselected using JavaScript
		 */
		UnSelectedJS,

		/**
		 * Key used to store the text locator
		 */
		Text_Locator,

		/**
		 * Key used to store the text criteria
		 */
		Text_Criteria,

		/**
		 * Key used to store the URL criteria
		 */
		URL_Criteria,

		/**
		 * Key used to store the Attribute locator
		 */
		Attribute_Locator,

		/**
		 * Key used to store the Attribute criteria
		 */
		Attribute_Criteria,

		/**
		 * Key used to store the expected text of the Attribute being compared
		 */
		Attribute_Text,

		/**
		 * Perform input value check (and key stores the expected value)
		 */
		InputValue,

		/**
		 * Key used to store the input locator
		 */
		InputValue_Locator,

		/**
		 * Key used to store the input value criteria
		 */
		InputValue_Criteria,

		/**
		 * Perform drop down value check (and key stores the expected value)
		 */
		DropDown,

		/**
		 * Key used to store the drop down locator
		 */
		DropDown_Locator,

		/**
		 * Key used to store the drop down value criteria
		 */
		DropDown_Criteria,

		/**
		 * Key used to store the drop down value compare option
		 */
		DropDown_Compare,

		/**
		 * Use reflection to perform check (and key stores the class)
		 */
		Reflection,

		/**
		 * Key used to store the method to execute using reflection
		 */
		Reflection_Method,

		/**
		 * Key used to store the arguments to be passed to the method being executed using reflection
		 */
		Reflection_Arguments
	}

	/**
	 * Each time a match occurs this variable is updated such that the correct stored value can be returned
	 */
	private Type matchingType;

	/**
	 * Constructor to set all variables
	 * 
	 * @param driver
	 * @param timeout - Max timeout in milliseconds to be used
	 * @param poll - Poll interval in milliseconds to be used
	 */
	public Condition(WebDriver driver, int timeout, int poll)
	{
		setDriver(driver);
		setTimeout(timeout);
		setPoll(poll);
	}

	/**
	 * Constructor - Timeout & Poll interval is same as Framework with driver set to null
	 */
	public Condition()
	{
		this(null, Framework.getTimeoutInMilliseconds(), Framework.getPollInterval());
	}

	/**
	 * Constructor - Timeout & Poll interval is same as Framework
	 * 
	 * @param driver
	 */
	public Condition(WebDriver driver)
	{
		this(driver, Framework.getTimeoutInMilliseconds(), Framework.getPollInterval());
	}

	/**
	 * Constructor - Timeout & Poll interval is same as Framework
	 * 
	 * @param pageObject - Page Object to initialize driver from
	 */
	public Condition(Framework pageObject)
	{
		this(pageObject.getDriver());
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
	 * @return the message (non-null)
	 */
	public String getMessage()
	{
		return Conversion.nonNull(message);
	}

	/**
	 * @return the handle (non-null)
	 */
	public String getHandle()
	{
		return Conversion.nonNull(handle);
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
	 * @param timeout the timeout to set in milliseconds
	 */
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	/**
	 * @param poll the poll to set in milliseconds
	 */
	public void setPoll(int poll)
	{
		this.poll = poll;
	}

	/**
	 * @param message the message to set
	 */
	protected void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @param handle the handle to set
	 */
	protected void setHandle(String handle)
	{
		this.handle = handle;
	}

	/**
	 * @param locator the locator to set
	 */
	protected void setLocator(String locator)
	{
		this.locator = locator;
	}

	/**
	 * Set the Matching Type
	 * 
	 * @param matchingType - Matching Type
	 */
	protected void setMatchingType(Type matchingType)
	{
		this.matchingType = matchingType;
	}

	/**
	 * Waits for the 1st criteria to match the current condition
	 * 
	 * @param criteria - List of criteria to check against current condition
	 * @return <li>-2 if criteria is null</li><BR>
	 *         <li>-1 if none of the criteria match the current condition</li><BR>
	 *         <li>Index of the 1st criteria that matches the current condition (>=0)</li><BR>
	 * @throws GenericUnexpectedException if none of the criteria match the current condition before timeout
	 *             occurs
	 */
	public int waitForMatch(List<GenericData> criteria)
	{
		return waitForMatch(criteria, true);
	}

	/**
	 * Waits for the 1st criteria to match the current condition
	 * 
	 * @param criteria - List of criteria to check against current condition
	 * @param throwError - true to write log & throw exception if timeout occurs
	 * @return <li>-2 if criteria is null</li><BR>
	 *         <li>-1 if none of the criteria match the current condition</li><BR>
	 *         <li>Index of the 1st criteria that matches the current condition (>=0)</li><BR>
	 * @throws GenericUnexpectedException if none of the criteria match the current condition before timeout
	 *             occurs and throw error flag set
	 */
	public int waitForMatch(List<GenericData> criteria, boolean throwError)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(getTimeout()))
		{
			int index = match(criteria);

			// If no criteria, then no need to wait until timeout
			if (index == -2)
				break;

			if (index >= 0)
				return index;
			else
				Framework.sleep(getPoll());
		}

		if (throwError)
		{
			Logs.logError("None of the criteria matched the current condition before timeout occurred.  Criteria:  "
					+ criteria.toString());
		}

		return -1;
	}

	/**
	 * Waits for <B>all</B> of the criteria to match the current condition
	 * 
	 * @param criteria - List of criteria to match against current condition
	 * @throws GenericUnexpectedException if all the criteria does not match before timeout occurs
	 */
	public void waitForAllMatches(List<GenericData> criteria)
	{
		waitForAllMatches(criteria, true);
	}

	/**
	 * Waits for <B>all</B> of the criteria to match the current condition
	 * 
	 * @param criteria - List of criteria to match against current condition
	 * @param throwError - true to write log & throw exception if timeout occurs
	 * @return true if all criteria was matched before timeout occurred else false
	 * @throws GenericUnexpectedException if all the criteria does not match before timeout occurs and throw
	 *             error flag set
	 */
	public boolean waitForAllMatches(List<GenericData> criteria, boolean throwError)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(getTimeout()))
		{
			if (isAllMatched(criteria))
				return true;
			else
				Framework.sleep(getPoll());
		}

		if (throwError)
		{
			Logs.logError("All of the criteria was not matched before timeout occurred.  Criteria:  "
					+ criteria.toString());
		}

		return false;
	}

	/**
	 * Checks if any of the criteria match the current condition
	 * 
	 * @param criteria - List of criteria to check against
	 * @return <li>-2 if criteria is null</li><BR>
	 *         <li>-1 if none of the criteria match the current condition</li><BR>
	 *         <li>Index of the 1st criteria that matches the current condition (>=0)</li><BR>
	 */
	public int match(List<GenericData> criteria)
	{
		if (criteria == null)
			return -2;

		for (int i = 0; i < criteria.size(); i++)
		{
			boolean result = isMatch(criteria.get(i));
			if (result)
				return i;
		}

		return -1;
	}

	/**
	 * Checks if <B>all</B> of the criteria match the current condition
	 * 
	 * @param criteria - List of criteria to check
	 * @return false if any criteria is not matched, true if all criteria are matched
	 */
	public boolean isAllMatched(List<GenericData> criteria)
	{
		if (criteria == null)
			return false;

		for (int i = 0; i < criteria.size(); i++)
		{
			boolean result = isMatch(criteria.get(i));
			if (!result)
				return false;
		}

		return true;
	}

	/**
	 * Wait for each of the criteria to be matched.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method can be used to wait for multiple conditions to be true that would correspond to the page
	 * being loaded after the method is done.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * You have a page that has multiple fields that are required. Once the page is loaded, all required
	 * fields are populated or show a validation message. However, the issue is a required field that has data
	 * may be blank for until the page is completely loaded. You can use the method in the following way to
	 * detect once the page is loaded. Pair each of the fields into criteria that when true indicate that the
	 * field is ready (one GenericData to match when field is non-empty and second to match when the
	 * validation message is detected.) Then, wait for all these criteria to be matches which indicates the
	 * page is loaded.<BR>
	 * <BR>
	 * <B>Code Example:</B><BR>
	 * &#47;&#47; Each criteria is OR<BR>
	 * List&lt;GenericData&gt; criteria1 = new ArrayList<GenericData>();<BR>
	 * criteria1.add(Condition.getCriteria_InputValue(sLoc_Field1, Comparison.NotEqual, ""));<BR>
	 * criteria1.add(Condition.getCriteria_Displayed(sLoc_Error_Field1));<BR>
	 * <BR>
	 * &#47;&#47; Each criteria is OR<BR>
	 * List&lt;GenericData&gt; criteria2 = new ArrayList<GenericData>();<BR>
	 * criteria2.add(Condition.getCriteria_InputValue(sLoc_Field2, Comparison.NotEqual, ""));<BR>
	 * criteria2.add(Condition.getCriteria_Displayed(sLoc_Error_Field2));<BR>
	 * <BR>
	 * &#47;&#47; The method does an AND of all the criteria<BR>
	 * Condition condition = new Condition(this);<BR>
	 * condition.waitForAnyMatchOnAllCriteria(true, criteria1, criteria2);<BR>
	 * 
	 * @param throwError - true to write log & throw exception if timeout occurs
	 * @param criteria - List of criteria to match against current condition
	 * @return true if each criteria of the array is matched
	 * @throws GenericUnexpectedException if false after timeout and throw error is true
	 */
	@SafeVarargs
	public final boolean waitForAnyMatchOnAllCriteria(boolean throwError, List<GenericData>... criteria)
	{
		boolean anyCriteriaNull = false;
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(getTimeout()))
		{
			// Start by assuming that all criteria match
			boolean allMatch = true;

			// Go through all the criteria and ensure that they match
			for (int i = 0; i < criteria.length; i++)
			{
				int index = match(criteria[0]);

				// If no criteria, then no need to wait until timeout as all criteria will never be true
				if (index == -2)
				{
					allMatch = false;
					anyCriteriaNull = true;
					break;
				}

				// If any of the criteria does not match, then stop checking for this iteration
				if (index < 0)
				{
					allMatch = false;
					break;
				}
			}

			// We are done if null criteria is found as all criteria can never be true in this case
			if (anyCriteriaNull)
				break;

			// We are done if all the criteria match else wait for checking again
			if (allMatch)
				return true;
			else
				Framework.sleep(getPoll());
		}

		if (throwError)
		{
			for (int i = 0; i < criteria.length; i++)
			{
				Logs.log.warn("Criteria (" + i + "):  " + Conversion.toString(criteria[i], ", "));
			}

			Logs.logError("Not All of the criteria matched the current condition before timeout occurred.  See above for details.");
		}

		return false;
	}

	/**
	 * Checks if the criteria matches the current condition
	 * 
	 * @param criteria - criteria to check against
	 * @return true if criteria matches the current condition else false
	 */
	public boolean isMatch(GenericData criteria)
	{
		if (criteria == null || criteria.isEmpty())
			return false;

		//
		// Check for a supported condition type
		//

		if (criteria.containsKey(Type.Alert))
			return isAlert(criteria);

		if (criteria.containsKey(Type.Ready))
			return isReady(criteria);

		if (criteria.containsKey(Type.Displayed))
			return isDisplayed(criteria);

		if (criteria.containsKey(Type.Removed))
			return isRemoved(criteria);

		if (criteria.containsKey(Type.Enabled))
			return isEnabled(criteria);

		if (criteria.containsKey(Type.Disabled))
			return isDisabled(criteria);

		if (criteria.containsKey(Type.Exists))
			return isExists(criteria);

		if (criteria.containsKey(Type.Text))
			return isText(criteria);

		if (criteria.containsKey(Type.Popup))
			return isPopup(criteria);

		if (criteria.containsKey(Type.URL))
			return isURL(criteria);

		if (criteria.containsKey(Type.Attribute))
			return isAttribute(criteria);

		if (criteria.containsKey(Type.Fresh))
			return isFresh(criteria);

		if (criteria.containsKey(Type.Stale))
			return isStale(criteria);

		if (criteria.containsKey(Type.Selected))
			return isSelected(criteria);

		if (criteria.containsKey(Type.UnSelected))
			return isUnSelected(criteria);

		if (criteria.containsKey(Type.TextJS))
			return isTextJS(criteria);

		if (criteria.containsKey(Type.SelectedJS))
			return isSelectedJS(criteria);

		if (criteria.containsKey(Type.UnSelectedJS))
			return isUnSelectedJS(criteria);

		if (criteria.containsKey(Type.InputValue))
			return isInputValue(criteria);

		if (criteria.containsKey(Type.DropDown))
			return isDropDown(criteria);

		if (criteria.containsKey(Type.Reflection))
			return isReflection(criteria);

		return false;
	}

	/**
	 * Check if there is an alert & stores the message to be retrieved later<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Message is empty if no alert appeared else it contains the alert message<BR>
	 * 
	 * @param criteria - Criteria that contains the data for the check
	 * @return true if alert appeared else false
	 */
	private boolean isAlert(GenericData criteria)
	{
		boolean accept = (Boolean) criteria.get(Type.Alert);
		setMessage(Framework.waitForAlert(driver, singleWaitTime, accept, false));
		boolean result = !Compare.equals(getMessage(), "", Comparison.Equal);
		if (result)
			setMatchingType(Type.Alert);

		return result;
	}

	/**
	 * Check if element is ready
	 * 
	 * @param criteria - Criteria that contains the data for the check
	 * @return true if element displayed & enabled else false
	 */
	private boolean isReady(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.Ready));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = Framework.isElementReady(element);
		if (result)
			setMatchingType(Type.Ready);

		return result;
	}

	/**
	 * Check if element is displayed
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if element displayed else false
	 */
	private boolean isDisplayed(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.Displayed));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = Framework.isElementDisplayed(element);
		if (result)
			setMatchingType(Type.Displayed);

		return result;
	}

	/**
	 * Check if element is removed
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if element is removed else false
	 */
	private boolean isRemoved(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.Removed));
		boolean result = Framework.isElementRemoved(driver, getLocator(), singleWaitTime);
		if (result)
			setMatchingType(Type.Removed);

		return result;
	}

	/**
	 * Check if element is enabled
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true the element is enabled else false
	 */
	private boolean isEnabled(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.Enabled));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = Framework.isElementEnabled(element);
		if (result)
			setMatchingType(Type.Enabled);

		return result;
	}

	/**
	 * Check if element is disabled
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true the element is disabled else false
	 */
	private boolean isDisabled(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.Disabled));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = !Framework.isElementEnabled(element);
		if (result)
			setMatchingType(Type.Disabled);

		return result;
	}

	/**
	 * Check if element exists on the page
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if the element exists on the page
	 */
	private boolean isExists(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.Exists));
		boolean result = Framework.isElementExists(driver, getLocator());
		if (result)
			setMatchingType(Type.Exists);

		return result;
	}

	/**
	 * Checks if element's text matches the criteria
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if element has text matching criteria else false
	 */
	private boolean isText(GenericData criteria)
	{
		String expectedText = (String) criteria.get(Type.Text);
		Comparison criteriaText = (Comparison) criteria.get(Type.Text_Criteria);
		setLocator((String) criteria.get(Type.Text_Locator));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = Framework.isTextDisplayed(element, criteriaText, expectedText);
		if (result)
			setMatchingType(Type.Text);

		return result;
	}

	/**
	 * Check if popup appears & stores the handle to be retrieved later<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Handle is empty if no popup appeared else it contains the popup's window handle<BR>
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if popup appeared else false
	 */
	private boolean isPopup(GenericData criteria)
	{
		@SuppressWarnings("unchecked")
		List<String> existing = (List<String>) criteria.get(Type.Popup);
		Framework f = new Framework(driver);
		setHandle(f.waitForPopup(existing, singleWaitTime));
		boolean result = !Compare.equals(getHandle(), "", Comparison.Equal);
		if (result)
			setMatchingType(Type.Popup);

		return result;
	}

	/**
	 * Checks if the current URL matches the value using the specified URL criteria
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if current URL matches the value using the specified URL criteria
	 */
	private boolean isURL(GenericData criteria)
	{
		String initial = (String) criteria.get(Type.URL);
		Comparison criteriaURL = (Comparison) criteria.get(Type.URL_Criteria);
		boolean result = Framework.isURL(driver, initial, criteriaURL);
		if (result)
			setMatchingType(Type.URL);

		return result;
	}

	/**
	 * Checks if the element has the attribute matching the criteria
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if attribute with matching criteria is found else false
	 */
	private boolean isAttribute(GenericData criteria)
	{
		String attribute = (String) criteria.get(Type.Attribute);
		setLocator((String) criteria.get(Type.Attribute_Locator));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		Comparison criteriaAttribute = (Comparison) criteria.get(Type.Attribute_Criteria);
		String expectedText = (String) criteria.get(Type.Attribute_Text);
		boolean result = Framework.isAttribute(element, attribute, criteriaAttribute, expectedText);
		if (result)
			setMatchingType(Type.Attribute);

		return result;
	}

	/**
	 * Checks if an element is fresh
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if element is fresh else false
	 */
	private boolean isFresh(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.Fresh));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = Framework.isElementFresh(element);
		if (result)
			setMatchingType(Type.Fresh);

		return result;
	}

	/**
	 * Checks if an element is stale
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if element is stale else false
	 */
	private boolean isStale(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.Stale));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = !Framework.isElementFresh(element);
		if (result)
			setMatchingType(Type.Stale);

		return result;
	}

	/**
	 * Check if element (check box/radio button) is selected
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if selected else false
	 */
	private boolean isSelected(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.Selected));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = Framework.isElementSelected(element);
		if (result)
			setMatchingType(Type.Selected);

		return result;
	}

	/**
	 * Check if element (check box/radio button) is unselected
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if unselected else false
	 */
	private boolean isUnSelected(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.UnSelected));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = !Framework.isElementSelected(element);
		if (result)
			setMatchingType(Type.UnSelected);

		return result;
	}

	/**
	 * Checks if element's text matches the criteria using JavaScript
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if element has text matching criteria using JavaScript else false
	 */
	private boolean isTextJS(GenericData criteria)
	{
		String expectedText = (String) criteria.get(Type.TextJS);
		Comparison criteriaText = (Comparison) criteria.get(Type.Text_Criteria);
		setLocator((String) criteria.get(Type.Text_Locator));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = JS_Util.isText(element, criteriaText, expectedText);
		if (result)
			setMatchingType(Type.TextJS);

		return result;
	}

	/**
	 * Check if element (check box/radio button) is selected using JavaScript
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if selected else false
	 */
	private boolean isSelectedJS(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.SelectedJS));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = Framework.isElementSelected(element);
		if (result)
			setMatchingType(Type.SelectedJS);

		return result;
	}

	/**
	 * Check if element (check box/radio button) is unselected using JavaScript
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if unselected else false
	 */
	private boolean isUnSelectedJS(GenericData criteria)
	{
		setLocator((String) criteria.get(Type.UnSelectedJS));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = !Framework.isElementSelected(element);
		if (result)
			setMatchingType(Type.UnSelectedJS);

		return result;
	}

	/**
	 * Check if the input value matches the criteria
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if element has input value matching criteria else false
	 */
	private boolean isInputValue(GenericData criteria)
	{
		String expectedValue = (String) criteria.get(Type.InputValue);
		Comparison criteriaValue = (Comparison) criteria.get(Type.InputValue_Criteria);
		setLocator((String) criteria.get(Type.InputValue_Locator));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = Framework.isInputValue(element, criteriaValue, expectedValue);
		if (result)
			setMatchingType(Type.InputValue);

		return result;
	}

	/**
	 * Check if the drop down value matches the criteria
	 * 
	 * @param criteria - Criteria that contain the data for the check
	 * @return true if element has drop down value matching criteria else false
	 */
	private boolean isDropDown(GenericData criteria)
	{
		String expectedValue = (String) criteria.get(Type.DropDown);
		Selection criteriaValue = (Selection) criteria.get(Type.DropDown_Criteria);
		Comparison compare = (Comparison) criteria.get(Type.DropDown_Compare);
		setLocator((String) criteria.get(Type.DropDown_Locator));
		WebElement element = Framework.findElement(driver, getLocator(), false);
		boolean result = Framework.isDropDown(element, criteriaValue, compare, expectedValue);
		if (result)
			setMatchingType(Type.DropDown);

		return result;
	}

	/**
	 * Check if executing method with specified arguments using reflection returns true
	 * 
	 * @param criteria - Criteria that contain the data to execute the method with arguments using reflection
	 * @return true if executing method with specified arguments using reflection returns true else false
	 */
	private boolean isReflection(GenericData criteria)
	{
		Class<?> clazz = (Class<?>) criteria.get(Type.Reflection);
		String method = (String) criteria.get(Type.Reflection_Method);
		Object[] args;
		if (criteria.get(Type.Reflection_Arguments) == null)
			args = null;
		else
			args = (Object[]) criteria.get(Type.Reflection_Arguments);

		boolean result;
		try
		{
			result = (Boolean) MethodUtils.invokeExactStaticMethod(clazz, method, args);
		}
		catch (Exception ex)
		{
			result = false;
		}

		if (result)
			setMatchingType(Type.Reflection);

		return result;
	}

	/**
	 * Checks if an alert message was stored
	 * 
	 * @return true if an alert message was stored
	 */
	public boolean wasAlertMessageStored()
	{
		if (matchingType == null)
			return false;

		if (matchingType == Type.Alert)
			return true;
		else
			return false;
	}

	/**
	 * Checks if a window handle was stored
	 * 
	 * @return true if a window handle was stored
	 */
	public boolean wasHandleStored()
	{
		if (matchingType == null)
			return false;

		if (matchingType == Type.Popup)
			return true;
		else
			return false;
	}

	/**
	 * Checks if a locator was stored
	 * 
	 * @return true if a locator was stored
	 */
	public boolean wasLocatorStored()
	{
		if (matchingType == null || matchingType == Type.Alert || matchingType == Type.Popup
				|| matchingType == Type.URL)
		{
			return false;
		}

		return true;
	}

	/**
	 * Get Criteria for alert check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the message to be retrieved later<BR>
	 * 
	 * @param accept - true to click 'OK' (accept)
	 * @return GenericData
	 */
	public static GenericData getCriteria_Alert(boolean accept)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Alert, accept);
		return criteria;
	}

	/**
	 * Get Criteria for element ready (displayed & enabled) check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_Ready(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Ready, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element displayed check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_Displayed(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Displayed, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element removed check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_Removed(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Removed, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element enabled check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_Enabled(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Enabled, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element disabled check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_Disabled(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Disabled, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element exists on the page check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_Exists(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Exists, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element's text matches the criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
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
	 * @param locator - Locator to be used
	 * @param criteriaText - Criteria for the text comparison
	 * @param expectedText - Expected text to match the criteria
	 * @return GenericData
	 */
	public static GenericData getCriteria_Text(String locator, Comparison criteriaText, String expectedText)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Text_Locator, locator);
		criteria.add(Type.Text_Criteria, criteriaText);
		criteria.add(Type.Text, Conversion.nonNull(expectedText));
		return criteria;
	}

	/**
	 * Get Criteria for pop-up check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the handle to be retrieved later<BR>
	 * 
	 * @param existing - List of existing pop-up windows to ignore
	 * @return GenericData
	 */
	public static GenericData getCriteria_Popup(List<String> existing)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Popup, existing);
		return criteria;
	}

	/**
	 * Get Criteria for URL check of current window<BR>
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
	 * @param value - Compare the current URL against this value
	 * @param criteriaURL - Criteria used to determine if current URL matches the value
	 * @return GenericData
	 */
	public static GenericData getCriteria_URL(String value, Comparison criteriaURL)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.URL, value);
		criteria.add(Type.URL_Criteria, criteriaURL);
		return criteria;
	}

	/**
	 * Get Criteria for attribute check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
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
	 * @param locator - Locator to be used
	 * @param attribute - Attribute to check for
	 * @param criteriaAttribute - Criteria for attribute comparison
	 * @param expectedValue - Expected attribute value
	 * @return GenericData
	 */
	public static GenericData getCriteria_Attribute(String locator, String attribute,
			Comparison criteriaAttribute, String expectedValue)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Attribute_Locator, locator);
		criteria.add(Type.Attribute, attribute);
		criteria.add(Type.Attribute_Criteria, criteriaAttribute);
		criteria.add(Type.Attribute_Text, expectedValue);
		return criteria;
	}

	/**
	 * Get Criteria for element fresh check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_Fresh(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Fresh, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element stale check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_Stale(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Stale, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element selected check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_Selected(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Selected, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element unselected check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_UnSelected(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.UnSelected, locator);
		return criteria;
	}

	/**
	 * Get Criteria for element's text matches the criteria using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
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
	 * @param locator - Locator to be used
	 * @param criteriaText - Criteria for the text comparison
	 * @param expectedText - Expected text to match the criteria
	 * @return GenericData
	 */
	public static GenericData getCriteria_TextJS(String locator, Comparison criteriaText, String expectedText)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Text_Locator, locator);
		criteria.add(Type.Text_Criteria, criteriaText);
		criteria.add(Type.TextJS, expectedText);
		return criteria;
	}

	/**
	 * Get Criteria for Selected JavaScript check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_SelectedJS(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.SelectedJS, locator);
		return criteria;
	}

	/**
	 * Get Criteria for UnSelected JavaScript check<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A match on this criteria will save the locator to be retrieved later<BR>
	 * 
	 * @param locator - Locator to be used
	 * @return GenericData
	 */
	public static GenericData getCriteria_UnSelectedJS(String locator)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.UnSelectedJS, locator);
		return criteria;
	}

	/**
	 * Get Criteria for Input Value check<BR>
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
	 * @param locator - Locator to be used
	 * @param criteriaValue - Criteria for the input value comparison
	 * @param expectedValue - Expected input value to match the criteria
	 * @return GenericData
	 */
	public static GenericData getCriteria_InputValue(String locator, Comparison criteriaValue,
			String expectedValue)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.InputValue_Locator, locator);
		criteria.add(Type.InputValue_Criteria, criteriaValue);
		criteria.add(Type.InputValue, Conversion.nonNull(expectedValue));
		return criteria;
	}

	/**
	 * Get Criteria for Drop Down Value check<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Selection.Index<BR>
	 * Selection.ValueHTML<BR>
	 * Selection.VisibleText (Default)<BR>
	 * <BR>
	 * <B>Compare options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * Contains (Default)<BR>
	 * 
	 * @param locator - Locator to be used
	 * @param criteriaValue - Which drop down value to compare against (Index/HTML Value/Visible)
	 * @param compare - How to compare the drop down value to the expected value
	 * @param expectedValue - Expected drop down value to match the options
	 * @return GenericData
	 */
	public static GenericData getCriteria_DropDown(String locator, Selection criteriaValue,
			Comparison compare, String expectedValue)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.DropDown_Locator, locator);
		criteria.add(Type.DropDown_Criteria, criteriaValue);
		criteria.add(Type.DropDown_Compare, compare);
		criteria.add(Type.DropDown, Conversion.nonNull(expectedValue));
		return criteria;
	}

	/**
	 * Get Criteria for check using reflection<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Set arguments to null if no arguments to be passed to the method<BR>
	 * 2) Method needs to return a result that can be cast to Boolean.<BR>
	 * 
	 * @param clazz - Invoke static method on this class
	 * @param method - Get method with this name
	 * @param arguments - use these arguments - treat null as empty array
	 * @return GenericData
	 */
	public static GenericData getCriteria_Reflection(Class<?> clazz, String method, Object[] arguments)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Reflection, clazz);
		criteria.add(Type.Reflection_Method, method);
		criteria.add(Type.Reflection_Arguments, arguments);
		return criteria;
	}

	/**
	 * Get Criteria for check using static method isInitAllowed<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The class must implement the static method isInitAllowed(WebDriver) which returns a result that can
	 * be cast to Boolean.<BR>
	 * 
	 * @param clazz - Invoke static method on this class
	 * @param driver
	 * @return GenericData
	 */
	public static GenericData getCriteria_InitAllowed(Class<?> clazz, WebDriver driver)
	{
		GenericData criteria = new GenericData();
		criteria.add(Type.Reflection, clazz);
		criteria.add(Type.Reflection_Method, "isInitAllowed");
		criteria.add(Type.Reflection_Arguments, Arrays.asList(driver).toArray());
		return criteria;
	}
}
