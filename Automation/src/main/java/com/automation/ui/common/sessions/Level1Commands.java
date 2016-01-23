package com.automation.ui.common.sessions;

/**
 * Available server protocol commands to start the communication (Level 1 commands)
 */
public enum Level1Commands
{
	/**
	 * Command to inform server that a test is going to be started
	 */
	PENDING,

	/**
	 * Command to inform server that a session count is to be increased or decreased
	 */
	SESSION,

	/**
	 * Command to inform server that the connection is just a test
	 */
	TEST,

	/**
	 * Command to inform server that a node is to be added or removed
	 */
	NODES,

	/**
	 * Used to indicate an unsupported level 1 command
	 */
	UNSUPPORTED;

	/**
	 * Convert string to enumeration
	 * 
	 * @param value - String to be converted
	 * @return UNSUPPORTED if not match else corresponding enumeration value
	 */
	public static Level1Commands to(String value)
	{
		if (value == null || value.equals(""))
			return UNSUPPORTED;

		for (Level1Commands item : Level1Commands.values())
		{
			if (value.equalsIgnoreCase(item.toString()))
				return item;
		}

		return UNSUPPORTED;
	}
}
