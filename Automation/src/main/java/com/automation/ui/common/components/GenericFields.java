package com.automation.ui.common.components;

import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.AutoCompleteField;
import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.FlakinessChecks;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.JS_Util;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.TestResults;
import com.automation.ui.common.utilities.Verify;

/**
 * This class works with generic (input) fields to attempt to reduce boiler plate code<BR>
 * <BR>
 * <B>Example of configuration before use:</B><BR>
 * GenericFields fields = new GenericFields(driver);<BR>
 * fields.addRadio(Radio.Yes, "yes", "Yes", true, false);<BR>
 * fields.addRadio(Radio.No, "no", "No", true, false);<BR>
 * fields.addRadio(Radio.DEFAULT, "default", "Default", true, false);<BR>
 * fields.verifyConfig();<BR>
 * // Use to fill with random radio button option already configured<BR>
 * fields.fill(Rand.randomEnum(Radio.Yes, 10000));<BR>
 */
public class GenericFields {
	protected WebDriver driver;

	/**
	 * The default target text to find the target for replacement in locators where applicable
	 */
	private static final String _DefaultTarget = "REPLACE";

	/**
	 * The target text used to find the target for replacement in locators where applicable
	 */
	protected String target;

	/**
	 * All the stored fields and corresponding configurations
	 */
	protected GenericData fields;

	/**
	 * All supported configuration options for each element
	 */
	protected enum Config
	{
		Locator, // Locator for the action
		Log, // Logging for the action
		WaitForReady, // True to wait for the element to become ready
		Verify, // True to perform verification after entering data
		SuggestionList, // Locator to the suggestions list for AutoComplete only
		SuggestionListOptions, // xpath to all the options for AutoComplete only
		Attribute, // Attribute used for drop down/check box click only
		NonStandardReady, // True to use non-standard ready method
		ReadyAttribute, // Attribute used in non-standard ready method
		EnabledCriteria, // Criteria for Enabled check in non-standard ready method
		EnabledValue, // Value used for the Enabled check (expected value) in non-standard ready method
		DisabledCriteria, // Criteria for Disabled check in non-standard ready method
		DisabledValue, // Value used for the Disabled check (expected value) in non-standard ready method
		CheckedCriteria, // Criteria for Selected check in check box click action
		CheckedValue, // Value used for the Selected check (expected value) in check box click action
		UnCheckedCriteria, // Criteria for Unselected check in check box click action
		UnCheckedValue; // Value used for the Unselected check (expected value) in check box click action
	}

	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public GenericFields(WebDriver driver)
	{
		setDriver(driver);
		setTarget(_DefaultTarget);
		fields = new GenericData();
	}

	/**
	 * Constructor
	 * 
	 * @param pageObject - Page Object used to set the driver
	 */
	public GenericFields(Framework pageObject)
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
	 * @param driver the driver to set
	 */
	public void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}

	/**
	 * @return The text to be used to find the target for replacement in locators where applicable
	 */
	public String getTarget()
	{
		return target;
	}

	/**
	 * @param target - The text to be used to find the target for replacement in locators where applicable
	 */
	public void setTarget(String target)
	{
		this.target = target;
	}

	/**
	 * Construct locator from the parameters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Replacement <B>only occurs</B> if locator is non-null and replacement is non-empty<BR>
	 * 
	 * @param locator - Locator for the field
	 * @param replacement - Replacement text
	 * @return String
	 */
	protected String constructLocator(String locator, String replacement)
	{
		if (locator == null || replacement == null || replacement.equals(""))
			return locator;

		return locator.replace(getTarget(), replacement);
	}

	/**
	 * Constructs the log from the parameters
	 * 
	 * @param log - Default Logging variable
	 * @param append - Text value to be appended
	 * @return log + append
	 */
	protected String constructLog(String log, String append)
	{
		return Conversion.nonNull(log) + Conversion.nonNull(append);
	}

	/**
	 * Waits for the element to become ready (displayed & enabled)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param config - Configuration Options
	 * @param replacement - Replacement text to be used in the locator
	 */
	protected void waitForElementReady(GenericData config, String replacement)
	{
		boolean nonStandardReady = (Boolean) config.get(Config.NonStandardReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);

		if (nonStandardReady)
		{
			String attribute = (String) config.get(Config.ReadyAttribute);
			Comparison enabledCriteria = (Comparison) config.get(Config.EnabledCriteria);
			String enabledAttributeValue = (String) config.get(Config.EnabledValue);
			Comparison disabledCriteria = (Comparison) config.get(Config.DisabledCriteria);
			String disabledAttributeValue = (String) config.get(Config.DisabledValue);
			Framework.waitForElementReady(driver, locator, attribute, enabledCriteria, enabledAttributeValue,
					disabledCriteria, disabledAttributeValue);
		}
		else
		{
			Framework.waitForElementReady(driver, locator);
		}
	}

	/**
	 * Fill a radio button option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) A null pointer exception will occur if the radio button option configuration was not added<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key)
	{
		return fill(key, "", "");
	}

	/**
	 * Fill a radio button option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) A null pointer exception will occur if the radio button option configuration was not added<BR>
	 * 3) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		Framework.click(element, log);

		if (verify)
		{
			Framework f = new Framework(driver);
			int poll = Framework.getPollInterval();
			int flakyTimeout = poll * f.getRetries();
			FlakinessChecks flaky = new FlakinessChecks(driver, flakyTimeout, poll);
			boolean selected = flaky.isElementSelected(element);
			if (!selected)
				Logs.logError("Expected the radio option (" + key + ") to be selected but it was unselected");
		}

		return element;
	}

	/**
	 * Fill an input field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param value - Value to be entered
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, InputField value)
	{
		return fill(key, value, null, "");
	}

	/**
	 * Fill an input field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param value - Value to be entered
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, InputField value, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		Framework.enterField(element, log, value);

		if (verify)
			Verify.inputField(element, log, value);

		return element;
	}

	/**
	 * Fill a check box field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, CheckBox cb)
	{
		return fill(key, cb, null, "");
	}

	/**
	 * Fill a check box field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, CheckBox cb, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		Framework.checkbox(element, log, cb);

		if (verify)
			Verify.checkBox(element, log, cb);

		return element;
	}

	/**
	 * Fill a drop down field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, DropDown dd)
	{
		return fill(key, dd, null, "");
	}

	/**
	 * Fill a drop down field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, DropDown dd, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		Framework.dropDownSelect(element, log, dd);

		if (verify)
			Verify.dropDown(element, dd);

		return element;
	}

	/**
	 * Fill a drop down field using a click<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Drop down options need to be displayed. This may require clicking a button before to have them
	 * displayed.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fillClick(Enum<?> key, DropDown dd)
	{
		fillClick(key, dd, null, "");
	}

	/**
	 * Fill a drop down field using a click<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Drop down options need to be displayed. This may require clicking a button before to have them
	 * displayed.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fillClick(Enum<?> key, DropDown dd, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		String attribute = (String) config.get(Config.Attribute);

		if (waitForReady)
			waitForElementReady(config, replacement);

		Framework.dropDownClick(driver, locator, attribute, log, true, dd);
	}

	/**
	 * Fill a check box using a click<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be only used on check boxes that are non-standard<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 */
	public WebElement fillClick(Enum<?> key, CheckBox cb)
	{
		return fillClick(key, cb, null, "");
	}

	/**
	 * Fill a check box using a click<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be only used on check boxes that are non-standard<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 */
	public WebElement fillClick(Enum<?> key, CheckBox cb, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);
		String attribute = (String) config.get(Config.Attribute);
		Comparison checkedCriteria = (Comparison) config.get(Config.CheckedCriteria);
		String checkedAttributeValue = (String) config.get(Config.CheckedValue);
		Comparison uncheckedCriteria = (Comparison) config.get(Config.UnCheckedCriteria);
		String uncheckedAttributeValue = (String) config.get(Config.UnCheckedValue);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		Framework.checkboxClick(element, log, attribute, checkedCriteria, checkedAttributeValue,
				uncheckedCriteria, uncheckedAttributeValue, cb);

		if (verify)
		{
			Verify.checkBox(element, log, attribute, checkedCriteria, checkedAttributeValue,
					uncheckedCriteria, uncheckedAttributeValue, cb);
		}

		return element;
	}

	/**
	 * Fill a check box using a click<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be only used on check boxes that are non-standard<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param retries - Number of times to retry if any exception occurs
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fillClick(Enum<?> key, CheckBox cb, int retries)
	{
		fillClick(key, cb, retries, null, "");
	}

	/**
	 * Fill a check box using a click<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be only used on check boxes that are non-standard<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param retries - Number of times to retry if any exception occurs
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fillClick(Enum<?> key, CheckBox cb, int retries, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);
		String attribute = (String) config.get(Config.Attribute);
		Comparison checkedCriteria = (Comparison) config.get(Config.CheckedCriteria);
		String checkedAttributeValue = (String) config.get(Config.CheckedValue);
		Comparison uncheckedCriteria = (Comparison) config.get(Config.UnCheckedCriteria);
		String uncheckedAttributeValue = (String) config.get(Config.UnCheckedValue);

		if (waitForReady)
			waitForElementReady(config, replacement);

		Framework.checkboxClick(driver, locator, log, attribute, checkedCriteria, checkedAttributeValue,
				uncheckedCriteria, uncheckedAttributeValue, cb, retries);

		if (verify)
		{
			Verify.checkBox(driver, locator, log, attribute, checkedCriteria, checkedAttributeValue,
					uncheckedCriteria, uncheckedAttributeValue, cb, retries);
		}
	}

	/**
	 * Fill a <B>non-standard</B> radio button option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be only used on radio button options that are non-standard<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @return WebElement
	 */
	public WebElement fillClick(Enum<?> key)
	{
		return fillClick(key, null, "");
	}

	/**
	 * Fill a <B>non-standard</B> radio button option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be only used on radio button options that are non-standard<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 */
	public WebElement fillClick(Enum<?> key, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);
		String attribute = (String) config.get(Config.Attribute);
		Comparison checkedCriteria = (Comparison) config.get(Config.CheckedCriteria);
		String checkedAttributeValue = (String) config.get(Config.CheckedValue);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		Framework.click(element, log);

		if (verify)
		{
			boolean state = Framework.isCorrectState(element, log, attribute, true, checkedCriteria,
					checkedAttributeValue, null, null);
			if (!state)
				Logs.logError("Radio Button option (" + log + ") was not in the selected state.");
		}

		return element;
	}

	/**
	 * Fill a <B>non-standard</B> radio button option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be only used on radio button options that are non-standard<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param retries - Number of times to retry if any exception occurs
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fillClick(Enum<?> key, int retries)
	{
		fillClick(key, retries, null, "");
	}

	/**
	 * Fill a <B>non-standard</B> radio button option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be only used on radio button options that are non-standard<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param retries - Number of times to retry if any exception occurs
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fillClick(Enum<?> key, int retries, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);
		String attribute = (String) config.get(Config.Attribute);
		Comparison checkedCriteria = (Comparison) config.get(Config.CheckedCriteria);
		String checkedAttributeValue = (String) config.get(Config.CheckedValue);

		if (waitForReady)
			waitForElementReady(config, replacement);

		Framework.click(driver, locator, log, retries);

		if (verify)
		{
			boolean state = Framework.isCorrectState(driver, locator, log, attribute, true, checkedCriteria,
					checkedAttributeValue, null, null, retries);
			if (!state)
			{
				Logs.logError("Radio Button option (" + log + ") was not in the selected state after "
						+ retries + " retries.");
			}
		}
	}

	/**
	 * Fill an auto complete field
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param acf - Object containing data to be input & option to be selected
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fill(Enum<?> key, AutoCompleteField acf)
	{
		fill(key, acf, null, "");
	}

	/**
	 * Fill an auto complete field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param acf - Object containing data to be input & option to be selected
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fill(Enum<?> key, AutoCompleteField acf, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		String sLocator_SuggestionList = (String) config.get(Config.SuggestionList);
		String sXpath_DropDownOptions = (String) config.get(Config.SuggestionListOptions);

		if (waitForReady)
			waitForElementReady(config, replacement);

		Framework.autoComplete(driver, locator, sLocator_SuggestionList, sXpath_DropDownOptions, log, acf);
	}

	/**
	 * Clicks a button<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement click(Enum<?> key)
	{
		return click(key, null, "");
	}

	/**
	 * Clicks a button<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement click(Enum<?> key, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		Framework.click(element, log);

		return element;
	}

	/**
	 * Fill a radio button option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A null pointer exception will occur if the radio button option configuration was not added<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fill(Enum<?> key, int retries)
	{
		fill(key, retries, null, "");
	}

	/**
	 * Fill a radio button option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A null pointer exception will occur if the radio button option configuration was not added<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fill(Enum<?> key, int retries, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		Framework.click(driver, locator, log, retries);

		if (verify)
		{
			Framework f = new Framework(driver);
			int poll = Framework.getPollInterval();
			int flakyTimeout = poll * f.getRetries();
			FlakinessChecks flaky = new FlakinessChecks(driver, flakyTimeout, poll);
			boolean selected = flaky.isElementSelected(locator);
			if (!selected)
				Logs.logError("Expected the radio option (" + key + ") to be selected but it was unselected");
		}
	}

	/**
	 * Fill input field
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param value - Value to be entered
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fill(Enum<?> key, InputField value, int retries)
	{
		fill(key, value, retries, null, "");
	}

	/**
	 * Fill input field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param value - Value to be entered
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fill(Enum<?> key, InputField value, int retries, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		Framework.enterField(driver, locator, log, value, retries);

		if (verify)
			Verify.inputField(driver, locator, log, retries, value);
	}

	/**
	 * Fill a check box field
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fill(Enum<?> key, CheckBox cb, int retries)
	{
		fill(key, cb, retries, null, "");
	}

	/**
	 * Fill a check box field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void fill(Enum<?> key, CheckBox cb, int retries, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		Framework.checkbox(driver, locator, log, cb, retries);

		if (verify)
			Verify.checkBox(driver, locator, log, cb, retries);
	}

	/**
	 * Fill a drop down field
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 * @return WebElement
	 */
	public void fill(Enum<?> key, DropDown dd, int retries)
	{
		fill(key, dd, retries, null, "");
	}

	/**
	 * Fill a drop down field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 * @return WebElement
	 */
	public void fill(Enum<?> key, DropDown dd, int retries, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		Framework.dropDownSelect(driver, locator, log, dd, retries);

		if (verify)
			Verify.dropDown(driver, log, dd, retries);
	}

	/**
	 * Clicks a button
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void click(Enum<?> key, int retries)
	{
		click(key, retries, null, "");
	}

	/**
	 * Clicks a button<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public void click(Enum<?> key, int retries, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);

		if (waitForReady)
			waitForElementReady(config, replacement);

		Framework.click(driver, locator, log, retries);
	}

	/**
	 * Fill a radio button option using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key)
	{
		return fillJS(key, "", "");
	}

	/**
	 * Fill a radio button option using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		JS_Util.click(element, log);

		if (verify)
		{
			Framework f = new Framework(driver);
			int poll = Framework.getPollInterval();
			int flakyTimeout = poll * f.getRetries();
			FlakinessChecks flaky = new FlakinessChecks(driver, flakyTimeout, poll);
			boolean selected = flaky.isElementSelected(element);
			if (!selected)
				Logs.logError("Expected the radio option (" + key + ") to be selected but it was unselected");
		}

		return element;
	}

	/**
	 * Fill a check box field using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key, CheckBox cb)
	{
		return fillJS(key, cb, "", "");
	}

	/**
	 * Fill a check box field using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key, CheckBox cb, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		JS_Util.checkbox(element, log, cb);

		if (verify)
			Verify.checkBox(element, log, cb);

		return element;
	}

	/**
	 * Clicks a button using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement clickJS(Enum<?> key)
	{
		return clickJS(key, null, "");
	}

	/**
	 * Clicks a button using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement clickJS(Enum<?> key, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		JS_Util.click(element, log);

		return element;
	}

	/**
	 * Clicks a button relative from an element using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param anchorElement - Anchor element from which to find relative element and click a button
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement clickJS(Enum<?> key, WebElement anchorElement)
	{
		return clickJS(key, anchorElement, null, "");
	}

	/**
	 * Clicks a button relative from an element using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param anchorElement - Anchor element from which to find relative element and click a button
	 * @param replacement - Replacement text to be used in the relative locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement clickJS(Enum<?> key, WebElement anchorElement, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);

		if (waitForReady)
			Framework.waitForElementReady(anchorElement, locator);

		WebElement element = Framework.findElement(anchorElement, locator, true);
		JS_Util.click(element, log);

		return element;
	}

	/**
	 * Clicks a button relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param anchorElement - Anchor element from which to find relative element and click a button
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement click(Enum<?> key, WebElement anchorElement)
	{
		return click(key, anchorElement, null, "");
	}

	/**
	 * Clicks a button relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param anchorElement - Anchor element from which to find relative element and click a button
	 * @param replacement - Replacement text to be used in the relative locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement click(Enum<?> key, WebElement anchorElement, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);

		if (waitForReady)
			Framework.waitForElementReady(anchorElement, locator);

		WebElement element = Framework.findElement(anchorElement, locator, true);
		Framework.click(element, log);

		return element;
	}

	/**
	 * Fill a check box field relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, CheckBox cb, WebElement anchorElement)
	{
		return fill(key, cb, anchorElement, null, "");
	}

	/**
	 * Fill a check box field relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @param replacement - Replacement text to be used in the relative locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, CheckBox cb, WebElement anchorElement, String replacement,
			String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			Framework.waitForElementReady(anchorElement, locator);

		WebElement element = Framework.findElement(anchorElement, locator, true);
		Framework.checkbox(element, log, cb);

		if (verify)
			Verify.checkBox(element, log, cb);

		return element;
	}

	/**
	 * Fill a drop down field relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, DropDown dd, WebElement anchorElement)
	{
		return fill(key, dd, anchorElement, null, "");
	}

	/**
	 * Fill a drop down field relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @param replacement - Replacement text to be used in the relative locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, DropDown dd, WebElement anchorElement, String replacement,
			String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			Framework.waitForElementReady(anchorElement, locator);

		WebElement element = Framework.findElement(anchorElement, locator, true);
		Framework.dropDownSelect(element, log, dd);

		if (verify)
			Verify.dropDown(element, dd);

		return element;
	}

	/**
	 * Fill an input field relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param value - Value to be entered
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, InputField value, WebElement anchorElement)
	{
		return fill(key, value, anchorElement, null, "");
	}

	/**
	 * Fill an input field relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param value - Value to be entered
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @param replacement - Replacement text to be used in the relative locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, InputField value, WebElement anchorElement, String replacement,
			String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			Framework.waitForElementReady(anchorElement, locator);

		WebElement element = Framework.findElement(anchorElement, locator, true);
		Framework.enterField(element, log, value);

		if (verify)
			Verify.inputField(element, log, value);

		return element;
	}

	/**
	 * Fill a radio button option relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) A null pointer exception will occur if the radio button option configuration was not added<BR>
	 * 3) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, WebElement anchorElement)
	{
		return fill(key, anchorElement, "", "");
	}

	/**
	 * Fill a radio button option relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) A null pointer exception will occur if the radio button option configuration was not added<BR>
	 * 3) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 4) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @param replacement - Replacement text to be used in the relative locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fill(Enum<?> key, WebElement anchorElement, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			Framework.waitForElementReady(anchorElement, locator);

		WebElement element = Framework.findElement(anchorElement, locator, true);
		Framework.click(element, log);

		if (verify)
		{
			Framework f = new Framework(driver);
			int poll = Framework.getPollInterval();
			int flakyTimeout = poll * f.getRetries();
			FlakinessChecks flaky = new FlakinessChecks(driver, flakyTimeout, poll);
			boolean selected = flaky.isElementSelected(element);
			if (!selected)
				Logs.logError("Expected the radio option (" + key + ") to be selected but it was unselected");
		}

		return element;
	}

	/**
	 * Fill a check box field using JavaScript relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key, CheckBox cb, WebElement anchorElement)
	{
		return fillJS(key, cb, anchorElement, "", "");
	}

	/**
	 * Fill a check box field using JavaScript relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param cb - Object that contains information about check box
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @param replacement - Replacement text to be used in the relative locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key, CheckBox cb, WebElement anchorElement, String replacement,
			String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			Framework.waitForElementReady(anchorElement, locator);

		WebElement element = Framework.findElement(anchorElement, locator, true);
		JS_Util.checkbox(element, log, cb);

		if (verify)
			Verify.checkBox(element, log, cb);

		return element;
	}

	/**
	 * Fill a drop down field using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key, DropDown dd)
	{
		return fillJS(key, dd, null, "");
	}

	/**
	 * Fill a drop down field using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @param replacement - Replacement text to be used in the locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key, DropDown dd, String replacement, String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			waitForElementReady(config, replacement);

		WebElement element = Framework.findElementAJAX(driver, locator);
		JS_Util.dropDownSelect(element, log, dd);

		if (verify)
			Verify.dropDown(element, dd);

		return element;
	}

	/**
	 * Fill a drop down field using JavaScript relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key, DropDown dd, WebElement anchorElement)
	{
		return fillJS(key, dd, anchorElement, null, "");
	}

	/**
	 * Fill a drop down field using JavaScript relative from an element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns the WebElement field such that any additional operations can be performed. For example, it
	 * may be necessary to trigger on change or on blur.<BR>
	 * 2) The target text (uses method <B>getTarget</B>) will be replaced with the replacement text if
	 * non-empty<BR>
	 * 3) Non-standard ready is not supported in this method<BR>
	 * 
	 * @param key - Enumeration to get field configuration
	 * @param dd - Object that contains information on which drop down option to select
	 * @param anchorElement - Anchor element from which to find relative element and fill
	 * @param replacement - Replacement text to be used in the relative locator
	 * @param logAppend - Text to be appended to the log variable
	 * @return WebElement
	 * @throws NullPointerException if field configuration corresponding to the key was not previously added
	 */
	public WebElement fillJS(Enum<?> key, DropDown dd, WebElement anchorElement, String replacement,
			String logAppend)
	{
		GenericData config = (GenericData) fields.get(key);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		String locator = constructLocator((String) config.get(Config.Locator), replacement);
		String log = constructLog((String) config.get(Config.Log), logAppend);
		boolean verify = (Boolean) config.get(Config.Verify);

		if (waitForReady)
			Framework.waitForElementReady(anchorElement, locator);

		WebElement element = Framework.findElement(anchorElement, locator, true);
		JS_Util.dropDownSelect(element, log, dd);

		if (verify)
			Verify.dropDown(element, dd);

		return element;
	}

	/**
	 * Get configuration variable based on specified parameters for non-standard actions
	 * 
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 * @param sLocator_SuggestionList - Locator to the Suggestion List (when it appears)
	 * @param sXpath_DropDownOptions - xpath to get all the drop down suggestion options
	 * @param attribute - HTML Attribute to be used to match HTML Value when this criteria is used or in case
	 *            of check box click the attribute used to determine if selected or not
	 * @param nonStandardReady - true to use non-standard ready method (only applies if wait for ready flag is
	 *            true)
	 * @param readyAttribute - For non-standard ready, the attribute used to determine if ready
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 * @return GenericData
	 */
	protected GenericData getGenericConfig(String locator, String log, boolean waitForReady, boolean verify,
			String sLocator_SuggestionList, String sXpath_DropDownOptions, String attribute,
			boolean nonStandardReady, String readyAttribute, Comparison enabledCriteria,
			String enabledAttributeValue, Comparison disabledCriteria, String disabledAttributeValue,
			Comparison checkedCriteria, String checkedAttributeValue, Comparison uncheckedCriteria,
			String uncheckedAttributeValue)
	{
		GenericData config = new GenericData();
		config.add(Config.Locator, locator);
		config.add(Config.Log, log);
		config.add(Config.WaitForReady, waitForReady);
		config.add(Config.Verify, verify);
		config.add(Config.SuggestionList, sLocator_SuggestionList);
		config.add(Config.SuggestionListOptions, sXpath_DropDownOptions);
		config.add(Config.Attribute, attribute);
		config.add(Config.NonStandardReady, nonStandardReady);
		config.add(Config.ReadyAttribute, readyAttribute);
		config.add(Config.EnabledCriteria, enabledCriteria);
		config.add(Config.EnabledValue, enabledAttributeValue);
		config.add(Config.DisabledCriteria, disabledCriteria);
		config.add(Config.DisabledValue, disabledAttributeValue);
		config.add(Config.CheckedCriteria, checkedCriteria);
		config.add(Config.CheckedValue, checkedAttributeValue);
		config.add(Config.UnCheckedCriteria, uncheckedCriteria);
		config.add(Config.UnCheckedValue, uncheckedAttributeValue);
		return config;
	}

	/**
	 * Get configuration variable based on specified parameters
	 * 
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 * @param sLocator_SuggestionList - Locator to the Suggestion List (when it appears)
	 * @param sXpath_DropDownOptions - xpath to get all the drop down suggestion options
	 * @param attribute - HTML Attribute to be used to match HTML Value when this criteria is used
	 * @return GenericData
	 */
	protected GenericData getGenericConfig(String locator, String log, boolean waitForReady, boolean verify,
			String sLocator_SuggestionList, String sXpath_DropDownOptions, String attribute)
	{
		GenericData config = new GenericData();
		config.add(Config.Locator, locator);
		config.add(Config.Log, log);
		config.add(Config.WaitForReady, waitForReady);
		config.add(Config.Verify, verify);
		config.add(Config.SuggestionList, sLocator_SuggestionList);
		config.add(Config.SuggestionListOptions, sXpath_DropDownOptions);
		config.add(Config.Attribute, attribute);
		config.add(Config.NonStandardReady, false);
		config.add(Config.ReadyAttribute, null);
		config.add(Config.EnabledCriteria, null);
		config.add(Config.EnabledValue, null);
		config.add(Config.DisabledCriteria, null);
		config.add(Config.DisabledValue, null);
		config.add(Config.CheckedCriteria, null);
		config.add(Config.CheckedValue, null);
		config.add(Config.UnCheckedCriteria, null);
		config.add(Config.UnCheckedValue, null);
		return config;
	}

	/**
	 * Add the field with configuration such that it can be worked with
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param config - Configuration of the field
	 */
	protected void setGenericConfig(Enum<?> key, GenericData config)
	{
		// Add the configured field such that can be worked with
		fields.add(key, config);
	}

	/**
	 * Add a radio button option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For each radio button option, this method needs to be used to add the configuration before use.<BR>
	 * 2) Verification will fail if element is not considered radio button option as it will always return
	 * false for isSelected<BR>
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 */
	public void addRadio(Enum<?> key, String locator, String log, boolean waitForReady, boolean verify)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, verify, null, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add an input field
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 */
	public void addInputField(Enum<?> key, String locator, String log, boolean waitForReady, boolean verify)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, verify, null, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add a check box field
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 */
	public void addCheckBox(Enum<?> key, String locator, String log, boolean waitForReady, boolean verify)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, verify, null, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add a drop down field
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 */
	public void addDropDown(Enum<?> key, String locator, String log, boolean waitForReady, boolean verify)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, verify, null, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add a drop down field that uses click to select
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator to find the 'drop down' options
	 * @param attribute - HTML Attribute to be used to match HTML Value when this criteria is used
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 */
	public void addDropDown(Enum<?> key, String locator, String attribute, String log, boolean waitForReady)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, false, null, null, attribute);
		setGenericConfig(key, config);
	}

	/**
	 * Add an Auto Complete Field
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 * @param sLocator_SuggestionList - Locator to the Suggestion List (when it appears)
	 * @param sXpath_DropDownOptions - xpath to get all the drop down suggestion options
	 */
	public void addAutoComplete(Enum<?> key, String locator, String log, boolean waitForReady,
			String sLocator_SuggestionList, String sXpath_DropDownOptions)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, false, sLocator_SuggestionList,
				sXpath_DropDownOptions, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add a button
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 */
	public void addButton(Enum<?> key, String locator, String log, boolean waitForReady)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, false, null, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add a check box field that is non-standard (but uses the standard ready method.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use method fillClick later to work with field<BR>
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 * @param attribute - Attribute used to determine if element is selected
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 */
	public void addCheckBox(Enum<?> key, String locator, String log, boolean waitForReady, boolean verify,
			String attribute, Comparison checkedCriteria, String checkedAttributeValue,
			Comparison uncheckedCriteria, String uncheckedAttributeValue)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, verify, null, null, attribute,
				false, null, null, null, null, null, checkedCriteria, checkedAttributeValue,
				uncheckedCriteria, uncheckedAttributeValue);
		setGenericConfig(key, config);
	}

	/**
	 * Add a check box field that is non-standard (and uses the <B>non-standard</B> ready method.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use method fillClick later to work with field<BR>
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 * @param attribute - Attribute used to determine if element is selected
	 * @param readyAttribute - Attribute used to determine if ready
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 */
	public void addCheckBox(Enum<?> key, String locator, String log, boolean waitForReady, boolean verify,
			String attribute, String readyAttribute, Comparison enabledCriteria,
			String enabledAttributeValue, Comparison disabledCriteria, String disabledAttributeValue,
			Comparison checkedCriteria, String checkedAttributeValue, Comparison uncheckedCriteria,
			String uncheckedAttributeValue)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, verify, null, null, attribute,
				true, readyAttribute, enabledCriteria, enabledAttributeValue, disabledCriteria,
				disabledAttributeValue, checkedCriteria, checkedAttributeValue, uncheckedCriteria,
				uncheckedAttributeValue);
		setGenericConfig(key, config);
	}

	/**
	 * Add a button that uses non-standard ready method
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param attribute - Attribute used to determine if ready
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 */
	public void addButton(Enum<?> key, String locator, String log, boolean waitForReady, String attribute,
			Comparison enabledCriteria, String enabledAttributeValue, Comparison disabledCriteria,
			String disabledAttributeValue)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, false, null, null, null, true,
				attribute, enabledCriteria, enabledAttributeValue, disabledCriteria, disabledAttributeValue,
				null, null, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add a radio button option that is non-standard<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use method fillClick later to work with field<BR>
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 * @param attribute - Attribute used to determine if element is selected
	 * @param readyAttribute - Attribute used to determine if ready
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 */
	public void addRadio(Enum<?> key, String locator, String log, boolean waitForReady, boolean verify,
			String attribute, String readyAttribute, Comparison enabledCriteria,
			String enabledAttributeValue, Comparison disabledCriteria, String disabledAttributeValue,
			Comparison checkedCriteria, String checkedAttributeValue)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, false, null, null, attribute, true,
				readyAttribute, enabledCriteria, enabledAttributeValue, disabledCriteria,
				disabledAttributeValue, checkedCriteria, checkedAttributeValue, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add an input field that uses non-standard ready method
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 * @param waitForReady - true to wait for the field to become ready
	 * @param verify - true to verify data after entry
	 * @param attribute - Attribute used to determine if ready
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 */
	public void addInputField(Enum<?> key, String locator, String log, boolean waitForReady, boolean verify,
			String attribute, Comparison enabledCriteria, String enabledAttributeValue,
			Comparison disabledCriteria, String disabledAttributeValue)
	{
		GenericData config = getGenericConfig(locator, log, waitForReady, verify, null, null, null, true,
				attribute, enabledCriteria, enabledAttributeValue, disabledCriteria, disabledAttributeValue,
				null, null, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add the key (that is not used) to allow verification of the configuration to be successful
	 * 
	 * @param key - Enumeration to set field configuration for
	 */
	public void add(Enum<?> key)
	{
		GenericData config = getGenericConfig("", key.toString(), false, false, null, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Add the key (that is not used) to allow verification of the configuration to be successful<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It may be necessary to store a locator/log to be retrieved from the class but custom code is
	 * necessary to work with it. For example, storing the locator for a label, later you may want to get the
	 * text for the label but there is no method in this class to do this as this action does not make sense.<BR>
	 * 
	 * @param key - Enumeration to set field configuration for
	 * @param locator - Locator for the field
	 * @param log - Logging for the field
	 */
	public void add(Enum<?> key, String locator, String log)
	{
		GenericData config = getGenericConfig(locator, log, false, false, null, null, null);
		setGenericConfig(key, config);
	}

	/**
	 * Verify Configuration of all the fields
	 */
	public void verifyConfig()
	{
		TestResults results = new TestResults();
		String sWarning;

		// Verify that all keys are stored
		fields.verify();

		// Verify the configuration keys are stored
		Set<Entry<Enum<?>, Object>> data = fields.get().entrySet();
		for (Entry<Enum<?>, Object> item : data)
		{
			GenericData config = (GenericData) item.getValue();
			for (Config option : Config.values())
			{
				sWarning = "The element (" + item.getKey() + ") was missing the configuration for " + option;
				results.expectTrue(config.containsKey(option), sWarning);
			}
		}

		String sFailure = "Some elements were not configured.  See above for details.";
		results.verify(sFailure);
	}

	/**
	 * Get Locator for key
	 * 
	 * @param key - Enumeration to get locator
	 * @return empty string if field configuration corresponding to the key was not previously added else the
	 *         locator value
	 */
	public String getLocator(Enum<?> key)
	{
		return getLocator(key, "");
	}

	/**
	 * Get Locator for key
	 * 
	 * @param key - Enumeration to get locator
	 * @param replacement - Replacement text to be used in the locator
	 * @return empty string if field configuration corresponding to the key was not previously added else the
	 *         locator value
	 */
	public String getLocator(Enum<?> key, String replacement)
	{
		GenericData config = (GenericData) fields.get(key);
		if (config == null)
		{
			return "";
		}
		else
		{
			return constructLocator((String) config.get(Config.Locator), replacement);
		}
	}

	/**
	 * Get Log for key
	 * 
	 * @param key - Enumeration to get log
	 * @return empty string if field configuration corresponding to the key was not previously added else the
	 *         log value
	 */
	public String getLog(Enum<?> key)
	{
		return getLog(key, "");
	}

	/**
	 * Get Log for key
	 * 
	 * @param key - Enumeration to get log
	 * @param append - Text to be appended to the log variable
	 * @return empty string if field configuration corresponding to the key was not previously added else the
	 *         log value
	 */
	public String getLog(Enum<?> key, String append)
	{
		GenericData config = (GenericData) fields.get(key);
		if (config == null)
		{
			return "";
		}
		else
		{
			return constructLog((String) config.get(Config.Log), append);
		}
	}
}
