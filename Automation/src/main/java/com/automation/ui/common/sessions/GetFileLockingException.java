package com.automation.ui.common.sessions;

@SuppressWarnings("serial")
public class GetFileLockingException extends RuntimeException {
	/**
	 * Exception to use when cannot get file lock
	 * 
	 * @param sError - Error Message
	 */
	public GetFileLockingException(String sError)
	{
		super(sError);
	}

	/**
	 * Exception to use when cannot get file lock
	 * 
	 * @param ex - exception
	 */
	public GetFileLockingException(Exception ex)
	{
		super(ex);
	}
}
