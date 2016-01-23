package com.automation.ui.common.sampleProject.exceptions;

@SuppressWarnings("serial")
public class LoginCredentialsWrongException extends RuntimeException {
	/**
	 * Exception to use when login fails due to the credentials being wrong
	 * 
	 * @param sError - Error Message
	 */
	public LoginCredentialsWrongException(String sError)
	{
		super(sError);
	}
}