package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class NoErrorOccurredException extends RuntimeException {
	/**
	 * Exception to use when performing negative tests but no error occurs
	 * 
	 * @param sError - Error Message
	 */
	public NoErrorOccurredException(String sError)
	{
		super(sError);
	}
}