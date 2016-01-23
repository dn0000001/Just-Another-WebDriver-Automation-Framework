package com.automation.ui.common.dataStructures;

/**
 * Drop Down selection options
 */
public enum Selection
{
	VisibleText, ValueHTML, Index, RegEx, Skip;

	/**
	 * Convert string to Selection enum
	 * 
	 * @param sValue - Value to convert
	 * @param defaultOption - Default Option if no match
	 * @return Selection
	 */
	public static Selection to(String sValue, Selection defaultOption)
	{
		if (sValue == null || sValue.equals(""))
			return defaultOption;

		if (sValue.equalsIgnoreCase("0") || sValue.equalsIgnoreCase("T") || sValue.equalsIgnoreCase("Text"))
			return VisibleText;

		if (sValue.equalsIgnoreCase("1") || sValue.equalsIgnoreCase("V") || sValue.equalsIgnoreCase("Value"))
			return ValueHTML;

		if (sValue.equalsIgnoreCase("2") || sValue.equalsIgnoreCase("I") || sValue.equalsIgnoreCase("Index"))
			return Index;

		if (sValue.equalsIgnoreCase("3") || sValue.equalsIgnoreCase("R") || sValue.equalsIgnoreCase("RegEx"))
			return RegEx;

		if (sValue.equalsIgnoreCase("4") || sValue.equalsIgnoreCase("S") || sValue.equalsIgnoreCase("Skip"))
			return Skip;

		return defaultOption;
	}
}
