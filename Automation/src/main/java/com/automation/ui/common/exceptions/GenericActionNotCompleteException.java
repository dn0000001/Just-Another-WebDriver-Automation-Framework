package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class GenericActionNotCompleteException extends RuntimeException {
	/**
	 * Exception to use when waiting for an action to complete but it does not.<BR>
	 * For example, Clicking button is successful but an other button is suppose to appear before returning
	 * control. However, it does not appear.
	 * 
	 * @param sError - Error Message
	 */
	public GenericActionNotCompleteException(String sError)
	{
		super(sError);
	}
}