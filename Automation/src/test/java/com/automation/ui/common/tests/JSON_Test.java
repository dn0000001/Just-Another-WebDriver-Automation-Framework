package com.automation.ui.common.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Rand;
import com.automation.ui.common.utilities.TestResults;
import com.automation.ui.common.utilities.WS_Util;

/**
 * This class hold the unit tests for JSON conversions
 */
public class JSON_Test {
	@Test
	public static void runArrayTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runArrayTest");

		TestResults results = new TestResults();
		String sJSON, sLog;
		boolean bResult;

		sJSON = "[]";
		List<Object> empty = WS_Util.toArrayList(sJSON);
		sLog = "Empty Test #1 failed";
		bResult = empty.isEmpty();
		results.expectTrue(bResult, sLog);

		sJSON = "[ ]";
		List<Object> empty2 = WS_Util.toArrayList(sJSON);
		sLog = "Empty Test #2 failed";
		bResult = empty2.isEmpty();
		results.expectTrue(bResult, sLog);

		sJSON = "[\"a\", 1, \"C\"]";
		List<Object> nonEmpty = WS_Util.toArrayList(sJSON);

		sLog = "Non-Empty Test #1 failed";
		bResult = nonEmpty.isEmpty();
		results.expectFalse(bResult, sLog);

		sLog = "Non-Empty size test failed";
		bResult = nonEmpty.size() == 3;
		if (results.expectTrue(bResult, sLog))
		{
			sLog = "Array item 0 was not as expected";
			String actual = Conversion.nonNull(nonEmpty.get(0));
			bResult = Compare.equals("a", actual, Comparison.Equal);
			if (!results.expectTrue(bResult, sLog))
				results.logWarn("a", actual);

			sLog = "Array item 1 was not as expected";
			int nActual = Conversion.parseInt(nonEmpty.get(1));
			bResult = 1 == nActual;
			if (!results.expectTrue(bResult, sLog))
				results.logWarn(1, nActual);

			sLog = "Array item 2 was not as expected";
			actual = Conversion.nonNull(nonEmpty.get(2));
			bResult = Compare.equals("C", actual, Comparison.Equal);
			if (!results.expectTrue(bResult, sLog))
				results.logWarn("C", actual);
		}

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runArrayTest");
	}

	@Test(expectedExceptions = GenericUnexpectedException.class)
	public static void runArrayErrorTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runArrayErrorTest");

		String sJSON = "[\"a\", , 1]";
		WS_Util.toArrayList(sJSON);
	}

	@Test
	public static void runMapTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runMapTest");
		TestResults results = new TestResults();
		String sJSON, sLog;
		boolean bResult;

		InputField in = new InputField(false, null, Rand.alphanumeric(5, 10), true, false, "", 15);
		sJSON = WS_Util.toJSON(in);
		Map<String, Object> objMap = WS_Util.toMap(sJSON);

		boolean verificationValue = in.getVerificationValue().equals(objMap.get("verificationValue"));
		boolean valueToInput = in.getValueToInput().equals(objMap.get("valueToInput"));

		boolean skip = Conversion.parseBoolean(objMap.get("skip"));
		String value = (String) objMap.get("value");
		String randomValue = Conversion.nonNull(objMap.get("randomValue"));
		boolean caseSensitive = Conversion.parseBoolean(objMap.get("caseSensitive"));
		boolean logAll = Conversion.parseBoolean(objMap.get("logAll"));
		String mask = Conversion.nonNull(objMap.get("mask"));
		int maxLength = Conversion.parseInt(objMap.get("maxLength"));
		InputField in2 = new InputField(skip, value, randomValue, caseSensitive, logAll, mask, maxLength);

		sLog = "verificationValue incorrect";
		results.expectTrue(verificationValue, sLog);

		sLog = "valueToInput incorrect";
		results.expectTrue(valueToInput, sLog);

		sLog = "in != in2";
		bResult = in.equals(in2);
		if (!results.expectTrue(bResult, sLog))
			results.logWarn(in, in2);

		sLog = "skip incorrect";
		bResult = in.skip == in2.skip;
		if (!results.expectTrue(bResult, sLog))
			results.logWarn(in.skip, in2.skip);

		sLog = "value incorrect";
		bResult = Compare.equals(in.value, in2.value, Comparison.Equal);
		if (!results.expectTrue(bResult, sLog))
			results.logWarn(in.value, in2.value);

		sLog = "value not null";
		bResult = in.value == null;
		results.expectTrue(bResult, sLog);

		sLog = "randomValue incorrect";
		bResult = Compare.equals(in.randomValue, in2.randomValue, Comparison.Equal);
		if (!results.expectTrue(bResult, sLog))
			results.logWarn(in.randomValue, in2.randomValue);

		sLog = "caseSensitive incorrect";
		bResult = in.caseSensitive == in2.caseSensitive;
		if (!results.expectTrue(bResult, sLog))
			results.logWarn(in.caseSensitive, in2.caseSensitive);

		sLog = "logAll incorrect";
		bResult = in.logAll == in2.logAll;
		if (!results.expectTrue(bResult, sLog))
			results.logWarn(in.logAll, in2.logAll);

		sLog = "mask incorrect";
		bResult = Compare.equals(in.mask, in2.mask, Comparison.Equal);
		if (!results.expectTrue(bResult, sLog))
			results.logWarn(in.mask, in2.mask);

		sLog = "maxLength incorrect";
		bResult = in.maxLength == in2.maxLength;
		if (!results.expectTrue(bResult, sLog))
			results.logWarn(in.maxLength, in2.maxLength);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runMapTest");
	}

	@Test
	public static void runMapListTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runMapListTest");
		TestResults results = new TestResults();
		String sJSON, sLog;
		boolean bResult;

		InputField in1 = new InputField(false, null, Rand.alphanumeric(5, 10), true, false, "\\D", -1);
		InputField in2 = new InputField(false, Rand.alphanumeric(5, 10), "", false, true, "", 4);
		List<InputField> expected = new ArrayList<InputField>();
		expected.add(in1);
		expected.add(in2);
		sJSON = WS_Util.toJSON(expected);
		List<Map<String, Object>> objList = WS_Util.toMapList(sJSON);

		List<InputField> actual = new ArrayList<InputField>();
		for (int i = 0; i < objList.size(); i++)
		{
			Map<String, Object> item = objList.get(i);

			String actualVV = Conversion.nonNull(item.get("verificationValue"));
			String expectedVV = expected.get(i).getVerificationValue();

			sLog = "verificationValue incorrect:  " + i;
			bResult = Compare.equals(expectedVV, actualVV, Comparison.Equal);
			if (!results.expectTrue(bResult, sLog))
				results.logWarn(expectedVV, actualVV);

			String actualV2i = Conversion.nonNull(item.get("valueToInput"));
			String expectedV2i = expected.get(i).getValueToInput();

			sLog = "valueToInput incorrect:  " + i;
			bResult = Compare.equals(expectedV2i, actualV2i, Comparison.Equal);
			if (!results.expectTrue(bResult, sLog))
				results.logWarn(expectedV2i, actualV2i);

			boolean skip = Conversion.parseBoolean(item.get("skip"));
			String value = (String) item.get("value");
			String randomValue = Conversion.nonNull(item.get("randomValue"));
			boolean caseSensitive = Conversion.parseBoolean(item.get("caseSensitive"));
			boolean logAll = Conversion.parseBoolean(item.get("logAll"));
			String mask = Conversion.nonNull(item.get("mask"));
			int maxLength = Conversion.parseInt(item.get("maxLength"));
			InputField temp = new InputField(skip, value, randomValue, caseSensitive, logAll, mask, maxLength);
			actual.add(temp);
		}

		sLog = "List Size incorrect";
		bResult = expected.size() == actual.size();
		if (results.expectTrue(bResult, sLog))
		{
			Comparison eq = Comparison.Equal;
			for (int i = 0; i < expected.size(); i++)
			{
				sLog = "skip incorrect:  " + i;
				bResult = expected.get(i).skip == actual.get(i).skip;
				if (!results.expectTrue(bResult, sLog))
					results.logWarn(expected.get(i).skip, actual.get(i).skip);

				sLog = "value incorrect:  " + i;
				bResult = Compare.equals(expected.get(i).value, actual.get(i).value, eq);
				if (!results.expectTrue(bResult, sLog))
					results.logWarn(expected.get(i).value, actual.get(i).value);

				sLog = "randomValue incorrect:  " + i;
				bResult = Compare.equals(expected.get(i).randomValue, actual.get(i).randomValue, eq);
				if (!results.expectTrue(bResult, sLog))
					results.logWarn(expected.get(i).randomValue, actual.get(i).randomValue);

				sLog = "caseSensitive incorrect:  " + i;
				bResult = expected.get(i).caseSensitive == actual.get(i).caseSensitive;
				if (!results.expectTrue(bResult, sLog))
					results.logWarn(expected.get(i).caseSensitive, actual.get(i).caseSensitive);

				sLog = "logAll incorrect:  " + i;
				bResult = expected.get(i).logAll == actual.get(i).logAll;
				if (!results.expectTrue(bResult, sLog))
					results.logWarn(expected.get(i).logAll, actual.get(i).logAll);

				sLog = "mask incorrect:  " + i;
				bResult = Compare.equals(expected.get(i).mask, actual.get(i).mask, eq);
				if (!results.expectTrue(bResult, sLog))
					results.logWarn(expected.get(i).mask, actual.get(i).mask);

				sLog = "maxLength incorrect:  " + i;
				bResult = expected.get(i).maxLength == actual.get(i).maxLength;
				if (!results.expectTrue(bResult, sLog))
					results.logWarn(expected.get(i).maxLength, actual.get(i).maxLength);

				sLog = "objects incorrect:  " + i;
				bResult = expected.get(i).equals(actual.get(i));
				if (!results.expectTrue(bResult, sLog))
					results.logWarn(expected.get(i), actual.get(i));
			}
		}
		else
		{
			results.logWarn(expected.size(), actual.size());
		}

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runMapListTest");
	}
}
