package com.automation.ui.common.dataStructures;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.sessions.SessionClient;
import com.automation.ui.common.sessions.SessionServer;
import com.automation.ui.common.utilities.Cloner;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.CryptoDESede;
import com.automation.ui.common.utilities.Database;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Misc;
import com.automation.ui.common.utilities.Screenshot;
import com.automation.ui.common.utilities.WS;
import com.automation.ui.common.utilities.WS_Util;

/**
 * This class contains the basic variables needed for any test case
 */
public class BasicTestContext {
	/**
	 * Stores the session information each time a browser is launched in the class
	 */
	protected List<SessionInfo> sessions;

	/**
	 * Flag used to indicate whether to set driver timeouts. (Used to bypass issue with Chrome Driver and
	 * Grid.)
	 */
	private boolean bSetDriverTimeouts;

	/**
	 * Chrome specific switches
	 */
	private static final String[] chromeSwitches = new String[] {
			//
			"start-maximized", // Flag may be ignored if non-interactive mode
			// "window-position=0,0", // Flag may be ignored if non-interactive mode
			// "window-size=1600,1000", // Flag may be ignored if non-interactive mode
			"disable-popup-blocking", //
			"always-authorize-plugins", //
			"allow-outdated-plugins", //
			"disable-hang-monitor", //
			"disable-notifications" //
	//
	};

	protected WebDriver driver;

	// Unique ID of test case
	protected String sUniqueID;

	// What browser & URL to use
	protected String sBrowser;
	protected String sDriverPath;
	protected String sBrowserProfile;
	protected String sUrl;
	protected int nPageTimeout;
	protected int nElementTimeout;
	protected int nPollInterval;
	protected int nMaxTimeout;
	protected int nMultiplierTimeout;
	protected int nAJAX_Retries;
	protected int nAJAX_Stable;

	// Selenium Grid variables
	protected String sHubURL;
	protected String sPlatform;
	protected String sVersion;

	// Session Server variables
	protected String sSessionServer;
	protected int nSessionServerPort;

	// What Database Server & Database to use for verification purposes
	protected String sDB_Server;
	protected String sDB;
	protected String sDB_User;
	protected String sDB_Password;
	protected boolean bEncodedPassword;
	protected int nDB_Port;
	protected DB_Type _DB_Type;
	protected Database db;

	// screenshot setup
	protected boolean bScreenshotsEnabled;
	protected String sScreenshotFolder;
	protected String sScreenshotPrefixName;

	/**
	 * Default Constructor - Set default values<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Need to set all variables with correct values for test case<BR>
	 * <BR>
	 * <B>Methods to be called to set the variables:</B><BR>
	 * 1) setUniqueID - optional but recommended as any failure will be difficult to find in the log without a
	 * unique ID<BR>
	 * 2) setBrowserRelated - required<BR>
	 * 3) setDelays - optional, default values should be sufficient<BR>
	 * 4) setGrid - only required if going to use Grid<BR>
	 * 5) setDatabase - only required if test case queries the database<BR>
	 * 6) setScreenshot - optional, only needed if you are going to take screenshots<BR>
	 * 7) setAJAX - optional, default values should be sufficient<BR>
	 * 8) setSessionServer - only required if going to use Grid<BR>
	 */
	public BasicTestContext()
	{
		driver = null;
		setDriverTimeoutsON();
		setUniqueID("");
		setBrowserRelated("IE", "", "", "");
		setDelays(5, Framework.getTimeout(), Framework.getPollInterval(), 5, 4);
		setGrid("", "", "");
		setDatabase("", "", "", "", false, Database.getDefaultPort(), DB_Type.SQL_Server);
		setScreenshot(false, "", "");
		setAJAX(5, 3000);
		setSessionServer("", -1);
		sessions = new ArrayList<SessionInfo>();
	}

	/**
	 * Constructor - Sets all variables
	 * 
	 * @param sUniqueID - Unique ID for the test case
	 * @param sBrowser - Browser to use
	 * @param sDriverPath - If a path to the driver is needed
	 * @param sBrowserProfile - If browser profile is needed
	 * @param sUrl - Test URL
	 * @param nPageTimeout - Page Timeout for WebDriver (minutes)
	 * @param nElementTimeout - Element Timeout (seconds)
	 * @param nPollInterval - Poll Interval (milliseconds)
	 * @param nMaxTimeout - Max Timeout for actions that take a lot longer than Element timeout (minutes)
	 * @param nMultiplierTimeout - Multiplier of timeout when necessary
	 * @param nAJAX_Retries - Number of times to retry if StaleElementReferenceException occurs
	 * @param nAJAX_Stable - The continuous amount of time (milliseconds) that the element must remain stable
	 * @param sHubURL - HUB URL
	 * @param sPlatform - Platform
	 * @param sVersion - Version (of Browser for test)
	 * @param sDB_Server - DB Server
	 * @param sDB - Database Name
	 * @param sDB_User - Username to use to connect
	 * @param sDB_Password - Password to use to connect
	 * @param bEncodedPassword - true if DB Password is encoded
	 * @param nDB_Port - Port to connect to DB Server on (if less than 0, then default port is used)
	 * @param _DB_Type - Database Type
	 * @param bScreenshotsEnabled - true to enable screenshots
	 * @param sScreenshotFolder - Folder to save screenshots
	 * @param sScreenshotPrefixName - Prefix to add to screenshot file names
	 * @param sSessionServer - Session Server
	 * @param nSessionServerPort - Session Server Port
	 */
	public BasicTestContext(String sUniqueID, String sBrowser, String sDriverPath, String sBrowserProfile,
			String sUrl, int nPageTimeout, int nElementTimeout, int nPollInterval, int nMaxTimeout,
			int nMultiplierTimeout, int nAJAX_Retries, int nAJAX_Stable, String sHubURL, String sPlatform,
			String sVersion, String sDB_Server, String sDB, String sDB_User, String sDB_Password,
			boolean bEncodedPassword, int nDB_Port, DB_Type _DB_Type, boolean bScreenshotsEnabled,
			String sScreenshotFolder, String sScreenshotPrefixName, String sSessionServer,
			int nSessionServerPort)
	{
		this();

		/*
		 * Set All the variables
		 */
		setUniqueID(sUniqueID);
		setBrowserRelated(sBrowser, sDriverPath, sBrowserProfile, sUrl);
		setDelays(nPageTimeout, nElementTimeout, nPollInterval, nMaxTimeout, nMultiplierTimeout);
		setGrid(sHubURL, sPlatform, sVersion);
		setDatabase(sDB_Server, sDB, sDB_User, sDB_Password, bEncodedPassword, nDB_Port, _DB_Type);
		setScreenshot(bScreenshotsEnabled, sScreenshotFolder, sScreenshotPrefixName);
		setAJAX(nAJAX_Retries, nAJAX_Stable);
		setSessionServer(sSessionServer, nSessionServerPort);
	}

	/**
	 * Add the session information to the list<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) User should never be able to set the Session ID as it is generated by WebDriver when it launches the
	 * browser<BR>
	 * 
	 * @param sessionInfo - Session Information to be added
	 */
	private void addSessionInfo(SessionInfo sessionInfo)
	{
		sessions.add(sessionInfo);
	}

	/**
	 * Get all session information
	 * 
	 * @return List&lt;SessionInfo&gt;
	 */
	public List<SessionInfo> getSessions()
	{
		return Cloner.deepClone(sessions);
	}

	/**
	 * Sets bSetDriverTimeouts to true which will set the driver timeouts when using method <B>getDriver</B>
	 */
	public void setDriverTimeoutsON()
	{
		bSetDriverTimeouts = true;
	}

	/**
	 * Sets bSetDriverTimeouts to false such that using method <B>getDriver</B> will not set the driver
	 * timeouts<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method can be used to bypass the issue with the Chrome Driver throwing an exception with Grid
	 * when page timeout is set<BR>
	 */
	public void setDriverTimeoutsOFF()
	{
		bSetDriverTimeouts = false;
	}

	/**
	 * Set Unique ID for the test case
	 * 
	 * @param sUniqueID - Unique ID for the test case
	 */
	public void setUniqueID(String sUniqueID)
	{
		this.sUniqueID = sUniqueID;
	}

	/**
	 * Set Browser related variables
	 * 
	 * @param sBrowser - Browser to use
	 * @param sDriverPath - If a path to the driver is needed
	 * @param sBrowserProfile - If browser profile is needed
	 * @param sUrl - Test URL
	 */
	public void setBrowserRelated(String sBrowser, String sDriverPath, String sBrowserProfile, String sUrl)
	{
		this.sBrowser = sBrowser;
		this.sDriverPath = sDriverPath;
		this.sBrowserProfile = sBrowserProfile;
		this.sUrl = sUrl;
	}

	/**
	 * Set Delays<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) nTimeout only set if greater than 0<BR>
	 * 2) nPollInterval only set if greater than 0<BR>
	 * 3) nPageTimeout only set if greater than 0<BR>
	 * 4) nMaxTimeout only set if greater than 0<BR>
	 * 5) nMultiplierTimeout only set if greater than 1<BR>
	 * 
	 * @param nPageTimeout - Page Timeout for WebDriver (minutes)
	 * @param nElementTimeout - Element Timeout (seconds)
	 * @param nPollInterval - Poll Interval (milliseconds)
	 * @param nMaxTimeout - Max Timeout for actions that take a lot longer than Element timeout (minutes)
	 * @param nMultiplierTimeout - Multiplier of timeout when necessary
	 */
	public void setDelays(int nPageTimeout, int nElementTimeout, int nPollInterval, int nMaxTimeout,
			int nMultiplierTimeout)
	{
		if (nPageTimeout > 0)
			this.nPageTimeout = nPageTimeout;

		if (nElementTimeout > 0)
			this.nElementTimeout = nElementTimeout;

		if (nPollInterval > 0)
			this.nPollInterval = nPollInterval;

		if (nMaxTimeout > 0)
			this.nMaxTimeout = nMaxTimeout;

		if (nMultiplierTimeout > 1)
			this.nMultiplierTimeout = nMultiplierTimeout;
	}

	/**
	 * Set Selenium Grid 2 variables
	 * 
	 * @param sHubURL - HUB URL
	 * @param sPlatform - Platform
	 * @param sVersion - Version (of Browser for test)
	 */
	public void setGrid(String sHubURL, String sPlatform, String sVersion)
	{
		this.sHubURL = sHubURL;
		this.sPlatform = sPlatform;
		this.sVersion = sVersion;
	}

	/**
	 * Set Session Server variables
	 * 
	 * @param sSessionServer - Session Server
	 * @param nSessionServerPort - Session Server Port
	 */
	public void setSessionServer(String sSessionServer, int nSessionServerPort)
	{
		this.sSessionServer = sSessionServer;
		this.nSessionServerPort = nSessionServerPort;
	}

	/**
	 * Set Database related variables
	 * 
	 * @param sDB_Server - DB Server
	 * @param sDB - Database Name
	 * @param sDB_User - Username to use to connect
	 * @param sDB_Password - Password to use to connect
	 * @param bEncodedPassword - true if DB Password is encoded
	 * @param nDB_Port - Port to connect to DB Server on (if less than 0, then default port is used)
	 * @param _DB_Type - Database Type
	 */
	public void setDatabase(String sDB_Server, String sDB, String sDB_User, String sDB_Password,
			boolean bEncodedPassword, int nDB_Port, DB_Type _DB_Type)
	{
		this.sDB_Server = sDB_Server;
		this.sDB = sDB;
		this.sDB_User = sDB_User;
		this._DB_Type = _DB_Type;

		// Decode password if necessary
		this.bEncodedPassword = bEncodedPassword;
		if (!sDB_Password.equals("") && bEncodedPassword)
		{
			this.sDB_Password = CryptoDESede.decode(sDB_Password);
		}
		else
		{
			this.sDB_Password = sDB_Password;
		}

		// Use Default port if necessary
		if (nDB_Port < 0)
		{
			this.nDB_Port = Database.getDefaultPort();
		}
		else
		{
			this.nDB_Port = nDB_Port;
		}

		this.db = new Database(sDB_User, this.sDB_Password, sDB_Server, this.nDB_Port, sDB, this._DB_Type);
	}

	/**
	 * Set Database related variables. (Password cannot be encoded.)
	 * 
	 * @param sDB_Server - DB Server
	 * @param sDB - Database Name
	 * @param sDB_User - Username to use to connect
	 * @param sDB_Password - Password to use to connect
	 * @param nDB_Port - Port to connect to DB Server on (if less than 0, then default port is used)
	 * @param _DB_Type - Database Type
	 */
	public void setDatabase(String sDB_Server, String sDB, String sDB_User, String sDB_Password,
			int nDB_Port, DB_Type _DB_Type)
	{
		setDatabase(sDB_Server, sDB, sDB_User, sDB_Password, false, nDB_Port, _DB_Type);
	}

	/**
	 * Set Screenshot related variables and modify Screenshot object with the preferences.
	 * 
	 * @param bScreenshotsEnabled - true to enable screenshots
	 * @param sScreenshotFolder - Folder to save screenshots
	 * @param sScreenshotPrefixName - Prefix to add to screenshot file names
	 */
	public void setScreenshot(boolean bScreenshotsEnabled, String sScreenshotFolder,
			String sScreenshotPrefixName)
	{
		this.bScreenshotsEnabled = bScreenshotsEnabled;
		this.sScreenshotFolder = sScreenshotFolder;
		this.sScreenshotPrefixName = sScreenshotPrefixName;
	}

	/**
	 * Sets whether to enable/disable screenshots during execution based on bScreenshotsEnabled
	 */
	public void setScreenshotPreferencesForTest()
	{
		// Set Screenshot Settings
		Screenshot.setScreenshotSettings(sScreenshotFolder, sScreenshotPrefixName);
		Screenshot.checkCreateScreenshotFolder();

		// Should screenshots be saved?
		if (bScreenshotsEnabled)
			Screenshot.enableAllowScreenshots();
		else
			Screenshot.disableAllowScreenshots();
	}

	/**
	 * Set AJAX related variables<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) nAJAX_Retries only set if greater than 0<BR>
	 * 2) nAJAX_Stable only set if greater than 0<BR>
	 * 
	 * @param nAJAX_Retries - Number of times to retry if StaleElementReferenceException occurs
	 * @param nAJAX_Stable - The continuous amount of time (milliseconds) that the element must remain stable
	 */
	public void setAJAX(int nAJAX_Retries, int nAJAX_Stable)
	{
		if (nAJAX_Retries > 0)
			this.nAJAX_Retries = nAJAX_Retries;

		if (nAJAX_Stable > 0)
			this.nAJAX_Stable = nAJAX_Stable;
	}

	/**
	 * Gets driver (and launches browser if not already open)<BR>
	 * <BR>
	 * <B>Notes: </B>Sets WebDriver timeouts<BR>
	 * 
	 * @return WebDriver
	 */
	public WebDriver getDriver()
	{
		if (driver == null)
			launchBrowser();

		// Set the timeouts for WebDriver
		if (bSetDriverTimeouts)
		{
			driver.manage().timeouts().pageLoadTimeout(nPageTimeout, TimeUnit.MINUTES);
		}

		return driver;
	}

	/**
	 * Gets db
	 * 
	 * @return Database
	 */
	public Database getDatabase()
	{
		if (db == null)
			setDatabase(sDB_Server, sDB, sDB_User, sDB_Password, bEncodedPassword, nDB_Port, _DB_Type);

		return db;
	}

	/**
	 * Gets sUniqueID
	 * 
	 * @return String
	 */
	public String getUniqueID()
	{
		return sUniqueID;
	}

	/**
	 * Gets the URL to test against
	 * 
	 * @return String
	 */
	public String getURL()
	{
		return sUrl;
	}

	/**
	 * Initialized driver which launches a browser window<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Adds new session information from launching the browser<BR>
	 */
	private void launchBrowser()
	{
		// New session information to be added to sessions
		SessionInfo sessionInfo = new SessionInfo();

		/*
		 * Determine the type of WebDriver to instantiate
		 */

		if (Browser.FireFox == Browser.to(sBrowser))
		{
			// Firefox is slow starting due to creating a new profile each time
			Logs.log.info("Starting browser (Firefox) ...");

			if (sHubURL == null || sHubURL.equals(""))
			{
				FirefoxProfile profile = getFirefoxProfileToUse();
				driver = new FirefoxDriver(profile);
			}
			else
			{
				initDriverForGrid(sessionInfo);
			}

			sessionInfo.sessionID = ((RemoteWebDriver) driver).getSessionId().toString();
			Logs.log.info("Session ID:  " + sessionInfo.sessionID);
			addSessionInfo(sessionInfo);
			return;
		}

		if (Browser.Chrome == Browser.to(sBrowser))
		{
			Logs.log.info("Starting browser (Chrome) ...");
			Misc.addProperty("webdriver.chrome.driver", sDriverPath);

			if (sHubURL == null || sHubURL.equals(""))
			{
				try
				{
					ChromeDriverService serv = ChromeDriverService.createDefaultService();
					driver = new ChromeDriver(serv, getChromeOptions());
				}
				catch (Exception ex)
				{
					String sError = "Check that config.xml contains node /config/ENV/DriverPath."
							+ "  Also, check that Chrome is installed.  Error message was following:  "
							+ ex.getMessage() + Framework.getNewLine();
					Logs.log.error(sError);
					throw new GenericUnexpectedException(sError);
				}
			}
			else
			{
				initDriverForGrid(sessionInfo);
			}

			sessionInfo.sessionID = ((RemoteWebDriver) driver).getSessionId().toString();
			Logs.log.info("Session ID:  " + sessionInfo.sessionID);
			addSessionInfo(sessionInfo);
			return;
		}

		if (Browser.Safari == Browser.to(sBrowser))
		{
			Logs.logError("Safari browser is not currently supported by the framework");

			if (sHubURL == null || sHubURL.equals(""))
			{

			}
			else
			{
				initDriverForGrid(sessionInfo);
			}

			sessionInfo.sessionID = ((RemoteWebDriver) driver).getSessionId().toString();
			Logs.log.info("Session ID:  " + sessionInfo.sessionID);
			addSessionInfo(sessionInfo);
			return;
		}

		// No match use the IE as the browser
		Logs.log.info("Starting browser (IE) ...");
		if (sHubURL == null || sHubURL.equals(""))
		{
			Misc.addProperty("webdriver.ie.driver", sDriverPath);
			DesiredCapabilities ie_dc = new DesiredCapabilities();
			ie_dc.setCapability("useLegacyInternalServer", true);
			driver = new InternetExplorerDriver(ie_dc);
		}
		else
		{
			initDriverForGrid(sessionInfo);
		}

		sessionInfo.sessionID = ((RemoteWebDriver) driver).getSessionId().toString();
		Logs.log.info("Session ID:  " + sessionInfo.sessionID);
		addSessionInfo(sessionInfo);
	}

	/**
	 * Initializes driver for grid use<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The SessionInfo parameter will be updated with the remote host if launching browser is successful &
	 * added to session server flag<BR>
	 * 2) Adds session to node if launching browser is successful<BR>
	 * 
	 * @param updateSessionInfo - Parameter is updated with information
	 */
	private void initDriverForGrid(SessionInfo updateSessionInfo)
	{
		// Used to send the requests to the session server that tracks for cleanup purposes
		SessionClient sc = new SessionClient(sSessionServer, nSessionServerPort);

		// Flag to indicate a pending session was added successfully
		boolean bPending = false;
		try
		{
			// Add pending session to prevent cleanup task from running
			sc.addPendingSession();
			bPending = true;
		}
		catch (Exception ex)
		{
			Logs.log.warn("Adding Pending Session failed.  Session Server (" + sSessionServer + ") & Port ("
					+ nSessionServerPort + ")");
			Logs.log.warn("Exception:  " + ex);
			bPending = false;
		}

		DesiredCapabilities cap = null;

		if (Browser.FireFox == Browser.to(sBrowser))
		{
			cap = DesiredCapabilities.firefox();
			cap.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
			cap.setPlatform(getPlatform());
			cap.setVersion(sVersion);

			FirefoxProfile profile = getFirefoxProfileToUse();
			cap.setCapability(FirefoxDriver.PROFILE, profile);
		}
		else if (Browser.Chrome == Browser.to(sBrowser))
		{
			cap = DesiredCapabilities.chrome();
			cap.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
			cap.setPlatform(getPlatform());
			cap.setVersion(sVersion);
			cap.setCapability(ChromeOptions.CAPABILITY, getChromeOptions());
		}
		else
		{
			cap = DesiredCapabilities.internetExplorer();
			cap.setCapability("useLegacyInternalServer", true);
			cap.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
			cap.setPlatform(getPlatform());
			cap.setVersion(sVersion);
		}

		try
		{
			Logs.log.info("Going to use Selenium Grid ('" + sHubURL + "') ...");
			driver = new Augmenter().augment(new RemoteWebDriver(new URL(sHubURL), cap));
			updateSessionInfo.remoteHost = getHostOfNode((RemoteWebDriver) driver, sHubURL);
			Logs.log.info("Remote Host:  " + updateSessionInfo.remoteHost);

			try
			{
				// Add session to prevent cleanup task from running on this node
				URL node = new URL(updateSessionInfo.remoteHost);
				sc.addSession(SessionServer.formatNode(node));
				updateSessionInfo.addedToSessionServer = true;
			}
			catch (Exception ex)
			{
				Logs.log.warn("Adding Session failed for node:  " + updateSessionInfo.remoteHost);
				Logs.log.warn("Exception:  " + ex);
			}
		}
		catch (Exception ex)
		{
			String sNodeIssue = "java.lang.String cannot be cast to java.util.Map".toLowerCase();
			String sStackTraceMessage = ex.getMessage().toLowerCase();
			if (sStackTraceMessage.contains(sNodeIssue))
			{
				Logs.log.warn("Check the URL for Selenium Grid is correct.  (By default it ends in /wd/hub)");
			}

			ex.printStackTrace();
			String sError = "Could not start remote session due to exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage() + Framework.getNewLine();
			Logs.logError(sError);
		}
		finally
		{
			try
			{
				// Remove pending session to allow the cleanup task to run
				if (bPending)
					sc.removePendingSession();
				else
					Logs.log.warn("Pending Flag was not set as such not removing pending session");
			}
			catch (Exception ex)
			{
				Logs.log.warn("Removing Pending Session failed.  Session Server (" + sSessionServer
						+ ") & Port (" + nSessionServerPort + ")");
				Logs.log.warn("Exception:  " + ex);
			}
		}
	}

	/**
	 * Get Chrome Options to be set
	 * 
	 * @return ChromeOptions
	 */
	private ChromeOptions getChromeOptions()
	{
		ChromeOptions options = new ChromeOptions();
		options.addArguments(chromeSwitches);

		// Use profile if specified
		if (!sBrowserProfile.equals(""))
		{
			String user_data_dir = "user-data-dir=" + sBrowserProfile;
			options.addArguments(user_data_dir);
		}

		return options;
	}

	/**
	 * Get Firefox default profile when none is specified
	 * 
	 * @return FirefoxProfile
	 */
	private FirefoxProfile getFirefoxDefaultProfile()
	{
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("dom.ipc.plugins.timeoutSecs", -1);
		profile.setPreference("layers.acceleration.disabled", true);
		return profile;
	}

	/**
	 * Get the Firefox profile to be used
	 * 
	 * @return FirefoxProfile
	 */
	private FirefoxProfile getFirefoxProfileToUse()
	{
		FirefoxProfile profile;
		if (sBrowserProfile.equals(""))
		{
			// Use default profile for automation
			profile = getFirefoxDefaultProfile();
		}
		else
		{
			try
			{
				// Load specified profile for automation
				// Note: Not all profile options are used
				File file = new File(sBrowserProfile);
				profile = new FirefoxProfile(file);
				Logs.log.info("Used Firefox profile based on profile from:  " + sBrowserProfile);
			}
			catch (Exception ex)
			{
				profile = null;
				Logs.log.warn("Could not load profile (" + sBrowserProfile + ") due to exception["
						+ ex.getClass().getName() + "]:  " + ex.getMessage());
			}
		}

		return profile;
	}

	/**
	 * Based on sPlatform returns corresponding Platform enum
	 * 
	 * @return Platform
	 */
	private Platform getPlatform()
	{
		if (sPlatform.equalsIgnoreCase("WINDOWS"))
			return Platform.WINDOWS;

		if (sPlatform.equalsIgnoreCase("XP"))
			return Platform.XP;

		if (sPlatform.equalsIgnoreCase("VISTA"))
			return Platform.VISTA;

		if (sPlatform.equalsIgnoreCase("MAC"))
			return Platform.MAC;

		if (sPlatform.equalsIgnoreCase("UNIX"))
			return Platform.UNIX;

		if (sPlatform.equalsIgnoreCase("LINUX"))
			return Platform.LINUX;

		if (sPlatform.equalsIgnoreCase("ANDROID"))
			return Platform.ANDROID;

		// Default to ANY
		return Platform.ANY;
	}

	/**
	 * Used to close the browser after each method<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Resets the sessions if valid session server & port & it was added to the session server<BR>
	 */
	public void quitBrowser()
	{
		try
		{
			driver.quit();
			Thread.sleep(1000);
		}
		catch (Exception ex)
		{
		}

		// Note: For local execution, session track is not needed
		if (!sSessionServer.equals("") && nSessionServerPort > 0)
		{
			// Used to send the requests to the session server that tracks for cleanup purposes
			SessionClient sc = new SessionClient(sSessionServer, nSessionServerPort);

			for (int i = 0; i < sessions.size(); i++)
			{
				String remoteHost = sessions.get(i).remoteHost;
				if (!remoteHost.equals("") && sessions.get(i).addedToSessionServer)
				{
					try
					{
						// Remove the session (that was previously added when browser was launched)
						// Note: This only applies to the grid
						URL node = new URL(remoteHost);
						sc.removeSession(SessionServer.formatNode(node));
					}
					catch (Exception ex)
					{
						Logs.log.warn("Removing Session failed for node:  " + remoteHost);
						Logs.log.warn("Exception:  " + ex);
					}
				}
			}

			// We can reset the sessions as they have been processed
			sessions = new ArrayList<SessionInfo>();
		}
	}

	/**
	 * Maximizes Window using driver (not JavaScript)
	 * 
	 * @param driver - driver
	 */
	public static void maximizeWindow(WebDriver driver)
	{
		driver.manage().window().maximize();
	}

	/**
	 * Moves Window using driver (not JavaScript)
	 * 
	 * @param driver - driver
	 * @param nCoordinateX - X
	 * @param nCoordinateY - Y
	 */
	public static void moveWindow(WebDriver driver, int nCoordinateX, int nCoordinateY)
	{
		driver.manage().window().setPosition(new Point(nCoordinateX, nCoordinateY));
	}

	/**
	 * Resizes Window using driver (not JavaScript)
	 * 
	 * @param driver - driver
	 * @param nWidth - Width
	 * @param nHeight - Height
	 */
	public static void resizeWindow(WebDriver driver, int nWidth, int nHeight)
	{
		driver.manage().window().setSize(new Dimension(nWidth, nHeight));
	}

	/**
	 * Gets the Remote Host name that test is being executed on
	 * 
	 * @param remoteDriver - Remote Driver
	 * @param sGridURL - Grid URL (ex. http://gridhub.com:4444/wd/hub)
	 * @return Remote Host or error string
	 */
	private static String getHostOfNode(RemoteWebDriver remoteDriver, String sGridURL)
	{
		String sProtocol = "";
		String sHost = "";
		int nPort = -1;
		String sRequest = "";
		String sSessionID = "";
		String sResponse = "";

		try
		{
			/*
			 * Parse the Grid URL to construct the URL to send the request that contains the information we
			 * are looking for.
			 */
			URL url = new URL(sGridURL);
			sProtocol = url.getProtocol();
			sHost = url.getHost();
			nPort = url.getPort();

			/*
			 * Construct the request URL from the parsed information
			 */
			sSessionID = remoteDriver.getSessionId().toString();
			sRequest = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort()
					+ "/grid/api/testsession?session=" + sSessionID;

			/*
			 * Send the request
			 */
			WS ws = new WS();
			ws.setRequestGET(sRequest, false);
			sResponse = WS_Util.toString(ws.sendAndReceiveGET());

			/*
			 * Parse the JSON response
			 * Steps:
			 * 1) Break into 2 pieces based on the variable we want ',"proxyId":"' and the variable we will be
			 * looking for is in the 2nd element in the array
			 * 2) Extract just the host by splitting on '","' and it is the 1st element in the array
			 */
			String[] parse1 = sResponse.split(",\"proxyId\":\"");
			String[] parse2 = parse1[1].split("\",\"");
			return parse2[0];
		}
		catch (Exception ex)
		{
			Logs.log.warn("");
			Logs.log.warn("Exception occurred attempting to get Host Node.  The following is debug information:");
			Logs.log.warn("Protocol:    " + sProtocol);
			Logs.log.warn("Host:        " + sHost);
			Logs.log.warn("Port:        " + nPort);
			Logs.log.warn("Session ID:  " + sSessionID);
			Logs.log.warn("Request:     " + sRequest);
			Logs.log.warn("Request:     " + sRequest);
			Logs.log.warn("Response:    " + sResponse);
			Logs.log.warn("");
			return "Could not get information";
		}
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object
	 * 
	 * @return BasicTestContext
	 */
	public BasicTestContext copy()
	{
		BasicTestContext btc = new BasicTestContext();
		btc.setUniqueID(sUniqueID);
		btc.setBrowserRelated(sBrowser, sDriverPath, sBrowserProfile, sUrl);
		btc.setDelays(nPageTimeout, nElementTimeout, nPollInterval, nMaxTimeout, nMultiplierTimeout);
		btc.setGrid(sHubURL, sPlatform, sVersion);
		btc.setDatabase(sDB_Server, sDB, sDB_User, sDB_Password, nDB_Port, _DB_Type);
		btc.setScreenshot(bScreenshotsEnabled, sScreenshotFolder, sScreenshotPrefixName);
		btc.setAJAX(nAJAX_Retries, nAJAX_Stable);
		btc.setSessionServer(sSessionServer, nSessionServerPort);

		if (!bSetDriverTimeouts)
			btc.setDriverTimeoutsOFF();

		return btc;
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return BasicTestContext
	 */
	public static BasicTestContext copy(BasicTestContext obj)
	{
		try
		{
			return obj.copy();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Gets the Page Timeout for WebDriver (minutes)
	 * 
	 * @return nPageTimeout
	 */
	public int getPageTimeout()
	{
		return nPageTimeout;
	}

	/**
	 * Gets the Element Timeout (seconds)
	 * 
	 * @return nElementTimeout
	 */
	public int getElementTimeout()
	{
		return nElementTimeout;
	}

	/**
	 * Gets the Element Timeout (milliseconds)
	 * 
	 * @return nElementTimeout * 1000
	 */
	public int getElementTimeoutInMilliseconds()
	{
		return nElementTimeout * 1000;
	}

	/**
	 * Gets the Poll Interval (milliseconds)
	 * 
	 * @return nPollInterval
	 */
	public int getPollInterval()
	{
		return nPollInterval;
	}

	/**
	 * Gets the Max Timeout for actions that take a lot longer than Element timeout (minutes)
	 * 
	 * @return nMaxTimeout
	 */
	public int getMaxTimeout()
	{
		return nMaxTimeout;
	}

	/**
	 * Gets the Multiplier of timeout (used when it is necessary to increase the timeout by a factor.)
	 * 
	 * @return nMultiplierTimeout
	 */
	public int getTimeoutMultiplier()
	{
		return nMultiplierTimeout;
	}

	/**
	 * Gets the AJAX Retries value
	 * 
	 * @return nAJAX_Retries
	 */
	public int getAJAX_Retries()
	{
		return nAJAX_Retries;
	}

	/**
	 * Gets the AJAX Stable value (milliseconds)
	 * 
	 * @return nAJAX_Stable
	 */
	public int getAJAX_Stable()
	{
		return nAJAX_Stable;
	}

	/**
	 * Closes all associated windows and sets driver to null<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If you closed all windows and later find you need to reuse the driver, then call this method before
	 * getDriver()<BR>
	 * 2) Resets the sessions if valid session server & port<BR>
	 */
	public void resetDriver()
	{
		if (driver != null)
		{
			quitBrowser();
			driver = null;
		}
	}

	/**
	 * Checks if there is any open window attached to driver
	 * 
	 * @return - true a window is open else false
	 */
	public boolean isDriver()
	{
		// Handle case that driver has not been initialized yet
		if (driver == null)
			return false;

		try
		{
			String[] openWindows = driver.getWindowHandles().toArray(new String[0]);
			if (openWindows.length > 0)
				return true;
			else
				return false;
		}
		catch (Exception ex)
		{
			// If no windows are open, then exception should occur
			return false;
		}
	}

	/**
	 * Gets the browser in use based on variable sBrowser
	 * 
	 * @return Browser
	 */
	public Browser getBrowser()
	{
		return Browser.to(sBrowser);
	}

	/**
	 * Get the Grid Hub URL
	 * 
	 * @return sHubURL
	 */
	public String getGridHubURL()
	{
		return sHubURL;
	}

	/**
	 * Get the Grid Platform
	 * 
	 * @return sPlatform
	 */
	public String getGridPlatform()
	{
		return sPlatform;
	}

	/**
	 * Get the Grid Browser Version
	 * 
	 * @return sVersion
	 */
	public String getGridVersion()
	{
		return sVersion;
	}

	/**
	 * Get the Session Server
	 * 
	 * @return sSessionServer
	 */
	public String getSessionServer()
	{
		return sSessionServer;
	}

	/**
	 * Get the Session Server Port
	 * 
	 * @return nSessionServerPort
	 */
	public int getSessionServerPort()
	{
		return nSessionServerPort;
	}

	/**
	 * Get the Screenshots enabled flag
	 * 
	 * @return bScreenshotsEnabled
	 */
	public boolean getScreenshotsEnabled()
	{
		return bScreenshotsEnabled;
	}

	/**
	 * Get the Screenshots Folder
	 * 
	 * @return sScreenshotFolder
	 */
	public String getScreenshotFolder()
	{
		return sScreenshotFolder;
	}

	/**
	 * Get the Screenshots Prefix Name
	 * 
	 * @return sScreenshotPrefixName
	 */
	public String getScreenshotPrefixName()
	{
		return sScreenshotPrefixName;
	}

	/**
	 * Get the Driver Path
	 * 
	 * @return sDriverPath
	 */
	public String getDriverPath()
	{
		return sDriverPath;
	}

	/**
	 * Get the Browser Profile
	 * 
	 * @return sBrowserProfile
	 */
	public String getBrowserProfile()
	{
		return sBrowserProfile;
	}

	public String toString()
	{
		String sLog, sLog_Browser, sLog_Grid, sLog_DB, sLog_Screenshot, sLog_SessionInfo, sLog_Sessions;

		sLog_SessionInfo = "'SessionInfo':[" + Conversion.toString(sessions, ", ") + "]";

		sLog_Browser = "'BrowserInfo':{'URL':'" + sUrl + "', " + "'Browser':'" + sBrowser
				+ "', 'DriverPath':'" + sDriverPath + "', 'Profile':'" + sBrowserProfile + "'}"
				+ ", 'Timeouts':{'PageTimeout':'" + nPageTimeout + "', 'ElementTimeout':'" + nElementTimeout
				+ "', 'PollInterval':'" + nPollInterval + "', 'MaxTimeout':'" + nMaxTimeout
				+ "', 'MultiplierTimeout':'" + nMultiplierTimeout + "',  'Retries':'" + nAJAX_Retries
				+ "', 'Stable':'" + nAJAX_Stable + "', " + sLog_SessionInfo + "}";

		sLog_Grid = "'Grid':{'HUB':'" + sHubURL + "', 'Platform':'" + sPlatform + "', 'Version':'" + sVersion
				+ "'}";

		sLog_Sessions = "'Sessions':{'Server':'" + sSessionServer + "', 'Port':'" + nSessionServerPort + "'}";

		sLog_DB = "'DB_Info':{'Type':'" + _DB_Type + "', 'Server':'" + sDB_Server + "', 'DB':'" + sDB
				+ "', 'User':'" + sDB_User + "', 'Password':'" + sDB_Password + "', 'Port':'" + nDB_Port
				+ "'}";

		sLog_Screenshot = "'Screenshots':{'Enabled':'" + bScreenshotsEnabled + "', 'Folder':'"
				+ sScreenshotFolder + "', 'Prefix':'" + sScreenshotPrefixName + "'}";

		sLog = "{" + sLog_Browser + ", " + sLog_Grid + ", " + sLog_DB + ", " + sLog_Screenshot + ", "
				+ sLog_Sessions + "}";

		return sLog.replace("'", "\"");
	}
}
