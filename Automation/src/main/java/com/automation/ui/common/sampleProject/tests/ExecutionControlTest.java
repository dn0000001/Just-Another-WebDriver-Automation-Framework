package com.automation.ui.common.sampleProject.tests;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.dataStructures.config.ContextKey;
import com.automation.ui.common.sampleProject.dataStructures.LoginDetails;
import com.automation.ui.common.sampleProject.dataStructures.TestContext;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.VTD_XML;

/**
 * This class is for all the Login tests
 */
public class ExecutionControlTest extends Controller {
	// Test Case count for each method used to make it easier to identify failed test cases
	private static int nTestCase = 0;

	// Flag to know if executing positive test cases
	private static boolean bExecutingPositiveTests = false;

	// Data Driven Values files
	private static String sDataDrivenValues_ExecutionControl;

	// root node of the xml file
	private static String sRootNode = "/data/";

	// xpath nodes to find data in the xml file for specific tests
	private static String sNode_ExecutionControl = "ExecutionControl";

	/*
	 * Method names for all the negative tests. Used in data provider(s) to pick correct data file to use.
	 * Note: If the during method names are changed during refactoring, this section must be updated.
	 */
	private static final String POSITIVE = "runExecutionControlTest";

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
		sDataDrivenValues_ExecutionControl = getDataDrivenValuesFileUsingBasePath(sNode_ExecutionControl);
		verifyValidDataDrivenFile(sUseConfigFile, sNode_ExecutionControl, sDataDrivenValues_ExecutionControl);
		Translations.update(ConfigRun.sTranslationsFile);
	}

	/**
	 * Gets data driven values for the test. (This is used for both positive & negative tests.)
	 * 
	 * @param m
	 * @return data for testNG
	 */
	@DataProvider(name = "ExecutionControl")
	public static Object[][] dataForLogin(Method m)
	{
		// Reset flag for positive test case execution used for logging
		bExecutingPositiveTests = false;

		/*
		 * Determine which data to get from the xml file
		 */
		String sXpath_Root = sRootNode;
		sXpath_Root += sNode_ExecutionControl;
		bExecutingPositiveTests = true;

		// Reset test case count for method used in logging
		nTestCase = 0;

		// Object array for testNG
		Object[][] testngDataObject = null;

		// Read the XML file
		String sXML_File = sDataDrivenValues_ExecutionControl;
		VTD_XML xml;
		try
		{
			// Parse into usable format
			xml = new VTD_XML(sXML_File);

			// Get max number of test cases
			int nMaxPosition = xml.getNodesCount(sXpath_Root);

			// Get the start & stop positions
			int nStart = getValidStartPosition(nMaxPosition, execution_start);
			int nStop = getValidStopPosition(nMaxPosition, execution_stop);

			// Calculate the array size needed
			// Note: Add 1 because inclusive
			int nNodes = nStop - nStart + 1;

			// Initialize array which will store the test data
			testngDataObject = new Object[nNodes][2];

			// Loop through all the test cases and load the data into the array
			int nIndex = 0;
			for (int pos = nStart; pos <= nStop; pos++)
			{
				/*
				 * Construct the xpath to the specific test case
				 */
				String sXpath_Start = sXpath_Root + "[" + pos + "]/";

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
				testngDataObject[nIndex][0] = sUniqueID;
				testngDataObject[nIndex][1] = tc;
				nIndex++;
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
	 * Test the Execution Control functionality
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param data - Test Context
	 */
	@Test(dataProvider = "ExecutionControl", enabled = true)
	public static void runExecutionControlTest(String sUniqueID, TestContext data)
	{
		if (nTestCase == 0 && bExecutingPositiveTests)
			Logs.log.info(Framework.getNewLine() + "Method " + POSITIVE + " executing ..."
					+ Framework.getNewLine());

		writeTestIDtoLog(data.browser1.getUniqueID());

		nTestCase++;
		WebDriver driver = data.browser1.getDriver();
		BasicTestContext.maximizeWindow(driver);
		Framework.get(driver, data.browser1.getURL());

		writeTestSuccessToLog(data.browser1.getUniqueID());
	}
}
