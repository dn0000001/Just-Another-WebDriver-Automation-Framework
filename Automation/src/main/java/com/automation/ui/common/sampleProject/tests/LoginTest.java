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
import com.automation.ui.common.sampleProject.exceptions.LoginCannotUseSamePasswordException;
import com.automation.ui.common.sampleProject.exceptions.LoginChangePasswordException;
import com.automation.ui.common.sampleProject.exceptions.LoginCredentialsWrongException;
import com.automation.ui.common.sampleProject.exceptions.LoginPasswordBlankException;
import com.automation.ui.common.sampleProject.exceptions.LoginUserNameBlankException;
import com.automation.ui.common.sampleProject.pages.Login;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.VTD_XML;

/**
 * This class is for all the Login tests
 */
public class LoginTest extends Controller {
	// Test Case count for each method used to make it easier to identify failed test cases
	private static int nTestCase = 0;

	// Flag to know if executing positive test cases
	private static boolean bExecutingPositiveTests = false;

	// Data Driven Values files
	private static String sDataDrivenValues_Login;

	// root node of the xml file
	private static String sRootNode = "/data/";

	// xpath nodes to find data in the xml file for specific tests
	private static String sNode_Login = "Login";
	private static String sNode_ChangePasswordAlert = "ChangePasswordAlert";
	private static String sNode_UsernameBlank = "UsernameBlank";
	private static String sNode_PasswordBlank = "PasswordBlank";
	private static String sNode_CredentialsWrong = "CredentialsWrong";
	private static String sNode_CannotReusePassword = "CannotReusePassword";

	/*
	 * Method names for all the negative tests. Used in data provider(s) to pick correct data file to use.
	 * Note: If the during method names are changed during refactoring, this section must be updated.
	 */
	private static final String POSITIVE = "runLoginPositiveFlow";
	private static final String ALERT_FLOW = "runChangePasswordAlertFlow";
	private static final String USERNAME_BLANK = "runUserNameBlankFlow";
	private static final String PASSWORD_BLANK = "runPasswordBlankFlow";
	private static final String CREDENTIALS_WRONG = "runCredentialsWrongFlow";
	private static final String CANNOT_REUSE_PASSWORD = "runChangePasswordReuseErrorFlow";

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
		sDataDrivenValues_Login = getDataDrivenValuesFileUsingBasePath(sNode_Login);
		verifyValidDataDrivenFile(sUseConfigFile, sNode_Login, sDataDrivenValues_Login);
		Translations.update(ConfigRun.sTranslationsFile);
	}

	/**
	 * Gets data driven values for the test. (This is used for both positive & negative tests.)
	 * 
	 * @param m
	 * @return data for testNG
	 */
	@DataProvider(name = "Login")
	public static Object[][] dataForLogin(Method m)
	{
		// Reset flag for positive test case execution used for logging
		bExecutingPositiveTests = false;

		/*
		 * Determine which data to get from the xml file
		 */
		String sXpath_Root = sRootNode;
		if (m.getName().equals(ALERT_FLOW))
		{
			sXpath_Root += sNode_ChangePasswordAlert;
		}
		else if (m.getName().equals(USERNAME_BLANK))
		{
			sXpath_Root += sNode_UsernameBlank;
		}
		else if (m.getName().equals(PASSWORD_BLANK))
		{
			sXpath_Root += sNode_PasswordBlank;
		}
		else if (m.getName().equals(CREDENTIALS_WRONG))
		{
			sXpath_Root += sNode_CredentialsWrong;
		}
		else if (m.getName().equals(CANNOT_REUSE_PASSWORD))
		{
			sXpath_Root += sNode_CannotReusePassword;
		}
		else
		{
			sXpath_Root += sNode_Login;
			bExecutingPositiveTests = true;
		}

		// Reset test case count for method used in logging
		nTestCase = 0;

		// Object array for testNG
		Object[][] testngDataObject = null;

		// Read the XML file
		String sXML_File = sDataDrivenValues_Login;
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
	 * Test the Login functionality
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param data - Test Context
	 */
	@Test(dataProvider = "Login", groups = { "LoginTest.runLoginPositiveFlow" }, enabled = true)
	public static void runLoginPositiveFlow(String sUniqueID, TestContext data)
	{
		if (nTestCase == 0 && bExecutingPositiveTests)
			Logs.log.info(Framework.getNewLine() + "Method " + POSITIVE + " executing ..."
					+ Framework.getNewLine());

		writeTestIDtoLog(data.browser1.getUniqueID());

		nTestCase++;
		WebDriver driver = data.browser1.getDriver();
		BasicTestContext.maximizeWindow(driver);
		Framework.get(driver, data.browser1.getURL());

		// Should screenshots be saved?
		data.browser1.setScreenshotPreferencesForTest();

		/*
		 * Login test using browser 1
		 */
		Login loginPage = new Login(driver);
		loginPage.loginAs(data.login);
		driver.close();

		/*
		 * Login test using browser 2
		 */
		driver = data.browser2.getDriver();
		BasicTestContext.maximizeWindow(driver);
		Framework.get(driver, data.browser2.getURL());
		loginPage = new Login(driver);
		loginPage.loginAs(data.login);
		driver.close();

		writeTestSuccessToLog(data.browser1.getUniqueID());
	}

	/**
	 * Test that Login fails when during Change Password, New Password does not match Confirm Password
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param data - Login detail variables
	 */
	@Test(dataProvider = "Login", groups = { "LoginTest.runChangePasswordAlertFlow" },
			expectedExceptions = LoginChangePasswordException.class, enabled = true)
	public static void runChangePasswordAlertFlow(String sUniqueID, TestContext data)
	{
		if (nTestCase == 0)
			Logs.log.info(Framework.getNewLine() + "Method " + ALERT_FLOW + " executing ..."
					+ Framework.getNewLine());

		runLoginPositiveFlow(sUniqueID, data);
	}

	/**
	 * Test that Login fails when the User Name is blank
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param data - Login detail variables
	 */
	@Test(dataProvider = "Login", groups = { "LoginTest.runUserNameBlankFlow" },
			expectedExceptions = LoginUserNameBlankException.class, enabled = true)
	public static void runUserNameBlankFlow(String sUniqueID, TestContext data)
	{
		if (nTestCase == 0)
			Logs.log.info(Framework.getNewLine() + "Method " + USERNAME_BLANK + " executing ..."
					+ Framework.getNewLine());

		runLoginPositiveFlow(sUniqueID, data);
	}

	/**
	 * Test that Login fails when the Password is blank
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param data - Login detail variables
	 */
	@Test(dataProvider = "Login", groups = { "LoginTest.runPasswordBlankFlow" },
			expectedExceptions = LoginPasswordBlankException.class, enabled = true)
	public static void runPasswordBlankFlow(String sUniqueID, TestContext data)
	{
		if (nTestCase == 0)
			Logs.log.info(Framework.getNewLine() + "Method " + PASSWORD_BLANK + " executing ..."
					+ Framework.getNewLine());

		runLoginPositiveFlow(sUniqueID, data);
	}

	/**
	 * Test that Login fails when the Credentials are wrong
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param data - Login detail variables
	 */
	@Test(dataProvider = "Login", groups = { "LoginTest.runCredentialsWrongFlow" },
			expectedExceptions = LoginCredentialsWrongException.class, enabled = true)
	public static void runCredentialsWrongFlow(String sUniqueID, TestContext data)
	{
		if (nTestCase == 0)
			Logs.log.info(Framework.getNewLine() + "Method " + CREDENTIALS_WRONG + " executing ..."
					+ Framework.getNewLine());

		runLoginPositiveFlow(sUniqueID, data);
	}

	/**
	 * Test that Login fails when during Change Password, the New Password cannot be reused again
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param data - Login detail variables
	 */
	@Test(dataProvider = "Login", groups = { "LoginTest.runChangePasswordReuseErrorFlow" },
			expectedExceptions = LoginCannotUseSamePasswordException.class, enabled = true)
	public static void runChangePasswordReuseErrorFlow(String sUniqueID, TestContext data)
	{
		if (nTestCase == 0)
			Logs.log.info(Framework.getNewLine() + "Method " + CANNOT_REUSE_PASSWORD + " executing ..."
					+ Framework.getNewLine());

		runLoginPositiveFlow(sUniqueID, data);
	}
}
