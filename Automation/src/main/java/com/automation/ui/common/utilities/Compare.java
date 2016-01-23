package com.automation.ui.common.utilities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.FindTextCriteria;
import com.automation.ui.common.dataStructures.SelectionCriteria;
import com.automation.ui.common.dataStructures.WebElementIndexOfMethod;

/**
 * This class holds various methods for comparison
 */
public class Compare {
	/**
	 * Compares 2 arrays to determine if they are equal.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * Comparison returns false immediately upon finding 1st mismatch (it does not continue to find any
	 * additional mismatches.)<BR>
	 * <BR>
	 * <B>Mismatching data:</B><BR>
	 * If the arrays are not the same, then the parameters mismatchColumn[0] will be set with the 1st item to
	 * fail.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) If Array size is different then mismatchColumn[0] = -1<BR>
	 * 2) If the content of column 5 is the 1st mismatch found then mismatchColumn[0] = 5
	 * 
	 * @param data1 - First array
	 * @param data2 - Second array to compare to the 1st array
	 * @param mismatchColumn - First mismatching column set only if mismatch
	 * @return true the content of the arrays are the same, false different content and mismatchColumn are set
	 */
	public static boolean equal(String[] data1, String[] data2, int[] mismatchColumn)
	{
		// Array size the same?
		if (data1.length != data2.length)
		{
			mismatchColumn[0] = -1;
			return false;
		}

		// Check each row
		int nRows = data1.length;
		for (int i = 0; i < nRows; i++)
		{
			// Data matches?
			if (!data1[i].equals(data2[i]))
			{
				mismatchColumn[0] = i;
				return false;
			}
		}

		return true;
	}

	/**
	 * Compares 2 arrays to determine if they are equal.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * Comparison returns false immediately upon finding 1st mismatch (it does not continue to find any
	 * additional mismatches.)<BR>
	 * <BR>
	 * <B>Mismatching data:</B><BR>
	 * If the arrays are not the same, then the parameters mismatchRow[0] & mismatchColumn[0] will be set with
	 * the 1st item to fail.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) If Array size is different (rows) then mismatchRow[0] = -1 & mismatchColumn[0] = 0<BR>
	 * 2) If there are a different number of columns on a row 5 then mismatchRow[0] = 5 & mismatchColumn[0] =
	 * -1<BR>
	 * 3) If the content of (3,9) is the 1st mismatch found then mismatchRow[0] = 3 & mismatchColumn[0] = 9
	 * 
	 * @param data1 - First array
	 * @param data2 - Second array to compare to the 1st array
	 * @param mismatchRow - First mismatching row set only if mismatch
	 * @param mismatchColumn - First mismatching column set only if mismatch
	 * @return true the content of the arrays are the same, false different content and mismatchRow &
	 *         mismatchColumn are set
	 */
	public static boolean equal(String[][] data1, String[][] data2, int[] mismatchRow, int[] mismatchColumn)
	{
		// Array size the same?
		if (data1.length != data2.length)
		{
			mismatchRow[0] = -1;
			mismatchColumn[0] = 0;
			return false;
		}

		// Check each row
		int nRows = data1.length;
		for (int i = 0; i < nRows; i++)
		{
			// Same number of columns on the row?
			if (data1[i].length != data2[i].length)
			{
				mismatchRow[0] = i;
				mismatchColumn[0] = -1;
				return false;
			}

			// Check each column of the current row
			int nColumns = data1[i].length;
			for (int j = 0; j < nColumns; j++)
			{
				// Data matches?
				if (!data1[i][j].equals(data2[i][j]))
				{
					mismatchRow[0] = i;
					mismatchColumn[0] = j;
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Determines if value exists in the specified array.
	 * 
	 * @param nValues - array of integers
	 * @param nFind - specific value to find
	 * @return true specified integer exists in the array, false could not find
	 */
	public static boolean exists(int[] nValues, int nFind)
	{
		try
		{
			int nLength = nValues.length;
			for (int i = 0; i < nLength; i++)
			{
				if (nValues[i] == nFind)
					return true;
			}
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Returns the index for the WebElement in the list that matches the criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Matches either using index or regular expression (if criteria.sFindMethod equals true, then index
	 * else regular expression<BR>
	 * 
	 * @param elements - List of elements to match criteria against
	 * @param criteria - Information to determine which WebElement to return
	 * @return -1 if no match else Index of matching WebElement in elements list
	 */
	public static int match(List<WebElement> elements, SelectionCriteria criteria)
	{
		FindWebElement find = new FindWebElement();
		find.setFindMethod(WebElementIndexOfMethod.VisibleText_RegEx);
		return find.indexOf(elements, criteria);
	}

	/**
	 * Applies the specified modification before doing a contains comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null values are converted to the empty string<BR>
	 * 2) The option is applied to both strings before doing a contains comparison<BR>
	 * 3) Supported options are Lower and Upper. For any other option, it does a normal contains<BR>
	 * 
	 * @param str1 - The String to be searched
	 * @param str2 - The String to be found
	 * @param option - Option to modify the strings before the contains operation
	 * @return true if str1 contains str2 based on option, false otherwise
	 */
	public static boolean contains(String str1, String str2, Comparison option)
	{
		if (option == Comparison.Lower)
		{
			String lower1 = Conversion.nonNull(str1).toLowerCase();
			String lower2 = Conversion.nonNull(str2).toLowerCase();
			return lower1.contains(lower2);
		}
		else if (option == Comparison.Upper)
		{
			String upper1 = Conversion.nonNull(str1).toUpperCase();
			String upper2 = Conversion.nonNull(str2).toUpperCase();
			return upper1.contains(upper2);
		}
		else
		{
			return Conversion.nonNull(str1).contains(Conversion.nonNull(str2));
		}
	}

	/**
	 * Checks if integers are considered equal based on specified variance
	 * 
	 * @param expected - Expected value
	 * @param actual - Actual value
	 * @param variance - Allowed variance value
	 * @return true if integers are within the specified variance else false
	 */
	public static boolean equals(int expected, int actual, int variance)
	{
		if (expected - actual > variance)
			return false;

		if (actual - expected > variance)
			return false;

		return true;
	}

	/**
	 * Applies the specified modification before doing an equals comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null values are converted to the empty string<BR>
	 * 2) The option is applied to both strings before doing an equals comparison<BR>
	 * 3) Supported options are Lower, Upper & EqualsIgnoreCase. For any other option, it does a normal equals<BR>
	 * 
	 * @param str1 - The String to be searched
	 * @param str2 - The String to be found
	 * @param option - Option to modify the strings before the equals operation
	 * @return true if str1 equals str2 based on option, false otherwise
	 */
	public static boolean equals(String str1, String str2, Comparison option)
	{
		if (option == Comparison.Lower)
		{
			String lower1 = Conversion.nonNull(str1).toLowerCase();
			String lower2 = Conversion.nonNull(str2).toLowerCase();
			return lower1.equals(lower2);
		}
		else if (option == Comparison.Upper)
		{
			String upper1 = Conversion.nonNull(str1).toUpperCase();
			String upper2 = Conversion.nonNull(str2).toUpperCase();
			return upper1.equals(upper2);
		}
		else if (option == Comparison.EqualsIgnoreCase)
		{
			return Conversion.nonNull(str1).equalsIgnoreCase(Conversion.nonNull(str2));
		}
		else
		{
			return Conversion.nonNull(str1).equals(Conversion.nonNull(str2));
		}
	}

	/**
	 * Uses reflection to check if objects are equal<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Returns true if both objects are null<BR>
	 * 2) Returns false if only one object is null<BR>
	 * 
	 * @param actual - Object containing the Actual Results
	 * @param expected - Object containing the Expected Results
	 * @param excludeFields - Fields that are excluded from the comparison
	 * @return true if objects are equal else false
	 */
	public static boolean equals(Object actual, Object expected, List<String> excludeFields)
	{
		// Consider objects to be the same if both are null
		if (actual == null && expected == null)
			return true;

		// Consider objects to be different if only one is null
		if (actual == null)
			return false;

		if (expected == null)
			return false;

		if (actual.getClass() != expected.getClass())
			return false;

		return EqualsBuilder.reflectionEquals(actual, expected, excludeFields);
	}

	/**
	 * Applies the specified modification before doing a contains comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the item to be found is null, then it is converted to the empty string<BR>
	 * 2) The option is applied to both strings before doing a contains comparison<BR>
	 * 3) Supported options are Lower and Upper. For any other option, it does a normal contains<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param find - Item to be found
	 * @param option - Option to modify the strings before the contains operation
	 * @return -3 if searchItems is null<BR>
	 *         -1 if no match<BR>
	 *         else index of 1st match<BR>
	 */
	public static int containsIndex(List<String> searchItems, String find, Comparison option)
	{
		return containsIndex(searchItems, find, option, 0);
	}

	/**
	 * Applies the specified modification before doing a contains comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the item to be found is null, then it is converted to the empty string<BR>
	 * 2) The option is applied to both strings before doing a contains comparison<BR>
	 * 3) Supported options are Lower and Upper. For any other option, it does a normal contains<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param find - Item to be found
	 * @param option - Option to modify the strings before the contains operation
	 * @param startIndex - Index to start searching list from
	 * @return -3 if searchItems is null<BR>
	 *         -2 if startIndex is less than 0<BR>
	 *         -1 if no match<BR>
	 *         else index of 1st match<BR>
	 */
	public static int containsIndex(List<String> searchItems, String find, Comparison option, int startIndex)
	{
		if (searchItems == null)
			return -3;

		if (startIndex < 0)
			return -2;

		for (int i = startIndex; i < searchItems.size(); i++)
		{
			if (contains(searchItems.get(i), find, option))
				return i;
		}

		return -1;
	}

	/**
	 * Applies the specified modification before doing a contains comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A null search list returns false<BR>
	 * 2) If the item to be found is null, then it is converted to the empty string<BR>
	 * 3) The option is applied to both strings before doing a contains comparison<BR>
	 * 4) Supported options are Lower and Upper. For any other option, it does a normal contains<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param find - Item to be found
	 * @param option - Option to modify the strings before the contains operation
	 * @return true if the item exists in the search list
	 */
	public static boolean contains(List<String> searchItems, String find, Comparison option)
	{
		if (containsIndex(searchItems, find, option) >= 0)
			return true;
		else
			return false;
	}

	/**
	 * Applies the specified modification before doing a contains comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A null search list returns false<BR>
	 * 2) A null find list returns false<BR>
	 * 3) The option is applied to both strings before doing a contains comparison<BR>
	 * 4) Supported options are Lower and Upper. For any other option, it does a normal contains<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param findItems - List of items to be found
	 * @param option - Option to modify the strings before the contains operation
	 * @return true if all items to be found are in the search list
	 */
	public static boolean contains(List<String> searchItems, List<String> findItems, Comparison option)
	{
		if (searchItems == null || findItems == null)
			return false;

		// Verify that each item to be found exists in the search list
		for (int i = 0; i < findItems.size(); i++)
		{
			// If the search list does not contain the item, then immediately return false
			if (!contains(searchItems, findItems.get(i), option))
				return false;
		}

		return true;
	}

	/**
	 * Applies the specified modification before doing an equals comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the item to be found is null, then it is converted to the empty string<BR>
	 * 2) The option is applied to both strings before doing an equals comparison<BR>
	 * 3) Supported options are Lower, Upper & EqualsIgnoreCase. For any other option, it does a normal equals<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param find - Item to be found
	 * @param option - Option to modify the strings before the equals operation
	 * @return -3 if searchItems is null<BR>
	 *         -1 if no match<BR>
	 *         else index of 1st match<BR>
	 */
	public static int matchIndex(List<String> searchItems, String find, Comparison option)
	{
		return matchIndex(searchItems, find, option, 0);
	}

	/**
	 * Applies the specified modification before doing an equals comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the item to be found is null, then it is converted to the empty string<BR>
	 * 2) The option is applied to both strings before doing an equals comparison<BR>
	 * 3) Supported options are Lower, Upper & EqualsIgnoreCase. For any other option, it does a normal equals<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param find - Item to be found
	 * @param option - Option to modify the strings before the equals operation
	 * @param startIndex - Index to start searching list from
	 * @return -3 if searchItems is null<BR>
	 *         -2 if startIndex is less than 0<BR>
	 *         -1 if no match<BR>
	 *         else index of 1st match<BR>
	 */
	public static int matchIndex(List<String> searchItems, String find, Comparison option, int startIndex)
	{
		if (searchItems == null)
			return -3;

		if (startIndex < 0)
			return -2;

		for (int i = startIndex; i < searchItems.size(); i++)
		{
			if (equals(searchItems.get(i), find, option))
				return i;
		}

		return -1;
	}

	/**
	 * Applies the specified modification before doing an equals comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A null search list returns false<BR>
	 * 2) If the item to be found is null, then it is converted to the empty string<BR>
	 * 3) The option is applied to both strings before doing an equals comparison<BR>
	 * 4) Supported options are Lower, Upper & EqualsIgnoreCase. For any other option, it does a normal equals<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param find - Item to be found
	 * @param option - Option to modify the strings before the equals operation
	 * @return true if the item exists in the search list
	 */
	public static boolean match(List<String> searchItems, String find, Comparison option)
	{
		if (matchIndex(searchItems, find, option) >= 0)
			return true;
		else
			return false;
	}

	/**
	 * Applies the specified modification before doing an equals comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) A null search list returns false<BR>
	 * 2) A null find list returns false<BR>
	 * 3) The option is applied to both strings before doing an equals comparison<BR>
	 * 4) Supported options are Lower, Upper & EqualsIgnoreCase. For any other option, it does a normal equals<BR>
	 * 
	 * @param searchItems - List of items to be searched
	 * @param findItems - List of items to be found
	 * @param option - Option to modify the strings before the equals operation
	 * @return true if all items to be found are in the search list
	 */
	public static boolean match(List<String> searchItems, List<String> findItems, Comparison option)
	{
		if (searchItems == null || findItems == null)
			return false;

		// Verify that each item to be found exists in the search list
		for (int i = 0; i < findItems.size(); i++)
		{
			// If the search list does not contain the item, then immediately return false
			if (!match(searchItems, findItems.get(i), option))
				return false;
		}

		return true;
	}

	/**
	 * Finds all rows that match ALL the criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The key (Integer) is the column number (zero based) for searchCriteria<BR>
	 * 2) The value (String) is the search criteria for searchCriteria<BR>
	 * <BR>
	 * <B>Supported Comparison options:</B><BR>
	 * 1) EqualsIgnoreCase<BR>
	 * 2) Contains (Lower, Upper)<BR>
	 * 3) DoesNotContain<BR>
	 * 4) NotEqual<BR>
	 * 5) RegEx<BR>
	 * 6) Equal (default if unsupported option)<BR>
	 * 
	 * @param tableData - Array with data to be searched
	 * @param searchCriteria - Find rows that matches ALL the column criteria
	 * @param option - The comparison used to determine matches
	 * @return List of Integers of the rows that matches ALL the column criteria
	 */
	public static List<Integer> findRows(String[][] tableData, HashMap<Integer, String> searchCriteria,
			Comparison option)
	{
		// Will contain all rows that match ALL the criteria
		List<Integer> allMatchingCriteriaRows = new ArrayList<Integer>();
		if (tableData == null)
			return allMatchingCriteriaRows;

		// This contains the matching rows for each criteria
		HashMap<Integer, List<Integer>> possibleMatches = new HashMap<Integer, List<Integer>>();

		Set<Integer> keys = searchCriteria.keySet();
		Iterator<Integer> itCriteria = keys.iterator();
		while (itCriteria.hasNext())
		{
			// Rows that match current criteria
			List<Integer> rowMatches = new ArrayList<Integer>();

			Integer nCol = itCriteria.next();
			if (nCol.intValue() < 0)
				Logs.logError("Criteria column was invalid:  " + nCol);

			for (int i = 0; i < tableData.length; i++)
			{
				if (Comparison.EqualsIgnoreCase == option)
				{
					if (Compare.equals(tableData[i][nCol.intValue()], searchCriteria.get(nCol), option))
					{
						rowMatches.add(new Integer(i));
					}
				}
				else if (Comparison.Contains == option || Comparison.Lower == option
						|| Comparison.Upper == option)
				{
					if (Compare.contains(tableData[i][nCol.intValue()], searchCriteria.get(nCol), option))
					{
						rowMatches.add(new Integer(i));
					}
				}
				else if (Comparison.DoesNotContain == option)
				{
					if (!Compare.contains(tableData[i][nCol.intValue()], searchCriteria.get(nCol), option))
					{
						rowMatches.add(new Integer(i));
					}
				}
				else if (Comparison.NotEqual == option)
				{
					if (!Compare.equals(tableData[i][nCol.intValue()], searchCriteria.get(nCol), option))
					{
						rowMatches.add(new Integer(i));
					}
				}
				else if (Comparison.RegEx == option)
				{
					if (Compare.matches(tableData[i][nCol.intValue()], searchCriteria.get(nCol)))
					{
						rowMatches.add(new Integer(i));
					}
				}
				else
				{
					if (Compare.equals(tableData[i][nCol.intValue()], searchCriteria.get(nCol), option))
					{
						// Add to list of possible matching rows
						rowMatches.add(new Integer(i));
					}
				}
			}

			if (rowMatches.size() < 1)
			{
				// return as there can be no matches
				return allMatchingCriteriaRows;
			}
			else
			{
				possibleMatches.put(nCol, rowMatches);
			}
		}

		/*
		 * For all the rows that had matching criteria, we only want the same rows to meet the AND criteria
		 */
		Set<Map.Entry<Integer, List<Integer>>> set = possibleMatches.entrySet();
		Iterator<Entry<Integer, List<Integer>>> it = set.iterator();
		if (it.hasNext())
		{
			// Assume that all matches for this list are correct
			Map.Entry<Integer, List<Integer>> me = (Map.Entry<Integer, List<Integer>>) it.next();
			allMatchingCriteriaRows = me.getValue();
		}
		else
		{
			// return as there can be no matches
			return allMatchingCriteriaRows;
		}

		/*
		 * Remove any rows that are not in the criteria from allMatchingCriteriaRows
		 */
		while (it.hasNext())
		{
			// Items that do not match and need to be removed
			List<Integer> removal = new ArrayList<Integer>();

			Map.Entry<Integer, List<Integer>> me = (Map.Entry<Integer, List<Integer>>) it.next();
			List<Integer> criteria = me.getValue();
			for (Integer findRow : allMatchingCriteriaRows)
			{
				if (!criteria.contains(findRow))
					removal.add(findRow);
			}

			// Remove the rows that do not match the criteria
			for (Integer item : removal)
			{
				allMatchingCriteriaRows.remove(item);
			}

			if (allMatchingCriteriaRows.size() < 1)
			{
				// return as there can be no matches
				return allMatchingCriteriaRows;
			}
		}

		return allMatchingCriteriaRows;
	}

	/**
	 * Finds all rows that match ALL the criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The key (Integer) is the column number (zero based) for searchCriteria<BR>
	 * 2) The value (String) is the search criteria for searchCriteria<BR>
	 * 3) Comparison Option is Equal<BR>
	 * 
	 * @param tableData - Array with data to be searched
	 * @param searchCriteria - Find rows that matches ALL the column criteria
	 * @return List of Integers of the rows that matches ALL the column criteria
	 */
	public static List<Integer> findRows(String[][] tableData, HashMap<Integer, String> searchCriteria)
	{
		return findRows(tableData, searchCriteria, Comparison.Equal);
	}

	/**
	 * Tells whether or not the string matches the given regular expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Null values converted to empty strings before comparison<BR>
	 * 2) No exception will ever be thrown. In any case that an exception is caught, then false is returned<BR>
	 * 
	 * @param str - String to match against
	 * @param regex - Regular expression to be used
	 * @return true if string matches the regular expression else false
	 */
	public static boolean matches(String str, String regex)
	{
		return matches(str, regex, false);
	}

	/**
	 * Tells whether or not the string matches the given regular expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Null values converted to empty strings before comparison<BR>
	 * 2) No exception will ever be thrown. In any case that an exception is caught, then false is returned<BR>
	 * 
	 * @param str - String to match against
	 * @param regex - Regular expression to be used
	 * @param logWarning - true to log a warning if an exception occurs (for debugging)
	 * @return true if string matches the regular expression else false
	 */
	public static boolean matches(String str, String regex, boolean logWarning)
	{
		try
		{
			return Conversion.nonNull(str).matches(Conversion.nonNull(regex));
		}
		catch (Exception ex)
		{
			if (logWarning)
				Logs.log.warn("Matches failed due to exception:  " + ex);

			return false;
		}
	}

	/**
	 * Checks if floats are considered equal based on specified variance<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Due to internal java representation, it may be necessary to allow for a small variance<BR>
	 * 2) Acceptable variance is defined as following: | expected - actual | &lt; variance<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * If you want to only compare to 2 decimal places, then use variance = new Float("0.001")<BR>
	 * 
	 * @param expected - Expected value
	 * @param actual - Actual value
	 * @param variance - Allowed variance value (greater than or equal to 0)
	 * @return true if floats are within the specified variance else false
	 */
	public static boolean equals(float expected, float actual, float variance)
	{
		// Float values are exactly the same
		if (Float.compare(expected, actual) == 0)
			return true;

		// Float values are within the variance
		// Note: Due to internal representation, it may be
		if (Math.abs(expected - actual) < variance)
			return true;

		return false;
	}

	/**
	 * Applies the specified modification before doing an startsWith comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null values are converted to the empty string<BR>
	 * 2) The option is applied to both strings before doing an startsWith comparison<BR>
	 * 3) Supported options are Lower & Upper. For any other option, it does a normal startsWith<BR>
	 * 
	 * @param str1 - The String to be searched
	 * @param str2 - The String to be found
	 * @param option - Option to modify the strings before the startsWith operation
	 * @return true if str1 startsWith str2 based on option, false otherwise
	 */
	public static boolean startsWith(String str1, String str2, Comparison option)
	{
		if (option == Comparison.Lower)
		{
			String lower1 = Conversion.nonNull(str1).toLowerCase();
			String lower2 = Conversion.nonNull(str2).toLowerCase();
			return lower1.startsWith(lower2);
		}
		else if (option == Comparison.Upper)
		{
			String upper1 = Conversion.nonNull(str1).toUpperCase();
			String upper2 = Conversion.nonNull(str2).toUpperCase();
			return upper1.startsWith(upper2);
		}
		else
		{
			return Conversion.nonNull(str1).startsWith(Conversion.nonNull(str2));
		}
	}

	/**
	 * Compares 2 lists using the equals method<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The objects should override the equals method<BR>
	 * 2) Lists need to be in same sort order<BR>
	 * 
	 * @param excludeFields - For each list item, the fields that are excluded from the comparison
	 * @param actual - List containing the Actual Results
	 * @param expected - List containing the Expected Results
	 * @return true if lists are equal else false
	 */
	public static <T> boolean equals(List<String> excludeFields, List<T> actual, List<T> expected)
	{
		if (actual.size() != expected.size())
			return false;

		for (int i = 0; i < actual.size(); i++)
		{
			boolean bResult = Compare.equals(actual.get(i), expected.get(i), excludeFields);
			if (!bResult)
				return false;
		}

		return true;
	}

	/**
	 * Compares 2 objects for sort order<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) if both objects are null, then 0 returned<BR>
	 * 2) if only lhs is null, then -1 is returned<BR>
	 * 3) if only rhs is null, then 1 is returned<BR>
	 * 4) if lhs and rhs are different classes then -1 is returned<BR>
	 * 
	 * @param lhs - Left Hand Side Object
	 * @param rhs - Right Hand Side Object
	 * @param excludeFields - Fields that are excluded from the comparison
	 * @return -1 if lhs < rhs<BR>
	 *         0 if lhs = rhs<BR>
	 *         1 if lhs > rhs<BR>
	 */
	public static int compareTo(Object lhs, Object rhs, List<String> excludeFields)
	{
		if (lhs == null && rhs == null)
			return 0;

		if (lhs == null)
			return -1;

		if (rhs == null)
			return 1;

		if (lhs.getClass() != rhs.getClass())
			return -1;

		return CompareToBuilder.reflectionCompare(lhs, rhs, excludeFields);
	}

	/**
	 * Compares 2 strings for sort order<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) if both strings are null, then 0 returned<BR>
	 * 2) if str1 is null, then if ascending returns -1 else 1<BR>
	 * 3) if str2 is null, then if ascending returns 1 else -1<BR>
	 * 
	 * @param str1 - String 1 for comparison
	 * @param str2 - String 2 for comparison
	 * @param ascending - true for ascending comparison else descending
	 * @return -1 if str1 < str2 (if descending then str2 < str1)<BR>
	 *         0 if str1 = str2<BR>
	 *         1 if str1 > str2 (if descending then str2 > str1)<BR>
	 */
	public static int compareTo(String str1, String str2, boolean ascending)
	{
		if (str1 == null & str2 == null)
			return 0;

		if (str1 == null)
			return (ascending) ? -1 : 1;

		if (str2 == null)
			return (ascending) ? 1 : -1;

		int nResult;
		if (ascending)
			nResult = str1.compareTo(str2);
		else
			nResult = str2.compareTo(str1);

		if (nResult < 0)
			return -1;
		else if (nResult > 0)
			return 1;
		else
			return 0;
	}

	/**
	 * Compares 2 strings for sort order<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The comparing of value pairs only continues until the result is not 0<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * 1) valuePairs = {"a", "b", "1", "1", "2", "3"} will stop after the comparison of "a" to "b" as the
	 * result will not be zero<BR>
	 * 2) valuePairs = {"a", "a", "2", "1", "aa", "bb"} will stop after the comparison of "2" to "1" as the
	 * result will not be zero<BR>
	 * 
	 * @param ascending - true for ascending comparison else descending
	 * @param valuePairs - Pairs of values to be compared
	 * @return -2 if value pairs are not even number<BR>
	 *         -1 if first inequality in value pairs is valuePairs[i] < valuePairs[i+1] (or if descending then
	 *         valuePairs[i+1] < valuePairs[i])<BR>
	 *         0 if all value pairs are equal<BR>
	 *         1 if first inequality in value pairs is valuePairs[i] > valuePairs[i+1] (or if descending then
	 *         valuePairs[i+1] > valuePairs[i])<BR>
	 */
	public static int compareTo(boolean ascending, String... valuePairs)
	{
		int length = valuePairs.length;
		if (length % 2 != 0)
			return -2;

		int index = 0;
		while (index + 1 < length)
		{
			String str1 = valuePairs[index];
			index++;

			String str2 = valuePairs[index];
			index++;

			int nResult = compareTo(str1, str2, ascending);
			if (nResult != 0)
				return nResult;
		}

		return 0;
	}

	/**
	 * Applies the specified modification before doing an endsWith comparison<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null values are converted to the empty string<BR>
	 * 2) The option is applied to both strings before doing an endsWith comparison<BR>
	 * 3) Supported options are Lower & Upper. For any other option, it does a normal endsWith<BR>
	 * 
	 * @param str1 - The String to be searched
	 * @param str2 - The String to be found
	 * @param option - Option to modify the strings before the endsWith operation
	 * @return true if str1 endsWith str2 based on option, false otherwise
	 */
	public static boolean endsWith(String str1, String str2, Comparison option)
	{
		if (option == Comparison.Lower)
		{
			String lower1 = Conversion.nonNull(str1).toLowerCase();
			String lower2 = Conversion.nonNull(str2).toLowerCase();
			return lower1.endsWith(lower2);
		}
		else if (option == Comparison.Upper)
		{
			String upper1 = Conversion.nonNull(str1).toUpperCase();
			String upper2 = Conversion.nonNull(str2).toUpperCase();
			return upper1.endsWith(upper2);
		}
		else
		{
			return Conversion.nonNull(str1).endsWith(Conversion.nonNull(str2));
		}
	}

	/**
	 * Sorts the specified list according to the order induced by the specified method<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Specified method must take a single parameter of the object type and return an integer (same as the
	 * compareTo method when implementing Comparable&lt;T&gt;)<BR>
	 * 2) Specified method must exist or list will not be sorted<BR>
	 * 
	 * @param <T> - Object type to be sorted
	 * @param data - List to be sorted
	 * @param method - Method to be used to sort
	 */
	public static <T> void sort(List<T> data, final String method)
	{
		Collections.sort(data, new Comparator<T>() {

			@Override
			public int compare(T arg0, T arg1)
			{
				try
				{
					Method m = arg0.getClass().getDeclaredMethod(method, arg1.getClass());
					return (Integer) m.invoke(arg0, arg1);
				}
				catch (Exception ex)
				{
					return 0;
				}
			}

		});
	}

	/**
	 * Compare text 1 (str1) to text 2 (str2)<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param str1 - Text 1
	 * @param str2 - Text 2
	 * @param criteria - Matching criteria
	 * @return true if text 1 (str1) matches text 2 (str2) using the specified criteria
	 */
	public static boolean text(String str1, String str2, Comparison criteria)
	{
		try
		{
			if (criteria == Comparison.NotEqual)
			{
				// Not Equal option
				if (!str1.equals(str2))
					return true;
			}
			else if (criteria == Comparison.Equal)
			{
				// Equals option
				if (str1.equals(str2))
					return true;
			}
			else if (criteria == Comparison.EqualsIgnoreCase)
			{
				// Equals Ignore Case option
				if (str1.equalsIgnoreCase(str2))
					return true;
			}
			else if (criteria == Comparison.RegEx)
			{
				// Regular Expression option
				if (str1.matches(str2))
					return true;
			}
			else if (criteria == Comparison.DoesNotContain)
			{
				// Does Not Contain option
				if (!str1.contains(str2))
					return true;
			}
			else
			{
				// Default option is contains the text
				if (str1.contains(str2))
					return true;
			}
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Compare text value using specified criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Converts text value and criteria value to non-null values<BR>
	 * 2) Method returns result of (text value) comparison (criteria value)<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param textValue - Text value to match criteria against
	 * @param criteria - Criteria comparison and value to use
	 * @return true if text value matches the specified criteria
	 */
	public static boolean text(String textValue, FindTextCriteria criteria)
	{
		return text(Conversion.nonNull(textValue), Conversion.nonNull(criteria.value), criteria.compare);
	}

	/**
	 * Searches the specified list according to the order induced by the specified method<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method uses the Collections.binarySearch as this method must follow the same rules. (This
	 * includes the results.)<BR>
	 * 2) The list must be sorted (ascending) prior to the binary search occurring or the results are
	 * undefined as such added flag to do this. (If sorting flag is checked, then the order should not matter
	 * as the same comparator is used.)<BR>
	 * 3) Specified method must take a single parameter of the object type and return an integer (same as the
	 * compareTo method when implementing Comparable&lt;T&gt;)<BR>
	 * 4) Specified method must exist or list will not be sorted<BR>
	 * 
	 * @param <T> - Object type to be searched
	 * @param data - List to be searched
	 * @param key - The key to be searched for
	 * @param method - Method to be used to sort
	 * @param sort - true to sort list before search
	 * @return The index of the search key, if it is contained in the list; otherwise less than 0
	 */
	public static <T> int binarySearch(List<T> data, T key, final String method, boolean sort)
	{
		if (sort)
			sort(data, method);

		return Collections.binarySearch(data, key, new Comparator<T>() {

			@Override
			public int compare(T arg0, T arg1)
			{
				try
				{
					Method m = arg0.getClass().getDeclaredMethod(method, arg1.getClass());
					return (Integer) m.invoke(arg0, arg1);
				}
				catch (Exception ex)
				{
					return 0;
				}
			}

		});
	}

	/**
	 * Returns mathematical modulus that is never negative.<BR>
	 * <BR>
	 * <B>Modulus is defined as: </B>A mod B = R, where A is the dividend, B is the divisor and R is the
	 * remainder. In Java, the modulus operator is % which makes the equation A % B = R.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If Divisor (B) is less than 0, then result may not be greater than 0 and possibly not correct as
	 * this was not tested/researched<BR>
	 * 
	 * @param dividend - Dividend (A)
	 * @param divisor - Divisor (B) which needs to be greater than 0
	 * @return remainder (R)
	 */
	public static int mod(int dividend, int divisor)
	{
		int remainder = dividend % divisor;

		// Adjust for java modulus
		if (remainder < 0)
			remainder += divisor;

		return remainder;
	}

	/**
	 * Checks if longs are considered equal based on specified variance
	 * 
	 * @param expected - Expected value
	 * @param actual - Actual value
	 * @param variance - Allowed variance value
	 * @return true if longs are within the specified variance else false
	 */
	public static boolean equals(long expected, long actual, int variance)
	{
		// Long values are exactly the same
		if (Long.compare(expected, actual) == 0)
			return true;

		if (Math.abs(expected - actual) < variance)
			return true;

		return false;
	}
}
