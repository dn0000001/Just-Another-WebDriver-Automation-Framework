package com.automation.ui.common.dataStructures;

/**
 * Class used for unit testing FlakinessChecks.isReflection
 */
public class AttemptTracker {
	private int attempts;
	private int _Max_Attempts;

	public AttemptTracker()
	{
		attempts = 0;
		_Max_Attempts = 1;
	}

	public void resetAttempts()
	{
		attempts = 0;
	}

	public int getAttempts()
	{
		return attempts;
	}

	public void setMaxAttempts(int max)
	{
		_Max_Attempts = max;
	}

	/**
	 * Increases the attempts count and checks if greater than max attempts<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is used to test FlakinessChecks.isReflection with no parameters<BR>
	 * 
	 * @return true if the attempts is greater than the max attempts else false
	 */
	public boolean isMaxAttempts()
	{
		return isMaxAttempts(_Max_Attempts);
	}

	/**
	 * Increases the attempts count and checks if greater than specified value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is used to test FlakinessChecks.isReflection with parameters<BR>
	 * 
	 * @param max - Max attempts value
	 * @return true if the attempts is greater than max else false
	 */
	public boolean isMaxAttempts(int max)
	{
		attempts++;
		if (attempts > max)
			return true;
		else
			return false;
	}
}
