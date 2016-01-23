package com.automation.ui.common.sampleProject.exceptions;

@SuppressWarnings("serial")
public class ChangeLanguageException extends RuntimeException {
	/**
	 * Exception to use when changing languages fails
	 * 
	 * @param sError - Error Message
	 */
	public ChangeLanguageException(String sError)
	{
		super(sError);
	}
}