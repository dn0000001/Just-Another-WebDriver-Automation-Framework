package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class CheckBoxWrongStateException extends RuntimeException {
	/**
	 * Exception to use when Check Box is in the wrong state to perform check/uncheck.
	 * 
	 * @param sError - Error Message
	 */
	public CheckBoxWrongStateException(String sError)
	{
		super(sError);
	}
}
