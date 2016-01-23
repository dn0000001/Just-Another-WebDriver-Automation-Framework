package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class JavaScriptException extends RuntimeException {
	/**
	 * Exception to use when the JavascriptExecutor throws an exception executing user JavaScript
	 * 
	 * @param sError - Error Message
	 */
	public JavaScriptException(String sError)
	{
		super(sError);
	}
}