package com.automation.ui.common.dataStructures.config;

import com.automation.ui.common.utilities.Rand;

/**
 * This class contains configuration SQL settings
 */
public class ConfigSQL {
	/**
	 * The base folder that contains all SQL files<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The ConfigRun.BaseJavaScriptFolder needs to be set with the desired location before any variable in
	 * this class is accessed because initialization of all the static variables will occur at this time.<BR>
	 */
	public static final String baseFolder = ConfigRun.BaseSQL_Folder;

	/**
	 * The placeholder that needs to be replaced in the SQL files. (It could be the placeholder prefix if
	 * multiple variables need to be replaced by different values.)
	 */
	public static String placeholder = "AUTOMATION_TEMP_REPLACE";

	/**
	 * The unique table prefix used to create unique table names. (It is recommended that this string be no
	 * longer than 7 characters such that the unique table name generated using this string is at most 30
	 * characters to be compatible with most databases.)
	 */
	public static String uniqueTablePrefix = "AUTO_";

	/**
	 * Gets an unique table name that can be used.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The returned value needs to be stored if required later.<BR>
	 * 2) Uses the uniqueTablePrefix variable, random 10 alphanumeric characters and current time (13
	 * characters) to create a unique string<BR>
	 * 3) It is recommended to limit the string to 30 characters to be compatible with most databases<BR>
	 * 
	 * @return String
	 */
	public static String getUniqueTableName()
	{
		return uniqueTablePrefix + Rand.alphanumeric(10) + Long.toString(System.currentTimeMillis());
	}

	/*
	 * Available SQL files
	 */
	public static final String _TempTableSQL = baseFolder + "data_createTable.sql";
	public static final String _SQL_File = baseFolder + "data_query.sql";
	public static final String _SQL_Sample = baseFolder + "data_sample.sql";
}
