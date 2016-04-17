package com.automation.ui.common.tests;

import java.util.Date;
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Rand;
import com.automation.ui.common.utilities.TestResults;

/**
 * This class is for unit testing the Conversion class
 */
public class ConversionTest {
	/**
	 * Test Object used for the parsing test
	 */
	private static class ParseObject {
		private String value;

		public ParseObject()
		{
			value = null;
		}

		public void setParse_Null()
		{
			value = null;
		}

		public void setParse_Boolean_True()
		{
			value = "1";
		}

		public void setParse_Boolean_False()
		{
			value = "0";
		}

		public void setParse_Int_Positive_True()
		{
			value = String.valueOf(Rand.randomRange(0, 1000));
		}

		public void setParse_Int_Negative_True()
		{
			value = String.valueOf(Rand.randomRange(-1000, -2));
		}

		public void setParse_Float_Positive_True()
		{
			value = String.valueOf(Rand.randomRange(1, 1000)) + "." + String.valueOf(Rand.randomRange(0, 99));
		}

		public void setParse_Float_Negative_True()
		{
			value = "-" + String.valueOf(Rand.randomRange(1, 1000)) + "."
					+ String.valueOf(Rand.randomRange(0, 99));
		}

		public void setParse_Int_Error()
		{
			value = "abc";
		}

		public void setParse_Float_Error()
		{
			value = "def";
		}

		public void setParse_Long_Error()
		{
			value = "ghi";
		}

		public String toString()
		{
			return value;
		}
	}

	@Test
	public static void runParsingTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runParsingTest");

		TestResults results = new TestResults();
		boolean bResult;
		String parse;
		String genericBoolean = "Failure parsing to boolean:  ";
		String genericInteger = "Failure parsing to integer:  ";
		String genericLong = "Failure parsing to long:  ";
		String genericFloat = "Failure parsing to float:  ";
		ParseObject parseObj = new ParseObject();
		ParseObject parseNullObj = null;
		int defaultPositiveInteger = 5;
		int defaultNegativeInteger = -5;
		Long defaultNegativeLong = Long.valueOf(defaultNegativeInteger);
		Long defaultPositiveLong = Long.valueOf(defaultPositiveInteger);
		float variance = new Float("0.001");
		float defaultNegativeFloat = Float.valueOf(defaultNegativeInteger);
		float defaultPositiveFloat = Float.valueOf(defaultPositiveInteger);

		//
		// Parsing to boolean - true
		//

		parse = "true";
		bResult = Conversion.parseBoolean(parse);
		results.expectTrue(bResult, genericBoolean + parse);

		parse = "True";
		bResult = Conversion.parseBoolean(parse);
		results.expectTrue(bResult, genericBoolean + parse);

		parse = "1";
		bResult = Conversion.parseBoolean(parse);
		results.expectTrue(bResult, genericBoolean + parse);

		//
		// Parsing to boolean - false
		//

		parse = "false";
		bResult = Conversion.parseBoolean(parse);
		results.expectFalse(bResult, genericBoolean + parse);

		parse = "False";
		bResult = Conversion.parseBoolean(parse);
		results.expectFalse(bResult, genericBoolean + parse);

		parse = "0";
		bResult = Conversion.parseBoolean(parse);
		results.expectFalse(bResult, genericBoolean + parse);

		parse = "yes";
		bResult = Conversion.parseBoolean(parse);
		results.expectFalse(bResult, genericBoolean + parse);

		parse = "";
		bResult = Conversion.parseBoolean(parse);
		results.expectFalse(bResult, genericBoolean + "empty string");

		parse = null;
		bResult = Conversion.parseBoolean(parse);
		results.expectFalse(bResult, genericBoolean + "null string");

		//
		// Parsing to boolean - Objects - true
		//
		parseObj.setParse_Boolean_True();
		bResult = Conversion.parseBoolean(parseObj);
		results.expectTrue(bResult, genericBoolean + parseObj + " from object setParse_Boolean_True");

		//
		// Parsing to boolean - Objects - false
		//
		parseObj.setParse_Boolean_False();
		bResult = Conversion.parseBoolean(parseObj);
		results.expectFalse(bResult, genericBoolean + parseObj + " from object setParse_Boolean_False");

		parseObj.setParse_Null();
		bResult = Conversion.parseBoolean(parseObj);
		results.expectFalse(bResult, genericBoolean + parseObj + " from object setParse_Null");

		bResult = Conversion.parseBoolean(parseNullObj);
		results.expectFalse(bResult, genericBoolean + "null object");

		//
		// Parsing to integer with no default specified
		//
		parse = "0";
		bResult = Conversion.parseInt(parse) == 0;
		results.expectTrue(bResult, genericInteger + parse);

		parse = "01";
		bResult = Conversion.parseInt(parse) == 1;
		results.expectTrue(bResult, genericInteger + parse);

		int positive = Rand.randomRange(0, 1000);
		parse = String.valueOf(positive);
		bResult = Conversion.parseInt(parse) == positive;
		results.expectTrue(bResult, genericInteger + parse);

		int negative = Rand.randomRange(-1000, -10);
		parse = String.valueOf(negative);
		bResult = Conversion.parseInt(parse) == negative;
		results.expectTrue(bResult, genericInteger + parse);

		//
		// Parsing to integer with no default specified - failure
		//
		parse = "";
		bResult = Conversion.parseInt(parse) == -1;
		results.expectTrue(bResult, genericInteger + "empty string");

		parse = null;
		bResult = Conversion.parseInt(parse) == -1;
		results.expectTrue(bResult, genericInteger + "null string");

		parse = "a1b";
		bResult = Conversion.parseInt(parse) == -1;
		results.expectTrue(bResult, genericInteger + parse);

		parse = "1.0";
		bResult = Conversion.parseInt(parse) == -1;
		results.expectTrue(bResult, genericInteger + parse);

		//
		// Parsing to integer with default specified
		//
		parse = "0";
		bResult = Conversion.parseInt(parse, defaultNegativeInteger) == 0;
		results.expectTrue(bResult, genericInteger + parse + " with default (" + defaultNegativeInteger + ")");

		parse = "0";
		bResult = Conversion.parseInt(parse, defaultPositiveInteger) == 0;
		results.expectTrue(bResult, genericInteger + parse + " with default (" + defaultPositiveInteger + ")");

		parse = "01";
		bResult = Conversion.parseInt(parse, defaultNegativeInteger) == 1;
		results.expectTrue(bResult, genericInteger + parse + " with default (" + defaultNegativeInteger + ")");

		parse = "01";
		bResult = Conversion.parseInt(parse, defaultPositiveInteger) == 1;
		results.expectTrue(bResult, genericInteger + parse + " with default (" + defaultPositiveInteger + ")");

		positive = Rand.randomRange(10, 1000);
		parse = String.valueOf(positive);
		bResult = Conversion.parseInt(parse, defaultNegativeInteger) == positive;
		results.expectTrue(bResult, genericInteger + parse);

		negative = Rand.randomRange(-1000, -10);
		parse = String.valueOf(negative);
		bResult = Conversion.parseInt(parse, defaultPositiveInteger) == negative;
		results.expectTrue(bResult, genericInteger + parse);

		//
		// Parsing to integer with default specified - failure
		//
		parse = "";
		bResult = Conversion.parseInt(parse, defaultNegativeInteger) == defaultNegativeInteger;
		results.expectTrue(bResult, genericInteger + "empty string with default (" + defaultNegativeInteger
				+ ")");

		parse = null;
		bResult = Conversion.parseInt(parse, defaultNegativeInteger) == defaultNegativeInteger;
		results.expectTrue(bResult, genericInteger + "null string with default (" + defaultNegativeInteger
				+ ")");

		parse = "a1b";
		bResult = Conversion.parseInt(parse, defaultNegativeInteger) == defaultNegativeInteger;
		results.expectTrue(bResult, genericInteger + parse + " with default (" + defaultNegativeInteger + ")");

		parse = "1.0";
		bResult = Conversion.parseInt(parse, defaultNegativeInteger) == defaultNegativeInteger;
		results.expectTrue(bResult, genericInteger + parse + " with default (" + defaultNegativeInteger + ")");

		parse = "";
		bResult = Conversion.parseInt(parse, defaultPositiveInteger) == defaultPositiveInteger;
		results.expectTrue(bResult, genericInteger + "empty string with default (" + defaultPositiveInteger
				+ ")");

		parse = null;
		bResult = Conversion.parseInt(parse, defaultPositiveInteger) == defaultPositiveInteger;
		results.expectTrue(bResult, genericInteger + "null string with default (" + defaultPositiveInteger
				+ ")");

		parse = "a1b";
		bResult = Conversion.parseInt(parse, defaultPositiveInteger) == defaultPositiveInteger;
		results.expectTrue(bResult, genericInteger + parse + " with default (" + defaultPositiveInteger + ")");

		parse = "1.0";
		bResult = Conversion.parseInt(parse, defaultPositiveInteger) == defaultPositiveInteger;
		results.expectTrue(bResult, genericInteger + parse + " with default (" + defaultPositiveInteger + ")");

		//
		// Parsing to integer - Objects - no default - success
		//
		parseObj.setParse_Int_Positive_True();
		parse = parseObj.toString();
		bResult = Conversion.parseInt(parseObj) == Conversion.parseInt(parse);
		results.expectTrue(bResult, genericInteger + parseObj + " from object setParse_Int_Positive_True");

		parseObj.setParse_Int_Negative_True();
		parse = parseObj.toString();
		bResult = Conversion.parseInt(parseObj) == Conversion.parseInt(parse);
		results.expectTrue(bResult, genericInteger + parseObj + " from object setParse_Int_Negative_True");

		int primitiveInt = Rand.randomRange(0, 1000);
		bResult = Conversion.parseInt(primitiveInt) == primitiveInt;
		results.expectTrue(bResult, genericInteger + primitiveInt + " primitive int");

		primitiveInt = Rand.randomRange(-10, -1000);
		bResult = Conversion.parseInt(primitiveInt) == primitiveInt;
		results.expectTrue(bResult, genericInteger + primitiveInt + " primitive int");

		//
		// Parsing to integer - Objects - default - success
		//
		parseObj.setParse_Int_Positive_True();
		parse = parseObj.toString();
		bResult = Conversion.parseInt(parseObj, defaultNegativeInteger) == Conversion.parseInt(parse,
				defaultNegativeInteger);
		results.expectTrue(bResult, genericInteger + parseObj + " object with default ("
				+ defaultNegativeInteger + ")");

		parseObj.setParse_Int_Negative_True();
		parse = parseObj.toString();
		bResult = Conversion.parseInt(parseObj, defaultPositiveInteger) == Conversion.parseInt(parse,
				defaultPositiveInteger);
		results.expectTrue(bResult, genericInteger + parseObj + " object with default ("
				+ defaultPositiveInteger + ")");

		parseObj.setParse_Int_Negative_True();
		parse = parseObj.toString();
		bResult = Conversion.parseInt(parseObj) == Conversion.parseInt(parse);
		results.expectTrue(bResult, genericInteger + parseObj + " from object setParse_Int_Negative_True");

		primitiveInt = Rand.randomRange(0, 1000);
		bResult = Conversion.parseInt(primitiveInt, defaultNegativeInteger) == primitiveInt;
		results.expectTrue(bResult, genericInteger + parseObj + " primitive int with default ("
				+ defaultNegativeInteger + ")");

		primitiveInt = Rand.randomRange(-10, -1000);
		bResult = Conversion.parseInt(primitiveInt, defaultPositiveInteger) == primitiveInt;
		results.expectTrue(bResult, genericInteger + parseObj + " primitive int with default ("
				+ defaultPositiveInteger + ")");

		//
		// Parsing to integer - Objects - no default - failure
		//
		parseObj.setParse_Int_Error();
		parse = parseObj.toString();
		bResult = Conversion.parseInt(parseObj) == -1;
		results.expectTrue(bResult, genericInteger + parseObj + " from object setParse_Int_Error");

		parseObj.setParse_Null();
		parse = parseObj.toString();
		bResult = Conversion.parseInt(parseObj) == Conversion.parseInt(parse);
		results.expectTrue(bResult, genericInteger + parseObj + " from object setParse_Null");

		bResult = Conversion.parseInt(parseNullObj) == -1;
		results.expectTrue(bResult, genericInteger + "null object");

		//
		// Parsing to integer - Objects - default - failure
		//
		parseObj.setParse_Int_Error();
		bResult = Conversion.parseInt(parseObj, defaultPositiveInteger) == defaultPositiveInteger;
		results.expectTrue(bResult, genericInteger + parseObj
				+ " from object setParse_Int_Error with default (" + defaultPositiveInteger + ")");

		parseObj.setParse_Int_Error();
		bResult = Conversion.parseInt(parseObj, defaultNegativeInteger) == defaultNegativeInteger;
		results.expectTrue(bResult, genericInteger + parseObj
				+ " from object setParse_Int_Error with default (" + defaultNegativeInteger + ")");

		parseObj.setParse_Null();
		bResult = Conversion.parseInt(parseObj, defaultPositiveInteger) == defaultPositiveInteger;
		results.expectTrue(bResult, genericInteger + parseObj + " from object setParse_Null with default ("
				+ defaultPositiveInteger + ")");

		parseObj.setParse_Null();
		bResult = Conversion.parseInt(parseObj, defaultNegativeInteger) == defaultNegativeInteger;
		results.expectTrue(bResult, genericInteger + parseObj + " from object setParse_Null with default ("
				+ defaultNegativeInteger + ")");

		bResult = Conversion.parseInt(parseNullObj, defaultPositiveInteger) == defaultPositiveInteger;
		results.expectTrue(bResult, genericInteger + "null object with default (" + defaultPositiveInteger
				+ ")");

		bResult = Conversion.parseInt(parseNullObj, defaultNegativeInteger) == defaultNegativeInteger;
		results.expectTrue(bResult, genericInteger + "null object with default (" + defaultNegativeInteger
				+ ")");

		//
		// Parsing to long with no default specified
		//
		parse = "0";
		bResult = Compare.equals(Conversion.parseLong(parse), 0, 0);
		results.expectTrue(bResult, genericLong + parse);

		parse = "1";
		bResult = Compare.equals(Conversion.parseLong(parse), 1, 0);
		results.expectTrue(bResult, genericLong + parse);

		positive = Rand.randomRange(0, 1000);
		parse = String.valueOf(positive);
		bResult = Compare.equals(Conversion.parseLong(parse), positive, 0);
		results.expectTrue(bResult, genericLong + parse);

		negative = Rand.randomRange(-1000, -10);
		parse = String.valueOf(negative);
		bResult = Compare.equals(Conversion.parseLong(parse), negative, 0);
		results.expectTrue(bResult, genericLong + parse);

		//
		// Parsing to long with no default specified - failure
		//
		parse = "";
		bResult = Compare.equals(Conversion.parseLong(parse), -1, 0);
		results.expectTrue(bResult, genericLong + "empty string");

		parse = null;
		bResult = Compare.equals(Conversion.parseLong(parse), -1, 0);
		results.expectTrue(bResult, genericLong + "null string");

		parse = "a1b";
		bResult = Compare.equals(Conversion.parseLong(parse), -1, 0);
		results.expectTrue(bResult, genericLong + parse);

		parse = "1.0";
		bResult = Compare.equals(Conversion.parseLong(parse), -1, 0);
		results.expectTrue(bResult, genericLong + parse);

		//
		// Parsing to long with default specified
		//
		parse = "0";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultNegativeLong), 0, 0);
		results.expectTrue(bResult, genericLong + parse + " with default (" + defaultNegativeInteger + ")");

		parse = "0";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultPositiveLong), 0, 0);
		results.expectTrue(bResult, genericLong + parse + " with default (" + defaultPositiveInteger + ")");

		parse = "01";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultNegativeLong), 1, 0);
		results.expectTrue(bResult, genericLong + parse + " with default (" + defaultNegativeInteger + ")");

		parse = "01";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultPositiveLong), 1, 0);
		results.expectTrue(bResult, genericLong + parse + " with default (" + defaultPositiveInteger + ")");

		positive = Rand.randomRange(10, 1000);
		parse = String.valueOf(positive);
		bResult = Compare.equals(Conversion.parseLong(parse, defaultNegativeLong), positive, 0);
		results.expectTrue(bResult, genericLong + parse);

		negative = Rand.randomRange(-1000, -10);
		parse = String.valueOf(negative);
		bResult = Compare.equals(Conversion.parseLong(parse, defaultPositiveLong), negative, 0);
		results.expectTrue(bResult, genericLong + parse);

		//
		// Parsing to long with default specified - failure
		//
		parse = "";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultNegativeLong), defaultNegativeLong, 0);
		results.expectTrue(bResult, genericLong + "empty string with default (" + defaultNegativeLong + ")");

		parse = null;
		bResult = Compare.equals(Conversion.parseLong(parse, defaultNegativeLong), defaultNegativeLong, 0);
		results.expectTrue(bResult, genericLong + "null string with default (" + defaultNegativeLong + ")");

		parse = "a1b";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultNegativeLong), defaultNegativeLong, 0);
		results.expectTrue(bResult, genericLong + parse + " with default (" + defaultNegativeInteger + ")");

		parse = "1.0";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultNegativeLong), defaultNegativeLong, 0);
		results.expectTrue(bResult, genericLong + parse + " with default (" + defaultNegativeInteger + ")");

		parse = "";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultPositiveLong), defaultPositiveLong, 0);
		results.expectTrue(bResult, genericLong + "empty string with default (" + defaultPositiveLong + ")");

		parse = null;
		bResult = Compare.equals(Conversion.parseLong(parse, defaultPositiveLong), defaultPositiveLong, 0);
		results.expectTrue(bResult, genericLong + "null string with default (" + defaultPositiveInteger + ")");

		parse = "a1b";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultPositiveLong), defaultPositiveLong, 0);
		results.expectTrue(bResult, genericLong + parse + " with default (" + defaultPositiveLong + ")");

		parse = "1.0";
		bResult = Compare.equals(Conversion.parseLong(parse, defaultPositiveLong), defaultPositiveLong, 0);
		results.expectTrue(bResult, genericLong + parse + " with default (" + defaultPositiveLong + ")");

		//
		// Parsing to long - Objects - no default - success
		//
		parseObj.setParse_Int_Positive_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseLong(parseObj), Conversion.parseLong(parse), 0);
		results.expectTrue(bResult, genericLong + parseObj + " from object setParse_Int_Positive_True");

		parseObj.setParse_Int_Negative_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseLong(parseObj), Conversion.parseLong(parse), 0);
		results.expectTrue(bResult, genericLong + parseObj + " from object setParse_Int_Negative_True");

		long primitiveLong = Rand.randomRange(0, 1000);
		bResult = Compare.equals(Conversion.parseLong(primitiveLong), primitiveLong, 0);
		results.expectTrue(bResult, genericLong + primitiveLong + " primitive long");

		primitiveLong = Rand.randomRange(-10, -1000);
		bResult = Compare.equals(Conversion.parseLong(primitiveLong), primitiveLong, 0);
		results.expectTrue(bResult, genericLong + primitiveLong + " primitive long");

		//
		// Parsing to long - Objects - default - success
		//
		parseObj.setParse_Int_Positive_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseLong(parseObj, defaultNegativeLong),
				Conversion.parseLong(parse, defaultNegativeLong), 0);
		results.expectTrue(bResult, genericLong + parseObj + " object with default (" + defaultNegativeLong
				+ ")");

		parseObj.setParse_Int_Negative_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseLong(parseObj, defaultPositiveLong),
				Conversion.parseLong(parse, defaultPositiveLong), 0);
		results.expectTrue(bResult, genericLong + parseObj + " object with default (" + defaultPositiveLong
				+ ")");

		parseObj.setParse_Int_Negative_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseLong(parseObj), Conversion.parseLong(parse), 0);
		results.expectTrue(bResult, genericLong + parseObj + " from object setParse_Int_Negative_True");

		primitiveLong = Rand.randomRange(0, 1000);
		bResult = Compare.equals(Conversion.parseLong(primitiveLong, defaultNegativeLong), primitiveLong, 0);
		results.expectTrue(bResult, genericLong + parseObj + " primitive long with default ("
				+ defaultNegativeLong + ")");

		primitiveLong = Rand.randomRange(-10, -1000);
		bResult = Compare.equals(Conversion.parseLong(primitiveLong, defaultPositiveLong), primitiveLong, 0);
		results.expectTrue(bResult, genericLong + parseObj + " primitive long with default ("
				+ defaultPositiveLong + ")");

		//
		// Parsing to long - Objects - no default - failure
		//
		parseObj.setParse_Long_Error();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseLong(parseObj), -1, 0);
		results.expectTrue(bResult, genericLong + parseObj + " from object setParse_Long_Error");

		parseObj.setParse_Null();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseLong(parseObj), Conversion.parseLong(parse), 0);
		results.expectTrue(bResult, genericLong + parseObj + " from object setParse_Null");

		bResult = Compare.equals(Conversion.parseLong(parseNullObj), -1, 0);
		results.expectTrue(bResult, genericLong + "null object");

		//
		// Parsing to long - Objects - default - failure
		//
		parseObj.setParse_Long_Error();
		bResult = Compare.equals(Conversion.parseLong(parseObj, defaultPositiveLong), defaultPositiveLong, 0);
		results.expectTrue(bResult, genericLong + parseObj
				+ " from object setParse_Long_Error with default (" + defaultPositiveLong + ")");

		parseObj.setParse_Long_Error();
		bResult = Compare.equals(Conversion.parseLong(parseObj, defaultNegativeLong), defaultNegativeLong, 0);
		results.expectTrue(bResult, genericLong + parseObj
				+ " from object setParse_Long_Error with default (" + defaultNegativeLong + ")");

		parseObj.setParse_Null();
		bResult = Compare.equals(Conversion.parseLong(parseObj, defaultPositiveLong), defaultPositiveLong, 0);
		results.expectTrue(bResult, genericLong + parseObj + " from object setParse_Null with default ("
				+ defaultPositiveLong + ")");

		parseObj.setParse_Null();
		bResult = Compare.equals(Conversion.parseLong(parseObj, defaultNegativeLong), defaultNegativeLong, 0);
		results.expectTrue(bResult, genericLong + parseObj + " from object setParse_Null with default ("
				+ defaultNegativeLong + ")");

		bResult = Compare.equals(Conversion.parseLong(parseObj, defaultPositiveLong), defaultPositiveLong, 0);
		results.expectTrue(bResult, genericLong + "null object with default (" + defaultPositiveLong + ")");

		bResult = Compare.equals(Conversion.parseLong(parseObj, defaultNegativeLong), defaultNegativeLong, 0);
		results.expectTrue(bResult, genericLong + "null object with default (" + defaultNegativeLong + ")");

		//
		// Parsing to float with no default specified
		//
		parse = "0";
		bResult = Compare.equals(Conversion.parseFloat(parse), 0, 0);
		results.expectTrue(bResult, genericFloat + parse);

		parse = "1";
		bResult = Compare.equals(Conversion.parseFloat(parse), 1, 0);
		results.expectTrue(bResult, genericFloat + parse);

		parse = "10";
		bResult = Compare.equals(Conversion.parseFloat(parse), 10, 0);
		results.expectTrue(bResult, genericFloat + parse);

		positive = Rand.randomRange(0, 1000);
		parse = String.valueOf(positive);
		bResult = Compare.equals(Conversion.parseFloat(parse), positive, 0);
		results.expectTrue(bResult, genericFloat + parse);

		negative = Rand.randomRange(-1000, -10);
		parse = String.valueOf(negative);
		bResult = Compare.equals(Conversion.parseFloat(parse), negative, 0);
		results.expectTrue(bResult, genericFloat + parse);

		parse = "0.12";
		float expected = new Float(parse);
		bResult = Compare.equals(expected, Conversion.parseFloat(parse), variance);
		results.expectTrue(bResult, genericFloat + parse);

		parse = "0.0";
		expected = new Float(parse);
		bResult = Compare.equals(expected, Conversion.parseFloat(parse), variance);
		results.expectTrue(bResult, genericFloat + parse);

		parse = "1.0";
		expected = new Float(parse);
		bResult = Compare.equals(expected, Conversion.parseFloat(parse), variance);
		results.expectTrue(bResult, genericFloat + parse);

		parse = "105.0";
		expected = new Float(parse);
		bResult = Compare.equals(expected, Conversion.parseFloat(parse), variance);
		results.expectTrue(bResult, genericFloat + parse);

		parse = "105.1258";
		expected = new Float(parse);
		bResult = Compare.equals(expected, Conversion.parseFloat(parse), variance);
		results.expectTrue(bResult, genericFloat + parse);

		parse = String.valueOf(Rand.randomRange(1, 1000)) + "." + String.valueOf(Rand.randomRange(0, 99));
		expected = new Float(parse);
		bResult = Compare.equals(expected, Conversion.parseFloat(parse), variance);
		results.expectTrue(bResult, genericFloat + parse);

		parse = "-" + String.valueOf(Rand.randomRange(1, 1000)) + "."
				+ String.valueOf(Rand.randomRange(0, 99));
		expected = new Float(parse);
		bResult = Compare.equals(expected, Conversion.parseFloat(parse), variance);
		results.expectTrue(bResult, genericFloat + parse);

		//
		// Parsing to float with no default specified - failure
		//
		parse = "";
		bResult = Compare.equals(Conversion.parseFloat(parse), -1, 0);
		results.expectTrue(bResult, genericFloat + "empty string");

		parse = null;
		bResult = Compare.equals(Conversion.parseFloat(parse), -1, 0);
		results.expectTrue(bResult, genericFloat + "null string");

		parse = "a1b";
		bResult = Compare.equals(Conversion.parseFloat(parse), -1, 0);
		results.expectTrue(bResult, genericFloat + parse);

		//
		// Parsing to float with default specified
		//
		parse = "0";
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultNegativeFloat), 0, 0);
		results.expectTrue(bResult, genericFloat + parse + " with default (" + defaultNegativeFloat + ")");

		parse = "0";
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultPositiveFloat), 0, 0);
		results.expectTrue(bResult, genericFloat + parse + " with default (" + defaultPositiveFloat + ")");

		parse = "01";
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultNegativeFloat), 1, 0);
		results.expectTrue(bResult, genericFloat + parse + " with default (" + defaultNegativeFloat + ")");

		parse = "01";
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultPositiveFloat), 1, 0);
		results.expectTrue(bResult, genericFloat + parse + " with default (" + defaultPositiveFloat + ")");

		positive = Rand.randomRange(10, 1000);
		parse = String.valueOf(positive);
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultNegativeFloat), positive, 0);
		results.expectTrue(bResult, genericFloat + parse);

		negative = Rand.randomRange(-1000, -10);
		parse = String.valueOf(negative);
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultPositiveFloat), negative, 0);
		results.expectTrue(bResult, genericFloat + parse);

		//
		// Parsing to float with default specified - failure
		//
		parse = "";
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultNegativeFloat), defaultNegativeFloat, 0);
		results.expectTrue(bResult, genericFloat + "empty string with default (" + defaultNegativeFloat + ")");

		parse = null;
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultNegativeFloat), defaultNegativeFloat, 0);
		results.expectTrue(bResult, genericFloat + "null string with default (" + defaultNegativeFloat + ")");

		parse = "a1b";
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultNegativeFloat), defaultNegativeFloat, 0);
		results.expectTrue(bResult, genericFloat + parse + " with default (" + defaultNegativeFloat + ")");

		parse = "";
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultPositiveFloat), defaultPositiveFloat, 0);
		results.expectTrue(bResult, genericFloat + "empty string with default (" + defaultPositiveFloat + ")");

		parse = null;
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultPositiveFloat), defaultPositiveFloat, 0);
		results.expectTrue(bResult, genericFloat + "null string with default (" + defaultPositiveFloat + ")");

		parse = "a1b";
		bResult = Compare.equals(Conversion.parseFloat(parse, defaultPositiveFloat), defaultPositiveFloat, 0);
		results.expectTrue(bResult, genericFloat + parse + " with default (" + defaultPositiveFloat + ")");

		//
		// Parsing to float - Objects - no default - success
		//
		parseObj.setParse_Int_Positive_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), Conversion.parseFloat(parse), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Int_Positive_True");

		parseObj.setParse_Int_Negative_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), Conversion.parseFloat(parse), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Int_Negative_True");

		parseObj.setParse_Float_Positive_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), Conversion.parseFloat(parse), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Float_Positive_True");

		parseObj.setParse_Float_Negative_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), Conversion.parseFloat(parse), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Float_Negative_True");

		float primitiveFloat = Rand.randomRange(0, 1000);
		bResult = Compare.equals(Conversion.parseFloat(primitiveFloat), primitiveFloat, 0);
		results.expectTrue(bResult, genericFloat + primitiveFloat + " primitive float");

		primitiveLong = Rand.randomRange(-10, -1000);
		bResult = Compare.equals(Conversion.parseFloat(primitiveFloat), primitiveFloat, 0);
		results.expectTrue(bResult, genericFloat + primitiveFloat + " primitive float");

		//
		// Parsing to float - Objects - default - success
		//
		parseObj.setParse_Int_Positive_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultNegativeFloat),
				Conversion.parseFloat(parse, defaultNegativeFloat), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " object with default (" + defaultNegativeFloat
				+ ")");

		parseObj.setParse_Int_Negative_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultPositiveFloat),
				Conversion.parseFloat(parse, defaultPositiveFloat), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " object with default (" + defaultPositiveFloat
				+ ")");

		parseObj.setParse_Float_Positive_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultNegativeFloat),
				Conversion.parseFloat(parse, defaultNegativeFloat), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " object with default (" + defaultNegativeFloat
				+ ")");

		parseObj.setParse_Float_Negative_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultPositiveFloat),
				Conversion.parseFloat(parse, defaultPositiveFloat), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " object with default (" + defaultPositiveFloat
				+ ")");

		parseObj.setParse_Int_Negative_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), Conversion.parseFloat(parse), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Int_Negative_True");

		parseObj.setParse_Int_Positive_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), Conversion.parseFloat(parse), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Int_Positive_True");

		parseObj.setParse_Float_Negative_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), Conversion.parseFloat(parse), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Float_Negative_True");

		parseObj.setParse_Float_Positive_True();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), Conversion.parseFloat(parse), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Float_Positive_True");

		primitiveFloat = Rand.randomRange(0, 1000);
		bResult = Compare.equals(Conversion.parseFloat(primitiveFloat, defaultNegativeFloat), primitiveFloat,
				0);
		results.expectTrue(bResult, genericFloat + parseObj + " primitive long with default ("
				+ defaultNegativeFloat + ")");

		primitiveFloat = Rand.randomRange(-10, -1000);
		bResult = Compare.equals(Conversion.parseFloat(primitiveFloat, defaultPositiveFloat), primitiveFloat,
				0);
		results.expectTrue(bResult, genericFloat + parseObj + " primitive long with default ("
				+ defaultPositiveFloat + ")");

		//
		// Parsing to float - Objects - no default - failure
		//
		parseObj.setParse_Float_Error();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), -1, 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Float_Error");

		parseObj.setParse_Null();
		parse = parseObj.toString();
		bResult = Compare.equals(Conversion.parseFloat(parseObj), Conversion.parseFloat(parse), 0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Null");

		bResult = Compare.equals(Conversion.parseFloat(parseNullObj), -1, 0);
		results.expectTrue(bResult, genericFloat + "null object");

		//
		// Parsing to float - Objects - default - failure
		//
		parseObj.setParse_Long_Error();
		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultPositiveFloat), defaultPositiveFloat,
				0);
		results.expectTrue(bResult, genericFloat + parseObj
				+ " from object setParse_Long_Error with default (" + defaultPositiveFloat + ")");

		parseObj.setParse_Long_Error();
		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultNegativeFloat), defaultNegativeFloat,
				0);
		results.expectTrue(bResult, genericFloat + parseObj
				+ " from object setParse_Long_Error with default (" + defaultNegativeFloat + ")");

		parseObj.setParse_Null();
		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultPositiveFloat), defaultPositiveFloat,
				0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Null with default ("
				+ defaultPositiveFloat + ")");

		parseObj.setParse_Null();
		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultNegativeFloat), defaultNegativeFloat,
				0);
		results.expectTrue(bResult, genericFloat + parseObj + " from object setParse_Null with default ("
				+ defaultNegativeFloat + ")");

		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultPositiveFloat), defaultPositiveFloat,
				0);
		results.expectTrue(bResult, genericFloat + "null object with default (" + defaultPositiveFloat + ")");

		bResult = Compare.equals(Conversion.parseFloat(parseObj, defaultNegativeFloat), defaultNegativeFloat,
				0);
		results.expectTrue(bResult, genericFloat + "null object with default (" + defaultNegativeFloat + ")");

		//
		//
		//

		results.verify("Some of the parsing tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runParsingTest");
	}

	@Test
	public static void runDateConversionTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runDateConversionTest");

		TestResults results = new TestResults();
		boolean bResult;
		String value, pattern, sWarning;
		TimeZone tz = TimeZone.getTimeZone("EST");

		// String to Date Test #1
		pattern = "MMMM dd, yyyy @ hh:mm a";
		value = "July 28, 2016 @ 10:10 AM";
		Date d1 = Conversion.toDate(value, pattern, tz, Languages.toLocale(Languages.English));

		value = "Juillet 28, 2016 @ 10:09 AM";
		Date d2 = Conversion.toDate(value, pattern, tz, Languages.toLocale(Languages.French));

		bResult = Compare.equals(d1, d2, 2);
		sWarning = "String to Date Test #1a";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals(d1, d2, 0);
		sWarning = "String to Date Test #1b";
		results.expectFalse(bResult, sWarning);

		// String to Date Test #2
		pattern = "MMM dd, yyyy @ hh:mm a";
		value = "Jul 28, 2016 @ 10:10 AM";
		Date d3 = Conversion.toDate(value, pattern, tz, Languages.toLocale(Languages.English));

		// Note: The key to parsing this is adding the period
		value = "Juil. 28, 2016 @ 10:09 AM";
		Date d4 = Conversion.toDate(value, pattern, tz, Languages.toLocale(Languages.French));

		bResult = Compare.equals(d3, d4, 2);
		sWarning = "String to Date Test #2a";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals(d3, d4, 0);
		sWarning = "String to Date Test #2b";
		results.expectFalse(bResult, sWarning);

		// String to Date Test #3
		pattern = "MMM dd, yyyy @ hh:mm a";
		value = "May 28, 2016 @ 10:10 AM";
		Date d5 = Conversion.toDate(value, pattern, tz, Languages.toLocale(Languages.English));

		value = "Mai 28, 2016 @ 10:09 AM";
		Date d6 = Conversion.toDate(value, pattern, tz, Languages.toLocale(Languages.French));

		bResult = Compare.equals(d5, d6, 2);
		sWarning = "String to Date Test #3a";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals(d5, d6, 0);
		sWarning = "String to Date Test #3b";
		results.expectFalse(bResult, sWarning);

		//
		// Verify dates are the same
		//

		bResult = Compare.equals(d1, d3, 0);
		sWarning = "Dates Equal Test #1";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals(d2, d4, 0);
		sWarning = "Dates Equal Test #2";
		results.expectTrue(bResult, sWarning);

		//
		// Verify dates that do not equal
		//

		bResult = Compare.equals(d1, d2, 0);
		sWarning = "Dates Not Equal Test #1";
		results.expectFalse(bResult, sWarning);

		bResult = Compare.equals(d3, d4, 0);
		sWarning = "Dates Not Equal Test #2";
		results.expectFalse(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runDateConversionTest");
	}

	@Test
	public static void runNonNullTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runNonNullTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning, convert;

		String empty = Conversion.nonNull("");
		String sNull = Conversion.nonNull(null);

		bResult = empty.equals(sNull);
		sWarning = "Empty == null Test failed";
		results.expectTrue(bResult, sWarning);

		bResult = empty.equals("");
		sWarning = "Empty String Test #1 failed";
		results.expectTrue(bResult, sWarning);

		bResult = empty == null;
		sWarning = "Empty String Test #2 failed";
		results.expectFalse(bResult, sWarning);

		bResult = sNull.equals("");
		sWarning = "Null Test #1 failed";
		results.expectTrue(bResult, sWarning);

		bResult = sNull == null;
		sWarning = "Null Test #2 failed";
		results.expectFalse(bResult, sWarning);

		// Single Letter Test #1
		String singleLetter = Rand.letters(1);
		convert = Conversion.nonNull(singleLetter);
		bResult = singleLetter.equals(convert);
		sWarning = "Single Letter Test #1 failed, details below:";
		if (!results.expectTrue(bResult, sWarning))
		{
			results.logWarn(sWarning);
			results.logWarn(singleLetter, convert);
			results.logWarn("");
		}

		// Single Number Test #1
		String singleNumber = Rand.numbers(1);
		convert = Conversion.nonNull(singleNumber);
		bResult = singleNumber.equals(convert);
		sWarning = "Single Number Test #1 failed, details below:";
		if (!results.expectTrue(bResult, sWarning))
		{
			results.logWarn(sWarning);
			results.logWarn(singleNumber, convert);
			results.logWarn("");
		}

		// Letters Test #1
		String letters = Rand.letters(2, 10);
		convert = Conversion.nonNull(letters);
		bResult = letters.equals(convert);
		sWarning = "Letters Test #1 failed, details below:";
		if (!results.expectTrue(bResult, sWarning))
		{
			results.logWarn(sWarning);
			results.logWarn(letters, convert);
			results.logWarn("");
		}

		// Numbers Test #1
		String numbers = Rand.numbers(2, 10);
		convert = Conversion.nonNull(numbers);
		bResult = numbers.equals(convert);
		sWarning = "Numbers Test #1 failed, details below:";
		if (!results.expectTrue(bResult, sWarning))
		{
			results.logWarn(sWarning);
			results.logWarn(numbers, convert);
			results.logWarn("");
		}

		// Alphanumeric Test #1
		String alphanumeric = Rand.alphanumeric(10, 20);
		convert = Conversion.nonNull(alphanumeric);
		bResult = alphanumeric.equals(convert);
		sWarning = "Alphanumeric Test #1 failed, details below:";
		if (!results.expectTrue(bResult, sWarning))
		{
			results.logWarn(sWarning);
			results.logWarn(alphanumeric, convert);
			results.logWarn("");
		}

		//
		// Object null Tests
		//
		Parameter nullObject = null;
		convert = Conversion.nonNull(nullObject);
		bResult = convert.equals("");
		sWarning = "Object null Test #1 failed";
		results.expectTrue(bResult, sWarning);

		bResult = convert == null;
		sWarning = "Object null Test #2 failed";
		results.expectFalse(bResult, sWarning);

		String replacement = Rand.alphanumeric(1, 10);
		convert = Conversion.nonNull(nullObject, replacement);
		bResult = convert.equals(replacement);
		sWarning = "Object null Test #3 failed";
		results.expectTrue(bResult, sWarning);

		bResult = convert == null;
		sWarning = "Object null Test #4 failed";
		results.expectFalse(bResult, sWarning);

		//
		// Objects non-null Tests
		//
		Parameter p1 = new Parameter(Rand.alphanumeric(1, 10), Rand.alphanumeric(1, 10));
		convert = Conversion.nonNull(p1);
		bResult = convert.equals(p1.toString());
		sWarning = "Object Test #1 failed, details below:";
		if (!results.expectTrue(bResult, sWarning))
		{
			results.logWarn(sWarning);
			results.logWarn(p1, convert);
			results.logWarn("");
		}

		bResult = convert.equals("");
		sWarning = "Object Test #2 failed, details below:";
		if (!results.expectFalse(bResult, sWarning))
		{
			results.logWarn(sWarning);
			results.logWarn(p1, convert);
			results.logWarn("");
		}

		Parameter p2 = new Parameter(Rand.alphanumeric(1, 10), Rand.alphanumeric(1, 10));
		replacement = Rand.alphanumeric(1, 10);
		convert = Conversion.nonNull(p2, replacement);
		bResult = convert.equals(p2.toString());
		sWarning = "Object Test #3 failed, details below:";
		if (!results.expectTrue(bResult, sWarning))
		{
			results.logWarn(sWarning);
			results.logWarn(p2, convert);
			results.logWarn("");
		}

		bResult = convert.equals(replacement);
		sWarning = "Object Test #4 failed, details below:";
		if (!results.expectFalse(bResult, sWarning))
		{
			results.logWarn(sWarning);
			results.logWarn(p2, convert);
			results.logWarn("");
		}

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runNonNullTest");
	}
}
