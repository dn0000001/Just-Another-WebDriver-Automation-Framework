package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class CheckBoxNotEnabled extends RuntimeException {
	/**
	 * Exception to use when Check Box element is not enabled
	 * 
	 * @param sError - Error Message
	 */
	public CheckBoxNotEnabled(String sError)
	{
		super(sError);
	}
}
