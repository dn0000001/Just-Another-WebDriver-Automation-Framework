package com.automation.ui.common.dataStructures;

import com.automation.ui.common.utilities.Compare;

/**
 * Comparison options
 */
public enum Comparison
{
	// Compare.contains options
	Lower, Upper, Standard,

	// Framework.isTextDisplayed options
	Contains, NotEqual, Equal, RegEx, DoesNotContain, EqualsIgnoreCase,

	// Equation options
	LessThan, LessThanEqualTo, GreaterThan, GreaterThanEqualTo;

	//
	// Variables to control string to enumeration conversion
	//
	private static final String _Contains = "*=";
	private static final String _NotEqual = "!=";
	private static final String _Equal = "==";
	private static final String _EqualsIgnoreCase = "~=";
	private static final String _RegEx = "regex";
	private static final String _DoesNotContain = "!*=";
	private static final String _Standard = "S";

	/**
	 * Convert String to Comparison enumeration<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The default return value is Comparison.Contains<BR>
	 * 
	 * @param value - Value to be converted
	 * @return Comparison
	 */
	public static Comparison to(String value)
	{
		if (Compare.equals(value, _Contains, Comparison.EqualsIgnoreCase))
		{
			return Comparison.Contains;
		}
		else if (Compare.equals(value, _NotEqual, Comparison.EqualsIgnoreCase))
		{
			return Comparison.NotEqual;
		}
		else if (Compare.equals(value, _Equal, Comparison.EqualsIgnoreCase))
		{
			return Comparison.Equal;
		}
		else if (Compare.equals(value, _EqualsIgnoreCase, Comparison.EqualsIgnoreCase))
		{
			return Comparison.EqualsIgnoreCase;
		}
		else if (Compare.equals(value, _RegEx, Comparison.EqualsIgnoreCase))
		{
			return Comparison.RegEx;
		}
		else if (Compare.equals(value, _DoesNotContain, Comparison.EqualsIgnoreCase))
		{
			return Comparison.DoesNotContain;
		}
		else if (Compare.equals(value, _Standard, Comparison.EqualsIgnoreCase))
		{
			return Comparison.Standard;
		}
		else
		{
			for (Comparison item : Comparison.values())
			{
				if (Compare.equals(value, item.toString(), Comparison.EqualsIgnoreCase))
					return item;
			}

			return Comparison.Contains;
		}
	}
}
