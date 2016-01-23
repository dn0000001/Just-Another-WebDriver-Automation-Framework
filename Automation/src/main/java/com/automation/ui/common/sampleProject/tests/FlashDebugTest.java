package com.automation.ui.common.sampleProject.tests;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.dataStructures.config.ContextKey;
import com.automation.ui.common.dataStructures.config.RuntimeProperty;
import com.automation.ui.common.sampleProject.dataStructures.LoginDetails;
import com.automation.ui.common.sampleProject.dataStructures.TestContext;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.sampleProject.pages.FlashDebugThread;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Misc;
import com.automation.ui.common.utilities.VTD_XML;

/**
 * This class is to test/debug issues with flash on the grid<BR>
 */
public class FlashDebugTest extends Controller {
	// Data Driven Values files
	private static String sDataDrivenValues_Flash;

	// root node of the xml file
	private static String sRootNode = "/data/";

	// xpath nodes to find data in the xml file for specific tests
	private static String sNode_Flash = "Flash";

	/*
	 * Method names for all the negative tests. Used in data provider(s) to pick correct data file to use.
	 * Note: If the during method names are changed during refactoring, this section must be updated.
	 */
	private static final String POSITIVE = "runFlashFlow";

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
		sDataDrivenValues_Flash = getDataDrivenValuesFileUsingBasePath(sNode_Flash);
		verifyValidDataDrivenFile(sUseConfigFile, sNode_Flash, sDataDrivenValues_Flash);
		Translations.update(ConfigRun.sTranslationsFile);
	}

	/**
	 * Gets data driven values for the test. (This is used for both positive & negative tests.)
	 * 
	 * @param m
	 * @return data for testNG
	 */
	@DataProvider(name = "FlashDebugTest")
	public static Object[][] dataForLogin(Method m)
	{
		/*
		 * Determine which data to get from the xml file
		 */
		String sXpath_Root = sRootNode;
		if (m.getName().equals(POSITIVE))
		{
			sXpath_Root += sNode_Flash;
		}
		else
		{
			Logs.logError("Unsupported method:  " + m.getName());
		}

		// Object array for testNG
		Object[][] testngDataObject = null;

		// Read the XML file
		String sXML_File = sDataDrivenValues_Flash;
		VTD_XML xml;
		try
		{
			// Parse into usable format
			xml = new VTD_XML(sXML_File);

			// Note: Use runtime property to determine number of tests to run unlike real tests
			int nNodes = Conversion.parseInt(Misc.getProperty(RuntimeProperty.processes_max, "1"), 1);

			// Initialize array which will store the test data
			testngDataObject = new Object[1][2];

			// Note: For this test we are only going to use the first node
			String sXpath_Start = sXpath_Root + "[1]/";

			// Get the uniqueID for the test case (or construct)
			String sUniqueID = xml.getNodeValue(sXpath_Start + "UniqueID", createUniqueID());

			// Login data
			LoginDetails login = null;
			// DataReader.getLoginData(xml, sXpath_Start + "TestOptions/Login/", _DecodedPairs);

			// Loop through all the test cases and load the data into the array
			List<TestContext> threadData = new ArrayList<TestContext>();
			for (int i = 0; i < nNodes; i++)
			{
				TestContext data = new TestContext(login);
				data.setBrowser1((BasicTestContext) context.get(ContextKey.primary));
				data.browser1.setUniqueID(sUniqueID);
				threadData.add(data);
			}

			testngDataObject[0][0] = sUniqueID;
			testngDataObject[0][1] = threadData;
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
	 * Gets LoginDetails from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @return LoginDetails
	 */
	public static LoginDetails getLoginData(VTD_XML xml, String sXpath_Base)
	{
		String sUserName = xml.getNodeValue(sXpath_Base + "UserName", "");
		String sPassword = xml.getNodeValue(sXpath_Base + "Password", "");
		Languages lang = Languages.toLanguages(xml.getNodeValue(sXpath_Base + "Language", "EN"));
		boolean bChangePassword = xml.getNodeValue(sXpath_Base + "ChangePassword", false);
		String sNewPassword = xml.getNodeValue(sXpath_Base + "NewPassword", "");
		String sConfirmPassword = xml.getNodeValue(sXpath_Base + "ConfirmPassword", "");
		return new LoginDetails(sUserName, sPassword, lang, bChangePassword, sNewPassword, sConfirmPassword);
	}

	/**
	 * Gets LoginDetails from XML file<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method reads the information from the XML file but the password is not used even if specified.
	 * The purpose of this method is minimize the work required for the frequent password changes<BR>
	 * 2) The username needs to exist in the decoded list of key value pairs if not the password is not
	 * updated<BR>
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param decoded - List of decoded key value pairs
	 * @return LoginData
	 */
	public static LoginDetails getLoginData(VTD_XML xml, String sXpath_Base, List<Parameter> decoded)
	{
		LoginDetails temp = getLoginData(xml, sXpath_Base);

		int nIndex = decoded.indexOf(new Parameter(temp.sUserName));
		if (nIndex >= 0)
			temp.sPassword = decoded.get(nIndex).value;

		return temp;
	}

	/**
	 * Flash Debug Test
	 * 
	 * @param sUniqueID - Unique ID for test
	 * @param data - Data for the test
	 * @throws Exception
	 */
	@Test(dataProvider = "FlashDebugTest", enabled = true)
	public static void runFlashFlow(String sUniqueID, List<TestContext> threadData) throws Exception
	{
		writeTestIDtoLog(sUniqueID);

		Thread[] threads = new Thread[threadData.size()];
		for (int i = 0; i < threads.length; i++)
		{
			Runnable r = new FlashDebugThread(threadData.get(i));
			threads[i] = new Thread(r);
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++)
		{
			threads[i].join();
		}

		writeTestSuccessToLog(sUniqueID);
	}
}