package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import com.automation.ui.common.dataStructures.GenericData;

/**
 * This is an abstract class for performing a find on a list
 * 
 * @param <T> - Data Type
 */
public abstract class BaseFind<T> {
	/**
	 * Find all items in the data list that match the criteria
	 * 
	 * @param data - List of objects to perform the search against
	 * @param criteria - Criteria to match against
	 * @param keys - Any enumeration value to use reflect to get all criteria lookup keys
	 * @return List of items matching the criteria
	 */
	public List<T> find(List<T> data, GenericData criteria, Enum<?> keys)
	{
		// Verify the criteria types are correct to prevent exceptions later
		verifyCriteriaTypes(criteria);
		List<T> foundMatches = new ArrayList<T>();

		// Return the empty list immediately if no criteria for better performance
		if (criteria.isEmpty())
			return foundMatches;

		// Go through each item and see if it satisfies the criteria
		for (T item : data)
		{
			TestResults results = new TestResults();
			for (Enum<?> key : keys.getDeclaringClass().getEnumConstants())
			{
				// If any criteria does not match for the item, then break the loop
				if (results.isError())
					break;

				// Only check the specified criteria
				if (criteria.containsKey(key))
					results.expectTrue(isMatch(item, criteria, key));
			}

			// If Item matched all selected criteria, then add to matching items list
			if (!results.isError())
				foundMatches.add(Cloner.deepClone(item));
		}

		return foundMatches;
	}

	/**
	 * Find First item in the data list that matches the criteria
	 * 
	 * @param data - List of objects to perform the search against
	 * @param criteria - Criteria to match against
	 * @param keys - Any enumeration value to use reflect to get all criteria lookup keys
	 * @return -2 if criteria is empty<BR>
	 *         -1 if no matching item<BR>
	 *         else valid index of matching item<BR>
	 */
	public int findFirst(List<T> data, GenericData criteria, Enum<?> keys)
	{
		return findFirst(data, 0, criteria, keys);
	}

	/**
	 * Find First item in the data list that matches the criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Start at index needs to equal to or greater than 0 or an out of bounds exception will occur<BR>
	 * 
	 * @param data - List of objects to perform the search against
	 * @param startAtIndex - Index to start the search from
	 * @param criteria - Criteria to match against
	 * @param keys - Any enumeration value to use reflect to get all criteria lookup keys
	 * @return -2 if criteria is empty<BR>
	 *         -1 if no matching item<BR>
	 *         else valid index of matching item<BR>
	 */
	public int findFirst(List<T> data, int startAtIndex, GenericData criteria, Enum<?> keys)
	{
		// Verify the criteria types are correct to prevent exceptions later
		verifyCriteriaTypes(criteria);

		// Return the empty list immediately if no criteria for better performance
		if (criteria.isEmpty())
			return -2;

		// Go through each item and see if it satisfies the criteria
		for (int i = startAtIndex; i < data.size(); i++)
		{
			T item = data.get(i);
			TestResults results = new TestResults();
			for (Enum<?> key : keys.getDeclaringClass().getEnumConstants())
			{
				// If any criteria does not match for the item, then break the loop
				if (results.isError())
					break;

				// Only check the specified criteria
				if (criteria.containsKey(key))
					results.expectTrue(isMatch(item, criteria, key));
			}

			// If Item matched all selected criteria, then return the item
			if (!results.isError())
				return i;
		}

		return -1;
	}

	/**
	 * Find All items in the data list that matches the criteria
	 * 
	 * @param data - List of objects to perform the search against
	 * @param criteria - Criteria to match against
	 * @param keys - Any enumeration value to use reflect to get all criteria lookup keys
	 * @return List of indexes that correspond to matching items
	 */
	public List<Integer> findAll(List<T> data, GenericData criteria, Enum<?> keys)
	{
		return findAll(data, 0, criteria, keys);
	}

	/**
	 * Find All items in the data list that matches the criteria
	 * 
	 * @param data - List of objects to perform the search against
	 * @param startAtIndex - Index to start the search from
	 * @param criteria - Criteria to match against
	 * @param keys - Any enumeration value to use reflect to get all criteria lookup keys
	 * @return List of indexes that correspond to matching items
	 */
	public List<Integer> findAll(List<T> data, int startAtIndex, GenericData criteria, Enum<?> keys)
	{
		List<Integer> matches = new ArrayList<Integer>();

		int useIndex = startAtIndex;
		while (true)
		{
			int match = findFirst(data, useIndex, criteria, keys);
			if (match >= 0)
			{
				matches.add(match);
				useIndex = match + 1;
			}
			else
			{
				break;
			}
		}

		return matches;
	}

	/**
	 * Verify the criteria types are correct to prevent exceptions later
	 * 
	 * @param criteria - Criteria to match against
	 */
	public abstract void verifyCriteriaTypes(GenericData criteria);

	/**
	 * Method to determine if data object satisfies the criteria corresponding to the key
	 * 
	 * @param item - Data to check if matches the criteria
	 * @param criteria - Criteria to match against
	 * @param key - The key corresponding to the criteria to determine match
	 * @return true if data object satisfies the specific key criteria
	 */
	protected abstract boolean isMatch(T item, GenericData criteria, Enum<?> key);
}
