package com.automation.ui.common.tests;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Rand;
import com.automation.ui.common.utilities.TestResults;

/**
 * This class hold the unit tests for the Rand class
 */
public class RandTest {
	@Test
	public static void runEnumTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runEnumTest");
		int maxIterations = 1000;
		TestResults results = new TestResults();

		int nEN = 0;
		int nFR = 0;
		int nKey = 0;
		for (int i = 0; i < maxIterations; i++)
		{
			Languages randomLang = (Languages) Rand.randomEnum(Languages.English, 10000);
			if (randomLang == Languages.English)
				nEN++;
			else if (randomLang == Languages.French)
				nFR++;
			else if (randomLang == Languages.KEY)
				nKey++;

			if (nEN > 0 && nFR > 0 && nKey > 0)
				break;
		}

		results.expectTrue(nEN > 0, "Languages.English was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, 10000)");
		results.expectTrue(nFR > 0, "Languages.French was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, 10000)");
		results.expectTrue(nKey > 0, "Languages.KEY was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, 10000)");

		nEN = 0;
		nFR = 0;
		nKey = 0;
		for (int i = 0; i < maxIterations; i++)
		{
			Languages randomLang = (Languages) Rand.randomEnum(Languages.English);
			if (randomLang == Languages.English)
				nEN++;
			else if (randomLang == Languages.French)
				nFR++;
			else if (randomLang == Languages.KEY)
				nKey++;

			if (nEN > 0 && nFR > 0 && nKey > 0)
				break;
		}

		results.expectTrue(nEN > 0, "Languages.English was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English)");
		results.expectTrue(nFR > 0, "Languages.French was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English)");
		results.expectTrue(nKey > 0, "Languages.KEY was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English)");

		nEN = 0;
		nFR = 0;
		nKey = 0;
		for (int i = 0; i < maxIterations; i++)
		{
			Languages randomLang = (Languages) Rand.randomEnum(Languages.English, 10000, Languages.KEY);
			if (randomLang == Languages.English)
				nEN++;
			else if (randomLang == Languages.French)
				nFR++;
			else if (randomLang == Languages.KEY)
				nKey++;
		}

		results.expectTrue(nEN > 0, "Languages.English was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, 10000, Languages.KEY)");
		results.expectTrue(nFR > 0, "Languages.French was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, 10000, Languages.KEY)");
		results.expectTrue(nKey == 0, "Languages.KEY generated " + nKey + " times after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, 10000, Languages.KEY)");

		nEN = 0;
		nFR = 0;
		nKey = 0;
		for (int i = 0; i < maxIterations; i++)
		{
			Languages randomLang = (Languages) Rand.randomEnum(Languages.English, 10000, Languages.KEY,
					Languages.French);
			if (randomLang == Languages.English)
				nEN++;
			else if (randomLang == Languages.French)
				nFR++;
			else if (randomLang == Languages.KEY)
				nKey++;
		}

		results.expectTrue(nEN > 0, "Languages.English was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, 10000, Languages.KEY, Languages.French)");
		results.expectTrue(
				nFR == 0,
				"Languages.French generated "
						+ nFR
						+ " times after "
						+ maxIterations
						+ " iterations by Rand.randomEnum(Languages.English, 10000, Languages.KEY, Languages.French))");
		results.expectTrue(nKey == 0, "Languages.KEY generated " + nKey + " times after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, 10000, Languages.KEY, Languages.French)");

		nEN = 0;
		nFR = 0;
		nKey = 0;
		for (int i = 0; i < maxIterations; i++)
		{
			Languages randomLang = (Languages) Rand.randomEnum(Languages.English, Languages.KEY);
			if (randomLang == Languages.English)
				nEN++;
			else if (randomLang == Languages.French)
				nFR++;
			else if (randomLang == Languages.KEY)
				nKey++;
		}

		results.expectTrue(nEN > 0, "Languages.English was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, Languages.KEY)");
		results.expectTrue(nFR > 0, "Languages.French was never generated after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, Languages.KEY)");
		results.expectTrue(nKey == 0, "Languages.KEY generated " + nKey + " times after " + maxIterations
				+ " iterations by Rand.randomEnum(Languages.English, Languages.KEY)");

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runEnumTest");
	}

	@Test
	public static void runRandomizeBasicTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRandomizeBasicTest");

		TestResults results = new TestResults();

		String sNull = Rand.randomize(null);
		results.expectTrue(sNull.equals(""), "Null Test Failed");

		String sEmpty = Rand.randomize("");
		results.expectTrue(sEmpty.equals(""), "Empty Test Failed");

		String sLength1 = Rand.randomize(Rand.letters(1));
		results.expectTrue(sLength1.length() == 1, "String Length 1 test failed");

		String sLength2 = Rand.randomize(Rand.letters(2));
		results.expectTrue(sLength2.length() == 2, "String Length 2 test failed");

		String initial = Rand.letters(10, 20);
		String modified = Rand.randomize(initial);
		results.expectTrue(initial.length() == modified.length(), "Random String Length test failed");

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runRandomizeBasicTest");
	}

	@Test
	public static void runRandomizeEqualLengthTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRandomizeEqualLengthTest");

		int runs = 1000;
		TestResults results = new TestResults();

		for (int i = 0; i < runs; i++)
		{
			String initial = Rand.letters(5, 10);
			String modified = Rand.randomize(initial);

			// Debug Information
			// results.logInfo(initial + " => " + modified);

			if (!results.expectTrue(initial.length() == modified.length()))
			{
				results.logWarn("Initial:   " + initial);
				results.logWarn("Modified:  " + modified);
				results.logWarn("");
			}
		}

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRandomizeEqualLengthTest");
	}

	@Test
	public static void runRandomizeDupTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRandomizeDupTest");

		int allowDupPercentage = 5;
		int runs = 1000;

		TestResults results = new TestResults();
		results.setMethod_Percent();
		results.setLimit(allowDupPercentage);

		for (int i = 0; i < runs; i++)
		{
			String initial = Rand.letters(5, 10);
			String modified = Rand.randomize(initial);
			results.expectFalse(initial.equals(modified));
		}

		// Debug Information
		// results.logInfo("Duplicate Percentage:  " + results.getFailurePercent());
		// results.logInfo("Unique Percentage:     " + results.getPassPercent());

		results.verify("The duplicate percentage (" + results.getFailurePercent()
				+ ")  was above the allowed percentage (" + allowDupPercentage + ")");

		Controller.writeTestSuccessToLog("runRandomizeDupTest");
	}

	@Test
	public static void runUniqueTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runUniqueTest");

		int runs = 1000;
		TestResults results = new TestResults();

		String sNull = Rand.uniqueAlphanumeric(null);
		results.expectTrue(sNull == null, "Null Test Alphanumeric #1 failed");

		sNull = Rand.uniqueLetters(null);
		results.expectTrue(sNull == null, "Null Test Letters #1 failed");

		sNull = Rand.uniqueNumbers(null);
		results.expectTrue(sNull == null, "Null Test Numbers #1 failed");

		sNull = Rand.uniqueAlphanumeric(null, 2);
		results.expectTrue(sNull == null, "Null Test Alphanumeric #2 failed");

		sNull = Rand.uniqueLetters(null, 2);
		results.expectTrue(sNull == null, "Null Test Letters #3 failed");

		sNull = Rand.uniqueNumbers(null, 2);
		results.expectTrue(sNull == null, "Null Test Numbers #3 failed");

		sNull = Rand.uniqueAlphanumeric("");
		results.expectTrue(sNull == null, "Null Test Alphanumeric #3 failed");

		sNull = Rand.uniqueLetters("");
		results.expectTrue(sNull == null, "Null Test Letters #3 failed");

		sNull = Rand.uniqueNumbers("");
		results.expectTrue(sNull == null, "Null Test Numbers #3 failed");

		sNull = Rand.uniqueAlphanumeric("", 2);
		results.expectTrue(sNull == null, "Null Test Alphanumeric #4 failed");

		sNull = Rand.uniqueLetters("", 2);
		results.expectTrue(sNull == null, "Null Test Letters #4 failed");

		sNull = Rand.uniqueNumbers("", 2);
		results.expectTrue(sNull == null, "Null Test Numbers #4 failed");

		String value = Rand.alphanumeric(1);
		String generated = Rand.uniqueAlphanumeric(value);
		int nValue = value.length();
		int nLength;
		try
		{
			nLength = generated.length();
		}
		catch (Exception ex)
		{
			nLength = -1;
		}

		results.expectTrue(nValue == nLength, "Alphanumeric - Value length (" + nValue
				+ ") did not match generated string length (" + nLength + ")");

		value = Rand.letters(1);
		generated = Rand.uniqueLetters(value);
		nValue = value.length();
		try
		{
			nLength = generated.length();
		}
		catch (Exception ex)
		{
			nLength = -1;
		}

		results.expectTrue(nValue == nLength, "Letters Only - Value length (" + nValue
				+ ") did not match generated string length (" + nLength + ")");

		value = Rand.numbers(1);
		generated = Rand.uniqueNumbers(value);
		nValue = value.length();
		try
		{
			nLength = generated.length();
		}
		catch (Exception ex)
		{
			nLength = -1;
		}

		results.expectTrue(nValue == nLength, "Numbers Only - Value length (" + nValue
				+ ") did not match generated string length (" + nLength + ")");

		for (int i = 0; i < runs; i++)
		{
			value = Rand.alphanumeric(1, 10);
			generated = Rand.uniqueAlphanumeric(value);
			nValue = value.length();
			try
			{
				nLength = generated.length();
			}
			catch (Exception ex)
			{
				nLength = -1;
			}

			// Debug Information
			// results.logInfo(value + " => " + generated);

			results.expectTrue(nValue == nLength, "Alphanumeric - Value length (" + nValue
					+ ") did not match generated string length (" + nLength + ")");
			results.expectFalse(Compare.equals(value, generated, Comparison.Equal), "Alphanumeric - Value ("
					+ value + ") generated a duplicate value (" + generated + ")");

			value = Rand.letters(1, 10);
			generated = Rand.uniqueLetters(value);
			nValue = value.length();
			try
			{
				nLength = generated.length();
			}
			catch (Exception ex)
			{
				nLength = -1;
			}

			// Debug Information
			// results.logInfo(value + " => " + generated);

			results.expectTrue(nValue == nLength, "Letters Only - Value length (" + nValue
					+ ") did not match generated string length (" + nLength + ")");
			results.expectFalse(Compare.equals(value, generated, Comparison.Equal), "Letters Only - Value ("
					+ value + ") generated a duplicate value (" + generated + ")");

			value = Rand.numbers(1, 10);
			generated = Rand.uniqueNumbers(value);
			nValue = value.length();
			try
			{
				nLength = generated.length();
			}
			catch (Exception ex)
			{
				nLength = -1;
			}

			// Debug Information
			// results.logInfo(value + " => " + generated);

			results.expectTrue(nValue == nLength, "Numbers Only - Value length (" + nValue
					+ ") did not match generated string length (" + nLength + ")");
			results.expectFalse(Compare.equals(value, generated, Comparison.Equal), "Numbers Only - Value ("
					+ value + ") generated a duplicate value (" + generated + ")");
		}

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runUniqueTest");
	}
}
