package com.automation.ui.common.utilities;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.UploadFileData;
import com.automation.ui.common.exceptions.GenericUnexpectedException;

/**
 * This class is for miscellaneous functions (i.e. they would be out of place in other classes.)
 */
public class Misc {

	/**
	 * Character to escape a metacharacter in a regular expression
	 */
	private static String escapeCharacter = "\\";

	/**
	 * There are 12 characters with special meanings in regular expressions: the backslash \, the caret ^, the
	 * dollar sign $, the period or dot ., the vertical bar or pipe symbol |, the question mark ?, the
	 * asterisk or star *, the plus sign +, the opening parenthesis (, the closing parenthesis ), and the
	 * opening square bracket [, the opening curly brace {, These special characters are often called
	 * "metacharacters". If you want to use any of these characters as a literal in a regex, you need to
	 * escape them with a backslash
	 */
	private static String[] metacharacters = new String[] { "^", "$", ".", "|", "?", "*", "+", "(", "[", "{",
			")", "}", "]" };

	/**
	 * Generic string to convert the xpath 2.0 function ends-with to the equivalent function using xpath 1.0
	 * functions.
	 */
	private static final String _EndsWith = "REPLACE002 = substring(REPLACE001, string-length(REPLACE001) - string-length(REPLACE002) + 1)";

	/**
	 * Adds the property to the System properties.<BR>
	 * <BR>
	 * Note: Only unique properties will be added.
	 * 
	 * @param sKey - Property to add
	 * @param sValue - Value for the property
	 * @return true if property added else false
	 */
	public static boolean addProperty(String sKey, String sValue)
	{
		String sExistingPropertyValue = System.getProperty(sKey);
		if (sExistingPropertyValue == null)
		{
			Properties applicationProps = new Properties(System.getProperties());
			applicationProps.put(sKey, sValue);
			System.setProperties(applicationProps);
			return true;
		}
		else
			return false;
	}

	/**
	 * This function will check & creates the folders as necessary.
	 * 
	 * @param sFolder - Checks/Creates these folders
	 * @return true if folder(s) exist after execution else false
	 */
	public static boolean bCheckCreateFolder(String sFolder)
	{
		// Check if the folder(s) already exist
		boolean bExists = (new File(sFolder)).exists();
		if (bExists)
			return true;

		// Folder(s) do not exist. So, create them.
		boolean bCreate = (new File(sFolder)).mkdirs();
		if (bCreate)
			return true;
		else
		{
			Logs.log.error("Could not create all folders");
			return false;
		}
	}

	/**
	 * Check if any of the tokens to be replaced exist in a specified string.
	 * 
	 * @param sSearch - String to search
	 * @param Tokens - Tokens to look for
	 * @return
	 */
	public static boolean bTokensToReplace(String sSearch, String[] Tokens)
	{
		for (int i = 0; i < Tokens.length; i++)
		{
			if (sSearch.contains(Tokens[i]))
				return true;
		}

		return false;
	}

	/**
	 * Replaces all tokens in the text with corresponding replacement.
	 * 
	 * @param sText - Text to replace all tokens
	 * @param Tokens - Tokens to look for
	 * @param Replacements - Replacement text for the token
	 * @return
	 */
	public static String replaceTokens(String sText, String[] Tokens, String[] Replacements)
	{
		try
		{
			/*
			 * If number of tokens & what to replace with do not match just return the text without
			 * modification
			 */
			if (Tokens.length != Replacements.length)
				return sText;

			// Go through each token and replace
			String sTemp = sText;
			for (int i = 0; i < Tokens.length; i++)
			{
				sTemp = sTemp.replace(Tokens[i], Replacements[i]);
			}

			return sTemp;
		}
		catch (Exception ex)
		{
			return sText;
		}
	}

	/**
	 * Replaces only the 1st occurrence of each token in the text with corresponding token.<BR>
	 * <BR>
	 * Note: Each token is considered as a regular expression. So, escaping of special characters is required
	 * to achieve expected results.<BR>
	 * <BR>
	 * Commonly used special characters:
	 * 
	 * <PRE>
	 *     ? (question mark) => \?
	 *     * (star)          => \*
	 *     . (period)        => \.
	 * </PRE>
	 * 
	 * <BR>
	 * 
	 * @param sText - Text to replace only the 1st occurrence of each token
	 * @param Tokens - Tokens as regular expressions to look for
	 * @param Replacements - Replacement text for the token
	 * @return
	 */
	public static String replace1stMatch(String sText, String[] Tokens, String[] Replacements)
	{
		try
		{
			/*
			 * If number of tokens & what to replace with do not match just return the text without
			 * modification
			 */
			if (Tokens.length != Replacements.length)
				return sText;

			// Go through each token and replace only the 1st occurrence
			String sTemp = sText;
			for (int i = 0; i < Tokens.length; i++)
			{
				sTemp = sTemp.replaceFirst(Tokens[i], Replacements[i]);
			}

			return sTemp;
		}
		catch (Exception ex)
		{
			return sText;
		}
	}

	/**
	 * This function changes the given xpath to uppercase/lowercase but leaves the strings in the xpath
	 * unchanged.<BR>
	 * <BR>
	 * This function is necessary when using an HTML parser and xpaths fail due to case sensitivity. (I have
	 * found this to be the case when using Cobra HTML.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * If there is a opening delimiter but no matching close delimiter, then only part of the (invalid) xpath
	 * will be converted
	 * properly. The reason for this is I am using a flag to know whether to conversion is necessary which is
	 * switched on/off after each delimiter.<BR>
	 * <BR>
	 * <B>Related Function:</B><BR>
	 * Framework.getText(WebDriver driver, String sXpath) to be used in conjuction with this function.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) You have a lowercase xpath <B>//div[@id='test']</B> but the actual HTML has all uppercase. Use
	 * xpathChangeCase("//div[@id='test']", "'", true) to get the uppercase xpath of <B>//DIV[@ID='test']</B> <BR>
	 * 2) You have an uppercase xpath <B>//DIV[@ID='test']</B> but the actual HTML has all lowercase. Use
	 * xpathChangeCase("//DIV[@ID='test']", "'", false) to get the lowercase xpath of <B>//div[@id='test']</B> <BR>
	 * 3) You have a lowercase xpath <B>//div[@id="test"]</B> but the actual HTML has all uppercase. Use
	 * xpathChangeCase("//div[@id=\"test\"]", "\"", true) to get the uppercase xpath of
	 * <B>//DIV[@ID="test"]</B> <BR>
	 * 4) You have an uppercase xpath <B>//DIV[@ID="test"]</B> but the actual HTML has all lowercase. Use
	 * xpathChangeCase("//DIV[@ID=\"test\"]", "\"", false) to get the lowercase xpath of
	 * <B>//div[@id="test"]</B> <BR>
	 * 
	 * @param sXpath - xpath to make uppercase/lowercase
	 * @param sDelimiter - Delimiter that indicates start/end of a string
	 * @param bToUppercase - true - uppercase; false - lowercase
	 * @return xpath string converted to uppercase/lowercase
	 */
	public static String xpathChangeCase(String sXpath, String sDelimiter, boolean bToUppercase)
	{
		String sConverted = "";
		int nBeginIndex = 0;
		int nEndIndex = sXpath.indexOf(sDelimiter, 0);

		// If cannot initially find the delimiter, then convert the entire string to uppercase/lowercase
		if (nEndIndex < 0)
		{
			if (bToUppercase)
				return sXpath.toUpperCase();
			else
				return sXpath.toLowerCase();
		}

		/*
		 * Need to convert on the xpath parts and not the strings. For example, //div[@id='test'] should
		 * become //DIV[@ID='test']
		 */
		boolean bXpathPart = true;
		boolean bStop = false;
		while (!bStop)
		{
			// Substring that may need to be made uppercase/lowercase
			String sValue = sXpath.substring(nBeginIndex, nEndIndex);

			/*
			 * 1) If xpath part of string, then convert to uppercase/lowercase.
			 * 2) Switch xpath part flag
			 */
			if (bXpathPart)
			{
				if (bToUppercase)
					sValue = sValue.toUpperCase();
				else
					sValue = sValue.toLowerCase();

				bXpathPart = false;
			}
			else
				bXpathPart = true;

			// Add converted string plus delimiter to string to return
			sConverted += sValue + sDelimiter;

			// Move the beginning index to the place where the next substring will be found
			nBeginIndex = nEndIndex + 1;

			// Find the next index of the delimiter
			nEndIndex = sXpath.indexOf(sDelimiter, nBeginIndex);

			// If we cannot find another index of the delimiter use the string length
			if (nEndIndex < 0)
				nEndIndex = sXpath.length();

			// If the beginning index >= string length, then we are complete
			if (nBeginIndex >= sXpath.length())
			{
				/*
				 * The delimiter is always added. However, this can cause the converted string to have an
				 * unwanted quote. So, we will remove if necessary.
				 */
				if (!sXpath.endsWith(sDelimiter))
					sConverted = sConverted.substring(0, sConverted.length() - 1);

				bStop = true;
			}
		}

		return sConverted;
	}

	/**
	 * Create a single line string that represents a node that spans 1 line. (Simplifies creating XML files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * createNode("RunID","8976") will return<BR>
	 * &lt;RunID>8976&lt;/RunID>
	 * 
	 * @param sNode - Node Name
	 * @param sValue - Value of node
	 * @return
	 */
	public static String createNode(String sNode, String sValue)
	{
		return "<" + sNode + ">" + sValue + "</" + sNode + ">";
	}

	/**
	 * Create a single line string that represents a node that spans 1 line. (Simplifies creating XML files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * createNode("Field","name","STATEMENT_DATE","2011-10-27") will return<BR>
	 * &lt;Field name="STATEMENT_DATE">2011-10-27&lt;/Field>
	 * 
	 * @param sNode - Node Name
	 * @param sAttr - Attribute Name
	 * @param sAttrValue - Attribute Value
	 * @param sValue - Value of node
	 * @return
	 */
	public static String createNode(String sNode, String sAttr, String sAttrValue, String sValue)
	{
		return "<" + sNode + " " + sAttr + "=\"" + sAttrValue + "\"" + ">" + sValue + "</" + sNode + ">";
	}

	/**
	 * Starts the node but does not complete the node. Use if there are child nodes. (Simplifies creating XML
	 * files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * startNode("RunID") will return<BR>
	 * &lt;RunID>
	 * 
	 * @param sNode - Node Name
	 * @return
	 */
	public static String startNode(String sNode)
	{
		return "<" + sNode + ">";
	}

	/**
	 * Starts the node but does not complete the node. Use if there are child nodes. (Simplifies creating XML
	 * files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * startNode("Field","name","STATEMENT_DATE") will return<BR>
	 * &lt;Field name="STATEMENT_DATE">
	 * 
	 * @param sNode - Node Name
	 * @param sAttr - Attribute Name
	 * @param sAttrValue - Attribute Value
	 * @return
	 */
	public static String startNode(String sNode, String sAttr, String sAttrValue)
	{
		return "<" + sNode + " " + sAttr + "=\"" + sAttrValue + "\"" + ">";
	}

	/**
	 * Starts the node but does not complete the node. Use if there are child nodes. (Simplifies creating XML
	 * files.)<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) startNode("Field", new String[][]{ {"a","1"}, {"b","2"}, false) will return<BR>
	 * &lt;Field a="1" b="2"><BR>
	 * 2) startNode("Field", new String[][]{ {"a","1"}, {"b","2"}, true) will return<BR>
	 * &lt;Field a="1" b="2"/><BR>
	 * 
	 * @param sNode - Node Name
	 * @param sAttr - For each attribute contains Attribute Name (sAttr[X][0]) & Attribute Value (sAttr[X][1])
	 * @param bNodeComplete - true to close the node using slash
	 * @return
	 */
	public static String startNode(String sNode, String[][] sAttr, boolean bNodeComplete)
	{
		String sAllAttributes = "";
		int nAttributes = sAttr.length;

		// For each attribute construct the xml
		for (int i = 0; i < nAttributes; i++)
		{
			String sAnotherAttribute = " " + sAttr[i][0] + "=\"" + sAttr[i][1] + "\"";
			sAllAttributes += sAnotherAttribute;
		}

		// Decide whether to close the tag on the same line using slash
		if (bNodeComplete)
			return "<" + sNode + sAllAttributes + "/>";
		else
			return "<" + sNode + sAllAttributes + ">";
	}

	/**
	 * Completes the node (Simplifies creating XML files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * completeNode("RunID") will return<BR>
	 * &lt;/RunID>
	 * 
	 * @param sNode - Node Name
	 * @return
	 */
	public static String completeNode(String sNode)
	{
		return "</" + sNode + ">";
	}

	/**
	 * Removes specified string from the end of the given string<BR>
	 * <BR>
	 * <B>Note:</B> If the given string has multiple sequences of the string only the last occurrence is
	 * removed<BR>
	 * <BR>
	 * <B>Examples: </B><BR>
	 * <table border="1">
	 * <tr>
	 * <td><B>Original String</B></td>
	 * <td><B>Ends With String to be removed</B></td>
	 * <td><B>Returned String</B></td>
	 * </tr>
	 * <tr>
	 * <td>//test/</td>
	 * <td>/</td>
	 * <td>//test</td>
	 * </tr>
	 * <tr>
	 * <td>something//</td>
	 * <td>/</td>
	 * <td>something/</td>
	 * </tr>
	 * <tr>
	 * <td>First NameTest</td>
	 * <td>Test</td>
	 * <td>First Name</td>
	 * </tr>
	 * <tr>
	 * <td>anotherTestTest</td>
	 * <td>Test</td>
	 * <td>anotherTest</td>
	 * </tr>
	 * </table>
	 * 
	 * @param sValue - String to work with
	 * @param sEndsWith - String to be removed from end of the given string
	 * @return
	 */
	public static String removeEndsWith(String sValue, String sEndsWith)
	{
		if (sValue == null || sEndsWith == null)
			return sValue;

		// Does the string end with what we want to remove?
		if (sValue.endsWith(sEndsWith))
		{
			return sValue.substring(0, sValue.length() - sEndsWith.length());
		}
		else
		{
			// No work to be done just return string
			return sValue;
		}
	}

	/**
	 * Removes specified string from the start of the given string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the given string starts with multiple sequences of the string only the 1st occurrence is removed<BR>
	 * 2) The option is applied to both strings before doing the comparison<BR>
	 * 3) Supported options are Lower, Upper. For any other option, it does a normal starts with<BR>
	 * 
	 * @param sValue - String to work with
	 * @param sStartsWith - String to be removed from start of the given string
	 * @param option - Option to modify the strings before the operation
	 * @return If sValue is null or prefix is null or does not start with the prefix, then sValue is returned.
	 *         If substring method causes an exception, then the empty string is returned else a string
	 *         without the prefix
	 */
	public static String removeStartsWith(String sValue, String sStartsWith, Comparison option)
	{
		if (sValue == null || sStartsWith == null)
			return sValue;

		String _Value, _StartsWith;
		if (option == Comparison.Lower)
		{
			_Value = Conversion.nonNull(sValue).toLowerCase();
			_StartsWith = Conversion.nonNull(sStartsWith).toLowerCase();
		}
		else if (option == Comparison.Upper)
		{
			_Value = Conversion.nonNull(sValue).toUpperCase();
			_StartsWith = Conversion.nonNull(sStartsWith).toUpperCase();
		}
		else
		{
			_Value = Conversion.nonNull(sValue);
			_StartsWith = Conversion.nonNull(sStartsWith);
		}

		// Does the string start with what we want to remove?
		if (_Value.startsWith(_StartsWith))
		{
			try
			{
				return _Value.substring(_StartsWith.length());
			}
			catch (Exception ex)
			{
				return "";
			}
		}
		else
		{
			// No work to be done just return string
			return _Value;
		}
	}

	/**
	 * Removes specified string from the start of the given string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the given string starts with multiple sequences of the string only the 1st occurrence is removed<BR>
	 * 
	 * @param sValue - String to work with
	 * @param sStartsWith - String to be removed from start of the given string
	 * @return If sValue is null or prefix is null or does not start with the prefix, then sValue is returned.
	 *         If substring method causes an exception, then the empty string is returned else a string
	 *         without the prefix
	 */
	public static String removeStartsWith(String sValue, String sStartsWith)
	{
		return removeStartsWith(sValue, sStartsWith, Comparison.Standard);
	}

	/**
	 * Get the specified System Property
	 * 
	 * @param sKey - the name of the system property
	 * @param sDefaultValue - Default value to return if error or property not found
	 * @return - sDefaultValue if System Property does not exist or is the empty string else the actual System
	 *         Property value
	 */
	public static String getProperty(String sKey, String sDefaultValue)
	{
		try
		{
			String sPropertyValue = System.getProperty(sKey);
			if (sPropertyValue == null || sPropertyValue.equals(""))
				return sDefaultValue;
			else
				return sPropertyValue;
		}
		catch (SecurityException s)
		{
			Logs.log.error(s);
			throw s;
		}
		catch (Exception ex)
		{
		}

		return sDefaultValue;
	}

	/**
	 * Replaces the last occurrence of the specified string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If sLast is the empty string, then sValue is returned<BR>
	 * 
	 * @param sValue - String to work with
	 * @param sLast - Last occurrence of string to remove
	 * @param sReplaceWith - Replacement string
	 * @return
	 */
	public static String replaceLast(String sValue, String sLast, String sReplaceWith)
	{
		if (sValue == null || sLast == null || sLast.equals(""))
			return sValue;

		int nLastIndex = sValue.lastIndexOf(sLast);
		if (nLastIndex == -1)
		{
			// No work to be done just return string
			return sValue;
		}
		else if (nLastIndex == 0)
		{
			if (sValue.length() == sLast.length())
				return sReplaceWith;
			else
				return sReplaceWith + sValue.substring(sLast.length(), sValue.length());
		}
		else
		{
			// The part of the string before the last occurrence
			String sPart1 = sValue.substring(0, nLastIndex);

			// Does the string end with the last occurrence?
			if (nLastIndex + sLast.length() >= sValue.length())
				return sPart1 + sReplaceWith;
			else
				return sPart1 + sReplaceWith + sValue.substring(nLastIndex + sLast.length(), sValue.length());
		}
	}

	/**
	 * Replaces only the 1st occurrence of each token in the text with corresponding token.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Each token is considered as a <B>regular expression</B>. So, escaping of special characters is
	 * required to achieve expected results.<BR>
	 * 2) Parameter.param = Token & Parameter.value = Replacement text<BR>
	 * <BR>
	 * <B>Commonly used special characters:</B><BR>
	 * 
	 * <PRE>
	 *     ? (question mark) => \?
	 *     * (star)          => \*
	 *     . (period)        => \.
	 * </PRE>
	 * 
	 * <BR>
	 * 
	 * @param sText - Text to replace only the 1st occurrence of each token
	 * @param items - Tokens (as regular expressions to look for) & Replacement text for the token
	 * @return String
	 */
	public static String replace1stMatch(String sText, List<Parameter> items)
	{
		try
		{
			String sTemp = sText;
			for (Parameter p : items)
			{
				sTemp = sTemp.replaceFirst(p.param, p.value);
			}

			return sTemp;
		}
		catch (Exception ex)
		{
			return sText;
		}
	}

	/**
	 * Returns a string with all metacharacters for regular expressions escaped<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use method if you have a string that may contain special characters (metacharacters) and it is to be
	 * used in a method as a parameter that is a regular expression but you don't want it to be treated as a
	 * regular expression<BR>
	 * 
	 * @param sValue - String for which to escape the metacharacters
	 * @return non-null and metacharacters are escaped
	 */
	public static String escapeForRegEx(String sValue)
	{
		String sWorking = Conversion.nonNull(sValue);

		// Replace the backslash
		sWorking = sWorking.replace(escapeCharacter, escapeCharacter + escapeCharacter);

		// Replace the remaining metacharacters
		for (int i = 0; i < metacharacters.length; i++)
		{
			sWorking = sWorking.replace(metacharacters[i], escapeCharacter + metacharacters[i]);
		}

		return sWorking;
	}

	/**
	 * Creates string by repeating given text specified number of times<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null is converted to empty string<BR>
	 * 2) empty string is returned if nTimes &lt; 1<BR>
	 * 
	 * @param sText - Text that will be repeated
	 * @param nTimes - Number of times to repeat text
	 * @return non-null
	 */
	public static String repeat(String sText, int nTimes)
	{
		String sWorking = "";
		for (int i = 0; i < nTimes; i++)
		{
			sWorking += Conversion.nonNull(sText);
		}

		return sWorking;
	}

	/**
	 * Takes a List of strings and converts to a single string for logging purposes
	 * 
	 * @param values - List of strings
	 * @param separator - Each string will be separated by this string
	 * @return String
	 */
	public static String toString(List<String> values, String separator)
	{
		String sLog = "";

		for (String item : values)
		{
			sLog += item + separator;
		}

		return replaceLast(sLog, separator, "");
	}

	/**
	 * Compacts series of multiple spaces in the given text to a single space<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) HTML pages have spaces compacted unless it is written as <B>&amp;nbsp;</B><BR>
	 * 2) Use this method when it is necessary to compare text that may contain multiple spaces that are not
	 * escaped.<BR>
	 * 
	 * @param sText - String to have spaces compacted
	 * @return non-null string with two or more spaces in a row compacted to a single space
	 */
	public static String compact(String sText)
	{
		if (sText == null)
			return "";
		else
			return sText.replaceAll(" {2,}", " ");
	}

	/**
	 * Returns a xpath 1.0 that is equivalent to using the xpath 2.0 function ends-with(str1, str2) which is
	 * str2 = substring(str1, string-length(str1) - string-length(str2) + 1)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null values are not supported<BR>
	 * 2) str2 is replaced first, then str1 is replaced<BR>
	 * 3) The variable needs to include the quotes (or double quotes) as necessary<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * 1) Test if attribute id of node ends with 'BLAH' => endsWith("@id", "'BLAH'") => @id = substring(@id,
	 * string-length(@id) - string-length('BLAH') + 1)<BR>
	 * 2) Test if the string 'Something' ends with 'thing' => endsWith("'Something'", "'thing'") =>
	 * 'thing' = substring('Something', string-length('Something') - string-length('thing') + 1)<BR>
	 * 
	 * @param _STR1 - str1
	 * @param _STR2 - str2
	 * @return "_STR2 = substring(_STR1, string-length(_STR1) - string-length(_STR2) + 1)"
	 */
	public static String endsWith(String _STR1, String _STR2)
	{
		// Replace _STR2 first as it could match part of _STR1
		String step1 = _EndsWith.replace("REPLACE002", _STR2);

		// Replace all occurrences of _STR1
		String step2 = step1.replace("REPLACE001", _STR1);
		return step2;
	}

	/**
	 * Get the months & corresponding integer representation
	 * 
	 * @param locale - Locale to get the months
	 * @param bVerifyMonths - true to verify there are 12 months
	 * @return HashMap&lt;Integer,String&gt;
	 * @throws GenericUnexpectedException if verifying the month index values fail
	 */
	public static HashMap<Integer, String> getMonths(Locale locale, boolean bVerifyMonths)
	{
		HashMap<Integer, String> data = new HashMap<Integer, String>();

		// Get the names of the months in the locale
		Calendar c1 = Calendar.getInstance();
		Map<String, Integer> months = c1.getDisplayNames(Calendar.MONTH, Calendar.LONG, locale);

		// Verify the months if specified
		boolean bError = false;
		if (bVerifyMonths)
		{
			for (int i = 0; i <= 11; i++)
			{
				if (!months.containsValue(i))
				{
					bError = true;
					Logs.log.warn("Missing month index value:  " + i);
				}
			}
		}

		if (bError)
		{
			Logs.logError("There were some missing month indexes.  See details above.");
		}

		// Each integer corresponding to the month is zero based as such we will add 1 to make it 1 based
		// which is better to work with for most purposes
		Set<String> keys = months.keySet();
		for (String key : keys)
		{
			data.put(months.get(key) + 1, key);
		}

		return data;
	}

	/**
	 * Get the months & corresponding integer representation<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It is possible the the returned map does not contain all months. Use overloaded method if it is
	 * necessary to ensure all months in map.<BR>
	 * 
	 * @param locale - Locale to get the months
	 * @return HashMap&lt;Integer,String&gt;
	 */
	public static HashMap<Integer, String> getMonths(Locale locale)
	{
		return getMonths(locale, false);
	}

	/**
	 * Reads any text file and returns it as a string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) File must be a plain text file like a JavaScript file or SQL file for the string to be usable<BR>
	 * 
	 * @param sTextFile - Text File to be read
	 * @return String
	 */
	public static String readFile(String sTextFile)
	{
		SQL_FileReader reader = new SQL_FileReader();
		reader.setFile(sTextFile);
		reader.readFile();
		return reader.getSQL();
	}

	/**
	 * Removes all non-digits from the string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses regular expression (\D) to remove all non-digits which includes minus & period<BR>
	 * 
	 * @param value - String to remove all non-digits
	 * @return non-null String with only digits
	 */
	public static String removeNonDigits(String value)
	{
		return Conversion.nonNull(value).replaceAll("\\D", "");
	}

	/**
	 * Determines if a delay is necessary to ensure a unique ID (or something else that a delay makes senses)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) No delay if size is less than one<BR>
	 * 2) No delay if index is zero and size is one<BR>
	 * 3) No delay if index + 1 greater than equal to size<BR>
	 * 4) In all other cases, a delay occurs<BR>
	 * 
	 * @param index - Current Index
	 * @param size - Size of the list
	 */
	public static void delay(int index, int size)
	{
		delay(index, size, Framework.getPollInterval());
	}

	/**
	 * Determines if a delay is necessary to ensure a unique ID (or something else that a delay makes senses)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) No delay if size is less than one<BR>
	 * 2) No delay if index is zero and size is one<BR>
	 * 3) No delay if index + 1 greater than equal to size<BR>
	 * 4) In all other cases, a delay occurs<BR>
	 * 
	 * @param index - Current Index
	 * @param size - Size of the list
	 * @param delay - Delay in milliseconds (if necessary)
	 */
	public static void delay(int index, int size, int delay)
	{
		// If no items in the list, then no need to sleep
		if (size < 1)
			return;

		// If only 1 item in the list, then no need to sleep
		if (index == 0 && size == 1)
			return;

		// If last item in the list, then no need to sleep
		if (index + 1 >= size)
			return;

		// Delay to ensure Unique ID
		Framework.sleep(delay);
	}

	/**
	 * Sets the dynamic properties for the files to be uploaded<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Sets the dynamic properties of File Size & Unique ID<BR>
	 * 2) Sleeps for 1 second between items as necessary to generic unique ID based on time<BR>
	 * 
	 * @param files - List of files to be uploaded
	 * @param updateAlias - true to update the alias field to use the generated unique ID
	 */
	public static void setDynamicProperties(List<UploadFileData> files, boolean updateAlias)
	{
		for (int i = 0; i < files.size(); i++)
		{
			files.get(i).setSize();
			files.get(i).setUniqueID();
			if (updateAlias)
				files.get(i).setAliasToUseUniqueID();

			// Delay to ensure Unique ID
			delay(i, files.size(), 1000);
		}
	}

	/**
	 * Replaces problem characters with other special characters.<BR>
	 * <BR>
	 * <B>Background:</B><BR>
	 * If the application is using a regular expression to determine which input could be malicious & it is
	 * applied to all fields without exception, then this can cause a problem when the field should allow the
	 * specified input. For example, any password field should not have the filter applied. However, due to
	 * this you can only use certain characters if they are followed by a space or encoded. In password fields
	 * this could be an issue as such you would want to replace with another special character and this method
	 * does this.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Special Character set is based on Rand.getSpecialDefaults() with the specified problem characters
	 * removed.<BR>
	 * 
	 * @param value - String to replace problem characters with other special characters
	 * @param problemChars - List of problem characters to be removed
	 * @return String that has any problem characters replaced with other special characters
	 */
	public static String replaceProblemCharacters(String value, List<String> problemChars)
	{
		//
		// Remove the problem special characters from the special character set
		//
		String sSpecialCharacterSet = Rand.getSpecialDefaults();
		for (String problem : problemChars)
		{
			sSpecialCharacterSet = sSpecialCharacterSet.replace(problem, "");
		}

		//
		// Go through and replace each problem character with another special character
		//
		String target = value;
		for (String problem : problemChars)
		{
			String replacement = Rand.special(1, sSpecialCharacterSet);
			target = target.replace(problem, replacement);
		}

		return target;
	}

	/**
	 * Use reflection to execute the method<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used to execute private/protected methods for unit testing purposes<BR>
	 * 2) The method to be executed cannot take any parameters<BR>
	 * 
	 * @param <T> - Object
	 * @param obj - Object to execute method on
	 * @param method - Method to be executed
	 * @return true if successful else false
	 */
	public static <T> boolean execute(T obj, String method)
	{
		try
		{
			Method m = obj.getClass().getDeclaredMethod(method);
			m.setAccessible(true);
			m.invoke(obj);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Gets all enumeration values except excluded values<BR>
	 * <BR>
	 * <B>Example: </B><BR>
	 * Languages[] values = (Languages[]) Misc.getValues(Languages.English, Languages.KEY);<BR>
	 * 
	 * @param e - Any Enumeration value
	 * @param exclude - enumeration values to be excluded from values returned
	 * @return enumeration values minus exclusions
	 */
	public static Enum<?>[] getValues(Enum<?> e, Enum<?>... exclude)
	{
		Enum<?>[] options = e.getDeclaringClass().getEnumConstants();
		for (Enum<?> item : exclude)
		{
			options = (Enum<?>[]) ArrayUtils.removeElement(options, item);
		}

		return options;
	}

	/**
	 * Generate log text for mismatch
	 * 
	 * @param expected - Expected number of items
	 * @param actual - Actual number of items
	 * @param itemLogName - Name of Item to log
	 * @return String
	 */
	public static String logMismatchText(int expected, int actual, String itemLogName)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Expected ");
		sb.append(expected);
		sb.append(" ");
		sb.append(itemLogName);
		sb.append(" but there were Actually ");
		sb.append(actual);
		sb.append(" ");
		sb.append(itemLogName);
		return sb.toString();
	}

	/**
	 * Generate log text for item not displayed
	 * 
	 * @param itemLogName - Name of Item to log
	 * @param obj - Object data to be written
	 * @return String
	 */
	public static <T> String logNotDisplayedText(String itemLogName, T obj)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(itemLogName);
		sb.append(" (");
		sb.append(obj);
		sb.append(") was not displayed");
		return sb.toString();
	}
}