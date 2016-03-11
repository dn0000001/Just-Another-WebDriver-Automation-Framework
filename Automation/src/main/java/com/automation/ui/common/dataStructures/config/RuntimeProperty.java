package com.automation.ui.common.dataStructures.config;

/**
 * This class contains the System Properties that can be defined at run-time via the command line
 */
public class RuntimeProperty {
	public static final String url = "url";

	public static final String delay_page_timeout = "delay.page.timeout";
	public static final String delay_element_timeout = "delay.element.timeout";
	public static final String delay_poll_interval = "delay.poll.interval";
	public static final String delay_max_timeout = "delay.max.timeout";
	public static final String delay_multiplier_timeout = "delay.multiplier.timeout";
	public static final String delay_ajax_retries = "delay.ajax.retries";
	public static final String delay_ajax_stable = "delay.ajax.stable";

	public static final String database_server = "database.server";
	public static final String database_name = "database.name";
	public static final String database_user = "database.user";
	public static final String database_password = "database.password";
	public static final String database_encodedPassword = "database.encodedPassword";
	public static final String database_port = "database.port";
	public static final String database_type = "database.type";

	public static final String grid_hub = "grid.hub";
	public static final String grid_browser_platform = "grid.browser.platform";
	public static final String grid_browser_version = "grid.browser.version";
	public static final String grid_browser_applicationName = "grid.browser.applicationName";

	public static final String sessions_server = "sessions.server";
	public static final String sessions_port = "sessions.port";

	public static final String external_sql_folder = "external.sql.folder";
	public static final String external_js_folder = "external.js.folder";

	public static final String screenshots_enabled = "screenshots.enabled";
	public static final String screenshots_output = "screenshots.output";
	public static final String screenshots_prefix = "screenshots.prefix";

	public static final String env_config = "env.config";
	public static final String env_log_prop = "env.log.prop";
	public static final String env_log_folderAndOrFile = "env.log.folderFile";
	public static final String env_log_subfolder = "env.log.subfolder";
	public static final String env_translations = "env.translations";
	public static final String env_basePath = "env.basePath";
	public static final String env_driverPath = "env.driverPath";
	public static final String env_passwordLookup = "env.passwordLookup";

	public static final String browser_name = "browser.name";
	public static final String browser_profile = "browser.profile";
	public static final String browser_binary = "browser.binary";

	public static final String processes_java_command = "processes.java.command";
	public static final String processes_config = "processes.config";
	public static final String processes_max = "processes.max";
	public static final String processes_timeout = "processes.timeout";
	public static final String processes_poll_interval = "processes.poll.interval";
	public static final String processes_start_delay = "processes.start.delay";
	public static final String processes_classpath = "processes.classpath";
	public static final String processes_testNG_BaseReport = "processes.testng.output";
	public static final String processes_id_prefix = "processes.id.prefix";
	public static final String processes_id_suffix = "processes.id.suffix";

	// The system property to change Allure output folder
	public static final String processes_allure_output = "allure.results.directory";

	public static final String execution_start = "execution.start";
	public static final String execution_stop = "execution.stop";

	// Prefix for all test data files
	public static final String data_prefix = "data.";

	// Prefix for all context variables
	public static final String context_prefix = "context.";
}