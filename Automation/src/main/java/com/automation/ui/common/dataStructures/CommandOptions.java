package com.automation.ui.common.dataStructures;

import java.util.ArrayList;
import java.util.List;

import com.automation.ui.common.utilities.Conversion;

/**
 * This class is to hold the command line options for executing a test
 */
public class CommandOptions {
	public String sID;
	public boolean cmdSystemProperties;
	public boolean commonSystemProperties;
	public List<Parameter> uniqueSystemProperties;
	public String _TestNG_ReportFolder;
	public String _TestNG_XML;
	public List<Parameter> _TestNG_Options;

	/**
	 * Constructor - Initialize all variables
	 * 
	 * @param sID - ID of test
	 * @param cmdSystemProperties - true to indicate use the command line system properties passed to Launcher
	 * @param commonSystemProperties - true to indicate use the common system properties
	 * @param uniqueSystemProperties - List of System Properties for the test
	 * @param _TestNG_ReportFolder - TestNG Report Output Folder
	 * @param _TestNG_XML - TestNG XML to use
	 * @param _TestNG_Options - List of TestNG Options to use
	 */
	public CommandOptions(String sID, boolean cmdSystemProperties, boolean commonSystemProperties,
			List<Parameter> uniqueSystemProperties, String _TestNG_ReportFolder, String _TestNG_XML,
			List<Parameter> _TestNG_Options)
	{
		init(sID, cmdSystemProperties, commonSystemProperties, uniqueSystemProperties, _TestNG_ReportFolder,
				_TestNG_XML, _TestNG_Options);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param sID - ID of test
	 * @param cmdSystemProperties - true to indicate use the command line system properties passed to Launcher
	 * @param commonSystemProperties - true to indicate use the common system properties
	 * @param uniqueSystemProperties - List of System Properties for the test
	 * @param _TestNG_ReportFolder - TestNG Report Output Folder
	 * @param _TestNG_XML - TestNG XML to use
	 * @param _TestNG_Options - List of TestNG Options to use
	 */
	private void init(String sID, boolean cmdSystemProperties, boolean commonSystemProperties,
			List<Parameter> uniqueSystemProperties, String _TestNG_ReportFolder, String _TestNG_XML,
			List<Parameter> _TestNG_Options)
	{
		this.sID = Conversion.nonNull(sID);
		this.cmdSystemProperties = cmdSystemProperties;
		this.commonSystemProperties = commonSystemProperties;

		this.uniqueSystemProperties = new ArrayList<Parameter>();
		if (uniqueSystemProperties != null)
		{
			for (Parameter item : uniqueSystemProperties)
			{
				this.uniqueSystemProperties.add(item.copy());
			}
		}

		this._TestNG_ReportFolder = Conversion.nonNull(_TestNG_ReportFolder);
		this._TestNG_XML = Conversion.nonNull(_TestNG_XML);

		this._TestNG_Options = new ArrayList<Parameter>();
		if (_TestNG_Options != null)
		{
			for (Parameter item : _TestNG_Options)
			{
				this._TestNG_Options.add(item.copy());
			}
		}
	}
}
