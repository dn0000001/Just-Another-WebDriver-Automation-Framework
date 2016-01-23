package com.automation.ui.common.sessions;

/**
 * Available server protocol commands to continue the communication (Level 2 commands)
 */
public enum Level2Commands
{
	/**
	 * Command to increase pending tests or session count
	 */
	INCREASE,

	/**
	 * Command to decrease pending tests or session count
	 */
	DECREASE,

	/**
	 * Command to list all the hosts
	 */
	LIST,

	/**
	 * Command to get pending tests or session count
	 */
	COUNT,

	/**
	 * Command to reset the pending tests or session count
	 */
	RESET,

	/**
	 * Reset all sessions to 0
	 */
	ALL,

	/**
	 * Reset session on a specific node to 0
	 */
	NODE,

	/**
	 * Used to indicate an unsupported level 2 command
	 */
	UNSUPPORTED;

	/**
	 * Convert string to enumeration
	 * 
	 * @param value - String to be converted
	 * @return UNSUPPORTED if not match else corresponding enumeration value
	 */
	public static Level2Commands to(String value)
	{
		if (value == null || value.equals(""))
			return UNSUPPORTED;

		for (Level2Commands item : Level2Commands.values())
		{
			if (value.equalsIgnoreCase(item.toString()))
				return item;
		}

		return UNSUPPORTED;
	}
}
