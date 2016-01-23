package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class WordNotFoundException extends RuntimeException {
	/**
	 * Exception to use when cannot find word (or null)
	 * 
	 * @param sError - Error Message
	 */
	public WordNotFoundException(String sError)
	{
		super(sError);
	}
}