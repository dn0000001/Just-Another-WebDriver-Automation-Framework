package com.automation.ui.common.utilities;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;

import com.automation.ui.common.dataStructures.CommandOptions;
import com.automation.ui.common.dataStructures.Commands;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.dataStructures.config.RuntimeProperty;

/**
 * This class launches multiple tests in different processes
 */
public class Launcher {
	private static String sConfig, sTestNG_BaseReportFolder, sID_Prefix, sID_Suffix;
	private static int nMaxProcesses, nMaxProcessTimeout, nProcessCompletePollInterval, nProcessStartDelay;

	/**
	 * Main program to launch the test cases
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ExecuteException
	 */
	public static void main(String[] args) throws ExecuteException, IOException
	{
		setProcessVariables();

		List<Commands> commands = new ArrayList<Commands>();
		// commands = getNotepadCommands();
		commands = getCommands();

		boolean bSleep = true;
		int nTotalProcessCount, nRunningProcesses, nCompletedProcesses;
		List<Executor> executors = new ArrayList<Executor>();
		List<DefaultExecuteResultHandler> results = new ArrayList<DefaultExecuteResultHandler>();
		List<String> ids = new ArrayList<String>();
		List<String> testsPassed = new ArrayList<String>();
		List<String> testsFailed = new ArrayList<String>();
		int nExitCode = 0;

		nTotalProcessCount = 0;
		nRunningProcesses = 0;
		nCompletedProcesses = 0;

		// Loop until all commands have been executed
		while (nCompletedProcesses < commands.size())
		{
			// Start the processes until max processes reached or the total number process count greater than
			// or equal to the commands to be executed
			while (nRunningProcesses < nMaxProcesses && nTotalProcessCount < commands.size())
			{
				// We will be adding a new item to the list as such it's index will be the current size of
				// the list.
				int nIndex = executors.size();
				executors.add(new DefaultExecutor());
				results.add(new DefaultExecuteResultHandler());
				ids.add(commands.get(nTotalProcessCount).id);

				// Launch process
				executors.get(nIndex).setExitValue(0);
				executors.get(nIndex).setWatchdog(new ExecuteWatchdog(nMaxProcessTimeout));
				executors.get(nIndex).setProcessDestroyer(new ShutdownHookProcessDestroyer());
				executors.get(nIndex).execute(commands.get(nTotalProcessCount).cmdLine, results.get(nIndex));

				System.out.println();
				System.out.println("ID:  " + commands.get(nTotalProcessCount).id);
				System.out.println("Executing Command:  "
						+ commands.get(nTotalProcessCount).cmdLine.toString());
				System.out.println();

				// This pause seems to be necessary for the watch dog to start or process to start. Without
				// this delay the check if the "process is running" will fail, even though this is not the
				// case.
				//
				// Note: This was the case when testing with notepad
				Framework.sleep(nProcessStartDelay);

				// Update running process count and total launched process count
				nRunningProcesses++;
				nTotalProcessCount++;
			}

			// Reset the sleep flag to true
			bSleep = true;

			// Check if any of the processes has completed. If any processes are complete, then do not sleep
			// but start another process else sleep to allow time for process to complete
			//
			// Note: As the list size may change, we will check the highest index first and make are way to
			// the lowest index
			for (int i = nRunningProcesses - 1; i >= 0; i--)
			{
				// If process is completed, then decrease number of processes running, increase processes
				// completed, set sleep flag to false and remove process from list
				if (i < executors.size() && !executors.get(i).getWatchdog().isWatching())
				{
					nRunningProcesses--;
					nCompletedProcesses++;
					bSleep = false;

					// Collect the exit code and store if not equal to 0 (indicates error)
					int nProcessExitCode = results.get(i).getExitValue();
					if (nProcessExitCode != 0)
					{
						nExitCode = nProcessExitCode;
						testsFailed.add(ids.get(i));
					}
					else
					{
						testsPassed.add(ids.get(i));
					}

					ids.remove(i);
					results.remove(i);
					executors.remove(i);
				}
			}

			// Should we sleep or can more processes be started
			if (bSleep)
				Framework.sleep(nProcessCompletePollInterval);
		}

		// Output the results
		System.out.println();
		if (testsPassed.size() > 0)
		{
			System.out.println("There were " + testsPassed.size() + " tests that passed (exit code = 0)");
			System.out.println("Pass Test IDs:  " + Conversion.toString(testsPassed, ", "));
		}
		else
		{
			System.out.println("There were no tests that passed (exit code = 0)");
		}

		System.out.println();
		if (testsFailed.size() > 0)
		{
			System.out.println("There were " + testsFailed.size() + " tests that failed (exit code != 0)");
			System.out.println("Failed Test IDs:  " + Conversion.toString(testsFailed, ", "));
		}
		else
		{
			System.out.println("There were no tests that failed (exit code != 0)");
		}

		// Return the exit code
		System.exit(nExitCode);
	}

	/**
	 * Sets the process variable from configuration file
	 */
	private static void setProcessVariables()
	{
		sConfig = Misc.getProperty(RuntimeProperty.processes_config, "executeTests.xml");

		VTD_XML xml;
		try
		{
			xml = new VTD_XML(sConfig);

			nMaxProcesses = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.processes_max,
							xml.getNodeValue("/ExecuteTests/Processes/MaxProcesses", "1")), 1);
			nMaxProcessTimeout = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.processes_timeout,
							xml.getNodeValue("/ExecuteTests/Processes/MaxProcessTimeout", "12600000")),
					12600000);
			nProcessCompletePollInterval = Conversion
					.parseInt(
							Misc.getProperty(RuntimeProperty.processes_poll_interval, xml.getNodeValue(
									"/ExecuteTests/Processes/ProcessCompletePollInterval", "30000")), 30000);
			nProcessStartDelay = Conversion.parseInt(
					Misc.getProperty(RuntimeProperty.processes_start_delay,
							xml.getNodeValue("/ExecuteTests/Processes/ProcessStartDelay", "100")), 100);
			sTestNG_BaseReportFolder = Misc.getProperty(RuntimeProperty.processes_testNG_BaseReport,
					xml.getNodeValue("/ExecuteTests/Processes/TestNG_BaseReportFolder", ""));
			sID_Prefix = Misc.getProperty(RuntimeProperty.processes_id_prefix,
					xml.getNodeValue("/ExecuteTests/Processes/ID_Prefix", ""));
			sID_Suffix = Misc.getProperty(RuntimeProperty.processes_id_suffix,
					xml.getNodeValue("/ExecuteTests/Processes/ID_Suffix", ""));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(3);
		}
	}

	/**
	 * Returns 10 notepad commands to be run for testing purposes
	 * 
	 * @return List&lt;Commands&gt;
	 */
	protected static List<Commands> getNotepadCommands()
	{
		List<Commands> commands = new ArrayList<Commands>();

		commands.add(new Commands("T1", new CommandLine("notepad.exe")));
		commands.add(new Commands("T2", new CommandLine("notepad.exe")));
		commands.add(new Commands("T3", new CommandLine("notepad.exe")));
		commands.add(new Commands("T4", new CommandLine("notepad.exe")));
		commands.add(new Commands("T5", new CommandLine("notepad.exe")));
		commands.add(new Commands("T6", new CommandLine("notepad.exe")));
		commands.add(new Commands("T7", new CommandLine("notepad.exe")));
		commands.add(new Commands("T8", new CommandLine("notepad.exe")));
		commands.add(new Commands("T9", new CommandLine("notepad.exe")));
		commands.add(new Commands("T10", new CommandLine("notepad.exe")));

		return commands;
	}

	/**
	 * Gets a Parameter object from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @return Parameter
	 */
	private static Parameter getSystemProperty(VTD_XML xml, String sXpath_Base)
	{
		Parameter p1 = new Parameter("Name", "");
		Parameter p2 = new Parameter("Value", "");

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		String sName = rv.get(rv.indexOf(p1)).value;
		String sValue = rv.get(rv.indexOf(p2)).value;

		return new Parameter(sName, sValue);
	}

	/**
	 * Gets the system properties from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @return List&lt;Parameter&gt;
	 */
	private static List<Parameter> getSystemProperties(VTD_XML xml, String sXpath_Base)
	{
		List<Parameter> commonSystemProperties = new ArrayList<Parameter>();

		String sXpath = sXpath_Base + "SystemProperty";
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			Parameter commonSystemProperty = getSystemProperty(xml, sXpath + "[" + (i + 1) + "]");
			commonSystemProperties.add(commonSystemProperty);
		}

		return commonSystemProperties;
	}

	/**
	 * Gets the TestNG options from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @return List&lt;Parameter&gt;
	 */
	private static List<Parameter> getTestNG_Options(VTD_XML xml, String sXpath_Base)
	{
		List<Parameter> _TestNG_Options = new ArrayList<Parameter>();

		String sXpath = sXpath_Base + "Option";
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			Parameter option = getSystemProperty(xml, sXpath + "[" + (i + 1) + "]");
			_TestNG_Options.add(option);
		}

		return _TestNG_Options;
	}

	/**
	 * Gets a specific Test from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @return CommandOptions
	 */
	private static CommandOptions getSpecificTest(VTD_XML xml, String sXpath_Base)
	{
		String sID = xml.getNodeValue(sXpath_Base + "ID", "");
		boolean cmdSystemProperties = xml.getNodeValue(sXpath_Base + "UseCommandLineSystemProperties", true);
		boolean commonSystemProperties = xml.getNodeValue(sXpath_Base + "UseCommonSystemProperties", true);
		List<Parameter> uniqueSystemProperties = getSystemProperties(xml, sXpath_Base);
		String _TestNG_ReportFolder = xml.getNodeValue(sXpath_Base + "TestNG_ReportFolder", "");
		String _TestNG_XML = xml.getNodeValue(sXpath_Base + "TestNG_XML", "");
		List<Parameter> _TestNG_Options = getTestNG_Options(xml, sXpath_Base + "TestNG_Options/");

		return new CommandOptions(sID, cmdSystemProperties, commonSystemProperties, uniqueSystemProperties,
				_TestNG_ReportFolder, _TestNG_XML, _TestNG_Options);
	}

	/**
	 * Gets all the tests from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @return List&lt;CommandOptions&gt;
	 */
	private static List<CommandOptions> getTestCommands(VTD_XML xml, String sXpath_Base)
	{
		List<CommandOptions> allTests = new ArrayList<CommandOptions>();

		String sXpath = sXpath_Base + "Test";
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			CommandOptions specificTest = getSpecificTest(xml, sXpath + "[" + (i + 1) + "]/");
			allTests.add(specificTest);
		}

		return allTests;
	}

	/**
	 * Returns all test commands
	 * 
	 * @return List&lt;Commands&gt;
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<Commands> getCommands()
	{
		List<Commands> commands = new ArrayList<Commands>();

		List<Parameter> cmdSystemProperties = new ArrayList<Parameter>();
		List<Parameter> commonSystemProperties = new ArrayList<Parameter>();
		String sClassPath = "";
		String sJavaCommand = "";
		List<CommandOptions> allTests = new ArrayList<CommandOptions>();

		VTD_XML xml;
		try
		{
			xml = new VTD_XML(sConfig);

			// Get List of all supported system properties that are currently available
			cmdSystemProperties = getCommandLineSystemProperties();

			// Read all the common system properties
			commonSystemProperties = getSystemProperties(xml, "/ExecuteTests/SystemProperties/");

			// Get the command to launch java
			sJavaCommand = Misc.getProperty(RuntimeProperty.processes_java_command,
					xml.getNodeValue("/ExecuteTests/JavaCommand", ConfigRun.DefaultJavaCommand));

			// Read classpath
			sClassPath = Misc.getProperty(RuntimeProperty.processes_classpath,
					xml.getNodeValue("/ExecuteTests/ClassPath", ""));

			// Read the Tests
			allTests = getTestCommands(xml, "/ExecuteTests/Tests/");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(3);
		}

		// Construct command for each test
		for (CommandOptions test : allTests)
		{
			int nIndex = 0;

			CommandLine cmdLine = new CommandLine(sJavaCommand);
			cmdLine.addArgument("-classpath");
			cmdLine.addArgument("${value" + nIndex + "}");
			HashMap map = new HashMap();
			map.put("value" + nIndex, sClassPath);
			nIndex++;

			// Add all supported (and non-empty) system properties that are currently available
			// Note: These system properties would have been used when invoking Launcher from the command line
			if (test.cmdSystemProperties)
			{
				for (Parameter cmdProp : cmdSystemProperties)
				{
					cmdLine.addArgument("-D" + cmdProp.param + "=${value" + nIndex + "}");
					map.put("value" + nIndex, cmdProp.value);
					cmdLine.setSubstitutionMap(map);
					nIndex++;
				}
			}

			// Add all common system properties
			if (test.commonSystemProperties)
			{
				for (Parameter csp : commonSystemProperties)
				{
					cmdLine.addArgument("-D" + csp.param + "=${value" + nIndex + "}");
					map.put("value" + nIndex, csp.value);
					cmdLine.setSubstitutionMap(map);
					nIndex++;
				}
			}

			// Add the system properties specific to the test
			for (Parameter usp : test.uniqueSystemProperties)
			{
				cmdLine.addArgument("-D" + usp.param + "=${value" + nIndex + "}");
				map.put("value" + nIndex, usp.value);
				cmdLine.setSubstitutionMap(map);
				nIndex++;
			}

			cmdLine.addArgument("org.testng.TestNG");

			// TestNG report output folder
			if (!test._TestNG_ReportFolder.equals(""))
			{
				cmdLine.addArgument("-d");
				cmdLine.addArgument("${value" + nIndex + "}");
				map.put("value" + nIndex, sTestNG_BaseReportFolder + test._TestNG_ReportFolder);
				cmdLine.setSubstitutionMap(map);
				nIndex++;
			}

			// TestNG options
			for (Parameter testNG : test._TestNG_Options)
			{
				cmdLine.addArgument(testNG.param);
				cmdLine.addArgument("${value" + nIndex + "}");
				map.put("value" + nIndex, testNG.value);
				cmdLine.setSubstitutionMap(map);
				nIndex++;
			}

			// Is it necessary to add the TestNG XML file?
			if (!test._TestNG_XML.equals(""))
			{
				cmdLine.addArgument("${value" + nIndex + "}");
				map.put("value" + nIndex, test._TestNG_XML);
				cmdLine.setSubstitutionMap(map);
				nIndex++;
			}

			// Needs to be done last
			cmdLine.setSubstitutionMap(map);
			commands.add(new Commands(sID_Prefix + test.sID + sID_Suffix, cmdLine));
		}

		return commands;
	}

	/**
	 * Gets the supported system properties that are current available (as such they must have been passed on
	 * the command line when invoking Launcher)
	 * 
	 * @return List&lt;Parameter&gt;
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static List<Parameter> getCommandLineSystemProperties() throws IllegalArgumentException,
			IllegalAccessException
	{
		List<Parameter> cmdSystemProperties = new ArrayList<Parameter>();

		// Get all the public variables in the RuntimeProperty class
		Field[] fields = RuntimeProperty.class.getFields();
		for (Field f : fields)
		{
			// Get the System Property
			// Note: Assumes that the variable is a Strings and static (which they always should be.)
			String sSystemProperty = (String) f.get(null);

			// Add the System Property to the list and value if it exists with a non-empty value
			String sValue = Misc.getProperty(sSystemProperty, "");
			if (!sValue.equals(""))
				cmdSystemProperties.add(new Parameter(sSystemProperty, sValue));
		}

		// Search for the dynamic keys of the test case data files & context variables
		Properties properties = System.getProperties();
		Set<Object> sysKeys = properties.keySet();
		for (Object key : sysKeys)
		{
			if (((String) key).startsWith(RuntimeProperty.data_prefix)
					|| ((String) key).startsWith(RuntimeProperty.context_prefix))
			{
				// Add the System Property to the list and value if it exists with a non-empty value
				String sValue = Misc.getProperty((String) key, "");
				if (!sValue.equals(""))
					cmdSystemProperties.add(new Parameter((String) key, sValue));
			}
		}

		return cmdSystemProperties;
	}
}
