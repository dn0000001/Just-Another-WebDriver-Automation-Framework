package com.automation.ui.common.sampleProject.dataStructures;

import com.automation.ui.common.utilities.Languages;

/**
 * Data Structure to hold variables for Login
 */
public class LoginDetails {
	public String sUserName, sPassword, sNewPassword, sConfirmPassword;
	public Languages lang; // Language to use
	public boolean bChangePassword; // Flag whether to click Change Password link

	/**
	 * Constructor that defaults Language to English & Change Password to false
	 * 
	 * @param sUserName - User Name
	 * @param sPassword - Password
	 */
	public LoginDetails(String sUserName, String sPassword)
	{
		// No language assume English user
		set(sUserName, sPassword, Languages.English, false, "", "");
	}

	/**
	 * Constructor that defaults Change Password to false
	 * 
	 * @param sUserName - User Name
	 * @param sPassword - Password
	 * @param lang - Language to use
	 */
	public LoginDetails(String sUserName, String sPassword, Languages lang)
	{
		set(sUserName, sPassword, lang, false, "", "");
	}

	/**
	 * Constructor that sets all variables
	 * 
	 * @param sUserName - User Name
	 * @param sPassword - Password
	 * @param lang - Language to use
	 * @param bChangePassword - Click Change Password link?
	 * @param sNewPassword - New Password
	 * @param sConfirmPassword - Confirm Password
	 */
	public LoginDetails(String sUserName, String sPassword, Languages lang, boolean bChangePassword,
			String sNewPassword, String sConfirmPassword)
	{
		set(sUserName, sPassword, lang, bChangePassword, sNewPassword, sConfirmPassword);
	}

	/**
	 * Sets all the variables.
	 * 
	 * @param sUserName - User Name
	 * @param sPassword - Password
	 * @param lang - Language to use
	 * @param bChangePassword - Click Change Password link?
	 * @param sNewPassword - New Password
	 * @param sConfirmPassword - Confirm Password
	 */
	private void set(String sUserName, String sPassword, Languages lang, boolean bChangePassword,
			String sNewPassword, String sConfirmPassword)
	{
		this.sUserName = sUserName;
		this.sPassword = sPassword;
		this.lang = lang;
		this.bChangePassword = bChangePassword;
		this.sNewPassword = sNewPassword;
		this.sConfirmPassword = sConfirmPassword;
	}

	/**
	 * Converts string into corresponding Languages. If no match, then English
	 * is returned.
	 * 
	 * @param sLanguage - Language to use as String
	 * @return
	 */
	public static Languages convert(String sLanguage)
	{
		/*
		 * If null is passed, then return English else try to match to specific
		 * language
		 */
		if (sLanguage == null)
			return Languages.English;

		/*
		 * See if sLanguage matches any of the supported languages
		 */
		if (sLanguage.equalsIgnoreCase("English") || sLanguage.equalsIgnoreCase("EN"))
			return Languages.English;

		if (sLanguage.equalsIgnoreCase("French") || sLanguage.equalsIgnoreCase("FR"))
			return Languages.French;

		// Could not find a matching language so just use default of English
		return Languages.English;
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return LoginDetails
	 */
	public LoginDetails copy()
	{
		return new LoginDetails(sUserName, sPassword, lang, bChangePassword, sNewPassword, sConfirmPassword);
	}
}