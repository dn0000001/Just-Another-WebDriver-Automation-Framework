package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class HTMLTableReaderRowCountException extends RuntimeException {
	/**
	 * Exception to use when HTMLTableReader cannot find any rows for the table
	 * 
	 * @param sError - Error Message
	 */
	public HTMLTableReaderRowCountException(String sError)
	{
		super(sError);
	}
}