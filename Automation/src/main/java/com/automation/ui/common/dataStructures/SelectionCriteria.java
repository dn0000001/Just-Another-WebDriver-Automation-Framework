package com.automation.ui.common.dataStructures;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Conversion;

/**
 * This class can be used whenever you need to store selection criteria.<BR>
 * <BR>
 * <B>Examples of appropriate use:</B><BR>
 * 1) After doing a search you need to select a result<BR>
 * 2) You want flexible Drop down selection criteria i.e. match using regular expression, visible text or
 * value.<BR>
 */
public class SelectionCriteria implements Comparable<SelectionCriteria> {
	/**
	 * How to find what you are looking for
	 */
	public String findMethod;

	/**
	 * Search Criteria based on selection criteria
	 */
	public String value;

	/**
	 * Constructor
	 * 
	 * @param findMethod - How to find what you are looking for
	 * @param value - Search Criteria based on selection criteria
	 */
	public SelectionCriteria(String findMethod, String value)
	{
		this.findMethod = Conversion.nonNull(findMethod);
		this.value = Conversion.nonNull(value);
	}

	/**
	 * Constructor used to set the find method using the specified index flag
	 * 
	 * @param index - true for index find method
	 * @param value - Search Criteria based on selection criteria
	 */
	public SelectionCriteria(boolean index, String value)
	{
		String sFindMethod = "false";
		if (index)
			sFindMethod = "true";

		this.findMethod = Conversion.nonNull(sFindMethod);
		this.value = Conversion.nonNull(value);
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return SelectionCriteria
	 */
	public SelectionCriteria copy()
	{
		return new SelectionCriteria(findMethod, value);
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return SelectionCriteria
	 */
	public static SelectionCriteria copy(SelectionCriteria obj)
	{
		try
		{
			return obj.copy();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Returns String that represents object
	 */
	public String toString()
	{
		return "Find Method:  '" + findMethod + "', Value:  '" + value + "'";
	}

	public boolean equals(Object obj)
	{
		List<String> excludeFields = new ArrayList<String>();
		return Compare.equals(this, obj, excludeFields);
	}

	public int hashCode()
	{
		List<String> excludeFields = new ArrayList<String>();
		return HashCodeBuilder.reflectionHashCode(this, excludeFields);
	}

	public int compareTo(SelectionCriteria arg0)
	{
		List<String> excludeFields = new ArrayList<String>();
		return Compare.compareTo(this, arg0, excludeFields);
	}
}
