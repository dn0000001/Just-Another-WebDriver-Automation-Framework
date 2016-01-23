package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Cookie;

import com.automation.ui.common.dataStructures.Parameter;

/**
 * The class provides methods to work with cookies
 */
public class CookieUtil {
	private final static String _CookieSeparator = "; ";
	private final static String _CookieDelimiter = ";";
	private final static String _CookieNameValueSeparator = "=";

	/**
	 * Combines all cookies in the set to be a String without the specified cookies<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) String can be used with method HttpURLConnection.setRequestProperty("Cookie", sAllCookies)<BR>
	 * 
	 * @param cookies - Cookies to converted to a String
	 * @param removeCookies - List of cookies to not add to the string
	 * @return Empty String if null else String that combines all cookies
	 */
	public static String toString(Set<Cookie> cookies, List<String> removeCookies)
	{
		String sAllCookies = "";
		if (cookies == null)
			return sAllCookies;

		int nCount = 0;
		for (Cookie cookie : cookies)
		{
			if (removeCookies != null && removeCookies.contains(cookie.getName()))
				continue;

			if (nCount != 0)
			{
				sAllCookies += _CookieSeparator;
			}

			sAllCookies += cookie.getName() + _CookieNameValueSeparator + cookie.getValue();
			nCount++;
		}

		return sAllCookies;
	}

	/**
	 * Combines all cookies in the set to be a String<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) String can be used with method HttpURLConnection.setRequestProperty("Cookie", sAllCookies)<BR>
	 * 
	 * @param cookies - Cookies to converted to a String
	 * @return Empty String if null else String that combines all cookies
	 */
	public static String toString(Set<Cookie> cookies)
	{
		return toString(cookies, null);
	}

	/**
	 * Combines all cookies in the list to be a String<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) String can be used with method HttpURLConnection.setRequestProperty("Cookie", sAllCookies)<BR>
	 * 
	 * @param cookies - List of cookies using Parameter object
	 * @return Empty String if null else String that combines all cookies
	 */
	public static String toString(List<Parameter> cookies)
	{
		String sAllCookies = "";
		if (cookies == null)
			return sAllCookies;

		int nCount = 0;
		for (Parameter cookie : cookies)
		{
			if (nCount != 0)
			{
				sAllCookies += _CookieSeparator;
			}

			sAllCookies += cookie.param + _CookieNameValueSeparator + cookie.value;
			nCount++;
		}

		return sAllCookies;
	}

	/**
	 * Extracts the cookies from the string and puts into a list<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) List is never null<BR>
	 * 
	 * @param sAllCookies - String that contains all cookies
	 * @return List&lt;Parameter&gt;
	 */
	public static List<Parameter> extract(String sAllCookies)
	{
		List<Parameter> cookies = new ArrayList<Parameter>();

		// Return if no work to be done
		if (sAllCookies == null || sAllCookies.trim().equals(""))
			return cookies;

		// Get all the cookies
		String[] splitCookies = sAllCookies.split(_CookieDelimiter);
		for (String parse : splitCookies)
		{
			// For each cookie, extract the name and value
			String[] parseCookie = parse.trim().split(_CookieNameValueSeparator);
			if (parseCookie.length == 2)
			{
				cookies.add(new Parameter(parseCookie[0].trim(), parseCookie[1].trim()));
			}
			else
			{
				/*
				 * Cookie could be malformed or no value. In both cases, just set the name and leave value
				 * empty
				 */
				cookies.add(new Parameter(parse.trim(), ""));
			}
		}

		return cookies;
	}
}
