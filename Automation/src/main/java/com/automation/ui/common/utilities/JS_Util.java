package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.DropDownDefaults;
import com.automation.ui.common.dataStructures.HTML_Event;
import com.automation.ui.common.dataStructures.LogErrorLevel;
import com.automation.ui.common.dataStructures.MouseEvent;
import com.automation.ui.common.dataStructures.Selection;
import com.automation.ui.common.dataStructures.config.ConfigJS;
import com.automation.ui.common.exceptions.CheckBoxNoSuchElementException;
import com.automation.ui.common.exceptions.CheckBoxNotEnabled;
import com.automation.ui.common.exceptions.CheckBoxWrongStateException;
import com.automation.ui.common.exceptions.DropDownIndexException;
import com.automation.ui.common.exceptions.DropDownNoSuchElementException;
import com.automation.ui.common.exceptions.DropDownPartialMatchException;
import com.automation.ui.common.exceptions.DropDownSelectionException;
import com.automation.ui.common.exceptions.ElementNotDisplayedException;
import com.automation.ui.common.exceptions.ElementNotEnabledException;
import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.exceptions.JavaScriptException;

/**
 * This class provides utility methods to work with JavaScript
 */
public class JS_Util {
	/*
	 * The JavaScript used for the entire class. Loaded only once for performance reasons.
	 */
	private static final String _JS_Click = Misc.readFile(ConfigJS._Click);
	private static final String _JS_ClickWithAlert = Misc.readFile(ConfigJS._ClickWithAlert);
	private static final String _JS_GetAttribute = Misc.readFile(ConfigJS._GetAttribute);
	private static final String _JS_GetChildren = Misc.readFile(ConfigJS._GetChildren);
	private static final String _JS_GetParent = Misc.readFile(ConfigJS._GetParent);
	private static final String _JS_GetSiblings = Misc.readFile(ConfigJS._GetSiblings);
	private static final String _JS_GetText = Misc.readFile(ConfigJS._GetText);
	private static final String _JS_GetValue = Misc.readFile(ConfigJS._GetValue);
	private static final String _JS_HTML_Event = Misc.readFile(ConfigJS._HTML_Event);
	private static final String _JS_MaximizeWindow = Misc.readFile(ConfigJS._MaximizeWindow);
	private static final String _JS_MouseEvent = Misc.readFile(ConfigJS._MouseEvent);
	private static final String _JS_MoveWindow = Misc.readFile(ConfigJS._MoveWindow);
	private static final String _JS_ResizeWindow = Misc.readFile(ConfigJS._ResizeWindow);
	private static final String _JS_ScrollIntoView = Misc.readFile(ConfigJS._ScrollIntoView);
	private static final String _JS_GetNonWatermark = Misc.readFile(ConfigJS._GetNonWatermarkInputValue);
	private static final String _JS_IsVisible = Misc.readFile(ConfigJS._IsVisible);
	private static final String _JS_ScrollToBottom = Misc.readFile(ConfigJS._ScrollToBottom);
	private static final String _JS_GetChecked = Misc.readFile(ConfigJS._GetChecked);
	private static final String _JS_GetTable = Misc.readFile(ConfigJS._GetTable);
	private static final String _JS_GetDropDownOptions = Misc.readFile(ConfigJS._GetDropDownOptions);
	private static final String _JS_SetDropDown = Misc.readFile(ConfigJS._SetDropDownByIndex);
	private static final String _JS_ClearValue = Misc.readFile(ConfigJS._ClearValue);

	/**
	 * Selects check box if unselected using JavaScript
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state
	 * @param bLogError - if true then log error else log warning
	 */
	public static void check(WebElement element, String sElementName, boolean bVerifyInitialState,
			boolean bLogError)
	{
		check(element, sElementName, bVerifyInitialState, LogErrorLevel.convert(bLogError));
	}

	/**
	 * Selects check box if unselected using JavaScript
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state
	 * @param level - Control error logging written
	 */
	private static void check(WebElement element, String sElementName, boolean bVerifyInitialState,
			LogErrorLevel level)
	{
		try
		{
			// Does user want to enforce state of unselected before selecting check box
			if (bVerifyInitialState)
			{
				if (element.isSelected())
					throw new CheckBoxWrongStateException("");
			}

			// Click check box only if it is not already selected
			if (!element.isSelected())
			{
				WebDriver useDriver = Framework.getWebDriver(element);
				((JavascriptExecutor) useDriver).executeScript(_JS_Click, element);
				Logs.log.info("Successfully selected check box for '" + sElementName
						+ "' successfully using JavaScript");
			}
			else
			{
				Logs.log.info("Check box for '" + sElementName + "' was already selected");
			}
		}
		catch (CheckBoxWrongStateException cbwse)
		{
			String sError = "Required initial state of the check box '" + sElementName
					+ "' was not correct for the check operation";
			Logs.logWarnError(level, new CheckBoxWrongStateException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
	}

	/**
	 * Unselects check box if selected using JavaScript
	 * 
	 * @param element - Check box to unselect
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state
	 * @param bLogError - if true then log error else log warning
	 */
	public static void uncheck(WebElement element, String sElementName, boolean bVerifyInitialState,
			boolean bLogError)
	{
		uncheck(element, sElementName, bVerifyInitialState, LogErrorLevel.convert(bLogError));
	}

	/**
	 * Unselects check box if selected using JavaScript
	 * 
	 * @param element - Check box to unselect
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state
	 * @param level - Control error logging written
	 */
	private static void uncheck(WebElement element, String sElementName, boolean bVerifyInitialState,
			LogErrorLevel level)
	{
		try
		{
			// Does user want to enforce state of selected before unselecting check box
			if (bVerifyInitialState)
			{
				if (!element.isSelected())
					throw new CheckBoxWrongStateException("");
			}

			// Click check box only if it is not already selected
			if (element.isSelected())
			{
				WebDriver useDriver = Framework.getWebDriver(element);
				((JavascriptExecutor) useDriver).executeScript(_JS_Click, element);
				Logs.log.info("Successfully unselected check box for '" + sElementName
						+ "' successfully using JavaScript");
			}
			else
			{
				Logs.log.info("Check box for '" + sElementName + "' was already unselected");
			}
		}
		catch (CheckBoxWrongStateException cbwse)
		{
			String sError = "Required initial state of the check box '" + sElementName
					+ "' was not correct for the uncheck operation";
			Logs.logWarnError(level, new CheckBoxWrongStateException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
	}

	/**
	 * Selects, Unselects or skips a check box using JavaScript
	 * 
	 * @param element - Check box to work with
	 * @param sElementName - Element Name to log
	 * @param checkbox - Object that contains information about check box
	 */
	public static void checkbox(WebElement element, String sElementName, CheckBox checkbox)
	{
		boolean bEnabled = Framework.isElementEnabled(element);
		boolean bChecked = Framework.isElementSelected(element);

		if (checkbox.skip)
		{
			String sMessage = "Check box for '" + sElementName + "' was skipped.  The check box was ";
			if (bEnabled)
				sMessage += "enabled";
			else
				sMessage += "disabled";

			if (bChecked)
				sMessage += " and selected";
			else
				sMessage += " and not selected";

			Logs.log.info(sMessage);
			return;
		}

		if (bEnabled)
		{
			if (checkbox.check)
				check(element, sElementName, checkbox.verifyInitialState, checkbox.logError);
			else
				uncheck(element, sElementName, checkbox.verifyInitialState, checkbox.logError);
		}
		else
		{
			if (checkbox.verifyEnabled)
			{
				String sError = "Check box for '" + sElementName + "' was disabled";
				Logs.logError(new CheckBoxNotEnabled(sError));
			}

			if (bChecked != checkbox.check)
			{
				String sError = "Check box for '" + sElementName
						+ "' was disabled and it was not in the correct desired state (" + checkbox.check
						+ ")";
				Logs.logError(new CheckBoxNotEnabled(sError));
			}
			else
			{
				String sWarn = "Check box for '" + sElementName
						+ "' was disabled but it was in the correct desired state (" + checkbox.check + ")";
				Logs.log.warn(sWarn);
			}
		}
	}

	/**
	 * Clicks specified element with logging using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only recommended to use if WebDriver is calculating the element's coordinates incorrectly which
	 * causes the incorrect element to be clicked.<BR>
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 */
	public static void click(WebElement element, String sElementName)
	{
		try
		{
			if (!element.isEnabled())
			{
				throw new ElementNotEnabledException("Element was not enabled");
			}

			WebDriver useDriver = Framework.getWebDriver(element);
			((JavascriptExecutor) useDriver).executeScript(_JS_Click, element);
			Logs.log.info("Clicked '" + sElementName + "' successfully using JavaScript");
		}
		catch (Exception ex)
		{
			String sError = "Could not click '" + sElementName + "' due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(sError);
		}
	}

	/**
	 * Clicks specified element with logging using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Suppresses any alerts that might appear<BR>
	 * 2) Only recommended to use if WebDriver is calculating the element's coordinates incorrectly which
	 * causes the incorrect element to be clicked.<BR>
	 * 
	 * @param element - Element to click
	 * @param bAccept - true to click 'OK' (accept)
	 * @param sElementName - Element Name to log
	 */
	public static void click(WebElement element, boolean bAccept, String sElementName)
	{
		try
		{
			if (!element.isEnabled())
			{
				throw new ElementNotEnabledException("Element was not enabled");
			}

			// Click the element using constructed JavaScript
			WebDriver useDriver = Framework.getWebDriver(element);
			((JavascriptExecutor) useDriver).executeScript(_JS_ClickWithAlert, element, bAccept);

			if (bAccept)
			{
				Logs.log.info("Clicked '" + sElementName
						+ "' successfully using JavaScript any alerts suppressed using OK");
			}
			else
			{
				Logs.log.info("Clicked '" + sElementName
						+ "' successfully using JavaScript any alerts suppressed using CANCEL");
			}
		}
		catch (Exception ex)
		{
			String sError = "Could not click '" + sElementName + "' due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(sError);
		}
	}

	/**
	 * Gets the attribute value on the WebElement using JavaScript
	 * 
	 * @param element - Element to look for attribute
	 * @param sAttribute - Attribute for which to get value
	 * @return null if value not set else attribute's current value
	 */
	public static String getAttribute(WebElement element, String sAttribute)
	{
		WebDriver userDriver = Framework.getWebDriver(element);
		return (String) execute(_JS_GetAttribute, userDriver, element, sAttribute);
	}

	/**
	 * Gets the text for the element using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Text may not necessarily be visible to user<BR>
	 * 2) It may be necessary to trim the text returned<BR>
	 * 
	 * @param element - element to get text from
	 * @return non-null
	 */
	public static String getText(WebElement element)
	{
		WebDriver userDriver = Framework.getWebDriver(element);
		String sResult = (String) execute(userDriver, _JS_GetText, element);
		return Conversion.nonNull(sResult);
	}

	/**
	 * Gets the input value on the WebElement (field)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For input fields the attribute "value" contains the entered data<BR>
	 * 
	 * @param element - Field for which to get input value
	 * @return non-null
	 */
	public static String getInputValue(WebElement element)
	{
		return Conversion.nonNull(getAttribute(element, Framework.getInputAttr()));
	}

	/**
	 * Moves window to specified coordinates
	 * 
	 * @param driver
	 * @param nCoordinateX - X
	 * @param nCoordinateY - Y
	 */
	public static void moveWindow(WebDriver driver, int nCoordinateX, int nCoordinateY)
	{
		execute(_JS_MoveWindow, driver, nCoordinateX, nCoordinateY);
	}

	/**
	 * Moves window to (0,0) & re-sizes window to maximum size
	 * 
	 * @param driver
	 */
	public static void maximizeWindow(WebDriver driver)
	{
		moveWindow(driver, 0, 0);
		execute(driver, _JS_MaximizeWindow, null);
	}

	/**
	 * Resizes window to specified dimensions but does not move window's position to (0,0)
	 * 
	 * @param driver
	 * @param nWidth - Width
	 * @param nHeight - Height
	 */
	public static void resizeWindow(WebDriver driver, int nWidth, int nHeight)
	{
		execute(_JS_ResizeWindow, driver, nWidth, nHeight);
	}

	/**
	 * Attempts to scroll into view the specified WebElement.
	 * 
	 * @param element - WebElement to scroll into view
	 * @return - false if an exception occurs else true
	 */
	public static boolean scrollIntoView(WebElement element)
	{
		return scrollIntoView(element, true);
	}

	/**
	 * Attempts to scroll into view the specified WebElement.
	 * 
	 * @param element - WebElement to scroll into view
	 * @param alignToTop - If true, the top of the element will be aligned to the top of the visible area of
	 *            the scrollable ancestor. If false, the bottom of the element will be aligned to the bottom
	 *            of the visible area of the scrollable ancestor.
	 * @return - false if an exception occurs else true
	 */
	public static boolean scrollIntoView(WebElement element, boolean alignToTop)
	{
		try
		{
			/*
			 * Trick to get real element that can return the WebDriver.
			 * Note:
			 * 1) If you use element directly it is a proxy and this cannot be cast to RemoteWebElement
			 * 2) If WebElement cannot be bound, then this will generate an exception
			 */
			WebElement realElement = element.findElement(By.xpath("."));

			// Get the WebDriver object from the real (bound) WebElement
			WebDriver useDriver = ((RemoteWebElement) realElement).getWrappedDriver();

			// Execute JavaScript to scroll into view
			((JavascriptExecutor) useDriver).executeScript(_JS_ScrollIntoView, element, alignToTop);

			return true;
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Trigger mouse event using JavaScript
	 * 
	 * @param event - Mouse Event Object
	 * @param element - Element to trigger mouse event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void triggerMouseEvent(MouseEvent event, WebElement element, String sLog, boolean bLogAll)
	{
		try
		{
			WebDriver useDriver = Framework.getWebDriver(element);
			((JavascriptExecutor) useDriver).executeScript(_JS_MouseEvent, element, event.type.toString(),
					event.canBubble, event.cancelable, event.view, event.detail, event.screenX,
					event.screenY, event.clientX, event.clientY, event.ctrlKey, event.altKey, event.shiftKey,
					event.metaKey, event.button, event.relatedTarget);

			if (bLogAll)
				Logs.log.info("Triggered " + event.type.toString() + " event for '" + sLog + "' successfully");
		}
		catch (Exception ex)
		{
			String sError = "Could not trigger " + event.type.toString() + " event for '" + sLog
					+ "' due to exception [" + ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(sError);
		}
	}

	/**
	 * Trigger HTML event using JavaScript
	 * 
	 * @param event - HTML Event Object
	 * @param element - Element to trigger HTML event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void triggerHTMLEvent(HTML_Event event, WebElement element, String sLog, boolean bLogAll)
	{
		try
		{
			WebDriver useDriver = Framework.getWebDriver(element);
			((JavascriptExecutor) useDriver).executeScript(_JS_HTML_Event, element, event.type.toString(),
					event.bubbles, event.cancelable);

			if (bLogAll)
				Logs.log.info("Triggered " + event.type.toString() + " event for '" + sLog + "' successfully");
		}
		catch (Exception ex)
		{
			String sError = "Could not trigger " + event.type.toString() + " event for '" + sLog
					+ "' due to exception [" + ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(sError);
		}
	}

	/**
	 * Executes JavaScript command (Logging is specified)
	 * 
	 * @param driver
	 * @param sJS - JavaScript to execute
	 * @param bLog - true for logging
	 * @return false if any exception occurred else true
	 */
	public static boolean execute(WebDriver driver, String sJS, boolean bLog)
	{
		try
		{
			((JavascriptExecutor) driver).executeScript(sJS);
			if (bLog)
				Logs.log.info("Executed:  " + Misc.removeEndsWith(sJS, "\n"));

			return true;
		}
		catch (Exception ex)
		{
			if (bLog)
			{
				String sError = "Exception occurred executing following javascript:  " + sJS
						+ Framework.getNewLine() + "Exception was following:  " + ex.getMessage()
						+ Framework.getNewLine();
				Logs.log.warn(sError);
			}
		}

		return false;
	}

	/**
	 * Executes JavaScript command
	 * 
	 * @param driver
	 * @param sJS - JavaScript to execute.
	 */
	public static void execute(WebDriver driver, String sJS)
	{
		if (!execute(driver, sJS, true))
		{
			String sError = "Javascript execution failed";
			Logs.logError(new JavaScriptException(sError));
		}
	}

	/**
	 * Executes JavaScript in the context of the currently selected frame or window. The script fragment
	 * provided will be executed as the body of an anonymous function.<BR>
	 * <BR>
	 * Within the script, use document to refer to the current document. Note that local variables will not be
	 * available once the script has finished executing, though global variables will persist.<BR>
	 * <BR>
	 * If the script has a return value (i.e. if the script contains a return statement), then the following
	 * steps will be taken:<BR>
	 * 1) For an HTML element, this method returns a WebElement<BR>
	 * 2) For a decimal, a Double is returned<BR>
	 * 3) For a non-decimal number, a Long is returned<BR>
	 * 4) For a boolean, a Boolean is returned<BR>
	 * 5) For all other cases, a String is returned.<BR>
	 * 6) For an array, return a List&lt;Object&gt; with each object following the rules above. We support
	 * nested lists.<BR>
	 * 7) Unless the value is null or there is no return value, in which null is returned<BR>
	 * <BR>
	 * Arguments must be a number, a boolean, a String, WebElement, or a List of any combination of the above.
	 * If not, then the script will not run and return null; Pass null for args if no arguments.<BR>
	 * <BR>
	 * <B>Additional Notes:</B><BR>
	 * 1) You can return an object and cast it to a Map<BR>
	 * 2) In the JavaScript the arguments can be accessed using the variable <B>arguments[X]</B> where X is
	 * the index of the variable<BR>
	 * 
	 * @param driver
	 * @param sJS - JavaScript to execute
	 * @param args - Arguments that will be made available to the JavaScript (Use null if no arguments.)
	 * @return null or One of Boolean, Long, String, List or WebElement
	 */
	public static Object execute(WebDriver driver, String sJS, Object args)
	{
		try
		{
			if (driver == null)
				throw new JavaScriptException("");

			if (args == null)
				return ((JavascriptExecutor) driver).executeScript(sJS);
			else
				return ((JavascriptExecutor) driver).executeScript(sJS, args);
		}
		catch (JavaScriptException jse)
		{
			String sWarn = "The JavaScript could not be executed because the WebDriver was null";
			Logs.log.warn(sWarn);
			return null;
		}
		catch (Exception ex)
		{
			String sWarn = "The JavaScript ('" + sJS + "') generated the following exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.log.warn(sWarn);
			return null;
		}
	}

	/**
	 * Executes JavaScript in the context of the currently selected frame or window. The script fragment
	 * provided will be executed as the body of an anonymous function.<BR>
	 * <BR>
	 * Within the script, use document to refer to the current document. Note that local variables will not be
	 * available once the script has finished executing, though global variables will persist.<BR>
	 * <BR>
	 * If the script has a return value (i.e. if the script contains a return statement), then the following
	 * steps will be taken:<BR>
	 * 1) For an HTML element, this method returns a WebElement<BR>
	 * 2) For a decimal, a Double is returned<BR>
	 * 3) For a non-decimal number, a Long is returned<BR>
	 * 4) For a boolean, a Boolean is returned<BR>
	 * 5) For all other cases, a String is returned.<BR>
	 * 6) For an array, return a List&lt;Object&gt; with each object following the rules above. We support
	 * nested lists.<BR>
	 * 7) Unless the value is null or there is no return value, in which null is returned<BR>
	 * <BR>
	 * Arguments must be a number, a boolean, a String, WebElement, or a List of any combination of the above.
	 * If not, then the script will not run and return null; Pass null for args if no arguments.<BR>
	 * <BR>
	 * <B>Additional Notes:</B><BR>
	 * 1) You can return an object and cast it to a Map<BR>
	 * 2) In the JavaScript the arguments can be accessed using the variable <B>arguments[X]</B> where X is
	 * the index of the variable<BR>
	 * 
	 * @param sJS - JavaScript to execute
	 * @param driver
	 * @param args - Arguments that will be made available to the JavaScript (Use null if no arguments.)
	 * @return null or One of Boolean, Long, String, List or WebElement
	 */
	public static Object execute(String sJS, WebDriver driver, Object... args)
	{
		try
		{
			if (driver == null)
				throw new JavaScriptException("");

			if (args == null)
				return ((JavascriptExecutor) driver).executeScript(sJS);
			else
				return ((JavascriptExecutor) driver).executeScript(sJS, args);
		}
		catch (JavaScriptException jse)
		{
			String sWarn = "The JavaScript could not be executed because the WebDriver was null";
			Logs.log.warn(sWarn);
			return null;
		}
		catch (Exception ex)
		{
			String sWarn = "The JavaScript ('" + sJS + "') generated the following exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.log.warn(sWarn);
			return null;
		}
	}

	/**
	 * Get Parent WebElement
	 * 
	 * @param element - Element for which to get Parent
	 * @return WebElement
	 */
	public static WebElement getParent(WebElement element)
	{
		WebDriver driver = Framework.getWebDriver(element);
		return (WebElement) execute(driver, _JS_GetParent, element);
	}

	/**
	 * Get Siblings WebElements excluding itself
	 * 
	 * @param element - Element for which to get Siblings
	 * @return List&lt;WebElement&gt;
	 */
	@SuppressWarnings("unchecked")
	public static List<WebElement> getSiblings(WebElement element)
	{
		WebDriver driver = Framework.getWebDriver(element);
		return (List<WebElement>) execute(driver, _JS_GetSiblings, element);
	}

	/**
	 * Get Children WebElements
	 * 
	 * @param element - Element for which to get Children
	 * @return List&lt;WebElement&gt;
	 */
	@SuppressWarnings("unchecked")
	public static List<WebElement> getChildren(WebElement element)
	{
		WebDriver driver = Framework.getWebDriver(element);
		return (List<WebElement>) execute(driver, _JS_GetChildren, element);
	}

	/**
	 * Gets the input value on the WebElement (field) taking into consideration if it is the default
	 * text/watermark<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For input fields the attribute "value" contains the entered data<BR>
	 * 2) If the normal getInputValue method is returning the default text/watermark, then this method will
	 * return the empty string which you probably expect<BR>
	 * 
	 * @param element - Field for which to get input value
	 * @return empty string if error or default text/watermark is the input value else the input value
	 */
	public static String getNonWatermarkInputValue(WebElement element)
	{
		WebDriver driver = Framework.getWebDriver(element);
		return Conversion.nonNull((String) execute(_JS_GetNonWatermark, driver, element));
	}

	/**
	 * Gets the input value on the WebElement (field)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method returns the value property (which can be different from the value attribute or
	 * undefined)<BR>
	 * 2) If the normal getInputValue method is returning the empty string, then this method will return the
	 * value property which should be the user's input<BR>
	 * 
	 * @param element - Field for which to get input value
	 * @return empty string if error or value property is undefined else the value property
	 */
	public static String getValue(WebElement element)
	{
		WebDriver driver = Framework.getWebDriver(element);
		return Conversion.nonNull((String) execute(_JS_GetValue, driver, element));
	}

	/**
	 * Checks if the element is visible<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) There are some cases in which this method returns the incorrect value<BR>
	 * 2) I have even found a case in which the method never returned the correct state for a specific element<BR>
	 * 3) This method is more accurate (but still incorrect in some cases) than WebDriver<BR>
	 * 
	 * @param element - Element to check visibility
	 * @return true if visible else false
	 */
	public static boolean isVisible(WebElement element)
	{
		WebDriver driver = Framework.getWebDriver(element);
		return (Boolean) execute(_JS_IsVisible, driver, element);
	}

	/**
	 * Scroll to the bottom of the page
	 * 
	 * @param driver
	 */
	public static void scrollToBottom(WebDriver driver)
	{
		execute(driver, _JS_ScrollToBottom, null);
	}

	/**
	 * Gets the checked property on the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method returns the checked property (which can be different from the value attribute or
	 * undefined)<BR>
	 * 2) If the normal isElementSelected method is returning false, then this method will return the checked
	 * property which should be the correct state<BR>
	 * 
	 * @param element - Element for which to get the checked property
	 * @return true if checked (selected) else false
	 */
	public static boolean isElementSelected(WebElement element)
	{
		WebDriver driver = Framework.getWebDriver(element);
		return (Boolean) execute(_JS_GetChecked, driver, element);
	}

	/**
	 * Checks if element's text matches the criteria<BR>
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
	 * @param element - Element to check text
	 * @param criteria - Criteria for the text comparison
	 * @param sExpectedText - Expected text to match the criteria (non-null)
	 * @return true if element has text matching criteria else false
	 */
	public static boolean isText(WebElement element, Comparison criteria, String sExpectedText)
	{
		String actual = Conversion.nonNull(getText(element)).trim();
		String expected = Conversion.nonNull(sExpectedText).trim();
		return Compare.text(actual, expected, criteria);
	}

	/**
	 * Checks if the table is valid
	 * 
	 * @param table - Table element to verify if valid
	 * @return <B>false</B> - if number of rows/columns is less than 1<BR>
	 *         <B>true</B> - if both conditions are met<BR>
	 */
	public static boolean isValidTable(WebElement table)
	{
		String sJSON;
		try
		{
			sJSON = String.valueOf(JS_Util.execute(Framework.getWebDriver(table), _JS_GetTable, table));
			Map<String, Object> page = WS_Util.toMap(sJSON);

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> rows = (List<Map<String, Object>>) page.get("rows");
			if (rows.size() < 1)
				return false;

			String sColumnLength = String.valueOf(page.get("maxColumnLength"));
			int maxColumnLength = Conversion.parseInt(sColumnLength);
			if (maxColumnLength < 1)
				return false;
		}
		catch (Exception ex)
		{
			return false;
		}

		return true;
	}

	/**
	 * Get data from non-empty table<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) All data is trimmed<BR>
	 * 
	 * @param table - Table element to get data from
	 * @return String[][]
	 * @throws GenericUnexpectedException if invalid max column length or there are no rows
	 */
	public static String[][] getTable(WebElement table)
	{
		String sJSON = String.valueOf(JS_Util.execute(Framework.getWebDriver(table), _JS_GetTable, table));
		Map<String, Object> page = WS_Util.toMap(sJSON);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>) page.get("rows");
		if (rows.size() < 1)
			Logs.logError("There must be at least 1 row");

		String sColumnLength = String.valueOf(page.get("maxColumnLength"));
		int maxColumnLength = Conversion.parseInt(sColumnLength);
		if (maxColumnLength < 1)
			Logs.logError("Invalid max column length:  " + sColumnLength);

		String[][] data = new String[rows.size()][maxColumnLength];
		for (int i = 0; i < rows.size(); i++)
		{
			Map<String, Object> row = rows.get(i);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> columns = (List<Map<String, Object>>) row.get("columns");
			for (int j = 0; j < maxColumnLength; j++)
			{
				if (j < columns.size())
				{
					Map<String, Object> cell = columns.get(j);
					data[i][j] = String.valueOf(cell.get("value"));
				}
				else
				{
					// In the event that the column sizes are different and this row does not have the max
					// column size, set it to the empty string.
					data[i][j] = "";
				}
			}
		}

		return data;
	}

	/**
	 * Extracts the drop down options from the DOM
	 * 
	 * @param dropdown - Drop Down element to get the options from
	 * @return List&lt;DropDownDefaults&gt;
	 */
	public static List<DropDownDefaults> getOptionsData(WebElement dropdown)
	{
		List<DropDownDefaults> data = new ArrayList<DropDownDefaults>();

		WebDriver driver = Framework.getWebDriver(dropdown);
		String sJSON = String.valueOf(JS_Util.execute(driver, _JS_GetDropDownOptions, dropdown));
		List<Map<String, Object>> dom = WS_Util.toMapList(sJSON);
		for (Map<String, Object> option : dom)
		{
			String sVisible = String.valueOf(option.get("visible"));
			String sValue = String.valueOf(option.get("value"));
			String sIndex = String.valueOf(option.get("index"));
			boolean bEnabled = (Boolean) option.get("enabled");
			DropDownDefaults item = new DropDownDefaults(sVisible, sValue, sIndex, bEnabled);
			data.add(item);
		}

		return data;
	}

	/**
	 * Set Drop Down using index
	 * 
	 * @param dropdown - Drop Down to set option
	 * @param index - Index to set the selected option to
	 * @return true if successful else false
	 */
	public static boolean setDropDown(WebElement dropdown, String index)
	{
		WebDriver driver = Framework.getWebDriver(dropdown);
		return (Boolean) JS_Util.execute(_JS_SetDropDown, driver, dropdown, index);
	}

	/**
	 * Selects value from drop down using specified option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 2) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 3) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sLog - Name to use in log for the drop down
	 * @param dd - Object that contains information on which drop down option to select
	 * @param level - Control error logging written
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	private static void dropDownSelection(WebElement dropdown, String sLog, DropDown dd, LogErrorLevel level)
	{
		if (dd.using == Selection.Skip)
		{
			Logs.log.info("Skipped entering '" + sLog + "'");
			return;
		}

		try
		{
			if (!dropdown.isEnabled())
				throw new ElementNotEnabledException("");

			if (!dropdown.isDisplayed())
				throw new ElementNotDisplayedException("");

			// Get all drop down options
			List<DropDownDefaults> options = getOptionsData(dropdown);

			// Calculate the index of the drop down option to be selected
			int index = -1;
			if (dd.using == Selection.Index)
			{
				index = Conversion.parseInt(dd.option);
				if (index < 0)
				{
					// Generate a valid random number based on the number of drop down options
					if (dd.minIndex >= options.size() || dd.minIndex < 0)
						index = Rand.randomRangeIndex(0, options.size());
					else
						index = Rand.randomRangeIndex(dd.minIndex, options.size());
				}
			}
			else
			{
				// All other options require searching the list of drop down options for the 1st match
				for (int i = 0; i < options.size(); i++)
				{
					boolean match;
					if (dd.using == Selection.ValueHTML)
					{
						// Comparison against the HTML Value
						match = Compare.equals(options.get(i).value, dd.option, Comparison.Equal);
					}
					else if (dd.using == Selection.RegEx)
					{
						// Regular Expression match against visible text
						match = Compare.matches(options.get(i).visible, dd.option);
					}
					else
					{
						// Visible Text is default
						match = Compare.equals(options.get(i).visible, dd.option, Comparison.Equal);
					}

					if (match)
					{
						index = i;
						break;
					}
				}
			}

			// Throw exception if valid option was not found
			if (index >= options.size() || index < 0)
			{
				if (dd.using == Selection.Index)
					throw new NumberFormatException("");
				else if (dd.using == Selection.ValueHTML)
					throw new NoSuchElementException("");
				else if (dd.using == Selection.RegEx)
					throw new DropDownPartialMatchException("");
				else
					throw new NoSuchElementException("");
			}

			// Get the drop down option to set using JavaScript
			DropDownDefaults _Option = options.get(index);

			// Check that the option to be selected is actually enabled
			if (!_Option.enabled)
				throw new ElementNotEnabledException("");

			// Set logging variables
			String failure = "Setting property selectedIndex (" + _Option.index
					+ ") using JavaScript failed for the drop down ('" + sLog + "')";
			String success;
			if (dd.using == Selection.Index)
			{
				success = "Successfully selected option ('" + _Option.visible + "') by index (" + index
						+ ") from the drop down ('" + sLog + "')";
			}
			else if (dd.using == Selection.ValueHTML)
			{
				success = "Successfully selected option ('" + _Option.visible + "') by value ('" + dd.option
						+ "') from the drop down ('" + sLog + "')";
			}
			else if (dd.using == Selection.RegEx)
			{
				success = "Successfully selected option ('" + _Option.visible + "') by using RegEx ('"
						+ dd.option + "') to match from the drop down ('" + sLog + "')";
			}
			else
			{
				success = "Successfully selected option ('" + _Option.visible + "') from the drop down ('"
						+ sLog + "')";
			}

			boolean result = setDropDown(dropdown, _Option.index);
			if (result)
				Logs.log.info(success);
			else
				throw new GenericUnexpectedException(failure);
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sLog + "') was not enabled";
			Logs.logWarnError(level, new ElementNotEnabledException(sError));
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sLog + "') was not displayed";
			Logs.logWarnError(level, new ElementNotDisplayedException(sError));
		}
		catch (NoSuchElementException nsee)
		{
			String sError = "Could not find '" + dd.option + "' in the drop down ('" + sLog + "')";
			Logs.logWarnError(level, new DropDownSelectionException(sError));
		}
		catch (NumberFormatException nfe)
		{
			String sError = "Could not find index '" + dd.option + "' in the drop down ('" + sLog + "')";
			Logs.logWarnError(level, new DropDownIndexException(sError));
		}
		catch (DropDownPartialMatchException ddpme)
		{
			String sError = "Could not find a partial match using the regular expression '" + dd.option
					+ "' in the drop down ('" + sLog + "')";
			Logs.logWarnError(level, new DropDownPartialMatchException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Drop down ('" + sLog
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new DropDownNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			String sError = "Could not find drop down ('" + sLog + "') due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new DropDownNoSuchElementException(sError));
		}
	}

	/**
	 * Selects a drop down option (Enhanced)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Supports all ways to select a drop down option (By Value (HTML), By Index, Regular Expression,
	 * Default - Visible Text)<BR>
	 * 2) Supports random selection for By Index when option to select converts to less than 0<BR>
	 * 3) Drop down must have at least 1 option<BR>
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sLog - Name to use in log for the drop down
	 * @param dd - Object that contains information on which drop down option to select
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelect(WebElement dropdown, String sLog, DropDown dd)
	{
		dropDownSelection(dropdown, sLog, dd, LogErrorLevel.ERROR);
	}

	/**
	 * Clear field
	 * 
	 * @param element - Element to clear
	 * @return true if successful else false
	 */
	public static boolean clearField(WebElement element)
	{
		WebDriver driver = Framework.getWebDriver(element);
		return Conversion.parseBoolean(JS_Util.execute(_JS_ClearValue, driver, element));
	}
}
