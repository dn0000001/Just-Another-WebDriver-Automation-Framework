package com.automation.ui.common.utilities;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * This class is for taking screenshots.<BR>
 * <BR>
 * <B>Note: The screenshots will be blank if running in a minimized Terminal Services window. However, other
 * windows can overlay this window.</B>
 */
public class Screenshot {
	private static String NewLine = System.getProperty("line.separator");
	private static String PathSeparator = System.getProperty("file.separator");
	private static int nScreenshotCounter = 1;
	private static String sScreenshotFolder = ".";
	private static String sScreenshotPrefixName = "";
	private static boolean bAllowScreenshots = false;

	/**
	 * Get the Path Separator
	 * 
	 * @return PathSeparator
	 */
	public static String getPathSeparator()
	{
		return PathSeparator;
	}

	/**
	 * Set the Path Separator
	 * 
	 * @param sPathSeparator - Path Separator to be used
	 */
	public static void setPathSepartor(String sPathSeparator)
	{
		Screenshot.PathSeparator = sPathSeparator;
	}

	/**
	 * Get the New Line
	 * 
	 * @return NewLine
	 */
	public static String getNewLine()
	{
		return NewLine;
	}

	/**
	 * Set the New Line
	 * 
	 * @param sNewLine - New Line to be used
	 */
	public static void setNewLine(String sNewLine)
	{
		Screenshot.NewLine = sNewLine;
	}

	/**
	 * Get the Screenshot Counter
	 * 
	 * @return nScreenshotCounter
	 */
	public synchronized static int getScreenshotCounter()
	{
		return nScreenshotCounter;
	}

	/**
	 * Allows you to change nScreenshotCounter to any value.<BR>
	 * <BR>
	 * Note: This is not recommended and it is unnecessary.
	 * 
	 * @param nValue - Value to set Screenshot Counter to
	 */
	public synchronized static void setScreenshotCounter(int nValue)
	{
		nScreenshotCounter = nValue;
	}

	/**
	 * Resets the nScreenshotCounter back to initial value of 1
	 */
	public synchronized static void resetScreenshotCounter()
	{
		nScreenshotCounter = 1;
	}

	/**
	 * Increments the Screenshot Counter by 1
	 */
	public synchronized static void incrementScreenshotCounter()
	{
		nScreenshotCounter++;
	}

	/**
	 * Set flag to allow screenshots to be saved
	 */
	public static void enableAllowScreenshots()
	{
		Screenshot.bAllowScreenshots = true;
	}

	/**
	 * Set flag to disable screenshots from being saved
	 */
	public static void disableAllowScreenshots()
	{
		Screenshot.bAllowScreenshots = false;
	}

	/**
	 * Set Screenshot Settings
	 * 
	 * @param sScreenshotFolder - Screenshot Folder to be used
	 * @param sScreenshotPrefixName - Screenshot Prefix Name to be used
	 */
	public static void setScreenshotSettings(String sScreenshotFolder, String sScreenshotPrefixName)
	{
		Screenshot.sScreenshotFolder = sScreenshotFolder;
		Screenshot.sScreenshotPrefixName = sScreenshotPrefixName;
	}

	/**
	 * Saves unique screenshot to following location with name: sScreenshotFolder + PathSeparator +
	 * sScreenshotPrefixName + nScreenshotCounter + ".png"
	 * 
	 * Example: c:\\temp\\screenshot001.png
	 * 
	 * @return true if successful else false
	 */
	public static boolean saveScreenshot()
	{
		String sFilename = sScreenshotFolder + PathSeparator + sScreenshotPrefixName
				+ getCounter(getScreenshotCounter(), 3) + ".png";
		if (saveScreenshot(sFilename))
		{
			incrementScreenshotCounter();
			return true;
		}

		return false;
	}

	/**
	 * Saves unique screenshot to following location with name: sScreenshotFolder + PathSeparator +
	 * sScreenshotPrefixName + nScreenshotCounter + "_" + sSuffix + ".png"
	 * 
	 * Example: c:\\temp\\screenshot001_login.png
	 * 
	 * @param sSuffix
	 * @return true if successful else false
	 */
	public static boolean saveScreenshotAddSuffix(String sSuffix)
	{
		String sFilename = sScreenshotFolder + PathSeparator + sScreenshotPrefixName
				+ getCounter(getScreenshotCounter(), 3) + "_" + sSuffix + ".png";
		if (saveScreenshot(sFilename))
		{
			incrementScreenshotCounter();
			return true;
		}

		return false;
	}

	/**
	 * Saves screenshot. (User needs to ensure file is unique and it is has a PNG extension)
	 * 
	 * @param sFilename
	 * @return true if successful else false
	 */
	public static boolean saveScreenshot(String sFilename)
	{
		// Allow user to take screenshots in program that can be
		// enabled/disabled without modifying the code in many places instead
		// just using a configurable switch
		if (!Screenshot.bAllowScreenshots)
			return false;

		try
		{
			Robot robot = new Robot();
			BufferedImage capture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit()
					.getScreenSize()));
			ImageIO.write(capture, "png", new File(sFilename));
			Logs.log.info("Saved screenshot to file:  " + sFilename);
			return true;
		}
		catch (Exception ex)
		{
			Logs.log.error(ex);
			return false;
		}
	}

	/**
	 * Creates a string with leading zeros (if necessary) to make specified total length.
	 * 
	 * @param nCounter - counter to convert to a string
	 * @param nTotalLength - size of string
	 * @return
	 */
	public static String getCounter(int nCounter, int nTotalLength)
	{
		String sConvert = String.valueOf(nCounter);
		String sLeadingZeros = "";
		int nPadding = nTotalLength - sConvert.length();
		for (int i = 0; i < nPadding; i++)
		{
			sLeadingZeros += "0";
		}

		return sLeadingZeros + sConvert;
	}

	/**
	 * This function will check & create the screenshot folder(s) as necessary.<BR>
	 * <BR>
	 * Note: If you do not know or cannot guarantee that the screenshot folder will exist, then use this
	 * function before creating any screenshots.
	 * 
	 * @return true if the screenshot folder exists after execution else false
	 */
	public static boolean checkCreateScreenshotFolder()
	{
		return Misc.bCheckCreateFolder(sScreenshotFolder);
	}

	/**
	 * Saves screenshot. (User needs to ensure file is unique and it is has a PNG extension)<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Must use this method if using Grid<BR>
	 * 2) If driver cannot be cast to TakesScreenshot, no screenshot will be taken<BR>
	 * 3) It is possible for the screenshot to be blank<BR>
	 * 4) If the page does not have a body tag, then an exception will occur when attempting to take a
	 * screenshot<BR>
	 * 
	 * @param driver - WebDriver that has the capability of TAKES_SCREENSHOT
	 * @param sFilename - File name needs to be unique and have PNG extension
	 * @param screenshotFlag - true to allow the screenshot to be taken, false to skip taking the screenshot
	 * @return true if successful else false
	 */
	private static boolean saveScreenshot(WebDriver driver, String sFilename, boolean screenshotFlag)
	{
		/*
		 * Allow user to take screenshots in program that can be enabled/disabled without modifying the code
		 * in many places instead just using a configurable switch
		 */
		if (!screenshotFlag)
			return false;

		try
		{
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(sFilename));
			Logs.log.info("Saved screenshot to file:  " + sFilename);
			return true;
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			Logs.log.error(ex);
			return false;
		}
	}

	/**
	 * Saves screenshot. (User needs to ensure file is unique and it is has a PNG extension)<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Must use this method if using Grid<BR>
	 * 2) If driver cannot be cast to TakesScreenshot, no screenshot will be taken<BR>
	 * 3) It is possible for the screenshot to be blank<BR>
	 * 4) If the page does not have a body tag, then an exception will occur when attempting to take a
	 * screenshot<BR>
	 * 
	 * @param driver - WebDriver that has the capability of TAKES_SCREENSHOT
	 * @param sFilename - File name needs to be unique and have PNG extension
	 * @return true if successful else false
	 */
	public static boolean saveScreenshot(WebDriver driver, String sFilename)
	{
		/*
		 * Allow user to take screenshots in program that can be enabled/disabled without modifying the code
		 * in many places instead just using a configurable switch
		 */
		return saveScreenshot(driver, sFilename, Screenshot.bAllowScreenshots);
	}

	/**
	 * Saves unique screenshot to following location with name: sScreenshotFolder + PathSeparator +
	 * sScreenshotPrefixName + nScreenshotCounter + ".png"<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Must use this method if using Grid<BR>
	 * 2) If driver cannot be cast to TakesScreenshot, no screenshot will be taken<BR>
	 * 3) It is possible for the screenshot to be blank<BR>
	 * 4) If the page does not have a body tag, then an exception will occur when attempting to take a
	 * screenshot<BR>
	 * <BR>
	 * <B>Example:</B> <BR>
	 * saveScreenshot(driver) => c:\\temp\\screenshot001.png<BR>
	 * 
	 * @param driver - WebDriver that has the capability of TAKES_SCREENSHOT
	 * @return true if successful else false
	 */
	public static boolean saveScreenshot(WebDriver driver)
	{
		String sFilename = sScreenshotFolder + PathSeparator + sScreenshotPrefixName
				+ getCounter(getScreenshotCounter(), 3) + ".png";
		if (saveScreenshot(driver, sFilename))
		{
			incrementScreenshotCounter();
			return true;
		}

		return false;
	}

	/**
	 * Get the Screenshot Folder
	 * 
	 * @return sScreenshotFolder
	 */
	public static String getScreenshotFolder()
	{
		return sScreenshotFolder;
	}

	/**
	 * Get the Screenshot Prefix Name
	 * 
	 * @return sScreenshotPrefixName
	 */
	public static String getScreenshotPrefixName()
	{
		return sScreenshotPrefixName;
	}

	/**
	 * Saves screenshot. (User needs to ensure file is unique and it is has a PNG extension)<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Must use this method if using Grid<BR>
	 * 2) If driver cannot be cast to TakesScreenshot, no screenshot will be taken<BR>
	 * 3) It is possible for the screenshot to be blank<BR>
	 * 4) If the page does not have a body tag, then an exception will occur when attempting to take a
	 * screenshot<BR>
	 * 
	 * @param driver - WebDriver that has the capability of TAKES_SCREENSHOT
	 * @param sFilename - File name needs to be unique and have PNG extension
	 * @return true if successful else false
	 */
	public static boolean saveScreenshotAlways(WebDriver driver, String sFilename)
	{
		return saveScreenshot(driver, sFilename, true);
	}

	/**
	 * Saves unique screenshot to following location with name: sScreenshotFolder + PathSeparator +
	 * sScreenshotPrefixName + nScreenshotCounter + ".png"<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Must use this method if using Grid<BR>
	 * 2) If driver cannot be cast to TakesScreenshot, no screenshot will be taken<BR>
	 * 3) It is possible for the screenshot to be blank<BR>
	 * 4) If the page does not have a body tag, then an exception will occur when attempting to take a
	 * screenshot<BR>
	 * 5) If screenshots are disabled, then the sScreenshotFolder may not have been set.<BR>
	 * <BR>
	 * <B>Example:</B> <BR>
	 * saveScreenshot(driver) => c:\\temp\\screenshot001.png<BR>
	 * 
	 * @param driver - WebDriver that has the capability of TAKES_SCREENSHOT
	 * @param useLogFolder - true to use Logs.getFolderFile() to store screenshot
	 * @return true if successful else false
	 */
	public static boolean saveScreenshotAlways(WebDriver driver, boolean useLogFolder)
	{
		String sFolder = (useLogFolder) ? Logs.getFolderFile() : sScreenshotFolder;
		sFolder = Misc.removeEndsWith(sFolder, PathSeparator) + PathSeparator;
		String sFilename = sFolder + sScreenshotPrefixName + getCounter(getScreenshotCounter(), 3) + ".png";
		if (saveScreenshotAlways(driver, sFilename))
		{
			incrementScreenshotCounter();
			return true;
		}

		return false;
	}

	/**
	 * Saves unique screenshot to following location with name: Logs.getFolderFile() + PathSeparator +
	 * sScreenshotPrefixName + nScreenshotCounter + ".png"<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Must use this method if using Grid<BR>
	 * 2) If driver cannot be cast to TakesScreenshot, no screenshot will be taken<BR>
	 * 3) It is possible for the screenshot to be blank<BR>
	 * 4) If the page does not have a body tag, then an exception will occur when attempting to take a
	 * screenshot<BR>
	 * 5) Uses Logs.getFolderFile() as the location to store the screenshot<BR>
	 * <BR>
	 * <B>Example:</B> <BR>
	 * saveScreenshot(driver) => c:\\temp\\screenshot001.png<BR>
	 * 
	 * @param driver - WebDriver that has the capability of TAKES_SCREENSHOT
	 * @return true if successful else false
	 */
	public static boolean saveScreenshotAlways(WebDriver driver)
	{
		return saveScreenshotAlways(driver, true);
	}
}
