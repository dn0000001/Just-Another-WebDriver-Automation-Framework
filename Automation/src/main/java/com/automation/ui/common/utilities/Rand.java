package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.GenericDate;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.Selection;

/**
 * This class holds methods related to randomization
 */
public class Rand {
	/**
	 * Default Special characters that cannot be changed
	 */
	private static final String SPECIAL = "`~!@#$%^&*()_+-=,./;'[]<>?:{}|\"\\";

	/**
	 * All valid ASCII letters that cannot be changed
	 */
	private static final String ASCII_Letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	/**
	 * All valid ASCII numbers that cannot be changed
	 */
	private static final String ASCII_Numbers = "0123456789";

	/**
	 * The default extended letters that cannot be changed<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) All non-ASCII letters that are to be supported should be assigned here<BR>
	 * 2) It is necessary to use unicode code points to avoid encoding issues<BR>
	 */
	private static final String Extended_Letters = Accents.getFrench();

	/**
	 * The default extended numbers that cannot be changed<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) All non-ASCII numbers that are to be supported should be assigned here<BR>
	 * 2) It is necessary to use unicode code points to avoid encoding issues<BR>
	 */
	private static final String Extended_Numbers = "";

	/**
	 * All the extended letters that cannot be changed that are used to support additional character sets
	 */
	private static final String ALL_Letters = ASCII_Letters + Extended_Letters;

	/**
	 * All the extended numbers that cannot be changed that are used to support additional character sets
	 */
	private static final String ALL_Numbers = ASCII_Numbers + Extended_Numbers;

	/**
	 * Special characters to use by default
	 */
	private static String sUsedSpecialCharacterSet = SPECIAL;

	/**
	 * Letters to use by default in specific methods to support additional character sets.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * The Apache library does not support specific character sets. However, there is a method that you can
	 * control the string used.<BR>
	 */
	private static String sUsedExtendedLetters = ALL_Letters;

	/**
	 * Numbers to use by default in specific methods to support additional character sets.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * The Apache library does not support specific character sets. However, there is a method that you can
	 * control the string used.<BR>
	 */
	private static String sUsedExtendedNumbers = ALL_Numbers;

	/**
	 * Special token to generate a numeric string within a range
	 */
	private static String sRange = "{RANDOM_RANGE(";

	/**
	 * Supported Tokens for replacement (Starts with to indicate token and must end with '}') which map to
	 * { Alphanumeric, Letters Only, Numbers Only, Uppercase Only, Lowercase Only, Special Characters Only,
	 * Boolean (true or false), Password that contains 1 uppercase, 1 lowercase & 1 number character, Password
	 * that contains 1 uppercase, 1 lowercase, 1 number & 1 special character}
	 */
	private static String[] TokensMap = new String[] { "{RANDOM_ALPHA=", "{RANDOM_LETTERS=", "{RANDOM_NUM=",
			"{RANDOM_UPPERCASE=", "{RANDOM_LOWERCASE=", "{RANDOM_SPECIAL=", "{RANDOM_BOOLEAN",
			"{RANDOM_PASSWORD=", "{RANDOM_PASSWORD_ALL=", sRange };

	/**
	 * Gets the default special characters.
	 * 
	 * @return SPECIAL
	 */
	public static String getSpecialDefaults()
	{
		return SPECIAL;
	}

	/**
	 * Gets the default letters used in specific methods to support additional character sets
	 * 
	 * @return ALL_Letters
	 */
	public static String getLettersDefaults()
	{
		return ALL_Letters;
	}

	/**
	 * Gets the default numbers used in specific methods to support additional character sets
	 * 
	 * @return ALL_Numbers
	 */
	public static String getNumbersDefaults()
	{
		return ALL_Numbers;
	}

	/**
	 * Gets only the extended default letters
	 * 
	 * @return Extended_Letters
	 */
	public static String getOnlyExtendedLetters()
	{
		return Extended_Letters;
	}

	/**
	 * Gets only the extended default numbers
	 * 
	 * @return Extended_Numbers
	 */
	public static String getOnlyExtendedNumbers()
	{
		return Extended_Numbers;
	}

	/**
	 * Resets the used special characters back to the original values
	 */
	public synchronized static void resetUsedSpecialToDefaults()
	{
		sUsedSpecialCharacterSet = SPECIAL;
	}

	/**
	 * Resets the used extended letters in specific methods to support additional character sets back to the
	 * original values
	 */
	public synchronized static void resetUsedExtendedLettersToDefaults()
	{
		sUsedExtendedLetters = ALL_Letters;
	}

	/**
	 * Resets the used extended numbers in specific methods to support additional character sets back to the
	 * original values
	 */
	public synchronized static void resetUsedExtendedNumbersToDefaults()
	{
		sUsedExtendedNumbers = ALL_Numbers;
	}

	/**
	 * Set the special characters to be a specific values
	 * 
	 * @param sValue - String that contains the special characters you wish to use
	 */
	public synchronized static void setUsedSpecial(String sValue)
	{
		sUsedSpecialCharacterSet = sValue;
	}

	/**
	 * Set the used extended letters in specific methods to support additional character sets to be a specific
	 * values
	 * 
	 * @param sValue - String that contains the letters you wish to use
	 */
	public synchronized static void setUsedExtendedLetters(String sValue)
	{
		sUsedExtendedLetters = sValue;
	}

	/**
	 * Set the used extended numbers in specific methods to support additional character sets to be a specific
	 * values
	 * 
	 * @param sValue - String that contains the letters you wish to use
	 */
	public synchronized static void setUsedExtendedNumbers(String sValue)
	{
		sUsedExtendedNumbers = sValue;
	}

	/**
	 * Gets the currently used special characters.
	 * 
	 * @return sUsedSpecialCharacterSet
	 */
	public synchronized static String getUsedSpecial()
	{
		return sUsedSpecialCharacterSet;
	}

	/**
	 * Gets the currently used extended letters in specific methods to support additional character sets
	 * 
	 * @return sUsedLetters
	 */
	public synchronized static String getUsedExtendedLetters()
	{
		return sUsedExtendedLetters;
	}

	/**
	 * Gets the currently used extended numbers in specific methods to support additional character sets
	 * 
	 * @return sUsedNumbers
	 */
	public synchronized static String getUsedExtendedNumbers()
	{
		return sUsedExtendedNumbers;
	}

	/**
	 * Generates an array of unique integers.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) nSize needs to be greater than 0 else defaults to array of size 1<BR>
	 * 2) nUpperBound needs be greater than or equal to nSize else defaults to value of nSize<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) IF nSize = -1, nUpperBound = 0 THEN array will be size 1 and have value of 0<BR>
	 * 2) IF nSize = -1, nUpperBound = 100 THEN array will be size 1 and have a value between 0 and 99
	 * inclusive<BR>
	 * 3) IF nSize = 3, nUpperBound = 1 THEN array will be size 3 and all values will be between 0 and 2
	 * inclusive<BR>
	 * 4) IF nSize = 5, nUpperBound = 100 THEN array will be size 5 and all values will be between 0 and 99
	 * inclusive<BR>
	 * 
	 * @param nSize - number of unique integers
	 * @param nUpperBound - generate numbers below this value
	 * @return array of unique integers
	 */
	public static int[] uniqueRandom(int nSize, int nUpperBound)
	{
		Random randomGenerator = new Random();
		int nRandomInt;
		int nUniqueCount = 0;

		// Need to ensure that min length is 1
		int nLength = 1;
		if (nSize > nLength)
			nLength = nSize;

		// Need to ensure that we can generate all unique integers
		int nMaxValue = nLength;
		if (nUpperBound > nMaxValue)
			nMaxValue = nUpperBound;

		// Generate the 1st unique integer
		int[] uniqueSet = new int[nLength];

		// Initialize array values to -1 which cannot be generated
		for (int i = 0; i < nLength; i++)
			uniqueSet[i] = -1;

		uniqueSet[0] = randomGenerator.nextInt(nMaxValue);
		nUniqueCount++;

		/*
		 * Generate the remaining unique integers.
		 * Note: This may take some time depending upon the upper bound for random integers.
		 */
		while (nUniqueCount < nLength)
		{
			nRandomInt = randomGenerator.nextInt(nMaxValue);
			if (!Compare.exists(uniqueSet, nRandomInt))
			{
				uniqueSet[nUniqueCount] = nRandomInt;
				nUniqueCount++;
			}
		}

		return uniqueSet;
	}

	/**
	 * Returns a random String that meets the requirements.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) <B>nLength</B> needs to be at least 1 or enough characters to meet the additional requirements<BR>
	 * 2) IF <B>nLength</B> < 1 THEN returned String is min characters to meet the additional requirements or
	 * 1<BR>
	 * 3) Use <B>getUsedSpecial()</B> to get the current special characters set <BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) random(1, true, true, true, true, getUsedSpecial()) returns a random String of <B>4</B> characters
	 * that has 1 uppercase letter, 1 lowercase letter, 1 number & 1 special character.<BR>
	 * 2) random(-1, false, false, false, false, getUsedSpecial()) returns a random String of <B>1</B>
	 * character<BR>
	 * 3) random(20, true, true, true, true, getUsedSpecial()) returns a random String of <B>20</B> characters
	 * that has 1 uppercase letter, 1 lowercase letter, 1 number & 1 special character.<BR>
	 * 
	 * @param nLength - Length of String to return
	 * @param bUpper - true ensures that returned String has at least 1 uppercase letter
	 * @param bLower - true ensures that returned String has at least 1 lowercase letter
	 * @param bNumber - true ensures that returned String has at least 1 number
	 * @param bSpecial - true ensures that returned String has at least 1 special character
	 * @param sSpecialCharacterSet - The Special Character Set to be used
	 * @return random String that meets parameter requirements
	 */
	public static String random(int nLength, boolean bUpper, boolean bLower, boolean bNumber,
			boolean bSpecial, String sSpecialCharacterSet)
	{
		/*
		 * 1) Start by generating a String Builder of the specified length
		 * 2) Insert an uppercase letter if necessary at a random position
		 * 3) Insert an lowercase letter if necessary at a random position
		 * 4) Insert a number if necessary at a random position
		 * 5) Insert a special character if necessary at a random position
		 */

		/*
		 * Ensure that String Builder is at least enough characters to make random String meet the
		 * requirements.
		 */
		int nMinLength = 0;
		if (bUpper)
			nMinLength++;

		if (bLower)
			nMinLength++;

		if (bNumber)
			nMinLength++;

		if (bSpecial)
			nMinLength++;

		if (nMinLength < 1)
			nMinLength = 1;

		if (nLength > nMinLength)
			nMinLength = nLength;

		// Generate the String Builder
		StringBuilder rest = new StringBuilder(RandomStringUtils.randomAlphanumeric(nMinLength));

		// Get 4 unique positions in the String Builder
		int[] nUniqueSet = uniqueRandom(nMinLength, nMinLength);
		int nIndex = 0;

		// Insert an uppercase letter if necessary at a random position
		if (bUpper)
		{
			rest.delete(nUniqueSet[nIndex], nUniqueSet[nIndex] + 1);
			rest.insert(nUniqueSet[nIndex], RandomStringUtils.randomAlphabetic(1).toUpperCase());
			nIndex++;
		}

		// Insert an lowercase letter if necessary at a random position
		if (bLower)
		{
			rest.delete(nUniqueSet[nIndex], nUniqueSet[nIndex] + 1);
			rest.insert(nUniqueSet[nIndex], RandomStringUtils.randomAlphabetic(1).toLowerCase());
			nIndex++;
		}

		// Insert a number if necessary at a random position
		if (bNumber)
		{
			rest.delete(nUniqueSet[nIndex], nUniqueSet[nIndex] + 1);
			rest.insert(nUniqueSet[nIndex], RandomStringUtils.randomNumeric(1));
			nIndex++;
		}

		// Insert a special character if necessary at a random position
		if (bSpecial)
		{
			rest.delete(nUniqueSet[nIndex], nUniqueSet[nIndex] + 1);
			rest.insert(nUniqueSet[nIndex], RandomStringUtils.random(1, sSpecialCharacterSet));
			nIndex++;
		}

		return rest.substring(0, nMinLength);
	}

	/**
	 * Returns a random String that meets the requirements.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) <B>nLength</B> needs to be at least 1 or enough characters to meet the additional requirements<BR>
	 * 2) IF <B>nLength</B> < 1 THEN returned String is min characters to meet the additional requirements or
	 * 1<BR>
	 * 3) Use <B>setUsedSpecial</B> if you need to change the special characters set <BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) random(1, true, true, true, true) returns a random String of <B>4</B> characters that has 1
	 * uppercase letter, 1 lowercase letter, 1 number & 1 special character.<BR>
	 * 2) random(-1, false, false, false, false) returns a random String of <B>1</B> character<BR>
	 * 3) random(20, true, true, true, true) returns a random String of <B>20</B> characters that has 1
	 * uppercase letter, 1 lowercase letter, 1 number & 1 special character.<BR>
	 * 
	 * @param nLength - Length of String to return
	 * @param bUpper - true ensures that returned String has at least 1 uppercase letter
	 * @param bLower - true ensures that returned String has at least 1 lowercase letter
	 * @param bNumber - true ensures that returned String has at least 1 number
	 * @param bSpecial - true ensures that returned String has at least 1 special character
	 * @return random String that meets parameter requirements
	 */
	public synchronized static String random(int nLength, boolean bUpper, boolean bLower, boolean bNumber,
			boolean bSpecial)
	{
		return random(nLength, bUpper, bLower, bNumber, bSpecial, getUsedSpecial());
	}

	/**
	 * Returns a randomized string corresponding to the token mapping
	 * 
	 * @param sToken - A supported token
	 * @param nLength - Length of String to return
	 * @return String
	 */
	public static String randomizeToken(String sToken, int nLength, String sSpecialCharacterSet)
	{
		/*
		 * Match Token to corresponding random function
		 */
		String sRandomized = sToken;

		// Random Alphanumeric String
		if (sToken.equals(TokensMap[0]))
		{
			sRandomized = RandomStringUtils.randomAlphanumeric(nLength);
		}

		// Random Letters Only String
		if (sToken.equals(TokensMap[1]))
		{
			sRandomized = RandomStringUtils.randomAlphabetic(nLength);
		}

		// Random Numbers Only String
		if (sToken.equals(TokensMap[2]))
		{
			sRandomized = RandomStringUtils.randomNumeric(nLength);
		}

		// Random Uppercase Only String
		if (sToken.equals(TokensMap[3]))
		{
			sRandomized = RandomStringUtils.randomAlphabetic(nLength).toUpperCase();
		}

		// Random Lowercase Only String
		if (sToken.equals(TokensMap[4]))
		{
			sRandomized = RandomStringUtils.randomAlphabetic(nLength).toLowerCase();
		}

		// Random Special Characters Only String
		if (sToken.equals(TokensMap[5]))
		{
			sRandomized = RandomStringUtils.random(nLength, sSpecialCharacterSet);
		}

		// Random Boolean String (true or false)
		if (sToken.equals(TokensMap[6]))
		{
			Random r = new Random();
			if ((r.nextInt(1000) % 2) == 0)
				sRandomized = "true";
			else
				sRandomized = "false";
		}

		// Random Password with 1 uppercase, 1 lowercase & 1 number character String
		if (sToken.equals(TokensMap[7]))
		{
			sRandomized = random(nLength, true, true, true, false, sSpecialCharacterSet);
		}

		// Random Password with 1 uppercase, 1 lowercase, 1 number & 1 special character String
		if (sToken.equals(TokensMap[8]))
		{
			sRandomized = random(nLength, true, true, true, true, sSpecialCharacterSet);
		}

		return sRandomized;
	}

	/**
	 * Returns a randomized string corresponding to the token mapping
	 * 
	 * @param sToken - A supported token
	 * @param nLength - Length of String to return
	 * @return String
	 */
	public synchronized static String randomizeToken(String sToken, int nLength)
	{
		return randomizeToken(sToken, nLength, getUsedSpecial());
	}

	/**
	 * Replaces the supported tokens with randomized data.<BR>
	 * <BR>
	 * <B>Supported Tokens:</B><BR>
	 * 1) {RANDOM_ALPHA=X} - Replaced with X alphanumeric characters<BR>
	 * 2) {RANDOM_LETTERS=X} - Replaced with X letters that could be uppercase or lowercase<BR>
	 * 3) {RANDOM_NUM=X} - Replaced with X numbers<BR>
	 * 4) {RANDOM_UPPERCASE=X} - Replaced with X uppercase letters<BR>
	 * 5) {RANDOM_LOWERCASE=X} - Replaced with X lowercase letters<BR>
	 * 6) {RANDOM_SPECIAL=X} - Replaced with X special characters<BR>
	 * 7) {RANDOM_BOOLEAN} - Replaced with 'true' or 'false'<BR>
	 * 8) {RANDOM_PASSWORD=X} - Replaced with X characters with at least 1 uppercase, 1 lowercase & 1 number<BR>
	 * 9) {RANDOM_PASSWORD_ALL=X} - Replaced with X characters with at least 1 uppercase, 1 lowercase, 1
	 * number & 1 special character<BR>
	 * 10) {RANDOM_RANGE(X,Y)} - Replaced with number that satisfies criteria X &lt;= number &lt;= Y<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) {RANDOM_ALPHA=3} => <B>aB1</B><BR>
	 * 2) {RANDOM_ALPHA=3} => <B>1qa</B><BR>
	 * 3) {RANDOM_LETTERS=5} => <B>dJukL</B><BR>
	 * 4) {RANDOM_LETTERS=5} => <B>Kuhjg</B><BR>
	 * 5) {RANDOM_NUM=3} => <B>658</B><BR>
	 * 6) {RANDOM_NUM=3} => <B>432</B><BR>
	 * 7) {RANDOM_UPPERCASE=4} => <B>DIEK</B><BR>
	 * 8) {RANDOM_UPPERCASE=4} => <B>NYHF</B><BR>
	 * 9) {RANDOM_LOWERCASE=4} => <B>bdgh</B><BR>
	 * 10) {RANDOM_LOWERCASE=4} => <B>npje</B><BR>
	 * 11) {RANDOM_SPECIAL=2} => <B>&^</B><BR>
	 * 12) {RANDOM_SPECIAL=2} => <B>$#</B><BR>
	 * 13) {RANDOM_LETTERS=1}-{RANDOM_NUM=3}-{RANDOM_ALPHA=3} => <B>x-083-Wdi</B><BR>
	 * 14) {RANDOM_ALPHA=3}-{RANDOM_ALPHA=3} => <B>Ab1-Y32</B><BR>
	 * 15) {RANDOM_NUM=3}-{RANDOM_NUM=3} => <B>123-456</B><BR>
	 * 16) {RANDOM_NUM=3}-{RANDOM_NUM=5} => <B>123-63538</B><BR>
	 * 
	 * @param sValue - String that contains Tokens which to be randomized
	 * @return String
	 */
	public synchronized static String replaceWithRandomizedTokens(String sValue)
	{
		String sRandom = sValue;
		int nSize = TokensMap.length;
		for (int i = 0; i < nSize; i++)
		{
			int nStartToken = 0;
			int nEndToken = 0;
			while (nStartToken != -1 && nEndToken != -1)
			{
				try
				{
					/*
					 * 1) Get the index for the start of the token
					 * 2) Calculate end of the start token indicator
					 * 3) Get the index of the token end
					 * 4) Get the desired length for the random string
					 * 5) Get the entire token
					 * 6) Replace the entire token with the desired length and type of random string
					 */
					nStartToken = sRandom.indexOf(TokensMap[i]);
					if (nStartToken == -1)
						continue;

					int nStartTokenIndicatorEnd = nStartToken + TokensMap[i].length();
					nEndToken = sRandom.indexOf("}", nStartTokenIndicatorEnd);
					if (nEndToken == -1)
						continue;

					String sLengthForRandomString = sRandom.substring(nStartTokenIndicatorEnd, nEndToken);
					String sEntireToken = sRandom.substring(nStartToken, nEndToken + 1);

					List<Parameter> items = new ArrayList<Parameter>();
					String sReplacement;

					if (TokensMap[i] == sRange)
					{
						// Range is separated by a comma
						String[] parse = sLengthForRandomString.split(",");

						// Max Range should end with a )
						if (parse[1].endsWith(")"))
							parse[1] = parse[1].substring(0, parse[1].length() - 1);

						int nStartIndex = Conversion.parseInt(parse[0], 0);
						int nEndIndex = Conversion.parseInt(parse[1], 0);
						sReplacement = String.valueOf(randomRange(nStartIndex, nEndIndex));
					}
					else
					{
						int nLength = Conversion.parseInt(sLengthForRandomString, 1);
						sReplacement = randomizeToken(TokensMap[i], nLength);
					}

					items.add(new Parameter(Misc.escapeForRegEx(sEntireToken), Misc
							.escapeForRegEx(sReplacement)));
					sRandom = Misc.replace1stMatch(sRandom, items);
				}
				catch (Exception ex)
				{
				}
			}
		}

		return sRandom;
	}

	/**
	 * Returns a number in the specified range.<BR>
	 * <BR>
	 * <B>Inclusive Examples:</B><BR>
	 * 1) randomizeRangeToken(1,12) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
	 * 2) randomizeRangeToken(12,1) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
	 * 3) randomizeRangeToken(-5,5) can return {-5, -4, -3, -2, -1, 0, 2, 3, 4, 5}<BR>
	 * <BR>
	 * <B>Exclusive Examples:</B><BR>
	 * 1) randomizeRangeToken(1,12) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}<BR>
	 * 2) randomizeRangeToken(12,1) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}<BR>
	 * 3) randomizeRangeToken(-5,5) can return {-5, -4, -3, -2, -1, 0, 2, 3, 4}<BR>
	 * 
	 * @param nMin - Minimum value
	 * @param nMax - Maximum value
	 * @param inclusive - true for max value to be inclusive, false for max value to be exclusive
	 * @return int
	 */
	private static int randomRange(int nMin, int nMax, boolean inclusive)
	{
		int nOffset, nUseRangeMin, nUseRangeMax, nRange;

		// Set the default values for the range
		nUseRangeMin = nMin;
		nUseRangeMax = nMax;

		// Get a valid range
		if (nUseRangeMin > nUseRangeMax)
		{
			int nTemp = nUseRangeMin;
			nUseRangeMin = nUseRangeMax;
			nUseRangeMax = nTemp;
		}

		/*
		 * Need to shift the range to be 0 to Max for random number generation. The offset will be used after
		 * to random number in range.
		 */
		nOffset = nUseRangeMin;

		// Need to ensure range is positive
		if (nUseRangeMin < 0)
		{
			nUseRangeMin += -1 * nOffset;
			nUseRangeMax += -1 * nOffset;
		}

		Random r = new Random();
		if (inclusive)
			nRange = nOffset + r.nextInt(nUseRangeMax - nUseRangeMin + 1);
		else
			nRange = nOffset + r.nextInt(nUseRangeMax - nUseRangeMin);

		return nRange;
	}

	/**
	 * Returns a number in the specified range.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) randomizeRangeToken(1,12) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
	 * 2) randomizeRangeToken(12,1) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
	 * 3) randomizeRangeToken(-5,5) can return {-5, -4, -3, -2, -1, 0, 2, 3, 4, 5}<BR>
	 * 
	 * @param nMin - Minimum value (inclusive)
	 * @param nMax - Maximum value (inclusive)
	 * @return int
	 */
	public static int randomRange(int nMin, int nMax)
	{
		return randomRange(nMin, nMax, true);
	}

	/**
	 * Returns a number in the specified range.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) randomizeRangeToken(1,12) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}<BR>
	 * 2) randomizeRangeToken(12,1) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}<BR>
	 * 3) randomizeRangeToken(-5,5) can return {-5, -4, -3, -2, -1, 0, 2, 3, 4}<BR>
	 * 
	 * @param nMin - Minimum value (inclusive)
	 * @param nMax - Maximum value (exclusive)
	 * @return int
	 */
	public static int randomRangeIndex(int nMin, int nMax)
	{
		return randomRange(nMin, nMax, false);
	}

	/**
	 * Gets a random enumeration option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Range Multiplier is 10000<BR>
	 * 
	 * @param e - Any Enumeration value
	 * @return enumeration value
	 */
	public static Enum<?> randomEnum(Enum<?> e)
	{
		return randomEnum(e, 10000);
	}

	/**
	 * Gets a random enumeration option<BR>
	 * <BR>
	 * <B>Example: </B><BR>
	 * Languages randomLang = (Languages) Rand.randomEnum(Languages.English, 10000);<BR>
	 * 
	 * @param e - Any Enumeration value
	 * @param nRangeMultiplier - Increases the range of values before getting the remainder when divided by
	 *            number of enumeration values for the enumeration
	 * @return enumeration value
	 */
	public static Enum<?> randomEnum(Enum<?> e, int nRangeMultiplier)
	{
		Enum<?>[] options = e.getDeclaringClass().getEnumConstants();
		int nSize = options.length;
		int nRandom = randomRange(0, nRangeMultiplier * nSize) % nSize;
		return options[nRandom];
	}

	/**
	 * Gets a random enumeration option<BR>
	 * <BR>
	 * <B>Example: </B><BR>
	 * Languages randomLang = (Languages) Rand.randomEnum(Languages.English, Languages.KEY);<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If all enumeration values are excluded, then an ArithmeticException will occur due to division by
	 * zero<BR>
	 * 2) Range Multiplier is 10000<BR>
	 * 
	 * @param e - Any Enumeration value
	 * @param exclude - enumeration values to be excluded from possible selection
	 * @return enumeration value
	 * @throws ArithmeticException if all enumeration values are excluded
	 */
	public static Enum<?> randomEnum(Enum<?> e, Enum<?>... exclude)
	{
		return randomEnum(e, 10000, exclude);
	}

	/**
	 * Gets a random enumeration option<BR>
	 * <BR>
	 * <B>Example: </B><BR>
	 * Languages randomLang = (Languages) Rand.randomEnum(Languages.English, 10000, Languages.KEY);<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If all enumeration values are excluded, then an ArithmeticException will occur due to division by
	 * zero<BR>
	 * 
	 * @param e - Any Enumeration value
	 * @param nRangeMultiplier - Increases the range of values before getting the remainder when divided by
	 *            number of enumeration values for the enumeration
	 * @param exclude - enumeration values to be excluded from possible selection
	 * @return enumeration value
	 * @throws ArithmeticException if all enumeration values are excluded
	 */
	public static Enum<?> randomEnum(Enum<?> e, int nRangeMultiplier, Enum<?>... exclude)
	{
		Enum<?>[] options = Misc.getValues(e, exclude);
		int nSize = options.length;
		int nRandom = randomRange(0, nRangeMultiplier * nSize) % nSize;
		return options[nRandom];
	}

	/**
	 * Returns a random string of the specified size with only alphabetic characters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Wraps the RandomStringUtils.randomAlphabetic<BR>
	 * 
	 * @param nLength - Size of String to return
	 * @return Random String of only Alphabetic characters that is of specified length
	 */
	public static String letters(int nLength)
	{
		return RandomStringUtils.randomAlphabetic(nLength);
	}

	/**
	 * Returns a random string that only consists of numbers and starts with a non-zero number
	 * 
	 * @param nLength - Size of String to return
	 * @return String that begins with non-zero number and is only numbers of specified size
	 */
	public static String numbers(int nLength)
	{
		String nonZero = String.valueOf(randomRange(1, 9));
		String rest = "";
		if (nLength - 1 > 0)
			rest = RandomStringUtils.randomNumeric(nLength - 1);

		return nonZero + rest;
	}

	/**
	 * Returns an alphanumeric string that starts with a letter of the specified size
	 * 
	 * @param nLength - Size of String to return
	 * @return String that begins with a letter and only consists of alphanumeric characters of specified
	 *         length
	 */
	public static String alphanumeric(int nLength)
	{
		String nonNumber = RandomStringUtils.randomAlphabetic(1);
		String rest = "";
		if (nLength - 1 > 0)
			rest = RandomStringUtils.randomAlphanumeric(nLength - 1);

		return nonNumber + rest;
	}

	/**
	 * Returns a string with only characters in the given set of the specified size
	 * 
	 * @param nLength - Size of String to return
	 * @param sCharacterSet - Character Set to be used
	 * @return String that only consists of characters in the set of the specified length
	 */
	public static String special(int nLength, String sCharacterSet)
	{
		// Generate the String Builder
		StringBuilder build = new StringBuilder();
		for (int i = 0; i < nLength; i++)
		{
			int nRandomPos = randomRange(0, sCharacterSet.length() - 1);
			build.append(sCharacterSet.charAt(nRandomPos));
		}

		return build.toString();
	}

	/**
	 * Returns a string with only special characters of the specified size
	 * 
	 * @param nLength - Size of String to return
	 * @return String that only consists of special characters of the specified length
	 */
	public synchronized static String special(int nLength)
	{
		return special(nLength, getUsedSpecial());
	}

	/**
	 * Returns either true or false based on a random number<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Max rand value is 10000<BR>
	 * 
	 * @return true if (rand % 2 == 0) else false
	 */
	public static boolean randomBoolean()
	{
		return randomBoolean(10000);
	}

	/**
	 * Returns either true or false based on a random number<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The higher the max random number, the better standard deviation of the results should be<BR>
	 * 
	 * @param nRange - Max Random number to determine true/false
	 * @return true if (rand % 2 == 0) else false
	 */
	public static boolean randomBoolean(int nRange)
	{
		if (randomRange(0, nRange) % 2 == 0)
			return true;
		else
			return false;
	}

	/**
	 * Gets a random valid index option for the specified drop down
	 * 
	 * @param dropdown - Drop down to get random index
	 * @param dd - Object that contains information on which drop down option to select
	 * @return If any error dd.sOption is returned else random valid index as String
	 */
	public static String randomIndexOption(WebElement dropdown, DropDown dd)
	{
		String sOption = dd.option;

		try
		{
			/*
			 * Get number of drop down options
			 */
			Select selectTag = new Select(dropdown);
			List<WebElement> options = selectTag.getOptions();
			int nMax = options.size();

			/*
			 * Generate a valid random number based on the number of drop down options
			 */
			int nRandomIndex;
			if (dd.minIndex >= nMax || dd.minIndex < 0)
				nRandomIndex = Rand.randomRange(0, nMax - 1);
			else
				nRandomIndex = Rand.randomRange(dd.minIndex, nMax - 1);

			// Get String value of the random index to be used
			sOption = String.valueOf(nRandomIndex);
		}
		catch (Exception ex)
		{
		}

		return sOption;
	}

	/**
	 * Returns a new DropDown object with a valid random index set for sOption<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Checks that dd.nUsing = DropDown.nINDEX & dd.sOption < 0<BR>
	 * 2) dd is return if any error occurs or the above checks fail<BR>
	 * 3) Retains nMinIndex from dd<BR>
	 * 4) This method should be used if there is AJAX on the drop down to prevent an AJAX timeout error<BR>
	 * 
	 * @param dropdown - Drop down to get random index
	 * @param dd - Object that contains information on which drop down option to select
	 * @return DropDown
	 */
	public static DropDown randomDropDown(WebElement dropdown, DropDown dd)
	{
		try
		{
			// If dd does not have random properties, then return dd
			if (dd.using != Selection.Index || Conversion.parseInt(dd.option) >= 0)
				return dd;

			// Make a copy of the object and set the random index information
			DropDown temp = dd.copy();
			temp.option = randomIndexOption(dropdown, dd);
			return temp;
		}
		catch (Exception ex)
		{
			return dd;
		}
	}

	/**
	 * Returns an array of boolean with at least the specified number of elements being true (if possible)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Array Size needs to be greater than 0 or an exception will occur<BR>
	 * 2) The number the true elements should be less than the Array Size<BR>
	 * 
	 * @param arraySize - Array size to return must be greater than 0
	 * @param numberTrue - The least number of true elements in the array (if possible)
	 * @return boolean[]
	 */
	public static boolean[] atLeastTrueArray(int arraySize, int numberTrue)
	{
		boolean[] arrayOfBoolean = new boolean[arraySize];

		int nTrueCount = 0;
		for (int i = 0; i < arraySize; i++)
		{
			// Initialize array and count number of true elements
			arrayOfBoolean[i] = randomBoolean();
			if (arrayOfBoolean[i])
				nTrueCount++;
		}

		// If the number of true is at least the desire amount, then we are done
		if (nTrueCount >= numberTrue)
			return arrayOfBoolean;

		// Make the first X options that are false to be true to satisfy the number of true desired
		// Note: If there is an invalid size for the true elements desired, then the array may end up all true
		// but not satisfying the number of true condition
		for (int i = 0; i < arraySize; i++)
		{
			if (!arrayOfBoolean[i])
			{
				arrayOfBoolean[i] = true;
				nTrueCount++;
			}

			if (nTrueCount >= numberTrue)
				return arrayOfBoolean;
		}

		return arrayOfBoolean;
	}

	/**
	 * Returns an array of boolean with at least 1 element being true<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Array Size needs to be greater than 0 or an exception will occur<BR>
	 * 
	 * @param arraySize - Array size to return must be greater than 0
	 * @return Returns an array of boolean with at least 1 element being true
	 */
	public static boolean[] atLeastTrueArray(int arraySize)
	{
		return atLeastTrueArray(arraySize, 1);
	}

	/**
	 * Modifies the array to contain at least the number of true elements by picking random elements that
	 * are false and making them true. (This method will stop after the max number of tries is reached.)
	 * 
	 * @param arrayOfBoolean - Array with the element to be modified
	 * @param numberTrue - Stop when at least this number of true elements
	 * @param nMaxTries - Max attempts to achieve the desired number of true elements
	 */
	public static void atLeastTrue(boolean[] arrayOfBoolean, int numberTrue, int nMaxTries)
	{
		int nTrueCount = 0;
		for (int i = 0; i < arrayOfBoolean.length; i++)
		{
			// Get count of true elements
			if (arrayOfBoolean[i])
				nTrueCount++;

			// If the number of true elements is greater than or equal to the desire number, then return as no
			// work is necessary
			if (nTrueCount >= numberTrue)
				return;
		}

		// NOTE: This may take some time depending on the number of true elements desired v.s. the available
		// elements
		int nAttempts = 0;
		while (true)
		{
			// Randomly pick an element
			int nIndex = randomRange(0, arrayOfBoolean.length - 1);

			// If element is false, then make it true
			if (!arrayOfBoolean[nIndex])
			{
				arrayOfBoolean[nIndex] = true;
				nTrueCount++;
			}

			// Have we satisfied the desired number of true elements?
			if (nTrueCount >= numberTrue)
				return;

			// Prevent infinite loop
			nAttempts++;
			if (nAttempts > nMaxTries)
				return;
		}
	}

	/**
	 * Modifies the array to contain at least 1 true element by picking random elements that
	 * are false and making them true. (This method will stop after 1000 tries.)
	 * 
	 * @param arrayOfBoolean - Array with the element to be modified
	 */
	public static void atLeastTrue(boolean[] arrayOfBoolean)
	{
		atLeastTrue(arrayOfBoolean, 1, 1000);
	}

	/**
	 * Modifies the list to contain at least the number of true elements by picking random elements that
	 * are false (& not to be skipped) and making them true. (This method will stop after the max number of
	 * tries is reached.)
	 * 
	 * @param checkboxes - List of Check Boxes to work with and modify
	 * @param numberTrue - Stop when at least this number of true elements
	 * @param nMaxTries - Max attempts to achieve the desired number of true elements
	 */
	public static void atLeastTrue(List<CheckBox> checkboxes, int numberTrue, int nMaxTries)
	{
		int nTrueCount = 0;
		for (CheckBox item : checkboxes)
		{
			// Get count of true elements that are not going to be skipped
			if (!item.skip && item.check)
				nTrueCount++;

			// If the number of true elements is greater than or equal to the desired number, then return as
			// no work is necessary
			if (nTrueCount >= numberTrue)
				return;
		}

		// NOTE: This may take some time depending on the number of true elements desired v.s. the available
		// elements
		int nAttempts = 0;
		while (true)
		{
			// Randomly pick an element
			int nIndex = randomRange(0, checkboxes.size() - 1);

			// Need to ensure not skipping the field
			if (!checkboxes.get(nIndex).skip && !checkboxes.get(nIndex).check)
			{
				checkboxes.get(nIndex).check = true;
				nTrueCount++;
			}

			// Have we satisfied the desired number of true elements?
			if (nTrueCount >= numberTrue)
				return;

			// Prevent infinite loop
			nAttempts++;
			if (nAttempts > nMaxTries)
				return;
		}
	}

	/**
	 * Modifies the list to contain at least 1 true element by picking random elements that
	 * are false (& not to be skipped) and making them true. (This method will stop after 1000 tries.)
	 * 
	 * @param checkboxes - List of Check Boxes to work with and modify
	 */
	public static void atLeastTrue(List<CheckBox> checkboxes)
	{
		atLeastTrue(checkboxes, 1, 1000);
	}

	/**
	 * Returns a list of CheckBox with at least the specified number of elements being true (if possible)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The number the true elements should be less than the List Size<BR>
	 * 
	 * @param size - List size to return
	 * @param numberTrue - The least number of true elements in the list (if possible)
	 * @return List&lt;CheckBox&gt;
	 */
	public static List<CheckBox> atLeastTrue(int size, int numberTrue)
	{
		List<CheckBox> checkboxes = new ArrayList<CheckBox>();

		int nTrueCount = 0;
		for (int i = 0; i < size; i++)
		{
			boolean bValue = randomBoolean();
			checkboxes.add(new CheckBox(bValue));
			if (bValue)
				nTrueCount++;
		}

		// If the number of true is at least the desire amount, then we are done
		if (nTrueCount >= numberTrue)
			return checkboxes;

		// Make the first X options that are false to be true to satisfy the number of true desired
		// Note: If there is an invalid size for the true elements desired, then the array may end up all true
		// but not satisfying the number of true condition
		for (int i = 0; i < size; i++)
		{
			// Note: Based on constructor used bSkip will always be false
			if (!checkboxes.get(i).check)
			{
				checkboxes.get(i).check = true;
				nTrueCount++;
			}

			if (nTrueCount >= numberTrue)
				return checkboxes;
		}

		return checkboxes;
	}

	/**
	 * Returns an list of CheckBox with at least 1 element being true (and bSkip = false)
	 * 
	 * @param size - List size to return
	 * @return Returns a list of CheckBox with at least 1 element being true
	 */
	public static List<CheckBox> atLeastTrue(int size)
	{
		return atLeastTrue(size, 1);
	}

	/**
	 * Returns a CheckBox that is set to random selection
	 * 
	 * @return CheckBox
	 */
	public static CheckBox randomCheckBox()
	{
		return CheckBox.getRandom();
	}

	/**
	 * Returns a DropDown that is set to random selection
	 * 
	 * @param nMinRandomIndex - Minimum Random Index for selection
	 * @return DropDown
	 */
	public static DropDown randomDropDown(int nMinRandomIndex)
	{
		return DropDown.getRandom(nMinRandomIndex);
	}

	/**
	 * Returns an InputField that is set to random input
	 * 
	 * @param sRandValue - Random Value
	 * @return InputField
	 */
	public static InputField randomInputField(String sRandValue)
	{
		return InputField.getRandom(sRandValue);
	}

	/**
	 * Returns a GenericDate based on the current date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For dates in the past use negative values<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) randomGenericDate(0, 0) => current day<BR>
	 * 2) randomGenericDate(1, 10) => current day + 1 to 10 days<BR>
	 * 3) randomGenericDate(-1, -10) => 1 to 10 days before the current day<BR>
	 * 4) randomGenericDate(-10, 10) => plus or minus up to 10 days from the current day<BR>
	 * 
	 * @param nMin - Min days to add
	 * @param nMax - Max days to add
	 * @return GenericDate
	 */
	public static GenericDate randomGenericDate(int nMin, int nMax)
	{
		return GenericDate.getRandomDate(nMin, nMax);
	}

	/**
	 * Returns a random string in the specified range size with only alphabetic characters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Minimum & Maximum values need to be greater than zero<BR>
	 * 2) The range is not affected if Minimum is greater than Maximum<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) letters(3, 5) could return an alphabetic string of size 3, 4 or 5 such as "abCd"<BR>
	 * 2) letters(5, 3) could return an alphabetic string of size 3, 4 or 5 such as "Zap"<BR>
	 * 
	 * @param nMin - Minimum value (inclusive)
	 * @param nMax - Maximum value (inclusive)
	 * @return Random String of only Alphabetic characters that is in the specified range size
	 */
	public static String letters(int nMin, int nMax)
	{
		int nLength = randomRange(nMin, nMax);
		return letters(nLength);
	}

	/**
	 * Returns a random string that only consists of numbers and starts with a non-zero number<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Minimum & Maximum values need to be greater than zero<BR>
	 * 2) The range is not affected if Minimum is greater than Maximum<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) numbers(3, 5) could return a numbers string starting with non-zero number of size 3, 4 or 5 such as
	 * "159"<BR>
	 * 2) numbers(5, 3) could return a numbers string starting with non-zero number of size 3, 4 or 5 such as
	 * "9510"<BR>
	 * 
	 * @param nMin - Minimum value (inclusive)
	 * @param nMax - Maximum value (inclusive)
	 * @return String that begins with non-zero number and is only numbers in the specified range size
	 */
	public static String numbers(int nMin, int nMax)
	{
		int nLength = randomRange(nMin, nMax);
		return numbers(nLength);
	}

	/**
	 * Returns an alphanumeric string that starts with a letter in the specified range size<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Minimum & Maximum values need to be greater than zero<BR>
	 * 2) The range is not affected if Minimum is greater than Maximum<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) alphanumeric(3, 5) could return an alphanumeric string starting with a letter of size 3, 4 or 5 such
	 * as "A159"<BR>
	 * 2) alphanumeric(5, 3) could return an alphanumeric string starting with a letter of size 3, 4 or 5 such
	 * as "Z9145"<BR>
	 * 
	 * @param nMin - Minimum value (inclusive)
	 * @param nMax - Maximum value (inclusive)
	 * @return String that begins with a letter and only consists of alphanumeric characters in the specified
	 *         range size
	 */
	public static String alphanumeric(int nMin, int nMax)
	{
		int nLength = randomRange(nMin, nMax);
		return alphanumeric(nLength);
	}

	/**
	 * Returns a random string of the specified size with only characters using the specified characters
	 * 
	 * @param nLength - Size of String to return
	 * @param chars - Characters that can be selected
	 * @return Random String of only specified characters that is of specified length
	 */
	public static String onlyChars(int nLength, String chars)
	{
		return RandomStringUtils.random(nLength, chars);
	}

	/**
	 * Returns a random string of the specified size with only alphabetic characters using the extended
	 * letters
	 * 
	 * @param nLength - Size of String to return
	 * @return Random String of only extended Alphabetic characters that is of specified length
	 */
	public static String extendedLetters(int nLength)
	{
		return onlyChars(nLength, sUsedExtendedLetters);
	}

	/**
	 * Returns a random string of the specified size with only numbers using the extended numbers
	 * 
	 * @param nLength - Size of String to return
	 * @return Random String of only extended number characters that is of specified length
	 */
	public static String extendedNumbers(int nLength)
	{
		return onlyChars(nLength, sUsedExtendedNumbers);
	}

	/**
	 * Returns a random string in the specified range size with only alphabetic characters using the extended
	 * letters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Minimum & Maximum values need to be greater than zero<BR>
	 * 2) The range is not affected if Minimum is greater than Maximum<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) letters(3, 5) could return an alphabetic string of size 3, 4 or 5 such as "abCd"<BR>
	 * 2) letters(5, 3) could return an alphabetic string of size 3, 4 or 5 such as "Zap"<BR>
	 * 
	 * @param nMin - Minimum value (inclusive)
	 * @param nMax - Maximum value (inclusive)
	 * @return Random String of only extended Alphabetic characters that is in the specified range size
	 */
	public static String extendedLetters(int nMin, int nMax)
	{
		int nLength = randomRange(nMin, nMax);
		return extendedLetters(nLength);
	}

	/**
	 * Returns a random string that only consists of extended numbers and starts with a non-zero number<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Minimum & Maximum values need to be greater than zero<BR>
	 * 2) The range is not affected if Minimum is greater than Maximum<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) numbers(3, 5) could return a numbers string starting with non-zero number of size 3, 4 or 5 such as
	 * "159"<BR>
	 * 2) numbers(5, 3) could return a numbers string starting with non-zero number of size 3, 4 or 5 such as
	 * "9510"<BR>
	 * 
	 * @param nMin - Minimum value (inclusive)
	 * @param nMax - Maximum value (inclusive)
	 * @return String that begins with non-zero number and is only extended numbers in the specified range
	 *         size
	 */
	public static String extendedNumbers(int nMin, int nMax)
	{
		int nLength = randomRange(nMin, nMax);
		return extendedNumbers(nLength);
	}

	/**
	 * Returns an alphanumeric string that starts with a letter of the specified size using the extended
	 * letters
	 * 
	 * @param nLength - Size of String to return
	 * @return String that begins with a letter and only consists of extended alphanumeric characters of
	 *         specified length
	 */
	public static String extendedAlphanumeric(int nLength)
	{
		String nonNumber = extendedLetters(1);
		String rest = "";
		if (nLength - 1 > 0)
			rest = onlyChars(nLength - 1, sUsedExtendedLetters + sUsedExtendedNumbers);

		return nonNumber + rest;
	}

	/**
	 * Returns an alphanumeric string that starts with a letter in the specified range size using the extended
	 * letters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Minimum & Maximum values need to be greater than zero<BR>
	 * 2) The range is not affected if Minimum is greater than Maximum<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) alphanumeric(3, 5) could return an alphanumeric string starting with a letter of size 3, 4 or 5 such
	 * as "A159"<BR>
	 * 2) alphanumeric(5, 3) could return an alphanumeric string starting with a letter of size 3, 4 or 5 such
	 * as "Z9145"<BR>
	 * 
	 * @param nMin - Minimum value (inclusive)
	 * @param nMax - Maximum value (inclusive)
	 * @return String that begins with a letter and only consists of extended alphanumeric characters in the
	 *         specified range size
	 */
	public static String extendedAlphanumeric(int nMin, int nMax)
	{
		int nLength = randomRange(nMin, nMax);
		return extendedAlphanumeric(nLength);
	}

	/**
	 * Replace the specified number of characters using the random values from the available specified
	 * characters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns input string immediately if it is null/empty or num < 1 or chars is null/empty<BR>
	 * 
	 * @param input - String to replace random characters in
	 * @param num - Number of characters to replace
	 * @param chars - Characters to be used as replacement characters
	 * @return String
	 */
	public static String replace(String input, int num, String chars)
	{
		if (Compare.equals(input, "", Comparison.Equal) || num < 1
				|| Compare.equals(chars, "", Comparison.Equal))
		{
			return input;
		}

		// Initialize working variable
		StringBuilder working = new StringBuilder(input);

		// Need to ensure that number of characters to be replaced is less than the input string length
		int nSize = num;
		if (num > input.length())
			nSize = input.length();

		// Get an array of unique index values to be replaced and process
		int[] indice = uniqueRandom(nSize, input.length());
		for (int i = 0; i < indice.length; i++)
		{
			int nIndex = indice[i];
			working.delete(nIndex, nIndex + 1);
			working.insert(nIndex, onlyChars(1, chars));
		}

		return working.toString();
	}

	/**
	 * Replace the specified number of characters using the random values from the available specified
	 * characters in the InputField object (<B>updated</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) InputField object cannot be null<BR>
	 * 
	 * @param field - InputField object to be <B>updated</B>
	 * @param num - Number of characters to replace
	 * @param chars - Characters to be used as replacement characters
	 */
	public static void replace(InputField field, int num, String chars)
	{
		if (field.useRandomValue())
		{
			field.randomValue = replace(field.randomValue, num, chars);
		}
		else
		{
			field.value = replace(field.value, num, chars);
		}
	}

	/**
	 * Insert the specified number of characters using the random values from the available specified
	 * characters into random positions<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns input string immediately if it is null/empty or num < 1 or chars is null/empty<BR>
	 * 
	 * @param input - String to insert random characters in
	 * @param num - Number of characters to insert
	 * @param chars - Characters available for insertion
	 * @return String
	 */
	public static String insert(String input, int num, String chars)
	{
		if (input == null || num < 1 || Compare.equals(chars, "", Comparison.Equal))
		{
			return input;
		}

		StringBuilder working = new StringBuilder(input);
		for (int i = 0; i < num; i++)
		{
			int max = working.length();
			if (max < 0)
				max = 0;

			int nIndex = randomRange(0, max);
			working.insert(nIndex, onlyChars(1, chars));
		}

		return working.toString();
	}

	/**
	 * Randomize the specified value
	 * 
	 * @param value - String to be randomized
	 * @return Empty String if null or empty else String characters in a random order
	 */
	public static String randomize(String value)
	{
		if (value == null || value.equals(""))
			return "";

		// A list of all the characters to randomize
		List<Character> characters = new ArrayList<Character>();
		for (int i = 0; i < value.length(); i++)
		{
			characters.add(value.charAt(i));
		}

		// Randomize the list to generate a randomized string
		Collections.shuffle(characters);

		// Re-construct String
		StringBuffer sb = new StringBuffer();
		for (Character item : characters)
		{
			sb.append(item.toString());
		}

		return sb.toString();
	}

	/**
	 * Generate an unique alphanumeric string as compared to the specified string
	 * 
	 * @param value - String that is not to be duplicated
	 * @param maxRetries - Max Retries to generate the unique string
	 * @return null if unable to generate an unique alphanumeric string in max retries else unique
	 *         alphanumeric string
	 */
	public static String uniqueAlphanumeric(String value, int maxRetries)
	{
		if (value == null || value.equals(""))
			return null;

		for (int i = 0; i < maxRetries; i++)
		{
			String unique = alphanumeric(value.length());
			if (!value.equals(unique))
				return unique;
		}

		return null;
	}

	/**
	 * Generate an unique letters only string as compared to the specified string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The value does not need to be a letters only string<BR>
	 * 
	 * @param value - String that is not to be duplicated
	 * @param maxRetries - Max Retries to generate the unique string
	 * @return null if unable to generate an unique letters only string in max retries else unique letters
	 *         only string
	 */
	public static String uniqueLetters(String value, int maxRetries)
	{
		if (value == null || value.equals(""))
			return null;

		for (int i = 0; i < maxRetries; i++)
		{
			String unique = letters(value.length());
			if (!value.equals(unique))
				return unique;
		}

		return null;
	}

	/**
	 * Generate an unique numbers only string as compared to the specified string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The value does not need to be a numbers only string<BR>
	 * 
	 * @param value - String that is not to be duplicated
	 * @param maxRetries - Max Retries to generate the unique string
	 * @return null if unable to generate an unique numbers only string in max retries else unique numbers
	 *         only string
	 */
	public static String uniqueNumbers(String value, int maxRetries)
	{
		if (value == null || value.equals(""))
			return null;

		for (int i = 0; i < maxRetries; i++)
		{
			String unique = numbers(value.length());
			if (!value.equals(unique))
				return unique;
		}

		return null;
	}

	/**
	 * Generate an unique alphanumeric string as compared to the specified string
	 * 
	 * @param value - String that is not to be duplicated
	 * @return null if unable to generate an unique alphanumeric string in max retries else unique
	 *         alphanumeric string
	 */
	public static String uniqueAlphanumeric(String value)
	{
		return uniqueAlphanumeric(value, 1000);
	}

	/**
	 * Generate an unique letters only string as compared to the specified string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The value does not need to be a letters only string<BR>
	 * 
	 * @param value - String that is not to be duplicated
	 * @return null if unable to generate an unique letters only string in max retries else unique letters
	 *         only string
	 */
	public static String uniqueLetters(String value)
	{
		return uniqueLetters(value, 1000);
	}

	/**
	 * Generate an unique numbers only string as compared to the specified string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The value does not need to be a numbers only string<BR>
	 * 
	 * @param value - String that is not to be duplicated
	 * @param maxRetries - Max Retries to generate the unique string
	 * @return null if unable to generate an unique numbers only string in max retries else unique numbers
	 *         only string
	 */
	public static String uniqueNumbers(String value)
	{
		return uniqueNumbers(value, 1000);
	}
}
