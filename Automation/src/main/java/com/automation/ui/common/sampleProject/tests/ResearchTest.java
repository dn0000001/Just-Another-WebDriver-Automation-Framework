package com.automation.ui.common.sampleProject.tests;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.FileTypes;
import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.dataStructures.GenericLocators;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.Radio;
import com.automation.ui.common.dataStructures.Selection;
import com.automation.ui.common.dataStructures.TimeZoneType;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.dataStructures.config.ContextKey;
import com.automation.ui.common.exceptions.EnumNotFoundException;
import com.automation.ui.common.exceptions.NoErrorOccurredException;
import com.automation.ui.common.sampleProject.dataStructures.LoginDetails;
import com.automation.ui.common.sampleProject.dataStructures.MathActionType;
import com.automation.ui.common.sampleProject.dataStructures.TestContext;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.sampleProject.pages.MathActionReader;
import com.automation.ui.common.sampleProject.pages.SamplePageCache;
import com.automation.ui.common.sampleProject.requests.SampleJSON;
import com.automation.ui.common.utilities.Accents;
import com.automation.ui.common.utilities.Cloner;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.DataReader;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Rand;
import com.automation.ui.common.utilities.TestResults;
import com.automation.ui.common.utilities.TestSections;
import com.automation.ui.common.utilities.VTD_XML;
import com.automation.ui.common.utilities.Verify;

/**
 * This class is for research
 */
public class ResearchTest extends Controller {
	// Test Case count for each method used to make it easier to identify failed test cases
	private static int nTestCase = 0;

	// Flag to know if executing positive test cases
	private static boolean bExecutingPositiveTests = false;

	// Data Driven Values files
	private static String sDataDrivenValues_Research;

	// root node of the xml file
	private static String sRootNode = "/data/";

	// xpath nodes to find data in the xml file for specific tests
	private static String sNode_Research = "Research";

	/*
	 * Method names for all the negative tests. Used in data provider(s) to pick correct data file to use.
	 * Note: If the during method names are changed during refactoring, this section must be updated.
	 */
	private static final String POSITIVE = "runResearchTest";

	public enum LoginVar
	{
		Username, Password
	}

	public enum AddressVar
	{
		FirstName, LastName, Phone, Address
	}

	//
	// Test sections that can be skipped
	//
	private static final Parameter _TS1 = new Parameter("TS1");
	private static final Parameter _TS2 = new Parameter("TS2");
	private static final Parameter _TS3 = new Parameter("TS3");

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
		// Reset flag for positive test case execution used for logging
		bExecutingPositiveTests = false;

		/*
		 * Determine which data to get from the xml file
		 */
		String sXpath_Root = sRootNode;

		sXpath_Root += sNode_Research;
		bExecutingPositiveTests = true;

		// Reset test case count for method used in logging
		nTestCase = 0;

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
			testngDataObject = new Object[nNodes][3];

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

				// Test Sections to allow disabling certain test sections for debugging
				TestSections testSections = new TestSections(xml, sXpath_Start + "Debug/Run/");
				testSections.setDefaultBehavior(xml.getNodeValue(sXpath_Start + "Debug/DebugMode", false));

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

				// Get test data
				MathActionReader _AR = new MathActionReader(MathActionType.Operation, xml, sXpath_Start
						+ "TestOptions/MathActions/Action/");
				List<GenericData> _Actions = _AR.getActions();

				TestContext tc = new TestContext(details);
				tc.setData(_Actions);

				tc.setBrowser1((BasicTestContext) context.get(ContextKey.primary));
				tc.browser1.setUniqueID(sUniqueID);

				tc.setBrowser2((BasicTestContext) context.get(ContextKey.primary));
				tc.browser2.setUniqueID(sUniqueID);

				// Put in the object array for testNG
				testngDataObject[i][0] = sUniqueID;
				testngDataObject[i][1] = testSections;
				testngDataObject[i][2] = tc;
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
	 * Test to research the functionality you are interested in
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param testSections - Test Sections Info
	 * @param data - Test Context
	 */
	@Test(dataProvider = "Research", enabled = false)
	public static void runResearchTest(String sUniqueID, TestSections testSections, TestContext data)
	{
		if (nTestCase == 0 && bExecutingPositiveTests)
			Logs.log.info(Framework.getNewLine() + "Method " + POSITIVE + " executing ..."
					+ Framework.getNewLine());

		writeTestIDtoLog(data.browser1.getUniqueID());

		nTestCase++;

		Logs.log.info("browser1:  " + data.browser1);
		Logs.log.info("browser2:  " + data.browser2);
		Logs.log.info("context1:  " + context);

		WebDriver driver = data.browser1.getDriver();
		BasicTestContext.maximizeWindow(driver);
		Framework.get(driver, data.browser1.getURL());
		// Logs.logError("test");

		writeTestSuccessToLog(data.browser1.getUniqueID());
	}

	@Test(enabled = true)
	public static void runEqualsBuilderUnitTest()
	{
		writeTestIDtoLog("runEqualsBuilderUnitTest");

		Parameter p1 = new Parameter("aaa", "bbb");
		Parameter p2 = new Parameter("aaa", "bbb");
		Parameter p3 = new Parameter("bbb", "bbb");
		Parameter p4 = new Parameter("bbb", "aaa");
		Parameter p5 = new Parameter("ccc", "ddd");
		Parameter p6 = new Parameter("aaa", "ddd");

		Logs.log.info("Match to p1:  " + p1.match(p1));
		Logs.log.info("Match to p2:  " + p1.match(p2));
		Logs.log.info("Match to p3:  " + p1.match(p3));
		Logs.log.info("Match to p4:  " + p1.match(p4));
		Logs.log.info("Match to p5:  " + p1.match(p5));
		Logs.log.info("Match to p6:  " + p1.match(p6));

		writeTestSuccessToLog("runEqualsBuilderUnitTest");
	}

	@Test(enabled = true)
	public static void runGenericLocatorsUnitTest()
	{
		writeTestIDtoLog("runGenericLocatorsUnitTest");
		String sValue = "Y";

		GenericLocators locators;
		// locators = new GenericLocators();
		// locators = new GenericLocators(Radio.No);
		locators = new GenericLocators(Radio.Yes, sValue);

		List<Enum<?>> keys = locators.getKeys();
		if (!keys.contains(Radio.Yes))
			Logs.logError("Key (" + Radio.Yes + ") was not in the HashMap");

		if (keys.contains(Radio.No))
			Logs.logError("Key (" + Radio.No
					+ ") was in the HashMap but should not have been as it was not added");

		if (!locators.get(Radio.Yes).equals(sValue))
			Logs.logError("Key (" + Radio.Yes + ") did not have the correct value:  " + sValue);

		if (!locators.get(Radio.No).equals(""))
			Logs.logError("Key (" + Radio.No + ") should not have been in the HashMap as it was not added");

		sValue = "Updated Y";
		if (!locators.add(Radio.Yes, sValue))
			Logs.logError("Key not added:  " + Radio.Yes);

		if (!locators.get(Radio.Yes).equals(sValue))
			Logs.logError("Key (" + Radio.Yes + ") did not have the correct updated value:  " + sValue);

		if (locators.isComplete())
			Logs.logError("Only 1 of 3 enumeration values added");

		if (locators.add(FileTypes.AVI, "avi"))
			Logs.logError("Should not have been able to add key " + FileTypes.AVI
					+ " as it is a different type");

		if (!locators.add(Radio.No, "N"))
			Logs.logError("Key not added:  " + Radio.No);

		if (locators.isComplete())
			Logs.logError("Only 3 of 3 enumeration values added");

		if (!locators.add(Radio.DEFAULT, "D"))
			Logs.logError("Key not added:  " + Radio.DEFAULT);

		if (!locators.isComplete())
			Logs.logError("All enumeration values added, complete should be true");

		locators.remove(Radio.DEFAULT);
		if (locators.isComplete())
			Logs.logError("Only 2 of 3 enumeration values added after removing 1");

		try
		{
			locators.verify();
		}
		catch (EnumNotFoundException ex)
		{
			Logs.log.info("The expected exception occurred");
		}

		if (!locators.add(Radio.DEFAULT, "D"))
			Logs.logError("Key not added:  " + Radio.DEFAULT);

		if (!locators.isComplete())
			Logs.logError("All enumeration values added, complete should be true");

		locators.remove(FileTypes.GIF);
		if (!locators.isComplete())
			Logs.logError("All enumeration values added, removed key not in map, complete should be true");

		locators.verify();

		locators = new GenericLocators();
		keys = locators.getKeys();
		if (keys.size() > 0)
			Logs.logError("There should have been no keys in the HashMap");

		locators = new GenericLocators(FileTypes.AVI);
		keys = locators.getKeys();
		if (keys.size() > 0)
			Logs.logError("There should have been no keys in the HashMap using Enum Constructor");

		try
		{
			locators.verify();
		}
		catch (EnumNotFoundException ex)
		{
			Logs.log.info("The expected exception occurred after re-initialization");
		}

		writeTestSuccessToLog("runGenericLocatorsUnitTest");
	}

	@Test(enabled = true)
	public static void runDataReaderUnitTest() throws Exception
	{
		writeTestIDtoLog("runDataReaderUnitTest");

		InputField in = new InputField(false, null, "");

		VTD_XML xml = new VTD_XML("resources/data/data_Login.xml");
		InputField field1 = DataReader.getInputField(xml, "/data/Login/TestOptions/Field1", in);
		Logs.log.info(field1);
		Logs.log.info(field1.getVerificationValue());

		InputField field2 = DataReader.getInputField(xml, "/data/Login/TestOptions/Field2", in);
		Logs.log.info(field2);
		Logs.log.info(field2.getVerificationValue());

		InputField field3 = DataReader.getInputField(xml, "/data/Login/TestOptions/Field3", in);
		Logs.log.info(field3);
		Logs.log.info(field3.getVerificationValue());

		InputField field4 = DataReader.getInputField(xml, "/data/Login/TestOptions/Field4", in);
		Logs.log.info(field4);
		Logs.log.info(field4.getVerificationValue());

		InputField field5 = DataReader.getInputField(xml, "/data/Login/TestOptions/Field5", in);
		Logs.log.info(field5);
		Logs.log.info(field5.getVerificationValue());

		InputField field6 = DataReader.getInputField(xml, "/data/Login/TestOptions/Field6", in);
		Logs.log.info(field6);
		Logs.log.info(field6.getVerificationValue());

		writeTestSuccessToLog("runDataReaderUnitTest");
	}

	@Test(enabled = true)
	public static void runGenericDataUnitTest()
	{
		writeTestIDtoLog("runGenericDataUnitTest");

		String _SimpleString = Rand.alphanumeric(10);
		boolean bSkip = Rand.randomBoolean();
		String _Value = Rand.alphanumeric(10);
		String _RandValue = Rand.alphanumeric(10);
		InputField _inExpected = new InputField(bSkip, _Value, _RandValue);

		int _SelectionIndex = Rand.randomRange(0, 4);
		String sOption = Rand.alphanumeric(5);
		int nMinIndex = Rand.randomRange(0, 10);
		Selection _SelectionRand = Selection.to(String.valueOf(_SelectionIndex), Selection.Index);
		DropDown _ddExpected = new DropDown(_SelectionRand, sOption, nMinIndex);

		boolean bChecked = Rand.randomBoolean();
		CheckBox _cbExpected = new CheckBox(bChecked);

		int _phoneExpected = Rand.randomRange(1000000, 9999999);

		boolean bSkip2 = Rand.randomBoolean();
		String _Value2 = Rand.alphanumeric(10);
		String _RandValue2 = Rand.alphanumeric(10);
		InputField _inExpected2 = new InputField(bSkip2, _Value2, _RandValue2);

		GenericData data = new GenericData();

		// Verify storing of simple variable (String)
		data.add(LoginVar.Username, _SimpleString);
		String sSimpleString = (String) data.get(LoginVar.Username);
		if (!sSimpleString.equals(_SimpleString))
			Logs.logError("String not stored in HashMap properly");

		// Verify storing of object
		data.add(LoginVar.Password, new InputField(bSkip, _Value, _RandValue));
		InputField _inActual = (InputField) data.get(LoginVar.Password);
		if (_inExpected.skip != _inActual.skip || !_inExpected.value.equals(_inActual.value)
				|| !_inExpected.randomValue.equals(_inActual.randomValue))
		{
			Logs.log.warn("Expected:  " + _inExpected);
			Logs.log.warn("Actual:    " + _inActual);
			Logs.logError("InputField not stored in HashMap properly");
		}

		data.verify();
		data.verify(LoginVar.Username);

		HashMap<Enum<?>, Class<?>> mustExist = new HashMap<Enum<?>, Class<?>>();
		mustExist.put(LoginVar.Password, InputField.class);
		data.verify(mustExist);

		mustExist.put(LoginVar.Username, String.class);
		data.verify(mustExist);

		// Change type
		mustExist.put(LoginVar.Username, Radio.class);
		data.add(LoginVar.Username, Radio.Yes);
		data.verify(mustExist);

		// Change type
		mustExist.put(LoginVar.Password, CheckBox.class);
		data.add(LoginVar.Password, new CheckBox(true));
		data.verify(mustExist);

		// Add another enumeration to the data
		data.add(AddressVar.FirstName, new DropDown(_SelectionRand, sOption, nMinIndex));
		data.add(AddressVar.LastName, new CheckBox(bChecked));
		data.add(AddressVar.Phone, _phoneExpected);

		// Verify 1st enumeration is not affected
		data.verify(LoginVar.Username);

		// Verify 2nd enumeration is verified properly
		HashMap<Enum<?>, Class<?>> mustExist2 = new HashMap<Enum<?>, Class<?>>();
		mustExist2.put(LoginVar.Username, Radio.class);
		mustExist2.put(AddressVar.FirstName, DropDown.class);
		mustExist2.put(AddressVar.LastName, CheckBox.class);
		mustExist2.put(AddressVar.Phone, Integer.class);
		data.verify(mustExist2);

		// Complete the 2nd enumeration
		data.add(AddressVar.Address, new InputField(bSkip2, _Value2, _RandValue2));

		// Verification of the data
		data.verify();
		data.verify(LoginVar.Username);
		data.verify(mustExist);

		// Change type
		data.add(AddressVar.Phone, false);
		mustExist2.put(AddressVar.Phone, Boolean.class);

		// Verification of the data
		data.verify();
		data.verify(LoginVar.Username);
		data.verify(mustExist);

		// Verify getting the entire enumeration
		data.add(AddressVar.Phone, _phoneExpected);
		mustExist2.put(AddressVar.Phone, Integer.class);
		HashMap<Enum<?>, Object> address = data.getAll(AddressVar.FirstName);
		DropDown ddActual = (DropDown) address.get(AddressVar.FirstName);
		CheckBox cbActual = (CheckBox) address.get(AddressVar.LastName);
		int nActualPhone = (Integer) address.get(AddressVar.Phone);
		InputField inActual = (InputField) address.get(AddressVar.Address);
		if (ddActual.using != _SelectionRand || !ddActual.option.equals(sOption)
				|| ddActual.minIndex != nMinIndex)
		{
			Logs.log.warn("Expected:  " + _ddExpected);
			Logs.log.warn("Actual:    " + ddActual);
			Logs.logError("DropDown not stored in HashMap properly");
		}

		if (cbActual.check != bChecked)
		{
			Logs.log.warn("Expected:  " + _cbExpected);
			Logs.log.warn("Actual:    " + cbActual);
			Logs.logError("CheckBox not stored in HashMap properly");
		}

		if (nActualPhone != _phoneExpected)
			Logs.logError("Integer not stored in HashMap properly");

		if (_inExpected2.skip != inActual.skip || !_inExpected2.value.equals(inActual.value)
				|| !_inExpected2.randomValue.equals(inActual.randomValue))
		{
			Logs.log.warn("Expected:  " + _inExpected2);
			Logs.log.warn("Actual:    " + inActual);
			Logs.logError("InputField not stored in HashMap properly");
		}

		// Verification failures
		GenericData data3 = new GenericData();
		data3.add(LoginVar.Username, _SimpleString);
		data3.add(AddressVar.FirstName, new DropDown(_SelectionRand, sOption, nMinIndex));
		data3.add(AddressVar.LastName, new InputField(bSkip2, _Value2, _RandValue2));
		data3.add(AddressVar.Address, null);

		if (!data3.containsKey(AddressVar.Address))
			Logs.logError("Could not find the key that had a null value in the HashMap");

		try
		{
			data3.verify();
			String sError = "The verification (1) should have failed but it did not";
			Logs.logError(new NoErrorOccurredException(sError));
		}
		catch (EnumNotFoundException ex)
		{
			Logs.log.info("EnumNotFoundException exception occurred as expected");
		}

		HashMap<Enum<?>, Class<?>> mustExist3 = new HashMap<Enum<?>, Class<?>>();
		mustExist3.put(LoginVar.Password, InputField.class);
		mustExist3.put(LoginVar.Username, String.class);
		mustExist3.put(AddressVar.FirstName, InputField.class);
		mustExist3.put(AddressVar.LastName, InputField.class);
		mustExist3.put(AddressVar.Phone, DropDown.class);
		mustExist3.put(AddressVar.Address, DropDown.class);

		try
		{
			data3.verify(mustExist3);
			String sError = "The verification (2) should have failed but it did not";
			Logs.logError(new NoErrorOccurredException(sError));
		}
		catch (EnumNotFoundException ex)
		{
			Logs.log.info("EnumNotFoundException exception occurred as expected");
		}

		try
		{
			// Match on null value but still are failures
			mustExist3.put(AddressVar.Address, null);
			data3.verify(mustExist3);
			String sError = "The verification (3) should have failed but it did not";
			Logs.logError(new NoErrorOccurredException(sError));
		}
		catch (EnumNotFoundException ex)
		{
			Logs.log.info("EnumNotFoundException exception occurred as expected");
		}

		try
		{
			// Data is non-null but expecting null
			mustExist3.put(AddressVar.FirstName, null);
			data3.verify(mustExist3);
			String sError = "The verification (4) should have failed but it did not";
			Logs.logError(new NoErrorOccurredException(sError));
		}
		catch (EnumNotFoundException ex)
		{
			Logs.log.info("EnumNotFoundException exception occurred as expected");
		}

		writeTestSuccessToLog("runGenericDataUnitTest");
	}

	@Test(enabled = true)
	public static void runDeepCopyTest()
	{
		writeTestIDtoLog("runDeepCopyTest");
		GenericData data1 = new GenericData();
		data1.add(LoginVar.Username, "test");
		data1.add(LoginVar.Password, 125);
		data1.add(AddressVar.Address, true);
		data1.add(AddressVar.FirstName, InputField.getRandom("test"));
		data1.add(AddressVar.LastName, DropDown.getRandom(2));
		data1.add(AddressVar.Phone, CheckBox.getRandom());

		// Deep copy
		GenericData data2 = data1.copy();

		// Make changes to the original object
		data1.add(LoginVar.Username, "changed");
		data1.add(LoginVar.Password, 5462);
		data1.add(AddressVar.Address, false);
		data1.add(AddressVar.FirstName, InputField.getRandom("abc"));
		data1.add(AddressVar.LastName, new DropDown("def"));
		data1.add(AddressVar.Phone, true);

		// Verify changes to original object do not affect the copy.
		if (data1.get(LoginVar.Username).equals(data2.get(LoginVar.Username)))
		{
			Logs.logError("Username appears to be mutable by changes in the orginal copy");
		}

		if (data1.get(LoginVar.Password).equals(data2.get(LoginVar.Password)))
		{
			Logs.logError("Password appears to be mutable by changes in the orginal copy");
		}

		if (data1.get(AddressVar.Address).equals(data2.get(AddressVar.Address)))
		{
			Logs.logError("Address appears to be mutable by changes in the orginal copy");
		}

		if (data1.get(AddressVar.FirstName).equals(data2.get(AddressVar.FirstName)))
		{
			Logs.logError("FirstName appears to be mutable by changes in the orginal copy");
		}

		if (data1.get(AddressVar.LastName).equals(data2.get(AddressVar.LastName)))
		{
			Logs.logError("LastName appears to be mutable by changes in the orginal copy");
		}

		if (data1.get(AddressVar.Phone).equals(data2.get(AddressVar.Phone)))
		{
			Logs.logError("Phone appears to be mutable by changes in the orginal copy");
		}

		InputField change001 = (InputField) data1.get(AddressVar.FirstName);
		change001.randomValue = "def";

		if (data1.get(AddressVar.FirstName).equals(data2.get(AddressVar.FirstName)))
		{
			Logs.logError("FirstName appears to be mutable by changes in the orginal copy");
		}

		Logs.log.info("data1:  " + data1);
		Logs.log.info("data2:  " + data2);

		HashMap<Integer, InputField> data3 = new HashMap<Integer, InputField>();
		data3.put(0, Rand.randomInputField("a"));
		data3.put(1, Rand.randomInputField("b"));

		HashMap<Integer, InputField> data4 = Cloner.deepClone(data3);
		HashMap<Integer, InputField> data5 = new HashMap<Integer, InputField>(data3);

		InputField change002 = data3.get(0);
		change002.randomValue = "c";

		InputField change003 = data3.get(1);
		change003.randomValue = "d";

		if (data3.get(0).equals(data4.get(0)))
		{
			Logs.logError("The value of key 0 appears to be mutable by changes in the orginal copy");
		}

		if (!data3.get(0).equals(data5.get(0)))
		{
			Logs.logError("The value of key 0 was not mutable as data3 value was " + data3.get(0)
					+ " and data5 value was " + data5.get(0));
		}

		if (data3.get(1).equals(data4.get(1)))
		{
			Logs.logError("The value of key 1 appears to be mutable by changes in the orginal copy");
		}

		if (!data3.get(1).equals(data5.get(1)))
		{
			Logs.logError("The value of key 1 was not mutable as data3 value was " + data3.get(1)
					+ " and data5 value was " + data5.get(1));
		}

		Logs.log.info("data3:  " + data3);
		Logs.log.info("data5:  " + data5);
		Logs.log.info("data4:  " + data4);

		InputField change004 = Rand.randomInputField("z");
		InputField change005 = Rand.randomInputField("y");

		List<InputField> data6 = new ArrayList<InputField>();
		data6.add(change004);
		data6.add(change005);

		List<InputField> data7 = Cloner.deepClone(data6);
		List<InputField> data8 = new ArrayList<InputField>(data6);

		change004.randomValue = "aa";
		change004.skip = true;
		change004.value = "def";
		change004.caseSensitive = true;
		change004.logAll = true;
		change004.mask = "abc";
		change004.maxLength = 10;

		change005.randomValue = "bb";

		if (data6.get(0).equals(data7.get(0)))
		{
			Logs.logError("The value of index 0 appears to be mutable by changes in the orginal copy");
		}

		if (!data6.get(0).equals(data8.get(0)))
		{
			Logs.logError("The value of index 0 was not mutable as data3 value was " + data3.get(0)
					+ " and data5 value was " + data8.get(0));
		}

		if (data6.get(1).equals(data7.get(1)))
		{
			Logs.logError("The value of index 1 appears to be mutable by changes in the orginal copy");
		}

		if (!data6.get(1).equals(data8.get(1)))
		{
			Logs.logError("The value of index 1 was not mutable as data3 value was " + data3.get(1)
					+ " and data5 value was " + data8.get(1));
		}

		Logs.log.info("data6:  " + Conversion.toString(data6, ", "));
		Logs.log.info("data8:  " + Conversion.toString(data8, ", "));
		Logs.log.info("data7:  " + Conversion.toString(data7, ", "));

		writeTestSuccessToLog("runDeepCopyTest");
	}

	private static void verifyCounts(TestResults se, int nFailureCount, int nPassCount, int nTotalCount,
			int nFailurePercent, int nPassPercent)
	{
		if (se.getFailureCount() != nFailureCount)
		{
			Logs.logError("Expected failure count to be " + nFailureCount + " but it was "
					+ se.getFailureCount());
		}

		if (se.getPassCount() != nPassCount)
			Logs.logError("Expected pass count to be " + nPassCount + " but it was " + se.getPassCount());

		if (se.getTotalCount() != nTotalCount)
			Logs.logError("Expected total count to be " + nTotalCount + " but it was " + se.getTotalCount());

		if (se.getFailurePercent() != nFailurePercent)
		{
			Logs.logError("Expected failure percent to be " + nFailurePercent + " but it was "
					+ se.getFailurePercent());
		}

		if (se.getPassPercent() != nPassPercent)
		{
			Logs.logError("Expected pass percent to be " + nPassPercent + " but it was "
					+ se.getPassPercent());
		}
	}

	@Test(enabled = true)
	public static void runStickyErrorTest()
	{
		writeTestIDtoLog("runStickyErrorTest");

		TestResults results = new TestResults();
		int nFailureCount = 0;
		int nPassCount = 0;
		int nTotalCount = 0;
		int nFailurePercent = 0;
		int nPassPercent = 0;

		Logs.log.info("Default (0 failures allowed)");
		results.expectTrue(true);
		nPassCount++;
		nTotalCount++;
		results.expectFalse(false);
		nPassCount++;
		nTotalCount++;
		results.expectTrue(true, "Test 3 failed");
		nPassCount++;
		nTotalCount++;
		results.expectFalse(false, "Test 4 failed");
		nPassCount++;
		nTotalCount++;
		results.expectTrue(true, "Test 5 passed", "Test 5 failed");
		nPassCount++;
		nTotalCount++;
		results.expectFalse(false, "Test 6 passed", "Test 6 failed");
		nPassCount++;
		nTotalCount++;

		nFailurePercent = 0;
		nPassPercent = 100;
		verifyCounts(results, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);
		results.verify("Default of 0 failures was not successful");

		Logs.log.info("Reset counts test");
		results.reset();
		nFailureCount = 0;
		nPassCount = 0;
		nTotalCount = 0;
		nFailurePercent = 0;
		nPassPercent = 100;
		verifyCounts(results, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);

		Logs.log.info("Cause failure test");
		results.expectTrue(false);
		nTotalCount++;
		nFailureCount++;
		nFailurePercent = 100;
		nPassPercent = 0;
		verifyCounts(results, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);

		boolean bError = results.isError();
		if (!bError)
			Logs.logError("Expected error (1a) but test excution still considered pass");

		try
		{
			results.verify("Expecting error (1)");
			bError = false;
		}
		catch (Exception ex)
		{
			if (!bError)
				Logs.logError("Expected error (1b) but test excution still considered pass");
			else
				Logs.log.info("Exception occurred (1) as expected");
		}

		results.reset();
		nFailureCount = 0;
		nPassCount = 0;
		nTotalCount = 0;
		nFailurePercent = 0;
		nPassPercent = 100;
		verifyCounts(results, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);
		results.expectFalse(true);

		nTotalCount++;
		nFailureCount++;
		nFailurePercent = 100;
		nPassPercent = 0;
		verifyCounts(results, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);

		bError = results.isError();
		if (!bError)
			Logs.logError("Expected error (2a) but test excution still considered pass");

		try
		{
			results.verify("Expecting error (2)");
			bError = false;
		}
		catch (Exception ex)
		{
			if (!bError)
				Logs.logError("Expected error (2b) but test excution still considered pass");
			else
				Logs.log.info("Exception occurred (2) as expected");
		}

		Logs.log.info("Multiple failure test");
		results.expectTrue(false, "Test 10 failed");
		nTotalCount++;
		nFailureCount++;
		results.expectTrue(false, "Test 11 passed", "Test 11 failed");
		nTotalCount++;
		nFailureCount++;
		results.expectFalse(true, "Test 12 failed");
		nTotalCount++;
		nFailureCount++;
		results.expectFalse(true, "Test 13 passed", "Test 13 failed");
		nTotalCount++;
		nFailureCount++;

		nFailurePercent = 100;
		nPassPercent = 0;
		verifyCounts(results, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);

		bError = results.isError();
		if (!bError)
			Logs.logError("Expected error (3a) but test excution still considered pass");

		try
		{
			results.verify("Expecting error (3)");
			bError = false;
		}
		catch (Exception ex)
		{
			if (!bError)
				Logs.logError("Expected error (3b) but test excution still considered pass");
			else
				Logs.log.info("Exception occurred (3) as expected");
		}

		results.expectTrue(true);
		nTotalCount++;
		nPassCount++;
		results.expectFalse(false);
		nTotalCount++;
		nPassCount++;

		nFailurePercent = 71;
		nPassPercent = 28;
		verifyCounts(results, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);

		bError = results.isError();
		if (!bError)
			Logs.logError("Expected error (4a) but test excution still considered pass");

		try
		{
			results.verify("Expecting error (4)");
			bError = false;
		}
		catch (Exception ex)
		{
			if (!bError)
				Logs.logError("Expected error (4b) but test excution still considered pass");
			else
				Logs.log.info("Exception occurred (4) as expected");
		}

		Logs.log.info("Change Limit test");
		results.setLimit(5);
		results.verify("Test 14 passed", "Test 14 failed");
		results.setLimit(4);
		if (!results.isError())
			Logs.logError("Expected error (5) but test excution still considered pass");

		Logs.log.info("Switch method test");
		results.setLimit(71);
		results.setMethod_Percent();
		results.verify("Test 15 passed", "Test 15 failed");
		results.setLimit(70);

		if (!results.isError())
			Logs.logError("Expected error (6a) but test excution still considered pass");

		try
		{
			results.verify("Expecting error (6)");
			bError = false;
		}
		catch (Exception ex)
		{
			if (!bError)
				Logs.logError("Expected error (6b) but test excution still considered pass");
			else
				Logs.log.info("Exception occurred (6) as expected");
		}

		Logs.log.info("Add Counts test");
		TestResults results2 = Cloner.deepClone(results);
		verifyCounts(results2, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);
		results2.add(results);
		nFailureCount *= 2;
		nPassCount *= 2;
		nTotalCount *= 2;
		verifyCounts(results2, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);

		TestResults results3 = new TestResults();
		results3.expectTrue(true);
		results3.expectTrue(true);
		results3.expectTrue(false);
		nFailureCount += 1;
		nPassCount += 2;
		nTotalCount += 3;
		nFailurePercent = 64;
		nPassPercent = 35;
		results2.add(results3);
		verifyCounts(results2, nFailureCount, nPassCount, nTotalCount, nFailurePercent, nPassPercent);

		Logs.log.info("Stored Logs test");
		TestResults results4 = new TestResults();
		results4.setWriteLogsInFuture();
		results4.expectTrue(true); // No logging here
		results4.expectTrue(true, "Future Warn 1 (skip)"); // No logging here
		results4.expectTrue(false, "Future Warn 2");
		results4.expectTrue(true, "Future Info 3", "Future Warn 3 (skip)");
		results4.expectTrue(false, "Future Info 4 (skip)", "Future Warn 4");
		results4.expectFalse(false); // No logging here
		results4.expectFalse(true, "Future Warn 5");
		results4.expectFalse(false, "Future Warn 6 (skip)");// No logging here
		results4.expectFalse(false, "Future Info 7", "Future Warn 7 (skip)");
		results4.expectFalse(true, "Future Info 8 (skip)", "Future Warn 8");

		List<Parameter> storedLogs = results4.getStoredLogs();
		if (storedLogs.size() != 6)
		{
			Logs.logError("Expected 6 stored logs but there were actually " + storedLogs.size()
					+ " stored logs");
		}

		bError = results4.isError();
		if (!bError)
			Logs.logError("Expected error (7a) but test excution still considered pass");

		try
		{
			results4.verify("Expecting error (7)");
			bError = false;
		}
		catch (Exception ex)
		{
			if (!bError)
				Logs.logError("Expected error (7b) but test excution still considered pass");
			else
				Logs.log.info("Exception occurred (7) as expected");
		}

		Logs.log.info("Stored Logs reset test");
		results4.expectTrue(true); // No logging here
		results4.expectTrue(true, "Future Warn 9 (skip)"); // No logging here
		results4.expectTrue(false, "Future Warn 10");
		results4.expectTrue(true, "Future Info 11", "Future Warn 11 (skip)");
		results4.expectTrue(false, "Future Info 12 (skip)", "Future Warn 12");
		results4.expectFalse(false); // No logging here
		results4.expectFalse(true, "Future Warn 13");
		results4.expectFalse(false, "Future Warn 14 (skip)");// No logging here
		results4.expectFalse(false, "Future Info 15", "Future Warn 15 (skip)");
		results4.expectFalse(true, "Future Info 16 (skip)", "Future Warn 16");
		storedLogs = results4.getStoredLogs();
		if (storedLogs.size() != 6)
		{
			Logs.logError("Expected 6 stored logs but there were actually " + storedLogs.size()
					+ " stored logs");
		}

		// Store the results for another test later
		TestResults results5 = Cloner.deepClone(results4);
		TestResults results6 = Cloner.deepClone(results4);

		results4.writeStoredLogs();
		storedLogs = results4.getStoredLogs();
		if (storedLogs.size() != 0)
		{
			Logs.logError("Expected no stored logs but there were " + storedLogs.size() + " stored logs");
		}

		bError = results4.isError();
		if (!bError)
			Logs.logError("Expected error (8a) but test excution still considered pass");

		Logs.log.info("Reset test");
		results5.reset();
		verifyCounts(results5, 0, 0, 0, 0, 100);
		storedLogs = results5.getStoredLogs();
		if (storedLogs.size() != 0)
		{
			Logs.logError("Expected no stored logs but there were " + storedLogs.size() + " stored logs");
		}

		Logs.log.info("Add test");
		TestResults results7 = new TestResults();
		results7.setWriteLogsInFuture();
		results7.expectTrue(true);
		results7.expectTrue(true, "no logged");
		results7.expectTrue(true, "Add test info 1", "Add test warn 1");
		results7.expectTrue(false, "Add test warn 2");
		results7.add(results6);

		nFailureCount = 1 + results6.getFailureCount();
		nPassCount = 3 + results6.getPassCount();
		nTotalCount = 4 + results6.getTotalCount();
		Float failurePercent = Float.valueOf(nFailureCount) / Float.valueOf(nTotalCount)
				* Float.valueOf("100");
		Float passPercent = Float.valueOf(nPassCount) / Float.valueOf(nTotalCount) * Float.valueOf("100");
		verifyCounts(results7, nFailureCount, nPassCount, nTotalCount, failurePercent.intValue(),
				passPercent.intValue());
		storedLogs = results7.getStoredLogs();
		if (storedLogs.size() != 8)
		{
			Logs.logError("Expected 8 stored logs but there were " + storedLogs.size() + " stored logs");
		}

		writeTestSuccessToLog("runStickyErrorTest");
	}

	/**
	 * Testing WebElement.equals
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param testSections - Test Sections Info
	 * @param data - Test Context
	 */
	@Test(dataProvider = "Research", enabled = false)
	public static void runWebElementsEqualsTest(String sUniqueID, TestSections testSections, TestContext data)
	{
		writeTestIDtoLog("runWebElementsEqualsTest");

		WebDriver driver = data.browser1.getDriver();
		BasicTestContext.maximizeWindow(driver);
		Framework.get(driver, data.browser1.getURL());

		//
		// Manually load file in browser before continuing:
		// /resources/TechnicalTests/AdvancedWebDriverTest.htm"
		//
		Framework.sleep(3000);

		String locator = "//input";
		List<String> exceptions = new ArrayList<String>();
		exceptions.add("//input[@data-bind='FirstName']");
		exceptions.add("//input[@data-bind='PostalCode']");
		exceptions.add("results");
		Framework.waitForElementRemoval(driver, locator, exceptions, Framework.getTimeoutInMilliseconds(),
				Framework.getPollInterval());

		writeTestSuccessToLog("runWebElementsEqualsTest");
	}

	/**
	 * Testing Conversion.toDate & Conversion.convertDate
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param data - Test Context
	 */
	@Test(enabled = true)
	public static void runDateConversionTest()
	{
		writeTestIDtoLog("runDateConversionTest");

		String sStoredSQL_Date = "2015-05-30 00:15:01.123";
		TimeZone tz = TimeZoneType.UTC.getTimeZone();
		String sSQL_DateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		Date d1 = Conversion.toDate(sStoredSQL_Date, sSQL_DateFormat, tz);

		String sFormatDisplayDate = "MMM dd, yyyy";
		String sDisplayDate = Conversion.convertDate(d1, sFormatDisplayDate, tz);
		TimeZone est = TimeZoneType.EST.getTimeZone();
		String sDisplayDateEST = Conversion.convertDate(d1, sFormatDisplayDate, est);
		String sSQL_EST = Conversion.convertDate(d1, sSQL_DateFormat, est);
		Logs.log.info("Stored SQL Date:   " + sStoredSQL_Date);
		Logs.log.info("Display Date:      " + sDisplayDate);
		Logs.log.info("Display Date EST:  " + sDisplayDateEST);
		Logs.log.info("SQL Date EST:      " + sSQL_EST);

		writeTestSuccessToLog("runDateConversionTest");
	}

	/**
	 * Testing TestSections class
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param testSections - Test Sections Info
	 * @param context - Test Context
	 */
	@Test(dataProvider = "Research", enabled = true)
	public static void runTestSections(String sUniqueID, TestSections testSections, TestContext context)
	{
		writeTestIDtoLog("runTestSections");

		if (testSections.isTestEnabled(_TS1))
			Logs.log.info("TS1 was enabled");
		else
			Logs.log.info("TS1 was disabled");

		if (testSections.isTestEnabled(_TS2))
			Logs.log.info("TS2 was enabled");
		else
			Logs.log.info("TS2 was disabled");

		if (testSections.isTestEnabled(_TS3))
			Logs.log.info("TS3 was enabled");
		else
			Logs.log.info("TS3 was disabled");

		writeTestSuccessToLog("runTestSections");
	}

	/**
	 * Testing BaseActionReader class
	 * 
	 * @param sUniqueID - Unique ID of test case
	 * @param testSections - Test Sections Info
	 * @param context - Test Context
	 */
	@Test(dataProvider = "Research", enabled = true)
	public static void runBaseActionReader(String sUniqueID, TestSections testSections, TestContext context)
	{
		writeTestIDtoLog("runBaseActionReader");

		int index = 0;
		for (GenericData item : context.data)
		{
			for (MathActionType key : MathActionType.values())
			{
				if (item.containsKey(key))
				{
					Logs.log.info("Item (" + index + ") key (" + key + "):  " + item.getCheck(key));
				}
				else
				{
					Logs.log.info("Item (" + index + ") did not have key:  " + key);
				}
			}

			Logs.log.info("");
			index++;
		}

		writeTestSuccessToLog("runBaseActionReader");
	}

	/**
	 * Testing BaseRequestJSON class
	 */
	@Test(enabled = true)
	public static void runBaseRequestJSON()
	{
		writeTestIDtoLog("runBaseRequestJSON");

		SampleJSON json = new SampleJSON("www.test.com/json/request");

		// {"Condition":1,"Search":{"Condition":1,"phone":"111-222-3333","address":"bb","name":"aa","active":true}}
		json.setRequestData("aa", "bb", "111-222-3333", true, 1);

		String response = "{\"Results\": [{\"ID\": 3},{\"ID\": 100}],\"Success\":true}";
		json.simulateRequest(response);

		Logs.log.info("Response:  " + Conversion.toString(json.getReponseData(), ", "));

		writeTestSuccessToLog("runBaseRequestJSON");
	}

	/**
	 * Testing Accents class
	 */
	@Test(enabled = true)
	public static void runAccents()
	{
		writeTestIDtoLog("runAccents");

		String _French = "ÀàÂâÆæÇçÈèÉéÊêËëÎîÏïÔôŒœÙùÛûÜü";
		String _Czech = "ÁáĄąÄäÉéĘęĚěÍíÓóÔôÚúŮůÝýČčďťĹĺŇňŔŕŘřŠšŽž";
		String _German = "ÄäÉéÖöÜüß";
		String _Hawaiian = "ÄäĒēĪīŌōŪūʻ";
		String _Italian = "ÀàÁáÈèÉéÌìÍíÒòÓóÙùÚú";
		String _Polish = "ĄąĘęÓóĆćŁłŃńŚśŹźŻż";
		String _Romanian = "ĂăÂâÎîȘșŞşȚțŢţ";
		String _Russian = "АаБбВвГгДдЕеЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя";
		String _Spanish = "ÁáÉéÍíÑñÓóÚúÜü";
		String _Turkish = "İıÖöÜüÇçĞğŞş";

		String[] _Languages = { _French, _Czech, _German, _Hawaiian, _Italian, _Polish, _Romanian, _Russian,
				_Spanish, _Turkish };

		// Used to generate the code
		for (String language : _Languages)
		{
			Logs.log.info(language + ":  ");

			StringBuilder sb = new StringBuilder();
			for (int offset = 0; offset < language.length(); offset++)
			{
				sb.append(language.codePointAt(offset));
				sb.append(", // ");
				sb.append(language.charAt(offset));
				sb.append("\n");
			}

			Logs.log.info(sb);
			Logs.log.info("");
		}

		String[] actualLanguages = { Accents.getFrench(), Accents.getCzech(), Accents.getGerman(),
				Accents.getHawaiian(), Accents.getItalian(), Accents.getPolish(), Accents.getRomanian(),
				Accents.getRussian(), Accents.getSpanish(), Accents.getTurkish() };

		Verify.equals(actualLanguages.length, _Languages.length, false);

		List<String> excludeFields = new ArrayList<String>();
		for (int i = 0; i < _Languages.length; i++)
		{
			Verify.equals(actualLanguages[i], _Languages[i], excludeFields, false);
		}

		writeTestSuccessToLog("runAccents");
	}

	/**
	 * Testing Cache
	 */
	@Test(enabled = true)
	public static void runCacheTest()
	{
		writeTestIDtoLog("runCacheTest");

		Logs.logSection("Caching WebElement Example Test #1");

		int nWebElements = 20000;
		int nFirstName_Size = 1;
		int nLastName_Size = 2;
		int nExpected_PossibleToBeFound = 10;
		int nExpected_NotPossibleToBeFound = 5;
		List<Parameter> expectedData = new ArrayList<Parameter>();
		for (int i = 0; i < nExpected_PossibleToBeFound; i++)
		{
			String first = Rand.letters(nFirstName_Size);
			String last = Rand.letters(nLastName_Size);
			expectedData.add(new Parameter(first, last));
		}

		for (int i = 0; i < nExpected_NotPossibleToBeFound; i++)
		{
			String first = Rand.letters(nFirstName_Size + Rand.randomRange(1, 10));
			String last = Rand.letters(nLastName_Size + Rand.randomRange(1, 10));
			expectedData.add(new Parameter(first, last));
		}

		HashMap<String, Parameter> cache = new HashMap<String, Parameter>();
		for (int i = 0; i < expectedData.size(); i++)
		{
			boolean found = false;
			Parameter item = expectedData.get(i);
			String expectedFullname = item.param + " " + item.value;

			for (int j = 0; j < nWebElements; j++)
			{
				// First check the cache for the actual full name to speed up execution
				Parameter p = cache.get(expectedFullname);

				// If not in cache, then extract actual information and store in cache to speed up possible
				// future iterations of the outer loop
				if (p == null)
				{
					// Simulate extraction from WebElement
					// Note: This would be the expensive operation that you want to only occur once
					String first = Rand.letters(nFirstName_Size);
					String last = Rand.letters(nLastName_Size);
					p = new Parameter(first, last);

					// Update cache
					cache.put(p.param + " " + p.value, p);
				}

				// Did we find what we were looking for?
				String actualFullname = p.param + " " + p.value;
				if (Compare.equals(actualFullname, expectedFullname, Comparison.EqualsIgnoreCase))
				{
					found = true;
					break;
				}
			}

			// Note: If the item is not found, then the cache contains all displayed (actual) items
			if (found)
			{
				Logs.log.info("Found:  " + expectedFullname);
			}
			else
			{
				Logs.log.warn("Could not find:  " + expectedFullname);
			}
		}

		Logs.logSection("Caching WebElement Example Test #2");
		WebDriver driver = null;
		SamplePageCache anotherCache = new SamplePageCache(driver);
		Logs.log.info("Caching Start:  " + new Date());
		for (int i = 0; i < nExpected_PossibleToBeFound; i++)
		{
			Logs.log.info("User's Language - Lookup (" + i + "):  " + anotherCache.getLanguage());
		}

		Logs.log.info("Caching Stop:   " + new Date());
		Logs.logSection("Caching Speed Test");

		int _TestData_Max = 20000;
		int _Lookup_Max = 10000;
		int _TestData_Size = 3;

		String[] data = new String[_TestData_Max];
		List<String> slower = new ArrayList<String>();
		for (int i = 0; i < _TestData_Max; i++)
		{
			String item = Rand.letters(_TestData_Size);
			data[i] = item;
			slower.add(item);
		}

		Logs.log.info("Caching Start:  " + new Date());
		HashMap<String, Integer> faster = Conversion.getCache(slower);
		Logs.log.info("Caching Stop:   " + new Date());
		Logs.log.info("Cache Size:     " + faster.size());
		Logs.log.info("");

		int[] randomIndex = Rand.uniqueRandom(_Lookup_Max, _TestData_Max);

		Logs.log.info("Slower Start:  " + new Date());
		for (int i = 0; i < _Lookup_Max; i++)
		{
			slower.indexOf(data[randomIndex[i]]);
			// Logs.log.info(slower.indexOf(data[randomIndex[i]]));
		}
		Logs.log.info("Slower Stop:   " + new Date());
		Logs.log.info("");
		Logs.log.info("Faster Start:  " + new Date());
		for (int i = 0; i < _Lookup_Max; i++)
		{
			faster.get(data[randomIndex[i]]);
			// Logs.log.info(faster.get(data[randomIndex[i]]));
		}

		Logs.log.info("Faster Stop:   " + new Date());

		writeTestSuccessToLog("runCacheTest");
	}
}
