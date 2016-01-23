package com.automation.ui.common.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.exceptions.DisabledActionException;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.JS_Util;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.TestResults;

/**
 * This class works with generic overlays<BR>
 * <BR>
 * <B>Example of configuration before use:</B><BR>
 * GenericOverlay overlay = new GenericOverlay(driver);<BR>
 * overlay.setConfig_OK(false, "ok", "OK", true, true, -1, GenericOverlay.ClickOption.Standard);<BR>
 * overlay.setConfig_Cancel(false, "cancel", "Cancel", true, true, -1, GenericOverlay.ClickOption.Standard);<BR>
 * overlay.setConfig_Close(false, "close", "X", true, true, -1, GenericOverlay.ClickOption.Standard);<BR>
 * overlay.setConfig_Message(false, "message", "Message", -1, GenericOverlay.TextOption.Visible, "");<BR>
 * overlay.setConfig_Title(false, "title", "Title", -1, GenericOverlay.TextOption.Visible, "");<BR>
 * overlay.verifyConfig();
 */
public class GenericOverlay {
	protected WebDriver driver;

	/**
	 * All supported configuration options for each element
	 */
	protected enum Config
	{
		Locator, // Locator for the action
		Log, // Logging for the action
		Disabled, // True to indicate that action is disabled and use causes an exception
		WaitForReady, // True to wait for the element to become ready
		WaitForRemoval, // True to wait for the element to be removed after the action
		Retries, // Number of retries if applicable
		ActionSpecific, // Stores an enumeration specific to the action to be performed
		Attribute, // Only for getting text
		NonStandardReady, // True to use non-standard ready method
		ReadyAttribute, // Attribute used in non-standard ready method
		EnabledCriteria, // Criteria for Enabled check in non-standard ready method
		EnabledValue, // Value used for the Enabled check (expected value) in non-standard ready method
		DisabledCriteria, // Criteria for Disabled check in non-standard ready method
		DisabledValue; // Value used for the Disabled check (expected value) in non-standard ready method
	}

	/**
	 * All supported configuration options for clicking the button
	 */
	public enum ClickOption
	{
		Standard, // Standard click
		UseJavaScript, // Use JavaScript to click
		Retries; // Use Standard click but retry if StaleElementReferenceException occurs
	}

	/**
	 * All supported configuration options for getting text
	 */
	public enum TextOption
	{
		Visible, // Standard visible text
		UseJavaScript, // Use JavaScript to get text
		VisbleWithRetries, // Use Standard visible text but retry if StaleElementReferenceException occurs
		Attribute, // Get text from an attribute
		AttributeJS; // Get text from an attribute using JavaScript
	}

	/**
	 * All supported action types
	 */
	protected enum ActionType
	{
		Button, // Action for a button
		Text; // Action for text
	}

	/**
	 * All supported elements
	 */
	protected enum Element
	{
		OK(ActionType.Button), //
		Cancel(ActionType.Button), //
		Close(ActionType.Button), //
		Message(ActionType.Text), //
		Title(ActionType.Text); //

		/**
		 * Store the action type
		 */
		private ActionType type;

		/**
		 * Constructor
		 * 
		 * @param type - Action Type to be set
		 */
		private Element(ActionType type)
		{
			this.type = type;
		}

		/**
		 * Get the action type
		 * 
		 * @return ActionType
		 */
		public ActionType getType()
		{
			return type;
		}
	}

	/**
	 * This contains all the available elements and corresponding configuration
	 */
	protected GenericData elements;

	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public GenericOverlay(WebDriver driver)
	{
		setDriver(driver);
		initElements();
	}

	/**
	 * Constructor
	 * 
	 * @param pageObject - Page Object used to set the driver
	 */
	public GenericOverlay(Framework pageObject)
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
	 * Initializes the elements variable if necessary
	 */
	protected void initElements()
	{
		if (elements == null)
			elements = new GenericData();
	}

	/**
	 * Click OK (or confirmation button)
	 */
	public void clickOK()
	{
		GenericData config = (GenericData) elements.get(Element.OK);
		clickGeneric(config);
	}

	/**
	 * Click Cancel (or abort button)
	 */
	public void clickCancel()
	{
		GenericData config = (GenericData) elements.get(Element.Cancel);
		clickGeneric(config);
	}

	/**
	 * Click Close (or 'X' button)
	 */
	public void clickClose()
	{
		GenericData config = (GenericData) elements.get(Element.Close);
		clickGeneric(config);
	}

	/**
	 * Get the Message (or Main) text<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The overlay needs to be ready before getting message or it could be empty.<BR>
	 * 
	 * @return non-null
	 */
	public String getMessage()
	{
		GenericData config = (GenericData) elements.get(Element.Message);
		return getTextGeneric(config);
	}

	/**
	 * Get the Title text<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The overlay needs to be ready before getting text or it could be empty.<BR>
	 * 
	 * @return non-null
	 */
	public String getTitle()
	{
		GenericData config = (GenericData) elements.get(Element.Title);
		return getTextGeneric(config);
	}

	/**
	 * Verify Configuration of all the elements
	 */
	public void verifyConfig()
	{
		TestResults results = new TestResults();
		for (Element key : Element.values())
		{
			String sWarning = "The element (" + key + ") was not configured";
			if (results.expectTrue(elements.containsKey(key), sWarning))
			{
				GenericData config = (GenericData) elements.get(key);
				for (Config option : Config.values())
				{
					sWarning = "The element (" + key + ") was missing the configuration for " + option;
					if (results.expectTrue(config.containsKey(option), sWarning))
					{
						if (option == Config.ActionSpecific)
						{
							boolean correctType = false;
							if (key.getType() == ActionType.Button)
							{
								try
								{
									@SuppressWarnings("unused")
									ClickOption action = (ClickOption) config.get(option);
									correctType = true;
								}
								catch (Exception ex)
								{
									correctType = false;
								}
							}
							else
							{
								try
								{
									@SuppressWarnings("unused")
									TextOption action = (TextOption) config.get(option);
									correctType = true;
								}
								catch (Exception ex)
								{
									correctType = false;
								}
							}

							sWarning = "The element (" + key + ") for option (" + option
									+ ") had the incorrect type for a " + key.getType();
							results.expectTrue(correctType, sWarning);
						}
					}
				}
			}
		}

		String sFailure = "Some elements were not configured.  See above for details.";
		results.verify(sFailure);
	}

	/**
	 * Get configuration variable based on specified parameters for a button<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param waitForReady - true to wait for the element to become ready
	 * @param waitForRemoval - true to wait for the element to be removed after the action
	 * @param retries - number of retries to bypass StaleElementReferenceException
	 * @param option - How to click the button (GenericOverlay.ClickOption)
	 * @return GenericData
	 */
	protected GenericData getGenericButtonConfig(boolean disabled, String locator, String log,
			boolean waitForReady, boolean waitForRemoval, int retries, ClickOption option)
	{
		return getGenericButtonConfig(disabled, locator, log, waitForReady, waitForRemoval, retries, option,
				false, null, null, null, null, null);
	}

	/**
	 * Get configuration variable based on specified parameters for a button<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param waitForReady - true to wait for the element to become ready
	 * @param waitForRemoval - true to wait for the element to be removed after the action
	 * @param retries - number of retries to bypass StaleElementReferenceException
	 * @param option - How to click the button (GenericOverlay.ClickOption)
	 * @param nonStandardReady - true to use non-standard ready method (only applies if wait for ready flag is
	 *            true)
	 * @param readyAttribute - For non-standard ready, the attribute used to determine if ready
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @return GenericData
	 */
	protected GenericData getGenericButtonConfig(boolean disabled, String locator, String log,
			boolean waitForReady, boolean waitForRemoval, int retries, ClickOption option,
			boolean nonStandardReady, String readyAttribute, Comparison enabledCriteria,
			String enabledAttributeValue, Comparison disabledCriteria, String disabledAttributeValue)
	{
		GenericData config = new GenericData();
		config.add(Config.Disabled, disabled);
		config.add(Config.Locator, locator);
		config.add(Config.Log, log);
		config.add(Config.WaitForReady, waitForReady);
		config.add(Config.WaitForRemoval, waitForRemoval);
		config.add(Config.Retries, retries);
		config.add(Config.ActionSpecific, option);
		config.add(Config.Attribute, null);
		config.add(Config.NonStandardReady, nonStandardReady);
		config.add(Config.ReadyAttribute, readyAttribute);
		config.add(Config.EnabledCriteria, enabledCriteria);
		config.add(Config.EnabledValue, enabledAttributeValue);
		config.add(Config.DisabledCriteria, disabledCriteria);
		config.add(Config.DisabledValue, disabledAttributeValue);
		return config;
	}

	/**
	 * Add the element with configuration such that it can be worked with
	 * 
	 * @param element - Element to be added
	 * @param config - Configuration of the element
	 */
	protected void setGenericConfig(Element element, GenericData config)
	{
		// Add the configured action to the elements that can be worked with
		elements.add(element, config);
	}

	/**
	 * Configures the OK element (<B>Confirmation</B> button) such that it can be worked with<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay. In
	 * general, OK element will always be available.<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param waitForReady - true to wait for the element to become ready
	 * @param waitForRemoval - true to wait for the element to be removed after the action
	 * @param retries - number of retries to bypass StaleElementReferenceException (only applicable for
	 *            GenericOverlay.ClickOption.Retries option)
	 * @param option - How to click the button (GenericOverlay.ClickOption)
	 */
	public void setConfig_OK(boolean disabled, String locator, String log, boolean waitForReady,
			boolean waitForRemoval, int retries, ClickOption option)
	{
		GenericData config = getGenericButtonConfig(disabled, locator, log, waitForReady, waitForRemoval,
				retries, option);
		setGenericConfig(Element.OK, config);
	}

	/**
	 * Configures the Cancel element (<B>Abort</B> button) such that it can be worked with<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay. This
	 * might be the case if the overlay has a message and it can only be cleared similar to an alert.<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param waitForReady - true to wait for the element to become ready
	 * @param waitForRemoval - true to wait for the element to be removed after the action
	 * @param retries - number of retries to bypass StaleElementReferenceException (only applicable for
	 *            GenericOverlay.ClickOption.Retries option)
	 * @param option - How to click the button (GenericOverlay.ClickOption)
	 */
	public void setConfig_Cancel(boolean disabled, String locator, String log, boolean waitForReady,
			boolean waitForRemoval, int retries, ClickOption option)
	{
		GenericData config = getGenericButtonConfig(disabled, locator, log, waitForReady, waitForRemoval,
				retries, option);
		setGenericConfig(Element.Cancel, config);
	}

	/**
	 * Configures the Close element (<B>X</B> button) such that it can be worked with<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay. This
	 * might be the case if the overlay has a message and it can only be cleared similar to an alert.<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param waitForReady - true to wait for the element to become ready
	 * @param waitForRemoval - true to wait for the element to be removed after the action
	 * @param retries - number of retries to bypass StaleElementReferenceException (only applicable for
	 *            GenericOverlay.ClickOption.Retries option)
	 * @param option - How to click the button (GenericOverlay.ClickOption)
	 */
	public void setConfig_Close(boolean disabled, String locator, String log, boolean waitForReady,
			boolean waitForRemoval, int retries, ClickOption option)
	{
		GenericData config = getGenericButtonConfig(disabled, locator, log, waitForReady, waitForRemoval,
				retries, option);
		setGenericConfig(Element.Close, config);
	}

	/**
	 * Generic Click
	 * 
	 * @param config - Configuration to determine behavior/actions
	 */
	protected void clickGeneric(GenericData config)
	{
		boolean disabled = (Boolean) config.get(Config.Disabled);
		boolean waitForReady = (Boolean) config.get(Config.WaitForReady);
		int retries = (Integer) config.get(Config.Retries);
		String locator = (String) config.get(Config.Locator);
		String log = (String) config.get(Config.Log);
		boolean waitForRemoval = (Boolean) config.get(Config.WaitForRemoval);
		ClickOption action = (ClickOption) config.get(Config.ActionSpecific);

		if (disabled)
		{
			String sError = "Clicking (" + log + ") was disabled by configuration in the class";
			DisabledActionException runtime = new DisabledActionException(sError);
			Logs.logError(runtime);
		}

		if (waitForReady)
		{
			boolean nonStandardReady = (Boolean) config.get(Config.NonStandardReady);
			if (nonStandardReady)
			{
				String attribute = (String) config.get(Config.ReadyAttribute);
				Comparison enabledCriteria = (Comparison) config.get(Config.EnabledCriteria);
				String enabledAttributeValue = (String) config.get(Config.EnabledValue);
				Comparison disabledCriteria = (Comparison) config.get(Config.DisabledCriteria);
				String disabledAttributeValue = (String) config.get(Config.DisabledValue);
				Framework.waitForElementReady(driver, locator, attribute, enabledCriteria,
						enabledAttributeValue, disabledCriteria, disabledAttributeValue);
			}
			else
			{
				Framework.waitForElementReady(driver, locator);
			}
		}

		if (action == ClickOption.Retries)
		{
			Framework.click(driver, locator, log, retries);
		}
		else
		{
			WebElement element = Framework.findElementAJAX(driver, locator);
			if (action == ClickOption.UseJavaScript)
				JS_Util.click(element, log);
			else
				Framework.click(element, log);
		}

		if (waitForRemoval)
		{
			Framework.waitForElementRemoval(driver, locator);
		}
	}

	/**
	 * Get configuration variable based on specified parameters for text<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param retries - number of retries to bypass StaleElementReferenceException (only applicable for
	 *            GenericOverlay.TextOption.VisbleWithRetries option)
	 * @param option - How to get the text (GenericOverlay.TextOption)
	 * @param attribute - Attribute to get text from if necessary based on option
	 * @return GenericData
	 */
	protected GenericData getGenericTextConfig(boolean disabled, String locator, String log, int retries,
			TextOption option, String attribute)
	{
		GenericData config = new GenericData();
		config.add(Config.Disabled, disabled);
		config.add(Config.Locator, locator);
		config.add(Config.Log, log);
		config.add(Config.WaitForReady, null);
		config.add(Config.WaitForRemoval, null);
		config.add(Config.Retries, retries);
		config.add(Config.ActionSpecific, option);
		config.add(Config.Attribute, attribute);
		config.add(Config.NonStandardReady, null);
		config.add(Config.ReadyAttribute, null);
		config.add(Config.EnabledCriteria, null);
		config.add(Config.EnabledValue, null);
		config.add(Config.DisabledCriteria, null);
		config.add(Config.DisabledValue, null);
		return config;
	}

	/**
	 * Configures the Message element (<B>Main Text</B>) such that it can be retrieved<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param retries - number of retries to bypass StaleElementReferenceException (only applicable for
	 *            GenericOverlay.TextOption.VisbleWithRetries option)
	 * @param option - How to get the text (GenericOverlay.TextOption)
	 * @param attribute - Attribute to get text from if necessary based on option
	 */
	public void setConfig_Message(boolean disabled, String locator, String log, int retries,
			TextOption option, String attribute)
	{
		GenericData config = getGenericTextConfig(disabled, locator, log, retries, option, attribute);
		setGenericConfig(Element.Message, config);
	}

	/**
	 * Configures the Title element (<B>Title Text</B>) such that it can be retrieved<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param retries - number of retries to bypass StaleElementReferenceException (only applicable for
	 *            GenericOverlay.TextOption.VisbleWithRetries option)
	 * @param option - How to get the text (GenericOverlay.TextOption)
	 * @param attribute - Attribute to get text from if necessary based on option
	 */
	public void setConfig_Title(boolean disabled, String locator, String log, int retries, TextOption option,
			String attribute)
	{
		GenericData config = getGenericTextConfig(disabled, locator, log, retries, option, attribute);
		setGenericConfig(Element.Title, config);
	}

	/**
	 * Configures the OK element (<B>Confirmation</B> button) for non-standard ready such that it can be
	 * worked with<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay. In
	 * general, OK element will always be available.<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param waitForReady - true to wait for the element to become ready
	 * @param waitForRemoval - true to wait for the element to be removed after the action
	 * @param readyAttribute - For non-standard ready, the attribute used to determine if ready
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param retries - number of retries to bypass StaleElementReferenceException (only applicable for
	 *            GenericOverlay.ClickOption.Retries option)
	 * @param option - How to click the button (GenericOverlay.ClickOption)
	 */
	public void setConfig_OK(boolean disabled, String locator, String log, boolean waitForReady,
			boolean waitForRemoval, String readyAttribute, Comparison enabledCriteria,
			String enabledAttributeValue, Comparison disabledCriteria, String disabledAttributeValue,
			int retries, ClickOption option)
	{
		GenericData config = getGenericButtonConfig(disabled, locator, log, waitForReady, waitForRemoval,
				retries, option, true, readyAttribute, enabledCriteria, enabledAttributeValue,
				disabledCriteria, disabledAttributeValue);
		setGenericConfig(Element.OK, config);
	}

	/**
	 * Configures the Cancel element (<B>Abort</B> button) for non-standard ready such that it can be worked
	 * with<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay. This
	 * might be the case if the overlay has a message and it can only be cleared similar to an alert.<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param waitForReady - true to wait for the element to become ready
	 * @param waitForRemoval - true to wait for the element to be removed after the action
	 * @param readyAttribute - For non-standard ready, the attribute used to determine if ready
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param retries - number of retries to bypass StaleElementReferenceException (only applicable for
	 *            GenericOverlay.ClickOption.Retries option)
	 * @param option - How to click the button (GenericOverlay.ClickOption)
	 */
	public void setConfig_Cancel(boolean disabled, String locator, String log, boolean waitForReady,
			boolean waitForRemoval, String readyAttribute, Comparison enabledCriteria,
			String enabledAttributeValue, Comparison disabledCriteria, String disabledAttributeValue,
			int retries, ClickOption option)
	{
		GenericData config = getGenericButtonConfig(disabled, locator, log, waitForReady, waitForRemoval,
				retries, option, true, readyAttribute, enabledCriteria, enabledAttributeValue,
				disabledCriteria, disabledAttributeValue);
		setGenericConfig(Element.Cancel, config);
	}

	/**
	 * Configures the Close element (<B>X</B> button) for non-standard ready such that it can be worked with<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set disabled to true if the action is not to be used or not applicable for the overlay. This
	 * might be the case if the overlay has a message and it can only be cleared similar to an alert.<BR>
	 * 
	 * @param disabled - true to indicate that action is disabled and use causes an exception
	 * @param locator - Locator for the action
	 * @param log - Logging for the action
	 * @param waitForReady - true to wait for the element to become ready
	 * @param waitForRemoval - true to wait for the element to be removed after the action
	 * @param readyAttribute - For non-standard ready, the attribute used to determine if ready
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param retries - number of retries to bypass StaleElementReferenceException (only applicable for
	 *            GenericOverlay.ClickOption.Retries option)
	 * @param option - How to click the button (GenericOverlay.ClickOption)
	 */
	public void setConfig_Close(boolean disabled, String locator, String log, boolean waitForReady,
			boolean waitForRemoval, String readyAttribute, Comparison enabledCriteria,
			String enabledAttributeValue, Comparison disabledCriteria, String disabledAttributeValue,
			int retries, ClickOption option)
	{
		GenericData config = getGenericButtonConfig(disabled, locator, log, waitForReady, waitForRemoval,
				retries, option, true, readyAttribute, enabledCriteria, enabledAttributeValue,
				disabledCriteria, disabledAttributeValue);
		setGenericConfig(Element.Close, config);
	}

	/**
	 * Generic get text
	 * 
	 * @param config - Configuration to determine behavior
	 * @return non-null
	 */
	protected String getTextGeneric(GenericData config)
	{
		boolean disabled = (Boolean) config.get(Config.Disabled);
		int retries = (Integer) config.get(Config.Retries);
		String locator = (String) config.get(Config.Locator);
		String log = (String) config.get(Config.Log);
		TextOption action = (TextOption) config.get(Config.ActionSpecific);
		String attribute = (String) config.get(Config.Attribute);

		if (disabled)
		{
			String sError = "Getting text (" + log + ") was disabled by configuration in the class";
			DisabledActionException runtime = new DisabledActionException(sError);
			Logs.logError(runtime);
		}

		String text;
		if (action == TextOption.VisbleWithRetries)
		{
			text = Framework.getText(driver, locator, retries);
		}
		else
		{
			WebElement element = Framework.findElementAJAX(driver, locator);
			if (action == TextOption.Attribute)
				text = Framework.getAttribute(element, attribute);
			else if (action == TextOption.AttributeJS)
				text = JS_Util.getAttribute(element, attribute);
			else if (action == TextOption.UseJavaScript)
				text = JS_Util.getText(element);
			else
				text = Framework.getText(element);
		}

		return Conversion.nonNull(text);
	}

	/**
	 * Generic Wait For Ready
	 * 
	 * @param config - Configuration to get locator
	 */
	protected void waitForReady(GenericData config)
	{
		String locator = (String) config.get(Config.Locator);
		Framework.waitForElementReady(driver, locator);
	}

	/**
	 * Wait for the OK element to be ready. (Can be used to wait for the overlay to be ready.)
	 */
	public void waitForReady_OK()
	{
		GenericData config = (GenericData) elements.get(Element.OK);
		waitForReady(config);
	}

	/**
	 * Wait for the Cancel element to be ready. (Can be used to wait for the overlay to be ready.)
	 */
	public void waitForReady_Cancel()
	{
		GenericData config = (GenericData) elements.get(Element.Cancel);
		waitForReady(config);
	}

	/**
	 * Wait for the Close element to be ready. (Can be used to wait for the overlay to be ready.)
	 */
	public void waitForReady_Close()
	{
		GenericData config = (GenericData) elements.get(Element.Close);
		waitForReady(config);
	}

	/**
	 * Updates configuration of the OK button to cause a type error for Config.ActionSpecific<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is only for unit testing<BR>
	 * 2) Use reflect to call method<BR>
	 */
	protected void causeButtonTypeError()
	{
		GenericData config = (GenericData) elements.get(Element.OK);
		config.add(Config.ActionSpecific, TextOption.Visible);
	}

	/**
	 * Updates configuration of the Message element to cause a type error for Config.ActionSpecific<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is only for unit testing<BR>
	 * 2) Use reflect to call method<BR>
	 */
	protected void causeTextTypeError()
	{
		GenericData config = (GenericData) elements.get(Element.Message);
		config.add(Config.ActionSpecific, ClickOption.Standard);
	}
}
