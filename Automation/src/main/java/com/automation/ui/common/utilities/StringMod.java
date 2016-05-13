package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.automation.ui.common.dataStructures.Comparison;

/**
 * Class to do multiple actions on a string which can be strung together that is more convenient/easier to
 * read/maintain (using Fluent Pattern.)
 */
public class StringMod {
	/**
	 * The current/working value
	 */
	private String working;

	/**
	 * Constructor that initializes the working string to be the empty string
	 */
	public StringMod()
	{
		this("");
	}

	/**
	 * Constructor that initializes the working string to the the specified value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A null value is converted to the empty string<BR>
	 * 
	 * @param value - Working string to start with
	 */
	public StringMod(String value)
	{
		working = Conversion.nonNull(value);
	}

	public boolean equals(Object obj)
	{
		List<String> excludeFields = new ArrayList<String>();
		return Compare.equals(this, obj, excludeFields);
	}

	/**
	 * Compares this StringMod to another StringMod, ignoring case considerations. Two StringMod are
	 * considered equal ignoring case if they are of the same length and corresponding characters in the two
	 * stored strings are equal ignoring case.<BR>
	 * 
	 * @param obj - the reference object with which to compare
	 * @return true if the argument is not null and it represents an equivalent object ignoring case; false
	 *         otherwise
	 */
	public boolean equalsIgnoreCase(Object obj)
	{
		if (obj == null)
			return false;

		if (obj.getClass() != this.getClass())
			return false;

		// Put in temporary variables as not to modify
		StringMod tempStored = new StringMod(working);
		StringMod tempObj = Cloner.deepClone((StringMod) obj);

		// First test if objects are equal without any modifications
		if (tempStored.equals(tempObj))
			return true;

		// Second test if objects are equal in lower case
		tempObj.toLowerCase();
		tempStored.toLowerCase();
		if (tempStored.equals(tempObj))
			return true;

		// Third test if objects are equal in upper case
		tempObj.toUpperCase();
		tempStored.toUpperCase();
		if (tempStored.equals(tempObj))
			return true;

		// Objects are not equal regardless of case
		return false;
	}

	public int hashCode()
	{
		List<String> excludeFields = new ArrayList<String>();
		return HashCodeBuilder.reflectionHashCode(this, excludeFields);
	}

	public String toString()
	{
		return working;
	}

	/**
	 * Get the current working string value (same as toString method)
	 * 
	 * @return String
	 */
	public String get()
	{
		return toString();
	}

	/**
	 * Appends the specified value to the working string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A null value is converted to the empty string<BR>
	 * 
	 * @param value - Value to be appended to the working string
	 * @return StringMod
	 */
	public StringMod append(String value)
	{
		working += Conversion.nonNull(value);
		return this;
	}

	/**
	 * Prepends the specified value to the working string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A null value is converted to the empty string<BR>
	 * 
	 * @param value - Value to be prepended to the working string
	 * @return StringMod
	 */
	public StringMod prepend(String value)
	{
		working = Conversion.nonNull(value) + working;
		return this;
	}

	/**
	 * Remove all characters matching the regular expression
	 * 
	 * @param regex - the regular expression to which this string is to be matched
	 * @return StringMod
	 */
	public StringMod removeAll(String regex)
	{
		return removeAll(regex, "");
	}

	/**
	 * Remove all characters matching the regular expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If regular expression is invalid, then nothing will be removed<BR>
	 * 
	 * @param regex - the regular expression to which this string is to be matched
	 * @param replacement - the string to be substituted for each match
	 * @return StringMod
	 */
	public StringMod removeAll(String regex, String replacement)
	{
		try
		{
			working = working.replaceAll(regex, replacement);
		}
		catch (Exception ex)
		{
		}

		return this;
	}

	/**
	 * Remove non-digits
	 * 
	 * @return StringMod
	 */
	public StringMod removeNonDigits()
	{
		return removeAll("\\D");
	}

	/**
	 * Remove non-letters (ASCII alphabet)
	 * 
	 * @return StringMod
	 */
	public StringMod removeNonLetters()
	{
		return removeAll("[^A-Za-z]");
	}

	/**
	 * Remove non-digits and non-letters (ASCII alphabet)
	 * 
	 * @return StringMod
	 */
	public StringMod removeNonAlphanumeric()
	{
		return removeAll("[^A-Za-z0-9]");
	}

	/**
	 * Insert the value at the specified offset<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If offset is invalid, then no string is inserted<BR>
	 * 2) Offset needs to be greater than or equal to 0 and less than or equal to the length of the currently
	 * stored string<BR>
	 * 
	 * @param offset - Offset to insert string at
	 * @param value - Value to be inserted
	 * @return StringMod
	 */
	public StringMod insert(int offset, String value)
	{
		StringBuilder sb = new StringBuilder(working);

		try
		{
			sb.insert(offset, value);
		}
		catch (Exception ex)
		{
		}

		working = sb.toString();
		return this;
	}

	/**
	 * Replaces each substring of this string that matches the literal target sequence with the specified
	 * literal replacement sequence. The replacement proceeds from the beginning of the string to the end,
	 * for example, replacing "aa" with "b" in the string "aaa" will result in "ba" rather than "ab".<BR>
	 * 
	 * @param target - String to find
	 * @param replacement - the string to be substituted for each match
	 * @return StringMod
	 */
	public StringMod replace(String target, String replacement)
	{
		working = working.replace(Conversion.nonNull(target), Conversion.nonNull(replacement));
		return this;
	}

	/**
	 * Removes each substring of this string that matches the literal target sequence with the empty string<BR>
	 * 
	 * @param target - String to find and replace with the empty string
	 * @return StringMod
	 */
	public StringMod remove(String target)
	{
		return replace(target, "");
	}

	/**
	 * Replace 1st match using the regular expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If regular expression is invalid, then nothing will be replaced<BR>
	 * 
	 * @param regex - the regular expression to which this string is to be matched
	 * @param replacement - the string to be substituted
	 * @return StringMod
	 */
	public StringMod replaceFirstRegEx(String regex, String replacement)
	{
		try
		{
			working = working.replaceFirst(regex, replacement);
		}
		catch (Exception ex)
		{
		}

		return this;
	}

	/**
	 * Remove 1st match using the regular expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If regular expression is invalid, then nothing will be replaced<BR>
	 * 
	 * @param regex - the regular expression to which this string is to be matched
	 * @return StringMod
	 */
	public StringMod removeFirstRegEx(String regex)
	{
		return replaceFirstRegEx(regex, "");
	}

	/**
	 * Replaces the first occurrence of the specified string
	 * 
	 * @param first - First occurrence of string to replace
	 * @param replacement - Replacement string
	 * @return StringMod
	 */
	public StringMod replaceFirst(String first, String replacement)
	{
		return replaceFirst(first, replacement, Comparison.Standard);
	}

	/**
	 * Replaces the first occurrence of the specified string
	 * 
	 * @param first - First occurrence of string to replace
	 * @param replacement - Replacement string
	 * @param option - Option to modify the strings before the operation
	 * @return StringMod
	 */
	public StringMod replaceFirst(String first, String replacement, Comparison option)
	{
		if (first == null || first.equals(""))
			return this;

		StringMod tempStored = new StringMod(working);
		tempStored.toCase(option);

		StringMod tempFirst = new StringMod(first);
		tempFirst.toCase(option);

		int nFirstIndex = tempStored.get().indexOf(tempFirst.get());
		if (nFirstIndex == 0)
		{
			if (working.length() == first.length())
			{
				working = replacement;
			}
			else
			{
				working = replacement + working.substring(first.length(), working.length());
			}
		}
		else if (nFirstIndex > 0)
		{
			// The part of the string before the first occurrence
			String sPart1 = working.substring(0, nFirstIndex);

			// Does the string end with the first occurrence?
			if (nFirstIndex + first.length() >= working.length())
			{
				working = sPart1 + replacement;
			}
			else
			{
				working = sPart1 + replacement
						+ working.substring(nFirstIndex + first.length(), working.length());
			}
		}

		return this;
	}

	/**
	 * Removes the first occurrence of the specified string
	 * 
	 * @param first - First occurrence of string to remove
	 * @return StringMod
	 */
	public StringMod removeFirst(String first)
	{
		return removeFirst(first, Comparison.Standard);
	}

	/**
	 * Removes the first occurrence of the specified string
	 * 
	 * @param first - First occurrence of string to remove
	 * @param option - Option to modify the strings before the operation
	 * @return StringMod
	 */
	public StringMod removeFirst(String first, Comparison option)
	{
		return replaceFirst(first, "", option);
	}

	/**
	 * Replaces the last occurrence of the specified string
	 * 
	 * @param last - Last occurrence of string to replace
	 * @param replacement - Replacement string
	 * @return StringMod
	 */
	public StringMod replaceLast(String last, String replacement)
	{
		return replaceLast(last, replacement, Comparison.Standard);
	}

	/**
	 * Replaces the last occurrence of the specified string
	 * 
	 * @param last - Last occurrence of string to replace
	 * @param replacement - Replacement string
	 * @param option - Option to modify the strings before the operation
	 * @return StringMod
	 */
	public StringMod replaceLast(String last, String replacement, Comparison option)
	{
		if (last == null || last.equals(""))
			return this;

		StringMod tempStored = new StringMod(working);
		tempStored.toCase(option);

		StringMod tempLast = new StringMod(last);
		tempLast.toCase(option);

		int nLastIndex = tempStored.get().lastIndexOf(tempLast.get());
		if (nLastIndex == 0)
		{
			if (working.length() == last.length())
			{
				working = replacement;
			}
			else
			{
				working = replacement + working.substring(last.length(), working.length());
			}
		}
		else if (nLastIndex > 0)
		{
			// The part of the string before the last occurrence
			String sPart1 = working.substring(0, nLastIndex);

			// Does the string end with the last occurrence?
			if (nLastIndex + last.length() >= working.length())
			{
				working = sPart1 + replacement;
			}
			else
			{
				working = sPart1 + replacement
						+ working.substring(nLastIndex + last.length(), working.length());
			}
		}

		return this;
	}

	/**
	 * Removes the last occurrence of the specified string
	 * 
	 * @param last - Last occurrence of string to remove
	 * @return StringMod
	 */
	public StringMod removeLast(String last)
	{
		return removeLast(last, Comparison.Standard);
	}

	/**
	 * Removes the last occurrence of the specified string
	 * 
	 * @param last - Last occurrence of string to remove
	 * @param option - Option to modify the strings before the operation
	 * @return StringMod
	 */
	public StringMod removeLast(String last, Comparison option)
	{
		return replaceLast(last, "", option);
	}

	/**
	 * Converts all of the characters in this String to lower case using the rules of the default locale
	 * 
	 * @return StringMod
	 */
	public StringMod toLowerCase()
	{
		working = working.toLowerCase();
		return this;
	}

	/**
	 * Converts all of the characters in this String to upper case using the rules of the default locale
	 * 
	 * @return StringMod
	 */
	public StringMod toUpperCase()
	{
		working = working.toUpperCase();
		return this;
	}

	/**
	 * Change Case of the stored string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Supported options are Lower, Upper. For any other option, no changes are made<BR>
	 * 
	 * @param option - Method used to change the case
	 * @return StringMod
	 */
	public StringMod toCase(Comparison option)
	{
		if (option == Comparison.Lower)
		{
			toLowerCase();
		}
		else if (option == Comparison.Upper)
		{
			toUpperCase();
		}

		return this;
	}

	/**
	 * Removes specified string from the end of the stored string and replaced with specified string<BR>
	 * 
	 * @param ending - String to be replaced from end of the stored string
	 * @param replacement - the replacement string to be used
	 * @return StringMod
	 */
	public StringMod replaceEndsWith(String ending, String replacement)
	{
		return replaceEndsWith(ending, replacement, Comparison.Standard);
	}

	/**
	 * Removes specified string from the end of the stored string<BR>
	 * 
	 * @param ending - String to be removed from end of the stored string
	 * @return StringMod
	 */
	public StringMod removeEndsWith(String ending)
	{
		return removeEndsWith(ending, Comparison.Standard);
	}

	/**
	 * Removes specified string from the end of the stored string and replaced with specified string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Supported options are Lower, Upper. For any other option, no changes are made<BR>
	 * 
	 * @param ending - String to be replaced from end of the stored string
	 * @param replacement - the replacement string to be used
	 * @param option - Option to modify the strings before the operation
	 * @return StringMod
	 */
	public StringMod replaceEndsWith(String ending, String replacement, Comparison option)
	{
		StringMod tempStored = new StringMod(working);
		tempStored.toCase(option);

		StringMod tempEnding = new StringMod(ending);
		tempEnding.toCase(option);

		if (ending != null && tempStored.get().endsWith(tempEnding.get()))
		{
			working = working.substring(0, working.length() - ending.length())
					+ Conversion.nonNull(replacement);
		}

		return this;
	}

	/**
	 * Removes specified string from the end of the stored string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Supported options are Lower, Upper. For any other option, no changes are made<BR>
	 * 
	 * @param ending - String to be removed from end of the stored string
	 * @param option - Option to modify the strings before the operation
	 * @return StringMod
	 */
	public StringMod removeEndsWith(String ending, Comparison option)
	{
		return replaceEndsWith(ending, "", option);
	}

	/**
	 * Removes specified string from the start of the stored string and replaced with specified string<BR>
	 * 
	 * @param starting - String to be replaced from start of the stored string
	 * @param replacement - the replacement string to be used
	 * @return StringMod
	 */
	public StringMod replaceStartsWith(String starting, String replacement)
	{
		return replaceStartsWith(starting, replacement, Comparison.Standard);
	}

	/**
	 * Removes specified string from the start of the stored string and replaced with specified string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Supported options are Lower, Upper. For any other option, no changes are made<BR>
	 * 
	 * @param starting - String to be replaced from start of the stored string
	 * @param replacement - the replacement string to be used
	 * @param option - Option to modify the strings before the operation
	 * @return StringMod
	 */
	public StringMod replaceStartsWith(String starting, String replacement, Comparison option)
	{
		StringMod tempStored = new StringMod(working);
		tempStored.toCase(option);

		StringMod tempEnding = new StringMod(starting);
		tempEnding.toCase(option);

		if (starting != null && tempStored.get().startsWith(tempEnding.get()))
		{
			try
			{
				working = Conversion.nonNull(replacement) + working.substring(starting.length());
			}
			catch (Exception ex)
			{
			}
		}

		return this;
	}

	/**
	 * Removes specified string from the start of the stored string<BR>
	 * 
	 * @param starting - String to be removed from start of the stored string
	 * @return StringMod
	 */
	public StringMod removeStartsWith(String starting)
	{
		return removeStartsWith(starting, Comparison.Standard);
	}

	/**
	 * Removes specified string from the start of the stored string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Supported options are Lower, Upper. For any other option, no changes are made<BR>
	 * 
	 * @param starting - String to be removed from start of the stored string
	 * @param option - the replacement string to be used
	 * @return StringMod
	 */
	public StringMod removeStartsWith(String starting, Comparison option)
	{
		return replaceStartsWith(starting, "", option);
	}

	/**
	 * Returns a string that is a substring of this string. The substring begins with the character at the
	 * specified index and extends to the end of this string.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If beginIndex is invalid, then stored string is not modified<BR>
	 * 
	 * @param beginIndex - the beginning index, inclusive.
	 * @return StringMod
	 */
	public StringMod substring(int beginIndex)
	{
		try
		{
			working = working.substring(beginIndex);
		}
		catch (Exception ex)
		{
		}

		return this;
	}

	/**
	 * Returns a string that is a substring of this string. The substring begins at the specified beginIndex
	 * and extends to the character at index endIndex - 1. Thus the length of the substring is
	 * endIndex-beginIndex. <BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If beginIndex and/or endIndex is invalid, then stored string is not modified<BR>
	 * 
	 * @param beginIndex - the beginning index, inclusive.
	 * @param endIndex - the ending index, exclusive.
	 * @return StringMod
	 */
	public StringMod substring(int beginIndex, int endIndex)
	{
		try
		{
			working = working.substring(beginIndex, endIndex);
		}
		catch (Exception ex)
		{
		}

		return this;
	}

	/**
	 * Trim leading and trailing whitespace<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This is the standard Java trim functionality<BR>
	 * 
	 * @return StringMod
	 */
	public StringMod trim()
	{
		working = working.trim();
		return this;
	}

	/**
	 * Trims whitespace (non-visible text) from beginning and end of the string
	 * 
	 * @return StringMod
	 */
	public StringMod trimNonVisible()
	{
		return removeAll("^[\u0000-\u0020\u007F-\u00A0]+|[\u0000-\u0020\u007F-\u00A0]+$");
	}

	/**
	 * Remove all Invisible Control characters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Regular expression is from following page: http://www.regular-expressions.info/unicode.html<BR>
	 * 
	 * @return StringMod
	 */
	public StringMod removeInvisibleControl()
	{
		return replaceInvisibleControl("");
	}

	/**
	 * Replace all Invisible Control characters with specified value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Regular expression is from following page: http://www.regular-expressions.info/unicode.html<BR>
	 * 
	 * @param replacement - the string to be substituted for each match
	 * @return StringMod
	 */
	public StringMod replaceInvisibleControl(String replacement)
	{
		return removeAll("\\p{C}", replacement);
	}

	/**
	 * Combines all methods that trim<BR>
	 * <BR>
	 * <B>Methods executed:</B>
	 * <ol>
	 * <li>trim()</li>
	 * <li>trimNonInvisible()</li>
	 * <li>removeInvisibleControl()</li>
	 * </ol>
	 * 
	 * @return StringMod
	 */
	public StringMod trimAll()
	{
		trim();
		trimNonVisible();
		removeInvisibleControl();
		return this;
	}

	/**
	 * Splits this string around matches of the given regular expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If regular expression is invalid, then the stored string will not change<BR>
	 * 2) If index is invalid (based on the array created from splitting using the regular expression), then
	 * the stored string will not change<BR>
	 * 
	 * @param regex - the delimiting regular expression
	 * @param index - index of array to keep after splitting using the regular expression
	 * @return StringMod
	 */
	public StringMod split(String regex, int index)
	{
		try
		{
			String[] pieces = working.split(regex);
			working = pieces[index];
		}
		catch (Exception ex)
		{
		}

		return this;
	}
}
