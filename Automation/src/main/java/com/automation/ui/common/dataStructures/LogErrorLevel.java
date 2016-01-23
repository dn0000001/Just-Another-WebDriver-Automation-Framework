package com.automation.ui.common.dataStructures;

/**
 * All supported options for error logging
 */
public enum LogErrorLevel
{
	WARN, // Write a warning to the log
	ERROR, // Write an error to the log
	NONE; // Do not write anything for the error in the log

	/**
	 * Converts boolean flag to enumeration (for compatibility with older methods that have not been converted
	 * to fully use the enumeration.)
	 * 
	 * @param bLogError - if true then log error else log warning
	 * @return if flag is true, then LogErrorLevel.ERROR else LogErrorLevel.WARN
	 */
	public static LogErrorLevel convert(boolean bLogError)
	{
		if (bLogError)
			return LogErrorLevel.ERROR;
		else
			return LogErrorLevel.WARN;
	}
}
