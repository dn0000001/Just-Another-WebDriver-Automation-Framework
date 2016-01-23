package com.automation.ui.common.sessions;

@SuppressWarnings("serial")
public class GetSocketLockException extends RuntimeException {
	/**
	 * Exception to use when cannot get socket lock within timeout
	 * 
	 * @param sError - Error Message
	 */
	public GetSocketLockException(String sError)
	{
		super(sError);
	}

	/**
	 * Exception to use when cannot get socket lock within timeout
	 * 
	 * @param ex - exception
	 */
	public GetSocketLockException(Exception ex)
	{
		super(ex);
	}
}
