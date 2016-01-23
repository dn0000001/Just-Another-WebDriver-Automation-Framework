package com.automation.ui.common.dataStructures.config;

/**
 * Class to hold constant variables that are used by any Listener Class
 */
public class Listeners {
	/**
	 * File that is written if failure occurs
	 */
	public static final String sFailFilename = "~fail.txt";

	/**
	 * Parameter name that is read from the TestNG XML file for the Failure Threshold
	 */
	public static final String sParameterName_FailureThreshold = "nFailureThreshold";

	/**
	 * Property Name to use for storing the Total Failure Count
	 */
	public static final String sPropertyName_nTotalFailureCount = "nTotalFailureCount";

	/**
	 * Property Names to use for storing the Total Test Count
	 */
	public static final String sPropertyName_nTotalTestCount = "nTotalTestCount";
}
