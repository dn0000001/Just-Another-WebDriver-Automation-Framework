package com.automation.ui.common.dataStructures;

import com.automation.ui.common.utilities.Conversion;

/**
 * This class is used to hold necessary information for auto complete field selections
 */
public class AutoCompleteField implements Comparable<AutoCompleteField> {
	/**
	 * true to skip entering information into this field, false to enter information
	 */
	public boolean skip;

	/**
	 * Value to enter into Auto Complete field
	 */
	public String value;

	/**
	 * Use to cancel the selection from the drop down after the value is entered into the field. This allows
	 * testing of values that are not in the auto complete drop down.
	 */
	public boolean cancelSelection;

	/**
	 * Use to select suggestion drop down option using an Index else Visible Text
	 */
	public boolean useIndex;

	/**
	 * How to select option from suggestion list that appears.<BR>
	 * <BR>
	 * <B>Note: When bUseIndex is true then value is an integer else it is visible text</B>
	 */
	public String selectOption;

	/**
	 * Contains the minimum number of characters that triggers the drop down suggestion list to appear
	 */
	public String triggerLength;

	/**
	 * Constructor - Initialize all variables (bSkip set to false)
	 * 
	 * @param value - Value to enter into the Auto Complete field
	 * @param cancelSelection - true to cancel selecting any drop down option
	 * @param useIndex - true to use Index for option selection else use Visible Text
	 * @param selectOption - Index or Text of the suggestion drop down list option to select. (Use empty
	 *            string to indicate random selection.)
	 * @param triggerLength - The minimum number of characters before the suggestion drop down
	 *            list will be displayed
	 */
	public AutoCompleteField(String value, boolean cancelSelection, boolean useIndex, String selectOption,
			String triggerLength)
	{
		init(false, value, cancelSelection, useIndex, selectOption, triggerLength);
	}

	/**
	 * Constructor - Initialize all variables
	 * 
	 * @param skip - true to skip entering information into this field, false to enter information
	 * @param value - Value to enter into the Auto Complete field
	 * @param cancelSelection - true to cancel selecting any drop down option
	 * @param useIndex - true to use Index for option selection else use Visible Text
	 * @param selectOption - Index or Text of the suggestion drop down list option to select. (Use empty
	 *            string to indicate random selection.)
	 * @param triggerLength - The minimum number of characters before the suggestion drop down
	 *            list will be displayed
	 */
	public AutoCompleteField(boolean skip, String value, boolean cancelSelection, boolean useIndex,
			String selectOption, String triggerLength)
	{
		init(skip, value, cancelSelection, useIndex, selectOption, triggerLength);
	}

	/**
	 * Constructor - Uses default values<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use this constructor if you want to compare data<BR>
	 * <BR>
	 * <B>Default Values:</B><BR>
	 * skip = false<BR>
	 * cancelSelection = true<BR>
	 * useIndex = true<BR>
	 * selectOption = empty string<BR>
	 * triggerLength = empty string<BR>
	 * 
	 * @param value - value to enter
	 */
	public AutoCompleteField(String value)
	{
		init(false, value, true, true, "", "");
	}

	/**
	 * Initialize all variables
	 * 
	 * @param skip - true to skip entering information into this field, false to enter information
	 * @param value - Value to enter into the Auto Complete field
	 * @param cancelSelection - true to cancel selecting any drop down option
	 * @param useIndex - true to use Index for option selection else use Visible Text
	 * @param selectOption - Index or Text of the suggestion drop down list option to select. (Use empty
	 *            string to indicate random selection.)
	 * @param triggerLength - The minimum number of characters before the suggestion drop down list will be
	 *            displayed
	 */
	private void init(boolean skip, String value, boolean cancelSelection, boolean useIndex,
			String selectOption, String triggerLength)
	{
		this.skip = skip;
		this.value = Conversion.nonNull(value);
		this.cancelSelection = cancelSelection;
		this.useIndex = useIndex;
		this.selectOption = Conversion.nonNull(selectOption);
		this.triggerLength = Conversion.nonNull(triggerLength);
	}

	/**
	 * Converts the selectOption (String) to an integer corresponding to the option to select from the
	 * suggestion list.<BR>
	 * <BR>
	 * <B>Note: </B> Use this method if always need index >= 0<BR>
	 * 
	 * @return Integer that is always greater than equal to 0
	 */
	public int getIndex()
	{
		return Conversion.parseInt(selectOption, 0);
	}

	/**
	 * Determines whether to use a random Index based on selectOption value (null or empty) provided user
	 * wants to use Index for the selection.
	 * 
	 * @return true if selectOption is null or the empty string & useIndex = true
	 */
	public boolean useRandomIndex()
	{
		// First user must want to use Index
		if (useIndex)
		{
			// If the option they want to select is less than 0, then do random
			if (Conversion.parseInt(selectOption, -1) < 0)
				return true;
			else
				return false;
		}
		else
			return false;
	}

	/**
	 * Determines whether the input text (value) should trigger a drop down suggestion list based on minimum
	 * trigger length (triggerLength.) For example, the suggestion drop down list may only appear if at least
	 * 3 characters are entered.
	 * 
	 * @return true if suggestion drop down list is expected to be displayed else false
	 */
	public boolean triggerSuggestions()
	{
		if (value.length() >= Conversion.parseInt(triggerLength, 0))
			return true;
		else
			return false;
	}

	/**
	 * Gets the minimum length that should trigger the drop down suggestions list by converting triggerLength
	 * to an integer
	 * 
	 * @return always greater than equal to 0
	 */
	public int getTriggerLength()
	{
		return Conversion.parseInt(triggerLength, 0);
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return AutoCompleteField
	 */
	public AutoCompleteField copy()
	{
		return new AutoCompleteField(skip, value, cancelSelection, useIndex, selectOption, triggerLength);
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return AutoCompleteField
	 */
	public static AutoCompleteField copy(AutoCompleteField obj)
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
	 * String for logging purposes
	 */
	public String toString()
	{
		return "Skip:  " + String.valueOf(skip) + ", Value:  '" + value + "', Cancel Selection:  "
				+ String.valueOf(cancelSelection) + ", Use Index:  " + String.valueOf(useIndex)
				+ ", Select Option:  '" + selectOption + "', Trigger Length:  '" + triggerLength + "'";
	}

	/**
	 * Convert the auto complete field object to an input field object
	 * 
	 * @return InputField
	 */
	public InputField toInputField()
	{
		return new InputField(skip, value, "");
	}

	/**
	 * Returns an AutoCompleteField that is set to skip<BR>
	 * <BR>
	 * <B>Variables Set To:</B><BR>
	 * 1) skip = true<BR>
	 * 2) value = empty string<BR>
	 * 3) cancelSelection = true<BR>
	 * 4) useIndex = true<BR>
	 * 5) selectOption = empty string<BR>
	 * 6) triggerLength = "1"<BR>
	 * 
	 * @return AutoCompleteField
	 */
	public static AutoCompleteField getSkip()
	{
		return new AutoCompleteField(true, "", true, true, "", "1");
	}

	/**
	 * Returns true if both objects are set to <B>skip</B> OR both objects have same <B>Verification Value</B>
	 */
	public boolean equals(Object obj)
	{
		if (!this.getClass().isInstance(obj))
			return false;

		AutoCompleteField auto = (AutoCompleteField) obj;

		// If both objects set to skip, then return true
		if (this.skip && this.skip == auto.skip)
			return true;

		// Both objects have same Verification Value regardless of skip value, then return true
		if (this.toInputField().equals(auto.toInputField()))
			return true;

		return false;
	}

	public int hashCode()
	{
		InputField field = toInputField();
		return field.hashCode();
	}

	/**
	 * Sorting a list of these objects only makes sense when the object is being reused for verification
	 * purposes in this case the sort order is based on the InputField sort order which uses
	 * getValueToInput().
	 */
	@Override
	public int compareTo(AutoCompleteField arg0)
	{
		int nResult = this.toInputField().compareTo(arg0.toInputField());
		if (nResult < 0)
			return -1;
		else if (nResult > 0)
			return 1;
		else
			return 0;
	}
}
