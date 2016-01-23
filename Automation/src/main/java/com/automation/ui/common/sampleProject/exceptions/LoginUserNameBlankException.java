package com.automation.ui.common.sampleProject.exceptions;

@SuppressWarnings("serial")
public class LoginUserNameBlankException extends RuntimeException {
	/**
	 * Exception to use when login fails due to the user name being blank
	 * 
	 * @param sError - Error Message
	 */
	public LoginUserNameBlankException(String sError)
	{
		super(sError);
	}
}