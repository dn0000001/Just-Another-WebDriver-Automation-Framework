package com.automation.ui.common.dataStructures.config;

/**
 * This class contains configuration E-mail results settings
 */
public class ConfigEmail {
	//
	// How to find the replacement token values in the temp file (created when run via a batch file)
	//
	public static final String reportFile = "/TOKENS/REPORT_FILE";
	public static final String date = "/TOKENS/DATE";
	public static final String logFile = "/TOKENS/LOG_FILE";

	public static boolean bSendEmail;
	public static String sSMTP_Server;
	public static String sFrom_EmailAddress;
	public static String[] sRecipients;
	public static String sSubject;
	public static String sMessage;
	public static int nSendEmailsAfterTestSuite;
	public static String[] Tokens = { "{REPORT_FILE}", "{DATE}", "{LOG_FILE}" };
	public static String[] Replacements;
	public static boolean bAttachments;
}
