package com.automation.ui.common.sampleProject.exceptions;

@SuppressWarnings("serial")
public class LoginChangePasswordException extends RuntimeException {
	/**
	 * Exception to use when login fails due to alert about New Password & Confirm Password not matching
	 * 
	 * @param sError - Error Message
	 */
	public LoginChangePasswordException(String sError)
	{
		super(sError);
	}
}