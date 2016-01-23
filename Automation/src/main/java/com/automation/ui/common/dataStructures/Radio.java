package com.automation.ui.common.dataStructures;

import com.automation.ui.common.utilities.Rand;

/**
 * Generic Radio button options that occur often
 */
public enum Radio
{
	Yes, No, DEFAULT;

	/**
	 * Convert string to Radio enum<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If null or empty string, then random option returned<BR>
	 * 
	 * @param sValue - Value to convert
	 * @return Radio
	 */
	public static Radio to(String sValue)
	{
		if (sValue == null || sValue.equals(""))
			return (Radio) Rand.randomEnum(Radio.Yes, 10000);

		if (sValue.equalsIgnoreCase("N") || sValue.equalsIgnoreCase("NO"))
			return No;

		if (sValue.equalsIgnoreCase("D") || sValue.equalsIgnoreCase("Default"))
			return DEFAULT;

		return Yes;
	}
}
