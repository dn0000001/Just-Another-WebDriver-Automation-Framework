package com.automation.ui.common.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.ui.Select;

import com.automation.ui.common.dataStructures.AutoCompleteField;
import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.Browser;
import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.DropDownDefaults;
import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.dataStructures.HTML_Event;
import com.automation.ui.common.dataStructures.HTML_EventType;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.dataStructures.LogErrorLevel;
import com.automation.ui.common.dataStructures.MouseEvent;
import com.automation.ui.common.dataStructures.MouseEventType;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.Selection;
import com.automation.ui.common.exceptions.CheckBoxNoSuchElementException;
import com.automation.ui.common.exceptions.CheckBoxNotEnabled;
import com.automation.ui.common.exceptions.CheckBoxWrongStateException;
import com.automation.ui.common.exceptions.ClickNoSuchElementException;
import com.automation.ui.common.exceptions.CloseWindowException;
import com.automation.ui.common.exceptions.DropDownIndexException;
import com.automation.ui.common.exceptions.DropDownNoSuchElementException;
import com.automation.ui.common.exceptions.DropDownPartialMatchException;
import com.automation.ui.common.exceptions.DropDownSelectionException;
import com.automation.ui.common.exceptions.ElementNotDisplayedException;
import com.automation.ui.common.exceptions.ElementNotEnabledException;
import com.automation.ui.common.exceptions.EnterFieldNoSuchElementException;
import com.automation.ui.common.exceptions.GenericActionNotCompleteException;
import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.exceptions.GetException;
import com.automation.ui.common.exceptions.MouseActionsException;
import com.automation.ui.common.exceptions.NotFoundWindowException;
import com.automation.ui.common.exceptions.WrongPageException;

/**
 * This class is for providing a common way to use Selenium that provides logging for use with page objects.<BR>
 * <BR>
 * <B>This should be the base class used for all page objects.</B>
 */
public class Framework {
	private static String NewLine = System.getProperty("line.separator");
	private static String PathSeparator = System.getProperty("file.separator");

	// Selenium variables
	protected WebDriver driver;

	/**
	 * Browser in use
	 */
	private Browser browser;

	/**
	 * Timeout in seconds
	 */
	private static int nTimeout = 30;

	/**
	 * Poll Interval in milliseconds
	 */
	private static int nPollInterval = 1000;

	/**
	 * Timeout Multiplier when timeout is not sufficient
	 */
	private int nTimeoutMultiplier = 1;

	/**
	 * Number of Retries to prevent StaleElementReferenceException for actions that require it
	 */
	private int nRetries = 5;

	/**
	 * The continuous amount of time (milliseconds) that the element must be stable for
	 */
	private int nStableTime = nPollInterval;

	/**
	 * The language of the page<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Used to store the language of the page such that multiple lookups are not necessary and it can be
	 * initialized from the constructor that takes a page object<BR>
	 * 2) Setting to an invalid language initially to force variable to be set to the actual language. (This
	 * should help in debugging as the user will not that it was not set.)<BR>
	 */
	private Languages lang = Languages.KEY;

	/**
	 * Handle when constructor is used
	 */
	private String sMainWindowHandle;

	/**
	 * Handle when working with a pop-up
	 */
	private String sPopupWindowHandle;

	/**
	 * The attribute that contains the input value
	 */
	private static final String _InputAttribute = "value";

	// Variables to handle AJAX
	// Supported AJAX types
	public static final String JQUERY = "jQuery";
	public static final String DOJO = "Dojo";
	public static final String CUSTOM = "Custom";

	// Default values for the supported AJAX types
	private static final String sDojo_AJAX_Complete = "if (ajaxConcurrentCallsCount <= 0) return true; else return false;";
	private static final String sJQuery_AJAX_Complete = "if (jQuery.active <= 0) return true; else return false;";
	private String sXpath_AJAX_Custom_Complete = "//div[@id='pageLoadWarningLabel' and @class='hidden']";

	// jQuery is the default AJAX type
	private String sAJAX_type = JQUERY;

	// Used in methods that need to check that an action has started before the action complete check
	private static String sActionStarted = "//div[@id='pageLoadWarningLabel' and not(@class='hidden')]";
	private static String sActionCompleted = "//div[@id='pageLoadWarningLabel' and @class='hidden']";

	// General class variables
	private String sPageName;
	private String sUrl;

	// Database variable
	private Database db;

	/**
	 * Default Constructor - if super is not used then this default constructor is implicitly called when this
	 * class is inherited
	 */
	public Framework()
	{
		this.driver = null;
		this.sMainWindowHandle = "";
		this.sPopupWindowHandle = null;
		setBrowser(Browser.InternetExplorer);
		setDatabase(null);
	}

	/**
	 * Constructor - This is the generally used constructor which sets the driver, window handles and browser
	 * 
	 * @param driver
	 */
	public Framework(WebDriver driver)
	{
		this.driver = driver;
		this.sMainWindowHandle = driver.getWindowHandle();
		this.sPopupWindowHandle = null;
		setBrowser(Browser.to(driver));
		setDatabase(null);
	}

	/**
	 * Constructor - Allows the timeout to be set at instantiation
	 * 
	 * @param driver
	 * @param nTimeout - Timeout in seconds to set
	 */
	public Framework(WebDriver driver, int nTimeout)
	{
		this.driver = driver;
		this.sMainWindowHandle = driver.getWindowHandle();
		this.sPopupWindowHandle = null;
		setTimeout(nTimeout);
		setBrowser(Browser.to(driver));
		setDatabase(null);
	}

	/**
	 * Constructor - Uses the given handle to switch to the window and initialize the class
	 * 
	 * @param driver
	 * @param sHandle - Window handle to switch to
	 * @throws GenericUnexpectedException if cannot switch to window using handle
	 */
	public Framework(WebDriver driver, String sHandle)
	{
		this.driver = driver;

		if (!switchToPopupWindow(sHandle))
		{
			String sError = "Could not switch to the window with handle ('" + sHandle + "')";
			Logs.logError(new NotFoundWindowException(sError));
		}

		this.sMainWindowHandle = sHandle;
		this.sPopupWindowHandle = null;
		setBrowser(Browser.to(driver));
		setDatabase(null);
	}

	/**
	 * Constructor - Sets all class variables from Page Object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Sets non-static variables that can be set for each object (such as Database, Timeout Multiplier,
	 * Retries, Stable Time, etc.)<BR>
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public Framework(Framework pageObject)
	{
		this(pageObject.getDriver());
		set(pageObject);
	}

	/**
	 * Constructor - Sets all class variables from Context<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Does not set language as it is not contained in the context<BR>
	 * 2) Sets non-static variables that can be set for each object (such as Database, Timeout Multiplier,
	 * Retries, Stable Time, etc.)<BR>
	 * 
	 * @param context - Context to initialize variables from
	 */
	public Framework(BasicTestContext context)
	{
		this(context.getDriver());
		set(context);
	}

	/**
	 * Verifies URL & sets URL & Page Name
	 * 
	 * @param sUrl - Expected URL
	 * @param sPageName - Page Name for logging
	 */
	protected void initialize(String sUrl, String sPageName)
	{
		initialize(Arrays.asList(sUrl), sPageName, getTimeoutInMilliseconds());
	}

	/**
	 * Verifies URL & sets URL & Page Name
	 * 
	 * @param sUrl - Expected URL
	 * @param sPageName - Page Name for logging
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 */
	protected void initialize(String sUrl, String sPageName, int nMaxWaitTime)
	{
		initialize(Arrays.asList(sUrl), sPageName, nMaxWaitTime);
	}

	/**
	 * Verifies that you are at an acceptable URL to continue initialization.<BR>
	 * <BR>
	 * <B>Note: </B>Use this method if the same page has multiple URLs associated with it.
	 * 
	 * @param urls - URLs that you will allow initialization
	 * @param sPageName - Page Name for logging
	 */
	protected void initialize(String[] urls, String sPageName)
	{
		initialize(Arrays.asList(urls), sPageName, getTimeoutInMilliseconds());
	}

	/**
	 * Verifies that you are at an acceptable URL to continue initialization.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use this method if the same page has multiple URLs associated with it.<BR>
	 * 2) For inline initialization of the list use Arrays.asList<BR>
	 * 
	 * @param urls - List of URLs that you will allow initialization
	 * @param sPageName - Page Name for logging
	 */
	protected void initialize(List<String> urls, String sPageName)
	{
		initialize(urls, sPageName, getTimeoutInMilliseconds());
	}

	/**
	 * Verifies that you are at an acceptable URL to continue initialization.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use this method if the same page has multiple URLs associated with it.<BR>
	 * 2) For inline initialization of the list use Arrays.asList<BR>
	 * 
	 * @param urls - List of URLs that you will allow initialization
	 * @param sPageName - Page Name for logging
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 */
	protected void initialize(List<String> urls, String sPageName, int nMaxWaitTime)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			for (String sURL : urls)
			{
				// If at an accepted URL, then complete initialization and return
				if (driver.getCurrentUrl().contains(sURL))
				{
					PageFactory.initElements(new DefaultElementLocatorFactory(driver), this);
					this.sUrl = sURL;
					this.sPageName = sPageName;
					return;
				}
			}

			sleep(getPollInterval());
		}

		// Make String of all URLs for error reporting
		String sAll_URLs = "";
		for (int i = 0; i < urls.size(); i++)
		{
			if (i != urls.size() - 1)
				sAll_URLs += "'" + urls.get(i) + "' , ";
			else
				sAll_URLs += "'" + urls.get(i) + "'";
		}

		String sError = "Not at " + sPageName + " page (must contain any of " + sAll_URLs + ")  URL:  "
				+ driver.getCurrentUrl();
		Screenshot.saveScreenshot(driver);
		// Screenshot.saveScreenshotAddSuffix("method_initialize");
		Logs.logError(new WrongPageException(sError));
	}

	/**
	 * Verifies that you are at an acceptable URL to continue initialization.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use this method if the same page has multiple URLs associated with it.<BR>
	 * 2) For inline initialization of the list use Arrays.asList<BR>
	 * 3) First checks if current URL contains the value. If necessary, checks if the current URL matches the
	 * value as regular expression<BR>
	 * 4) If using a regular expression, then starting with http.* to match the domain and follow with the
	 * partial text(s) to match such as http.*part1.*part2<BR>
	 * 
	 * @param urls - List of URLs that you will allow initialization
	 * @param pageName - Page Name for logging
	 * @param maxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 */
	protected void init(List<String> urls, String pageName, int maxWaitTime)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(maxWaitTime))
		{
			for (String sURL : urls)
			{
				// First check if current URL contains the expected URL
				boolean bCorrectPage = driver.getCurrentUrl().contains(sURL);

				// If necessary, check if current URL matches the expected URL using a regular expression
				if (!bCorrectPage)
				{
					try
					{
						// Update flag with the regular expression check
						bCorrectPage = driver.getCurrentUrl().matches(sURL);
					}
					catch (Exception ex)
					{
						// If performing a contains, then the string could be an invalid regular expression
						bCorrectPage = false;
					}
				}

				// If at an accepted URL, then complete initialization and return
				if (bCorrectPage)
				{
					PageFactory.initElements(new DefaultElementLocatorFactory(driver), this);
					this.sUrl = sURL;
					this.sPageName = pageName;
					return;
				}
			}

			sleep(getPollInterval());
		}

		String sError = "Not at " + pageName + " page (must contain any of "
				+ Conversion.toString(urls, ", ") + ")  URL:  " + driver.getCurrentUrl();
		Screenshot.saveScreenshot(driver);
		// Screenshot.saveScreenshotAddSuffix("method_initialize");
		Logs.logError(new WrongPageException(sError));
	}

	/**
	 * Verifies that you are at an acceptable URL to continue initialization.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use this method if the same page has multiple URLs associated with it.<BR>
	 * 2) For inline initialization of the list use Arrays.asList<BR>
	 * 3) First checks if current URL contains the value. If necessary, checks if the current URL matches the
	 * value as regular expression<BR>
	 * 4) If using a regular expression, then starting with http.* to match the domain and follow with the
	 * partial text(s) to match such as http.*part1.*part2<BR>
	 * 
	 * @param url - Expected URL
	 * @param pageName - Page Name for logging
	 * @param maxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 */
	protected void init(String url, String pageName, int maxWaitTime)
	{
		init(Arrays.asList(url), pageName, maxWaitTime);
	}

	/**
	 * Verifies that you are at an acceptable URL to continue initialization.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use this method if the same page has multiple URLs associated with it.<BR>
	 * 2) First checks if current URL contains the value. If necessary, checks if the current URL matches the
	 * value as regular expression<BR>
	 * 3) If using a regular expression, then starting with http.* to match the domain and follow with the
	 * partial text(s) to match such as http.*part1.*part2<BR>
	 * 
	 * @param urls - URLs that you will allow initialization
	 * @param pageName - Page Name for logging
	 * @param maxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 */
	protected void init(String[] urls, String pageName, int maxWaitTime)
	{
		init(Arrays.asList(urls), pageName, maxWaitTime);
	}

	/**
	 * Waits for the specified URL to appear.
	 * 
	 * @param driver
	 * @param sUrl
	 * @return true if URL appeared before timeout else false
	 */
	public static boolean isWaitForURL(WebDriver driver, String sUrl)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			if (driver.getCurrentUrl().contains(sUrl))
				return true;

			sleep(getPollInterval());
		}

		// URL did not become the expected value within the timeout
		return false;
	}

	/**
	 * Returns Parameter variable with the window information of the class<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Parameter.param = Page Name / Window Name<BR>
	 * 2) Parameter.value = Main Window Handle<BR>
	 * 
	 * @return Parameter
	 */
	public Parameter getWindowInfo()
	{
		return new Parameter(getPageName(), getMainWindowHandle());
	}

	/**
	 * Sets the browser in use
	 * 
	 * @param browser - browser in use
	 */
	public void setBrowser(Browser browser)
	{
		this.browser = browser;
	}

	/**
	 * Gets the browser in use that was set
	 * 
	 * @return Browser
	 */
	public Browser getBrowser()
	{
		return browser;
	}

	/**
	 * Gets the Page Name stored at initialization
	 * 
	 * @return sPageName
	 */
	public String getPageName()
	{
		return sPageName;
	}

	/**
	 * Sets the Page Name
	 * 
	 * @param sPageName - Page Name
	 */
	public void setPageName(String sPageName)
	{
		this.sPageName = Conversion.nonNull(sPageName);
	}

	/**
	 * Get the URL stored at initialization
	 * 
	 * @return sUrl
	 */
	public String getURL()
	{
		return sUrl;
	}

	/**
	 * Sets the URL for the class
	 * 
	 * @param sUrl - URL
	 */
	public void setURL(String sUrl)
	{
		this.sUrl = Conversion.nonNull(sUrl);
	}

	/**
	 * Sets nTimeoutMultiplier if greater than 1
	 * 
	 * @param nTimeoutMultiplier - Increases the timeout during specific actions by this factor
	 */
	public void setTimeoutMultiplier(int nTimeoutMultiplier)
	{
		if (nTimeoutMultiplier > 1)
			this.nTimeoutMultiplier = nTimeoutMultiplier;
	}

	/**
	 * Set the max number of retries to try and prevent StaleElementReferenceException for actions that
	 * require it<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set if greater than 0<BR>
	 * 
	 * @param nRetries - Max Number of Retries to try and prevent StaleElementReferenceException
	 */
	public void setRetries(int nRetries)
	{
		if (nRetries > 0)
			this.nRetries = nRetries;
	}

	/**
	 * Sets the time (milliseconds) that an element needs to remain removed or added to be considered stable<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only set if greater than 0<BR>
	 * 
	 * @param nStableTime - The continuous time in milliseconds for elements to be considered stable
	 */
	public void setStableTime(int nStableTime)
	{
		if (nStableTime > 0)
			this.nStableTime = nStableTime;
	}

	/**
	 * @return the language
	 */
	public Languages getLanguage()
	{
		return lang;
	}

	/**
	 * @param lang the language to set
	 */
	public void setLanguage(Languages lang)
	{
		this.lang = lang;
	}

	/**
	 * Set all class variables from the page object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Sets non-static variables that can be set for each object (such as Database, Timeout Multiplier,
	 * Retries, Stable Time, etc.)<BR>
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public void set(Framework pageObject)
	{
		setDatabase(pageObject.getDatabase());
		setTimeoutMultiplier(pageObject.getMultiplier());
		setRetries(pageObject.getRetries());
		setStableTime(pageObject.getStableTime());
		setLanguage(pageObject.getLanguage());
	}

	/**
	 * Set all class variables from the context<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Does not set language as it is not contained in the context<BR>
	 * 2) Sets non-static variables that can be set for each object (such as Database, Timeout Multiplier,
	 * Retries, Stable Time, etc.)<BR>
	 * 
	 * @param context
	 */
	public void set(BasicTestContext context)
	{
		setDatabase(context.getDatabase());
		setTimeoutMultiplier(context.getTimeoutMultiplier());
		setRetries(context.getAJAX_Retries());
		setStableTime(context.getAJAX_Stable());
	}

	/**
	 * Gets the current timeout value (seconds)
	 * 
	 * @return timeout value (seconds)
	 */
	public static int getTimeout()
	{
		return nTimeout;
	}

	/**
	 * Gets the current timeout value in milliseconds
	 * 
	 * @return timeout value (milliseconds)
	 */
	public static int getTimeoutInMilliseconds()
	{
		return nTimeout * 1000;
	}

	/**
	 * Changes the default timeout
	 * 
	 * @param nTimeout - new timeout value in seconds
	 */
	public synchronized static void setTimeout(int nTimeout)
	{
		Framework.nTimeout = nTimeout;
	}

	/**
	 * Gets the current poll interval value (milliseconds)
	 * 
	 * @return poll interval (milliseconds)
	 */
	public static int getPollInterval()
	{
		return nPollInterval;
	}

	/**
	 * Changes the default poll interval
	 * 
	 * @param nPollInterval - new poll interval value in milliseconds
	 */
	public synchronized static void setPollInterval(int nPollInterval)
	{
		Framework.nPollInterval = nPollInterval;
	}

	/**
	 * Gets Path Separator
	 * 
	 * @return PathSeparator
	 */
	public static String getPathSeparator()
	{
		return Framework.PathSeparator;
	}

	/**
	 * Sets Path Separator
	 * 
	 * @param sPathSeparator - Path Separator
	 */
	public static void setPathSepartor(String sPathSeparator)
	{
		Framework.PathSeparator = sPathSeparator;
	}

	/**
	 * Return the new line characters
	 * 
	 * @return NewLine
	 */
	public static String getNewLine()
	{
		return Framework.NewLine;
	}

	/**
	 * Sets the new line characters
	 * 
	 * @param sNewLine - New Line Characters
	 */
	public static void setNewLine(String sNewLine)
	{
		Framework.NewLine = sNewLine;
	}

	/**
	 * Sets the Database object for the class
	 * 
	 * @param db - Database object to set for the class
	 */
	public void setDatabase(Database db)
	{
		this.db = new Database(db);
	}

	/**
	 * Returns the Database object stored in the class<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The database object needs to have been set<BR>
	 * 
	 * @return Database
	 */
	public Database getDatabase()
	{
		return db;
	}

	/**
	 * Returns the WebDriver object stored in the class
	 * 
	 * @return WebDriver
	 */
	public WebDriver getDriver()
	{
		return driver;
	}

	/**
	 * Gets the current Custom AJAX xpath for testing if AJAX page is complete.
	 * 
	 * @return sXpath_AJAX_Custom_Complete
	 */
	public String getXpath_AJAX_Custom_Complete()
	{
		return sXpath_AJAX_Custom_Complete;
	}

	/**
	 * Changes the default xpath used to determine that the AJAX page is complete when sAJAX_type == CUSTOM
	 * 
	 * @param sXpath_AJAX_Custom_Complete - new xpath for testing if the AJAX page is complete
	 */
	public void setXpath_AJAX_Complete(String sXpath_AJAX_Custom_Complete)
	{
		this.sXpath_AJAX_Custom_Complete = sXpath_AJAX_Custom_Complete;
	}

	/**
	 * Gets the current AJAX type being used to wait for pages to complete
	 * 
	 * @return sAJAX_type
	 */
	public String getAJAX_type()
	{
		return sAJAX_type;
	}

	/**
	 * Sets sAJAX_type if type is supported (DOJO, JQUERY or CUSTOM)
	 * 
	 * @param sType - AJAX type being used
	 */
	public void setAJAX_type(String sType)
	{
		if (sType == DOJO)
			sAJAX_type = DOJO;

		if (sType == JQUERY)
			sAJAX_type = JQUERY;

		if (sType == CUSTOM)
			sAJAX_type = CUSTOM;
	}

	/**
	 * Get Action Started
	 * 
	 * @return sActionStarted
	 */
	public static String getActionStarted()
	{
		return sActionStarted;
	}

	/**
	 * Set Action Started
	 * 
	 * @param sValue - Action Started Value
	 */
	public static void setActionStarted(String sValue)
	{
		sActionStarted = sValue;
	}

	/**
	 * Get Action Complete
	 * 
	 * @return sActionCompleted
	 */
	public static String getActionComplete()
	{
		return sActionCompleted;
	}

	/**
	 * Set Action Complete
	 * 
	 * @param sValue - Action Complete Value
	 */
	public static void setActionComplete(String sValue)
	{
		sActionCompleted = sValue;
	}

	/**
	 * Gets the currently stored Pop-up Handle
	 * 
	 * @return sPopupWindowHandle
	 */
	public String getPopupWindowHandle()
	{
		return sPopupWindowHandle;
	}

	/**
	 * Gets the Main Window Handle from initialization
	 * 
	 * @return sMainWindowHandle
	 */
	public String getMainWindowHandle()
	{
		return sMainWindowHandle;
	}

	/**
	 * Get the Input Attribute
	 * 
	 * @return _InputAttribute
	 */
	public static String getInputAttr()
	{
		return _InputAttribute;
	}

	/**
	 * Returns the By Object to locate the element. Allows for selenium 1.0.3 use of locators.<BR>
	 * <BR>
	 * <B>Supported locators (matches in order):</B><BR>
	 * 1) <B>xpath:</B> // or ./ or xpath=<BR>
	 * 2) <B>name:</B> name=<BR>
	 * 3) <B>css:</B> css=<BR>
	 * 4) <B>link name:</B> link=<BR>
	 * 5) <B>class name:</B> class=<BR>
	 * 6) <B>tag name:</B> tag=<BR>
	 * 7) <B>partial link name:</B> plink=<BR>
	 * 8) <B>id:</B> id= or just id<BR>
	 * 
	 * @param sLocateUsing - How to locate the element
	 * @return By
	 */
	public static By locatedBy(String sLocateUsing)
	{
		// The locators that are being supported.
		// Note: I am only supporting the ones I use.
		String XPATH = "xpath=";
		String NAME = "name=";
		String CSS = "css=";
		String LINK = "link=";
		String CLASS = "class=";
		String TAG = "tag=";
		String partialLINK = "plink=";

		// standard xpath
		if (sLocateUsing.startsWith("/"))
			return By.xpath(sLocateUsing);

		// relative xpath
		if (sLocateUsing.startsWith("./"))
			return By.xpath(sLocateUsing);

		// xpath=//test
		if (sLocateUsing.toLowerCase().startsWith(XPATH))
			return By.xpath(sLocateUsing.substring(XPATH.length()));

		// name=something
		if (sLocateUsing.toLowerCase().startsWith(NAME))
			return By.name(sLocateUsing.substring(NAME.length()));

		// css=something
		if (sLocateUsing.toLowerCase().startsWith(CSS))
			return By.cssSelector(sLocateUsing.substring(CSS.length()));

		// link=something
		if (sLocateUsing.toLowerCase().startsWith(LINK))
			return By.linkText(sLocateUsing.substring(LINK.length()));

		// class=something
		if (sLocateUsing.toLowerCase().startsWith(CLASS))
			return By.className(sLocateUsing.substring(CLASS.length()));

		// tag=something
		if (sLocateUsing.toLowerCase().startsWith(TAG))
			return By.tagName(sLocateUsing.substring(TAG.length()));

		// plink=something
		if (sLocateUsing.toLowerCase().startsWith(partialLINK))
			return By.partialLinkText(sLocateUsing.substring(partialLINK.length()));

		// no match assume that it is ID
		return By.id(sLocateUsing);
	}

	/**
	 * Waits for an element to appear.<BR>
	 * <BR>
	 * <B>Note: </B>The selenium method isEnabled() returns true in most cases. If this does not work, then
	 * try waiting for the element to be displayed.
	 * 
	 * @param sLocator - How to locate the element
	 * @return - true if element appears before timeout else false
	 */
	public boolean isWaitForEnabledElement(String sLocator)
	{
		return isWaitForEnabledElement(driver, sLocator);
	}

	/**
	 * Waits for an element to appear & be enabled.<BR>
	 * <BR>
	 * <B>Note: </B>The selenium method isEnabled() returns true in most cases. If this does not work, then
	 * try waiting for the element to be displayed.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @return - true if element appears before timeout else false
	 */
	public static boolean isWaitForEnabledElement(WebDriver driver, String sLocator)
	{
		return isWaitForElement(driver, sLocator, true);
	}

	/**
	 * Waits for an element to appear.<BR>
	 * <BR>
	 * <B>Note: </B>The selenium method isEnabled() returns true in most cases. If this does not work, then
	 * try waiting for the element to be displayed.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @param bEnabled - Wait until element is enabled
	 * @return - true if element appears before timeout else false
	 */
	public static boolean isWaitForElement(WebDriver driver, String sLocator, boolean bEnabled)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			// Try to find the element
			WebElement element = findElement(driver, sLocator, false);

			// Wait until element is enabled?
			if (bEnabled)
			{
				if (isElementEnabled(element))
					return true;
			}
			else
			{
				return true;
			}

			sleep(getPollInterval());
		}

		Logs.log.warn("The locator ('" + sLocator + "') did not appear before Timeout occurred");
		return false;
	}

	/**
	 * Waits for the element to appear with specified attribute & value
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sAttributeToCheck - Attribute on the Element to look for
	 * @param sExpectedAttributeValue - Value of the Attribute
	 * @return - true if element appears with expected attribute value before timeout else false
	 */
	public static boolean isWaitForAttributeValue(WebDriver driver, String sLocator,
			String sAttributeToCheck, String sExpectedAttributeValue)
	{
		WebElement element;

		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			element = findElement(driver, sLocator);
			if (element != null && sExpectedAttributeValue.equals(element.getAttribute(sAttributeToCheck)))
				return true;

			sleep(getPollInterval());
		}

		// Attribute value never became expected value before timeout
		Logs.log.warn("Attribute ('" + sAttributeToCheck + "') did not get the value ('"
				+ sExpectedAttributeValue + "') for the locator ('" + sLocator + "') before Timeout occurred");
		return false;
	}

	/**
	 * Based on the sAJAX_type waits for the AJAX to complete loading.<BR>
	 * <BR>
	 * <B>Note:</B> Use this method if no instantiated Framework class available.
	 * 
	 * @param driver
	 * @param sAJAX_type - AJAX type to use (JQUERY, DOJO, Custom)
	 * @return true if the page completes loading before timeout occurs else false
	 */
	public static boolean isPageLoadedAJAX(WebDriver driver, String sAJAX_type)
	{
		// Instantiate Framework
		Framework f = new Framework(driver);

		// Set the AJAX type you need
		f.setAJAX_type(sAJAX_type);

		// Wait for the page to be loaded by using AJAX check
		return f.isPageLoaded();
	}

	/**
	 * Based on the sAJAX_type waits for the AJAX to complete loading.
	 * 
	 * @return true if the page completes loading before timeout occurs else false
	 */
	public boolean isPageLoaded()
	{
		Boolean bResult = false;
		String sJS;

		// Select the right javascript command to test that all AJAX requests
		// are complete
		if (sAJAX_type == JQUERY)
			sJS = sJQuery_AJAX_Complete;
		else if (sAJAX_type == DOJO)
			sJS = sDojo_AJAX_Complete;
		else
			return isPageLoaded(sXpath_AJAX_Custom_Complete);

		/*
		 * Wait for the ajaxConcurrentCallsCount to be <= 0 which means the page has completed loading
		 */
		try
		{
			ElapsedTime e = new ElapsedTime();
			while (!e.isTimeout())
			{
				bResult = (Boolean) JS_Util.execute(driver, sJS, null);
				if (bResult.booleanValue())
					return true;

				sleep(getPollInterval());
			}
		}
		catch (Exception ex)
		{
		}

		Logs.log.warn("Timeout occurred waiting for AJAX page to complete loading");
		return false;
	}

	/**
	 * Waits for the AJAX to complete loading <BR>
	 * <BR>
	 * NOTE: Use only if bPageLoaded with no parameters fails
	 * 
	 * @param sXpath - xpath of element to wait for
	 * @return true if the page completes loading before timeout occurs else false
	 */
	public boolean isPageLoaded(String sXpath)
	{
		// Wait for search to complete
		if (!isWaitForEnabledElement(sXpath))
		{
			return false;
		}

		return true;
	}

	/**
	 * Checks if an element exists on the page
	 * 
	 * @param sLocator - How to locate the element on the page
	 * @return true if the element exists on the page
	 */
	public boolean isElementExists(String sLocator)
	{
		return isElementExists(driver, sLocator);
	}

	/**
	 * Checks if an element exists on the page.<BR>
	 * <BR>
	 * <B>Note: </B> Does not check if element is enabled.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 * @return true if the element exists on the page
	 */
	public static boolean isElementExists(WebDriver driver, String sLocator)
	{
		return isElementExists(driver, sLocator, false);
	}

	/**
	 * Checks if an element exists on the page and enabled if necessary
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 * @param bEnabled - true if element must be enabled
	 * @return true if the element exists on the page (and enabled if requested)
	 */
	public static boolean isElementExists(WebDriver driver, String sLocator, boolean bEnabled)
	{
		WebElement element = findElement(driver, sLocator, false);
		if (element == null)
			return false;

		// Should we check if it is enabled?
		if (bEnabled)
			return isElementEnabled(element);
		else
			return true;
	}

	/**
	 * Switches to the 1st pop-up window (and stores handle) or back to the stored pop-up window handle.<BR>
	 * <BR>
	 * <B>Note: </B> Call disposePopupWindowHandle method once done with the pop-up
	 * 
	 * @return true if successful
	 */
	public boolean switchToPopupWindow()
	{
		try
		{
			// If already working with a pop-up
			if (sPopupWindowHandle != null)
			{
				driver.switchTo().window(sPopupWindowHandle);
				return true;
			}

			// Get all the window handles
			String[] openWindows = driver.getWindowHandles().toArray(new String[0]);
			for (int i = 0; i < openWindows.length; i++)
			{
				// Return 1st window handle that is not the current window handle
				if (!sMainWindowHandle.equals(openWindows[i]))
				{
					driver.switchTo().window(openWindows[i]);
					// Store the handle for possible use later
					sPopupWindowHandle = openWindows[i];
					return true;
				}
			}
		}
		catch (Exception ex)
		{
		}

		// There was only 1 browser window open or pop-up handle invalid (due to it being closed)
		return false;
	}

	/**
	 * Switches back to the Main Window (at time of instantiation)
	 * 
	 * @return true if successful
	 */
	public boolean switchToMainWindow()
	{
		try
		{
			driver.switchTo().window(sMainWindowHandle);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Disposes of the Pop-up Window Handle
	 */
	public void disposePopupWindowHandle()
	{
		sPopupWindowHandle = null;
	}

	/**
	 * Wait until a pop-up window appears
	 * 
	 * @return true if successful
	 */
	public boolean waitForPopup()
	{
		return waitForPopup(1);
	}

	/**
	 * Wait until at least the specified number of pop-ups appear or timeout occurs
	 * 
	 * @param nWindows - Number of Pop-up windows to wait for
	 * @return true if at least the number of pop-up windows appear before timeout else false
	 */
	public boolean waitForPopup(int nWindows)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			String[] openWindows = driver.getWindowHandles().toArray(new String[0]);
			if (openWindows.length > nWindows)
				return true;

			sleep(getPollInterval());
		}

		// No pop-up window appeared within the timeout value
		return false;
	}

	/**
	 * Wraps the Thread.sleep method
	 * 
	 * @param nMilliSeconds - MilliSeconds to pause for
	 */
	public static void sleep(int nMilliSeconds)
	{
		try
		{
			Thread.sleep(nMilliSeconds);
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * Selects value from drop down using visible text
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Visible text of option to select
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelect(WebElement dropdown, String sDropDownName, String sValue)
	{
		dropDownSelection(dropdown, sDropDownName, Selection.VisibleText, sValue, true);
	}

	/**
	 * If drop down exists, then Selects value from drop down using visible text
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Visible text of option to select
	 * @return true if no errors else false
	 */
	public static boolean dropDownSelectTry(WebElement dropdown, String sDropDownName, String sValue)
	{
		try
		{
			dropDownSelection(dropdown, sDropDownName, Selection.VisibleText, sValue, LogErrorLevel.NONE);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects value from drop down using value attribute
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Use Value Attribute of option to select
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelectByValue(WebElement dropdown, String sDropDownName, String sValue)
	{
		dropDownSelection(dropdown, sDropDownName, Selection.ValueHTML, sValue, true);
	}

	/**
	 * Selects value from drop down using value attribute
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Use Value Attribute of option to select
	 * @return true if no errors else false
	 */
	public static boolean dropDownSelectByValueTry(WebElement dropdown, String sDropDownName, String sValue)
	{
		try
		{
			dropDownSelection(dropdown, sDropDownName, Selection.ValueHTML, sValue, LogErrorLevel.NONE);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects value from drop down using zero based index
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Select option that has this index (zero based)
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelectByIndex(WebElement dropdown, String sDropDownName, String sValue)
	{
		dropDownSelection(dropdown, sDropDownName, Selection.Index, sValue, true);
	}

	/**
	 * Selects value from drop down using zero based index
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Select option that has this index (zero based)
	 * @return true if no errors else false
	 */
	public static boolean dropDownSelectByIndexTry(WebElement dropdown, String sDropDownName, String sValue)
	{
		try
		{
			dropDownSelection(dropdown, sDropDownName, Selection.Index, sValue, LogErrorLevel.NONE);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects value from drop down using a regular expression to find the 1st matching option.
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sRegEx - Regular Expression to find 1st match of drop down options
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelectByRegEx(WebElement dropdown, String sDropDownName, String sRegEx)
	{
		dropDownSelection(dropdown, sDropDownName, Selection.RegEx, sRegEx, true);
	}

	/**
	 * If able to find an option (visible text) matching the regular expression, then selects value from drop
	 * down.<BR>
	 * <BR>
	 * <B>Note: </B> To find a partial match of a string use ".*" + Option + ".*"<BR>
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sRegEx - Regular Expression to find 1st match of drop down options
	 * @return true if no errors else false
	 */
	public static boolean dropDownSelectByRegExTry(WebElement dropdown, String sDropDownName, String sRegEx)
	{
		try
		{
			dropDownSelection(dropdown, sDropDownName, Selection.RegEx, sRegEx, LogErrorLevel.NONE);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Goes through a list of visible text options and attempting to select the specified option from the drop
	 * down and returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList)
	{
		return dropDownSelectionMultipleTry(dropdown, sDropDownName, sOptionList, 0);
	}

	/**
	 * Goes through a list of value options and attempting to select the specified option from the drop down
	 * and returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectByValueMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList)
	{
		return dropDownSelectionMultipleTry(dropdown, sDropDownName, sOptionList, 1);
	}

	/**
	 * Goes through a list of index options and attempting to select the specified option from the drop down
	 * and returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectByIndexMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList)
	{
		return dropDownSelectionMultipleTry(dropdown, sDropDownName, sOptionList, 2);
	}

	/**
	 * Goes through a list (of RegEx) and attempting to select the specified option from the drop down and
	 * returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectByRegExMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList)
	{
		return dropDownSelectionMultipleTry(dropdown, sDropDownName, sOptionList, 3);
	}

	/**
	 * Goes through a list and attempting to select the specified option from the drop down and
	 * returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @param nOption How to locate the option. (1 - By Value, 2 - By Index, 3 - Regular Expression, Default -
	 *            Visible Text)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectionMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList, int nOption)
	{
		for (int i = 0; i < sOptionList.length; i++)
		{
			String sLogName = sDropDownName + "(try '" + sOptionList[i] + "')";
			if (nOption == 1)
			{
				// Able to select this option?
				if (dropDownSelectByValueTry(dropdown, sLogName, sOptionList[i]))
					return true;
			}
			else if (nOption == 2)
			{
				// Able to select this option?
				if (dropDownSelectByIndexTry(dropdown, sLogName, sOptionList[i]))
					return true;
			}
			else if (nOption == 3)
			{
				// Able to select this option?
				if (dropDownSelectByRegExTry(dropdown, sLogName, sOptionList[i]))
					return true;
			}
			else
			{
				// Able to select this option?
				if (dropDownSelectTry(dropdown, sLogName, sOptionList[i]))
					return true;
			}
		}

		// Could not find any of the options to select
		return false;
	}

	/**
	 * Selects value from drop down using specified option
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param _option - How to locate the option. (Value (HTML), Index, Regular Expression, Default - Visible
	 *            Text)
	 * @param sValue - Option to select
	 * @param bLogError - if true then log error else log warning
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelection(WebElement dropdown, String sDropDownName, Selection _option,
			String sValue, boolean bLogError)
	{
		dropDownSelection(dropdown, sDropDownName, _option, sValue, LogErrorLevel.convert(bLogError));
	}

	/**
	 * Selects value from drop down using specified option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 2) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 3) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param _option - How to locate the option. (Value (HTML), Index, Regular Expression, Default - Visible
	 *            Text)
	 * @param sValue - Option to select
	 * @param level - Control error logging written
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	private static void dropDownSelection(WebElement dropdown, String sDropDownName, Selection _option,
			String sValue, LogErrorLevel level)
	{
		try
		{
			if (!dropdown.isEnabled())
			{
				throw new ElementNotEnabledException("");
			}

			if (!dropdown.isDisplayed())
			{
				throw new ElementNotDisplayedException("");
			}

			Select value = new Select(dropdown);
			if (_option == Selection.ValueHTML)
			{
				value.selectByValue(sValue);
				Logs.log.info("Successfully selected by value '" + sValue + "' from the drop down ('"
						+ sDropDownName + "')");
			}
			else if (_option == Selection.Index)
			{
				value.selectByIndex(Integer.parseInt(sValue));
				Logs.log.info("Successfully selected by index '" + sValue + "' from the drop down ('"
						+ sDropDownName + "')");
			}
			else if (_option == Selection.RegEx)
			{
				int nIndex = 0;
				List<WebElement> availableOptions = value.getOptions();
				for (WebElement option : availableOptions)
				{
					// Select option if regular expression matches
					if (option.getText().matches(sValue))
					{
						value.selectByIndex(nIndex);
						Logs.log.info("Successfully selected by index '" + nIndex + "' by using RegEx '"
								+ sValue + "' to match from the drop down ('" + sDropDownName + "')");
						return;
					}

					nIndex++;
				}

				throw new DropDownPartialMatchException("");
			}
			else
			{
				value.selectByVisibleText(sValue);
				Logs.log.info("Successfully selected '" + sValue + "' from the drop down ('" + sDropDownName
						+ "')");
			}
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sDropDownName + "') was not enabled";
			Logs.logWarnError(level, new ElementNotEnabledException(sError));
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sDropDownName + "') was not displayed";
			Logs.logWarnError(level, new ElementNotDisplayedException(sError));
		}
		catch (NoSuchElementException nsee)
		{
			String sError = "Could not find '" + sValue + "' in the drop down ('" + sDropDownName + "')";
			Logs.logWarnError(level, new DropDownSelectionException(sError));
		}
		catch (NumberFormatException nfe)
		{
			String sError = "Could not find index '" + sValue + "' in the drop down ('" + sDropDownName
					+ "')";
			Logs.logWarnError(level, new DropDownIndexException(sError));
		}
		catch (DropDownPartialMatchException ddpme)
		{
			String sError = "Could not find a partial match using the regular expression '" + sValue
					+ "' in the drop down ('" + sDropDownName + "')";
			Logs.logWarnError(level, new DropDownPartialMatchException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Drop down ('" + sDropDownName
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new DropDownNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			String sError = "Could not find drop down ('" + sDropDownName + "') due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new DropDownNoSuchElementException(sError));
		}
	}

	/**
	 * Clicks specified element with logging
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws ClickNoSuchElementException if could not find the element
	 */
	public static void click(WebElement element, String sElementName)
	{
		click(element, sElementName, LogErrorLevel.ERROR);
	}

	/**
	 * If element exists, then Clicks specified element
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @return true if no errors else false
	 */
	public static boolean clickTry(WebElement element, String sElementName)
	{
		try
		{
			click(element, sElementName, LogErrorLevel.NONE);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Clicks specified element with logging
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @param bLogError - if true then log error else log warning
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws ClickNoSuchElementException if could not find the element
	 */
	public static void click(WebElement element, String sElementName, boolean bLogError)
	{
		click(element, sElementName, LogErrorLevel.convert(bLogError));
	}

	/**
	 * Clicks specified element with logging<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 2) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 3) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @param level - Control error logging written
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws ClickNoSuchElementException if could not find the element
	 */
	private static void click(WebElement element, String sElementName, LogErrorLevel level)
	{
		try
		{
			if (!element.isEnabled())
			{
				throw new ElementNotEnabledException("");
			}

			if (!element.isDisplayed())
			{
				throw new ElementNotDisplayedException("");
			}

			element.click();
			Logs.log.info("Clicked '" + sElementName + "' successfully");
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sElementName + "') was not enabled";
			Logs.logWarnError(level, new ElementNotEnabledException(sError));
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sElementName + "') was not displayed";
			Logs.logWarnError(level, new ElementNotDisplayedException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new ClickNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new ClickNoSuchElementException(sError));
		}
	}

	/**
	 * Clears & enters value into specified field<BR>
	 * <BR>
	 * <B>Note: </B> Field must exist.<BR>
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param sInputValue - Value to input into the field
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws EnterFieldNoSuchElementException if could not find the element
	 */
	public static void enterField(WebElement element, String sElementName, String sInputValue)
	{
		enterField(element, sElementName, sInputValue, true, LogErrorLevel.ERROR);
	}

	/**
	 * If the field exists, then clear & enters value into the field ELSE logs warning that field could not be
	 * found.<BR>
	 * <BR>
	 * <B>Note: </B> Use this method if it is acceptable to skip fields that are missing (or do not always
	 * appear.)<BR>
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param sInputValue - Value to input into the field
	 * @return true if no errors else false
	 */
	public static boolean enterFieldTry(WebElement element, String sElementName, String sInputValue)
	{
		try
		{
			enterField(element, sElementName, sInputValue, true, LogErrorLevel.NONE);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Enters value into specified field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Field must exist.<BR>
	 * 2) Does not clear field before entry.<BR>
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param sInputValue - Value to input into the field
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws EnterFieldNoSuchElementException if could not find the element
	 */
	public static void onlyEnterField(WebElement element, String sElementName, String sInputValue)
	{
		enterField(element, sElementName, sInputValue, false, LogErrorLevel.ERROR);
	}

	/**
	 * Clears & enters value into specified field
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param sInputValue - Value to input into the field
	 * @param bClearField - true to clear field before entering value
	 * @param bLogError - if true then log error else log warning
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws EnterFieldNoSuchElementException if could not find the element
	 */
	public static void enterField(WebElement element, String sElementName, String sInputValue,
			boolean bClearField, boolean bLogError)
	{
		enterField(element, sElementName, sInputValue, bClearField, LogErrorLevel.convert(bLogError));
	}

	/**
	 * Clears & enters value into specified field<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 2) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 3) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param sInputValue - Value to input into the field
	 * @param bClearField - true to clear field before entering value
	 * @param level - Control error logging written
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws EnterFieldNoSuchElementException if could not find the element
	 */
	private static void enterField(WebElement element, String sElementName, String sInputValue,
			boolean bClearField, LogErrorLevel level)
	{
		try
		{
			if (!element.isEnabled())
			{
				throw new ElementNotEnabledException("");
			}

			if (!element.isDisplayed())
			{
				throw new ElementNotDisplayedException("");
			}

			String sLogCleared = "";
			if (bClearField)
			{
				element.clear();
				sLogCleared = "cleared & ";
			}

			element.sendKeys(sInputValue);
			Logs.log.info("Successfully " + sLogCleared + "entered the value '" + sInputValue + "' into '"
					+ sElementName + "'");
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sElementName + "') was not enabled";
			Logs.logWarnError(level, new ElementNotEnabledException(sError));
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sElementName + "') was not displayed";
			Logs.logWarnError(level, new ElementNotDisplayedException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new EnterFieldNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new EnterFieldNoSuchElementException(sError));
		}
	}

	/**
	 * Selects check box if unselected<BR>
	 * <BR>
	 * <B>Note: </B> If (bVerifyInitialState == true) then user should catch possible
	 * GenericErrorDetectedException and throw a specific exception for their test.<BR>
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state and ready
	 */
	public static void check(WebElement element, String sElementName, boolean bVerifyInitialState)
	{
		check(element, sElementName, bVerifyInitialState, true);
	}

	/**
	 * If the field exists, then Selects check box if unselected
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state and ready
	 * @return true if no errors else false
	 */
	public static boolean checkTry(WebElement element, String sElementName, boolean bVerifyInitialState)
	{
		try
		{
			check(element, sElementName, bVerifyInitialState, LogErrorLevel.NONE);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects check box if unselected
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state and ready
	 * @param bLogError - if true then log error else log warning
	 * @throws CheckBoxWrongStateException if element is not initially unselected
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	public static void check(WebElement element, String sElementName, boolean bVerifyInitialState,
			boolean bLogError)
	{
		check(element, sElementName, bVerifyInitialState, LogErrorLevel.convert(bLogError));
	}

	/**
	 * Selects check box if unselected<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 2) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 3) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state and ready
	 * @param level - Control error logging written
	 * @throws CheckBoxWrongStateException if element is not initially unselected
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	private static void check(WebElement element, String sElementName, boolean bVerifyInitialState,
			LogErrorLevel level)
	{
		try
		{
			// Does user want to enforce state of unselected before selecting check box
			if (bVerifyInitialState)
			{
				if (element.isSelected())
					throw new CheckBoxWrongStateException("");

				if (!element.isEnabled())
					throw new ElementNotEnabledException("");

				if (!element.isDisplayed())
					throw new ElementNotDisplayedException("");
			}

			// Click check box only if it is not already selected
			if (!element.isSelected())
			{
				element.click();
				Logs.log.info("Successfully selected check box for '" + sElementName + "'");
			}
			else
			{
				Logs.log.info("Check box for '" + sElementName + "' was already selected");
			}
		}
		catch (CheckBoxWrongStateException cbwse)
		{
			String sError = "Required initial state of the check box '" + sElementName
					+ "' was not correct for the check operation";
			Logs.logWarnError(level, new CheckBoxWrongStateException(sError));
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sElementName + "') was not enabled";
			Logs.logWarnError(level, new ElementNotEnabledException(sError));
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sElementName + "') was not displayed";
			Logs.logWarnError(level, new ElementNotDisplayedException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
	}

	/**
	 * Unselects check box if selected<BR>
	 * <BR>
	 * <B>Note: </B> If (bVerifyInitialState == true) then user should catch possible
	 * GenericErrorDetectedException and throw a specific exception for their test.<BR>
	 * 
	 * @param element - Check box to unselect
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state and ready
	 * @throws CheckBoxWrongStateException if element is not initially selected
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	public static void uncheck(WebElement element, String sElementName, boolean bVerifyInitialState)
	{
		uncheck(element, sElementName, bVerifyInitialState, true);
	}

	/**
	 * If the field exists, then Unselects check box if selected
	 * 
	 * @param element - Check box to unselect
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state and ready
	 * @return true if no errors else false
	 */
	public static boolean uncheckTry(WebElement element, String sElementName, boolean bVerifyInitialState)
	{
		try
		{
			uncheck(element, sElementName, bVerifyInitialState, LogErrorLevel.NONE);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Unselects check box if selected
	 * 
	 * @param element - Check box to unselect
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state and ready
	 * @param bLogError - if true then log error else log warning
	 * @throws CheckBoxWrongStateException if element is not initially selected
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	public static void uncheck(WebElement element, String sElementName, boolean bVerifyInitialState,
			boolean bLogError)
	{
		uncheck(element, sElementName, bVerifyInitialState, LogErrorLevel.convert(bLogError));
	}

	/**
	 * Unselects check box if selected<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 2) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 3) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * @param element - Check box to unselect
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state and ready
	 * @param level - Control error logging written
	 * @throws CheckBoxWrongStateException if element is not initially selected
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	private static void uncheck(WebElement element, String sElementName, boolean bVerifyInitialState,
			LogErrorLevel level)
	{
		try
		{
			// Does user want to enforce state of selected before unselecting check box
			if (bVerifyInitialState)
			{
				if (!element.isSelected())
					throw new CheckBoxWrongStateException("");

				if (!element.isEnabled())
					throw new ElementNotEnabledException("");

				if (!element.isDisplayed())
					throw new ElementNotDisplayedException("");
			}

			// Click check box only if it is not already selected
			if (element.isSelected())
			{
				element.click();
				Logs.log.info("Successfully unselected check box for '" + sElementName + "'");
			}
			else
			{
				Logs.log.info("Check box for '" + sElementName + "' was already unselected");
			}
		}
		catch (CheckBoxWrongStateException cbwse)
		{
			String sError = "Required initial state of the check box '" + sElementName
					+ "' was not correct for the uncheck operation";
			Logs.logWarnError(level, new CheckBoxWrongStateException(sError));
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sElementName + "') was not enabled";
			Logs.logWarnError(level, new ElementNotEnabledException(sError));
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sElementName + "') was not displayed";
			Logs.logWarnError(level, new ElementNotDisplayedException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
	}

	/**
	 * Goes to URL if site is up.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This function prevents the waiting indefinitely issue with WebDriver.get(...)<BR>
	 * 2) If 'javax.net.ssl.SSLProtocolException: handshake alert: unrecognized_name' occurs, then issue is
	 * with server. However, you can bypass the issue by launching with following system
	 * property:<BR>
	 * -Djsse.enableSNIExtension=false<BR>
	 * 
	 * @param driver
	 * @param sUrl - URL to launch
	 */
	public static void get(WebDriver driver, String sUrl)
	{
		try
		{
			/*
			 * Need to determine if secure site or not
			 */
			boolean bSecureSite = false;
			if (sUrl.length() > 5)
			{
				String sSecurePrefix = sUrl.substring(0, 5);
				if (sSecurePrefix.equalsIgnoreCase("https"))
					bSecureSite = true;
			}

			/*
			 * Connect to the site and see if it is alive before using WebDriver.get(...)
			 */
			URL url = new URL(sUrl);
			if (bSecureSite)
			{
				try
				{
					HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
					httpsCon.setConnectTimeout(getTimeoutInMilliseconds());
					httpsCon.connect();
				}
				catch (SSLHandshakeException ex)
				{
					// We don't care that the certificate is invalid only that the site is responding
				}
			}
			else
			{
				URLConnection urlConn = url.openConnection();
				urlConn.setConnectTimeout(getTimeoutInMilliseconds());
				urlConn.connect();
			}

			driver.get(sUrl);
		}
		catch (Exception ex)
		{
			String sError = "The site ('" + sUrl
					+ "') is taking too long to respond or URL does not exist.  Error:  ";
			Logs.logError(new GetException(sError + ex));
		}
	}

	/**
	 * If the page is displayed try to click up to the specified times to get to next screen.
	 * 
	 * @param driver
	 * @param sURL - Page must contain this URL
	 * @param sComplete - xpath/id to determine when page is completed loading
	 * @param sType - The way to determine page is complete
	 * @param bWaitForActionToStart - True if need to wait for action to start
	 * @param sActionStartedCheck - xpath/id to determine if action has started
	 * @param bJS - true to execute JavaScript else normal click
	 * @param sLocatorOrJS - How to locate the element (button, link, etc.)
	 * @param sLogDetails - What to put in the log
	 * @param nTries - How many times to try and click to get to next page.
	 */
	public static void clickAtPage(WebDriver driver, String sURL, String sComplete, String sType,
			boolean bWaitForActionToStart, String sActionStartedCheck, boolean bJS, String sLocatorOrJS,
			String sLogDetails, int nTries)
	{
		for (int i = 0; i < nTries; i++)
		{
			/*
			 * Only try to click if still at specific page.
			 */
			if (driver.getCurrentUrl().contains(sURL))
			{
				/*
				 * Find the element, click & wait for page to complete loading.
				 */
				try
				{
					Framework selenium = new Framework(driver);

					// Wait for page to load
					selenium.setXpath_AJAX_Complete(sComplete);
					selenium.setAJAX_type(sType);
					if (!selenium.isPageLoaded())
						Logs.log.warn("AJAX Timeout before '" + sLogDetails + "' still going to continue");

					if (bJS)
					{
						JS_Util.execute(driver, sLocatorOrJS);
						Logs.log.info("Javascript Executed for '" + sLogDetails + "'");
					}
					else
					{
						WebElement element = driver.findElement(locatedBy(sLocatorOrJS));
						click(element, sLogDetails);
					}

					/*
					 * If there is a delay between the click and the action taking place, then we need to do a
					 * check before the AJAX complete check.
					 * 
					 * Note: If this is not done, then the AJAX complete check will success but the page will
					 * not be complete and this will cause intermittent timing issues.
					 */
					if (bWaitForActionToStart)
					{
						if (!isWaitForEnabledElement(driver, sActionStartedCheck))
							Logs.log.warn("Timeout waiting for action '" + sLogDetails
									+ "' to start still going to continue");
					}

					// Wait for page to load
					if (!selenium.isPageLoaded())
						Logs.log.warn("AJAX Timeout after '" + sLogDetails + "' still going to continue");
				}
				catch (Exception ex)
				{
				}
			}
			else
				return;
		}
	}

	/**
	 * Returns the WebElement for the specified sLocator.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param bLog - true for logging
	 * @return null if cannot find
	 */
	public static WebElement findElement(WebDriver driver, String sLocator, boolean bLog)
	{
		WebElement element = null;
		try
		{
			element = driver.findElement(locatedBy(sLocator));
		}
		catch (Exception ex)
		{
			if (bLog)
				Logs.log.warn("Could not find element using:  " + sLocator);
		}

		return element;
	}

	/**
	 * Returns the WebElement for the specified sLocator.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @return null if cannot find
	 */
	public static WebElement findElement(WebDriver driver, String sLocator)
	{
		return findElement(driver, sLocator, true);
	}

	/**
	 * Waits until the specified locator returns a matching WebElement.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @return null if no matching WebElement upon reaching timeout
	 */
	public static WebElement findElementAJAX(WebDriver driver, String sLocator)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			WebElement element = findElement(driver, sLocator, false);
			if (element != null)
				return element;

			sleep(getPollInterval());
		}

		Logs.log.warn("The locator ('" + sLocator + "') did not return any element before Timeout occurred");
		return null;
	}

	/**
	 * Returns the WebElements for the specified sLocator.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Elements
	 * @param bLog - true for logging
	 * @return List&lt;WebElement&gt; (could be null)
	 */
	public static List<WebElement> findElements(WebDriver driver, String sLocator, boolean bLog)
	{
		List<WebElement> elements = null;
		try
		{
			elements = driver.findElements(locatedBy(sLocator));
		}
		catch (Exception ex)
		{
			if (bLog)
				Logs.log.warn("Could not any find elements using:  " + sLocator);
		}

		return elements;
	}

	/**
	 * Returns the WebElements for the specified sLocator.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Elements
	 * @return null if cannot find
	 */
	public static List<WebElement> findElements(WebDriver driver, String sLocator)
	{
		return findElements(driver, sLocator, true);
	}

	/**
	 * Waits until the specified locator returns a matching WebElements.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Elements
	 * @return null if no matching WebElements upon reaching timeout
	 */
	public static List<WebElement> findElementsAJAX(WebDriver driver, String sLocator)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			List<WebElement> elements = findElements(driver, sLocator, false);
			if (elements != null)
				return elements;

			sleep(getPollInterval());
		}

		Logs.log.warn("The locator ('" + sLocator + "') did not return any elements before Timeout occurred");
		return null;
	}

	/**
	 * Waits until the specified locator returns a matching WebElements of at least the specified size (or
	 * timeout occurs.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If minimum number of elements less than 1, then value is ignored<BR>
	 * 2) If timeout occurs, then the latest list is returned which may not have the minimum number of
	 * elements<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Elements
	 * @param atleast - Wait until there are at least this number of elements in the list
	 * @return non-null
	 */
	public static List<WebElement> findElementsAJAX(WebDriver driver, String sLocator, int atleast)
	{
		List<WebElement> elements = null;

		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			elements = findElements(driver, sLocator, false);
			if (elements != null)
			{
				if (atleast <= 0 || (atleast > 0 && elements.size() >= atleast))
					return elements;
			}

			sleep(getPollInterval());
		}

		// The elements list can be populated but not necessarily with the minimum number of elements
		// specified. This is either because there can never be the specified number elements or the
		// application did not finish loading before timeout.
		if (elements != null)
			return elements;
		else
			return new ArrayList<WebElement>();
	}

	/**
	 * Gets the text for the element.<BR>
	 * <BR>
	 * <B>Note: </B> Text Visible to user<BR>
	 * 
	 * @param element - element to get text from
	 * @param sLogDetails - element name to log
	 * @param bLog - true for logging
	 * @return null if cannot find element and/or text
	 */
	public static String getText(WebElement element, String sLogDetails, boolean bLog)
	{
		try
		{
			String sText = element.getText();
			if (bLog)
				Logs.log.info("Element '" + sLogDetails + "' had the following text:  " + sText);

			return sText;
		}
		catch (Exception ex)
		{
			if (bLog)
				Logs.log.warn("Could not get text for '" + sLogDetails + "'");

			return null;
		}
	}

	/**
	 * Gets the text for the element. (No logging)<BR>
	 * <BR>
	 * <B>Note: </B> Text Visible to user<BR>
	 * 
	 * @param element - element to get text from
	 * @return null if cannot find element and/or text
	 */
	public static String getText(WebElement element)
	{
		return getText(element, "", false);
	}

	/**
	 * Gets the text for the element (with logging.)<BR>
	 * <BR>
	 * <B>Note: </B> Text Visible to user<BR>
	 * 
	 * @param element - element to get text from
	 * @param sLogDetails - element name to log
	 * @return null if cannot find element and/or text
	 */
	public static String getText(WebElement element, String sLogDetails)
	{
		return getText(element, sLogDetails, true);
	}

	/**
	 * Gets the text regardless whether visible or not (without logging) using the Cobra HTML parser<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * If there are no matches check that the xpath (which is case sensitive) is in the same case as the
	 * HTML source (use driver.getPageSource() to write the actual source to a file/log.) <BR>
	 * <BR>
	 * <B>Related function:</B><BR>
	 * Misc.xpathChangeCase(String sXpath, String sDelimiter, boolean bToUppercase) to be
	 * used in conjunction with this function to get correct case for xpath.
	 * 
	 * @param driver
	 * @param sXpath - xpath to node
	 * @return text of node if exists or null
	 */
	public static String getText(WebDriver driver, String sXpath)
	{
		try
		{
			HTML html = new HTML(driver);
			return html.getNodeValue(sXpath, null);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Get attribute using Cobra HTML parser instead of Selenium.<BR>
	 * <BR>
	 * <B>Note: </B> If there are no matches check that the xpath (which is case sensitive) is in the same
	 * case as the HTML source (use driver.getPageSource() to write the actual source to a file/log.)<BR>
	 * 
	 * @param driver
	 * @param sXpath - xpath to node
	 * @param sAttribute - attribute to get
	 * @return null if cannot find
	 */
	public static String getAttribute(WebDriver driver, String sXpath, String sAttribute)
	{
		try
		{
			HTML html = new HTML(driver);
			return html.getAttribute(sXpath, sAttribute);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Waits for the URL to change.<BR>
	 * Note: You need to save the initial URL before doing your action & then calling this method.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * <BR>
	 * sInitialValue = driver.getCurrentUrl();<BR>
	 * button.click();<BR>
	 * Framework.bWaitForURLtoChange(driver, sInitialValue, false);<BR>
	 * 
	 * @param driver
	 * @param sInitialValue - Complete Initial URL or Part of Initial URL that will is expected to change
	 * @param bPartial - true waits until URL no longer contains sInitialValue, false waits until any
	 *            difference in URL
	 * @return true if URL changed before timeout else false
	 */
	public static boolean isWaitForURLtoChange(WebDriver driver, String sInitialValue, boolean bPartial)
	{
		/*
		 * Just in case value is null (due to driver.getCurrentUrl returning null when passed to this method)
		 */
		String sValue = sInitialValue;
		if (sValue == null)
			sValue = "";

		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			/*
			 * Just in case value is null (due to driver.getCurrentUrl returning null.)
			 */
			String sCurrentURL;
			try
			{
				sCurrentURL = driver.getCurrentUrl();
			}
			catch (Exception ex)
			{
				sCurrentURL = "";
			}

			if (bPartial)
			{
				// Check that current URL does not contain initial value any more
				if (!sCurrentURL.contains(sInitialValue))
					return true;
			}
			else
			{
				// Check for any difference in URL
				if (!sCurrentURL.equalsIgnoreCase(sInitialValue))
					return true;
			}

			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Clicks the element and waits for the URL to change (any part)
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 */
	public static void clickAndWait(WebElement element, String sElementName)
	{
		clickAndWait(element, sElementName, null, false, null);
	}

	/**
	 * Clicks the element and waits for the URL to change and contains specified page
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @param sPageToWaitFor - Waits until URL contains this value
	 */
	public static void clickAndWaitForURL(WebElement element, String sElementName, String sPageToWaitFor)
	{
		clickAndWait(element, sElementName, null, false, sPageToWaitFor);
	}

	/**
	 * Clicks the element and waits for the URL to change.<BR>
	 * <BR>
	 * <B>Note: </B> Use this if you need more control.<BR>
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @param sInitialValue - Part of Initial URL that will is expected to change<BR>
	 *            If null, then current URL is used.
	 * @param bPartial - true waits until URL no longer contains sInitialValue, false waits until any
	 *            difference in URL
	 * @param sValueToWaitFor - Waits until URL contains this value. If null or empty, then it does not wait.
	 */
	public static void clickAndWait(WebElement element, String sElementName, String sInitialValue,
			boolean bPartial, String sValueToWaitFor)
	{
		try
		{
			/*
			 * Trick to get real element that can return the WebDriver.
			 * Note:
			 * 1) If you use element directly it is a proxy and this cannot be cast to RemoteWebElement
			 * 2) If WebElement cannot be bound, then this will generate an exception
			 */
			WebElement realElement = element.findElement(By.xpath("."));

			// Get the WebDriver object from the real (bound) WebElement
			WebDriver useDriver = ((RemoteWebElement) realElement).getWrappedDriver();

			// Store the initial URL to be able to tell when the URL changes
			String sUseValue = sInitialValue;

			// If sInitialValue is not set, then use current URL
			if (sInitialValue == null)
				sUseValue = useDriver.getCurrentUrl();

			// Click the element
			click(element, sElementName);

			// Wait for the URL to change
			isWaitForURLtoChange(useDriver, sUseValue, bPartial);

			// Wait for specific URL?
			if (sValueToWaitFor == null || sValueToWaitFor.equals(""))
				return;
			else
				isWaitForURL(useDriver, sValueToWaitFor);
		}
		catch (ClickNoSuchElementException ex)
		{
			throw new ClickNoSuchElementException(ex.getMessage());
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(new ClickNoSuchElementException(sError));
		}
	}

	/**
	 * Finds the element using the specified locator & clicks with logging
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sElementName - Element Name to log
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws ClickNoSuchElementException if could not find the element
	 */
	public static void click(WebDriver driver, String sLocator, String sElementName)
	{
		click(findElement(driver, sLocator), sElementName, LogErrorLevel.ERROR);
	}

	/**
	 * Selects check box if unselected<BR>
	 * <BR>
	 * <B>Note: </B> If (bVerifyInitialState == true) then user should catch possible
	 * GenericErrorDetectedException and throw a specific exception for their test.<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state and ready
	 */
	public static void check(WebDriver driver, String sLocator, String sElementName,
			boolean bVerifyInitialState)
	{
		check(findElement(driver, sLocator), sElementName, bVerifyInitialState, true);
	}

	/**
	 * Unselects check box if selected<BR>
	 * <BR>
	 * <B>Note: </B> If (bVerifyInitialState == true) then user should catch possible
	 * GenericErrorDetectedException and throw a specific exception for their test.<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state and ready
	 * @throws CheckBoxWrongStateException if element is not initially selected
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	public static void uncheck(WebDriver driver, String sLocator, String sElementName,
			boolean bVerifyInitialState)
	{
		uncheck(findElement(driver, sLocator), sElementName, bVerifyInitialState, true);
	}

	/**
	 * Selects value from drop down using visible text
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Option to select
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelect(WebDriver driver, String sLocator, String sDropDownName, String sValue)
	{
		dropDownSelection(findElement(driver, sLocator), sDropDownName, Selection.VisibleText, sValue, true);
	}

	/**
	 * Selects value from drop down using zero based index
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Select option that has this index (zero based)
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelectByIndex(WebDriver driver, String sLocator, String sDropDownName,
			String sValue)
	{
		dropDownSelection(findElement(driver, sLocator), sDropDownName, Selection.Index, sValue, true);
	}

	/**
	 * Selects value from drop down using a regular expression to find the 1st matching option.
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sRegEx - Regular Expression to find 1st match of drop down options
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelectByRegEx(WebDriver driver, String sLocator, String sDropDownName,
			String sRegEx)
	{
		dropDownSelection(findElement(driver, sLocator), sDropDownName, Selection.RegEx, sRegEx, true);
	}

	/**
	 * Selects value from drop down using value attribute
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Use Value Attribute of option to select
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelectByValue(WebDriver driver, String sLocator, String sDropDownName,
			String sValue)
	{
		dropDownSelection(findElement(driver, sLocator), sDropDownName, Selection.ValueHTML, sValue, true);
	}

	/**
	 * Wrapper function to work with an alert
	 * 
	 * @param driver
	 * @param bAccept - true to click 'OK' (accept)
	 * @param bLog - true to write to log
	 * @return Message text of the Alert
	 * @throws NoAlertPresentException (Selenium)
	 */
	public static String workWithAlert(WebDriver driver, boolean bAccept, boolean bLog)
	{
		// Give focus to the alert
		Alert alert = driver.switchTo().alert();

		// Get the text to return later
		String sMessage = alert.getText();

		if (bLog)
			Logs.log.info("Switched to alert with message:  " + sMessage);

		// Accept or Dismiss the alert
		if (bAccept)
		{
			alert.accept();
			if (bLog)
				Logs.log.info("Cleared alert via 'Accept'");
		}
		else
		{
			alert.dismiss();
			if (bLog)
				Logs.log.info("Cleared alert via 'Dismiss'");
		}

		return sMessage;
	}

	/**
	 * Clicks 'OK' (accept) for the alert and returns the message text of the alert
	 * 
	 * @param driver
	 * @return Message text of the Alert
	 * @throws NoAlertPresentException (Selenium)
	 */
	public static String acceptAlert(WebDriver driver)
	{
		return workWithAlert(driver, true, true);
	}

	/**
	 * Dismisses the alert and returns the message text of the alert
	 * 
	 * @param driver
	 * @return Message text of the Alert
	 * @throws NoAlertPresentException (Selenium)
	 */
	public static String dismissAlert(WebDriver driver)
	{
		return workWithAlert(driver, false, true);
	}

	/**
	 * Clicks 'OK' (accept) for the alert and returns the message text of the alert.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If no alert (or any exception occurs) then null is returned<BR>
	 * 2) No logging
	 * 
	 * @param driver
	 * @return Message text of the Alert
	 */
	public static String acceptAlertTry(WebDriver driver)
	{
		try
		{
			return workWithAlert(driver, true, false);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Dismisses the alert and returns the message text of the alert.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If no alert (or any exception occurs) then null is returned<BR>
	 * 2) No Logging
	 * 
	 * @param driver
	 * @return Message text of the Alert
	 */
	public static String dismissAlertTry(WebDriver driver)
	{
		try
		{
			return workWithAlert(driver, false, false);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Waits for the attribute to change on the element.<BR>
	 * <BR>
	 * <B>Note:</B> You need to save the initial attribute value before doing your action & then calling this
	 * method.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * <BR>
	 * sInitialValue = "j_id30";<BR>
	 * WebElement button = Framework.findElement(driver, "javax.faces.ViewState");
	 * button.click();<BR>
	 * Framework.bWaitForAttributeToChange(button, "value", sInitialValue);<BR>
	 * 
	 * @param element - Element to perform check against
	 * @param sAttribute - Attribute to wait for changes
	 * @param sInitialValue - Initial value of attribute
	 * @return
	 */
	public static boolean isWaitForAttributeToChange(WebElement element, String sAttribute,
			String sInitialValue)
	{
		/*
		 * Just in case value is null
		 */
		String sValue = sInitialValue;
		if (sValue == null)
			sValue = "";

		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			/*
			 * Just in case value is null
			 */
			String sCurrentAttributeValue;
			try
			{
				sCurrentAttributeValue = element.getAttribute(sAttribute);
			}
			catch (Exception ex)
			{
				sCurrentAttributeValue = "";
			}

			// If attribute value is null set to be empty string
			if (sCurrentAttributeValue == null)
				sCurrentAttributeValue = "";

			// Check if attribute has changed
			if (!sCurrentAttributeValue.equalsIgnoreCase(sInitialValue))
				return true;

			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * This method waits for an attribute to change on an element. (If attribute does not change, then
	 * exception is thrown.)
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sAttribute - Attribute to wait for changes
	 * @param sInitialValue - Initial value of attribute
	 * @param sLogging - Element Name to log
	 */
	public static void waitForAttributeChange(WebDriver driver, String sLocator, String sAttribute,
			String sInitialValue, String sLogging)
	{
		if (!isWaitForAttributeToChange(findElement(driver, sLocator, false), sAttribute, sInitialValue))
		{
			String sError = "Timeout occurred waiting for:  " + sLogging;
			Logs.logError(new GenericActionNotCompleteException(sError));
		}
	}

	/**
	 * Gets the attribute value on the WebElement
	 * 
	 * @param element
	 * @param sAttribute - Attribute for which to get value
	 * @return null if value not set else attribute's current value
	 */
	public static String getAttribute(WebElement element, String sAttribute)
	{
		String sCurrentAttributeValue;
		try
		{
			sCurrentAttributeValue = element.getAttribute(sAttribute);
		}
		catch (Exception ex)
		{
			sCurrentAttributeValue = null;
		}

		return sCurrentAttributeValue;
	}

	/**
	 * Gets the attribute value on the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If <B>any exception</B> occurs while getting the information, then a retry will occur until max
	 * retries is reached<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to element
	 * @param sAttribute - Attribute for which to get value
	 * @param retries - Number of retries to get information
	 * @return empty string or attribute's current value
	 */
	public static String getAttribute(WebDriver driver, String sLocator, String sAttribute, int retries)
	{
		String sCurrentAttributeValue = null;

		int attempts = 0;
		boolean bStop = false;
		while (!bStop)
		{
			try
			{
				WebElement element = findElement(driver, sLocator, false);
				sCurrentAttributeValue = element.getAttribute(sAttribute);
				bStop = true;
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then stop loop
				if (attempts > retries)
					bStop = true;
				else
					sleep(getPollInterval());
			}
		}

		return Conversion.nonNull(sCurrentAttributeValue);
	}

	/**
	 * Gets the selected option (1st) for a drop down.
	 * 
	 * @param element - Drop Down to get 1st option selected
	 * @return null if no option selected or not drop down
	 */
	public static String getSelectedOption(WebElement element)
	{
		try
		{
			Select dropdown = new Select(element);
			List<WebElement> options = dropdown.getOptions();
			for (int i = 0; i < options.size(); i++)
			{
				if (options.get(i).isSelected())
					return getText(options.get(i));
			}
		}
		catch (Exception ex)
		{
		}

		return null;
	}

	/**
	 * Waits for an element to appear & be enabled<BR>
	 * <BR>
	 * <B>Note: </B>The selenium method isEnabled() returns true in most cases. If this does not work, then
	 * try waiting for the element to be displayed.
	 * 
	 * @param element - Element to wait to become enabled
	 * @param sElementName - Element Name to log
	 * @return true if element appears & is enabled before timeout else false
	 */
	public static boolean isWaitForEnabledElement(WebElement element, String sElementName)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			try
			{
				if (element.isEnabled())
					return true;
			}
			catch (Exception ex)
			{
			}

			sleep(getPollInterval());
		}

		Logs.log.warn("The element ('" + sElementName + "') did not become enabled before Timeout occurred");
		return false;
	}

	/**
	 * Waits for an element to be displayed.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use if element already exists but it is hidden and you want to wait until it becomes displayed.<BR>
	 * 2) Do <B>NOT</B> use if <B>element does not exist</B> instead use same method that takes a WebDriver &
	 * Locator.<BR>
	 * 
	 * @param element - Element to wait to be displayed
	 * @param sElementName - Element Name to log
	 * @return true if element is displayed before timeout else false
	 */
	public static boolean isWaitForDisplayedElement(WebElement element, String sElementName)
	{
		return isWaitForDisplayedElement(element, sElementName, true);
	}

	/**
	 * Waits for an element to be displayed.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use if element already exists but it is hidden and you want to wait until it becomes displayed.<BR>
	 * 2) Do <B>NOT</B> use if <B>element does not exist</B> instead use same method that takes a WebDriver &
	 * Locator.<BR>
	 * 
	 * @param element - Element to wait to be displayed
	 * @param sElementName - Element Name to log
	 * @param bLog - true to log warning if element is not displayed
	 * @return true if element is displayed before timeout else false
	 */
	public static boolean isWaitForDisplayedElement(WebElement element, String sElementName, boolean bLog)
	{
		return isWaitForDisplayedElement(element, sElementName, bLog, getTimeoutInMilliseconds());
	}

	/**
	 * Waits for an element to be displayed.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use if element already exists but it is hidden and you want to wait until it becomes displayed.<BR>
	 * 2) Do <B>NOT</B> use if <B>element does not exist</B> instead use same method that takes a WebDriver &
	 * Locator.<BR>
	 * 
	 * @param element - Element to wait to be displayed
	 * @param sElementName - Element Name to log
	 * @param bLog - true to log warning if element is not displayed
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 * @return true if element is displayed before timeout else false
	 */
	public static boolean isWaitForDisplayedElement(WebElement element, String sElementName, boolean bLog,
			int nMaxWaitTime)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			try
			{
				// Check if displayed
				if (element.isDisplayed())
					return true;
			}
			catch (Exception ex)
			{
			}

			sleep(getPollInterval());
		}

		if (bLog)
			Logs.log.warn("The element ('" + sElementName + "') was not displayed before Timeout occurred");

		return false;
	}

	/**
	 * Waits for an element to be displayed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @return true if element is displayed before timeout else false
	 */
	public static boolean isWaitForDisplayedElement(WebDriver driver, String sLocator)
	{
		return isWaitForDisplayedElement(driver, sLocator, true, getTimeoutInMilliseconds());
	}

	/**
	 * Waits for an element to be displayed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @param bLog - true to log warning if element is not displayed
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 * @return true if element is displayed before timeout else false
	 */
	public static boolean isWaitForDisplayedElement(WebDriver driver, String sLocator, boolean bLog,
			int nMaxWaitTime)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			// Try to find the element
			WebElement element = findElement(driver, sLocator, false);

			// Check if displayed
			if (isElementDisplayed(element))
				return true;

			sleep(getPollInterval());
		}

		if (bLog)
			Logs.log.warn("The locator ('" + sLocator + "') was not displayed before Timeout occurred");

		return false;
	}

	/**
	 * Checks if an element is displayed on the page
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 * @return true if element displayed else false
	 */
	public static boolean isElementDisplayed(WebDriver driver, String sLocator)
	{
		WebElement element = findElement(driver, sLocator, false);
		return isElementDisplayed(element);
	}

	/**
	 * Checks if the element is displayed on the page
	 * 
	 * @param element - WebElement to check if displayed
	 * @return true if element displayed else false
	 */
	public static boolean isElementDisplayed(WebElement element)
	{
		try
		{
			// Check if displayed
			if (element.isDisplayed())
				return true;
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Determine whether this element is currently displayed<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If <B>any exception</B> occurs while getting the information, then a retry will occur until max
	 * retries is reached<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to element
	 * @param retries - Number of retries to get information
	 * @return true if the element is displayed else false
	 */
	public static boolean isElementDisplayed(WebDriver driver, String sLocator, int retries)
	{
		int attempts = 0;
		boolean bStop = false;
		while (!bStop)
		{
			try
			{
				WebElement element = findElement(driver, sLocator, false);
				return element.isDisplayed();
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then stop loop
				if (attempts > retries)
					bStop = true;
				else
					sleep(getPollInterval());
			}
		}

		return false;
	}

	/**
	 * Waits for an element to be displayed and throws exception if not displayed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 */
	public static void waitForDisplayedElement(WebDriver driver, String sLocator)
	{
		waitForDisplayedElement(driver, sLocator, true);
	}

	/**
	 * Waits for an element to be displayed and throws exception if not displayed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 * @param bLog - true to log success (failures are always logged)
	 */
	public static void waitForDisplayedElement(WebDriver driver, String sLocator, boolean bLog)
	{
		waitForDisplayedElement(driver, sLocator, bLog, getTimeoutInMilliseconds());
	}

	/**
	 * Waits for an element to be displayed and throws exception if not displayed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 * @param bLog - true to log success (failures are always logged)
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 */
	public static void waitForDisplayedElement(WebDriver driver, String sLocator, boolean bLog,
			int nMaxWaitTime)
	{
		if (isWaitForDisplayedElement(driver, sLocator, bLog, nMaxWaitTime))
		{
			if (bLog)
			{
				String sMessage = "The locator ('" + sLocator + "') was displayed";
				Logs.log.info(sMessage);
			}
		}
		else
		{
			String sError = "The locator ('" + sLocator + "') was not displayed before Timeout occurred";
			Logs.logError(new GenericActionNotCompleteException(sError));
		}
	}

	/**
	 * Waits for an element to be displayed and throws exception if not displayed
	 * 
	 * @param element - element to wait until displayed
	 * @param sElementName - Element Name to log
	 */
	public static void waitForDisplayedElement(WebElement element, String sElementName)
	{
		waitForDisplayedElement(element, sElementName, true);
	}

	/**
	 * Waits for an element to be displayed and throws exception if not displayed
	 * 
	 * @param element - element to wait until displayed
	 * @param sElementName - Element Name to log
	 * @param bLogAll - true to log success (failures are always logged)
	 */
	public static void waitForDisplayedElement(WebElement element, String sElementName, boolean bLogAll)
	{
		if (isWaitForDisplayedElement(element, sElementName, bLogAll))
		{
			String sMessage = "The element ('" + sElementName + "') was displayed";
			if (bLogAll)
				Logs.log.info(sMessage);
		}
		else
		{
			String sError = "The element ('" + sElementName + "') was not displayed before Timeout occurred";
			Logs.logError(new GenericActionNotCompleteException(sError));
		}
	}

	/**
	 * Verifies that Element is displayed & sets URL & Page Name
	 * 
	 * @param sLocator - Locator for the Element that must be displayed on the page
	 * @param sPageName - Page Name for logging
	 * @param sUrl - URL to set
	 */
	protected void initialize(String sLocator, String sPageName, String sUrl)
	{
		if (isWaitForDisplayedElement(driver, sLocator))
		{
			PageFactory.initElements(new DefaultElementLocatorFactory(driver), this);
			this.sUrl = sUrl;
			this.sPageName = sPageName;
		}
		else
		{
			String sError = "Not at the " + sPageName + " page as the Element ('" + sLocator
					+ "') was not displayed as such initialization is not allowed.";
			Screenshot.saveScreenshot(driver);
			// Screenshot.saveScreenshotAddSuffix("method_initialize");
			Logs.logError(new WrongPageException(sError));
		}
	}

	/**
	 * Gets the available options for a drop down<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * 1) If cannot get available options for any reason, then an empty list will be returned
	 * 
	 * @param element - Drop Down to get options for
	 * @return List&lt;WebElement&gt; - Available options for the drop down (list is never <B>null</B>)
	 */
	public static List<WebElement> getOptions(WebElement element)
	{
		try
		{
			// Get drop down to work with
			Select dropdown = new Select(element);

			// Get all the available options
			List<WebElement> found = dropdown.getOptions();

			// We do not want to return a null variable as such only return variable if not null
			if (found != null)
				return found;
		}
		catch (Exception ex)
		{
		}

		// If get to this point, then return empty list
		return new ArrayList<WebElement>();
	}

	/**
	 * Selects a drop down option (Enhanced)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Supports all ways to select a drop down option (By Value (HTML), By Index, Regular Expression,
	 * Default - Visible Text)<BR>
	 * 2) Supports random selection for By Index when option to select converts to less than 0<BR>
	 * 3) Drop down must have at least 1 option<BR>
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param dd - Object that contains information on which drop down option to select
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelect(WebElement dropdown, String sDropDownName, DropDown dd)
	{
		/*
		 * If Random Selection desired, then get a valid random index
		 */
		if (dd.using == Selection.Index && Conversion.parseInt(dd.option) < 0)
		{
			dropDownSelection(dropdown, sDropDownName, Selection.Index, Rand.randomIndexOption(dropdown, dd),
					true);
		}
		else if (dd.using == Selection.Skip)
		{
			Logs.log.info("Skipped entering '" + sDropDownName + "'");
		}
		else
			dropDownSelection(dropdown, sDropDownName, dd.using, dd.option, true);
	}

	/**
	 * Gets the (HTML) value of the selected option (1st) for a drop down.
	 * 
	 * @param element - Drop Down to get 1st option selected
	 * @return null if no option selected or not drop down
	 */
	public static String getSelectedValue(WebElement element)
	{
		try
		{
			Select dropdown = new Select(element);
			List<WebElement> options = dropdown.getOptions();
			for (int i = 0; i < options.size(); i++)
			{
				if (options.get(i).isSelected())
					return getAttribute(options.get(i), getInputAttr());
			}
		}
		catch (Exception ex)
		{
		}

		return null;
	}

	/**
	 * Gets the index of the selected option (1st) for a drop down.<BR>
	 * <BR>
	 * <B>Note: </B>Zero based index value<BR>
	 * 
	 * @param element - Drop Down to get 1st option selected
	 * @return -1 if no option selected or not drop down
	 */
	public static int getSelectedIndex(WebElement element)
	{
		try
		{
			Select dropdown = new Select(element);
			List<WebElement> options = dropdown.getOptions();
			for (int i = 0; i < options.size(); i++)
			{
				if (options.get(i).isSelected())
					return i;
			}
		}
		catch (Exception ex)
		{
		}

		return -1;
	}

	/**
	 * Attempts to scroll into view the specified WebElement.
	 * 
	 * @param element - WebElement to scroll into view
	 * @return - false if an exception occurs else true
	 */
	public static boolean scrollIntoView(WebElement element)
	{
		return JS_Util.scrollIntoView(element);
	}

	/**
	 * Determine whether or not this check box/radio button is selected or not.
	 * 
	 * @param element - Check box or radio button element
	 * @return true if selected else false
	 */
	public static boolean isElementSelected(WebElement element)
	{
		try
		{
			return element.isSelected();
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Determine whether or not this check box/radio button is selected or not.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If <B>any exception</B> occurs while getting the information, then a retry will occur until max
	 * retries is reached<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to check box/radio button
	 * @param retries - Number of retries to get information
	 * @return true if selected else false
	 */
	public static boolean isElementSelected(WebDriver driver, String sLocator, int retries)
	{
		int attempts = 0;
		boolean bStop = false;
		while (!bStop)
		{
			try
			{
				WebElement element = findElement(driver, sLocator, false);
				return element.isSelected();
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then stop loop
				if (attempts > retries)
					bStop = true;
				else
					sleep(getPollInterval());
			}
		}

		return false;
	}

	/**
	 * Determines if the element is currently enabled
	 * 
	 * @param element - Element to check if enabled
	 * @return true the element is enabled else false
	 */
	public static boolean isElementEnabled(WebElement element)
	{
		try
		{
			return element.isEnabled();
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Determine whether or not this element is currently enabled or not.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If <B>any exception</B> occurs while getting the information, then a retry will occur until max
	 * retries is reached<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to element
	 * @param retries - Number of retries to get information
	 * @return true if the element is enabled else false
	 */
	public static boolean isElementEnabled(WebDriver driver, String sLocator, int retries)
	{
		int attempts = 0;
		boolean bStop = false;
		while (!bStop)
		{
			try
			{
				WebElement element = findElement(driver, sLocator, false);
				return element.isEnabled();
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then stop loop
				if (attempts > retries)
					bStop = true;
				else
					sleep(getPollInterval());
			}
		}

		return false;
	}

	/**
	 * Clears an element (should be only used on text fields.)
	 * 
	 * @param element - Field to clear
	 * @return true if successful else false
	 */
	public static boolean clearField(WebElement element)
	{
		try
		{
			element.clear();
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects or Unselects a check box (requires check box to be enabled.)
	 * 
	 * @param element - Check box to work with
	 * @param sElementName - Element Name to log
	 * @param bCheck - true to select check box
	 * @param bVerifyInitialState - true to require check box in unselected state if to be selected or
	 *            selected state if to be unselected and ready
	 * @throws CheckBoxWrongStateException if element is not in correct initial state
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	public static void checkbox(WebElement element, String sElementName, boolean bCheck,
			boolean bVerifyInitialState)
	{
		checkbox(element, sElementName, bCheck, true, bVerifyInitialState);
	}

	/**
	 * Selects or Unselects a check box
	 * 
	 * @param element - Check box to work with
	 * @param sElementName - Element Name to log
	 * @param bCheck - true to select check box
	 * @param bVerifyEnabled - true to verify check box is enabled before taking the action
	 * @param bVerifyInitialState - true to require check box in unselected state if to be selected or
	 *            selected state if to be unselected and ready
	 * @throws CheckBoxWrongStateException if element is not in correct initial state
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	public static void checkbox(WebElement element, String sElementName, boolean bCheck,
			boolean bVerifyEnabled, boolean bVerifyInitialState)
	{
		if (bVerifyEnabled)
		{
			if (!isElementEnabled(element))
			{
				String sError = "Check box for '" + sElementName + "' was not enabled";
				Logs.logError(new CheckBoxNotEnabled(sError));
			}
		}

		if (bCheck)
			check(element, sElementName, bVerifyInitialState, false);
		else
			uncheck(element, sElementName, bVerifyInitialState, false);
	}

	/**
	 * Selects, Unselects or skips a check box
	 * 
	 * @param element - Check box to work with
	 * @param sElementName - Element Name to log
	 * @param checkbox - Object that contains information about check box
	 * @throws CheckBoxWrongStateException if element is not in correct initial state
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	public static void checkbox(WebElement element, String sElementName, CheckBox checkbox)
	{
		boolean bEnabled = isElementEnabled(element);
		boolean bChecked = isElementSelected(element);

		if (checkbox.skip)
		{
			String sMessage = "Check box for '" + sElementName + "' was skipped.  The check box was ";
			if (bEnabled)
				sMessage += "enabled";
			else
				sMessage += "disabled";

			if (bChecked)
				sMessage += " and selected";
			else
				sMessage += " and not selected";

			Logs.log.info(sMessage);
			return;
		}

		if (bEnabled)
		{
			if (checkbox.check)
				check(element, sElementName, checkbox.verifyInitialState, checkbox.logError);
			else
				uncheck(element, sElementName, checkbox.verifyInitialState, checkbox.logError);
		}
		else
		{
			if (checkbox.verifyEnabled)
			{
				String sError = "Check box for '" + sElementName + "' was disabled";
				Logs.logError(new CheckBoxNotEnabled(sError));
			}

			if (checkbox.verifyInitialState)
			{
				if (!isElementDisplayed(element))
				{
					String sError = "Check box for '" + sElementName + "' was not displayed";
					Logs.logError(new ElementNotDisplayedException(sError));
				}
			}

			if (bChecked != checkbox.check)
			{
				String sError = "Check box for '" + sElementName
						+ "' was disabled and it was not in the correct desired state (" + checkbox.check
						+ ")";
				Logs.logError(new CheckBoxNotEnabled(sError));
			}
			else
			{
				String sWarn = "Check box for '" + sElementName
						+ "' was disabled but it was in the correct desired state (" + checkbox.check + ")";
				Logs.log.warn(sWarn);
			}
		}
	}

	/**
	 * Get a WebDriver from a WebElement. This is useful in cases where you only have a WebElement but need a
	 * WebDriver to do some action.
	 * 
	 * @param element - WebElement to get the WebDriver from
	 * @return null if exception occurs else WebDriver
	 */
	public static WebDriver getWebDriver(WebElement element)
	{
		WebDriver useDriver = null;

		try
		{
			/*
			 * Trick to get real element that can return the WebDriver.
			 * Note:
			 * 1) If you use element directly it is a proxy and this cannot be cast to RemoteWebElement
			 * 2) If WebElement cannot be bound, then this will generate an exception
			 */
			WebElement realElement = element.findElement(By.xpath("."));

			// Get the WebDriver object from the real (bound) WebElement
			useDriver = ((RemoteWebElement) realElement).getWrappedDriver();
		}
		catch (Exception ex)
		{
		}

		return useDriver;
	}

	/**
	 * Enters input into an 'Auto Complete' field and selects a valid option from the suggestion drop down
	 * list that appears.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) To get sLocator_SuggestionList & sLocator_DropDownOptions you may need to Fiddler (or some other
	 * proxy.)<BR>
	 * 2) Verifies if the drop down suggestion list appears as it was suppose to<BR>
	 * 3) Uses standard contains when selecting the drop down suggestion option using visible text
	 * 
	 * @param driver
	 * @param sLocator_Input - Locator to the input field that triggers the drop down suggestion list
	 * @param sLocator_SuggestionList - Locator to the Suggestion List (when it appears)
	 * @param sXpath_DropDownOptions - xpath to get all the drop down suggestion options
	 * @param sLog_Input - Logging of entering the input field
	 * @param field - Object containing data to be input & option to be selected
	 */
	public static void autoComplete(WebDriver driver, String sLocator_Input, String sLocator_SuggestionList,
			String sXpath_DropDownOptions, String sLog_Input, AutoCompleteField field)
	{
		autoComplete(driver, sLocator_Input, sLocator_SuggestionList, sXpath_DropDownOptions, sLog_Input,
				field, true, Comparison.Standard, -1);
	}

	/**
	 * Enters input into an 'Auto Complete' field and selects a valid option from the suggestion drop down
	 * list that appears.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) To get sLocator_SuggestionList & sLocator_DropDownOptions you may need to Fiddler (or some other
	 * proxy.)<BR>
	 * 2) Index 0 is used to check if suggestion list is stable<BR>
	 * 
	 * @param driver
	 * @param sLocator_Input - Locator to the input field that triggers the drop down suggestion list
	 * @param sLocator_SuggestionList - Locator to the Suggestion List (when it appears)
	 * @param sXpath_DropDownOptions - xpath to get all the drop down suggestion options
	 * @param sLog_Input - Logging of entering the input field
	 * @param field - Object containing data to be input & option to be selected
	 * @param bLengthCheck - true to verify that if the drop down suggestion list appears as it was suppose
	 *            to. (Also, it can provide addition reason why suggestion list did not appear.)
	 * @param compareOption - When selecting drop down suggestion option using visible text, this allows the
	 *            contains comparison to be Lower, Upper or Standard
	 */
	public static void autoComplete(WebDriver driver, String sLocator_Input, String sLocator_SuggestionList,
			String sXpath_DropDownOptions, String sLog_Input, AutoCompleteField field, boolean bLengthCheck,
			Comparison compareOption)
	{
		autoComplete(driver, sLocator_Input, sLocator_SuggestionList, sXpath_DropDownOptions, sLog_Input,
				field, true, compareOption, 0);
	}

	/**
	 * Enters input into an 'Auto Complete' field and selects a valid option from the suggestion drop down
	 * list that appears.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) To get sLocator_SuggestionList & sLocator_DropDownOptions you may need to Fiddler (or some other
	 * proxy.)<BR>
	 * 2) If nCheckIndex is still invalid after timeout, then only a warning is logged<BR>
	 * 
	 * @param driver
	 * @param sLocator_Input - Locator to the input field that triggers the drop down suggestion list
	 * @param sLocator_SuggestionList - Locator to the Suggestion List (when it appears)
	 * @param sXpath_DropDownOptions - xpath to get all the drop down suggestion options
	 * @param sLog_Input - Logging of entering the input field
	 * @param field - Object containing data to be input & option to be selected
	 * @param bLengthCheck - true to verify that if the drop down suggestion list appears as it was suppose
	 *            to. (Also, it can provide addition reason why suggestion list did not appear.)
	 * @param compareOption - When selecting drop down suggestion option using visible text, this allows the
	 *            contains comparison to be Lower, Upper or Standard
	 * @param nCheckIndex - Index used to check if suggestion list is stable
	 *            <0 to skip this check
	 */
	public static void autoComplete(WebDriver driver, String sLocator_Input, String sLocator_SuggestionList,
			String sXpath_DropDownOptions, String sLog_Input, AutoCompleteField field, boolean bLengthCheck,
			Comparison compareOption, int nCheckIndex)
	{
		if (field.skip)
		{
			Logs.log.info("The auto complete field '" + sLog_Input + "' was skipped.");
			return;
		}

		//
		// Step 1: Enter the value into the field (which triggers the suggestion list)
		//
		WebElement auto = findElementAJAX(driver, sLocator_Input);
		enterField(auto, sLog_Input, field.value);

		//
		// For performance reasons, return immediately if the suggestion list should not be displayed and user
		// was going to cancel
		//
		if (field.cancelSelection && !field.triggerSuggestions())
		{
			Logs.log.info("Did not wait for the suggestion drop down list to appear to cancel");
			return;
		}

		//
		// Step 2: Wait for the suggestion list to appear
		//
		if (isWaitForDisplayedElement(driver, sLocator_SuggestionList))
		{
			// Was the suggestions list suppose to be displayed based on input length?
			if (!field.triggerSuggestions())
			{
				String sMessage = "The number of characters entered should not have triggered the suggestions list";
				if (bLengthCheck)
					Logs.logError(sMessage);
				else
					Logs.log.warn(sMessage);
			}

			// Does user wish to cancel the drop down selection process?
			if (field.cancelSelection)
			{
				try
				{
					auto.sendKeys(Keys.ESCAPE);
					Logs.log.info("Cancelled auto complete drop down selection using ESC");
					return;
				}
				catch (Exception ex)
				{
					Logs.logError("Could not cancel auto complete drop down selection due to exception ["
							+ ex.getClass().getName() + "]:  " + ex.getMessage());
				}
			}

			//
			// It is possible for the suggestion list to change before the selection occurs due to the
			// application being slow. To try and mitigate this issue, we will wait until the index
			// contains the entered text.
			//
			List<WebElement> suggestions = null;
			int nSuggestions = 0;
			boolean bError = true;

			ElapsedTime e = new ElapsedTime();
			while (!e.isTimeout())
			{
				// Get list of options available
				suggestions = findElementsAJAX(driver, sXpath_DropDownOptions);
				if (suggestions == null)
					Logs.logError("Could not find the '" + sLog_Input + "' suggestions available");

				nSuggestions = suggestions.size();
				if (nSuggestions < 1)
					Logs.logError("No suggestions in list");

				// Does user want to verify the suggest list is stable?
				if (nCheckIndex < 0)
				{
					bError = false;
					break;
				}

				// Get the verification suggestion option
				// Note: If the index is invalid, then make it the empty string
				String sSuggestion;
				if (nCheckIndex >= suggestions.size())
					sSuggestion = "";
				else
					sSuggestion = getText(suggestions.get(nCheckIndex));

				// Is does the suggestion option contain the entered text?
				if (Compare.contains(sSuggestion, field.value, Comparison.Lower))
				{
					bError = false;
					break;
				}
				else
				{
					// Wait before checking again
					sleep(getPollInterval());
				}
			}

			if (bError)
			{
				Logs.log.warn("The specified check index (" + nCheckIndex
						+ ") did not contain the entered text ('" + field.value
						+ "') before timeout occurred.  "
						+ "This may cause the selection suggestion to be INCORRECT.");
			}

			//
			// Does user want a random Index selection (if using Index)?
			// NOTE: If the random selection is not visible, then we will loop through the entire list and
			// pick 1st visible option.
			//
			if (field.useIndex && field.useRandomIndex())
			{
				// Generate valid index
				int nUseIndex = Rand.randomRange(0, nSuggestions - 1);

				WebElement random = suggestions.get(nUseIndex);
				if (isElementDisplayed(random))
				{
					String sValue = Conversion.nonNull(getText(random));
					click(random, "Suggestion ('" + sValue + "')");
					return;
				}
			}

			// Loop through suggestion list to find desired option to select
			for (int nIndex = 0; nIndex < nSuggestions; nIndex++)
			{
				// Option needs to be visible to the user
				WebElement element = suggestions.get(nIndex);
				if (isElementDisplayed(element))
				{
					// Assume that this is not the option we are looking for
					boolean bSelect = false;
					String sValue = Conversion.nonNull(getText(element));
					if (field.useIndex)
					{
						// Is this the index we are looking for?
						// Note: The specified index may be hidden. So, I will pick the first option that
						// is visible and greater than equal to the desired index.
						if (nIndex >= field.getIndex())
							bSelect = true;
					}
					else
					{
						// Does the option contain the visible text we are looking for?
						if (Compare.contains(sValue, field.selectOption, compareOption))
							bSelect = true;
					}

					// Should we select the option?
					if (bSelect)
					{
						click(element, "Suggestion ('" + sValue + "')");
						return;
					}
				}
			}

			//
			// We were unable to find the specified option after checking all visible options
			//
			if (field.useIndex)
				Logs.logError("There was no index greater than or equal to " + field.getIndex()
						+ " in the suggestion list that was visible");
			else
				Logs.logError("There was no option that contained '" + field.selectOption
						+ "' in the suggestion list that was visible");
		}
		else
		{
			// Does user wish to cancel the drop down selection process? If this is true, then it is possible
			// the value entered would never trigger the auto complete drop down. Also, it does not matter
			// that the drop down did not appear as the user was just going to cancel it.
			if (field.cancelSelection)
			{
				Logs.log.info("It was not necesary to cancel the process"
						+ " as the suggestion drop down list did not appear before timeout");
				return;
			}

			String sMessage = "The suggestion drop down list did not appear before timeout";
			if (bLengthCheck && !field.triggerSuggestions())
			{
				sMessage = "The number of characters (" + field.getTriggerLength()
						+ ") entered was not enough to trigger the suggestions list";
			}

			Logs.logError(sMessage);
		}
	}

	/**
	 * Clears & enters value into specified field. (This is specifically for fields that have default values
	 * as it gives the flexibility to skip the field, enter a specific or random value.)<BR>
	 * <BR>
	 * <B>Note: </B> If value is null or it is specified to skip, then the field is skipped<BR>
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 */
	public static void enterField(WebElement element, String sElementName, InputField value)
	{
		if (value != null && value.modifyField())
		{
			// Enter value
			enterField(element, sElementName, value.getValueToInput());
		}
		else
		{
			// Skip the field but log some useful information
			String sValue = Conversion.nonNull(getAttribute(element, getInputAttr()));
			Logs.log.info("The field '" + sElementName + "' was skipped.  Default value was '" + sValue + "'");
		}
	}

	/**
	 * Gets all cookies for the <B>current domain</B><BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It does not matter if you navigated to another domain previously only the current domains cookies
	 * can be retrieved due to security restrictions on JavaScript<BR>
	 * 2) If no cookies (or exception occurs), then the empty set is returned<BR>
	 * 
	 * @param driver
	 * @return Set&lt;Cookie&gt;
	 */
	public static Set<Cookie> getCookies(WebDriver driver)
	{
		try
		{
			return driver.manage().getCookies();
		}
		catch (Exception ex)
		{
		}

		return Collections.emptySet();
	}

	/**
	 * Logs all cookies
	 * 
	 * @param driver
	 */
	public static void logAllCookies(WebDriver driver)
	{
		Set<Cookie> allCookies = getCookies(driver);
		if (allCookies.isEmpty())
			Logs.log.info("No Cookies");

		for (Cookie c : allCookies)
		{
			Logs.log.info(c.toString());
		}
	}

	/**
	 * Writes the current page source to a file for debugging purposes.
	 * 
	 * @param driver
	 * @param sFileName - Filename to write the current page as
	 * @return true if saving current page as file is successful else false
	 */
	public static boolean saveCurrentPage(WebDriver driver, String sFileName)
	{
		try
		{
			File file = new File(sFileName);
			if (file.exists())
				file.delete();

			file.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(driver.getPageSource());
			out.close();
			return true;
		}
		catch (Exception ex)
		{
			Logs.log.warn("Saving current page source failed with exception:  " + ex.getMessage());
			return false;
		}
	}

	/**
	 * Logs the current date time for debugging purposes
	 */
	public static void logCurrentDateTime()
	{
		Logs.log.info("Current Date Time:  " + new Date());
	}

	/**
	 * Method used to remove focus from the current element with focus by clicking some other element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Element to click does not need to be enabled<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator of element to click
	 * @return true if clicked successful else false
	 */
	public static boolean loseFocus(WebDriver driver, String sLocator)
	{
		try
		{
			WebElement element = findElement(driver, sLocator, false);
			element.click();
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Method used to remove focus from the current element with focus by clicking the HTML tag
	 * 
	 * @param driver
	 * @return true if clicked successful else false
	 */
	public static boolean loseFocus(WebDriver driver)
	{
		return loseFocus(driver, "tag=html");
	}

	/**
	 * Method used to give focus to some element by clicking<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Element to click does not need to be enabled<BR>
	 * 2) This should not be done on any element that triggers an action on click (for example buttons.)<BR>
	 * 
	 * @param element - Element to focus by clicking
	 * @return true if clicked successful else false
	 */
	public static boolean focus(WebElement element)
	{
		try
		{
			element.click();
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Method used to give focus to some element by clicking<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Element to click does not need to be enabled<BR>
	 * 2) This should not be done on any element that triggers an action on click (for example buttons.)<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator of element to click
	 * @return true if clicked successful else false
	 */
	public static boolean focus(WebDriver driver, String sLocator)
	{
		WebElement element = findElement(driver, sLocator, false);
		return focus(element);
	}

	/**
	 * Used to for hover over menus and clicking specific menu option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Hover over occurs in order<BR>
	 * 2) Last element is hovered over and clicked<BR>
	 * 
	 * @param driver
	 * @param locatorsForHoverActions - List of locators in order to hover over. (Last one is also clicked.)
	 */
	public static void hoverClick(WebDriver driver, List<String> locatorsForHoverActions)
	{
		try
		{
			Actions actions = new Actions(driver);

			for (int i = 0; i < locatorsForHoverActions.size(); i++)
			{
				// Get locator
				String sLocator = locatorsForHoverActions.get(i);

				// Find element & move mouse over it
				WebElement element = findElement(driver, sLocator);
				actions.moveToElement(element);
				actions.perform();
				waitForDisplayedElement(driver, sLocator, false);
				Logs.log.info("Move Action successful for locator:  " + sLocator);

				// Click the last element
				if (i == locatorsForHoverActions.size() - 1)
				{
					actions.click();
					actions.perform();
					Logs.log.info("Mouse Click successful for locator:  " + sLocator);
				}
			}
		}
		catch (Exception ex)
		{
			String sError = "Moving Mouse and Clicking failed due to exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage();
			Logs.logError(new MouseActionsException(sError));
		}
	}

	/**
	 * Verifies Correct Page is displayed based on an element becoming displayed
	 * 
	 * @param sLocator - Locator for the element to be found
	 * @throws WrongPageException if element is not displayed within timeout
	 */
	protected void verifyCorrectPage(String sLocator)
	{
		verifyCorrectPage(sLocator, getTimeoutInMilliseconds());
	}

	/**
	 * Verifies Correct Page is displayed based on an element becoming displayed
	 * 
	 * @param sLocator - Locator for the element to be found
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 * @throws WrongPageException if element is not displayed within timeout
	 */
	protected void verifyCorrectPage(String sLocator, int nMaxWaitTime)
	{
		if (!isWaitForDisplayedElement(driver, sLocator, false, nMaxWaitTime))
		{
			String sTempPageName = sPageName;
			if (sTempPageName == null)
				sTempPageName = "null";

			String sError = "Could not find specific element on page that indicates at correct page ('"
					+ sTempPageName + "')";
			Logs.logError(new WrongPageException(sError));
		}
	}

	/**
	 * Verifies Correct Page is displayed based on the criteria being met (all or 1st match based on flag)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Timeout is the Framework value<BR>
	 * 2) Poll Interval is the Framework value<BR>
	 * 
	 * @param criteria - List of criteria that indicate at the correct page
	 * @param allMatches - true to wait for all criteria matches, false to wait for 1st matching criteria
	 */
	protected void verifyCorrectPage(List<GenericData> criteria, boolean allMatches)
	{
		verifyCorrectPage(criteria, getTimeoutInMilliseconds(), allMatches);
	}

	/**
	 * Verifies Correct Page is displayed based on the criteria being met (all or 1st match based on flag)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Poll Interval is the Framework value<BR>
	 * 
	 * @param criteria - List of criteria that indicate at the correct page
	 * @param allMatches - true to wait for all criteria matches, false to wait for 1st matching criteria
	 * @param timeout - Max Time (milliseconds) to wait for a criteria match
	 */
	protected void verifyCorrectPage(List<GenericData> criteria, int timeout, boolean allMatches)
	{
		verifyCorrectPage(criteria, timeout, getPollInterval(), allMatches);
	}

	/**
	 * Verifies Correct Page is displayed based on the criteria being met (all or 1st match based on flag)
	 * 
	 * @param criteria - List of criteria that indicate at the correct page
	 * @param timeout - Max Time (milliseconds) to wait for a criteria match
	 * @param poll - Poll interval in milliseconds to be used
	 * @param allMatches - true to wait for all criteria matches, false to wait for 1st matching criteria
	 * @throws WrongPageException if no criteria is met within timeout
	 */
	protected void verifyCorrectPage(List<GenericData> criteria, int timeout, int poll, boolean allMatches)
	{
		Condition condition = new Condition(driver, timeout, poll);

		int result = -3;
		if (allMatches)
		{
			if (condition.waitForAllMatches(criteria, false))
			{
				result = 0;
			}
		}
		else
		{
			result = condition.waitForMatch(criteria, false);
		}

		if (result < 0)
		{
			StringBuilder builder = new StringBuilder();
			if (allMatches)
			{
				builder.append("All of the criteria were not");
			}
			else
			{
				builder.append("None of the criteria were");
			}

			builder.append(" met to indicate at the correct page (");
			builder.append(getPageName());
			builder.append(").  Criteria:  ");
			builder.append(criteria.toString());

			Logs.logError(new WrongPageException(builder.toString()));
		}
	}

	/**
	 * Waits for an element to be removed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is removed
	 * @return true if element is removed before timeout else false
	 */
	public static boolean isElementRemoved(WebDriver driver, String sLocator, int nMaxWaitTime)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			try
			{
				// Try to find the element
				WebElement element = findElement(driver, sLocator, false);

				// Check if the element does not exist or not displayed
				if (!isElementDisplayed(element))
					return true;
			}
			catch (Exception ex)
			{
			}

			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Waits for an element to be removed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @return true if element is removed before timeout else false
	 */
	public static boolean wasElementRemoved(WebDriver driver, String sLocator)
	{
		return isElementRemoved(driver, sLocator, getTimeoutInMilliseconds());
	}

	/**
	 * Waits for an element to be removed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 */
	public static void waitForElementRemoval(WebDriver driver, String sLocator)
	{
		if (!wasElementRemoved(driver, sLocator))
		{
			String sError = "The element ('" + sLocator + "') was not removed before Timeout occurred";
			Logs.logError(sError);
		}
	}

	/**
	 * Wait until a new pop-up window appears<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes that valid window handles cannot be the empty string<BR>
	 * 2) If multiple pop-up windows appear, then only the 1st found is returned which may not necessarily be
	 * the 1st that appeared<BR>
	 * 
	 * @param existing - List of existing pop-up windows to ignore
	 * @return empty string if no new pop-up appears else window handle of 1st new window
	 */
	public String waitForPopup(List<String> existing)
	{
		return waitForPopup(existing, getTimeoutInMilliseconds());
	}

	/**
	 * Wait until a new pop-up window appears<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes that valid window handles cannot be the empty string<BR>
	 * 2) If multiple pop-up windows appear, then only the 1st found is returned which may not necessarily be
	 * the 1st that appeared<BR>
	 * 
	 * @param existing - List of existing pop-up windows to ignore
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait for alert to appear
	 * @return empty string if no new pop-up appears else window handle of 1st new window
	 */
	public String waitForPopup(List<String> existing, int nMaxWaitTime)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			String[] openWindows = driver.getWindowHandles().toArray(new String[0]);
			if (openWindows.length > 1)
			{
				// Check if any of the open windows are not in the existing list
				for (int i = 0; i < openWindows.length; i++)
				{
					if (!openWindows[i].equals("") && !existing.contains(openWindows[i]))
						return openWindows[i];
				}
			}

			sleep(getPollInterval());
		}

		// No new pop-up window appeared within the timeout value
		return "";
	}

	/**
	 * Wait until a new pop-up window appears<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes that valid window handles cannot be the empty string<BR>
	 * 2) If multiple pop-up windows appear, then only the 1st found is returned which may not necessarily be
	 * the 1st that appeared<BR>
	 * 
	 * @param existing - List of existing pop-up windows to ignore
	 * @param expectedWindowName - For error logging only, if expected window does not appear
	 * @return window handle of 1st new window
	 * @throws GenericUnexpectedException if no new window appears before timeout occurs
	 */
	public String waitForPopup(List<String> existing, String expectedWindowName)
	{
		String handle = waitForPopup(existing);
		if (Compare.equals(handle, "", Comparison.Equal))
			Logs.logError("The window ('" + expectedWindowName + "') did not appear before timeout");

		return handle;
	}

	/**
	 * Switches to the specified Window Handle (and stores handle)
	 * 
	 * @param sWindowHandle - Window Handle to use
	 * @return true if successful
	 */
	public boolean switchToPopupWindow(String sWindowHandle)
	{
		try
		{
			driver.switchTo().window(sWindowHandle);
			sPopupWindowHandle = sWindowHandle;
			return true;
		}
		catch (Exception ex)
		{
		}

		// Window Handle was invalid
		return false;
	}

	/**
	 * Waits for a pop-up window to appear and switches to it
	 * 
	 * @param sLogWindowName - Window Name for logging purposes
	 */
	public void workWithPopupWindow(String sLogWindowName)
	{
		if (waitForPopup())
		{
			Logs.log.info("The window ('" + sLogWindowName + "') appeared");
			if (switchToPopupWindow())
			{
				Logs.log.info("Switched to the window ('" + sLogWindowName + "') successfully");
			}
			else
			{
				Logs.logError("Could not switch to the window ('" + sLogWindowName + "')");
			}
		}
		else
		{
			Logs.logError("The window ('" + sLogWindowName + "') did not appear before timeout");
		}
	}

	/**
	 * Switches to a specified pop-up window
	 * 
	 * @param sWindowHandle - Window Handle to switch to
	 * @param sLogWindowName - Window Name for logging purposes
	 */
	public void workWithPopupWindow(String sWindowHandle, String sLogWindowName)
	{
		if (switchToPopupWindow(sWindowHandle))
		{
			Logs.log.info("Switched to the window ('" + sLogWindowName + "') successfully");
		}
		else
		{
			Logs.logError("Could not switch to the window ('" + sLogWindowName + "') with handle ('"
					+ sWindowHandle + "')");
		}
	}

	/**
	 * Finds the element using the specified locator & clicks with logging. Attempts to prevent
	 * StaleElementReferenceException by retrying the specified number times.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used if there is AJAX that continually refreshes the element every X
	 * seconds<BR>
	 * 2) Any exception other than StaleElementReferenceException will cause a failure immediately without any
	 * retry<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sElementName - Element Name to log
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws ClickNoSuchElementException if could not find the element
	 */
	public static void click(WebDriver driver, String sLocator, String sElementName, int retries)
	{
		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = driver.findElement(locatedBy(sLocator));
				if (!element.isEnabled())
				{
					throw new ElementNotEnabledException("");
				}

				if (!element.isDisplayed())
				{
					throw new ElementNotDisplayedException("");
				}

				element.click();
				Logs.log.info("Clicked '" + sElementName + "' successfully");
				return;
			}
			catch (ElementNotEnabledException ex)
			{
				String sError = "Element ('" + sElementName + "') was not enabled";
				Logs.logError(new ElementNotEnabledException(sError));
			}
			catch (ElementNotDisplayedException ex)
			{
				String sError = "Element ('" + sElementName + "') was not displayed";
				Logs.logError(new ElementNotDisplayedException(sError));
			}
			catch (StaleElementReferenceException ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then log error and throw
				// exception
				if (attempts > retries)
				{
					String sError = "Element ('" + sElementName
							+ "') was stale as StaleElementReferenceException was thrown.";
					Logs.logError(new ClickNoSuchElementException(sError));
				}
				else
				{
					sleep(getPollInterval());
				}
			}
			catch (Exception ex)
			{
				String sError = "Could not find '" + sElementName + "' due to exception["
						+ ex.getClass().getName() + "]:  " + ex.getMessage();
				Logs.logError(new ClickNoSuchElementException(sError));
			}
		}
	}

	/**
	 * Selects a drop down option. Attempts to prevent StaleElementReferenceException by retrying the
	 * specified number times.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used if there is AJAX that continually refreshes the element every X
	 * seconds<BR>
	 * 2) Any exception other than StaleElementReferenceException will cause a failure immediately without any
	 * retry<BR>
	 * 3) Supports all ways to select a drop down option (1 - By Value, 2 - By Index, 3 - Regular Expression,
	 * Default - Visible Text)<BR>
	 * 4) Supports random selection for By Index when option to select converts to less than 0<BR>
	 * 5) Drop down must have at least 1 option<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the drop down
	 * @param sDropDownName - Drop Down Name to log
	 * @param dd - Object that contains information on which drop down option to select
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void dropDownSelect(WebDriver driver, String sLocator, String sDropDownName, DropDown dd,
			int retries)
	{
		if (dd.using == Selection.Skip)
		{
			Logs.log.info("Skipped entering '" + sDropDownName + "'");
			return;
		}

		// Logging message if necessary
		String sError;

		// Used to store the random value. For performance reasons the random value will only be set once
		String sRandom = null;

		// Set flag to indicate random if necessary
		boolean bRandom = false;
		if (dd.using == Selection.Index && Conversion.parseInt(dd.option) < 0)
			bRandom = true;

		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement dropdown = driver.findElement(locatedBy(sLocator));
				if (!dropdown.isEnabled())
				{
					throw new ElementNotEnabledException("");
				}

				if (!dropdown.isDisplayed())
				{
					throw new ElementNotDisplayedException("");
				}

				Select value = new Select(dropdown);
				if (dd.using == Selection.ValueHTML)
				{
					value.selectByValue(dd.option);
					Logs.log.info("Successfully selected by value '" + dd.option + "' from the drop down ('"
							+ sDropDownName + "')");
					return;
				}
				else if (dd.using == Selection.Index && !bRandom)
				{
					value.selectByIndex(Integer.parseInt(dd.option));
					Logs.log.info("Successfully selected by index '" + dd.option + "' from the drop down ('"
							+ sDropDownName + "')");
					return;
				}
				else if (dd.using == Selection.Index && bRandom)
				{
					// Only set sRandom once for performance reasons
					if (sRandom == null)
						sRandom = Rand.randomIndexOption(dropdown, dd);

					value.selectByIndex(Integer.parseInt(sRandom));
					Logs.log.info("Successfully selected by index '" + sRandom + "' from the drop down ('"
							+ sDropDownName + "')");
					return;
				}
				else if (dd.using == Selection.RegEx)
				{
					int nIndex = 0;
					List<WebElement> availableOptions = value.getOptions();
					for (WebElement option : availableOptions)
					{
						// Select option if regular expression matches
						if (option.getText().matches(dd.option))
						{
							value.selectByIndex(nIndex);
							Logs.log.info("Successfully selected by index '" + nIndex + "' by using RegEx '"
									+ dd.option + "' to match from the drop down ('" + sDropDownName + "')");
							return;
						}

						nIndex++;
					}

					throw new DropDownPartialMatchException("");
				}
				else
				{
					value.selectByVisibleText(dd.option);
					Logs.log.info("Successfully selected '" + dd.option + "' from the drop down ('"
							+ sDropDownName + "')");
					return;
				}
			}
			catch (ElementNotEnabledException ex)
			{
				sError = "Element ('" + sDropDownName + "') was not enabled";
				Logs.logError(new ElementNotEnabledException(sError));
			}
			catch (ElementNotDisplayedException ex)
			{
				sError = "Element ('" + sDropDownName + "') was not displayed";
				Logs.logError(new ElementNotDisplayedException(sError));
			}
			catch (NoSuchElementException nsee)
			{
				sError = "Could not find '" + dd.option + "' in the drop down ('" + sDropDownName + "')";
				Logs.logError(new DropDownSelectionException(sError));
			}
			catch (NumberFormatException nfe)
			{
				sError = "Could not find index '" + dd.option + "' in the drop down ('" + sDropDownName
						+ "')";
				Logs.logError(new DropDownIndexException(sError));
			}
			catch (DropDownPartialMatchException ddpme)
			{
				sError = "Could not find a partial match using the regular expression '" + dd.option
						+ "' in the drop down ('" + sDropDownName + "')";
				Logs.logError(new DropDownPartialMatchException(sError));
			}
			catch (StaleElementReferenceException ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then log error and throw
				// exception
				if (attempts > retries)
				{
					sError = "Drop down ('" + sDropDownName
							+ "') was stale as StaleElementReferenceException was thrown.";
					Logs.logError(new DropDownNoSuchElementException(sError));
				}
				else
				{
					sleep(getPollInterval());
				}
			}
			catch (Exception ex)
			{
				sError = "Could not find drop down ('" + sDropDownName + "') due to exception ["
						+ ex.getClass().getName() + "]:  " + ex.getMessage();
				Logs.logError(new DropDownNoSuchElementException(sError));
			}
		}
	}

	/**
	 * Clears & enters value into specified field. Attempts to prevent StaleElementReferenceException by
	 * retrying the specified number times. (This is specifically for fields that have default values as it
	 * gives the flexibility to skip the field, enter a specific or random value.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used if there is AJAX that continually refreshes the element every X
	 * seconds<BR>
	 * 2) Any exception other than StaleElementReferenceException will cause a failure immediately without any
	 * retry<BR>
	 * 3) If value is null or it is specified to skip, then the field is skipped<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the field
	 * @param sElementName - Element Name to log
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws EnterFieldNoSuchElementException if could not find the element
	 */
	public static void enterField(WebDriver driver, String sLocator, String sElementName, InputField value,
			int retries)
	{
		enterField(driver, sLocator, sElementName, value, true, retries);
	}

	/**
	 * Clears (if specified) & enters value into specified field. Attempts to prevent
	 * StaleElementReferenceException by retrying the specified number times. (This is specifically for fields
	 * that have default values as it gives the flexibility to skip the field, enter a specific or random
	 * value.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used if there is AJAX that continually refreshes the element every X
	 * seconds<BR>
	 * 2) Any exception other than StaleElementReferenceException will cause a failure immediately without any
	 * retry<BR>
	 * 3) If value is null or it is specified to skip, then the field is skipped<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the field
	 * @param sElementName - Element Name to log
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 * @param bClearField - true to clear field before entering value
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws EnterFieldNoSuchElementException if could not find the element
	 */
	public static void enterField(WebDriver driver, String sLocator, String sElementName, InputField value,
			boolean bClearField, int retries)
	{
		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = driver.findElement(locatedBy(sLocator));

				if (value == null || !value.modifyField())
				{
					// Skip the field but log some useful information
					String sValue = Conversion.nonNull(getAttribute(element, getInputAttr()));
					Logs.log.info("The field '" + sElementName + "' was skipped.  Default value was '"
							+ sValue + "'");
					return;
				}

				if (!element.isEnabled())
				{
					throw new ElementNotEnabledException("");
				}

				if (!element.isDisplayed())
				{
					throw new ElementNotDisplayedException("");
				}

				String sLogCleared = "";
				if (bClearField)
				{
					element.clear();
					sLogCleared = "cleared & ";
				}

				element.sendKeys(value.getValueToInput());
				Logs.log.info("Successfully " + sLogCleared + "entered the value '" + value.getValueToInput()
						+ "' into '" + sElementName + "'");
				return;
			}
			catch (ElementNotEnabledException ex)
			{
				String sError = "Element ('" + sElementName + "') was not enabled";
				Logs.logError(new ElementNotEnabledException(sError));
			}
			catch (ElementNotDisplayedException ex)
			{
				String sError = "Element ('" + sElementName + "') was not displayed";
				Logs.logError(new ElementNotDisplayedException(sError));
			}
			catch (StaleElementReferenceException ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then log error and throw
				// exception
				if (attempts > retries)
				{
					String sError = "Element ('" + sElementName
							+ "') was stale as StaleElementReferenceException was thrown.";
					Logs.logError(new EnterFieldNoSuchElementException(sError));
				}
				else
				{
					sleep(getPollInterval());
				}
			}
			catch (Exception ex)
			{
				String sError = "Could not find '" + sElementName + "' due to exception["
						+ ex.getClass().getName() + "]:  " + ex.getMessage();
				Logs.logError(new EnterFieldNoSuchElementException(sError));
			}
		}
	}

	/**
	 * Selects, Unselects or skips a check box. Attempts to prevent StaleElementReferenceException by retrying
	 * the specified number times.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used if there is AJAX that continually refreshes the element every X
	 * seconds<BR>
	 * 2) Any exception other than StaleElementReferenceException will cause a failure immediately without any
	 * retry<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the check box
	 * @param sElementName - Element Name to log
	 * @param checkbox - Object that contains information about check box
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws CheckBoxWrongStateException if element is not in correct initial state
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 */
	public static void checkbox(WebDriver driver, String sLocator, String sElementName, CheckBox checkbox,
			int retries)
	{
		int attempts = 0;
		while (true)
		{
			WebElement element = findElement(driver, sLocator, false);
			boolean bEnabled = isElementEnabled(element);
			boolean bChecked = isElementSelected(element);

			if (checkbox.skip)
			{
				String sMessage = "Check box for '" + sElementName + "' was skipped.  The check box was ";
				if (bEnabled)
					sMessage += "enabled";
				else
					sMessage += "disabled";

				if (bChecked)
					sMessage += " and selected";
				else
					sMessage += " and not selected";

				Logs.log.info(sMessage);
				return;
			}

			if (bEnabled)
			{
				try
				{
					// Does user want to enforce state before action
					if (checkbox.verifyInitialState)
					{
						if (checkbox.check)
						{
							// User wants to select the check box as such it must not be selected
							if (element.isSelected())
								throw new CheckBoxWrongStateException("");
						}
						else
						{
							// User wants to unselect the check box as such it must be selected
							if (!element.isSelected())
								throw new CheckBoxWrongStateException("");
						}

						// Ensure element is enabled
						if (!element.isEnabled())
							throw new ElementNotEnabledException("");

						// Ensure element is displayed
						if (!element.isDisplayed())
							throw new ElementNotDisplayedException("");
					}

					if (checkbox.check)
					{
						// Click check box only if it is not already selected
						if (!element.isSelected())
						{
							element.click();
							Logs.log.info("Successfully selected check box for '" + sElementName + "'");
						}
						else
						{
							Logs.log.info("Check box for '" + sElementName + "' was already selected");
						}
					}
					else
					{
						// Click check box only if it is selected
						if (element.isSelected())
						{
							element.click();
							Logs.log.info("Successfully unselected check box for '" + sElementName + "'");
						}
						else
						{
							Logs.log.info("Check box for '" + sElementName + "' was already unselected");
						}
					}

					return;
				}
				catch (CheckBoxWrongStateException cbwse)
				{
					String sError = "Required initial state of the check box '" + sElementName
							+ "' was not correct for the check operation";
					Logs.logError(new CheckBoxWrongStateException(sError));
				}
				catch (ElementNotEnabledException ex)
				{
					String sError = "Element ('" + sElementName + "') was not enabled";
					Logs.logError(new ElementNotEnabledException(sError));
				}
				catch (ElementNotDisplayedException ex)
				{
					String sError = "Element ('" + sElementName + "') was not displayed";
					Logs.logError(new ElementNotDisplayedException(sError));
				}
				catch (StaleElementReferenceException ex)
				{
					// Increment the number of attempts
					attempts++;

					// If the number of attempts is greater than the retries specified, then log error and
					// throw exception
					if (attempts > retries)
					{
						String sError = "Element ('" + sElementName
								+ "') was stale as StaleElementReferenceException was thrown.";
						Logs.logError(new CheckBoxNoSuchElementException(sError));
					}
					else
					{
						sleep(getPollInterval());
					}
				}
				catch (Exception ex)
				{
					String sError = "Could not find '" + sElementName + "' due to exception["
							+ ex.getClass().getName() + "]:  " + ex.getMessage();
					Logs.logError(new CheckBoxNoSuchElementException(sError));
				}
			}
			else
			{
				if (checkbox.verifyEnabled)
				{
					String sError = "Check box for '" + sElementName + "' was disabled";
					Logs.logError(new CheckBoxNotEnabled(sError));
				}

				if (bChecked != checkbox.check)
				{
					String sError = "Check box for '" + sElementName
							+ "' was disabled and it was not in the correct desired state (" + checkbox.check
							+ ")";
					Logs.logError(new CheckBoxNotEnabled(sError));
				}
				else
				{
					String sWarn = "Check box for '" + sElementName
							+ "' was disabled but it was in the correct desired state (" + checkbox.check
							+ ")";
					Logs.log.warn(sWarn);
					return;
				}
			}
		}
	}

	/**
	 * Gets the text for the element. Attempts to prevent StaleElementReferenceException by retrying
	 * the specified number times.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Text Visible to user<BR>
	 * 2) Any exception other than StaleElementReferenceException will return the empty string<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the text
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @return non-null
	 */
	public static String getText(WebDriver driver, String sLocator, int retries)
	{
		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = driver.findElement(locatedBy(sLocator));
				String sText = element.getText();
				return Conversion.nonNull(sText);
			}
			catch (StaleElementReferenceException ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then return the empty
				// string
				if (attempts > retries)
					return "";
				else
					sleep(getPollInterval());
			}
			catch (Exception ex)
			{
				return "";
			}
		}
	}

	/**
	 * Waits until the element becomes stable (displayed & enabled) for the specified continuous amount of
	 * time<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The timeout value (seconds) should be greater than continuous (milliseconds)<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find element
	 * @param continuous - The continuous amount of time (milliseconds) that the element must remain displayed
	 *            and enabled
	 */
	public static void waitUntilStable(WebDriver driver, String sLocator, int continuous)
	{
		waitUntilStable(driver, sLocator, getTimeoutInMilliseconds(), continuous);
	}

	/**
	 * Waits until the element becomes stable (displayed & enabled) for the specified continuous amount of
	 * time<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) maxDuration should be greater than continuous<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find element
	 * @param maxDuration - Max Duration (milliseconds) to allow the element to become stable
	 * @param continuous - The continuous amount of time (milliseconds) that the element must remain displayed
	 *            and enabled
	 */
	public static void waitUntilStable(WebDriver driver, String sLocator, int maxDuration, int continuous)
	{
		// Max Timeout for the element to be stable as specified by user
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(maxDuration))
		{
			// Flag to indicate if element is successfully displayed and enabled for the specified time
			boolean bSuccess = true;

			// The amount of time the element needs to be continuously displayed and enabled
			ElapsedTime e2 = new ElapsedTime();
			while (!e2.isTimeout(continuous) && bSuccess)
			{
				try
				{
					WebElement element = findElement(driver, sLocator, false);
					if (element.isDisplayed() && element.isEnabled())
						bSuccess = true;
					else
						bSuccess = false;
				}
				catch (Exception ex)
				{
					bSuccess = false;
				}
			}

			// If the flag is true after the loop, then this indicates that the element was displayed and
			// enabled for the specified time continuously
			if (bSuccess)
				return;
		}

		Logs.logError("Element ('" + sLocator + "') never became stable (displayed and enabled) for "
				+ continuous + " milliseconds in a row before timeout occurred");
	}

	/**
	 * Waits for the element to remain removed for the specified continuous amount of time<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) nMaxWaitTime should be greater than continuous<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find element
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is removed
	 * @param continuous - The continuous amount of time (milliseconds) that the element must remain removed
	 * @return true if element is removed before timeout else false
	 */
	public static boolean isElementRemoved(WebDriver driver, String sLocator, int nMaxWaitTime, int continuous)
	{
		// Max Timeout for the element to be remain removed and not added back as specified by user
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			// Flag to indicate if element is successfully removed for the specified time
			boolean bSuccess = true;

			// The amount of time the element needs to be continuously removed
			ElapsedTime e2 = new ElapsedTime();
			while (!e2.isTimeout(continuous) && bSuccess)
			{
				bSuccess = wasElementRemoved(driver, sLocator);
			}

			// If the flag is true after the loop, then this indicates that the element was removed for the
			// specified time continuously
			if (bSuccess)
				return true;
		}

		return false;
	}

	/**
	 * Waits until the element remains removed for the specified continuous amount of time<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses default timeout from class
	 * 
	 * @param driver
	 * @param sLocator - Locator to find element
	 * @param continuous - The continuous amount of time (milliseconds) that the element must remain removed
	 */
	public static void wasElementRemoved(WebDriver driver, String sLocator, int continuous)
	{
		wasElementRemoved(driver, sLocator, getTimeoutInMilliseconds(), continuous);
	}

	/**
	 * Waits until the element to remain removed for the specified continuous amount of time<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) maxDuration should be greater than continuous<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find element
	 * @param maxDuration - Max Duration (milliseconds) to allow the element to remain removed
	 * @param continuous - The continuous amount of time (milliseconds) that the element must remain removed
	 */
	public static void wasElementRemoved(WebDriver driver, String sLocator, int maxDuration, int continuous)
	{
		if (!isElementRemoved(driver, sLocator, maxDuration, continuous))
		{
			Logs.logError("Element ('" + sLocator + "') never remained removed for " + continuous
					+ " milliseconds in a row before timeout occurred");
		}
	}

	/**
	 * Waits for the element to appear and contain the specified value for an attribute
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sAttributeToCheck - Attribute on the Element to look for
	 * @param sContainsAttributeValue - Value that must be contained in the Attribute value
	 */
	public static void waitForAttributeContains(WebDriver driver, String sLocator, String sAttributeToCheck,
			String sContainsAttributeValue)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			try
			{
				WebElement element = findElement(driver, sLocator, false);
				if (element.getAttribute(sAttributeToCheck).contains(sContainsAttributeValue))
					return;
			}
			catch (Exception ex)
			{
			}

			sleep(getPollInterval());
		}

		// Attribute value never contained the expected value before timeout
		Logs.logError("Attribute ('" + sAttributeToCheck + "') did not contain the value ('"
				+ sContainsAttributeValue + "') for the locator ('" + sLocator + "') before Timeout occurred");
	}

	/**
	 * Waits for the element to contain the specified value for an attribute
	 * 
	 * @param element - WebElement to perform check against
	 * @param sLog - Element name used to log errors
	 * @param sAttributeToCheck - Attribute on the Element to look for
	 * @param sContainsAttributeValue - Value that must be contained in the Attribute value
	 */
	public static void waitForAttributeContains(WebElement element, String sLog, String sAttributeToCheck,
			String sContainsAttributeValue)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			try
			{
				if (element.getAttribute(sAttributeToCheck).contains(sContainsAttributeValue))
					return;
			}
			catch (Exception ex)
			{
			}

			sleep(getPollInterval());
		}

		// Attribute value never contained the expected value before timeout
		Logs.logError("Attribute ('" + sAttributeToCheck + "') did not contain the value ('"
				+ sContainsAttributeValue + "') for '" + sLog + "' before Timeout occurred");
	}

	/**
	 * Waits until the specified locator returns a matching WebElements of at least the specified size (or
	 * timeout occurs.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If minimum number of elements less than 1, then value is ignored<BR>
	 * 2) If timeout occurs, then the latest list is returned which may not have the minimum number of
	 * elements<BR>
	 * 3) The list of elements may be <B>stale</B> by the time they are returned and accessing them may throw
	 * a <B>StaleElementReferenceException</B><BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the list
	 * @param nInterval - Interval (milliseconds) in which to sleep and re-check if the lists sizes are the
	 *            same
	 * @param nMinElementsExpected - Minimum number of elements to wait for
	 * @return List&lt;WebElement&gt;
	 */
	public static List<WebElement> findElementsAJAX(WebDriver driver, String sLocator, int nInterval,
			int nMinElementsExpected)
	{
		return findElementsAJAX(driver, sLocator, nInterval, nMinElementsExpected, false);
	}

	/**
	 * Waits until the specified locator returns a matching WebElements of at least the specified size or
	 * exact size if specified (or timeout occurs.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If minimum number of elements less than 1, then value is ignored<BR>
	 * 2) If timeout occurs, then the latest list is returned which may not have the minimum number of
	 * elements (or exact number specified)<BR>
	 * 3) The list of elements may be <B>stale</B> by the time they are returned and accessing them may throw
	 * a <B>StaleElementReferenceException</B><BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the list
	 * @param nInterval - Interval (milliseconds) in which to sleep and re-check if the lists sizes are the
	 *            same
	 * @param nMinElementsExpected - Minimum number of elements to wait for
	 * @param bExact - true to wait for exactly nMinElementsExpected (no more or no less)
	 * @return List&lt;WebElement&gt;
	 */
	public static List<WebElement> findElementsAJAX(WebDriver driver, String sLocator, int nInterval,
			int nMinElementsExpected, boolean bExact)
	{
		List<WebElement> stable = new ArrayList<WebElement>();

		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			List<WebElement> count1 = findElementsAJAX(driver, sLocator, nMinElementsExpected);
			sleep(nInterval);
			List<WebElement> count2 = findElementsAJAX(driver, sLocator, nMinElementsExpected);

			// It is possible for the lists to have the same size but not have the minimum number of elements
			boolean bSameSize = false;
			if (count1.size() == count2.size())
			{
				// Store latest stable list
				stable = count1;

				// Set flag to indicate stable
				bSameSize = true;
			}

			// If exact number of elements is required, return the stable list if it is the correct size
			if (bExact && count1.size() == nMinElementsExpected && bSameSize)
				return stable;

			// If minimum number of elements is required, return the stable list if has enough elements
			if (!bExact && count1.size() >= nMinElementsExpected && bSameSize)
				return stable;
		}

		// Returns the latest stable list which may not have the minimum number of elements
		return stable;
	}

	/**
	 * Clicks an option from a drop down option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) HTML Attribute set to default of "value"<BR>
	 * 2) Logging flag set to default of true<BR>
	 * 3) This method should only be used on a drop down that does not support normal selection or it has the
	 * drop down structure but is not really a drop down.<BR>
	 * 4) Supports all ways to select a drop down option (1 - By Value, 2 - By Index, 3 - Regular Expression,
	 * Default - Visible Text)<BR>
	 * 5) Supports random selection for By Index when option to select converts to less than 0<BR>
	 * 6) Drop down must have at least 1 option<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the 'drop down' options
	 * @param sLogName - Log Name to be used
	 * @param dd - Object that contains information on which drop down option to select
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 * @throws ClickNoSuchElementException if could not find the element
	 */
	public static void dropDownClick(WebDriver driver, String sLocator, String sLogName, DropDown dd)
	{
		dropDownClick(driver, sLocator, getInputAttr(), sLogName, true, dd);
	}

	/**
	 * Clicks an option from a drop down option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used on a drop down that does not support normal selection or it has the
	 * drop down structure but is not really a drop down.<BR>
	 * 2) Supports all ways to select a drop down option (1 - By Value, 2 - By Index, 3 - Regular Expression,
	 * Default - Visible Text)<BR>
	 * 3) Supports random selection for By Index when option to select converts to less than 0<BR>
	 * 4) Drop down must have at least 1 option<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the 'drop down' options
	 * @param sHTML_Attribute - HTML Attribute to be used to match HTML Value when this criteria is used
	 * @param sLogName - Log Name to be used
	 * @param bLog - if true then log error else log warning
	 * @param dd - Object that contains information on which drop down option to select
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 * @throws ClickNoSuchElementException if could not find the element
	 */
	public static void dropDownClick(WebDriver driver, String sLocator, String sHTML_Attribute,
			String sLogName, boolean bLog, DropDown dd)
	{
		dropDownClick(driver, sLocator, sHTML_Attribute, sLogName, LogErrorLevel.convert(bLog), dd);
	}

	/**
	 * Clicks an option from a drop down option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used on a drop down that does not support normal selection or it has the
	 * drop down structure but is not really a drop down.<BR>
	 * 2) Supports all ways to select a drop down option (1 - By Value, 2 - By Index, 3 - Regular Expression,
	 * Default - Visible Text)<BR>
	 * 3) Supports random selection for By Index when option to select converts to less than 0<BR>
	 * 4) Drop down must have at least 1 option<BR>
	 * 5) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 6) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 7) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the 'drop down' options
	 * @param sHTML_Attribute - HTML Attribute to be used to match HTML Value when this criteria is used
	 * @param sLogName - Log Name to be used
	 * @param level - Control error logging written
	 * @param dd - Object that contains information on which drop down option to select
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be selected
	 * @throws DropDownIndexException if could not find option (index) to be selected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be selected
	 * @throws DropDownNoSuchElementException if could not find the element
	 * @throws ClickNoSuchElementException if could not find the element
	 */
	private static void dropDownClick(WebDriver driver, String sLocator, String sHTML_Attribute,
			String sLogName, LogErrorLevel level, DropDown dd)
	{
		try
		{
			if (Selection.Skip == dd.using)
			{
				Logs.log.info("Skipped entering '" + sLogName + "'");
				return;
			}

			// Set the logging information
			String sLogging = sLogName;
			if (Selection.Index == dd.using)
				sLogging += " using Index (" + dd.option + ")";
			else if (Selection.ValueHTML == dd.using)
				sLogging += " using HTML Value (" + dd.option + ")";
			else if (Selection.RegEx == dd.using)
				sLogging += " using RegEx (" + dd.option + ")";
			else
				sLogging += " using Visible Text (" + dd.option + ")";

			// Get the element to work with
			WebElement element = findElement(driver, sLocator, sHTML_Attribute, dd);

			// Was an element found?
			if (element == null)
			{
				Logs.log.warn("No options could be found for " + sLogging + " and locator ('" + sLocator
						+ "')");

				if (Selection.Index == dd.using)
					throw new NumberFormatException("");
				else if (Selection.ValueHTML == dd.using)
					throw new NoSuchElementException("");
				else if (Selection.RegEx == dd.using)
					throw new DropDownPartialMatchException("");
				else
					throw new NoSuchElementException("");
			}

			// Is the element enabled?
			if (!element.isEnabled())
				throw new ElementNotEnabledException("");

			// Is the element displayed?
			if (!element.isDisplayed())
				throw new ElementNotDisplayedException("");

			// Click the element and return
			click(element, sLogging, LogErrorLevel.NONE);
			return;
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sLogName + "') was not enabled";
			Logs.logWarnError(level, new ElementNotEnabledException(sError));
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sLogName + "') was not displayed";
			Logs.logWarnError(level, new ElementNotDisplayedException(sError));
		}
		catch (NoSuchElementException nsee)
		{
			String sError = "Could not find '" + dd.option + "' in the drop down ('" + sLogName + "')";
			Logs.logWarnError(level, new DropDownSelectionException(sError));
		}
		catch (NumberFormatException nfe)
		{
			String sError = "Could not find index '" + dd.option + "' in the drop down ('" + sLogName + "')";
			Logs.logWarnError(level, new DropDownIndexException(sError));
		}
		catch (DropDownPartialMatchException ddpme)
		{
			String sError = "Could not find a partial match using the regular expression '" + dd.option
					+ "' in the drop down ('" + sLogName + "')";
			Logs.logWarnError(level, new DropDownPartialMatchException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Drop down ('" + sLogName
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new DropDownNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			String sError = "Could not find drop down ('" + sLogName + "') due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new DropDownNoSuchElementException(sError));
		}
	}

	/**
	 * Finds a matching element from a list of options
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the options
	 * @param sHTML_Attribute - HTML Attribute to be used to match HTML Value when this criteria is used
	 * @param dd - Object that contains information on which option to select
	 * @return null if no match or skip else matching WebElement
	 */
	public static WebElement findElement(WebDriver driver, String sLocator, String sHTML_Attribute,
			DropDown dd)
	{
		// Contains the element to be returned
		WebElement element = null;

		// Does user want to skip?
		if (Selection.Skip == dd.using)
			return element;

		// Get all options available
		List<WebElement> options = findElementsAJAX(driver, sLocator, 1);

		// *****
		// Deal with Index first
		// *****
		if (Selection.Index == dd.using)
		{
			// Get user's selection
			int nIndex = Conversion.parseInt(dd.option);

			// Random selection?
			if (dd.using == Selection.Index && nIndex < 0)
				nIndex = Rand.randomRange(0, options.size() - 1);

			// It is possible that the index specified by the user is invalid based on options available
			try
			{
				element = options.get(nIndex);
			}
			catch (Exception ex)
			{
				element = null;
			}

			return element;
		}

		// *****
		// Deal with any of the other options
		// *****
		for (int i = 0; i < options.size(); i++)
		{
			// Get WebElement to compare against criteria
			element = options.get(i);

			// Break loop if match is found
			String sCompare;
			if (Selection.ValueHTML == dd.using)
			{
				// Check the HTML Value for a match
				sCompare = Conversion.nonNull(getAttribute(element, sHTML_Attribute));
				if (sCompare.equals(dd.option))
					return element;
			}
			else
			{
				sCompare = Conversion.nonNull(getText(element));
				if (Selection.RegEx == dd.using)
				{
					// Check for a regular expression match
					if (sCompare.matches(dd.option))
						return element;
				}
				else
				{
					// Check for a visible text match
					if (sCompare.equals(dd.option))
						return element;
				}
			}
		}

		// Make element null as no match found
		element = null;
		return element;
	}

	/**
	 * Checks if element's text matches the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param element - Element to check text
	 * @param criteria - Criteria for the text comparison
	 * @param sExpectedText - Expected text to match the criteria (non-null)
	 * @return true if element has text matching criteria else false
	 */
	public static boolean isTextDisplayed(WebElement element, Comparison criteria, String sExpectedText)
	{
		String actual = Conversion.nonNull(getText(element)).trim();
		String expected = Conversion.nonNull(sExpectedText).trim();
		return Compare.text(actual, expected, criteria);
	}

	/**
	 * Waits until element's text matches the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param element - Element to check text
	 * @param criteria - Criteria for the text comparison
	 * @param maxDuration - Max Duration (milliseconds) to allow the element's text to match criteria
	 * @param sExpectedText - Expected text to match the criteria (non-null)
	 * @return true if element has text matching criteria within timeout
	 */
	public static boolean isTextDisplayed(WebElement element, Comparison criteria, int maxDuration,
			String sExpectedText)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(maxDuration))
		{
			if (isTextDisplayed(element, criteria, sExpectedText))
				return true;
			else
				sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Waits until element's text matches the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element to check text
	 * @param criteria - Criteria for the text comparison
	 * @param maxDuration - Max Duration (milliseconds) to allow the element's text to match criteria
	 * @param sExpectedText - Expected text to match the criteria (non-null)
	 * @return true if element has text matching criteria within timeout
	 */
	public static boolean isTextDisplayed(WebDriver driver, String sLocator, Comparison criteria,
			int maxDuration, String sExpectedText)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(maxDuration))
		{
			// Get element
			WebElement element = findElement(driver, sLocator, false);

			// Try to do just one check of the element by using 1 second as max duration here
			if (isTextDisplayed(element, criteria, 1000, sExpectedText))
				return true;

			// Sleep to allow time for element to be refreshed before next check
			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Waits until element's text matches the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param element - Element to check text
	 * @param criteria - Criteria for the text comparison
	 * @param sExpectedText - Expected text to match the criteria
	 * @throws GenericUnexpectedException if element does not have the text matching criteria within timeout
	 */
	public static void waitForText(WebElement element, Comparison criteria, String sExpectedText)
	{
		if (!isTextDisplayed(element, criteria, getTimeoutInMilliseconds(), sExpectedText))
		{
			String sCriteria;
			if (criteria == Comparison.NotEqual)
				sCriteria = "Not Equal to '" + sExpectedText + "'";
			else if (criteria == Comparison.Equal)
				sCriteria = "Equal to '" + sExpectedText + "'";
			else if (criteria == Comparison.EqualsIgnoreCase)
				sCriteria = "Equals Ignore Case to '" + sExpectedText + "'";
			else if (criteria == Comparison.RegEx)
				sCriteria = "Match using Regular Expression '" + sExpectedText + "'";
			else if (criteria == Comparison.DoesNotContain)
				sCriteria = "Does Not Contain '" + sExpectedText + "'";
			else
				sCriteria = "Contain '" + sExpectedText + "'";

			Logs.logError("The element's text did not meet criteria (" + sCriteria
					+ ") before timeout occurred");
		}
	}

	/**
	 * Waits until element's text matches the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element to check text
	 * @param criteria - Criteria for the text comparison
	 * @param sExpectedText - Expected text to match the criteria
	 * @throws GenericUnexpectedException if element does not have the text matching criteria within timeout
	 */
	public static void waitForText(WebDriver driver, String sLocator, Comparison criteria,
			String sExpectedText)
	{
		if (!isTextDisplayed(driver, sLocator, criteria, getTimeoutInMilliseconds(), sExpectedText))
		{
			String sCriteria;
			if (criteria == Comparison.NotEqual)
				sCriteria = "Not Equal to '" + sExpectedText + "'";
			else if (criteria == Comparison.Equal)
				sCriteria = "Equal to '" + sExpectedText + "'";
			else if (criteria == Comparison.EqualsIgnoreCase)
				sCriteria = "Equals Ignore Case to '" + sExpectedText + "'";
			else if (criteria == Comparison.RegEx)
				sCriteria = "Match using Regular Expression '" + sExpectedText + "'";
			else if (criteria == Comparison.DoesNotContain)
				sCriteria = "Does Not Contain '" + sExpectedText + "'";
			else
				sCriteria = "Contain '" + sExpectedText + "'";

			Logs.logError("The element's text did not meet criteria (" + sCriteria
					+ ") before timeout occurred");
		}
	}

	/**
	 * Triggers an onchange event for the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Any error will generate an exception regardless of logging flag<BR>
	 * 
	 * @param element - Element for which to trigger the onchange event
	 * @param sLog - Log Name to be used
	 * @param bLog - true to log all events
	 */
	public static void triggerOnChange(WebElement element, String sLog, boolean bLog)
	{
		HTML_Event event = new HTML_Event(HTML_EventType.change);
		triggerHTMLEvent(event, element, sLog, bLog);
	}

	/**
	 * Triggers an onblur event for the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Any error will generate an exception regardless of logging flag<BR>
	 * 
	 * @param element - Element for which to trigger the onblur event
	 * @param sLog - Log Name to be used
	 * @param bLog - true to log all events
	 */
	public static void triggerOnBlur(WebElement element, String sLog, boolean bLog)
	{
		HTML_Event event = new HTML_Event(HTML_EventType.blur);
		triggerHTMLEvent(event, element, sLog, bLog);
	}

	/**
	 * Triggers an onfocus event for the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Any error will generate an exception regardless of logging flag<BR>
	 * 
	 * @param element - Element for which to trigger the onfocus event
	 * @param sLog - Log Name to be used
	 * @param bLog - true to log all events
	 */
	public static void triggerOnFocus(WebElement element, String sLog, boolean bLog)
	{
		HTML_Event event = new HTML_Event(HTML_EventType.focus);
		triggerHTMLEvent(event, element, sLog, bLog);
	}

	/**
	 * Select a frame using an element
	 * 
	 * @param iframe - The frame element to switch to
	 * @param sFrameName - Frame Name for logging purposes
	 */
	public void frame(WebElement iframe, String sFrameName)
	{
		try
		{
			driver.switchTo().frame(iframe);
		}
		catch (Exception ex)
		{
			Logs.logError("Switching to iframe '" + sFrameName + "' failed due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
		}
	}

	/**
	 * Selects either the first frame on the page, or the main document when a page contains iframes.
	 */
	public void frameDefaultContent()
	{
		try
		{
			driver.switchTo().defaultContent();
		}
		catch (Exception ex)
		{
			Logs.logError("Could not focus the top window/first frame due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
		}
	}

	/**
	 * Returns the active element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) When it is difficult or impossible to get an element in the frame, this method can be used to get an
	 * element to work with<BR>
	 * 2) This matches the semantics of calling "document.activeElement" in JavaScript<BR>
	 * 
	 * @return WebElement
	 */
	public WebElement frameActiveElement()
	{
		WebElement active = null;
		try
		{
			active = driver.switchTo().activeElement();
		}
		catch (Exception ex)
		{
			Logs.logError("Could not get active element due to exception[" + ex.getClass().getName() + "]:  "
					+ ex.getMessage());
		}

		return active;
	}

	/**
	 * Checks if the element is displayed & enabled on the page
	 * 
	 * @param element - WebElement to check if displayed & enabled
	 * @return true if element displayed & enabled else false
	 */
	public static boolean isElementReady(WebElement element)
	{
		try
		{
			// Check if displayed
			if (element.isDisplayed() && element.isEnabled())
				return true;
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Checks if the element is displayed & enabled on the page<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method will only work if element always remains in the DOM but the attributes are changed when
	 * visible to user<BR>
	 * 2) If element is removed and added, then use overloaded method that takes a WebDriver<BR>
	 * 
	 * @param element - WebElement to check if displayed & enabled
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is ready
	 * @return true if element becomes displayed & enabled else false
	 */
	public static boolean isElementReady(WebElement element, int nMaxWaitTime)
	{
		// Max Timeout for the element to be ready as specified by user
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			if (isElementReady(element))
				return true;

			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Checks if the element is displayed & enabled on the page
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is ready
	 * @return true if element becomes displayed & enabled else false
	 */
	public static boolean isElementReady(WebDriver driver, String sLocator, int nMaxWaitTime)
	{
		// Max Timeout for the element to be ready as specified by user
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			WebElement element = findElement(driver, sLocator, false);
			if (isElementReady(element))
				return true;

			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Waits for the element to become ready (displayed & enabled)
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element
	 */
	public static void waitForElementReady(WebDriver driver, String sLocator)
	{
		waitForElementReady(driver, sLocator, getTimeoutInMilliseconds());
	}

	/**
	 * Waits for the element to become ready (displayed & enabled)
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is ready
	 */
	public static void waitForElementReady(WebDriver driver, String sLocator, int nMaxWaitTime)
	{
		if (!isElementReady(driver, sLocator, nMaxWaitTime))
		{
			Logs.logError("Element ('" + sLocator
					+ "') never became ready (displayed & enabled) before timeout occurred");
		}
	}

	/**
	 * Waits for the element to become ready (displayed & enabled)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Element must already exist in the DOM and not be removed/added. If not this method will never be
	 * successful (as a stale element exception will always cause the result to be false.)<BR>
	 * 
	 * @param element - Element to wait to become ready
	 * @param sLog - Element name to log if error occurs
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is ready
	 */
	public static void waitForElementReady(WebElement element, String sLog, int nMaxWaitTime)
	{
		if (!isElementReady(element, nMaxWaitTime))
		{
			Logs.logError("Element ('" + sLog
					+ "') never became ready (displayed & enabled) before timeout occurred");
		}
	}

	/**
	 * Used to for hover over menus and clicking specific menu option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) List must contain at least 1 element<BR>
	 * 2) Hover over occurs in order<BR>
	 * 3) Last element is hovered over and clicked<BR>
	 * 
	 * @param hoverElements - List of elements in order to hover over. (Last one is also clicked.)
	 */
	public static void hoverClick(List<WebElement> hoverElements)
	{
		try
		{
			// Get driver to use from first element in list (assumes at least 1 element in list)
			WebDriver useDriver = getWebDriver(hoverElements.get(0));
			Actions actions = new Actions(useDriver);
			for (int i = 0; i < hoverElements.size(); i++)
			{
				actions.moveToElement(hoverElements.get(i));
				actions.perform();
				waitForDisplayedElement(hoverElements.get(i), "" + (i + 1), false);
				Logs.log.info("Move Action successful for element (" + (i + 1) + ")");

				// Click the last element
				if (i == hoverElements.size() - 1)
				{
					actions.click();
					actions.perform();
					Logs.log.info("Mouse Click successful for element (" + (i + 1) + ")");
				}
			}
		}
		catch (Exception ex)
		{
			String sError = "Moving Mouse and Clicking failed due to exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage();
			Logs.logError(new MouseActionsException(sError));
		}
	}

	/**
	 * Get List of Window Handles
	 * 
	 * @return non-null
	 */
	public List<String> getWindowHandles()
	{
		List<String> existing = new ArrayList<String>();

		try
		{
			existing.addAll(driver.getWindowHandles());
		}
		catch (Exception ex)
		{
		}

		return existing;
	}

	/**
	 * Enters value into specified field. (This is specifically for fields that have default values
	 * as it gives the flexibility to skip the field, enter a specific or random value.)<BR>
	 * <BR>
	 * <B>Note: </B> If value is null or it is specified to skip, then the field is skipped<BR>
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param value - Object contains information on if to enter field and value to enter if necessary
	 */
	public static void onlyEnterField(WebElement element, String sElementName, InputField value)
	{
		if (value != null && value.modifyField())
		{
			// Enter value
			onlyEnterField(element, sElementName, value.getValueToInput());
		}
		else
		{
			// Skip the field but log some useful information
			String sValue = Conversion.nonNull(getAttribute(element, getInputAttr()));
			Logs.log.info("The field '" + sElementName + "' was skipped.  Default value was '" + sValue + "'");
		}
	}

	/**
	 * Closes the current window that has focus
	 * 
	 * @param sLog_WindowName - Window Name for logging purposes
	 */
	public void close(String sLog_WindowName)
	{
		try
		{
			driver.close();
			Logs.log.info("Closed window ('" + sLog_WindowName + "') successfully");
		}
		catch (Exception ex)
		{
			Logs.log.warn("Could not close window ('" + sLog_WindowName + "') due to Exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
		}
	}

	/**
	 * Switches to window and closes<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Parameter.param = Page Name / Window Name<BR>
	 * 2) Parameter.value = Window Handle<BR>
	 * 
	 * @param windowInfo - Contains window handle to switch to and window name for logging purposes
	 * @throws NotFoundWindowException if cannot switch to window using handle
	 * @throws CloseWindowException if cannot close window after switching to it
	 */
	public void close(Parameter windowInfo)
	{
		boolean bSwitchTo = false;
		try
		{
			driver.switchTo().window(windowInfo.value);
			bSwitchTo = true;
			driver.close();
		}
		catch (Exception ex)
		{
			String sError;
			if (bSwitchTo)
			{
				sError = "Could not close window ('" + windowInfo.param + "') due to Exception ["
						+ ex.getClass().getName() + "]:  " + ex.getMessage();
				Logs.logError(new CloseWindowException(sError));
			}
			else
			{
				sError = "Could not switch to window ('" + windowInfo.param + "') using handle ('"
						+ windowInfo.value + "') due to Exception [" + ex.getClass().getName() + "]:  "
						+ ex.getMessage();
				Logs.logError(new NotFoundWindowException(sError));
			}
		}
	}

	/**
	 * Number of Retries to prevent StaleElementReferenceException for actions that require it
	 * 
	 * @return nRetries
	 */
	public int getRetries()
	{
		return nRetries;
	}

	/**
	 * Timeout Multiplier when timeout is not sufficient
	 * 
	 * @return nTimeoutMultiplier
	 */
	public int getMultiplier()
	{
		return nTimeoutMultiplier;
	}

	/**
	 * The continuous amount of time (milliseconds) that the element must be stable for
	 * 
	 * @return nStableTime
	 */
	public int getStableTime()
	{
		return nStableTime;
	}

	/**
	 * Checks if an element specified by the locator contains the specified value for an attribute
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sAttributeToCheck - Attribute on the Element to look for
	 * @param sContainsAttributeValue - Value that must be contained in the Attribute value
	 * @return true if element specified by the locator exists & attribute exists & contains the specified
	 *         value
	 */
	public static boolean containsAttrValue(WebDriver driver, String sLocator, String sAttributeToCheck,
			String sContainsAttributeValue)
	{
		WebElement element = findElement(driver, sLocator, false);
		return containsAttrValue(element, sAttributeToCheck, sContainsAttributeValue);
	}

	/**
	 * Checks if the element contains the specified value for an attribute
	 * 
	 * @param element -
	 * @param sAttributeToCheck - Attribute on the Element to look for
	 * @param sContainsAttributeValue - Value that must be contained in the Attribute value
	 * @return true if element exists & attribute exists & contains the specified value
	 */
	public static boolean containsAttrValue(WebElement element, String sAttributeToCheck,
			String sContainsAttributeValue)
	{
		try
		{
			if (element.getAttribute(sAttributeToCheck).contains(sContainsAttributeValue))
				return true;
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Wait for pop-up windows to close<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Waits until timeout for the number of windows to be nExpectedWindows<BR>
	 * 2) Need to have taken some action that will close windows<BR>
	 * 
	 * @param nCurrentWindows - Current Number of Open Windows
	 * @param nExpectedWindows - Wait until there are the expected number of open windows before returning
	 */
	public void waitForPopupToClose(int nCurrentWindows, int nExpectedWindows)
	{
		// Store number of open windows in case of error
		int nOpenWindows = nCurrentWindows;

		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			List<String> openWindows = getWindowHandles();
			nOpenWindows = openWindows.size();
			if (nOpenWindows == nExpectedWindows)
				return;

			sleep(getPollInterval());
		}

		Logs.logError("Number of open windows was " + nOpenWindows + " at timeout but expected "
				+ nExpectedWindows);
	}

	/**
	 * Wait for pop-up windows to close<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Waits until timeout for the number of windows to be nExpectedWindows<BR>
	 * 2) Need to have taken some action that will close windows<BR>
	 * 
	 * @param nExpectedWindows - Wait until there are the expected number of open windows before returning
	 */
	public void waitForPopupToClose(int nExpectedWindows)
	{
		waitForPopupToClose(-1, nExpectedWindows);
	}

	/**
	 * Switches to the main window (from initialization)
	 */
	public void workWithMainWindow()
	{
		if (switchToPopupWindow(getMainWindowHandle()))
		{
			Logs.log.info("Switched to the main window ('" + getPageName() + "') successfully");
		}
		else
		{
			Logs.logError("Could not switch to the main window ('" + getPageName() + "')");
		}
	}

	/**
	 * Used to for hover over menus and return list of displayed menu options<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) List must contain at least 1 element<BR>
	 * 2) Hover over occurs in order<BR>
	 * 3) Last element is hovered over and the displayed menu options are gathered using the xpath<BR>
	 * 
	 * @param hoverElements - List of elements in order to hover over
	 * @param sXpath_MenuOptions - xpath to get the displayed menu options after last hover over
	 * @return List&lt;String&gt;
	 */
	public static List<String> hover(List<WebElement> hoverElements, String sXpath_MenuOptions)
	{
		List<String> menuOptionsText = new ArrayList<String>();

		try
		{
			// Get driver to use from first element in list (assumes at least 1 element in list)
			WebDriver useDriver = getWebDriver(hoverElements.get(0));
			Actions actions = new Actions(useDriver);
			for (int i = 0; i < hoverElements.size(); i++)
			{
				actions.moveToElement(hoverElements.get(i));
				actions.perform();
				waitForDisplayedElement(hoverElements.get(i), "" + (i + 1), false);
				Logs.log.info("Move Action successful for element (" + (i + 1) + ")");

				// Get displayed menu options on the last element hover
				if (i == hoverElements.size() - 1)
				{
					List<WebElement> menuOptions = findElementsAJAX(useDriver, sXpath_MenuOptions, 0);
					for (WebElement option : menuOptions)
					{
						menuOptionsText.add(Conversion.nonNull(getText(option)));
					}
				}
			}
		}
		catch (Exception ex)
		{
			String sError = "Moving Mouse failed due to exception [" + ex.getClass().getName() + "]:  "
					+ ex.getMessage();
			Logs.logError(new MouseActionsException(sError));
		}

		return menuOptionsText;
	}

	/**
	 * Used to for hover over menus and return list of displayed menu options<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Hover over occurs in order<BR>
	 * 2) Last element is hovered over and the displayed menu options are gathered using the xpath<BR>
	 * 
	 * @param driver
	 * @param locatorsForHoverActions - List of locators in order to hover over
	 * @param sXpath_MenuOptions - xpath to get the displayed menu options after last hover over
	 * @return List&lt;String&gt;
	 */
	public static List<String> hover(WebDriver driver, List<String> locatorsForHoverActions,
			String sXpath_MenuOptions)
	{
		List<String> menuOptionsText = new ArrayList<String>();

		try
		{
			Actions actions = new Actions(driver);

			for (int i = 0; i < locatorsForHoverActions.size(); i++)
			{
				// Get locator
				String sLocator = locatorsForHoverActions.get(i);

				// Find element & move mouse over it
				WebElement element = findElement(driver, sLocator);
				actions.moveToElement(element);
				actions.perform();
				waitForDisplayedElement(driver, sLocator, false);
				Logs.log.info("Move Action successful for locator:  " + sLocator);

				// Get displayed menu options on the last element hover
				if (i == locatorsForHoverActions.size() - 1)
				{
					List<WebElement> menuOptions = findElementsAJAX(driver, sXpath_MenuOptions, 0);
					for (WebElement option : menuOptions)
					{
						String sMenuText = Conversion.nonNull(getText(option));
						if (!sMenuText.equals(""))
							menuOptionsText.add(sMenuText);
					}
				}
			}
		}
		catch (Exception ex)
		{
			String sError = "Moving Mouse failed due to exception [" + ex.getClass().getName() + "]:  "
					+ ex.getMessage();
			Logs.logError(new MouseActionsException(sError));
		}

		return menuOptionsText;
	}

	/**
	 * Gets the input value on the WebElement (field)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For input fields the attribute "value" contains the entered data<BR>
	 * 
	 * @param element - Field for which to get input value
	 * @return non-null
	 */
	public static String getInputValue(WebElement element)
	{
		return Conversion.nonNull(getAttribute(element, getInputAttr()));
	}

	/**
	 * Trigger mouse event using JavaScript
	 * 
	 * @param event - Mouse Event Object
	 * @param element - Element to trigger mouse event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void triggerMouseEvent(MouseEvent event, WebElement element, String sLog, boolean bLogAll)
	{
		JS_Util.triggerMouseEvent(event, element, sLog, bLogAll);
	}

	/**
	 * Trigger HTML event using JavaScript
	 * 
	 * @param event - HTML Event Object
	 * @param element - Element to trigger HTML event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void triggerHTMLEvent(HTML_Event event, WebElement element, String sLog, boolean bLogAll)
	{
		JS_Util.triggerHTMLEvent(event, element, sLog, bLogAll);
	}

	/**
	 * Trigger mouse event using JavaScript
	 * 
	 * @param event - Mouse Event Object
	 * @param driver
	 * @param sLocator - Locator to element to trigger mouse event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void triggerMouseEvent(MouseEvent event, WebDriver driver, String sLocator, String sLog,
			boolean bLogAll)
	{
		triggerMouseEvent(event, findElement(driver, sLocator, false), sLog, bLogAll);
	}

	/**
	 * Trigger HTML event using JavaScript
	 * 
	 * @param event - HTML Event Object
	 * @param driver
	 * @param sLocator - Locator to element to trigger HTML event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void triggerHTMLEvent(HTML_Event event, WebDriver driver, String sLocator, String sLog,
			boolean bLogAll)
	{
		triggerHTMLEvent(event, findElement(driver, sLocator, false), sLog, bLogAll);
	}

	/**
	 * Trigger Mouse Over event using JavaScript
	 * 
	 * @param element - Element to trigger mouse over event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void mouseOver(WebElement element, String sLog, boolean bLogAll)
	{
		MouseEvent event = new MouseEvent(MouseEventType.mouseover);
		triggerMouseEvent(event, element, sLog, bLogAll);
	}

	/**
	 * Trigger Mouse Over event using JavaScript
	 * 
	 * @param driver
	 * @param sLocator - Locator to element to trigger mouse over event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void mouseOver(WebDriver driver, String sLocator, String sLog, boolean bLogAll)
	{
		mouseOver(findElement(driver, sLocator, false), sLog, bLogAll);
	}

	/**
	 * Trigger Mouse Out event using JavaScript
	 * 
	 * @param element - Element to trigger mouse out event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void mouseOut(WebElement element, String sLog, boolean bLogAll)
	{
		MouseEvent event = new MouseEvent(MouseEventType.mouseout);
		triggerMouseEvent(event, element, sLog, bLogAll);
	}

	/**
	 * Trigger Mouse Out event using JavaScript
	 * 
	 * @param driver
	 * @param sLocator - Locator to element to trigger mouse out event
	 * @param sLog - Element Name for logging
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void mouseOut(WebDriver driver, String sLocator, String sLog, boolean bLogAll)
	{
		mouseOut(findElement(driver, sLocator, false), sLog, bLogAll);
	}

	/**
	 * Checks if any of the locators has a corresponding ready element
	 * 
	 * @param driver
	 * @param locators - List of locators to elements to check if ready
	 * @param displayOnly - true to only consider if element is displayed (instead of ready)
	 * @return null if none of the locators have a corresponding ready element else the locator that
	 *         corresponds to the ready element
	 */
	public static String getLocator(WebDriver driver, List<String> locators, boolean displayOnly)
	{
		for (int i = 0; i < locators.size(); i++)
		{
			String sLocator = locators.get(i);
			WebElement element = findElement(driver, sLocator, false);
			if (displayOnly)
			{
				if (isElementDisplayed(element))
					return sLocator;
			}
			else
			{
				if (isElementReady(element))
					return sLocator;
			}
		}

		return null;
	}

	/**
	 * Returns the 1st locator that has a ready element
	 * 
	 * @param driver
	 * @param locators - List of locators to elements to check if ready
	 * @param bDisplayOnly - true to only consider if element is displayed (instead of ready)
	 * @throws GenericUnexpectedException if no locator returns any ready element before timeout
	 * @return String
	 */
	public static String getLocatorReadyElement(WebDriver driver, List<String> locators, boolean bDisplayOnly)
	{
		return getLocatorReadyElement(driver, locators, bDisplayOnly, getTimeoutInMilliseconds());
	}

	/**
	 * Returns the 1st locator that has a ready element
	 * 
	 * @param driver
	 * @param locators - List of locators to elements to check if ready
	 * @param bDisplayOnly - true to only consider if element is displayed (instead of ready)
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is displayed
	 * @throws GenericUnexpectedException if no locator returns any ready element before timeout
	 * @return String
	 */
	public static String getLocatorReadyElement(WebDriver driver, List<String> locators,
			boolean bDisplayOnly, int nMaxWaitTime)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			String sLocator = getLocator(driver, locators, bDisplayOnly);
			if (!Compare.equals(sLocator, "", Comparison.Equal))
				return sLocator;

			sleep(getPollInterval());
		}

		String sOption;
		if (bDisplayOnly)
			sOption = "ready";
		else
			sOption = "displayed";

		String sLog_Locators = Conversion.toString(locators, ", ");
		Logs.logError("No element became " + sOption + " using any of the locators (" + sLog_Locators
				+ ") before timeout occurred");
		return null;
	}

	/**
	 * Tabs off the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses END key to go to the end of the field before TAB key is sent<BR>
	 * 
	 * @param element - Element to tab off
	 * @param sLog - Element name used in logging
	 * @param bLogAll - true to log success else only failure is logged
	 */
	public static void tabOff(WebElement element, String sLog, boolean bLogAll)
	{
		try
		{
			element.sendKeys(Keys.END);
			element.sendKeys(Keys.TAB);

			if (bLogAll)
				Logs.log.info("Tabbed off field ('" + sLog + "') successfully");
		}
		catch (Exception ex)
		{
			Logs.logError("Tabbing off field ('" + sLog + "') failed due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
		}
	}

	/**
	 * Checks if the element is fresh by using a method that will cause a StaleElementReferenceException if it
	 * is not fresh
	 * 
	 * @param element - Element to check
	 * @return true if no StaleElementReferenceException occurs else false
	 */
	public static boolean isElementFresh(WebElement element)
	{
		try
		{
			element.getTagName();
			return true;
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Checks if element was refreshed within the specified timeout
	 * 
	 * @param element - Element to check
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @return true if StaleElementReferenceException occurs within timeout else false
	 */
	public static boolean wasElementRefreshed(WebElement element, int nMaxWaitTime)
	{
		// Max Timeout for the element to be refreshed as specified by user
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			if (!isElementFresh(element))
				return true;

			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Checks if element was refreshed within the timeout<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The current timeout value is used<BR>
	 * 
	 * @param element - Element to check
	 * @return true if StaleElementReferenceException occurs within timeout else false
	 */
	public static boolean wasElementRefreshed(WebElement element)
	{
		return wasElementRefreshed(element, getTimeoutInMilliseconds());
	}

	/**
	 * Waits for the element to be refreshed the specified number of times. (After each refresh the timeout is
	 * reset for the next refresh.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) At least 1 refresh always occurs regardless of the refreshes value<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find element
	 * @param sLog - Element name to log (if error)
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is refreshed
	 * @param refreshes - Number of times to wait for refresh
	 * @throws GenericUnexpectedException if any refresh does not occur
	 */
	public static void waitForElementRefresh(WebDriver driver, String sLocator, String sLog,
			int nMaxWaitTime, int refreshes)
	{
		int refreshCount = 0;
		while (true)
		{
			WebElement element = findElement(driver, sLocator);
			if (!wasElementRefreshed(element, nMaxWaitTime))
				Logs.logError("Element ('" + sLog + "') did not refresh within the specified timeout");
			else
				refreshCount++;

			if (refreshCount > refreshes)
				return;
		}
	}

	/**
	 * Waits for the element to be refreshed one time<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The current timeout value is used<BR>
	 * 
	 * @param element - Element to check
	 * @param sLog - Element name to log (if error)
	 * @throws GenericUnexpectedException if refresh does not occur
	 */
	public static void waitForElementRefresh(WebElement element, String sLog)
	{
		if (!wasElementRefreshed(element, getTimeoutInMilliseconds()))
			Logs.logError("Element ('" + sLog + "') did not refresh within the specified timeout");
	}

	/**
	 * Searches each of the open windows for any of the specified URLs and returns the handle of the 1st
	 * window that is a match<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Always switches back to main window before returning<BR>
	 * 2) Checks if the window URL contains the any of the URLs<BR>
	 * 3) Returns immediately if a matching window is found without checking any other windows<BR>
	 * 
	 * @param urls - List of URLs to search for
	 * @return empty string if cannot find window with any of the specified URLs else handle of corresponding
	 *         window
	 */
	public String getWindowHandle(List<String> urls)
	{
		List<String> existing = getWindowHandles();
		for (String handle : existing)
		{
			try
			{
				// Switch to window and check URL
				driver.switchTo().window(handle);
				String sCurrentURL = driver.getCurrentUrl();

				for (String url : urls)
				{
					if (sCurrentURL.contains(url))
					{
						// Switch back to main window before returning handle
						driver.switchTo().window(getMainWindowHandle());
						return handle;
					}
				}
			}
			catch (Exception ex)
			{
			}
		}

		// Switch back to main window before returning empty string
		try
		{
			driver.switchTo().window(getMainWindowHandle());
		}
		catch (Exception ex)
		{
		}

		return "";
	}

	/**
	 * Searches each of the open windows for the specified URL and returns the handle of the 1st window that
	 * is a match<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Always switches back to main window before returning<BR>
	 * 2) Checks if the window URL contains the URL<BR>
	 * 3) Returns immediately if a matching window is found without checking any other windows<BR>
	 * 
	 * @param url - URL to search for
	 * @return empty string if cannot find window with specified URL else handle of corresponding window
	 */
	public String getWindowHandle(String url)
	{
		List<String> urls = new ArrayList<String>();
		urls.add(url);
		return getWindowHandle(urls);
	}

	/**
	 * Waits for an alert to appear, clears alert and returns alert message
	 * 
	 * @param driver
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait for alert to appear
	 * @param bAccept - true to click 'OK' (accept)
	 * @param bLog - true to write to log OR false to write no log information
	 * @return Message text of the Alert or null if an error occurred
	 */
	public static String waitForAlert(WebDriver driver, int nMaxWaitTime, boolean bAccept, boolean bLog)
	{
		Alert alert = null;
		boolean bNoAlert = true;
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime) && bNoAlert)
		{
			try
			{
				// Give focus to the alert
				alert = driver.switchTo().alert();

				// Set flag to stop loop
				bNoAlert = false;
			}
			catch (Exception ex)
			{
				sleep(getPollInterval());
			}
		}

		if (bNoAlert)
		{
			if (bLog)
				Logs.log.warn("No Alert appeared before timeout occurred");

			return null;
		}

		try
		{
			// Get the text to return later
			String sMessage = alert.getText();

			if (bLog)
				Logs.log.info("Switched to alert with message:  " + sMessage);

			// Accept or Dismiss the alert
			if (bAccept)
			{
				alert.accept();
				if (bLog)
					Logs.log.info("Cleared alert via 'Accept'");
			}
			else
			{
				alert.dismiss();
				if (bLog)
					Logs.log.info("Cleared alert via 'Dismiss'");
			}

			return sMessage;
		}
		catch (Exception ex)
		{
			if (bLog)
				Logs.log.warn("Exception occurred working with alert:  " + ex);

			return null;
		}
	}

	/**
	 * Waits for an alert to appear, clears alert and returns alert message<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses current timeout value<BR>
	 * 2) Logs all information<BR>
	 * 
	 * @param driver
	 * @param bAccept - true to click 'OK' (accept)
	 * @return Message text of the Alert or null if an error occurred
	 */
	public static String waitForAlert(WebDriver driver, boolean bAccept)
	{
		return waitForAlert(driver, getTimeoutInMilliseconds(), bAccept, true);
	}

	/**
	 * Sends the keys to the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used to send keys that are pressable but are not text<BR>
	 * <BR>
	 * <B>Code Examples:</B><BR>
	 * 1) sendKeys(element, "Right Arrow, Enter", true, Keys.ARROW_RIGHT, Keys.ENTER); // Send Right Arrow
	 * followed by Enter and log all actions<BR>
	 * 2) sendKeys(element, "Enter", false, Keys.ENTER); // Send Enter key and only log if error occurs<BR>
	 * 
	 * @param element - Element to send input to
	 * @param sLog - Describe input for logging
	 * @param bLogAll - true to log success else only failure is logged
	 * @param keysToSend - Keys To Send
	 * @throws GenericUnexpectedException if an exception occurs entering the keys
	 */
	public static void sendKeys(WebElement element, String sLog, boolean bLogAll, CharSequence... keysToSend)
	{
		sendKeys(element, sLog, bLogAll, true, keysToSend);
	}

	/**
	 * Sends the keys to the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should only be used to send keys that are pressable but are not text<BR>
	 * <BR>
	 * <B>Code Examples:</B><BR>
	 * 1) sendKeys(element, "Right Arrow, Enter", true, Keys.ARROW_RIGHT, Keys.ENTER); // Send Right Arrow
	 * followed by Enter and log all actions<BR>
	 * 2) sendKeys(element, "Enter", false, Keys.ENTER); // Send Enter key and only log if error occurs<BR>
	 * 
	 * @param element - Element to send input to
	 * @param sLog - Describe input for logging
	 * @param bLogAll - true to log success else only failure is logged
	 * @param throwError - true to throw error if exception occurs else it is suppressed with no indication
	 * @param keysToSend - Keys To Send
	 * @throws GenericUnexpectedException if an exception occurs entering the keys
	 */
	public static void sendKeys(WebElement element, String sLog, boolean bLogAll, boolean throwError,
			CharSequence... keysToSend)
	{
		try
		{
			element.sendKeys(keysToSend);

			if (bLogAll)
				Logs.log.info("Sent CharSequence ('" + sLog + "') successfully");
		}
		catch (Exception ex)
		{
			if (throwError)
			{
				Logs.logError("Sending CharSequence ('" + sLog + "') failed due to exception ["
						+ ex.getClass().getName() + "]:  " + ex.getMessage());
			}
		}
	}

	/**
	 * Waits for a window to have the specified URL and returns the window handle<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Always switches back to main window before returning<BR>
	 * 
	 * @param url - URL to search for
	 * @return Handle of corresponding window
	 * @throws GenericUnexpectedException if window not found with URL before timeout
	 */
	public String waitForWindowHandle(String url)
	{
		String handle = waitForWindowHandle(url, getTimeoutInMilliseconds(), getPollInterval());
		if (Compare.equals(handle, "", Comparison.Equal))
			Logs.logError("No Window contained URL (" + url + ") before timeout occurred");

		return handle;
	}

	/**
	 * Waits for a window to have the specified URL and returns the window handle<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Always switches back to main window before returning<BR>
	 * 
	 * @param url - URL to search for
	 * @param maxWaitTime - Max Time to wait for window with specified URL
	 * @param poll - Poll Interval in milliseconds
	 * @return Handle of corresponding window or empty string if window with specified URL not found before
	 *         timeout occurs
	 */
	public String waitForWindowHandle(String url, int maxWaitTime, int poll)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(maxWaitTime))
		{
			String sFound = getWindowHandle(url);
			if (!sFound.equals(""))
				return sFound;

			sleep(poll);
		}

		return "";
	}

	/**
	 * Checks if the element has the attribute matching the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Standard<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Contains<BR>
	 * Does Not Contain<BR>
	 * Regular Expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If you do not want to check the attribute value, then use Comparison.Standard for the criteria<BR>
	 * 
	 * 
	 * @param element - Element to check for the attribute
	 * @param sAttribute - Attribute to check for
	 * @param criteria - Criteria for attribute comparison
	 * @param sExpectedAttrValue - Expected attribute value
	 * @return true if attribute with matching criteria is found else false
	 */
	public static boolean isAttribute(WebElement element, String sAttribute, Comparison criteria,
			String sExpectedAttrValue)
	{
		String sAttributeValue = getAttribute(element, sAttribute);
		if (sAttributeValue == null)
		{
			return false;
		}
		else
		{
			if (criteria == Comparison.Standard)
				return true;

			if (criteria == Comparison.Equal && sAttributeValue.equals(sExpectedAttrValue))
				return true;

			if (criteria == Comparison.EqualsIgnoreCase
					&& sAttributeValue.equalsIgnoreCase(sExpectedAttrValue))
				return true;

			if (criteria == Comparison.NotEqual && !sAttributeValue.equals(sExpectedAttrValue))
				return true;

			if (criteria == Comparison.Contains && sAttributeValue.contains(sExpectedAttrValue))
				return true;

			if (criteria == Comparison.DoesNotContain && !sAttributeValue.contains(sExpectedAttrValue))
				return true;

			if (criteria == Comparison.RegEx && sAttributeValue.matches(sExpectedAttrValue))
				return true;

			return false;
		}
	}

	/**
	 * Checks if the element has the specified attribute
	 * 
	 * @param element - Element to check for the attribute
	 * @param sAttribute - Attribute to check for
	 * @return true if attribute is found else false
	 */
	public static boolean isAttribute(WebElement element, String sAttribute)
	{
		return isAttribute(element, sAttribute, Comparison.Standard, "");
	}

	/**
	 * Waits for element to have the attribute with the matching criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Standard<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Contains<BR>
	 * Does Not Contain<BR>
	 * Regular Expression<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the element is removed and re-added to the DOM, then this method will fail due to stale element
	 * on each iteration before finally timing out. Use the other overloaded method in this case.<BR>
	 * 
	 * @param element - Element to check for the attribute
	 * @param sAttribute - Attribute to check for
	 * @param criteria - Criteria for attribute comparison
	 * @param sExpectedAttrValue - Expected attribute value
	 * @throws GenericUnexpectedException if element does not have the attribute with the matching criteria
	 */
	public static void waitForAttribute(WebElement element, String sAttribute, Comparison criteria,
			String sExpectedAttrValue)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			if (isAttribute(element, sAttribute, criteria, sExpectedAttrValue))
				return;

			sleep(getPollInterval());
		}

		Logs.logError("Attribute (" + sAttribute + ") never appeared or satisfied criteria (" + criteria
				+ ") on the element before timeout occurred");
	}

	/**
	 * Waits for element to have the attribute with the matching criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Standard<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Contains<BR>
	 * Does Not Contain<BR>
	 * Regular Expression<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element
	 * @param sAttribute - Attribute to check for
	 * @param criteria - Criteria for attribute comparison
	 * @param sExpectedAttrValue - Expected attribute value
	 * @throws GenericUnexpectedException if element does not have the attribute with the matching criteria
	 */
	public static void waitForAttribute(WebDriver driver, String sLocator, String sAttribute,
			Comparison criteria, String sExpectedAttrValue)
	{
		boolean bNotFound = false;
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			WebElement element = findElement(driver, sLocator, false);
			if (element == null)
				bNotFound = true;
			else
				bNotFound = false;

			if (isAttribute(element, sAttribute, criteria, sExpectedAttrValue))
				return;

			sleep(getPollInterval());
		}

		// Element may have never been located or on the last iteration it could have been removed
		if (bNotFound)
			Logs.log.warn("On the last iteration, the element was not found using locator:  " + sLocator);

		Logs.logError("Attribute ('" + sAttribute + "') never appeared or satisfied criteria (" + criteria
				+ ") on the element ('" + sLocator + "') before timeout occurred");
	}

	/**
	 * Uses the locator to get a list of buttons and clicks the specified one<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use this method if a specific locator to the button does not exist (or difficult to create) and the
	 * buttons are constantly being refreshed.<BR>
	 * 2) It may be necessary to use AJAX.getText/getAttribute to get a list that can be used to find the
	 * index of the button<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to list of buttons
	 * @param sLog - Element Name to log
	 * @param nIndex - Index of button to click (zero based)
	 * @param retries - Number of times to retry if StaleElementReferenceException occurs
	 * @throws ElementNotEnabledException if button is not enabled
	 * @throws ElementNotDisplayedException if button is not displayed
	 * @throws ClickNoSuchElementException if unable to click button successfully causes are following:<BR>
	 *             1 - Index was invalid<BR>
	 *             2 - Stale element still after the specified retries<BR>
	 *             3 - Any other exception<BR>
	 */
	public static void click(WebDriver driver, String sLocator, String sLog, int nIndex, int retries)
	{
		int attempts = 0;
		while (true)
		{
			List<WebElement> buttons = findElementsAJAX(driver, sLocator, nIndex + 1);
			if (nIndex >= buttons.size())
			{
				String sError = "The index " + nIndex + " was invalid as only " + buttons.size()
						+ " buttons were available";
				Logs.logError(new ClickNoSuchElementException(sError));
			}

			WebElement element = buttons.get(nIndex);
			try
			{
				if (!element.isEnabled())
				{
					String sError = "Element ('" + sLog + "') was not enabled";
					Logs.logError(new ElementNotEnabledException(sError));
				}

				if (!element.isDisplayed())
				{
					String sError = "Element ('" + sLog + "') was not displayed";
					Logs.logError(new ElementNotDisplayedException(sError));
				}

				element.click();
				Logs.log.info("Clicked '" + sLog + "' successfully");
				return;
			}
			catch (StaleElementReferenceException ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then log error and throw
				// exception
				if (attempts > retries)
				{
					String sError = "Element ('" + sLog
							+ "') was stale as StaleElementReferenceException was thrown.";
					Logs.logError(new ClickNoSuchElementException(sError));
				}
				else
				{
					sleep(getPollInterval());
				}
			}
			catch (Exception ex)
			{
				String sError = "Could not find '" + sLog + "' due to exception [" + ex.getClass().getName()
						+ "]:  " + ex.getMessage();
				Logs.logError(new ClickNoSuchElementException(sError));
			}
		}
	}

	/**
	 * Waits for an element to exist (& enabled if specified) until timeout
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element
	 * @param bEnabled - true to wait for the element to be enabled
	 * @param nTimeoutInMilliseconds - Timeout value in milliseconds
	 * @return true if element existed (& enabled if specified) before timeout occurred
	 */
	public static boolean isWaitForElementExists(WebDriver driver, String sLocator, boolean bEnabled,
			int nTimeoutInMilliseconds)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nTimeoutInMilliseconds))
		{
			if (isElementExists(driver, sLocator, bEnabled))
				return true;

			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Waits until element's attribute matches the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element to check attribute
	 * @param sAttribute - Attribute to check for
	 * @param sExpectedAttrValue - Expected attribute value
	 * @param criteria - Criteria for the text comparison
	 * @param maxDuration - Max Duration (milliseconds) to allow the element's text to match criteria
	 * @return true if element has text matching criteria within timeout
	 */
	public static boolean isAttribute(WebDriver driver, String sLocator, String sAttribute,
			String sExpectedAttrValue, Comparison criteria, int maxDuration)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(maxDuration))
		{
			// Get element
			WebElement element = findElement(driver, sLocator, false);

			if (isAttribute(element, sAttribute, criteria, sExpectedAttrValue))
				return true;

			// Sleep to allow time for element to be refreshed before next check
			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Checks if initialization of the class will be allowed based on current URL
	 * 
	 * @param driver
	 * @param _AllowedURL - URL that allows initialization for the class
	 * @return true if initialization would be successful else false
	 */
	protected static boolean isInitAllowed(WebDriver driver, String _AllowedURL)
	{
		try
		{
			return Compare.contains(driver.getCurrentUrl(), _AllowedURL, Comparison.Standard);
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Checks if initialization of the class will be allowed based on current URL
	 * 
	 * @param driver
	 * @param allowedURLs - List of URLs that allows initialization for the class
	 * @return true if initialization would be successful else false
	 */
	protected static boolean isInitAllowed(WebDriver driver, List<String> allowedURLs)
	{
		for (String url : allowedURLs)
		{
			if (isInitAllowed(driver, url))
				return true;
		}

		return false;
	}

	/**
	 * Checks if initialization of the class will be allowed based on current URL
	 * 
	 * @param driver
	 * @param allowedURLs - Array of URLs that allows initialization for the class
	 * @return true if initialization would be successful else false
	 */
	protected static boolean isInitAllowed(WebDriver driver, String[] allowedURLs)
	{
		return isInitAllowed(driver, Arrays.asList(allowedURLs));
	}

	/**
	 * Checks if initialization of the class will be allowed
	 * 
	 * @param driver
	 * @param criteria - List of criteria to be met for initialization of the page
	 * @param allMatches - true to wait for all criteria matches, false to wait for 1st matching criteria
	 * @return true if initialization would be successful else false
	 */
	protected static boolean isInitAllowed(WebDriver driver, List<GenericData> criteria, boolean allMatches)
	{
		Condition condition = new Condition(driver);

		if (allMatches)
		{
			return condition.isAllMatched(criteria);
		}
		else
		{
			if (condition.match(criteria) < 0)
				return false;
			else
				return true;
		}
	}

	/**
	 * Gets all the selected options for a (MultiSelect) drop down.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Can be used on single selection drop down<BR>
	 * 2) If exception occurs while iterating over the drop down options, then the list may not be complete.
	 * (This could occur if list is refreshed via AJAX during the operation.)<BR>
	 * 
	 * @param element - Drop Down to get all selected options
	 * @return non-null List&lt;DropDownDefaults&gt;
	 */
	public static List<DropDownDefaults> getAllSelectedOptions(WebElement element)
	{
		List<DropDownDefaults> allSelected = new ArrayList<DropDownDefaults>();

		try
		{
			Select dropdown = new Select(element);
			boolean bMulti = dropdown.isMultiple();
			List<WebElement> options = dropdown.getOptions();
			for (int i = 0; i < options.size(); i++)
			{
				if (options.get(i).isSelected())
				{
					String sVisible = getText(options.get(i));
					String sValue = getAttribute(options.get(i), getInputAttr());
					String sIndex = String.valueOf(i);
					boolean bEnabled = isElementEnabled(options.get(i));
					DropDownDefaults curSelection = new DropDownDefaults(sVisible, sValue, sIndex, bEnabled);
					allSelected.add(curSelection);

					// If drop down does not allow multiple selections, then break the loop to speed up
					// execution
					if (!bMulti)
						break;
				}
			}
		}
		catch (Exception ex)
		{
		}

		return allSelected;
	}

	/**
	 * Gets all the selected options for a (MultiSelect) drop down.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Can be used on single selection drop down<BR>
	 * 2) If max retries is reached, then the list may not be complete.<BR>
	 * 3) At least 1 retry always occurs<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the drop down
	 * @param retries - Max number of retries before returning the current list
	 * @return non-null List&lt;DropDownDefaults&gt;
	 */
	public static List<DropDownDefaults> getAllSelectedOptions(WebDriver driver, String sLocator, int retries)
	{
		List<DropDownDefaults> allSelected = new ArrayList<DropDownDefaults>();

		// Index of the Last successful option processed
		int nIndex = 0;

		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = findElement(driver, sLocator, false);
				Select dropdown = new Select(element);
				boolean bMulti = dropdown.isMultiple();
				List<WebElement> options = dropdown.getOptions();
				for (int i = nIndex; i < options.size(); i++)
				{
					if (options.get(i).isSelected())
					{
						String sVisible = getText(options.get(i));
						String sValue = getAttribute(options.get(i), getInputAttr());
						String sIndex = String.valueOf(i);
						boolean bEnabled = isElementEnabled(options.get(i));
						DropDownDefaults cSelection = new DropDownDefaults(sVisible, sValue, sIndex, bEnabled);
						allSelected.add(cSelection);

						// If drop down does not allow multiple selections, then break the loop to speed up
						// execution
						if (!bMulti)
							break;
					}

					// If get to here without exception, then increase the start index should an exception
					// occur on the next iteration
					nIndex++;
				}

				return allSelected;
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// Max attempts reached?
				if (attempts < retries)
					sleep(getPollInterval());
				else
					return allSelected;
			}
		}
	}

	/**
	 * Deselect all drop down options
	 * 
	 * @param element - Drop Down that supports multiple options
	 * @param sLog - Drop Down name to log
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownNoSuchElementException for any other error (stale, does not support multiple options,
	 *             etc)
	 */
	public static void deselectDropDownOptions(WebElement element, String sLog)
	{
		try
		{
			if (!element.isEnabled())
			{
				throw new ElementNotEnabledException("");
			}

			if (!element.isDisplayed())
			{
				throw new ElementNotDisplayedException("");
			}

			Select dropdown = new Select(element);
			boolean bMulti = dropdown.isMultiple();
			if (bMulti)
			{
				dropdown.deselectAll();
				Logs.log.info("Deselection of all drop down options was successfully for:  " + sLog);
			}
			else
			{
				throw new Exception(
						"The drop down did not support multiple selection as such deselection was not possible for:  "
								+ sLog);
			}
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sLog + "') was not enabled";
			Logs.logError(new ElementNotEnabledException(sError));
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sLog + "') was not displayed";
			Logs.logError(new ElementNotDisplayedException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sLog
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logError(new DropDownNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			String sError = "Could not deselect options for the element ('" + sLog + "') due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(new DropDownNoSuchElementException(sError));
		}
	}

	/**
	 * Deselects value from drop down using specified option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Does not support random or skip<BR>
	 * 
	 * @param dropdown - Drop down to work with
	 * @param sLog - Name to use in log for the drop down
	 * @param dd - Option to deselect
	 * @param bThrowOnly - if true just throw exception else log error (& throw exception)
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be deselected
	 * @throws DropDownIndexException if could not find option (index) to be deselected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be deselected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void deselectDropDownOption(WebElement dropdown, String sLog, DropDown dd,
			boolean bThrowOnly)
	{
		try
		{
			if (!dropdown.isEnabled())
			{
				throw new ElementNotEnabledException("");
			}

			if (!dropdown.isDisplayed())
			{
				throw new ElementNotDisplayedException("");
			}

			Select value = new Select(dropdown);
			if (dd.using == Selection.ValueHTML)
			{
				value.deselectByValue(dd.option);
				Logs.log.info("Successfully deselected by value '" + dd.option + "' from the drop down ('"
						+ sLog + "')");
			}
			else if (dd.using == Selection.Index)
			{
				value.deselectByIndex(Integer.parseInt(dd.option));
				Logs.log.info("Successfully deselected by index '" + dd.option + "' from the drop down ('"
						+ sLog + "')");
			}
			else if (dd.using == Selection.RegEx)
			{
				int nIndex = 0;
				List<WebElement> availableOptions = value.getOptions();
				for (WebElement option : availableOptions)
				{
					// Deselect option if regular expression matches
					if (option.getText().matches(dd.option))
					{
						value.deselectByIndex(nIndex);
						Logs.log.info("Successfully deselected by index '" + nIndex + "' by using RegEx '"
								+ dd.option + "' to match from the drop down ('" + sLog + "')");
						return;
					}

					nIndex++;
				}

				throw new DropDownPartialMatchException("");
			}
			else
			{
				value.deselectByVisibleText(dd.option);
				Logs.log.info("Successfully deselected '" + dd.option + "' from the drop down ('" + sLog
						+ "')");
			}
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sLog + "') was not enabled";
			ElementNotEnabledException runtime = new ElementNotEnabledException(sError);
			if (bThrowOnly)
				throw runtime;
			else
				Logs.logError(runtime);
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sLog + "') was not displayed";
			ElementNotDisplayedException runtime = new ElementNotDisplayedException(sError);
			if (bThrowOnly)
				throw runtime;
			else
				Logs.logError(runtime);
		}
		catch (NoSuchElementException nsee)
		{
			String sError = "Could not find '" + dd.option + "' in the drop down ('" + sLog + "')";
			DropDownSelectionException runtime = new DropDownSelectionException(sError);
			if (bThrowOnly)
				throw runtime;
			else
				Logs.logError(runtime);
		}
		catch (NumberFormatException nfe)
		{
			String sError = "Could not find index '" + dd.option + "' in the drop down ('" + sLog + "')";
			DropDownIndexException runtime = new DropDownIndexException(sError);
			if (bThrowOnly)
				throw runtime;
			else
				Logs.logError(runtime);
		}
		catch (DropDownPartialMatchException ddpme)
		{
			String sError = "Could not find a partial match using the regular expression '" + dd.option
					+ "' in the drop down ('" + sLog + "')";
			DropDownPartialMatchException runtime = new DropDownPartialMatchException(sError);
			if (bThrowOnly)
				throw runtime;
			else
				Logs.logError(runtime);
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Drop down ('" + sLog
					+ "') was stale as StaleElementReferenceException was thrown.";
			DropDownNoSuchElementException runtime = new DropDownNoSuchElementException(sError);
			if (bThrowOnly)
				throw runtime;
			else
				Logs.logError(runtime);
		}
		catch (Exception ex)
		{
			String sError = "Could not find drop down ('" + sLog + "') due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			DropDownNoSuchElementException runtime = new DropDownNoSuchElementException(sError);
			if (bThrowOnly)
				throw runtime;
			else
				Logs.logError(runtime);
		}
	}

	/**
	 * Deselects value from drop down using specified option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Does not support random or skip<BR>
	 * 
	 * @param dropdown - Drop down to work with
	 * @param sLog - Name to use in log for the drop down
	 * @param dd - Option to deselect
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be deselected
	 * @throws DropDownIndexException if could not find option (index) to be deselected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be deselected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void deselectDropDownOption(WebElement dropdown, String sLog, DropDown dd)
	{
		deselectDropDownOption(dropdown, sLog, dd, false);
	}

	/**
	 * Deselects value from drop down using specified option<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Does not support random or skip<BR>
	 * 2) At least 1 attempt is always made to deselect the option<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the drop down
	 * @param sLog - Name to use in log for the drop down
	 * @param dd - Option to deselect
	 * @param nRetries - Number of retries before error is logged and exception thrown
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws DropDownSelectionException if could not find option (visible/value) to be deselected
	 * @throws DropDownIndexException if could not find option (index) to be deselected
	 * @throws DropDownPartialMatchException if could not find option (regex) to be deselected
	 * @throws DropDownNoSuchElementException if could not find the element
	 */
	public static void deselectDropDownOption(WebDriver driver, String sLocator, String sLog, DropDown dd,
			int nRetries)
	{
		int nCount = 0;
		while (true)
		{
			try
			{
				WebElement element = findElement(driver, sLocator, false);
				deselectDropDownOption(element, sLog, dd, true);
				return;
			}
			catch (Exception ex)
			{
				nCount++;
				if (nCount < nRetries)
				{
					sleep(getPollInterval());
				}
				else
				{
					String sError = "Could not deselect option for drop down ('" + sLog
							+ "') due to exception [" + ex.getClass().getName() + "]:  " + ex.getMessage();
					Logs.logError(new DropDownNoSuchElementException(sError));
				}
			}
		}
	}

	/**
	 * Closes the window that was used to initialized the class
	 */
	public void close()
	{
		workWithMainWindow();
		Parameter _winInfo = getWindowInfo();
		String sLog_WindowName = (_winInfo.param.equals("")) ? _winInfo.value : _winInfo.param;
		close(sLog_WindowName);
	}

	/**
	 * Keeps the session alive by find the HTML node every poll interval until the keep alive time expires.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) When using Grid, the session seems to be lost intermittently when sleeping for a few minutes. In an
	 * attempt to prevent this we will take an action to keep the session alive that will not affect the test.<BR>
	 * 
	 * @param driver
	 * @param nKeepAliveInMilliseconds - Amount of time in milliseconds to keep session alive
	 * @param nPollInMilliseconds - Interval (in milliseconds) between requests
	 */
	public static void keepSessionAlive(WebDriver driver, int nKeepAliveInMilliseconds,
			int nPollInMilliseconds)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nKeepAliveInMilliseconds))
		{
			findElement(driver, "//html", false);
			sleep(nPollInMilliseconds);
		}
	}

	/**
	 * Keeps the session alive by find the HTML node every poll interval until the keep alive time expires<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) When using Grid, the session seems to be lost intermittently when sleeping for a few minutes. In an
	 * attempt to prevent this we will take an action to keep the session alive that will not affect the test.<BR>
	 * 2) Polling every second may put too much stress on the grid and cause the browser to die<BR>
	 * 
	 * @param driver
	 * @param nKeepAliveInMilliseconds - Amount of time in milliseconds to keep session alive
	 */
	public static void keepSessionAlive(WebDriver driver, int nKeepAliveInMilliseconds)
	{
		keepSessionAlive(driver, nKeepAliveInMilliseconds, getPollInterval());
	}

	/**
	 * Clicks the element and waits for a new pop-up window to appear
	 * 
	 * @param element - Element to click
	 * @param sLog - Logging of the click
	 * @param bErrorOnEmptyWindowHandle - true for an error if no new pop-up window appears
	 * @return empty string if no new pop-up appears else window handle of 1st new window
	 * @throws GenericUnexpectedException if specified for empty window handle
	 */
	public String clickAndWaitForPopup(WebElement element, String sLog, boolean bErrorOnEmptyWindowHandle)
	{
		List<String> existing = getWindowHandles();
		click(element, sLog);
		String sHandle = waitForPopup(existing);

		// Does user want to ensure non-empty handle?
		if (bErrorOnEmptyWindowHandle)
		{
			if (sHandle.equals(""))
			{
				Logs.logError("Clicking '" + sLog
						+ "' did not cause a new pop-up window to appear before timeout occurred");
			}
		}

		return sHandle;
	}

	/**
	 * Clicks the element and waits for a new pop-up window to appear
	 * 
	 * @param element - Element to click
	 * @param sLog - Logging of the click
	 * @return empty string if no new pop-up appears else window handle of 1st new window
	 * @throws GenericUnexpectedException if no new pop-up window appears
	 */
	public String clickAndWaitForPopup(WebElement element, String sLog)
	{
		return clickAndWaitForPopup(element, sLog, true);
	}

	/**
	 * Waits for the element to become disabled (or removed)
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is disabled (or removed)
	 * @throws GenericUnexpectedException if element is not disabled (or removed) before timeout occurs
	 */
	public static void waitForElementDisabled(WebDriver driver, String sLocator, int nMaxWaitTime)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			WebElement element = findElement(driver, sLocator, false);
			if (isElementEnabled(element))
				sleep(getPollInterval());
			else
				return;
		}

		Logs.logError("Element ('" + sLocator
				+ "') never became disabled (or removed) before timeout occurred");
	}

	/**
	 * Waits for the element to become disabled (or removed)
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the element
	 * @throws GenericUnexpectedException if element is not disabled (or removed) before timeout occurs
	 */
	public static void waitForElementDisabled(WebDriver driver, String sLocator)
	{
		waitForElementDisabled(driver, sLocator, getTimeoutInMilliseconds());
	}

	/**
	 * Waits until the elements remain removed for the specified continuous amount of time<BR>
	 * <BR>
	 * <B>Usage Example:</B><BR>
	 * There are many of the same/similar AJAX indicators on the page. However, one or more of the indicators
	 * do not affect the test. It may be difficult to find all the specific AJAX indicators to wait for. This
	 * method allows you to specify the specific AJAX indicators to ignore which will be easier.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) maxDuration should be greater than continuous<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to find elements
	 * @param exceptions - List of locators that are exceptions and do not necessarily need to be removed
	 * @param maxDuration - Max Duration (milliseconds) to allow the elements to remain removed
	 * @param continuous - The continuous amount of time (milliseconds) that the elements must remain removed
	 */
	public static void waitForElementRemoval(WebDriver driver, String locator, List<String> exceptions,
			int maxDuration, int continuous)
	{
		// Max Timeout for the element to be remain removed and not added back as specified by user
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(maxDuration))
		{
			// Flag to indicate if elements were successfully removed for the specified time
			// Note: Setting to true to start the loop
			boolean bSuccess = true;

			// The amount of time the element needs to be continuously removed
			ElapsedTime e2 = new ElapsedTime();
			while (!e2.isTimeout(continuous) && bSuccess)
			{
				// Set the flag to false each iteration
				bSuccess = false;

				// Use locator to get all elements that are to be removed
				List<WebElement> elements = findElementsAJAX(driver, locator, 0);

				// If no elements are returned, then start the next iteration
				if (elements.isEmpty())
				{
					bSuccess = true;
					sleep(getPollInterval());
					continue;
				}

				// Get WebElements to be removed as they are in the exceptions list
				List<WebElement> exclusions = new ArrayList<WebElement>();
				for (String locator_exception : exceptions)
				{
					WebElement exclude = findElement(driver, locator_exception, false);
					if (exclude != null)
						exclusions.add(exclude);
				}

				// Remove the WebElements that are on the exceptions list
				for (WebElement exclude : exclusions)
				{
					int nIndex = elements.indexOf(exclude);
					if (nIndex >= 0)
						elements.remove(nIndex);
				}

				// If no elements are returned, then start the next iteration
				if (elements.isEmpty())
				{
					bSuccess = true;
					sleep(getPollInterval());
					continue;
				}

				// Check if all WebElements are removed
				for (WebElement element : elements)
				{
					if (isElementDisplayed(element))
					{
						// If element is displayed, then set flag to false and break loop
						bSuccess = false;
						break;
					}
					else
					{
						// Set flag to true as it currently is false from the beginning of the iteration
						bSuccess = true;
					}
				}

				// Give page time to update before checking in next iteration
				sleep(getPollInterval());
			}

			// If the flag is true after the loop, then this indicates that the element was removed for the
			// specified time continuously
			if (bSuccess)
				return;
		}

		Logs.logError("Element ('" + locator + "') never remained removed for " + continuous
				+ " milliseconds in a row before timeout occurred. Exception List:  "
				+ Conversion.toString(exceptions, ", "));
	}

	/**
	 * Waits until the elements remain removed for the specified continuous amount of time<BR>
	 * <BR>
	 * <B>Usage Example:</B><BR>
	 * There are many of the same/similar AJAX indicators on the page. However, one or more of the indicators
	 * do not affect the test. It may be difficult to find all the specific AJAX indicators to wait for. This
	 * method allows you to specify the specific AJAX indicator to ignore which will be easier.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) maxDuration should be greater than continuous<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to find elements
	 * @param exception - Locator that does not necessarily need to be removed
	 * @param maxDuration - Max Duration (milliseconds) to allow the elements to remain removed
	 * @param continuous - The continuous amount of time (milliseconds) that the elements must remain removed
	 */
	public static void waitForElementRemoval(WebDriver driver, String locator, String exception,
			int maxDuration, int continuous)
	{
		List<String> exceptions = new ArrayList<String>();
		exceptions.add(exception);
		waitForElementRemoval(driver, locator, exceptions, maxDuration, continuous);
	}

	/**
	 * Returns the WebElement for the specified sLocator that matches a child element. See xpath note.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For xpath locators, use "xpath=.//" to find all children as "//" will search the entire document.
	 * The prefix xpath= is used to indicate an xpath locator because it does not start with a /<BR>
	 * 
	 * @param anchorElement - Anchor element
	 * @param sLocator - How to locate the Element relative from the anchor
	 * @param bLog - true for logging
	 * @return null if cannot find
	 */
	public static WebElement findElement(WebElement anchorElement, String sLocator, boolean bLog)
	{
		WebElement element = null;

		try
		{
			element = anchorElement.findElement(locatedBy(sLocator));
		}
		catch (Exception ex)
		{
			if (bLog)
				Logs.log.warn("Could not find element from anchor element using:  " + sLocator);
		}

		return element;
	}

	/**
	 * Returns the WebElements for the specified sLocator that matches a child element. See xpath note.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For xpath locators, use "xpath=.//" to find all children as "//" will search the entire document.
	 * The prefix xpath= is used to indicate an xpath locator because it does not start with a /<BR>
	 * 
	 * @param anchorElement - Anchor element
	 * @param sLocator - How to locate the Elements
	 * @param bLog - true for logging
	 * @return List&lt;WebElement&gt; (non-null)
	 */
	public static List<WebElement> findElements(WebElement anchorElement, String sLocator, boolean bLog)
	{
		List<WebElement> elements = new ArrayList<WebElement>();

		try
		{
			elements = anchorElement.findElements(locatedBy(sLocator));
		}
		catch (Exception ex)
		{
			if (bLog)
				Logs.log.warn("Could not any find elements from anchor element using:  " + sLocator);
		}

		return elements;
	}

	/**
	 * Returns the WebElements for the specified sLocator that matches a child element. See xpath note.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For xpath locators, use "xpath=.//" to find all children as "//" will search the entire document.
	 * The prefix xpath= is used to indicate an xpath locator because it does not start with a /<BR>
	 * 
	 * @param anchorElement - Anchor element
	 * @param sLocator - How to locate the Elements
	 * @param atleast - Minimum number of elements to wait for
	 * @param bLog - true for logging
	 * @return List&lt;WebElement&gt; (non-null)
	 */
	public static List<WebElement> findElements(WebElement anchorElement, String sLocator, int atleast,
			boolean bLog)
	{
		return findElements(anchorElement, sLocator, atleast, false, bLog);
	}

	/**
	 * Returns the WebElements for the specified sLocator that matches a child element. See xpath note.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For xpath locators, use "xpath=.//" to find all children as "//" will search the entire document.
	 * The prefix xpath= is used to indicate an xpath locator because it does not start with a /<BR>
	 * 
	 * @param anchorElement - Anchor element
	 * @param sLocator - How to locate the Elements
	 * @param atleast - Minimum number of elements to wait for
	 * @param bExact - true to wait for exactly atleast (no more or no less)
	 * @param bLog - true for logging
	 * @return List&lt;WebElement&gt; (non-null)
	 */
	public static List<WebElement> findElements(WebElement anchorElement, String sLocator, int atleast,
			boolean bExact, boolean bLog)
	{
		List<WebElement> elements = new ArrayList<WebElement>();

		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			try
			{
				elements = anchorElement.findElements(locatedBy(sLocator));
				if (bExact)
				{
					if (elements.size() == atleast)
						return elements;
				}
				else
				{
					if (elements.size() >= atleast)
						return elements;
				}
			}
			catch (Exception ex)
			{
			}

			sleep(getPollInterval());
		}

		if (bLog)
		{
			if (bExact)
			{
				Logs.log.warn("The locator ('" + sLocator + "') did not return exactly " + atleast
						+ " elements before Timeout occurred");
			}
			else
			{
				Logs.log.warn("The locator ('" + sLocator + "') did not return atleast " + atleast
						+ " elements before Timeout occurred");
			}
		}

		return elements;
	}

	/**
	 * Refreshes the page by going to the current URL<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This should be equivalent to using F5 to refresh the page<BR>
	 * 2) If page contains AJAX, then page may not be ready when method returns<BR>
	 * 3) Unsaved data may be lost by using this method<BR>
	 * 4) If method triggers a pop-up when leaving with unsaved data, then this method should not be used as
	 * it does not handle the pop-up<BR>
	 * 
	 * @param driver
	 */
	public static void refresh(WebDriver driver)
	{
		get(driver, driver.getCurrentUrl());
	}

	/**
	 * Extracts the data from the drop down options
	 * 
	 * @param options - List of elements from a drop down to extract data
	 * @return List&lt;DropDownDefaults&gt;
	 */
	public static List<DropDownDefaults> getOptionsData(List<WebElement> options)
	{
		List<DropDownDefaults> data = new ArrayList<DropDownDefaults>();

		for (int i = 0; i < options.size(); i++)
		{
			String sVisible = getText(options.get(i));
			String sValue = getAttribute(options.get(i), getInputAttr());
			String sIndex = String.valueOf(i);
			boolean bEnabled = isElementEnabled(options.get(i));
			DropDownDefaults item = new DropDownDefaults(sVisible, sValue, sIndex, bEnabled);
			data.add(item);
		}

		return data;
	}

	/**
	 * Checks if the specified option is enabled in the drop down list
	 * 
	 * @param dropdown - Drop Down used to find the option and compare against
	 * @param option - Option to check if enabled
	 * @return true if option is enabled in the drop down list, false if the option is not in the drop down
	 *         list or it is disabled in the drop down list
	 */
	public static boolean isOptionEnabled(WebElement dropdown, DropDown option)
	{
		return isOptionEnabled(dropdown, option, false);
	}

	/**
	 * Checks if the specified option is enabled in the drop down list<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Case Sensitive comparison only applies if option is using Visible Text or HTML Value<BR>
	 * 
	 * @param dropdown - Drop Down used to find the option and compare against
	 * @param option - Option to check if enabled
	 * @param caseSensitive - true to do a case sensitive comparison, false to do a case insensitive
	 *            comparison
	 * @return true if option is enabled in the drop down list, false if the option is not in the drop down
	 *         list or it is disabled in the drop down list
	 */
	public static boolean isOptionEnabled(WebElement dropdown, DropDown option, boolean caseSensitive)
	{
		// Get all the available options for the drop down
		List<WebElement> all = getOptions(dropdown);

		// Go through all the elements and construct list that can be used to determine if option enabled
		List<DropDownDefaults> optionsData = getOptionsData(all);

		// Check if the option is enabled
		for (DropDownDefaults item : optionsData)
		{
			if (item.equivalent(option, caseSensitive))
				return item.enabled;
		}

		// No matching option
		return false;
	}

	/**
	 * Clears an element using backspaces (should be only used on text fields)
	 * 
	 * @param element - Field to clear
	 * @return true if successful else false
	 */
	public static boolean clearFieldBackspaces(WebElement element)
	{
		return clearFieldBackspaces(element, false);
	}

	/**
	 * Clears an element using backspaces (should be only used on text fields)
	 * 
	 * @param element - Field to clear
	 * @param bLogAll - true to log success else only failure is logged
	 * @return true if successful else false
	 */
	public static boolean clearFieldBackspaces(WebElement element, boolean bLogAll)
	{
		try
		{
			// Get the current input field value to calculate number of backspaces needed to blank field
			String currentText = getInputValue(element);

			// Go to the end of the input field
			sendKeys(element, "END", bLogAll, false, Keys.END);

			// Use backspace to clear the field
			for (int i = 0; i < currentText.length(); i++)
			{
				sendKeys(element, "BACKSPACE", bLogAll, false, Keys.BACK_SPACE);
			}

			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Clears an element using backspaces (should be only used on text fields)
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sLog - Element name used in logging (failure only)
	 * @param retries - Number of times to retry if failure occurs
	 * @throws GenericUnexpectedException if clearing field is not successful after specified number of
	 *             retries
	 */
	public static void clearFieldBackspaces(WebDriver driver, String sLocator, String sLog, int retries)
	{
		for (int i = 0; i < retries; i++)
		{
			WebElement element = findElement(driver, sLocator, false);
			if (clearFieldBackspaces(element))
				return;
			else
				sleep(getPollInterval());
		}

		Logs.logError("Clearing field ('" + sLog + "') using backspaces was not successful");
	}

	/**
	 * Tabs off the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses END key to go to the end of the field before TAB key is sent<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sLog - Element name used in logging
	 * @param bLogAll - true to log success else only failure is logged
	 * @param retries - Number of times to retry if failure occurs
	 * @throws GenericUnexpectedException if tabbing off field is not successful after specified number of
	 *             retries
	 */
	public static void tabOff(WebDriver driver, String sLocator, String sLog, boolean bLogAll, int retries)
	{
		for (int i = 0; i < retries; i++)
		{
			try
			{
				WebElement element = findElement(driver, sLocator, false);
				element.sendKeys(Keys.END);
				element.sendKeys(Keys.TAB);
				if (bLogAll)
					Logs.log.info("Tabbed off field ('" + sLog + "') successfully");

				return;
			}
			catch (Exception ex)
			{
				sleep(getPollInterval());
			}
		}

		Logs.logError("Tabbing off field was not successful");
	}

	/**
	 * Waits for the URL to change.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * You need to save the initial URL before doing your action & then calling this method.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * sInitialValue = driver.getCurrentUrl();<BR>
	 * button.click();<BR>
	 * Framework.waitForURLtoChange(driver, sInitialValue, false);<BR>
	 * 
	 * @param driver
	 * @param sInitialValue - Complete Initial URL or Part of Initial URL that will is expected to change
	 * @param bPartial - true waits until URL no longer contains sInitialValue, false waits until any
	 *            difference in URL
	 * @throws GenericUnexpectedException if URL does not change before timeout occurs
	 */
	public static void waitForURLtoChange(WebDriver driver, String sInitialValue, boolean bPartial)
	{
		if (!isWaitForURLtoChange(driver, sInitialValue, bPartial))
			Logs.logError("URL did not change before timeout occurred");
	}

	/**
	 * Checks if the element is in the correct state based on criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method can be applied to <B>any</B> 2 state conditions like ON/OFF or checked/unchecked<BR>
	 * 2) If enabledCriteria or enabledAttributeValue or disabledCriteria or disabledAttributeValue is
	 * <B>null</B>, then verification of only enable/disabled states is skipped<BR>
	 * 3) If verification of only enable/disabled states is performed, then enabled check/criteria and
	 * disabled check/criteria need to return opposite results always or the verification will fail. For
	 * example, if the enabled check/criteria returns true, then the disabled check/criteria needs to return
	 * false. Similarly, if the enabled check/criteria returns false, then the disabled check/criteria needs
	 * to return true.<BR>
	 * 4) If the <B>attribute value changes</B> during the check, then an <B>inconsistent state may result</B>
	 * and cause an exception.<BR>
	 * 5) Use this method if the state is determined by an attribute value (outside of the attribute disabled
	 * which WebDriver seems to use to determine enabled/disabled.)<BR>
	 * <BR>
	 * <B>Supported Comparison options:</B><BR>
	 * 1) EqualsIgnoreCase<BR>
	 * 2) Contains (Lower, Upper)<BR>
	 * 3) DoesNotContain<BR>
	 * 4) NotEqual<BR>
	 * 5) RegEx<BR>
	 * 6) Equal (default if unsupported option)<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to find element
	 * @param log - Logging name for element (only used if error)
	 * @param attribute - Attribute for which to get value
	 * @param expectedState - true for expected state of enabled (false for expected state of disabled)
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param retries - Number of times to retry if any exception occurs
	 * @return <B>General:</B> true if correct element state else false<BR>
	 *         <B>Expected State of Enabled:</B> true if element is enabled based on the criteria else false<BR>
	 *         <B>Expected State of Disabled:</B> true if element is disabled based on the criteria else false<BR>
	 * @throws GenericUnexpectedException if unable to get state after retrying
	 */
	public static boolean isCorrectState(WebDriver driver, String locator, String log, String attribute,
			boolean expectedState, Comparison enabledCriteria, String enabledAttributeValue,
			Comparison disabledCriteria, String disabledAttributeValue, int retries)
	{
		for (int i = 0; i < retries; i++)
		{
			try
			{
				WebElement element = findElement(driver, locator, false);
				return isCorrectState(element, log, attribute, expectedState, enabledCriteria,
						enabledAttributeValue, disabledCriteria, disabledAttributeValue, LogErrorLevel.NONE);
			}
			catch (Exception ex)
			{
			}

			sleep(getPollInterval());
		}

		Logs.logError("Unable to get state after " + retries + " retries.");
		return false;
	}

	/**
	 * Checks if the element is in the correct state based on criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method can be applied to <B>any</B> 2 state conditions like ON/OFF or checked/unchecked<BR>
	 * 2) If enabledCriteria or enabledAttributeValue or disabledCriteria or disabledAttributeValue is
	 * <B>null</B>, then verification of only enable/disabled states is skipped<BR>
	 * 3) If verification of only enable/disabled states is performed, then enabled check/criteria and
	 * disabled check/criteria need to return opposite results always or the verification will fail. For
	 * example, if the enabled check/criteria returns true, then the disabled check/criteria needs to return
	 * false. Similarly, if the enabled check/criteria returns false, then the disabled check/criteria needs
	 * to return true.<BR>
	 * 4) If the <B>attribute value changes</B> during the check, then an <B>inconsistent state may result</B>
	 * and cause an exception.<BR>
	 * 5) Use this method if the state is determined by an attribute value (outside of the attribute disabled
	 * which WebDriver seems to use to determine enabled/disabled.)<BR>
	 * <BR>
	 * <B>Supported Comparison options:</B><BR>
	 * 1) EqualsIgnoreCase<BR>
	 * 2) Contains (Lower, Upper)<BR>
	 * 3) DoesNotContain<BR>
	 * 4) NotEqual<BR>
	 * 5) RegEx<BR>
	 * 6) Equal (default if unsupported option)<BR>
	 * 
	 * @param element - Element to check state of
	 * @param log - Logging name for element (only used if error)
	 * @param attribute - Attribute for which to get value
	 * @param expectedState - true for expected state of enabled (false for expected state of disabled)
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @return <B>General:</B> true if correct element state else false<BR>
	 *         <B>Expected State of Enabled:</B> true if element is enabled based on the criteria else false<BR>
	 *         <B>Expected State of Disabled:</B> true if element is disabled based on the criteria else false<BR>
	 * @throws GenericUnexpectedException if element is in an inconsistent state
	 */
	public static boolean isCorrectState(WebElement element, String log, String attribute,
			boolean expectedState, Comparison enabledCriteria, String enabledAttributeValue,
			Comparison disabledCriteria, String disabledAttributeValue)
	{
		return isCorrectState(element, log, attribute, expectedState, enabledCriteria, enabledAttributeValue,
				disabledCriteria, disabledAttributeValue, LogErrorLevel.ERROR);
	}

	/**
	 * Checks if the element is in the correct state based on criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method can be applied to <B>any</B> 2 state conditions like ON/OFF or checked/unchecked<BR>
	 * 2) If enabledCriteria or enabledAttributeValue or disabledCriteria or disabledAttributeValue is
	 * <B>null</B>, then verification of only enable/disabled states is skipped<BR>
	 * 3) If verification of only enable/disabled states is performed, then enabled check/criteria and
	 * disabled check/criteria need to return opposite results always or the verification will fail. For
	 * example, if the enabled check/criteria returns true, then the disabled check/criteria needs to return
	 * false. Similarly, if the enabled check/criteria returns false, then the disabled check/criteria needs
	 * to return true.<BR>
	 * 4) If the <B>attribute value changes</B> during the check, then an <B>inconsistent state may result</B>
	 * and cause an exception.<BR>
	 * 5) Use this method if the state is determined by an attribute value (outside of the attribute disabled
	 * which WebDriver seems to use to determine enabled/disabled.)<BR>
	 * 6) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 7) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 8) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * <BR>
	 * <B>Supported Comparison options:</B><BR>
	 * 1) EqualsIgnoreCase<BR>
	 * 2) Contains (Lower, Upper)<BR>
	 * 3) DoesNotContain<BR>
	 * 4) NotEqual<BR>
	 * 5) RegEx<BR>
	 * 6) Equal (default if unsupported option)<BR>
	 * 
	 * @param element - Element to check state of
	 * @param log - Logging name for element (only used if error)
	 * @param attribute - Attribute for which to get value
	 * @param expectedState - true for expected state of enabled (false for expected state of disabled)
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param level - Control error logging written
	 * @return <B>General:</B> true if correct element state else false<BR>
	 *         <B>Expected State of Enabled:</B> true if element is enabled based on the criteria else false<BR>
	 *         <B>Expected State of Disabled:</B> true if element is disabled based on the criteria else false<BR>
	 * @throws GenericUnexpectedException if element is in an inconsistent state
	 */
	private static boolean isCorrectState(WebElement element, String log, String attribute,
			boolean expectedState, Comparison enabledCriteria, String enabledAttributeValue,
			Comparison disabledCriteria, String disabledAttributeValue, LogErrorLevel level)
	{
		// Get attribute value
		String actualAttributeValue = getAttribute(element, attribute);

		// Check if element is enabled (or state #1)
		boolean enabled;
		if (Comparison.EqualsIgnoreCase == enabledCriteria)
		{
			enabled = Compare.equals(actualAttributeValue, enabledAttributeValue, enabledCriteria);
		}
		else if (Comparison.Contains == enabledCriteria || Comparison.Lower == enabledCriteria
				|| Comparison.Upper == enabledCriteria)
		{
			enabled = Compare.contains(actualAttributeValue, enabledAttributeValue, enabledCriteria);
		}
		else if (Comparison.DoesNotContain == enabledCriteria)
		{
			enabled = !Compare.contains(actualAttributeValue, enabledAttributeValue, enabledCriteria);
		}
		else if (Comparison.NotEqual == enabledCriteria)
		{
			enabled = !Compare.equals(actualAttributeValue, enabledAttributeValue, enabledCriteria);
		}
		else if (Comparison.RegEx == enabledCriteria)
		{
			enabled = Compare.matches(actualAttributeValue, enabledAttributeValue);
		}
		else
		{
			enabled = Compare.equals(actualAttributeValue, enabledAttributeValue, enabledCriteria);
		}

		// Check if element is disabled (or state #2)
		boolean disabled;
		if (Comparison.EqualsIgnoreCase == disabledCriteria)
		{
			disabled = Compare.equals(actualAttributeValue, disabledAttributeValue, disabledCriteria);
		}
		else if (Comparison.Contains == disabledCriteria || Comparison.Lower == disabledCriteria
				|| Comparison.Upper == disabledCriteria)
		{
			disabled = Compare.contains(actualAttributeValue, disabledAttributeValue, disabledCriteria);
		}
		else if (Comparison.DoesNotContain == disabledCriteria)
		{
			disabled = !Compare.contains(actualAttributeValue, disabledAttributeValue, disabledCriteria);
		}
		else if (Comparison.NotEqual == disabledCriteria)
		{
			disabled = !Compare.equals(actualAttributeValue, disabledAttributeValue, disabledCriteria);
		}
		else if (Comparison.RegEx == disabledCriteria)
		{
			disabled = Compare.matches(actualAttributeValue, disabledAttributeValue);
		}
		else
		{
			disabled = Compare.equals(actualAttributeValue, disabledAttributeValue, disabledCriteria);
		}

		// If any of the criteria is null, then assume that user did not want to verify only 2 states allowed
		if (enabledCriteria != null && enabledAttributeValue != null && disabledCriteria != null
				&& disabledAttributeValue != null)
		{
			// Check for any inconsistent state (mainly due to application changes)
			if (enabled && disabled || !enabled && !disabled)
			{
				String sError = "The element ('" + log + "') was both states";
				Logs.logWarnError(level, new GenericUnexpectedException(sError));
			}
		}

		// Return the specified state
		if (expectedState)
			return enabled;
		else
			return disabled;
	}

	/**
	 * Move back a single "item" in the browser's history
	 * 
	 * @param driver
	 */
	public static void back(WebDriver driver)
	{
		try
		{
			driver.navigate().back();
			Logs.log.info("Clicked the Browser BACK button successfully");
		}
		catch (Exception ex)
		{
			String sError = "Clicking the Browser BACK button caused the following exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(new GenericUnexpectedException(sError));
		}
	}

	/**
	 * Refreshes the page by using the Browser <B>REFRESH</B> button<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If page contains AJAX, then page may not be ready when method returns<BR>
	 * 2) Unsaved data may be lost by using this method<BR>
	 * 3) If method triggers a pop-up when leaving with unsaved data, then this method should not be used as
	 * it does not handle the pop-up<BR>
	 * 
	 * @param driver
	 */
	public static void refreshPage(WebDriver driver)
	{
		try
		{
			driver.navigate().refresh();
			Logs.log.info("Clicked the Browser REFRESH button successfully");
		}
		catch (Exception ex)
		{
			String sError = "Clicking the Browser REFRESH button caused the following exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(new GenericUnexpectedException(sError));
		}
	}

	/**
	 * Checks if the element is displayed & enabled on the page. See xpath note.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For xpath locators, use "xpath=.//" to find all children as "//" will search the entire document.
	 * The prefix xpath= is used to indicate an xpath locator because it does not start with a /<BR>
	 * 
	 * @param anchorElement - Anchor element
	 * @param locator - How to locate the Element relative from the anchor
	 * @return true if element displayed & enabled else false
	 */
	public static boolean isElementReady(WebElement anchorElement, String locator)
	{
		WebElement element = findElement(anchorElement, locator, false);
		return isElementReady(element);
	}

	/**
	 * Waits for the element relative to the anchor element to become ready (displayed & enabled.) See xpath
	 * note.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For xpath locators, use "xpath=.//" to find all children as "//" will search the entire document.
	 * The prefix xpath= is used to indicate an xpath locator because it does not start with a /<BR>
	 * 2) Anchor Element must already exist in the DOM and not be removed/added. If not this method will never
	 * be successful (as a stale element exception will always cause the result to be false.)<BR>
	 * 
	 * @param anchorElement - Anchor element
	 * @param locator - How to locate the Element relative from the anchor
	 * @throws GenericUnexpectedException if the element relative to the anchor element never becomes ready
	 *             before timeout occurs
	 */
	public static void waitForElementReady(WebElement anchorElement, String locator)
	{
		waitForElementReady(anchorElement, getTimeoutInMilliseconds(), locator);
	}

	/**
	 * Waits for the element relative to the anchor element to become ready (displayed & enabled.) See xpath
	 * note.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For xpath locators, use "xpath=.//" to find all children as "//" will search the entire document.
	 * The prefix xpath= is used to indicate an xpath locator because it does not start with a /<BR>
	 * 2) Anchor Element must already exist in the DOM and not be removed/added. If not this method will never
	 * be successful (as a stale element exception will always cause the result to be false.)<BR>
	 * 
	 * @param anchorElement - Anchor element
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is ready
	 * @param locator - How to locate the Element relative from the anchor
	 * @throws GenericUnexpectedException if the element relative to the anchor element never becomes ready
	 *             before timeout occurs
	 */
	public static void waitForElementReady(WebElement anchorElement, int nMaxWaitTime, String locator)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			if (isElementReady(anchorElement, locator))
				return;

			sleep(getPollInterval());
		}

		Logs.logError("Element relative (" + locator + ") to the anchor element "
				+ "never became ready (displayed & enabled) before timeout occurred");
	}

	/**
	 * Checks if the current URL matches the value using the specified criteria
	 * 
	 * @param driver
	 * @param value - Compare the current URL against this value
	 * @param criteria - Criteria used to determine if current URL matches the value
	 * @return true if current URL matches the value using the specified criteria
	 */
	public static boolean isURL(WebDriver driver, String value, Comparison criteria)
	{
		try
		{
			if (Compare.text(driver.getCurrentUrl(), Conversion.nonNull(value), criteria))
				return true;
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Checks if the current URL matches the value using the specified criteria before timeout occurs
	 * 
	 * @param driver
	 * @param timeout - Timeout value in milliseconds
	 * @param value - Compare the current URL against this value
	 * @param criteria - Criteria used to determine if current URL matches the value
	 * @return true if current URL matches the value using the specified criteria before timeout occurs
	 */
	public static boolean isURL(WebDriver driver, int timeout, String value, Comparison criteria)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(timeout))
		{
			if (isURL(driver, value, criteria))
				return true;
			else
				sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Get count of selected/unselected elements<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Each locator is used to get the element and immediately perform the check which is added to the
	 * returned count<BR>
	 * 
	 * @param driver
	 * @param locators - List of locators used to get the elements to get selected count from
	 * @param selected - true to get selected count, false to get unselected count
	 * @return count of selected/unselected elements
	 */
	public static int getSelectedCount(WebDriver driver, List<String> locators, boolean selected)
	{
		int count = 0;

		for (String locator : locators)
		{
			WebElement element = findElement(driver, locator, false);
			count += getSelectedCount(Arrays.asList(element), selected);
		}

		return count;
	}

	/**
	 * Get count of selected/unselected elements
	 * 
	 * @param elements - List of elements (Check box or radio button) to get selected count from
	 * @param selected - true to get selected count, false to get unselected count
	 * @return count of selected/unselected elements
	 */
	public static int getSelectedCount(List<WebElement> elements, boolean selected)
	{
		int count = 0;

		for (WebElement element : elements)
		{
			boolean result;
			if (selected)
				result = isElementSelected(element);
			else
				result = !isElementSelected(element);

			if (result)
				count++;
		}

		return count;
	}

	/**
	 * Performs drag and drop
	 * 
	 * @param source - Element to emulate button down at
	 * @param sLog_Source - Logging for Source
	 * @param target - Element to move to and release the mouse at
	 * @param sLog_Target - Logging for Target
	 */
	public static void dragAndDrop(WebElement source, String sLog_Source, WebElement target,
			String sLog_Target)
	{
		try
		{
			WebDriver driver = getWebDriver(source);
			if (driver == null)
				throw new MouseActionsException("");

			Actions actions = new Actions(driver);
			actions.dragAndDrop(source, target).perform();
			Logs.log.info("Drag & Drop (" + sLog_Source + " to " + sLog_Target + ") was successful");
		}
		catch (MouseActionsException ex)
		{
			String sError = "Drag & Drop could not be executed because the WebDriver was null";
			Logs.logError(sError);
		}
		catch (Exception ex)
		{
			String sError = "Could not Drag & Drop (" + sLog_Source + " to " + sLog_Target
					+ ") due to exception[" + ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(sError);
		}
	}

	/**
	 * Performs drag and drop
	 * 
	 * @param source - Element to emulate button down at
	 * @param sLog_Source - Logging for Source
	 * @param target - Element to move to and release the mouse at
	 * @param sLog_Target - Logging for Target
	 * @param delayHold - Delay after click & hold at source
	 * @param delayRelease - Delay after moving to target before releasing
	 */
	public static void dragAndDrop(WebElement source, String sLog_Source, WebElement target,
			String sLog_Target, int delayHold, int delayRelease)
	{
		try
		{
			WebDriver driver = getWebDriver(source);
			if (driver == null)
				throw new MouseActionsException("");

			Actions actions = new Actions(driver);
			actions.clickAndHold(source).perform();
			sleep(delayHold);
			actions.moveToElement(target).perform();
			sleep(delayRelease);
			actions.release().perform();
			Logs.log.info("Drag & Drop (" + sLog_Source + " to " + sLog_Target + ") was successful");
		}
		catch (MouseActionsException ex)
		{
			String sError = "Drag & Drop could not be executed because the WebDriver was null";
			Logs.logError(sError);
		}
		catch (Exception ex)
		{
			String sError = "Could not Drag & Drop (" + sLog_Source + " to " + sLog_Target
					+ ") due to exception[" + ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logError(sError);
		}
	}

	/**
	 * Selects, Unselects or skips a check box<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method works with non-standard tags that are check boxes like span by examining an attribute to
	 * determine if checked or not.<BR>
	 * 
	 * @param element - Check box to work with
	 * @param sElementName - Element Name to log
	 * @param attribute - Attribute used to determine if element is selected
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 * @param checkbox - Object that contains information about check box
	 * @throws CheckBoxWrongStateException if element is not in correct initial state
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 * @throws GenericUnexpectedException if element is in an inconsistent state
	 */
	public static void checkboxClick(WebElement element, String sElementName, String attribute,
			Comparison checkedCriteria, String checkedAttributeValue, Comparison uncheckedCriteria,
			String uncheckedAttributeValue, CheckBox checkbox)
	{
		boolean bEnabled = isElementEnabled(element);
		boolean bChecked = isCorrectState(element, sElementName, attribute, true, checkedCriteria,
				checkedAttributeValue, uncheckedCriteria, uncheckedAttributeValue);

		if (checkbox.skip)
		{
			String sMessage = "Check box for '" + sElementName + "' was skipped.  The check box was ";
			if (bEnabled)
				sMessage += "enabled";
			else
				sMessage += "disabled";

			if (bChecked)
				sMessage += " and selected";
			else
				sMessage += " and not selected";

			Logs.log.info(sMessage);
			return;
		}

		if (bEnabled)
		{
			checkClick(element, sElementName, checkbox.verifyInitialState, attribute, checkedCriteria,
					checkedAttributeValue, uncheckedCriteria, uncheckedAttributeValue, checkbox.check,
					LogErrorLevel.convert(checkbox.logError));
		}
		else
		{
			if (checkbox.verifyEnabled)
			{
				String sError = "Check box for '" + sElementName + "' was disabled";
				Logs.logError(new CheckBoxNotEnabled(sError));
			}

			if (checkbox.verifyInitialState)
			{
				if (!isElementDisplayed(element))
				{
					String sError = "Check box for '" + sElementName + "' was not displayed";
					Logs.logError(new ElementNotDisplayedException(sError));
				}
			}

			if (bChecked != checkbox.check)
			{
				String sError = "Check box for '" + sElementName
						+ "' was disabled and it was not in the correct desired state (" + checkbox.check
						+ ")";
				Logs.logError(new CheckBoxNotEnabled(sError));
			}
			else
			{
				String sWarn = "Check box for '" + sElementName
						+ "' was disabled but it was in the correct desired state (" + checkbox.check + ")";
				Logs.log.warn(sWarn);
			}
		}
	}

	/**
	 * Selects, Unselects or skips a check box<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method works with non-standard tags that are check boxes like span by examining an attribute to
	 * determine if checked or not.<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to find check box to work with
	 * @param sElementName - Element Name to log
	 * @param attribute - Attribute used to determine if element is selected
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 * @param checkbox - Object that contains information about check box
	 * @param retries - Number of times to retry if any exception occurs
	 * @throws CheckBoxWrongStateException if element is not in correct initial state
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 * @throws GenericUnexpectedException if element is in an inconsistent state
	 */
	public static void checkboxClick(WebDriver driver, String locator, String sElementName, String attribute,
			Comparison checkedCriteria, String checkedAttributeValue, Comparison uncheckedCriteria,
			String uncheckedAttributeValue, CheckBox checkbox, int retries)
	{
		boolean bEnabled = isElementEnabled(driver, locator, retries);
		boolean bChecked = isCorrectState(driver, locator, sElementName, attribute, true, checkedCriteria,
				checkedAttributeValue, uncheckedCriteria, uncheckedAttributeValue, retries);

		if (checkbox.skip)
		{
			String sMessage = "Check box for '" + sElementName + "' was skipped.  The check box was ";
			if (bEnabled)
				sMessage += "enabled";
			else
				sMessage += "disabled";

			if (bChecked)
				sMessage += " and selected";
			else
				sMessage += " and not selected";

			Logs.log.info(sMessage);
			return;
		}

		if (bEnabled)
		{
			for (int i = 0; i < retries; i++)
			{
				// Assume that action is a failure
				boolean success = false;

				try
				{
					WebElement element = findElement(driver, locator, false);
					checkClick(element, sElementName, checkbox.verifyInitialState, attribute,
							checkedCriteria, checkedAttributeValue, uncheckedCriteria,
							uncheckedAttributeValue, checkbox.check, LogErrorLevel.NONE);
					success = true;
				}
				catch (Exception ex)
				{
					success = false;
				}

				// Return if action was successful
				if (success)
					return;

				// Sleep before retry
				sleep(getPollInterval());
			}

			Logs.logError("Unable to perform the action on the check box after " + retries + " retries.");
		}
		else
		{
			if (checkbox.verifyEnabled)
			{
				String sError = "Check box for '" + sElementName + "' was disabled";
				Logs.logError(new CheckBoxNotEnabled(sError));
			}

			if (checkbox.verifyInitialState)
			{
				if (!isElementDisplayed(driver, locator, retries))
				{
					String sError = "Check box for '" + sElementName + "' was not displayed";
					Logs.logError(new ElementNotDisplayedException(sError));
				}
			}

			if (bChecked != checkbox.check)
			{
				String sError = "Check box for '" + sElementName
						+ "' was disabled and it was not in the correct desired state (" + checkbox.check
						+ ")";
				Logs.logError(new CheckBoxNotEnabled(sError));
			}
			else
			{
				String sWarn = "Check box for '" + sElementName
						+ "' was disabled but it was in the correct desired state (" + checkbox.check + ")";
				Logs.log.warn(sWarn);
			}
		}
	}

	/**
	 * Selects check box if unselected (or unselects check box if selected) based on check flag<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) LogErrorLevel.ERROR - Write an error to the log<BR>
	 * 2) LogErrorLevel.WARN - Write a warning to the log<BR>
	 * 3) LogErrorLevel.NONE - Nothing is written to the log<BR>
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in correct state and ready
	 * @param attribute - Attribute used to determine if element is selected or not
	 * @param checkedCriteria - Criteria used to determine if element is selected
	 * @param checkedAttributeValue - Value used for the Selected check (expected value)
	 * @param uncheckedCriteria - Criteria used to determine if element is unselected
	 * @param uncheckedAttributeValue - Value used for the UnSelected check (expected value)
	 * @param criteria - Criteria used to determine if element is selected or not
	 * @param expectedAttributeValue - Value used for the Selected/UnSelected check (expected value)
	 * @param check - true to check, false to uncheck
	 * @param level - Control error logging written
	 * @throws CheckBoxWrongStateException if element is not in the correct initial state. For selection, the
	 *             check box needs to be unselected and for unselection the check box needs to be selected.
	 * @throws ElementNotEnabledException if element is not enabled
	 * @throws ElementNotDisplayedException if element is not displayed
	 * @throws CheckBoxNoSuchElementException if could not find the element
	 * @throws GenericUnexpectedException if element is in an inconsistent state
	 */
	private static void checkClick(WebElement element, String sElementName, boolean bVerifyInitialState,
			String attribute, Comparison checkedCriteria, String checkedAttributeValue,
			Comparison uncheckedCriteria, String uncheckedAttributeValue, boolean check, LogErrorLevel level)
	{
		try
		{
			// Is element in state such that click puts it in the desired final state?
			boolean state = isCorrectState(element, sElementName, attribute, !check, checkedCriteria,
					checkedAttributeValue, uncheckedCriteria, uncheckedAttributeValue, level);
			if (bVerifyInitialState)
			{
				if (!state)
					throw new CheckBoxWrongStateException("");

				if (!element.isEnabled())
					throw new ElementNotEnabledException("");

				if (!element.isDisplayed())
					throw new ElementNotDisplayedException("");
			}

			String sState;
			if (check)
				sState = "selected";
			else
				sState = "unselected";

			if (state)
			{
				element.click();
				Logs.log.info("Successfully " + sState + " check box for '" + sElementName + "'");
			}
			else
			{
				Logs.log.info("Check box for '" + sElementName + "' was already " + sState);
			}
		}
		catch (CheckBoxWrongStateException cbwse)
		{
			String sError = "Required initial state of the check box '" + sElementName
					+ "' was not correct for the check operation";
			Logs.logWarnError(level, new CheckBoxWrongStateException(sError));
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sElementName + "') was not enabled";
			Logs.logWarnError(level, new ElementNotEnabledException(sError));
		}
		catch (ElementNotDisplayedException ex)
		{
			String sError = "Element ('" + sElementName + "') was not displayed";
			Logs.logWarnError(level, new ElementNotDisplayedException(sError));
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			Logs.logWarnError(level, new CheckBoxNoSuchElementException(sError));
		}
	}

	/**
	 * Checks if the element is displayed & enabled on the page for non-standard elements that WebDriver
	 * always considers enabled
	 * 
	 * @param driver
	 * @param locator - Locator to find the element
	 * @param attribute - Attribute for which to get value
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is ready
	 * @return true if element becomes displayed & enabled else false
	 */
	public static boolean isElementReady(WebDriver driver, String locator, String attribute,
			Comparison enabledCriteria, String enabledAttributeValue, Comparison disabledCriteria,
			String disabledAttributeValue, int nMaxWaitTime)
	{
		// Max Timeout for the element to be ready as specified by user
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(nMaxWaitTime))
		{
			WebElement element = findElement(driver, locator, false);
			if (isElementDisplayed(element)
					&& isCorrectState(element, "", attribute, true, enabledCriteria, enabledAttributeValue,
							disabledCriteria, disabledAttributeValue, LogErrorLevel.NONE))
			{
				return true;
			}

			sleep(getPollInterval());
		}

		return false;
	}

	/**
	 * Waits for the element to become ready (displayed & enabled) for non-standard elements that WebDriver
	 * always considers enabled
	 * 
	 * @param driver
	 * @param locator - Locator to find the element
	 * @param attribute - Attribute for which to get value
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 */
	public static void waitForElementReady(WebDriver driver, String locator, String attribute,
			Comparison enabledCriteria, String enabledAttributeValue, Comparison disabledCriteria,
			String disabledAttributeValue)
	{
		waitForElementReady(driver, locator, attribute, enabledCriteria, enabledAttributeValue,
				disabledCriteria, disabledAttributeValue, getTimeoutInMilliseconds());
	}

	/**
	 * Waits for the element to become ready (displayed & enabled) for non-standard elements that WebDriver
	 * always considers enabled
	 * 
	 * @param driver
	 * @param locator - Locator to find the element
	 * @param attribute - Attribute for which to get value
	 * @param enabledCriteria - Criteria for Enabled check
	 * @param enabledAttributeValue - Value used for the Enabled check (expected value)
	 * @param disabledCriteria - Criteria for Disabled check
	 * @param disabledAttributeValue - Value used for the Disabled check (expected value)
	 * @param nMaxWaitTime - Max Time (milliseconds) to wait until element is ready
	 */
	public static void waitForElementReady(WebDriver driver, String locator, String attribute,
			Comparison enabledCriteria, String enabledAttributeValue, Comparison disabledCriteria,
			String disabledAttributeValue, int nMaxWaitTime)
	{
		if (!isElementReady(driver, locator, attribute, enabledCriteria, enabledAttributeValue,
				disabledCriteria, disabledAttributeValue, nMaxWaitTime))
		{
			Logs.logError("Element ('" + locator
					+ "') never became ready (displayed & enabled) before timeout occurred");
		}
	}

	/**
	 * Get Current URL
	 * 
	 * @param driver
	 * @return empty if exception occurs else current URL
	 */
	public static String getCurrentURL(WebDriver driver)
	{
		String _URL = "";

		try
		{
			_URL = driver.getCurrentUrl();
		}
		catch (Exception ex)
		{
		}

		return _URL;
	}

	/**
	 * Get Current URL
	 * 
	 * @param element - Element to get WebDriver from
	 * @return empty if exception occurs else current URL
	 */
	public static String getCurrentURL(WebElement element)
	{
		WebDriver useDriver = getWebDriver(element);
		return getCurrentURL(useDriver);
	}

	/**
	 * Checks if element's input value matches the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param element - Element to check input value
	 * @param criteria - Criteria for the comparison
	 * @param sExpected - Expected input value to match the criteria (non-null)
	 * @return true if element has input value matching criteria else false
	 */
	public static boolean isInputValue(WebElement element, Comparison criteria, String sExpected)
	{
		String actual = getInputValue(element).trim();
		String expected = Conversion.nonNull(sExpected).trim();
		return Compare.text(actual, expected, criteria);
	}

	/**
	 * Checks if element's input value matches the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to find element for comparison
	 * @param log - Element name for logging purposes
	 * @param criteria - Criteria for the comparison
	 * @param expected - Expected input value to match the criteria (non-null)
	 * @throws GenericUnexpectedException if criteria does not match before timeout occurs
	 */
	public static void waitForInputValue(WebDriver driver, String locator, String log, Comparison criteria,
			String expected)
	{
		waitForInputValue(driver, locator, log, criteria, expected, getTimeoutInMilliseconds());
	}

	/**
	 * Checks if element's input value matches the criteria<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * else - Contains<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to find element for comparison
	 * @param log - Element name for logging purposes
	 * @param criteria - Criteria for the comparison
	 * @param expected - Expected input value to match the criteria (non-null)
	 * @param timeout - Max Time (milliseconds) to wait until criteria is matched
	 * @throws GenericUnexpectedException if criteria does not match before timeout occurs
	 */
	public static void waitForInputValue(WebDriver driver, String locator, String log, Comparison criteria,
			String expected, int timeout)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(timeout))
		{
			WebElement element = findElement(driver, locator, false);
			boolean result = isInputValue(element, criteria, expected);
			if (result)
				return;
			else
				sleep(getPollInterval());
		}

		String sCriteria;
		if (criteria == Comparison.NotEqual)
			sCriteria = "Not Equal to '" + expected + "'";
		else if (criteria == Comparison.Equal)
			sCriteria = "Equal to '" + expected + "'";
		else if (criteria == Comparison.EqualsIgnoreCase)
			sCriteria = "Equals Ignore Case to '" + expected + "'";
		else if (criteria == Comparison.RegEx)
			sCriteria = "Match using Regular Expression '" + expected + "'";
		else if (criteria == Comparison.DoesNotContain)
			sCriteria = "Does Not Contain '" + expected + "'";
		else
			sCriteria = "Contain '" + expected + "'";

		Logs.logError("The element ('" + log + "') input value did not meet criteria (" + sCriteria
				+ ") before timeout occurred");
	}

	/**
	 * Delete all the cookies for the <B>current domain</B><BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It does not matter if you navigated to another domain previously only the current domains cookies
	 * can be deleted due to security restrictions on JavaScript<BR>
	 * 
	 * @param driver
	 * @throws GenericUnexpectedException if exception occurs deleting the cookies
	 */
	public static void deleteAllCookies(WebDriver driver)
	{
		try
		{
			driver.manage().deleteAllCookies();
			Logs.log.info("Deleted All Cookies for the current domain successfully");
		}
		catch (Exception ex)
		{
			Logs.logError("Deleting All Cookies for the current domain generated the following exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
		}
	}

	/**
	 * Does drop down value match based on the options<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Selection.Index<BR>
	 * Selection.ValueHTML<BR>
	 * Selection.VisibleText (Default)<BR>
	 * <BR>
	 * <B>Compare options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * Contains (Default)<BR>
	 * 
	 * @param element - Drop Down to get value from for comparison
	 * @param criteria - Which drop down value to compare against (Index/HTML Value/Visible)
	 * @param compare - How to compare the drop down value to the expected value
	 * @param expected - The expected drop down value
	 * @return true if drop down value matches expected value
	 */
	public static boolean isDropDown(WebElement element, Selection criteria, Comparison compare,
			String expected)
	{
		DropDownDefaults current = DropDownDefaults.defaultsFromElement(element);
		if (criteria == Selection.Index)
			return Compare.equals(current.index, expected, Comparison.Equal);
		else if (criteria == Selection.ValueHTML)
			return Compare.text(current.value, expected, compare);
		else
			return Compare.text(current.visible, expected, compare);
	}

	/**
	 * Wait for the drop down value to match based on the options<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Selection.Index<BR>
	 * Selection.ValueHTML<BR>
	 * Selection.VisibleText (Default)<BR>
	 * <BR>
	 * <B>Compare options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * Contains (Default)<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to the drop down
	 * @param criteria - Which drop down value to compare against (Index/HTML Value/Visible)
	 * @param compare - How to compare the drop down value to the expected value
	 * @param expected - The expected drop down value
	 * @param timeout - Max Time (milliseconds) to wait until criteria is matched
	 * @throws GenericUnexpectedException if drop down value does not match criteria before timeout occurs
	 */
	public static void waitForDropDown(WebDriver driver, String locator, Selection criteria,
			Comparison compare, String expected, int timeout)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout(timeout))
		{
			WebElement element = findElement(driver, locator, false);
			boolean result = isDropDown(element, criteria, compare, expected);
			if (result)
				return;
			else
				sleep(getPollInterval());
		}

		String sOption;
		if (criteria == Selection.Index)
			sOption = "Index";
		else if (criteria == Selection.ValueHTML)
			sOption = "HTML Value";
		else
			sOption = "Visible Text";

		String sCriteria;
		if (compare == Comparison.NotEqual)
			sCriteria = "Not Equal to '" + expected + "'";
		else if (compare == Comparison.Equal)
			sCriteria = "Equal to '" + expected + "'";
		else if (compare == Comparison.EqualsIgnoreCase)
			sCriteria = "Equals Ignore Case to '" + expected + "'";
		else if (compare == Comparison.RegEx)
			sCriteria = "Match using Regular Expression '" + expected + "'";
		else if (compare == Comparison.DoesNotContain)
			sCriteria = "Does Not Contain '" + expected + "'";
		else
			sCriteria = "Contain '" + expected + "'";

		Logs.logError("The drop down value (" + sOption + ") did not meet criteria (" + sCriteria
				+ ") before timeout occurred");
	}

	/**
	 * Wait for the drop down value to match based on the options<BR>
	 * <BR>
	 * <B>Criteria options supported:</B><BR>
	 * Selection.Index<BR>
	 * Selection.ValueHTML<BR>
	 * Selection.VisibleText (Default)<BR>
	 * <BR>
	 * <B>Compare options supported:</B><BR>
	 * Contains<BR>
	 * Not Equal<BR>
	 * Equal<BR>
	 * Equals Ignore Case<BR>
	 * Regular Expression<BR>
	 * Does Not Contain<BR>
	 * Contains (Default)<BR>
	 * 
	 * @param driver
	 * @param locator - Locator to the drop down
	 * @param criteria - Which drop down value to compare against (Index/HTML Value/Visible)
	 * @param compare - How to compare the drop down value to the expected value
	 * @param expected - The expected drop down value
	 * @throws GenericUnexpectedException if drop down value does not match criteria before timeout occurs
	 */
	public static void waitForDropDown(WebDriver driver, String locator, Selection criteria,
			Comparison compare, String expected)
	{
		waitForDropDown(driver, locator, criteria, compare, expected, getTimeoutInMilliseconds());
	}
}
