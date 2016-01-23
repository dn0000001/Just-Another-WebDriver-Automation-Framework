package com.automation.ui.common.sampleProject.exceptions;

@SuppressWarnings("serial")
public class LoginPasswordBlankException extends RuntimeException {
	/**
	 * Exception to use when login fails due to the password being blank
	 * 
	 * @param sError - Error Message
	 */
	public LoginPasswordBlankException(String sError)
	{
		super(sError);
	}
}