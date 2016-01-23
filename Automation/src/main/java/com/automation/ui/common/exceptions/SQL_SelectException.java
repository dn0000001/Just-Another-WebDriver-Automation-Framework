package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class SQL_SelectException extends RuntimeException {
	/**
	 * Exception to use when a generic SQL exception occurs with a Select statement.
	 * 
	 * @param sError - Error Message
	 */
	public SQL_SelectException(String sError)
	{
		super(sError);
	}
}