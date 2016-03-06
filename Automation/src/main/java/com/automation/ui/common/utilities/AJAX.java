package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.automation.ui.common.dataStructures.AutoCompleteField;
import com.automation.ui.common.dataStructures.ChangeResults;
import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.DropDownDefaults;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.dataStructures.Selection;
import com.automation.ui.common.exceptions.CheckBoxNoSuchElementException;
import com.automation.ui.common.exceptions.CheckBoxNotEnabled;
import com.automation.ui.common.exceptions.ClickNoSuchElementException;
import com.automation.ui.common.exceptions.DropDownNoSuchElementException;
import com.automation.ui.common.exceptions.ElementRefreshException;
import com.automation.ui.common.exceptions.EnterFieldNoSuchElementException;
import com.automation.ui.common.exceptions.GenericActionNotCompleteException;
import com.automation.ui.common.exceptions.JavaScriptException;

/**
 * This class is for various common techniques that are used to determine that an action/page load is complete
 * which can be used in other packages/classes.
 */
public class AJAX {
	/**
	 * Delay after resetting an input field. By Default it is Framework.getPollInterval()<BR>
	 * <BR>
	 * <B>NOTE:</B> Intermittently after resetting an input, an AJAX timeout can occur. I suspect that the
	 * application is not triggering the AJAX call because the real user input is entered too fast as such it
	 * does not know there is data and the AJAX call should be triggered. This delay should resolve the issue.
	 */
	private static int nDelay = Framework.getPollInterval();

	// Variables for method click_TransactionID
	private static final String sID_Attribute_Changed_on_Complete = "javax.faces.ViewState";
	private static final String sAttribute_Changed_on_Complete = "value";

	// This is the node entered in the DOM as such it must be unique and follow HTML rules
	private final static String sTempUniqueNode = "dndelete";

	// Flag to indicate if AJAX timeout occurs, then throw exception
	private static boolean bException = true;

	//
	// Supported options for the methods getInfo
	//
	private final static int _VisibleText = 0;
	private final static int _VisibleAttribute = 1;
	private final static int _JavaScriptText = 2;
	private final static int _JavaScriptAttribute = 3;

	/**
	 * Gets sTempUniqueNode for use with methods <B>wasNodeRemovedFromDOM</B> or <B>removeNodeFromDOM</B>
	 * 
	 * @return AJAX.sTempUniqueNode (private variable)
	 */
	public static String getTempUniqueNode()
	{
		return sTempUniqueNode;
	}

	/**
	 * Set flag to indicate whether exception should be generated on timeout
	 * 
	 * @param bException - true if you want exception on timeout, false no exception
	 */
	public static void setExceptionOnTimeout(boolean bException)
	{
		AJAX.bException = bException;
	}

	/**
	 * Gets the flag that indicates whether exception should be generated on timeout
	 * 
	 * @return AJAX.bException
	 */
	public static boolean getExceptionOnTimeout()
	{
		return AJAX.bException;
	}

	/**
	 * This method waits for a "transaction ID" to change after clicking an element.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sLogging - Element Name to log
	 */
	public static void click_TransactionID(WebDriver driver, String sLocator, String sLogging)
	{
		click_TransactionID(driver, sLocator, sLogging, !bException);
	}

	/**
	 * This method waits for a "transaction ID" to change after clicking an element.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sLogging - Element Name to log
	 * @param bContinueOnTimeout - true to continue if timeout occurs else throw exception
	 */
	public static void click_TransactionID(WebDriver driver, String sLocator, String sLogging,
			boolean bContinueOnTimeout)
	{
		// Get the current "transaction ID"
		WebElement transactionID = null;
		String sInitialValue;
		try
		{
			transactionID = Framework.findElement(driver, sID_Attribute_Changed_on_Complete);
			sInitialValue = transactionID.getAttribute(sAttribute_Changed_on_Complete);
		}
		catch (Exception ex)
		{
			sInitialValue = "";
		}

		// Click the Next Page button
		Framework.click(Framework.findElement(driver, sLocator), sLogging);

		// Wait for the "transaction ID" to change
		if (!Framework.isWaitForAttributeToChange(transactionID, sAttribute_Changed_on_Complete,
				sInitialValue))
		{
			String sError = "Timeout occurred after clicking " + sLogging;
			if (bContinueOnTimeout)
			{
				Logs.log.warn(sError);
			}
			else
			{
				sError += Framework.getNewLine();
				Logs.log.error(sError);
				throw new GenericActionNotCompleteException(sError);
			}
		}
	}

	/**
	 * This method waits for a "transaction ID" to change after clicking an element.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param element - WebElement to click
	 * @param sLogging - Element Name to log
	 */
	public static void click_TransactionID(WebElement element, String sLogging)
	{
		click_TransactionID(element, sLogging, !bException);
	}

	/**
	 * This method waits for a "transaction ID" to change after clicking an element.
	 * 
	 * @param element - WebElement to click
	 * @param sLogging - Element Name to log
	 * @param bContinueOnTimeout - true to continue if timeout occurs else throw exception
	 */
	public static void click_TransactionID(WebElement element, String sLogging, boolean bContinueOnTimeout)
	{
		WebDriver useDriver = Framework.getWebDriver(element);
		if (useDriver == null)
			Logs.logError("Could not get WebDriver from WebElement ('" + sLogging + "')");

		// Get the current "transaction ID"
		WebElement transactionID = null;
		String sInitialValue;
		try
		{
			transactionID = Framework.findElement(useDriver, sID_Attribute_Changed_on_Complete);
			sInitialValue = transactionID.getAttribute(sAttribute_Changed_on_Complete);
		}
		catch (Exception ex)
		{
			sInitialValue = "";
		}

		// Click the Next Page button
		Framework.click(element, sLogging);

		// Wait for the "transaction ID" to change
		if (!Framework.isWaitForAttributeToChange(transactionID, sAttribute_Changed_on_Complete,
				sInitialValue))
		{
			String sError = "Timeout occurred after clicking " + sLogging;
			if (bContinueOnTimeout)
			{
				Logs.log.warn(sError);
			}
			else
			{
				sError += Framework.getNewLine();
				Logs.log.error(sError);
				throw new GenericActionNotCompleteException(sError);
			}
		}
	}

	/**
	 * Constructs the JavaScript to add a new child node using getTempUniqueNode() for the value to the
	 * specified parent node
	 * 
	 * @param sParentID - ID of node where the new child node will be added to the DOM
	 * @return
	 */
	public static String getJS_AddNodeToDOM(String sParentID)
	{
		String sAddNodeJS = "var myNode=document.createElement('" + getTempUniqueNode() + "'); ";
		sAddNodeJS += "myNode.setAttribute('id', '" + getTempUniqueNode() + "'); ";
		sAddNodeJS += "document.getElementById('" + sParentID + "').appendChild(myNode); ";
		return sAddNodeJS;
	}

	/**
	 * Constructs the JavaScript to add a new child node to the specified parent node
	 * 
	 * @param sParentID - ID of node where the new child node will be added to the DOM
	 * @param sNewChildNodeID - Unique value for both the child node name and ID to be added to the DOM
	 * @return
	 */
	public static String getJS_AddNodeToDOM(String sParentID, String sNewChildNodeID)
	{
		String sAddNodeJS = "var myNode=document.createElement('" + sNewChildNodeID + "'); ";
		sAddNodeJS += "myNode.setAttribute('id', '" + sNewChildNodeID + "'); ";
		sAddNodeJS += "document.getElementById('" + sParentID + "').appendChild(myNode); ";
		return sAddNodeJS;
	}

	/**
	 * Gets the ID of the element using the locator which is then used to construct JavaScript to add the node
	 * to the DOM that is returned<BR>
	 * <BR>
	 * <B>Note: </B> Uses getTempUniqueNode() to specify the Unique value for both the child node name and ID
	 * to be added to the DOM<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to locator the element to get the ID
	 * @return
	 */
	public static String getJS_AddNodeToDOM(WebDriver driver, String sLocator)
	{
		return getJS_AddNodeToDOM(driver, sLocator, getTempUniqueNode());
	}

	/**
	 * Gets the ID of the element using the locator which is then used to construct JavaScript to add the node
	 * to the DOM that is returned
	 * 
	 * @param driver
	 * @param sLocator - How to locator the element to get the ID
	 * @param sNewChildNodeID - Unique value for both the child node name and ID to be added to the DOM
	 * @return
	 */
	public static String getJS_AddNodeToDOM(WebDriver driver, String sLocator, String sNewChildNodeID)
	{
		WebElement element = Framework.findElementAJAX(driver, sLocator);
		String sID = Framework.getAttribute(element, "id");
		if (sID == null)
			Logs.logError("No element was found using locator '" + sLocator + "'");

		if (sID.equals(""))
			Logs.logError("The element found using locator '" + sLocator + "' did not have an ID");

		return getJS_AddNodeToDOM(sID, sNewChildNodeID);
	}

	/**
	 * This method adds a child node (sTempNodeID) to the specified node (sID). The purpose of this is to then
	 * trigger the AJAX which updates the child node(s) which you will know is complete when the temp node is
	 * no longer in the DOM<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The JavaScript only works for ID. If no ID exists, then different JavaScript to add the node will be
	 * needed.<BR>
	 * 2) This method needs to be called before the action that triggers the AJAX update.<BR>
	 * 3) In IE, certain node types (input) may not allow child nodes to be added<BR>
	 * 
	 * @param driver
	 * @param sID - ID of node that child node(s) will be updated by AJAX
	 * @param sTempNodeID - Unique value for both the child node name and ID to be added
	 * @param bException - true for exception if JavaScript execution fails
	 */
	public static void addNodeToDOM(WebDriver driver, String sID, String sTempNodeID, boolean bException)
	{
		String sJS = getJS_AddNodeToDOM(sID, sTempNodeID);
		if (!JS_Util.execute(driver, sJS, false))
		{
			String sError = "Javascript execution failed for following:  " + sJS;
			if (bException)
				Logs.logError(new JavaScriptException(sError));
			else
				Logs.log.warn(sError);
		}
	}

	/**
	 * This method adds a child node (sTempNodeID) to the specified node (sID). The purpose of this is to then
	 * trigger the AJAX which updates the child node(s) which you will know is complete when the temp node is
	 * no longer in the DOM<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The JavaScript only works for ID. If no ID exists, then different JavaScript to add the node will be
	 * needed.<BR>
	 * 2) This method needs to be called before the action that triggers the AJAX update.<BR>
	 * 3) In IE, certain node types (input) may not allow child nodes to be added<BR>
	 * 4) Exception occurs if JavaScript execution fails<BR>
	 * 
	 * @param driver
	 * @param sID - ID of node that child node(s) will be updated by AJAX
	 * @param sTempNodeID - Unique value for both the child node name and ID to be added
	 */
	public static void addNodeToDOM(WebDriver driver, String sID, String sTempNodeID)
	{
		addNodeToDOM(driver, sID, sTempNodeID, true);
	}

	/**
	 * This method adds a child node (AJAX.sTempUniqueNode) to the specified node (element). The purpose of
	 * this is to then trigger the AJAX which updates the child node(s) which you will know is complete when
	 * the temp node is no longer in the DOM<BR>
	 * 
	 * @param element - Child node added to this element in the DOM
	 */
	public static void addNodeToDOM(WebElement element)
	{
		String sResult = addNodeToDOM(element, sTempUniqueNode);
		if (!sResult.equals(""))
		{
			String sError = "Javascript execution failed with following message:  " + sResult;
			Logs.logError(new JavaScriptException(sError));
		}
	}

	/**
	 * This method adds a child node (sTempNodeID) to the specified node (element). The purpose of this is to
	 * then trigger the AJAX which updates the child node(s) which you will know is complete when the temp
	 * node is no longer in the DOM<BR>
	 * 
	 * @param element - Child node added to this element in the DOM
	 * @param sTempNodeID - Unique value for both the child node name and ID to be added
	 * @return empty string if successful else string is the error message
	 */
	public static String addNodeToDOM(WebElement element, String sTempNodeID)
	{
		try
		{
			// Construct JavaScript to execute
			String sAddNodeJS = "var myNode=document.createElement('" + sTempNodeID + "'); ";
			sAddNodeJS += "myNode.setAttribute('id', '" + sTempNodeID + "'); ";
			sAddNodeJS += "arguments[0].appendChild(myNode); ";

			/*
			 * Trick to get real element that can return the WebDriver.
			 * Note:
			 * 1) If you use element directly it is a proxy and this cannot be cast to RemoteWebElement
			 * 2) If WebElement cannot be bound, then this will generate an exception
			 */
			WebElement realElement = element.findElement(By.xpath("."));

			// Get the WebDriver object from the real (bound) WebElement
			WebDriver useDriver = ((RemoteWebElement) realElement).getWrappedDriver();

			// Execute JavaScript to add node to addJS_to_Element
			((JavascriptExecutor) useDriver).executeScript(sAddNodeJS, element);

			return "";
		}
		catch (Exception ex)
		{
			return ex.getMessage();
		}
	}

	/**
	 * Determines if the element specified was removed from the DOM before timeout.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * 1) This method should be used in conjunction with the method <B>addNodeToDOM</B> which adds a temp node
	 * to the DOM<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the element that is to be removed
	 * @return true if node was removed from the DOM else false
	 */
	public static boolean wasNodeRemovedFromDOM(WebDriver driver, String sLocator)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			// Check if the element was removed
			WebElement element = Framework.findElement(driver, sLocator, false);
			if (element == null)
				return true;

			Framework.sleep(Framework.getPollInterval());
		}

		return false;
	}

	/**
	 * Uses JavaScript to remove a node from the DOM. The purpose of this is to then trigger the AJAX which
	 * adds the node back at which time the AJAX request is complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The JavaScript only works for ID. If no ID exists, then different JavaScript to add the node will be
	 * needed.<BR>
	 * 2) This method needs to be called before the action that triggers the AJAX update.<BR>
	 * 
	 * @param driver
	 * @param sID - ID of node to be deleted
	 * @param bException - true for exception if JavaScript execution fails
	 */
	public static void removeNodeFromDOM(WebDriver driver, String sID, boolean bException)
	{
		String sRemoveNodeJS = "var dn_temp = document.getElementById('" + sID + "'); ";
		sRemoveNodeJS += "dn_temp.parentNode.removeChild(dn_temp); ";
		if (!JS_Util.execute(driver, sRemoveNodeJS, false))
		{
			String sError = "Javascript execution failed for following:  " + sRemoveNodeJS;
			if (bException)
				Logs.logError(new JavaScriptException(sError));
			else
				Logs.log.warn(sError);
		}
	}

	/**
	 * Uses JavaScript to remove a node from the DOM. The purpose of this is to then trigger the AJAX which
	 * adds the node back at which time the AJAX request is complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The JavaScript only works for ID. If no ID exists, then different JavaScript to add the node will be
	 * needed.<BR>
	 * 2) This method needs to be called before the action that triggers the AJAX update.<BR>
	 * 3) Exception occurs if JavaScript execution fails<BR>
	 * 
	 * @param driver
	 * @param sID - ID of node to be deleted
	 */
	public static void removeNodeFromDOM(WebDriver driver, String sID)
	{
		removeNodeFromDOM(driver, sID, true);
	}

	/**
	 * This method adds a node to the DOM using the <B>specified JavaScript</B> before clicking the option and
	 * waiting for the node to removed. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 4) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - How to find the option to click
	 * @param sLog - Element Name to log
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 */
	public static void click_DOM(WebDriver driver, String sLocator, String sLog, String sJavaScript)
	{
		click_DOM(driver, sLocator, sLog, sJavaScript, bException);
	}

	/**
	 * This method adds a node to the DOM using the <B>specified JavaScript</B> before clicking the option and
	 * waiting for the node to removed. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - How to find the option to click
	 * @param sLog - Element Name to log
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean click_DOM(WebDriver driver, String sLocator, String sLog, String sJavaScript,
			boolean bException)
	{
		// Run the JavaScript
		if (!JS_Util.execute(driver, sJavaScript, false))
		{
			String sError = "Javascript execution failed for following:  " + sJavaScript;
			Logs.logError(new JavaScriptException(sError));
		}

		// Click the option
		Framework.click(driver, sLocator, sLog);

		// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
		boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(driver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// Attempts manual removal (logs error but does not throw an exception)
			removeNodeFromDOM(driver, sTempUniqueNode);

			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred.";
				Logs.logError(new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Logs.log.warn("AJAX did not complete before timeout occurred");
		return false;
	}

	/**
	 * This method adds a node to the DOM before selecting the corresponding option and waiting for the node
	 * to removed.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - How to find the drop down
	 * @param sLog - Name to use in log for the drop down
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param dd - Object that contains information on which drop down option to select
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean dropdown_DOM(WebDriver driver, String sLocator, String sLog, String sJavaScript,
			DropDown dd, boolean bException)
	{
		if (dd.using == Selection.Skip)
		{
			Logs.log.info("Skipped entering '" + sLog + "'");
			return true;
		}

		// Run the JavaScript
		if (!JS_Util.execute(driver, sJavaScript, false))
		{
			String sError = "Javascript execution failed for following:  " + sJavaScript;
			Logs.logError(new JavaScriptException(sError));
		}

		// Select the drop down option
		Framework.dropDownSelect(Framework.findElementAJAX(driver, sLocator), sLog, dd);

		// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
		boolean bCompleteBeforeTimeout = AJAX.wasNodeRemovedFromDOM(driver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// Attempts manual removal (logs error but does not throw an exception)
			AJAX.removeNodeFromDOM(driver, sTempUniqueNode);

			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred.";
				Logs.logError(new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Logs.log.warn("AJAX did not complete before timeout occurred");
		return false;
	}

	/**
	 * This method adds a node to the DOM before selecting the corresponding option and waiting for the node
	 * to removed.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) No need to manually remove node upon failure as method handles this.<BR>
	 * 2) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param dropDownElement - Drop Down Element to select option from
	 * @param sLog - Name to use in log for the drop down
	 * @param addJS_to_Element - Element to append an unique node to
	 * @param dd - Object that contains information on which drop down option to select
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean dropdown_DOM(WebElement dropDownElement, String sLog, WebElement addJS_to_Element,
			DropDown dd, boolean bException)
	{
		if (dd.using == Selection.Skip)
		{
			Logs.log.info("Skipped entering '" + sLog + "'");
			return true;
		}

		WebDriver useDriver = Framework.getWebDriver(dropDownElement);
		if (useDriver == null)
			Logs.logError("Could not get WebDriver from WebElement ('" + sLog + "')");

		// Select the drop down option
		Framework.dropDownSelect(dropDownElement, sLog, dd);

		// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
		boolean bCompleteBeforeTimeout = AJAX.wasNodeRemovedFromDOM(useDriver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// Attempts manual removal (logs error but does not throw an exception)
			AJAX.removeNodeFromDOM(useDriver, sTempUniqueNode);

			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred.";
				Logs.logError(new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Logs.log.warn("AJAX did not complete before timeout occurred");
		return false;
	}

	/**
	 * This method adds a node to the DOM before clicking the option and waiting for the node to removed.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) No need to manually remove node upon failure as method handles this.<BR>
	 * 2) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 3) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param clickElement - Element to click
	 * @param sLog - Element Name to log
	 * @param addJS_to_Element - Element to append an unique node to
	 */
	public static void click_DOM(WebElement clickElement, String sLog, WebElement addJS_to_Element)
	{
		click_DOM(clickElement, sLog, addJS_to_Element, bException);
	}

	/**
	 * This method adds a node to the DOM before clicking the option and waiting for the node to removed.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) No need to manually remove node upon failure as method handles this.<BR>
	 * 2) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param clickElement - Element to click
	 * @param sLog - Element Name to log
	 * @param addJS_to_Element - Element to append an unique node to
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean click_DOM(WebElement clickElement, String sLog, WebElement addJS_to_Element,
			boolean bException)
	{
		WebDriver useDriver = Framework.getWebDriver(clickElement);
		if (useDriver == null)
			Logs.logError("Could not get WebDriver from WebElement ('" + sLog + "')");

		// Click the option
		Framework.click(clickElement, sLog);

		// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
		boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(useDriver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// Attempts manual removal (logs error but does not throw an exception)
			removeNodeFromDOM(useDriver, sTempUniqueNode);

			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred.";
				Logs.logError(new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Logs.log.warn("AJAX did not complete before timeout occurred");
		return false;
	}

	/**
	 * Determines if changing the drop down option is necessary and gives the drop down index<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) There must be at least <B>2 drop down options </B>for the results to be correct<BR>
	 * 2) ChangeResults.nIndex is only valid if <B>ChangeResults.bChange == true</B><BR>
	 * 3) If <B>future drop down is random selection</B>, then there will be no change as current and future
	 * options will <B>never match</B><BR>
	 * 4) Use <B>Framework.getRandomDropDown</B> if working with possibly random options to get <B>future</B>
	 * object<BR>
	 * 
	 * @param dropDownElement - The drop down element to prepare
	 * @param future - The future drop down option selection object
	 * @param sDefaultVisible - Default Visible Text value
	 * @param sDefaultValue - Default (HTML) Value Text
	 * @param sDefaultIndex - Default Index value
	 * @return ChangeResults.bChange contains true if change required else false & ChangeResults.dd can be
	 *         used to change the drop down option
	 */
	public static ChangeResults changeOption(WebElement dropDownElement, DropDown future,
			String sDefaultVisible, String sDefaultValue, String sDefaultIndex)
	{
		// Index of drop down option to switch to
		int nIndex;

		DropDown current = future.copy();

		/*
		 * Need to update the option.
		 * 
		 * NOTE: If future.using is purposely set to an invalid option, then current.option will not be
		 * updated. This means that both the current & future drop downs will match which could cause the
		 * results to be invalid
		 */
		if (future.using == Selection.Index)
			current.option = String.valueOf(Framework.getSelectedIndex(dropDownElement));

		if (future.using == Selection.VisibleText)
			current.option = Conversion.nonNull(Framework.getSelectedOption(dropDownElement));

		if (future.using == Selection.RegEx)
		{
			// Get the exact drop down option selected (visible text)
			current.option = Conversion.nonNull(Framework.getSelectedOption(dropDownElement));

			/*
			 * If the current selection matches the future selection, then make current = future by updating
			 * current.option
			 */
			if (current.option.matches(future.option))
				current.option = future.option;
		}

		if (future.using == Selection.ValueHTML)
			current.option = String.valueOf(Framework.getSelectedValue(dropDownElement));

		/*
		 * If the current selection equals the future selection, then we need to switch to a different option.
		 */
		if (current.equals(future))
		{
			// Value should always be greater than 0 but just in case log a warning for troubleshooting
			int nDefaultIndex = Conversion.parseInt(sDefaultIndex);
			if (nDefaultIndex < 0)
				Logs.log.warn("Default Index could not be parsed to an integer");

			// Can we use the default index to switch to?
			if (DropDown.isDefault(future, sDefaultVisible, sDefaultValue, sDefaultIndex))
			{
				/*
				 * Future index is the default. So, we will choose a different index
				 * 
				 * Notes:
				 * 1) This assumes that there are at least 2 options
				 * 2) If sDefaultIndex cannot be parsed, then it is assumed that index 0 is fine
				 */
				if (nDefaultIndex == 0)
					nIndex = 1;
				else
					nIndex = 0;
			}
			else
			{
				// Future Index is not the default. So, we can choose this index
				nIndex = nDefaultIndex;
			}

			return new ChangeResults(true, nIndex);
		}

		return new ChangeResults(false, 0);
	}

	/**
	 * Prepares the drop down for the user's selection and makes the selection. (Stale element can occur.)<BR>
	 * <BR>
	 * <B>High Level Details:</B><BR>
	 * 1) Get the real option to be selected for random selections<BR>
	 * 2) Change drop down selection as necessary based on the selection the user wants to make<BR>
	 * 3) Make the user's selection<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) There must be at least <B>2 drop down options </B>for the method to be successful always<BR>
	 * 2) Supports random drop down selections<BR>
	 * 3) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param driver
	 * @param sDropDown_Locator - How to find Drop Down element
	 * @param sLog - Used to log selection of drop down option
	 * @param dd - Object that contains information on which drop down option to select
	 * @param sAddJS_to_Element_Locator - Element to append an unique node to
	 * @param defaults - Object that contains all the default values for the drop down
	 */
	public static void dropdown_DOM(WebDriver driver, String sDropDown_Locator, String sLog, DropDown dd,
			String sAddJS_to_Element_Locator, DropDownDefaults defaults)
	{
		if (dd.using == Selection.Skip)
		{
			Logs.log.info("Skipped entering '" + sLog + "'");
			return;
		}

		// Element to append an unique node to
		WebElement addJS_to_Element;

		// Drop down element
		WebElement dropdown = Framework.findElementAJAX(driver, sDropDown_Locator);

		// Find the real option to be selected if necessary
		DropDown realOption = Rand.randomDropDown(dropdown, dd);

		// Do we need to change the current drop down selection to prepare for the user's selection?
		ChangeResults cr = AJAX.changeOption(dropdown, realOption, defaults.visible, defaults.value,
				defaults.index);
		if (cr.change)
		{
			addJS_to_Element = Framework.findElementAJAX(driver, sAddJS_to_Element_Locator);
			AJAX.dropdown_DOM(dropdown, sLog, addJS_to_Element, cr.dd, bException);
			Logs.log.info("Drop Down ('" + sLog + "') is ready for user's selection");
		}

		// Prevent stale elements & select the drop down option
		dropdown = Framework.findElementAJAX(driver, sDropDown_Locator);
		addJS_to_Element = Framework.findElementAJAX(driver, sAddJS_to_Element_Locator);
		AJAX.dropdown_DOM(dropdown, sLog, addJS_to_Element, realOption, bException);
	}

	/**
	 * Prepares the drop down for the user's selection and makes the selection. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>High Level Details:</B><BR>
	 * 1) Get the real option to be selected for random selections<BR>
	 * 2) Change drop down selection as necessary based on the selection the user wants to make<BR>
	 * 3) Make the user's selection<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) There must be at least <B>2 drop down options </B>for the method to be successful always<BR>
	 * 2) Supports random drop down selections<BR>
	 * 3) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param driver
	 * @param sDropDown_Locator - How to find Drop Down element
	 * @param sLog - Used to log selection of drop down option
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param dd - Object that contains information on which drop down option to select
	 * @param defaults - Object that contains all the default values for the drop down
	 */
	public static void dropdown_DOM(WebDriver driver, String sDropDown_Locator, String sLog,
			String sJavaScript, DropDown dd, DropDownDefaults defaults)
	{
		if (dd.using == Selection.Skip)
		{
			Logs.log.info("Skipped entering '" + sLog + "'");
			return;
		}

		// Drop down element
		WebElement dropdown = Framework.findElementAJAX(driver, sDropDown_Locator);

		// Find the real option to be selected if necessary
		DropDown realOption = Rand.randomDropDown(dropdown, dd);

		// Do we need to change the current drop down selection to prepare for the user's selection?
		ChangeResults cr = AJAX.changeOption(dropdown, realOption, defaults.visible, defaults.value,
				defaults.index);
		if (cr.change)
		{
			AJAX.dropdown_DOM(driver, sDropDown_Locator, sLog, sJavaScript, cr.dd, bException);
			Logs.log.info("Drop Down ('" + sLog + "') is ready for user's selection");
		}

		AJAX.dropdown_DOM(driver, sDropDown_Locator, sLog, sJavaScript, realOption, bException);
	}

	/**
	 * This method adds a node to the DOM before clearing & entering the value into the field and TABs off to
	 * generate AJAX call and waits for the AJAX call to complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param element - Element to enter text
	 * @param sLog - Element name to log
	 * @param sValue - Value to enter into the field
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean enter_DOM(WebElement element, String sLog, String sValue, String sJavaScript,
			boolean bException)
	{
		WebDriver useDriver = Framework.getWebDriver(element);
		if (useDriver == null)
			Logs.logError("Could not get WebDriver from WebElement ('" + sLog + "')");

		// Add Node to DOM
		if (!JS_Util.execute(useDriver, sJavaScript, false))
		{
			String sError = "Could not add node to DOM using the following Javascript:  " + sJavaScript;
			Logs.logError(new JavaScriptException(sError));
		}

		// Enter field
		Framework.enterField(element, sLog, sValue);

		// TAB away from the field to generate AJAX call
		try
		{
			element.sendKeys(Keys.TAB);
		}
		catch (Exception ex)
		{
			Logs.logError("Tabbing off element ('" + sLog + "') generated the following exception:  "
					+ ex.getMessage());
		}

		// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
		boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(useDriver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// Attempts manual removal (logs error but does not throw an exception)
			removeNodeFromDOM(useDriver, sTempUniqueNode);

			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred.";
				Logs.logError(new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Logs.log.warn("AJAX did not complete before timeout occurred");
		return false;
	}

	/**
	 * This method readies the element by changing the initial input value as necessary before adding a node
	 * to the DOM before clearing & entering the value into the field and TABs off to generate AJAX call and
	 * waits for the AJAX call to complete. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) Parameter Strings cannot be null<BR>
	 * 4) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param element - Element to enter text
	 * @param sLog - Element name to log
	 * @param sValue - Value to enter into the field
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param sTempValue - Temp Value to enter into the field if necessary to ready field for user real input.
	 *            This value cannot be the empty string.
	 */
	public static void enter_DOM(WebElement element, String sLog, String sValue, String sJavaScript,
			String sTempValue)
	{
		String sCurrentValue = Conversion.nonNull(Framework.getAttribute(element, Framework.getInputAttr()));
		if (sCurrentValue.equals(sValue))
		{
			/*
			 * If current value is the empty string, then change field to be the temp value (which cannot be
			 * empty) else use the empty string as the temp value.
			 */
			String sUsedTempValue;
			if (sCurrentValue.equals(""))
				sUsedTempValue = sTempValue;
			else
				sUsedTempValue = "";

			enter_DOM(element, sLog, sUsedTempValue, sJavaScript, bException);
			Logs.log.info("Element ('" + sLog + "') is ready for user's input");
			Framework.sleep(nDelay);
		}

		// Enter user's value and trigger AJAX
		enter_DOM(element, sLog, sValue, sJavaScript, bException);
	}

	/**
	 * Based on value object, field can be skipped. If field entered the following occurs, method readies the
	 * element by changing the initial input value as necessary before adding a node to the DOM before
	 * clearing & entering the value into the field and TABs off to generate AJAX call and waits for the AJAX
	 * call to complete. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) Parameter Strings cannot be null<BR>
	 * 4) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param element - Element to enter text
	 * @param sLog - Element name to log
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param sTempValue - Temp Value to enter into the field if necessary to ready field for user real input.
	 *            This value cannot be the empty string.
	 */
	public static void enter_DOM(WebElement element, String sLog, InputField value, String sJavaScript,
			String sTempValue)
	{
		if (value != null && value.modifyField())
		{
			// Enter user's value and trigger AJAX
			enter_DOM(element, sLog, value.getValueToInput(), sJavaScript, sTempValue);
		}
		else
		{
			// Skip the field but log some useful information
			String sValue = Conversion.nonNull(Framework.getAttribute(element, Framework.getInputAttr()));
			Logs.log.info("The field '" + sLog + "' was skipped.  Default value was '" + sValue + "'");
		}
	}

	/**
	 * This method adds a node to the DOM before selecting/unselecting the check box and TABs off to generate
	 * AJAX call and waits for the AJAX call to complete. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 4) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 5) Check box only selected or unselected if it generates an AJAX call<BR>
	 * 6) Verifies that check box is enabled<BR>
	 * 
	 * @param element - Check box element
	 * @param sLog - Element name to log
	 * @param bCheck - true to make check box selected after method else false
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 */
	public static void checkbox_DOM(WebElement element, String sLog, boolean bCheck, String sJavaScript)
	{
		checkbox_DOM(element, sLog, bCheck, sJavaScript, bException);
	}

	/**
	 * This method adds a node to the DOM before selecting/unselecting the check box and TABs off to generate
	 * AJAX call and waits for the AJAX call to complete. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 4) Check box only selected or unselected if it generates an AJAX call<BR>
	 * 5) Verifies that check box is enabled<BR>
	 * 
	 * @param element - Check box element
	 * @param sLog - Element name to log
	 * @param bCheck - true to make check box selected after method else false
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean checkbox_DOM(WebElement element, String sLog, boolean bCheck, String sJavaScript,
			boolean bException)
	{
		WebDriver useDriver = Framework.getWebDriver(element);
		if (useDriver == null)
			Logs.logError("Could not get WebDriver from WebElement ('" + sLog + "')");

		boolean bEnabled = Framework.isElementEnabled(element);
		if (!bEnabled)
		{
			String sError = "Check box for '" + sLog + "' was not enabled";
			Logs.logError(new CheckBoxNotEnabled(sError));
		}

		// Check box
		boolean bCurrentStateSelected = Framework.isElementSelected(element);
		boolean bOnChangeTrigger = false;
		if (bCheck)
		{
			if (bCurrentStateSelected)
			{
				Logs.log.info("Check box for '" + sLog + "' was already selected");
			}
			else
			{
				Framework.check(element, sLog, false);
				bOnChangeTrigger = true;
			}
		}
		else
		{
			if (bCurrentStateSelected)
			{
				Framework.uncheck(element, sLog, false);
				bOnChangeTrigger = true;
			}
			else
			{
				Logs.log.info("Check box for '" + sLog + "' was already unselected");
			}
		}

		// Will AJAX be triggered?
		if (bOnChangeTrigger)
		{
			// Add Node to DOM
			if (!JS_Util.execute(useDriver, sJavaScript, false))
			{
				String sError = "Could not add node to DOM using the following Javascript:  " + sJavaScript;
				Logs.logError(new JavaScriptException(sError));
			}

			// TAB away from the element to generate AJAX call
			try
			{
				element.sendKeys(Keys.TAB);
			}
			catch (Exception ex)
			{
				Logs.logError("Tabbing off element ('" + sLog + "') generated the following exception:  "
						+ ex.getMessage());
			}

			// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
			boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(useDriver, sTempUniqueNode);
			if (bCompleteBeforeTimeout)
			{
				// AJAX completed successfully before timeout
				return true;
			}
			else
			{
				// Attempts manual removal (logs error but does not throw an exception)
				removeNodeFromDOM(useDriver, sTempUniqueNode);

				// AJAX did not complete before timeout & user wants an exception thrown
				if (bException)
				{
					String sError = "AJAX did not complete before timeout occurred.";
					Logs.logError(new GenericActionNotCompleteException(sError));
				}
			}

			// AJAX did not complete before timeout (and user did not want an exception thrown)
			Logs.log.warn("AJAX did not complete before timeout occurred");
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * This method adds a node to the DOM using the <B>specified JavaScript</B> before clicking the option as
	 * necessary to make the check box have the user's desired state and waiting for the node to removed.
	 * (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 4) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 5) Check box only selected or unselected as necessary to get to user's desired state<BR>
	 * 6) Verifies that check box is enabled<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - How to find the option to click
	 * @param sLog - Element name to log
	 * @param bCheck - true to make check box selected after method else false
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 */
	public static void checkbox_DOM(WebDriver driver, String sLocator, String sLog, boolean bCheck,
			String sJavaScript)
	{
		// Is check box selected?
		WebElement checkbox = Framework.findElementAJAX(driver, sLocator);
		boolean bElementSelected = Framework.isElementSelected(checkbox);

		// Do we need to take action based on current state?
		boolean bTakeAction = false;
		if (bCheck)
		{
			if (bElementSelected)
				Logs.log.info("Check box for '" + sLog + "' was already selected");
			else
				bTakeAction = true;
		}
		else
		{
			if (bElementSelected)
				bTakeAction = true;
			else
				Logs.log.info("Check box for '" + sLog + "' was already unselected");
		}

		// Only click the check box if it is necessary to make it the desired state by the user
		if (bTakeAction)
		{
			AJAX.click_DOM(driver, sLocator, sLog, sJavaScript);
		}
	}

	/**
	 * Prepares the drop down for the user's selection and makes the selection. <B>This method handles case if
	 * only 1 option</B> (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>High Level Details:</B><BR>
	 * 1) Check the number of options available in the drop down<BR>
	 * 2) IF less than 2 options, THEN verify that the only option available is the option the user wanted<BR>
	 * ELSE<BR>
	 * 3a) Get the real option to be selected for random selections<BR>
	 * 3b) Change drop down selection as necessary based on the selection the user wants to make<BR>
	 * 3c) Make the user's selection<BR>
	 * 3d) Verify correct option selected<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Exception occurs if specified option is not selected<BR>
	 * 2) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find Drop Down element
	 * @param sLog - Used to log selection of drop down option
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param dropdown - Object that contains information on which drop down option to select
	 */
	public static void dropdown_DOM(WebDriver driver, String sLocator, String sLog, String sJavaScript,
			DropDown dropdown)
	{
		if (dropdown.using == Selection.Skip)
		{
			Logs.log.info("Skipped entering '" + sLog + "'");
			return;
		}

		WebElement element = Framework.findElementAJAX(driver, sLocator);
		List<WebElement> options = Framework.getOptions(element);
		if (options.size() < 2)
		{
			try
			{
				Logs.log.warn("There were not enough options to make a selection for '" + sLog + "'.  "
						+ "(There is AJAX that requires more than 1 option.)");

				// Verify that the only option is the option the user wanted
				Verify.dropDown(element, dropdown, true);
			}
			catch (Exception ex)
			{
				String sOptionsAvailable = "No Options Available";
				if (options.size() == 1)
				{
					DropDownDefaults selected = DropDownDefaults.defaultsFromElement(element);
					sOptionsAvailable = selected.toString();
				}

				Logs.logError("Drop down options:  '" + sOptionsAvailable + "'");
			}
		}
		else
		{
			DropDownDefaults defaults = DropDownDefaults.defaultsFromElement(element);
			AJAX.dropdown_DOM(driver, sLocator, sLog, sJavaScript, dropdown, defaults);
			element = Framework.findElementAJAX(driver, sLocator);
			Verify.dropDown(element, dropdown, false);
		}
	}

	/**
	 * Enters input into an 'Auto Complete' field, adds node to DOM, selects a valid option from the
	 * suggestion drop down
	 * list that appears waits for node to be removed from the DOM<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If you do not want to select one of the suggestion drop down options, then do not use this method as
	 * an exception will occur.<BR>
	 * 2) To get sLocator_SuggestionList & sLocator_DropDownOptions you may need to Fiddler (or some other
	 * proxy.)<BR>
	 * 
	 * @param driver
	 * @param sLocator_Input - Locator to the input field that triggers the drop down suggestion list
	 * @param sLocator_SuggestionList - Locator to the Suggestion List (when it appears)
	 * @param sXpath_DropDownOptions - xpath to get all the drop down suggestion options
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param sLog_Input - Logging of entering the input field
	 * @param field - Object containing data to be input & option to be selected
	 * @param bLengthCheck - true to verify that if the drop down suggestion list appears it was suppose to.
	 *            (Also, it can provide addition reason why suggestion list did not appear.)
	 */
	public static void autoComplete_DOM(WebDriver driver, String sLocator_Input,
			String sLocator_SuggestionList, String sXpath_DropDownOptions, String sJavaScript,
			String sLog_Input, AutoCompleteField field, boolean bLengthCheck)
	{
		/*
		 * Step 1: Enter the value into the field (which triggers the suggestion list)
		 */
		WebElement auto = Framework.findElementAJAX(driver, sLocator_Input);
		Framework.enterField(auto, sLog_Input, field.value);

		/*
		 * Step 2: Wait for the suggestion list to appear
		 */
		if (Framework.isWaitForDisplayedElement(driver, sLocator_SuggestionList))
		{
			// Was the suggestions list suppose to be displayed based on input length?
			if (!field.triggerSuggestions())
			{
				String sMessage = "The number of characters entered should not have triggered the suggestions list";
				if (bLengthCheck)
					Logs.logError(sMessage);
				else
					Logs.log.warn(sMessage);
			}

			// Get list of options available
			List<WebElement> suggestions = Framework.findElementsAJAX(driver, sXpath_DropDownOptions);
			if (suggestions == null)
				Logs.logError("Could not find the '" + sLog_Input + "' suggestions available");

			int nSuggestions = suggestions.size();
			if (nSuggestions < 1)
				Logs.logError("No suggests in list");
			else
			{
				/*
				 * Does user want a random Index selection (if using Index)?
				 * NOTE: If the random selection is not visible, then we will loop through the entire list and
				 * pick 1st visible option.
				 */
				if (field.useIndex && field.useRandomIndex())
				{
					// Generate valid index
					int nUseIndex = Rand.randomRange(0, nSuggestions - 1);

					WebElement random = suggestions.get(nUseIndex);
					if (Framework.isElementDisplayed(random))
					{
						String sValue = Conversion.nonNull(Framework.getText(random));

						/*
						 * Add Node to DOM. Stop if any error occurs
						 */
						if (!JS_Util.execute(driver, sJavaScript, false))
						{
							String sError = "Javascript execution failed for following:  " + sJavaScript;
							Logs.logError(new JavaScriptException(sError));
						}

						// Click the option
						Framework.click(random, "Suggestion ('" + sValue + "')");

						/*
						 * Wait for the node to be removed from the DOM which indicates that the AJAX is
						 * complete.
						 * 
						 * NOTE: We will not throw exception if AJAX does not complete because if the
						 * 'special' option is clicked for no matches found this may not trigger the AJAX.
						 * Instead we will just log a warning.
						 */
						boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(driver, sTempUniqueNode);
						if (!bCompleteBeforeTimeout)
						{
							// Attempts manual removal (logs error but does not throw an exception)
							removeNodeFromDOM(driver, sTempUniqueNode);

							String sError = "AJAX did not complete before timeout occurred.";
							Logs.log.warn(sError);
						}

						return;
					}
				}

				// Loop through suggestion list to find desired option to select
				for (int nIndex = 0; nIndex < nSuggestions; nIndex++)
				{
					// Option needs to be visible to the user
					WebElement element = suggestions.get(nIndex);
					if (Framework.isElementDisplayed(element))
					{
						// Assume that this is not the option we are looking for
						boolean bSelect = false;
						String sValue = Conversion.nonNull(Framework.getText(element));
						if (field.useIndex)
						{
							/*
							 * Is this the index we are looking for?
							 * Note: The specified index may be hidden. So, I will pick the first option that
							 * is visible and greater than equal to the desired index.
							 */
							if (nIndex >= field.getIndex())
								bSelect = true;
						}
						else
						{
							// Does the option contain the visible text we are looking for?
							if (sValue.contains(field.selectOption))
								bSelect = true;
						}

						// Should we select the option?
						if (bSelect)
						{
							/*
							 * Add Node to DOM. Stop if any error occurs
							 */
							if (!JS_Util.execute(driver, sJavaScript, false))
							{
								String sError = "Javascript execution failed for following:  " + sJavaScript;
								Logs.logError(new JavaScriptException(sError));
							}

							// Click the option
							Framework.click(element, "Suggestion ('" + sValue + "')");

							/*
							 * Wait for the node to be removed from the DOM which indicates that the AJAX is
							 * complete.
							 * 
							 * NOTE: We will not throw exception if AJAX does not complete because if the
							 * 'special' option is clicked for no matches found this may not trigger the AJAX.
							 * Instead we will just log a warning.
							 */
							boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(driver, sTempUniqueNode);
							if (!bCompleteBeforeTimeout)
							{
								// Attempts manual removal (logs error but does not throw an exception)
								removeNodeFromDOM(driver, sTempUniqueNode);

								String sError = "AJAX did not complete before timeout occurred.";
								Logs.log.warn(sError);
							}

							return;
						}
					}
				}

				/*
				 * We were unable to find the specified option after checking all visible options
				 */
				if (field.useIndex)
					Logs.logError("There was no index greater than or equal to " + field.getIndex()
							+ " in the suggestion list that was visible");
				else
					Logs.logError("There was no option that contained '" + field.selectOption
							+ "' in the suggestion list that was visible");
			}
		}
		else
		{
			String sMessage = "The suggestion drop down list did not appear before timeout";
			if (bLengthCheck && !field.triggerSuggestions())
			{
				sMessage = "The number of characters (" + field.getTriggerLength()
						+ ") entered was not enough to trigger the suggestions list";
			}

			Logs.logError(sMessage);
		}
	}

	/**
	 * This method adds a node to the DOM using the <B>specified JavaScript</B> before selecting the option
	 * and waiting for the node to removed as necessary. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 4) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - How to find the drop down to work with
	 * @param sLog - Element Name to log
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param dropdown - The drop down option to be selected
	 */
	public static void dropdownLoseFocus_DOM(WebDriver driver, String sLocator, String sLog,
			String sJavaScript, DropDown dropdown)
	{
		dropdownLoseFocus_DOM(driver, sLocator, sLog, sJavaScript, dropdown, bException);
	}

	/**
	 * This method adds a node to the DOM using the <B>specified JavaScript</B> before selecting the option
	 * and waiting for the node to removed as necessary. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - How to find the drop down to work with
	 * @param sLog - Element Name to log
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param dropdown - The drop down option to be selected
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean dropdownLoseFocus_DOM(WebDriver driver, String sLocator, String sLog,
			String sJavaScript, DropDown dropdown, boolean bException)
	{
		// Get the drop down to work with
		WebElement element = Framework.findElement(driver, sLocator);

		// Get current selected option before action
		int nBeforeAction = Framework.getSelectedIndex(element);

		// Use regular drop down selection method
		Framework.dropDownSelect(element, sLog, dropdown);

		// Get current selected option again
		int nAfterAction = Framework.getSelectedIndex(element);

		// Did selected option change?
		if (nBeforeAction != nAfterAction)
		{
			// Run the JavaScript
			if (!JS_Util.execute(driver, sJavaScript, false))
			{
				String sError = "Javascript execution failed for following:  " + sJavaScript;
				Logs.logError(new JavaScriptException(sError));
			}

			try
			{
				// Trigger AJAX by tabbing off element
				element.sendKeys(Keys.TAB);
			}
			catch (Exception ex)
			{
				Logs.logError("Tabbing off element ('" + sLog + "') generated the following exception:  "
						+ ex.getMessage());
			}

			// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
			boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(driver, sTempUniqueNode);
			if (bCompleteBeforeTimeout)
			{
				// AJAX completed successfully before timeout
				return true;
			}
			else
			{
				// Attempts manual removal (logs error but does not throw an exception)
				removeNodeFromDOM(driver, sTempUniqueNode);

				// AJAX did not complete before timeout & user wants an exception thrown
				if (bException)
				{
					String sError = "AJAX did not complete before timeout occurred.";
					Logs.logError(new GenericActionNotCompleteException(sError));
				}
			}

			// AJAX did not complete before timeout (and user did not want an exception thrown)
			Logs.log.warn("AJAX did not complete before timeout occurred");
			return false;
		}
		else
		{
			// No AJAX should be triggered
			return true;
		}
	}

	/**
	 * This method adds a node to the DOM before clearing & entering the value into the field and TABs off to
	 * generate AJAX call and waits for the AJAX call to complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) No need to manually remove node upon failure as method handles this.<BR>
	 * 2) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param element - Element to enter text
	 * @param sLog - Element name to log
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 * @param sLoc_AJAX - Locator to find the node on which a child node will be added to the DOM
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean enter_DOM(WebElement element, String sLog, InputField value, String sLoc_AJAX,
			boolean bException)
	{
		WebDriver useDriver = Framework.getWebDriver(element);
		if (useDriver == null)
			Logs.logError("Could not get WebDriver from WebElement ('" + sLog + "')");

		// Add Node to DOM
		WebElement ajax = Framework.findElementAJAX(useDriver, sLoc_AJAX);
		addNodeToDOM(ajax);

		// Enter field
		Framework.enterField(element, sLog, value);

		// TAB away from the field to generate AJAX call
		try
		{
			element.sendKeys(Keys.TAB);
		}
		catch (Exception ex)
		{
			Logs.logError("Tabbing off element ('" + sLog + "') generated the following exception:  "
					+ ex.getMessage());
		}

		// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
		boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(useDriver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// Attempts manual removal (logs error but does not throw an exception)
			removeNodeFromDOM(useDriver, sTempUniqueNode);

			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred.";
				Logs.logError(new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Logs.log.warn("AJAX did not complete before timeout occurred");
		return false;
	}

	/**
	 * Based on value object, field can be skipped. If field entered the following occurs, method readies the
	 * element by changing the initial input value as necessary before adding a node to the DOM before
	 * clearing & entering the value into the field and TABs off to generate AJAX call and waits for the AJAX
	 * call to complete. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) No need to manually remove node upon failure as method handles this.<BR>
	 * 2) Parameter Strings cannot be null<BR>
	 * 3) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 
	 * @param element - Element to enter text
	 * @param sLog - Element name to log
	 * @param sLoc_AJAX - Locator to find the node on which a child node will be added to the DOM
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 * @param sTempValue - Temp Value to enter into the field if necessary to ready field for user real input.
	 *            This value cannot be the empty string.
	 */
	public static void enter_DOM(WebElement element, String sLog, String sLoc_AJAX, InputField value,
			String sTempValue)
	{
		if (value != null && value.modifyField())
		{
			String sCurrentValue = Conversion.nonNull(Framework.getAttribute(element,
					Framework.getInputAttr()));
			if (sCurrentValue.equals(value.getValueToInput()))
			{
				/*
				 * If current value is the empty string, then change field to be the temp value (which cannot
				 * be empty) else use the empty string as the temp value.
				 */
				String sUsedTempValue;
				if (sCurrentValue.equals(""))
					sUsedTempValue = sTempValue;
				else
					sUsedTempValue = "";

				InputField tempValue = new InputField(false, sUsedTempValue, "");
				enter_DOM(element, sLog, tempValue, sLoc_AJAX, bException);
				Logs.log.info("Element ('" + sLog + "') is ready for user's input");
				Framework.sleep(nDelay);
			}

			// Enter user's value and trigger AJAX
			enter_DOM(element, sLog, value, sLoc_AJAX, bException);
		}
		else
		{
			// Skip the field but log some useful information
			String sValue = Conversion.nonNull(Framework.getAttribute(element, Framework.getInputAttr()));
			Logs.log.info("The field '" + sLog + "' was skipped.  Default value was '" + sValue + "'");
		}
	}

	/**
	 * This method adds a node to the DOM using the <B>specified JavaScript</B> before clicking the option as
	 * necessary to make the check box have the user's desired state and waiting for the node to removed.
	 * (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 4) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 5) Check box only selected or unselected as necessary to get to user's desired state<BR>
	 * 6) Verifies that check box is enabled<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the option to click
	 * @param sLog - Element name to log
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param checkbox - Object that contains information about check box
	 */
	public static void checkbox_DOM(WebDriver driver, String sLocator, String sLog, String sJavaScript,
			CheckBox checkbox)
	{
		// Is check box enable/selected?
		WebElement element = Framework.findElementAJAX(driver, sLocator);
		boolean bEnabled = Framework.isElementEnabled(element);
		boolean bChecked = Framework.isElementSelected(element);

		// Does user want to skip taking action?
		if (checkbox.skip)
		{
			String sMessage = "Check box for '" + sLog + "' was skipped.  The check box was ";
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
		else
		{
			checkbox_DOM(driver, sLocator, sLog, checkbox.check, sJavaScript);
		}
	}

	/**
	 * This method adds a node to the DOM before clicking the option and waiting for the node to removed.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) No need to manually remove node upon failure as method handles this.<BR>
	 * 2) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param element - Check box element
	 * @param sLog - Element name to log
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean click_DOM(WebElement element, String sLog, String sJavaScript, boolean bException)
	{
		WebDriver useDriver = Framework.getWebDriver(element);
		if (useDriver == null)
			Logs.logError("Could not get WebDriver from WebElement ('" + sLog + "')");

		// Run the JavaScript
		if (!JS_Util.execute(useDriver, sJavaScript, false))
		{
			String sError = "Javascript execution failed for following:  " + sJavaScript;
			Logs.logError(new JavaScriptException(sError));
		}

		// Click the option
		Framework.click(element, sLog);

		// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
		boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(useDriver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// Attempts manual removal (logs error but does not throw an exception)
			removeNodeFromDOM(useDriver, sTempUniqueNode);

			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred.";
				Logs.logError(new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Logs.log.warn("AJAX did not complete before timeout occurred");
		return false;
	}

	/**
	 * This method adds a node to the DOM using the <B>specified JavaScript</B> before clicking the option as
	 * necessary to make the check box have the user's desired state and waiting for the node to removed.
	 * (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) AJAX.bException controls whether an exception will be thrown or not for timeout<BR>
	 * 4) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 5) Check box only selected or unselected as necessary to get to user's desired state<BR>
	 * 6) Verifies that check box is enabled<BR>
	 * 
	 * @param element - Check box element
	 * @param sLog - Element name to log
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param checkbox - Object that contains information about check box
	 */
	public static void checkbox_DOM(WebElement element, String sLog, String sJavaScript, CheckBox checkbox)
	{
		// Is check box enable/selected?
		boolean bEnabled = Framework.isElementEnabled(element);
		boolean bChecked = Framework.isElementSelected(element);

		// Does user want to skip taking action?
		if (checkbox.skip)
		{
			String sMessage = "Check box for '" + sLog + "' was skipped.  The check box was ";
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
		else
		{
			// Do we need to take action based on current state?
			boolean bTakeAction = false;
			if (checkbox.check)
			{
				if (bChecked)
					Logs.log.info("Check box for '" + sLog + "' was already selected");
				else
					bTakeAction = true;
			}
			else
			{
				if (bChecked)
					bTakeAction = true;
				else
					Logs.log.info("Check box for '" + sLog + "' was already unselected");
			}

			// Only click the check box if it is necessary to make it the desired state by the user
			if (bTakeAction)
			{
				click_DOM(element, sLog, sJavaScript, bException);
			}
		}
	}

	/**
	 * Clicks an element and waits for the AJAX action to complete
	 * 
	 * @param element - Element to click
	 * @param sLog - Element Name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	private static boolean click(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime,
			boolean bException)
	{
		// Click the option
		Framework.click(element, sLog);

		// Wait for AJAX to complete
		if (Framework.wasElementRefreshed(ajax, nMaxWaitTime))
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred.";
				Logs.logError(new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Logs.log.warn("AJAX did not complete before timeout occurred");
		return false;
	}

	/**
	 * Clicks an element and waits for the AJAX action to complete
	 * 
	 * @param element - Element to click
	 * @param sLog - Element Name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 */
	public static void click(WebElement element, String sLog, WebElement ajax)
	{
		click(element, sLog, ajax, Framework.getTimeoutInMilliseconds(), bException);
	}

	/**
	 * Clicks an element and waits for the AJAX action to complete
	 * 
	 * @param element - Element to click
	 * @param sLog - Element Name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 */
	public static void click(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime)
	{
		click(element, sLog, ajax, nMaxWaitTime, bException);
	}

	/**
	 * Clicks an element and waits for the AJAX action to complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) At least waiting for 1 refresh always occurs regardless of refreshes specified<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - Locator for element to click
	 * @param sLog - Element Name to log
	 * @param sLoc_AJAX - Locator for element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param retries - Number of times to retry if any exception occurs
	 * @param refreshes - Number of times to wait for the AJAX element to refresh
	 */
	public static void click(WebDriver driver, String sLocator, String sLog, String sLoc_AJAX,
			int nMaxWaitTime, int retries, int refreshes)
	{
		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = Framework.findElement(driver, sLocator, false);
				WebElement ajax = Framework.findElement(driver, sLoc_AJAX, false);
				if (click(element, sLog, ajax, nMaxWaitTime, false))
				{
					if (refreshes > 1)
					{
						try
						{
							Framework.waitForElementRefresh(driver, sLoc_AJAX, sLog, nMaxWaitTime,
									refreshes - 1);
						}
						catch (Exception e1)
						{
							// Method has already logged the exception
							throw new ElementRefreshException("");
						}
					}

					return;
				}
				else
					throw new GenericActionNotCompleteException("Action was not successful");
			}
			catch (ElementRefreshException ere)
			{
				// Method has already logged the exception
				throw ere;
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then log error and throw
				// exception
				if (attempts > retries)
				{
					String sError = "Clicking Element ('" + sLog + "') was not successful after " + attempts
							+ " attempts";
					Logs.logError(new ClickNoSuchElementException(sError));
				}
				else
				{
					Framework.sleep(Framework.getPollInterval());
				}
			}
		}
	}

	/**
	 * Clicks check box if necessary to make it the desired state and waits for the AJAX action to complete
	 * 
	 * @param element - Check box element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @param checkbox - Object that contains information about check box
	 * @return true if successful else false
	 */
	private static boolean checkbox(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime,
			boolean bException, CheckBox checkbox)
	{
		// Is check box enable/selected?
		boolean bEnabled = Framework.isElementEnabled(element);
		boolean bChecked = Framework.isElementSelected(element);

		// Does user want to skip taking action?
		if (checkbox.skip)
		{
			String sMessage = "Check box for '" + sLog + "' was skipped.  The check box was ";
			if (bEnabled)
				sMessage += "enabled";
			else
				sMessage += "disabled";

			if (bChecked)
				sMessage += " and selected";
			else
				sMessage += " and not selected";

			Logs.log.info(sMessage);
		}
		else
		{
			// Do we need to take action based on current state?
			boolean bTakeAction = false;
			if (checkbox.check)
			{
				if (bChecked)
					Logs.log.info("Check box for '" + sLog + "' was already selected");
				else
					bTakeAction = true;
			}
			else
			{
				if (bChecked)
					bTakeAction = true;
				else
					Logs.log.info("Check box for '" + sLog + "' was already unselected");
			}

			// Only click the check box if it is necessary to make it the desired state by the user
			if (bTakeAction)
			{
				return click(element, sLog, ajax, nMaxWaitTime, bException);
			}
		}

		return true;
	}

	/**
	 * Clicks check box if necessary to make it the desired state and waits for the AJAX action to complete
	 * 
	 * @param element - Check box element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param checkbox - Object that contains information about check box
	 */
	public static void checkbox(WebElement element, String sLog, WebElement ajax, CheckBox checkbox)
	{
		checkbox(element, sLog, ajax, Framework.getTimeoutInMilliseconds(), bException, checkbox);
	}

	/**
	 * Clicks check box if necessary to make it the desired state and waits for the AJAX action to complete
	 * 
	 * @param element - Check box element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param checkbox - Object that contains information about check box
	 */
	public static void checkbox(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime,
			CheckBox checkbox)
	{
		checkbox(element, sLog, ajax, nMaxWaitTime, bException, checkbox);
	}

	/**
	 * Clicks check box if necessary to make it the desired state and waits for the AJAX action to complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) At least waiting for 1 refresh always occurs regardless of refreshes specified<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - Locator for element to click
	 * @param sLog - Element Name to log
	 * @param sLoc_AJAX - Locator for element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param retries - Number of times to retry if any exception occurs
	 * @param refreshes - Number of times to wait for the AJAX element to refresh
	 * @param checkbox - Object that contains information about check box
	 */
	public static void checkbox(WebDriver driver, String sLocator, String sLog, String sLoc_AJAX,
			int nMaxWaitTime, int retries, int refreshes, CheckBox checkbox)
	{
		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = Framework.findElement(driver, sLocator, false);
				WebElement ajax = Framework.findElement(driver, sLoc_AJAX, false);
				if (checkbox(element, sLog, ajax, nMaxWaitTime, false, checkbox))
				{
					if (refreshes > 1)
					{
						try
						{
							Framework.waitForElementRefresh(driver, sLoc_AJAX, sLog, nMaxWaitTime,
									refreshes - 1);
						}
						catch (Exception e1)
						{
							// Method has already logged the exception
							throw new ElementRefreshException("");
						}
					}

					return;
				}
				else
					throw new GenericActionNotCompleteException("Action was not successful");
			}
			catch (ElementRefreshException ere)
			{
				// Method has already logged the exception
				throw ere;
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then log error and throw
				// exception
				if (attempts > retries)
				{
					String sError = "Unable to perform checkbox action on Element ('" + sLog + "') after "
							+ attempts + " attempts";
					Logs.logError(new CheckBoxNoSuchElementException(sError));
				}
				else
				{
					Framework.sleep(Framework.getPollInterval());
				}
			}
		}
	}

	/**
	 * Clears & enters the value into the field and TABs off to generate AJAX call and waits for the AJAX call
	 * to complete
	 * 
	 * @param element - Input field element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 * @param clear - true to clear field before entering value
	 * @return true if successful else false
	 */
	private static boolean enterField(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime,
			boolean bException, InputField value, boolean clear)
	{
		String sCurrentValue = Conversion.nonNull(Framework.getAttribute(element, Framework.getInputAttr()));
		if (value == null || !value.modifyField())
		{
			// Skip the field but log some useful information
			Logs.log.info("The field '" + sLog + "' was skipped.  Default value was '" + sCurrentValue + "'");
			return true;
		}

		if (sCurrentValue.equals(value.getValueToInput()))
		{
			Logs.log.info("The field '" + sLog + "' was skipped because it already has the desired value ('"
					+ sCurrentValue + "')");
			return true;
		}
		else
		{
			// Enter field
			if (clear)
				Framework.enterField(element, sLog, value);
			else
				Framework.onlyEnterField(element, sLog, value);

			// TAB away from the field to generate AJAX call
			try
			{
				element.sendKeys(Keys.TAB);
			}
			catch (Exception ex)
			{
				Logs.logError("Tabbing off element ('" + sLog + "') generated the following exception:  "
						+ ex.getMessage());
			}

			// Wait for AJAX to complete
			if (Framework.wasElementRefreshed(ajax, nMaxWaitTime))
			{
				// AJAX completed successfully before timeout
				return true;
			}
			else
			{
				// AJAX did not complete before timeout & user wants an exception thrown
				if (bException)
				{
					String sError = "AJAX did not complete before timeout occurred.";
					Logs.logError(new GenericActionNotCompleteException(sError));
				}
			}

			// AJAX did not complete before timeout (and user did not want an exception thrown)
			Logs.log.warn("AJAX did not complete before timeout occurred");
			return false;
		}
	}

	/**
	 * Clears & enters the value into the field and TABs off to generate AJAX call and waits for the AJAX call
	 * to complete
	 * 
	 * @param element - Input field element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 */
	public static void enterField(WebElement element, String sLog, WebElement ajax, InputField value)
	{
		enterField(element, sLog, ajax, Framework.getTimeoutInMilliseconds(), bException, value, true);
	}

	/**
	 * Clears & enters the value into the field and TABs off to generate AJAX call and waits for the AJAX call
	 * to complete
	 * 
	 * @param element - Input field element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 */
	public static void enterField(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime,
			InputField value)
	{
		enterField(element, sLog, ajax, nMaxWaitTime, bException, value, true);
	}

	/**
	 * Clears & enters the value into the field and TABs off to generate AJAX call and waits for the AJAX call<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) At least waiting for 1 refresh always occurs regardless of refreshes specified to complete<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - Locator for input element
	 * @param sLog - Element name to log
	 * @param sLoc_AJAX - Locator for element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param retries - Number of times to retry if any exception occurs
	 * @param refreshes - Number of times to wait for the AJAX element to refresh
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 */
	public static void enterField(WebDriver driver, String sLocator, String sLog, String sLoc_AJAX,
			int nMaxWaitTime, int retries, int refreshes, InputField value)
	{
		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = Framework.findElement(driver, sLocator, false);
				WebElement ajax = Framework.findElement(driver, sLoc_AJAX, false);
				if (enterField(element, sLog, ajax, nMaxWaitTime, false, value, true))
				{
					if (refreshes > 1)
					{
						try
						{
							Framework.waitForElementRefresh(driver, sLoc_AJAX, sLog, nMaxWaitTime,
									refreshes - 1);
						}
						catch (Exception e1)
						{
							// Method has already logged the exception
							throw new ElementRefreshException("");
						}
					}

					return;
				}
				else
					throw new GenericActionNotCompleteException("Action was not successful");
			}
			catch (ElementRefreshException ere)
			{
				// Method has already logged the exception
				throw ere;
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then log error and throw
				// exception
				if (attempts > retries)
				{
					String sError = "Clearing & Entering value into Element ('" + sLog
							+ "') was not successful after " + attempts + " attempts";
					Logs.logError(new EnterFieldNoSuchElementException(sError));
				}
				else
				{
					Framework.sleep(Framework.getPollInterval());
				}
			}
		}
	}

	/**
	 * Selects drop down option if necessary and waits for the AJAX call to complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) An exception will occur if the option cannot be found<BR>
	 * 
	 * @param element - Drop Down element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @param bTabOff - true to Tab off to trigger AJAX
	 * @param dd - Object that contains information on which drop down option to select
	 * @return true if successful else false
	 */
	private static boolean dropdown(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime,
			boolean bException, boolean bTabOff, DropDown dd)
	{
		if (dd.using == Selection.Skip)
		{
			Logs.log.info("Skipped entering '" + sLog + "'");
			return true;
		}

		// Need to get an exact option to be selected for comparison
		DropDown useDD;
		if (dd.using == Selection.Index && Conversion.parseInt(dd.option) < 0)
		{
			String sOption = Rand.randomIndexOption(element, dd);
			int nIndex = Conversion.parseInt(sOption);
			if (nIndex < 0 || nIndex < dd.minIndex)
				Logs.logError("The index (" + sOption + ") is not valid for the drop down '" + sLog + "'");

			useDD = new DropDown(Selection.Index, sOption, dd.minIndex);
		}
		else
		{
			useDD = dd.copy();
		}

		DropDownDefaults initialState = DropDownDefaults.defaultsFromElement(element);
		if (initialState.equivalent(useDD, true))
		{
			Logs.log.info("The drop down '" + sLog
					+ "' was skipped because it already has the desired option (" + initialState.toString()
					+ ")");
			return true;
		}

		// Use regular drop down selection method
		Framework.dropDownSelect(element, sLog, useDD);

		if (bTabOff)
		{
			// Get current selected option again
			int nAfterAction = Framework.getSelectedIndex(element);

			// Did selected option change?
			if (Conversion.parseInt(initialState.index) != nAfterAction)
			{
				try
				{
					// Trigger AJAX by tabbing off element
					element.sendKeys(Keys.TAB);
				}
				catch (Exception ex)
				{
					Logs.logError("Tabbing off element ('" + sLog + "') generated the following exception:  "
							+ ex.getMessage());
				}
			}
			else
			{
				// No AJAX should be triggered
				return true;
			}
		}

		// Wait for AJAX to complete
		if (Framework.wasElementRefreshed(ajax, nMaxWaitTime))
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred.";
				Logs.logError(new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Logs.log.warn("AJAX did not complete before timeout occurred");
		return false;
	}

	/**
	 * Selects drop down option if necessary and waits for the AJAX call to complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) An exception will occur if the option cannot be found<BR>
	 * 
	 * @param element - Drop Down element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param dd - Object that contains information on which drop down option to select
	 */
	public static void dropdown(WebElement element, String sLog, WebElement ajax, DropDown dd)
	{
		dropdown(element, sLog, ajax, Framework.getTimeoutInMilliseconds(), bException, false, dd);
	}

	/**
	 * Selects drop down option if necessary and waits for the AJAX call to complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) An exception will occur if the option cannot be found<BR>
	 * 
	 * @param element - Drop Down element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param dd - Object that contains information on which drop down option to select
	 */
	public static void dropdown(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime,
			DropDown dd)
	{
		dropdown(element, sLog, ajax, nMaxWaitTime, bException, false, dd);
	}

	/**
	 * Selects drop down option if necessary & TABs off to trigger AJAX and waits for the AJAX call to
	 * complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) An exception will occur if the option cannot be found<BR>
	 * 
	 * @param element - Drop Down element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param dd - Object that contains information on which drop down option to select
	 */
	public static void dropdownTabOff(WebElement element, String sLog, WebElement ajax, DropDown dd)
	{
		dropdown(element, sLog, ajax, Framework.getTimeoutInMilliseconds(), bException, true, dd);
	}

	/**
	 * Selects drop down option if necessary & TABs off to trigger AJAX and waits for the AJAX call to
	 * complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) An exception will occur if the option cannot be found<BR>
	 * 
	 * @param element - Drop Down element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param dd - Object that contains information on which drop down option to select
	 */
	public static void dropdownTabOff(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime,
			DropDown dd)
	{
		dropdown(element, sLog, ajax, nMaxWaitTime, bException, true, dd);
	}

	/**
	 * Selects drop down option if necessary and waits for the AJAX call to complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) An exception will occur if the option cannot be found<BR>
	 * 2) At least waiting for 1 refresh always occurs regardless of refreshes specified<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - Locator for drop down element
	 * @param sLog - Element Name to log
	 * @param sLoc_AJAX - Locator for element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param retries - Number of times to retry if any exception occurs
	 * @param refreshes - Number of times to wait for the AJAX element to refresh
	 * @param bTabOff - true to Tab off to trigger AJAX
	 * @param dd - Object that contains information on which drop down option to select
	 */
	public static void dropdown(WebDriver driver, String sLocator, String sLog, String sLoc_AJAX,
			int nMaxWaitTime, int retries, int refreshes, boolean bTabOff, DropDown dd)
	{
		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = Framework.findElement(driver, sLocator, false);
				WebElement ajax = Framework.findElement(driver, sLoc_AJAX, false);
				if (dropdown(element, sLog, ajax, nMaxWaitTime, false, bTabOff, dd))
				{
					if (refreshes > 1)
					{
						try
						{
							Framework.waitForElementRefresh(driver, sLoc_AJAX, sLog, nMaxWaitTime,
									refreshes - 1);
						}
						catch (Exception e1)
						{
							// Method has already logged the exception
							throw new ElementRefreshException("");
						}
					}

					return;
				}
				else
					throw new GenericActionNotCompleteException("Action was not successful");
			}
			catch (ElementRefreshException ere)
			{
				// Method has already logged the exception
				throw ere;
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then log error and throw
				// exception
				if (attempts > retries)
				{
					String sError = "Unable to perform drop down selection on Element ('" + sLog
							+ "') after " + attempts + " attempts";
					Logs.logError(new DropDownNoSuchElementException(sError));
				}
				else
				{
					Framework.sleep(Framework.getPollInterval());
				}
			}
		}
	}

	/**
	 * Collects the desired information from a list that is constantly refreshed but the data is not changing<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the data changes, then the information returned will not be valid.<BR>
	 * 2) Only use method if you know the data is not changing but the page is constantly being refreshed.<BR>
	 * 
	 * @param nCalls - Recursive call count
	 * @param nMaxCalls - Max Recursive calls to allow
	 * @param driver
	 * @param sLocator - Locator to find elements
	 * @param sAttribute - Attribute to get value (based on nMethod may not be used)
	 * @param nMethod - How the data will be gathered<BR>
	 *            1 - _VisibleAttribute - Attribute value (visible)<BR>
	 *            2 - _JavaScriptText - Text using JavaScript<BR>
	 *            3 - _JavaScriptAttribute - Attribute value using JavaScript<BR>
	 *            else - Text (visible)<BR>
	 * @param nExpectedItems - Expected elements to be found
	 * @param nProcessIndex - Index to continue processing from
	 * @param collected - Collected information
	 * @return true if data collection was successful else false
	 */
	private static boolean getInfo(int nCalls, int nMaxCalls, WebDriver driver, String sLocator,
			String sAttribute, int nMethod, int nExpectedItems, int nProcessIndex, List<String> collected)
	{
		// Prevent infinite recursion as it is possible that not enough items may be returned
		if (nCalls > nMaxCalls)
			return false;

		// We cannot continue if the data available is less than the expected data
		List<WebElement> data = Framework.findElementsAJAX(driver, sLocator, nExpectedItems);
		if (data.size() < nExpectedItems)
			return false;

		// Is this a special case in which processing complete is already complete?
		if (nExpectedItems == 0 && data.size() == 0)
			return true;

		// We cannot continue if the process index is greater than the data available
		if (nProcessIndex >= data.size())
			return false;

		for (int i = nProcessIndex; i < data.size(); i++)
		{
			// Which method should be used to get the data?
			String sValue;
			if (nMethod == _VisibleAttribute)
			{
				sValue = Framework.getAttribute(data.get(i), sAttribute);
			}
			else if (nMethod == _JavaScriptText)
			{
				sValue = JS_Util.getText(data.get(i));
			}
			else if (nMethod == _JavaScriptAttribute)
			{
				sValue = JS_Util.getAttribute(data.get(i), sAttribute);
			}
			else
			{
				sValue = Framework.getText(data.get(i));
			}

			// NULL indicates that stale element occurred and the list needs to be refreshed else the value
			// can be added to the list and processing can continue
			if (sValue == null)
			{
				return getInfo(nCalls + 1, nMaxCalls, driver, sLocator, sAttribute, nMethod, i + 1, i,
						collected);
			}
			else
			{
				collected.add(sValue);
			}
		}

		return true;
	}

	/**
	 * Collects the desired information from a list that is constantly refreshed but the data is not changing<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the data changes, then the information returned will not be valid.<BR>
	 * 2) Only use method if you know the data is not changing but the page is constantly being refreshed.<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find elements
	 * @param sAttribute - Attribute to get value (based on nMethod may not be used)
	 * @param nMaxCalls - Max Recursive calls to allow
	 * @param nMethod - How the data will be gathered<BR>
	 *            1 - _VisibleAttribute - Attribute value (visible)<BR>
	 *            2 - _JavaScriptText - Text using JavaScript<BR>
	 *            3 - _JavaScriptAttribute - Attribute value using JavaScript<BR>
	 *            else - Text (visible)<BR>
	 * @return List&lt;String&gt;
	 */
	private static List<String> getInfo(WebDriver driver, String sLocator, String sAttribute, int nMaxCalls,
			int nMethod)
	{
		List<String> data = new ArrayList<String>();

		if (!getInfo(0, nMaxCalls, driver, sLocator, sAttribute, nMethod, 0, 0, data))
		{
			String sLog = "The collection of REPLACE data was not successful using locator:  " + sLocator;
			if (nMethod == _VisibleAttribute)
				sLog = sLog.replace("REPLACE", "Attribute (Visible)");
			else if (nMethod == _JavaScriptText)
				sLog = sLog.replace("REPLACE", "Text (JavaScript)");
			else if (nMethod == _JavaScriptAttribute)
				sLog = sLog.replace("REPLACE", "Attribute (JavaScript)");
			else
				sLog = sLog.replace("REPLACE", "Text (Visible)");

			Logs.logError(sLog);
		}

		return data;
	}

	/**
	 * Collects the Visible Text from a list that is constantly refreshed but the data is not changing<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the data changes, then the information returned will not be valid.<BR>
	 * 2) Only use method if you know the data is not changing but the page is constantly being refreshed.<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find elements
	 * @param nMaxCalls - Max Recursive calls to allow
	 * @return List&lt;String&gt;
	 */
	public static List<String> getText(WebDriver driver, String sLocator, int nMaxCalls)
	{
		return getInfo(driver, sLocator, "", nMaxCalls, _VisibleText);
	}

	/**
	 * Collects the Visible Attribute Value from a list that is constantly refreshed but the data is not
	 * changing<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the data changes, then the information returned will not be valid.<BR>
	 * 2) Only use method if you know the data is not changing but the page is constantly being refreshed.<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find elements
	 * @param sAttribute - Attribute to get value
	 * @param nMaxCalls - Max Recursive calls to allow
	 * @return List&lt;String&gt;
	 */
	public static List<String> getAttribute(WebDriver driver, String sLocator, String sAttribute,
			int nMaxCalls)
	{
		return getInfo(driver, sLocator, sAttribute, nMaxCalls, _VisibleAttribute);
	}

	/**
	 * Collects the Text using JavaScript from a list that is constantly refreshed but the data is not
	 * changing<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the data changes, then the information returned will not be valid.<BR>
	 * 2) Only use method if you know the data is not changing but the page is constantly being refreshed.<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find elements
	 * @param nMaxCalls - Max Recursive calls to allow
	 * @return List&lt;String&gt;
	 */
	public static List<String> getTextJS(WebDriver driver, String sLocator, int nMaxCalls)
	{
		return getInfo(driver, sLocator, "", nMaxCalls, _JavaScriptText);
	}

	/**
	 * Collects the Attribute Value using JavaScript from a list that is constantly refreshed but the data is
	 * not changing<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the data changes, then the information returned will not be valid.<BR>
	 * 2) Only use method if you know the data is not changing but the page is constantly being refreshed.<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find elements
	 * @param sAttribute - Attribute to get value
	 * @param nMaxCalls - Max Recursive calls to allow
	 * @return List&lt;String&gt;
	 */
	public static List<String> getAttributeJS(WebDriver driver, String sLocator, String sAttribute,
			int nMaxCalls)
	{
		return getInfo(driver, sLocator, sAttribute, nMaxCalls, _JavaScriptAttribute);
	}

	/**
	 * Wait for the temporary node removal from the DOM<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * 1) This method should be used in conjunction with the method <B>addNodeToDOM</B> which adds a temp node
	 * to the DOM<BR>
	 * 
	 * @param driver
	 * @throws GenericActionNotCompleteException if node is not removed from the DOM before timeout occurs
	 */
	public static void waitForNodeRemovalFromDOM(WebDriver driver)
	{
		boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(driver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
			return;

		String sError = "Node (" + sTempUniqueNode + ") was not removed from the DOM before timeout occurred";
		Logs.logError(new GenericActionNotCompleteException(sError));
	}

	/**
	 * Only enters the value into the field and TABs off to generate AJAX call and waits for the AJAX call to
	 * complete
	 * 
	 * @param element - Input field element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 */
	public static void onlyEnterField(WebElement element, String sLog, WebElement ajax, InputField value)
	{
		enterField(element, sLog, ajax, Framework.getTimeoutInMilliseconds(), bException, value, false);
	}

	/**
	 * Only enters the value into the field and TABs off to generate AJAX call and waits for the AJAX call to
	 * complete
	 * 
	 * @param element - Input field element
	 * @param sLog - Element name to log
	 * @param ajax - Element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 */
	public static void onlyEnterField(WebElement element, String sLog, WebElement ajax, int nMaxWaitTime,
			InputField value)
	{
		enterField(element, sLog, ajax, nMaxWaitTime, bException, value, false);
	}

	/**
	 * Only enters the value into the field and TABs off to generate AJAX call and waits for the AJAX call<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) At least waiting for 1 refresh always occurs regardless of refreshes specified to complete<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - Locator for input element
	 * @param sLog - Element name to log
	 * @param sLoc_AJAX - Locator for element that is refreshed once AJAX request is complete
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param retries - Number of times to retry if any exception occurs
	 * @param refreshes - Number of times to wait for the AJAX element to refresh
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 */
	public static void onlyEnterField(WebDriver driver, String sLocator, String sLog, String sLoc_AJAX,
			int nMaxWaitTime, int retries, int refreshes, InputField value)
	{
		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = Framework.findElement(driver, sLocator, false);
				WebElement ajax = Framework.findElement(driver, sLoc_AJAX, false);
				if (enterField(element, sLog, ajax, nMaxWaitTime, false, value, false))
				{
					if (refreshes > 1)
					{
						try
						{
							Framework.waitForElementRefresh(driver, sLoc_AJAX, sLog, nMaxWaitTime,
									refreshes - 1);
						}
						catch (Exception e1)
						{
							// Method has already logged the exception
							throw new ElementRefreshException("");
						}
					}

					return;
				}
				else
					throw new GenericActionNotCompleteException("Action was not successful");
			}
			catch (ElementRefreshException ere)
			{
				// Method has already logged the exception
				throw ere;
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then log error and throw
				// exception
				if (attempts > retries)
				{
					String sError = "Clearing & Entering value into Element ('" + sLog
							+ "') was not successful after " + attempts + " attempts";
					Logs.logError(new EnterFieldNoSuchElementException(sError));
				}
				else
				{
					Framework.sleep(Framework.getPollInterval());
				}
			}
		}
	}
}
