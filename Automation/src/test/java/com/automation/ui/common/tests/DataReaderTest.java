package com.automation.ui.common.tests;

import java.util.List;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.AutoCompleteField;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.FindTextCriteria;
import com.automation.ui.common.dataStructures.FindWebElementData;
import com.automation.ui.common.dataStructures.UploadFileData;
import com.automation.ui.common.dataStructures.WebElementIndexOfMethod;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.DataReader;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Rand;
import com.automation.ui.common.utilities.TestResults;
import com.automation.ui.common.utilities.VTD_XML;

/**
 * This class hold the unit tests for the class that parse XML files
 */
public class DataReaderTest {
	private static final String _TestXML = "src/test/resources/data_DataReader.xml";

	@Test
	public static void runAutoCompleteFieldTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runAutoCompleteFieldTests");
		TestResults results = new TestResults();
		String sXpath;

		VTD_XML vtd = new VTD_XML(_TestXML);

		// No Defaults Used #1
		boolean skip = false;
		String value = "aa";
		boolean cancelSelection = false;
		boolean useIndex = false;
		String selectOption = "zz";
		String triggerLength = "5";
		AutoCompleteField defaults = new AutoCompleteField(skip, value, cancelSelection, useIndex,
				selectOption, triggerLength);

		sXpath = "/data/AutoCompleteField/NoDefaultsUsed1";
		AutoCompleteField acf = DataReader.getAutoCompleteField(vtd, sXpath, defaults);
		results.expectFalse(acf.skip == skip);
		results.expectFalse(Compare.equals(acf.value, value, Comparison.Equal));
		results.expectFalse(acf.cancelSelection == cancelSelection);
		results.expectFalse(acf.useIndex == useIndex);
		results.expectFalse(Compare.equals(acf.selectOption, selectOption, Comparison.Equal));
		results.expectFalse(Compare.equals(acf.triggerLength, triggerLength, Comparison.Equal));

		if (results.isError())
		{
			results.logWarn("No Defaults Used #1 failed");
			results.logWarn("Defaults:  " + defaults);
			results.logWarn("Actual:    " + acf);
		}

		// No Defaults Used #2
		skip = true;
		value = "aa";
		cancelSelection = true;
		useIndex = true;
		selectOption = "zz";
		triggerLength = "5";
		defaults = new AutoCompleteField(skip, value, cancelSelection, useIndex, selectOption, triggerLength);

		sXpath = "/data/AutoCompleteField/NoDefaultsUsed2";
		AutoCompleteField acf2 = DataReader.getAutoCompleteField(vtd, sXpath, defaults);
		results.expectFalse(acf2.skip == skip);
		results.expectFalse(Compare.equals(acf2.value, value, Comparison.Equal));
		results.expectFalse(acf2.cancelSelection == cancelSelection);
		results.expectFalse(acf2.useIndex == useIndex);
		results.expectFalse(Compare.equals(acf2.selectOption, selectOption, Comparison.Equal));
		results.expectFalse(Compare.equals(acf2.triggerLength, triggerLength, Comparison.Equal));

		if (results.isError())
		{
			results.logWarn("No Defaults Used #2 failed");
			results.logWarn("Defaults:  " + defaults);
			results.logWarn("Actual:    " + acf2);
		}

		// All Defaults Used
		skip = true;
		value = "aa";
		cancelSelection = false;
		useIndex = true;
		selectOption = "zz";
		triggerLength = "5";
		defaults = new AutoCompleteField(skip, value, cancelSelection, useIndex, selectOption, triggerLength);

		sXpath = "/data/AutoCompleteField/AllDefaultsUsed";
		AutoCompleteField acf3 = DataReader.getAutoCompleteField(vtd, sXpath, defaults);
		results.expectTrue(acf3.skip == skip);
		results.expectTrue(Compare.equals(acf3.value, value, Comparison.Equal));
		results.expectTrue(acf3.cancelSelection == cancelSelection);
		results.expectTrue(acf3.useIndex == useIndex);
		results.expectTrue(Compare.equals(acf3.selectOption, selectOption, Comparison.Equal));
		results.expectTrue(Compare.equals(acf3.triggerLength, triggerLength, Comparison.Equal));

		if (results.isError())
		{
			results.logWarn("All Defaults Used failed");
			results.logWarn("Defaults:  " + defaults);
			results.logWarn("Actual:    " + acf3);
		}

		// Some Defaults Used #1
		skip = true;
		value = "aa";
		cancelSelection = false;
		useIndex = false;
		selectOption = "zz";
		triggerLength = "5";
		defaults = new AutoCompleteField(skip, value, cancelSelection, useIndex, selectOption, triggerLength);

		sXpath = "/data/AutoCompleteField/SomeDefaultsUsed1";
		AutoCompleteField acf4 = DataReader.getAutoCompleteField(vtd, sXpath, defaults);
		results.expectTrue(acf4.skip == skip);
		results.expectFalse(Compare.equals(acf4.value, value, Comparison.Equal));
		results.expectFalse(acf4.cancelSelection == cancelSelection);
		results.expectFalse(acf4.useIndex == useIndex);
		results.expectFalse(Compare.equals(acf4.selectOption, selectOption, Comparison.Equal));
		results.expectTrue(Compare.equals(acf4.triggerLength, triggerLength, Comparison.Equal));

		if (results.isError())
		{
			results.logWarn("Some Defaults Used #1 failed");
			results.logWarn("Defaults:  " + defaults);
			results.logWarn("Actual:    " + acf4);
		}

		// Some Defaults Used #2
		skip = true;
		value = "aa";
		cancelSelection = true;
		useIndex = true;
		selectOption = "zz";
		triggerLength = "5";
		defaults = new AutoCompleteField(skip, value, cancelSelection, useIndex, selectOption, triggerLength);

		sXpath = "/data/AutoCompleteField/SomeDefaultsUsed2";
		AutoCompleteField acf5 = DataReader.getAutoCompleteField(vtd, sXpath, defaults);
		results.expectFalse(acf5.skip == skip);
		results.expectTrue(Compare.equals(acf5.value, value, Comparison.Equal));
		results.expectFalse(acf5.cancelSelection == cancelSelection);
		results.expectFalse(acf5.useIndex == useIndex);
		results.expectTrue(Compare.equals(acf5.selectOption, selectOption, Comparison.Equal));
		results.expectFalse(Compare.equals(acf5.triggerLength, triggerLength, Comparison.Equal));

		if (results.isError())
		{
			results.logWarn("Some Defaults Used #2 failed");
			results.logWarn("Defaults:  " + defaults);
			results.logWarn("Actual:    " + acf5);
		}

		// List Test
		skip = true;
		value = "aa";
		cancelSelection = false;
		useIndex = true;
		selectOption = "zz";
		triggerLength = "5";
		defaults = new AutoCompleteField(skip, value, cancelSelection, useIndex, selectOption, triggerLength);

		sXpath = "/data/AutoCompleteField/List/ACF/";
		List<AutoCompleteField> acl = DataReader.getAutoCompleteList(vtd, sXpath, defaults);
		if (results.expectTrue(acl.size() == 5, "Expected list size (5) but Actual list size (" + acl.size()
				+ ")"))
		{
			results.expectTrue(acl.get(0).equals(acf), "List Item 0 did not match acf");
			results.expectTrue(acl.get(1).equals(acf2), "List Item 1 did not match acf2");
			results.expectTrue(acl.get(2).equals(acf3), "List Item 2 did not match acf3");

			results.logInfo("Verifying List items with defaults - Attributes present");
			results.expectTrue(acl.get(3).value.equals(acf4.value));
			results.expectTrue(acl.get(3).cancelSelection == acf4.cancelSelection);
			results.expectTrue(acl.get(3).useIndex == acf4.useIndex);
			results.expectTrue(acl.get(3).selectOption.equals(acf4.selectOption));
			results.expectTrue(acl.get(4).skip == acf5.skip);
			results.expectTrue(acl.get(4).cancelSelection == acf5.cancelSelection);
			results.expectTrue(acl.get(4).useIndex == acf5.useIndex);
			results.expectTrue(acl.get(4).triggerLength.equals(acf5.triggerLength));

			results.logInfo("Verifying List items with defaults - No Attributes");
			results.expectTrue(acl.get(3).skip == skip);
			results.expectTrue(acl.get(3).triggerLength.equals(triggerLength));
			results.expectTrue(acl.get(4).value.equals(value));
			results.expectTrue(acl.get(4).selectOption.equals(selectOption));
		}

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runAutoCompleteFieldTests");
	}

	@Test
	public static void runCheckBoxTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runCheckBoxTests");
		TestResults results = new TestResults();

		@SuppressWarnings("unused")
		VTD_XML vtd = new VTD_XML(_TestXML);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runCheckBoxTests");
	}

	@Test
	public static void runDropDownTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runDropDownTests");
		TestResults results = new TestResults();

		@SuppressWarnings("unused")
		VTD_XML vtd = new VTD_XML(_TestXML);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runDropDownTests");
	}

	@Test
	public static void runEncodedFieldTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runEncodedFieldTests");
		TestResults results = new TestResults();

		@SuppressWarnings("unused")
		VTD_XML vtd = new VTD_XML(_TestXML);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runEncodedFieldTests");
	}

	@Test
	public static void runFindTextCriteriaTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runFindTextCriteriaTests");
		TestResults results = new TestResults();

		@SuppressWarnings("unused")
		VTD_XML vtd = new VTD_XML(_TestXML);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runFindTextCriteriaTests");
	}

	@Test
	public static void runFindWebElementDataTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runFindWebElementDataTests");
		TestResults results = new TestResults();
		String sXpath;

		VTD_XML vtd = new VTD_XML(_TestXML);

		// No Defaults Used #1
		WebElementIndexOfMethod findMethod = WebElementIndexOfMethod.Text;
		String findAttribute = "id";
		Comparison compare = Comparison.Contains;
		String value = "abc";
		FindTextCriteria textCriteria = new FindTextCriteria(compare, value);
		FindWebElementData defaults = new FindWebElementData(findMethod, findAttribute, textCriteria);

		sXpath = "/data/FindWebElementData/NoDefaultsUsed1";
		FindWebElementData data1 = DataReader.getFindWebElementData(vtd, sXpath, defaults);
		results.expectFalse(data1.findMethod == findMethod);
		results.expectTrue(data1.findMethod == WebElementIndexOfMethod.Attribute);
		results.expectFalse(Compare.equals(data1.findAttribute, findAttribute, Comparison.Equal));
		results.expectTrue(Compare.equals(data1.findAttribute, "data-bind", Comparison.Equal));
		results.expectFalse(data1.textCriteria.compare == compare);
		results.expectTrue(data1.textCriteria.compare == Comparison.NotEqual);
		results.expectFalse(Compare.equals(data1.textCriteria.value, value, Comparison.Equal));
		results.expectTrue(Compare.equals(data1.textCriteria.value, "aAa", Comparison.Equal));

		if (results.isError())
		{
			results.logWarn("No Defaults Used #1 failed");
			results.logWarn("Defaults:  " + defaults);
			results.logWarn("Actual:    " + data1);
		}

		// No Defaults Used #2
		findMethod = WebElementIndexOfMethod.Text;
		findAttribute = "id";
		compare = Comparison.Contains;
		value = "abc";
		textCriteria = new FindTextCriteria(compare, value);
		defaults = new FindWebElementData(findMethod, findAttribute, textCriteria);

		sXpath = "/data/FindWebElementData/NoDefaultsUsed2";
		FindWebElementData data2 = DataReader.getFindWebElementData(vtd, sXpath, defaults);
		results.expectFalse(data2.findMethod == findMethod);
		results.expectTrue(data2.findMethod == WebElementIndexOfMethod.JavaScript);
		results.expectFalse(Compare.equals(data2.findAttribute, findAttribute, Comparison.Equal));
		results.expectTrue(Compare.equals(data2.findAttribute, "Value", Comparison.Equal));
		results.expectFalse(data2.textCriteria.compare == compare);
		results.expectTrue(data2.textCriteria.compare == Comparison.Equal);
		results.expectFalse(Compare.equals(data2.textCriteria.value, value, Comparison.Equal));
		results.expectTrue(Compare.equals(data2.textCriteria.value, "BbB", Comparison.Equal));

		if (results.isError())
		{
			results.logWarn("No Defaults Used #2 failed");
			results.logWarn("Defaults:  " + defaults);
			results.logWarn("Actual:    " + data2);
		}

		// All Defaults Used
		findMethod = WebElementIndexOfMethod.Text;
		findAttribute = "id";
		compare = Comparison.Contains;
		value = "abc";
		textCriteria = new FindTextCriteria(compare, value);
		defaults = new FindWebElementData(findMethod, findAttribute, textCriteria);

		sXpath = "/data/FindWebElementData/AllDefaultsUsed";
		FindWebElementData data3 = DataReader.getFindWebElementData(vtd, sXpath, defaults);
		results.expectTrue(data3.findMethod == findMethod);
		results.expectTrue(Compare.equals(data3.findAttribute, findAttribute, Comparison.Equal));
		results.expectTrue(data3.textCriteria.compare == compare);
		results.expectTrue(Compare.equals(data3.textCriteria.value, value, Comparison.Equal));

		if (results.isError())
		{
			results.logWarn("All Defaults Used failed");
			results.logWarn("Defaults:  " + defaults);
			results.logWarn("Actual:    " + data3);
		}

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runFindWebElementDataTests");
	}

	@Test
	public static void runGenericDateTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runGenericDateTests");
		TestResults results = new TestResults();

		@SuppressWarnings("unused")
		VTD_XML vtd = new VTD_XML(_TestXML);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runGenericDateTests");
	}

	@Test
	public static void runInputFieldTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runInputFieldTests");
		TestResults results = new TestResults();

		@SuppressWarnings("unused")
		VTD_XML vtd = new VTD_XML(_TestXML);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runInputFieldTests");
	}

	@Test
	public static void runKeyValuePairTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runKeyValuePairTests");
		TestResults results = new TestResults();

		@SuppressWarnings("unused")
		VTD_XML vtd = new VTD_XML(_TestXML);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runKeyValuePairTests");
	}

	@Test
	public static void runSelectionCriteriaTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runSelectionCriteriaTests");
		TestResults results = new TestResults();

		@SuppressWarnings("unused")
		VTD_XML vtd = new VTD_XML(_TestXML);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runSelectionCriteriaTests");
	}

	@Test
	public static void runStringListTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runStringListTests");
		TestResults results = new TestResults();

		@SuppressWarnings("unused")
		VTD_XML vtd = new VTD_XML(_TestXML);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runStringListTests");
	}

	@Test
	public static void runUploadFileTests() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runUploadFileTests");
		TestResults results = new TestResults();
		String sXpath;
		boolean bResult;

		VTD_XML vtd = new VTD_XML(_TestXML);

		String file = Rand.letters(5, 10);
		String alias = Rand.letters(10, 20);
		String uniqueID = Rand.numbers(5, 10);
		int size = Rand.randomRange(-100, -200);
		UploadFileData defaults = new UploadFileData(file, alias, uniqueID, size);

		sXpath = "/data/UploadFileData/NoDefaultsUsed1";
		results.logInfo("No Defaults Used #1 Tests");
		UploadFileData ufd1 = DataReader.getUploadFileData(vtd, sXpath, defaults);

		bResult = ufd1.file.equals(defaults.file);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(ufd1.file);

		bResult = ufd1.file.equals("test");
		if (!results.expectTrue(bResult))
			results.logWarn(ufd1.file, "test");

		bResult = ufd1.alias.equals(defaults.alias);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(ufd1.alias);

		bResult = ufd1.alias.equals("something");
		if (!results.expectTrue(bResult))
			results.logWarn(ufd1.alias, "something");

		bResult = ufd1.uniqueID.equals(defaults.uniqueID);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(ufd1.uniqueID);

		bResult = ufd1.uniqueID.equals("001");
		if (!results.expectTrue(bResult))
			results.logWarn(ufd1.uniqueID, "001");

		bResult = ufd1.size == defaults.size;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(ufd1.size);

		bResult = ufd1.size == 500;
		if (!results.expectTrue(bResult))
			results.logWarn(ufd1.size, 500);

		//
		// All Defaults Used Tests
		//

		sXpath = "/data/UploadFileData/AllDefaultsUsed";
		results.logInfo("All Defaults Used Tests");
		UploadFileData ufd2 = DataReader.getUploadFileData(vtd, sXpath, defaults);

		bResult = ufd2.file.equals(defaults.file);
		if (!results.expectTrue(bResult))
			results.logWarn(ufd2.file, defaults.file);

		bResult = ufd2.alias.equals(defaults.alias);
		if (!results.expectTrue(bResult))
			results.logWarn(ufd2.alias, defaults.alias);

		bResult = ufd2.uniqueID.equals(defaults.uniqueID);
		if (!results.expectTrue(bResult))
			results.logWarn(ufd2.uniqueID, defaults.uniqueID);

		bResult = ufd2.size == defaults.size;
		if (!results.expectTrue(bResult))
			results.logWarn(ufd2.size, defaults.size);

		//
		// Some Defaults Used #1 Tests
		//

		sXpath = "/data/UploadFileData/SomeDefaultsUsed1";
		results.logInfo("Some Defaults Used #1 Tests");
		UploadFileData ufd3 = DataReader.getUploadFileData(vtd, sXpath, defaults);

		bResult = ufd3.file.equals(defaults.file);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(ufd3.file);

		bResult = ufd3.file.equals("test2");
		if (!results.expectTrue(bResult))
			results.logWarn(ufd3.file, "test2");

		bResult = ufd3.alias.equals(defaults.alias);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(ufd3.alias);

		bResult = ufd3.alias.equals("something2");
		if (!results.expectTrue(bResult))
			results.logWarn(ufd3.alias, "something2");

		bResult = ufd3.uniqueID.equals(defaults.uniqueID);
		if (!results.expectTrue(bResult))
			results.logWarn(ufd3.uniqueID, defaults.uniqueID);

		bResult = ufd3.size == defaults.size;
		if (!results.expectTrue(bResult))
			results.logWarn(ufd3.size, defaults.size);

		//
		// Some Defaults Used #2 Tests
		//

		sXpath = "/data/UploadFileData/SomeDefaultsUsed2";
		results.logInfo("Some Defaults Used #2 Tests");
		UploadFileData ufd4 = DataReader.getUploadFileData(vtd, sXpath, defaults);

		bResult = ufd4.file.equals(defaults.file);
		if (!results.expectTrue(bResult))
			results.logWarn(ufd4.file, defaults.file);

		bResult = ufd4.alias.equals(defaults.alias);
		if (!results.expectTrue(bResult))
			results.logWarn(ufd4.alias, defaults.alias);

		bResult = ufd4.uniqueID.equals(defaults.uniqueID);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(ufd4.uniqueID);

		bResult = ufd4.uniqueID.equals("002");
		if (!results.expectTrue(bResult))
			results.logWarn(ufd4.uniqueID, "002");

		bResult = ufd4.size == defaults.size;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(ufd4.size);

		bResult = ufd4.size == 505;
		if (!results.expectTrue(bResult))
			results.logWarn(ufd4.size, 505);

		//
		//
		//

		sXpath = "/data/UploadFileData/List/UP/";
		List<UploadFileData> files = DataReader.getUploadFiles(vtd, sXpath, defaults);
		results.expectTrue(files.size() == 4, "Expected list size (4) but Actual list size (" + files.size()
				+ ")");

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runUploadFileTests");
	}
}
