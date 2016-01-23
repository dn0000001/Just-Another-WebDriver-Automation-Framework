package com.automation.ui.common.dataStructures;

import org.apache.commons.exec.CommandLine;

import com.automation.ui.common.utilities.Conversion;

/**
 * This class contains the command to be executed and ID for informational purposes
 */
public class Commands {
	public String id;
	public CommandLine cmdLine;

	/**
	 * Constructor
	 * 
	 * @param id - ID for test
	 * @param cmdLine - Command to be executed
	 */
	public Commands(String id, CommandLine cmdLine)
	{
		this.id = Conversion.nonNull(id);
		this.cmdLine = cmdLine;
	}
}
