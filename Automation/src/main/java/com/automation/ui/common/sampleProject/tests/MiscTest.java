package com.automation.ui.common.sampleProject.tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.CryptoDESede;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Lookup;
import com.automation.ui.common.utilities.Misc;
import com.automation.ui.common.utilities.Rand;

/**
 * This class hold the unit tests for the Misc class
 */
public class MiscTest {
	@Test
	public static void unitTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();

		String sProblem = "^$.|?*\\+([{)}]";

		List<Parameter> items = new ArrayList<Parameter>();
		items.add(new Parameter("REPLACE", Misc.escapeForRegEx(sProblem)));
		items.add(new Parameter("REPLACE", Misc.escapeForRegEx(sProblem)));
		items.add(new Parameter("REPLACE", Misc.escapeForRegEx("\\")));
		items.add(new Parameter("REPLACE", Misc.escapeForRegEx("$")));
		items.add(new Parameter("REPLACE", Misc.escapeForRegEx("?")));
		Logs.log.info(Misc.replace1stMatch("aaaREPLACEbbbREPLACEcccREPLACEdddREPLACEeeeREPLACEfff", items));
		Logs.log.info("");

		Logs.log.info(Rand.replaceWithRandomizedTokens(sProblem + "{RANDOM_RANGE(-50,50)}aa"));
		Logs.log.info(Rand.replaceWithRandomizedTokens(sProblem + "{RANDOM_RANGE(-50,50)}" + sProblem));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_RANGE(-50,50)}" + sProblem));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_BOOLEAN}" + " " + "{RANDOM_BOOLEAN}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_NUM=3}" + " " + "{RANDOM_NUM=3}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_ALPHA=3}" + " " + "{RANDOM_ALPHA=3}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_BOOLEAN}" + " " + "{RANDOM_NUM=3}" + " "
				+ "{RANDOM_ALPHA=3}" + " " + "{RANDOM_BOOLEAN}" + " " + "{RANDOM_NUM=3}" + " "
				+ "{RANDOM_ALPHA=3}"));

		Logs.log.info("");
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_RANGE(-5,5)}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_RANGE(5,-5)}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_RANGE(-5,5)}z"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_RANGE(5,-5)}z"));
		Logs.log.info("");
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_RANGE(1,12)}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_RANGE(1,12)}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_RANGE(1,12)}z"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_RANGE(1,12)}z"));
		Logs.log.info("");
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_BOOLEAN}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_BOOLEAN}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_BOOLEAN}z"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_BOOLEAN}z"));
		Logs.log.info("");
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_PASSWORD=8}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_PASSWORD=8}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_PASSWORD=8}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_PASSWORD=8}"));
		Logs.log.info("");
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_PASSWORD_ALL=8}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_PASSWORD_ALL=8}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_PASSWORD_ALL=8}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_PASSWORD_ALL=8}"));
		Logs.log.info("");
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_NUM=3}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_NUM=7}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_NUM=5}z"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_NUM=6}z"));
		Logs.log.info("");
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_ALPHA=3}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_ALPHA=7}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_ALPHA=5}z"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("a{RANDOM_ALPHA=6}z"));
		Logs.log.info("");
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_UPPERCASE=5}-{RANDOM_LOWERCASE=3}"));
		Logs.log.info("");

		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_ALPHA=5}, " + "{RANDOM_LETTERS=5}, "
				+ "{RANDOM_NUM=5}, " + "{RANDOM_UPPERCASE=5}, " + "{RANDOM_LOWERCASE=5}, "
				+ "{RANDOM_SPECIAL=5}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_ALPHA=5}, " + "{RANDOM_LETTERS=4}, "
				+ "{RANDOM_NUM=3}, " + "{RANDOM_UPPERCASE=2}, " + "{RANDOM_LOWERCASE=1}, "
				+ "{RANDOM_SPECIAL=6}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_ALPHA=5}{RANDOM_LETTERS=5}, "
				+ "{RANDOM_NUM=5}{RANDOM_UPPERCASE=5}, " + "{RANDOM_LOWERCASE=5}{RANDOM_SPECIAL=5}"
				+ "  REPEATS:  {RANDOM_ALPHA=5}{RANDOM_LETTERS=5}, " + "{RANDOM_NUM=5}{RANDOM_UPPERCASE=5}, "
				+ "{RANDOM_LOWERCASE=5}{RANDOM_SPECIAL=5}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_ALPHA=5}{RANDOM_LETTERS=5}, "
				+ "{RANDOM_NUM=5}{RANDOM_UPPERCASE=5}, " + "{RANDOM_LOWERCASE=5}{RANDOM_SPECIAL=5}"
				+ "  NEXT SET:  {RANDOM_ALPHA=3}{RANDOM_LETTERS=3}, "
				+ "{RANDOM_NUM=3}{RANDOM_UPPERCASE=3}, " + "{RANDOM_LOWERCASE=3}{RANDOM_SPECIAL=3}"));
		Logs.log.info(Rand.replaceWithRandomizedTokens("{RANDOM_ALPHA=1}{RANDOM_LETTERS=1}, "
				+ "{RANDOM_NUM=1}{RANDOM_UPPERCASE=1}, " + "{RANDOM_LOWERCASE=1}{RANDOM_SPECIAL=1}"
				+ "  NEXT SET:  {RANDOM_ALPHA=11}{RANDOM_LETTERS=11}, "
				+ "{RANDOM_NUM=11}{RANDOM_UPPERCASE=11}, " + "{RANDOM_LOWERCASE=11}{RANDOM_SPECIAL=11}"));
		Logs.log.info("");

		/*
		 * Random string generation tests
		 */
		int nLength = 8;
		for (int i = 0; i < nLength; i++)
		{
			Logs.log.info("Random String of length (" + nLength + "):  " + RandomStringUtils.random(nLength));
			Logs.log.info("Random Alphabetic String of length (" + nLength + "):  "
					+ RandomStringUtils.randomAlphabetic(nLength));
			Logs.log.info("Random Alphanumeric String of length (" + nLength + "):  "
					+ RandomStringUtils.randomAlphanumeric(nLength));
			Logs.log.info("Random Numeric String of length (" + nLength + "):  "
					+ RandomStringUtils.randomNumeric(nLength));
			Logs.log.info("Random String of length (" + nLength + ") that always starts with a letter:  "
					+ RandomStringUtils.randomAlphabetic(1)
					+ RandomStringUtils.randomAlphanumeric(nLength - 1));
			Logs.log.info("Random String of length (" + nLength
					+ ") that always contains an uppercase letter:  "
					+ Rand.random(nLength, true, false, false, false));
			Logs.log.info("Random String of length (" + nLength
					+ ") that always contains a lowercase letter:  "
					+ Rand.random(nLength, false, true, false, false));
			Logs.log.info("Random String of length (" + nLength + ") that always contains a number:  "
					+ Rand.random(nLength, false, false, true, false));
			Logs.log.info("Random String of length (" + nLength
					+ ") that always contains a special character:  "
					+ Rand.random(nLength, false, false, false, true));
			Logs.log.info("Random String of length (" + nLength
					+ ") that always contains an uppercase, lowercase, number & special character:  "
					+ Rand.random(nLength, true, true, true, true));
			Logs.log.info("Random String of length (4) that always contains an uppercase, lowercase, number & special character:  "
					+ Rand.random(1, true, true, true, true));
			Logs.log.info("Random String of length (1):  " + Rand.random(-1, false, false, false, false));
			Logs.log.info("Random String of length (3) that always contains an uppercase, number & special character:  "
					+ Rand.random(1, true, false, true, true));
			Logs.log.info("Random String of length (3) that always contains a lowercase, number & special character:  "
					+ Rand.random(1, false, true, true, true));
			Logs.log.info("Random String of length (3) that always contains an uppercase, lowercase & special character:  "
					+ Rand.random(1, true, true, false, true));
			Logs.log.info("Random String of length (3) that always contains an uppercase, lowercase & number:  "
					+ Rand.random(1, true, true, true, false));

			Rand.setUsedSpecial("&#");
			Logs.log.info("Special character set to \"&#\"");
			Logs.log.info("Random String of length (3) that always contains an uppercase, number & special character:  "
					+ Rand.random(1, true, false, true, true));
			Logs.log.info("Random String of length (3) that always contains a lowercase, number & special character:  "
					+ Rand.random(1, false, true, true, true));
			Logs.log.info("Random String of length (3) that always contains an uppercase, lowercase & special character:  "
					+ Rand.random(1, true, true, false, true));
			Logs.log.info("Random String of length (3) that always contains an uppercase, lowercase & number:  "
					+ Rand.random(1, true, true, true, false));
			Rand.resetUsedSpecialToDefaults();
			Logs.log.info("Special characters reset");
			Logs.log.info("");
		}

		Logs.log.info("");

		int[] mismatchRow = new int[] { -2 };
		int[] mismatchColumn = new int[] { -2 };

		mismatchRow[0] = -2;
		mismatchColumn[0] = -2;
		String[][] test1 = new String[][] { { "a", "1" }, { "b", "2" } };
		String[][] test2 = new String[][] { { "a", "1" }, { "b", "2" } };
		if (Compare.equal(test1, test2, mismatchRow, mismatchColumn))
			Logs.log.info("Arrays were equal as expected");
		else
		{
			Logs.log.error("Row:  " + mismatchRow[0] + "; Column:  " + mismatchColumn[0]);
			throw new GenericUnexpectedException("");
		}

		mismatchRow[0] = -2;
		mismatchColumn[0] = -2;
		test1 = new String[][] { { "a", "1" }, { "b", "2" } };
		test2 = new String[][] { { "a", "1" }, { "d", "2" } };
		if (Compare.equal(test1, test2, mismatchRow, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but there should have been a mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Row:  " + mismatchRow[0] + "; Column:  " + mismatchColumn[0]);

		mismatchRow[0] = -2;
		mismatchColumn[0] = -2;
		test1 = new String[][] { { "a", "1" }, { "b", "2" }, { "c", "3" } };
		test2 = new String[][] { { "a", "1" }, { "b", "2" } };
		if (Compare.equal(test1, test2, mismatchRow, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but there should have been a mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Row:  " + mismatchRow[0] + "; Column:  " + mismatchColumn[0]);

		mismatchRow[0] = -2;
		mismatchColumn[0] = -2;
		test1 = new String[][] { { "a", "1", "a1" }, { "b", "2", "b2" } };
		test2 = new String[][] { { "a", "1" }, { "b", "2" } };
		if (Compare.equal(test1, test2, mismatchRow, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but there should have been a mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Row:  " + mismatchRow[0] + "; Column:  " + mismatchColumn[0]);

		Logs.log.info("");
		mismatchColumn[0] = -2;
		String[] test3 = new String[] { "a", "1", "b", "2" };
		String[] test4 = new String[] { "a", "1", "b", "2" };
		if (Compare.equal(test3, test4, mismatchColumn))
			Logs.log.info("Arrays were equal as expected");
		else
		{
			Logs.log.error("Column:  " + mismatchColumn[0]);
			throw new GenericUnexpectedException("");
		}

		mismatchColumn[0] = -2;
		test3 = new String[] { "a", "1", "b", "2" };
		test4 = new String[] { "a", "1", "c", "2" };
		if (Compare.equal(test3, test4, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but expected mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Column:  " + mismatchColumn[0]);

		mismatchColumn[0] = -2;
		test3 = new String[] { "a", "1", "b", "2", "c" };
		test4 = new String[] { "a", "1", "b", "2" };
		if (Compare.equal(test3, test4, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but expected mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Column:  " + mismatchColumn[0]);

		Logs.log.info("letters" + "\t" + "numbers" + "\t" + "alpha" + "\t" + "special");
		for (int i = 1; i < 30; i++)
		{
			Logs.log.info(Rand.letters(i) + "\t" + Rand.numbers(i) + "\t" + Rand.alphanumeric(i) + "\t"
					+ Rand.special(i));
		}

		Logs.log.info("");
		Logs.log.info("Variable length random strings tests:");

		for (int i = 0; i < 20; i++)
		{
			int nMin = 5;
			int nMax = 20;
			String sLetters = Rand.letters(nMin, nMax);
			String sAlphaNumeric = Rand.alphanumeric(nMin, nMax);
			String sNumeric = Rand.numbers(nMin, nMax);

			if (sLetters.length() < nMin || sLetters.length() > nMax)
				Logs.logError("sLetters (" + sLetters + ") outside range ( " + nMin + "," + nMax + ")");

			if (sAlphaNumeric.length() < nMin || sAlphaNumeric.length() > nMax)
				Logs.logError("sAlphaNumeric (" + sAlphaNumeric + ") outside range ( " + nMin + "," + nMax
						+ ")");

			if (sNumeric.length() < nMin || sNumeric.length() > nMax)
				Logs.logError("sNumeric (" + sNumeric + ") outside range ( " + nMin + "," + nMax + ")");

			Logs.log.info(sLetters + ", " + sAlphaNumeric + ", " + sNumeric);
		}

		Logs.log.info("");

		Logs.log.info("replaceLast tests:");
		List<String> testing = new ArrayList<String>();
		testing.add("acd");
		testing.add(", ac");
		testing.add(", ");
		testing.add("ac, ");
		testing.add("a, c");
		for (String test : testing)
		{
			Logs.log.info(Misc.replaceLast(test, ", ", "& "));
		}
		Logs.log.info("");

		testing = new ArrayList<String>();
		testing.add("acd");
		testing.add("bbac");
		testing.add("bb");
		testing.add("acbb");
		testing.add("abbc");
		for (String test : testing)
		{
			Logs.log.info(Misc.replaceLast(test, "bb", "Z"));
		}
		Logs.log.info("");

		testing = new ArrayList<String>();
		testing.add("acd");
		testing.add("bbac");
		testing.add("bb");
		testing.add("acbb");
		testing.add("abbc");
		for (String test : testing)
		{
			Logs.log.info(Misc.replaceLast(test, "bb", "ZZZ"));
		}
		Logs.log.info("");

		testing = new ArrayList<String>();
		testing.add("acd");
		testing.add("bac");
		testing.add("b");
		testing.add("acb");
		testing.add("abc");
		for (String test : testing)
		{
			Logs.log.info(Misc.replaceLast(test, "b", "ZZ"));
		}
		Logs.log.info("");

		testing = new ArrayList<String>();
		testing.add("acd");
		testing.add("bac");
		testing.add("b");
		testing.add("acb");
		testing.add("abc");
		for (String test : testing)
		{
			Logs.log.info(Misc.replaceLast(test, "b", "Y"));
		}
		Logs.log.info("");

		testing = new ArrayList<String>();
		testing.add("acd");
		testing.add(", ac");
		testing.add(", ");
		testing.add("ac, ");
		testing.add("a, c");
		for (String test : testing)
		{
			Logs.log.info(Misc.replaceLast(test, ", ", ""));
		}
		Logs.log.info("");
	}

	@Test
	public static void unitTest2() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();

		// String sNewPassword = "test";
		String sBasePathToLookupFile = "resources/data/";

		ConfigRun.env_basePath = sBasePathToLookupFile;
		String sLookupFile = Controller.getDataDrivenValuesFileUsingBasePath("PasswordLookup");
		Logs.log.info("Lookup File:  " + sLookupFile);

		Lookup lookup = new Lookup(sLookupFile, "/Lookup/Password/");
		List<Parameter> pairs = lookup.getKeyValuePairs();
		for (Parameter item : pairs)
		{
			Logs.log.info("Key:  " + item.param + "\t\tPassword:  " + item.value + "\t\tNew Password:  "
					+ CryptoDESede.encode(item.value));
		}

		Logs.log.info("");
		StringBuffer sb = new StringBuffer();
		sb.append("<Lookup>");
		sb.append("<Password Key=\"dn001\" Value=\"Fawd3COJbViXRjLlxH/EYm68rl3lkvir9CoCwbgVmhKR+wBDVNlbxw==\" />");
		sb.append("<Password Key=\"dn002\" Value=\"something\" />");
		sb.append("</Lookup>");

		lookup = new Lookup(sb.toString().getBytes(), "/Lookup/Password/");
		pairs = lookup.getKeyValuePairs();
		for (Parameter item : pairs)
		{
			Logs.log.info("Key:  " + item.param + "\t\tPassword:  " + item.value + "\t\tNew Password:  "
					+ CryptoDESede.encode(item.value));
		}

		Logs.log.info("");
		int nIndex = pairs.indexOf(new Parameter("dn001"));
		Logs.log.info("Index:  " + nIndex);
		Logs.log.info("Pair:  " + pairs.get(nIndex));
	}
}
