package com.automation.ui.common.tests;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Rand;
import com.automation.ui.common.utilities.StringMod;
import com.automation.ui.common.utilities.TestResults;

/**
 * This class is for unit testing the StringMod class
 */
public class StringModTest {
	@Test
	public static void runAppendTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runAppendTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		StringMod mod = new StringMod();
		String sAppend1 = Rand.alphanumeric(5, 10);
		mod.append(sAppend1);
		bResult = sAppend1.equals(mod.get());
		sWarning = "Append #1 failed";
		results.expectTrue(bResult, sWarning);

		String sAppend2 = Rand.alphanumeric(5, 10);
		String sAll = sAppend1 + sAppend2;
		mod.append(sAppend2);
		bResult = sAll.equals(mod.get());
		sWarning = "Append #2 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(sAppend1);
		bResult = sAppend1.equals(mod.get());
		sWarning = "Append #3 failed";
		results.expectTrue(bResult, sWarning);

		mod.append(sAppend2);
		bResult = sAll.equals(mod.get());
		sWarning = "Append #4 failed";
		results.expectTrue(bResult, sWarning);

		StringMod mod2 = new StringMod().append(sAppend1).append(sAppend2);
		bResult = mod2.equals(mod);
		sWarning = "Append #5 failed";
		results.expectTrue(bResult, sWarning);

		bResult = sAll.equals(mod2.get());
		sWarning = "Append #6 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runAppendTest");
	}

	@Test
	public static void runPrependTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrependTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		StringMod mod = new StringMod();
		String sPrepend1 = Rand.alphanumeric(5, 10);
		mod.prepend(sPrepend1);
		bResult = sPrepend1.equals(mod.get());
		sWarning = "Prepend #1 failed";
		results.expectTrue(bResult, sWarning);

		String sPrepend2 = Rand.alphanumeric(5, 10);
		String sAll = sPrepend2 + sPrepend1;
		mod.prepend(sPrepend2);
		bResult = sAll.equals(mod.get());
		sWarning = "Prepend #2 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(sPrepend1);
		bResult = sPrepend1.equals(mod.get());
		sWarning = "Prepend #3 failed";
		results.expectTrue(bResult, sWarning);

		mod.prepend(sPrepend2);
		bResult = sAll.equals(mod.get());
		sWarning = "Prepend #4 failed";
		results.expectTrue(bResult, sWarning);

		StringMod mod2 = new StringMod().prepend(sPrepend1).prepend(sPrepend2);
		bResult = mod2.equals(mod);
		sWarning = "Prepend #5 failed";
		results.expectTrue(bResult, sWarning);

		bResult = sAll.equals(mod2.get());
		sWarning = "Prepend #6 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runPrependTest");
	}

	@Test
	public static void runRemoveNonDigitsTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRemoveNonDigitsTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String onlyLetters = Rand.letters(5, 10);
		StringMod mod = new StringMod(onlyLetters);
		mod.removeNonDigits();
		bResult = mod.get().equals("");
		sWarning = "Non-Digits #1 failed";
		results.expectTrue(bResult, sWarning);

		String onlyDigits = Rand.numbers(5, 10);
		mod = new StringMod(onlyDigits);
		mod.removeNonDigits();
		bResult = mod.get().equals(onlyDigits);
		sWarning = "Non-Digits #2 failed";
		results.expectTrue(bResult, sWarning);

		String before = Rand.numbers(5, 10);
		String after = Rand.numbers(5, 10);
		String mix = before + onlyLetters + after;
		mod = new StringMod(mix);
		mod.removeNonDigits();
		bResult = mod.get().equals(before + after);
		sWarning = "Non-Digits #3 failed";
		results.expectTrue(bResult, sWarning);

		int size = 10;
		String onlyDigits2 = Rand.numbers(size);
		String onlyLeters2 = Rand.letters(size);
		StringBuilder alternate = new StringBuilder();
		for (int i = 0; i < size; i++)
		{
			alternate.append(onlyDigits2.charAt(i));
			alternate.append(onlyLeters2.charAt(i));
		}

		mod = new StringMod(alternate.toString());
		mod.removeNonDigits();
		bResult = mod.get().equals(onlyDigits2);
		sWarning = "Non-Digits #4 failed";
		results.expectTrue(bResult, sWarning);

		StringBuilder unknownOrder = new StringBuilder(onlyDigits2);
		for (int i = 0; i < size; i++)
		{
			unknownOrder.insert(Rand.randomRangeIndex(0, unknownOrder.length()), onlyLeters2.charAt(i));
		}

		mod = new StringMod(unknownOrder.toString());
		mod.removeNonDigits();
		bResult = mod.get().equals(onlyDigits2);
		sWarning = "Non-Digits #5 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRemoveNonDigitsTest");
	}

	@Test
	public static void runRemoveNonLettersTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRemoveNonLettersTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String onlyDigits = Rand.numbers(5, 10);
		StringMod mod = new StringMod(onlyDigits);
		mod.removeNonLetters();
		bResult = mod.get().equals("");
		sWarning = "Non-Letters #1 failed";
		results.expectTrue(bResult, sWarning);

		String onlyLetters = Rand.letters(5, 10);
		mod = new StringMod(onlyLetters);
		mod.removeNonLetters();
		bResult = mod.get().equals(onlyLetters);
		sWarning = "Non-Letters #2 failed";
		results.expectTrue(bResult, sWarning);

		String before = Rand.letters(5, 10);
		String after = Rand.letters(5, 10);
		String mix = before + onlyDigits + after;
		mod = new StringMod(mix);
		mod.removeNonLetters();
		bResult = mod.get().equals(before + after);
		sWarning = "Non-Letters #3 failed";
		results.expectTrue(bResult, sWarning);

		int size = 10;
		String onlyDigits2 = Rand.numbers(size);
		String onlyLeters2 = Rand.letters(size);
		StringBuilder alternate = new StringBuilder();
		for (int i = 0; i < size; i++)
		{
			alternate.append(onlyDigits2.charAt(i));
			alternate.append(onlyLeters2.charAt(i));
		}

		mod = new StringMod(alternate.toString());
		mod.removeNonLetters();
		bResult = mod.get().equals(onlyLeters2);
		sWarning = "Non-Letters #4 failed";
		results.expectTrue(bResult, sWarning);

		StringBuilder unknownOrder = new StringBuilder(onlyLeters2);
		for (int i = 0; i < size; i++)
		{
			unknownOrder.insert(Rand.randomRangeIndex(0, unknownOrder.length()), onlyDigits2.charAt(i));
		}

		mod = new StringMod(unknownOrder.toString());
		mod.removeNonLetters();
		bResult = mod.get().equals(onlyLeters2);
		sWarning = "Non-Letters #5 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRemoveNonLettersTest");
	}

	@Test
	public static void runRemoveNonAlphanumericTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRemoveNonAlphanumericTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String onlyInvalid = Rand.onlyChars(Rand.randomRange(5, 10), Rand.getOnlyExtendedLetters());
		StringMod mod = new StringMod(onlyInvalid);
		mod.removeNonAlphanumeric();
		bResult = mod.get().equals("");
		sWarning = "Non-Alpha #1 failed";
		results.expectTrue(bResult, sWarning);

		String onlyAlphanumeric = Rand.alphanumeric(5, 10);
		mod = new StringMod(onlyAlphanumeric);
		mod.removeNonAlphanumeric();
		bResult = mod.get().equals(onlyAlphanumeric);
		sWarning = "Non-Alpha #2 failed";
		results.expectTrue(bResult, sWarning);

		String before = Rand.alphanumeric(5, 10);
		String after = Rand.alphanumeric(5, 10);
		String mix = before + onlyInvalid + after;
		mod = new StringMod(mix);
		mod.removeNonAlphanumeric();
		bResult = mod.get().equals(before + after);
		sWarning = "Non-Alpha #3 failed";
		results.expectTrue(bResult, sWarning);

		int size = 10;
		String onlyInvalid2 = Rand.onlyChars(size, Rand.getOnlyExtendedLetters());
		String onlyAlphanumeric2 = Rand.alphanumeric(size);
		StringBuilder alternate = new StringBuilder();
		for (int i = 0; i < size; i++)
		{
			alternate.append(onlyInvalid2.charAt(i));
			alternate.append(onlyAlphanumeric2.charAt(i));
		}

		mod = new StringMod(alternate.toString());
		mod.removeNonAlphanumeric();
		bResult = mod.get().equals(onlyAlphanumeric2);
		sWarning = "Non-Alpha #4 failed";
		results.expectTrue(bResult, sWarning);

		StringBuilder unknownOrder = new StringBuilder(onlyAlphanumeric2);
		for (int i = 0; i < size; i++)
		{
			unknownOrder.insert(Rand.randomRangeIndex(0, unknownOrder.length()), onlyInvalid2.charAt(i));
		}

		mod = new StringMod(unknownOrder.toString());
		mod.removeNonAlphanumeric();
		bResult = mod.get().equals(onlyAlphanumeric2);
		sWarning = "Non-Alpha #5 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRemoveNonAlphanumericTest");
	}

	@Test
	public static void runEqualTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runEqualTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		StringMod actual = new StringMod("");
		StringMod expected = new StringMod(null);

		bResult = actual.equals(expected);
		sWarning = "Equal #1 failed";
		results.expectTrue(bResult, sWarning);

		bResult = actual.equalsIgnoreCase(expected);
		sWarning = "Equal #2 failed";
		results.expectTrue(bResult, sWarning);

		bResult = expected.equalsIgnoreCase(actual);
		sWarning = "Equal #3 failed";
		results.expectTrue(bResult, sWarning);

		String randomCase = Rand.alphanumeric(20, 30);
		String lowercase = randomCase.toLowerCase();
		String uppercase = randomCase.toUpperCase();

		actual = new StringMod(randomCase);
		expected = new StringMod(randomCase);

		bResult = expected.equals(actual);
		sWarning = "Equal #4 failed";
		results.expectTrue(bResult, sWarning);

		actual = new StringMod(randomCase);
		expected = new StringMod(lowercase);

		bResult = expected.equalsIgnoreCase(actual);
		sWarning = "Equal #5 failed";
		results.expectTrue(bResult, sWarning);

		bResult = actual.equalsIgnoreCase(expected);
		sWarning = "Equal #6 failed";
		results.expectTrue(bResult, sWarning);

		actual = new StringMod(randomCase);
		expected = new StringMod(uppercase);

		bResult = expected.equalsIgnoreCase(actual);
		sWarning = "Equal #7 failed";
		results.expectTrue(bResult, sWarning);

		bResult = actual.equalsIgnoreCase(expected);
		sWarning = "Equal #8 failed";
		results.expectTrue(bResult, sWarning);

		actual = new StringMod(lowercase);
		expected = new StringMod(uppercase);

		bResult = expected.equalsIgnoreCase(actual);
		sWarning = "Equal #9 failed";
		results.expectTrue(bResult, sWarning);

		bResult = actual.equalsIgnoreCase(expected);
		sWarning = "Equal #10 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runEqualTest");
	}

	@Test
	public static void runNotEqualTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runNotEqualTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String sActual = Rand.alphanumeric(5, 10);
		String sExpected = Rand.alphanumeric(11, 20);
		StringMod actual = new StringMod(sActual);
		StringMod expected = new StringMod(sExpected);

		bResult = actual.equals(expected);
		sWarning = "Not Equal #1 failed";
		results.expectFalse(bResult, sWarning);

		bResult = expected.equals(actual);
		sWarning = "Not Equal #2 failed";
		results.expectFalse(bResult, sWarning);

		bResult = actual.equalsIgnoreCase(expected);
		sWarning = "Not Equal #3 failed";
		results.expectFalse(bResult, sWarning);

		bResult = expected.equalsIgnoreCase(actual);
		sWarning = "Not Equal #4 failed";
		results.expectFalse(bResult, sWarning);

		bResult = actual.get().equals(sActual);
		sWarning = "No Mod #1 failed";
		results.expectTrue(bResult, sWarning);

		bResult = expected.get().equals(sExpected);
		sWarning = "No Mod #2 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runNotEqualTest");
	}

	@Test
	public static void runInsertTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runInsertTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String cat = "caT";
		String dogs = "dOGs";
		String elephant = "elePHant";

		String phrase = cat + dogs;
		StringMod mod = new StringMod(phrase);
		mod.insert(cat.length(), elephant);
		bResult = mod.get().equals(cat + elephant + dogs);
		sWarning = "Insert #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + dogs;
		mod = new StringMod(phrase);
		mod.insert(0, elephant);
		bResult = mod.get().equals(elephant + cat + dogs);
		sWarning = "Insert #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + dogs;
		mod = new StringMod(phrase);
		mod.insert(phrase.length(), elephant);
		bResult = mod.get().equals(cat + dogs + elephant);
		sWarning = "Insert #3 failed";
		results.expectTrue(bResult, sWarning);

		// Invalid
		phrase = mod.get();
		mod.insert(phrase.length() + 1, one);
		bResult = mod.get().equals(phrase);
		sWarning = "Insert #4 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runInsertTest");
	}

	@Test
	public static void runReplaceTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runReplaceTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";
		String elephant = "elePHant";
		String two = "two";

		// No matches
		String phrase = cat + dogs;
		StringMod mod = new StringMod(phrase);
		mod.replace(test, elephant);
		bResult = mod.get().equals(phrase);
		sWarning = "Replace #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + dogs;
		mod = new StringMod(phrase);
		mod.replace(cat.toLowerCase(), elephant);
		bResult = mod.get().equals(phrase);
		sWarning = "Replace #2 failed";
		results.expectTrue(bResult, sWarning);

		// 1 match
		phrase = cat + one + dogs;
		mod = new StringMod(phrase);
		mod.replace(one, two);
		bResult = mod.get().equals(cat + two + dogs);
		sWarning = "Replace #3 failed";
		results.expectTrue(bResult, sWarning);

		// 1 match replace with smaller string
		phrase = cat + elephant + dogs;
		mod = new StringMod(phrase);
		mod.replace(elephant, two);
		bResult = mod.get().equals(cat + two + dogs);
		sWarning = "Replace #4 failed";
		results.expectTrue(bResult, sWarning);

		// 1 match replace with larger string
		phrase = cat + one + dogs;
		mod = new StringMod(phrase);
		mod.replace(one, elephant);
		bResult = mod.get().equals(cat + elephant + dogs);
		sWarning = "Replace #5 failed";
		results.expectTrue(bResult, sWarning);

		// Multiple matches
		phrase = cat + one + dogs + one;
		mod = new StringMod(phrase);
		mod.replace(one, two);
		bResult = mod.get().equals(cat + two + dogs + two);
		sWarning = "Replace #6 failed";
		results.expectTrue(bResult, sWarning);

		// Multiple matches replace with smaller string
		phrase = elephant + cat + elephant + dogs;
		mod = new StringMod(phrase);
		mod.replace(elephant, two);
		bResult = mod.get().equals(two + cat + two + dogs);
		sWarning = "Replace #7 failed";
		results.expectTrue(bResult, sWarning);

		// Multiple matches with larger string
		phrase = cat + one + dogs + one;
		mod = new StringMod(phrase);
		mod.replace(one, elephant);
		bResult = mod.get().equals(cat + elephant + dogs + elephant);
		sWarning = "Replace #8 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runReplaceTest");
	}

	@Test
	public static void runRemoveTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRemoveTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";

		// No matches
		String phrase = cat + dogs;
		StringMod mod = new StringMod(phrase);
		mod.remove(test);
		bResult = mod.get().equals(phrase);
		sWarning = "Remove #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + dogs;
		mod = new StringMod(phrase);
		mod.remove(cat.toLowerCase());
		bResult = mod.get().equals(phrase);
		sWarning = "Remove #2 failed";
		results.expectTrue(bResult, sWarning);

		// 1 match
		phrase = cat + one + dogs;
		mod = new StringMod(phrase);
		mod.remove(one);
		bResult = mod.get().equals(cat + dogs);
		sWarning = "Remove #3 failed";
		results.expectTrue(bResult, sWarning);

		// Multiple matches
		phrase = cat + one + dogs + one;
		mod = new StringMod(phrase);
		mod.remove(one);
		bResult = mod.get().equals(cat + dogs);
		sWarning = "Remove #4 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRemoveTest");
	}

	@Test
	public static void runReplaceFirstRegExTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runReplaceFirstRegExTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";
		String elephant = "elePHant";
		String two = "two";

		// Replacement at end

		String phrase = one + test;
		StringMod mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, dogs);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceFirstRegEx #1 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		mod.replaceFirstRegEx(dogs.toUpperCase(), test);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceFirstRegEx #2 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, dogs);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceFirstRegEx #3 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement in middle

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, dogs);
		bResult = mod.get().equals(cat + dogs + one);
		sWarning = "ReplaceFirstRegEx #4 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at end - Smaller

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, two);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceFirstRegEx #5 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning - Smaller

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, two);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceFirstRegEx #6 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement in middle - Smaller

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, two);
		bResult = mod.get().equals(cat + two + one);
		sWarning = "ReplaceFirstRegEx #7 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at end - Larger

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, elephant);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceFirstRegEx #8 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning - Larger

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, elephant);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceFirstRegEx #9 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement in middle - Larger

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, elephant);
		bResult = mod.get().equals(cat + elephant + one);
		sWarning = "ReplaceFirstRegEx #10 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement at end

		phrase = one + test + test;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, dogs);
		bResult = mod.get().equals(one + dogs + test);
		sWarning = "ReplaceFirstRegEx #11 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement at beginning

		phrase = test + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, dogs);
		bResult = mod.get().equals(dogs + test + one);
		sWarning = "ReplaceFirstRegEx #12 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement in middle

		phrase = cat + test + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, dogs);
		bResult = mod.get().equals(cat + dogs + test + one);
		sWarning = "ReplaceFirstRegEx #13 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, dogs);
		bResult = mod.get().equals(cat + dogs + one + test);
		sWarning = "ReplaceFirstRegEx #14 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Smaller

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, two);
		bResult = mod.get().equals(cat + two + one + test);
		sWarning = "ReplaceFirstRegEx #15 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Larger

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceFirstRegEx(test, elephant);
		bResult = mod.get().equals(cat + elephant + one + test);
		sWarning = "ReplaceFirstRegEx #16 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runReplaceFirstRegExTest");
	}

	@Test
	public static void runRemoveFirstRegExTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRemoveFirstRegExTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";

		// Remove at end

		String phrase = one + test;
		StringMod mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirstRegEx #1 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		mod.removeFirstRegEx(dogs.toUpperCase());
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirstRegEx #2 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at beginning

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirstRegEx #3 failed";
		results.expectTrue(bResult, sWarning);

		// Remove in middle

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirstRegEx #4 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at end - Smaller

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirstRegEx #5 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at beginning - Smaller

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirstRegEx #6 failed";
		results.expectTrue(bResult, sWarning);

		// Remove in middle - Smaller

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirstRegEx #7 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at end - Larger

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirstRegEx #8 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at beginning - Larger

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirstRegEx #9 failed";
		results.expectTrue(bResult, sWarning);

		// Remove in middle - Larger

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirstRegEx #10 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Replacement at end

		phrase = one + test + test;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(one + test);
		sWarning = "RemoveFirstRegEx #11 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Replacement at beginning

		phrase = test + test + one;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(test + one);
		sWarning = "RemoveFirstRegEx #12 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Replacement in middle

		phrase = cat + test + test + one;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(cat + test + one);
		sWarning = "RemoveFirstRegEx #13 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Alternating replacement

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(cat + one + test);
		sWarning = "RemoveFirstRegEx #14 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Alternating replacement - Smaller

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(cat + one + test);
		sWarning = "RemoveFirstRegEx #15 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Alternating replacement - Larger

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeFirstRegEx(test);
		bResult = mod.get().equals(cat + one + test);
		sWarning = "RemoveFirstRegEx #16 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRemoveFirstRegExTest");
	}

	@Test
	public static void runReplaceFirstTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runReplaceFirstTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";
		String elephant = "elePHant";
		String two = "two";

		// Replacement at end

		String phrase = one + test.toUpperCase();
		StringMod mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceFirst #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceFirst #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceFirst #3 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		mod.replaceFirst(dogs.toUpperCase(), test, Comparison.Standard);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceFirst #4 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceFirst #5 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceFirst #6 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceFirst #7 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement in middle

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(cat + dogs + one);
		sWarning = "ReplaceFirst #8 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(cat + dogs + one);
		sWarning = "ReplaceFirst #9 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(cat + dogs + one);
		sWarning = "ReplaceFirst #10 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at end - Smaller

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Lower);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceFirst #11 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Upper);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceFirst #12 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Standard);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceFirst #13 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning - Smaller

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Lower);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceFirst #14 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Upper);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceFirst #15 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Standard);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceFirst #16 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement in middle - Smaller

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Lower);
		bResult = mod.get().equals(cat + two + one);
		sWarning = "ReplaceFirst #17 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Upper);
		bResult = mod.get().equals(cat + two + one);
		sWarning = "ReplaceFirst #18 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Standard);
		bResult = mod.get().equals(cat + two + one);
		sWarning = "ReplaceFirst #19 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at end - Larger

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceFirst #20 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceFirst #21 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceFirst #22 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning - Larger

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceFirst #23 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceFirst #24 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceFirst #25 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement in middle - Larger

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(cat + elephant + one);
		sWarning = "ReplaceFirst #26 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(cat + elephant + one);
		sWarning = "ReplaceFirst #27 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(cat + elephant + one);
		sWarning = "ReplaceFirst #28 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement at end

		phrase = one + test.toUpperCase() + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(one + dogs + test.toUpperCase());
		sWarning = "ReplaceFirst #29 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase() + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(one + dogs + test.toLowerCase());
		sWarning = "ReplaceFirst #30 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test + test;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(one + dogs + test);
		sWarning = "ReplaceFirst #31 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement at beginning

		phrase = test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(dogs + test.toUpperCase() + one);
		sWarning = "ReplaceFirst #32 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(dogs + test.toLowerCase() + one);
		sWarning = "ReplaceFirst #33 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(dogs + test + one);
		sWarning = "ReplaceFirst #34 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement in middle

		phrase = cat + test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(cat + dogs + test.toUpperCase() + one);
		sWarning = "ReplaceFirst #35 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(cat + dogs + test.toLowerCase() + one);
		sWarning = "ReplaceFirst #36 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + test + one;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(cat + dogs + test + one);
		sWarning = "ReplaceFirst #37 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(cat + dogs + one + test.toUpperCase());
		sWarning = "ReplaceFirst #38 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(cat + dogs + one + test.toLowerCase());
		sWarning = "ReplaceFirst #39 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(cat + dogs + one + test);
		sWarning = "ReplaceFirst #40 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Smaller

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Lower);
		bResult = mod.get().equals(cat + two + one + test.toUpperCase());
		sWarning = "ReplaceFirst #41 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Upper);
		bResult = mod.get().equals(cat + two + one + test.toLowerCase());
		sWarning = "ReplaceFirst #42 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, two, Comparison.Standard);
		bResult = mod.get().equals(cat + two + one + test);
		sWarning = "ReplaceFirst #43 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Larger

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(cat + elephant + one + test.toUpperCase());
		sWarning = "ReplaceFirst #44 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(cat + elephant + one + test.toLowerCase());
		sWarning = "ReplaceFirst #45 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceFirst(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(cat + elephant + one + test);
		sWarning = "ReplaceFirst #46 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runReplaceFirstTest");
	}

	@Test
	public static void runRemoveFirstTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRemoveFirstTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";

		// Remove at end

		String phrase = one + test.toUpperCase();
		StringMod mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #3 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		mod.removeFirst(dogs.toUpperCase(), Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #4 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at beginning

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #5 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #6 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #7 failed";
		results.expectTrue(bResult, sWarning);

		// Remove in middle

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirst #8 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirst #9 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirst #10 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at end - Smaller

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #11 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #12 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #13 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at beginning - Smaller

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #14 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #15 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #16 failed";
		results.expectTrue(bResult, sWarning);

		// Remove in middle - Smaller

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirst #17 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirst #18 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirst #19 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at end - Larger

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #20 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #21 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #22 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at beginning - Larger

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #23 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #24 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveFirst #25 failed";
		results.expectTrue(bResult, sWarning);

		// Remove in middle - Larger

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirst #26 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirst #27 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveFirst #28 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Replacement at end

		phrase = one + test.toUpperCase() + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(one + test.toUpperCase());
		sWarning = "RemoveFirst #29 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase() + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(one + test.toLowerCase());
		sWarning = "RemoveFirst #30 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test + test;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(one + test);
		sWarning = "RemoveFirst #31 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Replacement at beginning

		phrase = test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(test.toUpperCase() + one);
		sWarning = "RemoveFirst #32 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(test.toLowerCase() + one);
		sWarning = "RemoveFirst #33 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + test + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(test + one);
		sWarning = "RemoveFirst #34 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Replacement in middle

		phrase = cat + test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one);
		sWarning = "RemoveFirst #35 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one);
		sWarning = "RemoveFirst #36 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + test + one;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one);
		sWarning = "RemoveFirst #37 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Alternating replacement

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(cat + one + test.toUpperCase());
		sWarning = "RemoveFirst #38 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(cat + one + test.toLowerCase());
		sWarning = "RemoveFirst #39 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(cat + one + test);
		sWarning = "RemoveFirst #40 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Alternating replacement - Smaller

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(cat + one + test.toUpperCase());
		sWarning = "RemoveFirst #41 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(cat + one + test.toLowerCase());
		sWarning = "RemoveFirst #42 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(cat + one + test);
		sWarning = "RemoveFirst #43 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Alternating replacement - Larger

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Lower);
		bResult = mod.get().equals(cat + one + test.toUpperCase());
		sWarning = "RemoveFirst #44 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Upper);
		bResult = mod.get().equals(cat + one + test.toLowerCase());
		sWarning = "RemoveFirst #45 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeFirst(test, Comparison.Standard);
		bResult = mod.get().equals(cat + one + test);
		sWarning = "RemoveFirst #46 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRemoveFirstTest");
	}

	@Test
	public static void runReplaceLastTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runReplaceLastTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";
		String elephant = "elePHant";
		String two = "two";

		// Replacement at end

		String phrase = one + test.toUpperCase();
		StringMod mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceLast #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceLast #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceLast #3 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		mod.replaceLast(dogs.toUpperCase(), test, Comparison.Standard);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceLast #4 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceLast #5 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceLast #6 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceLast #7 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement in middle

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(cat + dogs + one);
		sWarning = "ReplaceLast #8 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(cat + dogs + one);
		sWarning = "ReplaceLast #9 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(cat + dogs + one);
		sWarning = "ReplaceLast #10 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at end - Smaller

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Lower);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceLast #11 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Upper);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceLast #12 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Standard);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceLast #13 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning - Smaller

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Lower);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceLast #14 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Upper);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceLast #15 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Standard);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceLast #16 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement in middle - Smaller

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Lower);
		bResult = mod.get().equals(cat + two + one);
		sWarning = "ReplaceLast #17 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Upper);
		bResult = mod.get().equals(cat + two + one);
		sWarning = "ReplaceLast #18 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Standard);
		bResult = mod.get().equals(cat + two + one);
		sWarning = "ReplaceLast #19 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at end - Larger

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceLast #20 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceLast #21 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceLast #22 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning - Larger

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceLast #23 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceLast #24 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceLast #25 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement in middle - Larger

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(cat + elephant + one);
		sWarning = "ReplaceLast #26 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(cat + elephant + one);
		sWarning = "ReplaceLast #27 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(cat + elephant + one);
		sWarning = "ReplaceLast #28 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement at end

		phrase = one + test.toUpperCase() + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(one + test.toUpperCase() + dogs);
		sWarning = "ReplaceLast #29 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase() + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(one + test.toLowerCase() + dogs);
		sWarning = "ReplaceLast #30 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test + test;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(one + test + dogs);
		sWarning = "ReplaceLast #31 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement at beginning

		phrase = test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(test.toUpperCase() + dogs + one);
		sWarning = "ReplaceLast #32 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(test.toLowerCase() + dogs + one);
		sWarning = "ReplaceLast #33 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + test + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(test + dogs + one);
		sWarning = "ReplaceLast #34 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement in middle

		phrase = cat + test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + dogs + one);
		sWarning = "ReplaceLast #35 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + dogs + one);
		sWarning = "ReplaceLast #36 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + test + one;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(cat + test + dogs + one);
		sWarning = "ReplaceLast #37 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one + dogs);
		sWarning = "ReplaceLast #38 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one + dogs);
		sWarning = "ReplaceLast #39 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceLast(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one + dogs);
		sWarning = "ReplaceLast #40 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Smaller

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one + two);
		sWarning = "ReplaceLast #41 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one + two);
		sWarning = "ReplaceLast #42 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceLast(test, two, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one + two);
		sWarning = "ReplaceLast #43 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Larger

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one + elephant);
		sWarning = "ReplaceLast #44 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one + elephant);
		sWarning = "ReplaceLast #45 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceLast(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one + elephant);
		sWarning = "ReplaceLast #46 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runReplaceLastTest");
	}

	@Test
	public static void runRemoveLastTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRemoveLastTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";

		// Remove at end

		String phrase = one + test.toUpperCase();
		StringMod mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #3 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		mod.removeLast(dogs.toUpperCase(), Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #4 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at beginning

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #5 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #6 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #7 failed";
		results.expectTrue(bResult, sWarning);

		// Remove in middle

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveLast #8 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveLast #9 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveLast #10 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at end - Smaller

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #11 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #12 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #13 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at beginning - Smaller

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #14 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #15 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #16 failed";
		results.expectTrue(bResult, sWarning);

		// Remove in middle - Smaller

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveLast #17 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveLast #18 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveLast #19 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at end - Larger

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #20 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #21 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #22 failed";
		results.expectTrue(bResult, sWarning);

		// Remove at beginning - Larger

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #23 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #24 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveLast #25 failed";
		results.expectTrue(bResult, sWarning);

		// Remove in middle - Larger

		phrase = cat + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveLast #26 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveLast #27 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(cat + one);
		sWarning = "RemoveLast #28 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Replacement at end

		phrase = one + test.toUpperCase() + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(one + test.toUpperCase());
		sWarning = "RemoveLast #29 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase() + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(one + test.toLowerCase());
		sWarning = "RemoveLast #30 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test + test;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(one + test);
		sWarning = "RemoveLast #31 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Replacement at beginning

		phrase = test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(test.toUpperCase() + one);
		sWarning = "RemoveLast #32 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(test.toLowerCase() + one);
		sWarning = "RemoveLast #33 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + test + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(test + one);
		sWarning = "RemoveLast #34 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Replacement in middle

		phrase = cat + test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one);
		sWarning = "RemoveLast #35 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one);
		sWarning = "RemoveLast #36 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + test + one;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one);
		sWarning = "RemoveLast #37 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Alternating replacement

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one);
		sWarning = "RemoveLast #38 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one);
		sWarning = "RemoveLast #39 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one);
		sWarning = "RemoveLast #40 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Alternating replacement - Smaller

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one);
		sWarning = "RemoveLast #41 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one);
		sWarning = "RemoveLast #42 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one);
		sWarning = "RemoveLast #43 failed";
		results.expectTrue(bResult, sWarning);

		// Remove - Alternating replacement - Larger

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one);
		sWarning = "RemoveLast #44 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one);
		sWarning = "RemoveLast #45 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeLast(test, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one);
		sWarning = "RemoveLast #46 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRemoveLastTest");
	}

	@Test
	public static void runReplaceEndsWithTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runReplaceEndsWithTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";
		String elephant = "elePHant";
		String two = "two";

		// Replacement

		String phrase = one + test.toUpperCase();
		StringMod mod = new StringMod(phrase);
		mod.replaceEndsWith(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceEndsWith #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceEndsWith #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceEndsWith #3 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		mod.replaceEndsWith(dogs.toUpperCase(), test, Comparison.Standard);
		bResult = mod.get().equals(one + dogs);
		sWarning = "ReplaceEndsWith #4 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement - Smaller

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, two, Comparison.Lower);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceEndsWith #5 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, two, Comparison.Upper);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceEndsWith #6 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, two, Comparison.Standard);
		bResult = mod.get().equals(one + two);
		sWarning = "ReplaceEndsWith #7 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement - Larger

		phrase = one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceEndsWith #8 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceEndsWith #9 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(one + elephant);
		sWarning = "ReplaceEndsWith #10 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement

		phrase = one + test.toUpperCase() + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(one + test.toUpperCase() + dogs);
		sWarning = "ReplaceEndsWith #11 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase() + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(one + test.toLowerCase() + dogs);
		sWarning = "ReplaceEndsWith #12 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test + test;
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(one + test + dogs);
		sWarning = "ReplaceEndsWith #13 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one + dogs);
		sWarning = "ReplaceEndsWith #14 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one + dogs);
		sWarning = "ReplaceEndsWith #15 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one + dogs);
		sWarning = "ReplaceEndsWith #16 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Smaller

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, two, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one + two);
		sWarning = "ReplaceEndsWith #17 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, two, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one + two);
		sWarning = "ReplaceEndsWith #18 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, two, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one + two);
		sWarning = "ReplaceEndsWith #19 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Larger

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one + elephant);
		sWarning = "ReplaceEndsWith #20 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one + elephant);
		sWarning = "ReplaceEndsWith #21 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.replaceEndsWith(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one + elephant);
		sWarning = "ReplaceEndsWith #22 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runReplaceEndsWithTest");
	}

	@Test
	public static void runRemoveEndsWithTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRemoveEndsWithTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";

		// Removal at beginning

		String phrase = one + test.toUpperCase();
		StringMod mod = new StringMod(phrase);
		mod.removeEndsWith(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveEndsWith #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeEndsWith(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveEndsWith #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test;
		mod = new StringMod(phrase);
		mod.removeEndsWith(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveEndsWith #3 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeEndsWith(test.toUpperCase(), Comparison.Standard);
		bResult = mod.get().equals(phrase);
		sWarning = "RemoveEndsWith #4 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Removal at beginning

		phrase = one + test.toUpperCase() + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeEndsWith(test, Comparison.Lower);
		bResult = mod.get().equals(one + test.toUpperCase());
		sWarning = "RemoveEndsWith #5 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test.toLowerCase() + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeEndsWith(test, Comparison.Upper);
		bResult = mod.get().equals(one + test.toLowerCase());
		sWarning = "RemoveEndsWith #6 failed";
		results.expectTrue(bResult, sWarning);

		phrase = one + test + test;
		mod = new StringMod(phrase);
		mod.removeEndsWith(test, Comparison.Standard);
		bResult = mod.get().equals(one + test);
		sWarning = "RemoveEndsWith #7 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating Removal

		phrase = cat + test.toUpperCase() + one + test.toUpperCase();
		mod = new StringMod(phrase);
		mod.removeEndsWith(test, Comparison.Lower);
		bResult = mod.get().equals(cat + test.toUpperCase() + one);
		sWarning = "RemoveEndsWith #8 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test.toLowerCase() + one + test.toLowerCase();
		mod = new StringMod(phrase);
		mod.removeEndsWith(test, Comparison.Upper);
		bResult = mod.get().equals(cat + test.toLowerCase() + one);
		sWarning = "RemoveEndsWith #9 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + test + one + test;
		mod = new StringMod(phrase);
		mod.removeEndsWith(test, Comparison.Standard);
		bResult = mod.get().equals(cat + test + one);
		sWarning = "RemoveEndsWith #10 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRemoveEndsWithTest");
	}

	@Test
	public static void runReplaceStartsWithTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runReplaceStartsWithTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";
		String dogs = "dOGs";
		String elephant = "elePHant";
		String two = "two";

		// Replacement at beginning

		String phrase = test.toUpperCase() + one;
		StringMod mod = new StringMod(phrase);
		mod.replaceStartsWith(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceStartsWith #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceStartsWith #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceStartsWith #3 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		mod.replaceStartsWith(dogs.toUpperCase(), test, Comparison.Standard);
		bResult = mod.get().equals(dogs + one);
		sWarning = "ReplaceStartsWith #4 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning - Smaller

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, two, Comparison.Lower);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceStartsWith #5 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, two, Comparison.Upper);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceStartsWith #6 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, two, Comparison.Standard);
		bResult = mod.get().equals(two + one);
		sWarning = "ReplaceStartsWith #7 failed";
		results.expectTrue(bResult, sWarning);

		// Replacement at beginning - Larger

		phrase = test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceStartsWith #8 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceStartsWith #9 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(elephant + one);
		sWarning = "ReplaceStartsWith #10 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Replacement at beginning

		phrase = test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(dogs + test.toUpperCase() + one);
		sWarning = "ReplaceStartsWith #11 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(dogs + test.toLowerCase() + one);
		sWarning = "ReplaceStartsWith #12 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + test + one;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(dogs + test + one);
		sWarning = "ReplaceStartsWith #13 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement

		phrase = test.toUpperCase() + one + test.toUpperCase() + cat;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, dogs, Comparison.Lower);
		bResult = mod.get().equals(dogs + one + test.toUpperCase() + cat);
		sWarning = "ReplaceStartsWith #14 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one + test.toLowerCase() + cat;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, dogs, Comparison.Upper);
		bResult = mod.get().equals(dogs + one + test.toLowerCase() + cat);
		sWarning = "ReplaceStartsWith #15 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one + test + cat;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, dogs, Comparison.Standard);
		bResult = mod.get().equals(dogs + one + test + cat);
		sWarning = "ReplaceStartsWith #16 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Smaller

		phrase = test.toUpperCase() + one + test.toUpperCase() + cat;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, two, Comparison.Lower);
		bResult = mod.get().equals(two + one + test.toUpperCase() + cat);
		sWarning = "ReplaceStartsWith #17 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one + test.toLowerCase() + cat;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, two, Comparison.Upper);
		bResult = mod.get().equals(two + one + test.toLowerCase() + cat);
		sWarning = "ReplaceStartsWith #18 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one + test + cat;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, two, Comparison.Standard);
		bResult = mod.get().equals(two + one + test + cat);
		sWarning = "ReplaceStartsWith #19 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating replacement - Larger

		phrase = test.toUpperCase() + one + test.toUpperCase() + cat;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, elephant, Comparison.Lower);
		bResult = mod.get().equals(elephant + one + test.toUpperCase() + cat);
		sWarning = "ReplaceStartsWith #20 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one + test.toLowerCase() + cat;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, elephant, Comparison.Upper);
		bResult = mod.get().equals(elephant + one + test.toLowerCase() + cat);
		sWarning = "ReplaceStartsWith #21 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one + test + cat;
		mod = new StringMod(phrase);
		mod.replaceStartsWith(test, elephant, Comparison.Standard);
		bResult = mod.get().equals(elephant + one + test + cat);
		sWarning = "ReplaceStartsWith #22 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runReplaceStartsWithTest");
	}

	@Test
	public static void runRemoveStartsWithTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runRemoveStartsWithTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String one = "oNe";
		String test = "Test";
		String cat = "caT";

		// Removal at beginning

		String phrase = test.toUpperCase() + one;
		StringMod mod = new StringMod(phrase);
		mod.removeStartsWith(test, Comparison.Lower);
		bResult = mod.get().equals(one);
		sWarning = "RemoveStartsWith #1 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeStartsWith(test, Comparison.Upper);
		bResult = mod.get().equals(one);
		sWarning = "RemoveStartsWith #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeStartsWith(test, Comparison.Standard);
		bResult = mod.get().equals(one);
		sWarning = "RemoveStartsWith #3 failed";
		results.expectTrue(bResult, sWarning);

		// No modifications
		phrase = test + one;
		mod = new StringMod(phrase);
		mod.removeStartsWith(test.toUpperCase(), Comparison.Standard);
		bResult = mod.get().equals(phrase);
		sWarning = "RemoveStartsWith #4 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Removal at beginning

		phrase = test.toUpperCase() + test.toUpperCase() + one;
		mod = new StringMod(phrase);
		mod.removeStartsWith(test, Comparison.Lower);
		bResult = mod.get().equals(test.toUpperCase() + one);
		sWarning = "RemoveStartsWith #5 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + test.toLowerCase() + one;
		mod = new StringMod(phrase);
		mod.removeStartsWith(test, Comparison.Upper);
		bResult = mod.get().equals(test.toLowerCase() + one);
		sWarning = "RemoveStartsWith #6 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + test + one;
		mod = new StringMod(phrase);
		mod.removeStartsWith(test, Comparison.Standard);
		bResult = mod.get().equals(test + one);
		sWarning = "RemoveStartsWith #7 failed";
		results.expectTrue(bResult, sWarning);

		// Repeating - Alternating Removal

		phrase = test.toUpperCase() + one + test.toUpperCase() + cat;
		mod = new StringMod(phrase);
		mod.removeStartsWith(test, Comparison.Lower);
		bResult = mod.get().equals(one + test.toUpperCase() + cat);
		sWarning = "RemoveStartsWith #8 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test.toLowerCase() + one + test.toLowerCase() + cat;
		mod = new StringMod(phrase);
		mod.removeStartsWith(test, Comparison.Upper);
		bResult = mod.get().equals(one + test.toLowerCase() + cat);
		sWarning = "RemoveStartsWith #9 failed";
		results.expectTrue(bResult, sWarning);

		phrase = test + one + test + cat;
		mod = new StringMod(phrase);
		mod.removeStartsWith(test, Comparison.Standard);
		bResult = mod.get().equals(one + test + cat);
		sWarning = "RemoveStartsWith #10 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runRemoveStartsWithTest");
	}

	@Test
	public static void runSubstringTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runSubstringTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String cat = "caT";
		String dogs = "dOGs";
		String elephant = "elePHant";

		String phrase = cat + dogs + elephant;
		StringMod mod = new StringMod(phrase);
		mod.substring(0);
		bResult = mod.get().equals(phrase);
		sWarning = "Substring #1 failed";
		results.expectTrue(bResult, sWarning);

		mod.substring(0, cat.length());
		bResult = mod.get().equals(cat);
		sWarning = "Substring #2 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + dogs + elephant;
		mod = new StringMod(phrase);
		mod.substring(cat.length());
		bResult = mod.get().equals(dogs + elephant);
		sWarning = "Substring #3 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + dogs + elephant;
		mod = new StringMod(phrase);
		mod.substring(cat.length(), cat.length() + dogs.length());
		bResult = mod.get().equals(dogs);
		sWarning = "Substring #4 failed";
		results.expectTrue(bResult, sWarning);

		phrase = cat + dogs + elephant;
		mod = new StringMod(phrase);
		mod.substring(cat.length() + dogs.length(), phrase.length());
		bResult = mod.get().equals(elephant);
		sWarning = "Substring #5 failed";
		results.expectTrue(bResult, sWarning);

		// Invalid (stored value not modified

		mod.substring(phrase.length());
		bResult = mod.get().equals(elephant);
		sWarning = "Substring #6 failed";
		results.expectTrue(bResult, sWarning);

		mod.substring(2, phrase.length());
		bResult = mod.get().equals(elephant);
		sWarning = "Substring #7 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runSubstringTest");
	}

	@Test
	public static void runTrimTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runTrimTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String spaces = "      ";
		String random = Rand.alphanumeric(5, 10);

		StringMod mod = new StringMod(random);
		mod.append(spaces);
		mod.prepend(spaces);

		bResult = mod.get().equals(spaces + random + spaces);
		sWarning = "Trim #1 failed";
		results.expectTrue(bResult, sWarning);

		mod.trim();
		bResult = mod.get().equals(random);
		sWarning = "Trim #2 failed";
		results.expectTrue(bResult, sWarning);

		String random2 = Rand.alphanumeric(1, 5);
		mod = new StringMod(spaces + random2 + spaces + random + spaces);
		mod.trim();
		bResult = mod.get().equals(random2 + spaces + random);
		sWarning = "Trim #3 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(spaces + random);
		mod.trim();
		bResult = mod.get().equals(random);
		sWarning = "Trim #4 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(random + spaces);
		mod.trim();
		bResult = mod.get().equals(random);
		sWarning = "Trim #5 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runTrimTest");
	}

	@Test
	public static void runTrimNonVisibleTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runTrimNonVisibleTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String spaces = "      ";
		String random = Rand.alphanumeric(5, 10);

		StringMod mod = new StringMod(random);
		mod.append(spaces);
		mod.prepend(spaces);

		bResult = mod.get().equals(spaces + random + spaces);
		sWarning = "Trim Non-Invisible #1 failed";
		results.expectTrue(bResult, sWarning);

		mod.trimNonVisible();
		bResult = mod.get().equals(random);
		sWarning = "Trim Non-Invisible #2 failed";
		results.expectTrue(bResult, sWarning);

		String random2 = Rand.alphanumeric(1, 5);
		mod = new StringMod(spaces + random2 + spaces + random + spaces);
		mod.trimNonVisible();
		bResult = mod.get().equals(random2 + spaces + random);
		sWarning = "Trim Non-Invisible #3 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(spaces + random);
		mod.trimNonVisible();
		bResult = mod.get().equals(random);
		sWarning = "Trim Non-Invisible #4 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(random + spaces);
		mod.trimNonVisible();
		bResult = mod.get().equals(random);
		sWarning = "Trim Non-Invisible #5 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runTrimNonVisibleTest");
	}

	@Test
	public static void runTrimAllTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runTrimAllTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		String spaces = "      ";
		String random = Rand.alphanumeric(5, 10);

		StringMod mod = new StringMod(random);
		mod.append(spaces);
		mod.prepend(spaces);

		bResult = mod.get().equals(spaces + random + spaces);
		sWarning = "Trim All #1 failed";
		results.expectTrue(bResult, sWarning);

		mod.trimAll();
		bResult = mod.get().equals(random);
		sWarning = "Trim All #2 failed";
		results.expectTrue(bResult, sWarning);

		String random2 = Rand.alphanumeric(1, 5);
		mod = new StringMod(spaces + random2 + spaces + random + spaces);
		mod.trimAll();
		bResult = mod.get().equals(random2 + spaces + random);
		sWarning = "Trim All #3 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(spaces + random);
		mod.trimAll();
		bResult = mod.get().equals(random);
		sWarning = "Trim All #4 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(random + spaces);
		mod.trimAll();
		bResult = mod.get().equals(random);
		sWarning = "Trim All #5 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runTrimAllTest");
	}

	@Test
	public static void runSplitTest()
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runSplitTest");

		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		// test suite : positions; test suite : positions
		String test1_name = "test1";
		String test1_positions = "1-3";
		String test1 = test1_name + ":" + test1_positions;
		String test2_name = "test2";
		String test2_positions = "2-8";
		String test2 = test2_name + ":" + test2_positions;
		String test3_name = "test3";
		String test3_positions = "8,15-20";
		String test3 = test3_name + ":" + test3_positions;
		String example = test1 + ";" + test2 + ";" + test3;

		StringMod mod = new StringMod(example);
		mod.split(";", 0);
		bResult = mod.get().equals(test1);
		sWarning = "Split #1 failed";
		results.expectTrue(bResult, sWarning);

		mod.split(":", 1);
		bResult = mod.get().equals(test1_positions);
		sWarning = "Split #2 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(example);
		mod.split(";", 1);
		bResult = mod.get().equals(test2);
		sWarning = "Split #3 failed";
		results.expectTrue(bResult, sWarning);

		mod.split(":", 0);
		bResult = mod.get().equals(test2_name);
		sWarning = "Split #4 failed";
		results.expectTrue(bResult, sWarning);

		mod = new StringMod(example);
		mod.split(";", 2);
		bResult = mod.get().equals(test3);
		sWarning = "Split #5 failed";
		results.expectTrue(bResult, sWarning);

		mod.split(":", 0);
		bResult = mod.get().equals(test3_name);
		sWarning = "Split #6 failed";
		results.expectTrue(bResult, sWarning);

		//

		mod = new StringMod(example);
		mod.split(";", 5);
		bResult = mod.get().equals(example);
		sWarning = "Split Invalid #1 failed";
		results.expectTrue(bResult, sWarning);

		mod.split("\\", 0);
		bResult = mod.get().equals(example);
		sWarning = "Split Invalid #2 failed";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("runSplitTest");
	}
}
