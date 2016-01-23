package com.automation.ui.common.utilities;

import java.util.Locale;

/**
 * Enumeration for all supported languages
 */
public enum Languages
{
	KEY, English, French;

	/**
	 * Converts a String to corresponding Languages enumeration.<BR>
	 * <B>Note:</B> If cannot find a match (not case sensitive), then English is returned.<BR>
	 * <BR>
	 * <B>Supported Conversions:</B><BR>
	 * 1) French or FR => Languages.French<BR>
	 * 2) Anything else => Languages.English
	 * 
	 * @param sValue - Convert this string to corresponding Languages value
	 * @return Languages
	 */
	public static Languages toLanguages(String sValue)
	{
		if (sValue.equalsIgnoreCase("FRENCH") || sValue.equalsIgnoreCase("FR"))
			return Languages.French;

		return Languages.English;
	}

	/**
	 * Converts a Languages enumeration to corresponding Locale
	 * 
	 * @param lang - Languages enumeration to find corresponding locale
	 * @return Locale
	 */
	public static Locale toLocale(Languages lang)
	{
		if (lang == Languages.French)
			return Locale.FRENCH;

		return Locale.ENGLISH;
	}
}
