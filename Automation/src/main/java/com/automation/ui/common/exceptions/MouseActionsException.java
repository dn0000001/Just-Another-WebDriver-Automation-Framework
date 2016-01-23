package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class MouseActionsException extends RuntimeException {
	/**
	 * Exception to use when an error occurs while moving/hover/clicking with the mouse
	 * 
	 * @param sError - Error Message
	 */
	public MouseActionsException(String sError)
	{
		super(sError);
	}
}