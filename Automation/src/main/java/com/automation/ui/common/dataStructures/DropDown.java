package com.automation.ui.common.dataStructures;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This class is used to hold necessary information for drop down selection
 */
public class DropDown implements Comparable<DropDown> {
	// Used to determine corresponding nUsing value
	private static final String VISIBLE_TEXT = "Text";
	private static final String INDEX = "Index";
	private static final String VALUE = "Value";
	private static final String REGEX = "RegEx";
	private static final String SKIP = "Skip";

	/**
	 * How to select the drop down option using Visible Text (default), Regular Expression, Value, Index or
	 * Skip
	 */
	public Selection using;

	/**
	 * The option from the drop down to be selected (using specified method of using.)
	 */
	public String option;

	/**
	 * For random option, Minimum Random Index for selection
	 */
	public int minIndex;

	/*
	 * Verification variables
	 */

	/**
	 * true to log success as well, false only a failure is logged
	 */
	public boolean logAll;

	/**
	 * Default Constructor which sets drop down to be selected using Visible Text and the Option to be the
	 * empty string.
	 */
	public DropDown()
	{
		init(Selection.VisibleText, "", 0, false);
	}

	/**
	 * Constructor which sets drop down to be selected using Visible Text
	 * 
	 * @param option - Drop down option to be selected
	 */
	public DropDown(String option)
	{
		init(Selection.VisibleText, option, 0, false);
	}

	/**
	 * Constructor verifies using is valid before assigning values<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If using is not valid, then defaults to Visible Text<BR>
	 * 2) Null values will be converted to default values<BR>
	 * 3) Sets logAll to false<BR>
	 * 
	 * @param using - How to select drop down option
	 * @param option - Drop down option to be selected
	 * @param minIndex - For random option, Minimum Random Index for selection
	 */
	public DropDown(Selection using, String option, int minIndex)
	{
		init(using, option, minIndex, false);
	}

	/**
	 * Constructor verifies using is valid before assigning values<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If using is not valid, then defaults to Visible Text<BR>
	 * 2) Null values will be converted to default values<BR>
	 * 
	 * @param using - How to select drop down option
	 * @param option - Drop down option to be selected
	 * @param minIndex - For random option, Minimum Random Index for selection
	 * @param logAll - For verification, true to log success as well, false only a failure is logged
	 */
	public DropDown(Selection using, String option, int minIndex, boolean logAll)
	{
		init(using, option, minIndex, logAll);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param using - How to select drop down option
	 * @param option - Drop down option to be selected
	 * @param minIndex - For random option, Minimum Random Index for selection
	 * @param logAll - For verification, true to log success as well, false only a failure is logged
	 */
	private void init(Selection using, String option, int minIndex, boolean logAll)
	{
		// Don't allow any invalid way to select the drop down, default to Selection.VisibleText
		if (using == null)
			this.using = Selection.VisibleText;
		else
			this.using = using;

		// Don't allow null value to be assigned, default to empty string
		if (option == null)
			this.option = "";
		else
			this.option = option;

		// Don't allow random index to be less than 0
		if (minIndex < 0)
			this.minIndex = 0;
		else
			this.minIndex = minIndex;

		this.logAll = logAll;
	}

	/**
	 * Returns true if both using & option are the same
	 */
	public boolean equals(Object objDropDown)
	{
		if (!this.getClass().isInstance(objDropDown))
			return false;

		DropDown dd = (DropDown) objDropDown;
		if (dd.using == using && dd.option.equalsIgnoreCase(option))
			return true;

		return false;
	}

	public int hashCode()
	{
		HashCodeBuilder builder = new HashCodeBuilder(19, 45);
		builder.append(using);
		builder.append(option);
		return builder.toHashCode();
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return DropDown
	 */
	public DropDown copy()
	{
		return new DropDown(using, option, minIndex, logAll);
	}

	/**
	 * Based on selection criteria for the drop down does a comparison to determine if it is the default as
	 * specified.<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) All comparisons are case sensitive<BR>
	 * 2) For Regular Expression match, the default visible text is used<BR>
	 * 
	 * @param dd - DropDown object to check
	 * @param sDefaultVisible - Default Visible Text value
	 * @param sDefaultValue - Default (HTML) Value Text
	 * @param sDefaultIndex - Default Index value
	 * @return true if DropDown object is the default option else false
	 */
	public static boolean isDefault(DropDown dd, String sDefaultVisible, String sDefaultValue,
			String sDefaultIndex)
	{
		try
		{
			// Select drop down using Visible Text
			if (dd.using == Selection.VisibleText)
			{
				// Just compare with the Default Visible Text option
				if (dd.option.equals(sDefaultVisible))
					return true;
				else
					return false;
			}

			// Select drop down using (HTML) Value
			if (dd.using == Selection.ValueHTML)
			{
				// Just compare with the Default (HTML) Value option
				if (dd.option.equals(sDefaultValue))
					return true;
				else
					return false;
			}

			// Select drop down using Index
			if (dd.using == Selection.Index)
			{
				// Just compare with the Default Index option
				if (dd.option.equals(sDefaultIndex))
					return true;
				else
					return false;
			}

			// Select drop down using Regular Expression
			if (dd.using == Selection.RegEx)
			{
				// Try to match the default visible text to the regular expression
				if (sDefaultVisible.matches(dd.option))
					return true;
				else
					return false;
			}
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return DropDown
	 */
	public static DropDown copy(DropDown obj)
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
		return "Using:  " + using + " ('" + getUsing() + "')" + ", Option:  '" + option + "', Min Index:  "
				+ minIndex + "', Log All:  '" + String.valueOf(logAll) + "'";
	}

	/**
	 * Convert using to String
	 * 
	 * @return String
	 */
	private String getUsing()
	{
		if (using == Selection.Index)
			return DropDown.INDEX;

		if (using == Selection.RegEx)
			return DropDown.REGEX;

		if (using == Selection.ValueHTML)
			return DropDown.VALUE;

		if (using == Selection.Skip)
			return DropDown.SKIP;

		return DropDown.VISIBLE_TEXT;
	}

	/**
	 * Returns a DropDown that is set to skip<BR>
	 * <BR>
	 * <B>Variables Set To:</B><BR>
	 * 1) using = Selection.Skip<BR>
	 * 2) option = "-1"<BR>
	 * 3) minIndex = 0<BR>
	 * 4) logAll = false<BR>
	 * 
	 * @return DropDown
	 */
	public static DropDown getSkip()
	{
		return new DropDown(Selection.Skip, "-1", 0);
	}

	/**
	 * Returns a DropDown that is set to random selection<BR>
	 * <BR>
	 * <B>Variables Set To:</B><BR>
	 * 1) using = Selection.Index<BR>
	 * 2) option = "-1"<BR>
	 * 3) minIndex = nMinRandomIndex<BR>
	 * 4) logAll = false<BR>
	 * 
	 * @param nMinRandomIndex - Minimum Random Index for selection
	 * @return DropDown
	 */
	public static DropDown getRandom(int nMinRandomIndex)
	{
		return new DropDown(Selection.Index, "-1", nMinRandomIndex);
	}

	/**
	 * Sorting a list of these objects only makes sense when the object is being reused for verification
	 * purposes in this case the sort order is based on option.
	 */
	@Override
	public int compareTo(DropDown arg0)
	{
		int nResult = this.option.compareTo(arg0.option);
		if (nResult < 0)
			return -1;
		else if (nResult > 0)
			return 1;
		else
			return 0;
	}
}
