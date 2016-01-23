package com.automation.ui.common.dataStructures.config;

/**
 * This class contains relative node xpaths used to read values from the config.xml for context variables
 */
public class ConfigXML {
	//
	// Test Execution Control variables
	//
	public static final String execution_start = "Execution/Start";
	public static final String execution_stop = "Execution/Stop";

	//
	// Environment variables nodes
	//
	public static final String browser = "ENV/Browser";
	public static final String driverPath = "ENV/DriverPath";
	public static final String browserProfile = "ENV/BrowserProfile";
	public static final String singleURL = "ENV/SingleURL";
	public static final String url = "ENV/URL";
	public static final String pageTimeout = "ENV/PageTimeoutInMinutes";
	public static final String elementTimeout = "ENV/ElementTimeoutInSeconds";
	public static final String pollInterval = "ENV/PollIntervalInMilliSeconds";
	public static final String maxTimeout = "ENV/MaxTimeoutInMinutes";
	public static final String multiplierTimeout = "ENV/MultiplierTimeout";
	public static final String _AJAX_Retries = "ENV/AJAX_Retries";
	public static final String _AJAX_Stable = "ENV/AJAX_Stable";
	public static final String tempFile = "ENV/TEMP_FILE";

	//
	// Selenium Grid variables nodes
	//
	public static final String hubURL = "Grid/HubURL";
	public static final String platform = "Grid/Platform";
	public static final String version = "Grid/Version";

	//
	// Session Server variabled nodes
	//
	public static final String sessionServer = "Sessions/Server";
	public static final String sessionServerPort = "Sessions/Port";

	//
	// Database variables nodes
	//
	public static final String _DB_Server = "DB/Server";
	public static final String _DB = "DB/Database";
	public static final String _DB_User = "DB/User";
	public static final String _DB_Password = "DB/Password";
	public static final String _DB_Port = "DB/Port";
	public static final String _DB_EncodedPassword = "DB/EncodedPassword";
	public static final String _DB_Type = "DB/Type";

	//
	// Screenshots variables nodes
	//
	public static final String screenshotsEnabled = "Screenshots/Enabled";
	public static final String screenshotFolder = "Screenshots/Folder";
	public static final String screenshotPrefixName = "Screenshots/Prefix";

	//
	// External files
	//
	public static final String external_sql_folder = "External/SQL";
	public static final String external_js_folder = "External/JS";

	// Translations file
	public static final String translations = "Translations";

	//
	// Notification variables
	//
	public static final String sendEmail = "Notification/SendEmail";
	public static final String sendEmailsAfterTestSuite = "Notification/SendEmailsAfterTestSuite";
	public static final String _SMTP_Server = "Notification/SMTP_Server";
	public static final String from_EmailAddress = "Notification/From_EmailAddress";
	public static final String recipients = "Notification/Recipients";
	public static final String subject = "Notification/Subject";
	public static final String message = "Notification/Message";
	public static final String attachments = "Notification/Attachments";

	//
	// Additional Contexts
	//
	public static final String additionalContexts = "AdditionalContexts/Context";
	public static final String key = "Key";
}
