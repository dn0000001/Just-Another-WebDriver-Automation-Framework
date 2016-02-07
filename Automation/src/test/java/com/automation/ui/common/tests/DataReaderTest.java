package com.automation.ui.common.tests;

import java.util.List;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.AutoCompleteField;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.FindTextCriteria;
import com.automation.ui.common.dataStructures.FindWebElementData;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.SelectionCriteria;
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

		VTD_XML vtd = new VTD_XML(_TestXML);
		String sXpath;
		boolean bResult;

		boolean skip = true;
		String value = "enter";
		String randomValue = "rand";
		boolean caseSensitive = false;
		boolean logAll = false;
		String mask = "\\D";
		int maxLength = 20;
		InputField defaults = new InputField(skip, value, randomValue, caseSensitive, logAll, mask, maxLength);

		sXpath = "/data/InputFieldData/NoDefaultsUsed1";
		results.logInfo("No Defaults Used #1 Tests");
		InputField in1 = DataReader.getInputField(vtd, sXpath, defaults);

		bResult = in1.skip == defaults.skip;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in1.skip);

		bResult = in1.skip == false;
		if (!results.expectTrue(bResult))
			results.logWarn(in1.skip, false);

		bResult = in1.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in1.value);

		bResult = in1.value.equals("Truck");
		if (!results.expectTrue(bResult))
			results.logWarn(in1.value, "Truck");

		bResult = in1.randomValue.equals(defaults.randomValue);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in1.randomValue);

		bResult = in1.randomValue.equals("append");
		if (!results.expectTrue(bResult))
			results.logWarn(in1.randomValue, "append");

		bResult = in1.caseSensitive == defaults.caseSensitive;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in1.caseSensitive);

		bResult = in1.caseSensitive == true;
		if (!results.expectTrue(bResult))
			results.logWarn(in1.caseSensitive, true);

		bResult = in1.logAll == defaults.logAll;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in1.logAll);

		bResult = in1.logAll == true;
		if (!results.expectTrue(bResult))
			results.logWarn(in1.logAll, true);

		bResult = in1.mask.equals(defaults.mask);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in1.mask);

		bResult = in1.mask.equals("[^A-Za-z]");
		if (!results.expectTrue(bResult))
			results.logWarn(in1.mask, "[^A-Za-z]");

		bResult = in1.maxLength == defaults.maxLength;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in1.maxLength);

		bResult = in1.maxLength == 30;
		if (!results.expectTrue(bResult))
			results.logWarn(in1.maxLength, 30);

		boolean skip2 = false;
		String value2 = "enter";
		String randomValue2 = "rand";
		boolean caseSensitive2 = true;
		boolean logAll2 = true;
		String mask2 = "\\D";
		int maxLength2 = 20;
		InputField defaults2 = new InputField(skip2, value2, randomValue2, caseSensitive2, logAll2, mask2,
				maxLength2);

		sXpath = "/data/InputFieldData/NoDefaultsUsed2";
		results.logInfo("No Defaults Used #2 Tests");
		InputField in2 = DataReader.getInputField(vtd, sXpath, defaults2);

		bResult = in2.skip == defaults2.skip;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in2.skip);

		bResult = in2.skip == true;
		if (!results.expectTrue(bResult))
			results.logWarn(in2.skip, true);

		bResult = in2.value.equals(defaults2.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in2.value);

		bResult = in2.value.equals("Car");
		if (!results.expectTrue(bResult))
			results.logWarn(in2.value, "Car");

		bResult = in2.randomValue.equals(defaults2.randomValue);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in2.randomValue);

		bResult = in2.randomValue.equals("Something");
		if (!results.expectTrue(bResult))
			results.logWarn(in2.randomValue, "Something");

		bResult = in2.caseSensitive == defaults2.caseSensitive;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in2.caseSensitive);

		bResult = in2.caseSensitive == false;
		if (!results.expectTrue(bResult))
			results.logWarn(in2.caseSensitive, false);

		bResult = in2.logAll == defaults2.logAll;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in2.logAll);

		bResult = in2.logAll == false;
		if (!results.expectTrue(bResult))
			results.logWarn(in2.logAll, false);

		bResult = in2.mask.equals(defaults2.mask);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in2.mask);

		bResult = in2.mask.equals("mask");
		if (!results.expectTrue(bResult))
			results.logWarn(in2.mask, "mask");

		bResult = in2.maxLength == defaults2.maxLength;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in2.maxLength);

		bResult = in2.maxLength == 5;
		if (!results.expectTrue(bResult))
			results.logWarn(in2.maxLength, 5);

		//
		// All Defaults Used Tests
		//

		sXpath = "/data/InputFieldData/AllDefaultsUsed";
		results.logInfo("All Defaults Used Tests");
		InputField in3 = DataReader.getInputField(vtd, sXpath, defaults);

		bResult = in3.skip == defaults.skip;
		if (!results.expectTrue(bResult))
			results.logWarn(in3.skip, defaults.skip);

		bResult = in3.value.equals(defaults.value);
		if (!results.expectTrue(bResult))
			results.logWarn(in3.value, defaults.value);

		bResult = in3.randomValue.equals(defaults.randomValue);
		if (!results.expectTrue(bResult))
			results.logWarn(in3.randomValue, defaults.randomValue);

		bResult = in3.caseSensitive == defaults.caseSensitive;
		if (!results.expectTrue(bResult))
			results.logWarn(in3.caseSensitive, defaults.caseSensitive);

		bResult = in3.logAll == defaults.logAll;
		if (!results.expectTrue(bResult))
			results.logWarn(in3.logAll, defaults.logAll);

		bResult = in3.mask.equals(defaults.mask);
		if (!results.expectTrue(bResult))
			results.logWarn(in3.mask, defaults.mask);

		bResult = in3.maxLength == defaults.maxLength;
		if (!results.expectTrue(bResult))
			results.logWarn(in3.maxLength, defaults.maxLength);

		//
		// Some Defaults Used #1 Tests
		//

		sXpath = "/data/InputFieldData/SomeDefaultsUsed1";
		results.logInfo("Some Defaults Used #1 Tests");
		InputField in4 = DataReader.getInputField(vtd, sXpath, defaults);

		bResult = in4.skip == defaults.skip;
		if (!results.expectTrue(bResult))
			results.logWarn(in4.skip, defaults.skip);

		bResult = in4.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in4.value);

		bResult = in4.value.equals("Van");
		if (!results.expectTrue(bResult))
			results.logWarn(in4.value, "Van");

		bResult = in4.randomValue.equals(defaults.randomValue);
		if (!results.expectTrue(bResult))
			results.logWarn(in4.randomValue, defaults.randomValue);

		bResult = in4.caseSensitive == defaults.caseSensitive;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in4.caseSensitive);

		bResult = in4.caseSensitive == true;
		if (!results.expectTrue(bResult))
			results.logWarn(in4.caseSensitive, true);

		bResult = in4.logAll == defaults.logAll;
		if (!results.expectTrue(bResult))
			results.logWarn(in4.logAll, defaults.logAll);

		bResult = in4.mask.equals(defaults.mask);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in4.mask);

		bResult = in4.mask.equals("[^A-Za-z]");
		if (!results.expectTrue(bResult))
			results.logWarn(in4.mask, "[^A-Za-z]");

		bResult = in4.maxLength == defaults.maxLength;
		if (!results.expectTrue(bResult))
			results.logWarn(in4.maxLength, defaults.maxLength);

		//
		// Some Defaults Used #2 Tests
		//

		sXpath = "/data/InputFieldData/SomeDefaultsUsed2";
		results.logInfo("Some Defaults Used #2 Tests");
		InputField in5 = DataReader.getInputField(vtd, sXpath, defaults);

		bResult = in5.skip == defaults.skip;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in5.skip);

		bResult = in5.skip == false;
		if (!results.expectTrue(bResult))
			results.logWarn(in5.skip, false);

		bResult = in5.value.equals(defaults.value);
		if (!results.expectTrue(bResult))
			results.logWarn(in5.value, defaults.value);

		bResult = in5.randomValue.equals(defaults.randomValue);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in5.randomValue);

		bResult = in5.randomValue.equals("Rand");
		if (!results.expectTrue(bResult))
			results.logWarn(in5.randomValue, "Rand");

		bResult = in5.caseSensitive == defaults.caseSensitive;
		if (!results.expectTrue(bResult))
			results.logWarn(in5.caseSensitive, defaults.caseSensitive);

		bResult = in5.logAll == defaults.logAll;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in5.logAll);

		bResult = in5.logAll == true;
		if (!results.expectTrue(bResult))
			results.logWarn(in5.logAll, true);

		bResult = in5.mask.equals(defaults.mask);
		if (!results.expectTrue(bResult))
			results.logWarn(in5.mask, defaults.mask);

		bResult = in5.maxLength == defaults.maxLength;
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(in5.maxLength);

		bResult = in5.maxLength == 30;
		if (!results.expectTrue(bResult))
			results.logWarn(in5.maxLength, 30);

		//
		// List Tests
		//

		sXpath = "/data/InputFieldData/List/Field/";
		results.logInfo("List Tests");
		int expectedSize = 5;
		List<InputField> criteria = DataReader.getInputFields(vtd, sXpath, defaults);

		bResult = criteria.size() == expectedSize;
		if (results.expectTrue(bResult))
		{
			bResult = in1.equals(criteria.get(0));
			if (!results.expectTrue(bResult))
				results.logWarn(in1, criteria.get(0));

			bResult = in2.equals(criteria.get(1));
			if (!results.expectTrue(bResult))
				results.logWarn(in2, criteria.get(1));

			bResult = in3.equals(criteria.get(2));
			if (!results.expectTrue(bResult))
				results.logWarn(in3, criteria.get(2));

			bResult = in4.equals(criteria.get(3));
			if (!results.expectTrue(bResult))
				results.logWarn(in4, criteria.get(3));

			bResult = in5.equals(criteria.get(4));
			if (!results.expectTrue(bResult))
				results.logWarn(in5, criteria.get(4));
		}
		else
		{
			results.logWarn(expectedSize, criteria.size());
		}

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

		VTD_XML vtd = new VTD_XML(_TestXML);
		String sXpath;
		boolean bResult;

		// Defaults from DataReader.getKeyValuePair
		Parameter defaults = new Parameter("", "");

		sXpath = "/data/KeyValuePairData/NoDefaultsUsed1";
		results.logInfo("No Defaults Used #1 Tests");
		Parameter p1 = DataReader.getKeyValuePair(vtd, sXpath);

		bResult = p1.param.equals(defaults.param);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(p1.param);

		bResult = p1.param.equals("Test");
		if (!results.expectTrue(bResult))
			results.logWarn(p1.param, "Test");

		bResult = p1.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(p1.value);

		bResult = p1.value.equals("500");
		if (!results.expectTrue(bResult))
			results.logWarn(p1.value, "500");

		sXpath = "/data/KeyValuePairData/NoDefaultsUsed2";
		results.logInfo("No Defaults Used #2 Tests");
		Parameter p2 = DataReader.getKeyValuePair(vtd, sXpath);

		bResult = p2.param.equals(defaults.param);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(p2.param);

		bResult = p2.param.equals("1");
		if (!results.expectTrue(bResult))
			results.logWarn(p2.param, "1");

		bResult = p2.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(p2.value);

		bResult = p2.value.equals("Something");
		if (!results.expectTrue(bResult))
			results.logWarn(p2.value, "Something");

		//
		// All Defaults Used Tests
		//

		sXpath = "/data/KeyValuePairData/AllDefaultsUsed";
		results.logInfo("All Defaults Used Tests");
		Parameter p3 = DataReader.getKeyValuePair(vtd, sXpath);

		bResult = p3.param.equals(defaults.param);
		if (!results.expectTrue(bResult))
			results.logWarn(p3.param, defaults.param);

		bResult = p3.value.equals(defaults.value);
		if (!results.expectTrue(bResult))
			results.logWarn(p3.value, defaults.value);

		//
		// Some Defaults Used #1 Tests
		//

		sXpath = "/data/KeyValuePairData/SomeDefaultsUsed1";
		results.logInfo("Some Defaults Used #1 Tests");
		Parameter p4 = DataReader.getKeyValuePair(vtd, sXpath);

		bResult = p4.param.equals(defaults.param);
		if (!results.expectTrue(bResult))
			results.logWarn(p4.param, defaults.param);

		bResult = p4.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(p4.value);

		bResult = p4.value.equals("no key");
		if (!results.expectTrue(bResult))
			results.logWarn(p4.value, "no key");

		//
		// Some Defaults Used #2 Tests
		//

		sXpath = "/data/KeyValuePairData/SomeDefaultsUsed2";
		results.logInfo("Some Defaults Used #2 Tests");
		Parameter p5 = DataReader.getKeyValuePair(vtd, sXpath);

		bResult = p5.param.equals(defaults.param);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(p5.param);

		bResult = p5.param.equals("no value");
		if (!results.expectTrue(bResult))
			results.logWarn(p5.param, "no value");

		bResult = p5.value.equals(defaults.value);
		if (!results.expectTrue(bResult))
			results.logWarn(p5.value, defaults.value);

		//
		// List Tests
		//

		sXpath = "/data/KeyValuePairData/List/Pair/";
		results.logInfo("List Tests");
		int expectedSize = 5;
		List<Parameter> criteria = DataReader.getKeyValuePairs(vtd, sXpath);

		bResult = criteria.size() == expectedSize;
		if (results.expectTrue(bResult))
		{
			bResult = p1.equals(criteria.get(0));
			if (!results.expectTrue(bResult))
				results.logWarn(p1, criteria.get(0));

			bResult = p2.equals(criteria.get(1));
			if (!results.expectTrue(bResult))
				results.logWarn(p2, criteria.get(1));

			bResult = p3.equals(criteria.get(2));
			if (!results.expectTrue(bResult))
				results.logWarn(p3, criteria.get(2));

			bResult = p4.equals(criteria.get(3));
			if (!results.expectTrue(bResult))
				results.logWarn(p4, criteria.get(3));

			bResult = p5.equals(criteria.get(4));
			if (!results.expectTrue(bResult))
				results.logWarn(p5, criteria.get(4));
		}
		else
		{
			results.logWarn(expectedSize, criteria.size());
		}

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

		VTD_XML vtd = new VTD_XML(_TestXML);
		String sXpath;
		boolean bResult;

		// Defaults from DataReader.getSelectionCriteriaData
		SelectionCriteria defaults = new SelectionCriteria("false", ".*");

		sXpath = "/data/SelectionCriteriaData/NoDefaultsUsed1";
		results.logInfo("No Defaults Used #1 Tests");
		SelectionCriteria sc1 = DataReader.getSelectionCriteriaData(vtd, sXpath);

		bResult = sc1.findMethod.equals(defaults.findMethod);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc1.findMethod);

		bResult = sc1.findMethod.equals("true");
		if (!results.expectTrue(bResult))
			results.logWarn(sc1.findMethod, "true");

		bResult = sc1.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc1.value);

		bResult = sc1.value.equals("100");
		if (!results.expectTrue(bResult))
			results.logWarn(sc1.value, "100");

		sXpath = "/data/SelectionCriteriaData/NoDefaultsUsed2";
		results.logInfo("No Defaults Used #2 Tests");
		SelectionCriteria sc2 = DataReader.getSelectionCriteriaData(vtd, sXpath);

		bResult = sc2.findMethod.equals(defaults.findMethod);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc2.findMethod);

		bResult = sc2.findMethod.equals("true");
		if (!results.expectTrue(bResult))
			results.logWarn(sc2.findMethod, "true");

		bResult = sc2.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc2.value);

		bResult = sc2.value.equals("aAa");
		if (!results.expectTrue(bResult))
			results.logWarn(sc2.value, "aAa");

		sXpath = "/data/SelectionCriteriaData/NoDefaultsUsed3";
		results.logInfo("No Defaults Used #3 Tests");
		SelectionCriteria sc3 = DataReader.getSelectionCriteriaData(vtd, sXpath);

		bResult = sc3.findMethod.equals(defaults.findMethod);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc3.findMethod);

		bResult = sc3.findMethod.equals("0");
		if (!results.expectTrue(bResult))
			results.logWarn(sc3.findMethod, "0");

		bResult = sc3.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc3.value);

		bResult = sc3.value.equals("d");
		if (!results.expectTrue(bResult))
			results.logWarn(sc3.value, "d");

		sXpath = "/data/SelectionCriteriaData/NoDefaultsUsed4";
		results.logInfo("No Defaults Used #4 Tests");
		SelectionCriteria sc4 = DataReader.getSelectionCriteriaData(vtd, sXpath);

		bResult = sc4.findMethod.equals(defaults.findMethod);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc4.findMethod);

		bResult = sc4.findMethod.equals("Test");
		if (!results.expectTrue(bResult))
			results.logWarn(sc4.findMethod, "Test");

		bResult = sc4.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc4.value);

		bResult = sc4.value.equals("200");
		if (!results.expectTrue(bResult))
			results.logWarn(sc4.value, "200");

		//
		// All Defaults Used Tests
		//

		sXpath = "/data/SelectionCriteriaData/AllDefaultsUsed";
		results.logInfo("All Defaults Used Tests");
		SelectionCriteria sc5 = DataReader.getSelectionCriteriaData(vtd, sXpath);

		bResult = sc5.findMethod.equals(defaults.findMethod);
		if (!results.expectTrue(bResult))
			results.logWarn(sc5.findMethod, defaults.findMethod);

		bResult = sc5.value.equals(defaults.value);
		if (!results.expectTrue(bResult))
			results.logWarn(sc5.value, defaults.value);

		//
		// Some Defaults Used #1 Tests
		//

		sXpath = "/data/SelectionCriteriaData/SomeDefaultsUsed1";
		results.logInfo("Some Defaults Used #1 Tests");
		SelectionCriteria sc6 = DataReader.getSelectionCriteriaData(vtd, sXpath);

		bResult = sc6.findMethod.equals(defaults.findMethod);
		if (!results.expectTrue(bResult))
			results.logWarn(sc6.findMethod, defaults.findMethod);

		bResult = sc6.value.equals(defaults.value);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc6.value);

		bResult = sc6.value.equals(".*Test.*");
		if (!results.expectTrue(bResult))
			results.logWarn(sc6.value, ".*Test.*");

		//
		// Some Defaults Used #2 Tests
		//

		sXpath = "/data/SelectionCriteriaData/SomeDefaultsUsed2";
		results.logInfo("Some Defaults Used #2 Tests");
		SelectionCriteria sc7 = DataReader.getSelectionCriteriaData(vtd, sXpath);

		bResult = sc7.findMethod.equals(defaults.findMethod);
		if (!results.expectFalse(bResult))
			results.logWarnUnexpectedEqual(sc7.findMethod);

		bResult = sc7.findMethod.equals("true");
		if (!results.expectTrue(bResult))
			results.logWarn(sc7.findMethod, "true");

		bResult = sc7.value.equals(defaults.value);
		if (!results.expectTrue(bResult))
			results.logWarn(sc7.value, defaults.value);

		//
		// List Tests
		//

		sXpath = "/data/SelectionCriteriaData/List/Criteria/";
		results.logInfo("List Tests");
		int expectedSize = 7;
		List<SelectionCriteria> criteria = DataReader.getSelectionCriteriaLists(vtd, sXpath);

		bResult = criteria.size() == expectedSize;
		if (results.expectTrue(bResult))
		{
			bResult = sc1.equals(criteria.get(0));
			if (!results.expectTrue(bResult))
				results.logWarn(sc1, criteria.get(0));

			bResult = sc2.equals(criteria.get(1));
			if (!results.expectTrue(bResult))
				results.logWarn(sc2, criteria.get(1));

			bResult = sc3.equals(criteria.get(2));
			if (!results.expectTrue(bResult))
				results.logWarn(sc3, criteria.get(2));

			bResult = sc4.equals(criteria.get(3));
			if (!results.expectTrue(bResult))
				results.logWarn(sc4, criteria.get(3));

			bResult = sc5.equals(criteria.get(4));
			if (!results.expectTrue(bResult))
				results.logWarn(sc5, criteria.get(4));

			bResult = sc6.equals(criteria.get(5));
			if (!results.expectTrue(bResult))
				results.logWarn(sc6, criteria.get(5));

			bResult = sc7.equals(criteria.get(6));
			if (!results.expectTrue(bResult))
				results.logWarn(sc7, criteria.get(6));
		}
		else
		{
			results.logWarn(expectedSize, criteria.size());
		}

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

		VTD_XML vtd = new VTD_XML(_TestXML);
		String sXpath = "/data/StringListData/String/";
		boolean bResult;

		int expectedSize = 6;
		List<String> data = DataReader.getStringList(vtd, sXpath);
		bResult = data.size() == expectedSize;
		if (results.expectTrue(bResult))
		{
			String expectedValue = "a";
			bResult = expectedValue.equals(data.get(0));
			if (!results.expectTrue(bResult))
				results.logWarn(expectedValue, data.get(0));

			expectedValue = "abc";
			bResult = expectedValue.equals(data.get(1));
			if (!results.expectTrue(bResult))
				results.logWarn(expectedValue, data.get(1));

			expectedValue = "Test";
			bResult = expectedValue.equals(data.get(2));
			if (!results.expectTrue(bResult))
				results.logWarn(expectedValue, data.get(2));

			expectedValue = "1";
			bResult = expectedValue.equals(data.get(3));
			if (!results.expectTrue(bResult))
				results.logWarn(expectedValue, data.get(3));

			expectedValue = "SomeThing";
			bResult = expectedValue.equals(data.get(4));
			if (!results.expectTrue(bResult))
				results.logWarn(expectedValue, data.get(4));

			expectedValue = "123";
			bResult = expectedValue.equals(data.get(5));
			if (!results.expectTrue(bResult))
				results.logWarn(expectedValue, data.get(5));
		}
		else
		{
			results.logWarn(expectedSize, data.size());
		}

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
