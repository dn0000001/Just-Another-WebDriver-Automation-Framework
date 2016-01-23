package com.automation.ui.common.sampleProject.tests;

import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.dataStructures.config.ContextKey;
import com.automation.ui.common.sampleProject.dataStructures.LoginDetails;
import com.automation.ui.common.sampleProject.dataStructures.TestContext;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.JS_Util;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.VTD_XML;

/**
 * This class is for unit testing JS_Util class
 */
public class UnitTestJS extends Controller {
	// Data Driven Values files
	private static String sDataDrivenValues_Research;

	// root node of the xml file
	private static String sRootNode = "/data/";

	// xpath nodes to find data in the xml file for specific tests
	private static String sNode_Research = "Research";

	public enum LoginVar
	{
		Username, Password
	}

	public enum AddressVar
	{
		FirstName, LastName, Phone, Address
	}

	/**
	 * Initializes all variables from config file
	 * 
	 * @param sUseConfigFile - Configuration file to load variables from
	 * @param sAllContexts - Flag to indicate whether to make all contexts available or only contexts that are
	 *            specified by system properties or the configuration XML
	 */
	@Parameters({ "sUseConfigFile", "sAllContexts" })
	@BeforeSuite(groups = "setup")
	@Override
	public void initialization(@Optional(ConfigRun.CONFIG_FILE) String sUseConfigFile,
			@Optional(ConfigRun._AllContexts) String sAllContexts)
	{
		super.initialization(sUseConfigFile, sAllContexts);
		sDataDrivenValues_Research = getDataDrivenValuesFileUsingBasePath(sNode_Research);
		verifyValidDataDrivenFile(sUseConfigFile, sNode_Research, sDataDrivenValues_Research);
		Translations.update(ConfigRun.sTranslationsFile);
	}

	/**
	 * Gets data driven values for the test. (This is used for both positive & negative tests.)
	 * 
	 * @param m
	 * @return data for testNG
	 */
	@DataProvider(name = "Research")
	public static Object[][] dataForLogin(Method m)
	{
		/*
		 * Determine which data to get from the xml file
		 */
		String sXpath_Root = sRootNode;

		sXpath_Root += sNode_Research;

		// Object array for testNG
		Object[][] testngDataObject = null;

		// Read the XML file
		String sXML_File = sDataDrivenValues_Research;
		VTD_XML xml;
		try
		{
			// Parse into usable format
			xml = new VTD_XML(sXML_File);

			// Get number of test cases
			int nNodes = xml.getNodesCount(sXpath_Root);

			// Initialize array which will store the test data
			testngDataObject = new Object[nNodes][2];

			// Loop through all the test cases and load the data into the array
			for (int i = 0; i < nNodes; i++)
			{
				/*
				 * Construct the xpath to the specific test case
				 */
				String sXpath_Start = sXpath_Root + "[" + (i + 1) + "]/";

				/*
				 * Put data in variables before instantiating the object to hold them
				 */

				// Get the uniqueID for the test case (or construct)
				String sUniqueID = xml.getNodeValue(sXpath_Start + "UniqueID", createUniqueID());

				// Login variables
				String sUserName = xml.getNodeValue(sXpath_Start + "TestOptions/UserName", "");
				String sPassword = xml.getNodeValue(sXpath_Start + "TestOptions/Password", "");
				Languages lang = LoginDetails.convert(xml.getNodeValue(sXpath_Start + "TestOptions/Language",
						null));

				// Required Password Change variables
				boolean bChangePassword = xml
						.getNodeValue(sXpath_Start + "TestOptions/ChangePassword", false);
				String sNewPassword = xml.getNodeValue(sXpath_Start + "TestOptions/NewPassword", "");
				String sConfirmPassword = xml.getNodeValue(sXpath_Start + "TestOptions/ConfirmPassword", "");

				// Instantiate the objects
				LoginDetails details = new LoginDetails(sUserName, sPassword, lang, bChangePassword,
						sNewPassword, sConfirmPassword);

				TestContext tc = new TestContext(details);
				tc.setBrowser1((BasicTestContext) context.get(ContextKey.primary));
				tc.browser1.setUniqueID(sUniqueID);

				tc.setBrowser2((BasicTestContext) context.get(ContextKey.primary));
				tc.browser2.setUniqueID(sUniqueID);

				// Put in the object array for testNG
				testngDataObject[i][0] = sUniqueID;
				testngDataObject[i][1] = tc;
			}
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			Logs.log.error(ex);
			System.exit(3);
		}

		return testngDataObject;
	}

	/**
	 * Testing JS_Util methods
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param data - Test Context
	 */
	@Test(dataProvider = "Research", enabled = true)
	public static void runJS_Test(String sUniqueID, TestContext data)
	{
		writeTestIDtoLog("runJS_Test");

		WebDriver driver = data.browser1.getDriver();

		JS_Util.resizeWindow(driver, 500, 500);
		Framework.sleep(3000);
		JS_Util.maximizeWindow(driver);
		Framework.sleep(3000);
		Framework.get(driver, data.browser1.getURL());

		//
		// Manually load file in browser before continuing:
		// /resources/TechnicalTests/BasicWebDriverTest.htm"
		//
		Logs.log.info("You need to manually load:  BasicWebDriverTest.htm");

		// method click
		Framework.sleep(3000);
		WebElement login = Framework.findElement(driver, "login");
		JS_Util.click(login, "Login");
		// Verify that Login button is clicked (either error message to goes to next page)

		// method click with alert
		Framework.sleep(3000);
		Logs.log.info("You need to manually clear the alert");
		WebElement lookup = Framework.findElement(driver, "field");

		// Trigger without handling alert
		JS_Util.click(lookup, "Lookup");

		// Manually clear alert for next test
		Framework.sleep(3000);
		// Note: In IE6 this does not work.
		JS_Util.click(lookup, true, "Lookup");

		// method getAttribute
		Framework.sleep(3000);
		WebElement radio = Framework.findElement(driver, "lang_EN");
		Logs.log.info(JS_Util.getAttribute(radio, "id"));
		Logs.log.info(JS_Util.getAttribute(radio, "value"));

		// method getText
		WebElement expertText = Framework.findElement(driver, "labelExpert");
		Logs.log.info("JS:      " + JS_Util.getText(expertText));
		Logs.log.info("Driver:  " + Framework.getText(expertText));

		// method scrollIntoView
		Framework.sleep(3000);
		JS_Util.scrollIntoView(login);
		Framework.sleep(3000);
		JS_Util.scrollIntoView(lookup);

		// method checkbox
		Framework.sleep(3000);
		WebElement expert = Framework.findElement(driver, "expert");
		JS_Util.checkbox(expert, "Expert check box", new CheckBox(true));
		// Verify check box is selected
		Framework.sleep(3000);
		JS_Util.checkbox(expert, "Expert check box", new CheckBox(false));

		// method triggerMouseEvent
		Framework.sleep(3000);
		WebElement forgot = Framework.findElement(driver, "//a");
		Framework.mouseOver(forgot, "Mouse Over", true);
		// Verify that message mouse over is displayed
		Framework.sleep(3000);
		// Verify that message mouse over is removed
		Framework.mouseOut(forgot, "Mouse Out", true);

		// method triggerHTMLEvent
		Framework.sleep(3000);
		Framework.triggerOnChange(expert, "On Change", true);
		// Verify that message about changing expert option is displayed

		// method getParent
		Framework.sleep(3000);
		WebElement mainTable = Framework.findElement(driver, "//form/table");
		WebElement parent = JS_Util.getParent(mainTable);
		Logs.log.info("Form ID:  " + Framework.getAttribute(parent, "id"));

		// method getSiblings
		List<WebElement> siblings = JS_Util.getSiblings(radio);
		Logs.log.info("Radio options:  " + siblings.size());
		for (WebElement option : siblings)
		{
			Logs.log.info(Framework.getAttribute(option, "value"));
		}

		// method getChildren
		WebElement dropdown = Framework.findElement(driver, "//select");
		List<WebElement> children = JS_Util.getChildren(dropdown);
		Logs.log.info("Drop Down options:  " + children.size());
		for (WebElement option : children)
		{
			Logs.log.info(Framework.getAttribute(option, "value"));
		}

		writeTestSuccessToLog("runJS_Test");
	}
}
