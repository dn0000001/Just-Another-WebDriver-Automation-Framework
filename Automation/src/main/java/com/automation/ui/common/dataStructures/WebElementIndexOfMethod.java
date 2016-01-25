package com.automation.ui.common.dataStructures;

import com.automation.ui.common.utilities.Compare;

/**
 * This enumeration contains the supported methods of comparing WebElements
 */
public enum WebElementIndexOfMethod
{
	VisibleText, // Visible Text comparison using standard equals to find a match
	VisibleText_RegEx, // Visible Text comparison using regular expression to find a match
	VisibleText_Contains, // Visible Text comparison using standard contains to find a match

	JS_RegEx, // Uses JavaScript to get text and perform regular expression to find a match
	JS_Contains, // Uses JavaScript to get text and perform a standard contains to find a match

	Attribute_RegEx, // Comparison done against an attribute using regular expression to find a match
	Attribute_Contains, // Comparison done against an attribute using standard contains to find a match

	Text, // For New Search, the WebElement's text is extracted using getText for comparison
	Attribute, // For New Search, the WebElement's attribute is extracted using getAttribute for comparison
	JavaScript; // For New Search, the WebElement's text is extracted using JavaScript

	//
	// Variables to control string to enumeration conversion
	// Note: These are only for the New Search options
	//
	private static final String _Text = "T";
	private static final String _Attribute = "A";
	private static final String _JavaScript = "J";

	/**
	 * Convert String to WebElementIndexOfMethod enumeration<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The default return value is WebElementIndexOfMethod.Text<BR>
	 * 
	 * @param value - Value to be converted
	 * @return WebElementIndexOfMethod
	 */
	public static WebElementIndexOfMethod to(String value)
	{
		if (Compare.equals(value, _Text, Comparison.EqualsIgnoreCase))
		{
			return WebElementIndexOfMethod.Text;
		}
		else if (Compare.equals(value, _Attribute, Comparison.EqualsIgnoreCase))
		{
			return WebElementIndexOfMethod.Attribute;
		}
		else if (Compare.equals(value, _JavaScript, Comparison.EqualsIgnoreCase))
		{
			return WebElementIndexOfMethod.JavaScript;
		}
		else
		{
			for (WebElementIndexOfMethod item : WebElementIndexOfMethod.values())
			{
				if (Compare.equals(value, item.toString(), Comparison.EqualsIgnoreCase))
					return item;
			}

			return WebElementIndexOfMethod.Text;
		}
	}
}
