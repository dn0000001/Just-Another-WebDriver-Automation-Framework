package com.automation.ui.common.utilities;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.time.DateUtils;

import com.automation.ui.common.dataStructures.Comparison;

/**
 * Class for conversion functions.
 */
public class Conversion {
	/**
	 * Returns converts a string to a boolean value<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * 1) Uses Boolean.parseBoolean and adds "1" is true & null as false<BR>
	 * 
	 * @param sValue - String to convert
	 * @return true if ("1" or "true") else false
	 */
	public static boolean parseBoolean(String sValue)
	{
		if (sValue == null)
			return false;

		if (sValue.equals("1"))
			return true;

		return Boolean.parseBoolean(sValue);
	}

	/**
	 * Returns the date as a string in specific format<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The JavaDoc for the SimpleDateFormat class contains the pattern information<BR>
	 * 
	 * @param value - Date to convert to string
	 * @param sToFormat - Format to convert to (ex. "yyyy-MM-dd")
	 * @return null if exception occurs else String
	 */
	public static String convertDate(Date value, String sToFormat)
	{
		try
		{
			SimpleDateFormat dateformat = new SimpleDateFormat(sToFormat);
			String sConverted = new String(dateformat.format(value));
			return sConverted;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Returns the date as a string in specific format & TimeZone<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use TimeZone.getTimeZone to get specific TimeZone<BR>
	 * 2) You can use TimeZone.getAvailableIDs() to get all available TimeZone IDs<BR>
	 * 3) The JavaDoc for the SimpleDateFormat class contains the pattern information<BR>
	 * 
	 * @param value - Date to convert to string
	 * @param sToFormat - Format to convert to (ex. "yyyy-MM-dd HH:mm:ss.SSS")
	 * @param tz - TimeZone of the date represented by the string
	 * @return null if exception occurs else String
	 */
	public static String convertDate(Date value, String sToFormat, TimeZone tz)
	{
		try
		{
			SimpleDateFormat dateformat = new SimpleDateFormat(sToFormat);
			dateformat.setTimeZone(tz);
			String sConverted = new String(dateformat.format(value));
			return sConverted;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Returns the string as an integer.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) -1 is returned for strings that cannot be converted.<BR>
	 * 
	 * @param sValue - String to convert
	 * @return -1 if string cannot be converted else integer value
	 */
	public static int parseInt(String sValue)
	{
		return parseInt(sValue, -1);
	}

	/**
	 * Returns the string as an integer.
	 * 
	 * @param sValue - String to convert
	 * @param nDefault - Default value to return if conversion fails
	 * @return nDefault if string cannot be converted else integer value
	 */
	public static int parseInt(String sValue, int nDefault)
	{
		try
		{
			return Integer.parseInt(sValue);
		}
		catch (Exception ex)
		{
			return nDefault;
		}
	}

	/**
	 * Converts a String to a Date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The JavaDoc for the SimpleDateFormat class contains the pattern information<BR>
	 * 
	 * @param sValue - String to convert
	 * @param sPattern - The pattern describing the date and time format (ex. "yyyy-MM-dd")
	 * @return null if exception else Date
	 */
	public static Date toDate(String sValue, String sPattern)
	{
		try
		{
			DateFormat formatter = new SimpleDateFormat(sPattern);
			Date date = (Date) formatter.parse(sValue);
			return date;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Converts a String in specific TimeZone to a Date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use TimeZone.getTimeZone to get specific TimeZone<BR>
	 * 2) You can use TimeZone.getAvailableIDs() to get all available TimeZone IDs<BR>
	 * 3) The JavaDoc for the SimpleDateFormat class contains the pattern information<BR>
	 * 
	 * @param sValue - String to convert
	 * @param sPattern - The pattern describing the date and time format (ex. "yyyy-MM-dd HH:mm:ss.SSS")
	 * @param tz - TimeZone of the date represented by the string
	 * @return null if exception else Date
	 */
	public static Date toDate(String sValue, String sPattern, TimeZone tz)
	{
		try
		{
			DateFormat formatter = new SimpleDateFormat(sPattern);
			formatter.setTimeZone(tz);
			Date date = (Date) formatter.parse(sValue);
			return date;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Converts a String in specific TimeZone and Locale to a Date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use TimeZone.getTimeZone to get specific TimeZone<BR>
	 * 2) You can use TimeZone.getAvailableIDs() to get all available TimeZone IDs<BR>
	 * 3) The JavaDoc for the SimpleDateFormat class contains the pattern information<BR>
	 * 
	 * @param value - String to convert
	 * @param pattern - The pattern describing the date and time format (ex. "yyyy-MM-dd HH:mm:ss.SSS")
	 * @param tz - TimeZone of the date represented by the string
	 * @param locale - Locale of pattern
	 * @return null if exception else Date
	 */
	public static Date toDate(String value, String pattern, TimeZone tz, Locale locale)
	{
		try
		{
			DateFormat formatter = new SimpleDateFormat(pattern, locale);
			formatter.setTimeZone(tz);
			Date date = (Date) formatter.parse(value);
			return date;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Adds/Subtracts specified days to the given Date and returns result as a String formatted as specified<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use the method <B>toDate</B> to convert a String if necessary<BR>
	 * 2) Can be used to convert a date object to a specific string format<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) addDays(new Date(), 1, "MM-dd-yyyy") => Tomorrow in format MM-dd-yyyy<BR>
	 * 2) addDays(toDate("2012-09-11","yyyy-MM-dd") , 1, "MM-dd-yyyy") => "09-12-2012"<BR>
	 * 3) addDays(new Date(), 0, "MM-dd-yyyy") => Today in format MM-dd-yyyy<BR>
	 * 4) addDays(toDate("2012-09-11","yyyy-MM-dd") , 0, "MM-dd-yyyy") => "09-11-2012"<BR>
	 * 
	 * @param workingDate - Date object to add/subtract days to
	 * @param nAmount - days to add (positive) /subtract (negative)
	 * @param sOutputFormat - Format to convert to (ex. "yyyy-MM-dd")
	 * @return <B>null</B> if exception else String formated based on Date object
	 */
	public static String addDays(Date workingDate, int nAmount, String sOutputFormat)
	{
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(sOutputFormat);
			Date convert = DateUtils.addDays(workingDate, nAmount);
			return dateFormat.format(convert);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Returns the string as a float.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Float.valueOf("-1") is returned for strings that cannot be converted.<BR>
	 * 
	 * @param sValue - String to convert
	 * @return Float.valueOf("-1") if string cannot be converted else float value
	 */
	public static float parseFloat(String sValue)
	{
		float fDefault = Float.valueOf("-1");
		return parseFloat(sValue, fDefault);
	}

	/**
	 * Returns the string as a float.
	 * 
	 * @param sValue - String to convert
	 * @param fDefault - Default value to return if conversion fails
	 * @return fDefault if string cannot be converted else float value
	 */
	public static float parseFloat(String sValue, float fDefault)
	{
		try
		{
			return Float.parseFloat(sValue);
		}
		catch (Exception ex)
		{
			return fDefault;
		}
	}

	/**
	 * If sValue is null, then it returns the empty string else sValue
	 * 
	 * @param sValue
	 * @return sValue or empty string
	 */
	public static String nonNull(String sValue)
	{
		if (sValue == null)
			return "";
		else
			return sValue;
	}

	/**
	 * If sValue is null, then it returns sInsteadOfNull else sValue<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method allows you to convert a null value into a default value for comparison<BR>
	 * 
	 * @param sValue
	 * @param sInsteadOfNull
	 * @return sValue or sInsteadOfNull
	 */
	public static String nonNull(String sValue, String sInsteadOfNull)
	{
		if (sValue == null)
			return sInsteadOfNull;
		else
			return sValue;
	}

	/**
	 * If object is null, then returns the empty string else returns object as string
	 * 
	 * @param obj - Object to be converted to non-null string
	 * @return String.valueOf(obj) or empty string
	 */
	public static <T> String nonNull(T obj)
	{
		return nonNull(obj, "");
	}

	/**
	 * If object is null, then returns the replacement string else returns object as string
	 * 
	 * @param obj - Object to be converted to non-null string
	 * @param replacement - String to return when object is null
	 * @return String.valueOf(obj) or replacement
	 */
	public static <T> String nonNull(T obj, String replacement)
	{
		if (obj == null)
			return replacement;
		else
			return String.valueOf(obj);
	}

	/**
	 * Converts a list of objects to a string for purpose of logging using the method toString() for the
	 * object type<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) separator must be non-null<BR>
	 * 2) If method toString() causes an exception, then "null" is used for the item<BR>
	 * 
	 * @param <T>
	 * @param objects - List of objects to make a logging string
	 * @param separator - The separator used when appending the objects together
	 * @return non-null
	 */
	public static <T> String toString(List<T> objects, String separator)
	{
		StringBuilder builder = new StringBuilder();
		for (Object item : objects)
		{
			try
			{
				builder.append(item.toString());
			}
			catch (Exception ex)
			{
				builder.append("null");
			}

			builder.append(separator);
		}

		return Misc.removeEndsWith(builder.toString(), separator);
	}

	/**
	 * Trims whitespace (non-visible text) from beginning and end of the string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If any exception occurs, then empty string is returned<BR>
	 * 
	 * @param sValue - String to trim the whitespace
	 * @return non-null
	 */
	public static String trim(String sValue)
	{
		try
		{
			return sValue.replaceAll("^[\u0000-\u0020\u007F-\u00A0]+|[\u0000-\u0020\u007F-\u00A0]+$", "");
		}
		catch (Exception ex)
		{
		}

		return "";
	}

	/**
	 * Returns a string that has all characters and corresponding Unicode Code Point<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is only for debug purposes<BR>
	 * 
	 * @param problem - String that contains some problem characters
	 * @return A debug string that can be used to determine the problem character(s)
	 */
	public static String toUnicodeCodePoints(String problem)
	{
		if (problem == null)
			return "String was null";

		String sLog = "[ ";
		for (int i = 0; i < problem.length(); i++)
		{
			int codePoint = problem.codePointAt(i);
			sLog += "(" + problem.substring(i, i + 1) + ", " + codePoint + "), ";
		}

		sLog = Misc.removeEndsWith(sLog, ", ") + " ]";
		return sLog;
	}

	/**
	 * Converts the string to a list of code points<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is only for debug purposes<BR>
	 * 
	 * @param problem - String that contains some problem characters
	 * @return List&lt;Integer&gt;
	 */
	public static List<Integer> toUnicodeCodePointsList(String problem)
	{
		List<Integer> codepoints = new ArrayList<Integer>();
		if (problem == null)
			return codepoints;

		for (int i = 0; i < problem.length(); i++)
		{
			codepoints.add(problem.codePointAt(i));
		}

		return codepoints;
	}

	/**
	 * Removes problem characters from the beginning and end of the string using their Unicode code points<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is only for debug purposes<BR>
	 * 
	 * @param problem - String that contains problem characters to be removed
	 * @param removeCodePoints - List of Unicode code points to be removed
	 * @return String with problem characters removed
	 */
	public static String removeUsingUnicodeCodePoints(String problem, List<Integer> removeCodePoints)
	{
		// Put all characters into a single string
		String removeAll = "";
		for (int i : removeCodePoints)
		{
			String remove = new String(new int[] { i }, 0, 1);
			removeAll += remove;
		}

		// Construct regular expression to remove the problem string from beginning and end of the string only
		removeAll = "^[" + "" + removeAll + "]+|[" + removeAll + "]+$";

		// Replace all problem characters with empty string
		return problem.replaceAll(removeAll, "");
	}

	/**
	 * Removes all problem characters from the string using their Unicode code points<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is only for debug purposes<BR>
	 * 
	 * @param problem - String that contains problem characters to be removed
	 * @param removeCodePoints - List of Unicode code points to be removed
	 * @return String with all problem characters removed
	 */
	public static String removeAllUsingUnicodeCodePoints(String problem, List<Integer> removeCodePoints)
	{
		String cleaned = problem;
		for (int codepoint : removeCodePoints)
		{
			String target = new String(new int[] { codepoint }, 0, 1);
			cleaned = cleaned.replace(target, "");
		}

		return cleaned;
	}

	/**
	 * Returns the string as a Long.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Long.valueOf("-1") is returned for strings that cannot be converted.<BR>
	 * 
	 * @param sValue - String to convert
	 * @return Long.valueOf("-1") if string cannot be converted else Long value
	 */
	public static Long parseLong(String sValue)
	{
		Long lDefault = Long.valueOf("-1");
		return parseLong(sValue, lDefault);
	}

	/**
	 * Returns the string as a Long.
	 * 
	 * @param sValue - String to convert
	 * @param lDefault - Default value to return if conversion fails
	 * @return lDefault if string cannot be converted else Long value
	 */
	public static Long parseLong(String sValue, Long lDefault)
	{
		try
		{
			return Long.parseLong(sValue);
		}
		catch (Exception ex)
		{
			return lDefault;
		}
	}

	/**
	 * Converts an array of strings to a string for purpose of logging<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) separator must be non-null<BR>
	 * 
	 * @param separator - The separator used when appending the objects together
	 * @param variables - Array of String variables
	 * @return non-null
	 */
	public static String toString(String separator, String... variables)
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < variables.length; i++)
		{
			builder.append(variables[i]);
			builder.append(separator);
		}

		return Misc.removeEndsWith(builder.toString(), separator);
	}

	/**
	 * Converts an Unicode code point to a String
	 * 
	 * @param codePoint - Unicode code point to be converted to a String
	 * @return String
	 */
	public static String toString(int codePoint)
	{
		return new String(new int[] { codePoint }, 0, 1);
	}

	/**
	 * Converts an array of Unicode code points to a String
	 * 
	 * @param codePoints - Array of Unicode code points to be converted to a String
	 * @return empty string if code points array is null else string that contains all the code points
	 */
	public static String toString(int[] codePoints)
	{
		if (codePoints == null)
			return "";

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < codePoints.length; i++)
		{
			builder.append(toString(codePoints[i]));
		}

		return builder.toString();
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
	public static String pad(String value, int totalLength, String padWith)
	{
		String sProcess = Conversion.nonNull(value);
		String sLeadWith;
		if (padWith == null || padWith.equals(""))
			sLeadWith = "0";
		else
			sLeadWith = padWith.substring(0, 1);

		StringBuilder builder = new StringBuilder();
		int nPaddingRequired = totalLength - sProcess.length();
		for (int i = 0; i < nPaddingRequired; i++)
		{
			builder.append(sLeadWith);
		}

		builder.append(sProcess);
		return builder.toString();
	}

	/**
	 * Returns the filename with no invalid characters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null string converted to empty string<BR>
	 * 2) For most cases, the following regular expression is sufficient: <B>[^A-Za-z0-9_.-]</B><BR>
	 * 
	 * @param filename - Filename to be encoded to remove invalid characters
	 * @param encodeMask - The regular expression used to find invalid characters in the filename
	 * @param escapeChar - The character that will replace invalid characters in the filename
	 * @return encoded filename
	 */
	public static String encodeFilename(String filename, String encodeMask, String escapeChar)
	{
		return nonNull(filename).replaceAll(encodeMask, escapeChar);
	}

	/**
	 * Returns the filename with no invalid characters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null string converted to empty string<BR>
	 * 2) Uses the following regular expression to find invalid characters: <B>[^A-Za-z0-9_.-]</B><BR>
	 * 3) Invalid Characters are replaced with following: <B>_</B><BR>
	 * 
	 * @param filename - The filename to remove invalid characters
	 * @return encoded filename
	 */
	public static String encodeFilename(String filename)
	{
		return encodeFilename(filename, "[^A-Za-z0-9_.-]", "_");
	}

	/**
	 * Generates the valid server filename (by removing invalid characters)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If filename is null, then converted to empty string<BR>
	 * 2) For most cases, the following regular expression is sufficient: <B>[^A-Za-z0-9_.-]</B><BR>
	 * 
	 * @param filename - File to upload
	 * @param encodeMask - The regular expression used to find invalid characters in the filename
	 * @param escapeChar - The character that will replace invalid characters in the filename
	 * @return valid server filename
	 */
	public static String generateServerFilename(String filename, String encodeMask, String escapeChar)
	{
		File f = new File(filename);
		String name = f.getName();
		return encodeFilename(name, encodeMask, escapeChar);
	}

	/**
	 * Generates the valid server filename (by removing invalid characters)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If filename is null, then converted to empty string<BR>
	 * 2) Uses the following regular expression to find invalid characters: <B>[^A-Za-z0-9_.-]</B><BR>
	 * 3) Invalid Characters are replaced with following: <B>_</B><BR>
	 * 
	 * @param filename - File to upload
	 * @return valid server filename
	 */
	public static String generateServerFilename(String filename)
	{
		return generateServerFilename(filename, "[^A-Za-z0-9_.-]", "_");
	}

	/**
	 * Convert a string value to a specified enumeration value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The method toString() of the enumeration is used to determine if there is a match. It is recommended
	 * to override the method to perform the conversion desired.<BR>
	 * 2) Default value flag takes precedence over Error flag<BR>
	 * 
	 * @param e - Enumeration to get values
	 * @param value - Value to attempt conversion to enumeration value
	 * @param option - Option to modify the strings before the equals operation
	 * @param bError - true to log error and throw exception (<B>error flag</B>)
	 * @param bDefaultValue - true to the variable e as the default value (<B>default value flag</B>)
	 * @return null or corresponding enumeration value
	 * @throws GenericUnexpectedException if value could not be matched and no default value to be returned
	 *             and error flag set
	 */
	public static Enum<?> toEnum(Enum<?> e, String value, Comparison option, boolean bError,
			boolean bDefaultValue)
	{
		Enum<?>[] options = e.getDeclaringClass().getEnumConstants();
		for (Enum<?> item : options)
		{
			if (Compare.equals(value, item.toString(), option))
				return item;
		}

		if (bDefaultValue)
			return e;

		if (bError)
		{
			Logs.logError("Could not convert value (" + value + ") to the specified enumeration type ("
					+ e.getDeclaringClass() + ")");
		}

		return null;
	}

	/**
	 * Convert a string value to a specified enumeration value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The method toString() of the enumeration is used to determine if there is a match. It is recommended
	 * to override the method to perform the conversion desired.<BR>
	 * 
	 * @param e - Enumeration to get values
	 * @param value - Value to attempt conversion to enumeration value
	 * @param option - Option to modify the strings before the equals operation
	 * @return corresponding enumeration value
	 * @throws GenericUnexpectedException if value could not be matched
	 */
	public static Enum<?> toEnum(Enum<?> e, String value, Comparison option)
	{
		return toEnum(e, value, option, true, false);
	}

	/**
	 * Convert a string value to a specified enumeration value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The method toString() of the enumeration is used to determine if there is a match. It is recommended
	 * to override the method to perform the conversion desired.<BR>
	 * 2) Parameter e is also used as the default value if needed<BR>
	 * 
	 * @param value - Value to attempt conversion to enumeration value
	 * @param e - Enumeration to get values (and default value if needed)
	 * @param option - Option to modify the strings before the equals operation
	 * @return corresponding enumeration value or default value
	 */
	public static Enum<?> toEnum(String value, Enum<?> e, Comparison option)
	{
		Enum<?> converted = toEnum(e, value, option, false, true);
		if (converted == null)
			return e;
		else
			return converted;
	}

	/**
	 * Get all letters for the specified character set<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use Charset.availableCharsets() to get the available charsets that can be used<BR>
	 * 2) Use Charset.defaultCharset() if you need to know the default charset for the machine<BR>
	 * 
	 * @param charsetName - The name of a supported charset to use
	 * @return String
	 */
	public static String getAllLetters(String charsetName)
	{
		CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
		StringBuilder result = new StringBuilder();
		for (char c = 0; c < Character.MAX_VALUE; c++)
		{
			if (ce.canEncode(c) && Character.isLetter(c))
			{
				result.append(c);
			}
		}

		return result.toString();
	}

	/**
	 * Get all numbers for the specified character set<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use Charset.availableCharsets() to get the available charsets that can be used<BR>
	 * 2) Use Charset.defaultCharset() if you need to know the default charset for the machine<BR>
	 * 
	 * @param charsetName - The name of a supported charset to use
	 * @return String
	 */
	public static String getAllNumbers(String charsetName)
	{
		CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
		StringBuilder result = new StringBuilder();
		for (char c = 0; c < Character.MAX_VALUE; c++)
		{
			if (ce.canEncode(c) && Character.isDigit(c))
			{
				result.append(c);
			}
		}

		return result.toString();
	}

	/**
	 * Get all special characters (not letters or numbers or control characters) for the specified character
	 * set<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use Charset.availableCharsets() to get the available charsets that can be used<BR>
	 * 2) Use Charset.defaultCharset() if you need to know the default charset for the machine<BR>
	 * 
	 * @param charsetName - The name of a supported charset to use
	 * @return String
	 */
	public static String getAllSpecial(String charsetName)
	{
		CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
		StringBuilder result = new StringBuilder();
		for (char c = 0; c < Character.MAX_VALUE; c++)
		{
			if (ce.canEncode(c) && !Character.isLetter(c) && !Character.isDigit(c)
					&& !Character.isISOControl(c))
			{
				result.append(c);
			}
		}

		return result.toString();
	}

	/**
	 * Convert the list of objects to a cache format<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The methods equals and hashCode need to be implemented for the type of object.<BR>
	 * 2) The hashCode method needs to be implemented properly or the cache may not be populated properly.<BR>
	 * 3) If the list contains duplicates, then the cache will not match exactly as it contains no duplicates.<BR>
	 * 
	 * @param <T> - Type of Objects to cache
	 * @param data - List of objects to be converted to a cache
	 * @return empty cache if list is null else cache that contains all the items in the list
	 */
	public static <T> HashMap<T, Integer> getCache(List<T> data)
	{
		// Initialize cache
		HashMap<T, Integer> cache = new HashMap<T, Integer>();

		// Return empty cache if data is null
		if (data == null)
			return cache;

		// Populate the cache
		for (int i = 0; i < data.size(); i++)
		{
			cache.put(data.get(i), i);
		}

		// Return the populated cache
		return cache;
	}

	/**
	 * Gets the translations to the keywords in the specified language
	 * 
	 * @param voc - Vocabulary used to look up the translations
	 * @param lang - Translation Language
	 * @param keywords - Keywords to be translated
	 * @return Array with translated strings corresponding to the Array of Keywords
	 */
	public static String[] getTranslations(Vocabulary voc, Languages lang, String... keywords)
	{
		String[] translated = new String[keywords.length];

		for (int i = 0; i < keywords.length; i++)
		{
			translated[i] = voc.getWord(keywords[i]).getTranscription(lang);
		}

		return translated;
	}

	/**
	 * Create a key using the list of string values<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The separator is set to '+'. This may only be useful in debugging to know the values that made up
	 * the key.<BR>
	 * 2) Each object needs to override the toString method or equal objects may not have the same key<BR>
	 * 
	 * @param values - List of values to use in creating the key
	 * @return String
	 */
	public static String createKey(List<Object> values)
	{
		return createKey("+", values);
	}

	/**
	 * Create a key using the list of string values<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Each object needs to override the toString method or equal objects may not have the same key<BR>
	 * 
	 * @param separator - The separator that is appended after each field
	 * @param values - List of values to use in creating the key
	 * @return String
	 */
	public static String createKey(String separator, List<Object> values)
	{
		StringBuilder builder = new StringBuilder();

		for (Object item : values)
		{
			builder.append(item);
			builder.append(separator);
		}

		return builder.toString();
	}

	/**
	 * Create a key using the passed object and the specified fields<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If any field cannot be accessed, then the key will not contain a value for this field<BR>
	 * 2) The separator is set to '+'. This may only be useful in debugging to know the values that made up
	 * the key.<BR>
	 * 3) Each specified field needs to override the toString method or equal objects may not have the same
	 * key<BR>
	 * 
	 * @param target - Object to create a key using specified fields
	 * @param fields - Names of the fields to use in creating the key
	 * @return String
	 */
	public static <T> String createKey(T target, List<String> fields)
	{
		return createKey(target, "+", fields);
	}

	/**
	 * Create a key using the passed object and the specified fields<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If any field cannot be accessed, then the key will not contain a value for this field<BR>
	 * 2) The separator can be empty. This may only be useful in debugging to know the values that made up the
	 * key.<BR>
	 * 3) Each specified field needs to override the toString method or equal objects may not have the same
	 * key<BR>
	 * 
	 * @param target - Object to create a key using specified fields
	 * @param separator - The separator that is appended after each field
	 * @param fields - Names of the fields to use in creating the key
	 * @return String
	 */
	public static <T> String createKey(T target, String separator, List<String> fields)
	{
		StringBuilder builder = new StringBuilder();

		for (String fieldName : fields)
		{
			try
			{
				builder.append(FieldUtils.readField(target, fieldName, true));
			}
			catch (Exception ex)
			{
			}

			builder.append(separator);
		}

		return builder.toString();
	}

	/**
	 * Convert the list of objects to a cache format<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The methods equals and hashCode need to be implemented for the type of object.<BR>
	 * 2) The hashCode method needs to be implemented properly or the cache may not be populated properly.<BR>
	 * 3) If the list contains duplicates, then the cache will not match exactly as it contains no duplicates.<BR>
	 * 4) The toString method needs to be overridden or the cache may not be populated properly.<BR>
	 * 5) The separator is set to '+'<BR>
	 * 
	 * @param <T> - Type of Objects to cache
	 * @param data - List of objects to be converted to a cache
	 * @param fields - Names of the fields to use in creating the key
	 * @return Empty cache if list is null else cache that contains all the items in the list. The createKey
	 *         method is used to create the keys used in the cache
	 */
	public static <T> HashMap<String, T> getCache(List<T> data, List<String> fields)
	{
		return getCache(data, "+", fields);
	}

	/**
	 * Convert the list of objects to a cache format<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The methods equals and hashCode need to be implemented for the type of object.<BR>
	 * 2) The hashCode method needs to be implemented properly or the cache may not be populated properly.<BR>
	 * 3) If the list contains duplicates, then the cache will not match exactly as it contains no duplicates.<BR>
	 * 4) The toString method needs to be overridden or the cache may not be populated properly.<BR>
	 * 
	 * @param <T> - Type of Objects to cache
	 * @param data - List of objects to be converted to a cache
	 * @param separator - The separator that is appended after each field to create a key
	 * @param fields - Names of the fields to use in creating the key
	 * @return Empty cache if list is null else cache that contains all the items in the list. The createKey
	 *         method is used to create the keys used in the cache
	 */
	public static <T> HashMap<String, T> getCache(List<T> data, String separator, List<String> fields)
	{
		// Initialize cache
		HashMap<String, T> cache = new HashMap<String, T>();

		// Return empty cache if data is null
		if (data == null)
			return cache;

		// Populate the cache
		for (int i = 0; i < data.size(); i++)
		{
			cache.put(createKey(data.get(i), separator, fields), data.get(i));
		}

		// Return the populated cache
		return cache;
	}

	/**
	 * Returns the object (using toString method) as an integer.
	 * 
	 * @param obj - Object to convert
	 * @param nDefault - Default value to return if conversion fails
	 * @return -1 if object cannot be converted else integer value
	 */
	public static <T> int parseInt(T obj)
	{
		return parseInt(obj, -1);
	}

	/**
	 * Returns the object (using toString method) as an integer.
	 * 
	 * @param obj - Object to convert
	 * @param nDefault - Default value to return if conversion fails
	 * @return nDefault if object cannot be converted else integer value
	 */
	public static <T> int parseInt(T obj, int nDefault)
	{
		try
		{
			return parseInt(obj.toString(), nDefault);
		}
		catch (Exception ex)
		{
			return nDefault;
		}
	}

	/**
	 * Returns converts an object (using toString method) to a boolean value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses Boolean.parseBoolean and adds "1" is true & null as false<BR>
	 * 
	 * @param obj - Object to convert
	 * @return true if ("1" or "true") else false
	 */
	public static <T> boolean parseBoolean(T obj)
	{
		try
		{
			return parseBoolean(obj.toString());
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Returns the object (using toString method) as a float.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Float.valueOf("-1") is returned for objects that cannot be converted.<BR>
	 * 
	 * @param obj - Object to convert
	 * @return Float.valueOf("-1") if object cannot be converted else float value
	 */
	public static <T> float parseFloat(T obj)
	{
		float fDefault = Float.valueOf("-1");
		return parseFloat(obj, fDefault);
	}

	/**
	 * Returns the object (using toString method) as a float.
	 * 
	 * @param obj - Object to convert
	 * @param fDefault - Default value to return if conversion fails
	 * @return fDefault if string cannot be converted else float value
	 */
	public static <T> float parseFloat(T obj, float fDefault)
	{
		try
		{
			return parseFloat(obj.toString(), fDefault);
		}
		catch (Exception ex)
		{
			return fDefault;
		}
	}

	/**
	 * Returns the object (using toString method) as a Long.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Long.valueOf("-1") is returned for objects that cannot be converted.<BR>
	 * 
	 * @param obj - Object to convert
	 * @return Long.valueOf("-1") if object cannot be converted else Long value
	 */
	public static <T> Long parseLong(T obj)
	{
		Long lDefault = Long.valueOf("-1");
		return parseLong(obj, lDefault);
	}

	/**
	 * Returns the object (using toString method) as a Long.
	 * 
	 * @param obj - Object to convert
	 * @param lDefault - Default value to return if conversion fails
	 * @return lDefault if object cannot be converted else Long value
	 */
	public static <T> Long parseLong(T obj, Long lDefault)
	{
		try
		{
			return parseLong(obj.toString(), lDefault);
		}
		catch (Exception ex)
		{
			return lDefault;
		}
	}

	/**
	 * Converts an array of strings to a string for purpose of logging<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) separator must be non-null<BR>
	 * 
	 * @param separator - The separator used when appending the objects together
	 * @param data - Array of String variables
	 * @return non-null
	 */
	public static String toString(String separator, String[][] data)
	{
		StringBuilder builder = new StringBuilder();
		if (data == null)
			return "[]";

		builder.append("[");
		for (int i = 0; i < data.length; i++)
		{
			builder.append("[");
			builder.append(Conversion.toString(separator, data[i]));
			builder.append("]");
			builder.append(separator);
		}

		return Misc.removeEndsWith(builder.toString(), separator) + "]";
	}
}
