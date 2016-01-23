package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class DisabledActionException extends RuntimeException {
	/**
	 * Exception to use when an action is disabled. (Mainly for use with classes in the components package.)
	 * 
	 * @param sError - Error Message
	 */
	public DisabledActionException(String sError)
	{
		super(sError);
	}
}