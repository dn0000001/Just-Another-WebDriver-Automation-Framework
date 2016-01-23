package com.automation.ui.common.dataStructures.config;

/**
 * This class contains configuration variables for the test run
 */
public class ConfigRun {
	/**
	 * Default XML if none specified
	 */
	public static final String CONFIG_FILE = "config.xml";

	/**
	 * Loaded XML file (initially set to default XML)
	 */
	public static String sUseConfigFile = CONFIG_FILE;

	/**
	 * Flag to indicate whether to make all contexts available or only contexts that are specified by system
	 * properties or the configuration XML
	 */
	public static final String _AllContexts = "true";

	/**
	 * How to find the data in the XML file
	 */
	public static final String CONFIG_ROOT_XPATH = "/config/";

	/**
	 * How to find each Data Driven Values file
	 */
	public static final String DDV_ROOT_XPATH = "/config/DataDrivenValues";

	/**
	 * Node name under the DDV_ROOT_XPATH to find the base path for all the files
	 */
	public static final String DDV_BASEPATH_XPATH = "BasePath";

	/**
	 * Logging variables
	 */
	public static final String LOG_FILE = "results.log";

	/**
	 * Base folder location to find SQL files
	 */
	public static final String SQL_FOLDER = "src/main/resources/SQL/";

	/**
	 * Base folder location to find JavaScript files
	 */
	public static final String JavaScript_FOLDER = "src/main/resources/JS/";

	/**
	 * File with all translations
	 */
	public static String sTranslationsFile;

	/**
	 * Base Path
	 */
	public static String env_basePath = "";

	/**
	 * DataDrivenValues node that contains file for key value pairs lookup
	 */
	public static String NODE_LOOKUP = "PasswordLookup";

	/**
	 * xpath used to lookup key value pairs
	 */
	public static String NODE_LOOKUP_XPATH = "/Lookup/Password/";

	/**
	 * The base folder that contains all JavaScript files
	 */
	public static String BaseJavaScriptFolder = JavaScript_FOLDER;

	/**
	 * The base folder that contains all SQL files
	 */
	public static String BaseSQL_Folder = SQL_FOLDER;

	/**
	 * The logger.properties file to be used for unit testing (maven)
	 */
	public static final String UnitTestLoggerPropertiesFile = "src/test/resources/logger.properties";

	/**
	 * The default java command used to launch multiple tests
	 */
	public static final String DefaultJavaCommand = "java";
}
