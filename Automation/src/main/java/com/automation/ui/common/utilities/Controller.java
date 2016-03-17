package com.automation.ui.common.utilities;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DB_Type;
import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.SendEmailDetails;
import com.automation.ui.common.dataStructures.SessionInfo;
import com.automation.ui.common.dataStructures.config.ConfigEmail;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.dataStructures.config.ConfigXML;
import com.automation.ui.common.dataStructures.config.ContextKey;
import com.automation.ui.common.dataStructures.config.RuntimeProperty;
import com.automation.ui.common.exceptions.GenericUnexpectedException;

/**
 * This class controls the following:<BR>
 * 1) Initialization of the common test variables.<BR>
 * 2) Sending of e-mail after completion.<BR>
 * <BR>
 * <B>Use this as the base class for all tests.</B>
 */
public class Controller {
	/**
	 * Used when constructing an unique ID
	 * Note: Do not manually update this variable.
	 */
	private static int nUniqueIDNum = 1;

	/**
	 * Variable stores the decoded key value pairs
	 */
	protected static List<Parameter> _DecodedPairs;

	/**
	 * All the contexts available for the test
	 */
	protected static GenericData context;

	/**
	 * Execution Start Position of the tests in the test suite. This variable can be used to control which is
	 * the starting test in the test suite when the same TestNG XML file is used. This allows splitting many
	 * tests that take a long time to execution into sets for parallel execution. This value should be treated
	 * as inclusive.
	 */
	protected static int execution_start;

	/**
	 * Execution End Position for the tests in the test suite. This variable can be used to control which is
	 * the last test in the test suite when the same TestNG XML file is used. This allows splitting many
	 * tests that take a long time to execution into sets for parallel execution. This value should be treated
	 * as inclusive. The value -1 should be used to indicate from the start position to the last test in the
	 * file if unknown number of tests.
	 */
	protected static int execution_stop;

	/**
	 * Initializes all variables from config file<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should be overridden in any inherited class to get the data driven values files.
	 * (Call this method followed by the statements to get the data driven values files.) <BR>
	 * 2) The sTranslationsFile variable contains the location to the translations file. (When overriding this
	 * method in any inherited class it should update any translations)<BR>
	 * 
	 * @param sUseConfigFile - Configuration file to load variables from
	 * @param sAllContexts - Flag to indicate whether to make all contexts available or only contexts that are
	 *            specified by system properties or the configuration XML
	 */
	@Parameters({ "sUseConfigFile", "sAllContexts" })
	@BeforeSuite(groups = "setup")
	public void initialization(@Optional(ConfigRun.CONFIG_FILE) String sUseConfigFile,
			@Optional(ConfigRun._AllContexts) String sAllContexts)
	{
		// Change the log properties file if necessary
		String sTempLogProp = Misc.getProperty(RuntimeProperty.env_log_prop, "");
		if (!sTempLogProp.equals(""))
			Logs.LOG_PROPS = sTempLogProp;

		/*
		 * Change the log folder and/or file if necessary
		 * 
		 * NOTES:
		 * 1) For the change to be used, then the logger.properties file must be using ${Logs.FILE} to define
		 * the file locations. The reason for this is log4j is controlling this via configuration.
		 * 2) Based on the logger.properties file this could lead to an error creating the log file
		 */
		String sTempLogFolder = Misc.getProperty(RuntimeProperty.env_log_folderAndOrFile, "");
		if (!sTempLogFolder.equals(""))
		{
			// Add SubFolder if specified
			String sSubFolder = Misc.getProperty(RuntimeProperty.env_log_subfolder, "");
			sTempLogFolder += sSubFolder;

			Logs.setFolderFile(sTempLogFolder);
			Logs.setFolderHTML(sTempLogFolder);
			Logs.setProperty4FolderFile(sTempLogFolder);
			Logs.setProperty4FolderHTML(sTempLogFolder);
		}

		/*
		 * Log4j configuration
		 * Note: Must be called before attempting to write logs
		 */
		Logs.initializeLoggers();
		Logs.log.info("******************************");
		Logs.log.info("");
		Logs.log.info(new Date());

		// Increment test suite counter
		Counter.increment();

		// Variables used outside the try/catch block that need to be initialized to prevent compiler error
		int nElementTimeout = 30;
		int nPollInterval = 1000;
		String sScreenshotFolder = "";
		String sTempFile = "";

		VTD_XML xml;
		try
		{
			String sTempConfig = Misc.getProperty(RuntimeProperty.env_config, sUseConfigFile);
			ConfigRun.sUseConfigFile = sTempConfig;
			xml = new VTD_XML(sTempConfig);

			// Test Execution Control variables
			execution_start = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.execution_start,
							xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.execution_start, "1")),
					1);
			execution_stop = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.execution_stop,
							xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.execution_stop, "-1")),
					-1);

			// Environment variables
			String sBrowser = Misc.getProperty(RuntimeProperty.browser_name,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.browser, ""));
			String sDriverPath = Misc.getProperty(RuntimeProperty.env_driverPath,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.driverPath, ""));
			String sBrowserProfile = Misc.getProperty(RuntimeProperty.browser_profile,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.browserProfile, ""));
			String sUrl = Misc.getProperty(RuntimeProperty.url,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.url, ""));
			int nPageTimeout = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.delay_page_timeout,
							xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.pageTimeout, "5")), 5);
			nElementTimeout = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.delay_element_timeout,
							xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.elementTimeout, "30")),
					30);
			nPollInterval = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.delay_poll_interval,
							xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.pollInterval, "1000")),
					1000);
			int nMaxTimeout = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.delay_max_timeout,
							xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.maxTimeout, "5")), 5);
			int nMultiplierTimeout = Conversion
					.parseInt(
							Misc.getProperty(
									RuntimeProperty.delay_multiplier_timeout,
									xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH
											+ ConfigXML.multiplierTimeout, "4")), 4);
			int nAJAX_Retries = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.delay_ajax_retries,
							xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._AJAX_Retries, "5")), 5);
			int nAJAX_Stable = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.delay_ajax_stable,
							xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._AJAX_Stable, "3000")),
					3000);
			sTempFile = xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.tempFile, "");

			// Selenium Grid variables
			String sHubURL = Misc.getProperty(RuntimeProperty.grid_hub,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.hubURL, ""));
			String sPlatform = Misc.getProperty(RuntimeProperty.grid_browser_platform,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.platform, ""));
			String sVersion = Misc.getProperty(RuntimeProperty.grid_browser_version,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.version, ""));
			String sApplicationName = Misc.getProperty(RuntimeProperty.grid_browser_applicationName,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.applicationName, ""));

			// Session Server variables
			String sSessionServer = Misc.getProperty(RuntimeProperty.sessions_server,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.sessionServer, ""));
			int nSessionServerPort = Conversion.parseInt(Misc.getProperty(RuntimeProperty.sessions_port,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.sessionServerPort, "-1")), -1);

			// Database variables
			String sDB_Server = Misc.getProperty(RuntimeProperty.database_server,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._DB_Server, ""));
			String sDB = Misc.getProperty(RuntimeProperty.database_name,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._DB, ""));
			String sDB_User = Misc.getProperty(RuntimeProperty.database_user,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._DB_User, ""));
			String sDB_Password = Misc.getProperty(RuntimeProperty.database_password,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._DB_Password, ""));
			int nDB_Port = Conversion.parseInt(
					Misc.getProperty(
							RuntimeProperty.database_port,
							xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._DB_Port,
									String.valueOf(Database.getDefaultPort()))), Database.getDefaultPort());
			DB_Type _DB_Type = DB_Type.to(Misc.getProperty(RuntimeProperty.database_type,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._DB_Type, "")));

			// Decode password if necessary
			boolean bDB_EncodedPassword = Conversion.parseBoolean(Misc.getProperty(
					RuntimeProperty.database_encodedPassword,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._DB_EncodedPassword, "false")));
			if (!sDB_Password.equals("") && bDB_EncodedPassword)
			{
				sDB_Password = CryptoDESede.decode(sDB_Password);
			}

			// Set the base folder that contains the SQL files
			ConfigRun.BaseSQL_Folder = Misc.getProperty(RuntimeProperty.external_sql_folder, xml
					.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.external_sql_folder,
							ConfigRun.SQL_FOLDER));

			// Set the base folder that contains the JavaScript files
			ConfigRun.BaseJavaScriptFolder = Misc.getProperty(RuntimeProperty.external_js_folder, xml
					.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.external_js_folder,
							ConfigRun.JavaScript_FOLDER));

			// Additional Documentation (Screenshots)
			boolean bScreenshotsEnabled = Conversion.parseBoolean(Misc.getProperty(
					RuntimeProperty.screenshots_enabled,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.screenshotsEnabled, "false")));
			sScreenshotFolder = Misc.getProperty(RuntimeProperty.screenshots_output, xml.getNodeValue(
					ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.screenshotFolder, sTempLogFolder));
			String sScreenshotPrefixName = Misc.getProperty(RuntimeProperty.screenshots_prefix,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.screenshotPrefixName, ""));

			// Notification variables
			ConfigEmail.bSendEmail = Conversion.parseBoolean(xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH
					+ ConfigXML.sendEmail, "false"));
			ConfigEmail.nSendEmailsAfterTestSuite = Conversion.parseInt(xml.getNodeValue(
					ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.sendEmailsAfterTestSuite, ""));
			ConfigEmail.sSMTP_Server = xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML._SMTP_Server,
					"");
			ConfigEmail.sFrom_EmailAddress = xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH
					+ ConfigXML.from_EmailAddress, "");
			ConfigEmail.sRecipients = xml
					.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.recipients, "").split(";");
			ConfigEmail.sSubject = xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.subject, "");
			ConfigEmail.sMessage = xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.message, "");
			ConfigEmail.bAttachments = xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.attachments,
					true);

			// Translations file
			ConfigRun.sTranslationsFile = Misc.getProperty(RuntimeProperty.env_translations,
					xml.getNodeValue(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.translations, ""));

			// Base Path used to find the test case XML files
			ConfigRun.env_basePath = Misc.getProperty(RuntimeProperty.env_basePath,
					xml.getNodeValue(ConfigRun.DDV_ROOT_XPATH + "/" + ConfigRun.DDV_BASEPATH_XPATH, ""));

			// Load the decoded key value pairs list
			// Note: Method getDataDrivenValuesFileUsingBasePath uses the variable env_basePath
			Lookup lookup = new Lookup(Misc.getProperty(RuntimeProperty.env_passwordLookup,
					getDataDrivenValuesFileUsingBasePath(ConfigRun.NODE_LOOKUP)), ConfigRun.NODE_LOOKUP_XPATH);
			_DecodedPairs = lookup.getKeyValuePairs();

			// Note: Unique ID needs to be set by the data provider
			BasicTestContext primaryContext = new BasicTestContext();
			primaryContext.setBrowserRelated(sBrowser, sDriverPath, sBrowserProfile, sUrl);
			primaryContext.setDelays(nPageTimeout, nElementTimeout, nPollInterval, nMaxTimeout,
					nMultiplierTimeout);
			primaryContext.setGrid(sHubURL, sPlatform, sVersion, sApplicationName);
			primaryContext.setDatabase(sDB_Server, sDB, sDB_User, sDB_Password, nDB_Port, _DB_Type);
			primaryContext.setScreenshot(bScreenshotsEnabled, sScreenshotFolder, sScreenshotPrefixName);
			primaryContext.setAJAX(nAJAX_Retries, nAJAX_Stable);
			primaryContext.setSessionServer(sSessionServer, nSessionServerPort);

			// Initialize the variable that holds all the contexts
			context = new GenericData();

			List<ContextKey> desiredContexts;
			if (Conversion.parseBoolean(sAllContexts))
			{
				// Get all contexts in the ContextKey enumeration regardless if specified by system properties
				// or the configuration XML
				desiredContexts = getAllContexts();
			}
			else
			{
				// Get all the contexts specified as system properties to be added
				desiredContexts = getSystemPropertiesContexts();
			}

			//
			// Read any additional contexts. If variable for context is not specified, then it will default to
			// the primary context.
			//
			int nContextCount = xml.getNodesCount(ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.additionalContexts);
			for (int i = 0; i < nContextCount; i++)
			{
				String sXpath = ConfigRun.CONFIG_ROOT_XPATH + ConfigXML.additionalContexts + "[" + (i + 1)
						+ "]/";

				//
				// Get the context being read
				//
				ContextKey key = ContextKey.to(xml.getNodeValue(sXpath + ConfigXML.key, ""));

				// Remove from list of contexts specified as system properties to be added
				int nIndex = desiredContexts.indexOf(key);
				if (nIndex >= 0)
					desiredContexts.remove(nIndex);

				//
				// Read the context variables
				//

				String browser = xml.getNodeValue(sXpath + ConfigXML.browser, sBrowser);
				String driverPath = xml.getNodeValue(sXpath + ConfigXML.driverPath, sDriverPath);
				String browserProfile = xml.getNodeValue(sXpath + ConfigXML.browserProfile, sBrowserProfile);
				String url = xml.getNodeValue(sXpath + ConfigXML.url, sUrl);

				int pageTimeout = xml.getNodeValue(sXpath + ConfigXML.pageTimeout, nPageTimeout);
				int elementTimeout = xml.getNodeValue(sXpath + ConfigXML.elementTimeout, nElementTimeout);
				int pollInterval = xml.getNodeValue(sXpath + ConfigXML.pollInterval, nPollInterval);
				int maxTimeout = xml.getNodeValue(sXpath + ConfigXML.maxTimeout, nMaxTimeout);
				int multiplier = xml.getNodeValue(sXpath + ConfigXML.multiplierTimeout, nMultiplierTimeout);

				String hubURL = xml.getNodeValue(sXpath + ConfigXML.hubURL, sHubURL);
				String platform = xml.getNodeValue(sXpath + ConfigXML.platform, sPlatform);
				String version = xml.getNodeValue(sXpath + ConfigXML.version, sVersion);
				String applicationName = xml.getNodeValue(sXpath + ConfigXML.applicationName,
						sApplicationName);

				String sessionServer = xml.getNodeValue(sXpath + ConfigXML.sessionServer, sSessionServer);
				int sessionServerPort = xml.getNodeValue(sXpath + ConfigXML.sessionServerPort,
						nSessionServerPort);

				String _DB_Server = xml.getNodeValue(sXpath + ConfigXML._DB_Server, sDB_Server);
				String _DB = xml.getNodeValue(sXpath + ConfigXML._DB, sDB);
				String _DB_User = xml.getNodeValue(sXpath + ConfigXML._DB_User, sDB_User);
				String _DB_Password = xml.getNodeValue(sXpath + ConfigXML._DB_Password, null);
				int _DB_Port = xml.getNodeValue(sXpath + ConfigXML._DB_Port, nDB_Port);

				DB_Type _DB_Type2;
				String sDB_Type = xml.getNodeValue(sXpath + ConfigXML._DB_Type, null);
				if (sDB_Type == null)
				{
					// No DB type specified as such use default
					_DB_Type2 = _DB_Type;
				}
				else
				{
					// DB type was specified as such perform conversion
					_DB_Type2 = DB_Type.to(sDB_Type);
				}

				boolean _DB_EncodedPassword = xml.getNodeValue(sXpath + ConfigXML._DB_EncodedPassword,
						bDB_EncodedPassword);

				if (_DB_Password == null)
				{
					// No password node as such use default
					_DB_Password = sDB_Password;
				}
				else if (!_DB_Password.equals("") && _DB_EncodedPassword)
				{
					// Specified password is encrypted, decrypt and store
					_DB_Password = CryptoDESede.decode(_DB_Password);
				}

				boolean screenshotsEnabled = xml.getNodeValue(sXpath + ConfigXML.screenshotsEnabled,
						bScreenshotsEnabled);
				String screenshotFolder = xml.getNodeValue(sXpath + ConfigXML.screenshotFolder,
						sScreenshotFolder);
				String screenshotPrefixName = xml.getNodeValue(sXpath + ConfigXML.screenshotPrefixName,
						sScreenshotPrefixName);

				int _AJAX_Retries = xml.getNodeValue(sXpath + ConfigXML._AJAX_Retries, nAJAX_Retries);
				int _AJAX_Stable = xml.getNodeValue(sXpath + ConfigXML._AJAX_Stable, nAJAX_Stable);

				//
				// Initialize the context
				//
				BasicTestContext addContext = new BasicTestContext();
				addContext.setBrowserRelated(browser, driverPath, browserProfile, url);
				addContext.setDelays(pageTimeout, elementTimeout, pollInterval, maxTimeout, multiplier);
				addContext.setGrid(hubURL, platform, version, applicationName);
				addContext.setDatabase(_DB_Server, _DB, _DB_User, _DB_Password, _DB_Port, _DB_Type2);
				addContext.setScreenshot(screenshotsEnabled, screenshotFolder, screenshotPrefixName);
				addContext.setAJAX(_AJAX_Retries, _AJAX_Stable);
				addContext.setSessionServer(sessionServer, sessionServerPort);

				// Store the context
				context.add(key, getContext(key, addContext));
			}

			//
			// Add any contexts specified as system properties but were not in the configuration XML file
			//
			for (ContextKey contextKey : desiredContexts)
			{
				context.add(contextKey, getContext(contextKey, primaryContext));
			}

			// Add the Primary Context last such that if any of the additional contexts added the primary
			// context due to invalid key the original primary context is added.
			context.add(ContextKey.primary, primaryContext);

			/*
			 * Write Log information
			 */
			Logs.log.info("");
			for (ContextKey key : ContextKey.values())
			{
				if (context.containsKey(key))
				{
					BasicTestContext btc = (BasicTestContext) context.get(key);
					if (!Compare.equals(btc.getURL(), "", Comparison.Equal))
						Logs.log.info(key.toString() + "." + RuntimeProperty.url + ":  " + btc.getURL());
				}
			}

			Logs.log.info("");
			Logs.log.info("******************************" + Framework.getNewLine());
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			Logs.log.error(ex);
			System.exit(3);
		}

		// Set the timeout & poll interval that will be used.
		Framework.setTimeout(nElementTimeout);
		Framework.setPollInterval(nPollInterval);

		/*
		 * When running via a batch file. It is up to the batch file to create the temp file containing
		 * the replacements for the tokens. The reason for this is to allow the dynamic creation of the
		 * report file which the application cannot know at run-time.
		 */
		try
		{
			VTD_XML tempXML = new VTD_XML(sTempFile);
			ConfigEmail.Replacements = new String[ConfigEmail.Tokens.length];
			ConfigEmail.Replacements[0] = tempXML.getNodeValue(ConfigEmail.reportFile, ConfigEmail.Tokens[0]);

			/*
			 * Note: If the {DATE} token is used but not in the temp xml file, then I will use the current
			 * date which may not necessarily be correct. (Due to when the batch file is run versus this code
			 * being executed like running very close to midnight.)
			 */
			ConfigEmail.Replacements[1] = tempXML.getNodeValue(ConfigEmail.date,
					Conversion.convertDate(new Date(), "MM-dd-yyyy"));
			ConfigEmail.Replacements[2] = tempXML.getNodeValue(ConfigEmail.logFile, ConfigEmail.Tokens[2]);

			// Need to set the dynamic screenshot folder before executing test cases
			sScreenshotFolder = Misc.replaceTokens(sScreenshotFolder, ConfigEmail.Tokens,
					ConfigEmail.Replacements);
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * Used to send E-mail after execution
	 */
	@AfterSuite(groups = "results")
	public static void emailResults()
	{
		// Only send e-mail if count of test suites equal to or greater than specified number
		if (Counter.get() < ConfigEmail.nSendEmailsAfterTestSuite)
			return;

		// Only send e-mail if sSendEmail == true
		if (!ConfigEmail.bSendEmail)
			return;

		// Replace all the tokens from the message & subject
		String sConstructedMessage = ConfigEmail.sMessage;
		String sConstructedSubject = ConfigEmail.sSubject;
		if (ConfigEmail.Replacements != null
				&& (Misc.bTokensToReplace(ConfigEmail.sMessage, ConfigEmail.Tokens) || Misc.bTokensToReplace(
						ConfigEmail.sSubject, ConfigEmail.Tokens)))
		{
			sConstructedMessage = Misc.replaceTokens(ConfigEmail.sMessage, ConfigEmail.Tokens,
					ConfigEmail.Replacements);
			sConstructedSubject = Misc.replaceTokens(ConfigEmail.sSubject, ConfigEmail.Tokens,
					ConfigEmail.Replacements);
		}

		// Does user want attachments?
		String[] attachmentsList;
		if (ConfigEmail.bAttachments)
			attachmentsList = new String[] { ConfigRun.LOG_FILE };
		else
			attachmentsList = new String[] {};

		Email results = new Email();
		SendEmailDetails details = new SendEmailDetails(ConfigEmail.sSMTP_Server,
				ConfigEmail.sFrom_EmailAddress, ConfigEmail.sRecipients, sConstructedSubject,
				sConstructedMessage, attachmentsList);
		results.sendEmail(details);
	}

	/**
	 * Gets the value for given node from previously used & stored config XML. (If multiple values are found
	 * only the 1st is returned.)
	 * 
	 * @param sXpath - The root node that contains the node to retrieve data from
	 * @param sNode - Node that contains the information you want
	 * @return String
	 */
	public static String getSpecificNode(String sXpath, String sNode)
	{
		VTD_XML xml;
		try
		{
			xml = new VTD_XML(ConfigRun.sUseConfigFile);
			String[] DDV_NODE = { sNode };
			String[][] data = xml.getAllData(sXpath, DDV_NODE);
			return data[0][0];
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Gets the Data Driven Values File location for given node/method from system property or previously used
	 * & stored config XML. (If multiple values are found only the 1st is returned.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) System Property takes precedence over XML file<BR>
	 * 2) System Property of RuntimeProperty.data_prefix + sNode is checked/used<BR>
	 * 
	 * @param sNode - Node that contains the DDV file location
	 * @return String
	 */
	public static String getDataDrivenValuesFile(String sNode)
	{
		return Misc.getProperty(RuntimeProperty.data_prefix + sNode,
				getSpecificNode(ConfigRun.DDV_ROOT_XPATH, sNode));
	}

	/**
	 * Constructs the absolute path using the BasePath and the Data Driven Values File location for given
	 * node. (If multiple values are found only the 1st is returned.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The BasePath node only used if it can be found.<BR>
	 * 2) System Property takes precedence over XML file<BR>
	 * 3) System Property of RuntimeProperty.data_prefix + sNode is checked/used<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * <BR>
	 * <B>config.xml:</B><BR>
	 * &lt;config&gt;<BR>
	 * &nbsp;&nbsp;&lt;DataDrivenValues&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;BasePath&gt;C:\Workspace\&lt;/BasePath&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;Login&gt;data\data_Login.xml&lt;/Login&gt;<BR>
	 * &nbsp;&nbsp;&lt;/DataDrivenValues&gt;<BR>
	 * &lt;/config&gt;<BR>
	 * <BR>
	 * getDataDrivenValuesFileUsingBasePath("Login") => C:\Workspace\data\data_Login.xml<BR>
	 * 
	 * @param sNode - Node that contains the DDV file location
	 * @return String
	 */
	public static String getDataDrivenValuesFileUsingBasePath(String sNode)
	{
		VTD_XML xml;
		try
		{
			xml = new VTD_XML(ConfigRun.sUseConfigFile);

			// Get the Relative Path location
			String sRelativePathLocation = Misc.getProperty(RuntimeProperty.data_prefix + sNode,
					xml.getNodeValue(ConfigRun.DDV_ROOT_XPATH + "/" + sNode, null));
			if (sRelativePathLocation == null)
				return null;

			return ConfigRun.env_basePath + sRelativePathLocation;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Gets the synchronized value of nUniqueIDNum.
	 * 
	 * @return
	 */
	private synchronized static int getUniqueIDNum()
	{
		return nUniqueIDNum;
	}

	/**
	 * Increments nUniqueIDNum using synchronized value.
	 */
	private synchronized static void incrementUniqueIDNum()
	{
		nUniqueIDNum++;
	}

	/**
	 * Returns nUniqueIDNum as a String (and increments nUniqueIDNum)
	 * 
	 * @return
	 */
	public static String createUniqueID()
	{
		return createUniqueID("");
	}

	/**
	 * Returns nUniqueIDNum with a prefix as a String (and increments nUniqueIDNum)
	 * 
	 * @param sPrefix - prefix for the unique ID
	 * @return
	 */
	public static String createUniqueID(String sPrefix)
	{
		// Allow user to specific a prefix
		String sValue = sPrefix;
		if (sPrefix == null)
			sValue = "";

		// Add the unique ID number to the string
		sValue += String.valueOf(getUniqueIDNum());

		// Increment the unique ID number
		incrementUniqueIDNum();

		return sValue;
	}

	/**
	 * Write standard info about test being executed to log
	 * 
	 * @param sValue - Unique ID of test.
	 */
	public static void writeTestIDtoLog(String sValue)
	{
		Logs.log.info(new Date() + " & Executing Test:  " + sValue);
	}

	/**
	 * Write standard info about test being completed successfully to log
	 * 
	 * @param sValue - Unique ID of test.
	 */
	public static void writeTestSuccessToLog(String sValue)
	{
		Logs.log.info(new Date() + " & Completed Test Succesfully:  " + sValue + Framework.getNewLine());
	}

	/**
	 * This method will always throw an exception and it will cause the test case to be skipped because this
	 * is the default behavior of TestNG.<BR>
	 * <BR>
	 * <B>Use:</B><BR>
	 * In the TestNG config XML, put &lt;include name="skip" /&gt; before any test cases to be skipped.<BR>
	 * <BR>
	 * <B>Example TestNG XML file:</B><BR>
	 * &lt;test name="No Child Options Selected" group-by-instances="true" preserve-order="true"&gt;<BR>
	 * &nbsp;&nbsp;&lt;groups&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;run&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;include name="skip" /&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;include name="setup" /&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;include name="results" /&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;include
	 * name="SomeTest.runChildOptionNotSelected" /&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/run&gt;<BR>
	 * &nbsp;&nbsp;&lt;/groups&gt;<BR>
	 * &nbsp;&nbsp;&lt;classes&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;class name="com.automation.ui.tests.SomeTest"
	 * /&gt;<BR>
	 * &nbsp;&nbsp;&lt;/classes&gt;<BR>
	 * &lt;/test&gt;<BR>
	 */
	@BeforeMethod(groups = "skip")
	public static void skip()
	{
		String sError = "Skipped test case due to configuration in the XML for TestNG";
		throw new GenericUnexpectedException(sError);
	}

	/**
	 * Generates Debug information if necessary<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) When using a method with the annotation AfterMethod for TestNG that has a ITestResult parameter,
	 * then TestNG will inject this parameter with the test results.
	 * 
	 * @param result - TestNG object that contains the results of the test
	 * @param btc - Basic Test Context
	 */
	public static void generateDebugInfo(ITestResult result, BasicTestContext btc)
	{
		// Get the test case result
		int nResult = result.getStatus();

		// No work to be done if success or skip
		if (nResult == ITestResult.SUCCESS || nResult == ITestResult.SKIP)
		{
			return;
		}
		else
		{
			try
			{
				String sLog = "Stored Session IDs:  [";
				List<SessionInfo> sessions = btc.getSessions();
				for (int i = 0; i < sessions.size(); i++)
				{
					sLog += "{" + i + " : " + sessions.get(i).sessionID + "}, ";
				}

				Logs.log.info(Misc.removeEndsWith(sLog, ", ") + "]");
			}
			catch (Exception ex)
			{
				Logs.log.warn("Unable to get Stored Session IDs");
			}

			DebugInfo.generate(btc.getDriver());
		}
	}

	/**
	 * Used to close the browser after each method (and generate debug information if necessary)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It may be necessary to override this method to suite your needs as this method only processes
	 * instances of BasicTestContext or 1st level variable that has BasicTestContext variable(s)<BR>
	 * 
	 * @param result - The results of the test (injected by TestNG)
	 * @param obj - Array of parameters for the test (injected by TestNG)
	 */
	@AfterMethod(alwaysRun = true)
	public void quitBrowser(ITestResult result, Object[] obj)
	{
		// Get the test case result
		int nResult = result.getStatus();

		// Handle unusual case where test has no parameters
		if (obj == null)
		{
			if (nResult != ITestResult.SUCCESS && nResult != ITestResult.SKIP)
			{
				Logs.log.warn("Object[] obj was null indicating no parameters as such no screenshots could be generated");
				Logs.log.warn("" + Framework.getNewLine());
			}

			return;
		}

		// Need BasicTestContext variable to check type and better performance
		BasicTestContext tempBTC = new BasicTestContext();

		// Find all BasicTestContext objects and process
		for (int i = 0; i < obj.length; i++)
		{
			// Might be possible for the array to have null values
			if (obj[i] == null)
				continue;

			// Simple Case: The variable is a BasicTestContext object
			if (obj[i] instanceof BasicTestContext)
			{
				int nDebugCode = 100;

				try
				{
					// Write debug information to files if necessary
					if (((BasicTestContext) obj[i]).isDriver())
					{
						nDebugCode = 101;
						generateDebugInfo(result, (BasicTestContext) obj[i]);
					}
					else
					{
						if (nResult != ITestResult.SUCCESS && nResult != ITestResult.SKIP)
						{
							Logs.log.warn("obj["
									+ i
									+ "] did not have an open window as such no screenshots could be generated."
									+ "  Skipping cleanup for this object.");
							Logs.log.warn("" + Framework.getNewLine());
						}
					}

					nDebugCode = 102;

					// Quit Browser
					((BasicTestContext) obj[i]).quitBrowser();
				}
				catch (Exception ex)
				{
					Logs.log.warn("General Exception Debug Code:  " + nDebugCode);
					Logs.log.warn("Method quitBrowser caused exception [" + ex.getClass().getName() + "]:  "
							+ ex.getMessage());
				}
			}
			else
			{
				// Complex Case: The variable contains a BasicTestContext variable
				// Note: Only check the 1st level fields (no recursion to check all child fields)
				for (Field f : obj[i].getClass().getFields())
				{
					if (f.getType().isInstance(tempBTC))
					{
						// Debug Code to track down exception when alert is present sometimes
						// Note: Using WebDriver can only cause the exception but methods being used should
						// not cause the alert exception as they are being used to detect if an alert exists
						// or configure WebDriver
						int nDebugCode = 0;

						try
						{
							// Use reflection to get BasicTestContext object
							Object btc = f.get(obj[i]);
							if (btc == null)
							{
								if (nResult != ITestResult.SUCCESS && nResult != ITestResult.SKIP)
								{
									Logs.log.warn("Field " + f.getName()
											+ " was null as such no screenshots could be generated."
											+ "  Skipping cleanup for this object.");
									Logs.log.warn("" + Framework.getNewLine());
								}

								continue;
							}

							nDebugCode = 1;

							// Use reflection to get the methods to be invoked from BasicTestContext
							Method mIsDriver = btc.getClass().getMethod("isDriver");
							Method mQuitBrowser = btc.getClass().getMethod("quitBrowser");

							nDebugCode = 2;

							// Write debug information to files if necessary
							if ((Boolean) mIsDriver.invoke(btc))
							{
								nDebugCode = 3;
								generateDebugInfo(result, (BasicTestContext) btc);
							}
							else
							{
								if (nResult != ITestResult.SUCCESS && nResult != ITestResult.SKIP)
								{
									Logs.log.warn("Field "
											+ f.getName()
											+ " did not have an open window as such no screenshots could be generated."
											+ "  Skipping cleanup for this object.");
									Logs.log.warn("" + Framework.getNewLine());
								}
							}

							nDebugCode = 4;

							// Quit Browser
							mQuitBrowser.invoke(btc);
						}
						catch (InvocationTargetException ex)
						{
							Logs.log.warn("Invocation Target Exception Debug Code:  " + nDebugCode);
							Logs.log.warn("Method quitBrowser caused exception [InvocationTargetException]:  "
									+ ex.getCause());
						}
						catch (Exception ex)
						{
							Logs.log.warn("General Exception Debug Code:  " + nDebugCode);
							Logs.log.warn("Method quitBrowser caused exception [" + ex.getClass().getName()
									+ "]:  " + ex.getMessage());
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a BasicTestContext with all the values using precedence of system property first else
	 * configuration default
	 * 
	 * @param key - Context Key (which is the prefix of all the system properties for the returned context)
	 * @param configDefaults - Configuration Defaults
	 * @return BasicTestContext
	 */
	private static BasicTestContext getContext(ContextKey key, BasicTestContext configDefaults)
	{
		// Base of each property for the context
		String base = key.toString() + ".";

		//
		// For each of the variables, use the system property if found else use the context default
		//

		String sBrowser = Misc.getProperty(base + RuntimeProperty.browser_name, configDefaults.getBrowser()
				.toString());
		String sDriverPath = Misc.getProperty(base + RuntimeProperty.env_driverPath,
				configDefaults.getDriverPath());
		String sBrowserProfile = Misc.getProperty(base + RuntimeProperty.browser_profile,
				configDefaults.getBrowserProfile());
		String sUrl = Misc.getProperty(base + RuntimeProperty.url, configDefaults.getURL());

		int nPageTimeout = Conversion.parseInt(
				Misc.getProperty(base + RuntimeProperty.delay_page_timeout, ""),
				configDefaults.getPageTimeout());
		int nElementTimeout = Conversion.parseInt(
				Misc.getProperty(base + RuntimeProperty.delay_element_timeout, ""),
				configDefaults.getElementTimeout());
		int nPollInterval = Conversion.parseInt(
				Misc.getProperty(base + RuntimeProperty.delay_poll_interval, ""),
				configDefaults.getPollInterval());
		int nMaxTimeout = Conversion.parseInt(Misc.getProperty(base + RuntimeProperty.delay_max_timeout, ""),
				configDefaults.getMaxTimeout());
		int nMultiplierTimeout = Conversion.parseInt(
				Misc.getProperty(base + RuntimeProperty.delay_multiplier_timeout, ""),
				configDefaults.getTimeoutMultiplier());

		String sHubURL = Misc.getProperty(base + RuntimeProperty.grid_hub, configDefaults.getGridHubURL());
		String sPlatform = Misc.getProperty(base + RuntimeProperty.grid_browser_platform,
				configDefaults.getGridPlatform());
		String sVersion = Misc.getProperty(base + RuntimeProperty.grid_browser_version,
				configDefaults.getGridVersion());
		String sApplicationName = Misc.getProperty(base + RuntimeProperty.grid_browser_applicationName,
				configDefaults.getGridApplicationName());

		String sSessionServer = Misc.getProperty(base + RuntimeProperty.sessions_server,
				configDefaults.getSessionServer());
		int nSessionServerPort = Conversion.parseInt(
				Misc.getProperty(base + RuntimeProperty.sessions_port, ""),
				configDefaults.getSessionServerPort());

		Database db = configDefaults.getDatabase();
		String sDB_Server = Misc.getProperty(base + RuntimeProperty.database_server, db.getServer());
		String sDB = Misc.getProperty(base + RuntimeProperty.database_name, db.getDatabaseName());
		String sDB_User = Misc.getProperty(base + RuntimeProperty.database_user, db.getUser());
		String sDB_Password = Misc.getProperty(base + RuntimeProperty.database_password, null);
		int nDB_Port = Conversion.parseInt(Misc.getProperty(base + RuntimeProperty.database_port, ""),
				db.getPort());

		DB_Type _DB_Type;
		String sDB_Type = Misc.getProperty(base + RuntimeProperty.database_type, null);
		if (sDB_Type == null)
		{
			// No system property as such use default
			_DB_Type = db.getType();
		}
		else
		{
			// Convert system property to DB Type
			_DB_Type = DB_Type.to(sDB_Type);
		}

		boolean bDB_EncodedPassword = Conversion.parseBoolean(Misc.getProperty(base
				+ RuntimeProperty.database_encodedPassword, "false"));
		if (sDB_Password == null)
		{
			// No password node as such use default
			sDB_Password = db.getPassword();
		}
		else if (!sDB_Password.equals("") && bDB_EncodedPassword)
		{
			// Specified password is encrypted, decrypt and store
			sDB_Password = CryptoDESede.decode(sDB_Password);
		}

		boolean bScreenshotsEnabled = Conversion
				.parseBoolean(Misc.getProperty(base + RuntimeProperty.screenshots_enabled,
						String.valueOf(configDefaults.getScreenshotsEnabled())));
		String sScreenshotFolder = Misc.getProperty(base + RuntimeProperty.screenshots_output,
				configDefaults.getScreenshotFolder());
		String sScreenshotPrefixName = Misc.getProperty(base + RuntimeProperty.screenshots_prefix,
				configDefaults.getScreenshotPrefixName());

		int nAJAX_Retries = Conversion.parseInt(
				Misc.getProperty(base + RuntimeProperty.delay_ajax_retries, ""),
				configDefaults.getAJAX_Retries());
		int nAJAX_Stable = Conversion.parseInt(
				Misc.getProperty(base + RuntimeProperty.delay_ajax_stable, ""),
				configDefaults.getAJAX_Stable());

		//
		// Initialize the context
		//

		BasicTestContext useContext = new BasicTestContext();
		useContext.setBrowserRelated(sBrowser, sDriverPath, sBrowserProfile, sUrl);
		useContext.setDelays(nPageTimeout, nElementTimeout, nPollInterval, nMaxTimeout, nMultiplierTimeout);
		useContext.setGrid(sHubURL, sPlatform, sVersion, sApplicationName);
		useContext.setDatabase(sDB_Server, sDB, sDB_User, sDB_Password, nDB_Port, _DB_Type);
		useContext.setScreenshot(bScreenshotsEnabled, sScreenshotFolder, sScreenshotPrefixName);
		useContext.setAJAX(nAJAX_Retries, nAJAX_Stable);
		useContext.setSessionServer(sSessionServer, nSessionServerPort);

		return useContext;
	}

	/**
	 * Returns a list of all the contexts specified as system properties
	 * 
	 * @return List&lt;ContextKey&gt;
	 */
	private static List<ContextKey> getSystemPropertiesContexts()
	{
		List<ContextKey> systemPropertiesContexts = new ArrayList<ContextKey>();

		// Search for the dynamic keys of the context variables
		Properties properties = System.getProperties();
		Set<Object> sysKeys = properties.keySet();
		for (Object key : sysKeys)
		{
			if (((String) key).startsWith(RuntimeProperty.context_prefix))
			{
				for (ContextKey contextKey : ContextKey.values())
				{
					if (((String) key).equals(contextKey.toString()))
					{
						// Add the System Property to the list and value if it exists with a non-empty value
						String sValue = Misc.getProperty((String) key, "");
						if (!sValue.equals(""))
							systemPropertiesContexts.add(contextKey);

						// No need to check the other enumeration values as already found
						break;
					}
				}
			}
		}

		return systemPropertiesContexts;
	}

	/**
	 * Returns a list of all the contexts
	 * 
	 * @return List&lt;ContextKey&gt;
	 */
	private static List<ContextKey> getAllContexts()
	{
		List<ContextKey> allContexts = new ArrayList<ContextKey>();
		for (ContextKey contextKey : ContextKey.values())
		{
			allContexts.add(contextKey);
		}

		return allContexts;
	}

	/**
	 * Ensures that the position returned is a valid start position<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The position is valid for an xpath position<BR>
	 * 2) Max Valid Position should be the total number of nodes available<BR>
	 * 
	 * @param nMaxPosition - Max Valid Position
	 * @param nStartPosition - Start Position to validate
	 * @return 1 if start position is less than 1 or start position is greater than the max position else
	 *         start position
	 */
	public static int getValidStartPosition(int nMaxPosition, int nStartPosition)
	{
		if (nStartPosition < 1 || nStartPosition > nMaxPosition)
			return 1;
		else
			return nStartPosition;
	}

	/**
	 * Ensures that the position returned is a valid stop position<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The position is valid for an xpath position<BR>
	 * 2) Max Valid Position should be the total number of nodes available<BR>
	 * 
	 * @param nMaxPosition - Max Valid Position
	 * @param nStopPosition - Stop Position to validate
	 * @return Max Position if stop position is less than 1 or stop position is greater than the max position
	 *         else stop position
	 */
	public static int getValidStopPosition(int nMaxPosition, int nStopPosition)
	{
		if (nStopPosition < 1 || nStopPosition > nMaxPosition)
			return nMaxPosition;
		else
			return nStopPosition;
	}

	/**
	 * Verify that the data driven file is valid
	 * 
	 * @param configFile - Configuration file used to read the node & data driven file
	 * @param node - Node used to get data driven file
	 * @param file - Data Driven File
	 * @throws GenericUnexpectedException if configuration error
	 */
	protected static void verifyValidDataDrivenFile(String configFile, String node, String file)
	{
		if (configFile == null || configFile.equals(""))
		{
			Logs.logError("The configuration file was null/empty");
		}

		if (node == null || node.equals(""))
		{
			Logs.logError("The configuration for the node has not been setup as it is null/empty in the test class");
		}

		if (file == null || file.equals(""))
		{
			Logs.logError("The node (" + node + ") was missing in the configuration file (" + configFile
					+ ") as the Data Driven File was null/empty");
		}

		File ddf = new File(file);
		if (!ddf.exists())
		{
			Logs.logError("The Data Driven File (" + file + ") does not exist based on the node (" + node
					+ ") in the configuration file (" + configFile + ")");
		}
	}
}
