package com.automation.ui.common.dataStructures;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.JS_Util;

/**
 * All supported browsers
 */
public enum Browser
{
	InternetExplorer("IE"), FireFox("FF"), Chrome("C"), Safari("S");

	private String _Code;

	/**
	 * Constructor that sets the code
	 * 
	 * @param _Code - Code for the browser
	 */
	private Browser(String _Code)
	{
		this._Code = _Code;
	}

	/**
	 * Uses the code for the return string
	 */
	public String toString()
	{
		return _Code;
	}

	/**
	 * Convert string to Browser enum
	 * 
	 * @param sValue - Value to convert
	 * @return Browser
	 */
	public static Browser to(String sValue)
	{
		if (sValue == null || sValue.equals(""))
			return InternetExplorer;

		//
		// Attempt matching on the default codes first
		//
		for (Browser key : Browser.values())
		{
			if (key.toString().equalsIgnoreCase(sValue))
				return key;
		}

		//
		// Additional matching codes
		//
		if (sValue.equalsIgnoreCase("FireFox")
				|| sValue.equalsIgnoreCase(DesiredCapabilities.firefox().getBrowserName()))
			return FireFox;

		if (sValue.equalsIgnoreCase("Chrome")
				|| sValue.equalsIgnoreCase(DesiredCapabilities.chrome().getBrowserName()))
			return Chrome;

		if (sValue.equalsIgnoreCase("SAFARI"))
			return Safari;

		return InternetExplorer;
	}

	/**
	 * Get browser from the driver
	 * 
	 * @param driver - driver (cannot be null)
	 * @return Browser
	 */
	public static Browser to(WebDriver driver)
	{
		String sValue = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
		return Browser.to(sValue);
	}

	/**
	 * Executes JavaScript to get navigator.userAgent string<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If IE, then string should contain 'MSIE'<BR>
	 * 2) If Chrome, then string should contain 'Chrome'<BR>
	 * 3) If Firefox, then string should contain 'Firefox'<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * <BR>
	 * 1) IE8: Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR
	 * 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)<BR>
	 * 2) Chrome: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
	 * Chrome/30.0.1599.69 Safari/537.36<BR>
	 * 3) Firefox: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0<BR>
	 * 
	 * @param driver
	 * @return non-null
	 */
	public static String getUserAgent(WebDriver driver)
	{
		try
		{
			String sJS = "return navigator.userAgent;";
			String sUserAgent = (String) JS_Util.execute(driver, sJS, null);
			return Conversion.nonNull(sUserAgent);
		}
		catch (Exception ex)
		{
		}

		return "";
	}
}
