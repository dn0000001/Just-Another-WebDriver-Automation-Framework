package com.automation.ui.common.sampleProject.tests;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.validator.routines.checkdigit.ISINCheckDigit;
import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.sampleProject.dataStructures.CompanyNote;
import com.automation.ui.common.sampleProject.dataStructures.MathActionType;
import com.automation.ui.common.utilities.Accents;
import com.automation.ui.common.utilities.Cloner;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.ISIN;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Rand;
import com.automation.ui.common.utilities.TestResults;
import com.automation.ui.common.utilities.WS_Util;

/**
 * This class is for research that does not require a data provider
 */
public class ResearchTest2 {
	@Test
	public static void runSortEnumTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runSortEnumTest");

		// Determine min & max values for verification purposes
		int min1 = MathActionType.Operation.getOrderField1();
		int min2 = MathActionType.Operation.getOrderField2();
		int max1 = MathActionType.Operation.getOrderField1();
		int max2 = MathActionType.Operation.getOrderField2();
		for (MathActionType item : MathActionType.values())
		{
			if (item.getOrderField1() > max1)
				max1 = item.getOrderField1();

			if (item.getOrderField1() < min1)
				min1 = item.getOrderField1();

			if (item.getOrderField2() > max2)
				max2 = item.getOrderField2();

			if (item.getOrderField2() < min2)
				min2 = item.getOrderField2();
		}

		// Ensure min & max
		min1--;
		min2--;
		max1++;
		max2++;

		List<MathActionType> data = Arrays.asList(MathActionType.values());
		String method = "ascendingOrderField1";
		Compare.sort(data, method);
		Logs.log.info("ascendingOrderField1:  " + data);

		int order = min1;
		for (MathActionType item : data)
		{
			if (item.getOrderField1() > order)
			{
				order = item.getOrderField1();
			}
			else
			{
				Logs.logError("OrderField1 Item (" + item + ") was not greater than last order value ("
						+ order + ")");
			}
		}

		method = "descendingOrderField1";
		Compare.sort(data, method);
		Logs.log.info("descendingOrderField1:  " + data);

		order = max1;
		for (MathActionType item : data)
		{
			if (item.getOrderField1() < order)
			{
				order = item.getOrderField1();
			}
			else
			{
				Logs.logError("OrderField1 Item (" + item + ") was not less than last order value (" + order
						+ ")");
			}
		}

		method = "ascendingOrderField2";
		Compare.sort(data, method);
		Logs.log.info("ascendingOrderField2:  " + data);

		order = min2;
		for (MathActionType item : data)
		{
			if (item.getOrderField2() > order)
			{
				order = item.getOrderField2();
			}
			else
			{
				Logs.logError("OrderField2 Item (" + item + ") was not greater than last order value ("
						+ order + ")");
			}
		}

		method = "descendingOrderField2";
		Compare.sort(data, method);
		Logs.log.info("descendingOrderField2:  " + data);

		order = max2;
		for (MathActionType item : data)
		{
			if (item.getOrderField2() < order)
			{
				order = item.getOrderField2();
			}
			else
			{
				Logs.logError("OrderField2 Item (" + item + ") was not less than last order value (" + order
						+ ")");
			}
		}

		// Order not changed
		List<MathActionType> data2 = Cloner.deepClone(data);
		method = "unknown";
		Compare.sort(data2, method);
		Logs.log.info("unknown:  " + data2);

		if (data.size() != data2.size())
			Logs.logError("Data (" + data.size() + ") was not the same size as Data 2 (" + data2.size() + ")");

		for (int i = 0; i < data2.size(); i++)
		{
			if (data.get(i) != data2.get(i))
				Logs.logError("data[" + i + "] was not the same as data2[" + i + "]");
		}

		Controller.writeTestSuccessToLog("runSortEnumTest");
	}

	@Test
	public static void runSortObjectTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runSortObjectTest");

		// Create test data
		int nTestSize = 100;
		List<CompanyNote> testData = new ArrayList<CompanyNote>();
		for (int i = 0; i < nTestSize; i++)
		{
			int nID = Rand.randomRange(0, 10000);
			String Name = Rand.alphanumeric(5, 20);
			String NoteText = Rand.alphanumeric(5, 20);
			String CreatedDateString = Rand.alphanumeric(5, 20);
			String Createdby = Rand.alphanumeric(5, 20);
			CompanyNote item = new CompanyNote(nID, Name, NoteText, CreatedDateString, Createdby);
			testData.add(item);
		}

		Logs.log.info("Test Data:  " + testData);

		List<CompanyNote> ascendingIDs = Cloner.deepClone(testData);
		String method = "ascendingID";
		Compare.sort(ascendingIDs, method);
		Logs.log.info("ascendingID:  " + ascendingIDs);
		int order = ascendingIDs.get(0).ID;
		for (int i = 0; i < ascendingIDs.size(); i++)
		{
			if (ascendingIDs.get(i).ID >= order)
			{
				order = ascendingIDs.get(i).ID;
			}
			else
			{
				Logs.logError("ascendingID Item (" + ascendingIDs.get(i)
						+ ") was not equal to or greater than last order value (" + order + ")");
			}
		}

		List<CompanyNote> descendingIDs = Cloner.deepClone(testData);
		method = "descendingID";
		Compare.sort(descendingIDs, method);
		Logs.log.info("descendingID:  " + descendingIDs);
		order = descendingIDs.get(0).ID;
		for (int i = 0; i < descendingIDs.size(); i++)
		{
			if (descendingIDs.get(i).ID <= order)
			{
				order = descendingIDs.get(i).ID;
			}
			else
			{
				Logs.logError("descendingID Item (" + descendingIDs.get(i)
						+ ") was not less than or equal to last order value (" + order + ")");
			}
		}

		List<CompanyNote> ascendingNames = Cloner.deepClone(testData);
		method = "ascendingName";
		Compare.sort(ascendingNames, method);
		Logs.log.info("ascendingName:  " + ascendingNames);
		for (int i = 0; i < ascendingNames.size() - 1; i++)
		{
			int compare = ascendingNames.get(i).Name.compareTo(ascendingNames.get(i + 1).Name);
			if (compare > 0)
			{
				Logs.logError("ascendingName Item [" + i + "] (" + ascendingNames.get(i).Name
						+ ") was not equal to or greater than Item [" + (i + 1) + "] ("
						+ ascendingNames.get(i + 1).Name + "), compareTo=" + compare);
			}
		}

		List<CompanyNote> descendingNames = Cloner.deepClone(testData);
		method = "descendingName";
		Compare.sort(descendingNames, method);
		Logs.log.info("descendingName:  " + descendingNames);
		for (int i = 0; i < descendingNames.size() - 1; i++)
		{
			int compare = descendingNames.get(i).Name.compareTo(descendingNames.get(i + 1).Name);
			if (compare < 0)
			{
				Logs.logError("descendingName Item [" + i + "] (" + descendingNames.get(i).Name
						+ ") was not less than or equal to Item [" + (i + 1) + "] ("
						+ descendingNames.get(i + 1).Name + "), compareTo=" + compare);
			}
		}

		Controller.writeTestSuccessToLog("runSortObjectTest");
	}

	/**
	 * Creates a string with leading pad string (if necessary) to make specified total length<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only 1st character used for padding from padWith<BR>
	 * 2) If padWith is null or the empty string, then "0" is used to pad the string<BR>
	 * 3) If value is null, then converted to the empty string<BR>
	 * 4) If totalLength is less than the value length, then no padding occurs<BR>
	 * 
	 * @param value - The string to pad
	 * @param totalLength - The size of string you want to end with
	 * @param padWith - String to used for the padding
	 * @return String
	 */
	private static String pad(String value, int totalLength, String padWith)
	{
		String sProcess = Conversion.nonNull(value);
		String sLeadWith;
		if (padWith == null || padWith.equals(""))
			sLeadWith = "0";
		else
			sLeadWith = padWith.substring(0, 1);

		String sPadding = "";
		int nPaddingRequired = totalLength - sProcess.length();
		for (int i = 0; i < nPaddingRequired; i++)
		{
			sPadding += sLeadWith;
		}

		return sPadding + sProcess;
	}

	/**
	 * Converts an array of Unicode code points to a String
	 * 
	 * @param codePoints - Array of Unicode code points to be converted to a String
	 * @return empty string if code points array is null else string that contains all the code points
	 */
	private static String toString(int[] codePoints)
	{
		if (codePoints == null)
			return "";

		String combine = "";
		for (int i = 0; i < codePoints.length; i++)
		{
			combine += Conversion.toString(codePoints[i]);
		}

		return combine;
	}

	@Test
	public static void runRefactoringConversionTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runRefactoringConversionTest");

		int testSize = 100;
		int nMin = 1;
		int nMax = 10;
		String[] testData = new String[testSize];
		for (int i = 0; i < testSize; i++)
		{
			testData[i] = Rand.alphanumeric(nMin, nMax);
		}

		List<String> testData2 = Cloner.deepClone(Arrays.asList(testData));

		String td1 = Conversion.toString(", ", testData);
		String td2 = Conversion.toString(testData2, ", ");
		if (!Compare.equals(td1, td2, Comparison.Equal))
		{
			Logs.log.warn("td1:  " + td1);
			Logs.log.warn("td2:  " + td2);
			Logs.logError("td1 did not equal td2.  See above for details");
		}

		// Logs.log.info(td1);

		String[] paddingTestData = new String[] { "0", Rand.numbers(2), Rand.alphanumeric(2, 4) };
		for (int i = 0; i < paddingTestData.length; i++)
		{
			String padWith = paddingTestData[i];
			// Logs.log.info("Pad With:  " + padWith);

			for (int j = 0; j < 10; j++)
			{
				String sNumber = Rand.numbers(1, 5);
				int totalLength = Rand.randomRange(5, 10);

				// Logs.log.info("Length:  " + totalLength);

				String sOld = pad(sNumber, totalLength, padWith);
				String sNew = Conversion.pad(sNumber, totalLength, padWith);
				if (!Compare.equals(sOld, sNew, Comparison.Equal))
				{
					Logs.log.warn("sOld:  " + sOld);
					Logs.log.warn("sNew:  " + sNew);
					Logs.logError("sOld did not equal sNew.  See above for details");
				}

				// Logs.log.info(sNew);
				// Logs.log.info("");
			}

			// Logs.log.info("");
		}

		Field field_French = Accents.class.getDeclaredField("_French");
		field_French.setAccessible(true);
		int[] codePoints_French = (int[]) field_French.get(Accents.class);
		String sExpected_French = toString(codePoints_French);
		String sActual_French = Conversion.toString(codePoints_French);
		if (!Compare.equals(sExpected_French, sActual_French, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_French);
			Logs.log.warn("Actual:    " + sActual_French);
			Logs.logError("French - Expected did not equal Actual.  See above for details");
		}

		Field field_Czech = Accents.class.getDeclaredField("_Czech");
		field_Czech.setAccessible(true);
		int[] codePoints_Czech = (int[]) field_Czech.get(Accents.class);
		String sExpected_Czech = toString(codePoints_Czech);
		String sActual_Czech = Conversion.toString(codePoints_Czech);
		if (!Compare.equals(sExpected_Czech, sActual_Czech, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_Czech);
			Logs.log.warn("Actual:    " + sActual_Czech);
			Logs.logError("Czech - Expected did not equal Actual.  See above for details");
		}

		Field field_German = Accents.class.getDeclaredField("_German");
		field_German.setAccessible(true);
		int[] codePoints_German = (int[]) field_German.get(Accents.class);
		String sExpected_German = toString(codePoints_German);
		String sActual_German = Conversion.toString(codePoints_German);
		if (!Compare.equals(sExpected_German, sActual_German, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_German);
			Logs.log.warn("Actual:    " + sActual_German);
			Logs.logError("German - Expected did not equal Actual.  See above for details");
		}

		Field field_Hawaiian = Accents.class.getDeclaredField("_Hawaiian");
		field_Hawaiian.setAccessible(true);
		int[] codePoints_Hawaiian = (int[]) field_Hawaiian.get(Accents.class);
		String sExpected_Hawaiian = toString(codePoints_Hawaiian);
		String sActual_Hawaiian = Conversion.toString(codePoints_Hawaiian);
		if (!Compare.equals(sExpected_Hawaiian, sActual_Hawaiian, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_Hawaiian);
			Logs.log.warn("Actual:    " + sActual_Hawaiian);
			Logs.logError("Hawaiian - Expected did not equal Actual.  See above for details");
		}

		Field field_Italian = Accents.class.getDeclaredField("_Italian");
		field_Italian.setAccessible(true);
		int[] codePoints_Italian = (int[]) field_Italian.get(Accents.class);
		String sExpected_Italian = toString(codePoints_Italian);
		String sActual_Italian = Conversion.toString(codePoints_Italian);
		if (!Compare.equals(sExpected_Italian, sActual_Italian, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_Italian);
			Logs.log.warn("Actual:    " + sActual_Italian);
			Logs.logError("Italian - Expected did not equal Actual.  See above for details");
		}

		Field field_Polish = Accents.class.getDeclaredField("_Polish");
		field_Polish.setAccessible(true);
		int[] codePoints_Polish = (int[]) field_Polish.get(Accents.class);
		String sExpected_Polish = toString(codePoints_Polish);
		String sActual_Polish = Conversion.toString(codePoints_Polish);
		if (!Compare.equals(sExpected_Polish, sActual_Polish, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_Polish);
			Logs.log.warn("Actual:    " + sActual_Polish);
			Logs.logError("Polish - Expected did not equal Actual.  See above for details");
		}

		Field field_Romanian = Accents.class.getDeclaredField("_Romanian");
		field_Romanian.setAccessible(true);
		int[] codePoints_Romanian = (int[]) field_Romanian.get(Accents.class);
		String sExpected_Romanian = toString(codePoints_Romanian);
		String sActual_Romanian = Conversion.toString(codePoints_Romanian);
		if (!Compare.equals(sExpected_Romanian, sActual_Romanian, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_Romanian);
			Logs.log.warn("Actual:    " + sActual_Romanian);
			Logs.logError("Romanian - Expected did not equal Actual.  See above for details");
		}

		Field field_Russian = Accents.class.getDeclaredField("_Russian");
		field_Russian.setAccessible(true);
		int[] codePoints_Russian = (int[]) field_Russian.get(Accents.class);
		String sExpected_Russian = toString(codePoints_Russian);
		String sActual_Russian = Conversion.toString(codePoints_Russian);
		if (!Compare.equals(sExpected_Russian, sActual_Russian, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_Russian);
			Logs.log.warn("Actual:    " + sActual_Russian);
			Logs.logError("Russian - Expected did not equal Actual.  See above for details");
		}

		Field field_Spanish = Accents.class.getDeclaredField("_Spanish");
		field_Spanish.setAccessible(true);
		int[] codePoints_Spanish = (int[]) field_Spanish.get(Accents.class);
		String sExpected_Spanish = toString(codePoints_Spanish);
		String sActual_Spanish = Conversion.toString(codePoints_Spanish);
		if (!Compare.equals(sExpected_Spanish, sActual_Spanish, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_Spanish);
			Logs.log.warn("Actual:    " + sActual_Spanish);
			Logs.logError("Spanish - Expected did not equal Actual.  See above for details");
		}

		Field field_Turkish = Accents.class.getDeclaredField("_Turkish");
		field_Turkish.setAccessible(true);
		int[] codePoints_Turkish = (int[]) field_Turkish.get(Accents.class);
		String sExpected_Turkish = toString(codePoints_Turkish);
		String sActual_Turkish = Conversion.toString(codePoints_Turkish);
		if (!Compare.equals(sExpected_Turkish, sActual_Turkish, Comparison.Equal))
		{
			Logs.log.warn("Expected:  " + sExpected_Turkish);
			Logs.log.warn("Actual:    " + sActual_Turkish);
			Logs.logError("Turkish - Expected did not equal Actual.  See above for details");
		}

		// Logs.log.info("Spanish:  ");
		// for (int i = 0; i < codePoints_Spanish.length; i++)
		// {
		// Logs.log.info(codePoints_Spanish[i]);
		// }
		//
		// Logs.log.info("Turkish:  ");
		// for (int i = 0; i < codePoints_Turkish.length; i++)
		// {
		// Logs.log.info(codePoints_Turkish[i]);
		// }

		Controller.writeTestSuccessToLog("runRefactoringConversionTest");
	}

	@Test(enabled = false)
	public static void runBase64Test() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runBase64Test");

		String testData = Rand.alphanumeric(1, 100);
		Logs.log.info("String to be encoded:  " + testData);

		// encode data on your side using BASE64
		byte[] bytesEncoded = Base64.encodeBase64(testData.getBytes());
		String sEncoded = new String(bytesEncoded);
		Logs.log.info("Encoded value is " + sEncoded);

		// Decode data on other side, by processing encoded data
		byte[] valueDecoded = Base64.decodeBase64(bytesEncoded);
		String sDecoded = new String(valueDecoded);
		Logs.log.info("Decoded value is " + sDecoded);

		if (!testData.equals(sDecoded))
			Logs.logError("Data was not encoded/decoded properly");

		//
		// In JSON, normally sending a binary file is handled by encoding it into base64. This is an example
		// of how to do that.
		//

		String filename = "test-output/passed.png";
		// filename = "~lock.tmp";
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		byte[] binaryFile = new byte[(int) file.length()];
		fis.read(binaryFile);
		fis.close();
		bytesEncoded = Base64.encodeBase64(binaryFile);
		sEncoded = new String(bytesEncoded);
		Logs.log.info("Encoded file:  " + sEncoded);

		String sUtilEncode = WS_Util.encode(filename);
		if (sUtilEncode == null)
		{
			Logs.log.warn("Exception occurred encoding file");
		}
		else
		{
			if (sUtilEncode.equals(""))
				Logs.log.info("Encoded file was the empty string");

			if (!Compare.equals(sEncoded, sUtilEncode, Comparison.Equal))
				Logs.logError("There was an encoding failure");
		}

		String output = "output.txt";
		if (!WS_Util.decode(sEncoded, output))
			Logs.logError("Decoding and creating file were not successful");

		Controller.writeTestSuccessToLog("runBase64Test");
	}

	@Test
	public static void runComparisonTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runComparisonTest");

		String testValue = "aaa";

		String testNotEqual_True = "bbb";
		String testEqual_True = testValue;
		String testEqualIgnoreCase_True = "AAA";
		String testRegEx_True = ".a.";
		String testDoesNotContain_True = "ccc";
		String testContains_True = "a";

		String testNotEqual_False = testValue;
		String testEqual_False = "ddd";
		String testEqualIgnoreCase_False = "aBa";
		String testRegEx_False = ".ab";
		String testDoesNotContain_False = "a";
		String testContains_False = "b";

		Logs.log.info("Tests against:  " + testValue);

		TestResults results = new TestResults();

		boolean bResult = Compare.text(testValue, testNotEqual_True, Comparison.NotEqual);
		String generic = "Test:  " + testValue + " != " + testNotEqual_True;
		String pass = generic + " passed";
		String fail = generic + " failed";
		results.expectTrue(bResult, pass, fail);

		bResult = Compare.text(testValue, testEqual_True, Comparison.Equal);
		generic = "Test:  " + testValue + " == " + testEqual_True;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectTrue(bResult, pass, fail);

		bResult = Compare.text(testValue, testEqualIgnoreCase_True, Comparison.EqualsIgnoreCase);
		generic = "Test:  " + testValue + " == " + testEqualIgnoreCase_True;
		pass = generic + " passed (Ignore Case)";
		fail = generic + " failed (Ignore Case)";
		results.expectTrue(bResult, pass, fail);

		bResult = Compare.text(testValue, testRegEx_True, Comparison.RegEx);
		generic = "Test:  " + testValue + " RegEx " + testRegEx_True;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectTrue(bResult, pass, fail);

		bResult = Compare.text(testValue, testDoesNotContain_True, Comparison.DoesNotContain);
		generic = "Test:  " + testValue + " DoesNotContain " + testDoesNotContain_True;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectTrue(bResult, pass, fail);

		bResult = Compare.text(testValue, testContains_True, Comparison.Contains);
		generic = "Test:  " + testValue + " Contains " + testContains_True;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectTrue(bResult, pass, fail);

		bResult = Compare.text(testValue, testContains_True, Comparison.Standard);
		generic = "Test:  " + testValue + " Standard " + testContains_True;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectTrue(bResult, pass, fail);

		//

		bResult = Compare.text(testValue, testNotEqual_False, Comparison.NotEqual);
		generic = "Negative Test For " + testValue + " != " + testNotEqual_False;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectFalse(bResult, pass, fail);

		bResult = Compare.text(testValue, testEqual_False, Comparison.Equal);
		generic = "Negative Test For:  " + testValue + " == " + testEqual_False;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectFalse(bResult, pass, fail);

		bResult = Compare.text(testValue, testEqualIgnoreCase_False, Comparison.EqualsIgnoreCase);
		generic = "Negative Test For:  " + testValue + " == " + testEqualIgnoreCase_False;
		pass = generic + " passed (Ignore Case)";
		fail = generic + " failed (Ignore Case)";
		results.expectFalse(bResult, pass, fail);

		bResult = Compare.text(testValue, testRegEx_False, Comparison.RegEx);
		generic = "Negative Test For:  " + testValue + " RegEx " + testRegEx_False;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectFalse(bResult, pass, fail);

		bResult = Compare.text(testValue, testDoesNotContain_False, Comparison.DoesNotContain);
		generic = "Negative Test For:  " + testValue + " DoesNotContain " + testDoesNotContain_False;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectFalse(bResult, pass, fail);

		bResult = Compare.text(testValue, testContains_False, Comparison.Contains);
		generic = "Negative Test For:  " + testValue + " Contains " + testContains_False;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectFalse(bResult, pass, fail);

		bResult = Compare.text(testValue, testContains_False, Comparison.Standard);
		generic = "Negative Test For:  " + testValue + " Standard " + testContains_False;
		pass = generic + " passed";
		fail = generic + " failed";
		results.expectFalse(bResult, pass, fail);

		//

		pass = "All tests passed";
		fail = "One or more tests failed.  See above for details";
		results.verify(pass, fail);

		Controller.writeTestSuccessToLog("runComparisonTest");
	}

	@Test
	public static void runISIN_Test()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runISIN_Test");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		ISIN isin = new ISIN();
		String code = isin.random();

		bResult = code.length() == 12;
		sWarning = "ISIN was not correct size";
		if (!results.expectTrue(bResult, sWarning))
			results.logWarn(12, code.length());

		ISINCheckDigit icd = new ISINCheckDigit();
		bResult = icd.isValid(code);
		sWarning = "ISIN check digit was not valid:  " + code;
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runISIN_Test");
	}
}
