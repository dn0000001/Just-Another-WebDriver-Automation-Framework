package com.automation.ui.common.utilities;

import java.util.Date;

/**
 * This class is for functions around Elapsed Time. The main purpose of this class is to provide a simple
 * re-usable way to determine if a timeout has occurred.
 */
public class ElapsedTime {
	private long startTime, currentTime;

	/**
	 * Constructor which set the start time as the current time
	 */
	public ElapsedTime()
	{
		this.startTime = new Date().getTime();
	}

	/**
	 * Resets the start time to the current time. This allows for the variable to be reused instead of
	 * creating a new variable once timeout has occurred.
	 */
	public void resetStartTime()
	{
		this.startTime = new Date().getTime();
	}

	/**
	 * Gets the elapsed times in milliseconds.
	 * 
	 * @return
	 */
	public long getElapsedTime()
	{
		currentTime = new Date().getTime();
		return currentTime - startTime;
	}

	/**
	 * Determines if a timeout has occurred based on the start time.<BR>
	 * <BR>
	 * Note: Uses Framework.getTimeout() to get timeout value
	 * 
	 * @return true if timeout has occurred else false
	 */
	public boolean isTimeout()
	{
		return isTimeout(Framework.getTimeout() * 1000);
	}

	/**
	 * Determines if a timeout has occurred based on the start time.<BR>
	 * <BR>
	 * Note: Use this method if you do not want to use the default timeout.
	 * 
	 * @param nTimeoutInMilliseconds - Timeout value in milliseconds
	 * @return true if timeout has occurred else false
	 */
	public boolean isTimeout(int nTimeoutInMilliseconds)
	{
		if (getElapsedTime() < nTimeoutInMilliseconds)
			return false;
		else
			return true;
	}
}