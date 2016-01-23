package com.automation.ui.common.sampleProject.exceptions;

@SuppressWarnings("serial")
public class LoginCannotUseSamePasswordException extends RuntimeException {
	/**
	 * Exception to use when login fails due to re-using an old password as part of the change password
	 * section
	 * 
	 * @param sError - Error Message
	 */
	public LoginCannotUseSamePasswordException(String sError)
	{
		super(sError);
	}
}