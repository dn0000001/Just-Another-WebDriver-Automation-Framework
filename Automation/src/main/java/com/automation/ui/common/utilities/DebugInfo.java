package com.automation.ui.common.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.automation.ui.common.dataStructures.Browser;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.config.RuntimeProperty;

/**
 * This class is used to generate debug information
 */
public class DebugInfo {
	private WebDriver driver;
	private String storedPageSource;
	private File storedScreenshotFile;

	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public DebugInfo(WebDriver driver)
	{
		this.driver = driver;
	}

	/**
	 * Constructor
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public DebugInfo(Framework pageObject)
	{
		this(pageObject.getDriver());
	}

	/**
	 * Generates debugging information (screenshot and HTML source from failure)
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public static void generate(Framework pageObject)
	{
		generate(pageObject.getDriver());
	}

	/**
	 * Generates debugging information (screenshot and HTML source from failure)
	 * 
	 * @param driver
	 */
	public static void generate(WebDriver driver)
	{
		try
		{
			// Use system property to determine where to save the files
			String sTempLogFolder = Misc.getProperty(RuntimeProperty.env_log_folderAndOrFile, "");
			if (!sTempLogFolder.equals(""))
			{
				// Add SubFolder if specified
				String sSubFolder = Misc.getProperty(RuntimeProperty.env_log_subfolder, "");
				sTempLogFolder += sSubFolder;
			}

			// Attempt to make filenames unique
			String sUnique = Long.toString(System.currentTimeMillis());

			// Start with window that has focus to attempt to get state at time of failure
			String sActiveHandle = driver.getWindowHandle();
			String sActive_Screenshot = sTempLogFolder + sUnique + "_active.png";
			String sActive_HTML = sTempLogFolder + sUnique + "_active.html";

			Logs.log.info("Creating Debugging Files ...");
			saveScreenshot(driver, sActive_Screenshot);
			saveCurrentPage(driver, sActive_HTML);

			// Write debug information for other windows
			String sScreen, sHTML;
			Framework f = new Framework(driver);
			String[] openWindows = driver.getWindowHandles().toArray(new String[0]);
			if (openWindows.length > 1)
			{
				//
				// There is currently an issue with chromedriver 2.8 that takes a screenshot of the wrong
				// window when multiple windows. (Also, it is the same window for all screenshots with
				// multiple windows.) To workaround this issue, you need to close all unnecessary windows.
				//
				if (Browser.to(driver) == Browser.Chrome)
				{
					onlyActiveWindow(driver, sActiveHandle, sTempLogFolder, sUnique);
				}
				else
				{
					for (int i = 0; i < openWindows.length; i++)
					{
						if (!openWindows[i].equals(sActiveHandle))
						{
							if (f.switchToPopupWindow(openWindows[i]))
							{
								sScreen = sTempLogFolder + sUnique + "_" + i + ".png";
								sHTML = sTempLogFolder + sUnique + "_" + i + ".html";
								saveScreenshot(driver, sScreen);
								saveCurrentPage(driver, sHTML);
							}
							else
							{
								Logs.log.warn("Could not switch to window (" + openWindows[i]
										+ ") as such no debug information written");
							}
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("Failed to write debug information due to exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage());
		}

		Logs.log.info("" + Framework.getNewLine());
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
	 */
	private static void saveScreenshot(WebDriver driver, String sFilename)
	{
		int nDebugCode = 0;

		String sAlert = Framework.acceptAlertTry(driver);
		if (sAlert != null)
			Logs.log.info("Cleared alert via 'Accept' that had message:  " + sAlert);

		try
		{
			File file = new File(sFilename);
			if (file.exists())
				file.delete();

			nDebugCode = 1;

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			nDebugCode = 2;

			FileUtils.copyFile(scrFile, file);
			Logs.log.info("Saved screenshot to file:  " + sFilename);
		}
		catch (Exception ex)
		{
			Logs.log.warn("Debug Code:  " + nDebugCode);
			Logs.log.warn("Failed to save screenshot (" + sFilename + ") due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
		}
	}

	/**
	 * Writes the current page source to a file for debugging purposes.
	 * 
	 * @param driver
	 * @param sFilename - Filename to write the current page as
	 */
	private static void saveCurrentPage(WebDriver driver, String sFilename)
	{
		int nDebugCode = 0;

		String sAlert = Framework.acceptAlertTry(driver);
		if (sAlert != null)
			Logs.log.info("Cleared alert via 'Accept' that had message:  " + sAlert);

		try
		{
			File file = new File(sFilename);
			if (file.exists())
				file.delete();

			nDebugCode = 1;

			file.createNewFile();

			nDebugCode = 2;

			BufferedWriter out = new BufferedWriter(new FileWriter(file));

			nDebugCode = 3;

			out.write(driver.getPageSource());
			out.close();
			Logs.log.info("Saved HTML page to file:  " + sFilename);
		}
		catch (Exception ex)
		{
			Logs.log.warn("Debug Code:  " + nDebugCode);
			Logs.log.warn("Saving current page source (" + sFilename + ") failed with exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
		}
	}

	/**
	 * Closes all windows except the window with the specified handle & takes a screenshot and saves the HTML
	 * page<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Screenshot is saved as sFolder + sUnique + "_onlyActive.png"<BR>
	 * 2) HTML page is saved as sFolder + sUnique + "_onlyActive.html"<BR>
	 * 
	 * @param driver
	 * @param sHandle - Handle of window to take screenshot
	 * @param sFolder - Folder to save screenshot and HTML page to
	 * @param sUnique - Used to make the filenames of the screenshot and HTML page unique
	 */
	private static void onlyActiveWindow(WebDriver driver, String sHandle, String sFolder, String sUnique)
	{
		// Close all windows that we are not interested in
		Framework f = new Framework(driver);
		String[] openWindows = driver.getWindowHandles().toArray(new String[0]);
		for (int i = 0; i < openWindows.length; i++)
		{
			if (!openWindows[i].equals(sHandle))
			{
				Parameter windowInfo = new Parameter(openWindows[i], openWindows[i]);
				f.close(windowInfo);
			}
		}

		if (f.switchToPopupWindow(sHandle))
		{
			String sAlert = Framework.acceptAlertTry(driver);
			if (sAlert != null)
				Logs.log.info("Cleared alert via 'Accept' that had message:  " + sAlert);

			String sActive_Screenshot = sFolder + sUnique + "_onlyActive.png";
			String sActive_HTML = sFolder + sUnique + "_onlyActive.html";
			saveScreenshot(driver, sActive_Screenshot);
			saveCurrentPage(driver, sActive_HTML);
		}
		else
		{
			Logs.log.warn("Could not switch to window (" + sHandle + ") as such no debug information written");
		}
	}

	/**
	 * Generates debugging information (screenshot and HTML source) from current page
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 * @param prefix - Prefix for the screenshot/HTML file to be saved
	 */
	public static void generate(Framework pageObject, String prefix)
	{
		generate(pageObject.getDriver(), prefix);
	}

	/**
	 * Generates debugging information (screenshot and HTML source) from current page
	 * 
	 * @param driver
	 * @param prefix - Prefix for the screenshot/HTML file to be saved
	 */
	public static void generate(WebDriver driver, String prefix)
	{
		generate(driver, prefix, true, true);
	}

	/**
	 * Generates debugging information from current page
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 * @param prefix - Prefix for the screenshot/HTML file to be saved
	 * @param screenshot - true for screenshot
	 * @param html - true for HTML
	 */
	public static void generate(Framework pageObject, String prefix, boolean screenshot, boolean html)
	{
		generate(pageObject.getDriver(), prefix, screenshot, html);
	}

	/**
	 * Generates debugging information from current page
	 * 
	 * @param driver
	 * @param prefix - Prefix for the screenshot/HTML file to be saved
	 * @param screenshot - true for screenshot
	 * @param html - true for HTML
	 */
	public static void generate(WebDriver driver, String prefix, boolean screenshot, boolean html)
	{
		try
		{
			// Use system property to determine where to save the files
			String sTempLogFolder = Misc.getProperty(RuntimeProperty.env_log_folderAndOrFile, "");
			if (!sTempLogFolder.equals(""))
			{
				// Add SubFolder if specified
				String sSubFolder = Misc.getProperty(RuntimeProperty.env_log_subfolder, "");
				sTempLogFolder += sSubFolder;
			}

			// Attempt to make filenames unique
			String sUnique = prefix + Long.toString(System.currentTimeMillis());
			String sScreenshot = sTempLogFolder + sUnique + ".png";
			String sHTML = sTempLogFolder + sUnique + ".html";

			if (screenshot || html)
			{
				Logs.log.info("Creating Debugging Files ...");

				if (screenshot)
					saveScreenshot(driver, sScreenshot);

				if (html)
					saveCurrentPage(driver, sHTML);

				Logs.log.info("");
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("Failed to write debug information due to exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage());
		}
	}

	/**
	 * Store current information for later
	 * 
	 * @param screenshot - true for screenshot
	 * @param html - true for HTML
	 */
	private void storeInfo(boolean screenshot, boolean html)
	{
		try
		{
			String sAlert = Framework.acceptAlertTry(driver);
			if (sAlert != null)
				Logs.log.info("Cleared alert via 'Accept' that had message:  " + sAlert);

			if (screenshot)
				storedScreenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			if (html)
				storedPageSource = driver.getPageSource();
		}
		catch (Exception ex)
		{
			Logs.log.warn("Failed to store screenshot due to exception [" + ex.getClass().getName() + "]:  "
					+ ex.getMessage());
		}
	}

	/**
	 * Store current information for later
	 */
	public void storeInfo()
	{
		storeInfo(true, true);
	}

	/**
	 * Store the current page source for later
	 */
	public void storePageSource()
	{
		storeInfo(false, true);
	}

	/**
	 * Store screenshot of the current page for later
	 */
	public void storeScreenshot()
	{
		storeInfo(true, false);
	}

	/**
	 * Saves the stored screenshot. (User needs to ensure file is unique and it is has a PNG extension)
	 * 
	 * @param sFilename - File name needs to be unique and have PNG extension
	 */
	private void saveStoredScreenshot(String sFilename)
	{
		int nDebugCode = 0;

		try
		{
			File file = new File(sFilename);
			if (file.exists())
				file.delete();

			nDebugCode = 1;
			FileUtils.copyFile(storedScreenshotFile, file);
			Logs.log.info("Saved screenshot to file:  " + sFilename);
		}
		catch (Exception ex)
		{
			Logs.log.warn("Debug Code:  " + nDebugCode);
			Logs.log.warn("Failed to save stored screenshot (" + sFilename + ") due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
		}
	}

	/**
	 * Writes the stored page source to a file for debugging purposes.
	 * 
	 * @param sFilename - Filename to write the page as
	 */
	private void saveStoredPage(String sFilename)
	{
		int nDebugCode = 0;

		try
		{
			File file = new File(sFilename);
			if (file.exists())
				file.delete();

			nDebugCode = 1;

			file.createNewFile();

			nDebugCode = 2;

			BufferedWriter out = new BufferedWriter(new FileWriter(file));

			nDebugCode = 3;

			out.write(storedPageSource);
			out.close();
			Logs.log.info("Saved HTML page to file:  " + sFilename);
		}
		catch (Exception ex)
		{
			Logs.log.warn("Debug Code:  " + nDebugCode);
			Logs.log.warn("Saving stored page source (" + sFilename + ") failed with exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
		}
	}

	/**
	 * Generates debugging information from the stored information
	 * 
	 * @param prefix - Prefix for the screenshot/HTML file to be saved
	 */
	public void generate(String prefix)
	{
		generate(prefix, true, true);
	}

	/**
	 * Generates debugging information from the stored information
	 * 
	 * @param prefix - Prefix for the screenshot/HTML file to be saved
	 * @param screenshot - true for screenshot
	 * @param html - true for HTML
	 */
	public void generate(String prefix, boolean screenshot, boolean html)
	{
		try
		{
			// Use system property to determine where to save the files
			String sTempLogFolder = Misc.getProperty(RuntimeProperty.env_log_folderAndOrFile, "");
			if (!sTempLogFolder.equals(""))
			{
				// Add SubFolder if specified
				String sSubFolder = Misc.getProperty(RuntimeProperty.env_log_subfolder, "");
				sTempLogFolder += sSubFolder;
			}

			// Attempt to make filenames unique
			String sUnique = prefix + Long.toString(System.currentTimeMillis());
			String sScreenshot = sTempLogFolder + sUnique + ".png";
			String sHTML = sTempLogFolder + sUnique + ".html";

			if (screenshot || html)
			{
				Logs.log.info("Creating Debugging Files ...");

				if (screenshot)
					saveStoredScreenshot(sScreenshot);

				if (html)
					saveStoredPage(sHTML);

				Logs.log.info("");
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("Failed to write debug information due to exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage());
		}
	}
}
