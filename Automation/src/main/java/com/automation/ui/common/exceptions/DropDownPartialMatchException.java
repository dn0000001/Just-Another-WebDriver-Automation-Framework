package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class DropDownPartialMatchException extends RuntimeException {
	/**
	 * Exception to use when cannot find a match using a regular expression to select a drop down option
	 * 
	 * @param sError - Error Message
	 */
	public DropDownPartialMatchException(String sError)
	{
		super(sError);
	}
}
