package com.automation.ui.common.sampleProject.pages;

import org.openqa.selenium.WebDriver;

import com.automation.ui.common.utilities.BaseGenericCache;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Rand;

/**
 * Sample Page Cache
 */
public class SamplePageCache extends BaseGenericCache<Languages> {
	private WebDriver driver;

	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public SamplePageCache(WebDriver driver)
	{
		setDriver(driver);
	}

	/**
	 * Constructor
	 * 
	 * @param pageObject - Page Object to get WebDriver from
	 */
	public SamplePageCache(Framework pageObject)
	{
		this(pageObject.getDriver());
	}

	/**
	 * Set Driver
	 * 
	 * @param driver
	 */
	public void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}

	/**
	 * Get Driver
	 * 
	 * @return WebDriver
	 */
	public WebDriver getDriver()
	{
		return driver;
	}

	/**
	 * Set the Cached Language using the simulateExtraction method
	 * 
	 * @param lang - Language
	 */
	public void setCachedLanguage()
	{
		update(Languages.English);
	}

	/**
	 * Set the Cached Language
	 * 
	 * @param lang - Language
	 */
	public void setCachedLanguage(Languages lang)
	{
		put(Languages.English, lang);
	}

	/**
	 * Get the Cached Language<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the cached language is null, then it is retrieved from the page, cached and returned<BR>
	 * 
	 * @return Languages
	 */
	public Languages getLanguage()
	{
		return (Languages) get(Languages.English);
	}

	@Override
	public void update(Languages key)
	{
		// Execute some method to get the user's language. Normally, the method would not be in this class.
		put(Languages.English, simulateExtraction(driver));
	}

	private Languages simulateExtraction(WebDriver driver)
	{
		// Simulate the operation taking some time to be able to measure the savings when cached
		Framework.sleep(Rand.randomRange(1000, 5000));
		return (Languages) Rand.randomEnum(Languages.English, 10000);
	}
}
