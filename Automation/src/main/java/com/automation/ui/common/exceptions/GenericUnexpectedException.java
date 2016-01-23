package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class GenericUnexpectedException extends RuntimeException {
	/**
	 * Exception to use when a generic exception is needed (checks that never should fail.)
	 * 
	 * @param sError - Error Message
	 */
	public GenericUnexpectedException(String sError)
	{
		super(sError);
	}
}