package com.automation.ui.common.dataStructures;

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
	Attribute_Contains; // Comparison done against an attribute using standard contains to find a match
}
