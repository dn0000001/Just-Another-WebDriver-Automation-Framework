package com.automation.ui.common.sampleProject.tests;

import java.io.File;

import org.testng.annotations.Test;

import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Screenshot;

/**
 * This class hold the unit tests for the Logs class
 */
public class LogsTest {
	/**
	 * Unit test class & give example of use with dynamic folders
	 */
	@Test
	public static void unitTest()
	{
		/*
		 * Set various options for logging before PropertyConfigurator.configure(...) is called
		 */
		// If you want to change the default properties file used by Log4j
		Logs.LOG_PROPS = "unitTestLogger.properties";

		/*
		 * If you want to change the default property that stores the FILE log location. This is used in the
		 * LOG_PROPS file.
		 */
		Logs.FOLDER_FILE_PROP = "Logs.FILE";

		/*
		 * If you want to change the default property that stores the HTML log location. This is used in the
		 * LOG_PROPS file.
		 */
		Logs.FOLDER_HTML_PROP = "Logs.HTML";

		// If you want to use dynamic folders to store the FILE log
		Logs.setFolderFile(System.getProperty("user.dir") + System.getProperty("file.separator")
				+ Logs.generateUniqueFolderName() + System.getProperty("file.separator") + "text"
				+ System.getProperty("file.separator"));
		Logs.setProperty4FolderFile(Logs.getFolderFile());

		// If you want to use dynamic folders to store the FILE log
		Logs.setFolderHTML(System.getProperty("user.dir") + System.getProperty("file.separator")
				+ Logs.generateUniqueFolderName() + System.getProperty("file.separator") + "html"
				+ System.getProperty("file.separator"));
		Logs.setProperty4FolderHTML(Logs.getFolderHTML());

		// Makes the logger ready for use. (If logger used before this, then an error occurs.)
		Logs.initializeLoggers();

		Logs.log.info("Log 1");
		Logs.logHTML.info("Log 2");

		// Only goes to log1
		Logs.log.info("Some action 1");

		// Only goes to log2
		Logs.logHTML.error("Some error 1<BR>");

		Logs.log.info("Some action 2");
		Logs.logHTML.info("Some <B>action</B> 2<BR>");

		// Lets save screenshot to the HTML folder
		String sImageRelativeLocation = "screenshots" + System.getProperty("file.separator");
		String sImageName = "test.png";

		// Need to create the folder as it does not exist
		File f = new File(Logs.getFolderHTML() + System.getProperty("file.separator")
				+ sImageRelativeLocation);
		f.mkdir();

		// Take the screenshot
		Screenshot.enableAllowScreenshots();
		Screenshot.saveScreenshot(Logs.getFolderHTML() + sImageRelativeLocation + sImageName);

		// Lets add a link in the HTML to our screenshot
		Logs.logHTML.info("<a href=\"" + sImageRelativeLocation + sImageName + "\" target=\"_blank\">"
				+ "<img src=\"" + sImageRelativeLocation + sImageName + "\" alt=\"test\" width=\"1000\" />"
				+ "</a>");
	}
}
