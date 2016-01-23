package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class DuplicateWordException extends RuntimeException {
	/**
	 * Exception to use when attempt is made to insert a duplicate word
	 * 
	 * @param sError - Error Message
	 */
	public DuplicateWordException(String sError)
	{
		super(sError);
	}
}
