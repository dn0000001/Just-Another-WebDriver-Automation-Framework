package com.automation.ui.common.dataStructures;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Conversion;

/**
 * This class is used to hold necessary information for input fields that have default values and you want
 * enough information to be able handle the following cases:<BR>
 * 1) Leave the default value <B>OR</B><BR>
 * 2) Enter Blank into the field but it is a required field <B>OR</B><BR>
 * 3) Enter any value into the field <B>OR</B><BR>
 * 4) Enter some random information into the field<BR>
 */
public class InputField implements Comparable<InputField> {
	/**
	 * true to skip entering information into this field, false to enter information
	 */
	public boolean skip;

	/**
	 * value to enter (<B>null</B> value is used to indicate random)
	 */
	public String value;

	/**
	 * Random Value to use if necessary
	 */
	public String randomValue;

	/*
	 * Verification variables
	 */

	/**
	 * true to do a case sensitive comparison, false to do a case insensitive comparison
	 */
	public boolean caseSensitive;

	/**
	 * true to log success as well, false only a failure is logged
	 */
	public boolean logAll;

	/**
	 * Mask to remove characters from the input value (The empty string or null does not remove any
	 * characters. This is used for verification on fields that only allow the input of specific characters by
	 * using JavaScript as the user types in the field.)
	 */
	public String mask;

	/**
	 * The max length allowed by the field. Use -1 to skip field length validation or if no max length
	 */
	public int maxLength;

	/**
	 * Constructor to set defaults for a field that will allow random input
	 */
	public InputField()
	{
		init(false, null, "", false, false, "", -1);
	}

	/**
	 * Constructor - Initialize all variables
	 * 
	 * @param skip - true to skip entering information into this field, false to enter information
	 * @param value - value to enter (<B>null</B> value is used to indicate random)
	 * @param randomValue - Random Value to use if necessary
	 * @param caseSensitive - true to do a case sensitive comparison, false to do a case insensitive
	 *            comparison
	 * @param logAll - true to log success as well, false only a failure is logged
	 * @param mask - Mask to remove characters from the input value (The empty string or null does not remove
	 *            any characters. This is used for verification on fields that only allow the input of
	 *            specific characters by using JavaScript as the user types in the field.)
	 * @param maxLength - Max Length of field (Use -1 for no max length to be applied)
	 */
	public InputField(boolean skip, String value, String randomValue, boolean caseSensitive, boolean logAll,
			String mask, int maxLength)
	{
		init(skip, value, randomValue, caseSensitive, logAll, mask, maxLength);
	}

	/**
	 * Constructor - Verification variables set to false and mask set to empty string & Max Length set to -1
	 * 
	 * @param skip - true to skip entering information into this field, false to enter information
	 * @param value - value to enter (<B>null</B> value is used to indicate random)
	 * @param randomValue - Random Value to use if necessary
	 */
	public InputField(boolean skip, String value, String randomValue)
	{
		init(skip, value, randomValue, false, false, "", -1);
	}

	/**
	 * Constructor - Uses default values<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use this constructor if you want to compare data<BR>
	 * 2) Use this constructor if you want to enter specific value (no option for random)<BR>
	 * <BR>
	 * <B>Default Values:</B><BR>
	 * skip = false<BR>
	 * randomValue = empty string<BR>
	 * caseSensitive = false<BR>
	 * logAll = false<BR>
	 * mask = empty string<BR>
	 * maxLength = -1<BR>
	 * 
	 * @param value - value to enter
	 */
	public InputField(String value)
	{
		init(false, value, "", false, false, "", -1);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param skip - true to skip entering information into this field, false to enter information
	 * @param value - value to enter (<B>null</B> value is used to indicate random)
	 * @param randomValue - Random Value to use if necessary
	 * @param caseSensitive - true to do a case sensitive comparison, false to do a case insensitive
	 *            comparison
	 * @param logAll - true to log success as well, false only a failure is logged
	 * @param mask - Mask to remove characters from the input value (The empty string or null does not remove
	 *            any characters. This is used for verification on fields that only allow the input of
	 *            specific characters by using JavaScript as the user types in the field.)
	 * @param maxLength - Max Length of field (Use -1 for no max length to be applied)
	 */
	private void init(boolean skip, String value, String randomValue, boolean caseSensitive, boolean logAll,
			String mask, int maxLength)
	{
		this.skip = skip;
		this.value = value;
		this.randomValue = Conversion.nonNull(randomValue);

		this.caseSensitive = caseSensitive;
		this.logAll = logAll;
		this.mask = mask;
		this.maxLength = maxLength;
	}

	/**
	 * Determines if you should modify the field or not
	 * 
	 * @return true to modify the field, false to skip modifying field to retain default value
	 */
	public boolean modifyField()
	{
		if (skip)
			return false;
		else
			return true;
	}

	/**
	 * Determines if you should use the given value or generate a random value<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Only considers the sValue variable<BR>
	 * 2) <B>null</B> indicates to use the random value<BR>
	 * 
	 * @return true to use the random value, false to use specified value
	 */
	public boolean useRandomValue()
	{
		if (value == null)
			return true;
		else
			return false;
	}

	/**
	 * Determines whether to return sValue or sRandomValue based on if sValue is null or not<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Does not consider whether the field should be skipped<BR>
	 * 2) Only considers the sValue variable<BR>
	 * 3) <B>null</B> indicates to use the random value<BR>
	 * 
	 * @return The value to be input into the field
	 */
	public String getValueToInput()
	{
		if (useRandomValue())
			return randomValue;
		else
			return value;
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return InputField
	 */
	public InputField copy()
	{
		return new InputField(skip, value, randomValue, caseSensitive, logAll, mask, maxLength);
	}

	/**
	 * String for logging purposes
	 */
	public String toString()
	{
		String value2, random;
		if (value == null)
			value2 = "null";
		else
			value2 = value;

		if (randomValue == null)
			random = "null";
		else
			random = randomValue;

		return "Skip Field:  " + String.valueOf(skip) + ", Value:  '" + value2 + "', Random Value:  '"
				+ random + "', Case Sensitive Verify:  " + String.valueOf(caseSensitive) + ", Log All:  "
				+ String.valueOf(logAll) + ", Verification Mask:  " + Conversion.nonNull(mask)
				+ ", Max Length:  " + maxLength;
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return InputField
	 */
	public static InputField copy(InputField obj)
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
	 * Appends value to randomValue<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) randomValue = randomValue + sAppendValue<BR>
	 * 2) Null values converted to empty string<BR>
	 * 
	 * @param sAppendValue - Value to append to the randomValue variable
	 */
	public void appendRandom(String sAppendValue)
	{
		randomValue = Conversion.nonNull(randomValue) + Conversion.nonNull(sAppendValue);
	}

	/**
	 * Prepends value to randomValue<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) randomValue = sPrependValue + randomValue <BR>
	 * 2) Null values converted to empty string<BR>
	 * 
	 * @param sPrependValue - Value to prepend to the randomValue variable
	 */
	public void prependRandom(String sPrependValue)
	{
		randomValue = Conversion.nonNull(sPrependValue) + Conversion.nonNull(randomValue);
	}

	/**
	 * Applies the mask & max length to the input value to get the expected field value for verification
	 * purposes<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the mask is the empty string or null, then no characters are removed from the input string.<BR>
	 * 2) If the mask is non-empty and non-null, then the characters are removed using the regular expression
	 * mask<BR>
	 * 3) If maxLength > 0, then string is truncated to the max length after the mask is applied<BR>
	 * 4) Does not consider whether the field should be skipped<BR>
	 * 
	 * @return expected string for verification
	 */
	public String getVerificationValue()
	{
		// Apply mask to field
		String sMaskedValue = "";
		if (mask == null || mask.equals(""))
			sMaskedValue = getValueToInput();
		else
			sMaskedValue = Conversion.nonNull(getValueToInput()).replaceAll(mask, "");

		// Apply max length validation to field
		if (maxLength > 0)
		{
			if (maxLength > sMaskedValue.length())
				return sMaskedValue.substring(0, sMaskedValue.length());
			else
				return sMaskedValue.substring(0, maxLength);
		}
		else
		{
			return sMaskedValue;
		}
	}

	/**
	 * Returns true if both objects are set to <B>skip</B> OR both objects have same <B>Verification Value</B>
	 */
	public boolean equals(Object obj)
	{
		if (!this.getClass().isInstance(obj))
			return false;

		InputField in = (InputField) obj;

		// If both objects set to skip, then return true
		if (this.skip && this.skip == in.skip)
			return true;

		// Perform case sensitive compare?
		Comparison option = Comparison.EqualsIgnoreCase;
		if (caseSensitive)
			option = Comparison.Equal;

		// Both objects have same Verification Value regardless of skip value, then return true
		return Compare.equals(this.getVerificationValue(), in.getVerificationValue(), option);
	}

	public int hashCode()
	{
		HashCodeBuilder builder = new HashCodeBuilder(23, 49);
		if (skip)
			builder.append(skip);
		else
			builder.append(getVerificationValue());

		return builder.toHashCode();
	}

	/**
	 * Returns a InputField that is set to skip<BR>
	 * <BR>
	 * <B>Variables Set To:</B><BR>
	 * 1) skip = true<BR>
	 * 2) value = empty string<BR>
	 * 3) randomValue = empty string<BR>
	 * 4) caseSensitive - false<BR>
	 * 5) logAll - false<BR>
	 * 6) mask - empty string<BR>
	 * 7) maxLength = -1<BR>
	 * 
	 * @return InputField
	 */
	public static InputField getSkip()
	{
		return new InputField(true, "", "", false, false, "", -1);
	}

	/**
	 * Returns a InputField that is set to random input<BR>
	 * <BR>
	 * <B>Variables Set To:</B><BR>
	 * 1) skip = false<BR>
	 * 2) value = null<BR>
	 * 3) randomValue = sRandValue<BR>
	 * 4) caseSensitive - false<BR>
	 * 5) logAll - false<BR>
	 * 6) mask - empty string<BR>
	 * 7) maxLength = -1<BR>
	 * 
	 * @param sRandValue - Random Value
	 * @return InputField
	 */
	public static InputField getRandom(String sRandValue)
	{
		return new InputField(false, null, sRandValue, false, false, "", -1);
	}

	/**
	 * Sorting a list of these objects only makes sense when the object is being reused for verification
	 * purposes in this case the sort order is based on getVerificationValue().
	 */
	@Override
	public int compareTo(InputField arg0)
	{
		int nResult = this.getVerificationValue().compareTo(arg0.getVerificationValue());
		if (nResult < 0)
			return -1;
		else if (nResult > 0)
			return 1;
		else
			return 0;
	}

	/**
	 * Set Mask to remove anything that is not a digit
	 */
	public void setMask_OnlyDigits()
	{
		mask = "\\D";
	}

	/**
	 * Set Mask to remove anything that is not a letter (ASCII alphabet)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Accent characters will also be removed such as é or ä<BR>
	 * 2) Spaces will also be removed<BR>
	 */
	public void setMask_OnlyLetters()
	{
		mask = "[^A-Za-z]";
	}
}
