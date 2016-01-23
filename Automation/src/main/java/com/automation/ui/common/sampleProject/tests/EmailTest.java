package com.automation.ui.common.sampleProject.tests;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.SendEmailDetails;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.utilities.Email;
import com.automation.ui.common.utilities.Logs;

/**
 * This class hold the unit tests for the Email class
 */
public class EmailTest {
	/**
	 * Method called by testNG to test sendEmail
	 */
	@Test
	public static void unitTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Email results = new Email();
		// No Attachment
		SendEmailDetails details = new SendEmailDetails("test.com", "emailTest@test.com",
				new String[] { "test@test.com" }, "Unit Test sendEmail 1/3",
				"E-mail is working without an attachment", new String[] {});
		results.sendEmail(details);

		// 1 Attachment
		SendEmailDetails details2 = new SendEmailDetails("test.com", "emailTest@test.com",
				new String[] { "test@test.com" }, "Unit Test sendEmail 2/3",
				"E-mail is working with an attachment", new String[] { "csvTest.csv" });
		results.sendEmail(details2);

		// 2 Attachments
		SendEmailDetails details3 = new SendEmailDetails("test.com", "emailTest@test.com",
				new String[] { "test@test.com" }, "Unit Test sendEmail 3/3",
				"E-mail is working with an attachment", new String[] { "csvTest.csv", "xmlTest.xml" });
		results.sendEmail(details3);
	}
}
