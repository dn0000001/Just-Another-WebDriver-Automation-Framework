package com.automation.ui.common.dataStructures;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.automation.ui.common.utilities.Rand;

/**
 * This class is used to hold necessary information for check box
 */
public class CheckBox implements Comparable<CheckBox> {
	/**
	 * true to skip the check box
	 */
	public boolean skip;

	/**
	 * true to require check box in correct state such that end state is check
	 */
	public boolean verifyInitialState;

	/**
	 * true to verify check box is enabled before taking the action
	 */
	public boolean verifyEnabled;

	/**
	 * if true then log error else log warning (only applies if action is to be taken)
	 */
	public boolean logError;

	/**
	 * true to Select check box, false to UnSelect (when skip is false)
	 */
	public boolean check;

	/*
	 * Verification variables
	 */

	/**
	 * true to log success as well, false only a failure is logged
	 */
	public boolean logAll;

	/**
	 * Default Constructor - sets bSkip to true and all other variables to false
	 */
	public CheckBox()
	{
		init(true, false, false, false, false, false);
	}

	/**
	 * Constructor - Sets other variables to common values<BR>
	 * <BR>
	 * <B>Other Variables Set To:</B><BR>
	 * 1) skip = false<BR>
	 * 2) verifyInitialState = false<BR>
	 * 3) verifyEnabled = true<BR>
	 * 4) logError = true<BR>
	 * 5) logAll = false<BR>
	 * 
	 * @param bCheck - true to select check box
	 */
	public CheckBox(boolean bCheck)
	{
		init(false, false, true, true, bCheck, false);
	}

	/**
	 * Constructor - Initialize all variables
	 * 
	 * @param skip - true to skip the check box
	 * @param verifyInitialState - true to require check box in correct state such that end state is check
	 * @param verifyEnabled - true to verify check box is enabled before taking the action
	 * @param logError - if true then log error else log warning (only applies if action is to be taken)
	 * @param check - true to select check box
	 * @param logAll - For verification, true to log success as well, false only a failure is logged
	 */
	public CheckBox(boolean skip, boolean verifyInitialState, boolean verifyEnabled, boolean logError,
			boolean check, boolean logAll)
	{
		init(skip, verifyInitialState, verifyEnabled, logError, check, logAll);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param skip - true to skip the check box
	 * @param verifyInitialState - true to require check box in correct state such that end state is check
	 * @param verifyEnabled - true to verify check box is enabled before taking the action
	 * @param logError - if true then log error else log warning (only applies if action is to be taken)
	 * @param check - true to select check box
	 * @param logAll - For verification, true to log success as well, false only a failure is logged
	 */
	private void init(boolean skip, boolean verifyInitialState, boolean verifyEnabled, boolean logError,
			boolean check, boolean logAll)
	{
		this.skip = skip;
		this.verifyInitialState = verifyInitialState;
		this.verifyEnabled = verifyEnabled;
		this.logError = logError;
		this.check = check;
		this.logAll = logAll;
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return CheckBox
	 */
	public CheckBox copy()
	{
		return new CheckBox(skip, verifyInitialState, verifyEnabled, logError, check, logAll);
	}

	/**
	 * String for logging purposes
	 */
	public String toString()
	{
		return "Skip:  '" + String.valueOf(skip) + "', Check:  '" + String.valueOf(check)
				+ "', Verify Initial State:  " + String.valueOf(verifyInitialState) + ", VerifyEnabled:  '"
				+ String.valueOf(verifyEnabled) + "', Log Error:  '" + String.valueOf(logError)
				+ "', Log All:  '" + String.valueOf(logAll) + "'";
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return CheckBox
	 */
	public static CheckBox copy(CheckBox obj)
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
	 * Returns true if both objects are set to <B>skip</B> OR both objects have same <B>check</B> value
	 */
	public boolean equals(Object obj)
	{
		if (!this.getClass().isInstance(obj))
			return false;

		CheckBox cb = (CheckBox) obj;

		// If both objects set to skip, then return true
		if (this.skip && this.skip == cb.skip)
			return true;

		// Both objects set to perform same action regardless of skip value, then return true
		if (this.check == cb.check)
			return true;

		return false;
	}

	public int hashCode()
	{
		HashCodeBuilder builder = new HashCodeBuilder(21, 47);
		if (skip)
			builder.append(skip);
		else
			builder.append(check);

		return builder.toHashCode();
	}

	/**
	 * Returns a CheckBox that is set to skip<BR>
	 * <BR>
	 * <B>Variables Set To:</B><BR>
	 * 1) skip = true<BR>
	 * 2) verifyInitialState = false<BR>
	 * 3) verifyEnabled = false<BR>
	 * 4) logError = false<BR>
	 * 5) check = false<BR>
	 * 6) logAll = false<BR>
	 * 
	 * @return CheckBox
	 */
	public static CheckBox getSkip()
	{
		return new CheckBox(true, false, false, false, false, false);
	}

	/**
	 * Returns a CheckBox that is set to random selection<BR>
	 * <BR>
	 * <B>Variables Set To:</B><BR>
	 * 1) skip = false<BR>
	 * 2) verifyInitialState = false<BR>
	 * 3) verifyEnabled = true<BR>
	 * 4) logError = true<BR>
	 * 5) check = random<BR>
	 * 6) logAll = false<BR>
	 * 
	 * @return CheckBox
	 */
	public static CheckBox getRandom()
	{
		return new CheckBox(false, false, true, true, Rand.randomBoolean(), false);
	}

	/**
	 * Sorting a list of these objects only makes sense when the object is being reused for verification
	 * purposes in this case the sort order is based on check
	 */
	@Override
	public int compareTo(CheckBox arg0)
	{
		if (this.check == arg0.check)
			return 0;

		if (arg0.check)
			return 1;
		else
			return -1;
	}
}
