package com.automation.ui.common.utilities;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.FindTextCriteria;
import com.automation.ui.common.dataStructures.LogErrorLevel;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.SelectionCriteria;
import com.automation.ui.common.exceptions.GenericUnexpectedException;

/**
 * This class is used for common logging functionality using log4j. This class contains variables used to
 * setup logging.
 */
public class Logs {
	/**
	 * Logs to a text file
	 */
	public static Logger log;

	/**
	 * Logs to a HTML file
	 */
	public static Logger logHTML;

	/**
	 * Location of properties file for Log4j<BR>
	 * <BR>
	 * Use: PropertyConfigurator.configure(LOG_PROPS)
	 */
	public static String LOG_PROPS = "logger.properties";

	/**
	 * System Property to be used in LOG_PROPS file for dynamic folders for a file (text)<BR>
	 * <BR>
	 * ie: log4j.appender.F.File=${Logs.FILE}results.log
	 */
	public static String FOLDER_FILE_PROP = "Logs.FILE";

	/**
	 * System Property to be used in LOG_PROPS file for dynamic folders for a HTML file<BR>
	 * <BR>
	 * ie: log4j.appender.F.File=${Logs.HTML}results.log
	 */
	public static String FOLDER_HTML_PROP = "Logs.HTML";

	/**
	 * Name of logger that writes to a file (text). Use this when instantiating the FILE logger.<BR>
	 * <BR>
	 * Note: In the Log4j properties files this is used.<BR>
	 * ie: log4j.category.FILE=INFO, F
	 */
	public static String FILE = "FILE";

	/**
	 * Name of logger that writes to a HTML file. Use this when instantiating the HTML logger.<BR>
	 * <BR>
	 * Note: In the Log4j properties files this is used.<BR>
	 * ie: log4j.category.HTML=INFO, H
	 */
	public static String HTML = "HTML";

	/**
	 * The folder that contains the FILE log. (By default if no path is specified, it will be put in the
	 * user's working directory.) Use this variable if you are placing additional files with the FILE log.<BR>
	 * <BR>
	 * If using a dynamic folder, then this needs to be set before call to PropertyConfigurator.configure(...)<BR>
	 * <BR>
	 * It is recommended that you end the FOLDER_FILE with the "file.separator" such that the LOG_PROPS file
	 * can remain platform independent.<BR>
	 * <BR>
	 * Note: Update if using dynamic folders in "logger.properties" with ${System.Property}
	 */
	private static String FOLDER_FILE = System.getProperty("user.dir");

	/**
	 * The folder that contains the HTML log. (By default if no path is specified, it will be put in the
	 * user's working directory.) Use this variable if you are placing additional files with the HTML log.<BR>
	 * <BR>
	 * If using a dynamic folder, then this needs to be set before call to PropertyConfigurator.configure(...)<BR>
	 * <BR>
	 * It is recommended that you end the FOLDER_FILE with the "file.separator" such that the LOG_PROPS file
	 * can remain platform independent.<BR>
	 * <BR>
	 * Note: Update if using dynamic folders in "logger.properties" with ${System.Property}
	 */
	private static String FOLDER_HTML = System.getProperty("user.dir");

	/**
	 * Get the folder that contains the FILE log
	 */
	public synchronized static String getFolderFile()
	{
		return FOLDER_FILE;
	}

	/**
	 * Set the folder for the FILE log
	 * 
	 * @param sLocation - Location to use for log file
	 */
	public synchronized static void setFolderFile(String sLocation)
	{
		FOLDER_FILE = sLocation;
	}

	/**
	 * Get the folder that contains the HTML log
	 */
	public synchronized static String getFolderHTML()
	{
		return FOLDER_HTML;
	}

	/**
	 * Set the folder for the HTML log
	 * 
	 * @param sLocation - Location to use for HTML log file
	 */
	public synchronized static void setFolderHTML(String sLocation)
	{
		FOLDER_HTML = sLocation;
	}

	/**
	 * Sets the property FOLDER_FILE_PROP.<BR>
	 * <BR>
	 * Note: This property only needs to be set if using dynamic folders and must be set before call to
	 * PropertyConfigurator.configure(...)
	 * 
	 * @param sValue - Location to write FILE log
	 */
	public static void setProperty4FolderFile(String sValue)
	{
		// Tries to add the property if this fails, then an update occurs
		if (!Misc.addProperty(FOLDER_FILE_PROP, sValue))
			System.setProperty(FOLDER_FILE_PROP, sValue);
	}

	/**
	 * Sets the property FOLDER_HTML_PROP.<BR>
	 * <BR>
	 * Note: This property only needs to be set if using dynamic folders and must be set before call to
	 * PropertyConfigurator.configure(...)
	 * 
	 * @param sValue - Location to write FILE log
	 */
	public static void setProperty4FolderHTML(String sValue)
	{
		// Tries to add the property if this fails, then an update occurs
		if (!Misc.addProperty(FOLDER_HTML_PROP, sValue))
			System.setProperty(FOLDER_HTML_PROP, sValue);
	}

	/**
	 * Generates a unique folder name based on the current date & time.
	 * 
	 * @return
	 */
	public static String generateUniqueFolderName()
	{
		return Conversion.convertDate(new Date(), "yyyy-MM-dd_HH-mm-ss");
	}

	/**
	 * Initializes the loggers for use.
	 */
	public static void initializeLoggers()
	{
		log = Logger.getLogger(Logs.FILE);
		logHTML = Logger.getLogger(Logs.HTML);

		// Makes the logger ready for use. (If logger used before this, then an error occurs.)
		PropertyConfigurator.configure(Logs.LOG_PROPS);
	}

	/**
	 * Logs the given object using toString method by prefixing with specified text else the text for null
	 * value is logged as a warning.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Must have already initialized loggers using method Logs.initializeLoggers()<BR>
	 * 2) The method toString must have been implemented in the class to work properly<BR>
	 * 3) If Object is null, then the specified message is logged as a warning<BR>
	 * <BR>
	 * <B>Example:</B>
	 * If test object implements toString() always as "a, b" & logObject(test, "Text Before Object ",
	 * "Object was null") then following would be the results:<BR>
	 * 1) IF test is null then Logs.log.warn("Object was null")<BR>
	 * 2) IF test is not null then Logs.log.info("Text Before Object " + "a, b")<BR>
	 * 
	 * @param objectToLog - Object to log
	 * @param sPrefixText - Text to be output before the Object
	 * @param sNullText - Text to use if Object is null
	 */
	public static void logObject(Object objectToLog, String sPrefixText, String sNullText)
	{
		if (objectToLog == null)
			Logs.log.warn(sNullText);
		else
			Logs.log.info(sPrefixText + objectToLog.toString());
	}

	/**
	 * Logs Error (adds New Line) and throws generic exception<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Must have already initialized loggers using method Logs.initializeLoggers()<BR>
	 * 2) Added New Line to error message<BR>
	 * 3) Uses Logs.log.error<BR>
	 * 
	 * @param sErrorMessage - Error message to log
	 * @throws GenericUnexpectedException
	 */
	public static void logError(String sErrorMessage)
	{
		failureTime();
		Logs.log.error(sErrorMessage + Framework.getNewLine());
		throw new GenericUnexpectedException(sErrorMessage);
	}

	/**
	 * Logs Error (from the exception and adds New Line) and re-throws Run-time exception
	 * 
	 * @param runtime - Run-time Exception to get message and re-throw
	 */
	public static void logError(RuntimeException runtime)
	{
		failureTime();
		Logs.log.error(runtime.getMessage() + Framework.getNewLine());
		throw runtime;
	}

	/**
	 * Logs Error with debug information<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The FindWebElement object contains the cached debug information to be logged<BR>
	 * 2) The list of elements cannot be null. Also, they are used as keys to get the cached data<BR>
	 * 
	 * @param find - FindWebElement that was used to search the elements
	 * @param elements - List of elements that was searched
	 * @param criteria - Criteria used to search for the element
	 * @throws GenericUnexpectedException
	 */
	public static void logError(FindWebElement find, List<WebElement> elements, SelectionCriteria criteria)
	{
		Logs.log.warn("There were the following list elements:  ");
		for (int i = 0; i < elements.size(); i++)
		{
			Logs.log.warn("" + i + ":  " + find.getValue(elements.get(i)));
		}

		Logs.logError("Could not find an element using criteria:  " + criteria);
	}

	/**
	 * Logs Error with debug information<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The FindWebElement object contains the cached debug information to be logged<BR>
	 * 2) The list of elements cannot be null. Also, they are used as keys to get the cached data<BR>
	 * 
	 * @param find - FindWebElement that was used to search the elements
	 * @param elements - List of elements that was searched
	 * @param criteria - Criteria used to search for the element
	 * @throws GenericUnexpectedException
	 */
	public static void logError(FindWebElement find, List<WebElement> elements, FindTextCriteria criteria)
	{
		Logs.log.warn("There were the following list elements:  ");
		for (int i = 0; i < elements.size(); i++)
		{
			Logs.log.warn("" + i + ":  " + find.getValue(elements.get(i)));
		}

		Logs.logError("Could not find an element using criteria:  " + criteria);
	}

	/**
	 * Logs Failure Time<BR>
	 * <BR>
	 * <B>Note: </B> Uses Logs.log.error<BR>
	 */
	public static void failureTime()
	{
		Logs.log.error("Failure Time:  " + new Date());
	}

	/**
	 * Write an error, warning or nothing to the log and re-throws the Run-time exception<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 2) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 3) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * @param level - Control error logging written
	 * @param runtime - Run-time Exception to get message and re-throw
	 */
	public static void logWarnError(LogErrorLevel level, RuntimeException runtime)
	{
		if (level == LogErrorLevel.ERROR)
			Logs.log.error(runtime.getMessage() + Framework.getNewLine());

		if (level == LogErrorLevel.WARN)
			Logs.log.warn(runtime.getMessage());

		throw runtime;
	}

	/**
	 * Logs a standard way to indicate new section of actions
	 * 
	 * @param sSectionText - Section Text
	 */
	public static void logSection(String sSectionText)
	{
		Logs.log.info(Misc.repeat("*", 40));
		Logs.log.info(sSectionText);
		Logs.log.info(Misc.repeat("*", 40));
		Logs.log.info("");
	}

	/**
	 * Logs SQL file used and SQL parameters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Logs need to have been initialized<BR>
	 * 2) If parameter names are not provided, then index used as name<BR>
	 * 
	 * @param sSQL_File - Name of SQL file used for the query
	 * @param parameters - List of parameters for the query
	 * @param names - The parameter names in same order as list
	 */
	public static void logSQL(String sSQL_File, List<Parameter> parameters, String... names)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Parameters:  ");
		for (int i = 0; i < parameters.size(); i++)
		{
			String sName;
			try
			{
				sName = names[i];
			}
			catch (Exception ex)
			{
				sName = String.valueOf(i);
			}

			String sQuote;
			if (parameters.get(i).value != null && parameters.get(i).param.equalsIgnoreCase(Database.STRING))
				sQuote = "'";
			else
				sQuote = "";

			sb.append(sName + " = " + sQuote + parameters.get(i).value + sQuote + ", ");
		}

		Logs.log.info("SQL File Used:  " + sSQL_File);

		if (parameters.size() > 0)
			Logs.log.info(Misc.removeEndsWith(sb.toString(), ", "));

		Logs.log.info("");
	}

	/**
	 * Logs Error with debug information for 2 objects that do not match<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The objects need to override the toString method for logging<BR>
	 * 
	 * @param expected - Object containing the Expected Results
	 * @param actual - Object containing the Actual Results
	 */
	public static <T> void logError(T expected, T actual)
	{
		Logs.log.warn("Expected:  " + expected);
		Logs.log.warn("Actual:    " + actual);
		Logs.logError("The actual data did not match the expected data.  See above for details");
	}

	/**
	 * Logs a standard way to indicate end of a section of actions
	 */
	public static void logSectionDivider()
	{
		logSectionDivider(1);
	}

	/**
	 * Logs a standard way to indicate end of a section of actions
	 * 
	 * @param repeat - How many times to repeat the section divider
	 */
	public static void logSectionDivider(int repeat)
	{
		Logs.log.info("");

		for (int i = 0; i < repeat; i++)
		{
			Logs.log.info(Misc.repeat("*", 40));
		}

		Logs.log.info("");
	}
}
